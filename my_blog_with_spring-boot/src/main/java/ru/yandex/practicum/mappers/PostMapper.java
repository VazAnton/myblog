package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostOutputDto;
import ru.yandex.practicum.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "imagePath",
            expression = "java(\"/posts\" + postInputDto.getImage().getOriginalFilename())")
    Post postInputDtoToPost(PostInputDto postInputDto);

    PostOutputDto postToPostOutputDto(Post post);
}
