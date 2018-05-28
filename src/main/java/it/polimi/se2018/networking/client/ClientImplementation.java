package it.polimi.se2018.networking.client;

import it.polimi.se2018.model.events.ModelUpdate;
import it.polimi.se2018.networking.messages.Command;
import it.polimi.se2018.networking.messages.Message;
import it.polimi.se2018.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines what the client does when it receives a
 * message from the network.
 * <p>This class is immutable.</p>
 *
 * @author dvdmff
 */
public class ClientImplementation implements ClientNetInterface {

    /**
     * The view associated with the client.
     */
    private final View view;

    /**
     * Map that associates a name with a show method of the view.
     */
    private final Map<String, ShowMethod> showMethods;

    /**
     * The client the implementation refers to.
     */
    private final Client client;

    /**
     * Creates a new implementation relative to the specified client and view.
     *
     * @param client The client the implementation is bound to.
     * @param view   The view the implementation is bound to.
     */
    public ClientImplementation(Client client, View view) {
        this.client = client;
        this.view = view;
        showMethods = new HashMap<>();
        registerShowMethods(showMethods);
    }

    /**
     * Registers the show methods of the view to their identified.
     *
     * @param showMethods The map where show methods are stored.
     */
    private void registerShowMethods(Map<String, ShowMethod> showMethods) {
        showMethods.put("showMultiPlayerGame", view::showMultiPlayerGame);
        showMethods.put("showSinglePlayerGame", view::showSinglePlayerGame);
        showMethods.put("showPatternSelection", view::showPatternSelection);
        showMethods.put("showPrivateObjectiveSelection", view::showPrivateObjectiveSelection);
        showMethods.put("showScoreBoard", view::showScoreBoard);
        showMethods.put("showDieSelection", view::showDieSelection);
        // how to handle this?
        //showMethods.put("showMoveSelection", view::showMoveSelection);
        showMethods.put("showDifficultySelection", view::showDifficultySelection);
        showMethods.put("showLensCutterSelection", view::showLensCutterSelection);
        showMethods.put("showValueDestinationSelection", view::showValueDestinationSelection);
        showMethods.put("showFinalView", view::showFinalView);
    }

    /**
     * Getter for the username of the player.
     *
     * @return The username of the player.
     */
    @Override
    public String getUsername() {
        return view.getPlayerName();
    }

    /**
     * Dispatches the received message to the correct handler.
     *
     * @param message The message received from the network.
     */
    @Override
    public void notify(Message message) {
        if (message.getCommand() == Command.MODEL_UPDATE)
            client.notifyObservers((ModelUpdate) message.getBody());
        else if (message.getCommand() == Command.SHOW_ERROR)
            view.showError((String) message.getBody());
        else if (message.getCommand() == Command.SHOW) {
            ShowMethod showMethod = showMethods.get((String) message.getBody());
            if (showMethod != null)
                showMethod.show();
            else
                view.showError("Server sent strange a strange message");
        } else
            view.showError("Server sent an unknown message");
    }
}
