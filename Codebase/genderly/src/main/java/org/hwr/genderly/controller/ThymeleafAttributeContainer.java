package org.hwr.genderly.controller;

import org.springframework.stereotype.Component;

/**
 * Carrying all internally required Thymeleaf-specific attribute names
 * in order to reduce interactions with hard-coded copy-Strings.
 */
@Component
public class ThymeleafAttributeContainer {
    public final String APPNAME = "appName";
    public final String TRANSLATE = "translateTicket";
    public final String OUTPUT = "outputText";
}
