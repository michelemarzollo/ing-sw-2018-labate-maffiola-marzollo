package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.view.ClientView;

/**
 * Set of events regularly used by the player.
 */
public class StandardEventPack extends BoardEventPack {

    /**
     * The index of the selected die from the draft pool.
     */
    private int index = -1;
    /**
     * Flag to indicate if the player can place a die.
     */
    private boolean canPlace;
    /**
     * Flag to indicate if the player can use a tool card.
     */
    private boolean canUseToolCard;
    /**
     * Flag to indicate if the rule set is the one for multi player
     * or single player game.
     */
    private boolean isMultiPlayer;

    /**
     * Creates a new instance that uses the given client view to handle requests.
     * <p>The boolean flags are used to tune the behaviour of the object.</p>
     *
     * @param clientView     The client view responsible for requests.
     * @param canPlace       {@code true} to indicate the player can place.
     * @param canUseToolCard {@code true} to indicate the player can use a tool card.
     * @param isMultiPlayer  {@code true} if the game follows the multi player rule set;
     *                       {@code false} if it follows single player rule set;
     */
    public StandardEventPack(ClientView clientView, boolean canPlace,
                             boolean canUseToolCard, boolean isMultiPlayer) {
        super(clientView);
        this.canPlace = canPlace;
        this.canUseToolCard = canUseToolCard;
        this.isMultiPlayer = isMultiPlayer;
    }

    /**
     * Registers the index of the selected die.
     * @param index The index of the selected die.
     */
    @Override
    public void draftPoolHandler(int index) {
        this.index = index;
    }

    /**
     * Does nothing.
     * @param coordinates The coordinates of the selected die.
     */
    @Override
    public void roundTrackHandler(Coordinates coordinates) {
        //It can't be used
    }

    /**
     * Makes a request through the client view if the index has already been selected.
     * <p>After the request, the selected index is reset.</p>
     * @param coordinates The coordinates of the selected cell.
     */
    @Override
    public void patternHandler(Coordinates coordinates) {
        if (index != -1) {
            getClientView().handlePlacement(index, coordinates);
            index = -1;
        }

    }

    /**
     * Disables the player pattern if {@code canPlace} is {@code false} and the
     * tool card selection if {@code canUseToolCard} is {@code false}.
     * @param board The GameBoard instance displaying the game.
     */
    @Override
    public void prepareControls(GameBoard board) {
        board.getDraftPoolContainer().setDisable(false);
        board.getPlayerPatternContainer().setDisable(!canPlace);
        board.getToolCardContainer().setDisable(!canUseToolCard);
    }

    /**
     * Makes a request through the client view to activate a tool card.
     * <p>If the single player rule set is active, the user has to select a
     * die beforehand, otherwise an error message is shown.</p>
     * @param cardName The name of the tool card.
     */
    @Override
    public void toolCardHandler(String cardName) {
        if (isMultiPlayer)
            getClientView().handleToolCardSelection(cardName);
        else if (index != -1)
            getClientView().handleToolCardSelection(cardName, index);
        else
            getClientView().showError("You must select a die to use the tool card!");

    }
}
