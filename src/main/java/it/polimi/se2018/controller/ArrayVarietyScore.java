package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;

/**
 * The class that contains a useful method for it's subclasses
 * to see if all elements of an array of cell are different for
 * a certain property.
 *
 * @author michelemarzollo
 * @see ColumnVarietyScore
 * @see RowVarietyScore
 */
public abstract class ArrayVarietyScore extends PublicObjectiveScore {

    /**
     * The constructor of the class.
     *
     * @param victoryPoints the victory points related to the card.
     * @param property      the property of the die on which the card works.
     */
    ArrayVarietyScore(int victoryPoints, Property property) {
        super(victoryPoints, property);
    }

    /**
     * The method that controls if in an array of Cells all values or colours
     * of the dice are different, and there are no empty cells.
     *
     * @param cellArray the array.
     * @return {@code true} if all cells are not null and have dice with different
     * values or colours, {@code false} otherwise.
     */
    protected boolean allDifferentValues(Cell[] cellArray, Property property) {
        for (int i = 0; i < cellArray.length - 1; i++) {
            for (int j = i + 1; j < cellArray.length; j++) {
                Die die1 = cellArray[i].getDie();
                Die die2 = cellArray[j].getDie();
                if (die1 == null || die2 == null || getProperty(die1).equals(getProperty(die2)))
                    return false;
            }
        }
        return true;
    }

}
