package nl.beehive.beehive.service;


import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


@Service
public class AccountManagerService {

    private final BusinessDao businessDao;
    private final PinMachineDao pinMachineDao;
    private final AccountDao accountDao;


    private String feedback;
    private User user;

    public AccountManagerService(AccountDao accountDao, PinMachineDao pinMachineDao,BusinessDao businessDao) {
        this.businessDao = businessDao;
        this.pinMachineDao =pinMachineDao;
        this.accountDao = accountDao;
    }


    @ModelAttribute
    public void generatePinActivation(String string,Model model,HttpSession session){
        Account verifyAccount=existsInDatabase(string,model);
        Business businessChecking=null;
        model.addAttribute("selector", "generated");
        model.addAttribute("buttontext", "Koppel Andere Rekening");
        // bepaal of iban in de database voorkomt
        if(verifyAccount==null){ return;}
        // bepaal of het geselecteerde  een van het type bussiness checking is
        businessChecking=isBusinessChecking(verifyAccount,model);
        if (businessChecking==null){return;}
        //check of geactiveerd of gekoppeld anders genereer koppeling
        createPinMachineCoupling(verifyAccount,model);
        // voeg attributen toe
        model.addAttribute("accountdetails", "Pin machine: " + verifyAccount.getIban()+ " voor "+
                businessChecking.getCompany().getCompanyName() );
    }

    private Account existsInDatabase(String string, Model model){
        Account verifyAccount=null;
        if (accountDao.existsAccountByIban(string)){
            verifyAccount= accountDao.findAccountByIban(string);
        } else {
            // iban
            model.addAttribute("accountdetails", "Rekening " + string
                    + " bestaat niet");
        }
        return verifyAccount;
    }

    private Business isBusinessChecking(Account verifyAccount,Model model){
        Business businessChecking=null;
        if (!verifyAccount.getType().equals("BUSINESS_CH")){
            //model.addAttribute("selector", "generated");
            model.addAttribute("accountdetails", "Rekening " +verifyAccount.getIban()
                    + " is een "+ verifyAccount.accountTypeToName() );
            model.addAttribute("message", "Een pin automaat kan niet worden geactiveerd");
        } else {
            //creerrekening
            businessChecking = businessDao.findByAccountId(verifyAccount.getAccountId());
        }
        return businessChecking;
    }

    private void createPinMachineCoupling(Account verifyAccount,Model model){
        PinMachine existingPinMachine;
        // check of er al een pinMachine bestaat
        if (pinMachineDao.existsPinMachineByPinAccount(verifyAccount)){
            existingPinMachine=pinMachineDao.getPinMachineByPinAccount(verifyAccount);
            // bepaal of machine al geactiveerd en gekoppeld is.
            if (existingPinMachine.isValidated()){
                model.addAttribute("message", "Rekening is reeds gekoppeld");
            }
            else{
                String[] str =  existingPinMachine.getActivationDate().split("-");
                model.addAttribute("message", "validatie vereist");
                model.addAttribute("info","koppelcode: " +existingPinMachine.getActivationCode()
                        + " gemaakt " + str[0] + ":" + str[1] + " op " +str[2]+"-"+str[3]+"-"+str[4]);
            }
            // creeer nieuwe pinmachine koppeling
        } else {
            PinMachine pinMachine = new PinMachine(verifyAccount);
            pinMachineDao.save(pinMachine);
            model.addAttribute("message", "activatie code: " + pinMachine.getActivationCode());
        }
    }
}
