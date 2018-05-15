package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

public class ViewMessage {

    private final View view;
    private final String playerName;
    private final String action;

    public ViewMessage(String action, String playerName, View view) {
        this.view = view;
        this.playerName = playerName;
        this.action = action;
    }

    public View getView() {
        return view;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getAction() {
        return action;
    }
}
