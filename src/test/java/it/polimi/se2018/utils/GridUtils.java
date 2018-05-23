package it.polimi.se2018.utils;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Pattern;

/**
 * Simple utility class to provide unit tests some methods
 * to manipulate grids.
 *
 * @author dvdmff
 */
public class GridUtils {

    /**
     * Creates an empty grid with no cell restrictions.
     *
     * @param rows The number of rows of the grid.
     * @param cols The number of columns of the grid.
     * @return An empty grid with no placement restrictions.
     */
    public static Cell[][] getEmptyUnrestrictedGrid(int rows, int cols) {
        Cell[][] grid = new Cell[rows][cols];
        for (int row = 0; row < rows; ++row)
            for (int col = 0; col < cols; ++col)
                grid[row][col] = new Cell();
        return grid;
    }


    public static boolean haveSameRestriction(Cell[][] first, Cell[][] second) {

        for (int i = 0; i < Pattern.ROWS; ++i) {
            for (int j = 0; j < Pattern.COLS; ++j)
                if (!areCellEquals(first[i][j], second[i][j]))
                    return false;
        }
        return true;
    }

    private static boolean areCellEquals(Cell first, Cell second){
        return first.getValue() == second.getValue() &&
                first.getColour() == second.getColour();
    }

    public static boolean isEmpty(Cell[][] grid){
        for (int i = 0; i < Pattern.ROWS; ++i) {
            for (int j = 0; j < Pattern.COLS; ++j)
                if(grid[i][j].getDie() != null)
                    return false;
        }
        return true;
    }
}
