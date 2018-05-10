package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;

import java.util.ArrayList;

/**
 * @author giorgiolabate
 * The class represent a Window Pattern Card.
 */
public class Pattern {
    /**
     * The name of the Pattern
     */
    private String name;
    /**
     * The difficulty of the Pattern
     */
    private int difficulty;
    /**
     * The matrix of Cell that actually represents
     * the grid of the Pattern.
     */
    private Cell[][] grid;

    /**
     * Constructs a Pattern with the {@code grid} passed.
     * The matrix of Cell ({@code grid}) that represents
     * the Pattern is instantiated somewhere else and only
     * passed as a parameter here.
     *
     * @param name       The name of the Pattern.
     * @param difficulty The difficulty of the Pattern.
     *                   According to the Game's rules it must be
     *                   between 3 and 6 otherwise an
     *                   IllegalArgumentException will be thrown.
     * @param grid       The matrix of Cell that represents the Pattern.
     */
    public Pattern(String name, int difficulty, Cell[][] grid) {
        if (difficulty < 3 || difficulty > 6) {
            throw new IllegalArgumentException("The difficulty is not between 3 and 6");
        } else {
            this.name = name;
            this.difficulty = difficulty;
            this.grid = grid;
        }
    }

    /**
     * Getter for the name of the Pattern.
     *
     * @return the name of the Pattern
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the difficulty of the Pattern.
     *
     * @return the difficulty of the Pattern
     */

    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Getter for the grid of the Pattern.
     *
     * @return the grid of the Pattern.
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * Place the Die {@code d} at the position identified
     * by the Coordinates {@code c} on this Pattern.
     * @param d The die to be placed.
     * @param c The Coordinates that indicates
     *          the placement position on the
     *          Pattern's grid.
     * @throws PlacementErrorException if the placemen doesn't respect some restriction on the whole grid
     *                                 (at a 'global' level). It is thrown in three cases:
     *                                 If the Die {@code d} is the first Die to be placed
     *                                 on the grid and the Coordinates {@code c} don't
     *                                 indicate an edge or corner space;
     *                                 If the position indicated by the Coordinates {@code c}
     *                                 is not adjacent to a full {@link Cell} (with the exception of
     *                                 the first placement. This is why there is a control that
     *                                 verifies that the grid is not empty);
     *                                 If the position indicated by the Coordinates {@code c}
     *                                 is orthogonally adjacent to a Cell that has a Die of the
     *                                 same Colour or value of {@code d}.
     */
    public void placeDie(Die d, Coordinates c) throws PlacementErrorException {
        if (isEmpty(grid) && notEdgeOrCorner(c))
            throw new PlacementErrorException("The first die is not placed on an edge or corner space");
        if (!isEmpty(grid) && !isAdjacent(c))
            throw new PlacementErrorException("The die is not adjacent to a previously placed die");
        if (!respectRestrictions(d, getOrtogAdiacent(c)))
            throw new PlacementErrorException("The die is placed orthogonally adjacent to a die of the same color or the same value");
        grid[c.getRow()][c.getCol()].place(d);

    }

