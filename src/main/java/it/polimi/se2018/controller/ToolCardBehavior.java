package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.ViewMessage;

public interface ToolCardBehavior {
    void askParameters(ViewMessage message);
    void useToolCard(Game game, ViewMessage message);
}
