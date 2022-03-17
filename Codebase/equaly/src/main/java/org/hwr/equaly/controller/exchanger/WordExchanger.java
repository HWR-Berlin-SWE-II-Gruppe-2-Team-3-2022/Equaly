package org.hwr.equaly.controller.exchanger;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.Fragment;
import org.hwr.equaly.model.dbHandler.DBHandler;

public interface WordExchanger {

    /**
     * Takes in Fragments split up into sentences (x) and their word tokens (y), processes the fragment values and substitutes
     * fragments identified as substitutable substantives, notes which words where replaced where in the fragment set.
     * @param db this dbHandler carries the database connection we continuously use,
     *           aswell as all the logic necessary for the WordExchanger to interact with the DB
     * @param subSets The two-dimensionally split up and spread out input text (x: Sentence index, y: Word index for sentence x)
     * @return a container for the the same 2D fragment array, that also carries notes on which substantive was replaced how and where
     */
    AnalysisContainer exchangeSubstantives(DBHandler db, Language language, Fragment[][] subSets);

    /**
     * Takes in an AnalysisContainer carrying a set of Fragments that already may have undergone the substantive exchange process
     * Uses gathered informations from that previous exchange process to derive necessities for article replacements, carries them out
     * and notes them aswell in the then returned, modified AnalysisContainer.
     * @param db this dbHandler carries the connection we continuously use,
     *           aswell as all the logic necessary for the WordExchanger to interact with the DB
     * @return the analysisContainer now carrying the fully gender-adapted text (regarding substantives and their associated articles)
     * aswell as metadata on what was exchanged and how this was done
     */
    AnalysisContainer exchangeArticles(DBHandler db, Language language, AnalysisContainer container);
}
