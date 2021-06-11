package nl.beehive.beehive.service;

import nl.beehive.beehive.model.User;
import nl.beehive.beehive.model.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
public class LoginForgotService {

    private final UserDao userDao;

    public LoginForgotService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void resetPassword(String username, Model model){
        if (userDao.existsUserByUsername(username)) {
            User tempUser = userDao.findByUsername(username);
            int tempNumber = (int) Math.floor(Math.random() * ((100-10)+1) + 10);
            tempUser.setPassword("resetWW" + tempNumber);
            userDao.save(tempUser);
            model.addAttribute("feedback", "Je ontvangt een brief met een nieuw wachtwoord.");
        }
        else {
            model.addAttribute("feedback", "onbekende gebruikersnaam.");
        }
    }
}
