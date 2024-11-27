package ru.forum.forum.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.forum.forum.model.image.Image;
import ru.forum.forum.repository.ImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
  private final ImageRepository imageRepository;
  
  @Override
  public List<Image> findByOwnerId(Long id) {
    return this.imageRepository.findByOwnerId(id);
  }
  
  @Override
  public void save(Image image) {
    this.imageRepository.save(image);
  }
  
  @Override
  public Iterable<Image> findAll() {
    return this.imageRepository.findAll();
  }
  
  @Override
  public List<Image> findByOwner(String owner) {
    return this.imageRepository.findByOwner(owner);
  }
  
  @Override
  public void deleteAllByOwnerId(long id) {
    this.imageRepository.deleteAllByOwnerId(id);
  }
  
  @Override
  public List<Image> findByOwnerIdIn(List<Long> postIds) {
    return this.imageRepository.findByOwnerIdIn(postIds);
  }
}
