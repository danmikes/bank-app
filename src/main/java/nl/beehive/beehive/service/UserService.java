package nl.beehive.beehive.service;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.UserDao;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserService {

    private final UserDao userDao;
    private final HttpSession session;

    private String feedback;
    private User user;

    public UserService(UserDao userDao, HttpSession session) {
        this.userDao = userDao;
        this.session = session;
    }

    private boolean validateUserPassword(User formUser) {
        boolean loginOk;

        User dbUser = userDao.findByUsername(formUser.getUsername());

        if (dbUser != null && dbUser.getPassword().equals(formUser.getPassword())) {
            loginOk = true;
        } else {
            loginOk = false;
            feedback = "De combinatie van gebruikersnaam en wachtwoord klopt niet";
        }

        return loginOk;
    }

    //Methode om de login te behandelen per rol in de database
    public String loginByRole(User userLoginDetails) {

        //Check of het ingevoerde wachtwoord correct is
        if (validateUserPassword(userLoginDetails)) {
            //Sla de huidige gebruiker op
            user = userDao.findByUsername(userLoginDetails.getUsername());
            session.setAttribute("currentUser", user);
            return returnUserPage();
        } else return "loginFail";
    }

    public String fillHeader() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd MMM yyyy");
        return "Welkom " + getUser().getFirstName() + ". Het is nu " + LocalDateTime.now().format(formatter) + ", ";
    }

    public Map<String, String> navigationLinks() {
        Map<String, String> navigationList = new LinkedHashMap<>();

        if (getUser() instanceof Customer) {
            navigationList.put("customerPage", "Startpagina");
            navigationList.put("transactionStart", "Overboeken");
            navigationList.put("addAccount", "Rekening openen");
        }

        navigationList.put("userInfo", "Mijn gegevens");

        return navigationList;
    }

    private String returnUserPage() {
        if (user instanceof Customer) {
            return "redirect:customerPage";
        } else if (user instanceof AccountManager) {
            return "redirect:accountManager";
        } else if (user instanceof ChiefRetail) {
            return "redirect:chiefRetail";
        } else if (user instanceof ChiefBusiness) {
            return "redirect:chiefBusiness";
        } else return "login";
    }

    public void setUser(User user) {
        session.setAttribute("currentUser", user);
    }

    public User getUser() {
        return (User) session.getAttribute("currentUser");
    }

    public String getFeedback() {
        return feedback;
    }

    public HttpSession getSession() {
        return session;
    }

    public String getSessionID() {
        return session.getId();
    }
}