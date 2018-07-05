package it.polimi.se2018.model;

import it.polimi.se2018.model.events.GameEnd;
import it.polimi.se2018.model.events.GameSetup;
import it.polimi.se2018.model.events.ModelUpdate;
import it.polimi.se2018.utils.Observable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        player.setGame(this);
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
        for(ToolCard toolCard : toolCards)
            toolCard.setGame(this);
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
        Map<String, Integer> scoreMap = new LinkedHashMap<>();
        scoreBoard.forEach(p -> scoreMap.put(p.getName(), p.getScore()));
        GameEnd message = new GameEnd(scoreMap);
        notifyObservers(message);
        this.scoreBoard = scoreBoard;
    }

    /**
     * Updates the game status so it's ready to start.
     */
    public void terminateSetup() {
        GameSetup message = new GameSetup(this);
        notifyObservers(message);
        setupComplete = true;
    }

    /**
     * Starts the game.
     * <p>All the components the game requires during its execution
     * are instantiated and game status updated to started.</p>
     * After {@code start} execution is no one's turn yet:
     * {@link it.polimi.se2018.controller.Controller} will set it.
     */
    public void start() {
        if (setupComplete && !started) {

            draftPool = new DraftPool(this);
            diceBag = new DiceBag();
            roundTrack = new RoundTrack(TurnManager.ROUNDS, this);
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
