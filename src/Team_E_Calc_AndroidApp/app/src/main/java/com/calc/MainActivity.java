/**
 * Written by Team-E for COMP 5541, calculator project
 * Winter 2016
 *
 * This class handles the main view of the calculator, it calls InputHandler (controller)
 * for input and ExpressionEvaluator (controller) to handle evaluation
 *
 * Pushing buttons calls the appropriate methods in the controllers
 */
package com.calc;

import android.graphics.Typeface;
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

import com.example.friketrin.calc.R;

public class MainActivity extends AppCompatActivity {

    // TODO Memory, access history et al
    // TODO implement a landscape layout and possibly bigger screen layouts

    private boolean vibrate = true;
    private boolean copy = true;
    private boolean isExpressionActive = true;
    private static final int MIN_DURATION_OF_LONG_TOUCH = 1000;//

    /**Initialization called when the activity is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // TODO what do we need to change here?
        ExpressionHistory.appendEntry(""); // NOTE try to minimize the view calling the mode
        EditText text = (EditText) findViewById(R.id.textView);
        text.setOnTouchListener(otl);
        /* for some reason the xml value doesn't work with galaxy s3 (at least)
        * since it's the phone we have available for testing
        * so setting this flag ensures that suggestions are off.
        * http://stackoverflow.com/questions/1959576/turn-off-autosuggest-for-edittext
        * better this than input of password type (which is a nasty hack) */
        text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        // Change the font to ensure character's such as U - 232b
        Button backspace = (Button)findViewById(R.id.buttonBackspace);
        Typeface font = Typeface.createFromAsset(getAssets(), "DejaVuSans.ttf");
        backspace.setTypeface(font);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem vibrateToggle = menu.findItem(R.id.menu_vibrate);
        vibrateToggle.setChecked(vibrate);
        MenuItem radioItem;
        if(ExpressionEvaluator.getRadians())
            radioItem = menu.findItem(R.id.menu_rad);
        else
            radioItem = menu.findItem(R.id.menu_deg);
        radioItem.setChecked(true);
        return true;
    }

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
                // about was selected // TODO handle this with a little pop-up
                // TODO maybe include a link to the help doc?
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Override ontouch to disable soft keyboard and handle cursor position
    protected View.OnTouchListener otl = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View text, MotionEvent event) {
            final InputMethodManager imm = ((InputMethodManager) getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE));
            try{
                imm.hideSoftInputFromWindow(text.getApplicationWindowToken(), 0);
            }catch(Exception e){
                e.printStackTrace();
            }
            if(!isExpressionActive){
                return true; // if the expression is not active, we shouldn't do anything
            }
            float x = event.getX();
            float y = event.getY();
            int touchPosition = ((EditText)text).getOffsetForPosition(x, y);
            InputHandler.setCurrIndex(touchPosition);
            populateDisplay();
            if(event.getAction()==MotionEvent.ACTION_DOWN)
                copy = true;
            else if(event.getEventTime()-event.getDownTime() > MIN_DURATION_OF_LONG_TOUCH && copy){
                copyToClipboard(text);
                copy = false;
            }
            // else - nothing to do
            return true;
        }
    };
    protected void copyToClipboard(View text){
        String expression = ((EditText)text).getText().toString();
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip =
                android.content.ClipData.newPlainText("Expression copied from Eternity",
                        expression);
        clipboard.setPrimaryClip(clip);
        // now notify the user
        CharSequence notification = "Copied expression to clipboard";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), notification, duration);
        toast.show();
    }

    // just the messenger, provides an interface between the view and the model
    public void sendMessage(View view){
        Button button = (Button)view;
        boolean success = InputHandler.input(button.getText().toString());
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
    }

    public void evaluateExpression(View view){
        String result = ExpressionEvaluator.evaluate();
        TextView resultView = (TextView) findViewById(R.id.resultView);
        if (!result.isEmpty()) { // TODO temporary test
            resultView.setText(result);
            setExpressionActive(false);
        }
        if(vibrate && !result.isEmpty()) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
    }

    public void plusMinus(View view){
        InputHandler.plusMinus();
        populateDisplay();
    }

    public void clearExpression(View view){
        boolean success = InputHandler.clear();
        if(vibrate && success) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
        ResultBuffer.clear();
        TextView resultView = (TextView) findViewById(R.id.resultView);
        resultView.setText("" + ResultBuffer.getResult());
    }

    public void backspace(View view){
        boolean success = InputHandler.backspace();
        if(vibrate && success) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
    }

    public void left(View view){
        boolean success = InputHandler.moveLeft();
        if(vibrate && success) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
    }

    public void right(View view){
        boolean success = InputHandler.moveRight();
        if(vibrate && success) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
    }

    public void up(View view){
        // maybe we want the user to know that the expression on the screen is the prior expression
        /*if( !((EditText)view).getText().toString().equals(ExpressionHistory.getEntry())
                && ExpressionHistory.getEntry().isEmpty() ){ // we've evaluated an expression
            InputHandler.moveUp();
        }*/
        boolean success = InputHandler.moveUp();
        if(vibrate && success) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
    }

    public void down(View view){
        boolean success = InputHandler.moveDown();
        if(vibrate && success) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
    }

    //Set the memory
    public void setMemory(View view){
        EditText text = (EditText)findViewById(R.id.textView);
        InputHandler.setMemory(text.getText().toString());
        if(vibrate) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
    }

    private void populateDisplay(){ // check if we need to populate the display with new content
        if (ExpressionHistory.refreshDisplay) {
            EditText text = (EditText) findViewById(R.id.textView);
            text.setText(ExpressionHistory.getEntry());
            text.setSelection(InputHandler.getCurrIndex());
            ExpressionHistory.refreshDisplay = false;
            setExpressionActive(true);
        }
    }

    private void setExpressionActive(boolean isActive) {
        EditText expressionView = (EditText) findViewById(R.id.textView);
        isExpressionActive = isActive;
        if(isActive){
            expressionView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_text_active));
        }
        else{
            expressionView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.edit_text_inactive));
        }
    }


    /* copy text to clipboard
    * from http://stackoverflow.com/questions/19253786/how-to-copy-text-to-clip-board-in-android
    * entry by meow meo
    * *//*
    private void setClipboard(Context context,String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }*/
}
