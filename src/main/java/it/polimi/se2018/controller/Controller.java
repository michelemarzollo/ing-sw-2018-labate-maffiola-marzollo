package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.PlaceDie;
import it.polimi.se2018.model.events.SelectCard;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.Observer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Base abstract class for game controllers.
 * <p>The class contains the basic game logic and some common
 * moves players can do.</p>
 * <p>Concrete subclasses have to implement the abstract methods
 * which allow to tweak the behavior of the above operations.</p>
 * <p>Subclasses can override the method {@code registerActions} to define
 * how the controller will react when a specified view event occurs.</p>
 * <p>Similarly, overriding the method {@code registerToolCardBehaviors}
 * custom tool cards can be registered.</p>
 * <p>This class is an {@code Observable<ViewMessage>} and its instances
 * have to be registered as observer to all the {@code Observable<ViewMessage>}
 * that are handled by that instance.</p>
 * <p>This class has no thread-safety yet.</p>
 */
public abstract class Controller implements Observer<ViewMessage> {

    /**
     * The game the controller is bound to.
     */
    private Game game;

    /**
     * The dictionary which associates tool card names
     * to their behavior.
     */
    private final Map<String, ToolCardBehaviour> toolCardBehaviors;

    /**
     * Map used to resolve requests by mapping each action to its
     * handler.
     */
    private final Map<Action, Consumer<ViewMessage>> actionMap;

    /**
     * The amount of time to wait before making the turn skip although
     * the layer didn't make the move.
     */
    private int turnDuration;

    /**
     * The timer that handles the termination of the turn if the time to make the
     * move finished.
     */
    private Timer turnTimer;

    /**
     * The list of objects that contain the method to calculate the score of
     * {@link PublicObjectiveCard}s.
     */
    private List<PublicObjectiveScore> publicScoreCalculators = new ArrayList<>();

    /**
     * Tells if the given tool card can be used.
     *
     * @param message  The message generated by the view.
     * @param toolCard the {@link ToolCard} to use.
     * @return {@code true} if the specified tool card can be used;
     * {@code false} otherwise.
     */
    protected abstract boolean canUseToolCard(ViewMessage message, ToolCard toolCard);

    /**
     * Displays the correct game view.
     *
     * @param message The message sent by the view.
     */
    protected abstract void displayGame(ViewMessage message);

    /**
     * Specifies how many dice have to be drafted when a new round is
     * set up.
     *
     * @return The amount of dice to be drafted at the beginning of a
     * new round.
     */
    protected abstract int getDraftAmount();


    /**
     * Consumes resources after the usage of a tool card.
     *
     * @param message The message sent by the view.
     */
    protected abstract void consumeResources(ViewMessage message);

    /**
     * Computes the score for each player.
     */
    protected abstract void calculateScores();

    /**
     * Register a new player in the game.
     * <p>When the maximum number of players is reached, it starts the game.</p>
     *
     * @param message The message generated by the view.
     */
    protected abstract void registerPlayer(ViewMessage message);

    /**
     * Disconnects a player.
     * <p>If the game has not yet began, the player is removed from the game,
     * but if the game has already been set up, his connection status is updated
     * to disconnected and won't be able to make moves until he reconnects.</p>
     *
     * @param message The message generated by the view.
     */
    protected abstract void disconnectPlayer(ViewMessage message);

    /**
     * Allows to register custom action handlers.
     *
     * @param actions The map containing the name-handler pairs.
     */
    protected abstract void registerActions(Map<Action, Consumer<ViewMessage>> actions);

    /**
     * Handles the end of the game.
     * <p>When the game ends, the draft pool is cleaned, the final
     * scores computed and the view is informed to display the
     * score board.</p>
     *
     * @param message The message generated by the view.
     */
    protected abstract void endGame(ViewMessage message);

    /**
     * Creates a new controller associated to the specified game instance.
     *
     * @param game         The game to be bound to the controller.
     * @param turnDuration the maximum amount of time allowed for the player to make the move,
     *                     before the turn is automatically skipped.
     */
    protected Controller(Game game, int turnDuration) {
        this.game = game;
        toolCardBehaviors = new HashMap<>();
        registerToolCardBehaviors(toolCardBehaviors);
        turnTimer = new Timer("Turn timer");
        this.turnDuration = turnDuration;

        actionMap = new EnumMap<>(Action.class);
        registerActions(actionMap);
    }

    /**
     * Adds a new class to calculate the score of a {@link PublicObjectiveCard}
     * at the end of the game.
     * In the model there is the corresponding {@link PublicObjectiveCard}.
     *
     * @param scoreStrategy the class to calculate the score related to a certain card.
     */
    protected void addPublicScoreStrategy(PublicObjectiveScore scoreStrategy) {
        publicScoreCalculators.add(scoreStrategy);
    }

