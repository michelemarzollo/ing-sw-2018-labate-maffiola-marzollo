package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class RoundTrackTest {

    private RoundTrack track;

    @Before
    public void setUp(){
        Game game = new Game();
        track = new RoundTrack(10, game);
    }

    @Test
    public void testAddAllForRound() {
        ArrayList<Die> draft = new ArrayList<>();
        draft.add(new Die(3, new Random(), Colour.GREEN));
        draft.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft);
        assertEquals(track.getLeftovers().get(1), draft); //verifies that draft has been
        //effectively added to the RoundTrack at the specified position
    }

    @Test
    public void testGetSum() {
        ArrayList<Die> draft1 = new ArrayList<>();
        draft1.add(new Die(3, new Random(), Colour.GREEN));
        draft1.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft1);
        track.addAllForRound(9, draft1);
        assertEquals(18, track.getSum()); //verifies that the sum is computed correctly
    }

    @Test
    public void testGetSumWithNoDice() {
        assertEquals(0, track.getSum()); //verifies that the sum is computed correctly
    }


    @Test
    public void testValidSwap() {
        ArrayList<Die> draft1 = new ArrayList<>();
        draft1.add(new Die(3, new Random(), Colour.GREEN));
        draft1.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft1);
        Die substitute = new Die(3, new Random(), Colour.PURPLE);
        Die old = track.getLeftovers().get(1).get(1);
        int oldSize = track.getLeftovers().size();
        assertEquals(old, track.swap(new Coordinates(1,1), substitute)); //verifies that the swap method
        //returns the correct value
        assertTrue(track.getLeftovers().get(1).contains(substitute)); //verifies that the state of RoundTrack has changed in
        //the right way
        int newSize = track.getLeftovers().size();
        assertEquals(oldSize, newSize); //verifies that the state of RoundTrack has changed in
        //the right way
    }

    @Test
    public void testInvalidSwap() {
        ArrayList<Die> draft1 = new ArrayList<>();
        draft1.add(new Die(3, new Random(), Colour.GREEN));
        draft1.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft1);
        Die substitute = new Die(3, new Random(), Colour.PURPLE);
        assertNull(track.swap(new Coordinates(2, 2), substitute));
        assertEquals(draft1, track.getLeftovers().get(1));
    }

}