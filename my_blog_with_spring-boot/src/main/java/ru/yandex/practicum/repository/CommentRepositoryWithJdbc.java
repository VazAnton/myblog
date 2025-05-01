package ru.yandex.practicum.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.service.CommentJdbcService;

import java.sql.Types;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepositoryWithJdbc implements CommentJdbcService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteComment(long commentId, long postId) {
        jdbcTemplate.update("DELETE FROM comments_table " +
                "WHERE comment_id = ?;", ps ->
                ps.setObject(1, commentId, Types.BIGINT));
    }
}
