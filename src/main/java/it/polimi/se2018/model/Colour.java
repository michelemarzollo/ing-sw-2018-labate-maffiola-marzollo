package it.polimi.se2018.model;

/**
 * Enumeration for the possible colours in the game.
 */
public enum Colour {
    BLUE, GREEN, PURPLE, RED, YELLOW;

    /**
     * Builds a string containing the name of the enum value with
     * capitalization.
     * @return The capitalized name of the enum value.
     */
    @Override
    public String toString(){
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
