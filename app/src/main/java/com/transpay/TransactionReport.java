package com.transpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.transpay.tools.Receipts;
import com.telpo.tps550.api.printer.UsbThermalPrinter;

public class TransactionReport extends Activity {
    private String transactionString = "";
    private SharedPreferences sharedPref;
    private final UsbThermalPrinter mUsbThermalPrinter = new UsbThermalPrinter(TransactionReport.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_report);
        sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        TextView transactionCompletedSuccessfullyText = (TextView) findViewById(R.id.transactionCompletedSuccessfullyText);

        new PrintThread().start();

        if (sharedPref.getString("action_type", "pay").equals("pay")) {
            transactionString = "PAYMENT " + sharedPref.getString("total_amount", "0") + " AZN OK";
        } else if (sharedPref.getString("action_type", "pay").equals("reverse")) {
            transactionString = "REVERSE " + sharedPref.getString("reverseAmount", "0") + " AZN OK";
        } else if (sharedPref.getString("action_type", "pay").equals("zreport")) {
            transactionString = getString(R.string.reportCreatedSuccessfully);
        }

        transactionCompletedSuccessfullyText.setText(transactionString);

        if (!sharedPref.getString("action_type", "pay").equals("zreport")) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    new PrintThread().start();
                }
            }, 7000);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_ENTER == event.getKeyCode() || KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                startActivity(new Intent(TransactionReport.this, MainActivity.class));
        }
        return super.dispatchKeyEvent(event);
    }

    private class PrintThread extends Thread {
        @Override
        public void run() {
            Receipts receipts;
            super.run();
            try {
                receipts = new Receipts(mUsbThermalPrinter, sharedPref);
                mUsbThermalPrinter.start(1);
                mUsbThermalPrinter.reset();

                if (sharedPref.getString("action_type", "pay").equals("pay")) {
                    receipts.PaymentReceipt();
                } else if (sharedPref.getString("action_type", "pay").equals("reverse")) {
                    receipts.reverseReceipt();
                } else if (sharedPref.getString("action_type", "pay").equals("zreport")) {
                    receipts.zReportReceipt();
                }
                mUsbThermalPrinter.printString();
                mUsbThermalPrinter.walkPaper(30);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                mUsbThermalPrinter.stop();
            }
        }
    }
}

