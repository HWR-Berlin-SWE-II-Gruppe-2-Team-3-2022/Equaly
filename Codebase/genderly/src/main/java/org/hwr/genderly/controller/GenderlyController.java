package org.hwr.genderly.controller;

import org.hwr.genderly.controller.exchanger.WordExchanger;
import org.hwr.genderly.controller.textMerger.TextMerger;
import org.hwr.genderly.controller.textSplitter.TextSplitter;
import org.hwr.genderly.model.tickets.DBTicket;
import org.hwr.genderly.model.tickets.TranslateTicket;
import org.hwr.genderly.model.AnalysisContainer;
import org.hwr.genderly.model.dbHandler.DBHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller containing the logic used to interact with user through web frontend.
 * Takes data and forwards it to the frontend but also awaits user input to process text.
 */
@Controller
@Scope("session")
public class GenderlyController {

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
    private String outputText = "";

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
    public GenderlyController(DBHandler db) {
        db.initialize();
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
        return "main";
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
        // executing only if there really is any text
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
        return "redirect:/";
    }
}