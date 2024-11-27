package ru.forum.forum.model.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.forum.forum.model.comment.Comment;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @NotEmpty(message = "Заголовок поста не может быть пустым")
  @Size(min = 4, max = 1000, message = "Заголовок не должен быть меньше 4 и больше 1000")
  private String title;
  
  @JsonProperty(value = "content")
  @Size(message = "Контент слишком большой", max = 5000)
  private String content;
  
  private String article = UUID.randomUUID().toString();
  
  private String dateOfCreate;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Comment> comment = new ArrayList<>();
  
  public void addComment(Comment comment) {
    this.comment.add(comment);
  }
  
  @PrePersist
  public void setDataOfCreate() {
    if (this.dateOfCreate == null)
      this.dateOfCreate = ZonedDateTime.now().toLocalDate().toString();
  }
}
