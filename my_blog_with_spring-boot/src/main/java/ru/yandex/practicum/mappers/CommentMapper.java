package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.CommentInputDto;
import ru.yandex.practicum.dto.CommentOutputDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.CommentJdbc;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    Comment commentInputDtoToComment(CommentInputDto commentInput);

    CommentOutputDto commentToCommentOutputDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    CommentJdbc commentInputDtoTocommentJdbc(CommentInputDto commentInput);

    CommentOutputDto commentJdbcToCommentOutputDto(CommentJdbc commentJdbc);
}
