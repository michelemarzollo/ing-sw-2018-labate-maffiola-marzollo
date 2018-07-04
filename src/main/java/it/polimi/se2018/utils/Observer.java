package it.polimi.se2018.utils;

/**
 * An {@code Observer<T>} is an object that observes one or more
 * {@link Observable} objects.
 * @param <T> The type of the message the observable object
 *           can notify to the observer.
 * @author dvdmff
 */
public interface Observer<T> {
    /**
     * Updates the observer that an event has occurred.
     * <p>This method is called by the observables which the observer is
     * registered to whenever they want to notify of a change in their
     * state.</p>
     * @param message An object containing the data to define
     *                the event
     */
    void update(T message);

    /**
     * Gets called whenever an Observable drops the link to the Observer.
     */
    default void dropped(){
        //Do nothing by default.
    }
}
