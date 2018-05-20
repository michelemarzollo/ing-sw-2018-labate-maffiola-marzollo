package it.polimi.se2018.model;


/**
 * The Singleton for the Row Shade Variety Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class RowShadeVariety extends PublicObjectiveCard {
    /**
     * The instance of the Singleton.
     */
    private static RowShadeVariety instance = null;

    /**
     * The private constructor.
     */
    private RowShadeVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static RowShadeVariety getInstance() {
        if (instance == null) instance = new RowShadeVariety();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of complete rows (with no
     * empty cells) with dice of all different values, multiplied by 5.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {

        int numOfRows = 0;

        for (Cell[] row : grid) {
            if (allDifferentValues(row))
                numOfRows++;
        }
        return 5 * numOfRows;
    }

}
