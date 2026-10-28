package com.br.engine;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.br.connection.ConnectDB2;

/** Inserts of the request flow: SR_FLOWHEAD, SR_FLOWDETAIL, SR_FLOWAPPROVE (one row per status), SR_FILE. */
public class FlowInsert {

	private static final Logger logger = LogManager.getLogger(FlowInsert.class);

	protected static String DBNAME = EngineConfig.DBNAME;

	/**
	 * status = the status the request starts at (FHDSTA), the next one after the start status 00. Answers the
	 * new service number; throws when the head cannot be written (never answers an error as the number).
	 */
	public static String addServiceHead(String cono, String divi, String servicename, String requestdate,
			String status, String username, String depthead)
			throws Exception {
		logger.info("addServiceHead");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String getYear4 = requestdate.substring(0, 4);

			String query = "INSERT INTO " + DBNAME + ".SR_FLOWHEAD \n"
					+ "(FHCONO, FHDIVI, FHCODE, FHSRNO, FHDES1, FHDES2, FHREQU, FHREDA, FHDEPH, FHHSTA, FHDSTA, FHENDA, FHENTI, FHENUS) \n"
					+ "VALUES('" + cono + "' \n"
					+ ", '" + divi + "' \n"
					+ ", '" + servicename + "' \n"
					+ ", (SELECT CASE WHEN CAST(MAX(FHSRNO) AS DECIMAL(10,0)) > 0 THEN CAST(MAX(FHSRNO) AS DECIMAL(10,0)) + 1  \n"
					+ "ELSE CAST((SUBSTRING('" + requestdate + "',3,2) || '000001') AS DECIMAL(10,0)) END AS FHSRNO  \n"
					+ "FROM " + DBNAME + ".SR_FLOWHEAD  \n"
					+ "WHERE FHCONO = '" + cono + "'  \n"
					+ "AND FHDIVI = '" + divi + "' \n"
					+ "AND FHCODE = '" + servicename + "' \n"
					+ "AND SUBSTRING(CHAR(FHREDA,ISO),0,5) = SUBSTRING('" + requestdate + "',0,5)) \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '" + username + "' \n"
					+ ", '" + requestdate + "' \n"
					+ ", '" + depthead + "' \n"
					+ ", '2' \n"
					+ ", '" + status + "' \n"
					+ ", CURRENT DATE \n"
					+ ", CURRENT TIME \n"
					+ ", '" + username + "')";
			// System.out.println("addADRHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			String getServiceNumber = FlowQuery.getMaxServiceNumber(cono, divi, servicename, getYear4, username);
			logger.debug("getServiceNumber {}", getServiceNumber);
			if (getServiceNumber == null || getServiceNumber.trim().isEmpty()) {
				throw new Exception("Can't create service number of " + servicename + ", please try again.");
			}
			return getServiceNumber.trim();

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}

		}

	}

	/** status = the status the request starts at (FDDSTA), as in addServiceHead. */
	public static String addServiceDetail(String cono, String divi, String servicename, String serviceno,
			String requesttype, String data, String status,
			String username, String depthead)
			throws Exception {
		logger.info("addServiceDetail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			// String getYear4 = requestdate.substring(0, 4);

			String query = "INSERT INTO " + DBNAME + ".SR_FLOWDETAIL \n"
					+ "(FDCONO, FDDIVI, FDCODE, FDSRNO, FDTYPE, FDDATA, FDDES1, FDDES2, FDDSTA, FDENDA, FDENTI, FDENUS) \n"
					+ "VALUES('" + cono + "' \n"
					+ ", '" + divi + "' \n"
					+ ", '" + servicename + "' \n"
					+ ", '" + serviceno + "' \n"
					+ ", (SELECT RQTYPE \n"
					+ "FROM " + DBNAME + ".SR_REQUESTTYPE \n"
					+ "WHERE RQCONO = '" + cono + "' \n"
					+ "AND RQDIVI = '" + divi + "' \n"
					+ "AND RQCODE = '" + servicename + "' \n"
					+ "AND RQNAME = '" + requesttype + "') \n"
					+ ", '" + data + "' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '" + status + "' \n"
					+ ", CURRENT DATE \n"
					+ ", CURRENT TIME \n"
					+ ", '" + username + "')";
			// System.out.println("addADRHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Create complete.");

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}

		}

		return mJsonObj.toString();

	}

	public static String addServiceFlowApprove(String cono, String divi, String servicename, String serviceno,
			String requesttype)
			throws Exception {
		logger.info("addServiceFlowApprove");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "INSERT INTO " + DBNAME + ".SR_FLOWAPPROVE \n"
					+ "(FACONO, FADIVI, FACODE, FASRNO, FATYPE, FAAPLI, FAAPBY, FAAPDA, FAAPTI, FARJBY, FARJDA, FARJTI, FADES1, FADES2, FADES3, FADES4, FASTAT, FASTDE, FAENDA, FAENTI, FAENUS) \n"
					+ "SELECT PMCONO, PMDIVI, PMCODE, '" + serviceno + "' AS PMSRNO \n"
					+ ", (SELECT RQTYPE \n"
					+ "FROM " + DBNAME + ".SR_REQUESTTYPE \n"
					+ "WHERE RQCONO = '" + cono + "' \n"
					+ "AND RQDIVI = '" + divi + "' \n"
					+ "AND RQCODE = '" + servicename + "' \n"
					+ "AND RQNAME = '" + requesttype + "') AS PMTYPE \n"
					+ ", NAME_SERIAL, '' AS PMAPBY,CAST(NULL AS DATE) AS PMAPDA, CAST(NULL AS TIME) AS PMAPTI \n"
					+ ", '' AS PMRJBY, CAST(NULL AS DATE) AS PMRJDA, CAST(NULL AS TIME) AS PMRJTI, 'Wait for approve' AS PMDES1, PMDES1 AS PMDES2, '' AS PMDES3, '' AS PMDES4 \n"
					+ ", PMSTAT, '', CURRENT_DATE, CURRENT_TIME, 'M3SRVADM' \n"
					+ "FROM " + DBNAME + ".VI_PROCESS \n"
					+ "WHERE PMCONO = '" + cono + "' \n"
					+ "AND PMDIVI = '" + divi + "' \n"
					+ "AND PMCODE = '" + servicename + "' \n"
				    + "AND PMRQTY = '" + requesttype + "' \n"
					+ "ORDER BY INT(PMSTAT)";
			// System.out.println("addADRHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Create complete.");

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}

		}

		return mJsonObj.toString();

	}

	public static String addServiceFile(String cono, String divi, String servicename, String serviceno, int fileIndex,
			String fieldname, String originalFileName, String filetype, String filename, String username)
			throws Exception {
		logger.info("addServiceFile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "INSERT INTO " + DBNAME + ".SR_FILE \n"
					+ "(FICONO, FIDIVI, FICODE, FISRNO, FILINE, FISNAM, FIFNAM, FITYPE, FIREM1, FIREM2, FIENDA, FIENTI, FIENUS) \n"
					+ "VALUES(" + cono + " \n"
					+ ", " + divi + " \n"
					+ ", '" + servicename + "' \n"
					+ ", '" + serviceno + "' \n"
					+ ", '" + fileIndex + "' \n"
					+ ", '" + fieldname + "' \n"
					+ ", '" + originalFileName + "' \n"
					+ ", '" + filetype + "' \n"
					+ ", '" + filename + "' \n"
					+ ", '' \n"
					+ ", CURRENT DATE \n"
					+ ", CURRENT TIME \n"
					+ ", '" + username + "')";
			// System.out.println("addADRHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Create complete.");

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}

		}

		return mJsonObj.toString();

	}
}
