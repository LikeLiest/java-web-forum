package ru.forum.forum.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MyUserLocationInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String dateOfRegister;
  private String country;
  private String city;
  private String email;
  private String telephone;
  
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private MyUser myUser;
  
  // TODO -> String :: ENUM
  private String language;
}
