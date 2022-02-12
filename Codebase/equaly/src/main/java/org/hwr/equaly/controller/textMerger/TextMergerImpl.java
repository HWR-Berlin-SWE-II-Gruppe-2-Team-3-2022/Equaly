package org.hwr.equaly.controller.textMerger;

import org.hwr.equaly.model.AnalysisContainer;
import org.springframework.stereotype.Component;

@Component
public class TextMergerImpl implements TextMerger {

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