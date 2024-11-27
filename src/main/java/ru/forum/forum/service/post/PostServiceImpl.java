package ru.forum.forum.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.repository.PostRepository;
import ru.forum.forum.service.image.ImageService;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
  private final PostRepository postRepository;
  private final RedisTemplate<String, PostCache> redisTemplate;
  
  @Override
  @Transactional
  public Post savePost(Post post) {
    return postRepository.save(post);
  }
  
  @Override
  public void deletePost(long id) {
    PostCache postCache = redisTemplate.opsForValue().get("post_" + id);
    
    if (postCache != null)
      redisTemplate.delete("post_" + id);
    
    postRepository.deleteById(id);
  }
  
  // TODO
  @Override
  public List<Post> getAll() {
    Iterable<Post> posts = this.postRepository.findAll();
    return StreamSupport.stream(posts.spliterator(), false).toList();
  }
  
  @Override
  public Optional<List<Post>> getAllPostsByTitle(String title) {
    return this.postRepository.findAllByTitle(title);
  }
  
  @Override
  public Optional<PostCache> getPostById(long id) {
    PostCache postCache = redisTemplate.opsForValue().get("post_" + id);
    if (postCache != null)
      return Optional.of(postCache);
    
    var post = this.postRepository.findById(id);
    
    if (post.isPresent()) {
      PostCache postToSave = convertToCache(post.get());
      redisTemplate.opsForValue().set("post_" + id, postToSave);
      return Optional.of(postToSave);
    }
    
    return Optional.empty();
  }
  
  private PostCache convertToCache(Post post) {
    PostCache postCache = new PostCache();
    BeanUtils.copyProperties(post, postCache);
    return postCache;
  }
  
  @Override
  public Optional<PostCache> getPostByTitle(String title) {
    PostCache cache = redisTemplate.opsForValue().get("post_" + title);
    if (cache != null) {
      PostCache cacheById = redisTemplate.opsForValue().get("post_" + cache.getId());
      if(cacheById == cache)
        return Optional.of(cacheById);
      
      return Optional.of(cache);
    }
    
    var post = this.postRepository.findByTitle(title);
    
    if (post.isPresent()) {
      PostCache postToSave = convertToCache(post.get());
      redisTemplate.opsForValue().set("post_" + title, postToSave);
      return Optional.of(postToSave);
    }
    
    return Optional.empty();
  }
  
  @Override
  @Transactional
  public Post fullUpdatePost(Post post) {
    this.postRepository.save(post);
    return post;
  }
  
  // TODO
  @Override
  @Transactional
  public Post patchUpdatePost(String... values) {
    return null;
  }
}
