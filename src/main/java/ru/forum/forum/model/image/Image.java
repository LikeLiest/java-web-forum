package ru.forum.forum.model.image;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  @Column(columnDefinition = "TEXT")
  private String base64;
  
  private String owner;
  private long ownerId;
  
  public static Image setImageForObject(String base64, long ownerId, String owner) {
    Image image = new Image();
    image.setBase64(base64);
    image.setOwner(owner);
    image.setOwnerId(ownerId);
    return image;
  }
}
