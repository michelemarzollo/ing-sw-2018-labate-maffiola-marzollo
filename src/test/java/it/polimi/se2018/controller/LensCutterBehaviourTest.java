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

public class LensCutterBehaviourTest {
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

    private Game setupSuccessScenario(MockView mockView){
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
        behaviour.useToolCard(game, message);
        return game;
    }

    private Game setupFailureScenario(MockView mockView){
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
        behaviour.useToolCard(game, message);
        return game;
    }

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

    private List<Die> getRoundOneLeftovers() {
        List<Die> dice = new ArrayList<>();
        dice.add(new Die(4, new Random(0), Colour.PURPLE));
        dice.add(new Die(1, new Random(0), Colour.PURPLE));
        return dice;
    }

    private void fillRoundTrack(Game game) {
        game.getRoundTrack().addAllForRound(1, getRoundOneLeftovers());
    }

    @Test
    public void testUsageFailure(){
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
