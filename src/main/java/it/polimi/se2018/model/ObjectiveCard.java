package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * ObjectiveCard represents the common features of both public and private
 * objective cards
 *
 * @author michelemarzollo
 */
public interface ObjectiveCard extends Serializable {

    /**
     * Getter for the name of the Objective Card.
     * @return the card's name.
     */
    String getName();

    /**
     * Getter for the description of the Objective Card.
     * @return the card's description.
     */
    String getDescription();
}
