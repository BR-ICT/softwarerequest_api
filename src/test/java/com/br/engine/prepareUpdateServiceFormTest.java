package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.br.connection.ConnectDB2;

/**
 * SQL-quality strategy (syntax, table name, column order) for the individual
 * FlowUpdate methods that prepareUpdateServiceForm calls on approve/reject,
 * plus a version-guard smoke test for prepareUpdateServiceForm itself.
 * Each SQL-quality test calls its production method directly (not through
 * prepareUpdateServiceForm) and captures the single query that method
 * executes.
 *
 * Scope note: aside from the version guard and VACANT skipping (approve:
 * end of flow, middle skip, cycle; reject: own previous status, skip back,
 * cycle, VACANT start), prepareUpdateServiceForm's own orchestration logic
 * (REQUESTER/DEPTHEAD branching, reject '9' cancel, call order/count) is not
 * covered by this file or any other test.
 */
public class prepareUpdateServiceFormTest {

	private static final String CONO = "10";
	private static final String DIVI = "101";
	private static final String SERVICENAME = "SVRQ";
	private static final String REQUESTTYPE = "create";
	private static final String STATUS = "00";
	private static final String NEXT_STATUS = "S20";
	private static final String PREV_STATUS = "S00";
	private static final String VERSION = "1.0.0";
	private static final String SERVICE_NO = "26000001";
	private static final String APPROVE_BY = "approve01";
	private static final String DEPTHEAD = "dept01";
	private static final String REMARK = "test remark";

	private Statement mockStatement;
	private Connection mockConnection;
	private PreparedStatement mockPrepared;
	private MockedStatic<ConnectDB2> connectDB2;
	private MockedStatic<FlowQuery> selectData;
	private MockedStatic<Workflow> workflow;
	private MockedStatic<MultiApproveData> multiApprove;
	private MockedStatic<FormInterfaceCall> serviceInterface;

	@Before
	public void setUp() throws Exception {
		// No approve mode ALL status in these flows (MultiApproveDataTest covers it)
		MultiApproveData.Outcome notMulti = MultiApproveData.decide("00", new ArrayList<>(), "user");
		multiApprove = Mockito.mockStatic(MultiApproveData.class);
		multiApprove.when(() -> MultiApproveData.buildSubGroups(any(), any(), any(), any(), any()))
				.thenReturn(new ArrayList<>());
		multiApprove.when(() -> MultiApproveData.approve(any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(notMulti);
		// not mode ALL: the posted form data is saved as it is
		multiApprove.when(() -> MultiApproveData.mergeApproverReplies(any(), any(), any(), any(), any(), any(), any()))
				.thenAnswer(call -> call.getArgument(6));
		multiApprove.when(() -> MultiApproveData.mergeRejecterReplies(any(), any(), any(), any(), any(), any(), any(),
				any())).thenAnswer(call -> call.getArgument(7));
		// no status flagged Interface M3: the posted form data goes on as it is
		serviceInterface = Mockito.mockStatic(FormInterfaceCall.class);
		serviceInterface.when(() -> FormInterfaceCall.beforeApprove(any(), any(), any(), any(), any(), any(), any(),
				any(), any(), any())).thenAnswer(call -> new FormInterface.Answer(call.getArgument(9), ""));

		mockConnection = mock(Connection.class);
		mockStatement = mock(Statement.class);
		mockPrepared = mock(PreparedStatement.class);
		connectDB2 = Mockito.mockStatic(ConnectDB2.class);
		connectDB2.when(ConnectDB2::doConnect).thenReturn(mockConnection);
		when(mockConnection.createStatement()).thenReturn(mockStatement);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPrepared);

		// Only the prepareUpdateServiceForm version-guard smoke test needs these.
		selectData = Mockito.mockStatic(FlowQuery.class);
		// the request is at the status of the call and waits for approve (ApproveGuard)
		selectData.when(() -> FlowQuery.getHeadStatus(any(), any(), any(), any()))
				.thenAnswer(call -> new String[] { headAt, "2" });
		// the workflow (n8n) email is never sent from a test
		workflow = Mockito.mockStatic(Workflow.class);
	}

	@After
	public void tearDown() {
		connectDB2.close();
		selectData.close();
		workflow.close();
		multiApprove.close();
		serviceInterface.close();
	}

	// FHDSTA of the request (FlowQuery.getHeadStatus)
	private String headAt = STATUS;

	private String buildData(String version, boolean approval) {
		return "{\"vApproval\":\"" + approval + "\""
				+ ",\"vRequester\":\"unused01\""
				+ ",\"vDepthead\":\"" + DEPTHEAD + "\""
				+ ",\"vApproveBy\":\"" + APPROVE_BY + "\""
				+ ",\"vRemark\":\"" + REMARK + "\""
				+ formFields
				+ ",\"vVersion\":\"" + version + "\"}";
	}

	// extra form answers of the approve / reject call, e.g. ",\"creditLimit3Main\":\"600000.00\""
	private String formFields = "";

	// exact match, not contains(): catches a typo like "SR_FLOWHEADs"
	private static void assertTargetsTable(String sql, String expectedTable) {
		Matcher tableMatcher = Pattern.compile("UPDATE\\s+\\S+\\.(\\S+)").matcher(sql);
		assertTrue("UPDATE clause not found in query", tableMatcher.find());
		assertEquals(expectedTable, tableMatcher.group(1));
	}

	private String captureExecutedQuery() throws Exception {
		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockStatement, times(1)).execute(sqlCaptor.capture());
		return sqlCaptor.getValue();
	}

