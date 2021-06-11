package nl.beehive.beehive.service;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.CustomerDao;
import nl.beehive.beehive.model.dao.TransactionDao;
import nl.beehive.beehive.model.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TransactionScreensService {
    @Autowired
    AccountDao accountDao;
    @Autowired
    Validator validator;
    @Autowired
    UserDao userDao;
    @Autowired
    CustomerDao customerDao;
    @Autowired
    TransactionDao transactionDao;
    @Autowired
    UserService userService;

    @Autowired
    TransactionScreensService transactionScreensService;


    private String feedback = " ";


    public Transaction determineTypeOfTransaction(Transaction transaction){

        //Retreive the internal bankaccounts from the database
        Account bypassInSenderExternal = findAccountByType(AccountType.BEEH_BYPASS_IN.toString());
        Account bypassOutReceiverExternal = findAccountByType(AccountType.BEEH_BYPASS_OUT.toString());

        Transaction typeTransaction;
        String ibanReceiver = transaction.getInAcountIban();
        String ibanSender = transaction.getOutAccountIban();

        //Is either the receiving or sending account not part of our bank.
        if (!ibanReceiver.contains("BEEH")) {
            typeTransaction = new TransactionReceiverExternal(transaction, transaction.getNameReceiver(), ibanReceiver);
            typeTransaction.setInAccount(bypassOutReceiverExternal);
        } else if (!ibanSender.contains("BEEH")) {
            typeTransaction = new TransactionSenderExternal(transaction, "toBeDetermined", ibanSender);
            typeTransaction.setOutAccount(bypassInSenderExternal);
        } else{
            typeTransaction = transaction;
        }
        return typeTransaction;
    }

    public Account findAccountByAccountId(int accountId) {
        Account returnAccount = accountDao.findAccountByAccountId(accountId);
        return returnAccount;
    }
    public Account findAccountByType(String string) {
        return accountDao.findAccountByType(string);
    }

    public Account findAccountByIban(String iban) {
        Account returnAccount = accountDao.findAccountByIban(iban);
        return returnAccount;
    }

    public List<Customer> findUsersByIban(String iban) {
        Account tempAccount = accountDao.findAccountByIban(iban);
        return customerDao.findCustomersByAccounts(tempAccount);
    }

    public boolean doReadyTransaction(Transaction transaction) {
        boolean readyTransactionSucces = false;
        try {
            //Current balance including check value
            BigDecimal inBalance = accountDao.findAccountByIban(transaction.getInAccount().getIban()).getBalance();
            BigDecimal outBalance = accountDao.findAccountByIban(transaction.getOutAccount().getIban()).getBalance();
            BigDecimal checkvalueBefore = transactionAccountBalanceSum(transaction);

            //Set new balance in the system, generate second checkvalue
            transaction.getInAccount().addBalance(transaction.getAmount());
            transaction.getOutAccount().subtractBalance(transaction.getAmount());
            BigDecimal checkValueAfter = transactionAccountBalanceSum(transaction);

            //Only continu if the checkvalues are the same.
            if (checkValueAfter.equals(checkvalueBefore)) {
                executeTransaction(transaction);
                readyTransactionSucces = true;
            } else {
                feedback = "We kunnen deze overschrijving nu helaas niet uitvoeren.";
                transaction.getInAccount().setBalance(inBalance);
                transaction.getOutAccount().setBalance(outBalance);
                readyTransactionSucces = false;
            }
        } catch (Exception fout) {
            feedback = "Iets tijdens de transactie gaat niet goed";
            System.out.println(fout.getCause());
            readyTransactionSucces = false;
        }
        return readyTransactionSucces;
    }

    private void executeTransaction(Transaction transaction) {
        transaction.setDate(formatLocalDateTransaction());
        transaction.setType("online bankieren");
        transactionDao.save(transaction);
        accountDao.save(transaction.getOutAccount());
        accountDao.save(transaction.getInAccount());
    }

    public String formatLocalDateTransaction() {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("kk-mm-dd-MM-yyyy");
        return LocalDateTime.now().format(formatter);
    }

    private BigDecimal transactionAccountBalanceSum (Transaction transaction) {
        BigDecimal inBalance = accountDao.findAccountByIban(transaction.getInAccount().getIban()).getBalance();
        BigDecimal outBalance = accountDao.findAccountByIban(transaction.getOutAccount().getIban()).getBalance();
        return inBalance.add(outBalance);
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void addAttributesToPage(Model model, Transaction transaction, HttpSession session) {
        model.addAttribute("header", userService.fillHeader());
        model.addAttribute("allLinks", userService.navigationLinks());
        User user = (User)session.getAttribute("currentUser");
        model.addAttribute("name", user.getFirstName());
        model.addAttribute("feedback", validator.getFeedback().concat(transactionScreensService.feedback));

        if (transaction != null) {
            model.addAttribute("outAccount", transaction.getOutAccount().getIban());
            model.addAttribute("amount", transaction.getAmount());
            model.addAttribute("nameReceiver", transaction.getNameReceiver());
            model.addAttribute("balance", transaction.getOutAccount().getBalanceAsStringWithTwoDecimals());
            model.addAttribute("description", transaction.getDescription());

            if (transaction instanceof TransactionReceiverExternal) {
                model.addAttribute("inAccountdisplay", ((TransactionReceiverExternal) transaction).getIbanExternal());
            } else {
                model.addAttribute("inAccountdisplay", transaction.getInAccount().getIban());
            }
        }
    }
}
