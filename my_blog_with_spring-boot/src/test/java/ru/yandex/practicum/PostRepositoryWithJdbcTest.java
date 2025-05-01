package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.model.PostJdbc;
import ru.yandex.practicum.repository.PostRepositoryWithJdbc;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
public class PostRepositoryWithJdbcTest {

    final JdbcTemplate jdbcTemplate;
    PostRepositoryWithJdbc postRepository;
    PostJdbc testedPost;

    @Autowired
    public PostRepositoryWithJdbcTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void setUp() {
        postRepository = new PostRepositoryWithJdbc(jdbcTemplate);

        testedPost = PostJdbc.builder()
                .title("title")
                .text("text")
                .imagePath("myblog/src/test/mockFile")
                .likesCount(0L)
                .tags("tags")
                .comments(new ArrayList<>())
                .tagsAsText("tagsAsText")
                .textPreview("textPreview")
                .textParts("textParts")
                .build();
    }

    @Test
    public void checkAddPost() {
        PostJdbc savedPost = postRepository.addPost(testedPost);

        assertThat(savedPost)
                .isNotNull();
        assertEquals(savedPost.getTitle(), testedPost.getTitle());
        assertEquals(savedPost.getComments().size(), testedPost.getComments().size());
    }

    @Test
    public void checkUpdatePost() {
        PostJdbc savedPost = postRepository.addPost(testedPost);
        savedPost.setTitle("updatedTitle");
        PostJdbc updatedPost = postRepository.updatePost(savedPost, savedPost.getId());

        assertNotNull(updatedPost);
        assertFalse(postRepository.getPostsWithParameters(null, 10L, 0L).isEmpty());
        assertEquals(savedPost.getTitle(), updatedPost.getTitle());
    }

    @Test
    public void checkGetPost() {
        PostJdbc savedPost = postRepository.addPost(testedPost);

        assertNotNull(postRepository.getPost(savedPost.getId()));
    }

    @Test
    public void checkLikePost() {
        PostJdbc savedPost = postRepository.addPost(testedPost);

        postRepository.likePost(savedPost.getId(), true);

        assertEquals(1, postRepository.getPost(savedPost.getId()).getLikesCount());
    }

    @Test
    public void checkDeletePost() {
        PostJdbc savedPost = postRepository.addPost(testedPost);
        assertNotNull(postRepository.getPost(savedPost.getId()));

        postRepository.deletePost(savedPost.getId());

        assertTrue(postRepository.getPostsWithParameters(null, 10L, 0L).isEmpty());
    }
}
