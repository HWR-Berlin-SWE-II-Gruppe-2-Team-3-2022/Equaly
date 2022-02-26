package org.hwr.equaly.controller.posTagger;

import com.github.pemistahl.lingua.api.Language;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class POSTaggerImpl implements POSTagger {

    InputStream posModel_de, posModel_en;

    @Override
    public void initialize() {
        // loading the parts-of-speech model from stream
        try {
            this.posModel_de = new FileInputStream("./data/models/de-pos-maxent.bin");
            this.posModel_en = new FileInputStream("./data/models/en-pos-maxent.bin");
        } catch (
        FileNotFoundException f) {
            f.printStackTrace();
        }
    }

    @Override
    public String[] run(Language language, String[] tokens) {
        // Parts-Of-Speech Tagging
        POSModel posModel;
        try {
            if (language.equals(Language.GERMAN)) {
                posModel = new POSModel(posModel_de);
            } else {
                posModel = new POSModel(posModel_en);
            }
            // initializing the parts-of-speech tagger with model
            POSTaggerME posTagger = new POSTaggerME(posModel);
            // Tagger tagging the tokens
            return posTagger.tag(tokens);
        } catch(IOException io) {
            io.printStackTrace();
        }

        return new String[0];
    }
}