    /**
     * The getter for {@code publicScoreCalculators}.
     *
     * @return a copy of {@code publicScoreCalculators}.
     */
    protected List<PublicObjectiveScore> getPublicScoreCalculators() {
        return new ArrayList<>(publicScoreCalculators);
    }

    /**
     * Allows to register custom tool card behaviors.
     *
     * @param behaviors The map containing the name-behavior pairs.
     */
    private void registerToolCardBehaviors(Map<String, ToolCardBehaviour> behaviors) {
        behaviors.put("Grozing Pliers", new AlterDieValueBehaviour(true));
        behaviors.put("Eglomise Brush",
                new MoveDiceBehaviour(1, Restriction.ONLY_VALUE));
        behaviors.put("Copper Foil Burnisher",
                new MoveDiceBehaviour(1, Restriction.ONLY_COLOUR));
        behaviors.put("Lathekin", new MoveDiceBehaviour(2, Restriction.DEFAULT));
        behaviors.put("Lens Cutter", new SwapDiceBehaviour());
        behaviors.put("Flux Brush", new ReRollDieBehaviour());
        behaviors.put("Glazing Hammer", new ReRollDraftPoolBehaviour());
        behaviors.put("Running Pliers",
                new PlaceDieBehaviour(true, Restriction.DEFAULT));
        behaviors.put("Cork-backed Straightedge",
                new PlaceDieBehaviour(false, Restriction.NOT_ADJACENT));
        behaviors.put("Grinding Stone", new AlterDieValueBehaviour(false));
        behaviors.put("Flux Remover", new PullAgainAndPlaceBehaviour());
        behaviors.put("Tap Wheel", new MoveSomeDiceBehaviour());
    }

    /**
     * Detects what action {@code message} refers to and delegates the
     * registered method to handle it.
     *
     * @param message The message generated by the view.
     */
    protected void performAction(ViewMessage message) {
        Consumer<ViewMessage> action = actionMap.get(message.getAction());
        if (action != null)
            action.accept(message);
    }

    /**
     * Getter for game.
     *
     * @return The game the controller is bound to.
     */
    protected Game getGame() {
        return game;
    }

    /**
     * Entry point for events generated by the view.
     *
     * @param message The message generated by the view.
     */
    public final void update(ViewMessage message) {
        performAction(message);
    }

    /**
     * Finds a player among the ones in the game, according to it's name.
     *
     * @param playerName the player's name.
     * @return the {@link Player}.
     */
    protected Optional<Player> findPlayer(String playerName) {
        return getGame().getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();

    }

    /**
     * Handles the end of the current turn and makes the game progress.
     * <p>If the current round has finished, it also starts a new one.</p>
     *
     * @param message The message generated by the view.
     */
    protected synchronized void endTurn(ViewMessage message) {
        if (!canMove(message.getPlayerName()))
            return;
        turnTimer.cancel();
        boolean updateSuccessful = getGame().getTurnManager().updateTurn();
        if (updateSuccessful) {
            turnTimer = new Timer("TurnTimer");
            turnTimer.schedule(new EndTurnTask(message), (long) turnDuration * 1000);
        } else
            endRound(message);
    }

    /**
     * Tells if no more actions can be performed in the current turn.
     *
     * @return {@code true} if the current turn has no more possible
     * actions and can be safely ended; {@code false} otherwise.
     */
    protected boolean checkTurnEnd() {
        Turn currentTurn = getGame().getTurnManager().getCurrentTurn();
        return currentTurn.hasAlreadyPlacedDie() && currentTurn.hasAlreadyUsedToolCard();
    }

    /**
     * Handles the end of a round.
     * <p>If the game has finished, calls {@code endGame} to finalize
     * the match, otherwise sets up a new round and updates the current
     * turn.</p>
     *
     * @param message The message generated by the view.
     */
    protected void endRound(ViewMessage message) {
        try {
            cleanDraftPool();
            getGame().getTurnManager().setupNewRound();
            refillDraftPool();
            getGame().getTurnManager().updateTurn();
            turnTimer = new Timer("TurnTimer");
            turnTimer.schedule(new EndTurnTask(message), (long) turnDuration * 1000);
        } catch (TurnManager.GameFinishedException e) {
            endGame(message);
        }
    }

