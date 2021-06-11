package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.Customer;
import nl.beehive.beehive.model.User;
import nl.beehive.beehive.service.UserInfoService;
import nl.beehive.beehive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserService userService;

    public UserInfoController(UserInfoService userInfoService, UserService userService) {
        this.userInfoService = userInfoService;
        this.userService = userService;
    }

    @RequestMapping(value = "userInfo")
    public String userInfo(Model model, HttpSession session) {
        userInfoService.setUpUserInfoScreen(model, session);
        return "userInfo";
    }

    @RequestMapping(value = "/userChangeLoginHandler")
    public String userChangeLoginHandler(@ModelAttribute User user, HttpSession session, Model model) {
        userInfoService.doChangeUserInfo(user, session);
        userInfoService.setUpUserInfoScreen(model, session);
        return "userInfo";
    }


    @RequestMapping(value = "/customerAdresChangeHandler")
    public String customerAdresChangeHandler(@ModelAttribute Customer customer, HttpSession session, Model model) {
        model.addAttribute("customer", customer);
        userInfoService.doChangeCustomer(customer, session);
        userInfoService.setUpUserInfoScreen(model, session);
        return "userInfo";
    }
}