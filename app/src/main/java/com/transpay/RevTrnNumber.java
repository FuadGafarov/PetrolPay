package com.transpay;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class RevTrnNumber extends Activity {
    private EditText amountEditText;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private final View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rev_trn_number);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        amountEditText.setOnTouchListener(otl);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(amountEditText.getText().length() < 11) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.pleaseEnterCorrectTrNumber), Toast.LENGTH_SHORT);
                toast.show();
            } else {
                editor.putString("tr_no", amountEditText.getText().toString());
                editor.commit();
                new ServerData().execute(GlobalVars.SERVER_URL + "transactionid=" + sharedPref.getString("tr_no", "") + "&action=reverse&posid=" + sharedPref.getString("terminalID", ""));
            }
        }
        return super.dispatchKeyEvent(event);
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
                if(jObject.getString("responseCode").equals("SUCCESS")) {
                    editor.putString("action_type", "reverse");
                    editor.putString("reverseAmount", jObject.getString("reverseAmount"));
                    editor.putString("balanceOnReverse", jObject.getString("balance"));
                    editor.putString("reverseTransactionID", jObject.getString("reverseTransactionID"));
                    editor.putString("branchName", jObject.getString("branchName"));
                    editor.commit();
                    startActivity(new Intent(RevTrnNumber.this, TransactionReport.class));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "ERROR:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
