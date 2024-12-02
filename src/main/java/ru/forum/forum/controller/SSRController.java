package ru.forum.forum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.forum.forum.model.user.MyUser;
import ru.forum.forum.service.jwt.JWTService;
import ru.forum.forum.service.user.MyUserDetails;
import ru.forum.forum.service.user.MyUserService;

import java.time.Duration;
import java.util.Base64;


@Slf4j
@Controller
@RequiredArgsConstructor
public class SSRController {
  private final JWTService jwtService;
  private final MyUserService myUserService;
  
  
  @GetMapping("dashboard")
  public String postController() {
    return "dashboard";
  }
  
  @GetMapping("create")
  public String createPost() {
    return "createPost";
  }
  
  @GetMapping("info")
  public String postInfo() {
    return "postInfo";
  }
  
  @GetMapping("signup")
  public String signun() {
    return "signup";
  }
  
  @GetMapping("signin")
  public String signin() {
    return "signin";
  }
  
  @GetMapping("error")
  public String error() {
    return "error/error";
  }
  
  
  @GetMapping("my-account")
  public String account(HttpServletResponse response,
                        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
                        @CookieValue(value = "token", required = false) String tokenFromCookie,
                        @AuthenticationPrincipal MyUserDetails myUser
  ) {
    String token = tokenFromCookie;
    log.info("{}", token);
    
    if (token == null && authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
      token = authorizationHeader.substring(7);


    JwtParser parser = this.jwtService.validateToken(token);
    log.info("claims: {}", parser.toString());
    var temp = parser.parseSignedClaims(token);
    String qwerty = temp.getPayload().getSubject();
    log.info("TEMP: {}", temp);
    
    log.info("MYUSER: {}", myUser.toString());

    return "account";
  }
}
