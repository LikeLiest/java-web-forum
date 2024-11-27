package ru.forum.forum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.image.Image;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.model.post.PostRequestDTO;
import ru.forum.forum.model.post.PostResponseDTO;
import ru.forum.forum.repository.ImageRepository;
import ru.forum.forum.repository.redis.PostCacheRepository;
import ru.forum.forum.service.post.PostService;

import java.util.*;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("posts")
public class PostController {
  
  // TODO -> добавить сервисы для репозиториев
  private final PostService postService;
  private final ImageRepository imageRepository;
  private final PostCacheRepository postCacheRepository;
  private final RedisTemplate<String, PostCache> redisTemplate;
  
  // TODO -> Добавить ДТОошки
  
  @GetMapping("{id:[0-9]+}")
  public ResponseEntity<?> getPostById(@PathVariable("id") long id) {
    var post = this.postService.getPostById(id);
    if (post.isPresent()) {
      return returnFindPost(post);
    }
    
    if (post.get() instanceof PostCache) {
      log.info("CONTROLLER: POSTCACHE");
    }
    
    return ResponseEntity.badRequest().body("Не удалось найти запрашиваемый пост");
  }
  
  @GetMapping("/cache")
  public ResponseEntity<?> getPostFromCache() {
    Set<String> keys = redisTemplate.keys("post_*");
    
    if(keys == null || keys.isEmpty())
      return ResponseEntity.ok("Нет закэшированных данных ");
    
    List<PostCache> postCaches = keys.stream()
      .map(key -> redisTemplate.opsForValue().get(key))
      .toList();
    
    return ResponseEntity.ok(postCaches);
  }
  
  @GetMapping("{title:[^0-9].*}")
  public ResponseEntity<?> getPostById(@PathVariable("title") String title) {
    var post = this.postService.getPostByTitle(title);
    log.info("CONTROLLER: {}", post);
    
    if (post.isPresent()) {
      log.info("POST IS PRESENT");
      return returnFindPost(post);
    }
    
    return ResponseEntity.badRequest().body("Не удалось найти искомый пост");
  }
  
  @GetMapping("/all")
  public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
    List<Post> postList = this.postService.getAll();
    
    List<PostResponseDTO> postResponseDTOS = postList.stream()
      .map(this::convertEntityToResponse)
      .toList();
    
    Iterable<Image> imageList = this.imageRepository.findAll();
    
    postResponseDTOS.forEach(post -> {
      List<Image> images = StreamSupport.stream(imageList.spliterator(), false)
        .filter(img -> img.getOwnerId() == post.getId())
        .toList();
      images.forEach(post::addImage);
    });
    
    return ResponseEntity.ok(postResponseDTOS);
  }
  
  @PostMapping("/")
  public ResponseEntity<Post> savePost(@RequestBody PostRequestDTO requestDTO) {
    Post post = new Post();
    BeanUtils.copyProperties(requestDTO, post);
    
    this.postService.savePost(post);
    
    if (requestDTO.getBase64Images() != null) {
      requestDTO.getBase64Images().forEach(img -> {
        Image image = Image.setImageForObject(
          img,
          post.getId(),
          post.getTitle()
        );
        this.imageRepository.save(image);
      });
    }
    
    return ResponseEntity.ok(post);
  }
  
  @DeleteMapping("{id}")
  public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
    this.postService.deletePost(id);
    return ResponseEntity.ok("Пост с ID=%d успешно удален".formatted(id));
  }
  
  private ResponseEntity<?> returnFindPost(Optional<PostCache> findPost) {
    if (findPost.isPresent()) {
      PostCache post = findPost.get();
      var imageList = this.imageRepository.findByOwnerId(post.getId());
      
      var responseDto = new PostResponseDTO();
      
      BeanUtils.copyProperties(post, responseDto);
      responseDto.addListImages(imageList);
      
      log.info("{}", responseDto);
      return ResponseEntity.ok(responseDto);
    }
    
    return ResponseEntity.badRequest().body("Не удалось найти такой пост");
  }
  
  private PostResponseDTO convertEntityToResponse(Post post) {
    PostResponseDTO responseDTO = new PostResponseDTO();
    BeanUtils.copyProperties(post, responseDTO);
    return responseDTO;
  }
  
  // TODO -> update, patchUpdate
}
