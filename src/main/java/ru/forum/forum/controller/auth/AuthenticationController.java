package ru.forum.forum.controller.auth;


import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.forum.forum.model.ApiResponse;
import ru.forum.forum.model.image.Image;
import ru.forum.forum.model.user.MyUser;
import ru.forum.forum.model.user.credentials.Credentials;
import ru.forum.forum.model.user.credentials.RegisterCredentials;
import ru.forum.forum.service.image.ImageService;
import ru.forum.forum.service.jwt.JWTService;
import ru.forum.forum.service.user.MyUserService;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
  private final MyUserService myUserService;
  private final ImageService imageService;
  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;
  
  @PostMapping("/signin")
  public ResponseEntity<?> login(@RequestBody Credentials credentials, HttpServletResponse servletResponse) {
    log.info("{}", credentials.toString());
    
    String token = verify(credentials);
    
    if ("fail".equals(token))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильный логин или пароль");
    
    ResponseCookie cookie = ResponseCookie.from("token", token)
      .httpOnly(true)
      .secure(true)
      .sameSite("Strict")
      .path("/")
      .maxAge(Duration.ofHours(1))
      .build();
    
    servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    
    return ResponseEntity.ok(new JwtResponse(token));
  }
  
  @PostMapping("signup")
  @Transactional
  public ResponseEntity<ApiResponse<MyUser>> signUp(@RequestBody RegisterCredentials credentials) {
    MyUser myUser = new MyUser();
    BeanUtils.copyProperties(credentials, myUser);
    
    MyUser user = this.myUserService.saveUser(myUser);
    
    Image image = new Image();
    image.setBase64(credentials.getBase64String());
    image.setOwner(user.getUsername());
    image.setOwnerId(user.getId());
    
    this.imageService.save(image);
    
    return ResponseEntity.ok(new ApiResponse<>(true, "Успешная регистрация", myUser));
  }
  
  private String verify(Credentials credentials) {
    Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      credentials.getUsername(),
      credentials.getPassword()
    ));
    
    if (authentication.isAuthenticated())
      return this.jwtService.generateToken(credentials.getUsername());
    return "fail";
  }
  
  @Getter
  @RequiredArgsConstructor
  public static class JwtResponse {
    private final String token;
  }
}
