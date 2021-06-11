package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Bank;
import org.springframework.data.repository.CrudRepository;

public interface BankDao extends CrudRepository<Bank, Integer> {
}
