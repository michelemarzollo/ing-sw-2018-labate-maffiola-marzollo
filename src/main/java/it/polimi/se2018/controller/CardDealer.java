package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Logger;
import org.xml.sax.SAXException;

import java.util.Arrays;
import java.util.List;

/**
 * This Class is used in the game's setup to deal the cards of each type in
 * the right way according to the game's rules.
 *
 * @author giorgiolabate
 */
public class CardDealer {

    /**
     * Number of candidate patterns per player.
     */
    private static final int CANDIDATES_PER_PLAYER = 4;

    /**
     * A reference to the {@link Game}'s instance to which the CardDealer is associated.
     */
    private Game game;

    /**
     * Creates a new CardDealer associated to the specified game instance.
     *
     * @param game The {@link Game} instance to which the CardDealer has to refer.
     */
    public CardDealer(Game game) {
        this.game = game;
    }

    /**
     * Getter for the actual game instance.
     *
     * @return The reference to the actual game instance to which the CardDealer
     * is associated.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Deals the cards according to the passed parameters and returns to
     * the controller the object to calculate scores given by PublicObjectiveCards.
     * It also deals the 4 {@link Pattern} to each player.
     *
     * @param publicObj  The number of {@link PublicObjectiveCard}
     *                   to be dealt.
     * @param privateObj The number of {@link PrivateObjectiveCard} per player
     *                   to be dealt.
     * @param toolCards  The number of {@link ToolCard} to be dealt.
     * @return The array of objects that calculate the scores given by PublicObjectiveCards.
     */
    public PublicObjectiveScore[] deal(int publicObj, int privateObj, int toolCards) {
        dealToolCards(toolCards);
        dealPrivateObjectives(privateObj);
        dealCandidates();
        return dealPublicObjectives(publicObj);
    }

    /**
     * Deals candidates patterns.
     */
    private void dealCandidates() {
        try {
            List<Player> players = getGame().getPlayers();
            XmlPatternLoader loader = new XmlPatternLoader();
            Pattern[] patterns = loader.load(players.size() * CANDIDATES_PER_PLAYER);

            for (int i = 0; i < players.size(); i++) {
                int fromIndex = i * CANDIDATES_PER_PLAYER;
                int toIndex = (i + 1) * CANDIDATES_PER_PLAYER;
                players.get(i).setCandidates(Arrays.copyOfRange(patterns, fromIndex, toIndex));
            }
        } catch (SAXException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }

    }

    /**
     * Deals private objectives to players.
     *
     * @param privateObj The number of private objective cards per player.
     */
    private void dealPrivateObjectives(int privateObj) {
        PrivateObjectiveFactory privateObjectiveFactory = PrivateObjectiveFactory.getInstance();
        List<Player> players = getGame().getPlayers();
        PrivateObjectiveCard[] cards = privateObjectiveFactory.newInstances(players.size() * privateObj);

        for (int i = 0; i < players.size(); i++)
            players.get(i).setCards(Arrays.copyOfRange(cards, i * privateObj, i * privateObj + privateObj));

    }

    /**
     * Deals tool cards.
     *
     * @param toolCards The number of tool cards to deal.
     */
    private void dealToolCards(int toolCards) {
        if (ToolCardFactory.getInstance() == null) {
            try {
                XmlToolCardLoader xmlToolCardLoader = new XmlToolCardLoader();
                xmlToolCardLoader.createToolCardFactory();
            } catch (SAXException e) {
                Logger.getDefaultLogger().log("USAXException " + e);
            }
        }
        this.getGame().setToolCards(ToolCardFactory.getInstance().newInstances(toolCards));
    }

    /**
     * Deals public objectives.
     * <p>Sets the PublicObjectiveCards in the model and returns an array
     * of PublicObjectiveScores to the controller.</p>
     *
     * @param publicObj The number of public objectives to deal.
     * @return the array of PublicObjectiveScores to be set in the controller.
     */
    private PublicObjectiveScore[] dealPublicObjectives(int publicObj) {

        try {
            XmlPublicObjectiveLoader publicObjectiveFactory = new XmlPublicObjectiveLoader();
            PublicObjectiveElements publicObjectiveElements = publicObjectiveFactory.load(publicObj);

            //sets the cards in the model
            this.getGame().setPublicObjectiveCards(publicObjectiveElements.getCards());
            //returns the objects to calculate the score to the controller
            return publicObjectiveElements.getScoreCalculators();

        } catch (SAXException e) {
            Logger.getDefaultLogger().log("USAXException " + e);
        }

        return new PublicObjectiveScore[]{};
    }


}
