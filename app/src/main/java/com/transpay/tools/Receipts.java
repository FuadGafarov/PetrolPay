package com.transpay.tools;

import android.content.SharedPreferences;
import com.telpo.tps550.api.printer.UsbThermalPrinter;

public class Receipts {
    private UsbThermalPrinter mUsbThermalPrinter = null;
    private SharedPreferences sharedPref = null;

    public Receipts(UsbThermalPrinter mUsbThermalPrinter, SharedPreferences sharedPref) {
        this.mUsbThermalPrinter = mUsbThermalPrinter;
        this.sharedPref = sharedPref;
    }

    public void PaymentReceipt() throws Exception {
        this.mUsbThermalPrinter.addString("'TRANSPAY' MMC");
        this.mUsbThermalPrinter.addString("VOEN: 1803457241");
        this.mUsbThermalPrinter.addString("MENTEQE ADI   " + sharedPref.getString("branchName", ""));
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString(sharedPref.getString("servertime", ""));
        this.mUsbThermalPrinter.addString("POS ADI: " + sharedPref.getString("merchant_name", ""));
        this.mUsbThermalPrinter.addString("POS ID: " + sharedPref.getString("terminalID", ""));
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString("SIRKET:   " + sharedPref.getString("company", ""));
        this.mUsbThermalPrinter.addString("KARTIN SAHIBI:   " + sharedPref.getString("card_owner", ""));
        this.mUsbThermalPrinter.addString("KARTIN NOMRASI: " + sharedPref.getString("card_pan", ""));
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString("             QIYMAT(ADV DAXIL)");
        this.mUsbThermalPrinter.addString(sharedPref.getString("product", ""));
        this.mUsbThermalPrinter.addString(sharedPref.getString("productType", "") + "  " + sharedPref.getString("litres_amount", "") + "L * " + sharedPref.getString("per_litre_price", "") + " AZN = " + sharedPref.getString("total_amount", "") + " AZN");
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString("BALANS:      " + sharedPref.getString("balance", "0"));
        this.mUsbThermalPrinter.addString("EMELIYYAT NOMRASI:  " + sharedPref.getString("transaction_no", "0"));

    }

    public void zReportReceipt() throws Exception {
        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("NOVBE UZRE 'Z' HESABAT");
        this.mUsbThermalPrinter.addString("MENTEQE ADI   " + sharedPref.getString("branchName", ""));
        this.mUsbThermalPrinter.setAlgin(0);

        this.mUsbThermalPrinter.addString("Bashla: " + sharedPref.getString("startDateTime", ""));
        this.mUsbThermalPrinter.addString("Son: " + sharedPref.getString("endDateTime", ""));
        this.mUsbThermalPrinter.addString("POS ID: " + sharedPref.getString("terminalID", ""));

        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("---------- AI-92 ----------");
        this.mUsbThermalPrinter.addString("satis");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                      " + sharedPref.getString("ai92Litre", ""));
        this.mUsbThermalPrinter.addString("MANAT                     " + sharedPref.getString("ai92Amount", "") + " AZN");
        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("geri");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                      " + sharedPref.getString("ai92ReverseLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                     " + sharedPref.getString("ai92ReverseAmount", "") + " AZN");

        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString("---------- PREMIUM ---------");
        this.mUsbThermalPrinter.addString("satis");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                      " + sharedPref.getString("premiumLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                     " + sharedPref.getString("premiumAmount", "") + " AZN");
        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("geri");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                      " + sharedPref.getString("premiumReverseLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                     " + sharedPref.getString("premiumReverseAmount", "") + " AZN");

        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("---------- DIZEL ----------");
        this.mUsbThermalPrinter.addString("satis");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                   " + sharedPref.getString("dieselLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                  " + sharedPref.getString("dieselAmount", "") + " AZN");
        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("geri");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                      " + sharedPref.getString("dieselReverseLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                     " + sharedPref.getString("dieselReverseAmount", "") + " AZN");


        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("---------- SUPER ----------");
        this.mUsbThermalPrinter.addString("satis");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                   " + sharedPref.getString("superLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                  " + sharedPref.getString("superAmount", "") + " AZN");
        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("geri");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                      " + sharedPref.getString("superReverseLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                     " + sharedPref.getString("superReverseAmount", "") + " AZN");

        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("---------- QAZ ----------");
        this.mUsbThermalPrinter.addString("satis");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                   " + sharedPref.getString("gasLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                  " + sharedPref.getString("gasAmount", "") + " AZN");
        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("geri");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("LITR                      " + sharedPref.getString("gasReverseLitre", ""));
        this.mUsbThermalPrinter.addString("MANAT                     " + sharedPref.getString("gasReverseAmount", "") + " AZN");
        this.mUsbThermalPrinter.addString("---------------------------");

        this.mUsbThermalPrinter.addString("Geri qaytarma emeliyyati");
        this.mUsbThermalPrinter.addString("tranzaksiya                " + sharedPref.getString("totalreverseCount", ""));
        this.mUsbThermalPrinter.addString("MEBLEG                     " + sharedPref.getString("totalreverseAmount", "") + " AZN");
        this.mUsbThermalPrinter.setAlgin(1);
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.setAlgin(0);
        this.mUsbThermalPrinter.addString("MEBLEG                     " + sharedPref.getString("totalpaymentAmount", "") + " AZN");
        this.mUsbThermalPrinter.addString("                           " + sharedPref.getString("totalreverseAmount", "") + " AZN");
        this.mUsbThermalPrinter.addString("MEBLEG                     " + sharedPref.getString("total_income", "") + " AZN");

    }

    public void reverseReceipt() throws Exception {
        this.mUsbThermalPrinter.addString("'TRANSPAY' MMC / GERI QAYTARMA EMELIYYATI");
        this.mUsbThermalPrinter.addString("VOEN: 1803457241");
        this.mUsbThermalPrinter.addString("MENTEQE ADI   " + sharedPref.getString("branchName", ""));
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString(sharedPref.getString("servertime", ""));
        this.mUsbThermalPrinter.addString("POS ADI: " + sharedPref.getString("merchant_name", ""));
        this.mUsbThermalPrinter.addString("POS ID: " + sharedPref.getString("terminalID", ""));
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString("SIRKET:   " + sharedPref.getString("company", ""));
        this.mUsbThermalPrinter.addString("KARTIN SAHIBI:   " + sharedPref.getString("card_owner", ""));
        this.mUsbThermalPrinter.addString("KARTIN NOMRASI: " + sharedPref.getString("card_pan", ""));
        this.mUsbThermalPrinter.addString("---------------------------");
        this.mUsbThermalPrinter.addString("EMELIYYAT NOMRESI: " + sharedPref.getString("tr_no", ""));
        this.mUsbThermalPrinter.addString("QAYTARILAN MEBLEG: " + sharedPref.getString("reverseAmount", ""));
        this.mUsbThermalPrinter.addString("BALANS:      " + sharedPref.getString("balanceOnReverse", "0"));
        this.mUsbThermalPrinter.addString("---------------------------");

        this.mUsbThermalPrinter.addString("QAYTARMA EMELIYYAT NOMRESI:  " + sharedPref.getString("reverseTransactionID", "0"));
    }
}
