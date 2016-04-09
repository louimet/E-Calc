package com.calc;

import java.util.LinkedList;

/**
 * Created by friketrin on 3/26/2016.
 * This class is part of the model part of the MVC, it contains expression entries
 * from the calculator, including the current expression and an index for the current entry.
 * When past entries are edited, they're copied onto the end of the list, creating a new entry
 * RefreshDisplay is just a flag to inform the view that new info needs to be displayed
 */
public class ExpressionHistory {
    public static boolean refreshDisplay = false;

    private static LinkedList<String> history = new LinkedList<>();

    private static int entryIndex;

    public static boolean decrCurrIndex(){
        if (entryIndex > 0) {
            entryIndex--;
            refreshDisplay = true;
            return true;
        }
        else{
            return false; // we couldn't move further back
        }
    }

    public static boolean incrCurrIndex(){
        if (entryIndex < history.size()-1) {
            entryIndex++;
            refreshDisplay = true;
            return true;
        }
        else{
            return false; // we couldn't move further back
        }
    }

    public static int getCurrEntryIndex(){
        return entryIndex;
    }

    public static String getCurrEntry(){
        return getCurrEntry(entryIndex);
    }

    public static String getCurrEntry(int i){
        return history.get(i);
    }

    public static void setCurrEntry(String newEntry){
        history.set(entryIndex, newEntry);
    }

    public static int getSize(){
        return history.size();
    }

    public static void appendEntryToHistory(String expression){
        if(history.size() >= 1 && history.peekLast().equals(""))
        {
            history.removeLast(); // avoid having blank entries in history
        }
        history.add(expression);
        entryIndex = history.size()-1;
        refreshDisplay = true;
    }
}
