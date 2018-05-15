package it.polimi.se2018.model.events;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.View;

public class PlaceDie extends ViewMessage {

    private final int index;
    private final Coordinates destination;

    public PlaceDie(String action, String playerName, View view, int index, Coordinates destination) {
        super(action, playerName, view);
        this.destination = destination;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Coordinates getDestination() {
        return destination;
    }
}
