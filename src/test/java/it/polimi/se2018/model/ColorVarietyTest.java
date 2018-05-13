package it.polimi.se2018.model;

import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests for ColorVariety class.
 */
public class ColorVarietyTest {

    /**
     * Tests if the class handles instantiation as a singleton.
     */
    @Test
    public void testMultipleInstantiation() {
        ColorVariety firstInstance = ColorVariety.getInstance();
        ColorVariety secondInstance = ColorVariety.getInstance();
        Assert.assertSame(firstInstance, secondInstance);
    }

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid contains two sets of dice of
     * different colours.</p>
     */
    @Test
    public void testSimpleScoreCalculation() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = {
                new Die(new Random(), Colour.BLUE),
                new Die(new Random(), Colour.GREEN),
                new Die(new Random(), Colour.PURPLE),
                new Die(new Random(), Colour.RED),
                new Die(new Random(), Colour.YELLOW)
        };

        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 5; ++col) {
                try {
                    grid[row][col].place(dice[col]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }
            }
        }

        int actualScore = ColorVariety.getInstance().getScore(grid);
        int expectedScore = 4 * 2;
        Assert.assertEquals(expectedScore, actualScore);
    }


    /**
     * Tests if the score is 0 when there are no sets of dice of different colours.
     */
    @Test
    public void testNoScore() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = {
                new Die(new Random(), Colour.BLUE),
                new Die(new Random(), Colour.GREEN),
                new Die(new Random(), Colour.PURPLE),
                new Die(new Random(), Colour.RED),
                new Die(new Random(), Colour.RED)
        };

        for (int row = 0; row < 2; ++row)
            for (int col = 0; col < 5; ++col)
                try {
                    grid[row][col].place(dice[col]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        int actualScore = ColorVariety.getInstance().getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }
}
