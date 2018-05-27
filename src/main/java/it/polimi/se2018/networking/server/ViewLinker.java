package it.polimi.se2018.networking.server;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.MultiPlayerController;
import it.polimi.se2018.controller.SinglePlayerController;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.view.View;

import java.util.Timer;

/**
 * Singleton used to link a view to a controller.
 */
public class ViewLinker {

    /**
     * The only instance of the class.
     */
    private static ViewLinker instance;

    /**
     * A multi player controller that can accept new players.
     */
    private MultiPlayerController multiPlayer;

    /**
     * Private constructor to force singleton behaviour.
     */
    private ViewLinker() { }

    /**
     * Returns the only instance of ViewLinker.
     * @return The instance of ViewLinker.
     */
    public static ViewLinker getInstance(){
        if(instance == null)
            instance = new ViewLinker();
        return instance;
    }

    /**
     * Links a view to a multi player controller.
     * @param view The view to be linked.
     */
    public void linkMultiPlayer(View view){
        if(multiPlayer == null)
            multiPlayer = new MultiPlayerController(new Game(),150, new Timer());
        view.registerObserver(multiPlayer);
        ViewMessage message = new ViewMessage(view, Action.REGISTER_PLAYER, view.getPlayerName());
        multiPlayer.update(message);
    }

    /**
     * Links a view to a single player controller.
     * @param view The view to be linked.
     */
    public void linkSinglePlayer(View view){
        Controller singlePlayer = new SinglePlayerController(new Game(), new Timer(), 150);
        view.registerObserver(singlePlayer);
        ViewMessage message = new ViewMessage(view, Action.REGISTER_PLAYER, view.getPlayerName());
        singlePlayer.update(message);
    }
}
