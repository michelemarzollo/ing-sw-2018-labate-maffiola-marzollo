package it.polimi.se2018.view.cli;

/**
 * Represents an option in a textual menu.
 */
class Option {
    /**
     * The prompt message for this option.
     */
    private final String prompt;
    /**
     * The handler to manage this option.
     */
    private final Runnable handler;

    /**
     * Creates a new Option with the specified data.
     *
     * @param prompt  The prompt message for this option.
     * @param handler The handler to manage this option.
     */
    Option(String prompt, Runnable handler) {
        this.prompt = prompt;
        this.handler = handler;
    }

    /**
     * Getter for the prompt message.
     *
     * @return The prompt message.
     */
    String getPrompt() {
        return prompt;
    }

    /**
     * Getter for the option handler.
     *
     * @return The option handler.
     */
    Runnable getHandler() {
        return handler;
    }
}
