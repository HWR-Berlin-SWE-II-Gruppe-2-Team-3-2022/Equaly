package org.hwr.equaly.controller.tokenizer;

import com.github.pemistahl.lingua.api.Language;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Splitting up a text that may contain words/sentences into a set tokens
 * using the OpenNLP pre-trained Tokenizers for german or english
 */
@Component
public class TokenizerImpl implements Tokenizer {

    InputStream modelStreamDE, modelStreamEN;
    TokenizerModel model_de, model_en;

    /**
     * Initializing a Tokenizer object. This method is to be executed at first booting so
     * that later on performance is optimal.
     */
    public void initialize() {
        try {
            modelStreamDE = new FileInputStream("./data/models/de-tokenizer.bin");
            modelStreamEN = new FileInputStream("./data/models/en-tokenizer.bin");
            model_de = new TokenizerModel(modelStreamDE);
            model_en = new TokenizerModel(modelStreamEN);
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    /**
     * Given a detected language and a text, turn the text into a set of tokens (in original order)
     * @param language the language that the text was written in (detected here by LanguageTagger object
     * @param text the users text input
     * @return a set of tokens representing the text contents (and splitting it up in a way so that no meaning gets lost)
     */
    public String[] run(Language language, String text) {
        TokenizerModel model;
        if (language.equals(Language.GERMAN)) {
            model = model_de;
        } else {
            model = model_en;
        }
        TokenizerME tokenizer = new TokenizerME(model);
        return tokenizer.tokenize(text);
    }

}
