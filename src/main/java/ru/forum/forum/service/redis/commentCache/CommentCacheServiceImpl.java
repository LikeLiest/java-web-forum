package ru.forum.forum.service.redis.commentCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.forum.forum.model.cache.CommentCache;
import ru.forum.forum.repository.redis.CommentCacheRepository;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentCacheServiceImpl implements CommentCacheService {
  private final CommentCacheRepository commentCacheRepository;
  
  @Override
  public CommentCache saveCommentCache(CommentCache cache) {
    return this.commentCacheRepository.save(cache);
  }
  
  @Override
  public void deleteCommentCacheById(long id) {
    this.commentCacheRepository.deleteById(id);
  }
  
  @Override
  public CommentCache getCommentCacheById(long id) {
    return this.commentCacheRepository.findById(id).orElse(null);
  }
  
  // TODO
  @Override
  public CommentCache getCommentCacheByUsername(String username) {
    return null;
  }
  
  @Override
  public CommentCache getCommentCacheByPostContent(String content) {
    return this.commentCacheRepository.findByContent(content);
  }
  
  @Override
  public CommentCache fullUpdateCommentCache(CommentCache cache) {
    return this.commentCacheRepository.save(cache);
  }
  
  @Override
  public CommentCache getCommentCacheByArticle(String article) {
    return this.commentCacheRepository.findByPostArticle(article);
  }
  
  @Override
  public CommentCache patchUpdateCommentCache(String... values) {
    return null;
  }
  
  @Override
  public List<CommentCache> getAll() {
    Spliterator<CommentCache> commentCacheSpliterator = this.commentCacheRepository.findAll().spliterator();
    return StreamSupport.stream(commentCacheSpliterator, false).toList();
  }
}
