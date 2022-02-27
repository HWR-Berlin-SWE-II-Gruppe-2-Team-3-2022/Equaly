package org.hwr.equaly.model.dbHandler;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.model.Article;
import org.hwr.equaly.model.Substitute;
import org.hwr.equaly.model.tickets.DBTicket;

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
    Substitute getSubstantiveFor(String substantive, Language language);

    /**
     * Add a Substantive with case and gender aswell as (maybe) a replacement for this word.
     * @param dbTicket contains everything about the information on new words to be added
     */
    void addSubstantive(DBTicket dbTicket);

    /**
     * Find the current article's family - that is, if the current word is an article.
     * TODO: JavaDoc Update
     * @return null if nothing was found, the word family identifying string otherwise
     */
    Article getArticleFamily(String token, Language language);

    /**
     * Given an article family, find an article for the given casus, numerus, genus
     * @param articleFamily identifies the word group of the article
     * @param fall casus (Nominativ, Genitiv etc.)
     * @param gender (m, f, n)
     * @param numerus (singular, plural)
     * @return the article that fits the search description or null if this could not be satisfied.
     */
    String getArticleFor(String articleFamily, String token, Language language, String fall, String gender, String numerus);
}
