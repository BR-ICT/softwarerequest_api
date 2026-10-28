package com.br.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.connection.ConnectDB2;
import com.br.form.FormInterfaceImpl;
import com.br.utility.SqlLog;

/**
 * Interface M3 of a status (SR_PROCESS.PMM3IF '1' interface, '2' check): before an approve on it is written,
 * the engine calls this API's FormInterfaceImpl. Its refusal (an exception) refuses the approve, nothing is
 * written.
 *
 * Called on every approve of a flagged status, also a sub group approving while others still have to.
 * A flagged status skipped by the engine (VACANT / condition) is not called.
 * Also called on the first submit when the start status (00) is flagged: serviceno "" and savedData {};
 * 00 can only be flagged check (Process Flow menu). A check answers ok / refuses only: the posted data stays
 * and no reference is kept, whatever it answers.
 */
public class FormInterfaceCall {

	private static final Logger logger = LogManager.getLogger(FormInterfaceCall.class);

	private static final String DBNAME = EngineConfig.DBNAME;

	/** Approve: the form's answer, or the posted data when the status is not flagged. */
	static FormInterface.Answer beforeApprove(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, String username, String token, String linkToken, String data)
			throws Exception {
		String mode = interfaceMode(cono, divi, servicename, requesttype, status);
		if (mode.isEmpty()) {
			return new FormInterface.Answer(data, "");
		}
		Set<String> ownedFlags = MultiApproveData.ownedFlags(cono, divi, servicename, serviceno, requesttype,
				status, username);
		if (ownedFlags.isEmpty() && MultiApproveData.hasSubGroups(cono, divi, servicename, serviceno, status)) {
			// nothing open for this user: MultiApproveData.approve refuses it right after
			return new FormInterface.Answer(data, "");
		}
		return call(new FormInterface.Request(cono, divi, servicename, serviceno, requesttype, status, username,
				mode, ownedFlags, data, MultiApproveData.loadFormData(cono, divi, servicename, serviceno), token,
				linkToken));
	}

	/** First submit (no service number yet): the form's answer, or the posted data when not flagged. */
	static FormInterface.Answer beforeSubmit(String cono, String divi, String servicename, String requesttype,
			String status, String username, String token, String data) throws Exception {
		String mode = interfaceMode(cono, divi, servicename, requesttype, status);
		if (mode.isEmpty()) {
			return new FormInterface.Answer(data, "");
		}
		return call(new FormInterface.Request(cono, divi, servicename, "", requesttype, status, username, mode,
				Collections.<String>emptySet(), data, "{}", token, ""));
	}

	private static FormInterface.Answer call(FormInterface.Request request) throws Exception {
		logger.info("Interface M3 {} {} status {} ({})", request.servicename, request.serviceno, request.status,
				request.mode);
		FormInterface.Answer answer = new FormInterfaceImpl().call(request);
		return answerToSave(request, answer);
	}

	/** A check only says ok: the posted data stays and nothing is kept as reference. */
	static FormInterface.Answer answerToSave(FormInterface.Request request, FormInterface.Answer answer) {
		if (request.isCheck() || answer == null) {
			return FormInterface.Answer.unchanged(request);
		}
		return new FormInterface.Answer(answer.data == null ? request.data : answer.data, answer.reference);
	}

	/** The mode of PMM3IF: "interface" ('1'), "check" ('2') or "" (not flagged). */
	static String modeOf(String m3if) {
		switch (m3if == null ? "" : m3if.trim()) {
		case FormInterface.PMM3IF_INTERFACE:
			return FormInterface.MODE_INTERFACE;
		case FormInterface.PMM3IF_CHECK:
			return FormInterface.MODE_CHECK;
		default:
			return "";
		}
	}

	// every row of a status carries the same PMM3IF (Process Flow menu)
	private static String interfaceMode(String cono, String divi, String servicename, String requesttype,
			String status) throws Exception {
		String query = "SELECT PMM3IF FROM " + DBNAME + ".SR_PROCESS \n"
				+ "WHERE PMCONO = ? AND PMDIVI = ? AND PMCODE = ? AND PMRQTY = ? AND PMSTAT = ? \n"
				+ "AND PMM3IF IN ('1', '2') \n"
				+ "FETCH FIRST 1 ROWS ONLY";
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, cono);
			ps.setString(2, divi);
			ps.setString(3, servicename);
			ps.setString(4, requesttype);
			ps.setString(5, status);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? modeOf(rs.getString("PMM3IF")) : "";
			}
		}
	}
}
