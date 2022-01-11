package org.hwr.genderly.controller.textMerger;

import org.hwr.genderly.model.AnalysisContainer;
import org.springframework.stereotype.Component;

@Component
public class TextMergerImpl implements TextMerger {

    // TODO: This is to be tested
    @Override
    public String merge(AnalysisContainer analysisContainer) {
        String[][] subSets = analysisContainer.getSubSets();
        StringBuilder result = new StringBuilder();

        for (String[] text: subSets) {
            if (text.length == 3) {
                result.append(" ")
                      .append(text[1]);
            }
        }

        return result.toString();
    }
}