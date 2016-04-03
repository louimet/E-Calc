package com.calc;

import java.util.LinkedList;

/**
 * Created by friketrin on 3/26/2016.
 * This class is part of the model part of the MVC, it contains previous expression  entries
 * from the calculator
 */
public class ExpressionHistory {
    public static boolean refreshDisplay = false;

    private static LinkedList<String> history = new LinkedList<>();

    private static int currIndex;

    public static void decrCurrIndex(){
        if (currIndex > 0) {
            currIndex--;
            refreshDisplay = true;
        }
    }

    public static void incrCurrIndex(){
        if (currIndex < history.size()-1) {
            currIndex++;
            refreshDisplay = true;
        }
    }

    public static int getCurrEntry(){
        return currIndex;
    }

    public static String getEntry(){
        return getEntry(currIndex);
    }

    public static String getEntry(int i){
        return history.get(i);
    }

    public static void setEntry(String newEntry){
        history.set(currIndex, newEntry);
    }

    public static int getSize(){
        return history.size();
    }

    public static void appendEntry(String expression){
        if(history.size() >= 1 && history.peekLast().equals(""))
        {
            history.removeLast(); // avoid having blank entries in history
        }
        history.add(expression);
        currIndex = history.size()-1;
        refreshDisplay = true;
    }
    // TODO what else? just a template for the time being
}
