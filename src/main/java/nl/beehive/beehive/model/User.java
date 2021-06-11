package nl.beehive.beehive.model;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private int userId;

  @NotNull
  private String username;
  @NotNull
  private String password;
  private String firstName;
  private String lastName;
  private String email;
  private String telephone;

  public User() {
    this("","","","","","");
  }

  public User(String username) {this(username, "");}

  //Constructor voor login medewerker
  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  //Constructor voor login medewerker
  public User(String username, String password, String firstName) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
  }

  //Constructor voor nieuwe user
  public User(String username, String password, String firstName, String lastName, String email, String telephone) {
    super();
    //userId = 0;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.telephone = telephone;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String name) {
    this.username = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
