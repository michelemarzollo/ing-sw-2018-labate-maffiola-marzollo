package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.DiceSwap;
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

/**
 * Unit tests for LensCutterBehaviour class.
 */
public class LensCutterBehaviourTest {

    /**
     * Tests if requirements are not met when the player has already placed a die.
     */
    @Test
    public void testRequirements() {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().placeDie();
        LensCutterBehaviour behaviour = new LensCutterBehaviour();
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

        LensCutterBehaviour behaviour = new LensCutterBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showLensCutterSelection",
                mockView.getCalledMethods().get(0));
    }

    /**
     * Helper method that sets up a scenario where the application of the tool card
     * is successful.
     *
     * @param mockView The mock view used to register method calls.
     * @return A game instance where the application of the tool card is successful.
     */
    private Game setupSuccessScenario(MockView mockView) {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        fillRoundTrack(game);

        DiceSwap message = new DiceSwap(
                0,
                new Coordinates(0, 0),
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        LensCutterBehaviour behaviour = new LensCutterBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertTrue(success);
        return game;
    }

    /**
     * Helper method that sets up a scenario where the application of the tool card
     * has failed.
     *
     * @param mockView The mock view used to register method calls.
     * @return A game instance where the application of the tool card has failed.
     */
    private Game setupFailureScenario(MockView mockView) {
        Game game = GameUtils.getHalfwayGame();
        if (game == null)
            Assert.fail("Error on game initialization");

        fillRoundTrack(game);

        DiceSwap message = new DiceSwap(
                0,
                new Coordinates(0, 10),
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        LensCutterBehaviour behaviour = new LensCutterBehaviour();
        boolean success = behaviour.useToolCard(game, message);

        Assert.assertFalse(success);
        return game;
    }

    /**
     * Tests a case in which the usage of the tool card is successful.
     * <p>This means that no views are selected during the process and that
     * the selected dice are correctly swapped while the rest of the round track
     * and draft pool is not altered.</p>
     */
    @Test
    public void testUsageSuccess() {
        MockView mockView = new MockView("Pippo");
        Game game = setupSuccessScenario(mockView);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());

        Die swappedDie = oldDraftPool.remove(0);
        for (Die die : oldDraftPool) {
            boolean match = false;
            for (int j = 0; j < newDraftPool.size() && !match; ++j) {
                match = DieUtils.areEqual(die, newDraftPool.get(j));
                if (match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

        Assert.assertTrue(DieUtils.areEqual(
                new Die(4, new Random(), Colour.PURPLE),
                newDraftPool.get(0)));

        List<Die> roundOneLeftovers = game.getRoundTrack().getLeftovers().get(0);
        List<Die> expectedRoundLeftovers = new ArrayList<>();
        expectedRoundLeftovers.add(swappedDie);
        expectedRoundLeftovers.add(new Die(1, new Random(0), Colour.PURPLE));

        for (Die die : expectedRoundLeftovers) {
            boolean match = false;
            for (int j = 0; j < roundOneLeftovers.size() && !match; ++j) {
                match = DieUtils.areEqual(die, roundOneLeftovers.get(j));
                if (match)
                    roundOneLeftovers.remove(j);
            }
            Assert.assertTrue(match);
        }
        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertNotEquals(-1, actualIndex);
    }

    /**
     * Helper method that generates a well-known list of dice for the
     * round one section of the round track.
     * <p>The dice are a purple 4 and a purple 1.</p>
     *
     * @return A list of predefined dice.
     */
    private List<Die> getRoundOneLeftovers() {
        List<Die> dice = new ArrayList<>();
        dice.add(new Die(4, new Random(0), Colour.PURPLE));
        dice.add(new Die(1, new Random(0), Colour.PURPLE));
        return dice;
    }

    /**
     * Helper method that fills the first round of the round track with
     * well-known dice.
     *
     * @param game The game owning the round track.
     */
    private void fillRoundTrack(Game game) {
        game.getRoundTrack().addAllForRound(1, getRoundOneLeftovers());
    }

    /**
     * Tests a case in which the usage of the tool card is unsuccessful
     * because of an invalid selection in the round track.
     * <p>This means that an error view is selected during the process and that
     * no die is swapped and the round track and draft pool are not altered.</p>
     */
    @Test
    public void testUsageFailure() {
        MockView mockView = new MockView("Pippo");
        Game game = setupFailureScenario(mockView);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        Assert.assertEquals(oldDraftPool.size(), newDraftPool.size());

        for (Die die : oldDraftPool) {
            boolean match = false;
            for (int j = 0; j < newDraftPool.size() && !match; ++j) {
                match = DieUtils.areEqual(die, newDraftPool.get(j));
                if (match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

        List<Die> roundOneLeftovers = game.getRoundTrack().getLeftovers().get(0);
        List<Die> expectedRoundLeftovers = getRoundOneLeftovers();

        for (Die die : expectedRoundLeftovers) {
            boolean match = false;
            for (int j = 0; j < roundOneLeftovers.size() && !match; ++j) {
                match = DieUtils.areEqual(die, roundOneLeftovers.get(j));
                if (match)
                    roundOneLeftovers.remove(j);
            }
            Assert.assertTrue(match);
        }

        int actualIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        Assert.assertEquals(-1, actualIndex);
    }
}
