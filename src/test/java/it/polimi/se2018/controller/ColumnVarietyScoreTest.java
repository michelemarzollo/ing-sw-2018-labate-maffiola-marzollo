package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Unit tests for {@link ColumnVarietyScore} class.
 *
 * @author michelemarzollo
 */
public class ColumnVarietyScoreTest {

    //TESTS FOR THE CASE IN WHICH THE PROPERTY OF THE CARD IS THE COLOUR

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid contains two columns of dice of
     * different colours.</p>
     */
    @Test
    public void testSimpleScoreCalculationColour() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = {
                new Die(new Random(), Colour.BLUE),
                new Die(new Random(), Colour.GREEN),
                new Die(new Random(), Colour.PURPLE),
                new Die(new Random(), Colour.RED)
        };

        for (int row = 0; row < 4; ++row)
            for (int col = 0; col < 2; ++col)
                try {
                    grid[row][col].place(dice[row]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        ColumnVarietyScore columnVarietyScore = new ColumnVarietyScore(5, true);
        int actualScore = columnVarietyScore.getScore(grid);
        int expectedScore = 5 * 2;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly when there is repetition.
     * <p>In this test case, the grid contains two dice of the same colour
     * in the same columns.</p>
     */
    @Test
    public void testRepetitionScoreCalculationColour() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = {
                new Die(new Random(), Colour.BLUE),
                new Die(new Random(), Colour.GREEN),
                new Die(new Random(), Colour.PURPLE),
                new Die(new Random(), Colour.BLUE)
        };

        for (int row = 0; row < 4; ++row)
            for (int col = 0; col < 2; ++col)
                try {
                    grid[row][col].place(dice[row]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        ColumnVarietyScore columnVarietyScore = new ColumnVarietyScore(5, true);
        int actualScore = columnVarietyScore.getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly when there are gaps.
     * <p>In this test case, the grid contains partially filled columns.</p>
     */
    @Test
    public void testIncompleteColumnsScoreCalculationColour() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = {
                new Die(new Random(), Colour.BLUE),
                new Die(new Random(), Colour.GREEN),
                new Die(new Random(), Colour.PURPLE)
        };

        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 2; ++col)
                try {
                    grid[row][col].place(dice[row]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        ColumnVarietyScore columnVarietyScore = new ColumnVarietyScore(5, true);
        int actualScore = columnVarietyScore.getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }

    //TESTS FOR THE CASE IN WHICH THE PROPERTY OF THE CARD IS THE VALUE

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the grid contains two columns of dice of
     * different values.</p>
     */
    @Test
    public void testSimpleScoreCalculationValue() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[4];
        for (int i = 0; i < dice.length; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        for (int row = 0; row < 4; ++row)
            for (int col = 0; col < 2; ++col)
                try {
                    grid[row][col].place(dice[row]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        ColumnVarietyScore columnVarietyScore = new ColumnVarietyScore(4, false);
        int actualScore = columnVarietyScore.getScore(grid);
        int expectedScore = 4 * 2;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly when there is repetition.
     * <p>In this test case, the grid contains two dice of the same value
     * in the same columns.</p>
     */
    @Test
    public void testRepetitionScoreCalculationValue() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[4];
        for (int i = 0; i < dice.length - 1; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        dice[dice.length - 1] = new Die(1, new Random(), Colour.YELLOW);

        for (int row = 0; row < 4; ++row)
            for (int col = 0; col < 2; ++col)
                try {
                    grid[row][col].place(dice[row]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        ColumnVarietyScore columnVarietyScore = new ColumnVarietyScore(4, false);
        int actualScore = columnVarietyScore.getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly when there are gaps.
     * <p>In this test case, the grid contains partially filled columns.</p>
     */
    @Test
    public void testIncompleteColumnsScoreCalculationValue() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        Die[] dice = new Die[4];
        for (int i = 0; i < dice.length - 1; ++i)
            dice[i] = new Die(i + 1, new Random(), Colour.YELLOW);

        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 2; ++col)
                try {
                    grid[row][col].place(dice[row]);
                } catch (PlacementErrorException e) {
                    Assert.fail("PlacementErrorException has occurred: "
                            + e.getMessage());
                }

        ColumnVarietyScore columnVarietyScore = new ColumnVarietyScore(4, false);
        int actualScore = columnVarietyScore.getScore(grid);
        int expectedScore = 0;
        Assert.assertEquals(expectedScore, actualScore);
    }


}
