package ru.forum.forum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/**").permitAll()
        .requestMatchers("/my-account/**", "/*").fullyAuthenticated()
      )
      
      .formLogin(login -> login
        .loginPage("/auth/signin")
        .defaultSuccessUrl("/posts/all", true)
        .failureForwardUrl("/auth/signin?error=true")
      )
      
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
      
      .exceptionHandling(exc -> exc
        .accessDeniedPage("/error")
        .authenticationEntryPoint((_, response, _) -> response.sendRedirect("/auth/signin"))
      )
      
      .build();
  }
  
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("*"));
    corsConfiguration.setAllowedMethods(List.of("*"));
    corsConfiguration.setAllowedHeaders(List.of("*"));
    corsConfiguration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}