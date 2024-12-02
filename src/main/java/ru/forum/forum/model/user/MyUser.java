package ru.forum.forum.model.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.repository.cdi.Eager;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.model.user.role.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class MyUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(unique = true)
  private String username;
  @Column(unique = true)
  private String email;
  
  private String token;
  
  private String password;
  
  @Enumerated(EnumType.STRING)
  private Role role;
  
  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "myUser")
  @JsonManagedReference(value = "myuserLocationInfo")
  private MyUserLocationInfo myUserLocationInfo;
  
  //MY_USER_ADDITIONAL_INFO
  
  @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "myUser")
  @JsonManagedReference(value = "postList")
  private List<Post> postList;
  
  public void addPostToList(Post post) {
    this.postList.add(post);
  }
  
  public void addListOfPost(List<Post> postList) {
    this.postList.addAll(postList);
  }
}
