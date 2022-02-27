package org.hwr.equaly.controller;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.controller.exchanger.WordExchanger;
import org.hwr.equaly.controller.languageTagger.LanguageTagger;
import org.hwr.equaly.controller.posTagger.POSTagger;
import org.hwr.equaly.controller.textMerger.TextMerger;
import org.hwr.equaly.controller.textSplitter.TextSplitter;
import org.hwr.equaly.controller.tokenizer.Tokenizer;
import org.hwr.equaly.model.Fragment;
import org.hwr.equaly.model.Substitute;
import org.hwr.equaly.model.tickets.DBTicket;
import org.hwr.equaly.model.tickets.TranslateTicket;
import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.dbHandler.DBHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Controller containing the logic used to interact with user through web frontend.
 * Takes data and forwards it to the frontend but also awaits user input to process text.
 */
@Controller
@Scope("session")
public class EqualyController {

    // Actual implementation is wired in automatically thanks to Spring
    @Autowired
    private ThymeleafAttributeContainer thyme;
    @Autowired
    private TextSplitter textSplitter;
    @Autowired
    private WordExchanger wordExchanger;
    @Autowired
    private TextMerger textMerger;

    private TranslateTicket translateTicket = new TranslateTicket();
    private DBHandler db;
    private LanguageTagger languageTagger;
    private Tokenizer tokenizer;
    private POSTagger posTagger;
    private String outputText = "";
    private String language;

    // Injecting configured application's name into variable
    @Value("${spring.application.name}")
    final String appName = null;

    /**
     * DBHandler instance is autowired in like this
     * for performance reasons (only once at first startup),
     * initializing the database connection,
     * then making the DBHandler accessible to all methods.
     * @param db the DBHandler that 'is handed in from nowhere' (it's autowired)
     */
    @Autowired
    public EqualyController(DBHandler db, LanguageTagger languageTagger, Tokenizer tokenizer, POSTagger posTagger) {
        languageTagger.initialize();
        tokenizer.initialize();
        posTagger.initialize();
        db.initialize();
        this.languageTagger = languageTagger;
        this.tokenizer = tokenizer;
        this.posTagger = posTagger;
        this.db = db;
    }

    /**
     * Introducing the app's name and a carrier object for user input exchange to the frontend.
     * @param model works a container for data exchange between website and Spring backend
     * @return the name of the website to be displayed on initial opening of the application
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute(thyme.TRANSLATE, translateTicket);
        model.addAttribute(thyme.APPNAME, appName);
        model.addAttribute(thyme.OUTPUT, outputText);
        language = LocaleContextHolder.getLocale().getLanguage();

        if (language.equals("de")) {
            return "main_de";
        }

        return "main_en";
    }

    /**
     * Receiving the text entered by the user to be gendered.
     * @param translateTicket filled by and now received back from the frontend, contains user-entered text
     * @param model works a container for data exchange between website and Spring backend
     * @return redirecting to the start page
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String translate(@ModelAttribute TranslateTicket translateTicket, Model model) {
        // Makes the last entered input Text stay beyond reload
        this.translateTicket = translateTicket;
        this.translateTicket.setInputText(this.translateTicket.getInputText().trim());

        // executing only if there really is any text
        if (!translateTicket.getInputText().isEmpty()) {
            // detect text's language (de/en only for now)
            Language language = languageTagger.getLanguage(translateTicket.getInputText());
            // split up text into individual word/content fragments
            String[] tokens = tokenizer.run(language, translateTicket.getInputText());
            // gather information on individual fragments
            String[] tags = posTagger.run(language, tokens);
            // group tokens into sentences
            Fragment[][] splitText = textSplitter.createSubsets(tokens, tags);

            // run substantive replacement, replace them and make notes of replacements
            AnalysisContainer processedSubstantives = wordExchanger.exchangeSubstantives(db, language, splitText);

            // Nur zur Verifikation des aktuellen Arbeitsstandes!
            // TODO: Mit nächstem Turn entfernen
            HashMap<Integer, Substitute> x = processedSubstantives.getSubstantiveReplacements();
            for (int key: x.keySet()) {
                System.out.println(x.get(key).getWord());
                System.out.println(x.get(key).getFall());
                System.out.println(x.get(key).getGender());
                System.out.println(x.get(key).getNumerus());
                System.out.println(" ");
                System.out.println(x.get(key).getOldFall());
                System.out.println(x.get(key).getOldGender());
                System.out.println(x.get(key).getOldNumerus());
            }

        }

        /*
        if (!translateTicket.getInputText().trim().isEmpty()) {
            // split up text into individual word fragments
            String[][] splitText = textSplitter.createSubsets(translateTicket.getInputText());

            // run substantive replacement, replace them and make notes of replacements
            AnalysisContainer processedSubstantives = wordExchanger.exchangeSubstantives(db, splitText);

            // run article replacement, replace them and make notes of replacements
            AnalysisContainer processedArticles = wordExchanger.exchangeArticles(db, processedSubstantives);

            // combine text fragements of last analysis step to form a sentence again
            outputText = textMerger.merge(processedArticles).trim();
        }
        */

        return "redirect:/";
    }

    @GetMapping("/add")
    public String add(Model model) {
        // replaces placeholder appName in html with content of appName
        model.addAttribute(thyme.APPNAME, appName);

        ArrayList<String> genderList = new ArrayList<>(Arrays.asList("m", "f", "n"));
        ArrayList<String> fallList = new ArrayList<>(Arrays.asList("Nominativ", "Genitiv", "Dativ", "Akkusativ"));

        model.addAttribute("genderList", genderList);
        model.addAttribute("fallList", fallList);
        model.addAttribute("dbTicket", new DBTicket());
        return "add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, params = "action=add")
    public String add(@ModelAttribute DBTicket dbTicket, Model model) {

        if (dbTicket.isNotEmpty()) {
            db.addSubstantive(dbTicket);
        }

        return "redirect:/add";
    }
}