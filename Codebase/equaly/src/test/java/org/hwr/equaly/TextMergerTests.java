package org.hwr.equaly;

import org.hwr.equaly.controller.textMerger.TextMerger;
import org.hwr.equaly.controller.textMerger.TextMergerImpl;
import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.Fragment;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Objects;

class TextMergerTests {

    @Test
     void mergeTest(){
        TextMergerImpl TestMergerImpl = new TextMergerImpl();
        Fragment testFragment = new Fragment("Ein","m",1,1);
        Fragment testFragment2 = new Fragment("Hund","f",2,2);
        Fragment[][] testFragment2D = new Fragment[2][1];
        testFragment2D[0][0] = testFragment;
        testFragment2D[1][0] = testFragment2;
        AnalysisContainer testContainer = new AnalysisContainer(testFragment2D);

        System.out.println(TestMergerImpl.merge(testContainer));

        String compare1 = TestMergerImpl.merge(testContainer);
        String compare2 = "Ein Hund ";

        assert(compare1.equals(compare2));
    }

}