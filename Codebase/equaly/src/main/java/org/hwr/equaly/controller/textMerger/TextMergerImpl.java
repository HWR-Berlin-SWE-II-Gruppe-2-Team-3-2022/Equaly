package org.hwr.equaly.controller.textMerger;

import org.hwr.equaly.model.AnalysisContainer;
import org.hwr.equaly.model.Fragment;
import org.springframework.stereotype.Component;

@Component
public class TextMergerImpl implements TextMerger {

    @Override
    public String merge(AnalysisContainer analysisContainer) {
        Fragment[][] subSets = analysisContainer.getSubSets();
        StringBuilder result = new StringBuilder();

        for (Fragment[] sentence: subSets) {
            for (Fragment word: sentence) {
                result.append(" ").append(word.token);
            }
        }

        return result.toString();
    }
}