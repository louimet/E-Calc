/*
 * Written by Team-E for COMP 5541, calculator project
 * Winter 2016
 */
package com.calc;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.os.Vibrator;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Handles the main view of the calculator, it calls InputHandler (controller)
 * for input and ExpressionEvaluator (controller) to handle evaluation.
 * <p>
 * The class retrieves information from the model: ExpressionHistory and
 * ResultBuffer in order to populate the display
 *
 * Pushing buttons calls the appropriate methods in the controllers
 */

public class MainActivity extends AppCompatActivity {

    private static final int MIN_DURATION_OF_LONG_TOUCH = 1000;
    private static final int ALERT_DURATION = 80;

    private boolean vibrate = true;
    private boolean copy = true;
    private boolean isExpressionActive = true;
    private boolean isDisplayAlert;


    /**
     * Initialization called when the activity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NOTE try to minimize the view calling the mode, but it happens
        ExpressionHistory.appendEntryToHistory("");
        EditText text = (EditText) findViewById(R.id.textView);
        text.setOnTouchListener(otl);
        /* for some reason the xml value doesn't work with galaxy s3 (at least)
        * since it's the phone we have available for testing
        * so setting this flag ensures that suggestions are off.
        * http://stackoverflow.com/questions/1959576/turn-off-autosuggest-for-edittext
        * better this than input of password type (which is a nasty hack) */
        text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        TextView resultView = (TextView)findViewById(R.id.resultView);
        resultView.setOnLongClickListener(olcl);
        // Change the font to ensure displaying Unicode 232b - backspace
        Button backspace = (Button)findViewById(R.id.buttonBackspace);
        Typeface font = Typeface.createFromAsset(getAssets(), "DejaVuSans.ttf");
        backspace.setTypeface(font);
        isDisplayAlert = false;
    }

    /**
     * Menu creation routine
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Preparing the menu for display
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem vibrateToggle = menu.findItem(R.id.menu_vibrate);
        vibrateToggle.setChecked(vibrate);
        MenuItem radioItem;
        if (ExpressionEvaluator.getRadians()) {
            radioItem = menu.findItem(R.id.menu_rad);
        } else {
            radioItem = menu.findItem(R.id.menu_deg);
        }
        radioItem.setChecked(true);
        return true;
    }

    /**
     * Handle interaction with the menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_vibrate:
                vibrate = !vibrate;
                item.setChecked(vibrate);
                return true;
            case R.id.menu_rad:
                ExpressionEvaluator.setRadians(true);
                item.setChecked(true);
                return true;
            case R.id.menu_deg:
                ExpressionEvaluator.setRadians(false);
                item.setChecked(true);
                return true;
            case R.id.menu_about:
                Intent myIntent = new Intent(MainActivity.this, UserManual.class);
                MainActivity.this.startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Disable soft keyboard, handle cursor position and copy expression
     * to clipboard
     */
    protected View.OnTouchListener otl = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View text, MotionEvent event) {
            int touchPosition;
            final InputMethodManager imm
                    = ((InputMethodManager) getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            try {
                imm.hideSoftInputFromWindow(
                        text.getApplicationWindowToken(), 0);
            } catch(Exception e) {
                e.printStackTrace();
            }

            // if isExpressionActive, we shouldn't do anything
            if (!isExpressionActive) {
                return true;
            }

            float x = event.getX();
            float y = event.getY();

            touchPosition = ((EditText)text).getOffsetForPosition(x, y);
            InputHandler.setCursorPosition(touchPosition);
            populateDisplay();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                copy = true;
            } else if((event.getEventTime()-event.getDownTime()
                    > MIN_DURATION_OF_LONG_TOUCH)
                    && copy){
                copyToClipboard(text);
                copy = false;
            } // else - nothing to do
            return true;
        }
    };

    /**
     * Override onLongClick to copy text from the result display
     */
    protected View.OnLongClickListener olcl = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View text) {
            copyToClipboard(text);
            return true;
        }
    };

    /**
     * copies the selected screen's contents to the clipboard and toasts
     * accordingly
     */
    protected void copyToClipboard(View text){
        String clipping = "";
        String typeClip = "";
        if (text.getId() == R.id.textView) {
            clipping = ((EditText) text).getText().toString();
            typeClip = "Expression";
        } else if(text.getId() == R.id.resultView) {
            clipping = ((TextView) text).getText().toString();
            typeClip = "Result";
        }
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip =
                android.content.ClipData.newPlainText(typeClip+
                                " copied from Eternity", clipping);
        clipboard.setPrimaryClip(clip);

        // now notify the user
        CharSequence notification =
                "Copied "+typeClip.toLowerCase()+" to clipboard";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(
                getApplicationContext(), notification, duration);
        toast.show();
    }

    /**
     * just the messenger, provides an interface between the view and the model
    */
    public void sendMessage(View view) {
        Button button = (Button)view;
        boolean success = InputHandler.input(button.getText().toString());
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        } else if(!success) {
            usageAlert();
        }
        populateDisplay();
    }

    /**
     * Call ExpressionEvaluator
     */
    public void evaluateExpression(View view) {
        String result = ExpressionEvaluator.evaluate();
        TextView resultView = (TextView) findViewById(R.id.resultView);
        if (!result.isEmpty()) {
            resultView.setText(result);
            setExpressionActive(false);
        }
        if (vibrate && !result.isEmpty()) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
    }

    /**
     * Relays PlusMinus to InputHandler
     */
    public void plusMinus(View view) {
        boolean success = InputHandler.plusMinus();
        populateDisplay();
        if (vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        if (!success) {
            usageAlert();
        }
    }

    /**
     * Relays Clear to InputHandler
     */
    public void clearExpression(View view){
        boolean success = InputHandler.clear();
        if (vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        /* In addition to success, check if the display will be refreshed since
        * we might have an inactive expression and pressing the arrow actually
        * would have the same effect as clearing - so no alert because we
        * did clear
         */
        if (!success && !ExpressionHistory.refreshDisplay) {
            usageAlert();
        }
        populateDisplay();
        ResultBuffer.clear();
        TextView resultView = (TextView) findViewById(R.id.resultView);
        resultView.setText("" + ResultBuffer.getResult());
    }

    /**
     * Relays Backspace to InputHandler
     */
    public void backspace(View view) {
        boolean success = InputHandler.backspace();
        if (vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        /* In addition to success, check if the display will be refreshed since
        * we might have an inactive expression and pressing the arrow actually
        * would have the same effect as clearing - so no alert because we
        * did clear
         */
        if (!success && !ExpressionHistory.refreshDisplay) {
            usageAlert();
        }
        populateDisplay();
    }

    /**
     * Relays left arrow to InputHandler
     */
    public void left(View view) {
        boolean success = InputHandler.moveLeft();
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        if(!success){
            usageAlert();
        }
        populateDisplay();
    }

    /**
     * Relays right arrow to InputHandler
     */
    public void right(View view) {
        boolean success = InputHandler.moveRight();
        if (vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        if (!success) {
            usageAlert();
        }
        populateDisplay();
    }

    /**
     * Relays up arrow to InputHandler
     */
    public void up(View view) {
        boolean success = InputHandler.moveUp();
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        if (!success) {
            usageAlert();
        }
        populateDisplay();
    }

    /**
     * Relays down arrow to InputHandler
     */
    public void down(View view) {
        boolean success = InputHandler.moveDown();
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        /* In addition to success, check if the display will be refreshed since
        * we might have an inactive expression and pressing the arrow actually
        * would have the same effect as clearing - so no alert because we
        * did clear
         */
        if(!success && !ExpressionHistory.refreshDisplay){
            usageAlert();
        }
        populateDisplay();
    }

    /**
     * Relays memory set to InputHandler
     */
    public void setMemory(View view){
        EditText text = (EditText)findViewById(R.id.textView);
        InputHandler.setMemory(text.getText().toString());
        if(vibrate) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
    }

    /**
     * Verifies if ExpressionHistory has raised a flag signaling the need to
     * refresh the contents of the display. If so it repopulates
     */
    private void populateDisplay(){
        if (ExpressionHistory.refreshDisplay) {
            EditText text = (EditText) findViewById(R.id.textView);
            text.setText(ExpressionHistory.getEntry());
            text.setSelection(InputHandler.getCursorPosition());
            ExpressionHistory.refreshDisplay = false;
            setExpressionActive(true);
        }
    }

    /**
     * Sets the isExpressionActive value and changes the expression display
     * color accordingly, greying out inactive expressions
     * @param isActive boolean indicating if the currently displayed expression
     *                 is active or not
     */
    private void setExpressionActive(boolean isActive) {
        EditText expressionView = (EditText) findViewById(R.id.textView);
        isExpressionActive = isActive;
        if (isActive) {
            expressionView.setTextColor(
                    ContextCompat.getColor(getApplicationContext(),
                            R.color.text_active));
        } else {
            expressionView.setTextColor(
                    ContextCompat.getColor(getApplicationContext(),
                            R.color.text_inactive));
        }
    }

    /**
     * flash the display screens when invalid input is keyed in,
     * principle: feedback is good
     */
    private void usageAlert() {
        isDisplayAlert = true;              // to be toggled by r
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                EditText expressionView =
                        (EditText) findViewById(R.id.textView);
                TextView resultView = (TextView) findViewById(R.id.resultView);
                if (isDisplayAlert) {
                    expressionView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(),
                                    R.color.display_alert));
                    resultView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(),
                                    R.color.display_alert));
                    isDisplayAlert = false; // the alert has been sounded...
                } else {
                    expressionView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(),
                                    R.color.display_normal));
                    resultView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(),
                                    R.color.display_normal));
                }
            }
        };
        handler.post(r);                        // call with isDisplayAlert = T
        handler.postDelayed(r, ALERT_DURATION); // isDisplayAlert will be F then
    }

}
