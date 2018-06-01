package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurnTest {

    private Player player;

    @Before
    public void setUp(){
        Game game = new Game();
        player = new Player("Giocatore");
        game.addPlayer(player);
    }

    @Test
    public void testGetPlayer() {
        Turn turn = new Turn(player, true);
        assertEquals(player, turn.getPlayer());
    }

    @Test
    public void testIsSecondTurnAvailable() {
        Turn turn = new Turn(player, true);
        assertTrue(turn.isSecondTurnAvailable());
    }


    @Test
    public void testPositivePlaceDie() {
        Turn turn = new Turn(player, true);
        turn.placeDie();
        assertTrue(turn.hasAlreadyPlacedDie());
    }


    @Test
    public void testPositiveUseToolCard() {
        Turn turn = new Turn(player, true);
        turn.useToolCard();
        assertTrue(turn.hasAlreadyUsedToolCard());
    }

}