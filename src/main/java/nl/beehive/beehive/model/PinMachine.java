package nl.beehive.beehive.model;

import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class PinMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pinMachineId;

    @OneToOne
    private Account pinAccount;

    private int activationCode;
    private int validationCode;
    private String activationDate;
    //
    public PinMachine() {
        this(null);
    }

    public PinMachine(Account pinAccount) {
        super();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("kk-mm-dd-MM-yyyy");
        String nu= LocalDateTime.now().format(formatter);
        this.pinMachineId=0;
        this.pinAccount = pinAccount;
        this.activationCode=generateActivationCode();// method autogeneratenumber
        this.activationDate= nu;// timestamp
    }
    //

    public Account getPinAccount() {
        return pinAccount;
    }

    public void setPinAccount(Account pinAccount) {
        this.pinAccount = pinAccount;
    }

    public int getActivationCode() {
        return activationCode;
    }


    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    //
    public boolean isActivated(){
        return this.activationCode!=0;
    }
    public boolean isValidated(){
        return this.validationCode!=0;
    }
    public void generateValidationCode(){
        if(this.validationCode==0){
            this.validationCode= generateCode(8);
        } else {
            this.validationCode = this.validationCode;
        }
    }
    private int generateActivationCode(){
        if(this.activationCode==0){
            return generateCode(5);
        } else {
            return this.activationCode;
        }
    }

    private int generateCode(int length){
        String code;
        int min = 0;
        int max = 9;
        // generate stringnumber
        code = "" + generateNumberOrig(1,max);
        for (int i = 1; i < length ; i++) {
            code += generateNumberOrig(min, max);
        }
        return Integer.parseInt(code);
    }
    public int generateNumberOrig(int min, int max){
        return (int)(Math.random()*(max-min+1))+ min;
        //return (int)(Math.random()*max)+ min;
    }
}
