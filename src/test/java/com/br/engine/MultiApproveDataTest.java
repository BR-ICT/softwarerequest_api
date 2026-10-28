package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import com.br.engine.MultiApproveData.Outcome;
import com.br.engine.MultiApproveData.SubGroup;


/**
 * Rules of approve mode ALL (MultiApproveData) without a DB: who still has to approve, what an approve
 * does, and the FAAPLI length limit.
 */
public class MultiApproveDataTest {

	private static SubGroup row(String subGroup, String users, String approvedBy) {
		return new SubGroup("10", "ICT", subGroup, users, approvedBy);
	}

	// USRQ status 10: ERP, ADDON, OTHER
	private static List<SubGroup> usrq(String erpBy, String addonBy, String otherUsers) {
		return Arrays.asList(row("ERP", "erp1,erp2", erpBy), row("ADDON", "add1", addonBy),
				row("OTHER", otherUsers, ""));
	}

	@Test
	public void pending_listsOpenSubGroupsOnly() {
		assertEquals("erp1,erp2,add1,oth1", MultiApproveData.pendingApprovers(usrq("", "", "oth1")));
		assertEquals("add1,oth1", MultiApproveData.pendingApprovers(usrq("erp2", "", "oth1")));
		assertEquals("VACANT", MultiApproveData.pendingApprovers(usrq("erp2", "add1", "VACANT")));
	}

	@Test
	public void pending_userInTwoSubGroups_isListedOnce() {
		List<SubGroup> rows = Arrays.asList(row("ERP", "erp1,both", ""), row("ADDON", "both", ""));
		assertEquals("erp1,both", MultiApproveData.pendingApprovers(rows));
	}

	@Test
	public void all_ignoresApprovalsButNotVacant() {
		assertEquals("erp1,erp2,add1", MultiApproveData.allApprovers(usrq("erp1", "add1", "VACANT")));
		assertEquals("VACANT",
				MultiApproveData.allApprovers(Arrays.asList(row("ERP", "VACANT", ""), row("ADDON", "VACANT", ""))));
	}

	@Test
	public void decide_noChildRows_continuesAsAnyOneApprover() {
		Outcome outcome = MultiApproveData.decide("10", new ArrayList<>(), "erp1");
		assertEquals(Outcome.Kind.CONTINUE, outcome.kind);
		assertTrue(outcome.rows.isEmpty());
	}

	@Test
	public void decide_firstOfThree_waits() {
		Outcome outcome = MultiApproveData.decide("10", usrq("", "", "oth1"), "erp2");
		assertEquals(Outcome.Kind.WAIT, outcome.kind);
		assertEquals(1, outcome.rows.size());
		assertEquals("ERP", outcome.rows.get(0).subGroup);
	}

	@Test
	public void decide_lastOpenSubGroup_continues() {
		Outcome outcome = MultiApproveData.decide("10", usrq("erp1", "", "oth1"), "OTH1");
		assertEquals(Outcome.Kind.WAIT, outcome.kind);

		outcome = MultiApproveData.decide("10", usrq("erp1", "", "VACANT"), "add1");
		assertEquals(Outcome.Kind.CONTINUE, outcome.kind);
		assertEquals("ADDON", outcome.rows.get(0).subGroup);
	}

	@Test
	public void decide_vacantSubGroupIsNotCounted() {
		// OTHER not chosen by the requester: ERP + ADDON are enough
		Outcome outcome = MultiApproveData.decide("10", usrq("", "", "VACANT"), "erp1");
		assertEquals(Outcome.Kind.WAIT, outcome.kind);
		outcome = MultiApproveData.decide("10", usrq("erp1", "", "VACANT"), "add1");
		assertEquals(Outcome.Kind.CONTINUE, outcome.kind);
	}

	@Test
	public void decide_userInEverySubGroup_approvesAllAtOnce() {
		List<SubGroup> rows = Arrays.asList(row("ERP", "erp1,both", ""), row("ADDON", "both", ""));
		Outcome outcome = MultiApproveData.decide("10", rows, "both");
		assertEquals(Outcome.Kind.CONTINUE, outcome.kind);
		assertEquals(2, outcome.rows.size());
	}

