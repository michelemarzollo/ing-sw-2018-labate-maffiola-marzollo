package it.polimi.se2018.view;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.events.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ViewDataOrganizer {

    private GameSetup gameSetup;
    private GameEnd gameEnd;
    private List<PlayerStatus> playerStatusList;
    private List<PlayerConnectionStatus> connectionStatusList;
    private NextTurn nextTurn;
    private RoundTrackUpdate roundTrack;
    private DraftPoolUpdate draftPool;

    /**
     * The last {@link ModelUpdate} received.
     */
    private ModelUpdate lastUpdate;
    private String localPlayer;

    public GameSetup getGameSetup() {
        return gameSetup;
    }

    public Map<String, Integer> getScoreBoard() {
        return gameEnd.getScoreBoard();
    }

    public PlayerStatus getPlayerStatus(String name){
        Optional<PlayerStatus> playerStatus = findPlayerStatus(name);
        return playerStatus.orElse(null);
    }

    private Optional<PlayerStatus> findPlayerStatus(String name) {
        return playerStatusList.stream()
                .filter(p -> p.getPlayerName().equals(name))
                .findFirst();

    }

    public List<PlayerStatus> getAllPlayerStatus() {
        return playerStatusList;
    }

    public PlayerConnectionStatus getConnectionStatus(String name){
        Optional <PlayerConnectionStatus> connectionStatus = findConnectionStatus(name);
        return connectionStatus.orElse(null);
    }


    private Optional<PlayerConnectionStatus> findConnectionStatus(String name) {
        return connectionStatusList.stream()
                .filter(p -> p.getPlayerName().equals(name))
                .findFirst();

    }
    public List<PlayerConnectionStatus> getAllConnectionStatus() {
        return connectionStatusList;
    }

    public NextTurn getNextTurn() {
        return nextTurn;
    }

    public List<Die> getDraftPool() {
        return draftPool.getDice();
    }

    public List<List<Die>> getRoundTrack() {
        return roundTrack.getRoundTrack();
    }

    public ModelUpdate getLastUpdate() {
        return lastUpdate;
    }

    public void push(GameSetup gameSetup){
        this.gameSetup = gameSetup;
    }

    public void push(GameEnd gameEnd) {
        this.gameEnd = gameEnd;
    }

    public void push(PlayerStatus playerStatus) {

        this.playerStatusList
                .removeIf(p -> p.getPlayerName().equals(playerStatus.getPlayerName()));
        this.playerStatusList.add(playerStatus);
    }

    public void push(PlayerConnectionStatus connectionStatus) {
        this.playerStatusList
                .removeIf(p -> p.getPlayerName().equals(connectionStatus.getPlayerName()));
        this.connectionStatusList.add(connectionStatus);
    }

    public void push(NextTurn nextTurn) {
        this.nextTurn = nextTurn;
    }

    public void push(RoundTrackUpdate roundTrack) {
        this.roundTrack = roundTrack;
    }

    public void push(DraftPoolUpdate draftPool) {
        this.draftPool = draftPool;
    }

    public void push(ModelUpdate update){
        update.pushInto(this);
        this.lastUpdate = update;
    }


    public void setLocalPlayer(String playerName) {
        this.localPlayer = playerName;
    }

    public String getLocalPlayer() {
        return localPlayer;
    }
}
