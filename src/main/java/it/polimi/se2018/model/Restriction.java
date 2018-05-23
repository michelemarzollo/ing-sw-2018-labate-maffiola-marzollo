package it.polimi.se2018.model;

/**
 * This enum represents the three possible restriction
 * that have to be satisfied in the use of
 * 'Eglomise Brush', 'Copper Foil Burnisher' and
 * 'Cork-backed Straightedge' {@link ToolCard}.
 */
public enum Restriction {
    ONLY_COLOUR(true, false),
    ONLY_VALUE(false, true),
    NOT_ADJACENT(true, true),
    DEFAULT(true, true);

    /**
     * Flag for colour constraints.
     */
    private final boolean checkColour;

    /**
     * Flag for value constraints.
     */
    private final boolean checkValue;

    /**
     * Creates an entry with the specified mandatory constraints.
     * @param checkColour {@code true} if colour constraints are mandatory.
     * @param checkValue {@code true} if value constraints are mandatory.
     */
    Restriction(boolean checkColour, boolean checkValue) {
        this.checkColour = checkColour;
        this.checkValue = checkValue;
    }

    /**
     * Tells if the restriction forces colour constraints.
     *
     * @return {@code true} if colour constraint are mandatory;
     * {@code false} otherwise.
     */
    public boolean checkColourConstraint() {
        return checkColour;
    }

    /**
     * Tells if the restriction forces value constraints.
     *
     * @return {@code true} if value constraint are mandatory;
     * {@code false} otherwise.
     */
    public boolean checkValueConstraint() {
        return checkValue;
    }
}