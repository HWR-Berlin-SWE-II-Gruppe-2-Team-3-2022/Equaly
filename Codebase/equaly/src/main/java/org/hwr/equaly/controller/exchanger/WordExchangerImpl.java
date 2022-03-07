package org.hwr.equaly.controller.exchanger;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.Article;
import org.hwr.equaly.model.Fragment;
import org.hwr.equaly.model.Substitute;
import org.hwr.equaly.model.dbHandler.DBHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Handling the substitution process for words and articles through a step-by-step set of methods
 * to first exchange substantives and then articles.
 */
@Component
public class WordExchangerImpl implements WordExchanger {

    /**
     * Takes in Fragments split up into sentences (x) and their word tokens (y), processes the fragment values and substitutes
     * fragments identified as substitutable substantives, notes which words where replaced where in the fragment set.
     * @param db this dbHandler carries the database connection we continuously use,
     *           aswell as all the logic necessary for the WordExchanger to interact with the DB
     * @param subSets The two-dimensionally split up and spread out input text (x: Sentence index, y: Word index for sentence x)
     * @return a container for the the same 2D fragment array, that also carries notes on which substantive was replaced how and where
     */
    @Override
    public AnalysisContainer exchangeSubstantives(DBHandler db, Language language, Fragment[][] subSets) {
        // transform subSets to extended subSets objects for marking of substantive indices
        AnalysisContainer container = new AnalysisContainer(subSets);
        // Pro Satz, Pro Wort:
        for (int i = 0; i < subSets.length; i++) {
            for (int j = 0; j < subSets[i].length; j++) {
                // Wenn das aktuelle Wort mit dem Tag "NN" versehen wurde (explizit kein "NE" (Eigenname))
                if (subSets[i][j].tag.equals("NN")) {
                    // Suche ein Ersatzwort (das oder "" kommt als Rückgabe) in der DB
                    // Vermerke hier auch den alten Modus für spätere Artikelanalyse
                    Substitute substitute = db.getSubstantiveFor(subSets[i][j].token, language);
                    // Wenn Ersatzwort nicht leer:
                    if (!substitute.getWord().isEmpty()) {
                        substitute.setSentenceIndex(i);
                        // Füge Ersatzwort dem AnalysisContainer mit allen Metadaten an
                        container.addSubstantiveReplacement(substitute, i, j, subSets[i][j].index);
                    }
                }
            }
        }
        return container;
    }

    /**
     * Takes in an AnalysisContainer carrying a set of Fragments that already may have undergone the substantive exchange process
     * Uses gathered informations from that previous exchange process to derive necessities for article replacements, carries them out
     * and notes them aswell in the then returned, modified AnalysisContainer.
     * @param db this dbHandler carries the connection we continuously use,
     *           aswell as all the logic necessary for the WordExchanger to interact with the DB
     * @return the analysisContainer now carrying the fully gender-adapted text (regarding substantives and their associated articles)
     * aswell as metadata on what was exchanged and how this was done
     */
    @Override
    public AnalysisContainer exchangeArticles(DBHandler db, Language language, AnalysisContainer container) {
        HashMap<Integer, Substitute> replacements = container.getSubstantiveReplacements();
        // Für jeden der vermerkten Replacements:
        for (int key: replacements.keySet()) {
            // Suche den assoziierten Satz (in diesem Satz steht das Replacement)
            Fragment[] sentence = container.getSubSet(replacements.get(key).getSentenceIndex());
            // Iteriere über den Satz, suche dabei alle ART, PRELS im Satz
            for (Fragment word: sentence) {
                // Wenn aktuelles Wort ART/PRELS ist, ist es möglicherweise zu substituieren
                if (word.tag.equals("ART") || word.tag.equals("PRELS") || word.tag.equals("PDAT")) {
                    // erfahre Lemma und Kasus, Numerus, Genus des aktuellen Artikels
                    Article lemma = db.getArticleFamily(word.token, language);
                    // Wenn dieses Wort nun in Relation zum Modus des substituierten Wortes steht:
                    if (lemma != null && lemma.getGender().equals(replacements.get(key).getOldGender())) {
                        // Suche nach einer Substitution mit identischer Familie und Substitutions-Modus
                        String substitute = db.getArticleFor(lemma.getLemma(), word.token, language, replacements.get(key).getFall(), replacements.get(key).getGender(), replacements.get(key).getNumerus());
                        // Prüfe, ob die Suche erfolgreich war:
                        if (substitute != null) {
                            if (!(word.wordIndex < sentence.length - 1 && sentence[word.wordIndex + 1].tag.equals("NN")) || word.index == replacements.get(key).getGlobalIndex() - 1) {
                                container.addArticleReplacement(substitute, word.tag, replacements.get(key).getSentenceIndex(), word.wordIndex, word.index);
                            }
                        }
                    }
                }
            }
        }
        return container;
    }
}