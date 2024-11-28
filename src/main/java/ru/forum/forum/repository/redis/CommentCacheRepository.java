package ru.forum.forum.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.model.cache.CommentCache;

@Repository
public interface CommentCacheRepository extends CrudRepository<CommentCache, Long> {
  CommentCache findByContent(String content);
  
  CommentCache findByPostArticle(String article);
}
