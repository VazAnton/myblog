package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInputDto {

    private String title;
    private String text;
    private MultipartFile image;
    private String tags;
    private String tagsAsText;
    private String textPreview;
    private String textParts;
}
