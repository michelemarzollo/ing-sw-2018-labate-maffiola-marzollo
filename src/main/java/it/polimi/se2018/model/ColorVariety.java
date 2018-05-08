package it.polimi.se2018.model;

import static it.polimi.se2018.model.PublicObjectiveUtils.numberOfColours;

/**
 * The Singleton for the Color Variety Objective Card
 */
public class ColorVariety implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static ColorVariety instance = null;

    /**
     * The private constructor
     */
    private ColorVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static ColorVariety instance() {
        if (instance == null) return new ColorVariety();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card
     *
     * @param grid The grid on which the score must be calculated
     * @return The score
     */
    @Override
    public int getScore(Cell[][] grid) {
        return 4 * numberOfColours(grid);
    }

}
