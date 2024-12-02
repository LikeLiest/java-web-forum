package ru.forum.forum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.forum.forum.model.user.MyUser;
import ru.forum.forum.model.user.credentials.Credentials;
import ru.forum.forum.model.user.role.Role;
import ru.forum.forum.repository.MyUserRepository;
import ru.forum.forum.service.jwt.JWTService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserServiceImpl implements MyUserService {
  private final MyUserRepository myUserRepository;
  private final PasswordEncoder passwordEncoder;
  
  @Override
  public MyUser saveUser(MyUser myUser) {
    String password = myUser.getPassword();
    String encodedPassword = this.passwordEncoder.encode(password);
    myUser.setPassword(encodedPassword);
    
    myUser.setRole(Role.USER);
    log.info("ROLES:  {}", myUser.getRole());
    
    return this.myUserRepository.save(myUser);
  }
  
  @Override
  public void deleteUser(Long id) {
    this.myUserRepository.deleteById(id);
  }
  
  @Override
  public void deleteUser(String username) {
    this.myUserRepository.deleteByUsername(username);
  }
  
  @Override
  public Optional<MyUser> getMyUser(Long id) {
    return this.myUserRepository.findById(id);
  }
  
  @Override
  public Optional<MyUser> getMyUser(String username) {
    log.info("USERNAME: {}",username);
    return this.myUserRepository.findByUsername(username);
  }
  
  @Override
  public MyUser fullUpdateMyUser(MyUser myUser) {
    return null;
  }
  
  @Override
  public MyUser patchUpdateMyUser(String... values) {
    return null;
  }
  

}
