package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;

public interface CommentService {

    CommentOutputDto addComment(CommentInputDto commentInput, long postId); // к) POST "/posts/{id}/comments" - эндпоинт добавления комментария к посту

    CommentOutputDto updateComment(CommentInputDto commentInput, long commentId, long postId); // л) POST "/posts/{id}/comments/{commentId}" - эндпоинт редактирования комментария

    void deleteComment(long commentId, long postId); // м) POST "/posts/{id}/comments/{commentId}/delete" - эндпоинт удаления комментария
}
