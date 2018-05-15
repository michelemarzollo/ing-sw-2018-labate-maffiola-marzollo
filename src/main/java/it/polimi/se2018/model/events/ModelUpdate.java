package it.polimi.se2018.model.events;


/**
 * The superclass of all events directed from the model to the view.
 * The view is Observer of the model (in particular of the class Game) through
 * messages of this class (or of it's subclasses).
 *
 * @author michelemarzollo
 */
public class ModelUpdate {

    /**
     * The type of message it is
     */
    private String updateType;

    /**
     * The constructor of the class.
     *
     * @param updateType The kind of message it is.
     */
    public ModelUpdate(String updateType) {
        this.updateType = updateType;
    }

    /**
     * The getter of the attribute {@code updateType}.
     *
     * @return The String {@code updateType}.
     */
    public String getUpdateType() {
        return updateType;
    }

}
