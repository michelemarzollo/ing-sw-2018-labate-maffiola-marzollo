package it.polimi.se2018.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dvdmff
 * Base class for observable object that can have one or more
 * associated {@link Observer<T>} objects.
 * <p>The communication
 * between observable and observer is handled through messages
 * of type T.</p>
 * @param <T> The type of the message the observable object
 *  *        can notify to the observer.
 */
public abstract class Observable<T> {
    /**
     * List of all the registered observers.
     */
    private List<Observer<T>> observers = new ArrayList<>();

    /**
     * This method associates the given observer to the observable
     * object.
     * @param observer The observer to be associated with the
     *                 observable
     */
    public void registerObserver(Observer<T> observer){
        observers.add(observer);
    }

    /**
     * This method removes the association between the given observer
     * and the observable object if it exists.
     * @param observer The observer to be removed.
     * @return {@code true} if the observer has successfully been
     *         removed; {@code false} if there was no prior association
     *         between the two objects.
     */
    public boolean deregisterObserver(Observer<T> observer){
        return observers.remove(observer);
    }

    /**
     * When this method is invoked, all the currently registered
     * observers are notified through the given message by
     * the observable.
     * @param message The message to send to the observers to notify
     *                them.
     */
    public void notifyObservers(T message){
        observers.forEach(o -> o.update(message));
    }
}
