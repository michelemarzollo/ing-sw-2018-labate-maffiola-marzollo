package it.polimi.se2018.model;

import java.util.List;

import it.polimi.se2018.utils.Observable;

/**
 * The class that represents the status of the game
 * <strong>Must be adjusted after implementing the Controller</strong>
 *
 * @author michelemarzollo
 */
public class Game extends Observable {

    //ATTRIBUTES

    /**
     * The DraftPool, which contains the dice that can be drafted by players
     */
    private DraftPool draftPool;

    /**
     * The list of players
     */
    private List<Player> players;

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
    private Player[] scoreBoard;


    //GETTERS AND SETTERS

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
     * The setter for the list of players
     *
     * @param players The list
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
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
    public Player[] getScoreBoard() {
        return scoreBoard;
    }

    /**
     * The setter for {@code scoreBoard}
     *
     * @param scoreBoard The final ranking as an array of players
     */
    public void setScoreBoard(Player[] scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    //OTHER METHODS

    public void start() {

        draftPool = new DraftPool();
        diceBag = new DiceBag();
        roundTrack = new RoundTrack(10);    //use constant instead of number after davide's commit
        turnManager = new TurnManager(players);

    }

}
