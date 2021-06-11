package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.Transaction;
import nl.beehive.beehive.service.TransactionScreensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class TransactionConfirmController {

    Transaction transaction;

    @Autowired
    TransactionScreensService transactionScreensService;

    //Filling the transactionConfirmScreen
    @RequestMapping(value = "transactionConfirm", method = RequestMethod.GET)
    public String transactionConfirm(@ModelAttribute("transaction") Transaction transaction,
                                     HttpSession session,
                                     Model model) {
        this.transaction = transaction;
        addAttributesToPage(model, session);
        return "transactionConfirm"; //Initiate the transactionConfirmpage
    }

    //Fills the screen with info.
        @ModelAttribute
        public void addAttributesToPage(Model model, HttpSession session) {
            transactionScreensService.addAttributesToPage(model, transaction, session);
        }
    }

