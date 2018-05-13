package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The class is a factory that instantiates a given number of PublicObjectiveCards
 *
 * @author michelemarzollo
 */
public class PublicObjectiveFactory {

    public static final int NUMBER_OF_CARDS = 10;

    /**
     * The method that creates the random instances of the PublicObjectiveCards
     *
     * @param n The number of instances to create
     * @return The array with the cards
     */
    public PublicObjectiveCard[] newInstances(int n) {

        if(n > NUMBER_OF_CARDS)
            throw new IllegalArgumentException();

        List<Integer> arrayOfNumbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

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
                card = RowColorVariety.getInstance();
                break;
            case 2:
                card = ColumnColorVariety.getInstance();
                break;
            case 3:
                card = RowShadeVariety.getInstance();
                break;
            case 4:
                card = ColumnShadeVariety.getInstance();
                break;
            case 5:
                card = LightShades.getInstance();
                break;
            case 6:
                card = MediumShades.getInstance();
                break;
            case 7:
                card = DeepShades.getInstance();
                break;
            case 8:
                card = ShadeVariety.getInstance();
                break;
            case 9:
                card = ColorDiagonals.getInstance();
                break;
            case 10:
                card = ColorVariety.getInstance();
                break;
            default:
                return null;    //Maybe we could put an exception
        }
        return card;
    }

}
