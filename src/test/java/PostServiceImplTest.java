import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostOutputDto;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.exceptions.IncorrectDataException;
import ru.yandex.practicum.mappers.AbstractPostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.PostServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class PostServiceImplTest {

    @InjectMocks
    PostServiceImpl postService;
    @Mock
    AbstractPostMapper postMapper;
    @Mock
    PostRepository postRepository;
    PostInputDto postInputDto;
    PostOutputDto postOutputDto;
    Post post;

    @BeforeEach
    public void setUp() {
        postInputDto = new PostInputDto();
        postInputDto.setTitle("title");
        postInputDto.setText("text");
        postInputDto.setTags("tags");
        postInputDto.setImage(Mockito.mock(MultipartFile.class));
        postInputDto.setTextParts("textParts");
        postInputDto.setTextPreview("textPreview");
        postInputDto.setTagsAsText("tagsAsText");

        postOutputDto = new PostOutputDto();
        postOutputDto.setId(1L);
        postOutputDto.setComments(new HashSet<>());
        postOutputDto.setLikesCount(0L);
        postOutputDto.setText(postInputDto.getText());
        postOutputDto.setTitle(postInputDto.getTitle());
        postOutputDto.setImagePath("myblog/src/test/mockFile");
        postOutputDto.setTextParts(postInputDto.getTextParts());
        postOutputDto.setTags(postInputDto.getTags());
        postOutputDto.setTextPreview(postInputDto.getTextPreview());
        postOutputDto.setTagsAsText(postInputDto.getTagsAsText());

        post = new Post();
        post.setId(1L);
        post.setTitle(postInputDto.getTitle());
        post.setText(postInputDto.getText());
        post.setTags(postInputDto.getTags());
        post.setLikesCount(0L);
        post.setComments(new HashSet<>());
        post.setImagePath("myblog/src/test/mockFile");
        post.setTextPreview(postInputDto.getTextPreview());
        post.setTagsAsText(postInputDto.getTagsAsText());
        post.setTextParts(postInputDto.getTextParts());
    }

    @Test
    public void addPostShouldThrowExceptionIfPostIsNull() {
        assertThrows(IncorrectDataException.class,
                () -> postService.addPost(null));
    }

    @DisplayName(value = "Проверка добавления поста")
    @Test
    public void checkAddPost() {
        when(postMapper.postInputDtoToPost(postInputDto))
                .thenReturn(post);
        Post testPost = postMapper.postInputDtoToPost(postInputDto);
        when(postRepository.save(testPost))
                .thenReturn(post);
        postRepository.save(testPost);
        when(postMapper.postToPostOutputDto(testPost))
                .thenReturn(postOutputDto);
        PostOutputDto postOutput = postMapper.postToPostOutputDto(testPost);
        PostOutputDto actual = postService.addPost(postInputDto);
        assertNotNull(actual);
        assertEquals(postOutput, actual);
    }

    @DisplayName(value = "Проверка обновления поста")
    @Test
    public void checkUpdatePost() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        when(postRepository.save(post))
                .thenReturn(post);
        when(postMapper.postToPostOutputDto(post))
                .thenReturn(postOutputDto);
        PostOutputDto actual = postService.updatePost(postInputDto, 1L);
        assertNotNull(actual);
        assertEquals(postInputDto.getTitle(), actual.getTitle());
        assertEquals(postInputDto.getText(), actual.getText());
    }

    @Test
    public void checkGetPostWithoutTags() {
        List<PostOutputDto> postsDtos = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        PostOutputDto postOutputDto1 = new PostOutputDto();
        postOutputDto1.setId(2L);
        postOutputDto1.setComments(new HashSet<>());
        postOutputDto1.setLikesCount(0L);
        postOutputDto1.setText("text1");
        postOutputDto1.setTitle("title1");
        postOutputDto1.setImagePath("myblog/src/test/mockFile1");
        postsDtos.add(postOutputDto);
        postsDtos.add(postOutputDto1);
        Post post1 = new Post();
        post1.setId(2L);
        post1.setTitle("title1");
        post1.setText("text1");
        post1.setTags("tags1");
        post1.setLikesCount(0L);
        post1.setComments(new HashSet<>());
        post1.setImagePath("myblog/src/test/mockFile");
        posts.add(post);
        posts.add(post1);
        when(postRepository.findAllWithLimitAndOffset(10, 1))
                .thenReturn(posts);
        when(postMapper.postToPostOutputDto(post))
                .thenReturn(postOutputDto);
        when(postMapper.postToPostOutputDto(post1))
                .thenReturn(postOutputDto1);
        List<PostOutputDto> actual = postService.getPostsWithParameters(null, 1L, 10L);
        assertNotNull(actual);
        assertEquals(postsDtos, actual);
    }

    @Test
    public void checkGetPost() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        when(postMapper.postToPostOutputDto(post))
                .thenReturn(postOutputDto);
        PostOutputDto testOutput = postMapper.postToPostOutputDto(post);
        PostOutputDto actual = postService.getPost(1L);
        assertNotNull(actual);
        assertEquals(testOutput, actual);
    }

    @Test
    public void checkLikePost() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        Post likedPost = new Post();
        likedPost.setId(1L);
        likedPost.setTitle(post.getTitle());
        likedPost.setText(post.getText());
        likedPost.setTags(post.getTags());
        likedPost.setLikesCount(1L);
        likedPost.setComments(new HashSet<>());
        likedPost.setImagePath("myblog/src/test/mockFile");
        when(postRepository.save(post))
                .thenReturn(likedPost);
        postService.likePost(1L, true);
        assertEquals(1, likedPost.getLikesCount());
    }

    @DisplayName(value = "Ошибка при попытке удаления несуществующего поста")
    @Test
    public void deletePostShouldThrowExceptionIfPostNotExists() {
        assertThrows(EntityNotFoundException.class,
                () -> postService.deletePost(1L));
    }
}
