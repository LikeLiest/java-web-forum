package ru.forum.forum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.forum.forum.model.user.MyUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
  private final MyUserService myUserService;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    MyUser user = this.myUserService.getMyUser(username)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователь %s не найден".formatted(username)));
    log.info("Найден пользователь: {}", user.getUsername());
    return new MyUserDetails(user);
  }
}