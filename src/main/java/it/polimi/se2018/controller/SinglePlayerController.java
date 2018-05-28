package it.polimi.se2018.controller;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.events.*;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;


/**
 * This class extends the abstract {@link Controller} class and
 * implements those behaviors that are different in Single Player
 * and MultiPlayer mode: the main differences are in the management
 * of the connection/reconnection of the Player, in the game's setting up,
 * in the {@link ToolCard}'s usage and in the end game scoring.
 *
 * @author giorgiolabate
 */


public class SinglePlayerController extends Controller {

    /**
     * Timer that is used to keep the Game 'alive' for
     * a predefined {@code timeout} when the Player disconnects.
     */
    private Timer timer;

    /**
     * Amount of time (in seconds) for which the Controller
     * has to wait when the Player disconnects. At the end
     * of this period the {@link Game} is canceled.
     */
    private int timeOut;


    /**
     * Creates a new controller for the Single Player mode
     * associated to the specified game instance.
     * @param game The game that has to be bound
     *             to the controller.
     * @param timer The Timer associated to the Controller.
     * @param timeOut Period that has to be waited keeping the current {@link Game}
     *                still valid and after which the {@link Game} has to be
     *                deleted.
     */
    public SinglePlayerController(Game game, Timer timer, int timeOut) {
        super(game);
        this.timer = timer;
        this.timeOut = timeOut;
    }


    /**
     * Allows to register custom action handlers. It registers
     * all the actions common to both SinglePlayer mode and multiPlayer
     * and adds the typical SinglePlayer mode's actions.
     * @param actions The map containing the name-handler pairs.
     */
    @Override
    protected void registerActions(Map<Action, Consumer<ViewMessage>> actions) {
        actions.put(Action.REGISTER_PLAYER, this::registerPlayer);
        //The setup is available only when the Controller receives the level of difficulty.
        actions.put(Action.SELECT_DIFFICULTY, this::setUpGame);
        actions.put(Action.SELECT_PATTERN, this::selectPattern);
        actions.put(Action.PLACE_DIE, this::placeDie);
        actions.put(Action.ACTIVATE_TOOL_CARD, this::activateToolCard);
        actions.put(Action.APPLY_TOOL_CARD, this::applyToolCard);
        actions.put(Action.END_TURN, this::endTurn);
        actions.put(Action.DISCONNECT_PLAYER, this::disconnectPlayer);
        actions.put(Action.RECONNECT_PLAYER, this::reconnectPlayer);
        actions.put(Action.SELECT_PRIVATE_OBJECTIVE, this::selectPrivateObjective);
    }

    /**
     * Register the new player in the {@link Game}.
     * After the registration, it selects the {@link it.polimi.se2018.view.View}
     * asking the player the difficulty he wishes to play.
     * @param message The message generated by the view
     *                that contains the Player's name.
     */
    @Override
    protected void registerPlayer(ViewMessage message) {
        getGame().addPlayer(new Player(message.getPlayerName()));
        message.getView().showDifficultySelection();
    }

    /**
     * Handles the setUp of the {@link Game} creating a {@link CardDealer}
     * and dealing the right number of each type of card for
     * the SinglePlayer's mode: 3 {@link PublicObjectiveCard},
     * 2 {@link PrivateObjectiveCard} and a number of
     * {@link ToolCard} that is equal to the level of difficulty
     * chosen by the player.
     * @param message The message generated by the view
     *                that contains the level of difficulty
     *                selected by the Player.
     */

    private void setUpGame(ViewMessage message) {
        SelectDifficulty difficulty = (SelectDifficulty) message;
        if (difficulty.getDifficulty() < 1 || difficulty.getDifficulty() > 5){
            message.getView().showError("The level of difficulty must be in 1-5 range: choose another one");
            message.getView().showDifficultySelection();
        }
        else {
            CardDealer cardDealer = new CardDealer(getGame());
            //In singlePlayer mode the Player has two privateObjectiveCards and
            //the number of ToolCards depend on the level of difficulty chosen.
            cardDealer.deal(3, 2, difficulty.getDifficulty());
            getGame().terminateSetup();
            //now to begin the Game the only missing thing is the choice of a Pattern.
        }
    }


