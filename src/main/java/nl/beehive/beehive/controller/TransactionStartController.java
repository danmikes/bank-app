package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class TransactionStartController {

    @Autowired
    TransactionScreensService transactionScreensService;

    @Autowired
    Validator validator;

    @Autowired
    AccountDao accountDao;

    @Autowired
    CustomerService customerService;

    @Autowired
    UserService userService;

    private Transaction transaction = new Transaction();
    private String feedback;
    private List<Account> accounts = new ArrayList<>();

    @RequestMapping(value = "transactionStart")
    public String transactionStart(Model model, HttpSession session) {
        resetScreen();
        accounts = customerService.getAccountListByCustomer();
        addAttributes(model, session);
        return "transactionStart";
    }

    //Checking the information the user has supplied on the form. Storing it.
    @RequestMapping(value = "/transactionHandler", method = RequestMethod.POST)
    public String transactionHandler(@ModelAttribute("transaction") Transaction transaction,
                                     @RequestParam("accountSelect") int outAccountId,
                                     @RequestParam("inAccount.iban") String inAccountIban,
                                     HttpSession session,
                                     Model model,
                                     RedirectAttributes attributes) {
        //Turning the outAccount Id back into an Account object.
        Account outAccount = transactionScreensService.findAccountByAccountId(outAccountId);
        transaction.setOutAccount(outAccount);

        //Determine the type of transaction internal or partly external, this is necessary to determine what
        //checks can be done.
        transaction = transactionScreensService.determineTypeOfTransaction(transaction);

        //Check user input and set feedback if a problem was found
        boolean inputCorrect = validator.checkTransactionInput(transaction);

        //Continu if the input is correct, otherwise retrieve feedback and reload the screen
        if (inputCorrect) {
            //If the inAccount is our bank, get full information on the account.
            if (!(transaction instanceof TransactionReceiverExternal || transaction instanceof TransactionSenderExternal)) {
                transaction.setInAccount(transactionScreensService.findAccountByIban(inAccountIban));
                //Check the name the user has provided against our database.
                boolean checkname = validator.checkNameReceiver(transaction);
                if (!checkname) {
                    feedback = validator.getFeedback().concat(transactionScreensService.getFeedback());
                    addAttributes(model, session);
                    return "transactionStart"; //Stay on the page if the system finds a problem.
                }
            }
            //If all seems correct, show next screen.
            transaction.setOutAccount(transactionScreensService.findAccountByAccountId(outAccountId));
            attributes.addFlashAttribute("transaction", transaction);
            return "redirect:transactionCheckValidate"; // Go to next page if system finds no issues.
        } else {
            //Load the page again, but with feedback and the current transaction active.
            this.feedback = validator.getFeedback();
            addAttributes(model, session);
            return "transactionStart";//Stay on the page if the system finds a problem.
        }
    }

    //Fills the screen with info.
    @ModelAttribute
    private void addAttributes(Model model,HttpSession session) {
        model.addAttribute("header", userService.fillHeader());
        model.addAttribute("allLinks", userService.navigationLinks());
        model.addAttribute("transaction", transaction);
        model.addAttribute("allAccounts", accounts);
        model.addAttribute("feedbackLabel", feedback);
    }

    //Removes any previous transaction from the model.
    private void resetScreen(){
        this.transaction = new Transaction();
        feedback = " ";
    }
}
