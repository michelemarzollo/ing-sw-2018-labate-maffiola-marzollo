package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.SelectDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GrindingStoneBehaviourTest {

    @Test
    public void testAskParameters(){
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        GrindingStoneBehaviour behaviour = new GrindingStoneBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieSelection", mockView.getCalledMethods().get(0));
    }

    @Test
    public void testUsageSuccess(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");

        SelectDie message = new SelectDie(
                0,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrindingStoneBehaviour behaviour = new GrindingStoneBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        Die expectedDie = oldDraftPool.remove(0).flip();
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

        SelectDie message = new SelectDie(
                500,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrindingStoneBehaviour behaviour = new GrindingStoneBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());

        for(Die die : expectedDraftPool){
            boolean match = false;
            for(int j = 0; j < actualDraftPool.size() && !match; ++j){
                match = DieUtils.areEqual(die, actualDraftPool.get(j));
                if(match)
                    actualDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, actualIndex);

    }
}
