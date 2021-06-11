package nl.beehive.beehive.service;

public enum BusinessSector {
 ENERGIE("Energie"),
 VEETEELT("Veeteelt"),
 VISSERIJ("Visserij"),
 INDUSTRIE("Industrie"),
 TRANSPORT("Transport"),
 TOERISME("Toerisme"),
 ENTERTAINMENT("Entertainment"),
 ICT("ICT"),
 WETENSCHAP("Wetenschap");
 public final String label;
 private BusinessSector(String label) { this.label = label; }
}


