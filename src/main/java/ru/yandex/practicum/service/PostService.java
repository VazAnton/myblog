package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostOutputDto;

import java.util.List;

public interface PostService {

    PostOutputDto addPost(PostInputDto postInput);

    PostOutputDto updatePost(PostInputDto postInput, long postId);

    List<PostOutputDto> getPostsWithParameters(String search, Long pageSize, Long pageNumber);

    PostOutputDto getPost(long postId);

    long getSizeOfImage(long postId);

    void likePost(long postId, boolean like);

    void deletePost(long postId);

    long countAllPosts();
}
