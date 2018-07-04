package it.polimi.se2018.model.viewmodel;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.ToolCard;
import it.polimi.se2018.model.events.*;

import java.util.*;

/**
 * This class is used to store all information sent by the server to the view.
 */
public class ViewDataOrganizer {

    /**
     * GameSetup message.
     */
    private GameSetup gameSetup;

    /**
     * GameEnd message.
     */
    private GameEnd gameEnd;

    /**
     * List of latest PlayerStatus messages.
     */
    private List<PlayerStatus> playerStatusList = new ArrayList<>();

    /**
     * List of latest PlayerConnectionStatus messages.
     */
    private List<PlayerConnectionStatus> connectionStatusList = new ArrayList<>();

    /**
     * NextTurn message.
     */
    private NextTurn nextTurn;

    /**
     * Latest RoundTrackUpdate message.
     */
    private RoundTrackUpdate roundTrack;

    /**
     * Latest DraftPoolUpdate message.
     */
    private DraftPoolUpdate draftPool;

    /**
     * The name of the local player.
     */
    private String localPlayer;

    /**
     * Flag to flag to indicate whether the last update changed the turn.
     */
    private boolean turnChanged = false;

    /**
     * Index of the last altered player connection status.
     */
    private int changedConnectionIndex = -1;

    /**
     * The map that contains the names of the ToolCards of the game,
     * and a boolean value to sye if they were used.
     */
    private Map<String, Boolean> usedToolCards = new LinkedHashMap<>();

    /**
     * Returns the game setup.
     *
     * @return the game setup.
     */
    public GameSetup getGameSetup() {
        return gameSetup;
    }

    /**
     * Returns the score board.
     *
     * @return the score board.
     */
    public Map<String, Integer> getScoreBoard() {
        if (gameEnd != null)
            return gameEnd.getScoreBoard();
        return null;
    }

    /**
     * Return the player status relative to the specified player name, if it exists.
     *
     * @param name The name of the player.
     * @return the player status relative to the specified player name, or {@code null}
     * if it doesn't exist.
     */
    public PlayerStatus getPlayerStatus(String name) {
        Optional<PlayerStatus> playerStatus = findPlayerStatus(name);
        return playerStatus.orElse(null);
    }

    /**
     * Helper method to find the player status for a player.
     *
     * @param name The name of the player.
     * @return An optional player status.
     */
    private Optional<PlayerStatus> findPlayerStatus(String name) {
        return playerStatusList.stream()
                .filter(p -> p.getPlayerName().equals(name))
                .findFirst();

    }

    /**
     * Returns the list of player status.
     *
     * @return The list of player status.
     */
    public List<PlayerStatus> getAllPlayerStatus() {
        return playerStatusList;
    }

    /**
     * Return the player connection status relative to the specified player name, if it exists.
     *
     * @param name The name of the player.
     * @return the player connection status relative to the specified player name, or {@code null}
     * if it doesn't exist.
     */
    public PlayerConnectionStatus getConnectionStatus(String name) {
        Optional<PlayerConnectionStatus> connectionStatus = findConnectionStatus(name);
        return connectionStatus.orElse(null);
    }

    /**
     * Helper method to find the player connection status for a player.
     *
     * @param name The name of the player.
     * @return An optional player connection status.
     */
    private Optional<PlayerConnectionStatus> findConnectionStatus(String name) {
        return connectionStatusList.stream()
                .filter(p -> p.getPlayerName().equals(name))
                .findFirst();

    }

    /**
     * Returns the list of all player connection status.
     *
     * @return The list of all player connection status.
     */
    public List<PlayerConnectionStatus> getAllConnectionStatus() {
        return connectionStatusList;
    }

    /**
     * Returns the latest NextTurn message.
     *
     * @return The latest NextTurn message.
     */
    public NextTurn getNextTurn() {
        return nextTurn;
    }

    /**
     * Returns the draft pool.
     *
     * @return The draft pool or {@code null} if there isn't any.
     */
    public List<Die> getDraftPool() {
        if (draftPool != null)
            return draftPool.getDice();
        return new ArrayList<>();
    }

