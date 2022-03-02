package org.hwr.equaly.controller.textSplitter;

import org.hwr.equaly.model.Fragment;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class TextSplitterImpl implements TextSplitter {

    @Override
    public Fragment[][] createSubsets(String[] tokens, String[] tags) {
        ArrayList<Fragment[]> sentences = new ArrayList<>();
        ArrayList<Fragment> sentence = new ArrayList<>();

        int wordIndex = 0;
        for (int i = 0; i < tokens.length; i++) {
            sentence.add(new Fragment(tokens[i], tags[i], i, wordIndex));
            wordIndex++;
            if (endOfSentence(tokens[i]) || i == tokens.length - 1) {
                sentences.add(sentence.toArray(new Fragment[0]));
                sentence = new ArrayList<>();
                wordIndex = 0;
            }
        }

        Fragment[][] matrix = new Fragment[sentences.size()][];
        return sentences.toArray(matrix);
    }

    /**
     * Delivering a response to the question of whether or not a sentence ended with a given word
     * @param word this word is to be searched for sentence-ending-related symbols
     * @return true if a sentence-ending symbol was found
     */
    private Boolean endOfSentence(String word) {
        return (word.endsWith(".") ||
                word.endsWith("!") ||
                word.endsWith("?") ||
                word.endsWith(":"));
    }

}
