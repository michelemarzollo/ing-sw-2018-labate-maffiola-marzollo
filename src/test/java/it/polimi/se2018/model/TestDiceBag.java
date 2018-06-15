package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * The class to test the class {@link DiceBag}.
 *
 * @author michelemarzollo
 */
public class TestDiceBag {

    /**
     * The DiceBag that will be used for all tests.
     */
    private DiceBag bag;

    /**
     * Initialization of the bag.
     */
    @Before
    public void setUp() {
        bag = new DiceBag();
    }

    /**
     * Tests the method {@code draft()}: the length of the list returned
     * must be the one specified and the list must contain dice.
     */
    @Test
    public void testDraft() {
        List<Die> list = bag.draft(7);
        assertEquals(7, list.size());
        assertTrue(list.get(3).getValue() > 0 && list.get(3).getValue() < 7);
    }

    /**
     * Tests the method {@code pushBack()}: when the bag is empty, if i add a die with
     * {@code pushBack()} and than call {@code pushBack()} of 1 die,
     * the die returned must be equals to the one I pushed back.
     */
    @Test
    public void testPushBack() {
        bag.draft(90);
        Die die = new Die(4, new Random(), Colour.GREEN);
        bag.pushBack(die);
        List<Die> list1 = bag.draft(1);
        assertEquals(die, list1.get(0));
    }

    /**
     * Checks that the dice bag contains exactly 90 dice and throws an exception if
     * more are required.
     */
    @Test
    public void testDimension() {
        try {
            bag.draft(90);
        } catch (IllegalArgumentException e) {
            fail();
        }
        //Tries to draft a die when the bag is empty
        try {
            bag.draft(1);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Checks that the exception thrown by {@code draft()} is thrown in the proper way.
     */
    @Test
    public void testDraftException() {
        try {
            bag.draft(40);
        } catch (IllegalArgumentException e) {
            fail();
        }
        //Tries to draft more dice than the ones contained in the DiceBag
        try {
            bag.draft(60);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}