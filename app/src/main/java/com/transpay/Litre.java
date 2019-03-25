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

public class Litre extends Activity {
    private EditText litreEditText;
    private SharedPreferences sharedPref;
    private final DataManipulationTools dmt = new DataManipulationTools();
    private final View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {
            return true; // the listener has consumed the event
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litre);
        litreEditText = (EditText) findViewById(R.id.litreEditText);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);

        litreEditText.setOnTouchListener(otl);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        SharedPreferences.Editor editor;

        if (KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(!litreEditText.getText().toString().equals("")) {
                editor = sharedPref.edit();
                editor.putString("paytype", "litre");
                editor.putString("sum", litreEditText.getText().toString());
                editor.commit();
                startActivity(new Intent(Litre.this, Pin.class));
            } else {
                Toast EnterLitre = Toast.makeText(getApplicationContext(), getString(R.string.EnterLitre), Toast.LENGTH_SHORT);
                EnterLitre.show();
            }
        } else if(KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            startActivity(new Intent(Litre.this, PaymentType.class));
        }

        if(KeyEvent.KEYCODE_ENTER != event.getKeyCode() && event.getAction() == KeyEvent.ACTION_UP ||
                KeyEvent.KEYCODE_BACK != event.getKeyCode() && event.getAction() == KeyEvent.ACTION_UP) {
            if (!litreEditText.getText().toString().equals("")) {
                if(dmt.checkTwoDecimalFloating(litreEditText.getText().toString())) {
                    String tempString = litreEditText.getText().toString().substring(0, litreEditText.getText().toString().length() - 1);
                    litreEditText.setText("");
                    litreEditText.append(tempString);

                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
