package ru.forum.forum.model.comment;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.forum.forum.model.post.Post;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Post post;
  
  private String content;
  
  private LocalDate dateOfCreated;
  
  @PrePersist
  public void setDateOfCreated() {
    if(this.dateOfCreated != null)
      this.dateOfCreated = LocalDate.now();
  }
  
  //  TODO. ADD USER
}
