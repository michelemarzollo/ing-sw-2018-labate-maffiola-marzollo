package it.polimi.se2018.model;

import java.util.*;


/**
 * TurnManager is the component of Game that is responsible for
 * the correct flow of the rounds in the game. It keeps track of the
 * player who can make a move and handles the correct rotation of
 * turns after each round.
 * <p>The class relies on the inner class {@link TurnIterator} to select
 * the players in the correct order during the rounds.</p>
 */
public class TurnManager {
    private static final int ROUNDS = 10;

    /**
     * TurnIterator allows to go through a list of players
     * twice, the second time being in reverse order.
     * <p>This implementation doesn't offer the possibility to remove
     * elements nor the method forEachRemaining, it only grants
     * sequential access to the underlying list of players in the order
     * specified above.</p>
     */
    private class TurnIterator implements Iterator<Player> {

        private int nextIndex = 0;


        /**
         * Tells whether there are still objects to go through or not.
         *
         * @return {@code true} if there is at least one more element
         * to traverse; {@code false} otherwise.
         */
        @Override
        public boolean hasNext() {
            return !(getNextIndex() < 0 || getNextIndex() >= players.size());
        }

        /**
         * Tells if the iterator is going backwards.
         *
         * @return {@code true} if the iterator is going in reverse
         * order; {@code false} otherwise.
         */
        private boolean reverse() {
            return nextIndex >= players.size();
        }

        /**
         * Helper function to retrieve the correct index of the next item
         * to be traversed.
         *
         * @return The index of the next item to be visited.
         */
        private int getNextIndex() {
            if (reverse())
                return 2 * players.size() - 1 - nextIndex;

            return nextIndex;
        }

        /**
         * Progresses the iteration, if possible, returning the next value
         * according to the traversing order.
         * <p>This method is irreversible, so once invoked, it isn't possible
         * to put back the value or any kind of rewinding.</p>
         *
         * @return The next element to be visited according to the order
         * @throws NoSuchElementException if there isn't any element left
         *                                to be visited.
         */
        @Override
        public Player next() {
            if (!hasNext())
                throw new NoSuchElementException();

            Player element = players.get(getNextIndex());
            ++nextIndex;

            return element;
        }

        /**
         * Tells if the iterator has gone through at least once every
         * element in the list.
         * <p>When the return value is <code>true</code> it also means
         * that the iterator has started traversing backwards the objects.
         * </p>
         *
         * @return {@code true} if the list has been traversed in
         * whole at least once; {@code false} otherwise.
         */
        public boolean hasTraversedAllOnce() {
            return reverse();
        }
    }


    /**
     * The counter for the round number.
     */
    private int round;
    /**
     * The iterator used for the current round.
     */
    private TurnIterator turnIterator;
    /**
     * The list of all the players in the game.
     */
    private List<Player> players;
    /**
     * The player who currently can make a move.
     */
    private Turn currentTurn;
    /**
     * The list of player who will skip their second turn for
     * the current round.
     */
    private ArrayList<Player> playersToSkip;


    /**
     * Instantiates a new TurnManager for the given list of players.
     * <p><strong>note:</strong> the list passed as argument will be
     * modified, so using a copy instead of the original is to be
     * considered.</p>
     *
     * @param players The list containing the players who partake of
     *                the game; can be modified by some methods.
     * @throws NullPointerException if the list of players is null.
     */
    public TurnManager(List<Player> players) {
        if(players == null)
            throw new NullPointerException();
        this.players = players;
        turnIterator = new TurnIterator();
        playersToSkip = new ArrayList<>();
        round = 1;
    }

    /**
     * Getter for the current turn.
     * @return An instance of Turn containing all information
     *         about the current turn.
     */
    public Turn getCurrentTurn(){
        return currentTurn;
    }

    /**
     * Tells if the game has finished. It is equivalent to check if
     * the current round is terminated and it is the last one.
     *
     * @return {@code true} if the game is over; {@code false} otherwise.
     */
    public boolean isGameFinished() {
        return getRound() == ROUNDS && isLastTurn();
    }

    /**
     * Tells if the current round has reached the last turn.
     *
     * @return {@code true} if the current player is the
     * last one in the current round; {@code false} otherwise.
     */
    public boolean isLastTurn() {
        return !turnIterator.hasNext();
    }

    /**
     * Getter for the current round number.
     *
     * @return The current round number.
     */
    public int getRound() {
        return round;
    }

    /**
     * Sets up a new round. It ensures to increment the round counter,
     * to clear any leftover information from the previous one and
     * to correctly rotate the player order.
     */
    private void setupNewRound() {
        ++round;
        playersToSkip.clear();
        Collections.rotate(players, 1);
        turnIterator = new TurnIterator();
    }

    /**
     * This method makes the game progress by selecting the next player
     * who can make a move.
     * <p>Whenever a round terminates, it automatically sets up a new one,
     * if it is the case (i.e. the last round has finished)</p>
     * <p>All the players who are contained in the skip list will
     * be ignored, effectively losing their turn this round.</p>
     *
     * @throws GameFinishedException if the game is already over.
     */
    public void updateTurn() throws GameFinishedException {
        if (isGameFinished())
            throw new GameFinishedException();

        if (!turnIterator.hasNext())
            setupNewRound();

        Player nextPlayer = turnIterator.next();
        //skip all player whose second turn has been consumed
        while (playersToSkip.contains(nextPlayer) && turnIterator.hasNext())
            nextPlayer = turnIterator.next();

        if (playersToSkip.contains(nextPlayer))
            //no players have a second turn this round, so move the game forward.
            updateTurn();
        else
            //this player can move, set new turn
            currentTurn = new Turn(nextPlayer, isSecondTurnAvailable());

    }

    /**
     * Tells whether the current player has a second turn this round.
     *
     * @return {@code true} if the current player hasn't used his
     * second turn this round; {@code false} otherwise;.
     */
    public boolean isSecondTurnAvailable() {
        return !turnIterator.hasTraversedAllOnce();
    }

    /**
     * Prevents a player from being able to use his second turn in a
     * round. If this is not possible, a SecondTurnUnavailableException
     * is thrown.
     *
     * @param player The player who has to skip his second turn this round.
     * @throws SecondTurnUnavailableException if the player has already
     *                                        used his second turn this round.
     */
    public void consumeSecondTurn(Player player) throws SecondTurnUnavailableException {
        if (!isSecondTurnAvailable())
            throw new SecondTurnUnavailableException();
        playersToSkip.add(player);
    }

    private class GameFinishedException extends Exception {
    }

    private class SecondTurnUnavailableException extends Exception {
    }
}
