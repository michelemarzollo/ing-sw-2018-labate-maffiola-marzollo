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

/**
 * Unit tests for GrozingPliersBehaviour class.
 */
public class GrozingPliersBehaviourTest {

    /**
     * Tests if requirements are not met when a player has already placed a die.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().placeDie();
        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
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

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showDieSelection", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests a case in which the usage of the tool card in increase mode is successful.
     * <p>This means that no views are selected during the process and that the
     * selected die is correctly increased and set as the only option for drafting,
     * while the other dice in the draft pool are not altered.</p>
     */
    @Test
    public void testIncreaseUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                0,
                true,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Die expectedDie = oldDraftPool.remove(0).increase();
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
     * because the value can't be decreased (it's a 1).
     * <p>This means that an error view is selected during the process and that
     * no die in the draft pool is altered nor set as the only option for
     * drafting.</p>
     */
    @Test
    public void testUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                0,
                false,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

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
     * Tests a case in which the usage of the tool card in decrease mode is successful.
     * <p>This means that no views are selected during the process and that the
     * selected die is correctly decreased and set as the only option for drafting,
     * while the other dice in the draft pool are not altered.</p>
     */
    @Test
    public void testDecreaseUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        IncrementDieValue message = new IncrementDieValue(
                1,
                false,
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GrozingPliersBehaviour behaviour = new GrozingPliersBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        Assert.assertEquals(0, mockView.getCalledMethods().size());

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Die expectedDie = oldDraftPool.remove(1).decrease();
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
}