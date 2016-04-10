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
        int lastEntry;
        boolean isWellParenthesized;
        Queue<String> tokenQueue;
        String expression = ExpressionHistory.getEntry(currEntry);
        if((currEntry == 0) && expression.isEmpty()){
            return("");
        }
        lastEntry = ExpressionHistory.getSize() - 1;
        if (expression.equals("")) {

            // empty string should only happen on the last entry
            assert (currEntry == lastEntry);
            expression = ExpressionHistory.getEntry(lastEntry - 1);
        }
        isWellParenthesized = validateParentheses(expression);
        if (!isWellParenthesized) {
            return ("Err: mismatched parentheses");
        } else if (!validateExpression(expression)) {
            return ("Err: incorrect syntax");
        } /* else */
        tokenQueue = tokenize(expression);
        Double result;
        try {
            result = evaluateInfix(tokenQueue);
        } catch(ParseException e) {
            return e.getMessage();
        }

        // create a new entry in the history
        ExpressionHistory.appendEntryToHistory("");
        InputHandler.resetCurrPosition();
        Memory.setLastAnswer(result);
        ResultBuffer.setResult(result);
        return (result.toString());
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


    /** Separates expression into atomic components and places them in a queue
     * of strings.
     * @param expression complete expression
     * @return Queue<String> container filled with tokens */
    private static Queue<String> tokenize(String expression) {
        Queue<String> tokenQueue = new LinkedList<>();
        String s = expression; // work on a copy and keep the original
        final int expressionLength = s.length();
        while (!s.isEmpty()){
            if (s.matches("Log10\\(.*")) {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("Log10");
                tokenQueue.add("(");
                s = s.substring(6); // include parenthesis in deleted token
                continue;
            }
            if (s.matches("Sin\\(.*")) {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("Sin");
                tokenQueue.add("(");
                s = s.substring(4); // include parenthesis in deleted token
                continue;
            }
            if (s.matches("e\\^.*")) {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("e^");
                s = s.substring(2);
            }
            if (s.matches("√\\(.*")) {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("√");
                tokenQueue.add("(");
                s = s.substring(2); // include parenthesis in deleted token
            }

            if (s.charAt(0) == '(') {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = expression.charAt(lengthDifference-1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("(");
                s = s.substring(1);
                continue;
            }

            if (s.charAt(0) == ')') {
                tokenQueue.add(")");
                s = s.substring(1);
                continue;
            }

            if ((s.charAt(0) - '0' < 10 && s.charAt(0) - '0' >= 0)
                    || s.charAt(0) == '.') {
                int i = 1;
                while (s.length() > i &&
                        (((s.charAt(i) - '0' < 10) && (s.charAt(i) - '0' >= 0))
                                || s.charAt(i) == '.')) {
                    i++;
                }
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0 ) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (precedingChar == ')') {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add(s.substring(0, i));
                s = s.substring(i);
                continue;
            }

            if (s.charAt(0) == 'π') {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0 ) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("π");
                s = s.substring(1);
                continue;
            }

            if (s.charAt(0) == 'e') {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0 ) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("e");
                s = s.substring(1);
                continue;
            }

            if (s.matches("\\[M\\].*")){
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0) {
                    char precedingChar
                            = expression.charAt(lengthDifference - 1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("M");
                s = s.substring(3);
                continue;
            }

            if (s.matches("\\[Ans\\].*")){
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0){
                    char precedingChar = expression.charAt(lengthDifference -1);
                    if (isOperandChar(precedingChar)) {
                        tokenQueue.add("×");
                    }
                }
                tokenQueue.add("Ans");
                s = s.substring(5);
                continue;
            }

            /*
             * operator - if minus preceded by left parenthesis or operator,
             * multiply by -1
             */
            if (s.charAt(0) == '-') {
                int lengthDifference = expressionLength - s.length();
                if (lengthDifference > 0 ){
                    char precedingChar
                            = expression.charAt(lengthDifference-1);
                    if ((precedingChar != '(')
                            && (precedingChar != '+')
                            && (precedingChar != '-')
                            && (precedingChar != '×')
                            && (precedingChar != '÷')
                            && (precedingChar != '^')){
                        tokenQueue.add("-");
                    } else {
                        tokenQueue.add("-1");
                        tokenQueue.add("×");
                    }
                } else if (lengthDifference == 0) {
                    tokenQueue.add("-1");
                    tokenQueue.add("×");
                }
                s = s.substring(1);
                continue;
            }
            if (s.charAt(0) == '+'
                    || s.charAt(0) == '×'
                    || s.charAt(0) == '÷'
                    || s.charAt(0) == '^') {
                tokenQueue.add(s.substring(0, 1));
                s = s.substring(1);
                continue;
            }
        }
        return tokenQueue;
    }

    /** Evaluate the tokenized infix expression a token at a time, calling
     * precedence() and applyOperation() accordingly, depending on
     * precedence and parentheses. The function can raise ParseException
     *
     * @param tokenQueue the queue of tokens in String form
     * @return the result of the evaluation as a Double
     */
    private static Double evaluateInfix(Queue<String> tokenQueue)
            throws ParseException {
        Stack<Double> valueStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        while (!tokenQueue.isEmpty()) {
            String temp = tokenQueue.remove();
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
                operatorStack.push(temp);
            } else if (temp.equals(")")) {
                while(!operatorStack.peek().equals("(")) {
                    applyOperation(valueStack, operatorStack);
                }
                operatorStack.pop(); // discard lingering left parenthesis
            } else if (temp.equals("Log10") || temp.equals("Sin")
                    || temp.equals("×") || temp.equals("÷") || temp.equals("+")
                    || temp.equals("-") || temp.equals("^") || temp.equals("e^")
                    || temp.equals("√") || temp.equals("10^")) {
                int incomingPrecedence = precedence(temp);
                while (!operatorStack.isEmpty()
                        && (precedence(operatorStack.peek())
                        >= incomingPrecedence)) {
                    applyOperation(valueStack, operatorStack);
                }
                operatorStack.push(temp);
            }
        }
        while (!operatorStack.isEmpty()) {
            applyOperation(valueStack, operatorStack);
        }
        if (valueStack.size() > 1) {
            throw new ParseException("Err: no operator",
                    operatorStack.size() + valueStack.size());
        }
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
                valueStack.push(
                        com.teamE.Log10.calculate(valueStack.pop()));
                return;
            } else if (temp.equals("Sin")) {
                valueStack.push(
                        com.teamE.Sine.calculate(valueStack.pop(),radians));
                return;
            } else if (temp.equals("e^")) {
                valueStack.push(
                        com.teamE.ExpFunction.calculate(valueStack.pop()));
                return;
            } else if (temp.equals("√")) {
                valueStack.push(
                        com.teamE.SquareRoot.calculate(valueStack.pop()));
                return;
            }
        }

        if (valueStack.size()<2) {
            throw new ParseException("Err: no operand",
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
        String function = "((Sin\\()|(Log10\\()|(√\\()|(10\\^\\())";
        String operand = "(-?(\\d*\\.?\\d+(E\\d+)?)?(π|e|\\[M\\]|\\[Ans\\])*)";
        String operator = "((\\+)|(-)|(×)|(÷)|(\\^))";
        String s0 = "(\\(|("+function+"))";
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
        Stack<Character> parenthesesStack = new Stack<>();
        String parenthesesString = expression.replaceAll("[^\\(\\)]*", "");
        for (int i = 0; i < parenthesesString.length(); i++) {
            if (parenthesesString.charAt(i) == '(') {
                parenthesesStack.push('(');
            } else { // we have a closing parenthesis
                if (parenthesesStack.isEmpty()) {
                    return false;
                } else {
                    parenthesesStack.pop();
                }
            }
        }
        return parenthesesStack.isEmpty();
    }
}