    /**
     * Returns the round track.
     *
     * @return The round track or {@code null} if there isn't any.
     */
    public List<List<Die>> getRoundTrack() {
        if (roundTrack != null)
            return roundTrack.getRoundTrack();
        return new ArrayList<>();
    }

    /**
     * Pushes a GameSetup message.
     * Also initializes the map {@code usedToolCards}, adding all keys (the name of
     * the ToolCards) and setting {@code false} for each key.
     *
     * @param gameSetup The message to push.
     */
    public void push(GameSetup gameSetup) {
        this.gameSetup = gameSetup;
        for (ToolCard toolCard : gameSetup.getToolCards()) {
            usedToolCards.put(toolCard.getName(), false);
        }
    }

    /**
     * Pushes a GameEnd message.
     *
     * @param gameEnd The message to push.
     */
    public void push(GameEnd gameEnd) {
        this.gameEnd = gameEnd;
    }

    /**
     * Pushes a PlayerStatus message.
     *
     * @param playerStatus The message to push.
     */
    public void push(PlayerStatus playerStatus) {

        this.playerStatusList
                .removeIf(p -> p.getPlayerName().equals(playerStatus.getPlayerName()));
        this.playerStatusList.add(playerStatus);
    }

    /**
     * Pushes a PlayerConnectionStatus message.
     *
     * @param connectionStatus The message to push.
     */
    public void push(PlayerConnectionStatus connectionStatus) {
        this.playerStatusList
                .removeIf(p -> p.getPlayerName().equals(connectionStatus.getPlayerName()));
        this.connectionStatusList.add(connectionStatus);
        changedConnectionIndex = connectionStatusList.indexOf(connectionStatus);
    }

    /**
     * Pushes a NextTurn message.
     *
     * @param nextTurn The message to push.
     */
    public void push(NextTurn nextTurn) {
        if (this.nextTurn != null)
            turnChanged = !this.nextTurn.equals(nextTurn);
        else
            turnChanged = true;
        this.nextTurn = nextTurn;
    }

    /**
     * Pushes a RoundTrackUpdate message.
     *
     * @param roundTrack The message to push.
     */
    public void push(RoundTrackUpdate roundTrack) {
        this.roundTrack = roundTrack;
    }

    /**
     * Pushes a DraftPoolUpdate message.
     *
     * @param draftPool The message to push.
     */
    public void push(DraftPoolUpdate draftPool) {
        this.draftPool = draftPool;
    }

    /**
     * Sets to {@code true} the value of the map corresponding to the
     * key given by the name of the ToolCard in the message.
     *
     * @param usedToolCard the message that contains the name of the ToolCard.
     */
    public void push(UseToolCard usedToolCard) {
        usedToolCards.replace(usedToolCard.getToolCardName(), true);
    }

    /**
     * Pushes a generic ModelUpdate message.
     * <p>The push is achieved through a visitor pattern.</p>
     *
     * @param update the message to push.
     */
    public void push(ModelUpdate update) {
        turnChanged = false;
        changedConnectionIndex = -1;
        update.pushInto(this);
    }

    /**
     * Setter for the local player name.
     *
     * @param playerName The name of the local player.
     */
    public void setLocalPlayer(String playerName) {
        this.localPlayer = playerName;
    }

    /**
     * Getter for the local player name.
     *
     * @return The local player name.
     */
    public String getLocalPlayer() {
        return localPlayer;
    }

    /**
     * Getter for the turn changed flag.
     *
     * @return {@code true} if the last update changed the turn status; {@code false} otherwise.
     */
    public boolean isTurnChanged() {
        return turnChanged;
    }

    /**
     * Getter for the index of the player connection status changed with last update.
     *
     * @return The index of the player connection status changed by the last update, or -1
     * if it isn't the case.
     */
    public int getChangedConnectionIndex() {
        return changedConnectionIndex;
    }

    /**
     * The getter of the map of used ToolCards.
     *
     * @return the map.
     */
    public Map<String, Boolean> getUsedToolCards() {
        return usedToolCards;
    }
}
