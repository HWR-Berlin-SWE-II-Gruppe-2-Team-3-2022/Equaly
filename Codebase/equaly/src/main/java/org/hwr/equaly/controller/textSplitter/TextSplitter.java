package org.hwr.equaly.controller.textSplitter;

public interface TextSplitter {
    /**
     * Take the raw user-input text, split it up into sets of 3 (or less) consecutive
     * words which must be part of the same sentence.
     * @param inputText raw user-input text
     * @return A matrix of word contexts - one context per each word of the text
     */
    String[][] createSubsets(String inputText);
}
