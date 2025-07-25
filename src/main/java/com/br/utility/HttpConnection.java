package com.br.utility;

import static com.br.utility.SecurityConstants.HEADER_REQUEST_TOKEN;
import static com.br.utility.SecurityConstants.HEADER_REQUEST_UUID;
import static com.br.utility.SecurityConstants.MDC_UUID_KEY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class HttpConnection {

	private static final Logger logger = LogManager.getLogger(HttpConnection.class);

	protected static String CHECKTOKENURL = Constant.CHECKTOKENURL;
	protected static String GENTOKENURL = Constant.GENTOKENURL;
	protected static String GENTOKENWOTOKENURL = Constant.GENTOKENWOTOKENURL;
	
	private static final int CONNECT_TIMEOUT = 10000; // 10 seconds
    private static final int READ_TIMEOUT = 15000;    // 15 seconds
    private static final int MAX_RETRIES = 3;
    
    /**
     * Send HTTP request supporting JSON, form-data, or both
     *
     * @param method     HTTP method (GET, POST, PUT, DELETE)
     * @param urlStr     Target URL
     * @param headers    Map of headers
     * @param jsonBody   JSON body string (nullable)
     * @param formData   Form-data string (key=value&key2=value2, nullable)
     * @return Response body as String
     */
    public static String sendRequest(String method, String urlStr, Map<String, String> headers,
                                     String jsonBody, String formData) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            attempt++;
            HttpURLConnection conn = null;
            String requestUuid = ThreadContext.get(MDC_UUID_KEY);

            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method.toUpperCase());
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);

                // Enable output for POST/PUT
                if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                    conn.setDoOutput(true);
                }

                // Set headers
                conn.setRequestProperty("Accept", "application/json");
                if (requestUuid != null) {
                    conn.setRequestProperty(HEADER_REQUEST_UUID, requestUuid);
                }
                if (headers != null) {
                    headers.forEach(conn::setRequestProperty);
                }

                // Decide Content-Type and write body
                if (conn.getDoOutput()) {
                    if (jsonBody != null && formData == null) {
                        conn.setRequestProperty("Content-Type", "application/json");
                        writeBody(conn, jsonBody);
                    } else if (formData != null && jsonBody == null) {
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        writeBody(conn, formData);
                    } else if (jsonBody != null && formData != null) {
                        // Rare case: send JSON inside form-data
                        String mixedBody = "json=" + jsonBody + "&" + formData;
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        writeBody(conn, mixedBody);
                    }
                }

                int responseCode = conn.getResponseCode();
                logger.info("Attempt {}: HTTP {} {} → {}", attempt, method, urlStr, responseCode);

                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return response.toString();
                }

            } catch (IOException e) {
                logger.error("Attempt {} failed: {} {} → {}", attempt, method, urlStr, e.getMessage());
                if (attempt >= MAX_RETRIES) {
                    return buildErrorResponse(e.getMessage());
                }
                try {
                    Thread.sleep(1000 * attempt); // Backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return buildErrorResponse("Max retries exceeded");
    }

    private static void writeBody(HttpURLConnection conn, String body) throws IOException {
        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }

    private static String buildErrorResponse(String message) {
        JSONObject errorObj = new JSONObject();
        try {
			errorObj.put("result", "nok");
			errorObj.put("message", message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return errorObj.toString();
    }
    
	public static String httpConnectionCheckToken(String token) throws JSONException {
//		logger.info("httpConnectionCheckToken");
		JSONObject mJsonObj = new JSONObject();
		HttpURLConnection conn = null;
		try {

			URL obj = new URL(CHECKTOKENURL);
			conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("GET");
			
			// Retrieve UUID from ThreadContext (set in UuidMDCFilter)
            String requestUuid = ThreadContext.get(MDC_UUID_KEY);
			
			// Set request headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty(HEADER_REQUEST_TOKEN, token);
            conn.setRequestProperty(HEADER_REQUEST_UUID, requestUuid);
            
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

	public static String httpConnectionGenToken(String token, String username) throws JSONException {
		// logger.info("httpConnectionGenToken");

		JSONObject mJsonObj = new JSONObject();
		HttpURLConnection conn = null;
		try {

			URL obj = new URL(GENTOKENURL + "/" + username);
			// logger.debug(GENTOKENURL + "/" + username);

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
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}

		}

	}
	
	public static String httpConnectionGenTokenV2(String token, String username) throws JSONException {
		// logger.info("httpConnectionGenToken");

		JSONObject mJsonObj = new JSONObject();
		HttpURLConnection conn = null;
		try {

			URL obj = new URL(GENTOKENWOTOKENURL + "/" + username);
			// logger.debug(GENTOKENURL + "/" + username);

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
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();

		} finally {
			if (conn != null) {
				conn.disconnect();
			}

		}

	}
	
	public static String httpConnectionWebhook(String token) throws JSONException {
//		logger.info("httpConnectionCheckToken");
		JSONObject mJsonObj = new JSONObject();
		HttpURLConnection conn = null;
		try {

			URL obj = new URL(CHECKTOKENURL);
			conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("GET");
			
			// Retrieve UUID from ThreadContext (set in UuidMDCFilter)
            String requestUuid = ThreadContext.get(MDC_UUID_KEY);
			
			// Set request headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty(HEADER_REQUEST_TOKEN, token);
            conn.setRequestProperty(HEADER_REQUEST_UUID, requestUuid);
            
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
}
