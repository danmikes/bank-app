package nl.beehive.beehive.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bank {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private int bankId;
  private String bankName;
  private String bankCode;

  public Bank() {
    this("", "");
  }

  public Bank(String bankName, String bankCode) {
    super();
    bankId = 0;
    this.bankName = bankName;
    this.bankCode = bankCode;
  }

  public int getBankId() {
    return bankId;
  }

  public void setBankId(int bankId) {
    this.bankId = bankId;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }
}
