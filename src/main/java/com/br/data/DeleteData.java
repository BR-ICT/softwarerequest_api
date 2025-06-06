package com.br.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.br.connection.ConnectDB2;
import com.br.utility.Constant;
import com.br.utility.FileUtillity;

public class DeleteData {

	private static final Logger logger = LogManager.getLogger(DeleteData.class);

	protected static String DBNAME = Constant.DBNAME;
	protected static String DBM3NAME = Constant.DBM3NAME;

	
	
	
	
	public static String deleteSWRFile(String cono, String divi, String SFORNO)
			throws Exception {
		logger.info("deleteSWRFile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "DELETE BRLDTABK01.M3_MARDETAIL \r\n"
					+ "					WHERE SFCONO = '"+cono+"' \r\n"
					+ "					AND  SFDIVI= '"+101+"'\r\n"
					+ "				    AND  SFORNO = '"+SFORNO+"' ";
			// System.out.println("deleteMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Delete complete.");
			logger.info("Delete complete.");
			return mJsonObj.toString();

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
	
	
	public static String deleteMARDetail(String cono, String divi, String marno, String line)
			throws Exception {
		logger.info("deleteMARDetail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "DELETE " + DBNAME + ".M3_MARDETAIL \n"
					+ "WHERE MLCONO = '" + cono + "' \n"
					+ "AND MLDIVI = '" + divi + "' \n"
					+ "AND MLPREF || '-' || MLORNO = '" + marno + "' \n"
					+ "AND MLLINE = '" + line + "'";
			// System.out.println("deleteMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Delete complete.");
			logger.info("Delete complete.");
			return mJsonObj.toString();

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

	public static String deleteMARFile(HttpServletRequest httpServletRequest,String cono, String divi, String marno, String line, String name)
			throws Exception {
		logger.info("deleteMARFile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "DELETE " + DBNAME + ".M3_MARFILE \n"
					+ "WHERE MFCONO = '" + cono + "' \n"
					+ "AND MFDIVI = '" + divi + "' \n"
					+ "AND MFPREF || '-' || MFORNO = '" + marno + "' \n"
					+ "AND MFLINE = '" + line + "'";
			// System.out.println("deleteMARFile\n" + query);
			logger.debug(query);
			stmt.execute(query);
			
			String path = marno + "_" + line + "_" + name;

			FileUtillity.deleteFileV4(httpServletRequest, path);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Delete complete.");
			logger.info("Delete complete.");
			return mJsonObj.toString();

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

	public static String deleteADRDetail(String cono, String divi, String adrno, String line) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "DELETE BRLDTA0100.M3_ADRDETAIL \n"
						+ "WHERE ALCONO = '" + cono + "' \n"
						+ "AND ALDIVI = '" + divi + "' \n"
						+ "AND ALLINE = '" + line + "' \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "'";
				// System.out.println("deleteADRDetail\n" + query);
				stmt.execute(query);

				String setImageName = adrno + "_" + line + ".png";
				String filePath = "D:\\files\\api_project\\adr_images\\";
				// System.out.println("filePath: " + filePath + setImageName);
				FileUtillity.deleteFileServer(filePath);

				// mJsonObj.put("getPRNumber", getPRNumber);
				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;

	}

	public static String deleteItemDetail(String cono, String divi, String adrno, String line) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "DELETE " + DBNAME + ".M3_TAKEORDERDETAIL \n" +
						"WHERE TDCONO = '" + cono + "' \n" +
						"AND TDDIVI = '" + divi + "' \n" +
						"AND TDORNO = '" + adrno + "' \n" +
						"AND TDLINE = '" + line + "'";
				// System.out.println("deleteOrderLineDetail\n" + query);
				stmt.execute(query);

				String setImageName = adrno + "_" + line + ".png";
				String filePath = "D:\\files\\api_project\\adr_images\\";
				// System.out.println("filePath: " + filePath + setImageName);
				FileUtillity.deleteFileServer(filePath);

				// mJsonObj.put("getPRNumber", getPRNumber);
				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;

	}

	public static String deleteItemDetailSupport(String cono, String divi, String orderno, String line)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "DELETE " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" +
						"WHERE TDCONO = '" + cono + "' \n" +
						"AND TDDIVI = '" + divi + "' \n" +
						"AND TDORNO = '" + orderno + "' \n" +
						"AND TDLINE = '" + line + "'";
				// System.out.println("deleteOrderLineDetail\n" + query);
				stmt.execute(query);

				// mJsonObj.put("getPRNumber", getPRNumber);
				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;

	}

	public static String deleteUser(String cono, String divi, String salesup, String saleman) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "DELETE BRLDTA0100.SAL_SUPSALE \n" +
						"WHERE S_CONO = '" + cono + "' \n" +
						"AND S_SUPP = '" + salesup + "' \n" +
						"AND S_SALE = '" + saleman + "'";
				// System.out.println("deleteAuthUser\n" + query);
				stmt.execute(query);

				// mJsonObj.put("getPRNumber", getPRNumber);
				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;

	}

}
