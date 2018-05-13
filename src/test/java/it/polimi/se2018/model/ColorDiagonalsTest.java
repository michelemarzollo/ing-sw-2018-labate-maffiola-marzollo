package it.polimi.se2018.model;

import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests for ColorDiagonals class.
 */
public class ColorDiagonalsTest {

    /**
     * Tests if the class handles instantiation as a singleton.
     */
    @Test
    public void testMultipleInstantiation() {
        ColorDiagonals firstInstance = ColorDiagonals.getInstance();
        ColorDiagonals secondInstance = ColorDiagonals.getInstance();
        Assert.assertSame(firstInstance, secondInstance);
    }

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid has only one diagonal of
     * dice of the same colour.</p>
     */
    @Test
    public void testSimpleScoreCalculation() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);
        Die blueDie = new Die(new Random(), Colour.BLUE);
        for (int i = 0; i < 3; ++i)
            try {
                grid[i][i].place(blueDie);
            } catch (PlacementErrorException e) {
                Assert.fail("PlacementErrorException has occurred: "
                        + e.getMessage());
            }

        int actualScore = ColorDiagonals.getInstance().getScore(grid);
        int expectedScore = 3;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly in a more complex case.
     * <p>In this case, the grid has several diagonals of dice of
     * the same colour.</p>
     */
    @Test
    public void testManyColoursScoreCalculation() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die blueDie = new Die(new Random(), Colour.BLUE);
        Die greenDie = new Die(new Random(), Colour.GREEN);
        Die purpleDie = new Die(new Random(), Colour.PURPLE);
        Die redDie = new Die(new Random(), Colour.RED);

        for (int i = 0; i < 3; ++i)
            try {
                grid[i][i].place(blueDie);
                grid[i][i + 1].place(redDie);
                grid[i + 1][i].place(greenDie);
                grid[i][i + 2].place(purpleDie);

            } catch (PlacementErrorException e) {
                Assert.fail("PlacementErrorException has occurred: "
                        + e.getMessage());
            }

        int actualScore = ColorDiagonals.getInstance().getScore(grid);
        int expectedScore = 3 * 4;
        Assert.assertEquals(expectedScore, actualScore);
    }


    /**
     * Tests if the score is calculated correctly in a tricky case.
     * <p>In this case, the grid has a zig-zag pattern of dice of
     * the same colour.</p>
     */
    @Test
    public void testZigZagScoreCalculation() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die blueDie = new Die(new Random(), Colour.BLUE);
        try {
            grid[0][0].place(blueDie);
            grid[0][2].place(blueDie);
            grid[1][1].place(blueDie);
            grid[1][3].place(blueDie);
        } catch (PlacementErrorException e) {
            Assert.fail("PlacementErrorException has occurred: "
                    + e.getMessage());
        }

        int actualScore = ColorDiagonals.getInstance().getScore(grid);
        int expectedScore = 4;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly when there is no
     * diagonal patterns.
     * <p>In this case, the grid has a corresponding score of 0.</p>
     */
    @Test
    public void testNoScore() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die blueDie = new Die(new Random(), Colour.BLUE);
        try {
            grid[0][0].place(blueDie);
            grid[0][1].place(blueDie);
            grid[1][0].place(blueDie);
            grid[1][4].place(blueDie);

        } catch (PlacementErrorException e) {
            Assert.fail("PlacementErrorException has occurred: "
                    + e.getMessage());
        }

        int actualScore = ColorDiagonals.getInstance().getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }
}
