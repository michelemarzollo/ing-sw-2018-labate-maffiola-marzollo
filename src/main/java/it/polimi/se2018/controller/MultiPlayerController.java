package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.SelectCard;
import it.polimi.se2018.model.events.ViewMessage;

import java.util.*;
import java.util.function.Consumer;

/**
 * The controller for the MultiPlayer configuration. It implements the abstract
 * methods of the class {@link Controller}.
 *
 * @author michelemarzollo
 */
public class MultiPlayerController extends Controller {

    /**
     * The amount of time, counted from the moment the second player was added, to
     * wait before making the game start if there aren't still 4 players (in seconds).
     */
    private int timeOut;

    /**
     * The timer to be used in the lobby, when players are joining the game.
     */
    private Timer lobbyTimer;

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
     * The constructor of the class.
     *
     * @param game       The instance of the class Game, which will describe the situation
     *                   of the match for the whole duration of it.
     * @param timeOut    The amount of time, counted from the moment the last player was added, to
     *                   wait before making the game start if there aren't still 4 players.
     * @param lobbyTimer The timer to be used in the lobby
     */
    public MultiPlayerController(Game game, int timeOut, Timer lobbyTimer) {
        super(game);
        this.timeOut = timeOut;
        this.lobbyTimer = lobbyTimer;
    }

    /**
     * Allows to register custom action handlers. All actions are also done in
     * SinglePlayerConfiguration.
     *
     * @param actions The map containing the name-handler pairs.
     */
    @Override
    protected void registerActions(Map<Action, Consumer<ViewMessage>> actions) {

        actions.put(Action.REGISTER_PLAYER, this::registerPlayer);
        actions.put(Action.SELECT_PATTERN, this::selectPattern);
        actions.put(Action.PLACE_DIE, this::placeDie);
        actions.put(Action.ACTIVATE_TOOL_CARD, this::activateToolCard);
        actions.put(Action.APPLY_TOOL_CARD, this::applyToolCard);
        actions.put(Action.END_TURN, this::endTurn);
        actions.put(Action.DISCONNECT_PLAYER, this::disconnectPlayer);
        actions.put(Action.RECONNECT_PLAYER, this::reconnectPlayer);
        actions.put(Action.CHOOSE_VALUE, this::applyToolCard);
        //Messages that shouldn't be received in MultiPlayerConfiguration
        actions.put(Action.SELECT_DIFFICULTY, this::showError);
        actions.put(Action.SELECT_PRIVATE_OBJECTIVE, this::showError);

    }

    /**
     * Notifies the view when a message which was not valid in the MultiPlayer
     * configuration was sent (used by {@code registerAction}).
     *
     * @param message the message received from the view.
     */
    private void showError(ViewMessage message) {
        message.getView().showError("An invalid message was sent");
    }

    /**
     * The helper method {@code activateToolCard} in {@link Controller} to see
     * if a {@link ToolCard} can be used.
     * <p>
     * For the player to be allowed to use the ToolCard, the game must
     * be in the player's turn, the ToolCard the player wants to use
     * must be among the ones extracted at the beginning of the game,
     * and the player must have enough tokens: to use a ToolCard the
     * player must spend one token if the ToolCard was still never used by
     * someone during the game, two if it was already used.</p>
     *
     * @param message the message (whose type is {@link SelectCard}) that contains
     *                the information of which {@link Player} and which
     *                {@link ToolCard} are involved in the action.
     * @return {@code true} if the ToolCard can be used, {@code false} otherwise.
     */
    @Override
    protected boolean canUseToolCard(ViewMessage message) {
        //If it's the player's turn
        if (canMove(message.getPlayerName())) {
            for (ToolCard toolCard : getGame().getToolCards()) {
                //Checks in the array of the tool cards of the game if there is a tool card with
                //the same name of the name of the event SelectCard.
                if (toolCard.getName().equals(((SelectCard) message).getName()) &&
                        playerHasEnoughTokens(toolCard)) {

                    return (!toolCard.getName().equals("Running Pliers") &&
                            !toolCard.getName().equals("Glazing Hammer")) ||
                            (toolCard.getName().equals("Running Pliers") ?
                                    canUseRunningPliers(message) : canUseGlazingHammer(message));

                }
            }
        } else {
            message.getView().showError("Not your turn!");
        }
        return false;
    }

    //HELPER METHODS FOR canUseToolCard, TO REDUCE IT'S READABILITY COMPLEXITY

