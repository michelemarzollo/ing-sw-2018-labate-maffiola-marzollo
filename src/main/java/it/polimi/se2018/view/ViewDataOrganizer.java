package it.polimi.se2018.view;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.events.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * The last {@link ModelUpdate} received.
     */
    private ModelUpdate lastUpdate;
    /**
     * The name of the local player.
     */
    private String localPlayer;

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
     * @return The list of all player connection status.
     */
    public List<PlayerConnectionStatus> getAllConnectionStatus() {
        return connectionStatusList;
    }

    /**
     * Returns the latest NextTurn message.
     * @return The latest NextTurn message.
     */
    public NextTurn getNextTurn() {
        return nextTurn;
    }

    /**
     * Returns the draft pool.
     * @return The draft pool or {@code null} if there isn't any.
     */
    public List<Die> getDraftPool() {
        if (draftPool != null)
            return draftPool.getDice();
        return null;
    }

    /**
     * Returns the round track.
     * @return The round track or {@code null} if there isn't any.
     */
    public List<List<Die>> getRoundTrack() {
        if (roundTrack != null)
            return roundTrack.getRoundTrack();
        return null;
    }

    /**
     * Returns the latest pushed ModelUpdate message.
     * @return The latest pushed ModelUpdate message.
     */
    public ModelUpdate getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Pushes a GameSetup message.
     * @param gameSetup The message to push.
     */
    public void push(GameSetup gameSetup) {
        this.gameSetup = gameSetup;
    }

    /**
     * Pushes a GameEnd message.
     * @param gameEnd The message to push.
     */
    public void push(GameEnd gameEnd) {
        this.gameEnd = gameEnd;
    }

    /**
     * Pushes a PlayerStatus message.
     * @param playerStatus The message to push.
     */
    public void push(PlayerStatus playerStatus) {

        this.playerStatusList
                .removeIf(p -> p.getPlayerName().equals(playerStatus.getPlayerName()));
        this.playerStatusList.add(playerStatus);
    }

    /**
     * Pushes a PlayerConnectionStatus message.
     * @param connectionStatus The message to push.
     */
    public void push(PlayerConnectionStatus connectionStatus) {
        this.playerStatusList
                .removeIf(p -> p.getPlayerName().equals(connectionStatus.getPlayerName()));
        this.connectionStatusList.add(connectionStatus);
    }

    /**
     * Pushes a NextTurn message.
     * @param nextTurn The message to push.
     */
    public void push(NextTurn nextTurn) {
        this.nextTurn = nextTurn;
    }

    /**
     * Pushes a RoundTrackUpdate message.
     * @param roundTrack The message to push.
     */
    public void push(RoundTrackUpdate roundTrack) {
        this.roundTrack = roundTrack;
    }

    /**
     * Pushes a DraftPoolUpdate message.
     * @param draftPool The message to push.
     */
    public void push(DraftPoolUpdate draftPool) {
        this.draftPool = draftPool;
    }

    /**
     * Pushes a generic ModelUpdate message.
     * <p>The push is achieved through a visitor pattern.</p>
     * @param update the message to push.
     */
    public void push(ModelUpdate update) {
        update.pushInto(this);
        this.lastUpdate = update;
    }

    /**
     * Setter for the local player name.
     * @param playerName The name of the local player.
     */
    public void setLocalPlayer(String playerName) {
        this.localPlayer = playerName;
    }

    /**
     * Getter for the local player name.
     * @return The local player name.
     */
    public String getLocalPlayer() {
        return localPlayer;
    }
}
