package it.polimi.se2018.model;

/**
 * Class representing a turn in the game
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
     * Constructs a Turn for {@code player}.
     * @param player The player whose turn this is.
     * @param secondTurnAvailable Flag to indicate if {@code player}
     *                            has a second turn this round.
     */
    public Turn(Player player, boolean secondTurnAvailable) {
        this.player = player;
        this.secondTurnAvailable = secondTurnAvailable;
        this.alreadyPlacedDie = false;
        this.alreadyUsedToolCard = false;
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
     * Updates Turn instance to indicate that the player has placed
     * a die this turn.
     */
    public void placeDie() {
        this.alreadyPlacedDie = true;
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
     * Updates Turn instance to indicate that the player has used
     * a tool card this turn.
     */
    public void useToolCard() {
        this.alreadyUsedToolCard = true;
    }
}
