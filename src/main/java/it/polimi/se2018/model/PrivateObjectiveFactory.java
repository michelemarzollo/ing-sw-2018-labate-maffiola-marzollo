package it.polimi.se2018.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A factory to create the PrivateObjectiveCards that will be used during the game.
 *
 * @author michelemarzollo
 */
public class PrivateObjectiveFactory {

    /**
     * The method creates randomly and returns an array of PrivateObjectiveCards,
     * with the specified dimension.
     *
     * @param n the number of cards to return.
     * @return the array of cards.
     */
    public PrivateObjectiveCard[] newInstances(int n) {

        if (n > Colour.values().length)
            throw new IllegalArgumentException();

        //The ArrayList that contains the colours of the enum Colour
        List<Colour> listOfColours = new ArrayList<>(Arrays.asList(Colour.values()));
        Collections.shuffle(listOfColours);     //shuffles to draft random colours

        PrivateObjectiveCard[] arrayOfCards = new PrivateObjectiveCard[n];

        /*The name of the card is given by the string ShadesOf concatenated
        to the string with the name of the colour, starting with a capital letter*/
        for (int i = 0; i < n; i++) {
            Colour colour = listOfColours.get(i);
            arrayOfCards[i] = new PrivateObjectiveCard(
                    "ShadesOf" + colour.toString(),
                    colour,
                    "Sum of values on " + colour.toString().toLowerCase() + " dice"
            );
        }

        return arrayOfCards;

    }

}
