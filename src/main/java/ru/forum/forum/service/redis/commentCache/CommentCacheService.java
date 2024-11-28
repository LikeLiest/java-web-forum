package ru.forum.forum.service.redis.commentCache;

import ru.forum.forum.model.cache.CommentCache;

import java.util.List;

public interface CommentCacheService {
  CommentCache saveCommentCache(CommentCache cache);
  
  void deleteCommentCacheById(long id);
  
  CommentCache getCommentCacheById(long id);
  
  CommentCache getCommentCacheByUsername(String username);
  
  CommentCache getCommentCacheByPostContent(String content);
  
  CommentCache fullUpdateCommentCache(CommentCache cache);
  
  CommentCache getCommentCacheByArticle(String article);
  
  CommentCache patchUpdateCommentCache(String... values);
  
  List<CommentCache> getAll();
}
