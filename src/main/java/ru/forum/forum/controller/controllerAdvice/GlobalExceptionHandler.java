package ru.forum.forum.controller.controllerAdvice;


import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
  
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<String> handleValidationException(ValidationException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
}