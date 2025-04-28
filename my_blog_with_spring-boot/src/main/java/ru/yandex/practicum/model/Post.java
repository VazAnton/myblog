package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String title;
    @Column(name = "text_of_comment")
    private String text;
    @Column(name = "likes_count")
    private Long likesCount;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private Set<Comment> comments = new HashSet<>();
    private String tags;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "tags_as_text")
    private String tagsAsText;
    @Column(name = "text_preview")
    private String textPreview;
    @Column(name = "text_parts")
    private String textParts;
}
