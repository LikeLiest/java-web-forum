package ru.forum.forum.service.post;

import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
  Post savePost(Post post);
  
  void deletePost(long id);
  
  Optional<PostCache> getPostById(long id);
  Optional<PostCache> getPostByTitle(String title);
  Optional<List<Post>> getAllPostsByTitle(String title);
  
  Post fullUpdatePost(Post post);
  
  Post patchUpdatePost(String... values);
  
  List<Post> getAll();
}
