package nl.beehive.beehive.service;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Customer;
import nl.beehive.beehive.model.User;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CustomerService {

    private final AccountDao accountDao;

    private final UserService service;

    public CustomerService(AccountDao accountDao, UserService service) {
        this.accountDao = accountDao;
        this.service = service;
    }

    public List<Account> getAccountListByCustomer() {
        return accountDao.findAccountsByHoldersContains(service.getUser());
    }

    public Account getAccountById(int accountId) {
        return accountDao.findAccountByAccountId(accountId);
    }

}
