package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostJdbcOutputDto;
import ru.yandex.practicum.exceptions.IncorrectDataException;
import ru.yandex.practicum.mappers.AbstractPostMapper;
import ru.yandex.practicum.mappers.CommentMapper;
import ru.yandex.practicum.model.CommentJdbc;
import ru.yandex.practicum.model.PostJdbc;
import ru.yandex.practicum.repository.PostRepositoryWithJdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostJdbcServiceImpl implements PostJdbcService {

    private final PostRepositoryWithJdbc postRepository;
    private final AbstractPostMapper postMapper;
    private final CommentMapper commentMapper;
    private final Map<Long, MultipartFile> images = new HashMap<>();

    @Override
    public PostJdbcOutputDto addPost(PostInputDto postInput) {
        if (postInput != null) {
            PostJdbc post = postMapper.postInputDtoToPostJdbc(postInput);
            post.setLikesCount(0L);
            return postMapper.postJdbcToPostJdbcOutputDto(postRepository.addPost(post));
        }
        throw new IncorrectDataException("Внимание! Не передано значение поста!");
    }

    @Override
    public PostJdbcOutputDto updatePost(PostInputDto postInput, long postId) {
        if (postInput != null && getPost(postId) != null) {
            PostJdbc post = postRepository.getPost(postId);
            if (postInput.getTags() != null) {
                post.setTags(postInput.getTags());
            }
            if (postInput.getTitle() != null) {
                post.setTitle(postInput.getTitle());
            }
            if (postInput.getText() != null) {
                post.setText(postInput.getText());
            }
            if (postInput.getImage() != null) {
                images.remove(postId);
                images.put(postId, postInput.getImage());
            }
            if (postInput.getTextPreview() != null) {
                post.setTextPreview(postInput.getTextPreview());
            }
            if (postInput.getTagsAsText() != null) {
                post.setTagsAsText(postInput.getTagsAsText());
            }
            if (postInput.getTextParts() != null) {
                post.setTextParts(postInput.getTextParts());
            }
            return postMapper.postJdbcToPostJdbcOutputDto(postRepository.updatePost(post, postId));
        }
        throw new IncorrectDataException("Внимание! Не передано значение поста!");
    }

    @Override
    public List<PostJdbcOutputDto> getPostsWithParameters(String search, Long pageSize, Long pageNumber) {
        if (search != null) {
            List<PostJdbc> postsWithSearch = postRepository.getPostsWithParameters(search, pageSize, pageNumber);
            return postsWithSearch
                    .stream()
                    .map(postMapper::postJdbcToPostJdbcOutputDto)
                    .collect(Collectors.toList());
        }
        List<PostJdbc> postsWithoutSearch = postRepository.getPostsWithParameters(null, pageSize, pageNumber);
        return postsWithoutSearch
                .stream()
                .map(postMapper::postJdbcToPostJdbcOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostJdbcOutputDto getPost(long postId) {
        PostJdbc post = postRepository.getPost(postId);
        return postMapper.postJdbcToPostJdbcOutputDto(post);
    }

    @Override
    public long getSizeOfImage(long postId) {
        MultipartFile image = images.get(postId);
        return image.getSize();
    }

    @Override
    public void likePost(long postId, boolean like) {
        postRepository.likePost(postId, like);
    }

    @Override
    public void deletePost(long postId) {
        postRepository.deletePost(postId);
    }

    @Override
    public long countAllPosts() {
        return postRepository.countAllPosts();
    }

    @Override
    public CommentOutputDto addComment(CommentInputDto commentInput, long postId) {
        if (commentInput != null) {
            CommentJdbc comment = commentMapper.commentInputDtoTocommentJdbc(commentInput);
            return commentMapper.commentJdbcToCommentOutputDto(postRepository.addComment(comment, postId));
        }
        throw new IncorrectDataException("Внимание! Не передано значение комментария!");
    }

    @Override
    public CommentOutputDto updateComment(CommentInputDto commentInput, long commentId, long postId) {
        if (commentInput != null) {
            CommentJdbc comment = commentMapper.commentInputDtoTocommentJdbc(commentInput);
            return commentMapper.commentJdbcToCommentOutputDto(postRepository.updateComment(comment, commentId, postId));
        }
        throw new IncorrectDataException("Внимание! Не передано значение комментария!");
    }
}
