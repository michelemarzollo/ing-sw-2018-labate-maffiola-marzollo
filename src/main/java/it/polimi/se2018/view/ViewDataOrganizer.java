package it.polimi.se2018.view;

import it.polimi.se2018.model.Die;

import it.polimi.se2018.model.events.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ViewDataOrganizer {

    private GameSetup gameSetup;
    private GameEnd gameEnd;
    private PlayerStatus[] playerStatusList;
    private PlayerConnectionStatus[] connectionStatusList;
    private NextTurn nextTurn;
    private RoundTrackUpdate roundTrack;
    private DraftPoolUpdate draftPool;

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
        return Arrays.stream(playerStatusList)
                .filter(p -> p.getPlayerName().equals(name))
                .findFirst();

    }

    public PlayerStatus[] getAllPlayerStatus() {
        return playerStatusList;
    }

    public PlayerConnectionStatus getConnectionStatus(String name){
        Optional <PlayerConnectionStatus> connectionStatus = findConnectionStatus(name);
        return connectionStatus.orElse(null);
    }


    private Optional<PlayerConnectionStatus> findConnectionStatus(String name) {
        return Arrays.stream(connectionStatusList)
                .filter(p -> p.getPlayerName().equals(name))
                .findFirst();

    }
    public PlayerConnectionStatus[] getAllConnectionStatus() {
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


    public void push(GameSetup gameSetup){
        this.gameSetup = gameSetup;
    }

    public void push(GameEnd gameEnd) {
        this.gameEnd = gameEnd;
    }

    public void push(PlayerStatus[] playerStatusList) {
        this.playerStatusList = playerStatusList;
    }

    public void push(PlayerConnectionStatus[] connectionStatusList) {
        this.connectionStatusList = connectionStatusList;
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
}
