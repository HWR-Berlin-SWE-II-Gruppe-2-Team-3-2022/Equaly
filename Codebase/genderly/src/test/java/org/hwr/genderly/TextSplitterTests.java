package org.hwr.genderly;

import org.hwr.genderly.controller.textSplitter.TextSplitterImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TextSplitterTests {

    private TextSplitterImpl textSplitter = new TextSplitterImpl();

    @Test
    void emptyTextProcessing(){
        String[][] result = textSplitter.createSubsets("");
        assert(result.length == 1);
    }

    @Test
    void oneWordTextProcessing(){
        String[][] result = textSplitter.createSubsets("Word");
            assert(result[0][1].equals("Word"));
    }

    @Test
    void pointTextProcessing(){
        String[][] result = textSplitter.createSubsets(".");
        assert(result[0][1].equals("."));
    }

    @Test
    void questionmarkTextProcessing(){
        String[][] result = textSplitter.createSubsets("?");
        assert(result[0][1].equals("?"));
    }

    @Test
    void centerSpaceTextProcessing() {
        String[][] result = textSplitter.createSubsets("Ein Test");
        assert(result[0][1].equals("Ein"));
        assert(result[1][1].equals("Test"));
    }

    @Test
    void fullCorrectSentenceProcessing() {
        String[][] result = textSplitter.createSubsets("Ein ist ein Satz, der erst jetzt beendet ist.");

        assert(result.length == 9);
        assert(result[3][1].equals("Satz,"));
        assert(result[8][1].equals("ist."));
    }

}
