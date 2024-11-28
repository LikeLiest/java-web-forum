package ru.forum.forum.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  private final ImageService imageService;
  
  @Override
  @Transactional
  public void savePost(Post post) {
    postRepository.save(post);
  }
  
  @Override
  @Transactional
  public void deletePost(long id) {
    this.imageService.deleteAllByOwnerId(id);
    this.postRepository.deleteById(id);
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
  public Optional<Post> getPostByArticle(String article) {
    return this.postRepository.findByArticle(article);
  }
  
  @Override
  public Optional<Post> getPostById(long id) {
    return this.postRepository.findById(id);
  }
  
  @Override
  public Optional<Post> getPostByTitle(String title) {
    return this.postRepository.findByTitle(title);
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
