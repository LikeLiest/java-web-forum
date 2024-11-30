package ru.forum.forum.model.comment;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.forum.forum.model.post.Post;

@Entity
@Setter
@Getter
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @Column(columnDefinition = "TEXT")
  @Size(message = "Текст комментария не может быть слишком длинным", max = 2000)
  private String content;
  
  @NotEmpty(message = "Поле логина не может быть пйсто")
  private String username;
  @NotEmpty(message = "Отсутствует артикуль")
  private String postArticle;
  private String dateOfCreated;
  
  //  TODO
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonBackReference
  private Post post;
}
