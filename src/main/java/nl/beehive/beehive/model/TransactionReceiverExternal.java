package nl.beehive.beehive.model;

import javax.persistence.Entity;

@Entity
public class TransactionReceiverExternal extends Transaction {
    String nameReceiver;
    private String ibanExternal;

    public TransactionReceiverExternal() {
        this(new Transaction());
    }
    public TransactionReceiverExternal(Transaction transaction){
        this(transaction, "Onbekend", "");
    }
    public TransactionReceiverExternal(Transaction transaction, String nameReceiver, String ibanExternal){
        super(transaction.getInAccount(), transaction.getOutAccount(), transaction.getAmount(), transaction.getDescription(), transaction.getDate(), transaction.getType());
        this.nameReceiver = nameReceiver;
        this.ibanExternal = ibanExternal;
    }

    @Override
    public String getNameReceiver() {
        return nameReceiver;
    }

    public void setNameReceiver(String nameReceiver) {
        this.nameReceiver = nameReceiver;
    }

    public String getIbanExternal() {
        return ibanExternal;
    }

    public void setIbanExternal(String ibanExternal) {
        this.ibanExternal = ibanExternal;
    }

    @Override
    public String getInAcountIban() {
        return ibanExternal;
    }

    @Override
    public String getInAccountHolders() {
        return nameReceiver;
    }
}
