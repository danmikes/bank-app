package nl.beehive.beehive.service;
/*
https://www.baeldung.com/a-guide-to-java-enums
inhoud label: i.e. RoleUser.Customer.label
dit kan nog uitgebreid worden met methods etcetera
 */
public enum TransactionType {
    ACCEPTGIRO("Acceptgiro"),
    BETAALAUTOMAAT("Betaalautomaat"),
    DIVERSEN("Diversen"),
    FILILIAAL("Filiaal overschrijving"),
    SALDONLIJN("Saldolijn"),
    GELDAUTOMAAT("Geldautomaat")        ,
    ONLINEBANKIEREN("Online bankieren"),
    INCASSO("Incasso"),
    OVERSCHRIJVING("Overschrijving"),
    OPNAMEKANTOOR("Opname kantoor"),
    PERIODIEK("Periodieke overschrijving"),
    STORTING("Storting"),
    VERZAMELING("Verzamelbetaling");
    public final String label;
    private TransactionType(String label) {
        this.label = label;
    }
}