    /**
     * Checks if the player has enough tokens to use the {@link ToolCard}.
     *
     * @param toolCard the {@link ToolCard} that wants to be used.
     * @return {@code true} if the ToolCard can be used thanks to the
     * tokens, {@code false} otherwise.
     */
    private boolean playerHasEnoughTokens(ToolCard toolCard) {
        return getGame().getTurnManager().getCurrentTurn()    // checks that the player has enough tokens
                .getPlayer().getTokens() >= 1 + (toolCard.isUsed() ? 1 : 0);
    }

    /**
     * The method that says if the 'Running Pliers' {@link ToolCard} can be used.
     *
     * @param message the message from the view.
     * @return {@code true} if the ToolCard can be used thanks to the
     * turn, {@code false} otherwise.
     */
    private boolean canUseRunningPliers(ViewMessage message) {
        if (getGame().getTurnManager().isSecondTurnAvailable())
            return true;
        else {
            message.getView().showError("The Running Pliers Card can't be" +
                    "used in the second turn.");
            return false;
        }
    }

    /**
     * The method that says if the 'Glazing Hammer' {@link ToolCard} can be used.
     *
     * @param message the message from the view.
     * @return {@code true} if the ToolCard can be used thanks to the
     * turn, {@code false} otherwise.
     */
    private boolean canUseGlazingHammer(ViewMessage message) {
        if (!getGame().getTurnManager().isSecondTurnAvailable())
            return true;
        else {
            message.getView().showError("The Glazing Hammer Card can't be" +
                    "used in the first turn.");
            return false;
        }
    }
    //END OF HELPER METHODS

    /**
     * Extends the method {@code performAction()} to make the {@code turnTimer}
     * start when the action starts. If after a given amount of time
     * the player didn't make the move, the task that begins with the end of
     * the timer makes the player skip the turn.
     *
     * @param message The message generated by the view.
     */
    @Override
    protected void performAction(ViewMessage message) {
        turnTimer.schedule(new EndTurnTask(getGame(), message), (long) turnDuration * 1000);
        super.performAction(message);
        //When the action finished, if the timer is still going on, it is cancelled
        turnTimer.cancel();
    }

    /**
     * The method to select the view that shows the status of the game.
     *
     * @param message The message sent by the view.
     */
    @Override
    protected void displayGame(ViewMessage message) {
        message.getView().showMultiPlayerGame();
    }

    /**
     * The method to return the number of dice that will form the
     * {@link it.polimi.se2018.model.DraftPool} during the whole game;
     * in multi-player configuration is given by the formula: 2 * number of players + 1.
     *
     * @return the number of dice that compose the draftPool.
     */
    @Override
    protected int getDraftAmount() {
        return 2 * getGame().getPlayers().size() + 1;
    }

    /**
     * The method to consume the resources of the Player after having used a ToolCard.
     *
     * @param message the message from the view, that contains the name of the ToolCard.
     */
    @Override
    protected void consumeResources(ViewMessage message) {

        Player player = getGame().getTurnManager().getCurrentTurn().getPlayer();

        for (ToolCard toolCard : getGame().getToolCards()) {
            if (toolCard.getName().equals(((SelectCard) message).getName())) {
                if (toolCard.isUsed())
                    player.setTokens(player.getTokens() - 2);
                else {
                    player.setTokens(player.getTokens() - 1);
                    toolCard.use();
                }
            }
        }

    }

    /**
     * The method to calculate the scores of the players at the end of the game.
     * <p>
     * The score of each player is given by four different elements:
     * (1) The score given by each {@link PublicObjectiveCard} of the game, applied
     * to the player's final {@link it.polimi.se2018.model.Pattern}.
     * (2) The score given by the player's {@link it.polimi.se2018.model.PrivateObjectiveCard},
     * applied to the player's final {@link it.polimi.se2018.model.Pattern} (in
     * MultiPlayer configuration the player has just one PrivateObjectiveCard).
     * (3) One additional point for each unused token.
     * (4) One less point for each unfilled cell in the Player's
     * {@link it.polimi.se2018.model.Pattern}.</p>
     * <p>
     * After calculating the player's score, the method sets to the final score
     * the attribute {@code score} in {@link Player}</p>
     */
    @Override
    protected void calculateScores() {

        for (Player player : getGame().getPlayers()) {

            //The score of one player
            int score = 0;
            //Each PublicObjectiveCard gives an additional score
            for (PublicObjectiveCard publicCard : getGame().getPublicObjectiveCards()) {
                score += publicCard.getScore(player.getPattern().getGrid());
            }
            //The score given by the PrivateObjectiveCard
            score += player.getCards()[0].getScore(player.getPattern().getGrid());
            //The score given by the unused tokens
            score += player.getTokens();
            //The score subtracted by the empty cells
            score -= player.getPattern().emptyCells();

            player.setScore(score);

        }

    }

