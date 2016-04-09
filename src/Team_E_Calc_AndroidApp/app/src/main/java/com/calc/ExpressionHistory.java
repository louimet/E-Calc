/*
 * Written by Team E for COPM 5541, calculator project
 * Winter 2016
 */

package com.calc;

import java.util.LinkedList;

/**
 * This class is part of the model part of the MVC, it contains expression
 * entries from the calculator, including the current expression and an index
 * for the current entry. When past entries are edited, they're copied onto the
 * end of the list, creating a new entry.
 * <p>
 * RefreshDisplay is just a flag to inform
 * the view that new info needs to be displayed.
 */
public class ExpressionHistory {

    /**
     * Flag to signal others that the expression display should be refreshed
     */
    public static boolean refreshDisplay = false;

    // Keep all past expressions here, if editing, current is the last entry
    private static LinkedList<String> history = new LinkedList<>();

    // Which one is the user viewing?
    private static int entryIndex;

    /**
     * Scroll back through history entries
     * @return true if we were able to move one position back, false otherwise
     */
    public static boolean decrCurrIndex() {
        if (entryIndex > 0) {
            entryIndex--;
            refreshDisplay = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Scroll forward through history entries
     * @return true if we were able to move one position forward,
     * false otherwise
     */
    public static boolean incrCurrIndex() {
        if (entryIndex < (history.size() - 1)) {
            entryIndex++;
            refreshDisplay = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return int index of the current expression
     */
    public static int getCurrEntryIndex() {
        return entryIndex;
    }

    /**
     * @return String current expression
     */
    public static String getEntry() {
        return getEntry(entryIndex);
    }

    /**
     * @param i index of the string to be retrieved
     * @return String expression at ith position
     */
    public static String getEntry(int i) {
        return history.get(i);
    }

    /**
     * Set the current expression to newEntry
     * @param newEntry String to set at currentEntryIndex
     */
    public static void setCurrEntry(String newEntry){
        history.set(entryIndex, newEntry);
    }

    /**
     * @return size of the history of expressions
     */
    public static int getSize(){
        return history.size();
    }

    /**
     * Add a new expression to the end of our history
     * @param expression String to be appended
     */
    public static void appendEntryToHistory(String expression) {

        // avoid having blank entries in history
        if((history.size() >= 1) && history.peekLast().equals("")) {
            history.removeLast();
        }
        history.add(expression);
        entryIndex = history.size() - 1;
        refreshDisplay = true;
    }
}
