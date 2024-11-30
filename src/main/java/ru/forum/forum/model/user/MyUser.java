package ru.forum.forum.model.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.model.user.role.Role;

import java.util.List;

@Entity
@Getter
@Setter
public class MyUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String username;
  private String password;
  private String userIcon;
  
  @Enumerated(EnumType.STRING)
  @ElementCollection
  private List<Role> roles;
  
  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "myUser")
  @JsonManagedReference(value = "myuserLocationInfo")
  private MyUserLocationInfo myUserLocationInfo;
  
  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "myUser")
  @JsonManagedReference(value = "myUserInfo")
  private MyUserInfo myUserInfo;
  
  @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "myUser")
  @JsonManagedReference(value = "postList")
  private List<Post> postList;
  
  public void addPostToList(Post post) {
    this.postList.add(post);
  }
  
  public void addListOfPost(List<Post> postList) {
    this.postList.addAll(postList);
  }
}
