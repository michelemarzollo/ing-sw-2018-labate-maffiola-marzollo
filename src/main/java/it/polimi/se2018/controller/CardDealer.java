package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;

import java.util.Arrays;
import java.util.List;

/**
 * This Class is used in the game's setup to
 * deal the cards of each type in the right way
 * according to the game's rules.
 *
 * @author giorgiolabate
 */
public class CardDealer {

    /**
     * A reference to the {@link Game}'s
     * instance to which the CardDealer is
     * associated.
     */
    private Game game;

    /**
     * Creates a new CardDealer associated to the
     * specified game instance.
     * @param game The {@link Game} instance to which
     *             the CardDealer has to refer.
     */
    public CardDealer(Game game) {
        this.game = game;
    }

    /**
     * Getter for the actual game instance.
     * @return The reference to the actual
     * game instance to which the CardDealer
     * is associated.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Deals the cards according to the passed
     * parameters. It also deals the 4 {@link Pattern} to
     * each player.
     * @param publicObj The number of {@link PublicObjectiveCard}
     *                  to be dealt.
     * @param privateObj The number of {@link PrivateObjectiveCard}
     *                   to be dealt.
     * @param toolCards The number of {@link ToolCard} to be dealt.
     */
    public void deal(int publicObj, int privateObj, int toolCards){
        PublicObjectiveFactory publicObjectiveFactory = new PublicObjectiveFactory();
        ToolCardFactory toolCardFactory = new ToolCardFactory();
        PrivateObjectiveFactory privateObjectiveFactory = new PrivateObjectiveFactory();
        List<Player> players = game.getPlayers();
        this.getGame().setPublicObjectiveCards(publicObjectiveFactory.newInstances(publicObj));
        this.getGame().setToolCards(toolCardFactory.newInstances(toolCards));
        PrivateObjectiveCard[] cards = privateObjectiveFactory.newInstances(players.size()*privateObj);
        for(int i = 0; i < players.size(); i++){
            players.get(i).setCards(Arrays.copyOfRange(cards, i*privateObj, i*privateObj + privateObj));
            //TODO assign patterns (create the Factory)
        }
    }
}
