package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;

import java.util.ArrayList;

/**
 * The class represents a Window Pattern Card.
 * The class is immutable, so all the attributes are final.
 *
 * @author giorgiolbt
 */
public class Pattern {

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
        boolean isRectangular = true;
        int columns = grid[0].length;
        for (Cell[] row : grid){
            if(row.length != columns) isRectangular = false;
        }
        if (!isRectangular) throw new IllegalArgumentException("Irregular matrix: the matrix has to be rectangular");

        this.name = name;
        this.difficulty = difficulty;
        this.grid = grid;

    }

    /**
     * Copy constructor to create a new Pattern that is
     * the deep copy of the pattern passed as a parameter
     * @param pattern The pattern that has to be copied.
     */
    public Pattern(Pattern pattern){
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
     * @return A deep copy of the grid of the Pattern.
     */
    public Cell[][] getGrid() {
        Cell[][] gridCopy = new Cell[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
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
        Pattern pattern = new Pattern(this);
        if(d == null)
            throw new PlacementErrorException("The die to be placed does not exists");
        if (isEmpty(pattern.grid) && pattern.notEdgeOrCorner(c))
            throw new PlacementErrorException("The first die is not placed on an edge or corner space");
        if (!isEmpty(pattern.grid) && !pattern.isAdjacent(c))
            throw new PlacementErrorException("The die is not adjacent to a previously placed die");
        if (!pattern.respectRestrictions(d, getOrthogAdjacent(c)))
            throw new PlacementErrorException("The die is placed orthogonally adjacent to a die of the same color or the same value");
        pattern.grid[c.getRow()][c.getCol()].place(d);
        return pattern;
    }

    /**
     * Places the Die {@code d} at the position identified
     * by the Coordinates {@code c} on a copy of
     * this Pattern assuming that this placement
     * has to respect only the specified restriction
     * {@code restriction} and the restrictions that
     * have always to be respected ({@code d} not null and
     * first die placement restriction).
     * @param d The die to be placed.
     * @param c The Coordinates that indicates
     *          the placement position on the
     *          Pattern's grid.
     * @param restriction The restriction that
     *                    has to be respected.
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
    public Pattern placeDie(Die d, Coordinates c, Restriction restriction) throws PlacementErrorException {
        Pattern pattern = new Pattern(this);
        if(d == null)
            throw new PlacementErrorException("The die to be placed does not exists");
        if (isEmpty(pattern.grid) && pattern.notEdgeOrCorner(c))
            throw new PlacementErrorException("The first die is not placed on an edge or corner space");
        if(restriction.equals(Restriction.ONLY_COLOUR)){
           pattern.placeDieOnlyColour(d, c);
        }
        if(restriction.equals(Restriction.ONLY_VALUE)){
            pattern.placeDieOnlyValue(d, c);
        }
        if(restriction.equals(Restriction.NOT_ADJACENT)){
            pattern.placeDieNotAdjacent(d, c);
        }
        return pattern; //never executed
    }

    /**
     * Places the Die {@code d} at the specified position
     * {@code c} on the {@code pattern} passed as a parameter
     * controlling only that the Colour restriction and the
     * adjacency restriction are respected. This method is ensured
     * that the restrictions that have always to be respected
     * ({@code d} not null and first die placement restriction)
     * are effectively respected.
     * @param d The die to be placed.
     * @param c The Coordinates that indicates
     *          the placement position on the
     *          Pattern's grid.
     * @return The pattern passed as a parameter, but with {@code d}
     * placed at the correct position {@code c}.
     * @throws PlacementErrorException if the placement doesn't respect
     *                                 the Colour restriction or the
     *                                 adjacency restriction.
     *                                 It also propagates the exception if it is thrown from
     *                                 {@link Cell}'s {@code place} method.
     */
    private Pattern placeDieOnlyColour(Die d, Coordinates c) throws PlacementErrorException {
        if (!this.respectColourRestrictions(d, getOrthogAdjacent(c)))
            throw new PlacementErrorException("The die is placed orthogonally adjacent to a die of the same colour");
        if (!this.isEmpty(grid) && !this.isAdjacent(c))
            throw new PlacementErrorException("The die is not adjacent to a previously placed die");
        this.grid[c.getRow()][c.getCol()].place(d);
        return this;
    }

    /**
     * Places the Die {@code d} at the specified position
     * {@code c} on the {@code pattern} passed as a parameter
     * controlling only that the value restriction and the
     * adjacency restriction are respected. This method is ensured
     * that the restrictions that have always to be respected
     * ({@code d} not null and first die placement restriction)
     * are effectively respected.
     * @param d The die to be placed.
     * @param c The Coordinates that indicates
     *          the placement position on the
     *          Pattern's grid.
     * @return The pattern passed as a parameter, but with {@code d}
     * placed at the correct position {@code c}.
     * @throws PlacementErrorException if the placement doesn't respect
     *                                 the value restriction or the
     *                                 adjacency restriction.
     *                                 It also propagates the exception if it is thrown from
     *                                 {@link Cell}'s {@code place} method.
     */
    private Pattern placeDieOnlyValue(Die d, Coordinates c) throws PlacementErrorException{
        if (!this.respectValueRestrictions(d, getOrthogAdjacent(c)))
            throw new PlacementErrorException("The die is placed orthogonally adjacent to a die of the same value");
        if (!this.isEmpty(grid) && !this.isAdjacent(c))
            throw new PlacementErrorException("The die is not adjacent to a previously placed die");
        this.grid[c.getRow()][c.getCol()].place(d);
        return this;
    }

    /**
     * Places the Die {@code d} at the specified position
     * {@code c} on the {@code pattern} passed as a parameter
     * controlling only that the die is NOT adjacent to a
     * previously placed die. This method is ensured
     * that the restrictions that have always to be respected
     * ({@code d} not null and first die placement restriction)
     * are effectively respected.
     * @param d The die to be placed.
     * @param c The Coordinates that indicates
     *          the placement position on the
     *          Pattern's grid.
     * @return The pattern passed as a parameter, but with {@code d}
     * placed at the correct position {@code c}.
     * @throws PlacementErrorException if the die would be placed adjacent to a previously
     *                                 placed die.
     *                                 It also propagates the exception if it is thrown from
     *                                 {@link Cell}'s {@code place} method.
     */
    private Pattern placeDieNotAdjacent (Die d, Coordinates c) throws PlacementErrorException{
        if (!this.isEmpty(grid) && this.isAdjacent(c))
            throw new PlacementErrorException("The die is adjacent to a previously placed die");
        this.grid[c.getRow()][c.getCol()].place(d);
        return this;
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
     * @param source The present position of the Die
     *               on the Pattern.
     * @param destination The position in which the Player
     *                    wants to move the Die.
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
    public Pattern moveDie(Coordinates source, Coordinates destination, Restriction restriction) throws PlacementErrorException {
        Pattern pattern = new Pattern(this);
        Die d = pattern.removeDie(source);
        pattern.placeDie(d, destination, restriction); //throws an exception if there is some placement problem.
        return pattern; //returns the new Pattern if the placement has been ok
    }

    /**
     * Move two dice from a position to another one in a
     * {@link Player}'s Pattern. This action is available
     * only through the use of 'Lathekin' {@link ToolCard}.
     * @param sources The present positions of the dice
     *               on the Pattern.
     * @param destinations The positions in which the Player
     *                    wants to move the dice.
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
    public Pattern moveDice(Coordinates[] sources, Coordinates[] destinations) throws PlacementErrorException {
        Pattern pattern = new Pattern(this);
        Die d1 = pattern.removeDie(sources[0]);
        Die d2 = pattern.removeDie(sources[1]);
        pattern.placeDie(d1, destinations[0]); //throws an exception if there is some placement problem.
        pattern.placeDie(d2, destinations[1]); //throws an exception if there is some placement problem.
        return pattern; //returns the new Pattern if the placement has been ok
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
        for(Cell[] row: g){
            for(Cell cell: row){
                if(cell.getDie() != null) {
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
     * cells: the Colour and the value of an adjacent placed Die must be different.
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
     * Helper method that verifies if the Die {@code die}
     * passed as a parameter respect the Colour
     * restriction that concerns the orthogonally adjacent
     * cells: the Colour of an adjacent placed Die must be different.
     * It is necessary for the {@code placeDie} to verify
     * that one of the 'global' restrictions on the placement
     * of a Die is respected.
     * @param die is the Die that needs to be analyzed.
     * @param adj is a list of the die orthogonally adjacent to
     *        {@code d}.
     * @return {@code true} if {@code die} has different Colour
     * from all the dice in {@code adj}.
     */
    private boolean respectColourRestrictions(Die die, ArrayList<Die> adj){
        for(Die d : adj){
            if(d.getColour() == die.getColour()) return false;
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
     * @param die is the Die that needs to be analyzed.
     * @param adj is a list of the die orthogonally adjacent to
     *        {@code d}.
     * @return {@code true} if {@code die} has different value
     * from all the dice in {@code adj}.
     */
    private boolean respectValueRestrictions(Die die, ArrayList<Die> adj){
        for(Die d : adj){
            if(d.getValue() == die.getValue()) return false;
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
    private ArrayList<Die> getOrthogAdjacent(Coordinates c) {
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
    private Die removeDie(Coordinates c) {
        return grid[c.getRow()][c.getCol()].remove();
    }

    /**
     * Calculates the number of empty spaces
     * (spaces with no placed Die) in the grid.
     * It is useful in the end game scoring's calculus.
     * @return the number of empty spaces in the Pattern's
     * grid.
     */
    public int emptyCells(){
        int cells = 0;
        for (int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++){
                if(getGrid()[i][j].getDie() == null) cells++;
            }
        }
        return cells;
    }

}
