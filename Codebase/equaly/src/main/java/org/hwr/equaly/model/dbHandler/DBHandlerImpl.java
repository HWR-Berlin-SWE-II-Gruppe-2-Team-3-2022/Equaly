package org.hwr.equaly.model.dbHandler;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.model.Article;
import org.hwr.equaly.model.Substitute;
import org.hwr.equaly.model.tickets.DBTicket;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Takes care of every interaction with a given database file.
 * Processes DB requests and returns substantive or article information
 * for either german or english words
 */
@Component
public class DBHandlerImpl implements DBHandler {

    private Connection conn = null;
    private String path = "./data/equaly.db";
    private String url = "";

    /**
     * Establish a connection between Database file and Java backend.
     * Connection should persist to rule out tampering while at work.
     */
    @Override
    public void initialize() {
        this.url = "jdbc:sqlite:" + path;

        try {
            createDir(path);
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createTables();
    }

    /**
     * These commands are run against the now connected database file.
     * Generating a new table structure inside the DB file only if there isn't one already.
     */
    private void createTables() {
        // SQL-Statements for re-creating a new database if none could be found
        String wort_de = "CREATE TABLE IF NOT EXISTS Wort_DE (" +
                         " Wort_ID INTEGER PRIMARY KEY," +
                         " Fall TEXT CHECK( Fall IN ('Nominativ','Genitiv','Dativ', 'Akkusativ') ) NOT NULL," +
                         " Wort TEXT NOT NULL," +
                         " Gender TEXT CHECK ( Gender IN ('m', 'f', 'n', 'm/f') ) NOT NULL," +
                         " Numerus TEXT CHECK ( Numerus IN ('Singular', 'Plural')) NOT NULL" +
                         ");";

        String wort_ersetzt_wort_de = "CREATE TABLE IF NOT EXISTS Wort_ersetzt_Wort_DE (" +
                         " Wort_ID INTEGER," +
                         " Ersatzwort_ID INTEGER," +
                         " FOREIGN KEY (Wort_ID) REFERENCES Wort_DE(Wort_ID)," +
                         " FOREIGN KEY (Ersatzwort_ID) REFERENCES Wort_DE(Wort_ID)," +
                         " PRIMARY KEY (Wort_ID, Ersatzwort_ID)" +
                         ");";

        String artikel_de = "CREATE TABLE IF NOT EXISTS Artikel_DE (" +
                         " Artikel_ID INTEGER PRIMARY KEY, " +
                         " Artikel TEXT NOT NULL," +
                         " Fall TEXT CHECK( Fall IN ('Nominativ','Genitiv','Dativ', 'Akkusativ') ) NOT NULL," +
                         " Gender TEXT CHECK ( Gender IN ('m', 'f', 'n', 'm/f') ) NOT NULL," +
                         " Numerus TEXT CHECK ( Numerus IN ('Singular', 'Plural')) NOT NULL," +
                         " Familie TEXT NOT NULL" +
                         ");";


        String wort_en = "CREATE TABLE IF NOT EXISTS Wort_EN (" +
                         " Wort_ID INTEGER PRIMARY KEY," +
                         " Fall TEXT CHECK( Fall IN ('Nominativ','Genitiv','Dativ', 'Akkusativ') ) NOT NULL," +
                         " Wort TEXT NOT NULL," +
                         " Gender TEXT CHECK ( Gender IN ('m', 'f', 'n', 'm/f') ) NOT NULL," +
                         " Numerus TEXT CHECK ( Numerus IN ('Singular', 'Plural')) NOT NULL" +
                         ");";

        String wort_ersetzt_wort_en = "CREATE TABLE IF NOT EXISTS Wort_ersetzt_Wort_EN (" +
                         " Wort_ID INTEGER," +
                         " Ersatzwort_ID INTEGER," +
                         " FOREIGN KEY (Wort_ID) REFERENCES Wort_EN(Wort_ID)," +
                         " FOREIGN KEY (Ersatzwort_ID) REFERENCES Wort_EN(Wort_ID)," +
                         " PRIMARY KEY (Wort_ID, Ersatzwort_ID)" +
                         ");";

        String artikel_en = "CREATE TABLE IF NOT EXISTS Artikel_EN (" +
                         " Artikel_ID INTEGER PRIMARY KEY, " +
                         " Artikel TEXT NOT NULL," +
                         " Fall TEXT CHECK( Fall IN ('Nominativ','Genitiv','Dativ', 'Akkusativ') ) NOT NULL," +
                         " Gender TEXT CHECK ( Gender IN ('m', 'f', 'n', 'm/f') ) NOT NULL," +
                         " Numerus TEXT CHECK ( Numerus IN ('Singular', 'Plural')) NOT NULL," +
                         " Familie TEXT NOT NULL" +
                         ");";
        try {
            // Create a new DB if genuinely none could be found
            conn.createStatement().execute(wort_de);
            conn.createStatement().execute(wort_en);
            conn.createStatement().execute(wort_ersetzt_wort_de);
            conn.createStatement().execute(wort_ersetzt_wort_en);
            conn.createStatement().execute(artikel_de);
            conn.createStatement().execute(artikel_en);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a substantive, head into the DB to find a replacement
     * and return either this replacement or "" if none could be found
     * @param substantive the substantive for which a replacement is to be found
     * @return the replacement or an empty string of no replacement could be found
     */
    @Override
    public Substitute getSubstantiveFor(String substantive, Language language) {
        Substitute replacement = new Substitute();
        String lang = getLanguageCode(language);
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT Wort, Fall, Gender, Numerus FROM Wort_" + lang + " AS W INNER JOIN Wort_ersetzt_Wort_" + lang + " AS WW ON W.Wort_ID = WW.Ersatzwort_ID WHERE WW.Wort_ID = (SELECT Wort_ID FROM Wort_" + lang + " WHERE Wort = '" + substantive + "') LIMIT 1;");
            rs.next();
            replacement.setWord(rs.getString(1));
            replacement.setFall(rs.getString(2));
            replacement.setGender(rs.getString(3));
            replacement.setNumerus(rs.getString(4));
        } catch (SQLException e) {
            // Fine, we'll just throw the empty Substantive object then
            return replacement;
        }

        // Replacement hasn't been thrown, so something useful was found -> enrich it
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT Fall, Gender, Numerus FROM Wort_" + lang + " WHERE Wort = '" + substantive + "' LIMIT 1;");
            rs.next();
            replacement.setOldFall(rs.getString(1));
            replacement.setOldGender(rs.getString(2));
            replacement.setOldNumerus(rs.getString(3));
        } catch (SQLException e) {
            // Fine, we'll just throw with empty Substantive history then
            return replacement;
        }

        return replacement;
    }

    private String getLanguageCode(Language language) {
        if (language.equals(Language.GERMAN)) {
            return "DE";
        }
        return "EN";
    }

    /**
     * This should ALWAYS be run over incoming texts that are to be stored in order
     * to reduce risk of malfunction or corruption of the DB.
     * @param text - an incoming text
     * @return text with those symbols replaced that could cause error or corruption
     */
    private String formatForDB(String text) {
        return text.replaceAll("'", "&apos;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("<", "&lt;");
    }

    /**
     * Create database directory and file if none could be found.
     * @param path the directory-path to where the db should be located, but including the db's own filename
     */
    private void createDir(String path) {
        File dir = new File(path.substring(0, path.lastIndexOf("/")));
        File dbFile = new File(path);

        if (!dir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }

        if (!dbFile.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Affiliated with 'addSubstantive', processes
     * input from HTML-form, turns into correct Code
     * for DB-Table interaction
     * @param sprache - comes from HTML form
     * @return Language code used to address correct tables
     */
    private String getlang(String sprache) {
        switch(sprache) {
            case "Deutsch": return "DE";
            case "Englisch": return "EN";
        }
        return null;
    }

    /**
     * Add a Substantive with case and gender aswell as (maybe) a replacement for this word.
     * @param dbTicket contains everything about the information on new words to be added
     */
    @Override
    public void addSubstantive(DBTicket dbTicket) {
        // Look if added word is there already (with word, case and gender all together)
        int wordID;
        boolean wordExistedAlready;
        // Look if replacement word is there already (with word, case and gender all together)
        int replacementID;
        boolean replacementExistedAlready;
        String language = getlang(dbTicket.getSprache());

        try {
            ResultSet ws = conn.createStatement().executeQuery("SELECT Wort_ID FROM Wort_" + language + " WHERE Wort = '" + dbTicket.getWort() + "' AND Fall = '" + dbTicket.getFall() + "' AND Gender = '" + dbTicket.getGender() + "' AND Numerus = '"+ dbTicket.getNumerus() + "' LIMIT 1;");
            if (ws.next()) {
                // if so: note its ID
                wordID = ws.getInt(1);
                wordExistedAlready = true;
            } else {
                // if not: add it and note this new entry's ID
                wordID = conn.createStatement().executeQuery("SELECT MAX(Wort_ID) FROM Wort_" + language + ";").getInt(1) + 1;
                conn.createStatement().executeUpdate("INSERT INTO Wort_" + language + " VALUES (" + wordID + ", '" + dbTicket.getFall() + "' ,'" + dbTicket.getWort() + "', '" + dbTicket.getGender() + "', '" + dbTicket.getNumerus() + "');");
                wordExistedAlready = false;
            }

            ResultSet rs = conn.createStatement().executeQuery("SELECT Wort_ID FROM Wort_" + language + " WHERE Wort = '" + dbTicket.getErsatzWort() + "' AND Fall = '" + dbTicket.getFall() + "' AND Gender = '" + dbTicket.getErsatzGender() + "' AND Numerus = '" + dbTicket.getNumerus() + "' LIMIT 1;");
            if (rs.next()) {
                // if so: note its ID
                replacementID = rs.getInt(1);
                replacementExistedAlready = true;
            } else {
                // if not: add it and note this new entry's ID
                replacementID = conn.createStatement().executeQuery("SELECT MAX(Wort_ID) FROM Wort_" + language + ";").getInt(1) + 1;
                conn.createStatement().executeUpdate("INSERT INTO Wort_" + language + " VALUES (" + replacementID + ", '" + dbTicket.getFall() + "' ,'" + dbTicket.getErsatzWort() + "', '" + dbTicket.getErsatzGender() + "', '" + dbTicket.getNumerus() + "');");
                replacementExistedAlready = false;
            }

            // if either word or replacement were unknown to the DB: add their IDs to the m:n table as new interconnection
            if (!(replacementExistedAlready && wordExistedAlready)) {
                conn.createStatement().executeUpdate("INSERT INTO Wort_ersetzt_Wort_" + language + " VALUES (" + wordID + ", " + replacementID + ");");
            }

            // else: don't do anything, somebody is trying to be very funny here

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find the current article's family - that is, if the current word is an article.
     * Limit search to word sets for a given language.
     * @param token the potential article
     * @param language the text's / token set's language
     * @return null if nothing was found, the word family identifying string otherwise
     */
    @Override
    public Article getArticleFamily(String token, Language language) {
        String lang = getLanguageCode(language);
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT Familie, Fall, Numerus, Gender FROM Artikel_" + lang + " WHERE Artikel = '" + token.toLowerCase() + "';");
            rs.next();
            return new Article(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Given an article family and details about a current article's state and its desired state, find an article for the given casus, numerus, genus
     * @param articleFamily identifies the word group of the article
     * @param fall casus (Nominativ, Genitiv etc.)
     * @param gender (m, f, n)
     * @param numerus (singular, plural)
     * @return the article that fits the search description or null if this could not be satisfied.
     */
    @Override
    public String getArticleFor(String articleFamily, String token, Language language, String fall, String gender, String numerus) {
        String lang = getLanguageCode(language);
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT Artikel FROM Artikel_" + lang + " WHERE Familie = '" + articleFamily + "' AND Fall = '"+ fall + "' AND Gender = '" + gender + "' AND Numerus = '" + numerus + "' LIMIT 1;");
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            return null;
        }
    }
}
