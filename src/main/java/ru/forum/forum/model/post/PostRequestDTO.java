package ru.forum.forum.model.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PostRequestDTO {
  @JsonProperty(value = "title")
  @NotEmpty(message = "Заголовок поста не может быть пустым")
  @Size(min = 4, max = 1000, message = "Заголовок не должен быть меньше 4 и больше 1000")
  private String title;
  
  @JsonProperty(value = "content")
  @Size(message = "Контент слишком большой", max = 5000)
  private String content;
  
  private String dateOfCreate;
  
  @JsonProperty(value = "base64Image")
  private List<String> base64Images;
  
}
