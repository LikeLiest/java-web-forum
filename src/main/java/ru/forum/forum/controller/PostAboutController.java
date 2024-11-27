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

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostAboutController {
  private final ImageService imageService;
  private final PostService postService;
  private final RedisTemplate<String, PostCache> redisTemplate;
  
  @GetMapping("/about/{article}")
  public ResponseEntity<ApiResponse<PostResponseDTO>> getAboutPostData(@PathVariable("article") String article) {
    Optional<PostCache> postCache = findDataFromCache(article);
    
    if (postCache.isEmpty()) {
      // !!!
      PostCache cache = this.postService.getPostByArticle(article)
        .orElseThrow(() -> new IllegalArgumentException("Не удалось найти пост"));
      PostResponseDTO responseDTO = convertToResponseDTO(cache);
      
      List<Image> imageList = this.imageService.findByOwnerId(cache.getId());
      responseDTO.setImageList(imageList);
      
      return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найден", responseDTO));
    } else {
      PostCache cache = postCache.get();
      PostResponseDTO responseDTO = convertToResponseDTO(cache);
      BeanUtils.copyProperties(postCache, responseDTO);
      
      List<Image> imageList = this.imageService.findByOwnerId(cache.getId());
      responseDTO.setImageList(imageList);
      
      return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найден", responseDTO));
    }
  }
  
  private PostResponseDTO convertToResponseDTO(PostCache cache) {
    PostResponseDTO responseDTO = new PostResponseDTO();
    BeanUtils.copyProperties(cache, responseDTO);
    List<Image> imageList = this.imageService.findByOwnerId(responseDTO.getId());
    responseDTO.setImageList(imageList);
    return responseDTO;
  }
  
  private Optional<PostCache> findDataFromCache(String article) {
    PostCache postCache = this.redisTemplate.opsForValue().get("article_" + article);
    if (postCache != null) {
      log.info("Кэш найден: {}", postCache.getArticle());
      return Optional.of(postCache);
    }
    log.info("Кэш не найден");
    return Optional.empty();
  }
}
