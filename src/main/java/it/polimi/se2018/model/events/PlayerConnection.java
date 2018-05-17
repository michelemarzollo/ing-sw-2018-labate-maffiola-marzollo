package it.polimi.se2018.model.events;

public class PlayerConnection extends ModelUpdate {

    private final String playerName;
    private final boolean connected;

    public PlayerConnection(String playerName, boolean connected) {
        super(ModelEvent.PLAYER_CONNECTION);
        this.playerName = playerName;
        this.connected = connected;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isConnected() {
        return connected;
    }
}
