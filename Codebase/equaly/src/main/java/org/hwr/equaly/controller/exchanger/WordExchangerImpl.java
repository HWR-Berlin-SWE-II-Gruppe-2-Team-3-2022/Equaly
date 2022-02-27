package org.hwr.equaly.controller.exchanger;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.Fragment;
import org.hwr.equaly.model.Substitute;
import org.hwr.equaly.model.dbHandler.DBHandler;
import org.springframework.stereotype.Component;

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
    public AnalysisContainer exchangeArticles(DBHandler db, AnalysisContainer analysisContainer) {
        String predecessor;
        // iterate over all entries of analysisContainer's substantiveReplacements
        for (int key: analysisContainer.getSubstantiveReplacements().keySet()) {
            // Get the subSet's prior and tailing entry for the replacement
            predecessor = analysisContainer.getSubSet(key)[0];
            // If the word before or after the center one is empty -> skip this word, just do the other or none.
            if (predecessor == null || predecessor.isEmpty()) {
                continue;
            }
            // Create a copy of the word stripped of all sentence symbols and converted to lowercase
            String predecessorAlphabetized = alphabetize(predecessor);
            // If the copy could be found within the article database -> it's an article
            String articleFamily = db.getArticleFamily(predecessorAlphabetized);
            if (articleFamily == null) {
                continue;
            }
            // Take casus, numerus, genus from the replacement and look for an article with equal c, n, g and of the word family that the original article was from
            String replacement = db.getArticleFor(articleFamily,
                                                  analysisContainer.getSubstantiveReplacements().get(key).getFall(),
                                                  analysisContainer.getSubstantiveReplacements().get(key).getGender(),
                                                  analysisContainer.getSubstantiveReplacements().get(key).getNumerus());
            // Raise first Letter if necessary
            if (Character.isUpperCase(predecessor.charAt(0))) {
                replacement = Character.toUpperCase(replacement.charAt(0)) + replacement.substring(1);
            }
            // note it in analysisContainer
            analysisContainer.addArticleReplacement(predecessor.replace(predecessorAlphabetized, replacement), key - 1);
        }
        return analysisContainer;
    }
}
