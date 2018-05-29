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

/**
 * Unit tests for GrindingStoneBehaviour class.
 */
public class GrindingStoneBehaviourTest {

    /**
     * Tests id requirements are not met when a player has already placed a die.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().placeDie();
        GrindingStoneBehaviour behaviour = new GrindingStoneBehaviour();
        Assert.assertFalse(behaviour.areRequirementsSatisfied(game));
    }

    /**
     * Tests that the correct view is selected when asking parameters.
     */
    @Test
    public void testAskParameters() {
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

    /**
     * Tests a case in which the usage of the tool card is successful.
     * <p>This means that no views are selected during the process and that the
     * selected die is correctly flipped and set as the only option for drafting,
     * while the other dice in the draft pool are not altered.</p>
     */
    @Test
    public void testUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        SelectDie message = new SelectDie(
                0,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrindingStoneBehaviour behaviour = new GrindingStoneBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        Die expectedDie = oldDraftPool.remove(0).flip();
        Assert.assertTrue(DieUtils.areEqual(expectedDie, newDraftPool.get(actualIndex)));

        for (Die die : oldDraftPool) {
            boolean match = false;
            for (int j = 0; j < newDraftPool.size() && !match; ++j) {
                match = DieUtils.areEqual(die, newDraftPool.get(j));
                if (match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful
     * because of a bad selection index.
     * <p>This means that an error view is selected during the process and that
     * no die in the draft pool is altered nor set as the only option for drafting.</p>
     */
    @Test
    public void testUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        SelectDie message = new SelectDie(
                500,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrindingStoneBehaviour behaviour = new GrindingStoneBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        List<Die> expectedDraftPool = GameUtils.getDice(false);
        List<Die> actualDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(expectedDraftPool.size(), actualDraftPool.size());

        for (Die die : expectedDraftPool) {
            boolean match = false;
            for (int j = 0; j < actualDraftPool.size() && !match; ++j) {
                match = DieUtils.areEqual(die, actualDraftPool.get(j));
                if (match)
                    actualDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, actualIndex);

    }
}
