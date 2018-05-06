package it.polimi.se2018.model;

import java.util.stream.IntStream;

/**
 * The Singleton for the Color Diagonals Objective Card
 */
public class ColorDiagonals implements PublicObjectiveCard {

    /**
     * The instance of the Singleton
     */
    private static ColorDiagonals instance = null;

    /**
     * The private constructor
     */
    private ColorDiagonals() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one
     *
     * @return The instance
     */
    public static ColorDiagonals instance() {
        if (instance == null) return new ColorDiagonals();
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

        //Number of dice that have a diagonally adjacent die of the same colour
        int numOfCells = 0;

        //A matrix of the same dimension of grid, that contains 0 if the die in the corresponding
        //cell wasn't still counted, 1 otherwise. Every cell is initialized to 0 by default
        int[][] countedCells = new int[grid.length][grid[0].length];

        //If a die in the following row and precedent or following column has the same colour of the
        //current one they are both set to 1 in the corresponding cell in countedCells
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (row + 1 < grid.length && col - 1 > 0 && grid[row][col].getDie() != null
                        && grid[row + 1][col - 1].getDie() != null &&
                        grid[row][col].getDie().getColour().equals(grid[row + 1][col - 1].getDie().getColour())) {
                    countedCells[row][col] = 1;
                    countedCells[row + 1][col - 1] = 1;
                }
                if (row + 1 < grid.length && col + 1 < grid[0].length && grid[row][col].getDie() != null
                        && grid[row + 1][col + 1].getDie() != null &&
                        grid[row][col].getDie().getColour().equals(grid[row + 1][col + 1].getDie().getColour())) {
                    countedCells[row][col] = 1;
                    countedCells[row + 1][col + 1] = 1;
                }
            }
        }
        //Adds all the "1s" in countedCells to get the final result
        for (int i = 0; i < grid[0].length; i++) {
            numOfCells += IntStream.of(countedCells[i]).sum();
        }
        return numOfCells;
    }

}
