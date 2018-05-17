package it.polimi.se2018.model.events;

public enum ModelEvent {
    NEXT_TURN("NEXT"), GAME_SETUP("GAME-SETUP"), GAME_END("END"), PATTERN_SELECTION("SELECTION"),
    MOVE_UPDATE("MOVE"), ROUND_SETUP("ROUND-SETUP"), PLAYER_CONNECTION("CONNECTION");

    private String label;

    ModelEvent(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