	@Test
	public void decide_twiceBySameUser_refuses() {
		Outcome outcome = MultiApproveData.decide("10", usrq("erp1", "", "oth1"), "erp1");
		assertEquals(Outcome.Kind.REFUSE, outcome.kind);
		assertEquals("You already approved status 10, waiting for: add1,oth1", outcome.message);
	}

	@Test
	public void decide_otherMemberOfApprovedSubGroup_refuses() {
		Outcome outcome = MultiApproveData.decide("10", usrq("erp1", "", "oth1"), "erp2");
		assertEquals(Outcome.Kind.REFUSE, outcome.kind);
		assertEquals("ERP already approved by erp1, waiting for: add1,oth1", outcome.message);
	}

	@Test
	public void decide_userInNoSubGroup_refuses() {
		Outcome outcome = MultiApproveData.decide("10", usrq("erp1", "", "oth1"), "nobody");
		assertEquals(Outcome.Kind.REFUSE, outcome.kind);
		assertEquals("You are not an approver of status 10, waiting for: add1,oth1", outcome.message);
	}

	@Test
	public void decide_nothingOpenAnyMore_continues() {
		// the last approval wrote its child row but the status did not move: it can be approved again
		Outcome outcome = MultiApproveData.decide("10", usrq("erp1", "add1", "VACANT"), "add1");
		assertEquals(Outcome.Kind.CONTINUE, outcome.kind);
		assertTrue(outcome.rows.isEmpty());
	}

	@Test
	public void decide_namesMatchWholeUsersOnly() {
		// "erp" is not "erp1" (FAAPLI LIKE '%erp%' would match)
		Outcome outcome = MultiApproveData.decide("10", usrq("", "", "oth1"), "erp");
		assertEquals(Outcome.Kind.REFUSE, outcome.kind);
	}

	@Test
	public void waitMessage_countsOpenSubGroups() {
		assertEquals("Approve complete, waiting for 2 more sub group(s): add1,oth1",
				MultiApproveData.waitMessage(usrq("erp1", "", "oth1")));
	}

	@Test
	public void length_upTo255_passes() throws Exception {
		String users = users("u", 51); // 51 names of 4 characters: 51 * 5 - 1 = 254
		MultiApproveData.checkLength(MultiApproveData.byStatus(Arrays.asList(row("ERP", users, ""))));
	}

	@Test
	public void length_over255_throws() {
		List<SubGroup> rows = Arrays.asList(row("ERP", users("e", 30)), row("ADDON", users("a", 30)));
		try {
			MultiApproveData.checkLength(MultiApproveData.byStatus(rows));
			fail("expected the length check to throw");
		} catch (Exception e) {
			assertTrue(e.getMessage(), e.getMessage().startsWith("Status 10 has too many approvers"));
		}
	}

