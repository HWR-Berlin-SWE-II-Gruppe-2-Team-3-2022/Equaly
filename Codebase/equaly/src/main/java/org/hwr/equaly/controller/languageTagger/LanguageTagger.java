package org.hwr.equaly.controller.languageTagger;

import com.github.pemistahl.lingua.api.Language;

public interface LanguageTagger {

    /**
     * Initializing a Language Tagger object of the Lingua framework, limiting it to differentiate only between
     * German and English (for now and for performance). This method is to be executed at first booting so
     * that later on performance is optimal.
     */
    void initialize();

    /**
     * Given a text, determine whether the language used is German or English.
     * @param text this is the text to be analyzed. It can be anything except Null.
     * @return the language that the model is most certain on
     */
    Language getLanguage(String text);
}
