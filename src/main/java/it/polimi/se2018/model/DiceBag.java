package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author giorgiolabate
 * The class represents the DiceBag. It's main role is to
 * keep trace of all the dice of the Game, but that have not
 * been drafted yet and to draft new dice at the beginning of
 * each round.
 */

public class DiceBag {
    /**
     * The dice in the DiceBag.
     */
    private ArrayList<Die> dice;
    /**
     * Random number generator: assigned to every
     * {@link Die} to allow to roll it.
     */
    private Random random;

    /**
     * Constructs a new DiceBag creating immediately
     * all the 90 dice  having already rolled them
     * (they already have a random value assigned).
     */

    public DiceBag() {
        dice = new ArrayList<>();
        random = new Random();
        int diceCounter = 0;
        //18 dice for each of the five colour
        for (Colour colour : Colour.values()) {
            for (; diceCounter < 18; diceCounter++) {
                dice.add(new Die(random, colour));
            }
            diceCounter = 0;
        }
    }


    /**
     * Draft the indicated number of dice from the DiceBag
     * in a random way.
     * @param n The number of dice to be drafted.
     * @return An ArrayList containing the drafted dice
     * in the drafted order.
     */

    public List<Die> draft (int n){
        int diceCounter = 0;
        ArrayList<Die> draftPool = new ArrayList<>();
        for (; diceCounter < n; diceCounter++){
            //index is a random value between 0 and dice's size -1
            //to randomly choose the die from the DiceBag.
            int index = random.nextInt(dice.size());
            draftPool.add(dice.get(index));
            //Once drafted the die must be removed from the DiceBag.
            dice.remove(index);
        }
        return draftPool;
    }

    /**
     * Return a die to the DiceBag.
     * @param d The Die that has to be returned to the DiceBag.
     */
    public void pushBack(Die d){
        dice.add(d);
    }

}
