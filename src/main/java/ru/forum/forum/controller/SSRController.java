package ru.forum.forum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.forum.forum.model.user.MyUser;
import ru.forum.forum.service.user.MyUserDetails;

import java.util.Base64;


@Slf4j
@Controller
public class SSRController {
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
  
  
//  @CurrentSecurityContext Authentication authentication
  @PreAuthorize("isAuthenticated()")
  @GetMapping("my-account")
  public String account(HttpServletResponse response, @AuthenticationPrincipal MyUserDetails myUser) {
    log.info("{}", myUser.getUsername());
    
    MyUser userDataForCookie = new MyUser();
    BeanUtils.copyProperties(myUser, userDataForCookie);
    userDataForCookie.setPassword(null);
    
    try {
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(userDataForCookie);
      String encodedJson = Base64.getEncoder().encodeToString(json.getBytes());
      
      Cookie cookie = new Cookie("myuser", encodedJson);
      cookie.setMaxAge(60 * 60);
      cookie.setPath("/");
      cookie.setHttpOnly(true);
      
      response.addCookie(cookie);
      
    } catch (JsonProcessingException ignore) {
    }
    return "account";
  }
}
