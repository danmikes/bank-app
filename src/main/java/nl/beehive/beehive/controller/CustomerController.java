package nl.beehive.beehive.controller;

import nl.beehive.beehive.service.CustomerService;
import nl.beehive.beehive.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    private final UserService userService;

    public CustomerController(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping(value = "customerPage")
    public String customerPage(Model model) {
        model.addAttribute("allAccounts", customerService.getAccountListByCustomer());
        model.addAttribute("header", userService.fillHeader());
        model.addAttribute("allLinks", userService.navigationLinks());
        return "customerPage";
    }
}