package nl.beehive.beehive.controller;

import nl.beehive.beehive.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationCompleteController {

    private final RegistrationService registrationService;


    public RegistrationCompleteController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping(value = "accountOpened")
    public String accountOpened(Model model) {
        model.addAttribute("textComplete", registrationService.registrationText());

        return "accountOpened";
    }
}
