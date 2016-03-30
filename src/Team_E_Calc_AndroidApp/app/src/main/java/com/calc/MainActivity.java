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

import com.example.friketrin.calc.R;

public class MainActivity extends AppCompatActivity {

    // TODO Memory, access history et al
    // TODO implement a landscape layout and possibly bigger screen layouts

    private boolean vibrate = true;

    /**Initialization called when the activity is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // TODO what do we need to change here?
        ExpressionHistory.appendEntry(""); // NOTE try to minimize the view calling the mode
        EditText text = (EditText) findViewById(R.id.textView);
        text.setOnTouchListener(otl);
        /* for some reason the xml value doesn't work with galaxy s3 (at least)
        * so setting this flag ensures that suggestions are off.
        * http://stackoverflow.com/questions/1959576/turn-off-autosuggest-for-edittext
        * better this than input of password type (which is a nasty hack) */
        text.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
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
            float x = event.getX();
            float y = event.getY();
            int touchPosition = ((EditText)text).getOffsetForPosition(x, y);
            InputHandler.setCurrIndex(touchPosition);
            populateDisplay();
            return true;
        }
    };

    // just the messenger, provides an interface between the view and the model
    public void sendMessage(View view){
        Button button = (Button)view;
        InputHandler.input(button.getText().toString());
        if(vibrate) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        populateDisplay();
    }

    public void evaluateExpression(View view){//TODO change this once we have two displays (and an ans class)
        String result = ExpressionEvaluator.evaluate();
        EditText text = (EditText) findViewById(R.id.textView); //TODO  we should change the id, textView is another type of display
        text.setText(result);
        if(vibrate) {
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
        if(vibrate) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        InputHandler.clear();
        populateDisplay();
    }

    public void backspace(View view){
        Vibrator vibe;
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(20);
        InputHandler.backspace();
        populateDisplay();
    }

    public void left(View view){
        InputHandler.moveLeft();
        populateDisplay();
    }

    public void right(View view){
        InputHandler.moveRight();
        populateDisplay();
    }

    public void up(View view){
        InputHandler.moveUp();
        populateDisplay();
    }

    public void down(View view){
        InputHandler.moveDown();
        populateDisplay();
    }

    public void populateDisplay(){ // check if we need to populate the display with new content
        if (ExpressionHistory.refreshDisplay) {
            EditText text = (EditText) findViewById(R.id.textView);
            text.setText(ExpressionHistory.getEntry());
            text.setSelection(InputHandler.getCurrIndex());
            ExpressionHistory.refreshDisplay = false;
        }
    }

}
