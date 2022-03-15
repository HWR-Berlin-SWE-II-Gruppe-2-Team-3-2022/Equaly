package org.hwr.equaly;

import org.hwr.equaly.controller.setFormatter.SetFormatterImpl;
import org.hwr.equaly.model.Fragment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;

public class SetFormatterTests {

    @Test
    void CreateSubsetsTest() {
        SetFormatterImpl TestSetFormatterImpl = new SetFormatterImpl();
        //Tokens der Beispieleingabe
        String[] tokens = new String[1];
        tokens[0] = "Frau";
        //Tags der Beispieleingabe
        String[] tags = new String[1];
        tags[0] = "NN";
        //erwartete AusgabeFragment
        Fragment testAusgabeFragment0 = new Fragment("Frau", "NN", 0, 0);
        //erwartetes AusgabeFragmentArray
        Fragment[] testAusgabeFragmentArray = new Fragment[1];
        testAusgabeFragmentArray[0]= testAusgabeFragment0;
        //erwartete AusgabeArrayList gefüllt mit AusgabeFragments
        ArrayList<Fragment[]> testsentences = new ArrayList<>();
        testsentences.add(testAusgabeFragmentArray);
        //erwartete Ausgabetestmatrix, für infos => SetFormatterImpl
        Fragment[][] testmatrix = new Fragment[testsentences.size()][];
        //erwartete Returnvalue der Methode
        Fragment[][] testresult = testsentences.toArray(testmatrix);

        //tatsächliches ReturnValue der Methode
        Fragment[][] actualresult = TestSetFormatterImpl.createSubsets(tokens, tags);

        assert(actualresult.equals(testresult));

    }

}
