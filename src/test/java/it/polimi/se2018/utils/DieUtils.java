package it.polimi.se2018.utils;

import it.polimi.se2018.model.Die;

public class DieUtils {

    public static boolean areEqual(Die first, Die second){
//        System.err.println("first: " + first.getColour() + first.getValue() +
//                "\nsecond: " + first.getColour() + second.getValue());
        return first.getColour() == second.getColour() &&
                first.getValue() == second.getValue();
    }
}