	// -----------------------------------------------------------------------
	// prepareUpdateServiceForm — version guard (FlowQuery.checkVersion)
	// -----------------------------------------------------------------------

	@Test
	public void testprepareUpdateServiceForm_versionMismatch_returnsNok() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);

		String result = ServiceFormEngine.prepareUpdateServiceForm(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS,
				buildData("0.0.1", true), null, null, null, "", "", APPROVE_BY);

		JSONObject json = new JSONObject(result);
		assertEquals("nok", json.getString("result"));
		// Guard returns before any query-building method opens a connection.
		connectDB2.verifyNoInteractions();
	}

	@Test
	public void testprepareUpdateServiceForm_versionMatch_returnsOk() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		// nextApprover != VACANT (loop runs once) and nextGroup != DEPTHEAD (no
		// re-update block) keep this smoke test to the minimal query set — the
		// branch-specific SQL is already covered by the direct-call tests below.
		selectData.when(() -> FlowQuery.getStatusFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS))
				.thenReturn(APPROVE_BY + " ; " + STATUS + " ; " + NEXT_STATUS + " ; mgr01 ; MANAGER ; " + PREV_STATUS
						+ " ; prevApprover01");
		selectData.when(() -> FlowQuery.checkApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS)).thenReturn("");

		String result = ServiceFormEngine.prepareUpdateServiceForm(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS,
				buildData(VERSION, true), null, null, null, "", "", APPROVE_BY);

		JSONObject json = new JSONObject(result);
		assertEquals("ok", json.getString("result"));
	}

	@Test
	public void testprepareUpdateServiceForm_interfaceReference_writtenToFhdes2() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		selectData.when(() -> FlowQuery.getStatusFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS))
				.thenReturn(APPROVE_BY + " ; " + STATUS + " ; " + NEXT_STATUS + " ; mgr01 ; MANAGER ; " + PREV_STATUS
						+ " ; prevApprover01");
		selectData.when(() -> FlowQuery.checkApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS)).thenReturn("");
		serviceInterface.when(() -> FormInterfaceCall.beforeApprove(any(), any(), any(), any(), any(), any(), any(),
				any(), any(), any())).thenAnswer(call -> new FormInterface.Answer(call.getArgument(9), "ITEM-0001"));

		String result = ServiceFormEngine.prepareUpdateServiceForm(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS,
				buildData(VERSION, true), null, null, null, "", "", APPROVE_BY);

		assertEquals("ok", new JSONObject(result).getString("result"));
		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockConnection, Mockito.atLeastOnce()).prepareStatement(sqlCaptor.capture());
		assertTrue(sqlCaptor.getAllValues().stream().anyMatch(sql -> sql.contains("SET FHDES2 = ?")));
		verify(mockPrepared).setString(1, "ITEM-0001");
	}

	@Test
	public void testprepareUpdateServiceForm_interfaceRefuses_nothingWritten() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		selectData.when(() -> FlowQuery.getStatusFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS))
				.thenReturn(APPROVE_BY + " ; " + STATUS + " ; " + NEXT_STATUS + " ; mgr01 ; MANAGER ; " + PREV_STATUS
						+ " ; prevApprover01");
		selectData.when(() -> FlowQuery.checkApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS)).thenReturn("");
		serviceInterface.when(() -> FormInterfaceCall.beforeApprove(any(), any(), any(), any(), any(), any(), any(),
				any(), any(), any())).thenThrow(new Exception("MMS001 error"));

		String result = ServiceFormEngine.prepareUpdateServiceForm(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS,
				buildData(VERSION, true), null, null, null, "", "", APPROVE_BY);

		JSONObject json = new JSONObject(result);
		assertEquals("nok", json.getString("result"));
		assertEquals("MMS001 error", json.getString("message"));
		verify(mockStatement, Mockito.never()).execute(anyString());
		verify(mockPrepared, Mockito.never()).executeUpdate();
	}

	@Test
	public void testApproveAgain_refusedBeforeTheInterface() throws Exception {
		// double click / email link opened again: the interface (M3) must not run a second time
		stubFlow("60", "60", "it01", "50");
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		selectData.when(() -> FlowQuery.checkApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, "60"))
				.thenReturn(APPROVE_BY);
		headAt = "60";

		JSONObject json = new JSONObject(ServiceFormEngine.prepareUpdateServiceForm(CONO, DIVI, SERVICENAME, SERVICE_NO,
				REQUESTTYPE, "60", buildData(VERSION, true), null, null, null, "", "", APPROVE_BY));

		assertEquals("nok", json.getString("result"));
		assertEquals("Can't approve, Service approve already by " + APPROVE_BY, json.getString("message"));
		serviceInterface.verify(() -> FormInterfaceCall.beforeApprove(any(), any(), any(), any(), any(), any(),
				any(), any(), any(), any()), Mockito.never());
		verify(mockStatement, Mockito.never()).execute(anyString());
	}

	@Test
	public void testApproveAsAnotherUser_refusedWithoutWriting() throws Exception {
		stubFlow("10", "20", "mgr01", "00");

		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		selectData.when(() -> FlowQuery.checkApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, "10")).thenReturn("");
		headAt = "10";
		JSONObject json = new JSONObject(ServiceFormEngine.prepareUpdateServiceForm(CONO, DIVI, SERVICENAME, SERVICE_NO,
				REQUESTTYPE, "10", buildData(VERSION, true), null, null, null, "", "", "user99"));

		assertEquals("nok", json.getString("result"));
		assertTrue(json.getString("message").contains("you are user99"));
		verify(mockStatement, Mockito.never()).execute(anyString());
	}

	// -----------------------------------------------------------------------
	// prepareUpdateServiceForm — VACANT skip loop
	// getStatusFlowApprove: "approver ; status ; next ; nextApprover ; nextGroup ; prev ; prevApprover"
	// -----------------------------------------------------------------------

	// Fails the test instead of hanging the build when the skip loop never stops.
	// (@Test(timeout) can't be used: it runs the test on another thread, and MockedStatic is per thread.)
	private int flowCalls;

	private void stubFlow(String status, String next, String nextApprover, String prev) {
		stubFlow(status, next, nextApprover, prev, "prevApprover01");
	}

	private void stubFlow(String status, String next, String nextApprover, String prev, String prevApprover) {
		selectData.when(() -> FlowQuery.getStatusFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, status))
				.thenAnswer(invocation -> {
					if (++flowCalls > 20) {
						throw new AssertionError("VACANT skip loop did not stop");
					}
					return APPROVE_BY + " ; " + status + " ; " + next + " ; " + nextApprover + " ; FINANCE ; " + prev
							+ " ; " + prevApprover;
				});
	}

	private String approveAt(String status) throws Exception {
		return submitAt(status, true);
	}

	private String rejectAt(String status) throws Exception {
		return submitAt(status, false);
	}

	private String submitAt(String status, boolean approval) throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		selectData.when(() -> FlowQuery.checkApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, status)).thenReturn("");
		headAt = status;
		return ServiceFormEngine.prepareUpdateServiceForm(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, status,
				buildData(VERSION, approval), null, null, null, "", "", APPROVE_BY);
	}

	// FHHSTA values written by updateServiceHeadStatus
	private java.util.List<String> headStatuses() throws Exception {
		return writtenValues("FHHSTA");
	}

	// The status (WHERE FHDSTA) of the head row each updateServiceHeadStatus wrote FHHSTA to
	private java.util.List<String> headStatusRows() throws Exception {
		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockStatement, Mockito.atLeast(0)).execute(sqlCaptor.capture());
		java.util.List<String> rows = new java.util.ArrayList<>();
		for (String sql : sqlCaptor.getAllValues()) {
			if (SqlSyntaxAssertions.updateSetValues(sql).containsKey("FHHSTA")) {
				rows.add(SqlSyntaxAssertions.updateWhereConditions(sql).get("FHDSTA"));
			}
		}
		return rows;
	}

	// Values the executed UPDATEs SET for one column
	private java.util.List<String> writtenValues(String column) throws Exception {
		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockStatement, Mockito.atLeast(0)).execute(sqlCaptor.capture());
		java.util.List<String> values = new java.util.ArrayList<>();
		for (String sql : sqlCaptor.getAllValues()) {
			String value = SqlSyntaxAssertions.updateSetValues(sql).get(column);
			if (value != null) {
				values.add(value);
			}
		}
		return values;
	}

	@Test
	public void testReject_nextStepVacant_goesBackToOwnPreviousStatus() throws Exception {
		// 00 (req01) <- 10 (rejecting here) -> 20 VACANT: must not walk forward to 20 and read 20's previous (10)
		stubFlow("10", "20", "VACANT", "00", "req01");
		stubFlow("20", "30", "mgr01", "10", "approver01");

		JSONObject json = new JSONObject(rejectAt("10"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'00'"), writtenValues("FHDSTA")); // updateServiceHead
		assertEquals(java.util.Collections.singletonList("'1'"), headStatuses()); // 1 = Wait for submit
	}

	@Test
	public void testReject_previousStepVacant_skipsBackToApprover() throws Exception {
		// 10 (approver01) <- 20 VACANT <- 30 (rejecting here)
		stubFlow("30", "40", "mgr01", "20", "VACANT");
		stubFlow("20", "30", "mgr01", "10", "approver01");

		JSONObject json = new JSONObject(rejectAt("30"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'10'"), writtenValues("FHDSTA"));
		assertEquals(java.util.Collections.singletonList("'1'"), headStatuses());
	}

	@Test
	public void testReject_vacantPreviousCycle_returnsNokWithoutWriting() throws Exception {
		// 30 -> prev 20 VACANT -> prev 25 VACANT -> prev 20 ...
		stubFlow("30", "40", "mgr01", "20", "VACANT");
		stubFlow("20", "30", "mgr01", "25", "VACANT");
		stubFlow("25", "30", "mgr01", "20", "VACANT");

		JSONObject json = new JSONObject(rejectAt("30"));

		assertEquals("nok", json.getString("result"));
		assertTrue(json.getString("message").contains("loops through VACANT status 20"));
		Mockito.verify(mockStatement, Mockito.never()).execute(any());
	}

	@Test
	public void testReject_vacantStartOfFlow_returnsNokWithoutWriting() throws Exception {
		// 30 -> prev 00 VACANT, and 00 is the start (Previous Status = itself)
		stubFlow("30", "40", "mgr01", "00", "VACANT");
		stubFlow("00", "30", "mgr01", "00", "VACANT");

		JSONObject json = new JSONObject(rejectAt("30"));

		assertEquals("nok", json.getString("result"));
		assertTrue(json.getString("message").contains("starts with status 00"));
		Mockito.verify(mockStatement, Mockito.never()).execute(any());
	}

	@Test
	public void testVacantLastStep_pointingToItself_completesInsteadOfLooping() throws Exception {
		// 10 -> 20, and 20 is the end of the flow (Next Status = itself) with no approver
		stubFlow("10", "20", "VACANT", "00");
		stubFlow("20", "20", "VACANT", "10");

		JSONObject json = new JSONObject(approveAt("10"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'3'"), headStatuses()); // 3 = Approve Complete
		// written to the row the head moved to (was 10: no row matched, the request never completed)
		assertEquals(java.util.Collections.singletonList("'20'"), headStatusRows());
	}

	@Test
	public void testApprove_middleOfFlow_waitForApproveOnTheNewStatus() throws Exception {
		// 10 -> 20 (mgr01), 10's previous is 00
		stubFlow("10", "20", "mgr01", "00");

		JSONObject json = new JSONObject(approveAt("10"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'2'"), headStatuses());
		// was 00 (the previous status): no row matched, a resubmitted request stayed 1 = Wait for submit
		assertEquals(java.util.Collections.singletonList("'20'"), headStatusRows());
	}

	@Test
	public void testApprove_lastStatus_completesOnItsOwnRow() throws Exception {
		stubFlow("60", "60", "it01", "50");

		JSONObject json = new JSONObject(approveAt("60"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'3'"), headStatuses());
		assertEquals(java.util.Collections.singletonList("'60'"), headStatusRows());
	}

	@Test
	public void testReject_waitForSubmitOnThePreviousStatus() throws Exception {
		stubFlow("20", "30", "mgr01", "10", "approver01");

		JSONObject json = new JSONObject(rejectAt("20"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'1'"), headStatuses());
		assertEquals(java.util.Collections.singletonList("'10'"), headStatusRows());
	}

	@Test
	public void testVacantMiddleStep_isSkippedToNextApprover() throws Exception {
		// 10 -> 20 (VACANT, skipped) -> 30 (mgr01)
		stubFlow("10", "20", "VACANT", "00");
		stubFlow("20", "30", "mgr01", "10");

		JSONObject json = new JSONObject(approveAt("10"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'2'"), headStatuses()); // 2 = Wait for approve
		assertEquals(java.util.Collections.singletonList("'30'"), headStatusRows());
	}

	@Test
	public void testVacantCycle_returnsNokWithoutWriting() throws Exception {
		// 10 -> 20 -> 30 -> 20 ... all VACANT
		stubFlow("10", "20", "VACANT", "00");
		stubFlow("20", "30", "VACANT", "10");
		stubFlow("30", "20", "VACANT", "20");

		JSONObject json = new JSONObject(approveAt("10"));

		assertEquals("nok", json.getString("result"));
		assertTrue(json.getString("message").contains("loops through VACANT status 20"));
		Mockito.verify(mockStatement, Mockito.never()).execute(any());
	}

	// -----------------------------------------------------------------------
	// prepareUpdateServiceForm — condition skip (SR_PROCESS PMCFLD / PMCOPR / PMCVAL)
	// getStatusFlowApprove appends: " ; m3 ; nextField ; nextOperator ; nextValue ; prevField ; prevOperator ; prevValue"
	// -----------------------------------------------------------------------

	private static final String NO_CONDITION = " ;  ;  ; ";
	private static final String CREDIT_LIMIT = " ; creditLimit3Main ; >= ; 500001";

	private void stubFlow(String status, String next, String nextApprover, String prev, String prevApprover,
			String nextCondition, String previousCondition) {
		selectData.when(() -> FlowQuery.getStatusFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, status))
				.thenAnswer(invocation -> {
					if (++flowCalls > 20) {
						throw new AssertionError("skip loop did not stop");
					}
					return APPROVE_BY + " ; " + status + " ; " + next + " ; " + nextApprover + " ; FINANCE ; " + prev
							+ " ; " + prevApprover + " ; 1" + nextCondition + previousCondition;
				});
	}

	@Test
	public void testCondition_met_nextStatusNeedsApproval() throws Exception {
		// 50 -> 60 (credit limit >= 500001) -> 70
		formFields = ",\"creditLimit3Main\":\"600000.00\"";
		stubFlow("50", "60", "fin01", "40", "prev01", CREDIT_LIMIT, NO_CONDITION);

		JSONObject json = new JSONObject(approveAt("50"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'60'"), writtenValues("FHDSTA"));
		assertEquals(java.util.Collections.singletonList("'2'"), headStatuses());
	}

	@Test
	public void testCondition_notMet_nextStatusIsSkipped() throws Exception {
		formFields = ",\"creditLimit3Main\":500000";
		stubFlow("50", "60", "fin01", "40", "prev01", CREDIT_LIMIT, NO_CONDITION);
		stubFlow("60", "70", "mgr01", "50", "approver01", NO_CONDITION, NO_CONDITION);

		JSONObject json = new JSONObject(approveAt("50"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'70'"), writtenValues("FHDSTA"));
		assertEquals(java.util.Collections.singletonList("'2'"), headStatuses());
	}

	@Test
	public void testCondition_notMetOnLastStatus_completes() throws Exception {
		// 60 is the end of the flow (Next Status = itself): the request completes without its approver
		formFields = ",\"creditLimit3Main\":\"100\"";
		stubFlow("50", "60", "fin01", "40", "prev01", CREDIT_LIMIT, NO_CONDITION);
		stubFlow("60", "60", "fin01", "50", "approver01", CREDIT_LIMIT, NO_CONDITION);

		JSONObject json = new JSONObject(approveAt("50"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'3'"), headStatuses());
	}

	@Test
	public void testCondition_variableMissing_returnsNokWithoutWriting() throws Exception {
		stubFlow("50", "60", "fin01", "40", "prev01", CREDIT_LIMIT, NO_CONDITION);

		JSONObject json = new JSONObject(approveAt("50"));

		assertEquals("nok", json.getString("result"));
		assertEquals("Condition of status 60 (creditLimit3Main >= 500001) cannot be checked: "
				+ "creditLimit3Main is missing or empty in the form.", json.getString("message"));
		Mockito.verify(mockStatement, Mockito.never()).execute(any());
	}

	@Test
	public void testCondition_notANumber_returnsNokWithoutWriting() throws Exception {
		formFields = ",\"creditLimit3Main\":\"N/A\"";
		stubFlow("50", "60", "fin01", "40", "prev01", CREDIT_LIMIT, NO_CONDITION);

		JSONObject json = new JSONObject(approveAt("50"));

		assertEquals("nok", json.getString("result"));
		assertTrue(json.getString("message").contains("creditLimit3Main = \"N/A\" is not a number"));
		Mockito.verify(mockStatement, Mockito.never()).execute(any());
	}

	@Test
	public void testReject_previousConditionNotMet_skipsBackToApprover() throws Exception {
		// 50 (approver) <- 60 (credit limit, not met: was skipped on the way up) <- 70 (rejecting here)
		formFields = ",\"creditLimit3Main\":\"500000\"";
		stubFlow("70", "80", "mgr01", "60", "fin01", NO_CONDITION, CREDIT_LIMIT);
		stubFlow("60", "70", "mgr01", "50", "approver01", NO_CONDITION, NO_CONDITION);

		JSONObject json = new JSONObject(rejectAt("70"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'50'"), writtenValues("FHDSTA"));
	}

	@Test
	public void testReject_previousConditionMet_goesBackToThatStatus() throws Exception {
		formFields = ",\"creditLimit3Main\":\"500001\"";
		stubFlow("70", "80", "mgr01", "60", "fin01", NO_CONDITION, CREDIT_LIMIT);

		JSONObject json = new JSONObject(rejectAt("70"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'60'"), writtenValues("FHDSTA"));
	}

	@Test
	public void testReject_nextConditionIsNotChecked() throws Exception {
		// rejecting at 50: the condition of 60 does not matter, even when its variable is missing
		stubFlow("50", "60", "fin01", "40", "prev01", CREDIT_LIMIT, NO_CONDITION);

		JSONObject json = new JSONObject(rejectAt("50"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Collections.singletonList("'40'"), writtenValues("FHDSTA"));
	}

	@Test
	public void testConditionHolds_comparesNumbersAndText() throws Exception {
		JSONObject form = new JSONObject("{\"amount\":\"500000.00\",\"type\":\"Export\",\"qty\":7}");

		assertTrue(ServiceFormEngine.conditionHolds(form, "60", "", "", "")); // no condition
		assertTrue(ServiceFormEngine.conditionHolds(form, "60", "amount", "=", "500000")); // 500000.00 = 500000
		assertTrue(ServiceFormEngine.conditionHolds(form, "60", "amount", "<", "500001"));
		assertTrue(!ServiceFormEngine.conditionHolds(form, "60", "amount", ">=", "500001"));
		assertTrue(ServiceFormEngine.conditionHolds(form, "60", "qty", ">", "6.5"));
		assertTrue(ServiceFormEngine.conditionHolds(form, "60", "type", "=", "Export"));
		assertTrue(ServiceFormEngine.conditionHolds(form, "60", "type", "<>", "Local"));
		assertTrue(!ServiceFormEngine.conditionHolds(form, "60", "type", "=", "Local"));
	}

	// -----------------------------------------------------------------------
	// Approver names of System / Selector groups — form key = SR_GROUP.GMDES1
	// getFlowApproveNamedGroups: { status, group, current FAAPLI, form key }
	// -----------------------------------------------------------------------

	private void stubNamedGroups(String afterStatus, String[]... rows) {
		selectData.when(() -> FlowQuery.getFlowApproveNamedGroups(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE,
				afterStatus)).thenReturn(java.util.Arrays.asList(rows));
	}

	@Test
	public void testApproverName_readsFormKeyOfTheGroup() throws Exception {
		JSONObject form = new JSONObject("{\"vDepthead\":\" dept01 \",\"vApprover\":\"\",\"vSelector\":null}");

		assertEquals("dept01", ServiceFormEngine.approverName(form, "vDepthead"));
		assertEquals("dept01", ServiceFormEngine.approverName(form, "VDEPTHEAD")); // any letter case
		assertEquals("", ServiceFormEngine.approverName(form, "vApprover")); // not picked
		assertEquals("", ServiceFormEngine.approverName(form, "vSelector"));
		assertEquals("", ServiceFormEngine.approverName(form, "vSalesdirector")); // no such key
	}

	@Test
	public void testSyncApproverNames_readsTheKeyOfGmdes1_notTheGroupName() throws Exception {
		// SALES keeps its name in vSalesdirector, which is not "v" + group
		stubNamedGroups("", new String[] { "20", "SALES", "VACANT", "vSalesdirector" });
		JSONObject form = new JSONObject("{\"vSalesdirector\":\"sales01\",\"vSales\":\"wrong01\"}");

		ServiceFormEngine.syncApproverNames(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, "", form, "req01");

		assertEquals(java.util.Collections.singletonList("'sales01'"), writtenValues("FAAPLI"));
	}

	@Test
	public void testSyncApproverNames_groupWithoutFormKey_throws() throws Exception {
		stubNamedGroups("", new String[] { "10", "DEPTHEAD", "VACANT", "" });
		JSONObject form = new JSONObject("{\"vDepthead\":\"" + DEPTHEAD + "\"}");

		try {
			ServiceFormEngine.syncApproverNames(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, "", form, "req01");
			org.junit.Assert.fail("expected an exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("GMDES1"));
		}
		assertTrue(writtenValues("FAAPLI").isEmpty());
	}

	@Test
	public void testResubmitFromRequester_writesNamesOfLaterStatuses() throws Exception {
		// requester submits again from 00 after a reject:
		// 10 DEPTHEAD gets its name, 20 APPROVER was removed by the requester (back to VACANT),
		// 30 SELECTOR is unchanged, 40 SALES was never picked (already VACANT, nothing to write)
		formFields = ",\"vApprover\":\"\",\"vSelector\":\"sel01\"";
		stubNamedGroups("00", new String[] { "10", "DEPTHEAD", "VACANT", "vDepthead" },
				new String[] { "20", "APPROVER", "old01", "vApprover" },
				new String[] { "30", "SELECTOR", "sel01", "vSelector" },
				new String[] { "40", "SALES", "VACANT", "vSalesdirector" });
		stubFlow("00", "10", DEPTHEAD, "00");

		JSONObject json = new JSONObject(approveAt("00"));

		assertEquals("ok", json.getString("result"));
		assertEquals(java.util.Arrays.asList("'" + DEPTHEAD + "'", "'VACANT'"), writtenValues("FAAPLI"));
	}

	@Test
	public void testApprove_afterTheRequester_doesNotWriteNames() throws Exception {
		// only the requester picks approvers: an approver at 10 cannot change the names of later statuses
		selectData.when(() -> FlowQuery.getFlowApproveNamedGroups(any(), any(), any(), any(), any(), any()))
				.thenReturn(java.util.Collections.singletonList(new String[] { "20", "DEPTHEAD", "VACANT", "vDepthead" }));
		stubFlow("10", "20", "mgr01", "00");

		JSONObject json = new JSONObject(approveAt("10"));

		assertEquals("ok", json.getString("result"));
		assertTrue(writtenValues("FAAPLI").isEmpty());
	}

	@Test
	public void testReject_doesNotWriteNames() throws Exception {
		selectData.when(() -> FlowQuery.getFlowApproveNamedGroups(any(), any(), any(), any(), any(), any()))
				.thenReturn(java.util.Collections.singletonList(new String[] { "20", "DEPTHEAD", "VACANT", "vDepthead" }));
		stubFlow("00", "10", "mgr01", "00", "req01");

		JSONObject json = new JSONObject(rejectAt("00"));

		assertEquals("ok", json.getString("result"));
		assertTrue(writtenValues("FAAPLI").isEmpty());
	}

	@Test
	public void testSyncApproverNames_onCreate_writesEveryNamedStatus() throws Exception {
		// create: no afterStatus, so REQUESTER at 00 is written too
		stubNamedGroups("", new String[] { "00", "REQUESTER", "VACANT", "vRequester" },
				new String[] { "10", "DEPTHEAD", "VACANT", "vDepthead" });
		JSONObject form = new JSONObject("{\"vRequester\":\"req01\",\"vDepthead\":\"" + DEPTHEAD + "\"}");

		ServiceFormEngine.syncApproverNames(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, "", form, "req01");

		assertEquals(java.util.Arrays.asList("'req01'", "'" + DEPTHEAD + "'"), writtenValues("FAAPLI"));
	}

	// -----------------------------------------------------------------------
	// updateServiceUserHead — UPDATE SR_FLOWHEAD (DEPTHEAD re-update)
	// -----------------------------------------------------------------------

	@Test
	public void testUpdateServiceUserHead_query_isValid() throws Exception {
		FlowUpdate.updateServiceUserHead(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS, APPROVE_BY, DEPTHEAD);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWHEAD");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + DEPTHEAD + "'", setValues.get("FHDEPH"));
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FHENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FHCONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FHDIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FHCODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FHSRNO"));
		assertEquals("'" + STATUS + "'", whereValues.get("FHDSTA"));
	}

	// -----------------------------------------------------------------------
	// updateServiceUserFlowApprove — UPDATE SR_FLOWAPPROVE (DEPTHEAD re-update)
	// -----------------------------------------------------------------------

	@Test
	public void testupdateServiceUserFlowApprove_query_isValid() throws Exception {
		FlowUpdate.updateServiceUserFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, NEXT_STATUS, APPROVE_BY, DEPTHEAD);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + DEPTHEAD + "'", setValues.get("FAAPLI"));
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FAENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FACONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FADIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FACODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FASRNO"));
		assertEquals("'" + NEXT_STATUS + "'", whereValues.get("FASTAT"));
	}

	// -----------------------------------------------------------------------
	// updateServiceHead — UPDATE SR_FLOWHEAD
	// -----------------------------------------------------------------------

	@Test
	public void testUpdateServiceHead_query_isValid() throws Exception {
		FlowUpdate.updateServiceHead(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS, NEXT_STATUS, APPROVE_BY, DEPTHEAD);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWHEAD");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + DEPTHEAD + "'", setValues.get("FHDEPH"));
		assertEquals("'" + NEXT_STATUS + "'", setValues.get("FHDSTA"));
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FHENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FHCONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FHDIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FHCODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FHSRNO"));
		assertEquals("'" + STATUS + "'", whereValues.get("FHDSTA"));
	}

	// -----------------------------------------------------------------------
	// updateServiceDetail — UPDATE SR_FLOWDETAIL
	// -----------------------------------------------------------------------

	@Test
	public void testUpdateServiceDetail_query_isValid() throws Exception {
		// a ' in the form data must reach the DB as a value, not break the SQL
		String data = "{\"ictReply\":\"user's password reset\"}";
		FlowUpdate.updateServiceDetail(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS, NEXT_STATUS, data, APPROVE_BY);

		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockConnection).prepareStatement(sqlCaptor.capture());
		String sql = sqlCaptor.getValue();
		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWDETAIL");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("?", setValues.get("FDDATA"));
		assertEquals("?", setValues.get("FDDSTA"));
		assertEquals("?", setValues.get("FDENUS"));
		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("?", whereValues.get("FDSRNO"));

		verify(mockPrepared).setString(1, data);
		verify(mockPrepared).setString(2, NEXT_STATUS);
		verify(mockPrepared).setString(3, APPROVE_BY);
		verify(mockPrepared).setString(4, CONO);
		verify(mockPrepared).setString(5, DIVI);
		verify(mockPrepared).setString(6, SERVICENAME);
		verify(mockPrepared).setString(7, SERVICE_NO);
		verify(mockPrepared).executeUpdate();
	}

	// -----------------------------------------------------------------------
	// updateServiceFlowApproveBy — UPDATE SR_FLOWAPPROVE
	// -----------------------------------------------------------------------

	@Test
	public void testupdateServiceFlowApproveBy_query_isValid() throws Exception {
		FlowUpdate.updateServiceFlowApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS, REMARK, APPROVE_BY);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FAAPBY"));
		assertEquals("'" + REMARK + "'", setValues.get("FADES3"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FACONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FADIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FACODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FASRNO"));
		assertEquals("'" + STATUS + "'", whereValues.get("FASTAT"));
	}

	// -----------------------------------------------------------------------
	// updateServiceHeadStatus (approve branch) — UPDATE SR_FLOWHEAD
	// -----------------------------------------------------------------------

	@Test
	public void testUpdateServiceHeadStatus_approveBranch_query_isValid() throws Exception {
		// The orchestrator resolves tostatus to "2 = Wait for approve" when
		// setStatus != nextStatus, and reads the previous status as fromstatus.
		FlowUpdate.updateServiceHeadStatus(CONO, DIVI, SERVICENAME, SERVICE_NO, PREV_STATUS, "2", APPROVE_BY);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWHEAD");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'2'", setValues.get("FHHSTA"));
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FHENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FHCONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FHDIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FHCODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FHSRNO"));
		assertEquals("'" + PREV_STATUS + "'", whereValues.get("FHDSTA"));
	}

	// -----------------------------------------------------------------------
	// updateServiceFlowReject — UPDATE SR_FLOWAPPROVE
	// -----------------------------------------------------------------------

	@Test
	public void testUpdateServiceFlowReject_query_isValid() throws Exception {
		FlowUpdate.updateServiceFlowReject(CONO, DIVI, SERVICENAME, SERVICE_NO, PREV_STATUS, STATUS, REMARK,
				APPROVE_BY);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("''", setValues.get("FAAPBY"));
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FAENUS"));

		// Reject only resets the steps from the previous status up to the
		// current one, not the whole service record.
		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FACONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FADIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FACODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FASRNO"));
		assertTrue(sql.contains("INT(FASTAT) BETWEEN INT('" + PREV_STATUS + "') AND INT('" + STATUS + "')"));
	}

	// -----------------------------------------------------------------------
	// updateServiceFlowRejectBy — UPDATE SR_FLOWAPPROVE
	// -----------------------------------------------------------------------

	@Test
	public void testUpdateServiceFlowRejectBy_query_isValid() throws Exception {
		FlowUpdate.updateServiceFlowRejectBy(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS, REMARK, APPROVE_BY);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FARJBY"));
		assertEquals("'" + REMARK + "'", setValues.get("FADES4"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FACONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FADIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FACODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FASRNO"));
		assertEquals("'" + STATUS + "'", whereValues.get("FASTAT"));
	}

	// -----------------------------------------------------------------------
	// updateServiceHeadStatus (reject branch) — UPDATE SR_FLOWHEAD
	// -----------------------------------------------------------------------

	@Test
	public void testUpdateServiceHeadStatus_rejectBranch_query_isValid() throws Exception {
		// The orchestrator resolves tostatus to "1 = Wait for submit" on
		// reject, and reads the previous status as fromstatus.
		FlowUpdate.updateServiceHeadStatus(CONO, DIVI, SERVICENAME, SERVICE_NO, PREV_STATUS, "1", APPROVE_BY);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWHEAD");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'1'", setValues.get("FHHSTA"));
		assertEquals("'" + APPROVE_BY + "'", setValues.get("FHENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FHCONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FHDIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FHCODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FHSRNO"));
		assertEquals("'" + PREV_STATUS + "'", whereValues.get("FHDSTA"));
	}
}
