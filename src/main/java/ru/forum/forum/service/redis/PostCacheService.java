package ru.forum.forum.service.redis;

import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostCacheService {
  PostCache savePost(PostCache post);
  
  void deletePost(long id);
  
  Optional<PostCache> getPostById(long id);
  
  Optional<PostCache> getPostByTitle(String title);
  
  Optional<List<Post>> getAllPostsByTitle(String title);
  
  Optional<PostCache> getPostByArticle(String article);
  
  PostCache fullUpdatePost(PostCache cache);
  
  PostCache patchUpdatePost(String... values);
  
  List<PostCache> getAll();
  
}
