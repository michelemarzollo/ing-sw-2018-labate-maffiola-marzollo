package it.polimi.se2018.model.events;

/**
 * This enum contains all the possible actions that a player may want to perform
 * or that he automatically performs in both SinglePlayer mode and MultiPlayer mode.
 */
public enum Action {

    PLACE_DIE, ACTIVATE_TOOL_CARD, APPLY_TOOL_CARD, END_TURN,
    REGISTER_PLAYER, DISCONNECT_PLAYER, RECONNECT_PLAYER, SELECT_PATTERN,
    SELECT_DIFFICULTY, SELECT_PRIVATE_OBJECTIVE, //single player actions
    CHOOSE_VALUE //to be used by the ToolCard FluxRemover

}
