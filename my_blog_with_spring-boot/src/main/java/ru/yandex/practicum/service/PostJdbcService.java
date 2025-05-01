package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostJdbcOutputDto;

import java.util.List;

public interface PostJdbcService {

    PostJdbcOutputDto addPost(PostInputDto postInput);

    PostJdbcOutputDto updatePost(PostInputDto postInput, long postId);

    List<PostJdbcOutputDto> getPostsWithParameters(String search, Long pageSize, Long pageNumber);

    PostJdbcOutputDto getPost(long postId);

    long getSizeOfImage(long postId);

    void likePost(long postId, boolean like);

    void deletePost(long postId);

    long countAllPosts();

    CommentOutputDto addComment(CommentInputDto commentInput, long postId);

    CommentOutputDto updateComment(CommentInputDto commentInput, long commentId, long postId);
}
