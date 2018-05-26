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

public class FluxBrushBehaviourTest {

    @Test
    public void testAskParameters(){
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        FluxBrushBehaviour behaviour = new FluxBrushBehaviour();
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
                1,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        FluxBrushBehaviour behaviour = new FluxBrushBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        Die die = expectedDraftPool.remove(1).roll();
        expectedDraftPool.add(die);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());

        for (Die expectedDie : expectedDraftPool) {
            boolean match = false;
            for (int j = 0; j < actualDraftPool.size() && !match; j++) {
                match = DieUtils.areEqual(
                        expectedDie,
                        actualDraftPool.get(j));
            }
            Assert.assertTrue(match);
        }

        int expectedIndex = findIndex(die, actualDraftPool);
        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(expectedIndex, actualIndex);

    }

    private int findIndex(Die die, List<Die> dice) {
        for (int i = 0; i < dice.size(); i++) {
            if(DieUtils.areEqual(die, dice.get(i)))
                return i;
        }
        return -1;
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

        FluxBrushBehaviour behaviour = new FluxBrushBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());

        for (Die expectedDie : expectedDraftPool) {
            boolean match = false;
            for (int j = 0; j < actualDraftPool.size() && !match; j++) {
                match = DieUtils.areEqual(
                        expectedDie,
                        actualDraftPool.get(j));
            }
            Assert.assertTrue(match);
        }

        int expectedIndex = -1;
        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(expectedIndex, actualIndex);

    }
}
