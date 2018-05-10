package it.polimi.se2018.model;

import static it.polimi.se2018.model.PublicObjectiveUtils.allDifferentColours;

/**
 * The Singleton for the Column Color Variety Objective Card
 */
public class ColumnColorVariety implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static ColumnColorVariety instance = null;

    /**
     * The private constructor
     */
    private ColumnColorVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static ColumnColorVariety getInstance() {
        if (instance == null)
            instance = new ColumnColorVariety();
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

}
