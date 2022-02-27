package org.hwr.equaly.controller.textSplitter;

import org.hwr.equaly.model.Fragment;

public interface TextSplitter {
    /**
     * Take the raw user-input text, split it up into sets of 3 (or less) consecutive
     * words which must be part of the same sentence.
     * //TODO: update JavaDoc
     * @return A matrix of word contexts - one context per each word of the text
     */
    Fragment[][] createSubsets(String[] tokens, String[] tags);
}
