package com.calc;

/**
 * Created by friketrin on 3/25/2016.
 * This class is part of the model part of the MVC, it contains the current expression from the calculator
 */
public class ExpressionBuffer {
    private static String expression = "";
    public static void setExpression(String newExpression){
        expression = newExpression;
    }
    public static String getExpression(){
        return expression;
    }
    public static void clear(){
        expression = "";
    }
}
