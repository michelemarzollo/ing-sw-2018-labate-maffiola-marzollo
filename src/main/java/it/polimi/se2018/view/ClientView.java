package it.polimi.se2018.view;

import it.polimi.se2018.model.events.*;
import it.polimi.se2018.model.viewmodel.ViewDataOrganizer;
import it.polimi.se2018.networking.client.Client;
import it.polimi.se2018.networking.client.NetworkHandlerFactory;
import it.polimi.se2018.networking.server.ServerNetInterface;
import it.polimi.se2018.utils.Coordinates;

/**
 * This class represents the client-side view.
 */
public class ClientView extends View {
    /**
     * The display system the class uses for data representation.
     */
    private final Displayer displayer;

    /**
     * The data organizer used to keep track of model messages.
     */
    private final ViewDataOrganizer organizer;

    /**
     * The client the view is bound to.
     */
    private Client client;

    /**
     * Creates a ClientView instance that uses the given Displayer to
     * represent data on screen.
     *
     * @param displayer The displayer user to represent data on screen.
     */
    public ClientView(Displayer displayer) {
        organizer = new ViewDataOrganizer();
        this.displayer = displayer;
        //e il displayer non avrà bisogno del riferimento all'organizer, ma potrà accedervi tramite questa classe
        this.displayer.setView(this);
        displayer.displayLoginView();
    }

    /**
     * The getter for {@code organizer}.
     *
     * @return the organizer.
     */
    public ViewDataOrganizer getDataOrganizer() {
        return organizer;
    }

    /**
     * Shows the main multi player board.
     */
    @Override
    public void showMultiPlayerGame() {
        displayer.displayMultiPlayerGame();
    }

    /**
     * Shows the main single player board.
     */
    @Override
    public void showSinglePlayerGame() {
        displayer.displaySinglePlayerGame();
    }

    /**
     * Shows an error message.
     *
     * @param error the message that has to be displayed
     */
    @Override
    public void showError(String error) {
        displayer.displayError(error);
    }

    /**
     * Shows the pattern selection menu.
     */
    @Override
    public void showPatternSelection() {
        displayer.askPattern();
    }

    /**
     * Shows the private objective selection menu.
     */
    @Override
    public void showPrivateObjectiveSelection() {
        displayer.askPrivateObjective();
    }

    /**
     * Shows the score board.
     */
    @Override
    public void showScoreBoard() {
        displayer.displayScoreBoard();
    }

    /**
     * Shows the die selection view.
     */
    @Override
    public void showDieSelection() {
        displayer.selectDie();
    }

    /**
     * Shows the die increment view.
     */
    @Override
    public void showDieIncrementSelection() {
        displayer.askIncrement();
    }

    /**
     * Shows the move selection view.
     *
     * @param amount The amount of dice to move.
     */
    @Override
    public void showMoveSelection(int amount) {
        displayer.moveDice(amount, true);
    }

    /**
     * Shows the view to move up to two dice.
     */
    @Override
    public void showMoveUpToTwo() {
        displayer.moveDice(2, false);
    }

    /**
     * Shows the difficulty selection menu.
     */
    @Override
    public void showDifficultySelection() {
        displayer.askDifficulty();
    }

    /**
     * Shows the dice swap view.
     */
    @Override
    public void showLensCutterSelection() {
        displayer.askDiceToSwap();
    }

    /**
     * Shows the view to select the die value and place it.
     */
    @Override
    public void showValueDestinationSelection() {
        displayer.askValueDestination();
    }

    /**
     * Shows the view to place a die.
     */
    @Override
    public void showPlaceDie() {
        displayer.askPlacement();
    }

    /**
     * Shows a confirmation view.
     */
    @Override
    public void showConfirm() {
        displayer.askConfirm();
    }

    /**
     * Updates the displayed data.
     *
     * @param message An object containing the data to define
     */
    @Override
    public void update(ModelUpdate message) {
        organizer.push(message);
        if(message.getEventType() == ModelEvent.GAME_SETUP)
            showPatternSelection();
        if (organizer.getScoreBoard() != null)
            showScoreBoard();
        displayer.refreshDisplayedData();
    }

    /**
     * Handles the event in which the player wants to place a die.
     *
     * @param index       The index of the die from the draft pool.
     * @param destination The destination of the die on the pattern.
     */
    public void handlePlacement(int index, Coordinates destination) {
        notifyObservers(new PlaceDie(
                index,
                destination,
                this,
                Action.PLACE_DIE,
                getPlayerName())
        );
    }

