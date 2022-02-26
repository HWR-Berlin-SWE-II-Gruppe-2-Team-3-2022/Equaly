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

    InputStream model_de, model_en;

    public void initialize() {
        try {
            model_de = new FileInputStream("./data/models/de-tokenizer.bin");
            model_en = new FileInputStream("./data/models/en-tokenizer.bin");
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }
    }

    public String[] run(Language language, String text) {
        try {
            TokenizerModel model;
            if (language.equals(Language.GERMAN)) {
                model = new TokenizerModel(model_de);
            } else {
                model = new TokenizerModel(model_en);
            }
            TokenizerME tokenizer = new TokenizerME(model);
            return tokenizer.tokenize(text);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return new String[0];
    }

}
