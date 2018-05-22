package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Tests the methods of the class {@link Pattern}.
 * Also indirectly tests all the class {@link Coordinates}.
 *
 * @author michelemarzollo
 */
public class TestPattern {

    /**
     * The grid that will be used in many methods
     */
    private Cell[][] grid = new Cell[4][5];

    /**
     * Initializes a grid of cells, some of which contain a die.
     */
    @Before
    public void setUp() {

        //The cycle instantiates a grid of cell with no restrictions
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell();
            }
        }
        //Some cell have a restriction
        grid[0][1] = new Cell(Colour.BLUE);
        grid[0][2] = new Cell(2);
        grid[0][4] = new Cell(Colour.YELLOW);
        grid[1][1] = new Cell(4);
        grid[1][3] = new Cell(Colour.RED);
        grid[2][2] = new Cell(5);
        grid[2][3] = new Cell(Colour.YELLOW);
        grid[3][0] = new Cell(Colour.GREEN);
        grid[3][1] = new Cell(3);
        grid[3][4] = new Cell(Colour.PURPLE);

    }

    /**
     * Tests the behaviour of the constructor with an invalid difficulty
     * (higher than allowed).
     */
    @Test
    public void testInvalidConstructorHigher() {

        try {
            Pattern p = new Pattern("SunCatcher", 7, grid);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests the behaviour of the constructor with an invalid difficulty
     * (lower than allowed).
     */
    @Test
    public void testInvalidConstructorLower() {

        try {
            Pattern p = new Pattern("SunCatcher", 0, grid);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests the getters of the class
     */
    @Test
    public void testGetters() {

        Pattern p = new Pattern("SunCatcher", 3, grid);
        boolean check = true;

        assertEquals("SunCatcher", p.getName());
        assertEquals(3, p.getDifficulty());

        for (int i = 0; i < grid.length && check; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getColour() != p.getGrid()[i][j].getColour() ||
                        grid[i][j].getValue() != p.getGrid()[i][j].getValue()) {
                    check = false;
                }
            }
        }
        assertTrue(check);
    }

    //CHECK ON FIRST PLACEMENT

    /**
     * Checks that when a die is placed on a grid that is empty, if it is not placed
     * on a border an exception must be thrown (negative case).
     * <p>The die is not placed in a cell with restrictions, to avoid conflict
     * of exceptions</p>
     * <p>Checks that the exception thrown comes from the correct branch</p>
     */
    @Test
    public void testPlaceDieNotOnBorder() {

        Pattern p = new Pattern("SunCatcher", 3, grid);
        Die d1 = new Die(3, new Random(), Colour.GREEN);

        try {
            p.placeDie(d1, new Coordinates(2, 1));
            fail();
        } catch (PlacementErrorException e) {
            assertEquals("The first die is not placed on an edge or corner space", e.getMessage());
        }

    }

    /**
     * Checks that when a die is placed on an empty grid, if it is placed
     * on a border the placement must succeed (positive case).
     * <p>Also checks that the placed die is the correct one</p>
     */
    @Test
    public void testPlaceDieOnBorder() {

        Pattern p = new Pattern("SunCatcher", 3, grid);
        Die d1 = new Die(2, new Random(), Colour.GREEN);
        Coordinates c = new Coordinates(0, 2);

        try {
            p = p.placeDie(d1, c);
            Die d2 = p.getGrid()[c.getRow()][c.getCol()].getDie();
            //Checks that the die placed is the correct one
            assertEquals(d1, d2);
        } catch (PlacementErrorException e) {
            fail();
        }

    }

    //CHECK ON PLACEMENT IN NOT EMPTY GRID

    /**
     * If there are already dice in the pattern the next one to be placed
     * must be near one of them (negative case).
     * <p>The dice are not placed in a cell with restrictions, to avoid conflict
     * of exceptions</p>
     * <p>Checks that the exception thrown comes from the correct branch</p>
     */
    @Test
    public void testPlaceDieAdjacentFail() {

        Pattern p = new Pattern("SunCatcher", 3, grid);

        Die d1 = new Die(3, new Random(), Colour.GREEN);
        Die d2 = new Die(4, new Random(), Colour.RED);

        try {
            p = p.placeDie(d1, new Coordinates(3, 2));
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put the new die in a non adjacent cell
        try {
            p = p.placeDie(d2, new Coordinates(1, 0));
            fail();
        } catch (PlacementErrorException e) {
            assertEquals("The die is not adjacent to a previously placed die", e.getMessage());
        }
    }

    /**
     * If there are already dice in the pattern the next one to be placed
     * must be near one of them (positive case).
     * <p>The dice are not placed in a cell with restrictions, to avoid conflict
     * of exceptions</p>
     * <p>Also checks that the placed die is the correct one</p>
     */
    @Test
    public void testPlaceDieAdjacenctSuccessful() {

        Pattern p = new Pattern("SunCatcher", 3, grid);

        Die d1 = new Die(3, new Random(), Colour.GREEN);
        Die d2 = new Die(4, new Random(), Colour.RED);

        Coordinates c1 = new Coordinates(3, 2);
        Coordinates c2 = new Coordinates(2, 1);

        try {
            p = p.placeDie(d1, c1);
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put the new die in an adjacent cell
        try {
            p = p.placeDie(d2, c2);

            Die d3 = p.getGrid()[c2.getRow()][c2.getCol()].getDie();
            //Checks that the die placed is the correct one
            assertEquals(d2, d3);
        } catch (PlacementErrorException e) {
            fail();
        }
    }

    //CHECK ON VALIDITY OF PLACEMENT BECAUSE OF COLOUR ADJACENCY

    /**
     * Method to check that if an orthogonally adjacent die has the same
     * colour of the already placed one it can't be placed.
     * <p>Checks that the exception thrown comes from the correct branch</p>
     */
    @Test
    public void testPlaceDieColourRestrictionFail() {

        Pattern p = new Pattern("SunCatcher", 3, grid);

        Die d1 = new Die(3, new Random(), Colour.GREEN);
        Die d2 = new Die(4, new Random(), Colour.GREEN);

        try {
            p = p.placeDie(d1, new Coordinates(2, 0));
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put a die of the same colour to the already placed one
        try {
            p = p.placeDie(d2, new Coordinates(2, 1));
            fail();
        } catch (PlacementErrorException e) {
            assertEquals(
                    "The die is placed orthogonally adjacent to a " +
                    "die of the same color or the same value",
                    e.getMessage());
        }
    }

    /**
     * Method to check that if the restriction of adjacency
     * are respected the die must be placed.
     * <p>Also tests the correct placement on a cell with the correct
     * value restriction and with the second die in the border</p>
     */
    @Test
    public void testPlaceDieColourRestrictionSuccessful() {

        Pattern p = new Pattern("SunCatcher", 3, grid);

        Die d1 = new Die(4, new Random(), Colour.GREEN);
        Die d2 = new Die(2, new Random(), Colour.RED);

        Coordinates c = new Coordinates(0, 3);

        try {
            p = p.placeDie(d1, c);
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put the new die in an orthogonally adjacent cell
        try {

            p = p.placeDie(d2, new Coordinates(0, 2));

            Die d3 = p.getGrid()[0][2].getDie();
            //Checks that the die placed is the correct one
            assertEquals(d2, d3);

        } catch (PlacementErrorException e) {
            fail();
        }
    }

    //CHECK ON VALIDITY OF PLACEMENT BECAUSE OF VALUE ADJACENCY

    /**
     * Method to check that if an orthogonally adjacent die has the same
     * value of the already placed one it can't be placed.
     * <p>Checks that the exception thrown comes from the correct branch</p>
     */
    @Test
    public void testPlaceDieValueRestrictionFail() {

        Pattern p = new Pattern("SunCatcher", 3, grid);

        Die d1 = new Die(4, new Random(), Colour.PURPLE);
        Die d2 = new Die(4, new Random(), Colour.RED);

        Coordinates c1 = new Coordinates(3, 4);
        Coordinates c2 = new Coordinates(2, 4);

        try {
            p = p.placeDie(d1, c1);
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put a die of the same colour to the already placed one
        try {
            p = p.placeDie(d2, c2);
            fail();
        } catch (PlacementErrorException e) {
            assertEquals("The die is placed orthogonally adjacent to a die of the same color or the same value", e.getMessage());
        }
    }

    /**
     * Method to check that if the restriction of adjacency
     * are respected the die must be placed.
     * <p>Also tests the correct placement on a cell with the correct
     * value restriction and with the second die in the border</p>
     */
    @Test
    public void testPlaceDieValueRestrictionSuccessful() {

        Pattern p = new Pattern("SunCatcher", 3, grid);

        Die d1 = new Die(2, new Random(), Colour.GREEN);
        Die d2 = new Die(3, new Random(), Colour.BLUE);

        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(0, 1);

        try {
            p = p.placeDie(d1, c1);
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put the new die in an orthogonally adjacent cell
        try {

            p = p.placeDie(d2, c2);

            Die d3 = p.getGrid()[c2.getRow()][c2.getCol()].getDie();
            //Checks that the die placed is the correct one
            assertEquals(d2, d3);

        } catch (PlacementErrorException e) {
            fail();
        }
    }

    //TESTS ON OTHER METHODS

    /**
     * Tests the behaviour of remove: if a die is placed and then removed
     * the cell must remain empty and the method return must be the same
     * of the one put on it.
     */
//    @Test
//    public void testRemoveDie() {
//
//        Pattern p = new Pattern("SunCatcher", 3, grid);
//
//        Die d1 = new Die(3, new Random(), Colour.GREEN);
//
//        Coordinates c = new Coordinates(3, 0);
//
//        //Placement of a die respecting pattern and cell restrictions
//        try {
//            p.placeDie(d1, c);
//        } catch (PlacementErrorException e) {
//            fail();
//        }
//        //removal of the die
//        Die d2 = p.removeDie(c);
//        //Check that the removed die is the correct one
//        assertEquals(d1, d2);
//        //Check that the cell is now empty
//        assertNull(p.getGrid()[c.getRow()][c.getCol()].getDie());
//
//    }

    @Test
    public void testOrthogonalAdjacent() {

        Pattern p = new Pattern("SunCatcher", 3, grid);

        Die d1 = new Die(3, new Random(), Colour.BLUE);
        Die d2 = new Die(5, new Random(), Colour.PURPLE);
        Die d3 = new Die(6, new Random(), Colour.PURPLE);
        Die d4 = new Die(1, new Random(), Colour.YELLOW);
        Die d5 = new Die(4, new Random(), Colour.RED);

        Coordinates c1 = new Coordinates(0, 1);
        Coordinates c2 = new Coordinates(1, 0);
        Coordinates c3 = new Coordinates(1, 2);
        Coordinates c4 = new Coordinates(2, 1);
        Coordinates c5 = new Coordinates(1, 1);

        try {
            p = p.placeDie(d1, c1);
            p = p.placeDie(d2, c2);
            p = p.placeDie(d3, c3);
            p = p.placeDie(d4, c4);
            p = p.placeDie(d5, c5);
        } catch (PlacementErrorException e) {
            fail();
        }

    }
}