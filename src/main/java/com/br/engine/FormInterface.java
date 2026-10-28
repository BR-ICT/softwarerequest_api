package com.br.engine;

import java.util.Collections;
import java.util.Set;

/**
 * What one form does with the ERP when its request reaches a status flagged Interface M3
 * (SR_PROCESS.PMM3IF '1' interface, '2' check). Each form API implements it once, in
 * com.br.form.FormInterfaceImpl (not part of the engine copy): e.g. itemreq_api runs its MMS001 / MMS015 /
 * MMS002 calls there, supplier_request_api its CRS620 calls and the duplicate tax ID check.
 *
 * Called before anything of the approve / first submit is written: throwing refuses it (the message goes to
 * the user), nothing is written. One attempt only, no retry: the form may have written to M3 already.
 */
public interface FormInterface {

	/** SR_PROCESS.PMM3IF */
	String PMM3IF_INTERFACE = "1";
	String PMM3IF_CHECK = "2";

	/** Request.mode */
	String MODE_INTERFACE = "interface";
	String MODE_CHECK = "check";

	/**
	 * mode "check": only read the ERP and refuse (throw) or accept; the answer's data / reference are not used.
	 * mode "interface": may write the ERP; answer the form data to save and the reference (FHDES2, e.g. the
	 * code M3 gave the new item), or Answer.unchanged(request).
	 */
	Answer call(Request request) throws Exception;

	/** One call: the request at its status, who approves, the form of this call and the one in the DB. */
	final class Request {
		public final String cono;
		public final String divi;
		public final String servicename;
		/** "" on the first submit (no service number yet) */
		public final String serviceno;
		public final String requesttype;
		public final String status;
		/** the approver (first submit: the requester) */
		public final String username;
		public final String mode;
		/** approve mode ALL: flags of the sub groups the user approves for, else empty */
		public final Set<String> ownedFlags;
		/** the form of this call (JSON) */
		public final String data;
		/** the form in SR_FLOWDETAIL (JSON, "{}" on the first submit) */
		public final String savedData;
		/** the user's x-access-token, "" when approving from the email link */
		public final String token;
		/** x-approve-token of the email link, "" when logged in */
		public final String linkToken;

		public Request(String cono, String divi, String servicename, String serviceno, String requesttype,
				String status, String username, String mode, Set<String> ownedFlags, String data, String savedData,
				String token, String linkToken) {
			this.cono = cono;
			this.divi = divi;
			this.servicename = servicename;
			this.serviceno = serviceno;
			this.requesttype = requesttype;
			this.status = status;
			this.username = username;
			this.mode = mode;
			this.ownedFlags = ownedFlags == null ? Collections.<String>emptySet() : ownedFlags;
			this.data = data;
			this.savedData = savedData;
			this.token = token == null ? "" : token;
			this.linkToken = linkToken == null ? "" : linkToken;
		}

		public boolean isCheck() {
			return MODE_CHECK.equals(mode);
		}
	}

	/** What to save after the call: the form data and the reference (FHDES2, "" = leave it). */
	final class Answer {
		public final String data;
		public final String reference;

		public Answer(String data, String reference) {
			this.data = data;
			this.reference = reference == null ? "" : reference.trim();
		}

		/** The posted form saved as it is, no reference. */
		public static Answer unchanged(Request request) {
			return new Answer(request.data, "");
		}
	}
}
