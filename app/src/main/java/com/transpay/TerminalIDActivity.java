package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class TerminalIDActivity extends Activity {
    private EditText amountEditText;
    private SharedPreferences sharedPref;
    private final View.OnTouchListener otl = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {
            return true; // the listener has consumed the event
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminal_id);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        amountEditText.setOnTouchListener(otl);
        amountEditText.setOnKeyListener(new View.OnKeyListener() {
                                            @Override
                                            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                                                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                                    if (amountEditText.length() == 4) {
                                                        new ServerData().execute(GlobalVars.SERVER_URL + "action=terminalCheck&terminalID=" + amountEditText.getText());
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), getString(R.string.TerminalIDMustBeFourDigits), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                return false;
                                            }
                                        }
        );
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
            SharedPreferences.Editor editor;
                try {
                    if(result.trim().equals("true")) {
                        editor = sharedPref.edit();
                        editor.putString("terminalID", amountEditText.getText().toString());
                        editor.commit();
                        Intent toMainActivity = new Intent(TerminalIDActivity.this, MainActivity.class);
                        startActivity(toMainActivity);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.incorrectTerminalID), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast errorMessage = Toast.makeText(getApplicationContext(), "ERROR:"+e.getMessage(), Toast.LENGTH_SHORT);
                    errorMessage.show();
                }
            }
        }
    }


