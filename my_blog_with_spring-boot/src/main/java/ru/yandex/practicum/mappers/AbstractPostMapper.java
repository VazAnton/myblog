package ru.yandex.practicum.mappers;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostJdbcOutputDto;
import ru.yandex.practicum.dto.PostOutputDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.CommentJdbc;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.PostJdbc;

import java.util.*;

@Mapper(componentModel = "spring")
public abstract class AbstractPostMapper {

    public Post postInputDtoToPost(PostInputDto postInputDto) {
        if (postInputDto == null) {
            return null;
        }
        Post post = new Post();
        post.setTitle(postInputDto.getTitle());
        post.setText(postInputDto.getText());
        post.setTags(postInputDto.getTags());
        post.setTagsAsText(postInputDto.getTagsAsText());
        post.setTextPreview(postInputDto.getTextPreview());
        post.setTextParts(postInputDto.getTextParts());
        if (postInputDto.getImage() != null) {
            post.setImagePath("/posts" + postInputDto.getImage().getOriginalFilename());
        } else {
            post.setImagePath("/posts");
        }
        return post;
    }

    public PostOutputDto postToPostOutputDto(Post post) {
        if (post == null) {
            return null;
        }
        PostOutputDto postOutputDto = new PostOutputDto();
        postOutputDto.setId(post.getId());
        postOutputDto.setTitle(post.getTitle());
        postOutputDto.setText(post.getText());
        postOutputDto.setImagePath(post.getImagePath());
        postOutputDto.setLikesCount(post.getLikesCount());
        Set<Comment> set = post.getComments();
        postOutputDto.setComments(Objects.requireNonNullElseGet(set, HashSet::new));
        postOutputDto.setTagsAsText(post.getTagsAsText());
        postOutputDto.setTextPreview(post.getTextPreview());
        postOutputDto.setTextParts(post.getTextParts());
        return postOutputDto;
    }

    public PostJdbc postInputDtoToPostJdbc(PostInputDto postInputDto) {
        if (postInputDto == null) {
            return null;
        }
        PostJdbc post = new PostJdbc();
        post.setTitle(postInputDto.getTitle());
        post.setText(postInputDto.getText());
        post.setTags(postInputDto.getTags());
        post.setTagsAsText(postInputDto.getTagsAsText());
        post.setTextPreview(postInputDto.getTextPreview());
        post.setTextParts(postInputDto.getTextParts());
        if (postInputDto.getImage() != null) {
            post.setImagePath("/posts" + postInputDto.getImage().getOriginalFilename());
        } else {
            post.setImagePath("/posts");
        }
        return post;
    }

    public PostJdbcOutputDto postJdbcToPostJdbcOutputDto(PostJdbc post) {
        if (post == null) {
            return null;
        }
        PostJdbcOutputDto postOutputDto = new PostJdbcOutputDto();
        postOutputDto.setId(post.getId());
        postOutputDto.setTitle(post.getTitle());
        postOutputDto.setText(post.getText());
        postOutputDto.setImagePath(post.getImagePath());
        postOutputDto.setLikesCount(post.getLikesCount());
        List<CommentJdbc> list = post.getComments();
        postOutputDto.setComments(Objects.requireNonNullElseGet(list, ArrayList::new));
        postOutputDto.setTagsAsText(post.getTagsAsText());
        postOutputDto.setTextPreview(post.getTextPreview());
        postOutputDto.setTextParts(post.getTextParts());
        return postOutputDto;
    }

    public abstract PostJdbc postJdbcOutputDtoToPostJdbc(PostJdbcOutputDto postOutputDto);
}
