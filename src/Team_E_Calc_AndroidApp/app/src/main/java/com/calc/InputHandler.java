package com.calc;

/**
 * Created by friketrin on 3/25/2016.
 */
public class InputHandler {
    /*public static enum input{
        DIGIT, DOT, PI, SIGNUM, LPAR, RPAR, PLUS, MINUS, MULT, DIV
    }*/
    /*public String inputDigit(Integer n, String expression){
        return expression.concat(n.toString());
    }
    public String inputDot(String expression){
        return expression.concat(".");
    }
    public String inputPi(String expression){
        return expression.concat("π");
    }
    public String input(String expression){
        return expression.concat("π");
    }*/
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
}
