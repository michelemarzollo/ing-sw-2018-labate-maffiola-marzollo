package it.polimi.se2018.controller;

import it.polimi.se2018.model.PublicObjectiveCard;

/**
 * The class containing the {@link PublicObjectiveCard}s of the game and
 * the corresponding {@link PublicObjectiveScore}s to set i the controller.
 * It's used to be returned by the XmlLoader, since it must work on two different
 * kind of elements.
 *
 * @author michelemarzollo
 */
public class PublicObjectiveElements {

    /**
     * The array of cards.
     */
    private PublicObjectiveCard[] cards;

    /**
     * The array of scoreCalculators.
     */
    private PublicObjectiveScore[] scoreCalculators;

    /**
     * The constructor of the class
     * @param cards The array of cards.
     * @param scoreCalculators The array of scoreCalculators.
     */
    PublicObjectiveElements(PublicObjectiveCard[] cards, PublicObjectiveScore[] scoreCalculators){
        this.cards = cards;
        this.scoreCalculators = scoreCalculators;
    }

    /**
     * The getter for {@code scoreCalculators}.
     * @return the array of cards.
     */
    public PublicObjectiveCard[] getCards() {
        return cards;
    }

    /**
     * The getter for {@code cards}.
     * @return the array of scoreCalculators.
     */
    public PublicObjectiveScore[] getScoreCalculators() {
        return scoreCalculators;
    }
}
