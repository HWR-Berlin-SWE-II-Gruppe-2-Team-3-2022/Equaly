package org.hwr.genderly.model.dbHandler;

import org.hwr.genderly.model.Word;
import org.hwr.genderly.model.tickets.DBTicket;

public interface DBHandler {
    /**
     * Establish a connection between Database file and Java backend.
     * Connection should persist to rule out tampering while at work.
     */
    void initialize();

    /**
     * Given a substantive, head into the DB to find a replacement
     * and return either this replacement or "" if none could be found
     * @param substantive the substantive for which a replacement is to be found
     * @return the replacement or an empty string of no replacement could be found
     */
    Word getSubstantiveFor(String substantive);

    /**
     * Add a Substantive with case and gender aswell as (maybe) a replacement for this word.
     * @param dbTicket contains everything about the information on new words to be added
     */
    void addSubstantive(DBTicket dbTicket);

    /**
     * Find the current article's family - that is, if the current word is an article.
     * @param predecessorAlphabetized - an alphabetic only word
     * @return null if nothing was found, the word family identifying string otherwise
     */
    String getArticleFamily(String predecessorAlphabetized);

    /**
     * Given an article family, find an article for the given casus, numerus, genus
     * @param articleFamily identifies the word group of the article
     * @param fall casus (Nominativ, Genitiv etc.)
     * @param gender (m, f, n)
     * @param numerus (singular, plural)
     * @return the article that fits the search description or null if this could not be satisfied.
     */
    String getArticleFor(String articleFamily, String fall, String gender, String numerus);
}
