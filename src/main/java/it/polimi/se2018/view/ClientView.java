package it.polimi.se2018.view;

import it.polimi.se2018.model.events.*;
import it.polimi.se2018.networking.client.Client;
import it.polimi.se2018.networking.client.RmiNetworkHandler;
import it.polimi.se2018.networking.client.TcpNetworkHandler;
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
        this.displayer.setView(this);
    }

    @Override
    public void showMultiPlayerGame() {
        displayer.displayMultiPlayerGame();
    }

    @Override
    public void showSinglePlayerGame() {
        displayer.displaySinglePlayerGame();
    }

    @Override
    public void showError(String error) {
        displayer.displayError(error);
    }

    @Override
    public void showPatternSelection() {
        displayer.askPattern();
    }

    @Override
    public void showPrivateObjectiveSelection() {
        displayer.askPrivateObjective();
    }

    @Override
    public void showScoreBoard() {
        displayer.displayScoreBoard();
    }

    @Override
    public void showDieSelection() {
        displayer.selectDie();
    }

    @Override
    public void showMoveSelection(int amount) {
        displayer.moveDie(amount);
    }

    @Override
    public void showDifficultySelection() {
        displayer.askDifficulty();
    }

    @Override
    public void showLensCutterSelection() {
        displayer.askDiceToSwap();
    }

    @Override
    public void showValueDestinationSelection() {
        displayer.askValueDestination();
    }

    @Override
    public void showFinalView() {
        //???????
    }

    @Override
    public void update(ModelUpdate message) {
        message.pushInto(organizer);
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
                Action.INCREMENT_DIE,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
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
     * @param index The index of the die on the draft pool.
     * @param destination The destination of the die on the pattern.
     */
    public void handleToolCardUsage(int index, Coordinates destination) {
        notifyObservers(new PlaceDie(
                index,
                destination,
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     * @param source The source coordinates of the die on the pattern.
     * @param destination The destination coordinates of the die on the pattern.
     */
    public void handleToolCardUsage(Coordinates source, Coordinates destination) {
        notifyObservers(new MoveDie(
                source,
                destination,
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     * @param sources The source coordinates of the dice on the pattern.
     * @param destinations The destination coordinates of the dice on the pattern.
     */
    public void handleToolCardUsage(Coordinates[] sources, Coordinates[] destinations) {
        notifyObservers(new MoveTwoDice(
                sources,
                destinations,
                this,
                Action.APPLY_TOOL_CARD,
                getPlayerName()
        ));
    }

    /**
     * Handles the event in which the player wants to use a tool card.
     * @param destination The destination of the die on the pattern.
     * @param value The selected value of the die.
     */
    public void handleToolCardUsage(Coordinates destination, int value){
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
    }

    /**
     * Handles the case in which the player logs in.
     * <p>This method sets the communication protocol with the server to Java RMI.</p>
     * @param playerName The name of the player.
     * @param serverAddress The address of the server.
     * @param serviceName The name of the RMI service on the server.
     */
    public void handleLogin(String playerName, String serverAddress, String serviceName) {
        setPlayerName(playerName);
        organizer.setLocalPlayer(playerName);
        client = new Client(this, new RmiNetworkHandler(serverAddress, serviceName));

        notifyObservers(new ViewMessage(
                this,
                Action.REGISTER_PLAYER,
                getPlayerName()
        ));
    }

    /**
     * Handles the case in which the player logs in.
     * <p>This method sets the communication protocol with the server to TCP.</p>
     * @param playerName The name of the player.
     * @param serverAddress The address of the server.
     * @param port The port on the server where the service is listening on.
     */
    public void handleLogin(String playerName, String serverAddress, int port) {
        setPlayerName(playerName);
        organizer.setLocalPlayer(playerName);
        client = new Client(this, new TcpNetworkHandler(serverAddress, port));

        notifyObservers(new ViewMessage(
                this,
                Action.REGISTER_PLAYER,
                getPlayerName()
        ));
    }

    /**
     * The method to notify the controller after the selection of the
     * {@link it.polimi.se2018.model.PrivateObjectiveCard}.
     * @param name the name of the card.
     */
    public void handlePrivateSelection(String name){
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
     * @param difficulty the chosen difficulty.
     */
    public void handleDifficultySelection(int difficulty){
        notifyObservers(new SelectDifficulty(
                difficulty,
                this,
                getPlayerName()
        ));
    }

    /**
     * The getter for {@code organizer}.
     * @return the organizer.
     */
    public ViewDataOrganizer getDataOrganizer() {
        return organizer;
    }
}
