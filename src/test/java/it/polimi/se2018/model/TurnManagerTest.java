package it.polimi.se2018.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnManagerTest {

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
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        for(int i = 0; i < 9; i++){
            try {
                manager.setupNewRound();
            }
            catch(TurnManager.GameFinishedException ex){
                assertTrue(true);
            }
        }
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        assertTrue(manager.isLastTurnOfGame());
    }

    @Test
    public void testInvalidIsGameFinished() {
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        for(int i = 0; i < 9; i++){
            try {
                manager.setupNewRound();
            }
            catch(TurnManager.GameFinishedException ex){
                assertTrue(true);
            }
        }
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        assertFalse(manager.isLastTurnOfGame());//the first player has still to make his last turn
    }

    
    @Test
    public void testSetUpNewRound() {
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        try{
            manager.setupNewRound();
        }
        catch(TurnManager.GameFinishedException ex){
            fail();
        }
        assertEquals(2, manager.getRound());
        assertTrue(manager.getPlayersToSkip().isEmpty());
        manager.updateTurn();
        //Verifies that the rotation of the order has been applied
        assertTrue(manager.getCurrentTurn().getPlayer().getName().equals("Player2"));
        manager.updateTurn();
        assertTrue(manager.getCurrentTurn().getPlayer().getName().equals("Player1"));


    }

    @Test
    public void setUpNewRoundButGameFinished() {
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        for(int i = 0; i < 9; i++){
            try{
                manager.setupNewRound();
            }
            catch(TurnManager.GameFinishedException ex){
                fail();
            }
        }
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        try{
            manager.setupNewRound();
            fail();
        }
        catch(TurnManager.GameFinishedException ex){
            assertTrue(true);
        }

    }

    @Test
    public void testUpdateTurnLimitCase(){
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        manager.updateTurn();
        assertEquals("Player1",manager.getCurrentTurn().getPlayer().getName());
        assertEquals(true,manager.getCurrentTurn().isSecondTurnAvailable());
        manager.updateTurn();
        assertEquals("Player2",manager.getCurrentTurn().getPlayer().getName());
        assertEquals(true,manager.getCurrentTurn().isSecondTurnAvailable());
        manager.updateTurn();
        assertEquals("Player2",manager.getCurrentTurn().getPlayer().getName());
        assertEquals(false,manager.getCurrentTurn().isSecondTurnAvailable());
        assertTrue(manager.updateTurn()); //limit case because the next is the last turn remaining in the round
        assertEquals("Player1",manager.getCurrentTurn().getPlayer().getName());
        assertEquals(false,manager.getCurrentTurn().isSecondTurnAvailable());
    }
    @Test
    public void testUpdateTurnNoTurnLeft(){
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        assertFalse(manager.updateTurn());
    }


    @Test
    public void testUpdateTurnSkippingOnePlayer(){
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player0"));
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
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
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player0"));
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
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
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        TurnManager manager = new TurnManager(players);
        manager.updateTurn();
        assertEquals("Player1", manager.getCurrentTurn().getPlayer().getName());
        assertEquals(true,manager.getCurrentTurn().isSecondTurnAvailable());
        manager.updateTurn();
        assertEquals("Player1",manager.getCurrentTurn().getPlayer().getName());
        assertEquals(false,manager.getCurrentTurn().isSecondTurnAvailable());
        assertFalse(manager.updateTurn());
    }


    @Test
    public void testPositiveIsSecondTurnAvailable() {
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        manager.updateTurn();
        manager.updateTurn();//limit case (last first turn of the last player in the list)
        assertTrue(manager.isSecondTurnAvailable());
    }

    @Test
    public void testNegativeIsSecondTurnAvailable() {
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();//limit case (first second turn of the last player in the list);
        assertFalse(manager.isSecondTurnAvailable());
    }



    @Test
    public void testExceptionalConsumeSecondTurn() {
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        manager.updateTurn();
        manager.updateTurn();
        manager.updateTurn();
        try{
            manager.consumeSecondTurn(manager.getCurrentTurn().getPlayer());
            fail();
        }
        catch(TurnManager.SecondTurnUnavailableException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testSuccessfulConsumeSecondTurn() {
        ArrayList<Player> players= new ArrayList<>();
        players.add(new Player("Player1"));
        players.add(new Player("Player2"));
        TurnManager manager = new TurnManager(players);
        manager.updateTurn();
        manager.updateTurn();
        try{
            manager.consumeSecondTurn(manager.getCurrentTurn().getPlayer());
        }
        catch(TurnManager.SecondTurnUnavailableException ex){
            fail();
        }
    }

}


