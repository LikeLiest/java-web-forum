package ru.forum.forum.controller.controllerAdvice;


import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.util.NoSuchElementException;

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
  
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
  
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
  
  @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
  public ResponseEntity<String> handleInternalError(HttpServerErrorException.InternalServerError e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
  
  @ExceptionHandler(InternalError.class)
  public ResponseEntity<String> handleInternalError(InternalError e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }
}
