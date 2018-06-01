package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnManagerTest {

    private TurnManager twoPlayersManager;

    @Before
    public void setUp(){
        Game game = new Game();
        game.addPlayer(new Player("Player1"));
        game.addPlayer(new Player("Player2"));
        twoPlayersManager = new TurnManager(game.getPlayers());
    }

    @Test
    public void testTurnManagerConstructorNull(){
        try{
            TurnManager turnManager = new TurnManager(null);
            fail();
        }
        catch(NullPointerException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testTurnManagerConstructorEmpty(){
        try{
            ArrayList<Player> players = new ArrayList<>();
            TurnManager turnManager = new TurnManager(players);
            fail();
        }
        catch(IllegalArgumentException ex){
            assertTrue(true);
        }
    }


    @Test
    public void testValidIsLastTurnOfGame() {
        for(int i = 0; i < 9; i++){
            try {
                twoPlayersManager.setupNewRound();
            }
            catch(TurnManager.GameFinishedException ex){
                assertTrue(true);
            }
        }
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        assertTrue(twoPlayersManager.isLastTurnOfGame());
    }

    @Test
    public void testInvalidIsGameFinished() {
        for(int i = 0; i < 9; i++){
            try {
                twoPlayersManager.setupNewRound();
            }
            catch(TurnManager.GameFinishedException ex){
                assertTrue(true);
            }
        }
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        assertFalse(twoPlayersManager.isLastTurnOfGame());//the first player has still to make his last turn
    }

    
    @Test
    public void testSetUpNewRound() {
        try{
            twoPlayersManager.setupNewRound();
        }
        catch(TurnManager.GameFinishedException ex){
            fail();
        }
        assertEquals(2, twoPlayersManager.getRound());
        assertTrue(twoPlayersManager.getPlayersToSkip().isEmpty());
        twoPlayersManager.updateTurn();
        //Verifies that the rotation of the order has been applied
        assertEquals("Player2", twoPlayersManager.getCurrentTurn().getPlayer().getName());
        twoPlayersManager.updateTurn();
        assertEquals("Player1", twoPlayersManager.getCurrentTurn().getPlayer().getName());


    }

    @Test
    public void setUpNewRoundButGameFinished() {
        for(int i = 0; i < 9; i++){
            try{
                twoPlayersManager.setupNewRound();
            }
            catch(TurnManager.GameFinishedException ex){
                fail();
            }
        }
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        try{
            twoPlayersManager.setupNewRound();
            fail();
        }
        catch(TurnManager.GameFinishedException ex){
            assertTrue(true);
        }

    }

    @Test
    public void testUpdateTurnLimitCase(){
        twoPlayersManager.updateTurn();
        assertEquals("Player1", twoPlayersManager.getCurrentTurn().getPlayer().getName());
        assertTrue(twoPlayersManager.getCurrentTurn().isSecondTurnAvailable());
        twoPlayersManager.updateTurn();
        assertEquals("Player2", twoPlayersManager.getCurrentTurn().getPlayer().getName());
        assertTrue(twoPlayersManager.getCurrentTurn().isSecondTurnAvailable());
        twoPlayersManager.updateTurn();
        assertEquals("Player2", twoPlayersManager.getCurrentTurn().getPlayer().getName());
        assertFalse(twoPlayersManager.getCurrentTurn().isSecondTurnAvailable());
        assertTrue(twoPlayersManager.updateTurn()); //limit case because the next is the last turn remaining in the round
        assertEquals("Player1", twoPlayersManager.getCurrentTurn().getPlayer().getName());
        assertFalse(twoPlayersManager.getCurrentTurn().isSecondTurnAvailable());
    }
    @Test
    public void testUpdateTurnNoTurnLeft(){
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        assertFalse(twoPlayersManager.updateTurn());
    }


    @Test
    public void testUpdateTurnSkippingOnePlayer(){
        Game game = new Game();
        game.addPlayer(new Player("Player0"));
        game.addPlayer(new Player("Player1"));
        game.addPlayer(new Player("Player2"));
        TurnManager manager = new TurnManager(game.getPlayers());
        manager.updateTurn();
        manager.updateTurn();
        try {
            manager.consumeSecondTurn(manager.getCurrentTurn().getPlayer());
            //Player 1 has to be skipped at the second turn
        }
        catch(TurnManager.SecondTurnUnavailableException ex){
            fail();
        }
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        assertEquals("Player0", manager.getCurrentTurn().getPlayer().getName()); //verifies that Player 1 is skipped;
        assertFalse(manager.updateTurn());
    }

    @Test
    public void testUpdateTurnSkippingAllRemainingPlayer(){
        Game game = new Game();
        game.addPlayer(new Player("Player0"));
        game.addPlayer(new Player("Player1"));
        game.addPlayer(new Player("Player2"));
        TurnManager manager = new TurnManager(game.getPlayers());
        manager.updateTurn();
        try {
            manager.consumeSecondTurn(manager.getCurrentTurn().getPlayer());
            //Player 0 has to be skipped at the second turn
        }
        catch(TurnManager.SecondTurnUnavailableException ex){
            fail();
        }
        manager.updateTurn();
        try {
            manager.consumeSecondTurn(manager.getCurrentTurn().getPlayer());
            //Player 1 has to be skipped at the second turn
        }
        catch(TurnManager.SecondTurnUnavailableException ex){
            fail();
        }
        manager.updateTurn();
        manager.updateTurn();
        assertFalse(manager.updateTurn());
    }

    @Test
    public void testUpdateTurnSinglePlayer(){
        Game game = new Game();
        game.addPlayer(new Player("Player1"));
        TurnManager manager = new TurnManager(game.getPlayers());
        manager.updateTurn();
        assertEquals("Player1", manager.getCurrentTurn().getPlayer().getName());
        assertTrue(manager.getCurrentTurn().isSecondTurnAvailable());
        manager.updateTurn();
        assertEquals("Player1",manager.getCurrentTurn().getPlayer().getName());
        assertFalse(manager.getCurrentTurn().isSecondTurnAvailable());
        assertFalse(manager.updateTurn());
    }


    @Test
    public void testPositiveIsSecondTurnAvailable() {
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();//limit case (last first turn of the last player in the list)
        assertTrue(twoPlayersManager.isSecondTurnAvailable());
    }

    @Test
    public void testNegativeIsSecondTurnAvailable() {
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();//limit case (first second turn of the last player in the list);
        assertFalse(twoPlayersManager.isSecondTurnAvailable());
    }



    @Test
    public void testExceptionalConsumeSecondTurn() {
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        try{
            twoPlayersManager.consumeSecondTurn(twoPlayersManager.getCurrentTurn().getPlayer());
            fail();
        }
        catch(TurnManager.SecondTurnUnavailableException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testSuccessfulConsumeSecondTurn() {
        twoPlayersManager.updateTurn();
        twoPlayersManager.updateTurn();
        try{
            twoPlayersManager.consumeSecondTurn(twoPlayersManager.getCurrentTurn().getPlayer());
        }
        catch(TurnManager.SecondTurnUnavailableException ex){
            fail();
        }
    }

}


