package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "LIMIT ? " +
                    "OFFSET ?;")
    List<Post> findAllWithLimitAndOffset(long to, long from);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM posts " +
                    "WHERE tags LIKE ? " +
                    "LIMIT ? " +
                    "OFFSET ?;")
    List<Post> findAllWithParameters(String tag, long to, long from);
}
