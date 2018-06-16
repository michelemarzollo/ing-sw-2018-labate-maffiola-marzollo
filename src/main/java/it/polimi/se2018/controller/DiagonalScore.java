package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;

/**
 * The class to calculate the score given by the dice which are diagonally
 * adjacent for a certain property on the grid.
 * It's used by the card 'Color Diagonals'.
 *
 * @author michelemarzollo
 * @see this#getScore(Cell[][])
 */
public class DiagonalScore extends PublicObjectiveScore {

    /**
     * The constructor of the class.
     *
     * @param victoryPoints the victory points related to the card.
     * @param propertyIsColour the property of the die on which the card works.
     */
    public DiagonalScore(int victoryPoints, boolean propertyIsColour) {
        super(victoryPoints, propertyIsColour);
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of dice in the grid that are
     * diagonally adjacent to at least one other die with the same property.
     * To be clearer, each die of the grid in position (i, j) adds one (and only one)
     * point to the score if there is a die with the same property in at least one of the
     * following positions: (i-1, j-1), (i+1, j-1), (i-1, j+1), (i+1, j+1).</p>
     * <p>
     * The method uses an auxiliary matrix of the same dimension of {@code grid},
     * with all cells initialized to 0. If the die in position (i, j) has
     * a diagonally adjacent die in position (i-1, j+1) or (i+1, j+1), the corresponding
     * cells of the two (or three) adjacent dice are set to 1 in the auxiliary matrix.
     * The final score is given by the sum of the "1s" in the matrix.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
    public int getScore(Cell[][] grid) {

        //Number of dice that have a diagonally adjacent die of the same property
        int numOfCells = 0;

        //A matrix of the same dimension of grid, that contains 0 if the die in the corresponding
        //cell wasn't still counted, 1 otherwise. Every cell is initialized to 0 by default
        byte[][] countedCells = new byte[grid.length][grid[0].length];

        //If a die in the following row and precedent or following column has the same property of the
        //current one they are both set to 1 in the corresponding cell in countedCells
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (row + 1 < grid.length && col - 1 > 0 && grid[row][col].getDie() != null
                        && grid[row + 1][col - 1].getDie() != null &&
                        getProperty(grid[row][col].getDie()).equals(getProperty((grid[row + 1][col - 1].getDie())))) {

                    countedCells[row][col] = 1;
                    countedCells[row + 1][col - 1] = 1;

                }

                if (row + 1 < grid.length && col + 1 < grid[0].length && grid[row][col].getDie() != null
                        && grid[row + 1][col + 1].getDie() != null &&
                        getProperty(grid[row][col].getDie()).equals(getProperty((grid[row + 1][col + 1].getDie())))) {

                    countedCells[row][col] = 1;
                    countedCells[row + 1][col + 1] = 1;

                }
            }
        }
        //Adds all the "1s" in countedCells to get the final result
        for (byte[] row : countedCells) {
            for (byte elem : row)
                numOfCells += (int) elem;
        }

        return victoryPoints * numOfCells;

    }
}
