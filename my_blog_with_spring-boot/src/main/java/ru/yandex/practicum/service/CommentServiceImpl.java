package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.exceptions.IncorrectDataException;
import ru.yandex.practicum.mappers.CommentMapper;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    private Comment getCommentFromDb(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Внимание! Комментария " +
                        "с таким уникальным номером не существует!"));
    }

    private Post getPostFromDb(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Внимание! Поста с таким уникальным номером " +
                        "не существует!"));
    }

    @Override
    public CommentOutputDto addComment(CommentInputDto commentInput, long postId) {
        if (commentInput != null) {
            Comment comment = commentMapper.commentInputDtoToComment(commentInput);
            commentRepository.save(comment);
            log.info("Информация о комментарии обновлена");
            Post postFromDb = getPostFromDb(postId);
            Set<Comment> comments = postFromDb.getComments();
            comments.add(comment);
            postFromDb.setComments(comments);
            postRepository.save(postFromDb);
            log.info("Информация о посте обновлена");
            return commentMapper.commentToCommentOutputDto(comment);
        }
        throw new IncorrectDataException("Внимание! Не передано значение комментария!");
    }

    @Override
    public CommentOutputDto updateComment(CommentInputDto commentInput, long commentId, long postId) {
        if (commentInput != null) {
            Comment commentFromDb = getCommentFromDb(commentId);
            if (commentInput.getText() != null) {
                commentFromDb.setText(commentInput.getText());
            }
            commentRepository.save(commentFromDb);
            log.info("Информация о комментарии обновлена");
            Post postFromDb = getPostFromDb(postId);
            Set<Comment> comments = postFromDb.getComments();
            comments.add(commentFromDb);
            postFromDb.setComments(comments);
            postRepository.save(postFromDb);
            log.info("Информация о посте обновлена");
            return commentMapper.commentToCommentOutputDto(commentFromDb);
        }
        throw new IncorrectDataException("Внимание! Не передано значение комментария!");
    }

    @Override
    @Transactional
    public void deleteComment(long commentId, long postId) {
        Comment comment = getCommentFromDb(commentId);
        commentRepository.delete(comment);
        log.info("Информация о комментарии обновлена");
    }
}
