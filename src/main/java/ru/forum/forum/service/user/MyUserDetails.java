package ru.forum.forum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.forum.forum.model.user.MyUser;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class MyUserDetails implements UserDetails {
  private final MyUser myUser;
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    String userRole = myUser.getRoles().toString();
    
    return Collections.singletonList(
      new SimpleGrantedAuthority(userRole)
    );
  }
  
  @Override
  public String getPassword() {
    return myUser.getPassword();
  }
  
  @Override
  public String getUsername() {
    return myUser.getUsername();
  }
  
}
