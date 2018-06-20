package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.*;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Timer;

import static org.junit.Assert.*;

public class ControllerTest {

    /**
     * Tests the setter of PublicScoreStrategies.
     */
    @Test
    public void testAddPublicScoreStrategy(){
        Game game =  new Game();
        Controller controller = new SinglePlayerController(game, 10, 10);
        DiagonalScore diagonalScore = new DiagonalScore(2, true);
        controller.addPublicScoreStrategy(diagonalScore);
        assertEquals(diagonalScore, controller.getPublicScoreCalculators().get(0));
    }

    /**
     * Tests the getter of PublicScoreStrategies.
     */
    @Test
    public void testGetPublicScoreCalculators(){
        Game game =  new Game();
        Controller controller = new SinglePlayerController(game, 10, 10);
        DiagonalScore diagonalScore = new DiagonalScore(2, true);
        RowVarietyScore rowVarietyScore = new RowVarietyScore(2, true);
        controller.addPublicScoreStrategy(diagonalScore);
        controller.addPublicScoreStrategy(rowVarietyScore);
        PublicObjectiveScore publicObjectiveScore1 = controller.getPublicScoreCalculators().get(0);
        PublicObjectiveScore publicObjectiveScore2 = controller.getPublicScoreCalculators().get(1);
        assertEquals(diagonalScore, publicObjectiveScore1);
        assertEquals(rowVarietyScore, publicObjectiveScore2);
    }


    @Test
    public void testFillScoreBoardMustInvert() {
        Game game = GameUtils.getSetUpGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        game.getPlayers().get(0).setScore(49);
        game.getPlayers().get(1).setScore(50);
        controller.fillScoreBoard();
        assertEquals(50, game.getScoreBoard().get(0).getScore());
        assertEquals("Pluto", game.getScoreBoard().get(0).getName());
        assertEquals(49, game.getScoreBoard().get(1).getScore());
        assertEquals("Pippo", game.getScoreBoard().get(1).getName());
    }

    @Test
    public void testFillScoreBoardMustNotInvert() {
        Game game = GameUtils.getSetUpGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        game.getPlayers().get(0).setScore(50);
        game.getPlayers().get(1).setScore(49);
        controller.fillScoreBoard();
        assertEquals(50, game.getScoreBoard().get(0).getScore());
        assertEquals("Pippo", game.getScoreBoard().get(0).getName());
        assertEquals(49, game.getScoreBoard().get(1).getScore());
        assertEquals("Pluto", game.getScoreBoard().get(1).getName());
    }

    @Test
    public void testFindPlayerPositive() {
        Game game = new Game();
        Player player = new Player("Pippo");
        game.addPlayer(player);
        Controller controller = new MultiPlayerController(game, 100, 100);
        assertTrue(controller.findPlayer("Pippo").isPresent());
    }

    @Test
    public void testFindPlayerNegative() {
        Game game = new Game();
        Player player = new Player("Pippo");
        game.addPlayer(player);
        Controller controller = new MultiPlayerController(game, 100, 100);
        assertFalse(controller.findPlayer("Pluto").isPresent());
    }

