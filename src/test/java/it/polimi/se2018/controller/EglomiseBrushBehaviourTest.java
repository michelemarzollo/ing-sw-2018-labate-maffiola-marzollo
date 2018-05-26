package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.MoveDie;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.DieUtils;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class EglomiseBrushBehaviourTest {

    @Test
    public void testAskParameters(){
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        EglomiseBrushBehaviour behaviour = new EglomiseBrushBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        Assert.assertEquals("showMoveSelection1", mockView.getCalledMethods().get(0));
    }

    @Test
    public void testUsageSuccess(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();

        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        MoveDie message = new MoveDie(
                new Coordinates(1, 0),
                new Coordinates(3, 3),
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        EglomiseBrushBehaviour behaviour = new EglomiseBrushBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());

        Die yellow6 = new Die(6, new Random(), Colour.YELLOW);
        Assert.assertNull(player.getPattern().getGrid()[1][0].getDie());
        Assert.assertTrue(DieUtils.areEqual(yellow6, player.getPattern().getGrid()[3][3].getDie()));
    }

    @Test
    public void testUsageFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();

        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        MoveDie message = new MoveDie(
                new Coordinates(1, 0),
                new Coordinates(2, 0),
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        EglomiseBrushBehaviour behaviour = new EglomiseBrushBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Die yellow6 = new Die(6, new Random(), Colour.YELLOW);
        Assert.assertNull(player.getPattern().getGrid()[0][0].getDie());
        Assert.assertTrue(DieUtils.areEqual(yellow6, player.getPattern().getGrid()[1][0].getDie()));
    }

    @Test
    public void testBadIndexUsageFailure(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();

        if(game == null)
            Assert.fail("Error on game initialization");
        Player player = game.getPlayers().get(0);

        MoveDie message = new MoveDie(
                new Coordinates(1, 0),
                new Coordinates(50, 50),
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        EglomiseBrushBehaviour behaviour = new EglomiseBrushBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(1, mockView.getCalledMethods().size());
        boolean isError = mockView.getCalledMethods().get(0).startsWith("showError");
        Assert.assertTrue(isError);

        Die yellow6 = new Die(6, new Random(), Colour.YELLOW);
        Assert.assertNull(player.getPattern().getGrid()[0][0].getDie());
        Assert.assertTrue(DieUtils.areEqual(yellow6, player.getPattern().getGrid()[1][0].getDie()));
    }
}
