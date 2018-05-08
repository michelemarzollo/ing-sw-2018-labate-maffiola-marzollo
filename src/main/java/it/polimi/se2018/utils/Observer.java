package it.polimi.se2018.utils;

/**
 * @author dvdmff
 * Interface for an object that can observe an {@link Observable<T>}
 * for a message of type T
 * @param <T> The type of the message the observable object
 *           can notify to the observer.
 */
public interface Observer<T> {
    /**
     * Method to be called by the observable object whenever
     * it needs to notify its observers with a message of type T
     * @param message An object containing the data to define
     *                the event
     */
    void update(T message);
}
