package ru.forum.forum.model.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.forum.forum.model.image.Image;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PostResponseDTO {
  private long id;
  private String title;
  private String content;
  private String article;
  private String dateOfCreate;
  
  private List<Image> imageList = new ArrayList<>();
  
  public void addListImages(List<Image> images) {
    this.getImageList().addAll(images);
  }
  
  public void addImage(Image image) {
    this.getImageList().add(image);
  }
}
