package it.polimi.se2018.model;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * The class to test the methods of the class {@link Cell}.
 *
 * @author michelemarzollo
 */
public class CellTest {

    /**
     * Tests the constructor when gets an invalid argument.
     */
    @Test
    public void testConstructor() {
        try {
            new Cell(7);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests the constructor when gets an invalid argument (lower than the range).
     */
    @Test
    public void testConstructor2() {
        try {
            new Cell(0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests the method{@code getValue()}.
     */
    @Test
    public void testGetValue() {
        Cell cell = new Cell(3);
        assertEquals(3, cell.getValue());
    }

    /**
     * Tests the method{@code getColour()}.
     */
    @Test
    public void testGetColour() {
        Cell cell = new Cell(Colour.YELLOW);
        assertEquals(Colour.YELLOW, cell.getColour());
    }

    /**
     * Tests the method {@code place} when the cell is already occupied by a die.
     * Checks that the die that already occupied the cell is still there.
     * (First branch)
     * <p>Also tests the constructor with no arguments.</p>
     */
    @Test
    public void testPlaceOccupied() {
        Die die1 = new Die(5, new Random(), Colour.BLUE);
        Cell cell = new Cell();
        //Puts the first die in the cell
        try {
            cell.place(die1);
        } catch (PlacementErrorException e) {
            fail(e.getMessage());
        }

        //Tries to put a second die
        Die die2 = new Die(5, new Random(), Colour.BLUE);
        try {
            cell.place(die2);
            fail();
        } catch (PlacementErrorException e) {
            assertEquals(cell.getDie(), die1);
        }
    }

    /**
     * Tests the method {@code place} when the cell has a value restriction which is
     * not respected by the die and verifies that the die is not placed.
     * (Second branch)
     * <p>Also tests the constructor with a valid argument.</p>
     */
    @Test
    public void testPlaceValueRestriction() {
        Die die = new Die(6, new Random(), Colour.GREEN);
        Cell cell = new Cell(5);
        try {
            cell.place(die);
            fail();
        } catch (PlacementErrorException e) {
            //at the and there must be no placed die
            assertNull(cell.getDie());
        }
    }

    /**
     * Tests the method {@code place} when the cell has a colour restriction which is
     * not respected by the die and verifies that the die is not placed.
     * (Third branch)
     */
    @Test
    public void testPlaceColourRestriction() {
        Die die = new Die(3, new Random(), Colour.GREEN);
        Cell cell = new Cell(Colour.PURPLE);
        try {
            cell.place(die);
            fail();
        } catch (PlacementErrorException e) {
            assertNull(cell.getDie());
        }
    }

    /**
     * Tests the method {@code place} when the cell has no restriction
     * and verifies that the placed die is the correct one.
     * (Last branch)
     */
    @Test
    public void testPositivePlaceNoRestriction() {
        Die die = new Die(2, new Random(), Colour.PURPLE);
        Cell cell = new Cell();
        try {
            cell.place(die);
            //Verifies that the places die is the correct one
            assertEquals(cell.getDie(), die);
        } catch (PlacementErrorException e) {
            fail();
        }
    }

    /**
     * Tests the method {@code place} when the cell has a restriction which is respected
     * by the die and verifies that the placed die is the correct one.
     * (Last branch,in this case a colour restriction)
     */
    @Test
    public void testPositivePlaceColourRestriction() {
        Die die = new Die(3, new Random(), Colour.PURPLE);
        Cell cell = new Cell(Colour.PURPLE);
        try {
            cell.place(die);
            //Verifies that the places die is the correct one
            assertEquals(cell.getDie(), die);
        } catch (PlacementErrorException e) {
            fail();
        }
    }

    /**
     * Tests the method {@code place} when the cell has a restriction which is respected
     * by the die and verifies that the placed die is the correct one.
     * (Last branch,in this case a colour restriction)
     */
    @Test
    public void testPositivePlaceValueRestriction() {
        Die die = new Die(3, new Random(), Colour.PURPLE);
        Cell cell = new Cell(3);
        try {
            cell.place(die);
            //Verifies that the places die is the correct one
            assertEquals(cell.getDie(), die);
        } catch (PlacementErrorException e) {
            fail();
        }
    }

    /**
     * Tests the method to remove a die. At the end the cell must have
     * {@code die = null} and the the die returned must be the one that was in the cell.
     */
    @Test
    public void testRemove() {
        //Creation of a cell, with a die on it
        Cell cell = new Cell();
        Die die = new Die(2, new Random(), Colour.RED);
        try {
            cell.place(die);
        } catch (PlacementErrorException e) {
            fail();
        }

        Die d2 = cell.remove();
        assertEquals(d2, die);
    }

    @Test
    public void testLoosePlaceSuccess(){
        Die die = new Die(3, new Random(), Colour.PURPLE);
        Cell cell = new Cell(3);
        try {
            cell.place(die, Restriction.ONLY_COLOUR);
            assertEquals(cell.getDie(), die);
        } catch (PlacementErrorException e) {
            fail();
        }
    }

    @Test
    public void testLoosePlaceFailure(){
        Die die = new Die(3, new Random(), Colour.PURPLE);
        Cell cell = new Cell(3);
        try {
            cell.place(die, Restriction.ONLY_VALUE);
            assertEquals(cell.getDie(), die);
        } catch (PlacementErrorException e) {
            fail();
        }
    }
}