package it.polimi.se2018.controller;

/**
 * Enum that represents the response of the application of a tool card behaviour to a game.
 */
public enum ToolCardBehaviourResponse {
    FAILURE(false, false), SUCCESS(true, true), CONSUME(false, true), USE(true, false);

    /**
     * Flags to indicate the state of the response.
     */
    private final boolean useTurn;
    private final boolean consumeResources;

    /**
     * Creates a new response according to the parameters.
     *
     * @param useTurn          {@code true} if after the application, the turn must be update.
     * @param consumeResources {@code true} if after the application, the resources used to
     *                         activate the tool card must be consumed.
     */
    ToolCardBehaviourResponse(boolean useTurn, boolean consumeResources) {
        this.useTurn = useTurn;
        this.consumeResources = consumeResources;
    }

    /**
     * Tells if the resources used to activate the tool card have to be consumed.
     *
     * @return {@code true} if after the application, the resources used to
     * activate the tool card must be consumed; {@code false} otherwise.
     */
    public boolean consumeResources() {
        return consumeResources;
    }

    /**
     * Tells if the player can or can't use another tool card this turn.
     *
     * @return {@code true} if after the application, the turn must be update; {@code false}
     * otherwise.
     */
    public boolean useTurn() {
        return useTurn;
    }
}
