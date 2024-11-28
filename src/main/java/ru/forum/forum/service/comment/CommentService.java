package ru.forum.forum.service.comment;

import ru.forum.forum.model.comment.Comment;

import java.util.List;

public interface CommentService {
  void addComment(Comment comment);
  
  void deleteComment(Long id);
  
  Comment getComment(Long id);
  
  List<Comment> getAllComments();
  
  List<Comment> getAllCommentsByArticle(String article);
}
