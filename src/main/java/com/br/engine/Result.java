package com.br.engine;

import org.codehaus.jettison.json.JSONObject;

/** The answer of a form call: {"result": "ok" | "nok", "message": ...}. */
class Result {

	static String ok(String message) throws Exception {
		return of("ok", message);
	}

	static String nok(String message) throws Exception {
		return of("nok", message);
	}

	private static String of(String result, String message) throws Exception {
		return new JSONObject().put("result", result).put("message", message).toString();
	}
}
