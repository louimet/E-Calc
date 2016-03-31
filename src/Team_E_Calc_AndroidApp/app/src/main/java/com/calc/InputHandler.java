package com.calc;

/**
 * Created by friketrin on 3/25/2016.
 */
public class InputHandler {

    private static int currIndex = 0;

    public static int getCurrIndex() {
        return currIndex;
    }

    public static void resetCurrIndex() {
        currIndex = ExpressionHistory.getEntry().length();
    }

    public static void setCurrIndex(int newIndex) {
        final int MAX_NUM_FUN_CHARS = 6;
        int rightSubExpressionLength = ExpressionHistory.getEntry().length() - newIndex;
        int rightShift = (MAX_NUM_FUN_CHARS - 1) < rightSubExpressionLength
                ? MAX_NUM_FUN_CHARS - 1
                : rightSubExpressionLength;
        int tempIndex = newIndex + rightShift;
        for (int i = 0; i < rightShift; i++) {
            int tempOffset = findLeftOffsetAtIndex(tempIndex);
            if (tempOffset > tempIndex - newIndex) {
                newIndex = tempIndex - tempOffset;
                break;
            }
            tempIndex--;
        }
        currIndex = newIndex;
        ExpressionHistory.refreshDisplay = true;
    }

    public static void moveLeft() { // TODO connect a button from the interface (add the button too)
        if (currIndex == 0)
            return;
        currIndex -= findLeftOffsetAtIndex(currIndex);
        ExpressionHistory.refreshDisplay = true;
    }

    public static void moveRight() { // TODO connect a button from the interface (add the button too)
        if (currIndex == ExpressionHistory.getEntry().length())
            return;
        currIndex += findRightOffsetAtIndex(currIndex);
        ExpressionHistory.refreshDisplay = true;
    }

    public static void moveUp() {
        ExpressionHistory.decrCurrIndex();
        currIndex = ExpressionHistory.getEntry().length();
    }

    public static void moveDown() {
        ExpressionHistory.incrCurrIndex();
        currIndex = ExpressionHistory.getEntry().length();
    }

    public static void input(String s) {
        int newIndex = currIndex;
        switch (s) {
            case "Sin(x)":
                s = "Sin(";
                newIndex += 4;
                break;
            case "Log10(x)":
                s = "Log10(";
                newIndex += 6;
                break;
            case "ex":
                s = "e^(";
                newIndex += 3;
                break;
            case "√(x)":
                s = "√(";
                newIndex += 2;
                break;
            case "xy":
                s = "^";
                newIndex += 1;
                break;
            case ".":
                s = validateDot();
                newIndex += s.length();
                break;
            case "mr":
                s = "mr";
                newIndex += 2;
                break;
            case "Ans":
                s = "Ans";
                newIndex +=3;
                break;
            default:
                newIndex++;
        }
        String expression = ExpressionHistory.getEntry();
        String newExpression = expression.substring(0, currIndex) + s + expression.substring(currIndex);
        currIndex = newIndex;// the index in the handler refers to the cursor location, in history to the entry TODO change to more meaningful vars such as cursorLoc and entryIdx
        updateExpressionHistory(newExpression);
    }

    public static void plusMinus() {
        String expression = ExpressionHistory.getEntry();
        // TODO add appropriate evaluation and modification here, remember to handle currIndex
        // NOTE now we can handle a simple minus sign, no need for a hyphen
        //updateExpressionHistory(newExpression);
    }

