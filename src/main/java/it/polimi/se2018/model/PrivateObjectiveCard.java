package it.polimi.se2018.model;

/**
 * PrivateObjectiveCard represents all private objective cards.
 * <p>
 * The card, if it is assigned, is associated to a player, and it gives
 * the player a score at the and of the game, through the method {@code getScore()}.</p>
 *
 * @author michelemarzollo
 */
public class PrivateObjectiveCard implements ObjectiveCard {

    /**
     * The name of the card.
     */
    private final String name;
    /**
     * The colour of the shades.
     * <p>It's the only difference between the different cards.</p>
     */
    private final Colour colour;

    /**
     * The constructor of the class.
     *
     * @param c the colour of the card.
     */
    PrivateObjectiveCard(String name, Colour c) {
        this.name = name;
        this.colour = c;
    }



    /**
     * The getter of the name.
     *
     * @return the card's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * The getter of the colour.
     *
     * @return the card's colour.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * The method to calculate the score dealing with the PrivateObjectiveCard.
     * <p>The score is given by the number of dice with colour {@code colour}.</p>
     *
     * @param grid the grid on which the score must be calculated.
     * @return the score.
     */
    @Override
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

}
