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
  public String account() {
    return "account";
  }
}
