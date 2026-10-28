package com.br.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.br.connection.ConnectDB2;
import com.br.utility.ConvertResultSet;
import com.br.utility.SqlLog;

public class FlowQueryFormReadTest {

	private MockedStatic<ConnectDB2> connectDB2;
	private MockedStatic<SqlLog> sqlLog;
	private MockedStatic<ConvertResultSet> convert;
	private PreparedStatement ps;
	private final ArgumentCaptor<String> sql = ArgumentCaptor.forClass(String.class);

	@Before
	public void setUp() throws Exception {
		Connection conn = mock(Connection.class);
		ps = mock(PreparedStatement.class);
		when(ps.executeQuery()).thenReturn(mock(ResultSet.class));
		connectDB2 = Mockito.mockStatic(ConnectDB2.class);
		connectDB2.when(ConnectDB2::doConnect).thenReturn(conn);
		sqlLog = Mockito.mockStatic(SqlLog.class);
		sqlLog.when(() -> SqlLog.prepare(Mockito.any(Connection.class), sql.capture())).thenReturn(ps);
		convert = Mockito.mockStatic(ConvertResultSet.class);
		convert.when(() -> ConvertResultSet.convertResultSetToJson(Mockito.any())).thenReturn("[]");
	}

	@After
	public void tearDown() {
		convert.close();
		sqlLog.close();
		connectDB2.close();
	}

	@Test
	public void getServiceForm_bindsTheRequestAndIsValidSql() throws Exception {
		assertEquals("[]", FlowQuery.getServiceForm("10", "101", "SWRQ", "26000001"));

		SqlSyntaxAssertions.assertValidSyntax(sql.getValue());
		assertTrue(sql.getValue().contains("FDSRNO = ?"));
		verify(ps).setString(1, "10");
		verify(ps).setString(2, "101");
		verify(ps).setString(3, "SWRQ");
		verify(ps).setString(4, "26000001");
	}

	@Test
	public void getServiceForm_noValueInTheSql() throws Exception {
		FlowQuery.getServiceForm("10", "101", "SWRQ", "' OR '1'='1");

		assertTrue(!sql.getValue().contains("' OR '1'='1"));
		verify(ps).setString(4, "' OR '1'='1");
	}

	@Test
	public void getServiceFormHistory_bindsTheRequestTwice() throws Exception {
		assertEquals("[]", FlowQuery.getServiceFormHistory("10", "101", "SWRQ", "26000001"));

		// both parts of the UNION read the same request
		verify(ps).setString(1, "10");
		verify(ps).setString(5, "10");
		verify(ps).setString(4, "26000001");
		verify(ps).setString(8, "26000001");
	}
}
