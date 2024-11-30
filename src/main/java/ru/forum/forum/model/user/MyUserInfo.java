package ru.forum.forum.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MyUserInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  boolean isAccountNonExpired;
  boolean isAccountNonLocked;
  boolean isCredentialsNonExpired;
  boolean isEnabled;
  
  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
  @JsonBackReference
  private MyUser myUser;
}
