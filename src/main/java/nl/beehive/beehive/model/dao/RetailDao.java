package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Retail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RetailDao extends CrudRepository<Retail, Integer> {

  Retail findByAccountId(int accountId);
  List<Retail> findAllByType(String type);
}
