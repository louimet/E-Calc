package com.example.friketrin.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Vibrator;
import android.content.Context;

public class MainActivity extends AppCompatActivity {

    // TODO Rad/deg toggle button
    // TODO vibration disable option
    // TODO Memory, access history et al
    // TODO implement a landscape layout and possibly bigger screen layouts

    // Declare constants for a mask that tells us what keys can be input depending on previous input
    private static final short ALLOWDIGIT = 1; // How annoying, Java has no unsigned byte
    private static final short ALLOWDOT = 1<<1;
    private static final short ALLOWPI = 1<<2;
    private static final short ALLOWOPERAND = ALLOWDIGIT | ALLOWDOT | ALLOWPI;
    private static final short ALLOWMODIF = 1<<3; // plusMinus key
    private static final short ALLOWLPAR = 1<<4;
    private static final short ALLOWRPAR = 1<<5;
    private static final short ALLOWPAR = ALLOWLPAR | ALLOWRPAR;
    private static final short ALLOWOPERATOR = 1<<6;
    private static final short ALLOWFUN = 1<<7;
    private static final short SZALLOW = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;
    private short INPUTFLOWMASK;
    private short parenthesisOpen;
    private boolean isZS;

    /**Initialization called when the activity is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INPUTFLOWMASK = SZALLOW; // Allow what needs to be allowed in Zero State
        parenthesisOpen = 0; // we're keeping track of these
        isZS = true; // keep a flag for Zero State
    }


    /** Called when the user touches the button */
    public void sendMessage(View view) {

        String s = parseMessage(view);

        if (s.isEmpty()) return;

        EditText text = (EditText)findViewById(R.id.textView);

        Vibrator vibe;
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);
        text.setText(s);

