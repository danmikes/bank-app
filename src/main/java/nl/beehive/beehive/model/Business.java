package nl.beehive.beehive.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Business extends Account {

  @ManyToOne(cascade = CascadeType.ALL)
  private Company company;

  @ManyToOne(cascade = CascadeType.ALL)
  private AccountManager accountManager;

  public Business() {
    super();
    this.company = new Company();
    this.accountManager = new AccountManager();
  }

  public Business(Account account, Company company, AccountManager accountManager) {
    super(account.getIban(), account.getType(), account.getBalance(), account.getOpeningsDate());
    this.company = company;
    this.accountManager = accountManager;
  }

  public AccountManager getAccountManager() {
    return accountManager;
  }

  public void setAccountManager(AccountManager accountManager) {
    this.accountManager = accountManager;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  @Override
  public String displayAccountHolders() {
    return company.getCompanyName();
  }
}
