package it.polimi.se2018.view;

import it.polimi.se2018.utils.Coordinates;

public abstract class View {

    private final String playerName;

    protected View(String playerName) {
        this.playerName = playerName;
    }

    public abstract void showGame();
    public abstract void showError(String error);
    public abstract void showPatternSelection();
    public abstract void showPrivateObjectiveSelection();
    public abstract void showScoreBoard();

    public abstract void handlePlacement(int index, Coordinates destination);
    public abstract void handleToolCardSelection(String name);
    public abstract void handleEndTurn();
    public abstract void handlePatternSelection(String patternName);
    public abstract void handleToolCardUsage(String name, int index, boolean increment);
    public abstract void handleToolCardUsage(String name, int index);
    public abstract void handleToolCardUsage(String name, int index, Coordinates destination);
    public abstract void handleToolCardUsage(String name, Coordinates source, Coordinates destination);
    public abstract void handleToolCardUsage(String name, Coordinates[] sources, Coordinates[] destinations);
    public abstract void handleDisconnect();

}