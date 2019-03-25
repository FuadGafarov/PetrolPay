package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class PaymentType extends Activity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Intent toManatOrLitreIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_type);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        TextView paymentTypeLocator = (TextView) findViewById(R.id.paymentTypeLocator);
        editor = sharedPref.edit();

        paymentTypeLocator.setText("Product: " + sharedPref.getString("product", ""));
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(sharedPref.getString("product", "").equals("disel") || sharedPref.getString("product", "").equals("gas")) {
                startActivity(new Intent(PaymentType.this, ProductType.class));
            } else {
                startActivity(new Intent(PaymentType.this, PetrolTypes.class));
            }
        } else if(KeyEvent.KEYCODE_ENTER == event.getKeyCode()) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public void manatButtonClick(View v) {
        editor.putString("paymentType", "manat");
        editor.commit();
        toManatOrLitreIntent = new Intent(PaymentType.this, Sum.class);
        startActivity(toManatOrLitreIntent);
    }

    public void litreButtonClick(View v) {
        editor.putString("paymentType", "litre");
        editor.commit();
        toManatOrLitreIntent = new Intent(PaymentType.this, Litre.class);
        startActivity(toManatOrLitreIntent);
    }
}
