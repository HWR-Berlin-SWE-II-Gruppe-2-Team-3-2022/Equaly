package org.hwr.equaly;

import org.hwr.equaly.controller.setFormatter.SetFormatterImpl;
import org.hwr.equaly.model.Fragment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Objects;

public class SetFormatterTests {

    @Test
    void CreateSubsetsTest() {
        SetFormatterImpl TestSetFormatterImpl0 = new SetFormatterImpl();
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
        Fragment[][] actualresult = TestSetFormatterImpl0.createSubsets(tokens, tags);

        //Prüfen auf inhaltliche Gleichheit
        boolean eq1 = Objects.equals(testresult[0][0].token, actualresult[0][0].token);
        assert(eq1);
        boolean eq2 = Objects.equals(testresult[0][0].tag, actualresult[0][0].tag);
        assert(eq2);
        boolean eq3 = Objects.equals(testresult[0][0].index, actualresult[0][0].index);
        assert(eq3);

    }

}
