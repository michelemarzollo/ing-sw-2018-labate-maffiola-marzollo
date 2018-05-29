package it.polimi.se2018.utils;

import it.polimi.se2018.model.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Utility class to create Game instances at different stages.
 * <p>The available stages are:
 * <ul>
 * <li>game set up</li>
 * <li>game started (turn not updated yet)</li>
 * <li>game started on first turn of round 1 with one die placed (single player only)</li>
 * <li>game at the end of a round</li>
 * <li>game finished</li>
 * </ul>
 * </p>
 *
 * @author dvdmff
 */
public class GameUtils {

    /**
     * Assigns the dummy pattern duomo to all players.
     *
     * @param first  The first player.
     * @param second The second player.
     * @throws URISyntaxException if invalid uri is encountered.
     * @throws SAXException       if sax can't be used.
     */
    private static void assignCandidates(Player first, Player second)
            throws URISyntaxException, SAXException {

        File directory = new File(GameUtils.class
                .getResource("../model/patternSources/duomo").toURI()
        );
        XmlPatternLoader loader = new XmlPatternLoader(directory);
        Pattern pattern = loader.load(1)[0];
        first.setCandidates(new Pattern[]{
                pattern
        });
        second.setCandidates(new Pattern[]{
                pattern
        });

    }

    /**
     * Assigns private objectives to the players.
     * <p>Only blue and red private objectives are dealt according to the following pattern:
     * <ul>
     * <li>in single player mode the first player has [blue, red] in this order.</li>
     * <li>in multi player mode the first player has [blue] and the second one [red]</li>
     * </ul></p>
     *
     * @param first       The first player.
     * @param second      The second player.
     * @param multiPlayer {@code true} if in multi player mode.
     */
    private static void assignPrivateObjectives(Player first, Player second, boolean multiPlayer) {
        PrivateObjectiveCard[] allCards =
                new PrivateObjectiveFactory().newInstances(5);

        PrivateObjectiveCard blueCard = Arrays.stream(allCards)
                .filter(c -> c.getColour() == Colour.BLUE)
                .findFirst()
                .orElse(null);
        PrivateObjectiveCard redCard = Arrays.stream(allCards)
                .filter(c -> c.getColour() == Colour.RED)
                .findFirst()
                .orElse(null);

        if (multiPlayer) {
            first.setCards(new PrivateObjectiveCard[]{
                    blueCard
            });
            second.setCards(new PrivateObjectiveCard[]{
                    redCard
            });
        } else
            first.setCards(new PrivateObjectiveCard[]{
                    blueCard,
                    redCard
            });
    }

    /**
     * Deals all tool cards to the specified game.
     * @param game The game where to deal tool cards.
     */
    private static void dealToolCards(Game game){
        ToolCard[] toolCards = new ToolCardFactory().newInstances(12);
        game.setToolCards(toolCards);
    }

    /**
     * Deals all public objective cards to the specified game.
     * @param game The game where to public objective cards.
     */
    private static void dealPublicObjectives(Game game){
        PublicObjectiveCard[] publicObjectives = new PublicObjectiveFactory().newInstances(10);
        game.setPublicObjectiveCards(publicObjectives);
    }

    /**
     * Creates a fully set up game.
     * <p>In multi player mode, player one has private objective blue and player two
     * has red; in single player mode player one has blue and red private objectives,
     * in this order.</p>
     * <p>All players have the dummy pattern "duomo" set as the sole candidate.</p>
     *
     * @param multiPlayer {@code true} if in multi player mode.
     * @return An instance of Game that is fully set up or null if some problem occurred.
     */
    public static Game getSetUpGame(boolean multiPlayer) {
        Game game = new Game();
        Player playerOne = new Player("Pippo");
        Player playerTwo = new Player("Pluto");

        try {
            assignCandidates(playerOne, playerTwo);
        } catch (URISyntaxException | SAXException e) {
            return null;
        }

        assignPrivateObjectives(playerOne, playerTwo, multiPlayer);
        dealToolCards(game);
        dealPublicObjectives(game);

        game.addPlayer(playerOne);
        if (multiPlayer)
            game.addPlayer(playerTwo);


        game.terminateSetup();
        PublicObjectiveCard[] cards = {};
        game.setPublicObjectiveCards(cards);

        return game;
    }

