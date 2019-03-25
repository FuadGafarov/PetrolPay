package com.transpay.tools;

import java.text.NumberFormat;

public class DataManipulationTools {
    public String adjustFloatingStringToTwoDecimalPoint(String input) {
        if(this.checkTwoDecimalFloating(input)) {
            double d = Double.parseDouble(input);
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            return formatter.format(d).substring(1);
        } else return input;
    }

    public boolean checkTwoDecimalFloating(String input) {
            if(input.contains(".")) {
                return input.substring(input.indexOf(".")).length() > 3;
            } else return false;
    }
}
