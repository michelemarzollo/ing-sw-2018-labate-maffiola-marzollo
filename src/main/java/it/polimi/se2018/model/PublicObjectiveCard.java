package it.polimi.se2018.model;

/**
 * The class for PublicObjectiveCards.
 *
 * @author michelemarzollo
 */
public class PublicObjectiveCard extends ObjectiveCard {

    /**
     * The number of points that a player gains every
     * time that he pursues the objective indicated.
     */
    private int victoryPoints;

    /**
     * The constructor of the class.
     *
     * @param name          the name of the card.
     * @param description   the description of the card.
     * @param victoryPoints the victory points.
     */
    public PublicObjectiveCard(String name, String description, int victoryPoints) {
        super(name, description);
        this.victoryPoints = victoryPoints;
    }

    /**
     * Getter for the victory point of the Public Objective Card.
     *
     * @return the number of points that a player gains every
     * time that he pursues the objective indicated.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }
}
