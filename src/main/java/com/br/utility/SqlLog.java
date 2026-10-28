package com.br.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Debug log of prepared statements with their values: use SqlLog.prepare(conn, query) instead of
 * conn.prepareStatement(query). The statement remembers what set* puts in each parameter and, on execute,
 * logs the SQL with every ? replaced by its value and the class + line that ran it, e.g.
 * "GroupData.java:436 - INSERT INTO ... WHERE GMCONO = '10' AND GMDIVI = '101' ...".
 * A query that fails to prepare (e.g. SQL0418) is logged with the error. Debug off = the plain statement.
 * The logged SQL is for reading only: the values are still sent as parameters.
 */
public final class SqlLog {

	private static final Logger logger = LogManager.getLogger(SqlLog.class);

	private SqlLog() {
	}

	public static PreparedStatement prepare(Connection conn, String query) throws SQLException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
		} catch (SQLException e) {
			logger.error("{} - prepare failed: {}\n{}", caller(), e.getMessage(), query);
			throw e;
		}
		if (ps == null || !logger.isDebugEnabled()) {
			return ps;
		}

		Map<Integer, Object> values = new TreeMap<>();
		return (PreparedStatement) Proxy.newProxyInstance(SqlLog.class.getClassLoader(),
				new Class<?>[] { PreparedStatement.class }, (proxy, method, args) -> {
					String name = method.getName();
					// setString(index, value), setInt(..), setNull(index, type) ...; not setFetchSize(n) etc.
					if (name.startsWith("set") && args != null && args.length >= 2 && args[0] instanceof Integer) {
						values.put((Integer) args[0], name.equals("setNull") ? null : args[1]);
					} else if (name.equals("clearParameters")) {
						values.clear();
					} else if ((name.startsWith("execute") || name.equals("addBatch")) && (args == null || args.length == 0)) {
						logger.debug("{} - {}", caller(), format(query, values));
					}
					try {
						return method.invoke(ps, args);
					} catch (InvocationTargetException e) {
						throw e.getCause();
					}
				});
	}

	/** query with each ? outside a '...' literal replaced by its value: 'text' (quotes doubled), number, NULL. */
	static String format(String query, Map<Integer, Object> values) {
		StringBuilder sb = new StringBuilder(query.length() + 64);
		boolean inLiteral = false;
		int index = 0;
		for (char c : query.toCharArray()) {
			if (c == '\'') {
				inLiteral = !inLiteral;
			}
			if (c != '?' || inLiteral) {
				sb.append(c);
				continue;
			}
			index++;
			if (!values.containsKey(index)) {
				sb.append('?');
			} else {
				Object value = values.get(index);
				if (value == null) {
					sb.append("NULL");
				} else if (value instanceof Number || value instanceof Boolean) {
					sb.append(value);
				} else {
					sb.append('\'').append(value.toString().replace("'", "''")).append('\'');
				}
			}
		}
		return sb.toString();
	}

	// first frame outside this class and the proxy: the data class that ran the statement
	private static String caller() {
		for (StackTraceElement frame : new Throwable().getStackTrace()) {
			String cls = frame.getClassName();
			if (cls.startsWith(SqlLog.class.getName()) || cls.startsWith("java.") || cls.startsWith("jdk.")
					|| cls.startsWith("sun.") || cls.startsWith("com.sun.proxy") || cls.contains("$Proxy")) {
				continue;
			}
			return frame.getFileName() + ":" + frame.getLineNumber();
		}
		return "?";
	}
}
