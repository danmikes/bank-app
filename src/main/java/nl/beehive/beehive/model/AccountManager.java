package nl.beehive.beehive.model;

import nl.beehive.beehive.service.RoleUser;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountManager extends User {

  @OneToMany(mappedBy = "accountManager")
  private List<Business> businesses;


  public AccountManager() {
    this("","", "");
  }

  public AccountManager(String firstName) {
    this("", "",firstName);
  }

  public AccountManager(String username, String password, String firstName) {
    super(username, password, firstName);
    this.businesses = new ArrayList<>();
  }

  public AccountManager(List<Business> businesses) {
    super();
    this.businesses = businesses;
  }

  public List<Business> getBusinesses() {
    return businesses;
  }

  public void setBusinesses(List<Business> businesses) {
    this.businesses = businesses;
  }

  public void addBusiness(Business business) {
    businesses.add(business);
  }
}
