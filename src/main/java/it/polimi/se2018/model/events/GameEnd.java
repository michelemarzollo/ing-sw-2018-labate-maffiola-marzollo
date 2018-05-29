package it.polimi.se2018.model.events;


import java.util.Map;

/**
 * The event that updates the view when the game is finished: the final results of
 * the game must be shown to the players.
 *
 * @author michelemarzollo
 */
public class GameEnd extends ModelUpdate {

    /**
     * The map that contains the name of the player and the corresponding final score,
     * ordered by score.
     */
    private final Map<String, Integer> scoreBoard;

    /**
     * The constructor of the class.
     *
     * @param scoreBoard The map that contains the name of the player and the corresponding final score,
     *                   ordered by score.
     */
    public GameEnd(Map<String, Integer> scoreBoard) {

        super(ModelEvent.GAME_END);
        this.scoreBoard = scoreBoard;

    }

    /**
     * The getter for {@code scoreBoard}.
     *
     * @return {@code scoreBoard}
     */
    public Map<String, Integer> getScoreBoard() {
        return scoreBoard;
    }

}
