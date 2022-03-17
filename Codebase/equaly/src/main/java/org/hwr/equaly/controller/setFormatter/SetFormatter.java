package org.hwr.equaly.controller.setFormatter;

import org.hwr.equaly.model.Fragment;

public interface SetFormatter {
    /**
     * Given both a set of tokens and an (ideally) equally sized set of associated taggings, safely merge both these
     * sets together into one 2D structure of Fragment objects for further processing
     * @param tokens these were made from the input text
     * @param tags these were made from the input text's tokens
     * @return a unified data structure now enabling replacement
     */
    Fragment[][] createSubsets(String[] tokens, String[] tags);
}
