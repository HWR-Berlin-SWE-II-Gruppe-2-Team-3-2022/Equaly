package org.hwr.equaly.controller.languageTagger;

import com.github.pemistahl.lingua.api.Language;

public interface LanguageTagger {
    Language getLanguage(String text);
}
