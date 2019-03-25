package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class PetrolTypes extends Activity {
    private Intent toPaymentTypeIntent = null;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petrol_types);
        SharedPreferences sharedPref;
        toPaymentTypeIntent = new Intent(PetrolTypes.this, PaymentType.class);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
                startActivity(new Intent(PetrolTypes.this, ProductType.class));
        } else if(KeyEvent.KEYCODE_ENTER == event.getKeyCode()) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public void ai92ButtonClick(View v) {
        editor.putString("product", "AI-92");
        editor.commit();
        startActivity(toPaymentTypeIntent);
    }

    public void premiumButtonClick(View v) {
        editor.putString("product", "premium");
        editor.commit();
        startActivity(toPaymentTypeIntent);
    }

    public void superButtonClick(View v) {
        editor.putString("product", "super");
        editor.commit();
        startActivity(toPaymentTypeIntent);
    }
}
