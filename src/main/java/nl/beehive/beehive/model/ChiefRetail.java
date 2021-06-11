package nl.beehive.beehive.model;

import nl.beehive.beehive.service.RoleUser;

import javax.persistence.Entity;

@Entity
public class  ChiefRetail extends User {
    public ChiefRetail() {
        this("", "");
    }

    public ChiefRetail(String username, String password) {
        this(username, password, "");
    }

    public ChiefRetail(String username, String password, String firstName) {
        super(username, password, firstName);
    }
}
