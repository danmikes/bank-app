package nl.beehive.beehive.service;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Business;
import nl.beehive.beehive.model.Company;
import nl.beehive.beehive.model.Customer;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.BusinessDao;
import nl.beehive.beehive.model.dao.CustomerDao;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;
    private final BusinessDao businessDao;
    private final CustomerDao customerDao;
    private final AccountDao accountDao;
    private final AccountService accountService;

    private Account account;
    private Customer customer;

    public RegistrationService(UserService userService, BusinessDao businessDao, CustomerDao customerDao, AccountDao accountDao, AccountService accountService) {
        this.userService = userService;
        this.businessDao = businessDao;
        this.customerDao = customerDao;
        this.accountDao = accountDao;
        this.accountService = accountService;
    }

    public void saveNewRetailAccount() {
        account = accountService.generateAccount(AccountType.RETAIL_CHECKING.toString());
        customer = (Customer) userService.getUser();

        account.addHolder(customer);

        customerDao.save(customer);
        accountDao.save(account);

        userService.getSession().setAttribute("account", account);
    }

    public void saveNewBusinessAccount(Company company) {
        account = accountService.generateBusinessAccount(company);
        customer = (Customer) userService.getUser();

        account.addHolder(customer);

        customerDao.save(customer);
        businessDao.save((Business) account);

        userService.getSession().setAttribute("account", account);
    }

    public String registrationText() {

        String result = "Welkom " + customer.getFirstName() + ", je hebt een nieuwe rekening geopend bij de Beehive Bank";
        String iban = "Je nieuwe IBAN is: " + account.getIban();

        if (account instanceof Business) {
            return result + " voor " + Business.class.cast(account).getCompany().getCompanyName() + ".\n" + iban;
        }

        return result + "." + iban;
    }

    public Account getAccount() {
        return account;
    }
}
