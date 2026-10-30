package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
 * FlowInsert/FlowUpdate methods that prepareAddServiceForm calls on submit,
 * plus a version-guard smoke test for prepareAddServiceForm itself.
 * Each SQL-quality test calls its production method directly (not through
 * prepareAddServiceForm) and captures the single query that method executes.
 *
 * Scope note: aside from the version guard, prepareAddServiceForm's own
 * orchestration logic (REQUESTER/DEPTHEAD branching, title formatting, call
 * order/count) is not covered by this file or any other test.
 */
public class prepareAddServiceFormTest {

	private static final String CONO = "10";
	private static final String DIVI = "101";
	private static final String SERVICENAME = "SVRQ";
	private static final String REQUESTTYPE = "create";
	private static final String STATUS = "00";
	private static final String NEXT_STATUS = "10";
	private static final String VERSION = "1.0.0";
	private static final String SERVICE_NO = "26000001";
	private static final String REQUESTER = "user01";
	private static final String DEPTHEAD = "dept01";
	private static final String REQUESTDATE = "2026-01-15";
	private static final String TITLE = "Fix laptop";

	private Statement mockStatement;
	private PreparedStatement mockPrepared;
	private MockedStatic<ConnectDB2> connectDB2;
	private MockedStatic<FlowQuery> selectData;
	private MockedStatic<Workflow> workflow;
	private MockedStatic<MultiApproveData> multiApprove;
	private MockedStatic<FormInterfaceCall> serviceInterface;

	@Before
	public void setUp() throws Exception {
		// start status not flagged Interface M3: the posted form data goes on as it is
		serviceInterface = Mockito.mockStatic(FormInterfaceCall.class);
		serviceInterface.when(() -> FormInterfaceCall.beforeSubmit(any(), any(), any(), any(), any(), any(), any(),
				any())).thenAnswer(call -> new FormInterface.Answer(call.getArgument(7), ""));

		// No approve mode ALL status in these flows (MultiApproveDataTest covers it)
		MultiApproveData.Outcome notMulti = MultiApproveData.decide("00", new ArrayList<>(), "user");
		multiApprove = Mockito.mockStatic(MultiApproveData.class);
		multiApprove.when(() -> MultiApproveData.buildSubGroups(any(), any(), any(), any(), any()))
				.thenReturn(new ArrayList<>());
		multiApprove.when(() -> MultiApproveData.approve(any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(notMulti);

		Connection mockConnection = mock(Connection.class);
		mockStatement = mock(Statement.class);
		connectDB2 = Mockito.mockStatic(ConnectDB2.class);
		connectDB2.when(ConnectDB2::doConnect).thenReturn(mockConnection);
		when(mockConnection.createStatement()).thenReturn(mockStatement);
		// head title / reference (FlowUpdate.updateServiceHeadTexts)
		mockPrepared = mock(PreparedStatement.class);
		when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPrepared);

		// Only addServiceHead looks this up after its INSERT.
		selectData = Mockito.mockStatic(FlowQuery.class);
		selectData.when(() -> FlowQuery.getMaxServiceNumber(any(), any(), any(), any(), any()))
				.thenReturn(SERVICE_NO);

		// Only the prepareAddServiceForm version-guard smoke test needs this.
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

	private void flowStartsAt00() {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		selectData.when(() -> FlowQuery.getStatusNewFlowApproveV2(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS))
				.thenReturn("SVRQ ; 00 ; MANAGER ; " + NEXT_STATUS + " ; mgr01 ; MANAGER");
		flowApprove(STATUS, NEXT_STATUS, "mgr01");
	}

	// FlowQuery.getStatusFlowApprove of a status: its next status and that one's approver (no condition)
	private void flowApprove(String status, String next, String nextApprover) {
		selectData.when(() -> FlowQuery.getStatusFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, status))
				.thenReturn("x ; " + status + " ; " + next + " ; " + nextApprover + " ; G ; 00 ; user01 ; 0 ;  ;  ;  ;  ;  ; ");
	}

	@Test
	public void testprepareAddServiceForm_vacantNextStatus_isSkipped() throws Exception {
		flowStartsAt00();
		// SPRQ update: no selector at 10 (VACANT) -> the request starts at 20
		flowApprove(STATUS, "10", "VACANT");
		flowApprove("10", "20", "dept01");
		java.sql.PreparedStatement ps = mock(java.sql.PreparedStatement.class);
		when(ConnectDB2.doConnect().prepareStatement(any())).thenReturn(ps);

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				buildData(VERSION), null, null, null, "token", REQUESTER);

		assertEquals("ok", new JSONObject(result).getString("result"));
		assertTrue(executedQueries().stream().anyMatch(sql -> sql.contains("SR_FLOWHEAD")
				&& sql.contains("FHDSTA = '20'") && sql.contains("AND FHDSTA = '10'")));
		workflow.verify(() -> Workflow.notify(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, "20"));
	}

