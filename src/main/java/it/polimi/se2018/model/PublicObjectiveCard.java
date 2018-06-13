package it.polimi.se2018.model;

/**
 * The class for PublicObjectiveCards.
 *
 * @author michelemarzollo
 */
public class PublicObjectiveCard implements ObjectiveCard {

    /**
     * The name of the Public Objective Card
     */
    private String name;

    /**
     * The description of the card: it resumes synthetically the
     * goal the player should aim for, to get a good score.
     */
    private String description;

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
        this.name = name;
        this.description = description;
        this.victoryPoints = victoryPoints;
    }

    /**
     * The getter for the name.
     *
     * @return the name of the card.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * The getter for the description.
     *
     * @return the description of the card.
     */
    @Override
    public String getDescription() {
        return description;
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
