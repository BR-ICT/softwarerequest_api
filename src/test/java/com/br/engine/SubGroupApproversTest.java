package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.br.connection.ConnectDB2;
import com.br.utility.SqlLog;

/**
 * Lanes of approve mode ANY: the condition of a non-default sub group picks it, e.g. CMRQ COLLECTION 30:
 * CMRQ (default), LOCAL saleTypeMain <> 2, EXPORT saleTypeMain = 2.
 */
public class SubGroupApproversTest {

	private static String[] row(String status, String subGroup, String isDefault, String user, String field,
			String operator, String value) {
		return new String[] { status, subGroup, isDefault, user, field, operator, value };
	}

	// { status, sub group, PMDES2, user, PMCFLD, PMCOPR, PMCVAL } as FlowQuery.getProcessSubGroupUsers answers
	private static List<String[]> cmrq() {
		return Arrays.asList(
				row("20", "SALES", "1", "VACANT", "", "", ""),
				row("30", "CMRQ", "1", "NITTAY_KOM", "", "", ""),
				row("30", "CMRQ", "1", "SAVANE_KOM", "", "", ""),
				row("30", "EXPORT", "0", "NITTAY_KOM", "saleTypeMain", "=", "2"),
				row("30", "EXPORT", "0", "PORNRA_MET", "saleTypeMain", "=", "2"),
				row("30", "LOCAL", "0", "SUPAWI_NAS", "saleTypeMain", "<>", "2"),
				row("30", "NOCOND", "0", "OTHER", "", "", ""),
				row("60", "GFD", "1", "gfd1", "creditLimit3Main", ">=", "500001"));
	}

	private static JSONObject form(String saleType) throws Exception {
		return new JSONObject().put("saleTypeMain", saleType);
	}

	private static String only(List<String[]> approvers, String status) {
		assertEquals(1, approvers.size());
		assertEquals(status, approvers.get(0)[0]);
		return approvers.get(0)[1];
	}

	@Test
	public void laneWhoseConditionHolds_getsItsUsers() throws Exception {
		assertEquals("NITTAY_KOM,PORNRA_MET", only(ServiceFormEngine.chooseSubGroupApprovers(cmrq(), form("2")), "30"));
		assertEquals("SUPAWI_NAS", only(ServiceFormEngine.chooseSubGroupApprovers(cmrq(), form("1")), "30"));
		assertEquals("SUPAWI_NAS", only(ServiceFormEngine.chooseSubGroupApprovers(cmrq(), form("3")), "30"));
	}

	@Test
	public void severalLanesHold_allTheirUsersOnce() throws Exception {
		List<String[]> rows = Arrays.asList(
				row("20", "WHS", "1", "def1", "", "", ""),
				row("20", "A41", "0", "a41", "warehouseMain", "=", "A41"),
				row("20", "BIG", "0", "big1", "amountMain", ">", "100"),
				row("20", "BIG", "0", "a41", "amountMain", ">", "100"));
		JSONObject data = new JSONObject().put("warehouseMain", "A41").put("amountMain", "500");

		assertEquals("a41,big1", only(ServiceFormEngine.chooseSubGroupApprovers(rows, data), "20"));
	}

	@Test
	public void noLaneHolds_getsTheDefault() throws Exception {
		List<String[]> rows = new ArrayList<>(cmrq());
		rows.removeIf(r -> "LOCAL".equals(r[1]));
		// answered anyway: a resubmit that no longer picks a lane puts the default back
		assertEquals("NITTAY_KOM,SAVANE_KOM", only(ServiceFormEngine.chooseSubGroupApprovers(rows, form("1")), "30"));
	}

	@Test
	public void laneWithoutUsers_getsTheDefault() throws Exception {
		List<String[]> rows = Arrays.asList(
				row("30", "CMRQ", "1", "def1", "", "", ""),
				row("30", "EMPTY", "0", "", "saleTypeMain", "=", "2"));
		assertEquals("def1", only(ServiceFormEngine.chooseSubGroupApprovers(rows, form("2")), "30"));
	}

	@Test
	public void conditionOfTheDefault_isNotALane() throws Exception {
		// 60 has one sub group: its condition skips the status (FlowStep), the lanes leave it to VI_PROCESS
		JSONObject data = form("2").put("creditLimit3Main", "1");
		List<String[]> approvers = ServiceFormEngine.chooseSubGroupApprovers(cmrq(), data);
		assertEquals(1, approvers.size());
		assertEquals("30", approvers.get(0)[0]);
	}

	@Test
	public void statusWithOneSubGroup_isLeftToViProcess() throws Exception {
		List<String[]> rows = Collections.singletonList(row("40", "CREDIT", "1", "cr1", "", "", ""));
		assertTrue(ServiceFormEngine.chooseSubGroupApprovers(rows, form("1")).isEmpty());
		assertTrue(ServiceFormEngine.chooseSubGroupApprovers(Collections.<String[]>emptyList(), form("1")).isEmpty());
	}

	@Test
	public void laneFieldMissingInTheForm_throws() {
		try {
			ServiceFormEngine.chooseSubGroupApprovers(cmrq(), new JSONObject());
			fail("a lane condition that cannot be checked must throw");
		} catch (Exception e) {
			assertTrue(e.getMessage(), e.getMessage().contains("saleTypeMain is missing or empty"));
		}
	}

	@Test
	public void tooManyUsers_throws() {
		List<String[]> rows = new ArrayList<>();
		rows.add(row("30", "CMRQ", "1", "def1", "", "", ""));
		for (int i = 0; i < 30; i++) {
			rows.add(row("30", "LOCAL", "0", "USER_NAME" + i, "saleTypeMain", "=", "1"));
		}
		try {
			ServiceFormEngine.chooseSubGroupApprovers(rows, form("1"));
			fail("users that do not fit FAAPLI must throw");
		} catch (Exception e) {
			assertTrue(e.getMessage().startsWith("Status 30 has too many approvers"));
		}
	}

	@Test
	public void query_bindsTheRequestTypeAndIsValidSql() throws Exception {
		Connection conn = mock(Connection.class);
		PreparedStatement ps = mock(PreparedStatement.class);
		when(ps.executeQuery()).thenReturn(mock(ResultSet.class));
		ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);
		try (MockedStatic<ConnectDB2> connectDB2 = Mockito.mockStatic(ConnectDB2.class);
				MockedStatic<SqlLog> sqlLog = Mockito.mockStatic(SqlLog.class)) {
			connectDB2.when(ConnectDB2::doConnect).thenReturn(conn);
			sqlLog.when(() -> SqlLog.prepare(Mockito.any(Connection.class), sql.capture())).thenReturn(ps);

			assertTrue(FlowQuery.getProcessSubGroupUsers("10", "101", "CMRQ", "create").isEmpty());
		}

		SqlSyntaxAssertions.assertValidSyntax(sql.getValue());
		assertTrue(sql.getValue().contains("COALESCE(p.PMAPMD, '') <> '1'"));
		assertTrue(sql.getValue().contains("p.PMCFLD"));
		verify(ps).setString(1, "10");
		verify(ps).setString(2, "101");
		verify(ps).setString(3, "CMRQ");
		verify(ps).setString(4, "create");
	}
}
