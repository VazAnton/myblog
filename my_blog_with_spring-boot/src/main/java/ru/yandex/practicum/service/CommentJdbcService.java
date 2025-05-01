package ru.yandex.practicum.service;

public interface CommentJdbcService {

    void deleteComment(long commentId, long postId);
}
