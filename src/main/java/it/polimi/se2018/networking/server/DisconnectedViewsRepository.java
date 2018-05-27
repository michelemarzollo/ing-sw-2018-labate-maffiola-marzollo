package it.polimi.se2018.networking.server;

import it.polimi.se2018.view.VirtualView;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Singleton class that keeps track of disconnected views.
 * <p>The class <strong>doesn't own</strong> the views, so they live only
 * as long as some other element has a reference to them. This behaviour
 * allows to safely free resources when a match ends.</p>
 * @author dvdmff
 */
public class DisconnectedViewsRepository {

    /**
     * The only instance of DisconnectedViewsRepository.
     */
    private static DisconnectedViewsRepository instance;

    /**
     * Map that associates player names to the respective view.
     */
    private final Map<String, WeakReference<VirtualView>> views
            = new WeakHashMap<>();

    /**
     * Private constructor to force singleton behaviour.
     */
    private DisconnectedViewsRepository() { }

    /**
     * Returns the only instance of DisconnectedViewsRepository.
     * @return The instance of DisconnectedViewsRepository.
     */
    public static DisconnectedViewsRepository getInstance(){
        if(instance == null)
            instance = new DisconnectedViewsRepository();
        return instance;
    }

    /**
     * Adds a view to the disconnected views repository.
     * @param view The view to be added.
     */
    public void addView(VirtualView view){
        views.put(view.getPlayerName(), new WeakReference<>(view));
    }

    /**
     * Tries to retrieve the view with the specified player name among
     * the ones that are marked disconnected.
     * @param playerName The name of the player to search a view for.
     * @return A reference to the view that the player owned prior to
     * disconnection if it exists or {@code null} otherwise.
     */
    public VirtualView tryRetrieveViewFor(String playerName){
        WeakReference<VirtualView> view = views.remove(playerName);
        if(view == null)
            return null;
        else
            return view.get();
    }

}
