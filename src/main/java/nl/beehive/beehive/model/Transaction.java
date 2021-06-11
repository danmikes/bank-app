package nl.beehive.beehive.model;

import javax.persistence.*;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@Entity
@Table(name = "transaction")
@SessionAttributes
public class Transaction {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private int transactionId;

  @ManyToOne
  private Account inAccount;

  @ManyToOne
  private Account outAccount;

  private BigDecimal amount;
  private String description;
  private String date;
  private String type;
  private String nameReceiver;

  public Transaction() {
    this(new Account(), new Account(), new BigDecimal(0.00),"","","");
  }

  // date format: KK-MM-DD-MM-YY (hour - minute - day - month - year)
  public Transaction(Account inAccount, Account outAccount, BigDecimal amount, String description, String date, String type) {
    super();
    transactionId = 0;
    this.inAccount = inAccount;
    this.outAccount = outAccount;
    this.amount = amount;
    this.description = description;
    this.date = date;
    this.type = type;
  }

  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
  }

  public Account getInAccount() {
    return inAccount;
  }

  public Account getOutAccount() {
    return outAccount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setInAccount(Account inAccount) {
    this.inAccount = inAccount;
  }

  public void setOutAccount(Account outAccount) {
    this.outAccount = outAccount;
  }

  public String getNameReceiver() {
    return nameReceiver;
  }

  public void setNameReceiver(String nameReceiver) {
    this.nameReceiver = nameReceiver;
  }

  public String getOutAccountIban(){
    return outAccount.getIban();
  }

  public String getInAcountIban(){
    return inAccount.getIban();
  }

  public String getInAccountHolders(){
    return inAccount.displayAccountHolders();
  }

  public String getOutAccountHolders(){
    return outAccount.displayAccountHolders();
  }

  public String amountToString() {
    Locale netherlands = new Locale("nl", "NL");
    NumberFormat dutchMoneyformat = NumberFormat.getCurrencyInstance(netherlands);
    String amountEuro = dutchMoneyformat.format(amount);
    return amountEuro;
  }

}
