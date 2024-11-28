package ru.forum.forum.model.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import ru.forum.forum.model.post.Post;


@Setter
@Getter
@NoArgsConstructor
@RedisHash("CommentCache")
public class CommentCache {
  private Long id;
  private String content;
  private String username;
  private String dateOfCreated;
  private String postArticle;
  private Post post;
}
