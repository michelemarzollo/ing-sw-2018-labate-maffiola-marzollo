package it.polimi.se2018.model;

/**
 * The Singleton for the Column Color Variety Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class ColumnColorVariety extends PublicObjectiveCard {

    /**
     * The instance of the Singleton.
     */
    private static ColumnColorVariety instance = null;

    /**
     * The private constructor.
     */
    private ColumnColorVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static ColumnColorVariety getInstance() {
        if (instance == null)
            instance = new ColumnColorVariety();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of complete columns (with no
     * empty cells) with dice of all different colours, multiplied by 5.</p>
     * <p>
     * The method creates an array for each column and invokes the method
     * {@code allDifferentColours} of the superclass on each array, counting
     * how many of them have the required property. Then multiplies by five.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {
        //The number of complete columns with all different colours
        int numOfColumns = 0;

        Cell[] columnArray = new Cell[grid.length];

        //For each column I create an equivalent array
        for (int col = 0; col < grid[0].length; col++) {
            for (int row = 0; row < grid.length; row++) {
                columnArray[row] = grid[row][col];
            }
            if (allDifferentColours(columnArray))
                numOfColumns++;
        }

        return 5 * numOfColumns;
    }

    /**
     * Getter for the name of the Objective Card.
     * @return the card's name.
     */
    @Override
    public String getName() {
        return "Column Color Variety";
    }


    /**
     * Getter for the description of the Objective Card.
     * @return the card's description.
     */
    @Override
    public String getDescription() {
        return "Columns with no repeated colors";
    }

    /**
     * Getter for the victory point of the Public Objective Card.
     * @return the number of points that a player gains every
     * time that he pursue the objective indicated.
     */
    @Override
    public int getVictoryPoint() {
        return 5;
    }
}
