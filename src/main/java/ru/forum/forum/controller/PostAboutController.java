package ru.forum.forum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.ApiResponse;
import ru.forum.forum.model.image.Image;
import ru.forum.forum.model.post.Post;
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
  private final PostCacheService postCacheService;
  private final ImageService imageService;
  private final PostService postService;
  
  @GetMapping("/about")
  public ResponseEntity<ApiResponse<PostResponseDTO>> getAboutPostData(@RequestParam("article") String article) {
    PostCache cache = this.postCacheService.getPostByArticle(article);
    
    if (cache != null) {
      PostResponseDTO responseDTO = PostResponseDTO.convertObjectToResponse(cache);
      List<Image> imageList = this.imageService.findByOwnerId(responseDTO.getId());
      responseDTO.addListImages(imageList);
      return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найдено", responseDTO));
    } else {
      Optional<Post> post = this.postService.getPostByArticle(article);
      
      if (post.isPresent()) {
        Post currentPost = post.get();
        PostResponseDTO responseDTO = PostResponseDTO.convertObjectToResponse(currentPost);
        List<Image> imageList = this.imageService.findByOwnerId(responseDTO.getId());
        responseDTO.addListImages(imageList);
        
        PostCache postCache = new PostCache();
        BeanUtils.copyProperties(responseDTO, postCache);
        this.postCacheService.savePost(postCache);
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найдено", responseDTO));
      }
    }
    return null;
  }
}
