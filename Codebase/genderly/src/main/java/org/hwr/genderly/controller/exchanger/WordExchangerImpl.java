package org.hwr.genderly.controller.exchanger;

import org.hwr.genderly.model.AnalysisContainer;
import org.hwr.genderly.model.Word;
import org.hwr.genderly.model.WordAttributes;
import org.hwr.genderly.model.dbHandler.DBHandler;
import org.springframework.stereotype.Component;

@Component
public class WordExchangerImpl implements WordExchanger {

    // extracted for easy access later on
    private final int CONTEXTSIZE = 3;

    @Override
    public AnalysisContainer exchangeSubstantives(DBHandler db, String[][] subSets) {
        // transform subSets to extended subSets objects for marking of substantive indices
        AnalysisContainer container = new AnalysisContainer(subSets);
        // iterate over all entries of Array's first dimension [e.g. 0 -> [[word], [word], [word]])
        for (int i = 0; i < subSets.length; i++) {
            // check if the current entry conforms with a size of 3 for second dimension arrays
            // if not -> skip, something is wrong with it
            if (subSets[i].length == CONTEXTSIZE) {
                // if so -> focus only on central String entry (center word) of second dimension array from here
                // check if this String starts with capital letter -> substantive
                String centerWord = subSets[i][CONTEXTSIZE / 2];
                if (!centerWord.isEmpty()) {
                    if (Character.isUpperCase(centerWord.charAt(0))) {
                        // @ this point Strings like "Wort," or "Wort!" are possible
                        // we make a copy of this entry reduced to everything alphabetical only
                        String alphabetized = alphabetize(centerWord);
                        // Head out to find a replacement for the current substantive
                        // Substantive object carries either replacement or "" if none could be found
                        Word replacement = db.getSubstantiveFor(alphabetized);
                        // Only if a replacement took place to be put effort into ... actually replacing the word
                        if (!replacement.getWord().isEmpty()) {
                            // we are currently building an AnalysisContainer, so we just hand over all info
                            // regarding our current replacement request to it and make it ... do stuff with it.
                            container.addSubstantiveReplacement(centerWord.replace(alphabetized, replacement.getWord()), i, new WordAttributes(replacement.getGender(), replacement.getFall(), replacement.getNumerus()));
                        }
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
