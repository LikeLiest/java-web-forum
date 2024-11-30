package ru.forum.forum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.forum.forum.model.user.MyUser;

import java.util.Optional;

@Repository
public interface MyUserRepository extends CrudRepository<MyUser, Long> {
  Optional<MyUser> findByUsername(String username);
  
  void deleteByUsername(String username);
}
