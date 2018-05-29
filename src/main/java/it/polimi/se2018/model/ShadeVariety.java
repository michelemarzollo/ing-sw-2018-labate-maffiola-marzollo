package it.polimi.se2018.model;

/**
 * The Singleton for the Shade Variety Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class ShadeVariety extends PublicObjectiveCard {

    /**
     * The instance of the Singleton.
     */
    private static ShadeVariety instance = null;

    /**
     * The private constructor.
     */
    private ShadeVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static ShadeVariety getInstance() {
        if (instance == null)
            instance = new ShadeVariety();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of complete occurrences of all values
     * in the grid, multiplied by 5. Each die must be counted only once.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {
        int[] numbers = {1, 2, 3, 4, 5, 6};
        return 5 * numberOfShades(grid, numbers);
    }

    @Override
    public String getName() {
        return "ShadeVariety";
    }

}
