package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.utils.MissingConfigurationException;
import it.polimi.se2018.utils.ServerConfiguration;
import it.polimi.se2018.view.View;

import java.lang.ref.WeakReference;


/**
 * Singleton used to link a view to a controller.
 */
public class MatchMaker {

    /**
     * The only instance of the class.
     */
    private static MatchMaker instance;

    /**
     * A multi player controller that can accept new players.
     */
    private WeakReference<MultiPlayerController> multiPlayer;

    /**
     * Private constructor to force singleton behaviour.
     */
    private MatchMaker() {
    }

    /**
     * Returns the only instance of MatchMaker.
     *
     * @return The instance of MatchMaker.
     */
    public static MatchMaker getInstance() {
        if (instance == null)
            instance = new MatchMaker();
        return instance;
    }

    /**
     * Links a view to a multi player controller.
     *
     * @param view The view to be linked.
     */
    public void makeMultiPlayerMatchFor(View view) {
        MultiPlayerController controller = null;
        if (multiPlayer != null)
            controller = multiPlayer.get();

        if (controller == null || !controller.acceptsNewPlayers()) {
            try {
                ServerConfiguration configuration = ServerConfiguration.getInstance();
                controller = new MultiPlayerController(new Game(),
                        configuration.getTurnDuration(), configuration.getMultiPlayerTimeOut());

                multiPlayer = new WeakReference<>(controller);
            } catch (MissingConfigurationException e) {
                Logger.getDefaultLogger().log(e.getMessage());
                return;
            }
        }
        view.registerObserver(controller);
    }

    /**
     * Links a view to a single player controller.
     *
     * @param view The view to be linked.
     */
    public void makeSinglePlayerMatchFor(View view) {
        try {
            ServerConfiguration configuration = ServerConfiguration.getInstance();

            Controller singlePlayer = new SinglePlayerController(new Game(),
                    configuration.getTurnDuration());

            view.registerObserver(singlePlayer);
        } catch (MissingConfigurationException e) {
            Logger.getDefaultLogger().log(e.getMessage());
        }
    }
}
