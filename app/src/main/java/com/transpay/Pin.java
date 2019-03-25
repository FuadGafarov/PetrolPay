package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.telpo.tps550.api.reader.SLE4442Reader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Pin extends Activity {
    private EditText pinEditText;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private SLE4442Reader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        pinEditText = (EditText) findViewById(R.id.pinEditText);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        reader = new SLE4442Reader(Pin.this);
        editor = sharedPref.edit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

                if (KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(!pinEditText.getText().toString().equals("")) {
                        if (sharedPref.getString("action_type", "").equals("pay")) {
                            editor.putString("card_pan", "");
                            editor.commit();
                                try {
                                    new readTask().execute().get();
                                    if (sharedPref.getString("card_pan", "").length() == 16) {
                                        new ServerData().execute(GlobalVars.SERVER_URL + "action=pay" +
                                                "&amount=" + sharedPref.getString("sum", "") + "" +
                                                "&paytype=" + sharedPref.getString("paytype", "") + "" +
                                                "&card=" + sharedPref.getString("card_pan", "") + "" +
                                                "&product=" + sharedPref.getString("product", "") + "" +
                                                "&posid=" + sharedPref.getString("terminalID", "") + "" +
                                                "&pin=" + pinEditText.getText().toString()).get();
                                    } else {
                                        Toast insertCardMessage = Toast.makeText(getApplicationContext(), getString(R.string.PleaseInsertCard), Toast.LENGTH_SHORT);
                                        insertCardMessage.show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                        }
                    } else {
                        Toast insertPinMessage = Toast.makeText(getApplicationContext(), getString(R.string.addPinCode), Toast.LENGTH_SHORT);
                        insertPinMessage.show();
                    }
                }


            if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
                startActivity(new Intent(Pin.this, Sum.class));
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

            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.ErrorInConnection), Toast.LENGTH_SHORT);
            if (result == null || Objects.equals(result, "")) {
                toast.show();
            } else {
                editor = sharedPref.edit();

                try {
                    JSONObject jObject = new JSONObject(result);
                        editor.putString("litres_amount", jObject.getString("litres_amount"));
                        editor.putString("per_litre_price", jObject.getString("per_litre_price"));
                        editor.putString("card_owner", jObject.getString("card_owner"));
                        editor.putString("transaction_no", jObject.getString("transaction_no"));
                        editor.putString("balance", jObject.getString("balance"));
                        editor.putString("company", jObject.getString("company"));
                        editor.putString("total_amount", jObject.getString("total_amount"));
                        editor.putString("merchant_name", jObject.getString("merchant_name"));
                        editor.putString("servertime", jObject.getString("servertime"));
                        editor.putString("branchName", jObject.getString("branchName"));
                        editor.commit();

                        Intent trnIntent = new Intent(Pin.this, TransactionReport.class);
                        startActivity(trnIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class readTask extends AsyncTask<Integer, Integer, byte[]>
    {
        @Override
        protected void onPostExecute(byte[] result)
        {
            super.onPostExecute(result);
        }

        @Override
        protected byte[] doInBackground(Integer... params)
        {
            byte[] data;

            try
            {
                reader.iccPowerOn();
                reader.open();
                data = reader.readMainMemory(160, 96);
                StringBuilder dataString = new StringBuilder(BCD2Str(data));
                String[] panData = dataString.substring(0, dataString.indexOf("A8")).split(" ");
                dataString = new StringBuilder();
                for (String aPanData : panData) {
                    dataString.append(Integer.parseInt(aPanData));
                }
                editor.putString("card_pan", dataString.toString());
                editor.commit();
                reader.close();
                reader.iccPowerOff();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
            return data;
        }

    }

    private String BCD2Str(byte[] data)
    {
        String string;
        StringBuilder stringBuilder = new StringBuilder();

        for (byte aData : data) {
            string = Integer.toHexString(aData & 0xFF);
            if (string.length() == 1) {
                stringBuilder.append("0");
            }

            stringBuilder.append(string.toUpperCase());
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }
}
