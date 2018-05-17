package it.polimi.se2018.model.events;

/**
 * This enum represent the possible types of model updates.
 */
public enum ModelEvent {
    NEXT_TURN("NEXT"), GAME_SETUP("GAME-SETUP"), GAME_END("END"), PATTERN_SELECTION("SELECTION"),
    MOVE_UPDATE("MOVE"), ROUND_SETUP("ROUND-SETUP"), PLAYER_CONNECTION("CONNECTION");

    /**
     * The label associated with the event type.
     */
    private String label;

    /**
     * Creates a new enum entry with the given label.
     * @param label The label of the event type.
     */
    ModelEvent(String label) {
        this.label = label;
    }

    /**
     * Represents the enum entry as a string.
     * @return A string containing the label of the event type.
     */
    @Override
    public String toString() {
        return label;
    }
}
