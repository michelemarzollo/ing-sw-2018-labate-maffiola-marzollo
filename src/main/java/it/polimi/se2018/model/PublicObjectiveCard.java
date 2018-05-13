package it.polimi.se2018.model;

import java.util.Arrays;

/**
 * An interface for PublicObjectiveCards
 */
public abstract class PublicObjectiveCard implements ObjectiveCard {

    /**
     * The method that controls if in an array of Cells all values of the dice are different
     *
     * @param cellArray The array
     * @return {@code true} if all cells are not null and have dice with different
     * values, {@code false} otherwise
     */
    protected boolean allDifferentValues(Cell[] cellArray) {
        for (int i = 0; i < cellArray.length - 1; i++) {
            for (int j = i + 1; j < cellArray.length; j++) {
                Die die1 = cellArray[i].getDie();
                Die die2 = cellArray[j].getDie();
                if (die1 == null || die2 == null || die1.getValue() == die2.getValue())
                    return false;
            }
        }
        return true;
    }

    /**
     * The method that controls if in an array of Cells all colours of the dice are different
     *
     * @param cellArray The array
     * @return {@code true} if all cells are not null and have dice with different
     * colours, {@code false} otherwise
     */
    protected boolean allDifferentColours(Cell[] cellArray) {
        for (int i = 0; i < cellArray.length - 1; i++) {
            for (int j = i + 1; j < cellArray.length; j++) {
                Die die1 = cellArray[i].getDie();
                Die die2 = cellArray[j].getDie();
                if (die1 == null || die2 == null || die1.getColour() == die2.getColour())
                    return false;
            }
        }
        return true;
    }

    /**
     * Counts the number shades, i.e. the number of occurrences
     * of the value between the ones in {@code numbers} that occurs fewer times
     *
     * @param grid    The grid where to count the number of value-shades
     * @param numbers The array of numbers to count
     * @return The number of shades
     */
    protected int numberOfShades(Cell[][] grid, int[] numbers) {
        /*An array that counts the occurrences of the numbers in the array "numbers"
        in the grid, with a correspondence of positions with numbers.
        All cells are initialized to 0 by default*/
        int[] occurrences = new int[numbers.length];

        for (Cell[] row : grid) {
            for (Cell cell : row) {
                for (int i = 0; i < numbers.length; i++)
                    if (cell.getDie() != null &&
                            cell.getDie().getValue() == numbers[i])
                        occurrences[i]++;
            }
        }
        //Sorts the occurrences array, so that the minimum (the value to return) is in position 0
        Arrays.sort(occurrences);
        return occurrences[0];
    }

    /**
     * Counts the number of complete occurrences of the five colours
     *
     * @param grid The grid where to count the number of value-shades
     * @return The number of total occurrences
     */
    protected int numberOfColours(Cell[][] grid) {
        /*An array that counts the occurrences of the colours
        in the grid, with a correspondence of positions with the colours of the enum Colours.
        All cells are initialized to 0 by default*/
        int[] occurrences = new int[Colour.values().length];

        for (Cell[] row : grid) {
            for (Cell cell : row) {
                for (int i = 0; i < occurrences.length; i++)
                    if (cell.getDie() != null &&
                            cell.getDie().getColour() == Colour.values()[i])
                        occurrences[i]++;
            }
        }
        //Sorts the occurrences array, so that the minimum (the value to return) is in position 0
        Arrays.sort(occurrences);
        return occurrences[0];
    }

}
