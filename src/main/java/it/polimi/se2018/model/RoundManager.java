package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * RoundManager is the component of Game that is responsible for
 * the correct flow of the rounds in the game. It keeps track of the
 * player who can make a move and handles the correct rotation of
 * turns after each round.
 * <p>The class relies on {@link ForthAndBackIterator} to select
 * the players in the correct order during the rounds.</p>
 */
public class RoundManager {
    private static final int ROUNDS = 10;

    /**
     * The counter for the round number.
     */
    private int round;
    /**
     * The iterator used for the current round.
     */
    private ForthAndBackIterator<Player> playerIterator;
    /**
     * The list of all the players in the game.
     */
    private List<Player> players;
    /**
     * The player who currently can make a move.
     */
    private Player currentPlayer;
    /**
     * The list of player who will skip their second turn for
     * the current round.
     */
    private ArrayList<Player> playersToSkip;


    /**
     * Instantiates a new RoundManager for the given list of players.
     * <p><strong>note:</strong> the list passed as argument will be
     * modified, so using a copy instead of the original is to be
     * considered.</p>
     * @param players The list containing the players who partake of
     *                the game; can be modified by some methods.
     */
    public RoundManager(List<Player> players) {
        this.players = players;
        playerIterator = new ForthAndBackIterator<>(players);
        playersToSkip = new ArrayList<>();
        round = 1;
    }

    /**
     * Getter for the player who currently can make a move.
     * @return A reference to the player who currently can
     *         make a move.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Tells if the game has finished. It is equivalent to check if
     * the current round is terminated and it is the last one.
     * @return <code>true</code> if the game is over; <code>false</code>
     *         otherwise.
     */
    public boolean gameFinished() {
        return getRound() == ROUNDS && isLastTurn();
    }

    /**
     * Tells if the current round has reached the last turn.
     * @return <code>true</code> if the current player is the
     *         last one in the current round;
     *         <code>false</code> otherwise.
     */
    public boolean isLastTurn(){
        return !playerIterator.hasNext();
    }

    /**
     * Getter for the current round number.
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
    private void setupNewRound(){
        ++round;
        playersToSkip.clear();
        Collections.rotate(players, 1);
        playerIterator = new ForthAndBackIterator<>(players);
    }

    /**
     * This method makes the game progress by selecting the next player
     * who can make a move.
     * <p>Whenever a round terminates, it automatically sets up a new one,
     * if it is the case (i.e. the last round has finished)</p>
     * <p>All the players who are contained in the skip list will
     * be ignored, effectively losing their turn this round.</p>
     * @throws GameAlreadyFinishedException if the game is already over.
     */
    public void moveGameForward() throws GameAlreadyFinishedException {
        if (gameFinished())
            throw new GameAlreadyFinishedException();

        if (isLastTurn())
            setupNewRound();

        currentPlayer = playerIterator.next();
        while(playersToSkip.contains(currentPlayer) && playerIterator.hasNext())
            currentPlayer = playerIterator.next();

        //no players have a second turn this, round, so move the game forward.
        if(playersToSkip.contains(currentPlayer))
            moveGameForward();

    }

    /**
     * Tells whether the current player has a second turn this round.
     * @return <code>true</code> if the current player hasn't used his
     *         second turn this round; <code>false</code> otherwise;.
     */
    public boolean isSecondTurnAvailable(){
        return !playerIterator.hasTraversedAllOnce();
    }

    /**
     * Prevents a player from being able to use his second turn in a
     * round. If this is not possible, a SecondTurnUnavailableException
     * is thrown.
     * @param player The player who has to skip his second turn this round.
     * @throws SecondTurnUnavailableException if the player has already
     *         used his second turn this round.
     */
    public void consumeSecondTurn(Player player) throws SecondTurnUnavailableException {
        if(!isSecondTurnAvailable())
            throw new SecondTurnUnavailableException();
        playersToSkip.add(player);
    }

    private class GameAlreadyFinishedException extends Exception {
    }

    private class SecondTurnUnavailableException extends Exception {
    }
}
