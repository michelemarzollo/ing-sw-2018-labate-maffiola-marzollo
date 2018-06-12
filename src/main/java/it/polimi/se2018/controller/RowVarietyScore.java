package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;

/**
 * The class to calculate the score related to the property of having all
 * rows with different values for a certain property.
 * <p>
 * It extends the intermediate abstract class {@link ArrayVarietyScore},
 * which contains the method to check if a single row respects
 * the required property.</p>
 * <p>
 * It's used for the cards 'Row Color Variety' and 'Row Color Variety'.</p>
 *
 * @author michelemarzollo
 * @see this#getScore(Cell[][])
 */
public class RowVarietyScore extends ArrayVarietyScore {

    /**
     * The constructor of the class.
     *
     * @param victoryPoints the victory points related to the card.
     * @param property      the property of the die on which the card works.
     */
    RowVarietyScore(int victoryPoints, Property property) {
        super(victoryPoints, property);
    }

    /**
     * The score is given by the number of rows on the grid
     * that are complete, and that have all dice with all different values for
     * a certain property, multiplied by the {@code victoryPoints} of the card.
     *
     * @param grid the grid of the {@link it.polimi.se2018.model.Pattern}.
     * @return the score.
     */
    @Override
    protected int getScore(Cell[][] grid) {
        int numOfRows = 0;

        for (Cell[] row : grid) {
            if (allDifferentValues(row, property))
                numOfRows++;
        }
        return victoryPoints * numOfRows;
    }
}
