package it.polimi.se2018.model;

import java.util.Random;

public class Die {  //gli attributi sono final in quanto la classe è immutabile
    private final int value;
    private final Random random; // il generatore di numeri casuali (random) serve per generare il value del dado quando questo deve essere ritirato
    private final Colour colour;


    public Die(Random random, Colour colour) { //costruttore da utilizzare quando si ha un lancio del dado (nel metodo 'roll')
        this.value = random.nextInt(5)+1; //il value sarà un valore casuale tra 1 e 6
        this.random = random;
        this.colour = colour;
    }

    public Die (int value, Random random, Colour colour) throws DieValueException{ //costruttore da utilizzare quando il value del dado viene scelto in modo NON casuale)
        if(value < 1 || value > 6){
            throw new DieValueException("Die's value out of range: value must be between 1 and 6");
        }
        this.value = value;
        this.random = random;
        this.colour = colour;
    }

    public int getValue() {
        return value;
    }

    public Colour getColour() {
        return colour;
    }


    public Die roll(){ //metodo per lanciare il dado
        return new Die(random,colour);
    }

    public Die flip() { //metodo per girare il dado sulla faccia opposta
        return new Die(7-value, random, colour);
    }

    public Die decrease() throws DieValueException { //metodo per decrementare di 1 il value del dado (non utilizzabile se il value è 1)
        if (value == 1) {
            throw new DieValueException("Cannot decrease the value of the drafted Die: value must be between 1 and 6");
        } else {
            return new Die(value - 1, random, colour);
        }
    }

    public Die increase() throws DieValueException{ //metodo per incrementare di 1 il value del dado (non utilizzabile se il value è 6)
            if(value == 6) {
                throw new DieValueException("Cannot increase the value of the drafted Die: value must be between 1 and 6");
            }
        else{
                return new Die(value + 1, random, colour);
        }
    }
}
