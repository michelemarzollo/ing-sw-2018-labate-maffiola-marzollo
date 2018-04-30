package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DiceBag {
    private ArrayList<Die> dice; //sono i dadi nella DiceBag
    private Random random; //generatore di numeri casuali assegnato a ogni dado (serve per permettere di lanciarli)


    public DiceBag() { //creo i 90 dadi supponendo di averli già tirati (hanno già un valore casuale)
        dice = new ArrayList<Die>();
        random = new Random();
        int dicecounter = 0;
        for (Colour colour : Colour.values()) {
            for (; dicecounter < 18; dicecounter++) {    //ho 18 dadi per ogni colore (90 in tutto = 18*5): itero 18 volte per ogni colore
                dice.add(new Die(random, colour));
            }
            dicecounter = 0;
        }
        Collections.shuffle(dice); //mescola l'arraylist, si può eliminare
    }

    public List<Die> draft (int n){ //n è il numero di dadi da estrarre
        int dicecounter = 0;
        ArrayList<Die> draftpool = new ArrayList<Die>();
        for (; dicecounter < n; dicecounter++){
            int index = random.nextInt(dice.size());
            draftpool.add(dice.get(index)); //aggiungo il dado a quelli estratti
            dice.remove(index); //rimuovo il dado dal sacchetto
        }
        return draftpool;
    }

    public void pushback(Die d){
        dice.add(d);
    }

}
