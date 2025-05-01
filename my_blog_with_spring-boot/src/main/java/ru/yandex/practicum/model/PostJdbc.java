package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostJdbc {

    private Long id;
    private String title;
    private String text;
    private Long likesCount;
    private List<CommentJdbc> comments;
    private String tags;
    private String imagePath;
    private String tagsAsText;
    private String textPreview;
    private String textParts;
}
