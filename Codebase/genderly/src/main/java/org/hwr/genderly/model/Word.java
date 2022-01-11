package org.hwr.genderly.model;

public class Word {
    private String word = "";
    private String gender = "";
    private String fall = "";
    private String numerus = "";

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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

