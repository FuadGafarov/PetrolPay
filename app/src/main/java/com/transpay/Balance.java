package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Balance extends Activity {
        private TextView balanceTextView;
        private TextView numbersVariables;
        private ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref;
        setContentView(R.layout.activity_balance);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        numbersVariables = (TextView) findViewById(R.id.numbersVariables);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        new Balance.ServerData().execute(GlobalVars.SERVER_URL + "action=balance&card_no=" + sharedPref.getString("card_pan", ""));
    }

    public void buttonPayClick(View v) {
        Intent toProductTypeIntent = new Intent(Balance.this, ProductType.class);
        startActivity(toProductTypeIntent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_ENTER == event.getKeyCode()) {
            Intent toProductTypeIntent = new Intent(Balance.this, ProductType.class);
            startActivity(toProductTypeIntent);
        } else if(KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) startActivity(new Intent(Balance.this, MainActivity.class));
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {

    }

    class ServerData extends AsyncTask<String, String, String> {
        private final HttpClient Client = new DefaultHttpClient();
        private String Content;

        @Override
        protected String doInBackground(String... params) {
            try {
                HttpGet httpget = new HttpGet(params[0]);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                Content = Client.execute(httpget, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Content;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jObject = new JSONObject(result);
                balanceTextView.setText(getString(R.string.Balance) + ":     " + jObject.getString("balance") + " " + getString(R.string.azn));

                JSONArray numbersJSONArray = jObject.getJSONArray("vehicle_numbers");
                StringBuilder numbersText = new StringBuilder();

                for (int i = 0; i < numbersJSONArray.length(); i++) {
                        numbersText.append(numbersJSONArray.getString(i)).append("\n");
                }
                numbersVariables.setText(numbersText.toString());
                progressBar1.setVisibility(View.GONE);
                numbersVariables.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
