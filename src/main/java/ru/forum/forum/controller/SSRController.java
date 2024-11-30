package ru.forum.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
  
  @GetMapping("auth/signin")
  public String signin() {
    return "auth/signin";
  }
  
  @GetMapping("auth/signup")
  public String signup() {
    return "auth/signup";
  }
  
  @GetMapping("error")
  public String error() {
    return "error/error";
  }
}
