package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionDao extends CrudRepository<Transaction, Integer> {
    Transaction getByTransactionId(int id);
}
