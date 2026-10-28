package com.br.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.br.connection.ConnectDB2;
import com.br.utility.SqlLog;

/**
 * Approve mode ALL (SR_PROCESS.PMAPMD = '1', see md/MULTI_APPROVE_PLAN.md): every sub group of the status
 * approves before the flow moves on.
 *
 * - SR_FLOWAPPROVESUB holds one row per request + status + sub group; FSAPLI = users of the sub group
 *   (any one of them approves for it) or VACANT (sub group condition false: not counted, not mailed).
 * - SR_FLOWAPPROVE keeps its one row per status. While the request waits at a mode 1 status, its FAAPLI
 *   holds the users of the sub groups that have not approved yet, so lists, permission and email follow.
 *   Otherwise FAAPLI holds the users of every non-VACANT sub group.
 * - A request whose status has no child rows (created before the status was switched to mode 1) keeps
 *   the old rule: any one approver.
 * No transaction: the tables are not journaled. Child rows are written with FSAPBY = '' in the WHERE and
 * counted again afterwards, so two approvers at the same moment cannot both stay "waiting".
 */
public class MultiApproveData {

	private static final Logger logger = LogManager.getLogger(MultiApproveData.class);

	protected static String DBNAME = EngineConfig.DBNAME;

	public static final String MODE_ALL = "1";
	static final String VACANT = "VACANT";
	// SR_FLOWAPPROVE.FAAPLI / SR_FLOWAPPROVESUB.FSAPLI are VARCHAR(255)
	static final int APPROVER_LIST_LENGTH = 255;

	/** One sub group of a mode 1 status of a request. */
	static class SubGroup {
		final String status, group, subGroup, users;
		final String approvedBy;

		SubGroup(String status, String group, String subGroup, String users, String approvedBy) {
			this.status = status;
			this.group = group;
			this.subGroup = subGroup;
			this.users = users;
			this.approvedBy = approvedBy;
		}

		boolean isVacant() {
			return VACANT.equals(users);
		}

		boolean isOpen() {
			return !isVacant() && approvedBy.isEmpty();
		}

