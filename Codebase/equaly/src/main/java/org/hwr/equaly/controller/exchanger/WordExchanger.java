package org.hwr.equaly.controller.exchanger;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.Fragment;
import org.hwr.equaly.model.dbHandler.DBHandler;

public interface WordExchanger {
    /**
     *
     * @param db this dbHandler carries the connection we continuously use,
     *           aswell as all the logic necessary for the WordExchanger to interact with the DB
     * @param subSets The 2D split up and spread out input text
     * @return a container for the 2D text, now also carrying notes on which word was replaced how and where
     */
    AnalysisContainer exchangeSubstantives(DBHandler db, Language language, Fragment[][] subSets);

    /**
     *
     * @param db this dbHandler carries the connection we continuously use,
     *           aswell as all the logic necessary for the WordExchanger to interact with the DB
     * TODO: Update JavaDoc
     * @return the analysisContainer now carrying the fully gender-adapted text aswell as metadata on what was exchanged and how this was done
     */
    AnalysisContainer exchangeArticles(DBHandler db, Language language, AnalysisContainer container);
}
