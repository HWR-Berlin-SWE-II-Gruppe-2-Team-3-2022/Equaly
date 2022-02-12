package org.hwr.equaly.controller.textSplitter;

import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class TextSplitterImpl implements TextSplitter {

    @Override
    public String[][] createSubsets(String inputText) {
        // Split entire text into one big set of words
        String[] inputWords = splitToWords(inputText);

        // Define 2D array of Arrays of String
        // one 1D Array per Word implies Array's first dimensional size
        // second dimension arrays differ in size -> added later through splitting process
        String[][] subSets = new String[inputWords.length][];

        // Per each word: Create String List of minimum size 1, maximum size 3
        for (int i = 0; i < inputWords.length; i++) {
            ArrayList<String> subSet = new ArrayList<>();

            if (i == 0) {
                // If Word is the overall first word -> Treat like a sentence ended (like below)
                subSet.add("");
            } else {
                // If prior word ends with ".", "!", "?" or ";" -> Current Word is in first list place, else this prior word is
                if (endOfSentence(inputWords[i-1])) {
                    subSet.add("");
                } else {
                    subSet.add(inputWords[i-1]);
                }
            }

            // Either a word or a "" is in first place of the subSet.
            // The current word is ALWAYS on the second position
            subSet.add(inputWords[i]);

            // If current word ends with ".", "!", "?", ";" or ":" -> Current Word is in last list place, else look for succeeding word
            // If succeeding word ends with ".", "!", "?", ";" or ":" -> Who cares, all good, add to last list place
            if (!endOfSentence(inputWords[i]) && (i+1) < inputWords.length) {
                subSet.add(inputWords[i+1]);
            }

            // If we haven't reached a list size of 3, we fill the last space with ""
            if (endOfSentence(inputWords[i]) && subSet.size() < 3) {
                subSet.add("");
            }

            // Convert the created list to an array to fixate its size and accelerate processing later on
            String[] subSetArr = subSet.toArray(new String[subSet.size()]);

            // Add the created array to the previously created, bigger array at index of the current word
            subSets[i] = subSetArr;
        }

        return subSets;
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
                word.endsWith(";") ||
                word.endsWith(":"));
    }

    /**
     * Remove leading or trailing spaces, split the sentences up into words only.
     * Yes, even passages like " - " are treated as a word "-" for now.
     * Later on we'll just not find it in the DB so we won't replace it. But it has to stay here.
     * @param text - the raw text input
     * @return an array of 'words' (fragments of the sentence, much rather 'related symbol sets' but treated as words internally)
     */
    private String[] splitToWords(String text) {
        return text.trim()
                   .replaceAll("\n", " ")
                   .replaceAll("\t", " ")
                   .split(" ");
    }

}
