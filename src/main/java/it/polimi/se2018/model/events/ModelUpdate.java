package it.polimi.se2018.model.events;


import java.io.Serializable;

/**
 * The superclass of all events directed from the model to the view.
 * The view is Observer of the model (in particular of the class Game) through
 * messages of this class (or of it's subclasses).
 *
 * @author michelemarzollo
 */
public class ModelUpdate implements Serializable {

    /**
     * The type of message it is
     */
    private ModelEvent eventType;

    /**
     * The constructor of the class.
     *
     * @param eventType The kind of message it is.
     */
    public ModelUpdate(ModelEvent eventType) {
        this.eventType = eventType;
    }

    /**
     * The getter of the attribute {@code eventType}.
     *
     * @return The String {@code eventType}.
     */
    public ModelEvent getEventType() {
        return eventType;
    }

}
