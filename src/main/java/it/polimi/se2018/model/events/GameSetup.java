package it.polimi.se2018.model.events;

import it.polimi.se2018.model.*;
import it.polimi.se2018.view.ViewDataOrganizer;

import java.util.Arrays;


/**
 * The event that updates the view when a new game starts.
 *
 * @author michelemarzollo
 */
public class GameSetup extends ModelUpdate {

    /**
     * The array of players that will participate to the game.
     */
    private final String[] players;

    /**
     * The array of private objective cards.
     * There is a correspondence of indexes with the array {@code players} (each player
     * has one private objective card and vice versa).
     */
    private final String[][] privateObjectives;

    /**
     * The array of the tool cards that can be used in the game.
     */
    private final ToolCard[] toolCards;

    /**
     * The array of the public objective cards that will be used in the game.
     */
    private final String[] publicObjectives;

    /**
     * The array of quartets of patterns. Each cell of {@code candidates[]} contains an
     * array of 4 pattern cards, among which the player will choose the one to use in
     * the game. There is a correspondence of indexes with the array {@code players}.
     */
    private final Pattern[][] candidates;

    /**
     * Creates a game setup message taking information from the specified game.
     *
     * @param game The game containing the setup.
     */
    public GameSetup(Game game) {
        super(ModelEvent.GAME_SETUP);
        this.players = game.getPlayers().stream()
                .map(Player::getName)
                .toArray(String[]::new);
        this.toolCards = Arrays.stream(game.getToolCards())
                .map(ToolCard::new)
                .toArray(ToolCard[]::new);
        this.publicObjectives = Arrays.stream(game.getPublicObjectiveCards())
                .map(ObjectiveCard::getName)
                .toArray(String[]::new);
        this.candidates = game.getPlayers().stream()
                .map(Player::getCandidates)
                .toArray(Pattern[][]::new);
        this.privateObjectives = game.getPlayers().stream()
                .map(p -> Arrays.stream(p.getCards())
                        .map(PrivateObjectiveCard::getName)
                        .toArray(String[]::new))
                .toArray(String[][]::new);
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
    public String[][] getPrivateObjectives() {
        return privateObjectives;
    }

    /**
     * The getter for {@code toolCards}.
     *
     * @return {@code toolCards}
     */
    public ToolCard[] getToolCards() {
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

    /**
     * Pushes this instance of GameSetup into the organizer.
     * @param organizer The organizer where the message will be pushed into.
     */
    @Override
    public void pushInto(ViewDataOrganizer organizer) {
        organizer.push(this);
    }

}
