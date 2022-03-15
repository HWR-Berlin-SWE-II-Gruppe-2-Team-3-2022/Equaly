package org.hwr.equaly;

import com.github.pemistahl.lingua.api.Language;
import org.hwr.equaly.controller.languageTagger.LanguageTagger;
import org.hwr.equaly.controller.languageTagger.LanguageTaggerImpl;
import org.junit.jupiter.api.Test;

public class LanguageTaggerTests {

    @Test
    void getLanguage(){

        LanguageTaggerImpl TestLanguageTagger = new LanguageTaggerImpl();
        TestLanguageTagger.initialize();

        String testtext = "Junge";

        Language actualresult = TestLanguageTagger.getLanguage(testtext);
        Language expectedresult = Language.GERMAN;

        boolean eq1 = expectedresult == actualresult;
        assert(eq1);


    }

}
