package com.br.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.br.connection.ConnectDB2;
import com.br.utility.SqlLog;

/** Updates of the request flow: SR_FLOWHEAD, SR_FLOWDETAIL, SR_FLOWAPPROVE, SR_FILE. */
public class FlowUpdate {

	private static final Logger logger = LogManager.getLogger(FlowUpdate.class);

	protected static String DBNAME = EngineConfig.DBNAME;

	public static String updateServiceHead(String cono, String divi, String servicename, String serviceno,
			String fromstatus, String tostatus, String username, String depthead)
			throws Exception {
		logger.info("updateServiceHead");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWHEAD \n"
					+ "SET FHDEPH = '" + depthead + "' \n"
					+ ", FHDSTA = '" + tostatus + "' \n"
					// + ", FHDES1 = '' \n"
					// + ", FHDES2 = '' \n"
					+ ", FHENDA = CURRENT DATE \n"
					+ ", FHENTI = CURRENT TIME \n"
					+ ", FHENUS = '" + username + "' \n"
					+ "WHERE FHCONO = '" + cono + "' \n"
					+ "AND FHDIVI = '" + divi + "' \n"
					+ "AND FHCODE = '" + servicename + "' \n"
					+ "AND FHSRNO = '" + serviceno + "'\n"
					+ "AND FHDSTA = '" + fromstatus + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	public static String updateServiceUserHead(String cono, String divi, String servicename, String serviceno,
			String status, String username, String depthead)
			throws Exception {
		logger.info("updateServiceUserHead");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWHEAD \n"
					+ "SET FHDEPH = '" + depthead + "' \n"
					+ ", FHENDA = CURRENT DATE \n"
					+ ", FHENTI = CURRENT TIME \n"
					+ ", FHENUS = '" + username + "' \n"
					+ "WHERE FHCONO = '" + cono + "' \n"
					+ "AND FHDIVI = '" + divi + "' \n"
					+ "AND FHCODE = '" + servicename + "' \n"
					+ "AND FHSRNO = '" + serviceno + "'\n"
					+ "AND FHDSTA = '" + status + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	public static String updateServiceHeadStatus(String cono, String divi, String servicename, String serviceno,
			String fromstatus, String tostatus, String username)
			throws Exception {
		logger.info("updateServiceHeadStatus");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWHEAD \n"
					+ "SET FHHSTA = '" + tostatus + "' \n"
					+ ", FHENDA = CURRENT DATE \n"
					+ ", FHENTI = CURRENT TIME \n"
					+ ", FHENUS = '" + username + "' \n"
					+ "WHERE FHCONO = '" + cono + "' \n"
					+ "AND FHDIVI = '" + divi + "' \n"
					+ "AND FHCODE = '" + servicename + "' \n"
					+ "AND FHSRNO = '" + serviceno + "'\n"
					+ "AND FHDSTA = '" + fromstatus + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	/** SR_FLOWHEAD.FHDES2: the reference an Interface M3 answered (e.g. the new M3 code), whatever the status. */
	static void updateServiceHeadReference(String cono, String divi, String servicename, String serviceno,
			String reference, String username) throws Exception {
		String query = "UPDATE " + DBNAME + ".SR_FLOWHEAD \n"
				+ "SET FHDES2 = ?, FHENDA = CURRENT DATE, FHENTI = CURRENT TIME, FHENUS = ? \n"
				+ "WHERE FHCONO = ? AND FHDIVI = ? AND FHCODE = ? AND FHSRNO = ?";
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, reference);
			ps.setString(2, username);
			ps.setString(3, cono);
			ps.setString(4, divi);
			ps.setString(5, servicename);
			ps.setString(6, serviceno);
			ps.executeUpdate();
		}
	}

	// SR_FLOWHEAD.FHDES1 / FHDES2 are VARCHAR(255): a longer name must not make the approve fail
	static final int HEAD_TEXT_LENGTH = 255;
	private static final String HEAD_TEXT = "CAST(? AS VARCHAR(" + HEAD_TEXT_LENGTH + ") CCSID 838)";

	/** FHDES1 (title) and FHDES2 (reference) of the head; an empty one is left as it is, both empty = nothing. */
	static void updateServiceHeadTexts(String cono, String divi, String servicename, String serviceno, String title,
			String reference, String username) throws Exception {
		if (title.isEmpty() && reference.isEmpty()) {
			return;
		}
		// the markers need a CAST: "? = ''" types the marker like '' (length 0), so jt400 refuses any text
		// with Data truncation
		String query = "UPDATE " + DBNAME + ".SR_FLOWHEAD \n"
				+ "SET FHDES1 = CASE WHEN " + HEAD_TEXT + " = '' THEN FHDES1 ELSE " + HEAD_TEXT + " END \n"
				+ ", FHDES2 = CASE WHEN " + HEAD_TEXT + " = '' THEN FHDES2 ELSE " + HEAD_TEXT + " END \n"
				+ ", FHENDA = CURRENT DATE, FHENTI = CURRENT TIME, FHENUS = ? \n"
				+ "WHERE FHCONO = ? AND FHDIVI = ? AND FHCODE = ? AND FHSRNO = ?";
		String shortTitle = cut(title), shortReference = cut(reference);
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, shortTitle);
			ps.setString(2, shortTitle);
			ps.setString(3, shortReference);
			ps.setString(4, shortReference);
			ps.setString(5, username);
			ps.setString(6, cono);
			ps.setString(7, divi);
			ps.setString(8, servicename);
			ps.setString(9, serviceno);
			ps.executeUpdate();
		}
	}

	private static String cut(String text) {
		return text.length() > HEAD_TEXT_LENGTH ? text.substring(0, HEAD_TEXT_LENGTH) : text;
	}

	public static String updateServiceHeadTitle(String cono, String divi, String servicename, String serviceno,
			String status, String title, String username)
			throws Exception {
		logger.info("updateServiceHeadTitle");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWHEAD \n"
					+ "SET FHDES1 = '" + title + "' \n"
					+ ", FHENDA = CURRENT DATE \n"
					+ ", FHENTI = CURRENT TIME \n"
					+ ", FHENUS = '" + username + "' \n"
					+ "WHERE FHCONO = '" + cono + "' \n"
					+ "AND FHDIVI = '" + divi + "' \n"
					+ "AND FHCODE = '" + servicename + "' \n"
					+ "AND FHSRNO = '" + serviceno + "'\n"
					+ "AND FHDSTA = '" + status + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	public static String updateServiceDetail(String cono, String divi, String servicename, String serviceno,
			String fromstatus, String tostatus, String data, String username)
			throws Exception {
		logger.info("updateServiceDetail");

		JSONObject mJsonObj = new JSONObject();
		// prepared: the form data is free text, a ' in it (e.g. a reply "user's") broke the concatenated SQL
		String query = "UPDATE " + DBNAME + ".SR_FLOWDETAIL \n"
				+ "SET FDDATA = ? \n"
				+ ", FDDSTA = ? \n"
				+ ", FDENDA = CURRENT DATE \n"
				+ ", FDENTI = CURRENT TIME \n"
				+ ", FDENUS = ? \n"
				+ "WHERE FDCONO = ? \n"
				+ "AND FDDIVI = ? \n"
				+ "AND FDCODE = ? \n"
				+ "AND FDSRNO = ?";
		try (Connection conn = ConnectDB2.doConnect(); PreparedStatement ps = SqlLog.prepare(conn, query)) {
			ps.setString(1, data);
			ps.setString(2, tostatus);
			ps.setString(3, username);
			ps.setString(4, cono);
			ps.setString(5, divi);
			ps.setString(6, servicename);
			ps.setString(7, serviceno);
			ps.executeUpdate();

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}

	}

	public static String updateServiceFlowApproveBy(String cono, String divi, String servicename, String serviceno,
			String fromstatus, String remark, String username)
			throws Exception {
		logger.info("updateServiceFlowApproveBy");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n"
					+ "SET FAAPBY = '" + username + "' \n"
					+ ", FADES3 = '" + remark + "' \n"
					+ ", FAAPDA = CURRENT DATE \n"
					+ ", FAAPTI = CURRENT TIME \n"
					+ ", FAENDA = CURRENT DATE \n"
					+ ", FAENTI = CURRENT TIME \n"
					+ ", FAENUS = '" + username + "' \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "'\n"
					+ "AND FASTAT = '" + fromstatus + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	public static String updateServiceFlowReject(String cono, String divi, String servicename, String serviceno,
			String fromstatus, String tostatus, String remark, String username)
			throws Exception {
		logger.info("updateServiceFlowReject");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n"
					+ "SET FAAPBY = '' \n"
					+ ", FAAPDA = NULL \n"
					+ ", FAAPTI = NULL \n"
					+ ", FAENDA = CURRENT DATE \n"
					+ ", FAENTI = CURRENT TIME \n"
					+ ", FAENUS = '" + username + "' \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "' \n"
					// numeric compare: as strings '90' > '100', so a 3-digit status would reset nothing
					+ "AND INT(FASTAT) BETWEEN INT('" + fromstatus + "') AND INT('" + tostatus + "')";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	public static String updateServiceFlowRejectBy(String cono, String divi, String servicename, String serviceno,
			String fromstatus, String remark, String username)
			throws Exception {
		logger.info("updateServiceFlowRejectBy");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n"
					+ "SET FARJBY = '" + username + "' \n"
					+ ", FADES4 = '" + remark + "' \n"
					+ ", FARJDA = CURRENT DATE \n"
					+ ", FARJTI = CURRENT TIME \n"
					+ ", FAENDA = CURRENT DATE \n"
					+ ", FAENTI = CURRENT TIME \n"
					+ ", FAENUS = '" + username + "' \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "' \n"
					+ "AND FASTAT = '" + fromstatus + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	public static String updateServiceUserFlowApprove(String cono, String divi, String servicename, String serviceno,
			String status, String approveby, String username)
			throws Exception {
		logger.info("updateServiceUserFlowApprove");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n"
					+ "SET FAAPLI = '" + username + "' \n"
					+ ", FAENDA = CURRENT DATE \n"
					+ ", FAENTI = CURRENT TIME \n"
					+ ", FAENUS = '" + approveby + "' \n"
					+ "WHERE FACONO = '" + cono + "' \n"
					+ "AND FADIVI = '" + divi + "' \n"
					+ "AND FACODE = '" + servicename + "' \n"
					+ "AND FASRNO = '" + serviceno + "'\n"
					+ "AND FASTAT = '" + status + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}

	public static String updateServiceFile(String cono, String divi, String servicename, String serviceno,
			String fieldname, String originalFileName, String filetype, String newFileName, String username)
			throws Exception {
		logger.info("updateServiceFile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + DBNAME + ".SR_FILE \n"
					+ "SET FIFNAM = '" + originalFileName + "' \n"
					+ ", FITYPE = '" + filetype + "' \n"
					+ ", FIREM1 = '" + newFileName + "' \n"
					+ ", FIENDA = CURRENT DATE \n"
					+ ", FIENTI = CURRENT TIME \n"
					+ ", FIENUS = '" + username + "' \n"
					+ "WHERE FICONO = '" + cono + "' \n"
					+ "AND FIDIVI = '" + divi + "' \n"
					+ "AND FICODE = '" + servicename + "' \n"
					+ "AND FISRNO = '" + serviceno + "' \n"
					+ "AND FISNAM = '" + fieldname + "'";
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
			return mJsonObj.toString();

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

	}
}
