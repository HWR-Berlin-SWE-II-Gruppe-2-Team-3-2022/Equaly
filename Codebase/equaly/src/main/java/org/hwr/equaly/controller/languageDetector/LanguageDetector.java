package org.hwr.equaly.controller.languageDetector;

import com.github.pemistahl.lingua.api.Language;

public interface LanguageDetector {
    Language getLanguage(String text);
}
