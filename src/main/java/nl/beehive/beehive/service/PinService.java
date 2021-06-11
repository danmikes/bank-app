package nl.beehive.beehive.service;

public interface PinService {

    boolean verifyPincode(String iban, int pin);

    boolean executeTransaction(String ibanWinkelier, String ibanKlant, double bedrag);
}
