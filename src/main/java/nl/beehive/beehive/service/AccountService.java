package nl.beehive.beehive.service;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.AccountManagerDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class AccountService {

    private final AccountDao accountDao;
    private final AccountManagerDao accountManagerDao;
    private final TransactionScreensService transactionScreensService;

    public AccountService(AccountDao accountDao, AccountManagerDao accountManagerDao, TransactionScreensService transactionScreensService) {
        this.accountDao = accountDao;
        this.accountManagerDao = accountManagerDao;
        this.transactionScreensService = transactionScreensService;

    }

    public void saveAccount(Account account) {
        accountDao.save(account);
    }

    public String generateIban() {
        String countryCode = "NL";
        String bankCode = "BEEH";
        int lengthAccountnumber = 10;
        String accountNumber;
        String controleNumber;
        do {
            //genereer accountnummer
            accountNumber = generateAccountNumber(lengthAccountnumber);
            controleNumber = generateControlNumber(accountNumber, bankCode, countryCode);
            accountNumber = countryCode + controleNumber + bankCode + accountNumber;
            // controleer of iban in database voorkomt
        } while (accountDao.existsAccountByIban(accountNumber));
        return accountNumber;
    }

    public Account generateAccount(String type) {
        return new Account(generateIban(), type, new BigDecimal(0.00), transactionScreensService.formatLocalDateTransaction());
    }

    public Business generateBusinessAccount(Company company) {
        return new Business(generateAccount("BUSINESS_CH"), company, assignAccountManager());
    }

    //Methode om een random accountmanager toe te wijzen aan een nieuwe business
    private AccountManager assignAccountManager() {
        Random rand = new Random();
        AccountManager accountManager = new AccountManager();

        List<AccountManager> accountManagers = new ArrayList<>(accountManagerDao.findAll());

        int itemCount = accountManagers.size();

        for (int i = 0; i < itemCount; i++) {
            int randomIndex = rand.nextInt(itemCount);
            accountManager = accountManagers.get(randomIndex);
        }
        return accountManager;
    }

    //Methode maken om random accountManager uit de database toe te wijzen aan new Business.
    private String generateAccountNumber(int lengthAccountnumber) {
        // lengte nummers afhankelijk van land
        // ??final static van maken
        int min = 0;
        int max = 9;
        int c = (max - min + 1);
        int minLength = 7;
        int bound = lengthAccountnumber - minLength;

        String accountNumber;
        //int lengthCheck ;
        // create unique accountnumber
        // At least the first seven numbers are not zero : no personal banking number has an account shorter 7 numbers
        // do{
        accountNumber = "";
        //lengthCheck = 0;
        // generate number with 1 to 3 leading zero's
        for (int i = 0; i < bound; i++) {
            accountNumber += generateNumber(min, c);
        }
        for (int i = bound; i < lengthAccountnumber; i++) {
            accountNumber += generateNumber(min + 1, c - 1);
        }
        //} while (!accountNumbers.add(accountNumber));
        return accountNumber;
    }

    private String generateControlNumber(String accountNumber, String bankCode, String countryCode) {
        String ss;
        String unicodeSs;
        //
        ss = bankCode + accountNumber + countryCode;
        unicodeSs = charToUnicodeValue(ss);
        // add nullen
        unicodeSs += "00";
        int controleGetal = 98 - new BigInteger(unicodeSs).mod(new BigInteger("97")).intValue();
        return controleGetal > 9 ? "" + controleGetal : "" + 0 + controleGetal;
    }

    private int generateNumber(int min, int max) {
        //return (int)(Math.random()*(max-min+1))+ min;
        return (int) (Math.random() * max) + min;
    }

    private String charToUnicodeValue(String code) {
        String convertedString = "";
        char aChar;
        // IBAN need to be transformed into unicode int values
        for (int i = 0; i < code.length(); i++) {
            aChar = code.charAt(i);
            convertedString += Character.getNumericValue(aChar);
        }
        return convertedString;
    }
}