    /**
     * Handles the event in which the player selects a tool card.
     *
     * @param name The name of the tool card.
     */
    public void handleToolCardSelection(String name) {

        notifyObservers(new SelectCard(
                name,
                this,
                Action.ACTIVATE_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player selects a tool card in single player mode.
     *
     * @param name      The name of the tool card.
     * @param sacrifice The index of the die to be sacrificed to use the tool card.
     */
    public void handleToolCardSelection(String name, int sacrifice) {
        notifyObservers(new SelectCardSP(
                name,
                this,
                Action.ACTIVATE_TOOL_CARD,
                getPlayerName(),
                sacrifice
        ));
    }

    /**
     * Handles the event in which the player ends its turn manually.
     */
    public void handleEndTurn() {
        notifyObservers(new ViewMessage(this, Action.END_TURN, getPlayerName()));
    }

    /**
     * Handles the event in which the player selects its pattern among candidates.
     *
     * @param patternName The name of the pattern.
     */
    public void handlePatternSelection(String patternName) {
        notifyObservers(new SelectCard(
                patternName,
                this,
                Action.SELECT_PATTERN,
                getPlayerName())
        );

    }

    /**
     * Handles the event in which the player wants to use a tool card.
     */
    public void handleToolCardUsage() {
        notifyObservers(new ViewMessage(
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     *
     * @param index     The index of the die from the draft pool.
     * @param increment {@code true} if the die value has to be incremented;
     *                  {@code false} if it has to be decremented.
     */
    public void handleToolCardUsage(int index, boolean increment) {
        notifyObservers(new IncrementDieValue(
                index,
                increment,
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     *
     * @param index The index of the die on the draft pool.
     */
    public void handleToolCardUsage(int index) {
        notifyObservers(new SelectDie(
                index,
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     *
     * @param index       The index of the die on the draft pool.
     * @param destination The destination of the die on the pattern.
     */
    public void handleToolCardUsage(int index, Coordinates destination) {
        handleToolCardUsage(index, destination, false);
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     *
     * @param index       The index of the die on the draft pool.
     * @param destination The destination of the die on the pattern.
     * @param isSwap      {@code true} if a swap is required; {@code false} otherwise.
     */
    public void handleToolCardUsage(int index, Coordinates destination, boolean isSwap) {
        ViewMessage message;
        if (isSwap)
            message = new DiceSwap(
                    index,
                    destination,
                    this,
                    Action.APPLY_TOOL_CARD,
                    getPlayerName()
            );
        else
            message = new PlaceDie(
                    index,
                    destination,
                    this,
                    Action.APPLY_TOOL_CARD,
                    getPlayerName()
            );

        notifyObservers(message);
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     *
     * @param sources      The source coordinates of the dice on the pattern.
     * @param destinations The destination coordinates of the dice on the pattern.
     */
    public void handleToolCardUsage(Coordinates[] sources, Coordinates[] destinations) {
        notifyObservers(new MoveDice(
                sources,
                destinations,
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     *
     * @param destination The destination of the die on the pattern.
     * @param value       The selected value of the die.
     */
    public void handleToolCardUsage(Coordinates destination, int value) {
        notifyObservers(new ChooseValue(
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName(),
                value,
                destination
        ));
    }

    /**
     * Handles the event in which the player willingly disconnects.
     */
    public void handleDisconnect() {
        notifyObservers(new ViewMessage(
                this,
                Action.DISCONNECT_PLAYER,
                getPlayerName()
        ));
        client.disconnect();
    }

    public void handleLogin(String playerName, boolean multiPlayer, boolean rmi) {
        setPlayerName(playerName);
        organizer.setLocalPlayer(playerName);

        startClient(rmi, multiPlayer);

        notifyObservers(new ViewMessage(
                this,
                Action.REGISTER_PLAYER,
                getPlayerName()
        ));
    }

    /**
     * Starts the network client.
     *
     * @param isRmi         {@code true} if rmi is to be used; {@code false} if tcp is to be used.
     * @param isMultiPlayer {@code true} if in multi player mode; {@code false} if in
     *                      single player mode.
     */
    private void startClient(boolean isRmi, boolean isMultiPlayer) {
        ServerNetInterface netHandler;
        if (isRmi)
            netHandler = NetworkHandlerFactory.newRmiNetHandler();
        else
            netHandler = NetworkHandlerFactory.newTcpNetHandler();

        client = new Client(this, netHandler);
        try {
            client.connect(isMultiPlayer);
        } catch (Client.ConnectionRefusedException e) {
            client.getNetInterface().close();
        }
    }

    /**
     * The method to notify the controller after the selection of the
     * {@link it.polimi.se2018.model.PrivateObjectiveCard}.
     *
     * @param name the name of the card.
     */
    public void handlePrivateSelection(String name) {
        notifyObservers(new SelectCard(
                name,
                this,
                Action.SELECT_PRIVATE_OBJECTIVE,
                getPlayerName()
        ));
    }

    /**
     * Handles the selection of the difficulty at the beginning of a single-player
     * game.
     *
     * @param difficulty the chosen difficulty.
     */
    public void handleDifficultySelection(int difficulty) {
        notifyObservers(new SelectDifficulty(
                difficulty,
                this,
                getPlayerName()
        ));
    }

}
