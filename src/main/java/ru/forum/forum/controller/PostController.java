package ru.forum.forum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.forum.forum.model.cache.PostCache;
import ru.forum.forum.model.ApiResponse;
import ru.forum.forum.model.image.Image;
import ru.forum.forum.model.post.Post;
import ru.forum.forum.model.post.PostRequestDTO;
import ru.forum.forum.model.post.PostResponseDTO;
import ru.forum.forum.service.image.ImageService;
import ru.forum.forum.service.post.PostService;
import ru.forum.forum.service.redis.postCache.PostCacheService;

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
  private final PostCacheService postCacheService;
  
  // TODO -> ПЕРЕПИСАТЬ ВСЕ RESPONSE ENTITY ДОБАВИВ В НИХ APIRESPONSE
  
  @GetMapping("{id:[0-9]+}")
  public ResponseEntity<ApiResponse<PostResponseDTO>> getPostById(@PathVariable("id") long id) {
    PostCache cache = this.postCacheService.getPostById(id);
    
    if (cache != null) {
      log.info("Данные пришли из кэша");
      PostResponseDTO responseDTO = PostResponseDTO.convertObjectToResponse(cache);
      addImagesListToResponseDTOByID(responseDTO, responseDTO.getId());
      
      return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найдено", responseDTO));
    }
    
    Optional<Post> optionalPost = this.postService.getPostById(id);
    if (optionalPost.isPresent()) {
      Post post = optionalPost.get();
      
      PostCache cacheToSave = convertObjectToCache(post);
      this.postCacheService.savePost(cacheToSave);
      
      PostResponseDTO responseDTO = PostResponseDTO.convertObjectToResponse(post);
      addImagesListToResponseDTOByID(responseDTO, responseDTO.getId());
      
      return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найдено", responseDTO));
    }
    
    return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Ошибка. Не удалось найти запрашиваемые данные", null));
  }
  
  @GetMapping("/cache")
  public ResponseEntity<List<PostCache>> getPostFromCache() {
    List<PostCache> postCacheList = this.postCacheService.getAll();
    return ResponseEntity.ok(postCacheList);
  }
  
  @GetMapping("{title:[^0-9].*}")
  public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getPostById(@PathVariable("title") String title) {
    Optional<List<Post>> postList = this.postService.getAllPostsByTitle(title);
    
    if (postList.isPresent()) {
      List<Post> posts = postList.get();
      return findAllPosts(posts);
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
    this.postCacheService.deletePostById(id);
    return ResponseEntity.ok("Пост с ID=%d успешно удален".formatted(id));
  }
  
  private ResponseEntity<ApiResponse<List<PostResponseDTO>>> findAllPosts(List<Post> posts) {
    if (posts.isEmpty()) {
      return ResponseEntity.badRequest()
        .body(new ApiResponse<>(false, "Список постов пуст", null));
    }
    
    List<Long> postIds = posts.stream()
      .map(Post::getId)
      .toList();
    
    List<Image> imageList = this.imageService.findByOwnerIdIn(postIds);
    Map<Long, List<Image>> imagesByPostId = imageList.stream()
      .collect(Collectors.groupingBy(Image::getOwnerId));
    
    List<PostResponseDTO> responseDTOList = posts.stream()
      .map(post -> {
        PostResponseDTO dto = new PostResponseDTO();
        BeanUtils.copyProperties(post, dto);
        dto.addListImages(imagesByPostId.getOrDefault(post.getId(), imageList));
        log.info("{}", dto);
        return dto;
      })
      .toList();
    
    return ResponseEntity.ok(new ApiResponse<>(true, "Успешно найдено", responseDTOList));
  }
  
  private PostResponseDTO convertEntityToResponse(Post post) {
    return PostResponseDTO.convertObjectToResponse(post);
  }
  
  private void addImagesListToResponseDTOByID(PostResponseDTO responseDTO, Long ownerId) {
    List<Image> imageList = this.imageService.findByOwnerId(ownerId);
    responseDTO.addListImages(imageList);
  }
  
  private PostCache convertObjectToCache(Object entity) {
    PostCache cache = new PostCache();
    BeanUtils.copyProperties(entity, cache);
    return cache;
  }
  
}
