package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.service.PinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pin")
public class AccountController {

    private final AccountDao accountDao;
    private final PinService pinService;

    public AccountController(AccountDao accountDao, PinService pinService) {
        this.accountDao = accountDao;
        this.pinService = pinService;
    }

    @GetMapping("/all")
    public List<Account> getal(){
        return null;
    }

    @GetMapping("/{pin}")
    public Account getAccount(@PathVariable int pin){
        Account account = new Account();
        Account accountFromDB = accountDao.findAccountsByPincode(pin).get(0);

        account.setBalance(accountFromDB.getBalance());
        account.setIban(accountFromDB.getIban());
        account.setType(accountFromDB.getType());

        return account;
    }

    @GetMapping("/verify/{iban}")
    public boolean verifyPin(@PathVariable String iban) {
        try {
            return pinService.verifyPincode(iban.split(",")[0], Integer.parseInt(iban.split(",")[1]));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @GetMapping("/transaction/{transaction}")
    public boolean transaction(@PathVariable String transaction) {
        return pinService.executeTransaction(transaction.split(",")[0], transaction.split(",")[1],
                Double.parseDouble(transaction.split(",")[2]));
    }
}
