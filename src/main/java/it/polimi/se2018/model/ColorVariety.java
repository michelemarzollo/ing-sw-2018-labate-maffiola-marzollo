package it.polimi.se2018.model;

import java.util.Arrays;

/**
 * The Singleton for the Color Variety Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class ColorVariety extends PublicObjectiveCard {

    /**
     * The instance of the Singleton.
     */
    private static ColorVariety instance = null;

    /**
     * The private constructor.
     */
    private ColorVariety() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static ColorVariety getInstance() {
        if (instance == null)
            instance = new ColorVariety();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is calculated as the score given by {@code numberOfColours()}
     * multiplied by 4. See the documentation of {@code numberOfColours()} for
     * further details.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {
        return 4 * numberOfColours(grid);
    }

    /**
     * Counts the number of complete occurrences of the colours of the
     * enumeration {@link Colour}, i.e. the number of occurrences of the colour
     * that appears fewer times. (If a colour never occurs, that number is 0).
     *
     * @param grid the grid where to count the number of complete occurrences of
     *             all colours.
     * @return the number of total occurrences.
     */
    private int numberOfColours(Cell[][] grid) {
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
