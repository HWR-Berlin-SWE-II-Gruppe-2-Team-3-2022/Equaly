package org.hwr.equaly.model;

public class Fragment {
    public String token;
    public String tag;
    public int index;
    public int wordIndex;

    public Fragment(String token, String tag, int index, int wordIndex) {
        this.token = token;
        this.tag = tag;
        this.index = index;
        this.wordIndex = wordIndex;
    }
}
