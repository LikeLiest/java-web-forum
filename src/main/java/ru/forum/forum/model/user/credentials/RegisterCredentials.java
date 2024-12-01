package ru.forum.forum.model.user.credentials;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterCredentials {
  @JsonProperty("username")
  private String username;
  
  @JsonProperty("password")
  private String password;
  
  @JsonProperty("userIcon")
  private String base64String;
  
  @JsonProperty("email")
  private String email;
  
  //MY_USER_ADDITIONAL_INFO
}

