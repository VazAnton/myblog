package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    Post post;

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    public void checkSavePost() {
        Post savedPost = postRepository.save(post);

        assertNotNull(savedPost);
        assertEquals(savedPost.getText(), post.getText());
    }

    @Test
    public void checkDeletePost() {
        postRepository.save(post);
        postRepository.deleteById(1L);

        Optional<Post> deletedPost = postRepository.findById(1L);

        assertThat(deletedPost).isEmpty();
    }
}
