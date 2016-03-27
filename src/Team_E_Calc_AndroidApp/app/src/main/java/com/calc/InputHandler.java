package com.calc;

/**
 * Created by friketrin on 3/25/2016.
 */
public class InputHandler {

    private static int currIndex = 0;

    public static void resetIndex(){
        currIndex = ExpressionBuffer.getExpression().length();
    }

    public void moveLeft(){ // TODO connect a button from the interface (add the button too)
        if (currIndex == 0)
            return;
        // we only care about what's left of the cursor
        String expression = ExpressionBuffer.getExpression().substring(0,currIndex);
        if (expression.matches("(.*)Sin\\($") )
            currIndex -= 4;
        else if (expression.matches("(.*)Log10\\($") )
            currIndex -= 6;
        else if (expression.matches("(.*)e\\^\\($") )
            currIndex -= 3;
        else if (expression.matches("(.*)√\\($") )
            currIndex -= 2;
        else if (expression.matches("(.*)10\\^\\($") )
            currIndex -= 4;
        else // we should have a single character
            currIndex -= 1;
    }

    public void moveRight(){ // TODO connect a button from the interface (add the button too)
        if (currIndex == ExpressionBuffer.getExpression().length())
            return;
        // we only care about what's right of the cursor
        String expression = ExpressionBuffer.getExpression().substring(currIndex);
        if (expression.matches("(.*)Sin\\($") )
            currIndex += 4;
        else if (expression.matches("(.*)Log10\\($") )
            currIndex += 6;
        else if (expression.matches("(.*)e\\^\\($") )
            currIndex += 3;
        else if (expression.matches("(.*)√\\($") )
            currIndex += 2;
        else if (expression.matches("(.*)10\\^\\($") )
            currIndex += 4;
        else // we should have a single character
            currIndex += 1;
    }

    public static void input(String s){
        int newIndex = currIndex;
        switch(s){
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
            case "10x":
                s = "10^(";
                newIndex += 4;
                break;
            case "xy":
                s = "^";
                newIndex += 1;
                break;
            default:
                newIndex++;
        }
        String expression = ExpressionBuffer.getExpression();
        String newExpression = expression.substring(0,currIndex)+s+expression.substring(currIndex);
        currIndex = newIndex;
        ExpressionBuffer.setExpression(newExpression);
    }

    public static void plusMinus(){
        String expression = ExpressionBuffer.getExpression();
        // TODO add appropriate evaluation and modification here, remember to handle currIndex
        // NOTE now we can handle a simple minus sign, no need for a hyphen
    }

    public static void backspace(){
        //if (isZS || expression.isEmpty()) return"";
        String expression = ExpressionBuffer.getExpression();//TODO this works now with single-display
        String leftSubexpression = expression.substring(0, currIndex);
        String rightSubexpression = expression.substring(currIndex);
        if (leftSubexpression.isEmpty())
            return;
        int newIndex = currIndex;
        int length = leftSubexpression.length();
        char deleted = leftSubexpression.charAt(length - 1);
        // checking for functions being removed must go before checking anything else as it will
        // force us to remove the whole function, leave 1 character so remaining checks and masks work
        if (leftSubexpression.matches("(.*)Sin\\($") ) {
            leftSubexpression = leftSubexpression.substring(0, length - 4);
            newIndex -= 4;
        }
        else if (leftSubexpression.matches("(.*)Log10\\($") ) {
            leftSubexpression = leftSubexpression.substring(0, length - 6);
            newIndex -= 6;
        }
        else if (expression.matches("(.*)e\\^\\($") ) {
            leftSubexpression = leftSubexpression.substring(0, length - 3);
            newIndex -= 3;
        }
        else if (leftSubexpression.matches("(.*)√\\($") ) {
            leftSubexpression = leftSubexpression.substring(0, length - 2);
            newIndex -= 2;
        }
        /*else if (leftSubexpression.matches("(.*)10\\^\\($") ) {
            leftSubexpression = leftSubexpression.substring(0, length - 4);
            newIndex -= 4;
        }*/
        else { // we have a single character
            leftSubexpression = leftSubexpression.substring(0, length - 1);
            newIndex--;
        }
        ExpressionBuffer.setExpression(leftSubexpression+rightSubexpression);
        currIndex = newIndex;
    }

