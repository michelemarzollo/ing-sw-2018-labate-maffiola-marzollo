package it.polimi.se2018.model.events;

import it.polimi.se2018.view.View;

public class SelectCard extends ViewMessage{
    public SelectCard(String action, String playerName, View view) {
        super(action, playerName, view);
    }

    public String getName(){
        return "";
    }
}
