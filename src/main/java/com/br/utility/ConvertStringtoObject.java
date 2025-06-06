package com.br.utility;

public class ConvertStringtoObject {

	public static String convertNullToObject(String str) {
//		System.out.println("str:" + str);

		if (str.equals("null") || str.equals("")) {
			return null;
		} else {
			return str;
		}

	}

}
