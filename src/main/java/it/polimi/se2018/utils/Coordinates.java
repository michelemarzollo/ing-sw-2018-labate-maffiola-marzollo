package it.polimi.se2018.utils;

/**
 * Class that encapsulates the pair of
 * coordinates to refer to a
 * {@link it.polimi.se2018.model.Cell}
 * on a {@link it.polimi.se2018.model.Pattern}
 *
 * @author giorgiolbt
 */
public class Coordinates {

    /**
     * The coordinate that describes the row
     */
    private int row;

    /**
     * The coordinate that describes the column
     */
    private int col;

    /**
     * Constructs a pair of coordinates.
     * @param raw The raw index.
     * @param col The column index.
     */
    public Coordinates(int raw, int col) {
        this.row = raw;
        this.col = col;
    }

    /**
     * Getter for the raw index.
     * @return the raw index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for the col index.
     * @return the column index.
     */
    public int getCol() {
        return col;
    }

}
