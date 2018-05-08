package it.polimi.se2018.model;

import static it.polimi.se2018.model.PublicObjectiveUtils.numberOfShades;

/**
 * The Singleton for the Light Shades Objective Card
 */
public class LightShades implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static LightShades instance = null;

    /**
     * The private constructor
     */
    private LightShades() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static LightShades instance() {
        if (instance == null) return new LightShades();
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
        int[] numbers = {1, 2};
        return 2 * numberOfShades(grid, numbers);
    }

}
