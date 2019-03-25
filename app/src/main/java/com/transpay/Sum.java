package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.transpay.tools.DataManipulationTools;

public class Sum extends Activity {
    private EditText amountEditText;
    private SharedPreferences sharedPref;
    private final View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {
            return true;
        }
    };
    private final DataManipulationTools dmt = new DataManipulationTools();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        amountEditText.setOnTouchListener(otl);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        SharedPreferences.Editor editor;

        if (KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!amountEditText.getText().toString().equals("")) {

                editor = sharedPref.edit();
                editor.putString("sum", amountEditText.getText().toString());
                editor.putString("paytype", "money");
                editor.commit();
                startActivity(new Intent(Sum.this, Pin.class));
            } else {
                Toast EnterAmount = Toast.makeText(getApplicationContext(), getString(R.string.EnterAmount), Toast.LENGTH_SHORT);
                EnterAmount.show();
            }
        }

        if(KeyEvent.KEYCODE_ENTER != event.getKeyCode() && event.getAction() == KeyEvent.ACTION_UP ||
                KeyEvent.KEYCODE_BACK != event.getKeyCode() && event.getAction() == KeyEvent.ACTION_UP) {
            if (!amountEditText.getText().toString().equals("")) {
                if(dmt.checkTwoDecimalFloating(amountEditText.getText().toString())) {
                    String tempString = amountEditText.getText().toString().substring(0, amountEditText.getText().toString().length() - 1);
                    amountEditText.setText("");
                    amountEditText.append(tempString);

                }
            }
        }

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            startActivity(new Intent(Sum.this, PaymentType.class));
        }
        return super.dispatchKeyEvent(event);
    }
}
