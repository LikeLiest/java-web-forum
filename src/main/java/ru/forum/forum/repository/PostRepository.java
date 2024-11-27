package ru.forum.forum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.post.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
  Optional<Post> findByTitle(String title);
  Optional<List<Post>> findAllByTitle(String title);
  Optional<Post> findByArticle(String article);
}