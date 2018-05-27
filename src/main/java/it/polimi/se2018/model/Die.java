package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.Random;

/**
 * The class represent a six-sides Die.
 * The class is immutable, so all the attributes are final.
 *
 * @author giorgiolbt
 */
public class Die implements Serializable {

    /**
     * Value of the Die.
     */
    private final int value;

    /**
     * Random number generator: it allows to have a random {@code value}
     * when the Die is rolled or drafted from the
     * {@link DiceBag}.
     */
    private final transient Random random;

    /**
     * Colour of the Die.
     */
    private final Colour colour;

    /**
     * Constructor to be used when a Die is rolled or drafted
     * from the {@link DiceBag}: the value is a random number
     * between 1 and 6.
     * @param random The random number generator of the Die.
     * @param colour The colour of the Die.
     */
    public Die(Random random, Colour colour) {
        this.value = random.nextInt(5)+1;
        this.random = random;
        this.colour = colour;
    }

    /**
     * Constructor to be used when the Die's value has to be chosen
     * in a non-random way: it is the case of the use of a {@link ToolCard}.
     * @param value The value of the Die
     * @param random The random number generator of the Die.
     * @param colour The colour of the Die.
     * @throws DieValueException if the value is not among the possible
     * values of a six-sides die.
     */
    public Die (int value, Random random, Colour colour) {
        if(value < 1 || value > 6){
            throw new DieValueException("Die's value out of range: value must be between 1 and 6");
        }
        this.value = value;
        this.random = random;
        this.colour = colour;
    }

    /**
     * Getter for the value of the Die.
     * @return the value of the Die.
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter for the colour of the Die.
     * @return the colour of the Die.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Roll the Die.
     * @return a new Die with a random value.
     */
    public Die roll(){
        return new Die(random,colour);
    }

    /**
     * Flip the Die on the opposite face
     * @return a new Die with a fixed value that is
     * the one of the opposite face.
     */
    public Die flip() {
        return new Die(7-value, random, colour);
    }

    /**
     * Decrease the Die's value.
     * @return a new Die with value decreased by 1.
     * @throws DieValueException when value is 1:
     * 1 may not change to 6 due to the Game's rules.
     */
    public Die decrease(){
        if (value == 1) {
            throw new DieValueException("Cannot decrease the value of the drafted Die: value must be between 1 and 6");
        } else {
            return new Die(value - 1, random, colour);
        }
    }

    /**
     * Increase the Die's value.
     * @return a new Die with value increased by 1.
     * @throws DieValueException when value is 6:
     * 6 may not change to 1 due to the Game's rules.
     */
    public Die increase() {
            if(value == 6) {
                throw new DieValueException("Cannot increase the value of the drafted Die: value must be between 1 and 6");
            }
        else{
                return new Die(value + 1, random, colour);
        }
    }
}
