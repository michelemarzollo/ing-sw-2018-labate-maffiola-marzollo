package it.polimi.se2018.model;

/**
 * @author dvdmff
 * This class represents a tool card in the game.
 */
public class ToolCard {
    /**
     * The name of the tool card.
     */
    private final String name;
    /**
     * The colour restriction on the tool card.
     */
    private final Colour colour;
    /**
     * Flag to indicate if the tool card has already been
     * used during the game.
     */
    private boolean used;

    /**
     * Constructs a tool card with the given name and colour
     * restriction.
     * @param name The name of the tool card.
     * @param colour The colour restriction on the toolcard.
     */
    public ToolCard(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
    }

    /**
     * Getter for the name of the tool card.
     * @return The name of the tool card.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the colour restriction on the tool card.
     * @return The colour restriction on the tool card.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Indicates if the tool card has already been used during
     * the game.
     * @return {@code true} if the tool card has already been used
     *         this game; {@code false} otherwise.
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * If the tool card has never been used before, this method updates
     * its status to indicate that it has been used, otherwise the method
     * has no effects.
     */
    public void use() {
        this.used = true;
    }
}
