package it.polimi.se2018.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TurnTest {

    @Test
    public void testGetPlayer() {
        Player player = new Player("Giocatore");
        Turn turn = new Turn(player, true);
        assertEquals(player, turn.getPlayer());
    }

    @Test
    public void testIsSecondTurnAvailable() {
        Player player = new Player("Giocatore");
        Turn turn = new Turn(player, true);
        assertTrue(turn.isSecondTurnAvailable());
    }


    @Test
    public void testPositivePlaceDie() {
        Player player = new Player("Giocatore");
        Turn turn = new Turn(player, true);
        turn.placeDie();
        assertTrue(turn.hasAlreadyPlacedDie());
    }


    @Test
    public void testPositiveUseToolCard() {
        Player player = new Player("Giocatore");
        Turn turn = new Turn(player, true);
        turn.useToolCard();
        assertTrue(turn.hasAlreadyUsedToolCard());
    }

}