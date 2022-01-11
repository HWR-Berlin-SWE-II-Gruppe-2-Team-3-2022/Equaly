package org.hwr.genderly.model;

public class WordAttributes {
    private String gender;
    private String fall;
    private String numerus;

    public WordAttributes(String gender, String fall, String numerus) {
        this.gender = gender;
        this.fall = fall;
        this.numerus = numerus;
    }

    public String getNumerus() {
        return numerus;
    }

    public void setNumerus(String numerus) {
        this.numerus = numerus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFall() {
        return fall;
    }

    public void setFall(String fall) {
        this.fall = fall;
    }
}

