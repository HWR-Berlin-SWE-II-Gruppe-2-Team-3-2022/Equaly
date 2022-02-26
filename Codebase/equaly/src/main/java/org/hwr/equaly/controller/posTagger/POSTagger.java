package org.hwr.equaly.controller.posTagger;

import com.github.pemistahl.lingua.api.Language;

public interface POSTagger {
    void initialize();
    String[] run(Language language, String[] tokens);
}
