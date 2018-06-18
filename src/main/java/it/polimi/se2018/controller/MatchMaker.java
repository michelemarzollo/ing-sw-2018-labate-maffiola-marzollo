package it.polimi.se2018.controller;

import it.polimi.se2018.model.Game;
import it.polimi.se2018.view.View;

import java.lang.ref.WeakReference;


/**
 * Singleton used to link a view to a controller.
 */
public class MatchMaker {

    /**
     * The duration of a turn in multi-player configuration.
     */
    private static final int MULTI_PLAYER_TURN_DURATION = 200;

    /**
     * The duration of the timeout in multi-player configuration.
     */
    private static final int MULTI_PLAYER_TIMEOUT = 50;

    /**
     * The duration of a turn in single-player configuration.
     */
    private static final int SINGLE_PLAYER_TURN_DURATION = 200;

    /**
     * The duration of the timeout in single-player configuration.
     */
    private static final int SINGLE_PLAYER_TIMEOUT = 50;

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
            controller = new MultiPlayerController(new Game(), MULTI_PLAYER_TURN_DURATION, MULTI_PLAYER_TIMEOUT);
            multiPlayer = new WeakReference<>(controller);
        }

        view.registerObserver(controller);
    }

    /**
     * Links a view to a single player controller.
     *
     * @param view The view to be linked.
     */
    public void makeSinglePlayerMatchFor(View view) {
        Controller singlePlayer = new SinglePlayerController(new Game(), SINGLE_PLAYER_TURN_DURATION, SINGLE_PLAYER_TIMEOUT);
        view.registerObserver(singlePlayer);
    }
}
