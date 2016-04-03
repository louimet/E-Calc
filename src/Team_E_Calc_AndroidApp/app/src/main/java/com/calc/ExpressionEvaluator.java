/**
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 *
 * This class evaluates the expression contained in the calculator's buffer.
 * It evaluates for correct parenthesis and then tokenizes the expression separating operands,
 * operators, and functions for placement onto stacks. It finally evaluates by popping the value
 * stack and the operator stack accordingly.
 *
 * It has a single public method for interfacing: evaluate(string) : string
 */
package com.calc;
import java.text.ParseException;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

// TODO comments -  Class to parse and compute calculator expressions from team E's calculator for
// dot dot dot

public class ExpressionEvaluator {

    private static boolean radians = true;

    public static boolean getRadians(){
        return radians;
    }

    public static void setRadians(boolean isRadians){
        radians = isRadians;
    }

    // takes a string and returns the evaluation in a string format
    public static String evaluate(){
        int currEntry = ExpressionHistory.getCurrEntry();
        String expression = ExpressionHistory.getEntry(currEntry);
        // this should only happen on the last entry
        if (expression.equals("") && currEntry == ExpressionHistory.getSize()){
            expression = ExpressionHistory.getEntry(currEntry-1);
        }
        boolean isWellParenthesized = validateParenthesis(expression);
        if (!isWellParenthesized) {
            //ExpressionBuffer.clear();
            return ("Err prnths");
        }
        if (!validateExpression(expression)){
            //ExpressionBuffer.clear();
            return ("Err syntax");
        }
        Queue<String> infixTokenQueue = tokenize(expression);
        Double result;
        try {
            result = evaluateInfix(infixTokenQueue);
        }catch(ParseException e){
            //ExpressionBuffer.clear();
            return e.getMessage();
        }
        // open a new entry in the history
        ExpressionHistory.appendEntry("");
        InputHandler.resetCurrIndex();
        Memory.setLastAnswer(result);
        ResultBuffer.setResult(result);
        return (result.toString());
    }

    // Separate the expression in tokens to be placed on stacks
    private static Queue<String> tokenize(String infixExpression)
    {
        Queue<String> infixTokenQueue = new LinkedList<>();
        String s = infixExpression; // work on a copy and keep the original
        final int exprLength = s.length();
        while (!s.isEmpty()){
            // fun (include lpar) - if preceded by Rpar, digit
            // IMPORTANT this part must be before checking for digits so we keep 10^ here
            if (s.matches("Log10\\(.*")){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("Log10");
                infixTokenQueue.add("(");
                s = s.substring(6); // include parenthesis in token
                continue;
            }
            if (s.matches("Sin\\(.*")){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("Sin");
                infixTokenQueue.add("(");
                s = s.substring(4); // include parenthesis in token
                continue;
            }
            if (s.matches("e\\^.*")){
                int lengthDifference = exprLength - s.length();
                // if the character to be tokenized is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("e^");
                s = s.substring(2);
            }
            if (s.matches("√\\(.*")){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("√");
                infixTokenQueue.add("(");
                s = s.substring(2); // include parenthesis in token
            }
            /*if (s.matches("10\\^\\(.*\\)")){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("10^");
                infixTokenQueue.add("(");
                s = s.substring(4); // include parenthesis in token
            }*/

            // Lpar - if preceded by digit, pi or Rpar, insert *
            if (s.charAt(0) == '('){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("(");
                s = s.substring(1);
                continue;
            }
            // Rpar
            if (s.charAt(0) == ')'){
                infixTokenQueue.add(")");
                s = s.substring(1);
                continue;
            }
            //digit - if preceded by Rpar ins * loop until not digit or dot
            if ( (s.charAt(0)-'0' < 10 && s.charAt(0)-'0' >= 0) || s.charAt(0) == '.'){
                int i = 1;
                while ( s.length() > i && ((s.charAt(i)-'0' < 10 && s.charAt(i)-'0' >= 0) || s.charAt(i) == '.') )
                    i++;
                /*// we might be eating into a 10^ function
                if ( s.length() > i && s.charAt(i) == '^' && i > 2 && s.substring(i-2,i).equals("10") )
                    i=i-2;*/
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by Rpar, insert *
                if (lengthDifference > 0 ){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add(s.substring(0,i));
                s = s.substring(i);
                continue;
            }
            //pi - if preceded by digit or Rpar insert mult
            if (s.charAt(0) == 'π'){
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("π");
                s = s.substring(1);
                continue;
            }
            //e - if preceded by digit or Rpar insert mult
            if (s.charAt(0) == 'e'){
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("e");
                s = s.substring(1);
                continue;
            }

            //memory
            if (s.matches("\\[M\\].*")){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("M");
                s = s.substring(3);
                continue;
            }

            if (s.matches("\\[Ans\\].*")){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("Ans");
                s = s.substring(5);
                continue;
            }

            //operator - if minus preceded by Lpar or operator, mult by -1
            if (s.charAt(0) == '-'){
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( precedingChar != '(' && precedingChar != '+' && precedingChar != '-'
                            && precedingChar != '×' && precedingChar != '÷' && precedingChar != '^') {
                        infixTokenQueue.add("-");
                    }
                    else{
                        infixTokenQueue.add("-1");
                        infixTokenQueue.add("×");
                    }
                }
                else if (lengthDifference == 0) {
                    infixTokenQueue.add("-1");
                    infixTokenQueue.add("×");
                }
                s = s.substring(1);
                continue;
            }
            if (s.charAt(0) == '+' || s.charAt(0) == '×'
                    || s.charAt(0) == '÷' || s.charAt(0) == '^'){
                infixTokenQueue.add(s.substring(0,1));
                s = s.substring(1);
                continue;
            }
        }
        return infixTokenQueue;
    }

