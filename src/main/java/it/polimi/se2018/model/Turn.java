package it.polimi.se2018.model;

import it.polimi.se2018.model.events.NextTurn;

/**
 * Class representing a turn in the game.
 * @author dvdmff
 */
public class Turn {
    /**
     * The reference to the player whose turn this is.
     */
    private final Player player;

    /**
     * Flag to indicate whether the player has a second
     * turn this round.
     */
    private final boolean secondTurnAvailable;
    /**
     * Flag to indicate if the player has already placed
     * a die this turn.
     */
    private boolean alreadyPlacedDie;
    /**
     * Flag to indicate if the player has already used
     * a tool card this turn.
     */
    private boolean alreadyUsedToolCard;

    /**
     * The index of the die that the player is forced to select.
     */
    private int forcedSelectionIndex;

    /**
     * The index of the die that will be consumed to activate a tool
     * card in single player mode.
     */
    private int sacrificeIndex;

    /**
     * The tool card set to active.
     */
    private ToolCard selectedToolCard;

    /**
     * Creates a Turn object for the given player.
     * <p>The newly created turn has, by default, {@code hasAlreadyPlacedDie()}
     * and {@code hasAlreadyUsedToolCard()} returning {@code false}, since
     * the player is supposed not to have done any move yet.</p>
     * @param player The player whose turn this is.
     * @param secondTurnAvailable Flag to indicate if {@code player}
     *                            has a second turn this round.
     */
    public Turn(Player player, boolean secondTurnAvailable) {
        this.player = player;
        this.secondTurnAvailable = secondTurnAvailable;
        this.alreadyPlacedDie = false;
        this.alreadyUsedToolCard = false;
        this.forcedSelectionIndex = -1;
        this.sacrificeIndex = -1;
        this.selectedToolCard = null;
        notifyChange();
    }

    private void notifyChange(){
        NextTurn message = new NextTurn(this);
        player.getGame().notifyObservers(message);
    }

    /**
     * Getter for the player who can move.
     * @return The player whose turn this is.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Tells if the player has a second turn this round.
     * @return {@code true} if the player has a second turn
     *         this round; {@code false} otherwise.
     */
    public boolean isSecondTurnAvailable() {
        return secondTurnAvailable;
    }

    /**
     * Tells if the player has already placed a die this turn.
     * @return {@code true} if the player has already placed a die
     *         this turn; {@code false} otherwise.
     */
    public boolean hasAlreadyPlacedDie() {
        return alreadyPlacedDie;
    }

    /**
     * Updates the object to indicate that the player has placed
     * a die this turn.
     */
    public void placeDie() {
        this.alreadyPlacedDie = true;
        notifyChange();
    }

    /**
     * Tells if the player has already used a tool card this turn.
     * @return {@code true} if the player has already used a tool
     *         card this turn; {@code false} otherwise.
     */
    public boolean hasAlreadyUsedToolCard() {
        return alreadyUsedToolCard;
    }

    /**
     * Updates the object to indicate that the player has used
     * a tool card this turn.
     */
    public void useToolCard() {
        this.alreadyUsedToolCard = true;
        notifyChange();
    }

    /**
     * Getter for the die in the draft pool that has to be picked
     * this turn.
     * @return The index of the die in the draft pool to be picked
     * this turn.
     */
    public int getForcedSelectionIndex() {
        return forcedSelectionIndex;
    }

    /**
     * Setter for the die in the draft pool that has to be picked
     * this turn.
     *
     * @param forcedSelectionIndex The index of the die in the draft
     *                            pool to be picked this turn.
     */
    public void setForcedSelectionIndex(int forcedSelectionIndex) {
        this.forcedSelectionIndex = forcedSelectionIndex;
        notifyChange();
    }

    /**
     * Getter for the index of the sacrifice die used to activate tool cards.
     * @return The index of the sacrifice die used to activate tool cards.
     */
    public int getSacrificeIndex() {
        return sacrificeIndex;
    }

    /**
     * Setter for the index of the sacrifice die used to activate tool cards.
     * @param sacrificeIndex The index of the sacrifice die used to activate
     *                      tool cards.
     */
    public void setSacrificeIndex(int sacrificeIndex) {
        this.sacrificeIndex = sacrificeIndex;
        notifyChange();
    }

    /**
     * Getter for the currently active tool card.
     * <p>If a tool card is set active, it means that only that tool card can
     * be used.</p>
     * @return The currently active tool card.
     */
    public ToolCard getSelectedToolCard() {
        return selectedToolCard;
    }

    /**
     * Setter for the currently active tool card.
     * <p>If a tool card is set active, it means that only that tool card can
     * be used.</p>
     * @param selectedToolCard The tool card to be set active.
     */
    public void setSelectedToolCard(ToolCard selectedToolCard) {
        this.selectedToolCard = selectedToolCard;
        notifyChange();
    }
}
