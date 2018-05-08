package it.polimi.se2018.model;

import static it.polimi.se2018.model.PublicObjectiveUtils.allDifferentColours;

/**
 * The Singleton for the Row Color Variety Objective Card
 */
public class RowColorVariety implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static RowColorVariety instance = null;

    /**
     * The private constructor
     */
    private RowColorVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static RowColorVariety instance() {
        if (instance == null) return new RowColorVariety();
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
        int numOfRows = 0;

        for (Cell[] row : grid) {
            if (allDifferentColours(row))
                numOfRows++;
        }
        return 6 * numOfRows;
    }

}