    /**
     * Cleans the draft pool by moving leftovers to the round track.
     */
    protected void cleanDraftPool() {
        List<Die> leftovers = getGame().getDraftPool().getDice();
        int round = getGame().getTurnManager().getRound();
        getGame().getRoundTrack().addAllForRound(round, leftovers);
        getGame().getDraftPool().setDice(new ArrayList<>());
    }

    /**
     * Refills the draft pool with the dice drafted from the dice bag.
     * <p>The amount of dice drafted is specified by {@code getDraftAmount()}</p>
     */
    protected void refillDraftPool() {
        getGame().getDraftPool().setDice(
                getGame().getDiceBag().draft(getDraftAmount())
        );
    }

    /**
     * Helper method to check if the specified player can move this
     * turn.
     *
     * @param playerName The name of the player to be checked.
     * @return {@code true} if the player with the given name can move;
     * {@code false} otherwise.
     */
    protected boolean canMove(String playerName) {
        Turn currentTurn = getGame().getTurnManager().getCurrentTurn();
        return currentTurn.getPlayer().getName().equals(playerName);
    }

    /**
     * Places a die in the pattern of the player who made the move.
     * <p>It allows only the player who can currently move to perform a
     * placement. If any other player tries to make a move, he's notified
     * to wait.</p>
     * <p>If, after the placement, the current player can't make any other move,
     * his turn is automatically terminated.</p>
     *
     * @param message The message generated by the view.
     */
    protected void placeDie(ViewMessage message) {
        PlaceDie placeMessage = (PlaceDie) message;

        if (!canMove(placeMessage.getPlayerName())) {
            placeMessage.getView().showError("Not your turn!");
            return;
        }

        Turn currentTurn = getGame().getTurnManager().getCurrentTurn();
        if (currentTurn.hasAlreadyPlacedDie()) {
            placeMessage.getView().showError("You already placed a die!");
            return;
        }

        executePlacement((PlaceDie) message);
        if (checkTurnEnd())
            endTurn(message);
    }

    /**
     * Executes the placement according to the data stored in the message.
     *
     * @param message The message sent by the view.
     */
    private void executePlacement(PlaceDie message) {
        Turn currentTurn = getGame().getTurnManager().getCurrentTurn();
        try {
            Die die = getGame().getDraftPool().select(getDieIndex(message));
            // place die
            Pattern currentPattern = currentTurn.getPlayer().getPattern();
            Pattern newPattern = currentPattern.placeDie(die, message.getDestination());
            currentTurn.getPlayer().setPattern(newPattern);

            getGame().getDraftPool().draft(getDieIndex(message));
            currentTurn.placeDie();
        } catch (IndexOutOfBoundsException e) {
            message.getView().showError("Invalid selection!");
        } catch (PlacementErrorException e) {
            message.getView().showError(
                    "Placement doesn't respect restrictions!\n" + e.getMessage()
            );
        }
    }

    /**
     * Helper method for {@code placeDie} to get the right index in {@link DraftPool}
     * when placing a die.
     *
     * @param placeMessage The message passed by {@code placeDie} and
     *                     that contains the index inserted by the player.
     * @return The index inserted by the player if there are no restriction on the
     * choice or if it is equal to the forced index, -1 otherwise (the index inserted
     * differs from the forced one).
     */
    private int getDieIndex(PlaceDie placeMessage) {
        int forcedIndex = game.getTurnManager().getCurrentTurn().getForcedSelectionIndex();
        if (forcedIndex != -1) {
            if (forcedIndex == placeMessage.getDieIndex())
                return forcedIndex;
            else
                return -1;
        } else //There is no forced selection on the die
            return placeMessage.getDieIndex();
    }

    /**
     * Asks the view to gather the parameters to use a given tool card.
     * <p>The method selects the correct behavior and delegates to it
     * the handling of parameter request.</p>
     *
     * @param message The message generated by the view.
     */
    protected void activateToolCard(ViewMessage message) {
        SelectCard selectMessage = (SelectCard) message;
        if (!canMove(message.getPlayerName())) {
            selectMessage.getView().showError("Not your turn!");
            return;
        }
        ToolCard selectedToolCard = Arrays.stream(getGame().getToolCards())
                .filter(t -> t.getName().equals(selectMessage.getName()))
                .findFirst()
                .orElse(null);

        if (selectedToolCard == null) {
            selectMessage.getView().showError("The tool card doesn't exist.");
            return;
        }
        ToolCardBehaviour behaviour = toolCardBehaviors.get(selectMessage.getName());
        if (!behaviour.areRequirementsSatisfied(game)) {
            selectMessage.getView().showError("You can't use this tool card now.");
            return;
        }
        if (canUseToolCard(selectMessage, selectedToolCard)) {
            getGame().getTurnManager().getCurrentTurn().setSelectedToolCard(selectedToolCard);
            behaviour.askParameters(selectMessage);
        }
    }

