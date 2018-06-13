package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * This class represents a tool card in the game.
 * <p>Each tool card is defined by its name and its colour
 * restriction.</p>
 * <p>No thread-safety has yet been implemented.</p>
 * @author dvdmff
 */
public class ToolCard implements Serializable {
    /**
     * The name of the tool card.
     */
    private final String name;

    /**
     * The description of the card.
     */
    private final String description;
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
     * Creates a tool card with the given name and colour
     * restriction.
     * @param name The name of the tool card.
     * @param colour The colour restriction on the tool card.
     */
    public ToolCard(String name, String description, Colour colour) {
        this.name = name;
        this.description = description;
        this.colour = colour;
    }

    /**
     * Copy constructor.
     * @param toolCard The tool card to copy.
     */
    public ToolCard(ToolCard toolCard){
        this.name = toolCard.name;
        this.description = toolCard.description;
        this.colour = toolCard.colour;
        this.used = toolCard.used;
    }

    /**
     * Getter for the name of the tool card.
     * @return The name of the tool card.
     */
    public String getName() {
        return name;
    }

    public String getDescription() { return description; }

    /**
     * Getter for the colour restriction on the tool card.
     * @return The colour restriction on the tool card.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Indicates if the tool card has already been set as used.
     * @return {@code true} if the tool card has already been used
     *         this game; {@code false} otherwise.
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Sets the tool card as used.
     * <p>If the tool card has never been used before, this method updates
     * its status to indicate that it has been used, otherwise the method
     * has no effects.</p>
     */
    public void use() {
        this.used = true;
    }
}
