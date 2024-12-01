package ru.forum.forum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.forum.forum.service.user.MyUserDetailsService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
  
  @Bean
  public MyUserDetailsService myUserDetailsService() {
    return new MyUserDetailsService();
  }
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(myUserDetailsService());
    
    return http
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      
      .csrf(AbstractHttpConfigurer::disable)
      
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/my-account/**").authenticated()
        .requestMatchers("/**").permitAll()
      )

      .formLogin(login -> login
        .loginPage("/signin")
        .defaultSuccessUrl("/my-account", true)
        .failureForwardUrl("/signin?error=true")
      )
      
      .authenticationProvider(provider)

      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

      .exceptionHandling(exc -> exc
        .accessDeniedPage("/error")
        .authenticationEntryPoint((_, response, _) -> response.sendRedirect("/signin"))
      )
      
      .build();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of("**"));
    corsConfiguration.setAllowedMethods(List.of("**"));
    corsConfiguration.setAllowedHeaders(List.of("**"));
    corsConfiguration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}