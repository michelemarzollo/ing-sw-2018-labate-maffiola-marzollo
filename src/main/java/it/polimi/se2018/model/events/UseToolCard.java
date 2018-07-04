package it.polimi.se2018.model.events;

import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;

/**
 * The message that updates the views when a ToolCard in the model was used.
 *
 * @author michelemarzollo
 */
public class UseToolCard extends ModelUpdate {

    /**
     * The name of the {@link it.polimi.se2018.model.ToolCard}
     * that was just used.
     */
    private String toolCardName;

    /**
     * The constructor of the class.
     */
    public UseToolCard(String toolCardName) {
        super(ModelEvent.USE_TOOL_CARD);
        this.toolCardName = toolCardName;
    }

    /**
     * The method to add in the view-model the information that the ToolCard was used.
     *
     * @param organizer The organizer where the message will be pushed into.
     */
    @Override
    public void pushInto(ViewDataOrganizer organizer) {
        organizer.push(this);
    }

    /**
     * The getter for the name of the ToolCard.
     *
     * @return the name of the ToolCard.
     */
    public String getToolCardName() {
        return toolCardName;
    }
}
