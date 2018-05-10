package it.polimi.se2018.model;

import static it.polimi.se2018.model.PublicObjectiveUtils.numberOfShades;

/**
 * The Singleton for the Shade Variety Objective Card
 *
 * @author michelemarzollo
 */
public class ShadeVariety implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static ShadeVariety instance = null;

    /**
     * The private constructor
     */
    private ShadeVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static ShadeVariety getInstance() {
        if (instance == null)
            instance = new ShadeVariety();
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
        int[] numbers = {1, 2, 3, 4, 5, 6};
        return 5 * numberOfShades(grid, numbers);
    }

}
