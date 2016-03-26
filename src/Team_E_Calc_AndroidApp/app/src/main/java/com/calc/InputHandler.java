package com.calc;

/**
 * Created by friketrin on 3/25/2016.
 */
public class InputHandler {

    public static void input(String s){
        switch(s){
            case "Sin(x)":
                s = "Sin(";
                break;
            case "Log10(x)":
                s = "Log10(";
                break;
            case "ex":
                s = "e^(";
                break;
            case "√(x)":
                s = "√(";
                break;
            case "10x":
                s = "10^(";
        }
        String expression = ExpressionBuffer.getExpression();
        ExpressionBuffer.setExpression(expression.concat(s));
    }

    public static void plusMinus(){
        String expression = ExpressionBuffer.getExpression();//TODO this works now with single-display
        // TODO currIndex should be kept here and modified via interaction of the view or passed by the view
        int currIndex = expression.length(); // NOTE by definition it is out of bounds
        // TODO add appropriate evaluation and modification here
    }

    public static void backspace(){
        //if (isZS || expression.isEmpty()) return"";
        String expression = ExpressionBuffer.getExpression();//TODO this works now with single-display
        if (expression.isEmpty())
            return;
        int length = expression.length();
        char deleted = expression.charAt(length - 1);
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

        ExpressionBuffer.setExpression(expression.substring(0, length - 1));
    }

    public static void clear(){
        ExpressionBuffer.clear();
    }

    public static void setWithHistoryEntry(int i){
        if (i < ExpressionHistory.getSize())
            ExpressionBuffer.setExpression(ExpressionHistory.getEntry(i));
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
