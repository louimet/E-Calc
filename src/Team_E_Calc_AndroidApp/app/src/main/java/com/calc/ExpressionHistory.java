package com.calc;

import java.util.LinkedList;

/**
 * Created by friketrin on 3/26/2016.
 * This class is part of the model part of the MVC, it contains previous expression  entries
 * from the calculator
 */
public class ExpressionHistory {
    private static LinkedList<String> history;
    public LinkedList<String> getHistory(){
        return history;
    }
    public static String getEntry(int i){
        return history.get(i);
    }
    public static void appendEntry(String expression){
        history.add(expression);
    }
    // TODO what else? just a template for the time being
}
