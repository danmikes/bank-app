package nl.beehive.beehive.model;

import nl.beehive.beehive.service.RoleUser;

import javax.persistence.Entity;

@Entity
public class Employee extends User {

  public Employee() {
    this("", "");
  }

  public Employee(String username, String password) {
    this(username, password, "");
  }

  public Employee(String username, String password, String firstName){
    super(username, password, firstName);
  }
}
