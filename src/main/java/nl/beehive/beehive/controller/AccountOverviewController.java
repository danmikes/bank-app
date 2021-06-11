package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Transaction;
import nl.beehive.beehive.service.CustomerService;
import nl.beehive.beehive.service.TransactionHistoryService;
import nl.beehive.beehive.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountOverviewController {

    private final CustomerService customerService;

    private final UserService userService;

    private final TransactionHistoryService transactionHistoryService;

    private int selectedAccount;

    public AccountOverviewController(CustomerService customerService, UserService userService, TransactionHistoryService transactionHistoryService) {
        this.customerService = customerService;
        this.userService = userService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @PostMapping("accountOverview")
    public String accountOverview(@ModelAttribute Account account, @RequestParam int accountId, Model model) {

        //Geef het accountId door aan de volgende pagina op basis van de geselecteerde rekening
        model.addAttribute("accountId", accountId);

        return "redirect:accountOverview";
    }

    @GetMapping("accountOverview")
    public String accountOverview(int id, Model model) {
        Account account = customerService.getAccountById(id);
        selectedAccount = account.getAccountId();
        model.addAttribute("account", account);
        model.addAttribute("transactionHistory", transactionHistoryService.getTransactions(account));
        model.addAttribute("header", userService.fillHeader());
        model.addAttribute("allLinks", userService.navigationLinks());
        return "accountOverview";
    }

    @GetMapping("transactionDetails")
    public String transactionDetails(@RequestParam int id, Model model) {
        Transaction transaction = transactionHistoryService.getTransaction(id);
        model.addAttribute("selectedTransaction", transaction);
        model.addAttribute("transactionInfo", transactionHistoryService.transactionInfo(transaction, selectedAccount));
        return "transactionDetails";
    }
}