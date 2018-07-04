package it.polimi.se2018.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for observable objects.
 * <p>The communication between observable and observer is handled
 * through messages of type T.</p>
 *
 * @param <T> The type of the message the observable object
 *            can notify to the observer.
 * @author dvdmff
 */
public abstract class Observable<T> {
    /**
     * List of all the registered observers.
     */
    private List<Observer<T>> observers = new ArrayList<>();

    /**
     * Associates the specified observer to this observable.
     *
     * @param observer The observer to be associated with the
     *                 observable
     */
    public synchronized void registerObserver(Observer<T> observer) {
        observers.add(observer);
    }

    /**
     * Removes the association between the specified observer and this
     * observable object if such relation exists.
     *
     * @param observer The observer to be removed.
     * @return {@code true} if the observer has successfully been
     * removed; {@code false} if there was no prior association
     * between the two objects.
     */
    public synchronized boolean deregisterObserver(Observer<T> observer) {
        boolean removed = observers.remove(observer);
        if (removed)
            observer.dropped();
        return removed;
    }

    /**
     * Removes all registered observers.
     */
    public synchronized void deregisterAll() {
        observers.forEach(Observer::dropped);
        observers.clear();
    }

    /**
     * Notifies all the registered observers that are registered at the moment
     * the method is called.
     *
     * @param message The message to send to the observers in order to
     *                notify  them.
     */
    public void notifyObservers(T message) {
        List<Observer<T>> copy;
        synchronized (this) {
            copy = new ArrayList<>(observers);
        }
        copy.forEach(o -> o.update(message));
    }
}
