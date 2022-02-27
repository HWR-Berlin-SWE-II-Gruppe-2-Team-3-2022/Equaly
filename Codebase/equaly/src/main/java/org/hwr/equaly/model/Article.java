package org.hwr.equaly.model;

public class Article {
    private String lemma;
    private String gender;
    private String fall;
    private String numerus;

    public Article(String lemma, String fall, String numerus, String gender) {
        this.lemma = lemma;
        this.fall = fall;
        this.numerus = numerus;
        this.gender = gender;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
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

    public String getNumerus() {
        return numerus;
    }

    public void setNumerus(String numerus) {
        this.numerus = numerus;
    }

}
