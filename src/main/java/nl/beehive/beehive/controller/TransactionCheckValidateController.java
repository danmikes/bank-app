package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Transaction;
import nl.beehive.beehive.model.TransactionReceiverExternal;
import nl.beehive.beehive.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class TransactionCheckValidateController {

    @Autowired
    Validator validator;

    @Autowired
    TransactionScreensService transactionScreensService;

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    private Transaction transaction;
    private String feedback;


    //Filling the transactionCheckValidatescreen
    @RequestMapping(value = "transactionCheckValidate")
    public String transactionCheckValidate(
            Transaction transaction,
            HttpSession session,
            Model model) {
        this.transaction = transaction;
        rememberTransaction(transaction);
        addAttributesToPage(model, session);
        return "transactionCheckValidate"; //Initiate the transactionCheckValidatepage
    }

    //Button for the user to return to the previous page and adjust the transaction
    @RequestMapping(method = RequestMethod.GET, value = "returnButtonHandler")
    public String returnButtonHandler(Model model, HttpSession session) {
        //Explicitly return info to the previous screen. Do not remove.
        model.addAttribute("header", userService.fillHeader());
        model.addAttribute("allLinks", userService.navigationLinks());
        model.addAttribute("transaction", transaction);
        model.addAttribute("allAccounts", customerService.getAccountListByCustomer());
        model.addAttribute("feedbackLabel", "");
        if (transaction instanceof TransactionReceiverExternal) {
            transaction.getInAccount().setIban(((TransactionReceiverExternal) transaction).getIbanExternal());
        }
        return "transactionStart";
    }

    //The system checks the pincode and if it is right moves on to the next screen
    @RequestMapping(method = RequestMethod.POST, value = "pincodeCheckButtonHandler")
    public String pincodeCheckButtonHandler(@RequestParam("pincode") int pincode,
                                            Model model,
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) {
        //Compare the pincode with the pincode in the system, give feedback if incorrect
        boolean inputPincodecorrect = validator.checkPincode(transaction.getOutAccount(), pincode);
        if (!inputPincodecorrect) {
            feedback = validator.getFeedback();
            model.addAttribute("transaction", transaction);
            addAttributesToPage(model, session);
            return "transactionCheckValidate";
        }
        //See if er is enough money in the account before starting the transfer
        boolean checkBalance = validator.checkBalance(transaction.getOutAccount(), transaction.getAmount());
        if (!checkBalance) {
            feedback = validator.getFeedback();
            addAttributesToPage(model, session);
            return "transactionCheckValidate";
        }
        //Preparing and executing the transfer, catching any errors
        boolean transactionReady = transactionScreensService.doReadyTransaction(transaction);
        if (!transactionReady) {
            feedback = transactionScreensService.getFeedback();
            addAttributesToPage(model, session);
            return "transactionCheckValidate";
        }
        //If all checks are confirmed, send the information to next Controller.
        redirectAttributes.addFlashAttribute("transaction", transaction);
        return "redirect:transactionConfirm";
    }

    //Fills the screen with info.
    @ModelAttribute
    public void addAttributesToPage(Model model, HttpSession session) {
        transactionScreensService.addAttributesToPage(model, transaction, session);
    }

    @ModelAttribute("transaction")
    public Transaction rememberTransaction(Transaction transaction) {
        return transaction;
    }
}
