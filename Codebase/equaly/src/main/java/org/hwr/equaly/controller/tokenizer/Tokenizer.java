package org.hwr.equaly.controller.tokenizer;

import com.github.pemistahl.lingua.api.Language;

import java.io.IOException;

public interface Tokenizer {
    void initialize();
    String[] run(Language language, String text);
}
