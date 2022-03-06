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
                    result.append(subSet[j].token.substring(0, 1).toUpperCase()).append(subSet[j].token.substring(1)).append(" ");
                } else {
                    if (",;.?!".contains(subSet[j].token)) {
                        result.deleteCharAt(result.length()-1);
                    }
                    result.append(subSet[j].token).append(" ");
                }
            }
        }
        return result.toString().replaceAll(" / ", "/");
    }
}