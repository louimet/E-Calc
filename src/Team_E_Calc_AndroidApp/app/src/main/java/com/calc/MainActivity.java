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

import com.example.friketrin.calc.R;

public class MainActivity extends AppCompatActivity {
    // Some constants
    private static final int MIN_DURATION_OF_LONG_TOUCH = 1000;
    private static final int ALERT_DURATION = 80;

    private boolean vibrate = true;
    private boolean copy = true;
    private boolean isExpressionActive = true;
    private boolean isDisplayAlert;


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
        TextView resultView = (TextView)findViewById(R.id.resultView);
        resultView.setOnLongClickListener(olcl);
        // Change the font to ensure character's such as U - 232b
        Button backspace = (Button)findViewById(R.id.buttonBackspace);
        Typeface font = Typeface.createFromAsset(getAssets(), "DejaVuSans.ttf");
        backspace.setTypeface(font);
        isDisplayAlert = false;
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
                // about was selected // TODO what's making the viewer crash?
               /* //Uri uri = Uri.parse("file:///android_assets/doc/usermanual.html");
                Uri uri = Uri.parse("http://www.google.com");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                browserIntent.setDataAndType(Uri.parse("file:///android_assets/doc/usermanual.html"), "text/html");
                startActivity(browserIntent);*/
                Intent myIntent = new Intent(MainActivity.this, UserManual.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Disable soft keyboard, handle cursor position and copy expression to clipboard
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
    // Override onLongClick to copy text from the result display
    protected View.OnLongClickListener olcl = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View text) {
            copyToClipboard(text);
            return true;
        }
    };
    protected void copyToClipboard(View text){
        String clipping = "";
        String typeClip = "";
        if(text.getId() == R.id.textView){
            clipping = ((EditText) text).getText().toString();
            typeClip = "Expression";
        }
        else if(text.getId() == R.id.resultView){
            clipping = ((TextView) text).getText().toString();
            typeClip = "Result";
        }
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip =
                android.content.ClipData.newPlainText(typeClip+" copied from Eternity",
                        clipping);
        clipboard.setPrimaryClip(clip);
        // now notify the user
        CharSequence notification = "Copied "+typeClip.toLowerCase()+" to clipboard";
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
        else if(!success){
            usageAlert();
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
        boolean success = InputHandler.plusMinus();
        populateDisplay();
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        if(!success){
            usageAlert();
        }
    }

    public void clearExpression(View view){
        boolean success = InputHandler.clear();
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        /* also check if the display will be refreshed as we might have an inactive expression
        * and the arrow actually will have the effect of clearing - so no alert
         */
        if(!success && !ExpressionHistory.refreshDisplay){
            usageAlert();
        }
        populateDisplay();
        ResultBuffer.clear();
        TextView resultView = (TextView) findViewById(R.id.resultView);
        resultView.setText("" + ResultBuffer.getResult());
    }

    public void backspace(View view){
        boolean success = InputHandler.backspace();
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        /* also check if the display will be refreshed as we might have an inactive expression
        * and the arrow actually will have the effect of clearing - so no alert
         */
        if(!success && !ExpressionHistory.refreshDisplay){
            usageAlert();
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
        if(!success){
            usageAlert();
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
        if(!success){
            usageAlert();
        }
        populateDisplay();
    }

    public void up(View view){
        boolean success = InputHandler.moveUp();
        if(vibrate && success) {
            Vibrator vibe;// TODO maybe avoid vibrating if we're already clear
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        if(!success){
            usageAlert();
        }
        populateDisplay();
    }

    public void down(View view){
        boolean success = InputHandler.moveDown();
        if(vibrate && success) {
            Vibrator vibe;
            vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(20);
        }
        /* also check if the display will be refreshed as we might have an inactive expression
        * and the arrow actually will have the effect of clearing - so no alert
         */
        if(!success && !ExpressionHistory.refreshDisplay){
            usageAlert();
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
            expressionView.setTextColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.text_active));
        }
        else{
            expressionView.setTextColor(
                    ContextCompat.getColor(getApplicationContext(), R.color.text_inactive));
        }
    }
    // flash the display screens when invalid input is keyed in, principle, feedback is good
    private void usageAlert(){
        isDisplayAlert = true;
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                EditText expressionView = (EditText) findViewById(R.id.textView);
                TextView resultView = (TextView) findViewById(R.id.resultView);
                if(isDisplayAlert){
                    expressionView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.display_alert));
                    resultView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.display_alert));
                    isDisplayAlert = false; // the alert has been sounded...
                }
                else{
                    expressionView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.display_normal));
                    resultView.setBackgroundColor(
                            ContextCompat.getColor(getApplicationContext(), R.color.display_normal));
                }
            }
        };
        handler.post(r);
        handler.postDelayed(r, ALERT_DURATION);
    }

}
