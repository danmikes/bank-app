package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.AccountManager;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountManagerDao extends CrudRepository<AccountManager, Integer> {
    List<AccountManager> findAccountManagerByUsername(String username);

    List<AccountManager> findAll();

    AccountManager findAccountManagerByUserId(int userId);
}
