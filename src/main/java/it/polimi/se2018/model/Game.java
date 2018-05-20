package it.polimi.se2018.model;

import java.util.*;

import it.polimi.se2018.model.events.*;
import it.polimi.se2018.utils.Observable;

/**
 * The class that represents the status of the game
 * <strong>Must be adjusted after implementing the Controller</strong>
 *
 * @author michelemarzollo
 */
public class Game extends Observable<ModelUpdate> {

    /**
     * The DraftPool, which contains the dice that can be drafted by players
     */
    private DraftPool draftPool;

    /**
     * Flag to indicate if the game has started.
     */
    private boolean started = false;

    /**
     * Flag to indicate if the game is ready to start.
     */
    private boolean setupComplete = false;

    /**
     * The list of players
     */
    private List<Player> players = new ArrayList<>();

    /**
     * The container of the dice of the game that weren't already drafted
     */
    private DiceBag diceBag;

    /**
     * The object that handles the rounds
     */
    private RoundTrack roundTrack;

    /**
     * The object that handles the turn sequence
     */
    private TurnManager turnManager;

    /**
     * The array of the PublicObjectiveCards of the game
     */
    private PublicObjectiveCard[] publicObjectiveCards;

    /**
     * The array of the ToolCards of the game
     */
    private ToolCard[] toolCards;

    /**
     * The array containing the players in the order of the final ranking
     */
    private List<Player> scoreBoard;


    /**
     * The getter of the DraftPool
     *
     * @return The DraftPool
     */
    public DraftPool getDraftPool() {
        return draftPool;
    }

    /**
     * The getter for the list of players
     *
     * @return The list
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Adds the specified player to the game.
     *
     * @param player The new player.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Removes the specified player from the game.
     *
     * @param player The player to remove.
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * The getter for the DiceBag
     *
     * @return The DiceBag
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * The getter for the RoundTrack
     *
     * @return The RoundTrack
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * The getter for the TurnManager
     *
     * @return The TurnManager
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }

    /**
     * The getter for the array of PublicObjectiveCards
     *
     * @return The array
     */
    public PublicObjectiveCard[] getPublicObjectiveCards() {
        return publicObjectiveCards;
    }

    /**
     * The setter for the array of PublicObjectiveCards
     *
     * @param publicObjectiveCards The array to be set
     */
    public void setPublicObjectiveCards(PublicObjectiveCard[] publicObjectiveCards) {
        this.publicObjectiveCards = publicObjectiveCards;
    }

    /**
     * The getter for the array of ToolCards
     *
     * @return The array
     */
    public ToolCard[] getToolCards() {
        return toolCards;
    }

    /**
     * The setter for the array of ToolCards
     *
     * @param toolCards The array to be set
     */
    public void setToolCards(ToolCard[] toolCards) {
        this.toolCards = toolCards;
    }

    /**
     * The getter for {@code scoreBoard}
     *
     * @return The final ranking as an array of players
     */
    public List<Player> getScoreBoard() {
        return scoreBoard;
    }

    /**
     * Setter for the score board.
     *
     * @param scoreBoard The final ranking as an ordered array of players.
     */
    public void setScoreBoard(List<Player> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    /**
     * Updates the game status so it's ready to start.
     */
    public void terminateSetup() {
        setupComplete = true;
    }

    /**
     * Starts the game.
     * <p>All the components the game requires during its execution
     * are instantiated and game status updated to started.</p>
     */
    public void start() {
        if (setupComplete && !started) {

            draftPool = new DraftPool();
            diceBag = new DiceBag();
            roundTrack = new RoundTrack(TurnManager.ROUNDS);
            turnManager = new TurnManager(players);

            started = true;
        }
    }

    /**
     * Tells if the game is correctly set up and ready to start.
     *
     * @return {@code true} if the game is ready to start;
     * {@code false} otherwise.
     */
    public boolean isSetupComplete() {
        return setupComplete;
    }

    /**
     * Tells if the game has been started.
     *
     * @return {@code true} if the game has started; {@code false} otherwise.
     */
    public boolean isStarted() {
        return started;
    }


}
