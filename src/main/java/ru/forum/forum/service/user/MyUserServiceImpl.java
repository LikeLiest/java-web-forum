package ru.forum.forum.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.forum.forum.model.user.MyUser;
import ru.forum.forum.repository.MyUserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserServiceImpl implements MyUserService {
  private final MyUserRepository myUserRepository;
  
  @Override
  public void saveUser(MyUser myUser) {
    this.myUserRepository.save(myUser);
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
