package it.polimi.se2018.model;

/**
 * The Singleton for the Row Color Variety Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class RowColorVariety extends PublicObjectiveCard {

    /**
     * The instance of the Singleton.
     */
    private static RowColorVariety instance = null;

    /**
     * The private constructor.
     */
    private RowColorVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static RowColorVariety getInstance() {
        if (instance == null)
            instance = new RowColorVariety();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of complete rows (with no
     * empty cells) with dice of all different colours, multiplied by 6.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {

        int numOfRows = 0;

        for (Cell[] row : grid) {
            if (allDifferentColours(row))
                numOfRows++;
        }
        return 6 * numOfRows;
    }

}
