package org.hwr.equaly.controller.textMerger;

import org.hwr.equaly.model.AnalysisContainer;

public interface TextMerger {
    /**
     * Take the processed Tokens and line them up to form a text again.
     * @param analysisContainer contains all the tokens that may have been (partially) replaced
     * @return the proposal of a gendered text based on previously analyzed and in part substituted text
     */
    String merge(AnalysisContainer analysisContainer);
}