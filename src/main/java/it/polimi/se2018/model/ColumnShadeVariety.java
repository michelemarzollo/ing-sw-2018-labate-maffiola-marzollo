package it.polimi.se2018.model;

/**
 * The Singleton for the Column Shade Variety Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class ColumnShadeVariety extends PublicObjectiveCard {
    /**
     * The instance of the Singleton.
     */
    private static ColumnShadeVariety instance = null;

    /**
     * The private constructor.
     */
    private ColumnShadeVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static ColumnShadeVariety getInstance() {
        if (instance == null)
            instance = new ColumnShadeVariety();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of complete columns (with no
     * empty cells) with dice of all different values, multiplied by 4.</p>
     * <p>
     * The method creates an array for each column and invokes the method
     * {@code allDifferentValues} of the superclass on each array, counting
     * how many of them have the required property. Then multiplies by 4.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {

        int numOfColumns = 0;

        Cell[] columnArray = new Cell[grid.length];

        //For each column I create an equivalent array
        for (int col = 0; col < grid[0].length; col++) {
            for (int row = 0; row < grid.length; row++) {
                columnArray[row] = grid[row][col];
            }
            if (allDifferentValues(columnArray))
                numOfColumns++;
        }

        return 4 * numOfColumns;
    }

    @Override
    public String getName() {
        return "ColumnShadeVariety";
    }

}
