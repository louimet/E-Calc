/*
 * Written by Team-E for COMP 5541, calculator project
 * Winter 2016
 */

package com.calc;

/**
 * Part of the controller (MVC) InputHandler gets input from the view and evaluates what should be
 * done with the input, afterwards calling on the model to make appropriate changes to the expression
 * or the memory slots. it keeps an index for the cursor position within the current expression.
 * Upon receiving input, this class performs a validation of input relative to the left side of the
 * current expression (validating against the right-hand side would unnecessarily hamper editing
 * of expressions)
 */
public class InputHandler {

    /**
     * Current cursor position
     */
    private static int cursorPosition = 0;

    /**
     * @return current cursor position
     */
    public static int getCursorPosition() {
        return cursorPosition;
    }

    /**
     * reset current cursor position to the end of the current expression
     */
    public static void resetCurrPosition() {
        cursorPosition = ExpressionHistory.getEntry().length();
    }

    /**
     * set current cursor position to newIndex
     * @param newPosition position to which the sursor will be set
     */
    public static void setCursorPosition(int newPosition) {
        final int MAX_NUM_FUN_CHARS = 6;
        int rightSubExpressionLength =
                ExpressionHistory.getEntry().length() - newPosition;
        int rightShift = ((MAX_NUM_FUN_CHARS - 1) < rightSubExpressionLength)
                ? MAX_NUM_FUN_CHARS - 1
                : rightSubExpressionLength;
        int tempIndex = newPosition + rightShift;
        for (int i = 0; i < rightShift; i++) {
            int tempOffset = findLeftOffsetAtPosition(tempIndex);
            if (tempOffset > tempIndex - newPosition) {
                newPosition = tempIndex - tempOffset;
                break;
            }
            tempIndex--;
        }
        cursorPosition = newPosition;
        ExpressionHistory.refreshDisplay = true;
    }

    /**
     * Move the current position of the cursor to the left
     * @return true if we were able to move, false otherwise
     */
    public static boolean moveLeft() {
        if (cursorPosition == 0) {
            return false;
        }
        cursorPosition -= findLeftOffsetAtPosition(cursorPosition);
        ExpressionHistory.refreshDisplay = true;
        return true;
    }

    /**
     * Move the current position of the cursor to the right
     * @return true if we were able to move, false otherwise
     */
    public static boolean moveRight() {
        if (cursorPosition == ExpressionHistory.getEntry().length()) {
            return false;
        }
        cursorPosition += findRightOffsetAtPosition(cursorPosition);
        ExpressionHistory.refreshDisplay = true;
        return true;
    }

    /**
     * Scroll backwards through history
     * @return true if we were able to move, false otherwise
     */
    public static boolean moveUp() {
        boolean success = ExpressionHistory.decrCurrIndex();
        cursorPosition = ExpressionHistory.getEntry().length();
        return success;
    }

    /**
     * Scroll forwards through history
     * @return true if we were able to move, false otherwise
     */
    public static boolean moveDown() {
        boolean success = ExpressionHistory.incrCurrIndex();
        cursorPosition = ExpressionHistory.getEntry().length();
        return success;
    }

    /**
     * Handle adding input to our string, we should only expect it to be
     * unsuccessful with such inputs as 2.2. or ...()
     * @return true if the operation was successful, false otherwise
     */
    public static boolean input(String s) {
        String subExpression
                = ExpressionHistory.getEntry().substring(0, cursorPosition);
        if (!subExpression.isEmpty() && (cursorPosition > 0)
                && subExpression.charAt(cursorPosition -1) == '.') {

            // We cannot input anything other than a digit after a dot
            if (!Character.isDigit(s.charAt(0) )) {
                return false;
            }
        }

        int newPosition = cursorPosition;
        boolean success = true;
        switch (s) {
            case "Sin(x)":
                s = "Sin(";
                newPosition += 4;
                break;
            case "Log10(x)":
                s = "Log10(";
                newPosition += 6;
                break;
            case "ex":
                s = "e^(";
                newPosition += 3;
                break;
            case "√(x)":
                s = "√(";
                newPosition += 2;
                break;
            case "xy":
                s = "^";
                newPosition += 1;
                break;
            case ".":
                s = validateDot();
                newPosition += s.length();
                if (s.length() == 0) {
                    success = false;
                }
                break;
            case ")":
                s = validateParenthesis();
                newPosition += s.length();
                if (s.length() == 0) {
                    success = false;
                }
                break;
            case "M":
                s = "[M]";
                newPosition += 3;
                break;
            case "Ans":
                s = "[Ans]";
                newPosition +=5;
                break;
            default:
                // make sure that digits or dots don't go after literal operands
                if( Character.isDigit(s.charAt(0)) ) {
                    if (subExpression.matches("(.*[eπ\\]])$")) {
                        s = "";
                        success = false;
                    } else {
                        newPosition++;
                    }
                } else if((s.equals("+") || s.equals("-") || s.equals("×")
                        || s.equals("÷") || s.equals("^")) ) {
                    String patternString = "(.*[-×÷\\^\\+])$";

                    // allow operator and minus for the operand
                    if (s.equals("-")) {
                        patternString = "(.*[-×÷\\^\\+]-)$";
                    }
                    if (subExpression.matches(patternString)) {
                        s = "";
                        success = false;
                    } else {
                        newPosition++;
                    }
                } else { // just increment index for inputs such as e and pi
                    newPosition++;
                }
        }

        String expression = ExpressionHistory.getEntry();
        if (expression.isEmpty() && (s.equals("+") || s.equals("×")
                || s.equals("÷") || s.equals("^"))) {
                                            // no s.equals("-") allow "-1..."
            s = "[Ans]" + s;
            newPosition += 5;
        }
        String newExpression
                = expression.substring(0, cursorPosition)
                + s
                + expression.substring(cursorPosition);
        cursorPosition = newPosition;
        updateExpressionHistory(newExpression);
        return success;
    }

