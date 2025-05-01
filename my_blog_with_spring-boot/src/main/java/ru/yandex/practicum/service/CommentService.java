package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;

public interface CommentService {

    CommentOutputDto addComment(CommentInputDto commentInput, long postId);

    CommentOutputDto updateComment(CommentInputDto commentInput, long commentId, long postId);

    void deleteComment(long commentId, long postId);
}