    /**
     * Handles the end of the game.
     * When the game ends, the {@link DraftPool} is cleaned
     * and the view is informed to display the selection between the
     * two {@link PrivateObjectiveCard} for the Player. The end game
     * scoring and the management of the scoreBoard are delegated
     * to the {@code selectPrivateObjective} method: they require
     * the choice of the {@link PrivateObjectiveCard} by the Player
     * to ensure a correct execution.
     * @param message The event generated by the
     *                {@link it.polimi.se2018.view.View} that
     *                encapsulates the necessary data for
     *                the operation.
     **/
    @Override
    protected void endGame(ViewMessage message) {
        cleanDraftPool();
        message.getView().showPrivateObjectiveSelection();
    }

    /**
     * Handles the player's choice between his two
     * {@link PrivateObjectiveCard}: the player has to choose
     * between his two {@link PrivateObjectiveCard} at the end
     * of the game and the game scoring depends on this choice.
     * This method place the chosen card at the first position (0)
     * of the {@link Player}'s array of {@link PrivateObjectiveCard}.
     * It also creates a {@link Player} for the {@link RoundTrack} and adds
     * it to the array of Players in the {@link Game}: it is necessary
     * for a right execution of {@code calculateScores} and
     * {@code fillScoreBoard} that are supposed to find the
     * {@link RoundTrack} at the second position (1) in the array of
     * players and are executed right after a correct {@link PrivateObjectiveCard}
     * selection.
     *
     * @param message The message generated by the view that
     *                contains the colour of the card chosen.
     */
    private void selectPrivateObjective(ViewMessage message) {
        SelectPrivateObjective selectPrivateObjective = (SelectPrivateObjective) message;
        //Colour of the chosen PrivateObjectiveCard.
        Colour colour = selectPrivateObjective.getColour();
        Player player = getGame().getPlayers().get(0);
        for (PrivateObjectiveCard card : getGame().getPlayers().get(0).getCards()) {
            if (card.getColour().equals(colour)) {
                player.getCards()[0] = card;
                getGame().addPlayer(new Player("RoundTrack"));
                calculateScores();
                fillScoreBoard();
                message.getView().showScoreBoard();
                return;
            }
        }
        selectPrivateObjective.getView().showError("The chosen private objective card is not available!");
    }

    /**
     * Calculate the scores of the two players: the user
     * and the {@link RoundTrack}. It is calculated
     * following the game's rules, the only difference
     * in SinglePlayer mode is that the Player loses 3
     * point (in stead of 1) for each open space on his grid.
     */
    @Override
    protected void calculateScores() {
        int publicObjectiveScore = 0;
        int privateObjectiveScore;
        int emptySpacePenalty;
        Cell[][] pattern = getGame().getPlayers().get(0).getPattern().getGrid();
        for (PublicObjectiveCard card : getGame().getPublicObjectiveCards()) {
            publicObjectiveScore += card.getScore(pattern);
        }
        //The chosen PrivateObjectiveCard is placed in the first position of the array (0).
        privateObjectiveScore = getGame().getPlayers().get(0).getCards()[0].getScore(pattern);
        emptySpacePenalty = 3 * getGame().getPlayers().get(0).getPattern().emptyCells();
        //Player in position 0 is the user.
        getGame().getPlayers().get(0).
                setScore(publicObjectiveScore + privateObjectiveScore - emptySpacePenalty);
        //Player in position 1 is the RoundTrack.
        getGame().getPlayers().get(1).
                setScore(getGame().getRoundTrack().getSum());
    }