    /**
    * Check backwards for the end of an operand and toggle minus according to
    * the presence of operators or parentheses.
     * @return true if minus was toggled in the expression, false otherwise
     */
    public static boolean plusMinus() {
    /*
    * Pretty involved; must check for  2 cases: 1.- a minus sign left or
    * right of the operand and 2.- no minus sign, do we have an operand left
    * or right? In each case there are a few things to check.
    */
        String expression = ExpressionHistory.getEntry();
        StringBuffer sb = new StringBuffer(expression);
        boolean addMinus;
        boolean removeMinus;
        boolean success;
        if(expression == null || expression.equals("")) {
            return false;
        }
        int tempIndex = cursorPosition;

        while((tempIndex > 1)
                && isOperandChar(expression.charAt(tempIndex-1))) {
            tempIndex--;
        }

        // check if it's a minus to our left
        if((tempIndex > 0) && (sb.charAt(tempIndex-1) == '-')) {

            // check the minus is at the beginning of the expression
            if (tempIndex == 1) {
                removeMinus = true;

            /*
            * check if we have a minus to our right
            * -- we didn't move left in this case
            */
            } else if ((sb.length() > tempIndex)
                    && (sb.charAt(tempIndex) == '-')) {
                removeMinus = true;
                cursorPosition++; // delete minus to the right and keep position

            // check if we have an operand to the left
            } else if (sb.substring(0, tempIndex - 1).matches(
                    "(.*[-×÷\\^\\+\\(])$")) {
                removeMinus = true;
            } else {
                removeMinus = false;
            }

            // if we found a minus, either we remove it or add another
            if (removeMinus) {
                sb.deleteCharAt(tempIndex-1);
                cursorPosition--;
            } else { // already had a minus as operator, insert one for operand
                sb.insert(tempIndex-1, "-");
                cursorPosition++;
            }
            success = true;
        /* else
         * we didn't find a minus around the cursor...
         * check for an operand to the left if we're at position 1 or 0
        */
        } else {
            /* maybe we stopped going left to avoid falling off the expression
            * and checking charAt(-1) so check the first character to make sure
            * it's not an operand */
            if (tempIndex == 1 && isOperandChar(sb.charAt(0))) {
                tempIndex = 0;
                addMinus = true;
            /*
             * now that we now we don't have another operand left of us,
             * are we left of an operand?
             */
            } else if (tempIndex < sb.length()
                    && isOperandChar(sb.charAt(tempIndex))) {
                addMinus = true;
            } else {
                addMinus = false;
            }

            if (addMinus) {
                sb.insert(tempIndex, "-");
                cursorPosition++;
                success = true;
            } else {
                success = false;
            }
        }

        expression = sb.toString();
        updateExpressionHistory(expression);
        return success;
    }

    /**
     * Handle deletion of elements of the expression, functions are deleted
     * atomically
     * @return true if an element was deleted
     */
    public static boolean backspace() {
        String expression = ExpressionHistory.getEntry();
        String leftSubexpression = expression.substring(0, cursorPosition);
        String rightSubexpression = expression.substring(cursorPosition);
        if (leftSubexpression.isEmpty()) {
            return false;
        }
        int newIndex = cursorPosition;
        int length = leftSubexpression.length();

        /* checking for functions being removed must go before checking
         * anything else as it will forceus to remove the whole function,
         *  leave 1 character so remaining checks and masks work
         */
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
        cursorPosition = newIndex;
        return updateExpressionHistory(leftSubexpression + rightSubexpression);
    }

