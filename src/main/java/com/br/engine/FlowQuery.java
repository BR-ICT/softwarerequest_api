package com.br.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.connection.ConnectDB2;
import com.br.utility.ConvertResultSet;
import com.br.utility.SqlLog;

/** Reads of the request flow: SR_FLOWHEAD, SR_FLOWAPPROVE, VI_PROCESS, SR_GROUP, SR_FILE, APPCTL3 (version). */
public class FlowQuery {

	private static final Logger logger = LogManager.getLogger(FlowQuery.class);

	protected static String DBNAME = EngineConfig.DBNAME;
	protected static String DBM3NAME = EngineConfig.DBM3NAME;

	public static String getMaxServiceNumber(String cono, String divi, String servicename, String year, String username)
			throws Exception {
		logger.info("getMaxDodNumber");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT COALESCE(MAX(FHSRNO),0) AS SERVICENUMBER \n"
					+ "FROM " + DBNAME + ".SR_FLOWHEAD \n"
					+ "WHERE FHCONO = '" + cono + "' \n"
					+ "AND FHDIVI = '" + divi + "' \n"
					+ "AND FHCODE = '" + servicename + "' \n"
					+ "AND SUBSTRING(CHAR(FHREDA,ISO),0,5) = '" + year + "' \n"
					+ "AND FHREQU = '" + username + "'";
			// System.out.println("getMaxMARNumber\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("SERVICENUMBER").trim();
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		return null;

	}

	public static String checkApproveBy(String cono, String divi, String servicename, String serviceno,
			String status)
			throws Exception {
		logger.info("getApproveBy");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT FAAPBY \n"
					+ "FROM " + DBNAME + ".SR_FLOWAPPROVE \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "' \n"
					+ "AND FASTAT = '" + status + "'";
			// System.out.println("getBU\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("FAAPBY");

			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		return null;

	}

	/**
	 * Statuses of a request whose group is a System / Selector group (SR_GROUP.GMDES2 1 or 2:
	 * REQUESTER, DEPTHEAD, APPROVER, SELECTOR, ...). Those groups have no fixed users (VACANT), the
	 * approver's name comes from the form, in the key kept in SR_GROUP.GMDES1 (vRequester, vDepthead,
	 * vSalesdirector, ...). Returns { FASTAT, PMGROU, FAAPLI, GMDES1 } ordered by status;
	 * afterStatus = only the statuses after it, empty = every status.
	 * Unlike the older queries here, an SQL error is thrown: a name that is silently not written
	 * would leave the status VACANT and make the engine skip its approver.
	 */
	public static List<String[]> getFlowApproveNamedGroups(String cono, String divi, String servicename,
			String serviceno, String requesttype, String afterStatus) throws Exception {
		logger.info("getFlowApproveNamedGroups");

		String query = "SELECT a.FASTAT, b.PMGROU, a.FAAPLI, g.GMDES1 \n"
				+ "FROM " + DBNAME + ".SR_FLOWAPPROVE a, " + DBNAME + ".VI_PROCESS b \n"
				+ ", (SELECT GMGROU, MAX(COALESCE(GMDES1, '')) AS GMDES1 \n"
				+ "FROM " + DBNAME + ".SR_GROUP \n"
				+ "WHERE GMCONO = '" + cono + "' \n"
				+ "AND GMDIVI = '" + divi + "' \n"
				+ "AND GMDES2 IN ('1', '2') \n"
				+ "GROUP BY GMGROU) g \n"
				+ "WHERE a.FACONO = '" + cono + "' \n"
				+ "AND a.FADIVI = '" + divi + "' \n"
				+ "AND a.FACODE = '" + servicename + "' \n"
				+ "AND a.FASRNO = '" + serviceno + "' \n"
				+ "AND b.PMCONO = a.FACONO \n"
				+ "AND b.PMDIVI = a.FADIVI \n"
				+ "AND b.PMCODE = a.FACODE \n"
				+ "AND b.PMRQTY = '" + requesttype + "' \n"
				+ "AND b.PMSTAT = a.FASTAT \n"
				+ "AND g.GMGROU = b.PMGROU \n"
				+ (afterStatus == null || afterStatus.isEmpty() ? ""
						: "AND INT(a.FASTAT) > INT('" + afterStatus + "') \n")
				+ "ORDER BY INT(a.FASTAT)";
		logger.debug(query);

		List<String[]> statuses = new ArrayList<>();
		try (Connection conn = ConnectDB2.doConnect();
				Statement stmt = conn.createStatement();
				ResultSet mRes = stmt.executeQuery(query)) {
			while (mRes.next()) {
				statuses.add(new String[] { mRes.getString("FASTAT").trim(), mRes.getString("PMGROU").trim(),
						mRes.getString("FAAPLI").trim(), mRes.getString("GMDES1").trim() });
			}
		}
		return statuses;
	}

