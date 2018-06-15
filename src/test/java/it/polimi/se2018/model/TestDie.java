package it.polimi.se2018.model;

import org.junit.Test;

import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * The class to test the methods of the class {@link Die}.
 *
 * @author michelemarzollo
 */
public class TestDie {

    /**
     * Tests that it's not allowed to create a die with invalid value
     * (lower than the range).
     */
    @Test
    public void testFailedConstructor() {
        try {
            new Die(0, new Random(), Colour.RED);
            fail();
        } catch (DieValueException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests that it's not allowed to create a die with invalid value
     * (higher than the range).
     */
    @Test
    public void testFailedConstructor2() {
        try {
            new Die(7, new Random(), Colour.RED);
            fail();
        } catch (DieValueException e) {
            assertTrue(true);
        }
    }

    /**
     * Checks that when a die is rolled the colour remains the same.
     * <p>Also tests the method {@code getColour()} and the constructor with two parameters.</p>
     */
    @Test
    public void testRoll() {
        Die d1 = new Die(new Random(), Colour.GREEN);
        Die d2 = d1.roll();
        assertEquals(d2.getColour(), d1.getColour());
    }

    /**
     * Tests the method {@code flip()}: when a die d is flipped the new die must have
     * a value equal to {@code 7-d.getValue()} and the colour mustn't change.
     * <p>Also tests the method {@code getValue}.</p>
     */
    @Test
    public void testFlip() {
        Die d1 = new Die(new Random(), Colour.GREEN);
        Die d2 = d1.flip();
        assertTrue(d1.getValue() == 7 - d2.getValue() && d1.getColour() == d2.getColour());
    }

    /**
     * Tests the method decrease when it can be applied to a die.
     */
    @Test
    public void testDecrease() {
        Die d1 = new Die(4, new Random(), Colour.GREEN);
        Die d2 = d1.decrease();
        assertTrue(d2.getValue() == 3 && d1.getColour() == d2.getColour());
    }

    /**
     * Tests the behaviour of the method {@code decrease()} when it can't be
     * applied to a die: an exception must be thrown.
     */
    @Test
    public void testInvalidDecrease() {
        Die d1 = new Die(1, new Random(), Colour.GREEN);
        try {
            d1.decrease();
            fail();
        } catch (DieValueException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests the method increase when it can be applied to a die.
     */
    @Test
    public void testIncrease() {
        Die d1 = new Die(4, new Random(), Colour.GREEN);
        Die d2 = d1.increase();
        assertTrue(d2.getValue() == 5 && d1.getColour() == d2.getColour());
    }

    /**
     * Tests the behaviour of the method {@code increase()} when it can't be applied
     * to a die: an exception must be thrown.
     */
    @Test
    public void testInvalidIncrease() {
        Die d1 = new Die(6, new Random(), Colour.GREEN);
        try {
            d1.increase();
            fail();
        } catch (DieValueException e) {
            assertTrue(true);
        }
    }

    /**
     * Tests the method {@code hashCode()}.
     */
    @Test
    public void testHashCode(){
        Die die = new Die(3, new Random(), Colour.YELLOW);
        int hashCode = Objects.hash(die.getValue(), die.getColour());
        assertEquals(hashCode, die.hashCode());
    }

}