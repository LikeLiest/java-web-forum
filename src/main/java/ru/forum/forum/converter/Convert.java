package ru.forum.forum.converter;

import org.springframework.beans.BeanUtils;

public interface Convert<T, R> {
  default T toEntityConverter(T entity, R dto) {
    BeanUtils.copyProperties(dto, entity);
    return entity;
  }
  
  default R toResponseConverter(T entity, R response) {
    BeanUtils.copyProperties(response, entity);
    return response;
  }
}