    // evaluate the tokenized infix expression a token at a time, calling precedence
    // and applyOperation accordingly depending on precedence and parenthesis
    private static Double evaluateInfix(Queue<String> infixTokenQueue) throws ParseException
    {
        Stack<Double> valueStack = new Stack<>();
        Stack<String> opStack = new Stack<>();


        while (!infixTokenQueue.isEmpty())
        {
            String temp = infixTokenQueue.remove();
            if(temp.matches("^-?\\d*[\\d\\.]\\d*$"))
            {
                valueStack.push(Double.parseDouble(temp));
            }
            else if(temp.equals("π")) {
                valueStack.push(com.teamE.Pi.PI);
            }
            else if(temp.equals("e")) {
                valueStack.push(com.teamE.ExpFunction.calculate());
            }
            else if (temp.equals("M")){
                valueStack.push(Memory.getMemoryBuffer());
            }
            else if (temp.equals("Ans")){
                valueStack.push(Memory.getLastAnswer());
            }
            else if (temp.endsWith("("))
            {
                opStack.push(temp);
            }
            else if (temp.equals(")"))
            {
                while(!opStack.peek().equals("("))
                {
                    applyOperation(valueStack, opStack);
                }
                opStack.pop(); // discard lingering left parenthesis
            }

            else if (temp.equals("×") || temp.equals("÷") ||
                    temp.equals("+") || temp.equals("-") || temp.equals("^") ||
                    temp.equals("Log10") || temp.equals("Sin") ||
                    temp.equals("e^") || temp.equals("√") || temp.equals("10^") )
            {
                int incomingOp = precedence(temp);
                while (!opStack.isEmpty() && (precedence(opStack.peek()) >= incomingOp) )
                    applyOperation(valueStack, opStack);
                opStack.push(temp);
            }
        }
        while (!opStack.isEmpty())
            applyOperation(valueStack, opStack);
        if (valueStack.size() > 1) // NOTE for debugging add condition checking on stacks at end
            throw new ParseException("Err, Msng oprtrs", opStack.size()+valueStack.size());
        return valueStack.pop();
    }

