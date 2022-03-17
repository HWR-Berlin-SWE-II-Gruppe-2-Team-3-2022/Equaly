package org.hwr.equaly.controller.languageTagger;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

import static com.github.pemistahl.lingua.api.Language.*;
import org.springframework.stereotype.Component;

/**
 * Takes text given by the user through the Web UI and determines its language
 * Utilizes Lingua's pre-trained LanguageDetector
 * Limitation here: For now a text is only either german or english
 */
@Component
public class LanguageTaggerImpl implements LanguageTagger {
    private LanguageDetector detector;

    /**
     * Initializing a Language Tagger object of the Lingua framework, limiting it to differentiate only between
     * German and English (for now and for performance). This method is to be executed at first booting so
     * that later on performance is optimal.
     */
    @Override
    public void initialize() {
        this.detector = LanguageDetectorBuilder.fromLanguages(ENGLISH, GERMAN).build();
    }

    /**
     * Given a text, determine whether the language used is German or English.
     * @param text this is the text to be analyzed. It can be anything except Null.
     * @return the language that the model is most certain on
     */
    @Override
    public Language getLanguage(String text) {
        return detector.detectLanguageOf(text);
    }

}
