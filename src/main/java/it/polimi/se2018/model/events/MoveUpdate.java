package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Die;

/**
 * The event to update the view after a move in the middle of the game.
 *
 * @author michelemarzollo
 */
public class MoveUpdate extends ModelUpdate {

    /**
     * The name of the player who made the move.
     */
    private String player;

    /**
     * The updated number of tokens.
     */
    private int tokens;

    /**
     * The updated grid, after the player's move.
     */
    private Cell[][] grid;

    /**
     * The array containing the dice in the updated DraftPool.
     */
    private Die[] draftPool;

    /**
     * The current RoundTrack.
     */
    private Die[][] roundTrack;

    /**
     * The index of the die in {@code draftPool} that must be used by the player.
     * If the selection is not forced the value is set to -1.
     */
    private int forcedSelection;

    /**
     * The constructor of the class. All parameters must be initialized, so passed as
     * parameters.
     *
     * @param updateType      The kind of message it is.
     * @param player          The name of the player who made the move.
     * @param tokens          The updated number of tokens.
     * @param grid            The updated grid.
     * @param draftPool       The array containing the dice in the updated DraftPool.
     * @param roundTrack      The current RoundTrack.
     * @param forcedSelection The index of the die in {@code draftPool} that must be used by the player.
     */
    public MoveUpdate(String updateType, String player, int tokens, Cell[][] grid,
                      Die[] draftPool, Die[][] roundTrack, int forcedSelection) {

        super(updateType);
        this.player = player;
        this.tokens = tokens;
        this.grid = grid;
        this.draftPool = draftPool;
        this.roundTrack = roundTrack;
        this.forcedSelection = forcedSelection;

    }

    /**
     * The getter for {@code player}.
     *
     * @return {@code player}
     */
    public String getPlayer() {
        return player;
    }

    /**
     * The getter for {@code tokens}.
     *
     * @return {@code tokens}
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * The getter for {@code grid}.
     *
     * @return {@code grid}
     */
    public Cell[][] getGrid() {
        return grid;
    }

    /**
     * The getter for {@code draftPool}.
     *
     * @return {@code draftPool}
     */
    public Die[] getDraftPool() {
        return draftPool;
    }

    /**
     * The getter for {@code roundTrack}.
     *
     * @return {@code roundTrack}
     */
    public Die[][] getRoundTrack() {
        return roundTrack;
    }

    /**
     * The getter for {@code forcedSelection}.
     *
     * @return {@code forcedSelection}
     */
    public int getForcedSelection() {
        return forcedSelection;
    }

}
