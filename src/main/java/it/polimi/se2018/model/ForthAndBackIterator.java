package it.polimi.se2018.model;

import java.util.*;

/**
 * ForthAndBackIterator allows to go through a list of objects
 * twice, the second time being in reverse order.
 * <p>This implementation doesn't offer the possibility to remove
 * elements nor the method forEachRemaining, but instead only grants
 * sequential access to the underlying list of elements in the order
 * specified above.</p>
 * @param <T> The type of the objects contained in the list to iterate over.
 */
public class ForthAndBackIterator<T> implements Iterator<T> {

    private final List<T> list;
    private boolean goBack = false;
    private int current = 0;

    /**
     * Creates a new ForthAndBackIterator which will iterate over the given
     * list. If the reference to the list is null, it throws a
     * NullPointerException
     * @param list The list to iterate over.
     * @throws NullPointerException if the parameter list is null.
     */
    public ForthAndBackIterator(List<T> list) {
        if(list == null)
            throw new NullPointerException();

        this.list = list;
    }

    /**
     * Tells whether there are still objects to go through or not.
     * @return <code>true</code> if there is at least one more element
     *         to traverse; <code>false</code> otherwise.
     */
    @Override
    public boolean hasNext() {
        return !(current == 0 && goBack);
    }

    /**
     * Progresses the iteration, if possible, returning the next value
     * in the traversing order.
     * <p>This method is irreversible, so once invoked, it isn't possible
     * to put back the value or any kind of rewinding.</p>
     * @return The next element to be visited according to the order
     * @throws NoSuchElementException if there isn't any element left
     *         to be visited.
     */
    @Override
    public T next() {
        if(current < 0 || current >= list.size())
            throw new NoSuchElementException();

        T element = list.get(current);

        current += goBack ? -1 : +1;
        goBack |= current == list.size();

        return element;
    }

    /**
     * Tells if the iterator has gone through at least once every
     * element in the list.
     * <p>When the return value is <code>true</code> it also means
     * that the iterator has started traversing backwards the objects.
     * </p>
     * @return <code>true</code> if the list has been traversed in
     *         whole at least once; <code>false</code> otherwise.
     */
    public boolean hasTraversedAllOnce() {
        return goBack;
    }
}
