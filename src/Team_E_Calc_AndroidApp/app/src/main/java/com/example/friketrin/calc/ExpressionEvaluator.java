package com.example.friketrin.calc;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.lang.Math;
import com.example.friketrin.calc.MySine;

// TODO comments -  Class to parse and compute calculator expressions from team E's calculator for
// dot dot dot

public class ExpressionEvaluator {

    public static String Evaluate(String expression){
        boolean isWellParenthesized = validateParenthesis(expression);
        if (!isWellParenthesized) return ("Err parenthesis.");
        Queue<String> infixTokenQueue = Tokenize(expression);
        Double result = evaluateInfix(infixTokenQueue);
        return (result.toString());
    }

    private static Queue<String> Tokenize(String infixExpression)
    {
        Queue<String> infixTokenQueue = new LinkedList<>();
        String s = infixExpression; // work on a copy and keep the original
        final int exprLength = s.length();
        while (!s.isEmpty()){
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
            if ( s.charAt(0)-'0' < 10 && s.charAt(0)-'0' >= 0){
                int i = 1;
                while ( s.length() > i && ((s.charAt(i)-'0' < 10 && s.charAt(i)-'0' >= 0) || s.charAt(i) == '.') )
                    i++;
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
            //operator - if minus preceded by Lpar, mult by -1
            if (s.charAt(0) == '-'){
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( precedingChar != '(' ) {
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
            if (s.charAt(0) == '+' || s.charAt(0) == '×' || s.charAt(0) == '÷'){
                infixTokenQueue.add(s.substring(0,1));
                s = s.substring(1);
                continue;
            }
            // fun (include lpar) - if preceded by Rpar, digit
            if (s.matches("Log10\\(.*\\)")){
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
            if (s.matches("Sin\\(.*\\)")){
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
            if (s.matches("e\\^\\(.*\\)")){
                int lengthDifference = exprLength - s.length();
                // if the character to be deleted is preceded by digit, pi or Rpar, insert *
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if( (precedingChar-'0' < 10 && precedingChar-'0' >= 0) ||  // preceded by digit
                            precedingChar == 'π' || precedingChar == ')' )
                        infixTokenQueue.add("×");
                }
                infixTokenQueue.add("e^");
                infixTokenQueue.add("(");
                s = s.substring(3); // include parenthesis in token
            }
        }

        return infixTokenQueue;
    }

    // evaluate the tokenized infix expression a token at a time, calling precedence
    // and applyOperation accordingly depending on precedence and parenthesis
    private static Double evaluateInfix(Queue<String> infixTokenQueue)
    {
        Stack<Double> valueStack = new Stack<>();
        Stack<String> opStack = new Stack<>();


        while (!infixTokenQueue.isEmpty())
        {
            String temp = infixTokenQueue.remove();
            if(temp.matches("^-?\\d+\\.?\\d*$"))
            {
                valueStack.push(Double.parseDouble(temp));
            }
            else if(temp.equals("π")) {
                valueStack.push(MyPi.PI);
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
                    temp.equals("+") || temp.equals("-") ||
                    temp.equals("Log10") || temp.equals("Sin") ||
                    temp.equals("e^") )
            {
                int incomingOp = precedence(temp);
                while (!opStack.isEmpty() && (precedence(opStack.peek()) >= incomingOp) )
                    applyOperation(valueStack, opStack);
                opStack.push(temp);
            }
        }
        while (!opStack.isEmpty())
            applyOperation(valueStack, opStack);
        /*if (valueStack.size() > 1) // NOTE for debugging add condition checking on stacks at end
            return Double.NaN;*/
        return valueStack.pop();
    }

    // Apply whatever operation is waiting in the stacks
    private static void applyOperation(Stack<Double> valueStack, Stack<String> opStack)
    {
        if (opStack.peek().length() > 1){ // we have a function
            String temp = opStack.pop();
            if (temp.equals("Log10")) { // TODO replace with team funs
                valueStack.push(Math.log10(valueStack.pop()));
                return;
            }
            else if (temp.equals("Sin")) { // TODO replace with team funs
                valueStack.push(MySine.calculate(valueStack.pop()));
                return;
            }
            else if (temp.equals("e^")) { // TODO replace with team funs
                valueStack.push(Math.exp(valueStack.pop()));
                return;
            }

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
        }
    }

    // returns operator precedence in order to verify if the next operator should be stacked
    // or we should apply the next operation
    private static int precedence(String op)
    {
        if (op.equals("Log10") || op.equals("Sin") || op.equals("e^"))
            return 3;
        else if (op.equals("×") || op.equals("÷"))
            return 2;
        else if (op.equals("+") || op.equals("-"))
            return 1;
        else if (op.equals("(")); // includes functions
            return 0;
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
