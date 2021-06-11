package nl.beehive.beehive.model;

import javax.persistence.Entity;

@Entity
public class TransactionSenderExternal extends Transaction {
    private String nameSender;
    private String ibanExternal;

    public TransactionSenderExternal() {
        this(new Transaction());
    }
    public TransactionSenderExternal(Transaction transaction){
        this(transaction, "Onbekend", "");
    }
    public TransactionSenderExternal(Transaction transaction, String nameSender, String ibanExternal){
        super(transaction.getInAccount(), transaction.getOutAccount(), transaction.getAmount(), transaction.getDescription(), transaction.getDate(), transaction.getType());
        this.nameSender = nameSender;
        this.ibanExternal = ibanExternal;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getIbanExternal() {
        return ibanExternal;
    }

    public void setIbanExternal(String ibanExternal) {
        this.ibanExternal = ibanExternal;
    }

    @Override
    public String getOutAccountIban() {
        return ibanExternal;
    }

    @Override
    public String getOutAccountHolders() {
        return nameSender;
    }
}
