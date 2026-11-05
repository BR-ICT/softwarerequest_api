package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.br.connection.ConnectDB2;

/** FlowQuery.getStatusFlowApprove: NULL columns (LEFT JOIN without a row) are read as "". */
public class FlowQueryStatusFlowTest {

	private MockedStatic<ConnectDB2> connectDB2;
	private ResultSet rs;

	@Before
	public void setUp() throws Exception {
		Connection conn = mock(Connection.class);
		Statement stmt = mock(Statement.class);
		rs = mock(ResultSet.class);
		when(conn.createStatement()).thenReturn(stmt);
		when(stmt.executeQuery(anyString())).thenReturn(rs);
		when(rs.next()).thenReturn(true, false);
		connectDB2 = Mockito.mockStatic(ConnectDB2.class);
		connectDB2.when(ConnectDB2::doConnect).thenReturn(conn);
	}

	@After
	public void tearDown() {
		connectDB2.close();
	}

	private void row(String nextGroup) throws Exception {
		when(rs.getString(anyString())).thenAnswer(i -> " " + i.getArgument(0) + " ");
		when(rs.getString("NEXT_GROUP")).thenReturn(nextGroup);
	}

	@Test
	public void nullNextGroup_readAsEmpty() throws Exception {
		row(null);

		String flow = FlowQuery.getStatusFlowApprove("10", "101", "CMRQ", "26000001", "create", "80");

		String[] fields = flow.split(";");
		assertEquals(14, fields.length);
		assertEquals("", fields[4].trim());
		assertEquals("NEXT_APPROVE", fields[3].trim());
	}

	@Test
	public void values_trimmed() throws Exception {
		row(" ICT ");

		String flow = FlowQuery.getStatusFlowApprove("10", "101", "CMRQ", "26000001", "create", "80");

		assertEquals("FAAPLI ; PMSTAT ; NEXT_STAT ; NEXT_APPROVE ; ICT ; PREVIOUS_STAT ; PREVIOUS_APPROVE"
				+ " ; PMM3IF ; NEXT_CFLD ; NEXT_COPR ; NEXT_CVAL ; PREVIOUS_CFLD ; PREVIOUS_COPR ; PREVIOUS_CVAL", flow);
	}
}