    /**
     * This method is used by the {@code activateToolCard}
     * to verify that the usage of the {@link ToolCard} indicated in
     * {@code message} is allowed.
     * The toolCard won't be available in solo mode in various
     * situations: it is not the Turn of the Player who made the
     * request; the indicated Die's Colour in the DraftPool doesn't match
     * the ToolCard's Colour; the ToolCard has been already used;
     * the ToolCard doesn't exists.
     * @param message The event generated by the
     *                {@link it.polimi.se2018.view.View} that
     *                encapsulates the necessary data for
     *                the operation (name of the {@link ToolCard} and
     *                index of the Die in the {@link DraftPool} that
     *                Player wishes to spend for the action).
     * @return {@code true} if it is possible to use the
     * ToolCard indicated in the {@code message}, {@code false}
     * otherwise.
     */
    @Override
    protected boolean canUseToolCard(ViewMessage message, ToolCard toolCard) {
        SelectCardSP selectionMessage = (SelectCardSP) message;
        int dieIndex = selectionMessage.getDieIndex();
        String playerName = message.getPlayerName();
        Turn currentTurn = getGame().getTurnManager().getCurrentTurn();
        if (!currentTurn.getPlayer().getName().equals(playerName)) {
            selectionMessage.getView().showError("Not your turn!");
            return false;
        }
        for (ToolCard card : super.getGame().getToolCards()) {
            if (selectionMessage.getName().equals(card.getName())) {
                if (!card.isUsed()) {
                    if (super.getGame().getDraftPool().getDice().get(dieIndex).getColour().
                            equals(card.getColour())) {
                        getGame().getTurnManager().getCurrentTurn().
                                setSacrificeIndex(dieIndex);
                        return true;
                    } else {
                        selectionMessage.getView().showError("The requested die doesn't match the toolcard's colour.");
                        return false;
                    }
                } else {
                    //The toolCard has been already used.
                    selectionMessage.getView().showError("The toolcard has already been used.");
                    return false;
                }
            }
        }
        //If the loop terminates no corresponding ToolCard to the one requested has been found.
        selectionMessage.getView().showError("The toolcard doesn't exists.");
        return false;
    }

    /**
     * Show the correct view for Single Player mode.
     * @param message The message sent by the view.
     */
    @Override
    protected void displayGame(ViewMessage message) {
        message.getView().showSinglePlayerGame();
    }

    /**
     * This method tells the number of dice
     * that have to be drafted: it is useful
     * in the setting up of each new Round.
     *
     * @return the number of dice that has to be
     * drafted at the beginning of each Round: in single
     * Player mode they are always 4.
     */
    @Override
    protected int getDraftAmount() {
        return 4;
    }

    /**
     * This method eliminates the selected Die for the {@link ToolCard}'s usage
     * that is associated to the actual {@link Turn}.
     */
    @Override
    protected void consumeResources(ViewMessage message) {
        int dieIndex = getGame().getTurnManager().getCurrentTurn().getSacrificeIndex();
        getGame().getDraftPool().draft(dieIndex);
    }

    /**
     * This inner class is necessary in the case in which the Player disconnects
     * and the timer expires: the run method contains the actions that has to be
     * performed in that moment.
     */
    private class EndGameTask extends TimerTask{
        @Override
        public void run() {
            //@TODO invoke finalizeMatch methow that will be in the abstract controller 
        }
    }

    /**
     * This method receives a message of {@code DISCONNECT_PLAYER} and
     * it starts timer and maintains the actual {@link Game} instance still available
     * for timeout(ms)*1000 seconds, then it destroys it.
     * @param message The message generated by the view.
     */
    @Override
    protected void disconnectPlayer(ViewMessage message) {
        timer.schedule(new EndGameTask(), (long) timeOut*1000);//faccio partire il timer
        getGame().getPlayers().get(0).setConnected(false);
    }

    /**
     * This method receives a message of {@code RECONNECT_PLAYER} and
     * it interrupts the timer because the Player has reconnected within
     * the established period of time.
     * @param message The message generated by the view.
     */
    @Override
    protected void reconnectPlayer(ViewMessage message) {
        if(message.getPlayerName().equals(getGame().getPlayers().get(0).getName())) {
            timer.cancel();
            getGame().getPlayers().get(0).setConnected(true);
        }
        else{
            message.getView().showError("Your username doesn't match with the actual player's username");
        }
    }


}