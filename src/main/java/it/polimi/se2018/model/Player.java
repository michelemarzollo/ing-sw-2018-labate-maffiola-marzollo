package it.polimi.se2018.model;

import it.polimi.se2018.model.events.PlayerConnectionStatus;
import it.polimi.se2018.model.events.PlayerStatus;

/**
 * This class represents a Player
 * of the Game.
 *
 * @author giorgiolbt
 */

public class Player {

    /**
     * The Player's username.
     */
    private final String name;

    /**
     * The game the player is bound to.
     */
    private Game game;

    /**
     * The Player's score: it is
     * computed at the end of the Game.
     */
    private int score;

    /**
     * Number of Player's favor tokens.
     */
    private int tokens;

    /**
     * boolean value that indicates if
     * a Player is connected.
     */
    private boolean connected;

    /**
     * Array of the 4 {@link Pattern} candidates
     * for the player's choice
     */
    private Pattern[] candidates;

    /**
     * The {@link Pattern} actually chosen by the player.
     */
    private Pattern pattern;

    /**
     * Array that contains the Private Objective Card/Cards of the player.
     * In MultiPlayer mode the player has only one Private
     * Objective Card: the array will have just one element
     */
    private PrivateObjectiveCard[] cards;

    /**
     * The constructor of Player
     * @param name is the Player's name.
     */
    public Player(String name) {
        this.name = name;
        this.connected = true;
    }

    /**
     * Getter for the attribute name of Player.
     * @return the name of Player.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the attribute score
     * @return the score of Player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter for the score of Player
     * @param score is the value set for the attribute.
     *              This value is computed at the end of the Game.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Getter for the attribute tokens of Player.
     * @return the tokens of Player.
     */
    public int getTokens() {
        return tokens;
    }

    /**
     * Setter for the attribute tokens of Player.
     * @param tokens The new token amount.
     */
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }


    /**
     * Getter for the attribute connected.
     * @return {@code true} if Player result connected
     * to the Server, {@code false} otherwise.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Setter for the attribute connected.
     * @param connected Is the value set for the attribute:
     *                  it will be {@code true} if the Player
     *                  is connected, {@code false} otherwise.
     *
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
        PlayerConnectionStatus message = new PlayerConnectionStatus(getName(), connected);
        game.notifyObservers(message);
    }


    /**
     * Getter for the attribute candidates.
     * @return the {@link Pattern} candidates of Player.
     */
    public Pattern[] getCandidates() {
        return candidates;
    }

    /**
     * Setter for the attribute candidates.
     * @param candidates are the {@link Pattern} array
     *                   among which the Player has to make
     *                   his choice.
     */
    public void setCandidates(Pattern[] candidates) {
        this.candidates = candidates;
    }

    /**
     * Getter for the attribute pattern.
     * @return the Pattern chosen by the player.
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Setter for the attribute pattern.
     * @param pattern is the {@link Pattern} set for the attribute.
     *                This method also set the number of tokens of the player
     *                that depends on the {@code difficulty} of the {@link Pattern}.
     */
    public void setPattern(Pattern pattern) {
        PlayerStatus message = new PlayerStatus(this);
        game.notifyObservers(message);
        this.pattern = pattern;
    }

    /**
     * Getter for the attribute cards
     * @return the array of PrivateObjectiveCards
     * of the player.
     */
    public PrivateObjectiveCard[] getCards() {
        return cards;
    }

    /**
     * Setter for the attribute cards.
     * @param cards is array of PrivateObjectiveCards assigned
     *             to the Player.
     */
    public void setCards(PrivateObjectiveCard[] cards) {
        this.cards = cards;
    }

    /**
     * Method for decrementing the number of tokens of Player
     * when using a {@link ToolCard}.
     * @param n is the number of tokens needed for the usage of
     *         the {@link ToolCard}.
     * @throws NotEnoughTokensException when the player doesn't
     * have enough tokens to use the {@link ToolCard}.
     */
    public void consumeTokens(int n) throws NotEnoughTokensException{
        if(n > getTokens()){
            throw new NotEnoughTokensException("Not enough tokens to use the Toolcard");
        }
        tokens -= n;
    }

    /**
     * Setter for the game the player is bound to.
     * @param game The game the player is bound to.
     */
    void setGame(Game game) {
        this.game = game;
    }

    /**
     * Getter for the game the player is bound to.
     * @return The game the player is bound to.
     */
    Game getGame(){
        return game;
    }
}
