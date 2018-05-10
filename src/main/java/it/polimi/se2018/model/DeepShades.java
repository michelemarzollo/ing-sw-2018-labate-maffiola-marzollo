package it.polimi.se2018.model;

import static it.polimi.se2018.model.PublicObjectiveUtils.numberOfShades;

/**
 * The Singleton for the Deep Shades Objective Card
 *
 * @author michelemarzollo
 */
public class DeepShades implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static DeepShades instance = null;

    /**
     * The private constructor
     */
    private DeepShades() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static DeepShades getInstance() {
        if (instance == null)
            instance = new DeepShades();
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
        int[] numbers = {5, 6};
        return 2 * numberOfShades(grid, numbers);
    }
}
