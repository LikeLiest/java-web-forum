package ru.forum.forum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.model.post.Post;

import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
  Optional<Post> findByTitle(String title);
}
