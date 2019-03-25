package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.telpo.tps550.api.reader.SLE4442Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends Activity {
    private Intent sumIntent;
    private Intent balanceIntent;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private SLE4442Reader reader;
    private Toast toast = null;
    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        reader = new SLE4442Reader(MainActivity.this);

        toast = Toast.makeText(getApplicationContext(), getString(R.string.PleaseInsertCard), Toast.LENGTH_SHORT);

        if (Objects.equals(sharedPref.getString("terminalID", "0"), "0")) {
            Intent toTerminalID = new Intent(MainActivity.this, TerminalIDActivity.class);
            startActivity(toTerminalID);
        }

        setContentView(R.layout.activity_main);
        sumIntent = new Intent(MainActivity.this, Sum.class);
        balanceIntent = new Intent(MainActivity.this, Balance.class);

        Button payButton = (Button) findViewById(R.id.payButton);
        Button reverseButton = (Button) findViewById(R.id.reverseButton);
        Button balanceButton = (Button) findViewById(R.id.balanceButton);
        Button reportButton = (Button) findViewById(R.id.reportButton);

        payButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor.putString("card_pan", "");
                editor.commit();
                try {
                    new readTask().execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(sharedPref.getString("card_pan", "").length() == 16) {
                    editor.putString("action_type", "pay");
                    editor.commit();
                    startActivity(balanceIntent);
                } else {
                    toast.show();
                }
            }
        });

        reverseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    sumIntent = new Intent(MainActivity.this, RevTrnNumber.class);
                    editor.putString("action_type", "reverse");
                    editor.commit();
                    startActivity(sumIntent);
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editor.putString("action_type", "report");
                editor.commit();
                startActivity(new Intent(MainActivity.this, ReportInput.class));
            }
        });



        balanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    editor.putString("card_pan", "");
                    editor.commit();

                try {
                    new readTask().execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(sharedPref.getString("card_pan", "").length() == 16) {
                        balanceIntent = new Intent(MainActivity.this, Balance.class);
                        editor.putString("action_type", "pay");
                        editor.commit();
                    startActivity(balanceIntent);
                    } else {
                        toast.show();
                    }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_BACKSLASH == event.getKeyCode()) {

        } else {
            return false;
        }

        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
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
