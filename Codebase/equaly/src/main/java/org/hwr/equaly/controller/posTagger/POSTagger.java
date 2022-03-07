package org.hwr.equaly.controller.posTagger;

import com.github.pemistahl.lingua.api.Language;

public interface POSTagger {
    /**
     * Setting up a Part-of-Speech Tagger, loading the pretrained modules from files (for german and english)
     */
    void initialize();

    /**
     * Given a text's language and the text as ordered token set (meaning in original order) run the POS-Tagger
     * on these Tokens and return the determined tags (in order of the original token set)
     * @param language the text was written in this language (determined here by LanguageTagger object)
     * @param tokens the tokenized form of the text (one word/meaning, one token)
     * @return the POS-tags determined for the tokens, in order of the tokens
     */
    String[] run(Language language, String[] tokens);
}
