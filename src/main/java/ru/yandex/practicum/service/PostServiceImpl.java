package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostOutputDto;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.exceptions.IncorrectDataException;
import ru.yandex.practicum.mappers.AbstractPostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AbstractPostMapper postMapper;
    private final Map<Long, MultipartFile> images = new HashMap<>();

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           AbstractPostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    private Post getPostFromDb(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Внимание! Поста с таким уникальным номером " +
                        "не существует!"));
    }

    @Override
    public PostOutputDto addPost(PostInputDto postInput) {
        if (postInput != null) {
            Post post = postMapper.postInputDtoToPost(postInput);
            post.setComments(new HashSet<>());
            post.setLikesCount(0L);
            post.setTextParts(postInput.getText());
            postRepository.save(post);
            log.info("Пост успешно сохранен");
            images.put(post.getId(), postInput.getImage());
            return postMapper.postToPostOutputDto(post);
        }
        throw new IncorrectDataException("Внимание! Не передано значение поста!");
    }

    @Override
    public PostOutputDto updatePost(PostInputDto postInput,
                                    long postId) {
        Post postFromDb = getPostFromDb(postId);
        if (postInput != null) {
            if (postInput.getTags() != null) {
                postFromDb.setTags(postInput.getTags());
            }
            if (postInput.getTitle() != null) {
                postFromDb.setTitle(postInput.getTitle());
            }
            if (postInput.getText() != null) {
                postFromDb.setText(postInput.getText());
            }
            if (postInput.getImage() != null) {
                images.remove(postId);
                images.put(postId, postInput.getImage());
            }
            if (postInput.getTextPreview() != null) {
                postFromDb.setTextPreview(postInput.getTextPreview());
            }
            if (postInput.getTagsAsText() != null) {
                postFromDb.setTagsAsText(postInput.getTagsAsText());
            }
            if (postInput.getTextParts() != null) {
                postFromDb.setTextParts(postInput.getTextParts());
            }
            postRepository.save(postFromDb);
            log.info("Пост успешно изменен.");
        }
        return postMapper.postToPostOutputDto(postFromDb);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostOutputDto> getPostsWithParameters(String search,
                                                      Long pageNumber,
                                                      Long pageSize) {
        if (search != null) {
            return postRepository.findAllWithParameters(search, pageSize, pageNumber)
                    .stream()
                    .map(postMapper::postToPostOutputDto)
                    .collect(Collectors.toList());
        }
        return postRepository.findAllWithLimitAndOffset(pageSize, pageNumber)
                .stream()
                .map(postMapper::postToPostOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostOutputDto getPost(long postId) {
        return postMapper.postToPostOutputDto(getPostFromDb(postId));
    }

    @Override
    @Transactional(readOnly = true)
    public long getSizeOfImage(long postId) {
        MultipartFile image = images.get(postId);
        return image.getSize();
    }

    @Override
    public void likePost(long postId,
                         boolean like) {
        Post postFromDb = getPostFromDb(postId);
        Long likes = postFromDb.getLikesCount();
        if (like) {
            likes = likes + 1;
        } else {
            likes = likes - 1;
        }
        postFromDb.setLikesCount(likes);
        postRepository.save(postFromDb);
        log.info("Информация о лайках изменена");
    }

    @Override
    public void deletePost(long postId) {
        postRepository.delete(getPostFromDb(postId));
        log.info("Пост успешно удален.");
    }

    @Override
    public long countAllPosts() {
        return postRepository.findAll().size();
    }
}
