package ru.forum.forum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.model.image.Image;

import java.util.List;
import java.util.Set;


@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
  List<Image> findByOwnerId(long id);
  List<Image> findByOwner(String owner);
  
  void deleteAllByOwnerId(long id);
}
