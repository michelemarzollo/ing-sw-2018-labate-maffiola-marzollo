package it.polimi.se2018.controller;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.ToolCard;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a factory class for {@link ToolCard}.
 * <p>The class handles the correct instantiation of all the
 * possible tool cards.</p>
 * <p>This class is thread-safe by design.</p>
 *
 * @author dvdmff
 */
public class ToolCardFactory {

    /**
     * List of all the possible names of the tool cards.
     */
    private List<String> names;

    /**
     * List of all the possible descriptions of the tool cards.
     */
    private List<String> descriptions;

    /**
     * The colour restrictions on the tool cards. The index is the same
     * as the one for the name of the card.
     */
    private List<Colour> colours;

    /**
     * The instance of the singleton.
     */
    private static ToolCardFactory instance = null;

    /**
     * The private constructor.
     *
     * @param names        the list of all the possible names of the tool cards.
     * @param descriptions the list of all the possible descriptions of the tool cards.
     * @param colours      the list of the colour restrictions on the tool cards.
     */
    private ToolCardFactory(List<String> names, List<String> descriptions, List<Colour> colours) {
        this.names = names;
        this.descriptions = descriptions;
        this.colours = colours;
    }

    /**
     * The method that instantiates the class. If it is called when {@code instance}
     * is not null it does nothing and ignores the new parameters.
     *
     * @param names        the list of all the possible names of the tool cards.
     * @param descriptions the list of all the possible descriptions of the tool cards.
     * @param colours      the list of the colour restrictions on the tool cards.
     */
    public static void makeInstance(List<String> names, List<String> descriptions, List<Colour> colours) {
        if (instance == null)
            instance = new ToolCardFactory(names, descriptions, colours);
    }

    /**
     * Returns the instance of the class.
     * @return {@code instance}.
     */
    public static ToolCardFactory getInstance() {
        return instance;
    }

    /**
     * Resets the instance to null;
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Instantiates <code>n</code> random tool cards with no repetition.
     *
     * @param n The number of distinct tool card instances to retrieve.
     * @return An array containing exactly n instances of distinct
     * tool cards.
     */
    public ToolCard[] newInstances(int n) {

        if (n > names.size())
            throw new IllegalArgumentException();

        List<Integer> ints = IntStream.range(0, names.size())
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(ints);

        return ints.stream()
                .limit(n)
                .map(i -> new ToolCard(names.get(i), descriptions.get(i), colours.get(i)))
                .toArray(ToolCard[]::new);

    }
}
