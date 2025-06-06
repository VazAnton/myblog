package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
