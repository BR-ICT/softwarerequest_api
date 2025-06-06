package com.br.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class HttpConnection {
	private static final Logger logger = LogManager.getLogger(HttpConnection.class);

	protected static String URL = Constant.CHECKTOKENURL;
	protected static String CHECKTOKENURL = Constant.CHECKTOKENURL;
	protected static String GENTOKENURL = Constant.GENTOKENURL;
	protected static String GENTOKENWOTOKENURL = Constant.GENTOKENWOTOKENURL;

	
	public static String httpConnectionCheckToken(String token) throws JSONException {
//		logger.info("httpConnectionCheckToken");
		JSONObject mJsonObj = new JSONObject();
		HttpURLConnection conn = null;
		try {

			URL obj = new URL(CHECKTOKENURL);
			conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("x-access-token", token);

			int responseCode = conn.getResponseCode();

			// System.out.println("Sending 'GET' request to URL : " + URL);
			// System.out.println("Response Code : " + responseCode);

			// Get message
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}
			in.close();

			// logger.info(response.toString());
			return response.toString();

		} catch (IOException e) {
			logger.error(e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", false);
			return mJsonObj.toString();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}

		}

	}
	
	
	public static Boolean httpConnection(String getToken) {
		// System.out.println("getToken: " + getToken);

		HttpURLConnection conn = null;
		try {

			URL obj = new URL(URL);
			conn = (HttpURLConnection) obj.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("x-access-token", getToken);

			int responseCode = conn.getResponseCode();
//			System.out.println("Sending 'GET' request to URL : " + URL);
//			System.out.println("Response Code : " + responseCode);

			if (responseCode == 200) {
				return true;

			} else {
				return false;

			}

		} catch (IOException e) {
			return false;
			// e.printStackTrace();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}

		}

	}

	public static String httpConnectionV2(String getToken) throws JSONException {
		// System.out.println("getToken: " + getToken);
		JSONObject mJsonObj = new JSONObject();
		HttpURLConnection conn = null;
		try {

			URL obj = new URL(URL);
			conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("x-access-token", getToken);

			int responseCode = conn.getResponseCode();

//			System.out.println("Sending 'GET' request to URL : " + URL);
//			System.out.println("Response Code : " + responseCode);

			// Get message
			BufferedReader in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);

			}
			in.close();

//			System.out.println("Result: " + response.toString());

			return response.toString();

		} catch (IOException e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", false);
			return mJsonObj.toString();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}

		}

	}
}
