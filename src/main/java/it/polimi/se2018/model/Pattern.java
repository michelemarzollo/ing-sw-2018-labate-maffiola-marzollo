package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The class represents a Window Pattern Card.
 * The class is immutable, so all the attributes are final.
 *
 * @author giorgiolbt
 */
public class Pattern implements Serializable {

    /**
     * The number of rows in a pattern.
     */
    public static final int ROWS = 4;
    /**
     * The number of columns in a pattern.
     */
    public static final int COLS = 5;


    /**
     * The name of the Pattern
     */
    private final String name;

    /**
     * The difficulty of the Pattern
     */
    private final int difficulty;

    /**
     * The matrix of Cell that actually represents
     * the grid of the Pattern.
     */
    private final Cell[][] grid;

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
        }
        boolean isRegular = (grid.length == ROWS);
        for (Cell[] row : grid) {
            if (row.length != COLS) isRegular = false;
        }
        if (!isRegular) throw new IllegalArgumentException("Irregular matrix: the matrix has to be rectangular");

        this.name = name;
        this.difficulty = difficulty;
        this.grid = grid;

    }

    /**
     * Copy constructor to create a new Pattern that is
     * the deep copy of the pattern passed as a parameter
     *
     * @param pattern The pattern that has to be copied.
     */
    public Pattern(Pattern pattern) {
        this.name = pattern.name;
        this.difficulty = pattern.difficulty;
        this.grid = pattern.getGrid();
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
     * It returns a deep copy of the grid: it
     * is necessary for the copy constructor
     *
     * @return A deep copy of the grid of the Pattern.
     */
    public Cell[][] getGrid() {
        Cell[][] gridCopy = new Cell[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gridCopy[i][j] = new Cell(grid[i][j]);
            }
        }
        return gridCopy;
    }


    /**
     * Places the Die {@code d} at the position identified
     * by the Coordinates {@code c} on a copy of
     * this Pattern assuming that this placement
     * has to respect all possible restrictions.
     *
     * @param d The die to be placed.
     * @param c The Coordinates that indicates
     *          the placement position on the
     *          Pattern's grid.
     * @return A new pattern that is a copy of
     * this, but with {@code d} placed in the
     * correct position indicated by {@code c}.
     * @throws PlacementErrorException if {@code d} is null or if the placement doesn't
     *                                 respect some restriction on the whole grid
     *                                 (at a 'global' level):
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
     *                                 It also propagates the exception if it is thrown from
     *                                 {@link Cell}'s {@code place} method.
     */
    public Pattern placeDie(Die d, Coordinates c) throws PlacementErrorException {
        return placeDie(d, c, Restriction.DEFAULT);
    }

    /**
     * Places the Die {@code d} at the position identified
     * by the Coordinates {@code c} on a copy of
     * this Pattern assuming that this placement
     * has to respect only the specified restriction
     * {@code restriction} and the restrictions that
     * have always to be respected ({@code d} not null and
     * first die placement restriction).
     *
     * @param die         The die to be placed.
     * @param coordinates The Coordinates that indicates the placement position on
     *                    the Pattern's grid.
     * @param restriction The restriction that has to be respected.
     * @return A new pattern that is a copy of
     * the present one, but with {@code d} placed in the
     * correct position indicated by {@code c}.
     * @throws PlacementErrorException if the placement doesn't respect the restriction
     *                                 on the first Die placement (that has to be always respected) or
     *                                 if {@code d} is null or if the {@code restriction} specified
     *                                 is not respected.
     *                                 It also propagates the exception if it is thrown from
     *                                 {@link Cell}'s {@code place} method.
     */
    public Pattern placeDie(Die die, Coordinates coordinates,
                            Restriction restriction) throws PlacementErrorException {
        if (die == null)
            throw new PlacementErrorException("The die to be placed does not exists");

        Pattern pattern = new Pattern(this);
        if (pattern.isEmpty(pattern.grid) && pattern.notEdgeOrCorner(coordinates))
            throw new PlacementErrorException("The first die is not placed on an edge or corner space");


        boolean hasAdjacent = isEmpty(pattern.grid) || isAdjacent(coordinates);
        pattern.checkAdjacency(hasAdjacent, restriction);

        pattern.checkConstraints(die, coordinates, restriction);

        pattern.grid[coordinates.getRow()][coordinates.getCol()]
                .place(die, restriction);

        return pattern;
    }

    /**
     * Checks if adjacency is respected according to the restriction.
     *
     * @param hasAdjacent {@code true} if there is at least one adjacent die;
     *                    {@code false} otherwise.
     * @param restriction The restriction under which the adjacency has to be checked.
     * @throws PlacementErrorException if adjacency rules are not respected.
     */
    private void checkAdjacency(boolean hasAdjacent,
                                Restriction restriction) throws PlacementErrorException {

        if (restriction != Restriction.NOT_ADJACENT && !hasAdjacent)
            throw new PlacementErrorException(
                    "The die is not adjacent to a previously placed die");

        if (restriction == Restriction.NOT_ADJACENT && hasAdjacent)
            throw new PlacementErrorException(
                    "The die is adjacent to a previously placed die when a non-adjacent place is used");
    }

    /**
     * Checks if the specified die can be moved in the cell identified by the coordinates.
     *
     * @param die         The die to be placed.
     * @param coordinates The coordinates where the die will be placed.
     * @param restriction The restriction under which the constraints has to be checked.
     * @throws PlacementErrorException if a colour or value constraint is not respected.
     */
    private void checkConstraints(Die die, Coordinates coordinates,
                                  Restriction restriction) throws PlacementErrorException {

        List<Die> orthogonallyAdjacent = getOrthogonallyAdjacent(coordinates);

        if (restriction.checkColourConstraint() &&
                !respectColourRestrictions(die, orthogonallyAdjacent))
            throw new PlacementErrorException(
                    "The die is placed orthogonally adjacent to a die of the same colour");

        if (restriction.checkValueConstraint() &&
                !respectValueRestrictions(die, orthogonallyAdjacent))
            throw new PlacementErrorException(
                    "The die is placed orthogonally adjacent to a die of the same value");
    }

    /**
     * Move a Die from a position to another one in a
     * {@link Player}'s Pattern. This action is available
     * only through the use of 'Eglomise Brush' and
     * 'Copper Foil Burnisher' {@link ToolCard}. Both of them
     * let the Player ignore value or colour restriction, so
     * {@code restriction} indicates the restriction
     * that has to be respected (in addition to the other placement
     * restrictions).
     *
     * @param source      The present position of the Die
     *                    on the Pattern.
     * @param destination The position in which the Player
     *                    wants to move the Die.
     * @param restriction The restriction that has to be respected.
     * @return A new pattern that is a copy of
     * the present one, but with the chosen Die that was placed
     * in the position {@code source} in the
     * correct position indicated by {@code destination}.
     * @throws PlacementErrorException if the placement doesn't respect some restriction on the whole grid
     *                                 (at a 'global' level) or if {@code d} is null.
     *                                 It is propagated if thrown by the {@code placeDie}.
     *                                 It also propagates the exception if it is thrown from
     *                                 {@link Cell}'s {@code place} method.
     */
    public Pattern moveDie(Coordinates source, Coordinates destination,
                           Restriction restriction) throws PlacementErrorException {
        Pattern pattern = new Pattern(this);
        Die die = pattern.removeDie(source);
        //throws an exception if there is some placement problem.
        return pattern.placeDie(die, destination, restriction);
    }

    /**
     * Move two dice from a position to another one in a
     * {@link Player}'s Pattern. This action is available
     * only through the use of 'Lathekin' and 'Tap Wheel' {@link ToolCard}.
     *
     * @param sources      The present positions of the dice
     *                     on the Pattern.
     * @param destinations The positions in which the Player
     *                     wants to move the dice.
     * @return A new pattern that is a copy of
     * the present one, but with the chosen dice that were placed
     * in the positions {@code sources} in the
     * correct positions indicated by {@code destinations}.
     * @throws PlacementErrorException if the placement doesn't respect some restriction on the whole grid
     *                                 (at a 'global' level) or if {@code d1} or {@code d2} is null.
     *                                 It is propagated if thrown by the {@code placeDie}.
     *                                 It also propagates the exception if it is thrown from
     *                                 {@link Cell}'s {@code place} method.
     */
    public Pattern moveDice(Coordinates[] sources, Coordinates[] destinations, Restriction restriction)
            throws PlacementErrorException {

        Pattern pattern = new Pattern(this);
        List<Die> removed = new ArrayList<>();
        for (Coordinates source : sources)
            removed.add(pattern.removeDie(source));

        for (int i = 0; i < removed.size(); ++i)
            //throws an exception if there is some placement problem.
            pattern = pattern.placeDie(removed.get(i), destinations[i], restriction);

        return pattern; //returns the new Pattern if the placement has been successful
    }

    public Pattern moveDice(Coordinates[] sources, Coordinates[] destinations) throws PlacementErrorException {
        return moveDice(sources, destinations, Restriction.DEFAULT);
    }

    /**
     * Helper method that verifies that the grid is empty
     * (that has no placed dice).
     * It is necessary for the {@code placeDie} to verify whether
     * the Die that has to be placed is the first one or not.
     *
     * @param g The grid to be analyzed.
     * @return {@code true} if the grid {@code g} is empty.
     * {@code false} otherwise.
     */
    private boolean isEmpty(Cell[][] g) {
        for (Cell[] row : g) {
            for (Cell cell : row) {
                if (cell.getDie() != null) {
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
     *
     * @param c The {@link Coordinates} to be analyzed.
     * @return {@code true} if {@code c} doesn't represent
     * an edge or corner space, {@code false} otherwise.
     */
    private boolean notEdgeOrCorner(Coordinates c) {
        return c.getRow() >= 1 && c.getRow() <= 2 && c.getCol() >= 1 && c.getCol() <= 3;
    }

    /**
     * Helper method that verifies if the {@link Cell}
     * indicated by {@link Coordinates} {@code c} is adjacent
     * to a previously placed Die or not.
     * It is necessary for the {@code placeDie} to verify
     * that one of the 'global' restrictions on the placement
     * of a Die is respected.
     *
     * @param c the {@link Coordinates} to be analyzed.
     * @return {@code true} if {@link Cell}
     * indicated by {@link Coordinates} {@code c} is adjacent
     * to a previously placed Die, {@code false} otherwise.
     */
    private boolean isAdjacent(Coordinates c) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (validCoordinates(c.getRow() + i, c.getCol() + j) &&
                        fullCell(c.getRow() + i, c.getCol() + j) &&
                        (i != 0 || j != 0)) {
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
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    /**
     * Helper method that verifies that the {@link Cell} indicated
     * by the indexes passed as parameters is full.
     *
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
     * passed as a parameter respect the Colour
     * restriction that concerns the orthogonally adjacent
     * cells: the Colour of an adjacent placed Die must be different.
     * It is necessary for the {@code placeDie} to verify
     * that one of the 'global' restrictions on the placement
     * of a Die is respected.
     *
     * @param die is the Die that needs to be analyzed.
     * @param adj is a list of the die orthogonally adjacent to
     *            {@code d}.
     * @return {@code true} if {@code die} has different Colour
     * from all the dice in {@code adj}.
     */
    private boolean respectColourRestrictions(Die die, List<Die> adj) {
        for (Die d : adj) {
            if (d.getColour() == die.getColour()) return false;
        }
        return true;
    }

    /**
     * Helper method that verifies if the Die {@code die}
     * passed as a parameter respect the value
     * restriction that concerns the orthogonally adjacent
     * cells: the value of an adjacent placed Die must be different.
     * It is necessary for the {@code placeDie} to verify
     * that one of the 'global' restrictions on the placement
     * of a Die is respected.
     *
     * @param die is the Die that needs to be analyzed.
     * @param adj is a list of the die orthogonally adjacent to
     *            {@code d}.
     * @return {@code true} if {@code die} has different value
     * from all the dice in {@code adj}.
     */
    private boolean respectValueRestrictions(Die die, List<Die> adj) {
        for (Die d : adj) {
            if (d.getValue() == die.getValue()) return false;
        }
        return true;
    }

    /**
     * Helper method that returns the list of the orthogonally adjacent
     * dice to the {@link Cell} at {@link Coordinates} {@code c}.
     *
     * @param c the {@link Coordinates} of the {@link Cell} that has to be
     *          analyzed.
     * @return A list of the orthogonally adjacent dice to the {@link Cell} of
     * {@link Coordinates} {@code c}.
     */
    private ArrayList<Die> getOrthogonallyAdjacent(Coordinates c) {
        ArrayList<Die> result = new ArrayList<>();

        if (c.getRow() - 1 >= 0 && fullCell(c.getRow() - 1, c.getCol())) {
            result.add(grid[c.getRow() - 1][c.getCol()].getDie());
        }
        if (c.getRow() + 1 < ROWS && fullCell(c.getRow() + 1, c.getCol())) {
            result.add(grid[c.getRow() + 1][c.getCol()].getDie());
        }
        if (c.getCol() - 1 >= 0 && fullCell(c.getRow(), c.getCol() - 1)) {
            result.add(grid[c.getRow()][c.getCol() - 1].getDie());
        }
        if (c.getCol() + 1 < COLS && fullCell(c.getRow(), c.getCol() + 1)) {
            result.add(grid[c.getRow()][c.getCol() + 1].getDie());
        }

        return result;
    }

    /**
     * Remove the Die placed at the {@link Cell}
     * of Coordinates {@code c}.
     *
     * @param c The coordinates of the Cell
     *          that contains the Die to be
     *          removed.
     * @return The removed Die (null if the
     * {@link Cell was empty).
     */
    private Die removeDie(Coordinates c) {
        return grid[c.getRow()][c.getCol()].remove();
    }

    /**
     * Calculates the number of empty spaces
     * (spaces with no placed Die) in the grid.
     * It is useful in the end game scoring's calculus.
     *
     * @return the number of empty spaces in the Pattern's
     * grid.
     */
    public int emptyCells() {
        int cells = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (getGrid()[i][j].getDie() == null) cells++;
            }
        }
        return cells;
    }
}
