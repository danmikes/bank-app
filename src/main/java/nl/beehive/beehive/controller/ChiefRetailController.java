package nl.beehive.beehive.controller;

import nl.beehive.beehive.service.ChiefService;
import nl.beehive.beehive.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChiefRetailController {

  private final UserService userService;

  private final ChiefService chiefService;

  public ChiefRetailController(UserService userService, ChiefService chiefService) {
    this.userService = userService;
    this.chiefService = chiefService;
  }

  @GetMapping(value = "chiefRetail")
  public String chiefRetailHandler(Model model) {
    model.addAttribute("topAccounts", chiefService.getTop10Klanten("RETAIL_CHECKING"));
    model.addAttribute("header", userService.fillHeader());
    model.addAttribute("allLinks", userService.navigationLinks());
    return "chiefRetail";
  }
}