    /**
     * Applies the effect of the given tool card.
     * <p>The method selects the correct behavior and delegates to it
     * the actual application of the effect to the model.</p>
     * <p>If, after the placement, the current player can't make any other move,
     * his turn is automatically terminated.</p>
     *
     * @param message The message generated by the view.
     */
    protected void applyToolCard(ViewMessage message) {
        if (!canMove(message.getPlayerName())) {
            message.getView().showError("Not your turn");
            return;
        }

        if (game.getTurnManager().getCurrentTurn().getSelectedToolCard() == null) {
            message.getView().showError("No toolCard has been activated yet");
            return;
        }

        applyBehaviour(message);
        getGame().getTurnManager().getCurrentTurn().setSacrificeIndex(-1);

        if (checkTurnEnd())
            endTurn(message);
    }

    /**
     * Applies the effect of the active tool card.
     *
     * @param message The message sent by the view.
     */
    private void applyBehaviour(ViewMessage message) {
        ToolCardBehaviour behavior =
                toolCardBehaviors.get(game.getTurnManager().getCurrentTurn().getSelectedToolCard().getName());

        if (behavior != null) {
            boolean success = behavior.useToolCard(getGame(), message);
            if (success) {
                getGame().getTurnManager().getCurrentTurn().useToolCard();
                consumeResources(message);
            }
        }
    }

    /**
     * Handles the selection of a pattern among candidates by a player.
     * <p>If the specified player or pattern is invalid, the view is notified
     * of the error and nothing else happens. Otherwise, the player is updated
     * to use the given pattern.</p>
     * <p>After the assignment, it's checked if all the players have already
     * choose their pattern and, if so, the game starts.</p>
     *
     * @param message The message generated by the view.
     */
    protected void selectPattern(ViewMessage message) {
        SelectCard selectMessage = (SelectCard) message;

        Optional<Player> maybePlayer = findPlayer(selectMessage.getPlayerName());
        if (!maybePlayer.isPresent()) {
            selectMessage.getView().showError("Invalid player!");
            return;
        }
        Player player = maybePlayer.get();

        String patternName = selectMessage.getName();
        Pattern pattern = Arrays.stream(player.getCandidates())
                .filter(p -> p.getName().equals(patternName))
                .findFirst()
                .orElse(null);

        if (pattern == null) {
            selectMessage.getView().showError("Invalid pattern!");
            return;
        }

        player.setTokens(pattern.getDifficulty());
        player.setPattern(pattern);
        displayGame(message); //selectGameView

        tryStartGame(message);
    }

    /**
     * Starts the game if all player have chosen their pattern.
     *
     * @param message The message sent by the view.
     */
    private void tryStartGame(ViewMessage message) {
        boolean gameReady = getGame().getPlayers().stream()
                .allMatch(p -> p.getPattern() != null);

        if (gameReady) {
            getGame().start();
            getGame().getTurnManager().updateTurn();
            refillDraftPool();
            turnTimer.schedule(new EndTurnTask(message), (long) turnDuration * 1000);
        }
    }

    /**
     * Fills the score board.
     */
    protected void fillScoreBoard() {
        List<Player> scoreboard = getGame().getPlayers().stream()
                .sorted((player1, player2) -> player2.getScore() - player1.getScore())
                .collect(Collectors.toList());
        getGame().setScoreBoard(scoreboard); //this invocation in Game generates the GAME_END message
    }

    /**
     * Let's the reference to the game fall, to allow the Garbage Collector
     * to get rid of it.
     */
    protected void finalizeMatch() {
        game.deregisterAll();
        turnTimer.cancel();
    }

    /**
     * The task to end the turn when the turnTimer ends because the player
     * spent to much time to make the move.
     */
    private class EndTurnTask extends TimerTask {

        /**
         * The message form the view. It's used to call {@code endTurn}.
         */
        private ViewMessage message;

        /**
         * The constructor of the inner class.
         *
         * @param message the message of the view.
         */
        EndTurnTask(ViewMessage message) {
            this.message = message;
        }

        /**
         * The action to do when the {@code turnTimer} finishes: the turn must
         * be updated.
         */
        @Override
        public void run() {
            endTurn(message);
        }
    }

    /**
     * Tells if the controller can accept other new players.
     *
     * @return {@code true} if it can accept new players; {@code false} otherwise.
     */
    boolean acceptsNewPlayers() {
        return !getGame().isSetupComplete();
    }

}
