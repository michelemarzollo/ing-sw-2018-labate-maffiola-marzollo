package it.polimi.se2018.model.events;


import it.polimi.se2018.model.Die;

import java.util.List;
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
    private List<List<Die>> roundTrack;

    /**
     * The constructor of the class.
     *
     * @param scoreBoard The map that contains the name of the player and the corresponding final score,
     *                   ordered by score.
     * @param roundTrack The final {@link it.polimi.se2018.model.RoundTrack} of the game.
     */
    public GameEnd(Map<String, Integer> scoreBoard, List<List<Die>> roundTrack) {

        super(ModelEvent.GAME_END);
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
    public List<List<Die>>   getRoundTrack() {
        return roundTrack;
    }

}