	// count different names of 4 characters: prefix + 3 digits
	private static String users(String prefix, int count) {
		List<String> names = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			names.add(prefix + String.format("%03d", i));
		}
		return String.join(",", names);
	}

	private static SubGroup row(String subGroup, String users) {
		return row(subGroup, users, "");
	}

	@Test
	public void length_vacantSubGroupsDoNotCount() throws Exception {
		List<SubGroup> rows = Arrays.asList(row("ERP", "erp1"), row("ADDON", "VACANT"));
		MultiApproveData.checkLength(MultiApproveData.byStatus(rows));
		assertEquals("erp1", MultiApproveData.allApprovers(rows));
	}

	// ---------------------------------------------------------------------
	// Replies per line (mergeLineReplies)
	// ---------------------------------------------------------------------

	// a USRQ form with one line per application; reply = ictReply of the line, null = no reply
	private static String form(String reason, String erpReply, String addonReply) throws Exception {
		JSONArray lines = new JSONArray();
		lines.put(line("M3INFO", "appERP", erpReply));
		lines.put(line("AddOnM3", "appAddon", addonReply));
		return new JSONObject().put("reasonMain", reason).put("authorizeLines", lines).toString();
	}

	private static JSONObject line(String application, String flag, String reply) throws Exception {
		JSONObject line = new JSONObject().put("application", application).put("approvalFlag", flag);
		if (reply != null) {
			line.put("ictResult", "DONE").put("ictReply", reply);
		}
		return line;
	}

	private static JSONObject lineOf(String data, int index) throws Exception {
		return new JSONObject(data).getJSONArray("authorizeLines").getJSONObject(index);
	}

	private static Set<String> flags(String... flags) {
		return new HashSet<>(Arrays.asList(flags));
	}

	@Test
	public void merge_keepsTheRepliesOfTheOtherSubGroup() throws Exception {
		// ERP replied and approved first; ADDON still has the form as it was before
		String db = form("staff 1", "user created", null);
		String posted = form("staff 1", null, "program added");

		String merged = MultiApproveData.mergeLineReplies(db, posted, flags("appAddon"));

		assertEquals("user created", lineOf(merged, 0).getString("ictReply"));
		assertEquals("program added", lineOf(merged, 1).getString("ictReply"));
	}

	@Test
	public void merge_takesOnlyReplyKeysOfOwnLines() throws Exception {
		String db = form("staff 1", null, null);
		// the posted form tries to change the reason and the ERP line too
		String posted = form("changed", "not mine", "program added");

		String merged = MultiApproveData.mergeLineReplies(db, posted, flags("appAddon"));

		assertEquals("staff 1", new JSONObject(merged).getString("reasonMain"));
		assertTrue(!lineOf(merged, 0).has("ictReply"));
		assertEquals("program added", lineOf(merged, 1).getString("ictReply"));
	}

	@Test
	public void merge_userInTwoSubGroups_repliesBoth() throws Exception {
		String merged = MultiApproveData.mergeLineReplies(form("s", null, null), form("s", "a", "b"),
				flags("appERP", "appAddon"));

		assertEquals("a", lineOf(merged, 0).getString("ictReply"));
		assertEquals("b", lineOf(merged, 1).getString("ictReply"));
	}

	@Test
	public void merge_clearedReplyIsRemoved() throws Exception {
		String merged = MultiApproveData.mergeLineReplies(form("s", "old", null), form("s", null, null),
				flags("appERP"));

		assertTrue(!lineOf(merged, 0).has("ictReply"));
		assertTrue(!lineOf(merged, 0).has("ictResult"));
	}

	@Test
	public void merge_quoteInReplyIsKept() throws Exception {
		String merged = MultiApproveData.mergeLineReplies(form("s", null, null), form("s", "user's id", null),
				flags("appERP"));

		assertEquals("user's id", lineOf(merged, 0).getString("ictReply"));
	}

	@Test
	public void merge_noLines_keepsDbData() throws Exception {
		String db = new JSONObject().put("reasonMain", "s").toString();

		assertEquals(db, MultiApproveData.mergeLineReplies(db, form("s", "a", null), flags("appERP")));
	}

	@Test
	public void merge_createdUsernameIsKeptFromDb() throws Exception {
		String db = "{\"authorizeLines\":[{\"application\":\"AddOnM3\",\"approvalFlag\":\"appAddon\","
				+ "\"ictUsername\":\"WATTAN_SAE\",\"ictUserCreated\":\"Y\"}]}";
		String posted = "{\"authorizeLines\":[{\"application\":\"AddOnM3\",\"approvalFlag\":\"appAddon\","
				+ "\"ictUsername\":\"OTHER\",\"ictUserCreated\":\"N\",\"ictReply\":\"ok\"}]}";
		JSONObject merged = new JSONObject(
				MultiApproveData.mergeLineReplies(db, posted, Collections.singleton("appAddon")));
		JSONObject line = merged.getJSONArray("authorizeLines").getJSONObject(0);
		assertEquals("WATTAN_SAE", line.getString("ictUsername"));
		assertEquals("Y", line.getString("ictUserCreated"));
		assertEquals("ok", line.getString("ictReply"));
	}

	@Test
	public void merge_usernameNotCreatedYetComesFromTheForm() throws Exception {
		String db = "{\"authorizeLines\":[{\"application\":\"AddOnM3\",\"approvalFlag\":\"appAddon\"}]}";
		String posted = "{\"authorizeLines\":[{\"application\":\"AddOnM3\",\"approvalFlag\":\"appAddon\","
				+ "\"ictUsername\":\"WATTAN_SA1\"}]}";
		JSONObject merged = new JSONObject(
				MultiApproveData.mergeLineReplies(db, posted, Collections.singleton("appAddon")));
		assertEquals("WATTAN_SA1",
				merged.getJSONArray("authorizeLines").getJSONObject(0).getString("ictUsername"));
	}
}
