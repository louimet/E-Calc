/*
 * Written by Team E for COPM 5541, calculator project
 * Winter 2016
 */
package com.calc;
import java.text.ParseException;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ExpressionEvaluator is a controller in the MVC sense. It parses the
 * expression contained in the calculator's expression buffer (Model) and
 * translates it into something that the model can use for evaluation.
 * It evaluates for correctness and then tokenizes the expression: separating
 * operands, operators and and functions for placement onto stacks(performing
 * a translation of operands from Strings to Doubles, ). It finally calls on the
 * library(M) or built-in operations for evaluation by popping the value stack
 * and the operator stack accordingly.
 * <p>
 * It has a three public methods for interfacing: evaluate(string) : string
 * and a setter/getter for the radians boolean value. The latter is a member
 * used to determine if trigonometric calculations are performed on radians or
 * degrees
 */

public class ExpressionEvaluator {

    /** used to determine if trigonometric calculations are performed on radians
     *  or degrees */
    private static boolean radians = true;

    /** @return boolean, true if trigonometric functions operate on radians
     * false for degrees */
    public static boolean getRadians() {
        return radians;
    }

    /** @param isRadians true if trigonometric functions operate on
     * radians false for degrees */
    public static void setRadians(boolean isRadians) {
        radians = isRadians;
    }

    /** retrieves ExpressionHistory's current entry and evaluates it.
     * @return String with the evaluated expression, appropriate error message
     * or an empty string if there is no expression to evaluate */
    public static String evaluate() {
        int currEntry = ExpressionHistory.getCurrEntryIndex();
        String expression = ExpressionHistory.getCurrEntry(currEntry);
        if((currEntry == 0) && expression.isEmpty()){
            return("");
        }
        int lastEntry = ExpressionHistory.getSize()-1;
        if (expression.equals("")) {

            // empty string should only happen on the last entry
            assert (currEntry == lastEntry);
            expression = ExpressionHistory.getCurrEntry(lastEntry - 1);
        }
        boolean isWellParenthesized = validateParentheses(expression);
        if (!isWellParenthesized) {
            return ("Err prnths");
        } else if (!validateExpression(expression)) {
            return ("Err syntax");
        } /* else */
        Queue<String> infixTokenQueue = tokenize(expression);
        Double result;
        try {
            result = evaluateInfix(infixTokenQueue);
        } catch(ParseException e) {
            return e.getMessage();
        }

        // open a new entry in the history
        ExpressionHistory.appendEntryToHistory("");
        InputHandler.resetCurrPosition();
        Memory.setLastAnswer(result);
        ResultBuffer.setResult(result);
        return (result.toString());
    }

