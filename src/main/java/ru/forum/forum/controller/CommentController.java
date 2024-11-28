package ru.forum.forum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.forum.forum.model.ApiResponse;
import ru.forum.forum.model.cache.CommentCache;
import ru.forum.forum.model.cache.PostCache;
import ru.forum.forum.model.comment.Comment;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.service.comment.CommentService;
import ru.forum.forum.service.post.PostService;
import ru.forum.forum.service.redis.commentCache.CommentCacheService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("comment")
public class CommentController {
  private final CommentCacheService commentCacheService;
  private final CommentService commentService;
  private final PostService postService;
  
  @PostMapping("addComment")
  public ResponseEntity<ApiResponse<Comment>> addComment(@RequestBody Comment comment) {
    String article = comment.getPostArticle();
    
    
    if (article != null) {
      Post post = this.postService.getPostByArticle(article)
        .orElseThrow(() -> new IllegalArgumentException("Не удалось найти пост"));
      
      comment.setPost(post);
      this.commentService.addComment(comment);
      post.addComment(comment);
      
      return ResponseEntity.ok(
        new ApiResponse<>(true, "Комментарий успешно добавлен", comment)
      );
    }
    
    return ResponseEntity.badRequest().body(
      new ApiResponse<>(false, "Не удалось добавить комментарий", null)
    );
    
  }
  
  @GetMapping("{id}")
  public ResponseEntity<ApiResponse<Comment>> getCommentByID(@PathVariable Long id) {
    CommentCache commentCache = this.commentCacheService.getCommentCacheById(id);
    
    if (commentCache != null) {
      Comment comment = new Comment();
      BeanUtils.copyProperties(commentCache, comment);
      
      return ResponseEntity.ok(
        new ApiResponse<>(true, "Успешно найдено", comment)
      );
    }
    
    Comment comment = this.commentService.getComment(id);
    
    if (comment != null) {
      CommentCache cache = new CommentCache();
      BeanUtils.copyProperties(comment, cache);
      this.commentCacheService.saveCommentCache(cache);
      
      return ResponseEntity.ok(
        new ApiResponse<>(true, "Успешно найдено", comment)
      );
    }
    
    return ResponseEntity.badRequest()
      .body(new ApiResponse<>(false, "Ошибка. Ничего не найдено", null));
  }
  
  // TODO -> Сначала вытащить все данные из кэша, а потом уже из бд
  @GetMapping("/all")
  public ResponseEntity<ApiResponse<List<Comment>>> getAllComments(@RequestParam String article) {
    List<Comment> commentList = this.commentService.getAllCommentsByArticle(article);
    
    if (!commentList.isEmpty()) {
      return ResponseEntity.ok(
        new ApiResponse<>(true, "Успешно найдено", commentList)
      );
    }
    
    return ResponseEntity.badRequest()
      .body(new ApiResponse<>(false, "Ошибка. Ничего не найдено", null));
  }
}
