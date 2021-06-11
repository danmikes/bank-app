package nl.beehive.beehive.service;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.CustomerDao;
import nl.beehive.beehive.model.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoService {


    @Autowired
    UserDao userDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    AccountManagerService accountManagerService;

    @Autowired
    UserService userService;


    public void doChangeUserInfo (User userChanges, HttpSession session){
        User user = userService.getUser();
        user.setUsername(userChanges.getUsername());
        user.setPassword(userChanges.getPassword());
        user.setFirstName(userChanges.getFirstName());
        user.setLastName(userChanges.getLastName());
        user.setEmail(userChanges.getEmail());
        user.setTelephone(userChanges.getTelephone());
        userDao.save(user);
    }

    public void doChangeCustomer (Customer userChanges, HttpSession session){
        Customer user = (Customer) userService.getUser();
        user.setStreet(userChanges.getStreet());
        user.setHouseNumber(userChanges.getHouseNumber());
        user.setHouseSuffix(userChanges.getHouseSuffix());
        user.setZipCode(userChanges.getZipCode());
        user.setCity(userChanges.getCity());
        customerDao.save(user);
    }

    @ModelAttribute
    public void setUpUserInfoScreen(Model model, HttpSession session){
        //Opbouw header
        Map<String, String> links = new HashMap<>();
        if (userService.getUser() instanceof Customer) {
            links = userService.navigationLinks();
        } if (userService.getUser() instanceof AccountManager){
            links.put("accountManager", "Startpagina");
        } if (userService.getUser() instanceof ChiefBusiness) {
            links.put("chiefBusiness", "Startpagina");
        } if (userService.getUser() instanceof ChiefRetail) {
            links.put("chiefRetail", "Startpagina");
        }
        model.addAttribute("infoLinks", links);
        model.addAttribute("header", userService.fillHeader());
        // opbouw scherm
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("user", user);
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            model.addAttribute("customer", customer );
        }
    }
}
