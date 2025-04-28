package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    Post post;
    Comment comment;

    @BeforeEach
    public void setUp() {
        commentRepository.deleteAll();
        postRepository.deleteAll();

        post = new Post();
        post.setTitle("title");
        post.setText("text");
        post.setTags("tags");
        post.setLikesCount(0L);
        post.setComments(new HashSet<>());
        post.setImagePath("myblog/src/test/mockFile");
        post.setTextPreview("textPreview");
        post.setTagsAsText("tagsAsText");
        post.setTextParts("textParts");

        comment = new Comment();
        comment.setText("commentsText");
    }

    @Test
    public void checkSaveComment() {
        postRepository.save(post);
        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment);
        assertEquals(savedComment.getText(), comment.getText());
    }

    @Test
    public void checkFoundComment() {
        postRepository.save(post);
        commentRepository.save(comment);

        Optional<Comment> foundComment = commentRepository.findById(1L);

        assertTrue(foundComment.isPresent());
        assertEquals(foundComment.get().getId(), 1L);
        assertEquals(foundComment.get().getText(), comment.getText());
    }

    @Test
    public void checkDeleteComment() {
        postRepository.save(post);
        commentRepository.save(comment);
        commentRepository.deleteById(1L);

        Optional<Comment> deletedComment = commentRepository.findById(1L);
        assertTrue(deletedComment.isEmpty());
    }
}
