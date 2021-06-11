package nl.beehive.beehive.controller;

import nl.beehive.beehive.service.ChiefService;
import nl.beehive.beehive.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChiefBusinessController {

    private final ChiefService chiefService;

    private final UserService userService;

    public ChiefBusinessController(ChiefService chiefService, UserService userService) {
        this.chiefService = chiefService;
        this.userService = userService;
    }

    @GetMapping("chiefBusiness")
    public String EmployeeBusinessDashboardHandler(Model model) {
        model.addAttribute("topAccounts", chiefService.getTop10Klanten("BUSINESS_CH"));
        model.addAttribute("gemiddeldeSaldo", chiefService.getGemideldeSaldoPerSector());
        model.addAttribute("topTransactieVolume", chiefService.getTopRekeningenVolume());

        model.addAttribute("header", userService.fillHeader());
        model.addAttribute("allLinks", userService.navigationLinks());
        return "chiefBusiness";
    }
}
