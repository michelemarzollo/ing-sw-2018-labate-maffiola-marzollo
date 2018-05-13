package it.polimi.se2018.model;

import it.polimi.se2018.utils.Coordinates;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the round track in the game.
 * <p>Its role is to keep track of all the dice that
 * are left unused after each round, since they still may
 * be used in some cases</p>
 * <p>The class is immutable, but is not yet thread-safe.</p>
 * @author dvdmff
 */
public class RoundTrack {

    /**
     * List of all the discarded dice, ordered by the round number.
     */
    private final List<List<Die>> leftovers;

    /**
     * Creates a RoundTrack object able to manage the specified
     * number of rounds.
     * @param rounds The number of rounds in the game.
     */
    public RoundTrack(int rounds) {
        leftovers = new ArrayList<>(rounds);
        for (int i = 0; i < rounds; ++i)
            leftovers.add(new ArrayList<>());
    }

    /**
     * Inserts a list of dice in the leftovers for
     * the specified round.
     * @param round The round number to which {@code dice} belongs.
     * @param dice The collection of dice to be added.
     */
    public void addAllForRound(int round, List<? extends Die> dice) {
        leftovers.get(round).addAll(dice);
    }

    /**
     * Calculates the sum of the values of all the dice in the RoundTrack.
     * @return The sum of the values of the dice.
     */
    public int getSum() {
        return leftovers.stream()
                .map(a -> a.stream()
                        .map(Die::getValue)
                        .reduce(0, (s, v) -> s + v))
                .reduce(0, (s, v) -> s + v);
    }

    /**
     * Returns a deep copy of the leftover dice.
     * @return A copy of the List of dice in the RoundTrack.
     */
    public List<List<Die>> getLeftovers() {
        List<List<Die>> copy = new ArrayList<>(leftovers.size());
        for(List<Die> dice : leftovers)
            //Die is immutable, so it's not necessary to deep copy it.
            copy.add(new ArrayList<>(dice));

        return copy;
    }

    /**
     * Exchanges the die at the specified coordinates with the given one.
     *
     * This method swaps two dice, one of which has to be among
     * the leftovers and the other mustn't. If this isn't the case,
     * the method returns false to indicate that the operation
     * is not permitted.
     * @param coordinates The location of the die to swap in the round track.
     * @param die The die to be inserted in the leftovers.
     * @return A reference to the die that has been removed from the
     * round track. A value of {@code null} indicates that the swap
     * was unsuccessful.
     */
    public Die swap(Coordinates coordinates, Die die) {
        //Can't swap if no die is found at coordinates.
        Die toSwap;
        try {
            toSwap = leftovers.get(coordinates.getRow())
                    .get(coordinates.getCol());
        }
        catch(IndexOutOfBoundsException ex){
            return null;
        }
        leftovers.get(coordinates.getRow())
                .remove(toSwap);
        leftovers.get(coordinates.getRow())
                .add(die);
        return toSwap;
    }
}
