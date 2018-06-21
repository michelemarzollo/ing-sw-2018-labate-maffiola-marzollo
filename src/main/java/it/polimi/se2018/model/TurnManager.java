package it.polimi.se2018.model;


import java.util.*;


/**
 * This class is responsible for the correct flow of turns in a game.
 * <p>A game lasts exactly 10 rounds and during each of them every
 * player has two turns, the second of which can be skipped in some
 * cases if signaled.</p>
 * <p>During a round the turns are handled according to a forth-and-back
 * model. For instance, if the set of players is {1, 2, 3} and 1 has the
 * first turn this round, then the correct distribution of turns will be
 * {1, 2, 3, 3, 2, 1}.</p>
 * <p>The class is mutable.</p>
 * No thread safety mechanism has yet been introduced.
 * @author dvdmff
 */
public class TurnManager {
    /**
     * Constant indicating the total number of rounds.
     */
    public static final int ROUNDS = 10;

    /**
     * This class allows to go through the list of players contained
     * in the outer class twice, the second time being in reverse order.
     * <p>This iterator doesn't offer the possibility to remove
     * elements nor the method forEachRemaining.</p>
     * <p>The visiting is done in a forth-and-back manner, meaning that
     * the list is traversed from the first element to the last one first,
     * then from the last element to the first one.</p>
     * @author dvdmff
     */
    private class TurnIterator implements Iterator<Player> {

        private int nextIndex = 0;


        /**
         * Tells whether there are still elements to go through or not.
         *
         * @return {@code true} if there is at least one more element
         *         to traverse; {@code false} otherwise.
         */
        @Override
        public boolean hasNext() {
            return !(getNextIndex() < 0 || getNextIndex() >= players.size());
        }

        /**
         * Tells if the iterator is going backwards.
         *
         * @return {@code true} if the iterator is going in reverse
         *         order; {@code false} otherwise.
         */
        private boolean reverse() {
            return nextIndex >= players.size();
        }

        /**
         * Helper function to retrieve the correct index of the next item
         * to be visited.
         *
         * @return The index of the next item to be visited.
         */
        private int getNextIndex() {
            if (reverse())
                return 2 * players.size() - 1 - nextIndex;

            return nextIndex;
        }

        /**
         * Progresses the iteration, if possible.
         * <p>This method is irreversible, so once invoked, it isn't possible
         * to put back the element or any other kind of rewinding.</p>
         *
         * @return The next element to be visited according to the order.
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
         * Tells if the iterator has visited at least once every
         * element in the list.
         * <p>When the return value is <code>true</code> it also means
         * that the iterator has started traversing backwards the objects.
         * </p>
         *
         * @return {@code true} if the list has been traversed in
         * whole at least once; {@code false} otherwise.
         */
        public boolean hasTraversedAllOnce() {
            return nextIndex > players.size();
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
     * Creates a TurnManager for the specified list of players.
     * <p>The list passed as argument can be modified, so to use
     * a copy instead of the original one is recommended if the
     * order of the elements matters.</p>
     *
     * @param players The list containing the players who partake of
     *                the game; can be modified by some methods.
     * @throws NullPointerException if the list of players is null.
     */
    public TurnManager(List<Player> players) {
        if(players == null )
            throw new NullPointerException();
        if(players.isEmpty())
            throw new IllegalArgumentException();
        this.players = players;
        turnIterator = new TurnIterator();
        playersToSkip = new ArrayList<>();
        round = 1;
    }

    /**
     * Getter for the current turn.
     *
     * @return An instance of Turn containing all information
     *         about the current turn.
     */
    public Turn getCurrentTurn(){
        return currentTurn;
    }

    /**
     * Getter for the players that have to  be skipped
     *
     * @return a copy of the ArrayList of players that
     * have to be skipped
     */
    public List<Player> getPlayersToSkip() {
        return new ArrayList<>(playersToSkip);
    }

    /**
     * Tells if the game has finished.
     *
     * @return {@code true} if the game is over; {@code false} otherwise.
     */
    public boolean isLastTurnOfGame() {
        return getRound() == ROUNDS && !turnIterator.hasNext();
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
     * Sets up a new round.
     * <p>The call to this method results in the increment of the
     * round counter, the cleaning of any leftover information from
     * the previous one and the correct rotation of the first player
     * to have the turn.</p>
     *
     * @throws GameFinishedException if it's the last turn of the game
     * and it is asked to setup a new round: the game is finished.
     */
    public void setupNewRound() throws GameFinishedException {
        if (isLastTurnOfGame()) throw new GameFinishedException();
        ++round;
        playersToSkip.clear();
        Collections.rotate(players, 1);
        turnIterator = new TurnIterator();
        //return value is ignored since there must exist at least one
        //valid turn
        //updateTurn(); da levare probabilmente
    }

    /**
     * Updates the current turn, if possible.
     * <p>The method forces the turns to follow a forth-and-back order,
     * but will skip all those players whose second turn has already
     * been consumed.</p>
     * <p>If no players can move this round the method does nothing but signal
     * the caller the failure with its return value.</p>
     *
     * @return {@code true} if the turn has been successfully updated;
     *         {@code false} otherwise.
     */
    public boolean updateTurn() {
        if (!turnIterator.hasNext())
            return false;

        Player nextPlayer = turnIterator.next();
        //skip all player whose second turn has been consumed
        while (skipPlayer(nextPlayer) && turnIterator.hasNext())
            nextPlayer = turnIterator.next();

        if (skipPlayer(nextPlayer))
            //no players have a second turn this round, so move the game forward.
            return false;

        //this player can move, set new turn
        currentTurn = new Turn(nextPlayer, isSecondTurnAvailable());

        return true;

    }

    /**
     * Tells if a player has to be skipped.
     * @param player The player to check.
     * @return {@code true} if the player has to be skipped; {@code false} otherwise.
     */
    private boolean skipPlayer(Player player){
        return playersToSkip.contains(player) || !player.isConnected();
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
     * Prevents a player from being able to use his second turn in the
     * same round.
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

    public class GameFinishedException extends Exception {
    }

    public class SecondTurnUnavailableException extends Exception {
    }
}
