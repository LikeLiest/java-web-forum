package ru.forum.forum.service.post;

import ru.forum.forum.model.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
  void savePost(Post post);
  
  void deletePost(long id);
  
  Optional<Post> getPostById(long id);
  
  Optional<Post> getPostByTitle(String title);
  
  Optional<List<Post>> getAllPostsByTitle(String title);
  
  Optional<Post> getPostByArticle(String article);
  
  Post fullUpdatePost(Post post);
  
  Post patchUpdatePost(String... values);
  
  List<Post> getAll();
  
}
