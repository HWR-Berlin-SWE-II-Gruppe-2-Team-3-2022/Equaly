package org.hwr.equaly.controller.exchanger;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.Article;
import org.hwr.equaly.model.Fragment;
import org.hwr.equaly.model.Substitute;
import org.hwr.equaly.model.dbHandler.DBHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class WordExchangerImpl implements WordExchanger {

    @Override
    public AnalysisContainer exchangeSubstantives(DBHandler db, Language language, Fragment[][] subSets) {
        // transform subSets to extended subSets objects for marking of substantive indices
        AnalysisContainer container = new AnalysisContainer(subSets);
        // Pro Satz, Pro Wort:
        for (int i = 0; i < subSets.length; i++) {
            for (int j = 0; j < subSets[i].length; j++) {
                // Wenn das aktuelle Wort mit dem Tag "NN" versehen wurde:
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
     * Remove all non-alphabetic characters (that is all characters not belonging to the german alphabet) from a String
     * @param word this will be reduced to an alphabetic-only String
     * @return word with only its letter contents
     */
    private String alphabetize(String word){
        return word.replaceAll("[^a-zA-ZäÄöÖüÜßẞ-]", "");
    }

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
                if (word.tag.equals("ART") || word.tag.equals("PRELS")) {
                    // erfahre Lemma und Kasus, Numerus, Genus des aktuellen Artikels
                    Article lemma = db.getArticleFamily(word.token, language);
                    // Wenn dieses Wort nun in Relation zum Modus des substituierten Wortes steht:
                    if (lemma.getGender().equals(replacements.get(key).getOldGender())
                        || word.index == replacements.get(key).getGlobalIndex() - 1) {
                        // Suche nach einer Substitution mit identischer Familie und Substitutions-Modus
                        String substitute = db.getArticleFor(lemma.getLemma(), language, replacements.get(key).getFall(), replacements.get(key).getGender(), replacements.get(key).getNumerus());
                        // Prüfe, ob die Suche erfolgreich war:
                        if (substitute != null) {
                            container.addArticleReplacement(substitute, word.tag, replacements.get(key).getSentenceIndex(), word.wordIndex, word.index);
                        }
                    }
                }
            }
        }
        return container;
    }
}
