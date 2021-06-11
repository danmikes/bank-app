package nl.beehive.beehive.model;

import nl.beehive.beehive.service.RoleUser;

import javax.persistence.Entity;

@Entity
public class ChiefBusiness extends User {
    public ChiefBusiness() {
        this("","");
    }

    public ChiefBusiness(String username, String password) {
        super(username, password);
    }

    public ChiefBusiness(String username, String password, String firstName) {
        super(username, password, firstName);
    }
}
