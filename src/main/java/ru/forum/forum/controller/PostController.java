package ru.forum.forum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.forum.forum.cache.PostCache;
import ru.forum.forum.model.ApiResponse;
import ru.forum.forum.model.image.Image;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.model.post.PostRequestDTO;
import ru.forum.forum.model.post.PostResponseDTO;
import ru.forum.forum.service.image.ImageService;
import ru.forum.forum.service.post.PostService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("posts")
public class PostController {
  private final PostService postService;
  private final ImageService imageService;
  private final RedisTemplate<String, PostCache> redisTemplate;
  
  @GetMapping("{id:[0-9]+}")
  public ResponseEntity<?> getPostById(@PathVariable("id") long id) {
    var post = this.postService.getPostById(id);
    if (post.isPresent()) {
      return findPost(post);
    }
    
    return ResponseEntity.badRequest().body("Не удалось найти запрашиваемый пост");
  }
  
  @GetMapping("/cache")
  public ResponseEntity<?> getPostFromCache() {
    Set<String> keys = redisTemplate.keys("post_*");
    
    if (keys == null || keys.isEmpty())
      return ResponseEntity.ok("Нет закэшированных данных ");
    
    List<PostCache> postCaches = keys.stream()
      .map(key -> redisTemplate.opsForValue().get(key))
      .toList();
    
    return ResponseEntity.ok(postCaches);
  }
  
  @GetMapping("{title:[^0-9].*}")
  public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getPostById(@PathVariable("title") String title) {
    Optional<List<Post>> optionalPostList = this.postService.getAllPostsByTitle(title);
    
    if (optionalPostList.isPresent()) {
      List<Post> postList = optionalPostList.get();
      return findAllPosts(postList);
    }
    
    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Не удалось получить список постов", null));
  }
  
  @GetMapping("/all")
  public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
    List<Post> postList = this.postService.getAll();
    
    List<PostResponseDTO> postResponseDTOS = postList.stream()
      .map(this::convertEntityToResponse)
      .toList();
    
    Iterable<Image> imageList = this.imageService.findAll();
    
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
        this.imageService.save(image);
      });
    }
    
    return ResponseEntity.ok(post);
  }
  
  @DeleteMapping("{id}")
  public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
    this.postService.deletePost(id);
    return ResponseEntity.ok("Пост с ID=%d успешно удален".formatted(id));
  }
  
  private ResponseEntity<?> findPost(Optional<PostCache> findPost) {
    if (findPost.isPresent()) {
      PostCache post = findPost.get();
      var imageList = this.imageService.findByOwnerId(post.getId());
      
      var responseDto = new PostResponseDTO();
      
      BeanUtils.copyProperties(post, responseDto);
      responseDto.addListImages(imageList);
      
      log.info("{}", responseDto);
      return ResponseEntity.ok(responseDto);
    }
    
    return ResponseEntity.badRequest().body("Не удалось найти такой пост");
  }
  
  private ResponseEntity<ApiResponse<List<PostResponseDTO>>> findAllPosts(List<Post> postCaches) {
    if (postCaches.isEmpty()) {
      return ResponseEntity.badRequest()
        .body(new ApiResponse<>(false, "Список постов пуст", null));
    }
    
    List<Long> postIds = postCaches.stream()
      .map(Post::getId)
      .toList();
    
    // Нужно получить только само изображение(base64), а не объект изображения
    // Клиент неправильно из-за этого рендерит страницу
    
    List<Image> imageList = this.imageService.findByOwnerIdIn(postIds);
    Map<Long, List<Image>> imagesByPostId = imageList.stream()
      .collect(Collectors.groupingBy(Image::getOwnerId));
    
    List<PostResponseDTO> responseDTOList = postCaches.stream()
      .map(post -> {
        var dto = new PostResponseDTO();
        BeanUtils.copyProperties(post, dto);
        dto.addListImages(imagesByPostId.getOrDefault(post.getId(), imageList));
        log.info("{}", dto);
        return dto;
      })
      .toList();
    
    return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найдено", responseDTOList));
  }
  
  private PostResponseDTO convertEntityToResponse(Post post) {
    PostResponseDTO responseDTO = new PostResponseDTO();
    BeanUtils.copyProperties(post, responseDTO);
    return responseDTO;
  }
  
}
