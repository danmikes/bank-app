package nl.beehive.beehive.service;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Transaction;
import nl.beehive.beehive.model.TransactionSenderExternal;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionHistoryService {

    private final TransactionDao transactionDao;

    public TransactionHistoryService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    //Methode om een lijst met alle transacties te krijgen bij een rekening
    public List<String> getTransactions(Account account) {

        //Lege lijst voor de resultaten
        List<String> history = new ArrayList<>();

        //Lijst met alle in en uit transacties van een rekening
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(account.getOutTransactions());
        transactions.addAll(account.getInTransactions());

        if (transactions.isEmpty()) {
            history.add(", Nog geen transacties op deze rekening, , ");
            return history;
        }

        //Sorteer de lijst op datum
        transactions = sortTransactionsByDate(transactions);
        if (transactions.size() > 10) {
            transactions = transactions.subList(0,10);
        }

        //Format voor de resultaten voor in/uit transacties
        transactions.forEach(transaction -> history.add(transaction.getInAccount().accountId == account.accountId ?
                transaction.getDate().substring(6) + "," +  transaction.getOutAccountHolders() + ", € " + transaction.getAmount() + "," + transaction.getTransactionId():
                transaction.getDate().substring(6) + "," + transaction.getInAccountHolders()  + ", € " + transaction.getAmount() + "," + transaction.getTransactionId()));

        return history;
    }

    //Methode om een lijst met transacties op datum te sorteren
    private List<Transaction> sortTransactionsByDate(List<Transaction> transactions) {

        //'Ingewikkelde' lambda-functie die eerst het jaar vergelijkt, daarna de maand, dan de dag en vervolgens de uren en minuten om te sorteren

        transactions.sort((t1, t2) ->
                split(t2, 4) - split(t1, 4) == 0 ?
                        split(t2, 3) - split(t1, 3) == 0 ?
                                split(t2, 2) - split(t1, 2) == 0 ?
                                        split(t2, 0) - split(t1, 0) == 0 ?
                                                split(t2, 1) - split(t1, 1) :
                                                split(t2, 0) - split(t1, 0) :
                                        split(t2, 2) - split(t1, 2) :
                                split(t2, 3) - split(t1, 3) :
                        split(t2, 4) - split(t1, 4));

        return transactions;
    }

    public Transaction getTransaction(int transactionId) {
        return transactionDao.getByTransactionId(transactionId) instanceof TransactionSenderExternal ? null : transactionDao.getByTransactionId(transactionId);
    }

    public String transactionInfo(Transaction transaction, int accountId) {
        if (transaction == null) {
            return "Deze transactie heeft geen extra informatie,";
        } else if (transaction.getInAccount().getAccountId() == accountId) {
            return "Bijgeschreven vanaf rekening:," + transaction.getOutAccount().getIban() + "," + transaction.getOutAccount().displayAccountHolders();
        } return "Afgeschreven naar rekening:," + transaction.getInAccount().getIban() + "," + transaction.getInAccount().displayAccountHolders();
    }

    //Hulp methode om de datum te sorteren, doormiddel van de cijfers te lezen uit de datum string
    private int split(Transaction transaction, int index) {
        return Integer.parseInt(transaction.getDate().split("-")[index]);
    }
}