package nl.beehive.beehive.service;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.UserDao;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class Validator {

    private final UserDao userDao;
    private final AccountDao accountDao;

    public Validator(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    private String feedback;

    private boolean validateBsn(int bsn) {
        String bsnString = String.valueOf(bsn);
        int checkNumber = 0;
        boolean passedTest;
        // vervangen door enum
        int varConstant = 9;
        //  Controleer of number de juiste lengte heeft
        if (bsnString.length() != varConstant) {
            return false;
        }
        // first part eleven check
        // (9 × A) + (8 × B) + (7 × C) + (6 × D) + (5 × E) + (4 × F) + (3 × G) + (2 × H) + (-1 × I)
        for (int i = 0; i < 8; i++) {
            checkNumber += Character.getNumericValue(bsnString.charAt(i)) * varConstant--;
        }
        checkNumber -= Character.getNumericValue(bsnString.charAt(8));
        // do eleven check
        return checkNumber % 11 == 0;
    }

    private boolean validateIban(String iban) {
        char[] sortedString = new char[iban.length()];
        if (iban.length() < 18) {
            return false;
        }
        String unicodeIBAN;
        int resultMod97;
        // check whether letters are uppercase iban has uppercase
        for (int i = 0; i < iban.length(); i++) {
            if (Character.isLetter(iban.charAt(i))) {
                if (!Character.isUpperCase(iban.charAt(i))) {
                    return false;
                }
            }
        }
        // put country code and check code at end
        iban.getChars(4, iban.length(), sortedString, 0);
        iban.getChars(0, 4, sortedString, iban.length() - 4);
        // convert iban to unicode
        unicodeIBAN = charToUnicodeValue(new String(sortedString));
        // perform mod 97 check
        resultMod97 = new BigInteger(unicodeIBAN).mod(new BigInteger("97")).intValue();
        return resultMod97 == 1;
    }

    // Waarom is dit vereist? (Daniel)
    private String charToUnicodeValue(String code) {
        // String with number and character (i.e.) IBAN need to be transformed into unicode int values
        String convertedString = "";
        char aChar;
        for (int i = 0; i < code.length(); i++) {
            aChar = code.charAt(i);
            convertedString += Character.getNumericValue(aChar);
        }
        return convertedString;
    }

    // Check user input on openAccount form and provide relevant feedback
    public boolean checkAccountInput(Customer customer) {
        boolean testInput = true;
        feedback = "Wij vonden de volgende problemen met de opgegeven invoer: ";
        if (!validateBsn(customer.getBsn())) {
            this.feedback = feedback.concat("Bsn moet bestaan;");
            testInput = false;
        }
        if (isUsername(customer)) {
            this.feedback = feedback.concat("Deze gebruikersnaam bestaat al, login of kies andere gebruikersnaam;");
            testInput = false;
        }
        if (testInput) {
            feedback = " ";
        }
        return testInput;
    }


    private boolean isUsername(User user) {
        return userDao.findByUsername(user.getUsername()) != null;
    }

    //Check the input the user has given for a transaction and give appropriate feedback if incorrect
    public boolean checkTransactionInput(Transaction transaction) {
        feedback = "We vonden de volgende problemen met de gegeven input: ";
        boolean doIbanChecks = doIbanChecks(transaction);
        boolean ibanExists = true;
        if (!(transaction instanceof TransactionReceiverExternal || transaction instanceof TransactionSenderExternal)) {
            ibanExists = (accountDao.findAccountByIban(transaction.getInAcountIban()) != null);
        }
        if (doIbanChecks && ibanExists) {
            feedback = "";
        }
        return doIbanChecks && ibanExists;
    }

    private boolean doIbanChecks( Transaction transaction) {
        boolean ibanCorrect = true;
        String inAccountIban = transaction.getInAccount().getIban();
        String outAccountIban = transaction.getOutAccount().getIban();
        if (transaction instanceof TransactionReceiverExternal){
            inAccountIban = ((TransactionReceiverExternal) transaction).getIbanExternal();
        }
        if (!validateIban(inAccountIban)) {
            this.feedback = feedback.concat("De Iban van de ontvanger is niet juist geformatteerd;");
            ibanCorrect = false;
        }
        if (inAccountIban.equals(outAccountIban)) {
            this.feedback = feedback.concat("Je kunt geen geld overmaken naar dezelfde rekening");
            ibanCorrect = false;
        }
        return ibanCorrect;
    }

    //Comparing the names of the account holder to the registered names. PR
    public boolean checkNameReceiver(Transaction transaction) {
        String nameInHolderList = transaction.getInAccountHolders();
        boolean nameCheck = nameInHolderList.contains(transaction.getNameReceiver());
        if (!nameCheck) {
            this.feedback = feedback.concat("\r\n De naam van de ontvanger komt niet overeen; \r\n");
            if (nameInHolderList != "") {
                this.feedback = feedback.concat("volgens onze gegevens: " + nameInHolderList + ";");
            }
        }
        return nameCheck;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean checkPincode(Account account, int pincode) {
        if (account.getPincode() != pincode) {
            feedback = "Je ingegeven pincode is niet juist.";
        }
        return account.getPincode() == pincode;
    }

    public boolean checkBalance(Account outAccount, BigDecimal amount) {
        boolean balanceSufficient = true;
        if (outAccount.getBalance().compareTo(amount) < 0) {
            feedback = feedback.concat("Je hebt niet genoeg geld op je rekening voor deze overschrijving");
            balanceSufficient = false;
        }
        return balanceSufficient;
    }

}
