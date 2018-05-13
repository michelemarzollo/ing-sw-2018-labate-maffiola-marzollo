package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class RoundTrackTest {

    @Test
    public void testAddAllForRound() {
        RoundTrack track = new RoundTrack(10);
        ArrayList<Die> draft = new ArrayList<>();
        draft.add(new Die(3, new Random(), Colour.GREEN));
        draft.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft);
        assertTrue(track.getLeftovers().get(2).equals(draft)); //verifies that draft has been
        //effectively added to the RoundTrack at the specified position
    }

    @Test
    public void testGetSum() {
        RoundTrack track = new RoundTrack(10);
        ArrayList<Die> draft1 = new ArrayList<>();
        draft1.add(new Die(3, new Random(), Colour.GREEN));
        draft1.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft1);
        track.addAllForRound(9, draft1);
        assertEquals(18, track.getSum()); //verifies that the sum is computed correctly
    }

    @Test
    public void testGetSumWithNoDice() {
        RoundTrack track = new RoundTrack(10);
        assertEquals(0, track.getSum()); //verifies that the sum is computed correctly
    }


    @Test
    public void testValidSwap() {
        RoundTrack track = new RoundTrack(10);
        ArrayList<Die> draft1 = new ArrayList<>();
        draft1.add(new Die(3, new Random(), Colour.GREEN));
        draft1.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft1);
        Die substitute = new Die(3, new Random(), Colour.PURPLE);
        Die old = track.getLeftovers().get(2).get(1);
        int oldSize = track.getLeftovers().size();
        assertEquals(old, track.swap(new Coordinates(2,1), substitute)); //verifies that the swap method
        //returns the correct value
        assertTrue(track.getLeftovers().get(2).contains(substitute)); //verifies that the state of RoundTrack has changed in
        //the right way
        int newSize = track.getLeftovers().size();
        assertTrue(oldSize == newSize); //verifies that the state of RoundTrack has changed in
        //the right way
    }

    @Test
    public void testInvalidSwap() {
        RoundTrack track = new RoundTrack(10);
        ArrayList<Die> draft1 = new ArrayList<>();
        draft1.add(new Die(3, new Random(), Colour.GREEN));
        draft1.add(new Die(6, new Random(), Colour.GREEN));
        track.addAllForRound(2, draft1);
        Die substitute = new Die(3, new Random(), Colour.PURPLE);
        assertEquals(null, track.swap(new Coordinates(2,2), substitute));
        assertEquals(draft1, track.getLeftovers().get(2));
    }

}