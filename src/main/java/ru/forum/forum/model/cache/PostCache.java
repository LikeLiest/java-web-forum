package ru.forum.forum.model.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash("PostCache")
@Getter
@Setter
@NoArgsConstructor
public class PostCache {
  private long id;
  private String article = UUID.randomUUID().toString();
  private String title;
  private String content;
  
  private String dateOfCreate;
  private String commentsJson;
}