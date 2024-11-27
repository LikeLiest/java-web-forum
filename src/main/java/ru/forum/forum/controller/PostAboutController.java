package ru.forum.forum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.ApiResponse;
import ru.forum.forum.model.image.Image;
import ru.forum.forum.model.post.PostResponseDTO;
import ru.forum.forum.service.image.ImageService;
import ru.forum.forum.service.post.PostService;
import ru.forum.forum.service.redis.PostCacheService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostAboutController {
  private final ImageService imageService;
  private final PostService postService;
  
  @GetMapping("/about/{article}")
  public ResponseEntity<ApiResponse<PostResponseDTO>> getAboutPostData(@PathVariable("article") String article) {
    return null;
  }
}
