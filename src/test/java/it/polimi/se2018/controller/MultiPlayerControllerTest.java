package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.*;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import it.polimi.se2018.view.View;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.Timer;

import static org.junit.Assert.*;

public class MultiPlayerControllerTest {

    /**
     * Verifies that the showError method is invoked when receiving
     * an invalid ViewMessage.
     * The method must be tested through the performAction method because
     * showError is private.
     */
    @Test
    public void testPerformActionInvalidMessage() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectDifficulty msg = new SelectDifficulty(3, view, "Pippo");
        controller.performAction(msg);
        assertTrue(view.getCalledMethods().contains("showError: An invalid message was sent"));
        assertEquals(1, view.getCalledMethods().size());
    }

    /**
     * Tests canUseToolCard method of MultiPlayer Controller in
     * a case in which is possible to use the ToolCard: it is Pippo's turn,
     * the requested toolCard is part of the game and Pippo has enough tokens.
     */
    @Test
    public void testPositiveCanUseToolCard() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo");
        boolean canUse = controller.canUseToolCard(msg, toolCards[0]);
        assertTrue(canUse);
        assertTrue(view.getCalledMethods().isEmpty());
    }


    /**
     * Tests the case in which player has not enough tokens and
     * the toolCard has NOT been used (costs 1 tokens).
     */
    @Test
    public void testCanUseToolCardNotEnoughTokens() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        game.getPlayers().get(0).setTokens(0);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo");
        controller.canUseToolCard(msg, toolCards[0]);
        assertTrue(view.getCalledMethods().contains("showError: You don't have enough tokens."));
        assertEquals(1, view.getCalledMethods().size());
        assertEquals(0, game.getPlayers().get(0).getTokens());
    }


    /**
     * Tests the case in which player has not enough tokens and
     * the toolCard has  been used (costs 2 tokens).
     */
    @Test
    public void testCanUseToolCardNotEnoughTokensUsed() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        game.getPlayers().get(0).setTokens(1);
        game.getToolCards()[0].use();
        //now the ToolCard 'Grozing Pliers' costs two tokens.
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo");
        controller.canUseToolCard(msg, toolCards[0]);
        assertTrue(view.getCalledMethods().contains("showError: You don't have enough tokens."));
        assertEquals(1, view.getCalledMethods().size());
        assertEquals(1, game.getPlayers().get(0).getTokens());
    }


    @Test
    public void testDisplayGame() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        ViewMessage msg = new ViewMessage(view, Action.END_TURN, "Pippo"); //generic action, just to have a ViewMessage
        controller.displayGame(msg);
        assertTrue(view.getCalledMethods().contains("showMultiPlayerGame"));
    }


    @Test
    public void testGetDraftAmount() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        assertEquals(5, controller.getDraftAmount());
    }


    @Test
    public void testConsumeResourcesNotUsedToolCard() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        game.getTurnManager().getCurrentTurn().setSelectedToolCard(toolCards[0]);
        IncrementDieValue msg = new IncrementDieValue(2, true, view, Action.APPLY_TOOL_CARD, "Pippo");
        int previousTokens = game.getPlayers().get(0).getTokens();
        controller.consumeResources(msg);
        assertEquals(previousTokens, game.getPlayers().get(0).getTokens() + 1);
    }

    /**
     * Similar to the previous, but in this casse the game also has
     * Public Objective cards, and a player gets points from one if those.
     */
    @Test
    public void testConsumeResourcesUsedToolCard() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        MockView view2 = new MockView("Pluto");
        game.getTurnManager().getCurrentTurn().setSelectedToolCard(toolCards[0]);
        IncrementDieValue msg = new IncrementDieValue(2, true, view1, Action.APPLY_TOOL_CARD, "Pippo");
        toolCards[0].use();
        controller.consumeResources(msg);
        int previousTokens = game.getPlayers().get(1).getTokens();
        assertEquals(previousTokens, game.getPlayers().get(0).getTokens() + 2);
    }


    /**
     * Tests the calculateScores supposing not to have PublicObjectiveCards.
     * The test suppose that Pippo has placed a blue die with value 6 in position [1][0]
     * and that Pluto has placed a red die with value 5 in the same position.
     * Pippo's score must be : 6 (value of the die of his private objective placed) -
     * 19 (number of empty cells left) +
     * 4 (number of tokens)
     * Pluto's score must be : 5 (value of the die of his private objective placed) -
     * 19 (number of empty cells left) +
     * 4 (number of tokens)      *
     */
    @Test
    public void testCalculateScores() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        Die blue6 = new Die(6, new Random(), Colour.BLUE);
        Die red5 = new Die(5, new Random(), Colour.RED);
        Player pippo = game.getPlayers().get(0);
        Player pluto = game.getPlayers().get(1);
        try {
            Pattern newPattern1 = pippo.getPattern()
                    .placeDie(blue6, new Coordinates(1, 0));
            pippo.setPattern(newPattern1);
            Pattern newPattern2 = pluto.getPattern()
                    .placeDie(red5, new Coordinates(1, 0));
            pluto.setPattern(newPattern2);
        } catch (PlacementErrorException e) {
            //will never enter here
            fail();
        }
        controller.calculateScores();
        assertEquals(6 - 19 + 4, pippo.getScore());
        assertEquals(5 - 19 + 4, pluto.getScore());
    }

    @Test
    public void testCalculateScoresWithCards() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        Die blue6 = new Die(6, new Random(), Colour.BLUE);
        Die red5 = new Die(5, new Random(), Colour.RED);
        Player pippo = game.getPlayers().get(0);
        Player pluto = game.getPlayers().get(1);
        PublicObjectiveCard[] publicObjectiveCards = {
                new PublicObjectiveCard("Color Variety", "TestDescription", 4),
                new PublicObjectiveCard("Deep Shades", "TestDescription", 2),
                new PublicObjectiveCard("Row Color Variety", "TestDescription", 6)
        };
        game.setPublicObjectiveCards(publicObjectiveCards);
        try {
            Pattern newPattern1 = pippo.getPattern()
                    .placeDie(blue6, new Coordinates(1, 0));
            pippo.setPattern(newPattern1);
            newPattern1 = pippo.getPattern()
                    .placeDie(red5, new Coordinates(1, 1));
            pippo.setPattern(newPattern1);
            Pattern newPattern2 = pluto.getPattern()
                    .placeDie(red5, new Coordinates(1, 0));
            pluto.setPattern(newPattern2);
        } catch (PlacementErrorException e) {
            //will never enter here
            fail();
        }
        controller.calculateScores();
        assertEquals(6 - 18 + 4 + 2, pippo.getScore());
        assertEquals(5 - 19 + 4, pluto.getScore());
    }

    @Test
    public void testRegisterPlayerSetUpComplete() {
        Game game = new Game();
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        MockView view2 = new MockView("Pluto");
        MockView view3 = new MockView("Topolino");
        MockView view4 = new MockView("Paperino");
        ViewMessage msg1 = new ViewMessage(view1, Action.REGISTER_PLAYER, "Pippo");
        ViewMessage msg2 = new ViewMessage(view2, Action.REGISTER_PLAYER, "Pluto");
        ViewMessage msg3 = new ViewMessage(view3, Action.REGISTER_PLAYER, "Topolino");
        ViewMessage msg4 = new ViewMessage(view4, Action.REGISTER_PLAYER, "Paperino");
        controller.registerPlayer(msg1);
        controller.registerPlayer(msg2);
        controller.registerPlayer(msg3);
        controller.registerPlayer(msg4);
        assertTrue(game.isSetupComplete());
    }

    /**
     * Checks that if there ae already four players in the game and a message
     * with action {@code REGISTER_PLAYER} is received, the registration is refused.
     */
    @Test
    public void testNegativeRegisterPlayerSetUpComplete() {
        Game game = new Game();
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        MockView view2 = new MockView("Pluto");
        MockView view3 = new MockView("Topolino");
        MockView view4 = new MockView("Paperino");
        MockView view5 = new MockView("Paperone");
        ViewMessage msg1 = new ViewMessage(view1, Action.REGISTER_PLAYER, "Pippo");
        ViewMessage msg2 = new ViewMessage(view2, Action.REGISTER_PLAYER, "Pluto");
        ViewMessage msg3 = new ViewMessage(view3, Action.REGISTER_PLAYER, "Topolino");
        ViewMessage msg4 = new ViewMessage(view4, Action.REGISTER_PLAYER, "Paperino");
        controller.registerPlayer(msg1);
        controller.registerPlayer(msg2);
        controller.registerPlayer(msg3);
        controller.registerPlayer(msg4);
        ViewMessage msg5 = new ViewMessage(view5, Action.REGISTER_PLAYER, "Paperone");
        controller.registerPlayer(msg5);
        assertEquals(4, game.getPlayers().size());
        assertTrue(view5.getCalledMethods().contains("showError: There are already four registered players!"));
    }

    @Test
    public void testRegisterPlayerSetUpNotComplete() {
        Game game = new Game();
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        MockView view2 = new MockView("Pluto");
        MockView view3 = new MockView("Topolino");
        ViewMessage msg1 = new ViewMessage(view1, Action.REGISTER_PLAYER, "Pippo");
        ViewMessage msg2 = new ViewMessage(view2, Action.REGISTER_PLAYER, "Pluto");
        ViewMessage msg3 = new ViewMessage(view3, Action.REGISTER_PLAYER, "Topolino");
        controller.registerPlayer(msg1);
        controller.registerPlayer(msg2);
        controller.registerPlayer(msg3);
        assertFalse(game.isSetupComplete());
    }

    @Test
    public void testEndGame() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        //The endGame method is invoked as a consequence of EndRound when the Game is finished.
        //Its ViewMessage could be, for example, an EndTurn message.
        ViewMessage msg = new ViewMessage(view, Action.END_TURN, "Pippo");
        controller.endGame(msg);
        assertTrue(game.getDraftPool().getDice().isEmpty());
        //ensures that the calculateScores method has been invoked.
        assertTrue(game.getPlayers().get(0).getScore() != 0);
        assertTrue(game.getPlayers().get(1).getScore() != 0);
        //before endGame method invocation the scoreBoard is not even instantiated.
        assertNotNull(game.getScoreBoard());
        // assertTrue(view.getCalledMethods().contains("showFinalView"));
        assertEquals(0, view.getCalledMethods().size());
    }

    /**
     * Tests the disconnection of two players when the setup is complete and the
     * messages are correct. When there are no more players the game must be set to null.
     */
    @Test
    public void testDisconnectPlayerSetUpCompleted() {
        Game game = GameUtils.getSetUpGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        ViewMessage msg = new ViewMessage(view1, Action.DISCONNECT_PLAYER, "Pippo");
        controller.disconnectPlayer(msg);
        assertFalse(game.getPlayers().get(0).isConnected());
        assertTrue(game.getPlayers().get(1).isConnected());

        MockView view2 = new MockView("Pluto");
        ViewMessage msg2 = new ViewMessage(view2, Action.DISCONNECT_PLAYER, "Pluto");
        controller.disconnectPlayer(msg2);
        assertFalse(game.getPlayers().get(0).isConnected());
        assertFalse(game.getPlayers().get(1).isConnected());
    }

    /**
     * If the message contains the name of a player which is not connected, no
     * player should be disconnected.
     */
    @Test
    public void testNegativeDisconnectPlayerSetUpCompleted() {
        Game game = GameUtils.getSetUpGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        ViewMessage msg = new ViewMessage(view1, Action.DISCONNECT_PLAYER, "Giovanni");
        controller.disconnectPlayer(msg);
        assertTrue(game.getPlayers().get(0).isConnected());
        assertTrue(game.getPlayers().get(1).isConnected());
    }

    @Test
    public void testDisconnectPlayerSetUpNotCompleted() {
        Game game = new Game();
        Player player = new Player("Pippo");
        game.addPlayer(player);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        ViewMessage msg = new ViewMessage(view1, Action.DISCONNECT_PLAYER, "Pippo");
        controller.disconnectPlayer(msg);
        assertTrue(game.getPlayers().isEmpty());
    }

    @Test
    public void testValidReconnectPlayer() {
        Game game = GameUtils.getSetUpGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        game.getPlayers().get(0).setConnected(false);
        ViewMessage msg = new ViewMessage(view1, Action.RECONNECT_PLAYER, "Pippo");
        controller.reconnectPlayer(msg);
        assertTrue(game.getPlayers().get(0).isConnected());
    }

    @Test
    public void testInvalidReconnectPlayer() {
        Game game = GameUtils.getSetUpGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        game.getPlayers().get(0).setConnected(false);
        ViewMessage msg = new ViewMessage(view1, Action.RECONNECT_PLAYER, "Paperino");
        controller.reconnectPlayer(msg);
        assertFalse(game.getPlayers().get(0).isConnected());
    }

}