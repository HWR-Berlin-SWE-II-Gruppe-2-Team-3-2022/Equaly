package org.hwr.equaly.controller.posTagger;

import com.github.pemistahl.lingua.api.Language;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implements the POS-Tagging in order to process text whose language was
 * identified and which was tokenized already, utilizes OpenNLP's pretrained maxent models for german and english
 */
@Component
public class POSTaggerImpl implements POSTagger {

    InputStream posModelStreamDE, posModelStreamEN;
    POSModel posModel_de, posModel_en;

    /**
     * Setting up a Part-of-Speech Tagger, loading the pretrained modules from files (for german and english)
     */
    @Override
    public void initialize() {
        // loading the parts-of-speech model from stream (using pre-trained, official OpenNLP models here
        try {
            this.posModelStreamDE = new FileInputStream("./data/models/de-pos-maxent.bin");
            this.posModelStreamEN = new FileInputStream("./data/models/en-pos-maxent.bin");
            posModel_de = new POSModel(posModelStreamDE);
            posModel_en = new POSModel(posModelStreamEN);
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

    /**
     * Given a text's language and the text as ordered token set (meaning in original order) run the POS-Tagger
     * on these Tokens and return the determined tags (in order of the original token set)
     * @param language the text was written in this language (determined here by LanguageTagger object)
     * @param tokens the tokenized form of the text (one word/meaning, one token)
     * @return the POS-tags determined for the tokens, in order of the tokens
     */
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