	@Test
	public void testprepareAddServiceForm_nextStatusWithApprover_staysThere() throws Exception {
		flowStartsAt00();

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				buildData(VERSION), null, null, null, "token", REQUESTER);

		assertEquals("ok", new JSONObject(result).getString("result"));
		assertTrue(executedQueries().stream().noneMatch(sql -> sql.contains("SET FHDEPH")));
	}

	@Test
	public void testnextNeededStatus_loopThroughVacant_throws() throws Exception {
		flowApprove(STATUS, "10", "VACANT");
		flowApprove("10", "00", "VACANT");
		try {
			ServiceFormEngine.nextNeededStatus(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, STATUS, new JSONObject());
			org.junit.Assert.fail("loop accepted");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("loops through VACANT status 00"));
		}
	}

	private java.util.List<String> executedQueries() throws Exception {
		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockStatement, Mockito.atLeast(0)).execute(sqlCaptor.capture());
		return sqlCaptor.getAllValues();
	}

	// -----------------------------------------------------------------------
	// prepareAddServiceForm — title, Interface M3 of the start status
	// -----------------------------------------------------------------------

	@Test
	public void testprepareAddServiceForm_vTitleWinsOverReason() throws Exception {
		flowStartsAt00();
		String data = buildData(VERSION).replace("}", ",\"vTitle\":\"Photoshop\"}");

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS, data, null,
				null, null, "token", REQUESTER);

		assertEquals("ok", new JSONObject(result).getString("result"));
		verify(mockPrepared, Mockito.atLeastOnce()).setString(2, SERVICENAME + "-" + SERVICE_NO + "-CREATE-Photoshop");
	}

	@Test
	public void testprepareAddServiceForm_vReferenceWrittenToFhdes2() throws Exception {
		flowStartsAt00();
		String data = buildData(VERSION).replace("}", ",\"vReference\":\"C0001\"}");

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS, data, null,
				null, null, "token", REQUESTER);

		assertEquals("ok", new JSONObject(result).getString("result"));
		verify(mockPrepared, Mockito.atLeastOnce()).setString(4, "C0001");
	}

	@Test
	public void testprepareAddServiceForm_noReasonNoTitle_returnsNok() throws Exception {
		flowStartsAt00();
		String data = buildData(VERSION).replace(",\"reasonMain\":\"" + TITLE + "\"", "");

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS, data, null,
				null, null, "token", REQUESTER);

		assertEquals("nok", new JSONObject(result).getString("result"));
		connectDB2.verifyNoInteractions();
	}

	@Test
	public void testprepareAddServiceForm_interfaceRefuses_nothingWritten() throws Exception {
		flowStartsAt00();
		serviceInterface.when(() -> FormInterfaceCall.beforeSubmit(any(), any(), any(), any(), any(), any(), any(),
				any())).thenThrow(new Exception("Supplier 1234 already exists."));

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				buildData(VERSION), null, null, null, "token", REQUESTER);

		JSONObject json = new JSONObject(result);
		assertEquals("nok", json.getString("result"));
		assertEquals("Supplier 1234 already exists.", json.getString("message"));
		connectDB2.verifyNoInteractions();
		serviceInterface.verify(() -> FormInterfaceCall.beforeSubmit(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				REQUESTER, "token", buildData(VERSION)));
	}

	@Test
	public void testprepareAddServiceForm_interfaceDataAndReferenceAreSaved() throws Exception {
		flowStartsAt00();
		String changed = buildData(VERSION).replace("}", ",\"checked\":\"Y\"}");
		serviceInterface.when(() -> FormInterfaceCall.beforeSubmit(any(), any(), any(), any(), any(), any(), any(),
				any())).thenReturn(new FormInterface.Answer(changed, "REF01"));
		java.sql.PreparedStatement ps = mock(java.sql.PreparedStatement.class);
		when(ConnectDB2.doConnect().prepareStatement(any())).thenReturn(ps);

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				buildData(VERSION), null, null, null, "token", REQUESTER);

		assertEquals("ok", new JSONObject(result).getString("result"));
		assertTrue(executedQueries().stream()
				.anyMatch(sql -> sql.contains("SR_FLOWDETAIL") && sql.contains("\"checked\":\"Y\"")));
		verify(ps).setString(4, "REF01");
		verify(ps).executeUpdate();
	}

	private String buildData(String version) {
		return "{\"vApproval\":\"true\""
				+ ",\"vRequester\":\"" + REQUESTER + "\""
				+ ",\"vDepthead\":\"" + DEPTHEAD + "\""
				+ ",\"vApproveBy\":\"approve01\""
				+ ",\"vRemark\":\"test remark\""
				+ ",\"vVersion\":\"" + version + "\""
				+ ",\"requestDateMain\":\"" + REQUESTDATE + "\""
				+ ",\"reasonMain\":\"" + TITLE + "\"}";
	}

	// exact match, not contains(): catches a typo like "SR_FLOWHEADs"
	private static void assertTargetsTable(String sql, String expectedTable) {
		Matcher tableMatcher = Pattern.compile("(?:INSERT INTO|UPDATE)\\s+\\S+\\.(\\S+)").matcher(sql);
		assertTrue("INSERT INTO / UPDATE clause not found in query", tableMatcher.find());
		assertEquals(expectedTable, tableMatcher.group(1));
	}

	private String captureExecutedQuery() throws Exception {
		ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
		verify(mockStatement, times(1)).execute(sqlCaptor.capture());
		return sqlCaptor.getValue();
	}

	// -----------------------------------------------------------------------
	// prepareAddServiceForm — version guard (FlowQuery.checkVersion)
	// -----------------------------------------------------------------------

	@Test
	public void testprepareAddServiceForm_versionMismatch_returnsNok() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				buildData("0.0.1"), null, null, null, "", REQUESTER);

		JSONObject json = new JSONObject(result);
		assertEquals("nok", json.getString("result"));
		// Guard returns before any query-building method opens a connection.
		connectDB2.verifyNoInteractions();
	}

	@Test
	public void testprepareAddServiceForm_requesterIsNotTheCaller_returnsNok() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				buildData(VERSION), null, null, null, "token", "user99");

		JSONObject json = new JSONObject(result);
		assertEquals("nok", json.getString("result"));
		assertEquals("Can't Create service number, you are user99 but the form was sent as " + REQUESTER
				+ ", please reload.", json.getString("message"));
		connectDB2.verifyNoInteractions();
	}

	@Test
	public void testprepareAddServiceForm_requesterInOtherLetterCase_isTheCaller() throws Exception {
		assertEquals(null, ApproveGuard.submitRefusal("USER01", " user01 "));
	}

	@Test
	public void testprepareAddServiceForm_versionMatch_returnsOk() throws Exception {
		selectData.when(() -> FlowQuery.checkVersion("SRQ")).thenReturn(VERSION);
		// Group values other than REQUESTER/DEPTHEAD keep this smoke test to
		// the minimal query set — the branch-specific SQL is already covered
		// by the direct-call tests above.
		selectData.when(() -> FlowQuery.getStatusNewFlowApproveV2(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS))
				.thenReturn("SVRQ ; 00 ; MANAGER ; " + NEXT_STATUS + " ; mgr01 ; MANAGER");
		flowApprove(STATUS, NEXT_STATUS, "mgr01");

		String result = ServiceFormEngine.prepareAddServiceForm(CONO, DIVI, SERVICENAME, REQUESTTYPE, STATUS,
				buildData(VERSION), null, null, null, "", REQUESTER);

		JSONObject json = new JSONObject(result);
		assertEquals("ok", json.getString("result"));
	}
	
	// -----------------------------------------------------------------------
	// addServiceHead — INSERT INTO SR_FLOWHEAD
	// -----------------------------------------------------------------------

	@Test
	public void testaddServiceHead_query_isValid() throws Exception {
		FlowInsert.addServiceHead(CONO, DIVI, SERVICENAME, REQUESTDATE, NEXT_STATUS, REQUESTER, DEPTHEAD);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWHEAD");

		Map<String, String> values = SqlSyntaxAssertions.insertColumnValues(sql);
		assertEquals("'" + CONO + "'", values.get("FHCONO"));
		assertEquals("'" + DIVI + "'", values.get("FHDIVI"));
		assertEquals("'" + SERVICENAME + "'", values.get("FHCODE"));
		assertEquals("'" + REQUESTER + "'", values.get("FHREQU"));
		assertEquals("'" + REQUESTDATE + "'", values.get("FHREDA"));
		assertEquals("'" + DEPTHEAD + "'", values.get("FHDEPH"));

		// FHSRNO (index 3) is the auto-numbering subquery
		Map<String, String> subWhere = SqlSyntaxAssertions.subqueryWhereConditions(sql, 3);
		assertEquals("'" + CONO + "'", subWhere.get("FHCONO"));
		assertEquals("'" + DIVI + "'", subWhere.get("FHDIVI"));
		assertEquals("'" + SERVICENAME + "'", subWhere.get("FHCODE"));
	}

	// -----------------------------------------------------------------------
	// addServiceDetail — INSERT INTO SR_FLOWDETAIL
	// -----------------------------------------------------------------------

	@Test
	public void testaddServiceDetail_query_isValid() throws Exception {
		FlowInsert.addServiceDetail(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE, buildData(VERSION), NEXT_STATUS, REQUESTER,
				DEPTHEAD);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWDETAIL");

		Map<String, String> values = SqlSyntaxAssertions.insertColumnValues(sql);
		assertEquals("'" + CONO + "'", values.get("FDCONO"));
		assertEquals("'" + DIVI + "'", values.get("FDDIVI"));
		assertEquals("'" + SERVICENAME + "'", values.get("FDCODE"));
		assertEquals("'" + SERVICE_NO + "'", values.get("FDSRNO"));
		assertEquals("'" + REQUESTER + "'", values.get("FDENUS"));

		// FDTYPE (index 4) is the request-type lookup subquery
		Map<String, String> subWhere = SqlSyntaxAssertions.subqueryWhereConditions(sql, 4);
		assertEquals("'" + CONO + "'", subWhere.get("RQCONO"));
		assertEquals("'" + DIVI + "'", subWhere.get("RQDIVI"));
		assertEquals("'" + SERVICENAME + "'", subWhere.get("RQCODE"));
		assertEquals("'" + REQUESTTYPE + "'", subWhere.get("RQNAME"));
	}

	// -----------------------------------------------------------------------
	// updateServiceHeadTitle — UPDATE SR_FLOWHEAD
	// -----------------------------------------------------------------------

	@Test
	public void testupdateServiceHeadTitle_query_isValid() throws Exception {
		String title = SERVICENAME + "-" + SERVICE_NO + "-" + REQUESTTYPE.toUpperCase() + "-" + TITLE;
		FlowUpdate.updateServiceHeadTitle(CONO, DIVI, SERVICENAME, SERVICE_NO, NEXT_STATUS, title, REQUESTER);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWHEAD");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + title + "'", setValues.get("FHDES1"));
		assertEquals("'" + REQUESTER + "'", setValues.get("FHENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FHCONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FHDIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FHCODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FHSRNO"));
		assertEquals("'" + NEXT_STATUS + "'", whereValues.get("FHDSTA"));
	}

	// -----------------------------------------------------------------------
	// addServiceFlowApprove — INSERT INTO SR_FLOWAPPROVE ... SELECT
	// -----------------------------------------------------------------------

	@Test
	public void testaddServiceFlowApprove_query_isValid() throws Exception {
		FlowInsert.addServiceFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, REQUESTTYPE);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		// INSERT ... SELECT has no VALUES clause, so insertColumnValues()
		// doesn't apply — check the propagated serviceNumber and WHERE
		// predicates directly instead.
		assertTrue(sql.contains("'" + SERVICE_NO + "' AS PMSRNO"));
		assertTrue(sql.contains("WHERE PMCONO = '" + CONO + "'"));
		assertTrue(sql.contains("AND PMDIVI = '" + DIVI + "'"));
		assertTrue(sql.contains("AND PMCODE = '" + SERVICENAME + "'"));
	}

	// -----------------------------------------------------------------------
	// updateServiceUserFlowApprove (REQUESTER branch) — UPDATE SR_FLOWAPPROVE
	// -----------------------------------------------------------------------

	@Test
	public void testupdateServiceUserFlowApprove_requesterBranch_query_isValid() throws Exception {
		FlowUpdate.updateServiceUserFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS, REQUESTER, REQUESTER);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + REQUESTER + "'", setValues.get("FAAPLI"));
		assertEquals("'" + REQUESTER + "'", setValues.get("FAENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FACONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FADIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FACODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FASRNO"));
		assertEquals("'" + STATUS + "'", whereValues.get("FASTAT"));
	}

	// -----------------------------------------------------------------------
	// updateServiceFlowApproveBy — UPDATE SR_FLOWAPPROVE
	// -----------------------------------------------------------------------

	@Test
	public void testupdateServiceFlowApproveBy_query_isValid() throws Exception {
		FlowUpdate.updateServiceFlowApproveBy(CONO, DIVI, SERVICENAME, SERVICE_NO, STATUS, "", REQUESTER);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + REQUESTER + "'", setValues.get("FAAPBY"));
		assertEquals("''", setValues.get("FADES3"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FACONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FADIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FACODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FASRNO"));
		assertEquals("'" + STATUS + "'", whereValues.get("FASTAT"));
	}

	// -----------------------------------------------------------------------
	// updateServiceUserFlowApprove (DEPTHEAD branch) — UPDATE SR_FLOWAPPROVE
	// -----------------------------------------------------------------------

	@Test
	public void testupdateServiceUserFlowApprove_deptheadBranch_query_isValid() throws Exception {
		FlowUpdate.updateServiceUserFlowApprove(CONO, DIVI, SERVICENAME, SERVICE_NO, NEXT_STATUS, REQUESTER, DEPTHEAD);
		String sql = captureExecutedQuery();

		SqlSyntaxAssertions.assertValidSyntax(sql);
		assertTargetsTable(sql, "SR_FLOWAPPROVE");

		Map<String, String> setValues = SqlSyntaxAssertions.updateSetValues(sql);
		assertEquals("'" + DEPTHEAD + "'", setValues.get("FAAPLI"));
		assertEquals("'" + REQUESTER + "'", setValues.get("FAENUS"));

		Map<String, String> whereValues = SqlSyntaxAssertions.updateWhereConditions(sql);
		assertEquals("'" + CONO + "'", whereValues.get("FACONO"));
		assertEquals("'" + DIVI + "'", whereValues.get("FADIVI"));
		assertEquals("'" + SERVICENAME + "'", whereValues.get("FACODE"));
		assertEquals("'" + SERVICE_NO + "'", whereValues.get("FASRNO"));
		assertEquals("'" + NEXT_STATUS + "'", whereValues.get("FASTAT"));
	}

}
