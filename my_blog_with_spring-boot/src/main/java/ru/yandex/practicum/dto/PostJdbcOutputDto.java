package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.CommentJdbc;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostJdbcOutputDto {

    private Long id;
    private String title;
    private String text;
    private String imagePath;
    private Long likesCount;
    private List<CommentJdbc> comments;
    private String tagsAsText;
    private String textPreview;
    private String tags;
    private String textParts;
}
