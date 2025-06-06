package com.br.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConvertDate {
    
    static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    static SimpleDateFormat parser = new SimpleDateFormat("ddMMyy", Locale.ENGLISH);

    public static String convertDateToDecimal(String date) {
//		System.out.println(date);
//		System.out.println(date.substring(0,4) + date.substring(5,7) + date.substring(8,10));
	return date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
    }

    public static String convertDecimalToDateM3(String date) {
//		System.out.println(date);
	System.out.println(date.substring(6, 8) + date.substring(4, 6) + date.substring(2, 4));
	return date.substring(6, 8) + date.substring(4, 6) + date.substring(2, 4);
    }

    public static String convertDateToDateM3(String date) {
//		System.out.println(date);
//		System.out.println(date.substring(0,4) + date.substring(5,7) + date.substring(8,10));
	return date.substring(8, 10) + date.substring(5, 7) + date.substring(2, 4);
    }
    
    public static Date convertStringToDate(String date) throws ParseException {
//		System.out.println(date);
//		System.out.println(date.substring(0,4) + date.substring(5,7) + date.substring(8,10));
	Date cvDate = format.parse(date);
	return cvDate;
    }
    
    public static String convertDateM3ToDate(String date) throws ParseException {
//		System.out.println(date);
//		System.out.println(date.substring(0,4) + date.substring(5,7) + date.substring(8,10));
//	return date.substring(4, 6) + date.substring(2, 4) + date.substring(0, 2);
	return format.format(parser.parse(date));
    }

}
