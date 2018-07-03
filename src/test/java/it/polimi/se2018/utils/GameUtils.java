package it.polimi.se2018.utils;

import it.polimi.se2018.controller.*;
import it.polimi.se2018.model.*;
import org.junit.Assert;
import org.xml.sax.SAXException;

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
 *
 * @author dvdmff
 */
public class GameUtils {

    /**
     * Assigns the dummy pattern duomo to all players.
     *
     * @param first  The first player.
     * @param second The second player.
     * @throws SAXException if sax can't be used.
     */
    private static void assignCandidates(Player first, Player second)
            throws SAXException {

        String listPath = "it/polimi/se2018/model/patternSources/duomo/";

        XmlPatternLoader loader = new XmlPatternLoader(listPath, "duomo.list");
        Pattern pattern = loader.load(1)[0];
        first.setCandidates(new Pattern[]{
                pattern, pattern
        });
        second.setCandidates(new Pattern[]{
                pattern, pattern
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
     *
     * @param game The game where to deal tool cards.
     */
    private static void dealToolCards(Game game) {
        ToolCardFactory.reset();
        if (ToolCardFactory.getInstance() == null) {
            try {
                XmlToolCardLoader xmlToolCardLoader = new XmlToolCardLoader();
                xmlToolCardLoader.createToolCardFactory();
            } catch (SAXException e) {
                Logger.getDefaultLogger().log("USAXException " + e);
            }
        }
        ToolCard[] toolCards = ToolCardFactory.getInstance().newInstances(12);
        game.setToolCards(toolCards);
    }

    /**
     * Deals all public objective cards to the specified game.
     *
     * @param game The game where to public objective cards.
     */
    private static void dealPublicObjectives(Game game) {
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
        } catch (SAXException e) {
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
     * </ul>
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

    /**
     * Places some dice on the pattern.
     *
     * @param pattern The pattern where dice are to be placed.
     * @param random  The random used to initialize dice.
     * @return A pattern with some dice placed.
     */
    private static Pattern fillPattern(Pattern pattern, Random random) {
        try {
            pattern = pattern.placeDie(new Die(6, random, Colour.PURPLE), new Coordinates(3, 4));
            pattern = pattern.placeDie(new Die(4, random, Colour.YELLOW), new Coordinates(2, 3));
            pattern = pattern.placeDie(new Die(5, random, Colour.RED), new Coordinates(2, 2));
            pattern = pattern.placeDie(new Die(3, random, Colour.BLUE), new Coordinates(3, 1));
            pattern = pattern.placeDie(new Die(1, random, Colour.GREEN), new Coordinates(3, 0));
            pattern = pattern.placeDie(new Die(1, random, Colour.RED), new Coordinates(1, 3));
            pattern = pattern.placeDie(new Die(2, random, Colour.YELLOW), new Coordinates(0, 3));
            pattern = pattern.placeDie(new Die(5, random, Colour.GREEN), new Coordinates(1, 4));
            pattern = pattern.placeDie(new Die(3, random, Colour.BLUE), new Coordinates(2, 0));
        } catch (PlacementErrorException e) {
            System.out.println("Placement error");
        }
        return pattern;
    }

    /**
     * Fills the draft pool with a dummy matrix.
     *
     * @param game   The game owning the round track.
     * @param random The random used to initialize dice.
     */
    private static void fillRoundTrack(Game game, Random random) {
        game.getRoundTrack().addAllForRound(1, new ArrayList<>(Arrays.asList(
                new Die(6, random, Colour.PURPLE),
                new Die(4, random, Colour.YELLOW))));
        game.getRoundTrack().addAllForRound(2, new ArrayList<>(Arrays.asList(
                new Die(5, random, Colour.RED),
                new Die(3, random, Colour.BLUE),
                new Die(1, random, Colour.GREEN))));
    }

    /**
     * Fills the draft pool with a dummy list.
     *
     * @param game   The game owning the draft pool.
     * @param random The random used to initialize dice.
     */
    private static void fillDraftPool(Game game, Random random) {
        List<Die> dice = new ArrayList<>(Arrays.asList(
                new Die(4, random, Colour.YELLOW),
                new Die(5, random, Colour.RED),
                new Die(3, random, Colour.BLUE),
                new Die(6, random, Colour.PURPLE)));

        game.getDraftPool().setDice(dice);
    }

    /**
     * Creates a more complete game than the ones in {@link GameUtils}, with a real
     * window pattern and 5 placed dice, but where the setUp is not done casually
     * and it is easy to see how it should behave.
     * (For single player configuration).
     * <p>
     * The dice are chosen so that the sum of the values for each colour is always six, so that
     * independently from the Private Objective chosen, the score related to it
     * will always be 6.
     * The {@link PublicObjectiveCard}S are chosen randomly, among a few cards that
     * are in the directory resources. The reason why this method is not in {@link GameUtils}
     * is that to load those cards the loader had to be in the package controller.</p>
     *
     * @return the game
     * @author michelemarzollo
     */
    public static Game getCompleteSinglePlayerGame(Game game, Controller controller) {
        Pattern sunCatcher;
        try {
            String patternDir = "it/polimi/se2018/model/patternSources/sun_catcher/";
            XmlPatternLoader patternLoader = new XmlPatternLoader(patternDir, "patterns.list");
            sunCatcher = patternLoader.load(1)[0];
        } catch (SAXException | IndexOutOfBoundsException e) {
            return null;
        }

        Random random = new Random();
        sunCatcher = fillPattern(sunCatcher, random);

        Player player = new Player("Pippo");
        game.addPlayer(player);
        //sets the public Objective Cards in the game
        try {
            XmlPublicObjectiveLoader publicObjectiveFactory = new XmlPublicObjectiveLoader(controller);
            game.setPublicObjectiveCards(publicObjectiveFactory.load(3));
        } catch (SAXException e) {
            Logger.getDefaultLogger().log("USAXException " + e);
        }
        dealToolCards(game);
        assignPrivateObjectives(player, null, false);

        game.terminateSetup();
        game.start();
        player.setPattern(sunCatcher);

        fillDraftPool(game, random);
        fillRoundTrack(game, random);

        return game;
    }
}
