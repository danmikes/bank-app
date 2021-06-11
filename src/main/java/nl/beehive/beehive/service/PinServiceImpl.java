package nl.beehive.beehive.service;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Transaction;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.TransactionDao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PinServiceImpl implements PinService {

    private final AccountDao accountDao;
    private final TransactionScreensService service;
    private final TransactionDao transactionDao;

    public PinServiceImpl(AccountDao accountDao, TransactionScreensService service, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.service = service;
        this.transactionDao = transactionDao;
    }

    @Override
    public boolean verifyPincode(String iban, int pin) {
        Account account = accountDao.findAccountByIban(iban);

        return account.getPincode() == pin;
    }

    private boolean verifyTransaction(String iban, double bedrag) {
        Account account = accountDao.findAccountByIban(iban);

        System.out.println(account.getBalance());
        return account.getBalance().doubleValue() >= bedrag;
    }

    private Transaction prepareTransaction(String ibanWinkelier, String ibanKlant, double bedrag) {
        Transaction transaction = new Transaction();

        transaction.setInAccount(accountDao.findAccountByIban(ibanWinkelier));
        transaction.setOutAccount(accountDao.findAccountByIban(ibanKlant));
        transaction.setAmount(BigDecimal.valueOf(bedrag));
        transaction.setType("pin transactie");
        transaction.setDate(service.formatLocalDateTransaction());

        return  transaction;
    }

    private boolean resolveBalances(Transaction transaction) {
        Account inAccount = transaction.getInAccount();
        Account outAccount = transaction.getOutAccount();
        BigDecimal amount = transaction.getAmount();

        inAccount.setBalance(inAccount.getBalance().add(amount));
        outAccount.setBalance(outAccount.getBalance().subtract(amount));

        accountDao.save(inAccount);
        accountDao.save(outAccount);

        return true;
    }

    @Override
    public boolean executeTransaction(String ibanWinkelier, String ibanKlant, double bedrag) {

        if (verifyTransaction(ibanKlant, bedrag)) {
           Transaction transaction = prepareTransaction(ibanWinkelier, ibanKlant, bedrag);

           resolveBalances(transaction);

           transactionDao.save(transaction);

           return true;
        } else return false;

    }


}
