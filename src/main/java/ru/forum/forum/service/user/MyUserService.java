package ru.forum.forum.service.user;

import ru.forum.forum.model.user.MyUser;

import java.util.Optional;

public interface MyUserService {
  void saveUser(MyUser myUser);

  void deleteUser(Long id);

  void deleteUser(String username);

  Optional<MyUser> getMyUser(Long id);

  Optional<MyUser> getMyUser(String username);

  MyUser fullUpdateMyUser(MyUser myUser);

  MyUser patchUpdateMyUser(String... values);
}
