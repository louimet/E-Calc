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

    public static boolean moveLeft() {
        if (currIndex == 0)
            return false; // we can't move further left
        currIndex -= findLeftOffsetAtIndex(currIndex);
        ExpressionHistory.refreshDisplay = true;
        return true;
    }

    public static boolean moveRight() {
        if (currIndex == ExpressionHistory.getEntry().length())
            return false; // we can't move further left
        currIndex += findRightOffsetAtIndex(currIndex);
        ExpressionHistory.refreshDisplay = true;
        return true;
    }

    public static boolean moveUp() {
        boolean success = ExpressionHistory.decrCurrIndex();
        currIndex = ExpressionHistory.getEntry().length();
        return success;
    }

    public static boolean moveDown() {
        boolean success = ExpressionHistory.incrCurrIndex();
        currIndex = ExpressionHistory.getEntry().length();
        return success;
    }

    /* handle adding input to our string, return true if the operation was
     * successful, we only expect it to be unsuccessful with such inputs as 2.2.
     * or ...()
     */
    public static boolean input(String s) {
        int newIndex = currIndex;
        boolean success = true;
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
                if (s.length() == 0) {
                    success = false;
                }
                break;
            case ")":
                s = validateParenthesis();
                newIndex += s.length();
                if (s.length() == 0) {
                    success = false;
                }
                break;
            case "M":
                s = "[M]";
                newIndex += 3;
                break;
            case "Ans":
                s = "[Ans]";
                newIndex +=5;
                break;
            default:
                newIndex++;
        }
        String expression = ExpressionHistory.getEntry();
        if (expression.isEmpty() && (s.equals("+")| s.equals("-") | s.equals("×") | s.equals("÷")| s.equals("^"))){
            s = "[Ans]" + s;
            newIndex += 5;
        }
        String newExpression = expression.substring(0, currIndex) + s + expression.substring(currIndex);
        currIndex = newIndex;// the index in the handler refers to the cursor location, in history to the entry TODO change to more meaningful vars such as cursorLoc and entryIdx
        updateExpressionHistory(newExpression);
        return success;
    }

    public static void plusMinus() {
        /*String expression = ExpressionHistory.getEntry();
        // TODO add appropriate evaluation and modification here, remember to handle currIndex
        int tempIndex = currIndex;
        // Keep decrementing currIndex until character at index is not a digit
        while(tempIndex >=0 && Character.isDigit(expression.charAt(tempIndex))) {
            tempIndex--;
        }
        // At this point, character at currIndex is not a digit.
        if(expression.charAt(tempIndex) == 'g') {   // Check whether currIndex is pointing at "Log'10". (' = index)
            //currIndex = currIndex + 2; // if we're at the g, then we're trying to change the sign of an operator...
            // I say leave
            return;
        } else if (expression.charAt(currIndex) == '(' ||
                expression.charAt(currIndex) == 'π' ||
                expression.charAt(currIndex) == 'e') {
            currIndex = currIndex - 1;
        }
        // At this point, currIndex is on the character after which the minus sign should go.
        StringBuffer sb = new StringBuffer(expression);
        sb.insert(currIndex, "-(");
        currIndex = currIndex + 2;
        while(Character.isDigit(sb.charAt(currIndex))) {
            currIndex++;
        }
        sb.insert(currIndex - 1, ')');
        expression = sb.toString();
        updateExpressionHistory(expression);*/
    }

    public static boolean backspace() {
        //if (isZS || expression.isEmpty()) return"";
        String expression = ExpressionHistory.getEntry();//TODO this works now with single-display
        String leftSubexpression = expression.substring(0, currIndex);
        String rightSubexpression = expression.substring(currIndex);
        if (leftSubexpression.isEmpty())
            return false; // we didn't delete anything
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
        } else if (leftSubexpression.matches("(.*)\\[M\\]$")) {
            leftSubexpression = leftSubexpression.substring(0, length - 3);
            newIndex -= 3;
        } else if (leftSubexpression.matches("(.*)\\[Ans\\]$")) {
            leftSubexpression = leftSubexpression.substring(0, length - 5);
            newIndex -= 5;
        } else { // we have a single character
            leftSubexpression = leftSubexpression.substring(0, length - 1);
            newIndex--;
        }
        currIndex = newIndex;
        boolean success = updateExpressionHistory(leftSubexpression + rightSubexpression);
        return success;
    }

    public static boolean clear() {
        boolean success = updateExpressionHistory("");
        currIndex = 0;
        return success;
    }

    //Set the memory
    public static void setMemory(String text){
        Memory.setMemoryBuffer(ResultBuffer.getResult());
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
        else if (expression.matches("(.*)\\[M\\]$"))
            return 3;
        else if (expression.matches("(.*)\\[Ans\\]$"))
            return 5;
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
        else if (expression.matches("\\[M\\].*"))
            return 3;
        else if (expression.matches("\\[Ans\\].*"))
            return 5;
        else // we should have a single character
            return 1;
    }

    static private boolean updateExpressionHistory(String newExpression) {
        if (ExpressionHistory.getCurrEntry() == ExpressionHistory.getSize() - 1) {
            if(newExpression.isEmpty() && ExpressionHistory.getEntry().isEmpty()){
                return false; // we did nothing since nothing was needed
            }
            ExpressionHistory.setEntry(newExpression);
        } else { // avoid altering the course of history, modified entries are new entries
            ExpressionHistory.appendEntry(newExpression);
        }
        ExpressionHistory.refreshDisplay = true;
        return true;
    }

    static private String validateDot(){
        String subExpression = ExpressionHistory.getEntry().substring(0,currIndex);
        String s = "";
        if( subExpression.equals("")
                || subExpression.matches("(.*(Log10)?\\(\\d*)$")
                || subExpression.matches("((.*\\d[^\\.\\d])*\\d*)$")
                || subExpression.matches("(.*[\\+|-|×|÷|\\^])$") ){

            s = ".";
        }
        return s;
    }

    static private String validateParenthesis() {
        String subExpression = ExpressionHistory.getEntry().substring(0, currIndex);
        String s = "";
        if (!(currIndex == 0) && subExpression.charAt(currIndex-1) != '('){
            int openParenthesis = 0;
            for(int i=0; i < subExpression.length(); i++){
                if (subExpression.charAt(i) == '('){
                    openParenthesis++;
                }
                else if (subExpression.charAt(i) == ')'){
                    openParenthesis--;
                }
            }
            if (openParenthesis > 0){
                s = ")";
            }
        }
        return s;
    }
}