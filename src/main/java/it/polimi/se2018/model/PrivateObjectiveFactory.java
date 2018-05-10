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

        //The ArrayList that contains the colours of the enum Colour
        List<Colour> listOfColours = new ArrayList<>(Arrays.asList(Colour.values()));
        /*
         * The ArrayList that contains the strings corresponding to colours of the enum Color,
         * with a capital letter followed by lowercase letters
         * The array elements will be part of the name of the card
         */
        List<String> camelCaseColours = new ArrayList<>();

        //The array to return
        PrivateObjectiveCard[] arrayOfCards = new PrivateObjectiveCard[n];

        Collections.shuffle(listOfColours);     //shuffles to draft random colours

        //Creation of the camelCaseColours array
        for (int i = 0; i < listOfColours.size(); i++) {
            camelCaseColours.add(i, listOfColours.get(i).toString().substring(0, 1) +
                    listOfColours.get(i).toString().substring(1).toLowerCase());
        }

        //Creation of the array to return, using the constructor of PrivateObjectiveCard
        for (int i = 0; i < n; i++) {
            arrayOfCards[i] = new PrivateObjectiveCard("ShadesOf" +
                    camelCaseColours.get(i), listOfColours.get(i));
        }

        return arrayOfCards;
    }

}
