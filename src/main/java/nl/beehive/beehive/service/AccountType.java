package nl.beehive.beehive.service;
public enum AccountType {
    RETAIL_CHECKING("Betaalrekening"),
    RETAIL_SAVING("Spaarrekening"),
    BUSINESS_CH("Zakelijke Rekening"),
    BUSINESS_SV("Zakelijke Spaarrekening"),
    TRADE("Beleggingsrekening"),
    CHILD("Maya de Bij"),
    BEEH_MORGAGE("Hypotheek Rekening Beehive"),
    BEEH_INSURANCE("Verzekering Beehive"),
    BEEH_TRADE("Belegging Beehive"),
    BEEH_BYPASS_IN("Tussenrekening In"),
    BEEH_LOAN("Lening"),
    BEEH_BYPASS_OUT("Tussenrekening Uit"),
    BEEH_INTER3("Honingraat Rekening"),
    BEEH_INTER4("Honing Pot");
    public final String label;
    private AccountType(String label) {
            this.label = label;
        }
}
