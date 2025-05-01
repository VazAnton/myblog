package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.model.CommentJdbc;
import ru.yandex.practicum.model.PostJdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepositoryWithJdbc {

    private final JdbcTemplate jdbcTemplate;

    private List<CommentJdbc> createComments(SqlRowSet commentsRow) {
        List<CommentJdbc> comments = new ArrayList<>();
        while (commentsRow.next()) {
            String commentIds = commentsRow.getString("comment_id");
            String commentTexts = commentsRow.getString("text_of_comment");
            if (commentIds != null) {
                String[] ids = commentIds.split("'");
                String[] texts = commentTexts.split("'");
                for (int i = 0; i < ids.length; i++) {
                    comments.add(CommentJdbc.builder()
                            .id(Long.parseLong(ids[i]))
                            .text(texts[i])
                            .build());
                }
            }
        }
        return comments;
    }

    private PostJdbc createPost(ResultSet rs, int rowNum) throws SQLException {
        String forComments = "SELECT* " +
                "FROM comments_table " +
                "WHERE comment_id IN( " +
                "SELECT p.comment_id " +
                "FROM posts AS p " +
                "WHERE post_id = ?) " +
                "ORDER BY comment_id DESC;";
        SqlRowSet commentsRow = jdbcTemplate.queryForRowSet(forComments, rs.getLong("post_id"));
        return PostJdbc.builder()
                .id(rs.getLong("post_id"))
                .tags(rs.getString("tags"))
                .title(rs.getString("title"))
                .tagsAsText(rs.getString("tags_as_text"))
                .imagePath(rs.getString("image_path"))
                .likesCount(rs.getLong("likes_count"))
                .textPreview(rs.getString("text_preview"))
                .textParts(rs.getString("text_parts"))
                .comments(createComments(commentsRow))
                .build();
    }

    public PostJdbc addPost(PostJdbc post) {
        post.setLikesCount(0L);
        SimpleJdbcInsert simplePostInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("posts")
                .usingGeneratedKeyColumns("post_id");
        Long id = simplePostInsert.executeAndReturnKey(
                Map.of(
                        "title", post.getTitle(),
                        "text_of_comment", post.getText(),
                        "likes_count", post.getLikesCount(),
                        "tags", post.getTags(),
                        "image_path", post.getImagePath(),
                        "tags_as_text", post.getTagsAsText(),
                        "text_preview", post.getTextPreview(),
                        "text_parts", post.getTextParts()
                )).longValue();
        post.setId(id);
        setComments(post);
        log.info("Пост успешно сохранен");
        return post;
    }

    private void removeComment(long postId) {
        jdbcTemplate.update("DELETE FROM comments_table " +
                "WHERE comment_id IN( " +
                "SELECT p.comment_id " +
                "FROM posts AS p " +
                "WHERE post_id = ?);", postId);
    }

    private List<CommentJdbc> setComments(PostJdbc post) {
        removeComment(post.getId());
        if (post.getComments() == null) {
            return new ArrayList<>();
        }
        List<CommentJdbc> comments = new ArrayList<>(post.getComments());
        comments.sort(Comparator.comparingLong(CommentJdbc::getId));
        jdbcTemplate.batchUpdate("MERGE INTO posts (post_id, comment_id) VALUES (?, ?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, post.getId());
                        ps.setLong(2, comments.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return comments.size();
                    }
                });
        return comments;
    }

    public PostJdbc updatePost(PostJdbc post, long postId) {
        jdbcTemplate.update("UPDATE posts set title = ?, text_of_comment = ?, image_path = ?, likes_count = ?, " +
                        "tags = ?, tags_as_text = ?, text_preview = ?, text_parts = ? " +
                        "WHERE post_id = ?;", post.getTitle(), post.getText(), post.getImagePath(), post.getLikesCount(),
                post.getTags(), post.getTagsAsText(), post.getTextPreview(), post.getTextParts(), postId);
        setComments(post);
        return post;
    }

    public List<PostJdbc> getPostsWithParameters(String search, Long pageSize, Long pageNumber) {
        if (search != null) {
            String searchPattern = "%" + search + "%";
            return jdbcTemplate.query("SELECT * " +
                    "FROM posts " +
                    "WHERE tags LIKE ? " +
                    "LIMIT ? " +
                    "OFFSET ?;", new Object[]{searchPattern, pageSize.intValue(), pageNumber.intValue()}, this::createPost);
        }
        return jdbcTemplate.query("SELECT * " +
                "FROM posts " +
                "LIMIT ? " +
                "OFFSET ?;", new Object[]{pageSize.intValue(), pageNumber.intValue()}, this::createPost);
    }

    public PostJdbc getPost(long postId) {
        return jdbcTemplate.queryForObject("SELECT* " +
                "FROM posts " +
                "WHERE post_id = ?;", this::createPost, postId);
    }

    public void likePost(long postId, boolean like) {
        if (getPost(postId) != null) {
            PostJdbc post = getPost(postId);
            Long likes = post.getLikesCount();
            if (like) {
                likes = likes + 1;
            } else {
                likes = likes - 1;
            }
            jdbcTemplate.update("UPDATE posts set likes_count = ? WHERE post_id = ?;", likes, postId);
            log.info("Информация о лайках изменена");
        } else {
            throw new EntityNotFoundException("Внимание! Поста с таким уникальным номером " +
                    "не существует!");
        }
    }

    public void deletePost(long postId) {
        if (getPost(postId) != null) {
            jdbcTemplate.update("DELETE FROM posts " +
                    "WHERE post_id = ?", postId);
            log.info("Информация о посте успешно удалена!");
        } else {
            throw new EntityNotFoundException("Внимание! Поста с таким уникальным номером " +
                    "не существует!");
        }
    }

    public long countAllPosts() {
        return jdbcTemplate.getFetchSize();
    }

    public CommentJdbc addComment(CommentJdbc comment, long postId) {
        SimpleJdbcInsert simpleCommentInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comments_table")
                .usingGeneratedKeyColumns("comment_id");
        Long id = simpleCommentInsert
                .executeAndReturnKey(
                        Map.of(
                                "text_of_comment", comment.getText()
                        )).longValue();
        comment.setId(id);
        log.info("Информация о комментарии обновлена");
        return comment;
    }

    public CommentJdbc updateComment(CommentJdbc comment, long commentId, long postId) {
        jdbcTemplate.update("UPDATE comments_table set text_of_comment = ? " +
                "WHERE comment_id = ?;", comment.getText(), commentId);
        log.info("Информация о комментарии обновлена");
        return comment;
    }
}
