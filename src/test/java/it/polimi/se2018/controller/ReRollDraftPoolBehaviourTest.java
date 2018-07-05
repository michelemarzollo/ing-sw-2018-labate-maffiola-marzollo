package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

/**
 * Unit tests for ReRollDraftPoolBehaviour class.
 */
public class ReRollDraftPoolBehaviourTest {

    /**
     * Tests if requirements are not met when a player has already placed a die.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().placeDie();
        ReRollDraftPoolBehaviour behaviour = new ReRollDraftPoolBehaviour();
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

        ReRollDraftPoolBehaviour behaviour = new ReRollDraftPoolBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showConfirm", mockView.getCalledMethods().get(0));
    }

    /**
     * Tests a case in which the usage of the tool card is successful.
     * <p>This means that no views are selected during the process and that all
     * dice in the draft pool have been re-rolled.</p>
     */
    @Test
    public void testUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        ReRollDraftPoolBehaviour behaviour = new ReRollDraftPoolBehaviour();
        ToolCardBehaviourResponse response = behaviour.useToolCard(game, message);

        Assert.assertEquals(ToolCardBehaviourResponse.SUCCESS, response);
        Assert.assertEquals(0, mockView.getCalledMethods().size());
        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        //check dice have been rolled
        int expectedValue = new Random(0).nextInt(5) + 1;
        for (Die die : newDraftPool)
            Assert.assertEquals(expectedValue, die.getValue());

        // check all colours are kept
        for (Die expected : oldDraftPool) {
            boolean match = false;
            for (int j = 0; j < newDraftPool.size() && !match; ++j) {
                match = expected.getColour() == newDraftPool.get(j).getColour();
                if (match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

    }
}