		boolean hasUser(String username) {
			for (String user : users.split(",")) {
				if (user.trim().equalsIgnoreCase(username.trim())) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * What prepareUpdateServiceForm does after the mode 1 check of an approve:
	 * CONTINUE = not a mode 1 status of this request, or the last sub group approves: run the normal
	 * approve, then finishApprove. WAIT = a sub group approved, others still have to (message for the
	 * user). REFUSE = the user has nothing to approve here (message).
	 */
	static class Outcome {
		enum Kind {
			CONTINUE, WAIT, REFUSE
		}

		final Kind kind;
		final String message;
		final List<SubGroup> rows;

		private Outcome(Kind kind, String message, List<SubGroup> rows) {
			this.kind = kind;
			this.message = message;
			this.rows = rows;
		}
	}

	// ---------------------------------------------------------------------
	// Rules (no DB)
	// ---------------------------------------------------------------------

	/** Users of the sub groups that still have to approve, comma separated without duplicates; VACANT if none. */
	static String pendingApprovers(List<SubGroup> rows) {
		return joinUsers(rows, true);
	}

	/** Users of every non-VACANT sub group; VACANT if none (the engine then skips the status). */
	static String allApprovers(List<SubGroup> rows) {
		return joinUsers(rows, false);
	}

	private static String joinUsers(List<SubGroup> rows, boolean openOnly) {
		Set<String> users = new LinkedHashSet<>();
		for (SubGroup row : rows) {
			if (row.isVacant() || (openOnly && !row.isOpen())) {
				continue;
			}
			for (String user : row.users.split(",")) {
				if (!user.trim().isEmpty()) {
					users.add(user.trim());
				}
			}
		}
		return users.isEmpty() ? VACANT : String.join(",", users);
	}

	/** Open sub groups the user may approve for (a user in two sub groups approves both at once). */
	static List<SubGroup> openRowsOf(List<SubGroup> rows, String username) {
		List<SubGroup> mine = new ArrayList<>();
		for (SubGroup row : rows) {
			if (row.isOpen() && row.hasUser(username)) {
				mine.add(row);
			}
		}
		return mine;
	}

	/** Mode 1 rows of a status, grouped by status in flow order. */
	static Map<String, List<SubGroup>> byStatus(List<SubGroup> rows) {
		Map<String, List<SubGroup>> statuses = new LinkedHashMap<>();
		for (SubGroup row : rows) {
			statuses.computeIfAbsent(row.status, key -> new ArrayList<>()).add(row);
		}
		return statuses;
	}

	/** The approvers of a status must fit FAAPLI; refused before anything is written. */
	static void checkLength(Map<String, List<SubGroup>> statuses) throws Exception {
		for (Map.Entry<String, List<SubGroup>> status : statuses.entrySet()) {
			String users = allApprovers(status.getValue());
			if (users.length() > APPROVER_LIST_LENGTH) {
				throw new Exception("Status " + status.getKey() + " has too many approvers for one request ("
						+ users.length() + " characters, max " + APPROVER_LIST_LENGTH
						+ "). Remove users from its sub groups.");
			}
		}
	}

	/**
	 * Decides an approve on a mode 1 status from its rows, before anything is written.
	 * rows empty = not mode 1 for this request. Nothing open any more (all approved, e.g. the last
	 * approval was interrupted after the child rows were written) = CONTINUE, so the status can still move.
	 */
	static Outcome decide(String status, List<SubGroup> rows, String username) {
		if (rows.isEmpty() || VACANT.equals(pendingApprovers(rows))) {
			return new Outcome(Outcome.Kind.CONTINUE, "", new ArrayList<>());
		}
		List<SubGroup> mine = openRowsOf(rows, username);
		if (mine.isEmpty()) {
			return new Outcome(Outcome.Kind.REFUSE,
					refuseReason(status, rows, username) + ", waiting for: " + pendingApprovers(rows), mine);
		}
		List<SubGroup> others = new ArrayList<>(rows);
		others.removeAll(mine);
		if (VACANT.equals(pendingApprovers(others))) {
			return new Outcome(Outcome.Kind.CONTINUE, "", mine);
		}
		return new Outcome(Outcome.Kind.WAIT, "", mine);
	}

	// Why a user with no open sub group cannot approve: approved before / a colleague approved the sub group
	// (e.g. "ADDON already approved by MAHAKI_CHU") / not in any sub group of the status
	private static String refuseReason(String status, List<SubGroup> rows, String username) {
		if (rows.stream().anyMatch(row -> row.approvedBy.equalsIgnoreCase(username.trim()))) {
			return "You already approved status " + status;
		}
		List<String> approvedByOthers = new ArrayList<>();
		for (SubGroup row : rows) {
			if (!row.isVacant() && !row.isOpen() && row.hasUser(username)) {
				approvedByOthers.add(row.subGroup + " already approved by " + row.approvedBy);
			}
		}
		if (!approvedByOthers.isEmpty()) {
			return String.join(", ", approvedByOthers);
		}
		return "You are not an approver of status " + status;
	}

	static String waitMessage(List<SubGroup> rows) {
		long open = rows.stream().filter(SubGroup::isOpen).count();
		return "Approve complete, waiting for " + open + " more sub group(s): " + pendingApprovers(rows);
	}

	// ---------------------------------------------------------------------
	// Create / resubmit
	// ---------------------------------------------------------------------

	/**
	 * Sub groups of every mode 1 status of the flow (VI_PROCESS_MULTISTS), with the condition of each
	 * sub group checked against the form data: false = VACANT. Throws (before any write) when a
	 * condition cannot be checked or a status has more approvers than FAAPLI holds.
	 */
	static List<SubGroup> buildSubGroups(String cono, String divi, String servicename, String requesttype,
			JSONObject data) throws Exception {
		String query = "SELECT PMSTAT, PMGROU, PMSGRO, NAME_SERIAL \n"
				+ ", COALESCE(PMCFLD, '') AS PMCFLD, COALESCE(PMCOPR, '') AS PMCOPR, COALESCE(PMCVAL, '') AS PMCVAL \n"
				+ "FROM " + DBNAME + ".VI_PROCESS_MULTISTS \n"
				+ "WHERE PMCONO = ? AND PMDIVI = ? AND PMCODE = ? AND PMRQTY = ? \n"
				+ "ORDER BY INT(PMSTAT), PMSGRO";

		List<SubGroup> rows = new ArrayList<>();
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, cono);
			ps.setString(2, divi);
			ps.setString(3, servicename);
			ps.setString(4, requesttype);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String status = rs.getString("PMSTAT").trim();
					String users = rs.getString("NAME_SERIAL").trim();
					boolean needed = ServiceFormEngine.conditionHolds(data, status, rs.getString("PMCFLD").trim(),
							rs.getString("PMCOPR").trim(), rs.getString("PMCVAL").trim());
					rows.add(new SubGroup(status, rs.getString("PMGROU").trim(), rs.getString("PMSGRO").trim(),
							needed ? users : VACANT, ""));
				}
			}
		}
		checkLength(byStatus(rows));
		return rows;
	}

	/**
	 * Replaces the child rows of a request (create, and resubmit from 00 with maybe other form flags) and
	 * sets FAAPLI of each mode 1 status to its approvers.
	 */
	static void saveSubGroups(String cono, String divi, String servicename, String serviceno, List<SubGroup> rows,
			String username) throws Exception {
		try (Connection conn = ConnectDB2.doConnect()) {
			String delete = "DELETE FROM " + DBNAME + ".SR_FLOWAPPROVESUB \n"
					+ "WHERE FSCONO = ? AND FSDIVI = ? AND FSCODE = ? AND FSSRNO = ?";
			try (PreparedStatement ps = SqlLog.prepare(conn, delete)) {
				ps.setString(1, cono);
				ps.setString(2, divi);
				ps.setString(3, servicename);
				ps.setString(4, serviceno);
				ps.executeUpdate();
			}

			String insert = "INSERT INTO " + DBNAME + ".SR_FLOWAPPROVESUB \n"
					+ "(FSCONO, FSDIVI, FSCODE, FSSRNO, FSSTAT, FSGROU, FSSGRO, FSAPLI, FSAPBY, FSAPDA, FSAPTI, FSDES1, FSENDA, FSENTI, FSENUS) \n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, '', NULL, NULL, '', CURRENT_DATE, CURRENT_TIME, ?)";
			try (PreparedStatement ps = SqlLog.prepare(conn, insert)) {
				for (SubGroup row : rows) {
					ps.setString(1, cono);
					ps.setString(2, divi);
					ps.setString(3, servicename);
					ps.setString(4, serviceno);
					ps.setString(5, row.status);
					ps.setString(6, row.group);
					ps.setString(7, row.subGroup);
					ps.setString(8, row.users);
					ps.setString(9, username);
					ps.executeUpdate();
				}
			}

			for (Map.Entry<String, List<SubGroup>> status : byStatus(rows).entrySet()) {
				String users = allApprovers(status.getValue());
				logger.info("Approve mode ALL, status {}: {}", status.getKey(), users);
				updateApproverList(conn, cono, divi, servicename, serviceno, status.getKey(), users, username);
			}
		}
	}

	// ---------------------------------------------------------------------
	// Approve / reject
	// ---------------------------------------------------------------------

	/**
	 * First part of an approve on a status. WAIT already wrote the user's sub groups and the remaining
	 * names to FAAPLI; CONTINUE wrote nothing - the caller runs the normal approve and then finishApprove.
	 */
	static Outcome approve(String cono, String divi, String servicename, String serviceno, String status,
			String username, String remark) throws Exception {
		try (Connection conn = ConnectDB2.doConnect()) {
			List<SubGroup> rows = loadSubGroups(conn, cono, divi, servicename, serviceno, status);
			Outcome outcome = decide(status, rows, username);
			if (outcome.kind != Outcome.Kind.WAIT) {
				return outcome;
			}

			writeApproval(conn, cono, divi, servicename, serviceno, outcome.rows, username, remark);
			List<SubGroup> now = loadSubGroups(conn, cono, divi, servicename, serviceno, status);
			String pending = pendingApprovers(now);
			if (VACANT.equals(pending)) {
				// the others approved at the same moment: this approval moves the status. The rows stay this
				// user's sub groups (mergeLineReplies needs them; finishApprove writes them again as a no-op)
				return new Outcome(Outcome.Kind.CONTINUE, "", outcome.rows);
			}
			updateApproverList(conn, cono, divi, servicename, serviceno, status, pending, username);
			logger.info("Approve mode ALL, status {} approved by {}, waiting for {}", status, username, pending);
			return new Outcome(Outcome.Kind.WAIT, waitMessage(now), outcome.rows);
		}
	}

	/** After the normal approve of a CONTINUE: the user's sub groups, and FAAPLI back to every approver. */
	static void finishApprove(String cono, String divi, String servicename, String serviceno, String status,
			Outcome outcome, String username, String remark) throws Exception {
		try (Connection conn = ConnectDB2.doConnect()) {
			List<SubGroup> rows = loadSubGroups(conn, cono, divi, servicename, serviceno, status);
			if (rows.isEmpty()) {
				return;
			}
			writeApproval(conn, cono, divi, servicename, serviceno, outcome.rows, username, remark);
			updateApproverList(conn, cono, divi, servicename, serviceno, status, allApprovers(rows), username);
		}
	}

	/**
	 * Reject: the statuses from fromStatus to toStatus are approved again (as updateServiceFlowReject resets
	 * FAAPBY), so their sub groups are cleared and FAAPLI gets every approver back.
	 */
	static void resetApprovals(String cono, String divi, String servicename, String serviceno, String fromStatus,
			String toStatus, String username) throws Exception {
		String range = "AND INT(FSSTAT) BETWEEN INT(CAST(? AS VARCHAR(3))) AND INT(CAST(? AS VARCHAR(3)))";
		try (Connection conn = ConnectDB2.doConnect()) {
			String query = "SELECT FSSTAT, FSGROU, FSSGRO, FSAPLI, COALESCE(FSAPBY, '') AS FSAPBY \n"
					+ "FROM " + DBNAME + ".SR_FLOWAPPROVESUB \n"
					+ "WHERE FSCONO = ? AND FSDIVI = ? AND FSCODE = ? AND FSSRNO = ? \n" + range + " \n"
					+ "ORDER BY INT(FSSTAT), FSSGRO";
			List<SubGroup> rows = select(conn, query, cono, divi, servicename, serviceno, fromStatus, toStatus);
			if (rows.isEmpty()) {
				return;
			}

			String update = "UPDATE " + DBNAME + ".SR_FLOWAPPROVESUB \n"
					+ "SET FSAPBY = '', FSAPDA = NULL, FSAPTI = NULL, FSDES1 = '' \n"
					+ ", FSENDA = CURRENT_DATE, FSENTI = CURRENT_TIME, FSENUS = ? \n"
					+ "WHERE FSCONO = ? AND FSDIVI = ? AND FSCODE = ? AND FSSRNO = ? \n" + range;
			try (PreparedStatement ps = SqlLog.prepare(conn, update)) {
				ps.setString(1, username);
				ps.setString(2, cono);
				ps.setString(3, divi);
				ps.setString(4, servicename);
				ps.setString(5, serviceno);
				ps.setString(6, fromStatus);
				ps.setString(7, toStatus);
				ps.executeUpdate();
			}
			for (Map.Entry<String, List<SubGroup>> status : byStatus(rows).entrySet()) {
				updateApproverList(conn, cono, divi, servicename, serviceno, status.getKey(),
						allApprovers(status.getValue()), username);
			}
		}
	}

	/**
	 * A status with child rows decides "skip" by FAAPLI only (all sub groups VACANT): its condition differs
	 * per sub group and was applied when the rows were built, so the status condition is not looked at.
	 */
	static boolean hasSubGroups(String cono, String divi, String servicename, String serviceno, String status)
			throws Exception {
		try (Connection conn = ConnectDB2.doConnect()) {
			return !loadSubGroups(conn, cono, divi, servicename, serviceno, status).isEmpty();
		}
	}

	// ---------------------------------------------------------------------
	// Replies per line (USRQ authorizeLines.ict*)
	// ---------------------------------------------------------------------

	static final String LINES_KEY = "authorizeLines";
	// the line's sub group flag, the same name as the sub group condition SR_PROCESS.PMCFLD (e.g. appERP)
	static final String LINE_FLAG_KEY = "approvalFlag";
	// a line is found again by its application (the form refuses a duplicate application)
	static final String LINE_ID_KEY = "application";
	// USRQ Add-on: ictUsername = the AS400 username ICT gives; ictUserCreated = Y once the service API created
	// it (Interface M3, FormInterfaceCall). ictUserCreated is never taken from the form, and a created
	// username cannot be changed any more
	static final String USERNAME_KEY = "ictUsername";
	static final String CREATED_KEY = "ictUserCreated";
	static final String[] REPLY_KEYS = { "ictResult", "ictReply", "ictBy", "ictDate", USERNAME_KEY };

	/**
	 * The sub groups of a mode 1 status approve in parallel and each sends the whole form, so the last
	 * one would overwrite the replies of the others. Starts from the data in the DB and takes from this
	 * call only the reply keys of the lines whose flag belongs to the user's sub groups; every other
	 * key stays as in the DB (read-only for the approvers anyway).
	 * Not a mode 1 approve (no rows) or no lines in the form: the posted data as before.
	 */
	static String mergeApproverReplies(String cono, String divi, String servicename, String serviceno,
			String requesttype, Outcome outcome, String postedData) throws Exception {
		if (outcome == null || outcome.rows.isEmpty() || !new JSONObject(postedData).has(LINES_KEY)) {
			return postedData;
		}
		Set<String> flags = conditionFields(cono, divi, servicename, requesttype, outcome.rows);
		String merged = mergeLineReplies(loadFormData(cono, divi, servicename, serviceno), postedData, flags);
		logger.info("Approve mode ALL, replies of lines {} taken from the form, the rest from the DB", flags);
		return merged;
	}

	/**
	 * Reject on a mode 1 status: the same merge as mergeApproverReplies, with the sub groups the user can
	 * still approve (read before resetApprovals opens them all again). The others may have approved and
	 * replied after this user opened the form, so their replies are kept from the DB.
	 * Not a mode 1 status (no rows) or no lines in the form: the posted data as before.
	 */
	static String mergeRejecterReplies(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, String username, String postedData) throws Exception {
		if (!new JSONObject(postedData).has(LINES_KEY)) {
			return postedData;
		}
		List<SubGroup> rows;
		try (Connection conn = ConnectDB2.doConnect()) {
			rows = loadSubGroups(conn, cono, divi, servicename, serviceno, status);
		}
		if (rows.isEmpty()) {
			return postedData;
		}
		List<SubGroup> mine = openRowsOf(rows, username);
		Set<String> flags = mine.isEmpty() ? new HashSet<>()
				: conditionFields(cono, divi, servicename, requesttype, mine);
		String merged = mergeLineReplies(loadFormData(cono, divi, servicename, serviceno), postedData, flags);
		logger.info("Reject mode ALL, replies of lines {} taken from the form, the rest from the DB", flags);
		return merged;
	}

	/**
	 * Flags (sub group condition fields) of the sub groups the user can still approve at the status, as
	 * {"flags": ["appERP", ...]}: the form opens the reply of those lines only, the same lines that
	 * mergeApproverReplies keeps. Approved already, not an approver, or not a mode 1 status: no flags.
	 */
	public static String approverFlags(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, String username) throws Exception {
		Set<String> flags = ownedFlags(cono, divi, servicename, serviceno, requesttype, status, username);
		return new JSONObject().put("flags", new JSONArray(flags)).toString();
	}

	/** The flags of approverFlags as a set (empty when the user has nothing open at the status). */
	static Set<String> ownedFlags(String cono, String divi, String servicename, String serviceno,
			String requesttype, String status, String username) throws Exception {
		List<SubGroup> mine;
		try (Connection conn = ConnectDB2.doConnect()) {
			mine = openRowsOf(loadSubGroups(conn, cono, divi, servicename, serviceno, status), username);
		}
		return mine.isEmpty() ? new HashSet<>() : conditionFields(cono, divi, servicename, requesttype, mine);
	}

	/** Rule of mergeApproverReplies, without a DB. */
	static String mergeLineReplies(String dbData, String postedData, Set<String> ownedFlags) throws JSONException {
		JSONObject db = new JSONObject(dbData);
		JSONArray dbLines = db.optJSONArray(LINES_KEY);
		JSONArray postedLines = new JSONObject(postedData).optJSONArray(LINES_KEY);
		if (dbLines == null || postedLines == null) {
			return dbData;
		}
		for (int i = 0; i < dbLines.length(); i++) {
			JSONObject dbLine = dbLines.optJSONObject(i);
			if (dbLine == null || !ownedFlags.contains(dbLine.optString(LINE_FLAG_KEY))) {
				continue;
			}
			JSONObject postedLine = findLine(postedLines, dbLine.optString(LINE_ID_KEY));
			if (postedLine == null) {
				continue;
			}
			boolean created = "Y".equals(dbLine.optString(CREATED_KEY));
			for (String key : REPLY_KEYS) {
				if (created && key.equals(USERNAME_KEY)) {
					continue;
				}
				if (postedLine.has(key)) {
					dbLine.put(key, postedLine.get(key));
				} else {
					dbLine.remove(key);
				}
			}
		}
		return db.toString();
	}

	static JSONObject findLine(JSONArray lines, String id) {
		for (int i = 0; i < lines.length(); i++) {
			JSONObject line = lines.optJSONObject(i);
			if (line != null && !id.isEmpty() && id.equals(line.optString(LINE_ID_KEY))) {
				return line;
			}
		}
		return null;
	}

	/** Condition fields (PMCFLD) of the given sub groups, e.g. [appERP] or [appERP, appAddon]. */
	private static Set<String> conditionFields(String cono, String divi, String servicename, String requesttype,
			List<SubGroup> rows) throws Exception {
		String query = "SELECT PMGROU, PMSGRO, COALESCE(PMCFLD, '') AS PMCFLD \n"
				+ "FROM " + DBNAME + ".VI_PROCESS_MULTISTS \n"
				+ "WHERE PMCONO = ? AND PMDIVI = ? AND PMCODE = ? AND PMRQTY = ? AND PMSTAT = ?";
		Set<String> fields = new HashSet<>();
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, cono);
			ps.setString(2, divi);
			ps.setString(3, servicename);
			ps.setString(4, requesttype);
			ps.setString(5, rows.get(0).status);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String group = rs.getString("PMGROU").trim(), subGroup = rs.getString("PMSGRO").trim();
					String field = rs.getString("PMCFLD").trim();
					boolean mine = rows.stream()
							.anyMatch(row -> row.group.equals(group) && row.subGroup.equals(subGroup));
					if (mine && !field.isEmpty()) {
						fields.add(field);
					}
				}
			}
		}
		return fields;
	}

	static String loadFormData(String cono, String divi, String servicename, String serviceno)
			throws Exception {
		String query = "SELECT FDDATA FROM " + DBNAME + ".SR_FLOWDETAIL \n"
				+ "WHERE FDCONO = ? AND FDDIVI = ? AND FDCODE = ? AND FDSRNO = ?";
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, cono);
			ps.setString(2, divi);
			ps.setString(3, servicename);
			ps.setString(4, serviceno);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("FDDATA");
				}
			}
		}
		throw new Exception("Form data of " + servicename + " " + serviceno + " not found.");
	}

	// ---------------------------------------------------------------------
	// DB helpers
	// ---------------------------------------------------------------------

	private static List<SubGroup> loadSubGroups(Connection conn, String cono, String divi, String servicename,
			String serviceno, String status) throws Exception {
		String query = "SELECT FSSTAT, FSGROU, FSSGRO, FSAPLI, COALESCE(FSAPBY, '') AS FSAPBY \n"
				+ "FROM " + DBNAME + ".SR_FLOWAPPROVESUB \n"
				+ "WHERE FSCONO = ? AND FSDIVI = ? AND FSCODE = ? AND FSSRNO = ? AND FSSTAT = ? \n"
				+ "ORDER BY FSSGRO";
		return select(conn, query, cono, divi, servicename, serviceno, status);
	}

	private static List<SubGroup> select(Connection conn, String query, String... params) throws Exception {
		List<SubGroup> rows = new ArrayList<>();
		try (PreparedStatement ps = SqlLog.prepare(conn, query)) {
			for (int i = 0; i < params.length; i++) {
				ps.setString(i + 1, params[i]);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rows.add(new SubGroup(rs.getString("FSSTAT").trim(), rs.getString("FSGROU").trim(),
							rs.getString("FSSGRO").trim(), rs.getString("FSAPLI").trim(),
							rs.getString("FSAPBY").trim()));
				}
			}
		}
		return rows;
	}

	private static void writeApproval(Connection conn, String cono, String divi, String servicename,
			String serviceno, List<SubGroup> rows, String username, String remark) throws Exception {
		String update = "UPDATE " + DBNAME + ".SR_FLOWAPPROVESUB \n"
				+ "SET FSAPBY = ?, FSAPDA = CURRENT_DATE, FSAPTI = CURRENT_TIME, FSDES1 = ? \n"
				+ ", FSENDA = CURRENT_DATE, FSENTI = CURRENT_TIME, FSENUS = ? \n"
				+ "WHERE FSCONO = ? AND FSDIVI = ? AND FSCODE = ? AND FSSRNO = ? \n"
				+ "AND FSSTAT = ? AND FSGROU = ? AND FSSGRO = ? AND FSAPBY = ''";
		try (PreparedStatement ps = SqlLog.prepare(conn, update)) {
			for (SubGroup row : rows) {
				ps.setString(1, username);
				ps.setString(2, remark.length() > APPROVER_LIST_LENGTH ? remark.substring(0, APPROVER_LIST_LENGTH)
						: remark);
				ps.setString(3, username);
				ps.setString(4, cono);
				ps.setString(5, divi);
				ps.setString(6, servicename);
				ps.setString(7, serviceno);
				ps.setString(8, row.status);
				ps.setString(9, row.group);
				ps.setString(10, row.subGroup);
				ps.executeUpdate();
			}
		}
	}

	private static void updateApproverList(Connection conn, String cono, String divi, String servicename,
			String serviceno, String status, String users, String username) throws Exception {
		String update = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n"
				+ "SET FAAPLI = ?, FAENDA = CURRENT_DATE, FAENTI = CURRENT_TIME, FAENUS = ? \n"
				+ "WHERE FACONO = ? AND FADIVI = ? AND FACODE = ? AND FASRNO = ? AND FASTAT = ?";
		try (PreparedStatement ps = SqlLog.prepare(conn, update)) {
			ps.setString(1, users);
			ps.setString(2, username);
			ps.setString(3, cono);
			ps.setString(4, divi);
			ps.setString(5, servicename);
			ps.setString(6, serviceno);
			ps.setString(7, status);
			ps.executeUpdate();
		}
	}
}
