package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;

/**
 * The abstract class to calculate the score related to a single
 * {@link it.polimi.se2018.model.PublicObjectiveCard}. It is extended by
 * the classes that implement the method {@code getScore({@link Cell}[][])},
 * to allow the usage of the design pattern Strategy.
 *
 * @author michelemarzollo
 */
public abstract class PublicObjectiveScore {

    /**
     * The victory points related to the {@link it.polimi.se2018.model.PublicObjectiveCard}.
     */
    protected final int victoryPoints;

    /**
     * The property of the die on which the card works. It can be either the value
     * or the colour of the die.
     */
    protected final Property property;

    /**
     * The constructor of the class.
     *
     * @param victoryPoints the victory points related to the card.
     * @param property      the property of the die on which the card works.
     */
    PublicObjectiveScore(int victoryPoints, Property property) {
        this.victoryPoints = victoryPoints;
        this.property = property;
    }

    /**
     * The method to calculate the score on the final {@link it.polimi.se2018.model.Pattern}
     * related to the {@link it.polimi.se2018.model.PublicObjectiveCard}.
     *
     * @param grid the grid of the Pattern.
     * @return the score.
     */
    protected abstract int getScore(Cell[][] grid);

    /**
     * Gets the attribute of the die related to the property of the card:
     * if the card work on colours it'a the colour, otherwise it's the value.
     *
     * @param die the die we are interested in.
     * @return the colour or value of the die.
     */
    protected Object getProperty(Die die) {
        if (property == Property.COLOUR)
            return die.getColour();
        else return die.getValue();
    }

}
