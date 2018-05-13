package it.polimi.se2018.model;

import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests for ShadeVariety class.
 */
public class ShadeVarietyTest {

    /**
     * Tests if the class handles instantiation as a singleton.
     */
    @Test
    public void testMultipleInstantiation() {
        ShadeVariety firstInstance = ShadeVariety.getInstance();
        ShadeVariety secondInstance = ShadeVariety.getInstance();
        Assert.assertSame(firstInstance, secondInstance);
    }

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid contains two sets of dice of
     * different values.</p>
     */
    @Test
    public void testSimpleScoreCalculation() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);


        Die[] dice = new Die[6];
        for (int i = 0; i < dice.length; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        try {
            for (int row = 0; row < 2; ++row)
                for (int col = 0; col < 5; ++col)
                    grid[row][col].place(dice[col]);

            grid[2][0].place(dice[5]);
            grid[2][1].place(dice[5]);
        } catch (PlacementErrorException e) {
            Assert.fail("PlacementErrorException has occurred: "
                    + e.getMessage());

        }

        int actualScore = ShadeVariety.getInstance().getScore(grid);
        int expectedScore = 5 * 2;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is 0 when there are no sets of dice of different values.
     */
    @Test
    public void testNoScore() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[6];
        for (int i = 0; i < dice.length; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);


        for (int row = 0; row < 2; ++row)
            for (int col = 0; col < 5; ++col)
                try {
                    grid[row][col].place(dice[col]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        int actualScore = ShadeVariety.getInstance().getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }
}
