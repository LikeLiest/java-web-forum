package ru.forum.forum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.model.comment.Comment;


@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
}