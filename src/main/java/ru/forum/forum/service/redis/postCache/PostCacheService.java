package ru.forum.forum.service.redis.postCache;

import ru.forum.forum.model.cache.PostCache;

import java.util.List;

public interface PostCacheService {
  void savePost(PostCache cache);
  
  void deletePostById(long id);
  
  PostCache getPostById(long id);
  
  PostCache getPostByTitle(String title);
  
  List<PostCache> getAllPostsByTitle(String title);
  
  PostCache getPostByArticle(String article);
  
  PostCache fullUpdatePost(PostCache cache);
  
  PostCache patchUpdatePost(String... values);
  
  List<PostCache> getAll();
  
}
