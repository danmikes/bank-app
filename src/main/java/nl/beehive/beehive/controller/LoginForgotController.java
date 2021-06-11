package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.User;
import nl.beehive.beehive.service.LoginForgotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginForgotController {

    private final LoginForgotService loginForgotService;

    public LoginForgotController(LoginForgotService loginForgotService) {
        this.loginForgotService = loginForgotService;
    }

    @RequestMapping(value = "loginForgot")
    public String loginForgot(Model model) {
        model.addAttribute("user", new User());
        return "loginForgot";
    }

    @RequestMapping(value = "/loginForgotHandler")
    public String loginForgotHandler(User user, Model model) {
        loginForgotService.resetPassword(user.getUsername(), model);
        return "loginForgot";
    }
}
