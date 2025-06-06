package com.br.utility;

import java.text.DecimalFormat;

public class NumberToText {

    static DecimalFormat df = new DecimalFormat("#");
    static DecimalFormat df2 = new DecimalFormat("#.##");
    static DecimalFormat df3 = new DecimalFormat("#.###");
    static DecimalFormat df4 = new DecimalFormat("#.####");

    private static final String[] tensNames = { "", " ten", " twenty", " thirty", " forty", " fifty", " sixty",
	    " seventy", " eighty", " ninety" };

    private static final String[] numNames = { "", " one", " two", " three", " four", " five", " six", " seven",
	    " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
	    " seventeen", " eighteen", " nineteen" };

    private static String convertLessThanOneThousand(int number) {
	String soFar;

	if (number % 100 < 20) {
	    soFar = numNames[number % 100];
	    number /= 100;
	} else {
	    soFar = numNames[number % 10];
	    number /= 10;

	    soFar = tensNames[number % 10] + soFar;
	    number /= 10;
	}
	if (number == 0)
	    return soFar;
	return numNames[number] + " hundred" + soFar;
    }

    public static String ThaiBaht(String txtnumber) {

	String bahtTxt, n, bahtTH = "";
	Double amount;
	try {
	    amount = Double.parseDouble(txtnumber);
	} catch (NumberFormatException ex) {
	    amount = 0.00;
	}

//	System.out.println(amount);

	try {
	    DecimalFormat df = new DecimalFormat("####.00");
	    bahtTxt = df.format(amount);
	    String[] num = {"x"};
	    String[] rank = { "" };
	    String[] temp = bahtTxt.split("[.]");
	    String intVal = temp[0];
	    String decVal = temp[1];
	    if (Double.parseDouble(bahtTxt) == 0) {
		bahtTH = "xxxx";
	    } else {
		for (int i = 0; i < intVal.length(); i++) {
		    n = intVal.substring(i, i + 1);
		    if (!"0".equals(n)) {
			if ((i == (intVal.length() - 1)) && ("1".equals(n))) {
			    bahtTH += "xxxx";
			} else if ((i == (intVal.length() - 2)) && ("2".equals(n))) {
			    bahtTH += "xxx";
			} else if ((i == (intVal.length() - 2)) && ("1".equals(n))) {
			    bahtTH += "";
			} else {
			    bahtTH += num[Integer.parseInt(n)];
			}
			bahtTH += rank[(intVal.length() - i) - 1];
		    }
		}
		bahtTH += "xx";
		if ("00".equals(decVal)) {
		    bahtTH += "xxxx";
		} else {
		    for (int i = 0; i < decVal.length(); i++) {
			n = decVal.substring(i, i + 1);
			if (!"0".equals(n)) {
			    if ((i == decVal.length() - 1) && ("1".equals(n))) {
				bahtTH += "xxx";
			    } else if ((i == (decVal.length() - 2)) && ("2".equals(n))) {
				bahtTH += "xxx";
			    } else if ((i == (decVal.length() - 2)) && ("1".equals(n))) {
				bahtTH += "";
			    } else {
				bahtTH += num[Integer.parseInt(n)];
			    }
			    bahtTH += rank[(decVal.length() - i) - 1];
			}
		    }
		    bahtTH += "xxxcc";
		}
	    }

	} catch (NumberFormatException exe) {

	    System.out.print(exe.getMessage());
	}
	return "(" + bahtTH + ")";

    }

    public static String EnglishBaht(String txtnumber) {

	// 0 to 999 999 999 999
	if (Long.valueOf(txtnumber) == 0) {
	    return "zero";
	}

	String number = txtnumber;

	// pad with "0"
	String mask = "000000000000";
	DecimalFormat dfm = new DecimalFormat(mask);
	number = dfm.format(Long.valueOf(txtnumber));

	// XXXnnnnnnnnn
	int billions = Integer.parseInt(number.substring(0, 3));
	// nnnXXXnnnnnn
	int millions = Integer.parseInt(number.substring(3, 6));
	// nnnnnnXXXnnn
	int hundredThousands = Integer.parseInt(number.substring(6, 9));
	// nnnnnnnnnXXX
	int thousands = Integer.parseInt(number.substring(9, 12));

	String tradBillions;
	switch (billions) {
	case 0:
	    tradBillions = "";
	    break;
	case 1:
	    tradBillions = convertLessThanOneThousand(billions) + " billion ";
	    break;
	default:
	    tradBillions = convertLessThanOneThousand(billions) + " billion ";
	}
	String result = tradBillions;

	String tradMillions;
	switch (millions) {
	case 0:
	    tradMillions = "";
	    break;
	case 1:
	    tradMillions = convertLessThanOneThousand(millions) + " million ";
	    break;
	default:
	    tradMillions = convertLessThanOneThousand(millions) + " million ";
	}
	result = result + tradMillions;

	String tradHundredThousands;
	switch (hundredThousands) {
	case 0:
	    tradHundredThousands = "";
	    break;
	case 1:
	    tradHundredThousands = "one thousand ";
	    break;
	default:
	    tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
	}
	result = result + tradHundredThousands;

	String tradThousand;
	tradThousand = convertLessThanOneThousand(thousands);
	result = result + tradThousand;

	return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

    public static String EnglishBaht_bac310521(long number) {

	// 0 to 999 999 999 999
	if (number == 0) {
	    return "zero";
	}

	String snumber = Long.toString(number);

	// pad with "0"
	String mask = "000000000000";
	DecimalFormat df = new DecimalFormat(mask);
	snumber = df.format(number);

	// XXXnnnnnnnnn
	int billions = Integer.parseInt(snumber.substring(0, 3));
	// nnnXXXnnnnnn
	int millions = Integer.parseInt(snumber.substring(3, 6));
	// nnnnnnXXXnnn
	int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
	// nnnnnnnnnXXX
	int thousands = Integer.parseInt(snumber.substring(9, 12));

	String tradBillions;
	switch (billions) {
	case 0:
	    tradBillions = "";
	    break;
	case 1:
	    tradBillions = convertLessThanOneThousand(billions) + " billion ";
	    break;
	default:
	    tradBillions = convertLessThanOneThousand(billions) + " billion ";
	}
	String result = tradBillions;

	String tradMillions;
	switch (millions) {
	case 0:
	    tradMillions = "";
	    break;
	case 1:
	    tradMillions = convertLessThanOneThousand(millions) + " million ";
	    break;
	default:
	    tradMillions = convertLessThanOneThousand(millions) + " million ";
	}
	result = result + tradMillions;

	String tradHundredThousands;
	switch (hundredThousands) {
	case 0:
	    tradHundredThousands = "";
	    break;
	case 1:
	    tradHundredThousands = "one thousand ";
	    break;
	default:
	    tradHundredThousands = convertLessThanOneThousand(hundredThousands) + " thousand ";
	}
	result = result + tradHundredThousands;

	String tradThousand;
	tradThousand = convertLessThanOneThousand(thousands);
	result = result + tradThousand;

	return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

}
