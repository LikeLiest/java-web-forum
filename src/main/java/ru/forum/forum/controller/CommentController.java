package ru.forum.forum.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.forum.forum.model.comment.Comment;
import ru.forum.forum.service.comment.CommentService;
import ru.forum.forum.service.post.PostService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
  private final CommentService commentService;
  private final PostService postService;
  
  @GetMapping("{id}")
  public ResponseEntity<Comment> getCommentById(@PathVariable("id") long id) {
    Comment comment = this.commentService.getComment(id);
    return ResponseEntity.ok(comment);
  }
  
  // TODO Получать не все комментарии, а только те, что привязаны к конкретному посту.
  // TODO Коммент без поста существовать не может
}
