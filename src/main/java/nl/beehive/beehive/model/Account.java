package nl.beehive.beehive.model;

import nl.beehive.beehive.service.AccountType;
import nl.beehive.beehive.service.RoleUser;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

@Entity
@Table(name = "account")

public class Account {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  public int accountId;

  @Column(unique = true)
  private String iban;

  private String type;
  private BigDecimal balance;
  private String openingsDate;
  private int pincode;

  @ManyToMany
  private List<Customer> holders;

  //@OneToMany(fetch = FetchType.EAGER, mappedBy = "inAccount")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "inAccount")
  private List<Transaction> inTransactions;

  //@OneToMany(fetch = FetchType.EAGER, mappedBy = "outAccount")
  @LazyCollection(LazyCollectionOption.FALSE)
  @OneToMany(mappedBy = "outAccount")
  private List<Transaction> outTransactions;

  @OneToOne(optional = true, mappedBy = "pinAccount")
  private PinMachine pinMachine;

  public Account() {
    this("", "", new BigDecimal(0.00), "");
  }

 public Account(String iban, String type, BigDecimal balance, String openingsDate) {
    this(iban, type, balance, openingsDate,1234);
 }

 public Account(String iban, String type, BigDecimal balance, String openingsDate, int pincode) {
    accountId = 0;
    this.iban = iban;
    this.type = type;
    this.balance = balance;
    this.openingsDate = openingsDate;
    this.pincode = pincode;

    holders = new ArrayList<>();
    inTransactions = new ArrayList<>();
    outTransactions = new ArrayList<>();
  }
  public void addBalance(BigDecimal amount){this.balance= this.balance.add(amount);}

  public void subtractBalance(BigDecimal amount){this.balance=this.balance.subtract(amount);}


  public void addInTransaction(Transaction inTransaction) {
      inTransactions.add(inTransaction);
  }

  public void addOutTransaction(Transaction outTransaction) {
      outTransactions.add(outTransaction);
  }

  public void addHolder(Customer customer) { holders.add(customer); }

  public int getAccountId() {
    return accountId;
  }

  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getType() {
      return type;
  }

  public String getStringType() {
      return AccountType.valueOf(type).label;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public String getOpeningsDate() {
    return openingsDate;
  }

  public void setOpeningsDate(String openingsDate) {
    this.openingsDate = openingsDate;
  }

  public List<Customer> getHolders() {
    return holders;
  }

  public void setHolders(List<Customer> holders) {
    this.holders = holders;
  }

  public List<Transaction> getInTransactions() {
    return inTransactions;
  }

  public void setInTransactions(List<Transaction> inTransactions) {
    this.inTransactions = inTransactions;
  }

  public List<Transaction> getOutTransactions() {
    return outTransactions;
  }

  public void setOutTransactions(List<Transaction> outTransactions) {
    this.outTransactions = outTransactions;
  }

  public int getPincode() {
    return pincode;
  }

  public void setPincode(int pincode) {
    this.pincode = pincode;
  }

  public String getBalanceAsStringWithTwoDecimals () {
      DecimalFormat df = new DecimalFormat("0.00");
      return df.format(balance);
  }

    public String displayAccountHolders() {
        List<Customer> holders=getHolders();
        String stringAccountHolders = "";
        for (Customer customer : holders
        ) {
            stringAccountHolders = stringAccountHolders.concat(customer.getLastName() + "; ");
        }
        return stringAccountHolders;
    }

    public String balanceToString() {
        Locale netherlands = new Locale("nl", "NL");
        NumberFormat dutchMoneyformat = NumberFormat.getCurrencyInstance(netherlands);
        String balanceEuro = dutchMoneyformat.format(balance);
        return balanceEuro;
    }
    public String accountTypeToName(){
        AccountType roles = Enum.valueOf(AccountType.class, this.getType());
        return roles.label;
    }
}
