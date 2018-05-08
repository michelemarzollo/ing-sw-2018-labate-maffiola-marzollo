package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The class is a factory that instantiates a given number of PublicObjectiveCards
 */
public class PublicObjectiveFactory {
    /**
     * The method that creates the random instances of the PublicObjectiveCards
     *
     * @param n The number of instances to create
     * @return The array with the cards
     */
    public PublicObjectiveCard[] newInstances(int n) {

        ArrayList<Integer> arrayOfNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        Collections.shuffle(arrayOfNumbers);

        //The array to return
        PublicObjectiveCard[] arrayOfCards = new PublicObjectiveCard[n];

        for (int i = 0; i < n; i++) {
            arrayOfCards[i] = createCard(arrayOfNumbers.get(i));
        }
        return arrayOfCards;
    }

    /**
     * The method that instantiates one PublicObjectiveCard
     *
     * @param randomNumber A random number corresponding to the card to instantiate
     * @return The card
     */
    private PublicObjectiveCard createCard(int randomNumber) {

        PublicObjectiveCard card;

        switch (randomNumber) {
            case 1:
                card = RowColorVariety.instance();
                break;
            case 2:
                card = ColumnColorVariety.instance();
                break;
            case 3:
                card = RowShadeVariety.instance();
                break;
            case 4:
                card = ColumnShadeVariety.instance();
                break;
            case 5:
                card = LightShades.instance();
                break;
            case 6:
                card = MediumShades.instance();
                break;
            case 7:
                card = DeepShades.instance();
                break;
            case 8:
                card = ShadeVariety.instance();
                break;
            case 9:
                card = ColorDiagonals.instance();
                break;
            case 10:
                card = ColorVariety.instance();
                break;
            default:
                return null;
        }
        return card;
    }

}
