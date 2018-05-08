package it.polimi.se2018.model;

/**
 * @author giorgiolbt
 */

public class Player {
    private final String name;
    private int score;
    private int tokens;
    /**
     * Array of the 4 pattern candidates for the player's choice
     */
    private Pattern[] candidates;
    /**
     * The pattern actually chosen by the player
     */
    private Pattern pattern;
    /**
     * Array that contains the Private Objective Card/Cards of the player. In MultiPlayer mode the player has only one Private
     * Objective Card: the second element of the array will be null
     */
    private PrivateObjectiveCard[] cards;

    /**
     * The constructor of <code>Player</code>
     *
     * @param name is the <code>name</code> of the player
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Getter for the attribute <code>name</code>
     * @return the <code>name</code> of the player
     */

    public String getName() {
        return name;
    }

    /**
     * Getter for the attribute <code>score</code>
     * @return the <code>score</code> of the player
     */

    public int getScore() {
        return score;
    }

    /**
     * Setter for the <code>score</code> of the player
     * @param score is the value set for the attribute
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Getter for the attribute <code>tokens</code>
     * @return the <code>tokens</code> of the player
     */

    public int getTokens() {
        return tokens;
    }

    /**
     * Getter for the attribute <code>canditates</code>
     * @return the <code>candidates</code> of the player
     */

    public Pattern[] getCandidates() {
        return candidates;
    }

    /**
     * Setter for the attribute <code>candidates </code>
     * @param candidates is the value setted for the attribute
     */

    public void setCandidates(Pattern[] candidates) {
        this.candidates = candidates;
    }

    /**
     * Getter for the attribute <code>pattern</code>
     * @return the <code>pattern</code> of the player
     */

    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Setter for the attribute <code>candidates </code>
     * @param pattern is the value set for the attribute. This method also set the number of <code>tokens</code> of the player
     *                that depends on the <code>difficulty</code> of the <code>pattern</code>
     */

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
        this.tokens = pattern.getDifficulty();
    }

    /**
     * Getter for the attribute <code>cards</code>
     * @return the <code>cards</code> of the player
     */

    public PrivateObjectiveCard[] getCards() {
        return cards;
    }

    /**
     * Setter for the attribute <code>cards</code>
     * @param cards is the value setted for the attribute
     */
    public void setCards(PrivateObjectiveCard[] cards) {
        this.cards = cards;
    }

    /**
     * method for decrementing the number of <code>tokens</code> of the player when using a Tool Card
     * @param n is the number of <code> tokens</code> used for using the Tool Card
     * @throws NotEnoughTokensException when the player doesn't have enough tokens to use the Tool Card
     */
    public void consumeTokens(int n) throws NotEnoughTokensException{
        if(n > getTokens()){
            throw new NotEnoughTokensException("Not enough tokens to use the Toolcard");
        }
        tokens -= n;
    }
}
