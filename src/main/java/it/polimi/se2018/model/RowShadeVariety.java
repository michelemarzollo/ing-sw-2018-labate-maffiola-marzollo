package it.polimi.se2018.model;


import static it.polimi.se2018.model.PublicObjectiveUtils.allDifferentValues;

/**
 * The Singleton for the Row Shade Variety Objective Card
 *
 * @author michelemarzollo
 */
public class RowShadeVariety implements PublicObjectiveCard {
    /**
     * The instance of the Singleton
     */
    private static RowShadeVariety instance = null;

    /**
     * The private constructor
     */
    private RowShadeVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static RowShadeVariety getInstance() {
        if (instance == null) instance = new RowShadeVariety();
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
            if (allDifferentValues(row))
                numOfRows++;
        }
        return 5 * numOfRows;
    }

}
