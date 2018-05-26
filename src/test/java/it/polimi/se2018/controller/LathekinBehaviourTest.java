package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.MoveTwoDice;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class LathekinBehaviourTest {
    @Test
    public void testAskParameters(){
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        LathekinBehaviour behaviour = new LathekinBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showMoveSelection2", mockView.getCalledMethods().get(0));
    }

    @Test
    public void testUsageSuccess(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);
        boolean control = placeAnotherDie(player);
        Assert.assertTrue("Error on placement", control);

        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[] {
                        new Coordinates(1, 0),
                        new Coordinates(1, 1)
                },
                new Coordinates[]{
                        new Coordinates(1, 4),
                        new Coordinates(0, 4)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        LathekinBehaviour behaviour = new LathekinBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        Cell[][] grid = player.getPattern().getGrid();

        Assert.assertNull(grid[1][0].getDie());
        Assert.assertNull(grid[1][1].getDie());

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die red2 = new Die(2, new Random(0), Colour.RED);
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[1][4].getDie()));
        Assert.assertTrue(DieUtils.areEqual(red2, grid[0][4].getDie()));
    }

    private boolean placeAnotherDie(Player player) {
        Pattern pattern = player.getPattern();
        Die red2 = new Die(2, new Random(0), Colour.RED);
        try {
            pattern = pattern.placeDie(red2, new Coordinates(1, 1));
        } catch (PlacementErrorException e) {
            return false;
        }

        player.setPattern(pattern);
        return true;
    }


    @Test
    public void testUsageFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);
        boolean control = placeAnotherDie(player);
        Assert.assertTrue("Error on placement", control);

        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[] {
                        new Coordinates(1, 0),
                        new Coordinates(1, 1)
                },
                new Coordinates[]{
                        new Coordinates(1, 4),
                        new Coordinates(3, 4)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        LathekinBehaviour behaviour = new LathekinBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Cell[][] grid = player.getPattern().getGrid();

        Assert.assertNull(grid[1][4].getDie());
        Assert.assertNull(grid[3][4].getDie());

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die red2 = new Die(2, new Random(0), Colour.RED);
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(red2, grid[1][1].getDie()));
    }


    @Test
    public void testBadIndexUsageFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);
        boolean control = placeAnotherDie(player);
        Assert.assertTrue("Error on placement", control);

        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[] {
                        new Coordinates(1, 0),
                        new Coordinates(1, 1)
                },
                new Coordinates[]{
                        new Coordinates(1, 4),
                        new Coordinates(500, 500)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        LathekinBehaviour behaviour = new LathekinBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Cell[][] grid = player.getPattern().getGrid();

        Assert.assertNull(grid[1][4].getDie());
        Assert.assertNull(grid[3][4].getDie());

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die red2 = new Die(2, new Random(0), Colour.RED);
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(red2, grid[1][1].getDie()));
    }
}
