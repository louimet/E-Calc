package com.calc;

/**
 * Created by friketrin on 3/25/2016.
 * Part of the controller (MVC) InputHandler gets input from the view and evaluates what should be
 * done with the input, afterwards calling on the model to make appropriate changes to the expression
 * or the memory slots. it keeps an index for the cursor position within the current expression.
 * Upon receiving input, this class performs a validation of input relative to the left side of the
 * current expression (validating against the right-hand side would unnecessarily hamper editing
 * of expressions)
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
        String subExpression = ExpressionHistory.getEntry().substring(0, currIndex);
        if (!subExpression.isEmpty() && currIndex > 0 && subExpression.charAt(currIndex-1) == '.'){
            if( !Character.isDigit(s.charAt(0) ) ){
                return false; // We cannot input anything other than a digit after a dot
            }
        }
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
                // make sure that digits and dots don't go after literal operands
                if( Character.isDigit(s.charAt(0)) ) {
                    if (subExpression.matches("(.*[eπ\\]])$")) {
                        s = "";
                        success = false;
                    }
                    else{
                        newIndex++;
                    }
                }
                else if( (s.equals("+") || s.equals("-") || s.equals("×")
                        || s.equals("÷") || s.equals("^")) ){
                    String patternString = "(.*[-×÷\\^\\+])$";
                    if(s.equals("-")){ // allow operator and minus for the operand
                        patternString = "(.*[-×÷\\^\\+]-)$";
                    }
                    if (subExpression.matches(patternString)) {
                        s = "";
                        success = false;
                    }
                    else{
                        newIndex++;
                    }

                }
                // just increment index for inputs such as e and pi
                else {
                    newIndex++;
                }
        }
        String expression = ExpressionHistory.getEntry();
        if (expression.isEmpty() && (s.equals("+") || s.equals("×")
                || s.equals("÷") || s.equals("^"))){ // removed s.equals("-") to allow eg "-1..."
            s = "[Ans]" + s;
            newIndex += 5;
        }
        String newExpression = expression.substring(0, currIndex) + s + expression.substring(currIndex);
        currIndex = newIndex;// the index in the handler refers to the cursor location, in history to the entry TODO change to more meaningful vars such as cursorLoc and entryIdx
        updateExpressionHistory(newExpression);
        return success;
    }

    /* Check backwards for the end of an operand and toggle minus according to the presence of
    * operators or parenthesis. Pretty involved; must check for 2 cases: 1.- a minus sign left
    * or right of the operand and 2.- no minus sign, do we have an operand left or right?
    * In each case there are a few things to check.
     */
    public static boolean plusMinus() {
        String expression = ExpressionHistory.getEntry();
        StringBuffer sb = new StringBuffer(expression);
        boolean addMinus;
        boolean removeMinus;
        boolean success;
        if(expression == null || expression.equals("")) {
            return false;
        }
        int tempIndex = currIndex;
        // Keep decrementing tempIndex until character at index is not an operand
        while(tempIndex > 1 && isOperandChar(expression.charAt(tempIndex-1))) {
            tempIndex--;
        }
        // At this point, tempIndex is @ leftmost of operand, check if it's a minus to our left
        if(tempIndex > 0 && sb.charAt(tempIndex-1) == '-'){

            // check the minus is at the beginning of the expression
            if( tempIndex == 1 ){
                removeMinus = true;
            }// check if we have a minus to our right -- we didn't move left in this case
            else if(sb.length() > tempIndex && sb.charAt(tempIndex) == '-'){
                removeMinus = true;
                currIndex++; // delete the minus to the right and keep our position
            }
            // check if we have an operand to the left
            else if(sb.substring(0,tempIndex-1).matches("(.*[-×÷\\^\\+\\(])$")){
                removeMinus = true;
            }
            else{
                removeMinus = false;
            }
            // if we found a minus, either we remove it or add another
            if(removeMinus){
                sb.deleteCharAt(tempIndex-1);
                currIndex--;
            }
            else{ // since we already had a minus as an operator insert one for operand
                sb.insert(tempIndex-1, "-");
                currIndex++;
            }
            success = true;
        }
        // we didn't find a minus around the cursor...
        // check for an operand to the left if we're at position 1 or 0
        else{
            /* maybe we stopped going left to avoid falling off the expression
            * and checking charAt(-1) so check the first character to make sure
            * it's not an operand */
            if(tempIndex == 1 && isOperandChar(sb.charAt(0))){
                tempIndex = 0;
                addMinus = true;
            }
            /* now that we now we don't have another operand left of us,
            * are we left of an operand? */
            else if(tempIndex < sb.length() && isOperandChar(sb.charAt(tempIndex)) ){
                addMinus = true;
            }
            else{
                addMinus = false;
            }
            if(addMinus) {
                sb.insert(tempIndex, "-");
                currIndex++;
                success = true;
            }
            else{
                success = false;
            }
        }
        expression = sb.toString();
        updateExpressionHistory(expression);
        return success;
        //currIndex = tempIndex; // we want the cursor to stay where it was
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
                || subExpression.matches("(.*[\\+|-|×|÷|\\^])$")
                || (!subExpression.matches(".*\\d*\\.\\d+$") // || subExpression.matches("((.*\\d[^\\.\\d])*\\d*)$")
                        && !subExpression.matches("(.*[eπ\\]\\.])$") ) ){
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

    /* helper for plusMinus, we don't look for ( as it's used for going left to find
    * the start of the operand. Since subtraction is not associative, crossing too many
    * opening parenthesis would change the result*/
    private static boolean isOperandChar(char c){
        boolean isOperandChar = (Character.isDigit(c) || c == '.' //|| c == ')'
                || c == 'e' || c == 'π' || c == '[' || c == 'M' || c == ']'
                || c == 'A' || c == 'n' || c == 's');
        return isOperandChar;
    }
}