        // Do something in response to button click
    }

    /**Handles button input and outputs to the display of the input is valid*/
    private String parseMessage(View view){

        // buttonClear - back to Zero State
        if (view.getId() == R.id.buttonClear) {
            INPUTFLOWMASK = SZALLOW;
            parenthesisOpen = 0;
            isZS = true;
            return "0.0";
        }

        // for most everything else, we're interested in what's on the display, get it
        EditText text = (EditText)findViewById(R.id.textView);
        String expression;

        expression = text.getText().toString(); // get whatever is on the screen

        // button EVAL
        if (view.getId() == R.id.buttonEval) {
            if (isZS) return(""); // can't evaluate a zero state expression
            String result = ExpressionEvaluator.Evaluate(expression);

            if(result.matches("-?\\d+\\.?\\d*$")) { // We're OK, we got a number from the evaluation
                INPUTFLOWMASK =  ALLOWPI | ALLOWMODIF | ALLOWLPAR | ALLOWRPAR | ALLOWOPERATOR | ALLOWFUN;
                parenthesisOpen = 0;
            }
            else{ // Something went wrong, pass the message but return to ZeroState
                INPUTFLOWMASK = SZALLOW;
                parenthesisOpen = 0;
                isZS = true;
                // return("Err"); // we could go the uninformative way...
            }

            return(result);
        }

        // check if we're getting an operand (or an unknown, which pops up as -1)
        int buttonNum  = parseButton(view.getId());

        // ---------- MODIFIER -------------
        if (view.getId() == R.id.buttonPlusMinus) { // the plus minus key
            if ((INPUTFLOWMASK & ALLOWMODIF)== 0) return ""; // we can't press this key now
            // TODO get last regex matching an operand and toggle plus minus - toggle parenthesis?
            // TODO figure this one out
            // TODO remember +/- should not work on 0
        }
        // ------- END MODIFIER -------

        // ---------- OPERATOR -------------
        if (view.getId() == R.id.buttonPlus || view.getId() == R.id.buttonMinus
                || view.getId() == R.id.buttonTimes || view.getId() == R.id.buttonDivide) {
            if ((INPUTFLOWMASK & ALLOWOPERATOR) == 0) return "";
            INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;
            Button b = (Button) view;
            expression = (isZS)? "" : expression;// If we're here from a ZS, remove 0.0
            isZS = false;
            return expression + b.getText().toString();
        }
        // ------- END OPERATOR -------

        // ------- PARENTHESIS -------
        if (view.getId() == R.id.buttonLpar ) {
            if ((INPUTFLOWMASK & ALLOWLPAR) == 0) return "";
            INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;
            parenthesisOpen++;
            expression = (isZS)? "" : expression;// If we're here from a ZS, remove 0.0
            isZS = false;
            return expression + "(";
        }
        if (view.getId() == R.id.buttonRpar) {
            if ((INPUTFLOWMASK & ALLOWRPAR) == 0 || parenthesisOpen <= 0) return "";
            INPUTFLOWMASK = ALLOWOPERAND | ALLOWPAR | ALLOWOPERATOR | ALLOWFUN;
            parenthesisOpen--;
            expression = (isZS)? "" : expression;// If we're here from a ZS, remove 0.0
            isZS = false;
            return expression + ")";
        }
        // -----END PARENTHESIS ------

        // --------- FUNCTION --------
        if (view.getId() == R.id.buttonSin) {
            if ((INPUTFLOWMASK & ALLOWFUN) == 0) return "";
            INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;
            parenthesisOpen++;
            expression = (isZS)? "" : expression;// If we're here from a ZS, remove 0.0
            isZS = false;
            return expression + "Sin(";
        }

        if (view.getId() == R.id.buttonLog10) {
            if ((INPUTFLOWMASK & ALLOWFUN) == 0) return "";
            INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;
            parenthesisOpen++;
            expression = (isZS)? "" : expression;// If we're here from a ZS, remove 0.0
            isZS = false;
            return expression + "Log10(";
        }
        if (view.getId() == R.id.buttonExp) {
            if ((INPUTFLOWMASK & ALLOWFUN) == 0) return "";
            INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;
            parenthesisOpen++;
            expression = (isZS)? "" : expression;// If we're here from a ZS, remove 0.0
            isZS = false;
            return expression + "e^(";
        }
        // --------END FUNCTION-------

        // ----------BACKSPACE------- This is pretty involved...
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

            if (newLast == '×' || newLast == '÷' || newLast == '+' || newLast == '-')
                INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;

            if (newLast == '(')
                INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWFUN;

            if (newLast == ')') {
                INPUTFLOWMASK = ALLOWOPERAND | ALLOWLPAR | ALLOWOPERATOR | ALLOWFUN;
                if (parenthesisOpen < 0) INPUTFLOWMASK |= ALLOWRPAR;
            }

            return expression.substring(0, length-1);
        }
        // -------END BACKSPACE------

        // --------  OPERAND --------
        // DIGIT
        String s = "";
        boolean isOperand = false;
        if ( buttonNum >=0 && buttonNum < 0xa ) {
            if ((INPUTFLOWMASK & ALLOWDIGIT)== 0) return "";
            INPUTFLOWMASK |= ALLOWDIGIT | ALLOWPI | ALLOWMODIF | ALLOWLPAR | ALLOWRPAR | ALLOWOPERATOR | ALLOWFUN;
            s = Integer.toString(buttonNum);
            isOperand = true;
        } // DECIMAL POINT
        else if (buttonNum == 0xa){
            if ((INPUTFLOWMASK & ALLOWDOT)== 0) return "";
            INPUTFLOWMASK = ALLOWDIGIT; // only digits can go after
            s = ".";
            isOperand = true;
        } // PI
        else if (buttonNum == 0xb){
            if ((INPUTFLOWMASK & ALLOWPI)== 0) return "";
            INPUTFLOWMASK = ALLOWMODIF | ALLOWLPAR | ALLOWRPAR | ALLOWOPERATOR | ALLOWFUN;
            s = "π";
            isOperand = true;
        }
        if (isOperand) {
            expression = (isZS) ? "" : expression;// If we're here from a ZS, remove 0.0
            isZS = false;
            return expression + s;
        }
        // ------- END OPERAND -------

        return ""; // we found no match, return an empty string
    }

    // Returns a number for the number buttons as well as dot and pi
    private int parseButton(int id){
        switch (id) {
            case R.id.button0:
                return 0;
            case R.id.button1:
                return 1;
            case R.id.button2:
                return 2;
            case R.id.button3:
                return 3;
            case R.id.button4:
                return 4;
            case R.id.button5:
                return 5;
            case R.id.button6:
                return 6;
            case R.id.button7:
                return 7;
            case R.id.button8:
                return 8;
            case R.id.button9:
                return 9;
            case R.id.buttonDot:
                return 0xa;
            case R.id.buttonPi:
                return 0xb;
            default: // Why are we here?
                return -1;
        }
    }

}
