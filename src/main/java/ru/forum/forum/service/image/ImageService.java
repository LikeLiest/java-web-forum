package ru.forum.forum.service.image;

import ru.forum.forum.model.image.Image;

import java.util.List;

public interface ImageService {
  List<Image> findByOwnerId(Long id);
  
  void save(Image image);
  
  Iterable<Image> findAll();
  
  List<Image> findByOwner(String owner);
  
  void deleteAllByOwnerId(long id);
  
  List<Image> findByOwnerIdIn(List<Long> postIds);
}
