package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

  private final UserService userService;

//  @Autowired
//  TestDataService testDataService;

  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("login")
  public String login(Model model) {
    //testDataService.insertData();
    model.addAttribute("user", new User());
    return "login";
  }

  @GetMapping("logout")
  public String logoutHandeler() {
    userService.setUser(null);
    return "redirect:/";
  }

  @PostMapping(value = "loginHandler")
  public String loginHandler(@ModelAttribute User user, Model model) {
    if (userService.loginByRole(user).equals("loginFail")) {
      model.addAttribute("feedback", userService.getFeedback());
      return "login";
    }
    return userService.loginByRole(user);
  }
}