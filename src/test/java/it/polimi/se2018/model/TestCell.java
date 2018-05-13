package it.polimi.se2018.model;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * The class to test the methods of the class {@link Cell}.
 *
 * @author michelemarzollo
 */
public class TestCell {

    /**
     * Tests the constructor when gets an invalid argument.
     */
    @Test
    public void testConstructor() {
        try {
            Cell cell = new Cell(7);
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
            Cell cell = new Cell(0);
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
        Cell c = new Cell(3);
        assertEquals(3, c.getValue());
    }

    /**
     * Tests the method{@code getColour()}.
     */
    @Test
    public void testGetColour() {
        Cell c = new Cell(Colour.YELLOW);
        assertEquals(Colour.YELLOW, c.getColour());
    }

    /**
     * Tests the method {@code place} when the cell is already occupied by a die.
     * Checks that the die that already occupied the cell is still there.
     * (First branch)
     * <p>Also tests the constructor with no arguments.</p>
     */
    @Test
    public void testPlaceOccupied() {
        Die d1 = new Die(5, new Random(), Colour.BLUE);
        Cell c = new Cell();
        //Puts the first die in the cell
        try {
            c.place(d1);
        } catch (PlacementErrorException e) {
            fail();
        }

        //Tries to put a second die
        Die d2 = new Die(5, new Random(), Colour.BLUE);
        try {
            c.place(d2);
            fail();
        } catch (PlacementErrorException e) {
            assertEquals(c.getDie(), d1);
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
        Die d = new Die(6, new Random(), Colour.GREEN);
        Cell c = new Cell(5);
        try {
            c.place(d);
            fail();
        } catch (PlacementErrorException e) {
            //at the and there must be no placed die
            assertNull(c.getDie());
        }
    }

    /**
     * Tests the method {@code place} when the cell has a colour restriction which is
     * not respected by the die and verifies that the die is not placed.
     * (Third branch)
     */
    @Test
    public void testPlaceColourRestriction() {
        Die d = new Die(3, new Random(), Colour.GREEN);
        Cell c = new Cell(Colour.PURPLE);
        try {
            c.place(d);
            fail();
        } catch (PlacementErrorException e) {
            assertNull(c.getDie());
        }
    }

    /**
     * Tests the method {@code place} when the cell has no restriction
     * and verifies that the placed die is the correct one.
     * (Last branch)
     */
    @Test
    public void testPositivePlaceNoRestriction() {
        Die d = new Die(2, new Random(), Colour.PURPLE);
        Cell c = new Cell();
        try {
            c.place(d);
            //Verifies that the places die is the correct one
            assertEquals(c.getDie(), d);
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
        Die d = new Die(3, new Random(), Colour.PURPLE);
        Cell c = new Cell(Colour.PURPLE);
        try {
            c.place(d);
            //Verifies that the places die is the correct one
            assertEquals(c.getDie(), d);
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
        Die d = new Die(3, new Random(), Colour.PURPLE);
        Cell c = new Cell(3);
        try {
            c.place(d);
            //Verifies that the places die is the correct one
            assertEquals(c.getDie(), d);
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
        Cell c = new Cell();
        Die d = new Die(2, new Random(), Colour.RED);
        try {
            c.place(d);
        } catch (PlacementErrorException e) {
            fail();
        }

        Die d2 = c.remove();
        assertEquals(d2, d);
    }
}