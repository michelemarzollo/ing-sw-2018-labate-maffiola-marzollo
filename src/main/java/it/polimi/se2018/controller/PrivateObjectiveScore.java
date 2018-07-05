package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;

/**
 * The class to calculate the score given by the {@link it.polimi.se2018.model.PrivateObjectiveCard}
 * to the player at the end of the game.
 *
 * @author michelemarzollo
 */
public class PrivateObjectiveScore {

    /**
     * The instance of the singleton.
     */
    private static PrivateObjectiveScore instance = null;

    /**
     * The private constructor.
     */
    private PrivateObjectiveScore(){

    }

    /**
     * The method to get (and eventually create) the instance of the singleton.
     *
     * @return the instance of the singleton.
     */
    public static PrivateObjectiveScore getInstance() {
        if (instance == null)
            instance = new PrivateObjectiveScore();
        return instance;
    }

    /**
     * The method to calculate the score dealing with the PrivateObjectiveCard.
     * <p>The score is given by the number of dice with colour {@code colour}.</p>
     *
     * @param grid   the grid on which the score must be calculated.
     * @param colour the colour for which the score must be calculated.
     * @return the score.
     */
    public int getScore(Cell[][] grid, Colour colour) {
        int count = 0;

        //Counts all dice with colour "colour"
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                Die die = cell.getDie();
                if (die != null && die.getColour() == colour)
                    count += cell.getDie().getValue();
            }
        }
        return count;
    }
}