    public static void backspace() {
        //if (isZS || expression.isEmpty()) return"";
        String expression = ExpressionHistory.getEntry();//TODO this works now with single-display
        String leftSubexpression = expression.substring(0, currIndex);
        String rightSubexpression = expression.substring(currIndex);
        if (leftSubexpression.isEmpty())
            return;
        int newIndex = currIndex;
        int length = leftSubexpression.length();
        /* checking for functions being removed must go before checking anything else
        *  as it will forceus to remove the whole function,
        *  leave 1 character so remaining checks and masks work*/
        if (leftSubexpression.matches("(.*)Sin\\($")) {
            leftSubexpression = leftSubexpression.substring(0, length - 4);
            newIndex -= 4;
        } else if (leftSubexpression.matches("(.*)Log10\\($")) {
            leftSubexpression = leftSubexpression.substring(0, length - 6);
            newIndex -= 6;
        } else if (expression.matches("(.*)e\\^\\($")) {
            leftSubexpression = leftSubexpression.substring(0, length - 3);
            newIndex -= 3;
        } else if (leftSubexpression.matches("(.*)√\\($")) {
            leftSubexpression = leftSubexpression.substring(0, length - 2);
            newIndex -= 2;
        } else if (leftSubexpression.matches("(.*)mr$")) {
            leftSubexpression = leftSubexpression.substring(0, length - 2);
            newIndex -= 2;
        } else if (leftSubexpression.matches("(.*)Ans$")) {
            leftSubexpression = leftSubexpression.substring(0, length - 3);
            newIndex -= 3;
        } else { // we have a single character
            leftSubexpression = leftSubexpression.substring(0, length - 1);
            newIndex--;
        }
        currIndex = newIndex;
        updateExpressionHistory(leftSubexpression + rightSubexpression);
    }

    public static void clear() {
        updateExpressionHistory("");
        currIndex = 0;
    }

    //Set the memory
    public static void setMemory(String text){
        if ( (text.charAt(0)-'0' < 10 && text.charAt(0)-'0' >= 0) || text.charAt(0) == '.') {
            int i = 1;
            while (text.length() > i && ((text.charAt(i) - '0' < 10 && text.charAt(i) - '0' >= 0) || text.charAt(i) == '.'))
                i++;
            Memory.setMemoryBuffer(Double.parseDouble(text.substring(0,i)));
        }
    }

    // return the necessary offset to move left and keep function substrings atomic
    private static int findLeftOffsetAtIndex(int index) {
        // we only care about what's left of the cursor
        String expression = ExpressionHistory.getEntry().substring(0, index);
        if (expression.matches("(.*)Sin\\($"))
            return 4;
        else if (expression.matches("(.*)Log10\\($"))
            return 6;
        else if (expression.matches("(.*)√\\($"))
            return 2;
        else if (expression.matches("(.*)mr$"))
            return 2;
        else if (expression.matches("(.*)Ans$"))
            return 3;
        else // we should have a single character
            return 1;
    }

    // return the necessary offset to move right and keep function substrings atomic
    private static int findRightOffsetAtIndex(int n) {
        // we only care about what's right of the cursor
        String expression = ExpressionHistory.getEntry().substring(n);
        if (expression.matches("^Sin\\((.*)"))
            return 4;
        else if (expression.matches("^Log10\\((.*)"))
            return 6;
        else if (expression.matches("^√\\((.*)"))
            return 2;
        else if (expression.matches("mr.*"))
            return 2;
        else if (expression.matches("Ans.*"))
            return 3;
        else // we should have a single character
            return 1;
    }

    static private void updateExpressionHistory(String newExpression) {
        if (ExpressionHistory.getCurrEntry() == ExpressionHistory.getSize() - 1) {
            ExpressionHistory.setEntry(newExpression);
        } else { // avoid altering the course of history, modified entries are new entries
            ExpressionHistory.appendEntry(newExpression);
            currIndex = (ExpressionHistory.getEntry().length());
        }
        ExpressionHistory.refreshDisplay = true;
    }

    static private String validateDot(){
        String subExpression = ExpressionHistory.getEntry().substring(0,currIndex);
        String s = "";
        if( subExpression.equals("")
                || subExpression.matches("(.*(Log10)?\\()$")
                || subExpression.matches("((.*\\d[^\\.\\d])*\\d*)$")
                || subExpression.matches("(.*[\\+|-|×|÷|\\^])$") ){

            s = ".";
        }
        return s;
    }

}