package com.br.engine;

import java.util.Arrays;

/**
 * Whether a user may approve / reject a request at a status. Checked before anything is called or written,
 * Interface M3 included: an approve sent twice (double click, the email link opened again) must not reach
 * M3 a second time.
 *
 * The request must still be at that status and open, nobody approved the status yet, the caller (login token
 * or approve link) must be vApproveBy of the form, and vApproveBy one of the approvers of the status
 * (SR_FLOWAPPROVE.FAAPLI, "USER1,USER2"; approve mode ALL: the users still to approve).
 */
class ApproveGuard {

	/** null = may go on, else the message for the user. */
	static String refusal(String cono, String divi, String servicename, String serviceno, String status,
			boolean approval, String caller, String approveBy, FlowStep step) throws Exception {
		return decide(FlowQuery.getHeadStatus(cono, divi, servicename, serviceno),
				FlowQuery.checkApproveBy(cono, divi, servicename, serviceno, status), step.approver, serviceno,
				status, approval, caller, approveBy);
	}

	static String decide(String[] head, String approvedBy, String approvers, String serviceno, String status,
			boolean approval, String caller, String approveBy) {
		String refused = approval ? "Can't approve, " : "Can't reject, ";
		if (head == null) {
			return refused + "service number " + serviceno + " does not exist.";
		}
		if (head[1].equals("3") || head[1].equals("9")) {
			return refused + "service number " + serviceno + " is already "
					+ (head[1].equals("3") ? "complete" : "cancelled") + ".";
		}
		if (!head[0].equals(status)) {
			return refused + "service number " + serviceno + " is at status " + head[0] + " now (not " + status
					+ "), please reload.";
		}
		if (approvedBy != null && !approvedBy.trim().isEmpty()) {
			return refused + "Service approve already by " + approvedBy.trim();
		}
		String user = approveBy == null ? "" : approveBy.trim();
		if (caller == null || !caller.trim().equalsIgnoreCase(user)) {
			return refused + "you are " + (caller == null ? "" : caller.trim()) + " but the form was sent as " + user
					+ ", please reload.";
		}
		if (!isApprover(approvers, user)) {
			return refused + user + " is not an approver of status " + status + " (" + approvers + ").";
		}
		return null;
	}

	/** First submit: null = may go on, else the message (the caller of the token must be vRequester). */
	static String submitRefusal(String caller, String requester) {
		String user = requester == null ? "" : requester.trim();
		if (caller == null || user.isEmpty() || !caller.trim().equalsIgnoreCase(user)) {
			return "Can't Create service number, you are " + (caller == null ? "" : caller.trim())
					+ " but the form was sent as " + user + ", please reload.";
		}
		return null;
	}

	static boolean isApprover(String approvers, String user) {
		return approvers != null && Arrays.stream(approvers.split(","))
				.anyMatch(approver -> approver.trim().equalsIgnoreCase(user));
	}
}
