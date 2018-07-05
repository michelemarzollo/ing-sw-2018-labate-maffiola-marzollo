package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * The abstract class that contains common features of Objective Cards.
 *
 * @author michelemarzollo
 */
public abstract class ObjectiveCard implements Serializable {

    /**
     * The name of the Objective Card.
     */
    private String name;

    /**
     * The description of the card: it resumes synthetically the
     * goal the player should aim for, to get a good score.
     */
    private String description;

    /**
     * The constructor of the class.
     *
     * @param name        The name of the Objective Card.
     * @param description The description of the card.
     */
    protected ObjectiveCard(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * The getter for the name.
     *
     * @return the name of the card.
     */
    public String getName() {
        return name;
    }

    /**
     * The getter for the description.
     *
     * @return the description of the card.
     */
    public String getDescription() {
        return description;
    }
}
