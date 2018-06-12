package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class GridVarietyScoreTest {

    //Tests in the case in which the shades are of two values (refers to the card Light Shades)

    /**
     * Tests if the score is calculated correctly in a simple case,
     * when the shades must be of two values.
     * <p>In this simple case, the grid contains three pairs of dice
     * the value of which is 1 and 2.</p>
     */
    @Test
    public void testSimpleScoreCalculationLightShades() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[6];
        for (int i = 0; i < dice.length; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        try {
            for (int i = 0; i < grid[0].length; ++i)
                grid[0][i].place(dice[1]);
            for (int i = 0; i < grid[0].length - 2; ++i)
                grid[1][i].place(dice[0]);


        } catch (PlacementErrorException e) {
            Assert.fail("PlacementErrorException has occurred: "
                    + e.getMessage());
        }

        GridVarietyScore gridVarietyScore = new GridVarietyScore(2, false, new Integer[]{1, 2});
        int actualScore = gridVarietyScore.getScore(grid);
        int expectedScore = 2 * 3;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly when there are no pairs.
     * <p>In this test case, the grid doesn't contain any pair of dice
     * the value of which is 1 and 2.</p>
     */
    @Test
    public void testNoScoreLightShades() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[6];
        for (int i = 0; i < dice.length; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        try {
            for (int i = 0; i < grid[0].length; ++i)
                grid[0][i].place(dice[1]);

        } catch (PlacementErrorException e) {
            Assert.fail("PlacementErrorException has occurred: "
                    + e.getMessage());
        }

        GridVarietyScore gridVarietyScore = new GridVarietyScore(2, false, new Integer[]{1, 2});
        int actualScore = gridVarietyScore.getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }

    //Tests for the card Shade Variety (complete occurrences of the 6 values)

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid contains two complete sets of dice of
     * different values.</p>
     */
    @Test
    public void testSimpleScoreCalculationShadeVariety() {
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
        Integer[] values = {1, 2, 3, 4, 5, 6};
        GridVarietyScore gridVarietyScore = new GridVarietyScore(5, false, values);
        int actualScore = gridVarietyScore.getScore(grid);
        int expectedScore = 5 * 2;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is 0 when there are no complete sets of dice of different values.
     */
    @Test
    public void testNoScoreShadeVariety() {
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

        Integer[] values = {1, 2, 3, 4, 5, 6};
        GridVarietyScore gridVarietyScore = new GridVarietyScore(5, false, values);
        int actualScore = gridVarietyScore.getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }

    //Tests for the card Colour Variety (complete occurrences of all colours)

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid contains two sets of dice of
     * different colours.</p>
     */
    @Test
    public void testSimpleScoreCalculationColorVariety() {
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

        GridVarietyScore gridVarietyScore = new GridVarietyScore(4, true, Colour.values());
        int actualScore = gridVarietyScore.getScore(grid);
        int expectedScore = 4 * 2;
        Assert.assertEquals(expectedScore, actualScore);
    }


    /**
     * Tests if the score is 0 when there are no sets of dice of different colours.
     */
    @Test
    public void testNoScoreColorVariety() {
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

        GridVarietyScore gridVarietyScore = new GridVarietyScore(4, true, Colour.values());
        int actualScore = gridVarietyScore.getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }

}