    /**
     * Helper method that verifies that the grid is empty
     * (that has no placed dice).
     * It is necessary for the {@code placeDie} to verify whether
     * the Die that has to be placed is the first one or not.
     * @param g The grid to be analyzed.
     * @return {@code true } if the grid {@code g} is empty.
     * {@code false} otherwise.
     */
    private boolean isEmpty(Cell[][] g){
        for(int row = 0; row <= 3; row++){
            for(int col = 0; col <= 4; col++){
                if(g[row][col].getDie() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Helper method that verifies that {@code c} is NOT
     * a {@link Coordinates} that represents the edges or corner
     * spaces of a {@link Pattern}.
     * It is necessary for the {@code placeDie} to verify that
     * the restrictions on the first Die's placement are respected.
     * @param c The {@link Coordinates} to be analyzed.
     * @return {@code true} if {@code c} doesn't represent
     * an edge or corner space, {@code false} otherwise.
     */

    private boolean notEdgeOrCorner (Coordinates c){
        return c.getRow() >= 1 && c.getRow() <= 2 && c.getCol() >= 1 && c.getCol() <= 3;
    }

    /**
     * Helper method that verifies if the {@link Cell}
     * indicated by {@link Coordinates} {@code c} is adjacent
     * to a previously placed Die or not.
     * It is necessary for the {@code placeDie} to verify
     * that one of the 'global' restrictions on the placement
     * of a Die is respected.
     * @param c the {@link Coordinates} to be analyzed.
     * @return {@code true} if {@link Cell}
     * indicated by {@link Coordinates} {@code c} is adjacent
     * to a previously placed Die, {@code false} otherwise.
     */
    private boolean isAdjacent(Coordinates c) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (validCoordinates(c.getRow() + i, c.getCol() + j) && fullCell(c.getRow() + i, c.getCol() + j) && (i != 0 || j != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper method that verifies that the {@code row} and {@code col}
     * indexes are valid: row index must be between 0 and 3 and col index
     * must be between 0 and 4.
     *
     * @param row The row index
     * @param col The col index
     * @return {@code true} if the Coordinates are valid according to
     * the previous description, {@code false} otherwise.
     */

    private boolean validCoordinates(int row, int col) {
        return row >= 0 && row <= 3 && col >= 0 && col <= 4;
    }

    /**
     * Helper method that verifies that the {@link Cell} indicated
     * by the indexes passed as parameters is full.
     * @param row The row index of the {@link Cell}.
     * @param col The column index of the {@link Cell}.
     * @return {@code true} if the {@link Cell} indicated
     * by the indexes passed as parameters is full, {@code false}
     * otherwise.
     */
    private boolean fullCell(int row, int col) {
        return grid[row][col].getDie() != null;
    }

    /**
     * Helper method that verifies if the Die {@code die}
     * passed as a parameter respect the Colour and value
     * restrictions that concerns the orthogonally adjacent
     * cells: the Colour and the value must be different.
     * It is necessary for the {@code placeDie} to verify
     * that one of the 'global' restrictions on the placement
     * of a Die is respected.
     * @param die is the Die that needs to be analyzed.
     * @param adj is a list of the die orthogonally adjacent to
     *        {@code d}.
     * @return {@code true} if {@code die} has different Colour
     * and value from all the dice in {@code adj}.
     */
    private boolean respectRestrictions(Die die, ArrayList<Die> adj) {
        for (Die d : adj) {
            if (d.getColour() == die.getColour() || d.getValue() == die.getValue()) return false;
        }
        return true;
    }

    /**
     * Helper method for {@code respectRestrictions} that returns
     * a list of the orthogonally adjacent dice to the {@link Cell} of
     * {@link Coordinates} {@code c}.
     * @param c the {@link Coordinates} of the {@link Cell} that has to be
     *          analyzed.
     * @return A list of the orthogonally adjacent dice to the {@link Cell} of
     * {@link Coordinates} {@code c}.
     */
    private ArrayList<Die> getOrtogAdiacent(Coordinates c) {
        ArrayList<Die> result = new ArrayList<>();

        if (c.getRow() - 1 >= 0 && fullCell(c.getRow() - 1, c.getCol())) {
            result.add(grid[c.getRow() - 1][c.getCol()].getDie());
        }
        if (c.getRow() + 1 <= 3 && fullCell(c.getRow() + 1, c.getCol())) {
            result.add(grid[c.getRow() + 1][c.getCol()].getDie());
        }
        if (c.getCol() - 1 >= 0 && fullCell(c.getRow(), c.getCol() - 1)) {
            result.add(grid[c.getRow()][c.getCol() - 1].getDie());
        }
        if (c.getCol() + 1 <= 4 && fullCell(c.getRow(), c.getCol() + 1)) {
            result.add(grid[c.getRow()][c.getCol() + 1].getDie());
        }

        return result;
    }

    /**
     * Remove the Die placed at the {@link Cell}
     * of Coordinates {@code c}.
     * @param c The coordinates of the Cell
     *          that contains the Die to be
     *          removed.
     * @return The removed Die (null if the
     * {@link Cell was empty).
     */

    public Die removeDie(Coordinates c) {
        return grid[c.getRow()][c.getCol()].remove();
    }


}
