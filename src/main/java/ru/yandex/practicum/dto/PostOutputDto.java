package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.Comment;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostOutputDto {

    private Long id;
    private String title;
    private String text;
    private String imagePath;
    private Long likesCount;
    private Set<Comment> comments;
    private String tagsAsText;
    private String textPreview;
    private String tags;
    private String textParts;
}
