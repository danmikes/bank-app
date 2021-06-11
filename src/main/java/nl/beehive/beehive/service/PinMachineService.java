package nl.beehive.beehive.service;

import com.google.gson.Gson;
import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.PinMachine;
import nl.beehive.beehive.model.Transaction;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.PinMachineDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PinMachineService {
    @Autowired
    PinMachineDao pinMachineDao;

    @Autowired
    AccountDao accountDao;

    public PinMachineService() {
        super();
    }
    public String serialize(Transaction transaction) {
        Gson gson = new Gson();
        String json = gson.toJson(transaction);
        return json;
    }
    public Transaction deserialize(String json) {
        Gson gson = new Gson();
        Transaction transaction = gson.fromJson(json, Transaction.class);
        return transaction;
    }
    public String serializePinMachine(PinMachine pinMachine){
        Gson gson = new Gson();
        String json = gson.toJson(pinMachine);
        return json;
    }
    public String procesActivation(PinMachine pinMachine){
        PinMachine localPinMachine=null;
        Account pinAccount =accountDao.findAccountByIban(pinMachine.getPinAccount().getIban());
        // controleer of machinee reeds geactiveerd is
        if (pinMachineDao.existsPinMachineByPinAccount(pinAccount)){
            localPinMachine=pinMachineDao.getPinMachineByPinAccount(pinAccount);
            System.out.println("locale pinmachine "+localPinMachine.getPinAccount().accountId);
            //als de activatie in orde is genereer validation code
            if(pinMachine.getActivationCode()==localPinMachine.getActivationCode()){
                localPinMachine.generateValidationCode();
                pinMachineDao.save(localPinMachine);
                pinMachine.setValidationCode(localPinMachine.getValidationCode());
            } else {
             // no valid activation code entered
             pinMachine.setValidationCode(-2);
            }
        } else {
            // no activation code generated
            pinMachine.setValidationCode(-1);
        }
        return serializePinMachine(pinMachine);
    }
}