    public static void clear(){
        ExpressionBuffer.clear();
        currIndex = 0;
    }

    public static void setWithHistoryEntry(int i){
        if (i < ExpressionHistory.getSize())
            ExpressionBuffer.setExpression(ExpressionHistory.getEntry(i));
            currIndex = ExpressionBuffer.getExpression().length();
    }

    // This could be of use for the plusMinus modifier key, TODO delete when done
    /*// ----------BACKSPACE------- This is pretty involved...
        if (view.getId() == R.id.buttonBackspace) {
            if (isZS || expression.isEmpty()) return"";
            int length = expression.length();


            char deleted = expression.charAt(length-1);
            // checking for functions being removed must go before checking anything else as it will
            // force us to remove the whole function, leave 1 character so remaining checks and masks work
            if (expression.matches("(.*)Sin\\($") )
                expression = expression.substring(0, length-3);
            else if (expression.matches("(.*)Log10\\($") )
                expression = expression.substring(0, length-5);
            else if (expression.matches("(.*)e\\^\\($") )
                expression = expression.substring(0, length-2);
            else if (expression.matches("(.*)√\\($") )
                expression = expression.substring(0, length-1);
            else if (expression.matches("(.*)10\\^\\($") )
                expression = expression.substring(0, length-3);

            // maybe the length changed, get it again
            length = expression.length();

            if (length == 1) { // we're done, only the deleted character is there
                INPUTFLOWMASK = SZALLOW;
                parenthesisOpen = 0;
                isZS = true;
                return "0.0";
            }

            switch (deleted) {
                case '(':
                    parenthesisOpen--;
                    break;
                case ')':
                    parenthesisOpen++;
                    break;
                case '.':
                    INPUTFLOWMASK |= ALLOWDOT;
                    break;
                case 'π':
                    INPUTFLOWMASK |= ALLOWPI;

            }

            char newLast = expression.charAt(length-2);

            if (newLast == '.') INPUTFLOWMASK = ALLOWDIGIT; // only digits after dot

            if (newLast == 'π')
                INPUTFLOWMASK = ALLOWMODIF | ALLOWLPAR | ALLOWRPAR | ALLOWOPERATOR | ALLOWFUN;

            if (newLast <= '0' && newLast >= '9'){
                INPUTFLOWMASK |= ALLOWDIGIT | ALLOWDOT | ALLOWPI | ALLOWMODIF | ALLOWLPAR | ALLOWRPAR | ALLOWOPERATOR | ALLOWFUN;
                int i = 3;
                while(i > length &&
                        ( ( expression.charAt(length-i) >= '0' && expression.charAt(length-i) <= '9' ) ||
                                expression.charAt(length-i) == '.') ) {
                    if (expression.charAt(length - i) == '.') {
                        INPUTFLOWMASK &= ~ALLOWDOT;
                        break;
                    }
                }
            }

            if (newLast == '×' || newLast == '÷' || newLast == '+' )
                INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;

            if (newLast == '-'){
                INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;
                if(length > 2) {
                    char newNext2Last = expression.charAt(length - 3);
                    if (newNext2Last == '(')
                        INPUTFLOWMASK &= ~ALLOWLPAR; // TODO make sure &= is what we need
                }
            }

            if (newLast == '(')
                INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN | ALLOWMINUS;

            if (newLast == ')') {
                INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWOPERATOR | ALLOWFUN;
                if (parenthesisOpen < 0) INPUTFLOWMASK |= ALLOWRPAR;
            }

            return expression.substring(0, length-1);
        }
        // -------END BACKSPACE------*/
}
