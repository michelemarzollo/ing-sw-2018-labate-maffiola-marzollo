package it.polimi.se2018.model;

/**
 * PrivateObjectiveCard represents all private objective cards
 */
public class PrivateObjectiveCard implements ObjectiveCard {

    /**
     * The name of the card
     */
    private final String name;
    /**
     * The colour of the shades
     * <p>It's the only difference between the different cards </p>
     */
    private final Colour colour;

    /**
     * The constructor of the class
     *
     * @param c The colour of the card
     */
    PrivateObjectiveCard(String name, Colour c) {
        this.name = name;
        this.colour = c;
    }

    /**
     * The getter of the name
     */
    public String getName() {
        return name;
    }

    /**
     * The getter of the colour
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * The method to calculate the score dealing with the PrivateObjectiveCard
     * <p>The score is given by the number of dice with colour <code>colour</code></p>
     *
     * @param grid The grid on which the score must be calculated
     * @return The score
     */
    @Override
    public int getScore(Cell[][] grid) {
        int count = 0;

        //Counts all dice with colour "colour"
        for (Cell[] row : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (row[j].getDie().getColour() == colour)
                    count += row[j].getDie().getValue();
            }
        }
        return count;
    }

}
