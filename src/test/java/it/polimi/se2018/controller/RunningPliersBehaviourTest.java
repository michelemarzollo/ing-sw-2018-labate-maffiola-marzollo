package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.PlaceDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RunningPliersBehaviourTest {
    @Test
    public void testAskParameters() {
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        RunningPliersBehaviour behaviour = new RunningPliersBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieSelection", mockView.getCalledMethods().get(0));
    }

    @Test
    public void testUsageSuccess(){
        MockView mockView= new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        PlaceDie message = new PlaceDie(
                0,
                new Coordinates(1, 1),
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        RunningPliersBehaviour behaviour = new RunningPliersBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        Assert.assertTrue(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());
        Assert.assertTrue(game.getTurnManager().getPlayersToSkip().contains(player));

        List<Die> oldDraftPool = GameUtils.getDice(false);
        Die red2 = oldDraftPool.remove(0);
        Assert.assertTrue(DieUtils.areEqual(red2, player.getPattern().getGrid()[1][1].getDie()));

        List<Die> newDraftPool = game.getDraftPool().getDice();

        for(Die die : oldDraftPool){
            boolean match = false;
            for(int j = 0; j < newDraftPool.size() && !match; ++j){
                match = DieUtils.areEqual(die, newDraftPool.get(j));
                if(match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

    }

    private Game getFailure(MockView mockView, boolean badIndex){
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");
        Coordinates coordinates;
        if(badIndex)
            coordinates = new Coordinates(500, 500);
        else
            coordinates = new Coordinates(0, 0);
        PlaceDie message = new PlaceDie(
                0,
                coordinates,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        RunningPliersBehaviour behaviour = new RunningPliersBehaviour();
        behaviour.useToolCard(game, message);

        return game;
    }

    private void assertUnchanged(Game game, MockView mockView){
        Player player = game.getPlayers().get(0);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Assert.assertFalse(game.getTurnManager().getCurrentTurn().hasAlreadyPlacedDie());
        Assert.assertFalse(game.getTurnManager().getPlayersToSkip().contains(player));

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        for(Die die : oldDraftPool){
            boolean match = false;
            for(int j = 0; j < newDraftPool.size() && !match; ++j){
                match = DieUtils.areEqual(die, newDraftPool.get(j));
                if(match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }
    }

    @Test
    public void testUsageFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = getFailure(mockView, false);

        assertUnchanged(game, mockView);

        Cell[][] grid = game.getPlayers().get(0).getPattern().getGrid();

        Assert.assertNull(grid[0][0].getDie());
    }

    @Test
    public void testBadIndexFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = getFailure(mockView, true);

        assertUnchanged(game, mockView);
    }
}
