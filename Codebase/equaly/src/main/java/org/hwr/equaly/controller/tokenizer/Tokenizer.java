package org.hwr.equaly.controller.tokenizer;

import com.github.pemistahl.lingua.api.Language;

public interface Tokenizer {

    /**
     * Initializing a Tokenizer object. This method is to be executed at first booting so
     * that later on performance is optimal.
     */
    void initialize();

    /**
     * Given a detected language and a text, turn the text into a set of tokens (in original order)
     * @param language the language that the text was written in (detected here by LanguageTagger object
     * @param text the users text input
     * @return a set of tokens representing the text contents (and splitting it up in a way so that no meaning gets lost)
     */
    String[] run(Language language, String text);
}
