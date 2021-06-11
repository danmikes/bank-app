package nl.beehive.beehive.controller;


import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.AccountManager;
import nl.beehive.beehive.service.AccountManagerService;

import nl.beehive.beehive.service.UserInfoService;
import nl.beehive.beehive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
public class AccountManagerController {
    private final UserService userService;

    private final AccountManagerService accountManagerService;

//    @Autowired
//    AccountManagerService accManSer;

    public AccountManagerController(UserService userService, AccountManagerService accountManagerService) {
        this.userService = userService;
        this.accountManagerService =accountManagerService;
    }

    @GetMapping(value = "accountManager")
    public String accountManagerHandler(Model model) {
        //accManSer.addAttributes(model, session);
        model.addAttribute("header", userService.fillHeader());
        model.addAttribute("allLinks", userService.navigationLinks());
        return "accountManager";
    }

    @PostMapping(value = "check")
    public String verifyPinIban( @ModelAttribute("account") Account account, Model model,HttpSession session) {
        accountManagerService.generatePinActivation(account.getIban(), model,session);
        return "accountManager";
    }

    @GetMapping("accountManagerButton")
    public String accountManagerButton(Model model) {
        return "accountManager";
    }
}

