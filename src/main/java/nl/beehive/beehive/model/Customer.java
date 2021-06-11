package nl.beehive.beehive.model;

import nl.beehive.beehive.service.RoleUser;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Customer extends User {

  @NotNull
  private int bsn;

  //private char gender;
  private String birthDate;
  private String street;
  private int houseNumber;
  private String houseSuffix;
  private String zipCode;
  private String city;
  private String country;

  @ManyToMany(mappedBy = "holders", fetch = FetchType.EAGER)
  private List<Account> accounts;

  public Customer() {
    this("", "","" , "", 0, "", "", 0, "", "", "", "");
  }

  public Customer(String firstName, String lastName, String email, String telephone, int bsn, String birthDate, String street, int houseNumber, String houseSuffix, String zipCode, String city, String country) {
    this("","", firstName, lastName, email, telephone, bsn, birthDate, street, houseNumber, houseSuffix, zipCode, city, country);
   }
  public Customer(String username, String password, String firstName, String lastName, String email, String telephone, int bsn, String birthDate, String street, int houseNumber, String houseSuffix, String zipCode, String city, String country) {
    super(username, password, firstName, lastName, email, telephone);
    this.bsn = bsn;
    this.birthDate = birthDate;
    this.street = street;
    this.houseNumber = houseNumber;
    this.houseSuffix = houseSuffix;
    this.zipCode = zipCode;
    this.city = city;
    this.country = country;
    this.accounts = new ArrayList<>();
  }

  public void addAccount(Account account) {
      accounts.add(account);
  }

  public int getBsn() {
    return bsn;
  }

  public void setBsn(int bsn) {
    this.bsn = bsn;
  }

  /*public char getGender() {
    return gender;
  }

  public void setGender(char gender) {
    this.gender = gender;
  }*/

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public List<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public int getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(int houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getHouseSuffix() {
    return houseSuffix;
  }

  public void setHouseSuffix(String houseSuffix) {
    this.houseSuffix = houseSuffix;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}