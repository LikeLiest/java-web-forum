package ru.forum.forum.configuration.redisConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import ru.forum.forum.model.cache.PostCache;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
  @Bean
  public RedisConnectionFactory connectionFactory() {
    return new LettuceConnectionFactory("localhost", 6379);
  }
  
  @Bean
  public RedisTemplate<String, PostCache> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, PostCache> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    
    return redisTemplate;
  }
}