	public static String getStatusNewFlowApproveV2(String cono, String divi, String servicename, String requesttype,
			String status)
			throws Exception {
		logger.info("getStatusNewFlowApprove");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT RQNAME, RQSTAT, RQGROUP, NEXT_STAT, NEXT_APPROVE, NEXT_GROUP \n"
					+ "FROM \n"
					+ "(SELECT NAME_SERIAL AS RQNAME, PMSTAT AS RQSTAT, PMGROU AS RQGROUP, NEXT_STAT \n"
					+ "FROM " + DBNAME + ".VI_PROCESS \n"
					+ "WHERE PMCONO = '" + cono + "' \n"
					+ "AND PMDIVI = '" + divi + "' \n"
					+ "AND PMCODE = '" + servicename + "' \n"
					+ "AND PMSTAT = '" + status + "'  \n"
					+ "AND PMRQTY = '" + requesttype + "' ) a \n"
					+ "LEFT JOIN \n"
					+ "(SELECT PMSTAT, NAME_SERIAL AS NEXT_APPROVE, PMGROU AS NEXT_GROUP \n"
					+ "FROM " + DBNAME + ".VI_PROCESS \n"
					+ "WHERE PMCONO = '" + cono + "' \n"
					+ "AND PMDIVI = '" + divi + "' \n"
					+ "AND PMCODE = '" + servicename + "' \n"
					+ " AND PMRQTY = '" + requesttype + "' ) b \n"
					+ "ON b.PMSTAT = a.NEXT_STAT";
			// System.out.println("getMARAccountant\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);
			// RQNAME, RQSTAT, RQGROUP, NEXT_STAT, NEXT_APPROVE, NEXT_GROUP
			while (mRes.next()) {
				return mRes.getString("RQNAME").trim()
						+ " ; " + mRes.getString("RQSTAT").trim()
						+ " ; " + mRes.getString("RQGROUP").trim()
						+ " ; " + mRes.getString("NEXT_STAT").trim()
						+ " ; " + mRes.getString("NEXT_APPROVE").trim()
						+ " ; " + mRes.getString("NEXT_GROUP").trim();

			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		return null;

	}

	public static String getStatusFlowApprove(String cono, String divi, String servicename, String serviceno,
			String requesttype,
			String status)
			throws Exception {
		logger.info("getStatusFlowApprove");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			// After the original 7 fields: interface M3 flag of this status, then the condition
			// (variable, operator, value) of the next status and of the previous status.
			String query = "SELECT FAAPLI, PMSTAT, NEXT_STAT, COALESCE(NEXT_APPROVE,'') AS NEXT_APPROVE, NEXT_GROUP, PREVIOUS_STAT, COALESCE(PREVIOUS_APPROVE,'') AS PREVIOUS_APPROVE \n"
					+ ", PMM3IF, COALESCE(NEXT_CFLD,'') AS NEXT_CFLD, COALESCE(NEXT_COPR,'') AS NEXT_COPR, COALESCE(NEXT_CVAL,'') AS NEXT_CVAL \n"
					+ ", COALESCE(PREVIOUS_CFLD,'') AS PREVIOUS_CFLD, COALESCE(PREVIOUS_COPR,'') AS PREVIOUS_COPR, COALESCE(PREVIOUS_CVAL,'') AS PREVIOUS_CVAL \n"
					+ "FROM \n"
					+ "(SELECT FAAPLI, PMSTAT, NEXT_STAT, PREVIOUS_STAT, PMM3IF \n"
					+ "FROM " + DBNAME + ".SR_FLOWAPPROVE a, " + DBNAME + ".VI_PROCESS b \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "' \n"
					+ "AND FASTAT = '" + status + "' \n"
					+ "AND b.PMCONO = a.FACONO   \n"
					+ "AND b.PMDIVI = a.FADIVI   \n"
					+ "AND b.PMCODE = a.FACODE  \n"
					+ "AND b.PMRQTY = '" + requesttype + "'  \n"
					+ "AND b.PMSTAT = a.FASTAT) a \n"
					+ "LEFT JOIN \n"
					+ "(SELECT FASTAT,FAAPLI AS NEXT_APPROVE, PMGROU AS NEXT_GROUP \n"
					+ ", PMCFLD AS NEXT_CFLD, PMCOPR AS NEXT_COPR, PMCVAL AS NEXT_CVAL \n"
					+ "FROM " + DBNAME + ".SR_FLOWAPPROVE a, " + DBNAME + ".VI_PROCESS b \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "' \n"
					+ "AND b.PMRQTY = '" + requesttype + "'  \n"
					+ "AND b.PMCONO = a.FACONO \n"
					+ "AND b.PMDIVI = a.FADIVI \n"
					+ "AND b.PMCODE = a.FACODE \n"
					+ "AND b.PMSTAT = a.FASTAT) b \n"
					+ "ON b.FASTAT = a.NEXT_STAT \n"
					+ "LEFT JOIN \n"
					+ "(SELECT FASTAT,FAAPLI AS PREVIOUS_APPROVE \n"
					+ "FROM " + DBNAME + ".SR_FLOWAPPROVE \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "') c \n"
					+ "ON c.FASTAT = a.PREVIOUS_STAT \n"
					+ "LEFT JOIN \n"
					+ "(SELECT PMSTAT AS PREVIOUS_PMSTAT, PMCFLD AS PREVIOUS_CFLD, PMCOPR AS PREVIOUS_COPR, PMCVAL AS PREVIOUS_CVAL \n"
					+ "FROM " + DBNAME + ".VI_PROCESS \n"
					+ "WHERE PMCONO = '" + cono + "' \n"
					+ "AND PMDIVI = '" + divi + "' \n"
					+ "AND PMCODE = '" + servicename + "' \n"
					+ "AND PMRQTY = '" + requesttype + "') d \n"
					+ "ON d.PREVIOUS_PMSTAT = a.PREVIOUS_STAT";
			// System.out.println("getMARAccountant\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("FAAPLI").trim()
						+ " ; " + mRes.getString("PMSTAT").trim()
						+ " ; " + mRes.getString("NEXT_STAT").trim()
						+ " ; " + mRes.getString("NEXT_APPROVE").trim()
						+ " ; " + mRes.getString("NEXT_GROUP").trim()
						+ " ; " + mRes.getString("PREVIOUS_STAT").trim()
						+ " ; " + mRes.getString("PREVIOUS_APPROVE").trim()
						+ " ; " + mRes.getString("PMM3IF").trim()
						+ " ; " + mRes.getString("NEXT_CFLD").trim()
						+ " ; " + mRes.getString("NEXT_COPR").trim()
						+ " ; " + mRes.getString("NEXT_CVAL").trim()
						+ " ; " + mRes.getString("PREVIOUS_CFLD").trim()
						+ " ; " + mRes.getString("PREVIOUS_COPR").trim()
						+ " ; " + mRes.getString("PREVIOUS_CVAL").trim();

			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		return null;

	}

	/** { FHDSTA (status the request is at), FHHSTA (1 wait submit, 2 wait approve, 3 complete, 9 cancel) }, null = no request. */
	public static String[] getHeadStatus(String cono, String divi, String servicename, String serviceno)
			throws Exception {
		String query = "SELECT FHDSTA, FHHSTA FROM " + DBNAME + ".SR_FLOWHEAD \n"
				+ "WHERE FHCONO = ? AND FHDIVI = ? AND FHCODE = ? AND FHSRNO = ?";
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, cono);
			ps.setString(2, divi);
			ps.setString(3, servicename);
			ps.setString(4, serviceno);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() ? new String[] { rs.getString("FHDSTA").trim(), rs.getString("FHHSTA").trim() } : null;
			}
		}
	}

	public static String checkVersion(String application) throws Exception {
		logger.info("checkVersion");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT CTL3_REM1 AS VERSION \n"
					+ "FROM " + DBNAME + ".APPCTL3 \n"
					+ "WHERE CTL3_CODE = '" + application + "'";

			// System.out.println("getUserM3\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			logger.debug(query);

			while (mRes.next()) {
				return mRes.getString("VERSION").trim();
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		return null;

	}

	public static Boolean checkCancelService(String cono, String divi, String servicename, String serviceno,
			String username) throws Exception {
		logger.info("checkCancelService");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT FHCONO, FHDIVI, FHCODE, FHSRNO, FHHSTA, c.FAAPLI, CASE WHEN FHHSTA NOT IN ('3', '9') THEN 'true' ELSE 'false' END AS CHECKSTATUS, CASE WHEN (FHREQU LIKE '%"
					+ username + "%' OR c.FAAPLI LIKE '%" + username + "%') THEN 'true' ELSE 'false' END CHECKAUTHEN \n"
					+ "FROM " + DBNAME + ".SR_FLOWHEAD AS a \n"
					+ "LEFT JOIN " + DBNAME + ".SR_FLOWAPPROVE AS c  \n"
					+ "ON c.FACONO = a.FHCONO \n"
					+ "AND c.FADIVI = a.FHDIVI  \n"
					+ "AND c.FACODE = a.FHCODE  \n"
					+ "AND c.FASRNO = a.FHSRNO  \n"
					+ "AND c.FASTAT = a.FHDSTA \n"
					+ "WHERE a.FHCONO = '" + cono + "' \n"
					+ "AND a.FHDIVI = '" + divi + "' \n"
					+ "AND a.FHCODE = '" + servicename + "' \n"
					+ "AND a.FHSRNO = '" + serviceno + "' \n"
					+ "AND (FHREQU LIKE '%" + username + "%' OR c.FAAPLI LIKE '%" + username + "%')";

			// System.out.println("getUserM3\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			logger.debug(query);

			while (mRes.next()) {
				if (Boolean.valueOf(mRes.getString("CHECKSTATUS")) == true
						&& Boolean.valueOf(mRes.getString("CHECKAUTHEN")) == true)
					return true;
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		return false;

	}

	public static String getServiceFile(String cono, String divi, String servicename, String serviceno)
			throws Exception {
		logger.info("getServiceFile");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT FILINE,FISNAM,FIFNAM,FITYPE,FIREM1 \n"
					+ "FROM " + DBNAME + ".SR_FILE \n"
					+ "WHERE FICONO = '" + cono + "' \n"
					+ "AND FIDIVI  = '" + divi + "' \n"
					+ "AND FICODE = '" + servicename + "' \n"
					+ "AND FISRNO  = '" + serviceno + "' \n"
					+ "ORDER BY FILINE";
			// System.out.println("getBU\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			return ConvertResultSet.convertResultSetToJson(mRes);

		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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

		return null;

	}

	/** The form of a request (SR_FLOWDETAIL) with its head, the emails of the requester / dept head and the company. */
	public static String getServiceForm(String cono, String divi, String servicename, String serviceno)
			throws Exception {
		logger.info("getServiceForm");

		String query = "SELECT FDCONO, FDDIVI, FDCODE, FDSRNO, FDDATA, FDDSTA, FHREQU, FHDEPH, FHHSTA, COALESCE(FHDES1,'') AS FHDES1, COALESCE(FHDES2,'') AS FHDES2, TRIM(CCROW3) AS COMPANY, TRIM(CCCOA4) AS SHORTCOMPANY, COALESCE(rq.ST_EMAIL,'') AS REQUESTER_EMAIL, COALESCE(dh.ST_EMAIL,'') AS DEPTHEAD_EMAIL \n"
				+ "FROM \n"
				+ "(SELECT FDCONO,FDDIVI,FDCODE,FDSRNO,FDDATA,FDDSTA \n"
				+ "FROM " + DBNAME + ".SR_FLOWDETAIL \n"
				+ "WHERE FDCONO = ? AND FDDIVI = ? AND FDCODE = ? AND FDSRNO = ?) AS dt \n"
				+ "LEFT JOIN " + DBNAME + ".SR_FLOWHEAD fh \n"
				+ "ON fh.FHCONO = dt.FDCONO \n"
				+ "AND fh.FHDIVI = dt.FDDIVI \n"
				+ "AND fh.FHCODE = dt.FDCODE \n"
				+ "AND fh.FHSRNO = dt.FDSRNO \n"
				+ "LEFT JOIN " + DBNAME + ".STAFFLIST rq \n"
				+ "ON fh.FHREQU = rq.ST_N6L3 \n"
				+ "LEFT JOIN " + DBNAME + ".STAFFLIST dh \n"
				+ "ON dh.ST_N6L3 = fh.FHDEPH \n"
				+ "LEFT JOIN " + DBM3NAME + ".CMNDIV na \n"
				+ "ON na.CCCONO = fh.FHCONO \n"
				+ "AND na.CCDIVI = fh.FHDIVI";

		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, cono);
			ps.setString(2, divi);
			ps.setString(3, servicename);
			ps.setString(4, serviceno);
			try (ResultSet mRes = ps.executeQuery()) {
				return ConvertResultSet.convertResultSetToJson(mRes);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	public static String getServiceFormHistory(String cono, String divi, String servicename, String serviceno)
			throws Exception {
		logger.info("getServiceHistory");

		// One row per status; a status with SR_FLOWAPPROVESUB rows (approve mode ALL) gives one row per sub
		// group instead: its users, who approved it, when and the remark. The reject of the status (kept on
		// SR_FLOWAPPROVE only) is shown on one row: the first sub group the rejecter (FARJBY) is a user of,
		// else (not found, e.g. the sub groups were built again) the first sub group.
		String sameStatus = "s.FSCONO = sf.FACONO AND s.FSDIVI = sf.FADIVI AND s.FSCODE = sf.FACODE \n"
				+ "AND s.FSSRNO = sf.FASRNO AND s.FSSTAT = sf.FASTAT";
		String subGroupsOfStatus = "FROM " + DBNAME + ".SR_FLOWAPPROVESUB f \n"
				+ "WHERE f.FSCONO = s.FSCONO AND f.FSDIVI = s.FSDIVI AND f.FSCODE = s.FSCODE \n"
				+ "AND f.FSSRNO = s.FSSRNO AND f.FSSTAT = s.FSSTAT";
		String rejectSubGroup = "s.FSSGRO = COALESCE( \n"
				+ "(SELECT MIN(f.FSSGRO) " + subGroupsOfStatus + " \n"
				+ "AND ',' || REPLACE(f.FSAPLI, ' ', '') || ',' LIKE '%,' || TRIM(sf.FARJBY) || ',%'), \n"
				+ "(SELECT MIN(f.FSSGRO) " + subGroupsOfStatus + "))";
		String request = "WHERE sf.FACONO = ? AND sf.FADIVI = ? AND sf.FACODE = ? AND sf.FASRNO = ? \n";
		String query = "SELECT ID, DOC_CODE, DOC_NO, APPROVER, APPROVER_DESC, STATUS, STATUS_DESC, APPROVE_BY \n"
				+ ", APPROVE_DATE, APPROVE_REMARK, REJECT_BY, REJECT_DATE, REJECT_REMARK \n"
				+ "FROM ( \n"
				+ "SELECT COALESCE(CHAR(sf.ID), ''), COALESCE(sf.FACODE, ''), COALESCE(sf.FASRNO, ''), COALESCE(sf.FAAPLI, '') \n"
				+ ", COALESCE(CHAR(sf.FASTAT), '') || ' : ' || COALESCE(CHAR(sf.FADES2), '') \n"
				+ ", COALESCE(CHAR(sf.FASTAT), ''), COALESCE(CHAR(sf.FADES1), ''), COALESCE(sf.FAAPBY, '') \n"
				+ ", COALESCE(CHAR(sf.FAAPDA), '') || ' ' || COALESCE(CHAR(sf.FAAPTI), ''), COALESCE(sf.FADES3, '') \n"
				+ ", COALESCE(sf.FARJBY, ''), COALESCE(CHAR(sf.FARJDA), '') || ' ' || COALESCE(CHAR(sf.FARJTI), '') \n"
				+ ", COALESCE(sf.FADES4, ''), '' \n"
				+ "FROM " + DBNAME + ".SR_FLOWAPPROVE sf \n"
				+ request
				+ "AND NOT EXISTS (SELECT 1 FROM " + DBNAME + ".SR_FLOWAPPROVESUB s WHERE " + sameStatus + ") \n"
				+ "UNION ALL \n"
				+ "SELECT COALESCE(CHAR(sf.ID), ''), COALESCE(sf.FACODE, ''), COALESCE(sf.FASRNO, ''), COALESCE(s.FSAPLI, '') \n"
				+ ", COALESCE(CHAR(sf.FASTAT), '') || ' : ' || COALESCE(s.FSSGRO, '') \n"
				+ ", COALESCE(CHAR(sf.FASTAT), ''), COALESCE(CHAR(sf.FADES1), ''), COALESCE(s.FSAPBY, '') \n"
				+ ", COALESCE(CHAR(s.FSAPDA), '') || ' ' || COALESCE(CHAR(s.FSAPTI), ''), COALESCE(s.FSDES1, '') \n"
				+ ", CASE WHEN " + rejectSubGroup + " THEN COALESCE(sf.FARJBY, '') ELSE '' END \n"
				+ ", CASE WHEN " + rejectSubGroup + " \n"
				+ "THEN COALESCE(CHAR(sf.FARJDA), '') || ' ' || COALESCE(CHAR(sf.FARJTI), '') ELSE '' END \n"
				+ ", CASE WHEN " + rejectSubGroup + " THEN COALESCE(sf.FADES4, '') ELSE '' END \n"
				+ ", s.FSSGRO \n"
				+ "FROM " + DBNAME + ".SR_FLOWAPPROVE sf \n"
				+ "JOIN " + DBNAME + ".SR_FLOWAPPROVESUB s ON " + sameStatus + " \n"
				+ request
				+ ") H (ID, DOC_CODE, DOC_NO, APPROVER, APPROVER_DESC, STATUS, STATUS_DESC, APPROVE_BY \n"
				+ ", APPROVE_DATE, APPROVE_REMARK, REJECT_BY, REJECT_DATE, REJECT_REMARK, SUB_GROUP) \n"
				+ "ORDER BY INT(STATUS), SUB_GROUP";

		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			String[] params = { cono, divi, servicename, serviceno };
			for (int i = 0; i < 8; i++) {
				ps.setString(i + 1, params[i % params.length]);
			}
			try (ResultSet mRes = ps.executeQuery()) {
				return ConvertResultSet.convertResultSetToJson(mRes);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}
}