    // Apply whatever operation is waiting in the stacks
    private static void applyOperation(Stack<Double> valueStack, Stack<String> opStack) throws ParseException
    {
        if (opStack.peek().length() > 1 || opStack.peek().equals("√")){ // we have a function
            String temp = opStack.pop();
            if (temp.equals("Log10")) {
                valueStack.push(com.teamE.Log10.calculate(valueStack.pop()));
                return;
            }
            else if (temp.equals("Sin")) {
                valueStack.push(com.teamE.Sine.calculate(valueStack.pop(),radians));
                return;
            }
            else if (temp.equals("e^")) {
                valueStack.push(com.teamE.ExpFunction.calculate(valueStack.pop()));
                return;
            }
            else if (temp.equals("√")) {
                valueStack.push(com.teamE.SquareRoot2.calculate(valueStack.pop()));
                return;
            }
            /*else if (temp.equals("10^")) {
                valueStack.push(com.teamE.PowerOfTen.calculate(valueStack.pop()));
                return;
            }*/

        }

        if (valueStack.size()<2){
            throw new ParseException("Err, msng oprnds", opStack.size()+valueStack.size());
        }
        double y = valueStack.pop();
        double x = valueStack.pop();
        char temp = opStack.pop().charAt(0);
        switch(temp)
        {
            case '+':
                valueStack.push(x+y);
                break;
            case '-':
                valueStack.push(x-y);
                break;
            case '×':
                valueStack.push(x*y);
                break;
            case '÷':
                valueStack.push(x/y);
                break;
            case '^':
                valueStack.push(com.teamE.PowerFunction.calculate(x,y));
                break;
        }
    }

    // returns operator precedence in order to verify if the next operator should be stacked
    // or we should apply the next operation
    private static int precedence(String op)
    {
        if (op.equals("Log10") || op.equals("Sin") || op.equals("e^") ||
                op.equals("√") || op.equals("10^") || op.equals("^") )
            return 3;
        else if (op.equals("×") || op.equals("÷"))
            return 2;
        else if (op.equals("+") || op.equals("-"))
            return 1;
        else if (op.equals("(")); // includes functions
            return 0;
    }

    // expression should match regex ((\(|(fun))*((d\+)|(d\)*)))*d\)*
    // where fun is anyone of the functions, + is any operator and
    // d = <hyphen>?\d*(\.\d+)?<pi>?
    private static boolean validateExpression(String expression){
        String fun = "((Sin\\()|(Log10\\()|(√\\()|(10\\^\\())"; // removed (e\^\()|
        String operand = "((-?\\d*\\.?\\d+(E\\d+)?)" +
                "|(-?\\d*(\\.?(\\d+E)?\\d+)?π)" +
                "|(-?\\d*(\\.?(\\d+E)?\\d+)?e)"+
                "|(-?\\d*(\\.?(\\d+E)?\\d+)?\\[M\\])"+
                "|(-?\\d*(\\.?(\\d+E)?\\d+)?\\[Ans\\]))";//(-?\d*\.?\d*e)
        String operator = "((\\+)|(-)|(×)|(÷)|(\\^))";
        String s0 = "(\\(|("+fun+"))";
        String regex = "("+s0+"*)(("+operand+"\\)*("+operator+"|\\)|"+s0+")"+s0+"*)*)"+operand+"?";//("+operand+"\\)*)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(expression);
        return m.matches();
    }

    // validate that our expression is properly formatted, we could use IllegalFormatExpression but
    // the String is an argument to create the class and we can give more meaningful information
    // with IllegalArgumentException
    private static boolean validateParenthesis(String infix)
    {
        boolean isWellParenthesized = true;
        Stack<Character> parenthesisStack = new Stack<>();
        String parenthesisString = infix.replaceAll("[^\\(\\)]*", "");
        for (int i = 0; i < parenthesisString.length(); i++)
        {
            if (parenthesisString.charAt(i) == '(')
                parenthesisStack.push('(');
            else if (parenthesisStack.isEmpty())
            {
                isWellParenthesized = false;
                break;
            }
            else
                parenthesisStack.pop();
        }
        if (!parenthesisStack.isEmpty())
        {
            isWellParenthesized = false;
        }
        return isWellParenthesized;
    }
}