    /** Separates expression into atomic components and places them in a queue
     * of strings.
     * @param infixExpression complete infix expression
     * @return Queue<String> container filled with tokens */
    private static Queue<String> tokenize(String infixExpression) {
        Queue<String> infixTokenQueue = new LinkedList<>();
        String s = infixExpression; // work on a copy and keep the original
        final int exprLength = s.length();
        while (!s.isEmpty()){
            if (s.matches("Log10\\(.*")) {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("Log10");
                infixTokenQueue.add("(");
                s = s.substring(6); // include parenthesis in deleted token
                continue;
            }
            if (s.matches("Sin\\(.*")) {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("Sin");
                infixTokenQueue.add("(");
                s = s.substring(4); // include parenthesis in deleted token
                continue;
            }
            if (s.matches("e\\^.*")) {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("e^");
                s = s.substring(2);
            }
            if (s.matches("√\\(.*")) {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("√");
                infixTokenQueue.add("(");
                s = s.substring(2); // include parenthesis in deleted token
            }

            if (s.charAt(0) == '(') {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("(");
                s = s.substring(1);
                continue;
            }

            if (s.charAt(0) == ')') {
                infixTokenQueue.add(")");
                s = s.substring(1);
                continue;
            }

            if ((s.charAt(0)-'0' < 10 && s.charAt(0)-'0' >= 0)
                    || s.charAt(0) == '.') {
                int i = 1;
                while (s.length() > i &&
                        (((s.charAt(i)-'0' < 10) && (s.charAt(i)-'0' >= 0))
                                || s.charAt(i) == '.')) {
                    i++;
                }
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (precedingChar == ')') {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add(s.substring(0,i));
                s = s.substring(i);
                continue;
            }

            if (s.charAt(0) == 'π') {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("π");
                s = s.substring(1);
                continue;
            }

            if (s.charAt(0) == 'e') {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("e");
                s = s.substring(1);
                continue;
            }

            if (s.matches("\\[M\\].*")){
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("M");
                s = s.substring(3);
                continue;
            }

            if (s.matches("\\[Ans\\].*")){
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0){
                    char precedingChar = infixExpression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        infixTokenQueue.add("×");
                    }
                }
                infixTokenQueue.add("Ans");
                s = s.substring(5);
                continue;
            }

            // operator - if minus preceded by Lpar or operator, mult by -1
            if (s.charAt(0) == '-') {
                int lengthDifference = exprLength - s.length();
                if (lengthDifference > 0 ){
                    char precedingChar
                            = infixExpression.charAt(lengthDifference-1);
                    if ((precedingChar != '(')
                            && (precedingChar != '+')
                            && (precedingChar != '-')
                            && (precedingChar != '×')
                            && (precedingChar != '÷')
                            && (precedingChar != '^')){
                        infixTokenQueue.add("-");
                    } else {
                        infixTokenQueue.add("-1");
                        infixTokenQueue.add("×");
                    }
                } else if (lengthDifference == 0) {
                    infixTokenQueue.add("-1");
                    infixTokenQueue.add("×");
                }
                s = s.substring(1);
                continue;
            }
            if (s.charAt(0) == '+'
                    || s.charAt(0) == '×'
                    || s.charAt(0) == '÷'
                    || s.charAt(0) == '^') {
                infixTokenQueue.add(s.substring(0,1));
                s = s.substring(1);
                continue;
            }
        }
        return infixTokenQueue;
    }

    /** Helper Function to aid in tokenization of the expression
     *
     * @param c character to check
     * @return true if the character is an operand or ) ]
     */
    private static boolean isOperandChar(char c) {
        return (Character.isDigit(c)
                || (c == 'e') || (c == 'π')
                || (c == ']') || (c == ')'));
    }

    /** Evaluate the tokenized infix expression a token at a time, calling
     * precedence() and applyOperation() accordingly, depending on
     * precedence and parentheses. The function can raise ParseException
     *
     * @param infixTokenQueue the queue of tokens in String form
     * @return the result of the exaluation as a Double
     */
    private static Double evaluateInfix(Queue<String> infixTokenQueue)
            throws ParseException {
        Stack<Double> valueStack = new Stack<>();
        Stack<String> opStack = new Stack<>();

        while (!infixTokenQueue.isEmpty()) {
            String temp = infixTokenQueue.remove();
            if(temp.matches("^-?\\d*[\\d\\.]\\d*$")) {
                valueStack.push(Double.parseDouble(temp));
            } else if(temp.equals("π")) {
                valueStack.push(com.teamE.Pi.PI);
            } else if(temp.equals("e")) {
                valueStack.push(com.teamE.ExpFunction.calculate());
            } else if (temp.equals("M")){
                valueStack.push(Memory.getMemoryBuffer());
            } else if (temp.equals("Ans")){
                valueStack.push(Memory.getLastAnswer());
            } else if (temp.endsWith("(")) {
                opStack.push(temp);
            } else if (temp.equals(")")) {
                while(!opStack.peek().equals("(")) {
                    applyOperation(valueStack, opStack);
                }
                opStack.pop(); // discard lingering left parenthesis
            } else if (temp.equals("Log10") || temp.equals("Sin")
                    || temp.equals("×") || temp.equals("÷") || temp.equals("+")
                    || temp.equals("-") || temp.equals("^") || temp.equals("e^")
                    || temp.equals("√") || temp.equals("10^")) {
                int incomingOp = precedence(temp);
                while (!opStack.isEmpty()
                        && (precedence(opStack.peek()) >= incomingOp)) {
                    applyOperation(valueStack, opStack);
                }
                opStack.push(temp);
            }
        }
        while (!opStack.isEmpty()) {
            applyOperation(valueStack, opStack);
        }
        if (valueStack.size() > 1)
            throw new ParseException("Err, Msng oprtrs",
                    opStack.size()+valueStack.size());
        return valueStack.pop();
    }

    /** Apply whatever operation is waiting in the stacks
     *
     * @param valueStack
     * @param opStack
     * @throws ParseException
     */
    private static void applyOperation(Stack<Double> valueStack,
                                       Stack<String> opStack)
            throws ParseException {

        // function?
        if (opStack.peek().length() > 1 || opStack.peek().equals("√")) {
            String temp = opStack.pop();
            if (temp.equals("Log10")) {
                valueStack.push(com.teamE.Log10.calculate(valueStack.pop()));
                return;
            } else if (temp.equals("Sin")) {
                valueStack.push(com.teamE.Sine.calculate(valueStack.pop(),radians));
                return;
            } else if (temp.equals("e^")) {
                valueStack.push(com.teamE.ExpFunction.calculate(valueStack.pop()));
                return;
            } else if (temp.equals("√")) {
                valueStack.push(com.teamE.SquareRoot2.calculate(valueStack.pop()));
                return;
            }
        }

        if (valueStack.size()<2) {
            throw new ParseException("Err, msng oprnds",
                    opStack.size() + valueStack.size());
        }
        double y = valueStack.pop();
        double x = valueStack.pop();
        char temp = opStack.pop().charAt(0);
        switch(temp) {
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
            default:
                ; // why are we here? assert something?
        }
    }

    /**
     * returns operator precedence in order so we can verify if the next
     * operator should be stacked or if we should apply the next operation.
     *
     *  @param op operator
     *  @return level of precedence
     */
    private static int precedence(String op) {
        if (op.equals("Log10") || op.equals("Sin")
                || op.equals("e^") || op.equals("√")) {
            return 4;
        } else if (op.equals("^")) {
            return 3;
        } else if (op.equals("×") || op.equals("÷")) {
            return 2;
        } else if (op.equals("+") || op.equals("-")) {
            return 1;
        } else if (op.equals("(")) {
            return 0;
        } else {
            return -1; // we shouldn't be here
        }
    }

    /**
     * Validates the syntax of the expression to be evaluated via pattern
     * matching. See the project documentation for a finite state machine
     * representation
     *
     * @param expression expression to be evaluated
     * @return true if the expression is in a valid syntax, false otherwise
     */
    private static boolean validateExpression(String expression) {
        String fun = "((Sin\\()|(Log10\\()|(√\\()|(10\\^\\())";
        String operand = "(-?(\\d*\\.?\\d+(E\\d+)?)?(π|e|\\[M\\]|\\[Ans\\])*)";
        String operator = "((\\+)|(-)|(×)|(÷)|(\\^))";
        String s0 = "(\\(|("+fun+"))";
        String regex                    // XXX do not break the following line
                = "("+s0+"*)(("+operand+"\\)*("+operator+"|\\)|"+s0+")" +s0+"*)*)"+operand+"?";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(expression);
        return m.matches();
    }

    /**
     * Validate the correctness of the parentheses in the expression
     *
     * @param expression expression to be evaluated
     * @return true if the expression properly parenthesized, false otherwise
     */
    private static boolean validateParentheses(String expression) {
        boolean isWellParenthesized = true;
        Stack<Character> parenthesisStack = new Stack<>();
        String parenthesisString = expression.replaceAll("[^\\(\\)]*", "");
        for (int i = 0; i < parenthesisString.length(); i++) {
            if (parenthesisString.charAt(i) == '(') {
                parenthesisStack.push('(');
            } else if (parenthesisStack.isEmpty()) {
                isWellParenthesized = false;
                break;
            } else {
                parenthesisStack.pop();
            }
        }

        return parenthesisStack.isEmpty();
    }
}
