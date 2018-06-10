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
     * Array of all the possible description of the tool cards.
     */
    private static final String[] descriptions = {
            "After drafting, increase or decrease the value of the drafted die by 1\n" +
                    "1 may not change to 6, or 6 to 1",
            "Move any one die in your window ignoring color restrictions\n" +
                    "You must obey all other placement restrictions",
            "Move any one die in your window ignoring value restrictions\n" +
                    "You must obey all other placement restrictions",
            "Move exactly two dice, obeying all placement restrictions",
            "After drafting, swap the drafted die with a die from the Round Track",
            "After drafting, re-roll the drafted die\n" +
                    "If it cannot be placed, return it to the Draft Pool",
            "Re-roll all dice in the Draft Pool\n" +
                    "This may only be used on your second turn before drafting",
            "After your first turn, immediately draft a die\n" +
                    "Skip your next turn this round",
            "After drafting, place the die in a spot that is not adjacent to another die\n" +
                    "You must obey all other placement restrictions",
            "After drafting, flip the die to its opposite side\n" +
                    "6 flips to 1, 5 to 2, 4 to 3, etc.",
            "After drafting, return the die to the Dice Bag and pull 1 die from the bag\n" +
                    "Choose a value and place the new die, obeying all placement restrictions, or\n" +
                    "return it to the Draft Pool",
            "Move up to two dice of the same color that match the color of a die\n" +
                    "on the Round Track\n" +
                    "You must obey all placement restrictions"

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
    public ToolCard[] newInstances(int n){
        List<Integer> ints = IntStream.range(0, 12)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(ints);

        return ints.stream()
                .limit(n)
                .map(i -> new ToolCard(names[i], descriptions[i], colours[i]))
                .toArray(ToolCard[]::new);

    }
}