    /*
    Tests that the turn updates correctly within the same
    Round: Round must not be updated
     */
    @Test
    public void testEndTurnSameRound() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        assertEquals("Pippo", game.getTurnManager().getCurrentTurn().getPlayer().getName());
        ViewMessage msg = new ViewMessage(view1, Action.END_TURN, "Pippo");
        controller.endTurn(msg); //invoke the endTurn through the performAction
        assertEquals("Pluto", game.getTurnManager().getCurrentTurn().getPlayer().getName());
    }

    @Test
    public void testEndTurnChangeRound() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        MockView view2 = new MockView("Pluto");
        ViewMessage msg1 = new ViewMessage(view1, Action.END_TURN, "Pippo");
        ViewMessage msg2 = new ViewMessage(view2, Action.END_TURN, "Pluto");
        controller.endTurn(msg1); //invoke the endTurn through the performAction
        controller.endTurn(msg2);
        controller.endTurn(msg2);
        assertEquals(1, game.getTurnManager().getRound());
        controller.endTurn(msg1);
        assertEquals(2, game.getTurnManager().getRound());
    }

    /**
     * The checkTurnEndMethod must return {@code false} because the Player
     * has not used the ToolCard yet, he only placed a die.
     */
    @Test
    public void testCheckTurnEndFalse() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        game.getTurnManager().getCurrentTurn().placeDie();
        assertFalse(controller.checkTurnEnd());
    }

    @Test
    public void testCheckTurnEndTrue() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        game.getTurnManager().getCurrentTurn().placeDie();
        game.getTurnManager().getCurrentTurn().useToolCard();
        assertTrue(controller.checkTurnEnd());
    }

    @Test
    public void testEndRoundGameNotFinished() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        MockView view2 = new MockView("Pluto");
        ViewMessage msg1 = new ViewMessage(view1, Action.END_TURN, "Pippo");
        ViewMessage msg2 = new ViewMessage(view2, Action.END_TURN, "Pluto");
        controller.endTurn(msg1); //invoke the endTurn through the performAction
        controller.endTurn(msg2);
        controller.endTurn(msg2);
        controller.endTurn(msg1);
        //check the conditions expected to be True when updating the Round
        assertEquals(2, game.getTurnManager().getRound());
        assertEquals("Pluto", game.getTurnManager().getCurrentTurn().getPlayer().getName());
    }

    @Test
    public void testEndRoundGameFinished() {
        Game game = GameUtils.getFinishedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        ViewMessage msg = new ViewMessage(view1, Action.END_TURN, "Pippo");
        controller.endRound(msg); //game is over, it will invoke the 'endGame' method.
       // assertTrue(view1.getCalledMethods().contains("showFinalView")); //ensures that the endGame method has been called
        // within endRound method
    }


    /**
     * This tests verifies that all the DraftPool's dice are put in the RoundTrack
     * at the position indicated by the current round and that the draftPool is now empty.
     */
    @Test
    public void testCleanDraftPool() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        int round = game.getTurnManager().getRound();
        List<Die> dice = game.getDraftPool().getDice();
        controller.cleanDraftPool();
        assertTrue(game.getDraftPool().getDice().isEmpty());
        assertEquals(dice, game.getRoundTrack().getLeftovers().get(round - 1));
    }

    @Test
    public void testRefillDraftPool() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        controller.refillDraftPool();
        assertEquals(5, game.getDraftPool().getDice().size() );
    }

    @Test
    public void testCanMovePositive() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        //The game is prepared so that at the beginning of the first Round is Pippo's turn
        assertTrue(controller.canMove("Pippo"));
    }

    @Test
    public void testCanMoveNegative() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        //The game is prepared so that at the beginning of the first Round is Pippo's turn
        assertFalse(controller.canMove("Pluto"));
    }

    @Test
    public void testPlaceDieNotYourTurn() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg = new PlaceDie(0, new Coordinates(2,3), view, Action.PLACE_DIE, "Pluto" );
        controller.placeDie(msg);
        assertTrue(view.getCalledMethods().contains("showError: Not your turn!"));
    }

    @Test
    public void testPlaceDieAlreadyPlacedDie() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg = new PlaceDie(0, new Coordinates(2,3), view, Action.PLACE_DIE, "Pippo" );
        game.getTurnManager().getCurrentTurn().placeDie();
        controller.placeDie(msg);
        assertTrue(view.getCalledMethods().contains("showError: You already placed a die!"));
    }

    @Test
    public void testPlaceDieCorrectPlacement() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        int initialLength = game.getDraftPool().getDice().size();
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg = new PlaceDie(0, new Coordinates(0,1), view, Action.PLACE_DIE, "Pippo" );
        controller.placeDie(msg);
        assertEquals(Colour.RED, game.getTurnManager().getCurrentTurn().getPlayer().getPattern().
                getGrid()[0][1].getDie().getColour());
        assertEquals(1, game.getTurnManager().getCurrentTurn().getPlayer().getPattern().
                getGrid()[0][1].getDie().getValue());
        assertEquals(initialLength, game.getDraftPool().getDice().size()+1);
    }


    @Test
    public void testPlaceDieWrongPlacement() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg = new PlaceDie(0, new Coordinates(0,0), view, Action.PLACE_DIE, "Pippo" );
        //Wrong placement: at grid[0][0] there is a restriction on the die's colour: it must be blue while
        //the selected die is red.
        controller.placeDie(msg);
        assertTrue(view.getCalledMethods().contains("showError: Placement doesn't respect restrictions!\n" +
                "The placedDie's colour is different from the colour restriction of the cell"));
    }
    @Test
    public void testPlaceDieWrongDieSelection() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg = new PlaceDie(5, new Coordinates(0,1), view, Action.PLACE_DIE, "Pippo" );
        //wrong selection: the DraftPool has 5 dice, not 6!
        controller.placeDie(msg);
        assertTrue(view.getCalledMethods().contains("showError: Invalid selection!"));
    }

    @Test
    public void testPlaceDieWithTurnEnd() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        game.getTurnManager().getCurrentTurn().useToolCard();
        //correct placement, after this the turn has to be updated because Pippo has done any action he could
        PlaceDie msg = new PlaceDie(0, new Coordinates(0,1), view, Action.PLACE_DIE, "Pippo" );
        controller.placeDie(msg);
        assertEquals("Pluto", game.getTurnManager().getCurrentTurn().getPlayer().getName());
    }

    /**
     * Tests the case in which the die to be placed is the one indicated
     * by the forcedSelectionIndex in the Turn class imposed by the use
     * of some tool card and the player indicates the right index.
     */
    @Test
    public void testPlaceDieValidForcedSelection() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(0);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg = new PlaceDie(0, new Coordinates(0,1), view, Action.PLACE_DIE, "Pippo" );
        controller.placeDie(msg);
        assertEquals(Colour.RED, game.getTurnManager().getCurrentTurn().getPlayer().getPattern().
                getGrid()[0][1].getDie().getColour());
        assertEquals(1, game.getTurnManager().getCurrentTurn().getPlayer().getPattern().
                getGrid()[0][1].getDie().getValue());
    }

    /**
     * Tests the case in which the die to be placed is the one indicated
     * by the forcedSelectionIndex in the Turn class imposed by the use
     * of some tool card and the player indicates a different index.
     */
    @Test
    public void testPlaceDieInvalidForcedSelection() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        game.getTurnManager().getCurrentTurn().setForcedSelectionIndex(0);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg = new PlaceDie(1, new Coordinates(0,1), view, Action.PLACE_DIE, "Pippo" );
        controller.placeDie(msg);
        assertTrue(view.getCalledMethods().contains("showError: Invalid selection!"));
    }

    @Test
    public void testActivateToolCardNotYourTurn() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pluto");
        controller.activateToolCard(msg);
        assertTrue(view.getCalledMethods().contains("showError: Not your turn!"));
    }

    @Test
    public void testActivateToolCardInvalidCard() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Lathekin", view, Action.ACTIVATE_TOOL_CARD, "Pippo");
        controller.activateToolCard(msg);
        assertTrue(view.getCalledMethods().contains("showError: The tool card doesn't exist."));
    }

    /**
     * Player can use a ToolCard, but not the selected one.
     */
    @Test
    public void testActivateToolCardRequirementsNotSatisfied() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo");
        game.getTurnManager().getCurrentTurn().placeDie();
        controller.activateToolCard(msg);
        assertTrue(view.getCalledMethods().contains("showError: You can't use this tool card now."));
    }

    @Test
    public void testActivateToolCardValid() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Grozing Pliers", view, Action.ACTIVATE_TOOL_CARD, "Pippo");
        controller.activateToolCard(msg);
        assertTrue(view.getCalledMethods().contains("showDieIncrementSelection"));
        assertEquals("Grozing Pliers", game.getTurnManager().getCurrentTurn().getSelectedToolCard().getName());
    }

    @Test
    public void testApplyToolCardNotYourTurn() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        //It's not Pluto's turn at the first turn of the first round
        IncrementDieValue msg = new IncrementDieValue(2,true, view, Action.APPLY_TOOL_CARD, "Pluto");
        game.getTurnManager().getCurrentTurn().setSelectedToolCard(toolCards[0]);
        controller.applyToolCard(msg);
        assertTrue(view.getCalledMethods().contains("showError: Not your turn"));
    }

    @Test
    public void testApplyToolCardNoActivation() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        IncrementDieValue msg = new IncrementDieValue(2,true, view, Action.APPLY_TOOL_CARD, "Pippo");
        controller.applyToolCard(msg);
        assertTrue(view.getCalledMethods().contains("showError: No toolCard has been activated yet"));
    }

    /**
     * Case in which the useToolCard method return false.
     */
    @Test
    public void testApplyToolCardInvalidData() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        //The dice in draftPool are 5: 7 is out of bound.
        IncrementDieValue msg = new IncrementDieValue(7,true, view, Action.APPLY_TOOL_CARD, "Pippo");
        game.getTurnManager().getCurrentTurn().setSelectedToolCard(toolCards[0]);
        controller.applyToolCard(msg);
        //assertEquals(null, game.getTurnManager().getCurrentTurn().getSelectedToolCard());
        assertEquals(4, game.getTurnManager().getCurrentTurn().getPlayer().getTokens());
        assertEquals("Pippo", game.getTurnManager().getCurrentTurn().getPlayer().getName());
    }

    @Test
    public void testApplyToolCardValid() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        IncrementDieValue msg = new IncrementDieValue(0,true, view, Action.APPLY_TOOL_CARD, "Pippo");
        game.getTurnManager().getCurrentTurn().setSelectedToolCard(toolCards[0]);
        controller.applyToolCard(msg);
        assertEquals(3, game.getTurnManager().getCurrentTurn().getPlayer().getTokens());
        assertTrue(game.getTurnManager().getCurrentTurn().hasAlreadyUsedToolCard());
        assertEquals("Pippo", game.getTurnManager().getCurrentTurn().getPlayer().getName());
    }

    @Test
    public void testApplyToolCardValidAndChangeTurn() {
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        ToolCard[] toolCards = {new ToolCard("Grozing Pliers", "Description", Colour.PURPLE),
                new ToolCard("Eglomise Brush", "Description", Colour.BLUE),
                new ToolCard("Copper Foil Burnisher", "Description", Colour.RED)};
        game.setToolCards(toolCards);
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        PlaceDie msg1 = new PlaceDie(0, new Coordinates(0,1), view, Action.PLACE_DIE, "Pippo");
        controller.placeDie(msg1);
        MoveDie msg2 = new MoveDie(new Coordinates(0,1), new Coordinates(0,1), view, Action.APPLY_TOOL_CARD, "Pippo");
        game.getTurnManager().getCurrentTurn().setSelectedToolCard(toolCards[1]);
        controller.applyToolCard(msg2);
        assertEquals(3, game.getPlayers().get(0).getTokens());
        //The turn must update since player has already used toolCard and placed a die.
        assertEquals("Pluto", game.getTurnManager().getCurrentTurn().getPlayer().getName());
    }


    @Test
    public void testSelectPatternInvalidPlayer() {
        Game game = GameUtils.getSetUpGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Duomo", view, Action.SELECT_PATTERN, "Paperino");
        controller.selectPattern(msg);
        assertTrue(view.getCalledMethods().contains("showError: Invalid player!"));
    }

    @Test
    public void testSelectPatternInvalidPattern() {
        Game game = GameUtils.getSetUpGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Battistero", view, Action.SELECT_PATTERN, "Pippo");
        controller.selectPattern(msg);
        assertTrue(view.getCalledMethods().contains("showError: Invalid pattern!"));
    }

    @Test
    public void testSelectPatternValidButGameNotReady() {
        Game game = GameUtils.getSetUpGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view = new MockView("Pippo");
        SelectCard msg = new SelectCard("Duomo", view, Action.SELECT_PATTERN, "Pippo");
        controller.selectPattern(msg);
        assertEquals("Duomo", game.getPlayers().get(0).getPattern().getName());
        assertEquals(4, game.getPlayers().get(0).getTokens());
        assertTrue(view.getCalledMethods().contains("showMultiPlayerGame"));
        //Pluto doesn't have chosen his pattern yet.
        assertNull(game.getPlayers().get(1).getPattern());
        //The game is not started yet because it is not true that ALL the players have chosen their pattern.
        assertFalse(game.isStarted());
    }

    @Test
    public void testSelectPatternValidAndGameReady() {
        Game game = GameUtils.getSetUpGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        MockView view1 = new MockView("Pippo");
        MockView view2 = new MockView("Pluto");
        SelectCard msg1 = new SelectCard("Duomo", view1, Action.SELECT_PATTERN, "Pippo");
        controller.selectPattern(msg1);
        SelectCard msg2 = new SelectCard("Duomo", view2, Action.SELECT_PATTERN, "Pluto");
        controller.selectPattern(msg2);
        assertEquals("Duomo", game.getPlayers().get(1).getPattern().getName());
        assertEquals(4, game.getPlayers().get(1).getTokens());
        assertTrue(view2.getCalledMethods().contains("showMultiPlayerGame"));
        //The game is  started  because it is true that ALL the players have chosen their pattern.
        assertTrue(game.isStarted());
        assertEquals("Pippo", game.getTurnManager().getCurrentTurn().getPlayer().getName());
        assertEquals(5, game.getDraftPool().getDice().size());
    }

    @Test
    public void finalizeMatch(){
        Game game = GameUtils.getStartedGame(true);
        if(game == null)
            Assert.fail("Error on game initialization");
        Controller controller = new MultiPlayerController(game, 100, 100);
        assertNotNull(controller.getGame());
        controller.finalizeMatch();
        assertNotNull(controller.getGame());
    }

}