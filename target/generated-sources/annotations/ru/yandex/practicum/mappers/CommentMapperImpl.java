package ru.yandex.practicum.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;
import ru.yandex.practicum.model.Comment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T21:51:16+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment commentInputDtoToComment(CommentInputDto commentInput) {
        if ( commentInput == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setText( commentInput.getText() );

        return comment;
    }

    @Override
    public CommentOutputDto commentToCommentOutputDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentOutputDto commentOutputDto = new CommentOutputDto();

        commentOutputDto.setText( comment.getText() );

        return commentOutputDto;
    }
}
