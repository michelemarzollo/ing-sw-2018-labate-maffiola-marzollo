package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.GridUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class PrivateObjectiveScoreTest {

    /**
     * Tests if the score is calculated correctly in a simple case.
     * <p>In this simple case, the gird contains five dice of value 4
     * with the same colour as the private objective card.</p>
     */
    @Test
    public void testSimpleScoreCalculation() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);
        Die redDie = new Die(4, new Random(), Colour.RED);

        try {
            grid[0][0].place(redDie);
            grid[1][1].place(redDie);
            grid[2][2].place(redDie);
            grid[3][3].place(redDie);
            grid[0][4].place(redDie);
        } catch (PlacementErrorException e) {
            Assert.fail();
        }

        PrivateObjectiveScore cardScore = new PrivateObjectiveScore();
        cardScore.setColour(Colour.RED);
        int actualScore = cardScore.getScore(grid);
        int expectedScore = 4 * 5;

        Assert.assertEquals(expectedScore, actualScore);
    }

    /**
     * Tests if the score is calculated correctly on an empty grid.
     */
    @Test
    public void testEmptyGridScore() {
        Cell[][] grid = GridUtils.getEmptyUnrestrictedGrid(4, 5);

        PrivateObjectiveScore cardScore = new PrivateObjectiveScore();
        cardScore.setColour(Colour.GREEN);
        int actualScore = cardScore.getScore(grid);

        Assert.assertEquals(0, actualScore);
    }
}