    /**
     * The method to register a new player to the game before the start of the game.
     * <p>
     * When a new player applies to the game it is registered. The player can
     * also deregister from the game (the behaviour is delegated to
     * {@code disconnectPlayer()} in {@link Controller}). When the players are two, the timer
     * is started and after {@code timeOut} seconds, if there are still at least two
     * players, the game is started. When the number of players is 4 the game is started
     * independently from the timer. If the number of players is less than two
     * the game is not started</p>
     *
     * @param message the message generated by the view, containing the name of the new
     *                player.
     */
    @Override
    protected void registerPlayer(ViewMessage message) {

        //When the method is called there is one player to add
        getGame().addPlayer(new Player(message.getPlayerName()));

        if (getGame().getPlayers().size() == 2) {
            lobbyTimer.schedule(new StartingTask(getGame()), (long) timeOut * 1000);
        }

        if (getGame().getPlayers().size() == 4) {
            lobbyTimer.cancel();
            setUpGame();
        }

    }

    /**
     * The actions to do when the game is finished.
     *
     * @param message The message generated by the view.
     */
    @Override
    protected void endGame(ViewMessage message) {
        cleanDraftPool();   //is it necessary?
        calculateScores();
        fillScoreBoard();
        message.getView().showFinalView();
    }

    /**
     * The method handles the set up of the game: the ObjectiveCards and the
     * Window Pattern Cards are distributed to the players or set in the game.
     */
    private void setUpGame() {
        CardDealer cardDealer = new CardDealer(getGame());
        cardDealer.deal(getGame().getPlayers().size(), 3, 3);
        getGame().terminateSetup();
    }

    /**
     * Disconnects a player.
     * <p>If the game has not yet began, the player is removed from the game,
     * but if the game has already been set up, his connection status is updated
     * to disconnected and won't be able to make moves until he reconnects.</p>
     *
     * @param message The message generated by the view.
     */
    protected void disconnectPlayer(ViewMessage message) {
        String playerName = message.getPlayerName();
        Optional<Player> player = getGame().getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();

        if (getGame().isSetupComplete())
            player.ifPresent(p -> p.setConnected(false));
        else
            player.ifPresent(p -> getGame().removePlayer(p));

    }

    /**
     * Reconnects a disconnected player to the game.
     *
     * @param message The message generated by the view.
     */
    protected void reconnectPlayer(ViewMessage message) {
        String playerName = message.getPlayerName();
        getGame().getPlayers().stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst()
                .ifPresent(player -> player.setConnected(true));
    }

    /**
     * The class to implement the task for the Timer in {@code registerPlayer()}.
     *
     * @author michelemarzollo
     */
    private class StartingTask extends TimerTask {

        /**
         * The game that contains the players and that must be started at the
         * end of the timer.
         */
        private Game game;

        /**
         * The constructor of the inner class.
         *
         * @param game the game to start.
         */
        StartingTask(Game game) {
            this.game = game;
        }

        /**
         * The task to be scheduled when the Timer stops:
         * if the number of players of the game is less than two the method
         * doesn't do anything, otherwise it starts the game.
         */
        @Override
        public void run() {

            if (game.getPlayers().size() > 1)
                setUpGame();

        }
    }

    /**
     * The task to end the turn when the turnTimer ends because the player
     * spent to much time to make the move.
     */
    private class EndTurnTask extends TimerTask {

        /**
         * The game where the {@code turnTimer} is running.
         */
        private Game game;

        /**
         * The message form the view. It's used to call eventually {@code endGame}.
         */
        private ViewMessage message;

        /**
         * The constructor of the inner class.
         *
         * @param game the game where the {@code turnTimer} is running.
         */
        EndTurnTask(Game game, ViewMessage message) {
            this.game = game;
            this.message = message;
        }

        /**
         * The action to do when the {@code turnTimer} finishes: the turn must
         * be updated. If there is no player who has an available turn during
         * the round, the round is updated. In this case, if the round was the
         * last one the game is stopped.
         */
        @Override
        public void run() {
            boolean hadSuccess = game.getTurnManager().updateTurn();
            if (!hadSuccess) {
                try {
                    game.getTurnManager().setupNewRound();
                } catch (TurnManager.GameFinishedException e) {
                    endGame(message);
                }
            }
        }
    }
}
