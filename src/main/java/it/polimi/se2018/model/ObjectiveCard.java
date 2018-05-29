package it.polimi.se2018.model;

/**
 * ObjectiveCard represents the common features of both public and private
 * objective cards
 *
 * @author michelemarzollo
 */
public interface ObjectiveCard {

    /**
     * The method that calculates the score
     * <p>It must be overrode by the subclasses</p>
     *
     * @param grid The grid on which the score must be calculated
     * @return The score
     */
    int getScore(Cell[][] grid);

    String getName();
}
