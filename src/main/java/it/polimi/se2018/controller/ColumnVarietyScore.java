package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;

/**
 * The class to calculate the score related to the property of having all
 * columns with different values for a certain property.
 * <p>
 * It extends the intermediate abstract class {@link ArrayVarietyScore},
 * which contains the method to check if a single column respects
 * the required property.</p>
 * <p>
 * It's used for the cards 'Column Color Variety' and 'Column Color Variety'.</p>
 *
 * @author michelemarzollo
 * @see this#getScore(Cell[][])
 */
public class ColumnVarietyScore extends ArrayVarietyScore {

    /**
     * The constructor of the class.
     *
     * @param victoryPoints the victory points related to the card.
     * @param propertyIsColour the property of the die on which the card works.
     */
    public ColumnVarietyScore(int victoryPoints, boolean propertyIsColour) {
        super(victoryPoints, propertyIsColour);
    }

    /**
     * The score is given by the number of columns on the grid
     * that are complete, and that have all dice with all different values for
     * a certain property, multiplied by the {@code victoryPoints} of the card.
     *
     * @param grid the grid of the {@link it.polimi.se2018.model.Pattern}.
     * @return the score.
     */
    @Override
    protected int getScore(Cell[][] grid) {
        //The number of complete columns with all different property
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

        return victoryPoints * numOfColumns;
    }
}
