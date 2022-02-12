package org.hwr.equaly.model;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalysisContainer {

    private String[][] subSets;
    private HashMap<Integer, WordAttributes> substantiveReplacements;
    private ArrayList<Integer> articleIndices;
    private final int CONTEXTSIZE = 3;

    public AnalysisContainer(String[][] subSets) {
        this.subSets = subSets;
        substantiveReplacements = new HashMap<Integer, WordAttributes>();
        articleIndices = new ArrayList<Integer>();
    }

    public String[][] getSubSets() {
        return subSets;
    }

    public HashMap<Integer, WordAttributes> getSubstantiveReplacements() {
        return substantiveReplacements;
    }

    public ArrayList<Integer> getArticleIndices() {
        return articleIndices;
    }

    /**
     * Add information on a replacing substantive to the Container's dedicated list.
     * @param word this is the new word to be added to the 2D text structure
     * @param index this is the first dimension coordinate at which the text will be added in the center
     * @param wordAttributes the attributes of the new, replacing word
     */
    public void addSubstantiveReplacement(String word, int index, WordAttributes wordAttributes) {
        subSets[index][CONTEXTSIZE / 2] = word;
        substantiveReplacements.put(index, wordAttributes);
    }

    public void addArticleReplacement(String article, int index) {
        subSets[index][CONTEXTSIZE / 2] = article;
        articleIndices.add(index);
    }

    public String[] getSubSet(int index) {
        if (index >= 0 && index < subSets.length) {
            return subSets[index];
        }
        return null;
    }
}
