package it.polimi.se2018.networking.server;

import it.polimi.se2018.controller.MatchMaker;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.networking.client.ClientNetInterface;
import it.polimi.se2018.networking.messages.Command;
import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.view.VirtualView;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the default behaviour of servers.
 * <p>By default servers register the new connections and associates them
 * with a virtual view. Such view can either be created from scratch or retrieved from
 * the disconnected views repository. The latter case happens when a connection dies
 * during a match.</p>
 *
 * @author dvdmff
 */
public class DefaultNetInterface implements ServerNetInterface {

    /**
     * Map associating the player names with their view. Only keeps track
     * of active view.
     */
    private final Map<String, VirtualView> views = new HashMap<>();

    /**
     * The server that handles the connections.
     */
    private final Server server;

    /**
     * Creates a new DefaultNetInterface that uses the specified server as
     * the connection handler.
     *
     * @param server The server that handles the connections.
     */
    public DefaultNetInterface(Server server) {
        this.server = server;
    }

    /**
     * Handles the receiving of messages from the clients.
     * <p>Here, only ViewMessages are received, since clients can only generate
     * such messages.</p>
     *
     * @param message The message sent over the network by the client.
     */
    @Override
    public void send(Message message) {
        ViewMessage viewMessage = (ViewMessage) message.getBody();
        String playerName = viewMessage.getPlayerName();
        VirtualView view = views.get(playerName);
        if (view != null)
            view.handle(viewMessage);
    }

    /**
     * Links a connection to a view.
     * <p>If the player was previously playing a game and got disconnected,
     * the view relative to the match is retrieved from the disconnected views
     * repository. Otherwise a new view is created and is linked to a controller.</p>
     *
     * @param client        The client to be associated with a view.
     * @param isMultiPlayer {@code true} if the client wants to play in multi player mode;
     *                      {@code false} if it wants ti play in single player mode.
     */
    private void associateView(ClientNetInterface client, boolean isMultiPlayer) {
        VirtualView view = DisconnectedViewsRepository.getInstance()
                .tryRetrieveViewFor(client.getUsername());

        if (view == null) {
            view = new VirtualView(client);
            if (isMultiPlayer)
                MatchMaker.getInstance().makeMultiPlayerMatchFor(view);
            else
                MatchMaker.getInstance().makeSinglePlayerMatchFor(view);
        } else {
            view.setClient(client);
            // inform controller of reconnection
            view.handle(new ViewMessage(view, Action.RECONNECT_PLAYER, client.getUsername()));
        }
        views.put(client.getUsername(), view);
    }

    /**
     * Adds a new client and associates it with a view.
     *
     * @param client        The newly connected client.
     * @param isMultiPlayer {@code true} if the client wants to play in multi player mode;
     *                      {@code false} if it wants ti play in single player mode.
     * @return {@code true} if the client has been added; {@code false} otherwise.
     */
    @Override
    public boolean addClient(ClientNetInterface client, boolean isMultiPlayer) {
        boolean clientAdded = server.addClient(client);

        client.notify(new Message(Command.ACK, clientAdded));

        if (!clientAdded)
            return false;

        associateView(client, isMultiPlayer);
        return true;
    }

    /**
     * Removes and disconnects a client.
     * <p>The view relative to the dead connection is moved to the disconnected view
     * repository.</p>
     *
     * @param client The client to remove.
     */
    @Override
    public void removeClient(ClientNetInterface client) {
        VirtualView view = views.remove(client.getUsername());

        if (view != null) {
            view.setClient(null);
            DisconnectedViewsRepository.getInstance().addView(view);
        }

        server.removeClient(client);
        client.close();
    }
}
