package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.IncrementDieValue;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GrozingPliersBehaviourTest {

    @Test
    public void testAskParameters(){
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieSelection", mockView.getCalledMethods().get(0));
    }

    @Test
    public void testIncreaseUsageSuccess(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                0,
                true,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Die expectedDie = oldDraftPool.remove(0).increase();
        Assert.assertTrue(DieUtils.areEqual(expectedDie, newDraftPool.get(actualIndex)));

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
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                0,
                false,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, actualIndex);

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
    public void testDecreaseUsageSuccess(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                1,
                false,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Die expectedDie = oldDraftPool.remove(1).decrease();
        Assert.assertTrue(DieUtils.areEqual(expectedDie, newDraftPool.get(actualIndex)));

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
}
