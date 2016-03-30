/**
 * Written by Federico O'Reilly Regueiro for COPM 5541, calculator project
 * Winter 2016
 *
 * This class handles the main view of the calculator, it calls InputHandler (controller)
 * for input and ExpressionEvaluator (controller) to handle evaluation
 *
 * Pushing buttons calls interface functions, which has been linked to the appropriate methods in
 * the controllers
 */
package com.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Vibrator;
import android.content.Context;

import com.example.friketrin.calc.R;

public class MainActivity extends AppCompatActivity {

    // TODO Rad/deg toggle button in menu?
    // TODO Memory, access history et al
    // TODO implement a landscape layout and possibly bigger screen layouts

    private boolean vibrate = true;

    /**Initialization called when the activity is created*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // TODO what do we need to change here?
        ExpressionHistory.appendEntry(""); // NOTE try to minimize the view calling the model
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

    /* TODO We'll probably need a static int for the current cursor position in the current
     * position on the expression string and we'll need to pass it along to input handler
     * this will be done when we have navigation on the text input window
     */
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
        EditText text = (EditText) findViewById(R.id.textView);
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
        EditText text = (EditText) findViewById(R.id.textView);
        text.setSelection(InputHandler.getCurrIndex());
    }

    public void right(View view){
        InputHandler.moveRight();
        EditText text = (EditText) findViewById(R.id.textView);
        text.setSelection(InputHandler.getCurrIndex());
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
