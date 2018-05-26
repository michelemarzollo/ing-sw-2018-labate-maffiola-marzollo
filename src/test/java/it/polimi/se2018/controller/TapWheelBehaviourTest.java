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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TapWheelBehaviourTest {

    @Test
    public void testRequirements(){
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");
        TapWheelBehaviour behaviour = new TapWheelBehaviour();
        Assert.assertTrue(behaviour.areRequirementsSatisfied(game));
    }

    @Test
    public void testAskParameters() {
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        TapWheelBehaviour behaviour = new TapWheelBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showMoveSelection2", mockView.getCalledMethods().get(0));
    }

    @Test
    public void testUsageSuccess(){
        MockView mockView = new MockView("Pippo");
        Game game = setupSuccessScenario(mockView);
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(0,mockView.getCalledMethods().size());

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);

        Cell[][] grid = player.getPattern().getGrid();
        Assert.assertTrue(DieUtils.areEqual(yellow3, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[2][2].getDie()));

    }

    private Game setupSuccessScenario(MockView mockView){
        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[]{
                        new Coordinates(1, 0),
                        new Coordinates(2, 2)
                },
                new Coordinates[]{
                        new Coordinates(2, 2),
                        new Coordinates(1, 0)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        return setupScenario(message, true, true);
    }

    private boolean placeSomeDice(Player player) {
        Pattern pattern = player.getPattern();
        Die red2 = new Die(2, new Random(0), Colour.RED);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);
        try {
            pattern = pattern.placeDie(red2, new Coordinates(1, 1));
            pattern = pattern.placeDie(yellow3, new Coordinates(2, 2));
        } catch (PlacementErrorException e) {
            return false;
        }

        player.setPattern(pattern);
        return true;
    }

    private List<Die> getLeftovers(boolean insertYellow){
        List<Die> dice = new ArrayList<>();
        dice.add(new Die(2, new Random(0), Colour.PURPLE));
        dice.add(new Die(5, new Random(0), Colour.PURPLE));
        if(insertYellow)
            dice.add(new Die(6, new Random(0), Colour.YELLOW));
        return dice;
    }

    private void fillRoundTrack(Game game, boolean insertYellow){
        game.getRoundTrack().addAllForRound(1, getLeftovers(insertYellow));
    }


    private Game setupScenario(MoveTwoDice message, boolean insertYellow, boolean isSuccess){
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        fillRoundTrack(game, insertYellow);
        boolean control = placeSomeDice(player);
        Assert.assertTrue("Error on placement", control);

        TapWheelBehaviour behaviour = new TapWheelBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertEquals(isSuccess, success);

        return game;
    }

    private Game setupBadColourScenario(MockView mockView){
        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[]{
                        new Coordinates(1, 0),
                        new Coordinates(2, 2)
                },
                new Coordinates[]{
                        new Coordinates(2, 2),
                        new Coordinates(1, 0)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        return setupScenario(message, false, false);
    }

    @Test
    public void testBadColourUsageFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = setupBadColourScenario(mockView);
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);

        Cell[][] grid = player.getPattern().getGrid();
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(yellow3, grid[2][2].getDie()));
    }

    @Test
    public void testBadAmountsUsageFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = setupBadAmountScenario(mockView);
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Die yellow6 = new Die(6, new Random(0), Colour.YELLOW);
        Die yellow3 = new Die(3, new Random(0), Colour.YELLOW);

        Cell[][] grid = player.getPattern().getGrid();
        Assert.assertTrue(DieUtils.areEqual(yellow6, grid[1][0].getDie()));
        Assert.assertTrue(DieUtils.areEqual(yellow3, grid[2][2].getDie()));
    }

    private Game setupBadAmountScenario(MockView mockView) {
        MoveTwoDice message = new MoveTwoDice(
                new Coordinates[]{
                        new Coordinates(1, 0),
                        new Coordinates(2, 2)
                },
                new Coordinates[]{
                        new Coordinates(2, 2)
                },
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );
        return setupScenario(message, true, false);
    }
}
