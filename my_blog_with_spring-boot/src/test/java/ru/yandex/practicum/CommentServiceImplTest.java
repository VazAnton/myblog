package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;
import ru.yandex.practicum.exceptions.EntityNotFoundException;
import ru.yandex.practicum.exceptions.IncorrectDataException;
import ru.yandex.practicum.mappers.CommentMapper;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.service.CommentServiceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @InjectMocks
    CommentServiceImpl commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    CommentMapper commentMapper;
    @Mock
    PostRepository postRepository;
    Comment comment;
    Post post;
    CommentOutputDto commentOutputDto;
    CommentInputDto commentInputDto;
    Set<Comment> comments;

    @BeforeEach
    public void setUp() {
        comments = new HashSet<>();
        commentInputDto = new CommentInputDto("text");
        comment = new Comment();
        comment.setId(1L);
        comment.setText(commentInputDto.getText());
        comments.add(comment);
        commentOutputDto = new CommentOutputDto(comment.getText());
        post = new Post();
        post.setId(1L);
        post.setTitle("title");
        post.setText("text");
        post.setTags("tags");
        post.setLikesCount(0L);
        post.setComments(comments);
        post.setImagePath("myblog/src/test/mockFile");
    }

    @Test
    public void addCommentShouldThrowExceptionIfCommentIsNull() {
        assertThrows(IncorrectDataException.class,
                () -> commentService.addComment(null, post.getId()));
    }

    @Test
    public void checkAddComment() {
        when(commentMapper.commentInputDtoToComment(commentInputDto))
                .thenReturn(comment);
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        when(postRepository.save(post))
                .thenReturn(post);
        when(commentRepository.save(comment))
                .thenReturn(comment);
        when(commentMapper.commentToCommentOutputDto(comment))
                .thenReturn(commentOutputDto);
        CommentOutputDto actual = commentService.addComment(commentInputDto, 1L);
        assertNotNull(actual);
        assertEquals(commentInputDto.getText(), actual.getText());
    }

    @Test
    public void checkUpdateComment() {
        Comment updatedComment = new Comment();
        updatedComment.setId(comment.getId());
        updatedComment.setText("updated");
        when(commentRepository.findById(comment.getId()))
                .thenReturn(Optional.of(updatedComment));
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));
        comments.clear();
        comments.add(updatedComment);
        post.setComments(comments);
        when(postRepository.save(post))
                .thenReturn(post);
        when(commentRepository.save(updatedComment))
                .thenReturn(updatedComment);
        CommentOutputDto updatedCommentDto = new CommentOutputDto(updatedComment.getText());
        when(commentMapper.commentToCommentOutputDto(updatedComment))
                .thenReturn(updatedCommentDto);
        CommentOutputDto actual = commentService.updateComment(commentInputDto, comment.getId(), post.getId());
        assertNotNull(actual);
        assertEquals("updated", updatedCommentDto.getText());
    }

    @Test
    public void updateCommentShouldThrowExceptionIfCommentIsNull() {
        assertThrows(IncorrectDataException.class,
                () -> commentService.updateComment(null, comment.getId(), post.getId()));
    }

    @Test
    public void deleteCommentShouldThrowExceptionIfCommentNotExists() {
        assertThrows(EntityNotFoundException.class,
                () -> commentService.deleteComment(2L, post.getId()));
    }
}
