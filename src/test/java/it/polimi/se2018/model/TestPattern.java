package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;
import org.junit.Assert;
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
     * A sample pattern that is used frequently in test cases.
     */
    private Pattern sunCatcher;

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

        sunCatcher = new Pattern("SunCatcher", 3, grid);

    }

    /**
     * Tests the behaviour of the constructor with an invalid difficulty
     * (higher than allowed).
     */
    @Test
    public void testInvalidConstructorTooHighDifficulty() {

        try {
            new Pattern("SunCatcher", 7, grid);
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
    public void testInvalidConstructorTooLowDifficulty() {

        try {
            new Pattern("SunCatcher", 0, grid);
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

        boolean check = true;

        assertEquals("SunCatcher", sunCatcher.getName());
        assertEquals(3, sunCatcher.getDifficulty());

        for (int i = 0; i < grid.length && check; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getColour() != sunCatcher.getGrid()[i][j].getColour() ||
                        grid[i][j].getValue() != sunCatcher.getGrid()[i][j].getValue()) {
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

        Die d1 = new Die(3, new Random(), Colour.GREEN);

        try {
            sunCatcher.placeDie(d1, new Coordinates(2, 1));
            fail();
        } catch (PlacementErrorException e) {
            assertEquals("The first die is not placed on an edge or corner space",
                    e.getMessage());
        }

    }

    /**
     * Checks if an {@code IndexOutOfBoundsException} is thrown when
     * bad indexes are used.
     */
    @Test
    public void testPlaceDieOutOfBound() {

        Die d1 = new Die(3, new Random(), Colour.GREEN);

        try {
            sunCatcher.placeDie(d1, new Coordinates(50, 50));
            fail();
        } catch (PlacementErrorException e) {
            Assert.fail(e.getMessage());
        } catch (IndexOutOfBoundsException e){
            Assert.assertTrue(true);
        }

    }

    /**
     * Checks that when a die is placed on an empty grid, if it is placed
     * on a border the placement must succeed (positive case).
     * <p>Also checks that the placed die is the correct one</p>
     */
    @Test
    public void testPlaceDieOnBorder() {

        Die d1 = new Die(2, new Random(), Colour.GREEN);
        Coordinates c = new Coordinates(0, 2);

        try {
            sunCatcher = sunCatcher.placeDie(d1, c);
            Die d2 = sunCatcher.getGrid()[c.getRow()][c.getCol()].getDie();
            //Checks that the die placed is the correct one
            assertEquals(d1, d2);
        } catch (PlacementErrorException e) {
            fail(e.getMessage());
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

        Die d1 = new Die(3, new Random(), Colour.GREEN);
        Die d2 = new Die(4, new Random(), Colour.RED);

        try {
            sunCatcher = sunCatcher.placeDie(d1, new Coordinates(3, 2));
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put the new die in a non adjacent cell
        try {
            sunCatcher = sunCatcher.placeDie(d2, new Coordinates(1, 0));
            fail();
        } catch (PlacementErrorException e) {
            assertEquals("The die is not adjacent to a previously placed die",
                    e.getMessage());
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
    public void testPlaceDieAdjacentSuccessful() {

        Die d1 = new Die(3, new Random(), Colour.GREEN);
        Die d2 = new Die(4, new Random(), Colour.RED);

        Coordinates c1 = new Coordinates(3, 2);
        Coordinates c2 = new Coordinates(2, 1);

        try {
            sunCatcher = sunCatcher.placeDie(d1, c1);
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put the new die in an adjacent cell
        try {
            sunCatcher = sunCatcher.placeDie(d2, c2);

            Die d3 = sunCatcher.getGrid()[c2.getRow()][c2.getCol()].getDie();
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

        Die d1 = new Die(3, new Random(), Colour.GREEN);
        Die d2 = new Die(4, new Random(), Colour.GREEN);

        try {
            sunCatcher = sunCatcher.placeDie(d1, new Coordinates(2, 0));
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put a die of the same colour to the already placed one
        try {
            sunCatcher = sunCatcher.placeDie(d2, new Coordinates(2, 1));
            fail();
        } catch (PlacementErrorException e) {
            assertEquals(
                    "The die is placed orthogonally adjacent to a " +
                    "die of the same colour",
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

        Die d1 = new Die(4, new Random(), Colour.GREEN);
        Die d2 = new Die(2, new Random(), Colour.RED);

        Coordinates c = new Coordinates(0, 3);

        try {
            sunCatcher = sunCatcher.placeDie(d1, c);
        } catch (PlacementErrorException e) {
            fail();
        }

        // Tries to put the new die in an orthogonally adjacent cell
        try {

            sunCatcher = sunCatcher.placeDie(d2, new Coordinates(0, 2));

            Die d3 = sunCatcher.getGrid()[0][2].getDie();
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

        Die d1 = new Die(4, new Random(), Colour.PURPLE);
        Die d2 = new Die(4, new Random(), Colour.RED);

        Coordinates c1 = new Coordinates(3, 4);
        Coordinates c2 = new Coordinates(2, 4);

        try {
            sunCatcher = sunCatcher.placeDie(d1, c1);
        } catch (PlacementErrorException e) {
            fail(e.getMessage());
        }

        // Tries to put a die of the same colour to the already placed one
        try {
            sunCatcher = sunCatcher.placeDie(d2, c2);
            fail();
        } catch (PlacementErrorException e) {
            assertEquals("The die is placed orthogonally adjacent to a " +
                    "die of the same value",
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
    public void testPlaceDieValueRestrictionSuccessful() {

        Die d1 = new Die(2, new Random(), Colour.GREEN);
        Die d2 = new Die(3, new Random(), Colour.BLUE);

        Coordinates c1 = new Coordinates(0, 0);
        Coordinates c2 = new Coordinates(0, 1);

        try {
            sunCatcher = sunCatcher.placeDie(d1, c1);
        } catch (PlacementErrorException e) {
            fail(e.getMessage());
        }

        // Tries to put the new die in an orthogonally adjacent cell
        try {

            sunCatcher = sunCatcher.placeDie(d2, c2);

            Die d3 = sunCatcher.getGrid()[c2.getRow()][c2.getCol()].getDie();
            //Checks that the die placed is the correct one
            assertEquals(d2, d3);

        } catch (PlacementErrorException e) {
            fail();
        }
    }

    /**
     * Tests if dice are correctly placed when they form a cross-shaped pattern.
     */
    @Test
    public void testOrthogonalAdjacent() {

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
            sunCatcher = sunCatcher.placeDie(d1, c1);
            sunCatcher = sunCatcher.placeDie(d2, c2);
            sunCatcher = sunCatcher.placeDie(d3, c3);
            sunCatcher = sunCatcher.placeDie(d4, c4);
            sunCatcher = sunCatcher.placeDie(d5, c5);
        } catch (PlacementErrorException e) {
            fail(e.getMessage());
        }

    }

    /**
     * Tests if the loose placement where only colour restriction are applied
     * acts correctly.
     */
    @Test
    public void testOnlyColourRestrictionPlacementSuccess(){
        Die die = new Die(4, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(
                    die,
                    new Coordinates(0, 2),
                    Restriction.ONLY_COLOUR);
        } catch (PlacementErrorException e) {
            Assert.fail();
        }

        Assert.assertEquals(die, sunCatcher.getGrid()[0][2].getDie());
    }

    /**
     * Tests if the loose placement where only colour restriction are applied
     * fails when a bad destination is provided.
     * <p>The state of the pattern after the failure must be the same as before,
     * meaning that the failed operation left no side effects.</p>
     */
    @Test
    public void testOnlyColourRestrictionPlacementFailure(){
        Die die = new Die(2, new Random(), Colour.RED);
        try{
            sunCatcher = sunCatcher.placeDie(
                    die,
                    new Coordinates(0, 1),
                    Restriction.ONLY_COLOUR);
            Assert.fail();
        } catch (PlacementErrorException e) {
            Assert.assertNull(sunCatcher.getGrid()[0][1].getDie());
        }
    }

    /**
     * Tests if the loose placement where only value restriction are applied
     * acts correctly.
     */
    @Test
    public void testOnlyValueRestrictionPlacementSuccess(){
        Die die = new Die(2, new Random(), Colour.RED);

        try{
            sunCatcher = sunCatcher.placeDie(
                    die,
                    new Coordinates(0, 1),
                    Restriction.ONLY_VALUE);
        } catch (PlacementErrorException e) {
            Assert.fail();
        }

        Assert.assertEquals(die, sunCatcher.getGrid()[0][1].getDie());
    }

    /**
     * Tests if the loose placement where only value restriction are applied
     * fails when a bad destination is provided.
     * <p>The state of the pattern after the failure must be the same as before,
     * meaning that the failed operation left no side effects.</p>
     */
    @Test
    public void testOnlyValueRestrictionPlacementFailure(){
        Die die = new Die(4, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(
                    die,
                    new Coordinates(0, 2),
                    Restriction.ONLY_VALUE);
            Assert.fail();
        } catch (PlacementErrorException e) {
            Assert.assertNull(sunCatcher.getGrid()[0][2].getDie());
        }
    }

    /**
     * Tests if the placement where the placed die must <strong>not</strong> be
     * adjacent to another acts correctly.
     */
    @Test
    public void testOnlyNotAdjacentPlacementSuccess(){
        Die firstDie = new Die(2, new Random(), Colour.BLUE);
        Die secondDie = new Die(2, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(firstDie, new Coordinates(0, 0));
        } catch (PlacementErrorException e) {
            Assert.fail("First die cannot be placed");
        }

        try{
            sunCatcher = sunCatcher.placeDie(
                    secondDie,
                    new Coordinates(1, 2),
                    Restriction.NOT_ADJACENT);
        } catch (PlacementErrorException e) {
            Assert.fail("Second die cannot be placed");
        }

        Assert.assertEquals(secondDie, sunCatcher.getGrid()[1][2].getDie());
    }

    /**
     * Tests if the placement where the placed die must <strong>not</strong> be
     * adjacent to another fails when a bad destination is provided.
     * <p>The state of the pattern after the failure must be the same as before,
     * meaning that the failed operation left no side effects.</p>
     */
    @Test
    public void testOnlyNotAdjacentPlacementFailure(){
        Die firstDie = new Die(2, new Random(), Colour.BLUE);
        Die secondDie = new Die(2, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(firstDie, new Coordinates(0, 0));
        } catch (PlacementErrorException e) {
            Assert.fail("First die cannot be placed");
        }

        try{
            sunCatcher = sunCatcher.placeDie(
                    secondDie,
                    new Coordinates(1, 1),
                    Restriction.NOT_ADJACENT);
            Assert.fail("Second die placed wrong");
        } catch (PlacementErrorException e) {
            Assert.assertNull(sunCatcher.getGrid()[1][1].getDie());
        }

    }

    /**
     * Tests if {@code moveDie} acts correctly in a regular case where all
     * restrictions are met.
     */
    @Test
    public void testMoveDieSuccessful(){
        Die firstDie = new Die(2, new Random(), Colour.YELLOW);
        Die secondDie = new Die(3, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(firstDie, new Coordinates(0, 0));
            sunCatcher = sunCatcher.placeDie(secondDie, new Coordinates(0, 1));
        } catch (PlacementErrorException e) {
            Assert.fail("Dice cannot be placed");
        }

        try {
            sunCatcher = sunCatcher.moveDie(
                    new Coordinates(0, 0),
                    new Coordinates(1, 0),
                    Restriction.DEFAULT);
        } catch (PlacementErrorException e) {
            Assert.fail();
        }

        Assert.assertNull(sunCatcher.getGrid()[0][0].getDie());
        Assert.assertEquals(firstDie, sunCatcher.getGrid()[1][0].getDie());
    }

    /**
     * Tests if {@code moveDie} fails when an invalid destination is provided.
     * <p>When the method fails, the pattern state must be fully restored to the
     * valid one it had just before the call.</p>
     */
    @Test
    public void testMoveDieFailure(){
        Die firstDie = new Die(2, new Random(), Colour.YELLOW);
        Die secondDie = new Die(3, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(firstDie, new Coordinates(0, 0));
            sunCatcher = sunCatcher.placeDie(secondDie, new Coordinates(0, 1));
        } catch (PlacementErrorException e) {
            Assert.fail("Dice cannot be placed");
        }

        try {
            sunCatcher = sunCatcher.moveDie(
                    new Coordinates(0, 0),
                    new Coordinates(1, 1),
                    Restriction.DEFAULT);
            Assert.fail();
        } catch (PlacementErrorException e) {
            Assert.assertNull(sunCatcher.getGrid()[1][1].getDie());
            Assert.assertEquals(firstDie, sunCatcher.getGrid()[0][0].getDie());
        }

    }

    /**
     * Tests if {@code moveDice} acts correctly in a regular case where all
     * restrictions are met.
     */
    @Test
    public void testMoveDiceSuccessful(){
        Die firstDie = new Die(2, new Random(), Colour.YELLOW);
        Die secondDie = new Die(3, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(firstDie, new Coordinates(0, 0));
            sunCatcher = sunCatcher.placeDie(secondDie, new Coordinates(0, 1));
        } catch (PlacementErrorException e) {
            Assert.fail("Dice cannot be placed");
        }

        Coordinates[] sources = {
                new Coordinates(0, 0),
                new Coordinates(0, 1)
        };
        Coordinates[] destinations = {
                new Coordinates(0, 4),
                new Coordinates(1, 4)
        };

        try {
            sunCatcher = sunCatcher.moveDice(sources, destinations);
        } catch (PlacementErrorException e) {
            Assert.fail(e.getMessage());
        }

        Assert.assertNull(sunCatcher.getGrid()[0][0].getDie());
        Assert.assertNull(sunCatcher.getGrid()[0][1].getDie());

        Assert.assertEquals(firstDie, sunCatcher.getGrid()[0][4].getDie());
        Assert.assertEquals(secondDie, sunCatcher.getGrid()[1][4].getDie());
    }

    /**
     * Tests if {@code moveDice} fails when invalid destinations are provided.
     * <p>When the method fails, the pattern state must be fully restored to the
     * valid one it had just before the call.</p>
     */
    @Test
    public void testMoveDiceFailure(){
        Die firstDie = new Die(2, new Random(), Colour.YELLOW);
        Die secondDie = new Die(3, new Random(), Colour.BLUE);

        try{
            sunCatcher = sunCatcher.placeDie(firstDie, new Coordinates(0, 0));
            sunCatcher = sunCatcher.placeDie(secondDie, new Coordinates(0, 1));
        } catch (PlacementErrorException e) {
            Assert.fail("Dice cannot be placed");
        }

        Coordinates[] sources = {
                new Coordinates(0, 0),
                new Coordinates(0, 1)
        };
        Coordinates[] destinations = {
                new Coordinates(0, 4),
                new Coordinates(1, 1)
        };

        try {
            sunCatcher = sunCatcher.moveDice(sources, destinations);
            Assert.fail();
        } catch (PlacementErrorException e) {
            Assert.assertNull(sunCatcher.getGrid()[0][4].getDie());
            Assert.assertNull(sunCatcher.getGrid()[1][1].getDie());
            Assert.assertEquals(firstDie, sunCatcher.getGrid()[0][0].getDie());
            Assert.assertEquals(secondDie, sunCatcher.getGrid()[0][1].getDie());
        }
    }

    /**
     * Tests if the count of empty cells is 0 on an empty pattern.
     */
    @Test
    public void testCountEmptyOnEmptyPattern(){
        int expected = Pattern.ROWS * Pattern.COLS;
        Assert.assertEquals(expected, sunCatcher.emptyCells());
    }

    /**
     * Tests if the count of empty cell is correct when some are filled
     * with a die.
     */
    @Test
    public void testCountEmptyOnPattern(){
        Random random = new Random();
        Die[] dice = {
                new Die(2, random, Colour.RED),
                new Die(5, random, Colour.BLUE),
                new Die(2, random, Colour.RED),
                new Die(1, random, Colour.GREEN),
                new Die(2, random, Colour.YELLOW),
        };

        try {
            for(int i = 0; i < dice.length; ++i)
            sunCatcher = sunCatcher.placeDie(dice[i], new Coordinates(0, i));
        } catch (PlacementErrorException e) {
            Assert.fail(e.getMessage());
        }

        int expected = Pattern.ROWS * Pattern.COLS - dice.length;

        Assert.assertEquals(expected, sunCatcher.emptyCells());
    }

}