package org.hwr.genderly.model.tickets;

public class DBTicket {
    private String wort = "";
    private String fall = "";
    private String gender = "";
    private String numerus = "";
    private String ersatzWort = "";
    private String ersatzGender = "";

    public String getWort() {
        return wort;
    }

    public void setWort(String wort) {
        this.wort = wort;
    }

    public String getFall() {
        return fall;
    }

    public void setFall(String fall) {
        this.fall = fall;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumerus() {
        return numerus;
    }

    public void setNumerus(String numerus) {
        this.numerus = numerus;
    }

    public String getErsatzWort() {
        return ersatzWort;
    }

    public void setErsatzWort(String ersatzWort) {
        this.ersatzWort = ersatzWort;
    }

    public String getErsatzGender() {
        return ersatzGender;
    }

    public void setErsatzGender(String ersatzGender) {
        this.ersatzGender = ersatzGender;
    }

    public boolean isNotEmpty() {
        return !(wort.isEmpty() || fall.isEmpty() || gender.isEmpty() || numerus.isEmpty() ||
                ersatzWort.isEmpty() || ersatzGender.isEmpty());
    }
}
