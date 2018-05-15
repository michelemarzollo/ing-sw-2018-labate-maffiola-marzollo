package it.polimi.se2018.model.events;


import it.polimi.se2018.model.Die;

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
    private Map<String, Integer> scoreBoard;

    /**
     * The final {@link it.polimi.se2018.model.RoundTrack} of the game.
     */
    private Die[][] roundTrack;

    /**
     * The constructor of the class.
     *
     * @param updateType The kind of message it is.
     * @param scoreBoard The map that contains the name of the player and the corresponding final score,
     *                   ordered by score.
     * @param roundTrack The final {@link it.polimi.se2018.model.RoundTrack} of the game.
     */
    public GameEnd(String updateType, Map<String, Integer> scoreBoard, Die[][] roundTrack) {

        super(updateType);
        this.scoreBoard = scoreBoard;
        this.roundTrack = roundTrack;

    }

    /**
     * The getter for {@code scoreBoard}.
     *
     * @return {@code scoreBoard}
     */
    public Map<String, Integer> getScoreBoard() {
        return scoreBoard;
    }

    /**
     * The getter for {@code roundTrack}.
     *
     * @return {@code roundTrack}
     */
    public Die[][] getRoundTrack() {
        return roundTrack;
    }

}
