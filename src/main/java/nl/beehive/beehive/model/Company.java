package nl.beehive.beehive.model;

import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;

@Entity
public class Company {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private int companyId;
  private int kvk;
  private String companyName;
  private String sector;

  public Company() {
    this(0, "", "");
  }

  public Company(int kvk, String companyName, String sector) {
    super();
    companyId = 0;
    this.kvk = kvk;
    this.companyName = companyName;
    this.sector = sector;
  }

  public int getCompanyId() {
    return companyId;
  }

  public void setCompanyId(int companyId) {
    this.companyId = companyId;
  }

  public int getKvk() {
    return kvk;
  }

  public void setKvk(int kvk) {
    this.kvk = kvk;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getSector() {
    return sector;
  }

  public void setSector(String sector) {
    this.sector = sector;
  }
}
