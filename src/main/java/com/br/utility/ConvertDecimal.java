package com.br.utility;

import java.text.DecimalFormat;

public class ConvertDecimal {

	public static double convertDoubleDigit(double number) {
        try {
            String numberBeforeconvert = String.valueOf(new DecimalFormat("##.###").format(Math.round(number * 1000.0) / 1000.0));
            double numberreturn = Double.parseDouble(numberBeforeconvert);
            return numberreturn;
        } catch (Exception e) {
            return 0;
        }

    }

}