    /**
     * Clear the current expression
     * @return true if a non-empty expression was there to be cleared
     */
    public static boolean clear() {
        boolean success = updateExpressionHistory("");
        cursorPosition = 0;
        return success;
    }

    /**
     * Set the memory
     * @param text The String representation of a result to be stored in memory
     */
    public static void setMemory(String text){
        Memory.setMemoryBuffer(ResultBuffer.getResult());
    }

    /**
     * Handle movement of the cursor leftwards ensuring the atomicity of function
     * sub-strings.
     * @param position position from where to check leftwards
     * @return necessary offset to move left guaranteeing atomicity
     */
    private static int findLeftOffsetAtPosition(int position) {
        // we only care about what's left of the cursor
        String expression = ExpressionHistory.getEntry().substring(0, position);
        if (expression.matches("(.*)Sin\\($")) {
            return 4;
        } else if (expression.matches("(.*)Log10\\($")) {
            return 6;
        } else if (expression.matches("(.*)√\\($")) {
            return 2;
        } else if (expression.matches("(.*)\\[M\\]$")) {
            return 3;
        } else if (expression.matches("(.*)\\[Ans\\]$")) {
            return 5;
        } else { // we should have a single character
            return 1;
        }
    }

    /**
     * Handle movement of the cursor rightwards ensuring the atomicity of
     * function sub-strings.
     * @param position position from where to check rightwards
     * @return necessary offset to move left guaranteeing atomicity
     */
    private static int findRightOffsetAtPosition(int position) {
        // we only care about what's right of the cursor
        String expression = ExpressionHistory.getEntry().substring(position);
        if (expression.matches("^Sin\\((.*)")) {
            return 4;
        } else if (expression.matches("^Log10\\((.*)")) {
            return 6;
        } else if (expression.matches("^√\\((.*)")) {
            return 2;
        } else if (expression.matches("\\[M\\].*")) {
            return 3;
        } else if (expression.matches("\\[Ans\\].*")) {
            return 5;
        } else { // we should have a single character
            return 1;
        }
    }

    /**
     * Handle changing expression, if we're modifying an expression which is
     * not the last, copy it to the last position in the queue, keep at most
     * one empty string at the end of the history queue, ignore empty string
     * @param newExpression expression to be inserted at the last position
     * @return true if the expression was updated, false otherwise
     */
    static private boolean updateExpressionHistory(String newExpression) {
        if (ExpressionHistory.getCurrEntryIndex()
                == ExpressionHistory.getSize() - 1) {
            if(newExpression.isEmpty()
                    && ExpressionHistory.getEntry().isEmpty()) {
                return false; // we did nothing since nothing was needed
            }
            ExpressionHistory.setCurrEntry(newExpression);

        // avoid altering the course of history, modified entries = new entries
        } else {
            ExpressionHistory.appendEntryToHistory(newExpression);
        }
        ExpressionHistory.refreshDisplay = true;
        return true;
    }

    /**
     * ensure that the insertion of a dot leaves a valid expression left-wise
     * @return "." if the left subExpression can take a dot, "" otherwise
     */
    static private String validateDot(){
        String subExpression
                = ExpressionHistory.getEntry().substring(0, cursorPosition);
        String s = "";
        if( subExpression.equals("")
                || subExpression.matches("(.*(Log10)?\\(\\d*)$")
                || subExpression.matches("(.*[\\+|-|×|÷|\\^])$")
                || (!subExpression.matches(".*\\d*\\.\\d+$")
                        && !subExpression.matches("(.*[eπ\\]\\.])$") ) ){
            s = ".";
        }
        return s;
    }

    /**
     * Ensure that inserting parentheses will leave a valid expression left-wise
     * @return String with a parenthesis if it is a valid entry, ""otherwise
     */
    static private String validateParenthesis() {
        String subExpression
                = ExpressionHistory.getEntry().substring(0, cursorPosition);
        String s = "";
        if (!(cursorPosition == 0)
                && subExpression.charAt(cursorPosition -1) != '(') {
            int openParenthesis = 0;
            for (int i=0; i < subExpression.length(); i++) {
                if (subExpression.charAt(i) == '(') {
                    openParenthesis++;
                } else if (subExpression.charAt(i) == ')') {
                    openParenthesis--;
                }
            }
            if (openParenthesis > 0){
                s = ")";
            }
        }
        return s;
    }

    /** helper for plusMinus, we don't look for ( as it's used for going left to find
     * the start of the operand. Since subtraction is not associative, crossing too many
     * opening parenthesis would change the result
     * @param c The character to be evaluated
     * @return true if the character belongs to an operand, false otherwise
     */
    private static boolean isOperandChar(char c){
        return (Character.isDigit(c) || c == '.'
                || c == 'e' || c == 'π' || c == '[' || c == 'M' || c == ']'
                || c == 'A' || c == 'n' || c == 's');
    }
}
