package com.calc;

import java.util.LinkedList;

/**
 * Created by friketrin on 3/26/2016.
 * This class is part of the model part of the MVC, it contains previous expression  entries
 * from the calculator
 */
public class ExpressionHistory {
    private static LinkedList<String> history = new LinkedList<>();
    public LinkedList<String> getHistory(){ // TODO this can be used to populate the history view
        return history;
    }
    public static String getEntry(int i){
        return history.get(i);
    }
    public static int getSize(){
        return history.size();
    }
    public static void appendEntry(String expression){
        history.add(expression);
    }
    // TODO what else? just a template for the time being
}
