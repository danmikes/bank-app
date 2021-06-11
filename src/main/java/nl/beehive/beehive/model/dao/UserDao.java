package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<User, Integer> {

  User findByUsername(String username);
  boolean existsByEmail(String email);
  boolean existsByTelephone(String telefoonNummer);
  boolean existsUserByUsername(String username);
}