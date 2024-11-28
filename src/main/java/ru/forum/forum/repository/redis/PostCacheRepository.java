package ru.forum.forum.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.model.cache.PostCache;

import java.util.List;

@Repository
public interface PostCacheRepository extends CrudRepository<PostCache, Long> {
  PostCache findByTitle(String title);
  
  PostCache findByArticle(String article);
  
  List<PostCache> findAllByTitle(String title);
}
