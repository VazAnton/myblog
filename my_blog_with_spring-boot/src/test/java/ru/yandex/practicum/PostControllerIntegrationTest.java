package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    public MockMvc mockMvc;
    Post post;
    Comment comment;

    @BeforeEach
    public void setUp() {
        comment = new Comment();
        comment.setId(1L);
        comment.setText("commentsText");
        post = new Post();
        post.setId(1L);
        post.setTitle("title");
        post.setText("text");
        post.setTags("tags");
        post.setLikesCount(0L);
        post.setComments(new HashSet<>());
        post.setImagePath("myblog/src/test/mockFile");
        post.setTextParts("text_parts");
        post.setTagsAsText("tags_as_text");
        post.setTextPreview("text_preview");
    }

    @Test
    @Order(1)
    public void checkAddPost() throws Exception {
        mockMvc.perform(get("/posts/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"title\": \"title\",\n" +
                                "    \"text\": \"text\",\n" +
                                "    \"image\": null,\n" +
                                "    \"tags\": \"tags\",\n" +
                                "    \"tagsAsText\": \"tagsAsText\",\n" +
                                "    \"textPreview\": \"textPreview\",\n" +
                                "    \"textParts\": \"textPart1, textPart2\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"));
    }

    @Test
    @Order(2)
    public void checkUpdatePost() throws Exception {
        mockMvc.perform(get("/posts/1/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"title\": \"title\",\n" +
                                "    \"text\": \"update_text\",\n" +
                                "    \"image\": null,\n" +
                                "    \"tags\": \"tags\",\n" +
                                "    \"tagsAsText\": \"tagsAsText\",\n" +
                                "    \"textPreview\": \"textPreview\",\n" +
                                "    \"textParts\": \"textPart1, textPart2\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"));
    }

    @Test
    @Order(3)
    public void checkGetPost() throws Exception {
        commentRepository.save(comment);
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isFound())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"));
    }

    @Test
    @Order(4)
    public void checkGetPosts() throws Exception {
        commentRepository.save(comment);
        mockMvc.perform(get("/posts?search=\"tags\""))
                .andExpect(status().isFound())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"));
    }

    @Test
    @Order(5)
    public void checkLikePost() throws Exception {
        mockMvc.perform(post("/posts/1/like?like=true"))
                .andExpect(status().isFound())
                .andReturn();
    }

    @Test
    @Order(6)
    public void checkAddComment() throws Exception {
        commentRepository.delete(comment);
        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"text\": \"commentsText\"\n" +
                                "}"))
                .andExpect(status().isCreated())
                .andExpect(view().name("redirect:/posts/{postId}"));
    }

    @Test
    @Order(7)
    public void checkUpdateComment() throws Exception {
        commentRepository.save(comment);
        mockMvc.perform(post("/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"text\": \"updatedCommentsText\"\n" +
                                "}"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/posts/{postId}"));
    }

    @Test
    @Order(8)
    public void checkDeleteComment() throws Exception {
        commentRepository.save(comment);
        mockMvc.perform(post("/posts/1/comments/1/delete"))
                .andExpect(status().isNoContent())
                .andExpect(view().name("redirect:/posts/{postId}"))
                .andReturn();
    }
}
