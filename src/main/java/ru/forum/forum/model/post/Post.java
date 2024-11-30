package ru.forum.forum.model.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CollectionType;
import ru.forum.forum.model.comment.Comment;
import ru.forum.forum.model.user.MyUser;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Slf4j
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
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", orphanRemoval = true)
  @JsonManagedReference(value = "comments")
  private List<Comment> comments = new ArrayList<>();
  
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonBackReference(value = "post")
  @JoinColumn(name = "myuser_id")
  private MyUser myUser;
  
  public void addComment(Comment comment) {
    log.info("{}", comment);
    this.comments.add(comment);
  }
  
  @PrePersist
  public void setDataOfCreate() {
    if (this.dateOfCreate == null)
      this.dateOfCreate = ZonedDateTime.now().toLocalDate().toString();
  }
}
