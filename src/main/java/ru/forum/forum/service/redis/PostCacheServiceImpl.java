package ru.forum.forum.service.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.repository.redis.PostCacheRepository;

import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCacheServiceImpl implements PostCacheService {
  private final PostCacheRepository postCacheRepository;
  
  @Override
  public PostCache savePost(PostCache cache) {
    return this.postCacheRepository.save(cache);
  }
  
  @Override
  public void deletePost(long id) {
    this.postCacheRepository.deleteById(id);
  }
  
  @Override
  public Optional<PostCache> getPostById(long id) {
    return this.postCacheRepository.findById(id);
  }
  
  @Override
  public Optional<PostCache> getPostByTitle(String title) {
    return this.postCacheRepository.findByTitle(title);
  }
  
  @Override
  public Optional<List<Post>> getAllPostsByTitle(String title) {
    return this.postCacheRepository.findAllByTitle(title);
  }
  
  @Override
  public Optional<PostCache> getPostByArticle(String article) {
    return this.postCacheRepository.findByArticle(article);
  }
  
  @Override
  public PostCache fullUpdatePost(PostCache post) {
    return null;
  }
  
  @Override
  public PostCache patchUpdatePost(String... values) {
    return null;
  }
  
  @Override
  public List<PostCache> getAll() {
    Spliterator<PostCache> postCacheSpliterator = this.postCacheRepository.findAll().spliterator();
    return StreamSupport.stream(postCacheSpliterator, false).toList();
  }
}
