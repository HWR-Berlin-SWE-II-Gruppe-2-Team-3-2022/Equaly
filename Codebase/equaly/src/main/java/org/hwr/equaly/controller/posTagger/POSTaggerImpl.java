package org.hwr.equaly.controller.posTagger;

import com.github.pemistahl.lingua.api.Language;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class POSTaggerImpl implements POSTagger {

    InputStream posModelStreamDE, posModelStreamEN;
    POSModel posModel_de, posModel_en;

    @Override
    public void initialize() {
        // loading the parts-of-speech model from stream
        try {
            this.posModelStreamDE = new FileInputStream("./data/models/de-pos-maxent.bin");
            this.posModelStreamEN = new FileInputStream("./data/models/en-pos-maxent.bin");
            posModel_de = new POSModel(posModelStreamDE);
            posModel_en = new POSModel(posModelStreamEN);
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    @Override
    public String[] run(Language language, String[] tokens) {
        // Parts-Of-Speech Tagging
        POSModel posModel;
        if (language.equals(Language.GERMAN)) {
            posModel = posModel_de;
        } else {
            posModel = posModel_en;
        }
        // initializing the parts-of-speech tagger with model
        POSTaggerME posTagger = new POSTaggerME(posModel);
        // Tagger tagging the tokens
        return posTagger.tag(tokens);
    }
}
