package com.br.utility;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * The token of the approve link from email (n8n), sent as x-approve-token when the approver is not logged
 * in: base64 of "serviceno|status|approver|AhaToken".
 *
 * Not signed: anyone who knows a service no and an approver can build one. To be replaced by a signed token
 * (HMAC with expiry) made by n8n; only decode() changes then.
 */
public class ApproveLinkToken {

	private static final String SUFFIX = "AhaToken";

	public final String serviceno;
	public final String status;
	public final String approver;

	private ApproveLinkToken(String serviceno, String status, String approver) {
		this.serviceno = serviceno;
		this.status = status;
		this.approver = approver;
	}

	/** null when the token is not one of an approve link */
	public static ApproveLinkToken decode(String token) {
		if (token == null || token.trim().isEmpty()) {
			return null;
		}
		String text;
		try {
			text = new String(Base64.getDecoder().decode(token.trim()), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException e) {
			return null;
		}
		String[] parts = text.split("\\|", -1);
		if (parts.length != 4 || !SUFFIX.equals(parts[3]) || parts[0].trim().isEmpty()
				|| parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
			return null;
		}
		return new ApproveLinkToken(parts[0].trim(), parts[1].trim(), parts[2].trim());
	}

	/** the link was sent for this request and status */
	public boolean isFor(String serviceno, String status) {
		return this.serviceno.equals(serviceno == null ? "" : serviceno.trim())
				&& this.status.equals(status == null ? "" : status.trim());
	}
}
