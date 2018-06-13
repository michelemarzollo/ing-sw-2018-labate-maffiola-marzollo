package it.polimi.se2018.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The class is a factory that instantiates a given number of PublicObjectiveCards.
 *
 * @author michelemarzollo
 * @see PublicObjectiveCard
 */
public class PublicObjectiveFactory {

    /**
     * Array of all the possible names of the Public Objective Cards.
     */
    private static final String[] names = {
            "Row Color Variety",
            "Column Color Variety",
            "Row Shade Variety",
            "Column Shade Variety",
            "Light Shades",
            "Medium Shades",
            "Deep Shades",
            "Shade Variety",
            "Color Diagonals",
            "Color Variety"
    };

    /**
     * Array of all the possible description of the Public Objective Cards.
     * The index is the same as the one for the name of the card.
     */
    private static final String[] descriptions = {
            "Rows with no repeated colors",
            "Columns with no repeated colors",
            "Rows with no repeated values",
            "Columns with no repeated values",
            "Sets of 1 & 2 values anywhere",
            "Sets of 3 & 4 values anywhere",
            "Sets of 5 & 6 values anywhere",
            "Sets of one of each value anywhere",
            "Sets of one of each color anywhere",
            "Count of diagonally adjacent same color dice"
    };

    /**
     * The victory points of the cards. The index is the same
     * as the one for the name of the card.
     *
     * @see PublicObjectiveCard#victoryPoints
     */
    private static final int[] victoryPoints = {
            6, 5, 5, 4, 2, 2, 2, 5, 1, 4
    };

    /**
     * Instantiates <code>n</code> random Public Objective Cards with no repetition.
     *
     * @param n The number of distinct card instances to retrieve.
     * @return An array containing exactly n instances of distinct cards.
     */
    public PublicObjectiveCard[] newInstances(int n) {

        if (n > names.length)
            throw new IllegalArgumentException();

        List<Integer> ints = IntStream.range(0, 10)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(ints);

        return ints.stream()
                .limit(n)
                .map(i -> new PublicObjectiveCard(names[i], descriptions[i], victoryPoints[i]))
                .toArray(PublicObjectiveCard[]::new);

    }

}
