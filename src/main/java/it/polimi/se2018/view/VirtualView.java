package it.polimi.se2018.view;

import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.ModelEvent;
import it.polimi.se2018.model.events.ModelUpdate;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Command;
import it.polimi.se2018.networking.messages.Message;

/**
 * This class represent a view on the server-side of the application.
 * <p>The aim of this class is to decouple the MVC architecture from
 * the underlying networking services.</p>
 * <p>This class is mutable and has no thread safety yet.</p>
 *
 * @author dvdmff
 */
public class VirtualView extends View {

    /**
     * Flag to indicate if the view has expired.
     */
    private boolean expired = false;

    /**
     * Reference to the client associated with this view.
     */
    private ClientNetInterface client;

    private boolean notifyDisconnection = false;

    /**
     * Creates a new virtual view with the specified player name that is
     * associated with the given client.
     *
     * @param client The client connection the player is using to communicate
     *               with the server.
     * @throws IllegalArgumentException if {@code client} is null.
     */
    public VirtualView(ClientNetInterface client) {
        if (client == null) {
            throw new IllegalArgumentException("client can't be null");
        }

        setPlayerName(client.getUsername());
        this.client = client;
    }

    /**
     * Sets a new client connection.
     * <p>This method must be called whenever the old connection is dead and a new one
     * is established, otherwise the virtual view results disconnected.</p>
     *
     * @param client The client connection the player is using to communicate
     *               with the server.
     */
    public synchronized void setClient(ClientNetInterface client) {
        notifyDisconnection = client == null;
        this.client = client;
    }

    /**
     * Retrieves the client connection currently associated with the view.
     * <p>If the connection has died, notifies the observers of the disconnection
     * and returns null.</p>
     *
     * @return The client connection currently in use or {@code null} if the connection
     * has died.
     */
    private synchronized ClientNetInterface getClient() {
        if (client == null && notifyDisconnection) {
            notifyDisconnection = false;
            // disconnected, notify controller
            new Thread(() ->
                    handle(new ViewMessage(this, Action.DISCONNECT_PLAYER, this.getPlayerName())))
                    .start();
        }
        return client;
    }

    /**
     * Sends to the associated client the information about what to show.
     *
     * @param what A string representing what to show.
     */
    private void show(String what) {
        ClientNetInterface clientNetInterface = getClient();
        if (clientNetInterface != null)
            clientNetInterface.notify(new Message(Command.SHOW, what));
    }

    /**
     * Informs the associated client to show the multi player game view.
     */
    @Override
    public void showMultiPlayerGame() {
        show("showMultiPlayerGame");
    }

    /**
     * Informs the associated client to show the single player game view.
     */
    @Override
    public void showSinglePlayerGame() {
        show("showSinglePlayerGame");
    }

    /**
     * Informs the associated client to show an error message.
     *
     * @param error The error message to be shown.
     */
    @Override
    public void showError(String error) {
        ClientNetInterface clientNetInterface = getClient();
        if (clientNetInterface != null)
            clientNetInterface.notify(new Message(Command.SHOW_ERROR, error));
    }

    /**
     * Informs the associated client to show the pattern selection view.
     */
    @Override
    public void showPatternSelection() {
        show("showPatternSelection");
    }

    /**
     * Informs the associated client to show the private objective card selection view.
     */
    @Override
    public void showPrivateObjectiveSelection() {
        show("showPrivateObjectiveSelection");
    }

    /**
     * Informs the associated client to show the score board view.
     */
    @Override
    public void showScoreBoard() {
        show("showScoreBoard");
    }

    /**
     * Informs the associated client to show the die selection view.
     */
    @Override
    public void showDieSelection() {
        show("showDieSelection");
    }

    @Override
    public void showDieIncrementSelection() {
        show("showDieIncrementSelection");
    }

    /**
     * Informs the associated client to show the move die selection view.
     * <p><strong>Note: there are troubles with the sending of the argument.</strong></p>
     *
     * @param amount The amount of selection to make.
     */
    @Override
    public void showMoveSelection(int amount) {
        show("showMoveSelection" + amount);
    }

    @Override
    public void showMoveUpToTwo() {
        show("showMoveUpToTwo");
    }

    /**
     * Informs the associated client to show the difficulty selection view.
     */
    @Override
    public void showDifficultySelection() {
        show("showDifficultySelection");
    }

    /**
     * Informs the associated client to show the view dedicated to the lens cutter tool card.
     */
    @Override
    public void showLensCutterSelection() {
        show("showLensCutterSelection");
    }

    /**
     * Informs the associated client to show the die destination and value selection view.
     */
    @Override
    public void showValueDestinationSelection() {
        show("showValueDestinationSelection");
    }

    @Override
    public void showPlaceDie() {
        show("showPlaceDie");
    }

    @Override
    public void showConfirm() {
        show("showConfirm");
    }

    /**
     * Propagates messages received from the network to the rest of the MVC architecture.
     *
     * @param message The message received from the network.
     */
    public void handle(ViewMessage message) {
        message.setView(this);
        notifyObservers(message);
    }

    /**
     * Propagates the messages notified by the observables to the network.
     *
     * @param message The update message sent by the model.
     */
    @Override
    public void update(ModelUpdate message) {
        ClientNetInterface clientNetInterface = getClient();
        expired |= message.getEventType() == ModelEvent.GAME_END;
        if (clientNetInterface != null)
            clientNetInterface.notify(new Message(Command.MODEL_UPDATE, message));
    }

    /**
     * Getter for the expired flag.
     *
     * @return {@code true} if the view has expired; {@code false} otherwise.
     */
    public boolean isNotExpired() {
        return !expired;
    }
}
