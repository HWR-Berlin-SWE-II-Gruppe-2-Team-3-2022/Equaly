package org.hwr.equaly;

import com.github.pemistahl.lingua.api.Language;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.hwr.equaly.controller.posTagger.POSTagger;
import org.hwr.equaly.controller.posTagger.POSTaggerImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

public class POSTaggerTests {

    @Test
    void runTest(){

        POSTaggerImpl POSTaggerTest = new POSTaggerImpl();
        //Eingabedaten für den Test
        Language testlanguage = Language.GERMAN;
        String[] testtokens = new String[1];
        testtokens[0] = "Mann";
        //initialisierung für fehlerfreie Ausführung nötig
        POSTaggerTest.initialize();
        //erwarteter ReturnValue der Methode
        String[] testresult = new String[1];
        testresult[0] = "NN";

        //tatsächlicher ReturnValue der Methode
        String[] actualresult = POSTaggerTest.run(testlanguage, testtokens);


        boolean eq1 = testresult[0].equals(actualresult[0]);
        assert(eq1);

    }


}
