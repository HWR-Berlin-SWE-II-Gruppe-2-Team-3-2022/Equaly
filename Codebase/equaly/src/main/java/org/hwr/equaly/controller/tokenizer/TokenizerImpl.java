package org.hwr.equaly.controller.tokenizer;

import com.github.pemistahl.lingua.api.Language;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class TokenizerImpl implements Tokenizer {

    InputStream modelStreamDE, modelStreamEN;
    TokenizerModel model_de, model_en;

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
