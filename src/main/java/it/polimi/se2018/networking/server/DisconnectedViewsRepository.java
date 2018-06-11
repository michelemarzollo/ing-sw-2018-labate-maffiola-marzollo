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
 * <p>This class is thread-safe.</p>
 *
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
    private DisconnectedViewsRepository() {
    }

    /**
     * Returns the only instance of DisconnectedViewsRepository.
     *
     * @return The instance of DisconnectedViewsRepository.
     */
    public static synchronized DisconnectedViewsRepository getInstance() {
        if (instance == null)
            instance = new DisconnectedViewsRepository();
        return instance;
    }

    /**
     * Adds a view to the disconnected views repository.
     * <p>If the view is marked as not to store, nothing is done.</p>
     *
     * @param view The view to be added.
     */
    public synchronized void addView(VirtualView view) {
        if (view.isNotExpired())
            views.put(view.getPlayerName(), new WeakReference<>(view));
    }

    /**
     * Tries to retrieve the view with the specified player name among
     * the ones that are marked disconnected.
     *
     * @param playerName The name of the player to search a view for.
     * @return A reference to the view that the player owned prior to
     * disconnection if it exists and can be used or {@code null} otherwise.
     */
    public synchronized VirtualView tryRetrieveViewFor(String playerName) {
        WeakReference<VirtualView> maybeView = views.remove(playerName);

        if (maybeView == null)
            return null;

        VirtualView view = maybeView.get();
        if (view != null && view.isNotExpired())
            return view;
        else
            return null;
    }

    /**
     * Force to drop all stored views.
     */
    public synchronized void flush(){
        views.clear();
    }

}
