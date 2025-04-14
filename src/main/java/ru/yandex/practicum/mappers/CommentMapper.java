package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;
import ru.yandex.practicum.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment commentInputDtoToComment(CommentInputDto commentInput);

    CommentOutputDto commentToCommentOutputDto(Comment comment);
}
