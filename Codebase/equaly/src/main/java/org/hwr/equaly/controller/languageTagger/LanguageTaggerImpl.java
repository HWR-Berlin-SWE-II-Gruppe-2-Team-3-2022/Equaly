package org.hwr.equaly.controller.languageTagger;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

import static com.github.pemistahl.lingua.api.Language.*;
import org.springframework.stereotype.Component;

@Component
public class LanguageTaggerImpl implements LanguageTagger {
    private LanguageDetector detector;

    public void initialize() {
        this.detector = LanguageDetectorBuilder.fromLanguages(ENGLISH, GERMAN).build();
    }

    @Override
    public Language getLanguage(String text) {
        return detector.detectLanguageOf(text);
    }

}
