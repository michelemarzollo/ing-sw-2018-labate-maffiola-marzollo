package it.polimi.se2018.model;

/**
 * PrivateObjectiveCard represents all private objective cards.
 * <p>
 * The card, if it is assigned, is associated to a player, and it gives
 * the player a score at the and of the game.</p>
 *
 * @author michelemarzollo
 */
public class PrivateObjectiveCard extends ObjectiveCard {

    /**
     * The colour of the dice that will count int the score of the card.
     */
    private final Colour colour;

    /**
     * The constructor of the class.
     *
     * @param name        the name of the card.
     * @param colour      the colour of the card.
     * @param description the description of the card.
     */
    public PrivateObjectiveCard(String name, Colour colour, String description) {
        super(name, description);
        this.colour = colour;
    }

    /**
     * The getter for the colour.
     *
     * @return the card's colour.
     */
    public Colour getColour() {
        return colour;
    }

}
