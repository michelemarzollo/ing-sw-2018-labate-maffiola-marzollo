package it.polimi.se2018.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a factory class for {@link ToolCard}.
 * <p>The class handles the correct instantiation of all the
 * possible tool cards.</p>
 * <p>This class is thread-safe by design.</p>
 * @author dvdmff
 */
public class ToolCardFactory {

    /**
     * Array of all the possible names of the tool cards.
     */
    private static final String[] names = {
            "Grozing Pliers",
            "Eglomise Brush",
            "Copper Foil Burnisher",
            "Lathekin",
            "Lens Cutter",
            "Flux Brush",
            "Glazing Hammer",
            "Running Pliers",
            "Cork-backed Straightedge",
            "Grinding Stone",
            "Flux Remover",
            "Tap Wheel"
    };

    /**
     * The colour restrictions on the tool cards. The index is the same
     * as the one for the name of the card.
     */
    private static final Colour[] colours = {
            Colour.PURPLE,
            Colour.BLUE,
            Colour.RED,
            Colour.YELLOW,
            Colour.GREEN,
            Colour.PURPLE,
            Colour.BLUE,
            Colour.RED,
            Colour.YELLOW,
            Colour.GREEN,
            Colour.PURPLE,
            Colour.BLUE
    };

    /**
     * Instantiates <code>n</code> random tool cards with no repetition.
     * @param n The number of distinct tool card instances to retrieve.
     * @return An array containing exactly n instances of distinct
     *         tool cards.
     */
    public ToolCard[] getInstances(int n){
        List<Integer> ints = IntStream.range(0, 12)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(ints);

        return ints.stream()
                .limit(n)
                .map(i -> new ToolCard(names[i], colours[i]))
                .toArray(ToolCard[]::new);

    }
}
