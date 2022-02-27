package org.hwr.equaly.model;

import java.util.ArrayList;
import java.util.HashMap;

public class AnalysisContainer {

    private Fragment[][] subSets;
    private HashMap<Integer, Substitute> substantiveReplacements;
    private ArrayList<Integer> articleIndices;

    public AnalysisContainer(Fragment[][] subSets) {
        this.subSets = subSets;
        substantiveReplacements = new HashMap<>();
        articleIndices = new ArrayList<>();
    }

    public Fragment[][] getSubSets() {
        return subSets;
    }
    public HashMap<Integer, Substitute> getSubstantiveReplacements() {
        return substantiveReplacements;
    }
    public ArrayList<Integer> getArticleIndices() {
        return articleIndices;
    }

    /**
     * Add information on a replacing substantive to the Container's dedicated list.
     * TODO: Update JavaDoc
     */
    public void addSubstantiveReplacement(Substitute substitute, int index_sentence, int index_word, int index_global) {
        subSets[index_sentence][index_word] = new Fragment(substitute.getWord(), "NN", index_global);
        substantiveReplacements.put(index_global, substitute);
    }

    public void addArticleReplacement(String article, int index) {
        //subSets[index][CONTEXTSIZE / 2] = article;
        //articleIndices.add(index);
    }

    public String[] getSubSet(int index) {
        //if (index >= 0 && index < subSets.length) {
            //return subSets[index];
        //}
        return null;
    }
}
