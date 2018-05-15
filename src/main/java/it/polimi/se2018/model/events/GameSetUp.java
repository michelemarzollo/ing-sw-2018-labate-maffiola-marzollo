package it.polimi.se2018.model.events;

import it.polimi.se2018.model.Pattern;


/**
 * The event that updates the view when a new game starts.
 *
 * @author michelemarzollo
 */
public class GameSetUp extends ModelUpdate {

    /**
     * The array of players that will participate to the game.
     */
    private String[] players;

    /**
     * The array of private objective cards.
     * There is a correspondence of indexes with the array {@code players} (each player
     * has one private objective card and vice versa).
     */
    private String[] privateObjectives;

    /**
     * The array of the tool cards that can be used in the game.
     */
    private String[] toolCards;

    /**
     * The array of the public objective cards that will be used in the game.
     */
    private String[] publicObjectives;

    /**
     * The array of quartets of patterns. Each cell of {@code candidates[]} contains an
     * array of 4 pattern cards, among which the player will choose the one to use in
     * the game. There is a correspondence of indexes with the array {@code players}.
     */
    private Pattern[][] candidates;

    /**
     * The constructor of the class.
     *
     * @param updateType       The kind of message it is.
     * @param players          The array of players that will participate to the game.
     * @param toolCards        The array of private objective cards.
     * @param publicObjectives The array of the public objective cards that will be used in the game.
     * @param candidates       The array of quartets of patterns.
     */
    public GameSetUp(String updateType, String[] players, String[] toolCards,
                     String[] publicObjectives, Pattern[][] candidates) {

        super(updateType);
        this.players = players;
        this.toolCards = toolCards;
        this.publicObjectives = publicObjectives;
        this.candidates = candidates;

    }

    /**
     * The getter for {@code players}.
     *
     * @return {@code players}
     */
    public String[] getPlayers() {
        return players;
    }

    /**
     * The getter for {@code privateObjectives}.
     *
     * @return {@code privateObjectives}
     */
    public String[] getPrivateObjectives() {
        return privateObjectives;
    }

    /**
     * The getter for {@code toolCards}.
     *
     * @return {@code toolCards}
     */
    public String[] getToolCards() {
        return toolCards;
    }

    /**
     * The getter for {@code publicObjectives}.
     *
     * @return {@code publicObjectives}
     */
    public String[] getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * The getter for {@code candidates}.
     *
     * @return {@code candidates}
     */
    public Pattern[][] getCandidates() {
        return candidates;
    }

}
