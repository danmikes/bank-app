package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Account;



import nl.beehive.beehive.model.Customer;
import org.springframework.data.repository.CrudRepository;
import nl.beehive.beehive.model.Account;

import java.util.List;


public interface CustomerDao extends CrudRepository<Customer,Integer> {


    Customer findCustomerByUserId(int userID);
    List<Customer> findCustomerByUsername(String username);
    List<Customer> findCustomersByAccounts(Account account);

    boolean existsByBsn(int bsn);
}
