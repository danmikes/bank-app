package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.Customer;
import nl.beehive.beehive.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class CustomerRegistrationController {

    private final Validator validator;
    private final UserService userService;
    private final RegistrationService registrationService;

    public CustomerRegistrationController(Validator validator, UserService userService, RegistrationService registrationService) {
        this.validator = validator;
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping("openAccount")
    public String newAccountRegistration(Model model) {
        model.addAttribute("customer", new Customer());
        return "openAccount";
    }

    @PostMapping(value = "save")
    public String saveNewAccount(@ModelAttribute("customer") Customer customer,
                                 @RequestParam("TypeAccount") String typeAccount,
                                 Model model) {

        //De gebruiker 'inloggen'
        if (validator.checkAccountInput(customer)) {

            userService.setUser(customer);

            if (typeAccount.equals(AccountType.RETAIL_CHECKING.toString())) {
                //Data klaarzetten voor het succes scherm
                registrationService.saveNewRetailAccount();
                return "redirect:accountOpened";
            } else if (typeAccount.equals(AccountType.BUSINESS_CH.toString())) {

                return "redirect:openAccountBusiness";
            }
        }

        model.addAttribute("feedback", validator.getFeedback());
        return "openAccount";
    }

    @RequestMapping(value = "addAccount")
    public String addAccount() {
        return "addAccount";
    }

    @RequestMapping(value = "/addAccountHandler")
    public String addAccountHandler(@RequestParam("TypeAccount") String typeAccount) {
        if (typeAccount.equals(AccountType.RETAIL_CHECKING.toString())) {
            registrationService.saveNewRetailAccount();
            return "redirect:accountOpened";
        } else {
            return "redirect:openAccountBusiness";
        }
    }
}
