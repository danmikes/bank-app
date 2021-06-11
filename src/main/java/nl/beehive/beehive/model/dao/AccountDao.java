package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Company;
import nl.beehive.beehive.model.Customer;
import nl.beehive.beehive.model.User;
import nl.beehive.beehive.service.AccountType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountDao extends CrudRepository<Account, Integer> {
    List<Account> findAccountsByType(String type);
    List<Account> findAccountsByHoldersContains(User user);
    Account findAccountByAccountId(int accountId);
    Account findAccountByIban(String string);
    List<Account> findAccountsByPincode(int pin);
    Account findAccountByType(String string);
    boolean existsAccountByIban(String string);
}
