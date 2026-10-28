package com.br.engine;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.connection.ConnectDB2;

/** Deletes of the request flow: SR_FILE. */
public class FlowDelete {

	private static final Logger logger = LogManager.getLogger(FlowDelete.class);

	protected static String DBNAME = EngineConfig.DBNAME;

	public static void deleteServiceFile(String cono, String divi, String servicename, String serviceno,
			String fieldname) throws Exception {
		logger.info("deleteServiceFile");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "DELETE " + DBNAME + ".SR_FILE \n"
					+ "WHERE FICONO = '" + cono + "' \n"
					+ "AND FIDIVI = '" + divi + "' \n"
					+ "AND FICODE = '" + servicename + "' \n"
					+ "AND FISRNO = '" + serviceno + "' \n"
					+ "AND FISNAM = '" + fieldname + "'";
			logger.debug(query);
			stmt.execute(query);

			logger.info("deleteServiceFile complete fieldName {}", fieldname);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