    /**
     * Creates an already started game.
     * <p>The game has the same setup as in {@code getSetUpGame}.</p>
     * <p>All players have the dummy pattern "duomo" set as active pattern.</p>
     *
     * @param multiPlayer {@code true} if in multi player mode.
     * @return An instance of Game that is already started or null if some problem occurred.
     * @see GameUtils#getSetUpGame(boolean)
     */
    public static Game getStartedGame(boolean multiPlayer) {

        Game game = getSetUpGame(multiPlayer);

        if (game == null)
            return null;

        game.getPlayers().forEach(p -> {
            p.setPattern(p.getCandidates()[0]);
            p.setTokens(4);
        });

        game.start();
        List<Die> dice = getDice(true);
        game.getDraftPool().setDice(dice);
        game.getTurnManager().updateTurn();

        return game;
    }

    /**
     * Creates a game that is at round one end.
     * <p>The game has the same setup as in {@code getStartedGame}.</p>
     * <p>All players have the dummy pattern "duomo" set as active pattern.</p>
     *
     * @param multiPlayer {@code true} if in multi player mode.
     * @return An instance of Game that is at round one end or null if some problem occurred.
     * @see GameUtils#getStartedGame(boolean)
     */
    public static Game getRoundFinishedGame(boolean multiPlayer) {
        Game game = getStartedGame(multiPlayer);
        if (game == null)
            return null;

        boolean success;
        do {
            success = game.getTurnManager().updateTurn();
        } while (success);

        return game;
    }

    /**
     * Creates an already finished game.
     * <p>The game has the same setup as in {@code getStartedGame}.</p>
     * <p>All players have the dummy pattern "duomo" set as active pattern.</p>
     *
     * @param multiPlayer {@code true} if in multi player mode.
     * @return An instance of Game that is ended or null if some problem occurred.
     * @see GameUtils#getStartedGame(boolean)
     */
    public static Game getFinishedGame(boolean multiPlayer) {
        Game game = getStartedGame(multiPlayer);

        if (game == null)
            return null;

        try {
            for (int i = 0; i < TurnManager.ROUNDS; ++i) {
                boolean success;
                do {
                    success = game.getTurnManager().updateTurn();
                } while (success);

                game.getTurnManager().setupNewRound();
            }
        } catch (TurnManager.GameFinishedException e) {
            return game;
        }

        return null;
    }

    /**
     * Creates an already started game where a player has already placed one die.
     * <p>The placed die is a yellow 6 in position (1, 0).</p>
     * <p>The game has the same setup as in {@code getStartedGame}.</p>
     * <p>All players have the dummy pattern "duomo" set as active pattern.</p>
     *
     * @return An instance of Game that is already started or null if some problem occurred.
     * @see GameUtils#getStartedGame(boolean)
     */
    public static Game getHalfwayGame() {

        Game game = GameUtils.getStartedGame(false);
        if (game == null || !game.isStarted())
            return null;



        Player player = game.getPlayers().get(0);
        Die yellow6 = new Die(6, new Random(), Colour.YELLOW);

        try {
            Pattern newPattern = player.getPattern()
                    .placeDie(yellow6, new Coordinates(1, 0));
            player.setPattern(newPattern);
        } catch (PlacementErrorException e) {
            return null;
        }

        game.getDraftPool().setDice(GameUtils.getDice(false));
        return game;
    }

    /**
     * Returns a fixed list of dice.
     * <p>The dice are, in order
     * <ul>
     * <li>Red 1</li>
     * <li>Blue 4</li>
     * <li>Yellow 6</li>
     * <li>Red 2</li>
     * <li>Green 4 (if in multi player mode)</li>
     * </ul></p>
     * <p>All dice have been independently initialized with a random with seed 0,
     * meaning that they have a well-known future set of values.</p>
     *
     * @param multiPlayer {@code true} if in multi player mode.
     * @return A list containing well-known dice.
     */
    public static List<Die> getDice(boolean multiPlayer) {
        List<Die> dice = new ArrayList<>();
        dice.add(new Die(1, new Random(0), Colour.RED));
        dice.add(new Die(4, new Random(0), Colour.BLUE));
        dice.add(new Die(6, new Random(0), Colour.YELLOW));
        dice.add(new Die(2, new Random(0), Colour.RED));
        if (multiPlayer)
            dice.add(new Die(4, new Random(0), Colour.GREEN));
        return dice;
    }
}
