package ru.forum.forum.repository.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.post.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostCacheRepository extends CrudRepository<PostCache, Long> {
  PostCache findByTitle(String title);
  
  PostCache findByArticle(String article);
  
  List<PostCache> findAllByTitle(String title);
}
