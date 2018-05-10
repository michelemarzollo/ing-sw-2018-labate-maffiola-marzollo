package it.polimi.se2018.model;

import static it.polimi.se2018.model.PublicObjectiveUtils.numberOfShades;

/**
 * The Singleton for the Medium Shades Objective Card
 *
 * @author michelemarzollo
 */
public class MediumShades implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static MediumShades instance = null;

    /**
     * The private constructor
     */
    private MediumShades() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static MediumShades getInstance() {
        if (instance == null)
            instance = new MediumShades();
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
        int[] numbers = {3, 4};
        return 2 * numberOfShades(grid, numbers);
    }

}
