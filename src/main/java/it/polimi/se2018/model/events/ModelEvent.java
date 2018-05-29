package it.polimi.se2018.model.events;

/**
 * This enum represent the possible types of model updates.
 */
public enum ModelEvent {
    GAME_SETUP,
    GAME_END,
    PLAYER_STATUS,
    DRAFT_POOL_UPDATE,
    NEXT_TURN,
    ROUND_TRACK_UPDATE,
    PLAYER_CONNECTION_STATUS,
    FORCED_SELECTION
}
