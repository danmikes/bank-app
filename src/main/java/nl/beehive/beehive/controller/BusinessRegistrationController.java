package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BusinessRegistrationController {

    private final UserService userService;
    private final RegistrationService registrationService;

    public BusinessRegistrationController(UserService userService, RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping(value = "openAccountBusiness")
    public String newAccountRegistrationBusiness(Model model) {
        model.addAttribute("naam", userService.getUser().getFirstName());
        model.addAttribute("sectoren", BusinessSector.values());
        return "openAccountBusiness";
    }

    @PostMapping(value = "saveCompany")
    public String saveNewCompany(@ModelAttribute Company company) {
        registrationService.saveNewBusinessAccount(company);
        return "redirect:accountOpened";
    }
}