package ru.forum.forum.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.cache.PostCache;

import java.util.Optional;

@Repository
public interface PostCacheRepository extends CrudRepository<PostCache, Long> {
  Optional<PostCache> findByTitle(String title);
  
  Optional<PostCache> findByArticle(String article);
}
