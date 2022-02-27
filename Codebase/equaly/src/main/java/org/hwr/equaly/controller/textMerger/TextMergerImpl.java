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

        for (Fragment[] subSet : subSets) {
            for (int j = 0; j < subSet.length; j++) {
                if (j == 0) {
                    result.append(subSet[j].token.substring(0, 1).toUpperCase()).append(subSet[j].token.substring(1));
                } else if (j == subSet.length - 1 && ".!?;:".contains(subSet[j].token)) {
                    result.append(subSet[j].token).append(" ");
                } else {
                    result.append(" ").append(subSet[j].token);
                }
            }
        }
        return result.toString();
    }
}