package org.hwr.genderly.model.tickets;

/**
 * Used to gather user input from the frontend
 * currently focused solely on the containing of user-entered 'to be analysed' text
 */
public class TranslateTicket {

    private String inputText = "";

    // Thymeleaf got freaked out without explicit Getter and Setter
    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
}
