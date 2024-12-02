package ru.forum.forum.service.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.NestedCollection;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.builder;

@Service
public class JWTService {
  private final String SECRET_KEY;
  private final SecretKey secretKey;
  private final Map<String, Object> claims = new HashMap<>();
  
  public JWTService() {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
      secretKey = keyGen.generateKey();
      SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
  
  public String generateToken(String username) {
    return builder()
      .claims(claims)
      .subject(username)
      .issuedAt(new Date(System.currentTimeMillis() + 60 * 60 * 30))
      .signWith(getKey())
      .compact();
  }
  
  public JwtParser validateToken(String token) {
    return Jwts.parser()
      .verifyWith(secretKey)
      .build();
  }
  
  public Key getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
