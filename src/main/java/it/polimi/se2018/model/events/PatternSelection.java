package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Pattern;

/**
 * The message to update the view after the selection of a {@link Pattern}.
 * At the beginning of the game each player must select his own window pattern;
 * after the choice this message allows to update the view.
 *
 * @author michelemarzollo
 */
public class PatternSelection extends ModelUpdate {

    /**
     * The name of player that has just selected the pattern.
     */
    private String player;

    /**
     * The pattern selected.
     */
    private Pattern pattern;

    /**
     * The constructor of the class.
     *
     * @param player     The name of player that has just selected the pattern.
     * @param pattern    The pattern selected.
     */
    public PatternSelection(String player, Pattern pattern) {

        super(ModelEvent.PATTERN_SELECTION);
        this.player = player;
        this.pattern = pattern;

    }

    /**
     * The getter for {@code player}.
     *
     * @return {@code player}
     */
    public String getPlayer() {
        return player;
    }

    /**
     * The getter for {@code pattern}.
     *
     * @return {@code pattern}
     */
    public Pattern getPattern() {
        return pattern;
    }

}
