package it.polimi.se2018.controller;

import it.polimi.se2018.model.Cell;
import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;

/**
 * The class to calculate the score given by the {@link it.polimi.se2018.model.PrivateObjectiveCard}
 * to the player at the end of the game.
 */
public class PrivateObjectiveScore {

    /**
     * The colour of the dice that will count int the score of the card.
     */
    private Colour colour;

    /**
     * The method to calculate the score dealing with the PrivateObjectiveCard.
     * <p>The score is given by the number of dice with colour {@code colour}.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    public int getScore(Cell[][] grid) {
        int count = 0;

        //Counts all dice with colour "colour"
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                Die die = cell.getDie();
                if (die != null && die.getColour() == getColour())
                    count += cell.getDie().getValue();
            }
        }
        return count;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    /**
     * The getter for the colour.
     *
     * @return the card's colour.
     */
    public Colour getColour() {
        return colour;
    }
}
