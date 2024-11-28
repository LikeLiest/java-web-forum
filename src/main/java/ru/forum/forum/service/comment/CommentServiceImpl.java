package ru.forum.forum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.forum.forum.model.comment.Comment;
import ru.forum.forum.repository.CommentRepository;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;
  
  @Override
  public void addComment(Comment comment) {
    this.commentRepository.save(comment);
  }
  
  @Override
  public void deleteComment(Long id) {
    this.commentRepository.deleteById(id);
  }
  
  @Override
  public Comment getComment(Long id) {
    return this.commentRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Не удалось найти комментарий"));
  }
  
  @Override
  public List<Comment> getAllComments() {
    Iterable<Comment> commentIterable = this.commentRepository.findAll();
    return StreamSupport.stream(commentIterable.spliterator(), false).toList();
  }
  
  @Override
  public List<Comment> getAllCommentsByArticle(String article) {
    Spliterator<Comment> commentSpliterator = this.commentRepository.findAllByPostArticle(article).spliterator();
    return StreamSupport.stream(commentSpliterator, false).toList();
  }
}
