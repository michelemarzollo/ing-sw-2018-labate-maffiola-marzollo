package it.polimi.se2018.model;


/**
 * The Singleton for the Color Diagonals Objective Card.
 * <p>
 * See the documentation of {@link PublicObjectiveCard} for
 * further information.</p>
 *
 * @author michelemarzollo
 */
public class ColorDiagonals extends PublicObjectiveCard {

    /**
     * The instance of the Singleton.
     */
    private static ColorDiagonals instance = null;

    /**
     * The private constructor.
     */
    private ColorDiagonals() {
    }

    /**
     * The method to return the new instance of the class,
     * or whether it already exists, the already existing one.
     *
     * @return the instance.
     */
    public static ColorDiagonals getInstance() {
        if (instance == null)
            instance = new ColorDiagonals();
        return instance;
    }

    /**
     * The method that calculates the score associated to the card.
     * <p>
     * The score is given by the number of dice in the grid that are
     * diagonally adjacent to at least one other die of the same colour.
     * To be clearer, each die of the grid in position (i, j) adds one (and only one)
     * point to the score if there is a die of the same colour in at least one of the
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

        //Number of dice that have a diagonally adjacent die of the same colour
        int numOfCells = 0;

        //A matrix of the same dimension of grid, that contains 0 if the die in the corresponding
        //cell wasn't still counted, 1 otherwise. Every cell is initialized to 0 by default
        byte[][] countedCells = new byte[grid.length][grid[0].length];

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
        for (byte[] row : countedCells) {
            for (byte elem : row)
                numOfCells += (int) elem;
        }

        return numOfCells;

    }

    /**
     * Getter for the name of the Objective Card.
     * @return the card's name.
     */
    @Override
    public String getName() {
        return "Color Diagonals";
    }


    /**
     * Getter for the description of the Objective Card.
     * @return the card's description.
     */
    @Override
    public String getDescription() {
        return "Count of diagonally adjacent same-color dice";
    }

    /**
     * Getter for the victory point of the Public Objective Card.
     * @return the number of points that a player gains every
     * time that he pursue the objective indicated.
     */
    @Override
    public int getVictoryPoint() {
        return 0; //@TODO it is ok?
    }
}
