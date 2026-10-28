package com.br.engine;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

/**
 * The workflow (n8n, EngineConfig.WORKFLOW): emails the approver of the status a request has moved to.
 * A workflow that cannot be reached is logged, not a reason to fail: the request is written already.
 */
class Workflow {

	private static final Logger logger = LogManager.getLogger(Workflow.class);

	private static final int CONNECT_TIMEOUT = 10000;
	private static final int READ_TIMEOUT = 30000;

	static void notify(String cono, String divi, String servicename, String serviceno, String requesttype,
			String status) throws Exception {
		JSONObject body = new JSONObject();
		body.put("cono", cono);
		body.put("divi", divi);
		body.put("servicename", servicename);
		body.put("serviceno", serviceno);
		body.put("requesttype", requesttype);
		body.put("status", status);
		logger.info("body: " + body.toString());

		try {
			logger.info("response: " + post(EngineConfig.WORKFLOW, body.toString()));
		} catch (Exception e) {
			logger.error("Workflow {} not reached for {} status {}: {}", EngineConfig.WORKFLOW, serviceno, status,
					e.getMessage());
		}
	}

	private static String post(String url, String body) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		try {
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("x-access-token", EngineConfig.WORKFLOWTOKEN);
			try (OutputStream os = conn.getOutputStream()) {
				os.write(body.getBytes(StandardCharsets.UTF_8));
			}
			int code = conn.getResponseCode();
			InputStream in = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
			return "HTTP " + code + " " + (in == null ? "" : read(in));
		} finally {
			conn.disconnect();
		}
	}

	private static String read(InputStream in) throws Exception {
		try (InputStream stream = in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[4096];
			for (int n; (n = stream.read(buffer)) > 0;) {
				out.write(buffer, 0, n);
			}
			return new String(out.toByteArray(), StandardCharsets.UTF_8);
		}
	}
}
