package it.polimi.se2018.model;

import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests for LightShades class.
 */
public class LightShadesTest {

    /**
     * Tests if the class handles instantiation as a singleton.
     */
    @Test
    public void testMultipleInstantiation() {
        LightShades firstInstance = LightShades.getInstance();
        LightShades secondInstance = LightShades.getInstance();
        Assert.assertSame(firstInstance, secondInstance);
    }

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid contains three pairs of dice
     * the value of which is 1 and 2.</p>
     */
    @Test
    public void testSimpleScoreCalculation() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[6];
        for (int i = 0; i < dice.length; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        try {
            for(int i = 0; i < grid[0].length; ++i)
                grid[0][i].place(dice[1]);
            for(int i = 0; i < grid[0].length - 2; ++i)
                grid[1][i].place(dice[0]);


        } catch (PlacementErrorException e) {
            Assert.fail("PlacementErrorException has occurred: "
                    + e.getMessage());
        }


        int actualScore = LightShades.getInstance().getScore(grid);
        int expectedScore = 2 * 3;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly when there are no pairs.
     * <p>In this test case, the grid doesn't contain any pair of dice
     * the value of which is 1 and 2.</p>
     */
    @Test
    public void testNoScore() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[6];
        for (int i = 0; i < dice.length; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        try {
            for(int i = 0; i < grid[0].length; ++i)
                grid[0][i].place(dice[1]);

        } catch (PlacementErrorException e) {
            Assert.fail("PlacementErrorException has occurred: "
                    + e.getMessage());
        }

        int actualScore = LightShades.getInstance().getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }
}
