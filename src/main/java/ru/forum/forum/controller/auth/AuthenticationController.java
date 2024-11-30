package ru.forum.forum.controller.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.forum.forum.model.ApiResponse;
import ru.forum.forum.model.user.MyUser;
import ru.forum.forum.model.user.credentials.Credentials;
import ru.forum.forum.service.user.MyUserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
  private final PasswordEncoder encoder;
  private final MyUserService myUserService;
  
  @PostMapping("/signin")
  @Transactional
  public ResponseEntity<ApiResponse<MyUser>> login(@RequestBody Credentials credentials) {
    String username = credentials.getUsername();
    String password = credentials.getPassword();
    
    if (password == null)
      return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Неуспешная попытка авторизации", null));
    
    MyUser user = this.myUserService.getMyUser(username)
      .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден"));
    
    String userPassword = user.getPassword();
    
    if (encoder.matches(password, userPassword)) {
      log.info("Успешная попытка авторизации {}, {}", username, password);
      return ResponseEntity.ok().body(new ApiResponse<>(true, "Успешная попытка авторизации", user));
    }
    
    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Неуспешная попытка авторизации", null));
  }
  
  @PostMapping("signup")
  @Transactional
  public ResponseEntity<ApiResponse<MyUser>> signUp(@RequestParam String username, @RequestParam String password) {
    MyUser myUser = new MyUser();
    myUser.setPassword(password);
    myUser.setUsername(username);
    this.myUserService.saveUser(myUser);
    
    return ResponseEntity.ok(new ApiResponse<>(true, "Успешная регистрация", myUser));
  }
}
