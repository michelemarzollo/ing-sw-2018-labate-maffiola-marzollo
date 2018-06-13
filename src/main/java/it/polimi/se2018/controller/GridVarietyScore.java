package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;

import java.util.Arrays;

/**
 * The class to calculate the score linked with the presence of a certain
 * set of objects in the grid.
 * <p>It is used for the following cards: Light Shades, Medium Shades, Deep Shades,
 * Shade Variety, Color Variety.</p>
 *
 * @author michelemarzollo
 * @see this#getScore(Cell[][])
 */
public class GridVarietyScore extends PublicObjectiveScore {

    /**
     * The elements on which the number of complete occurrences
     * must be calculated.
     */
    private final Object[] elements;

    /**
     * The constructor of the class.
     *
     * @param victoryPoints the victory points related to the card.
     * @param propertyIsColour the property of the die on which the card works.
     * @param elements      the elements on which
     */
    GridVarietyScore(int victoryPoints, boolean propertyIsColour, Object[] elements) {
        super(victoryPoints, propertyIsColour);
        this.elements = elements;
    }

    /**
     * Given a set of objects, counts the number of times all objects of the set
     * occur in the whole grid. (It's the number of occurrences of the element
     * among the ones in {@code elements} that occurs fewer times).
     * <p>The score is given by the number of occurrences multiplied by
     * the victory points of the {@link it.polimi.se2018.model.PublicObjectiveCard}</p>
     *
     * @param grid the grid where to count the number of value-shades.
     * @return the number of complete occurrences.
     */
    @Override
    protected int getScore(Cell[][] grid) {
        /*An array that counts the occurrences of the elements in the array "elements"
        in the grid, with a correspondence of positions with elements.
        All cells are initialized to 0 by default*/
        int[] occurrences = new int[elements.length];

        for (Cell[] row : grid) {
            for (Cell cell : row) {
                for (int i = 0; i < elements.length; i++)
                    if (cell.getDie() != null &&
                            getProperty(cell.getDie()).equals(elements[i]))
                        occurrences[i]++;
            }
        }
        //Sorts the occurrences array, so that the minimum (the value on which the result depends)
        //is in position 0
        Arrays.sort(occurrences);
        System.out.println(occurrences[0]);
        return victoryPoints * occurrences[0];
    }

}
