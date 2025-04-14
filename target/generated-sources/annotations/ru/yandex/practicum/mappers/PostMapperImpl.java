package ru.yandex.practicum.mappers;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.PostInputDto;
import ru.yandex.practicum.dto.PostOutputDto;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-14T21:51:16+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post postInputDtoToPost(PostInputDto postInputDto) {
        if ( postInputDto == null ) {
            return null;
        }

        Post post = new Post();

        post.setTitle( postInputDto.getTitle() );
        post.setText( postInputDto.getText() );
        post.setTags( postInputDto.getTags() );
        post.setTagsAsText( postInputDto.getTagsAsText() );
        post.setTextPreview( postInputDto.getTextPreview() );
        post.setTextParts( postInputDto.getTextParts() );

        post.setImagePath( "/posts" + postInputDto.getImage().getOriginalFilename() );

        return post;
    }

    @Override
    public PostOutputDto postToPostOutputDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostOutputDto postOutputDto = new PostOutputDto();

        postOutputDto.setId( post.getId() );
        postOutputDto.setTitle( post.getTitle() );
        postOutputDto.setText( post.getText() );
        postOutputDto.setImagePath( post.getImagePath() );
        postOutputDto.setLikesCount( post.getLikesCount() );
        Set<Comment> set = post.getComments();
        if ( set != null ) {
            postOutputDto.setComments( new LinkedHashSet<Comment>( set ) );
        }
        postOutputDto.setTagsAsText( post.getTagsAsText() );
        postOutputDto.setTextPreview( post.getTextPreview() );
        postOutputDto.setTags( post.getTags() );
        postOutputDto.setTextParts( post.getTextParts() );

        return postOutputDto;
    }
}
