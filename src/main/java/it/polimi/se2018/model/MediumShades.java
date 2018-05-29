package it.polimi.se2018.model;

/**
 * The Singleton for the Medium Shades Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class MediumShades extends PublicObjectiveCard {

    /**
     * The instance of the Singleton.
     */
    private static MediumShades instance = null;

    /**
     * The private constructor.
     */
    private MediumShades() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static MediumShades getInstance() {
        if (instance == null)
            instance = new MediumShades();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of couples of dice with value
     * 3 and 4, multiplied by 2. Each die must be counted only once.</p>
     * <p>
     * More easily, it is the number of occurrences of the value that
     * occurs fewer times between 3 and 4, multiplied by 2.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {
        int[] numbers = {3, 4};
        return 2 * numberOfShades(grid, numbers);
    }

    @Override
    public String getName() {
        return "MediumShades";
    }

}
