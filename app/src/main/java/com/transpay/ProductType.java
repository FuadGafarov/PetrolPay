package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class ProductType extends Activity {
    private Intent toOtherTypesIntent = null;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);
        SharedPreferences sharedPref;
        toOtherTypesIntent = new Intent(ProductType.this, PaymentType.class);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent toProductTypeIntent = new Intent(ProductType.this, Balance.class);
            startActivity(toProductTypeIntent);
        } else if(KeyEvent.KEYCODE_ENTER == event.getKeyCode()) {
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    public void petrolButtonClick(View v) {
        Intent toPetrolTypeIntent = new Intent(ProductType.this, PetrolTypes.class);
        startActivity(toPetrolTypeIntent);
    }

    public void dieselButtonClick(View v) {
        editor.putString("product", "disel");
        editor.commit();
        startActivity(toOtherTypesIntent);
    }

    public void gasButtonClick(View v) {
        editor.putString("product", "gas");
        editor.commit();
        startActivity(toOtherTypesIntent);
    }
}
