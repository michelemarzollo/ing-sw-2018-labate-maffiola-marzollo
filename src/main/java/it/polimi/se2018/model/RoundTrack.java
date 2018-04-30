package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The class represents the round track in the game.
 * <p>Its main role is to keep track of all the dice that
 * have been discarded during the rounds, since they can
 * still be used in some cases (most notably, state if player
 * has won in single player mode).</p>
 */
public class RoundTrack {

    /**
     * Collection of all the discarded dice, ordered by
     * the round number.
     */
    private final ArrayList<List<Die>> leftovers;

    /**
     * Constructs a RoundTrack object able to correctly manage the
     * leftover dice for the given number of rounds.
     * @param rounds The number of rounds in the game, must be non-negative.
     */
    public RoundTrack(int rounds) {
        leftovers = new ArrayList<>(rounds);
        for (int i = 0; i < rounds; ++i)
            leftovers.add(new ArrayList<>());
    }

    /**
     * Inserts a collection of dice in the leftovers for
     * a given round.
     * @param round The round number to which {@code dice} belongs.
     * @param dice The collection of dice to be added.
     */
    public void addAllForRound(int round, Collection<? extends Die> dice) {
        leftovers.get(round).addAll(dice);
    }

    /**
     * Computes the sum of all the values of the dice in the RoundTrack
     * @return The sum of all the values of the dice.
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
     * Helper method to find the number of round in which a die
     * is located. If the die isn't in the leftovers, it returns -1
     * to indicate failure.
     * @param die The die to be searched.
     * @return The number of round where the die is located.
     */
    private int getRoundOf(Die die){
        for(int i = 0; i < leftovers.size(); ++i){
            if(leftovers.get(i).contains(die))
                return i;
        }
        return -1;
    }

    /**
     * This method swaps two dice, one of which has to be among
     * the leftovers and the other mustn't. If this isn't the case,
     * the method returns false to indicate that the operation
     * is not permitted.
     * @param leftover The leftover die, which has to be in RoundTrack.
     * @param die A non-leftover die to be inserted in the leftovers.
     * @return {@code true} if the swap is successful, {@code false}
     *         otherwise.
     */
    public boolean swap(Die leftover, Die die) {
        //Can't swap if leftover is not in RoundTrack or die is.
        int round = getRoundOf(leftover);

        if(round < 0 || getRoundOf(die) > 0)
            return false;

        boolean removed = leftovers.get(round).remove(leftover);
        if(removed)
            leftovers.get(round).add(die);

        return removed;
    }
}
