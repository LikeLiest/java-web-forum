package ru.forum.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
