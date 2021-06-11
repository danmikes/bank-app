package nl.beehive.beehive.service;
/*
https://www.baeldung.com/a-guide-to-java-enums
inhoud label: i.e. RoleUser.Customer.label
dit kan nog uitgebreid worden met methods etcetera
*/
// Deze service kan nu toch weg?
public enum RoleUser {
    CUSTOMER("Klant"),
    EMPLOYEE("Medewerker"),
    ACCOUNTMANAGER("Account Manager"),
    RETAILMANAGER("Hoofd Particulieren"),
    BUSINESSMANAGER("Hoofd MKB");
    public final String label;
    private RoleUser(String label) {this.label = label;
    }
}
