package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ReportInput extends Activity {
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private String startDateTime = "";
    private String endDateTime = "";
    private String printedStartDateTime = "";
    private String printedEndDateTime = "";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_input);
        startDatePicker = (DatePicker) findViewById(R.id.StartDatePicker);
        endDatePicker = (DatePicker) findViewById(R.id.EndDatePicker);
        startTimePicker = (TimePicker) findViewById(R.id.StartTimePicker);
        endTimePicker = (TimePicker) findViewById(R.id.EndTimePicker);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //setting timepicker to 24 hour format
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_ENTER == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            startDateTime = startDatePicker.getDayOfMonth() + "-" + (startDatePicker.getMonth()+1) + "-" + startDatePicker.getYear() + startTimePicker.getCurrentHour() + ":" + startTimePicker.getCurrentMinute() + ":00";
            endDateTime = endDatePicker.getDayOfMonth() + "-" + (endDatePicker.getMonth()+1) + "-" + endDatePicker.getYear() + endTimePicker.getCurrentHour() + ":" + endTimePicker.getCurrentMinute() + ":00";
            printedStartDateTime = startDatePicker.getDayOfMonth() + "-" + (startDatePicker.getMonth()+1) + "-" + startDatePicker.getYear() + " " + startTimePicker.getCurrentHour() + ":" + startTimePicker.getCurrentMinute() + ":00";
            printedEndDateTime = endDatePicker.getDayOfMonth() + "-" + (endDatePicker.getMonth()+1) + "-" + endDatePicker.getYear() + " " + endTimePicker.getCurrentHour() + ":" + endTimePicker.getCurrentMinute() + ":00";
            try {
                new readZReport().execute(GlobalVars.SERVER_URL  +
                        "action=zreport" +
                        "&posid=" + sharedPref.getString("terminalID", "") +
                        "&fromdate=" + startDateTime +
                        "&todate=" + endDateTime).get();
            } catch (Exception ex) {
                Log.i("", ex.getMessage());
            }

        } else if(KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            startActivity(new Intent(ReportInput.this, MainActivity.class));
        }
        return super.dispatchKeyEvent(event);
    }

    class readZReport extends AsyncTask<String, String, String> {
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
                    editor.putString("action_type", "zreport");

                    //initialize all variables to zero
                    editor.putString("totalreverseCount", "0");
                    editor.putString("totalreverseAmount", "0");
                    editor.putString("total_income", "0");
                    editor.putString("totalpaymentCount", "0");
                    editor.putString("totalpaymentAmount", "0");

                    //ai-92
                    editor.putString("ai92Amount", "0");
                    editor.putString("ai92Litre", "0");
                    editor.putString("ai92ReverseAmount", "0");
                    editor.putString("ai92ReverseLitre", "0");

                    //diesel
                    editor.putString("dieselAmount", "0");
                    editor.putString("dieselLitre", "0");
                    editor.putString("dieselReverseAmount", "0");
                    editor.putString("dieselReverseLitre", "0");

                    //premium
                    editor.putString("premiumAmount", "0");
                    editor.putString("premiumLitre", "0");
                    editor.putString("premiumReverseAmount", "0");
                    editor.putString("premiumReverseLitre", "0");

                    //super
                    editor.putString("superAmount", "0");
                    editor.putString("superLitre", "0");
                    editor.putString("superReverseAmount", "0");
                    editor.putString("superReverseLitre", "0");

                    //gas
                    editor.putString("gasAmount", "0");
                    editor.putString("gasLitre", "0");
                    editor.putString("gasReverseAmount", "0");
                    editor.putString("gasReverseLitre", "0");

                    JSONObject jObject = new JSONObject(result);
                    editor.putString("totalreverseAmount", jObject.getString("totalreverseAmount"));
                    editor.putString("total_income", jObject.getString("total_income"));
                    editor.putString("totalpaymentCount", jObject.getString("totalpaymentCount"));
                    editor.putString("totalreverseCount", jObject.getString("totalreverseCount"));
                    editor.putString("totalpaymentAmount", jObject.getString("totalpaymentAmount"));
                    editor.putString("branchName", jObject.getString("branchName"));
                    editor.putString("startDateTime", printedStartDateTime);
                    editor.putString("endDateTime", printedEndDateTime);

                    JSONArray productsArray = jObject.getJSONArray("detail");

                   for (int i = 0; i < productsArray.length(); i++) {
                       JSONObject obj = productsArray.getJSONObject(i);

                        if(obj.getString("product").equals("AI-92")) {
                            if(obj.getString("status").equals("PAYMENT")) {
                                editor.putString("ai92Amount", obj.getString("totalamount"));
                                editor.putString("ai92Litre", obj.getString("totallitres"));
                            } else if(obj.getString("status").equals("REVERSE")) {
                                editor.putString("ai92ReverseAmount", obj.getString("totalamount"));
                                editor.putString("ai92ReverseLitre", obj.getString("totallitres"));
                            }
                        } else if(obj.getString("product").equals("disel")) {
                            if(obj.getString("status").equals("PAYMENT")) {
                                editor.putString("dieselAmount", obj.getString("totalamount"));
                                editor.putString("dieselLitre", obj.getString("totallitres"));
                            } else if(obj.getString("status").equals("REVERSE")) {
                                editor.putString("dieselReverseAmount", obj.getString("totalamount"));
                                editor.putString("dieselReverseLitre", obj.getString("totallitres"));
                            }
                        } else if(obj.getString("product").equals("premium")) {
                            if(obj.getString("status").equals("PAYMENT")) {
                                editor.putString("premiumAmount", obj.getString("totalamount"));
                                editor.putString("premiumLitre", obj.getString("totallitres"));
                            }else if(obj.getString("status").equals("REVERSE")) {
                                editor.putString("premiumReverseAmount", obj.getString("totalamount"));
                                editor.putString("premiumReverseLitre", obj.getString("totallitres"));
                            }
                        } else if(obj.getString("product").equals("super")) {
                            if(obj.getString("status").equals("PAYMENT")) {
                                editor.putString("superAmount", obj.getString("totalamount"));
                                editor.putString("superLitre", obj.getString("totallitres"));
                            }else if(obj.getString("status").equals("REVERSE")) {
                                editor.putString("superReverseAmount", obj.getString("totalamount"));
                                editor.putString("superReverseLitre", obj.getString("totallitres"));
                            }
                        } else if(obj.getString("product").equals("gas")) {
                            if(obj.getString("status").equals("PAYMENT")) {
                                editor.putString("gasAmount", obj.getString("totalamount"));
                                editor.putString("gasLitre", obj.getString("totallitres"));
                            }else if(obj.getString("status").equals("REVERSE")) {
                                editor.putString("gasReverseAmount", obj.getString("totalamount"));
                                editor.putString("gasReverseLitre", obj.getString("totallitres"));
                            }
                        }
                   }

                    editor.commit();
                    Intent trnIntent = new Intent(ReportInput.this, TransactionReport.class);
                    startActivity(trnIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
