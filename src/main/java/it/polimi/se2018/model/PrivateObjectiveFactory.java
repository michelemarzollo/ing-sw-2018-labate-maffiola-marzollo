package it.polimi.se2018.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A factory to create the PrivateObjectiveCards that will be used during the game
 *
 * @author michelemarzollo
 */
public class PrivateObjectiveFactory {

    /**
     * The method creates a random set of PrivateObjectiveCards
     *
     * @param n The number of cards to return
     * @return The array of cards
     */
    public PrivateObjectiveCard[] newInstances(int n) {

        if(n > Colour.values().length)
            throw new IllegalArgumentException();

        //The ArrayList that contains the colours of the enum Colour
        List<Colour> listOfColours = new ArrayList<>(Arrays.asList(Colour.values()));
        Collections.shuffle(listOfColours);     //shuffles to draft random colours

        /*
         * The ArrayList that contains the strings corresponding to colours of the enum Color,
         * with a capital letter followed by lowercase letters
         * The array elements will be part of the name of the card
         */
        PrivateObjectiveCard[] arrayOfCards = new PrivateObjectiveCard[n];
        for (int i = 0; i < n; i++) {
            Colour colour = listOfColours.get(i);
            arrayOfCards[i] = new PrivateObjectiveCard(
                    "ShadesOf" + colour.toString(),
                    colour);
        }

        return arrayOfCards;
    }

}
