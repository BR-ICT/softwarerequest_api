package com.br.data;

import java.sql.Connection;
import java.time.LocalDate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.connection.ConnectDB2;
import com.br.utility.Constant;
import com.br.utility.ConvertResultSet;

public class SelectData {

	private static final Logger logger = LogManager.getLogger(SelectData.class);

	protected static String DBNAME = Constant.DBNAME;
	protected static String DBM3NAME = Constant.DBM3NAME;
	
	  public static String SR_HEAD =  Constant.SR_HEAD;
	  public static String SR_DETAIL = Constant.SR_DETAIL; 
	  public static String SR_APPROVE = Constant.SR_APPROVE; 
	  public static String SR_FLOW = Constant.SR_FLOW; 
	  public static String SR_GROUP = Constant.SR_GROUP; 
	

	// protected static String DBNAMEPP = ""+Constant.DBNAME+"";
	protected static String DBNAMEPP = "" + DBNAME + "";

	public static String getstaffcode(String username, String lastname) throws Exception {
		logger.info("getCompany");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ST_CODE FROM " + Constant.DBNAME + ".STAFFLIST s \r\n" + "WHERE  ST_ENAME = '"
					+ username.trim()
					+ "' \r\n" + "AND ST_ELNAME = '" + lastname.trim() + "'";
			// System.out.println("SelectCompany\n" + query);
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

	public static String getimage(@Context HttpServletRequest httpServletRequest, String vID)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT * FROM " + Constant.DBNAME + ".VISITOR_HEAD vh \r\n"
						+ "WHERE H_ID = '" + vID.trim() + "'";
				System.out.println("getimage\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				// return ConvertResultSet.convertResultSetToJson(mRes);
				return ConvertResultSet.convertResultSetToJsonVPP(mRes);

			} else {
				System.out.println("Server can't connect.");
			}

		} catch (SQLException e) {
			throw e;
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

	///////////////////////////////// BANKMAPPING/////////////////////////////

	// String DBNAMEe =""+Constant.DBNAME+"" ;

	static String DBNAMEe = "" + Constant.DBNAME + "";

	public static String getHistory(String ID) {
		logger.info("getHistory");

		Connection conn = null;
		Statement stmt = null;
		String jsonResult = "[]"; // default เป็น array ว่าง

		int year = LocalDate.now().getYear();
		String lastTwoDigits = String.valueOf(year).substring(2);

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT  \r\n"
					+ "COALESCE(ID, '-') AS ID,\r\n"
					+ "COALESCE(DOC_CODE, '-') AS DOC_CODE,\r\n"
					+ "COALESCE(DOC_NO, '-') AS DOC_NO,\r\n"
					+ "COALESCE(APPROVE, '-') AS APPROVE,\r\n"
					+ "COALESCE(APPROVED_USER, '-') AS APPROVED_USER,\r\n"
					+ "COALESCE(CHAR(APPROVE_DATE), '-') AS APPROVE_DATE,\r\n"
					+ "COALESCE(CHAR(STATUS), '-') AS STATUS,\r\n"
					+ "COALESCE(REMARK, '-') AS REMARKNAME,\r\n"
					+ "COALESCE(STS_DESC, '-') AS STS_DESC,\r\n"
					+ "COALESCE(TIME_ST, '-') AS TIME_ST,\r\n"
					+ "COALESCE(SR_COMMENT, '-') AS REMARK\r\n"
					+ "FROM "+DBNAME+"."+SR_APPROVE+" WHERE DOC_NO = '" + ID + "'\r\n"
					+ "";

			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);
			jsonResult = ConvertResultSet.convertResultSetToJson(mRes);

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
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

		return jsonResult;
	}

	public static String getivoiceid(String cono, String divi) throws Exception {
		logger.info("getWarehouse");

		Connection conn = null;
		Statement stmt = null;

		int year = LocalDate.now().getYear();
		String lastTwoDigits = String.valueOf(year).substring(2);
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			/*
			 * String query = "SELECT * FROM "+Constant.DBNAME+".HR_RECEIPT hr \r\n"
			 * + "				WHERE  HR_CONO  = '10'\r\n"
			 * + "				AND HC_PYDT = 0 \r\n"
			 * + "				AND HC_STS  NOT IN (3,9) \r\n"
			 * + "				AND HC_FNNO IS NOT NULL \r\n"
			 * + "				AND HC_RCNO LIKE '"+lastTwoDigits+"%'";
			 * // System.out.println("getWarehouse\n" + query);
			 * 
			 */

			String query = "SELECT *  \r\n"
					+ "FROM " + DBNAMEe + ".HR_RECEIPT hr \r\n"
					+ "WHERE (HC_BANK = 'INVOICE' OR HC_BANK = 'NONE' )\r\n"
					+ "  AND HC_PYDT = '0'\r\n"
					+ "  AND HC_STS != 9\r\n"
					+ "  AND HC_FIXNO IS NOT NULL\r\n"
					+ "  AND (HC_FNNO IS NULL OR HC_FNNO NOT IN (\r\n"
					+ "      SELECT BM_FNNO \r\n"
					+ "      FROM " + DBNAME + ".BANK_MAPPING\r\n"
					+ "      WHERE BM_FNNO IS NOT NULL AND  BM_STATUS = '10' \r\n"
					+ "  ))\r\n"
					+ " AND ( HC_RCNO LIKE '" + lastTwoDigits
					+ "%'  OR   HC_RCNO LIKE '24%'  OR   HC_RCNO LIKE '25%')  ";
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

	public static String getpayer(String cono, String divi) throws Exception {
		logger.info("getWarehouse");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = " SELECT TRIM(OKCUNM)as OKCUNM ,TRIM(OKPYNO) as OKPYNO  , TRIM(OKCUNO) as OKCUNO , TRIM(OKCUNO) || ':' || TRIM(OKCUNM) as OKSHOW \r\n"
					+ " 						FROM  " + DBM3NAME + ".OCUSMA o \r\n"
					+ "                       WHERE OKCONO = '" + cono + "' \r\n"
					+ "                       AND  OKSTAT = '20' \r\n"
					+ "                       AND    OKPYNO = OKCUNO \r\n"
					+ "                       AND    OKPYNO LIKE 'TH%' \r\n"
					+ "                       OR   OKCONO = '" + cono + "' \r\n"
					+ "                       AND  OKPYNO LIKE 'TH%' \r\n"
					+ "                       AND  OKSTAT = '20' \r\n"
					+ "                       AND OKPYNO = '' AND OKCUNO != ''"
					+ "                       OR   OKCONO = '" + cono + "' \r\n"
					+ "                       AND  OKCUNO LIKE 'TH%' \r\n"
					+ "                       AND  OKSTAT = '20' \r\n"
					+ "                       AND OKPYNO = '' AND OKCUNO != ''";
			// System.out.println("getWarehouse\n" + query);
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

	public static String getusermas(String username)
			throws Exception {

		logger.info("GETUSER");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT 'OK' AS result\r\n"
					+ "FROM " + Constant.DBNAME + ".APPCTL1\r\n"
					+ "WHERE CTL_CODE = 'MAS'\r\n"
					+ "AND CTL_UID = '" + username + "'\r\n"
					+ "FETCH FIRST 1 ROW ONLY";
			System.out.println("GETUSER\n" + query);
			logger.debug(query);

			System.out.print(getListData);

			// return ConvertResultSet.convertResultSetToJsonVPP(mRes);

			try (ResultSet mRes = stmt.executeQuery(query)) {
				if (mRes.next()) { // ตรวจสอบว่ามีผลลัพธ์หรือไม่
					String result = mRes.getString("result"); // อ่านค่าจากคอลัมน์ "result"
					return result; // คืนค่าออกไป
				} else {
					return null; // ไม่มีข้อมูลใน ResultSet
				}
			} catch (SQLException e) {
				logger.error("Error executing query: ", e);
				throw e; // หรือจัดการ error ตามต้องการ
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

	///////////////////////////////////////// REAL WF

	public static String getSTATUSIDITEMRQ(String vID)
			throws Exception {

		logger.info("getSTATUSIDITEMRQ");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT JSON_DATA,STATUS, SERVICE_ID \n"
					+ "FROM  "+DBNAME+"."+SR_DETAIL+"\r\n"
					+ "WHERE  SERVICE_ID = '" + vID + "' \n"
					+ "AND PROMGRAM_CODE = 'ITMRQ'";

			logger.debug(query);
			logger.debug(getListData);

			ResultSet mRes = stmt.executeQuery(query);

			return ConvertResultSet.convertResultSetToJson(mRes);

			// return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	///////////////////////////////////////// REAL WF

	/*
	 * 
	 * 
	 * public static String getSTATUSID(String vID)
	 * throws Exception {
	 * 
	 * 
	 * logger.info("getSTATUSID");
	 * 
	 * List<String> getListData = new ArrayList<String>();
	 * 
	 * Connection conn = null;
	 * Statement stmt = null;
	 * try {
	 * conn = ConnectDB2.doConnect();
	 * stmt = conn.createStatement();
	 * 
	 * String query =
	 * "SELECT H_COMPANYNAME AS COM ,H_LICENSE AS WH,H_NAME AS ITM ,H_REASON AS APV ,H_TIMEOUT APPROVER,H_EMPLOYEE AS CMMAPV,H_SURNAME AS ITAPPROVER,H_ROOMNO AS ITCMMAPV,  H_TEL  FROM  BRLDTABK01.VISITOR_HEAD  \r\n"
	 * + "					WHERE  H_ID  = '"+vID+"'";
	 * System.out.println("getSTATUSID" + query);
	 * logger.debug(query);
	 * 
	 * 
	 * System.out.print(getListData);
	 * 
	 * ResultSet mRes = stmt.executeQuery(query);
	 * 
	 * return ConvertResultSet.convertResultSetToJson(mRes);
	 * 
	 * //return ConvertResultSet.convertResultSetToJsonVPP(mRes);
	 * 
	 * 
	 * 
	 * 
	 * } catch (SQLException e) {
	 * logger.error(e.getMessage());
	 * } catch (Exception e) {
	 * logger.error(e.getMessage());
	 * } finally {
	 * try {
	 * if (stmt != null) {
	 * stmt.close();
	 * }
	 * } catch (SQLException e) {
	 * logger.error(e.getMessage());
	 * }
	 * try {
	 * if (conn != null) {
	 * conn.close();
	 * }
	 * } catch (SQLException e) {
	 * logger.error(e.getMessage());
	 * }
	 * 
	 * }
	 * 
	 * return null;
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * public static String getcurrentID()
	 * throws Exception {
	 * 
	 * 
	 * logger.info("GETUSER");
	 * 
	 * List<String> getListData = new ArrayList<String>();
	 * 
	 * Connection conn = null;
	 * Statement stmt = null;
	 * try {
	 * conn = ConnectDB2.doConnect();
	 * stmt = conn.createStatement();
	 * 
	 * String query = "SELECT \r\n"
	 * +
	 * "  '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(H_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID\r\n"
	 * + "FROM \r\n"
	 * + "  BRLDTABK01.VISITOR_HEAD\r\n"
	 * + "WHERE \r\n"
	 * + "  SUBSTR(H_ID, 1, 2) = '25' ";
	 * System.out.println("GETID\n" + query);
	 * logger.debug(query);
	 * 
	 * 
	 * System.out.print(getListData);
	 * 
	 * //return ConvertResultSet.convertResultSetToJsonVPP(mRes);
	 * 
	 * try (ResultSet mRes = stmt.executeQuery(query)) {
	 * if (mRes.next()) { // ตรวจสอบว่ามีผลลัพธ์หรือไม่
	 * String result = mRes.getString("CURRENT_ID"); // อ่านค่าจากคอลัมน์ "result"
	 * return result; // คืนค่าออกไป
	 * } else {
	 * return null; // ไม่มีข้อมูลใน ResultSet
	 * }
	 * } catch (SQLException e) {
	 * logger.error("Error executing query: ", e);
	 * throw e; // หรือจัดการ error ตามต้องการ
	 * }
	 * 
	 * 
	 * } catch (SQLException e) {
	 * logger.error(e.getMessage());
	 * } catch (Exception e) {
	 * logger.error(e.getMessage());
	 * } finally {
	 * try {
	 * if (stmt != null) {
	 * stmt.close();
	 * }
	 * } catch (SQLException e) {
	 * logger.error(e.getMessage());
	 * }
	 * try {
	 * if (conn != null) {
	 * conn.close();
	 * }
	 * } catch (SQLException e) {
	 * logger.error(e.getMessage());
	 * }
	 * 
	 * }
	 * 
	 * return null;
	 * 
	 * 
	 * 
	 * }
	 * 
	 */

	public static String getuserdel(String username)
			throws Exception {

		logger.info("GETUSER");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT 'OK' AS result\r\n"
					+ "FROM " + Constant.DBNAME + ".APPCTL1\r\n"
					+ "WHERE CTL_CODE = 'DEL'\r\n"
					+ "AND CTL_UID = '" + username + "'\r\n"
					+ "FETCH FIRST 1 ROW ONLY";
			System.out.println("GETUSER\n" + query);
			logger.debug(query);

			System.out.print(getListData);

			// return ConvertResultSet.convertResultSetToJsonVPP(mRes);

			try (ResultSet mRes = stmt.executeQuery(query)) {
				if (mRes.next()) { // ตรวจสอบว่ามีผลลัพธ์หรือไม่
					String result = mRes.getString("result"); // อ่านค่าจากคอลัมน์ "result"
					return result; // คืนค่าออกไป
				} else {
					return null; // ไม่มีข้อมูลใน ResultSet
				}
			} catch (SQLException e) {
				logger.error("Error executing query: ", e);
				throw e; // หรือจัดการ error ตามต้องการ
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

	public static String getuser(String username)
			throws Exception {

		logger.info("GETUSER");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT 'OK' AS result\r\n"
					+ "FROM " + Constant.DBNAME + ".APPCTL1\r\n"
					+ "WHERE CTL_CODE = 'BNK'\r\n"
					+ "AND CTL_UID = '" + username + "'\r\n"
					+ "FETCH FIRST 1 ROW ONLY";
			System.out.println("GETUSER\n" + query);
			logger.debug(query);

			System.out.print(getListData);

			// return ConvertResultSet.convertResultSetToJsonVPP(mRes);

			try (ResultSet mRes = stmt.executeQuery(query)) {
				if (mRes.next()) { // ตรวจสอบว่ามีผลลัพธ์หรือไม่
					String result = mRes.getString("result"); // อ่านค่าจากคอลัมน์ "result"
					return result; // คืนค่าออกไป
				} else {
					return null; // ไม่มีข้อมูลใน ResultSet
				}
			} catch (SQLException e) {
				logger.error("Error executing query: ", e);
				throw e; // หรือจัดการ error ตามต้องการ
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

	//////////////// master //////////////////

	public static String GETITEMBANKMAPPING(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT BM_ID, BM_CONO ,BM_DIVI,\r\n"
					+ "COALESCE(BM_BANK,'-') AS BM_BANK,\r\n"
					+ "COALESCE(BM_ACCODE,'-') AS BM_ACCODE,\r\n"
					+ "COALESCE(BM_DATE,'-') AS BM_DATE,\r\n"
					+ "COALESCE(BM_TIME,'-') AS BM_TIME,\r\n"
					+ "COALESCE(BM_CHQNO,'-') AS BM_CHQNO,\r\n"
					+ "COALESCE(BM_CUNO,'-') AS BM_CUNO,\r\n"
					+ "COALESCE(BM_AMOUNT,'-') AS BM_AMOUNT,\r\n"
					+ "COALESCE(BM_DESC,'-') AS BM_DESC,\r\n"
					+ "COALESCE(BM_RCNO,'-') AS BM_RCNO,\r\n"
					+ "COALESCE(BM_USER,'-') AS BM_USER,\r\n"
					+ "COALESCE(BM_BKCHARGE,'-') AS BM_BKCHARGE,\r\n"
					+ "COALESCE(BM_OVPAY,'-') AS BM_OVPAY,\r\n"
					+ "COALESCE(BM_CNDN,'-') AS BM_CNDN,\r\n"
					+ "COALESCE(BM_STATUS,'-') AS BM_STATUS,\r\n"
					+ "COALESCE(BM_FNNO,'-') AS BM_FNNO,\r\n"
					+ "COALESCE(BM_LCODE,'-') AS BM_LCODE,\r\n"
					+ "COALESCE(BM_PARENT_ID,'-') AS BM_PARENT_ID,\r\n"
					+ "COALESCE(BM_PARENT_STS,'-') AS BM_PARENT_STS\r\n"
					+ "FROM " + Constant.DBNAME + ".BANK_MAPPING bm \r\n"
					+ "WHERE BM_DATE  = '" + statemenDate + "'\r\n"
					+ "AND BM_BANK  = '" + statemenType + "'\r\n"
					+ "AND BM_CONO  = '10'";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMHEAD(@Context HttpServletRequest httpServletRequest, String selectedID)
			throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT  \r\n"
					+ "COALESCE(H_CONO,'-') AS  H_CONO,\r\n"
					+ "COALESCE(H_DIVI,'-') AS  H_DIVI,\r\n"
					+ "COALESCE(H_RCNO,'-') AS  H_RCNO,\r\n"
					+ "COALESCE(H_RNNO,'-') AS  H_RNNO,\r\n"
					+ "COALESCE(H_CUNO,'-') AS  H_CUNO,\r\n"
					+ "COALESCE(H_PYNO,'-') AS  H_PYNO,\r\n"
					+ "COALESCE(H_STS,'-') AS  H_STS,\r\n"
					+ "COALESCE(H_VCNO,'-') AS  H_VCNO,\r\n"
					+ "COALESCE(H_LOCATION,'-') AS  H_LOCATION,\r\n"
					+ "COALESCE(H_TYPE,'-') AS H_TYPE\r\n"
					+ "FROM " + Constant.DBNAME + ".HEAD_RECIPT hr \r\n"
					+ "WHERE H_CONO  ='10' \r\n"
					+ "AND H_RNNO  = '" + selectedID + "'\r\n"
					+ "";

			System.out.println("GETITEMHEAD\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMLRC(@Context HttpServletRequest httpServletRequest, String selectedLRC)
			throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT  \r\n"
					+ "COALESCE(LR_CONO,'-') AS LR_CONO,\r\n"
					+ "COALESCE(LR_DIVI,'-') AS LR_DIVI,\r\n"
					+ "COALESCE(LR_RCNO,'-') AS LR_RCNO,\r\n"
					+ "COALESCE(LR_INVNO,'-') AS LR_INVNO,\r\n"
					+ "COALESCE(LR_INVDT,'-') AS LR_INVDT,\r\n"
					+ "COALESCE(LR_INVAMT,'-') AS LR_INVAMT,\r\n"
					+ "COALESCE(LR_REAMT,'-') AS LR_REAMT,\r\n"
					+ "COALESCE(LR_STS,'-') AS LR_STS \r\n"
					+ "FROM " + Constant.DBNAME + ".LR_LINERECEIPT ll \r\n"
					+ "WHERE LR_RCNO  = '" + selectedLRC + "'\r\n"
					+ "AND LR_CONO  ='10'"
					+ "";

			System.out.println("GETITEMRC\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMRC(@Context HttpServletRequest httpServletRequest, String selectedRC) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT HR_CONO, HR_DIVI,\r\n"
					+ "COALESCE(HC_RCNO,'-') AS HC_RCNO,\r\n"
					+ "COALESCE(HC_TRDT,'-') AS HC_TRDT,\r\n"
					+ "COALESCE(HC_PYNO,'-') AS HC_PYNO,\r\n"
					+ "COALESCE(HC_REAMT,'-') AS HC_REAMT,\r\n"
					+ "COALESCE(HC_TYPE,'-') AS HC_TYPE,\r\n"
					+ "COALESCE(HC_BANK,'-') AS HC_BANK,\r\n"
					+ "COALESCE(HC_ACCODE,'-') AS HC_ACCODE,\r\n"
					+ "COALESCE(HC_PYDT,'-') AS HC_PYDT,\r\n"
					+ "COALESCE(HC_CHKNO,'-') AS HC_CHKNO,\r\n"
					+ "COALESCE(HC_USER,'-') AS HC_USER,\r\n"
					+ "COALESCE(HC_VCNO,'-') AS HC_VCNO,\r\n"
					+ "COALESCE(HC_STS,'-') AS HC_STS,\r\n"
					+ "COALESCE(HR_BKCHG,'0') AS HR_BKCHG,\r\n"
					+ "COALESCE(HC_LOCATION,'-') AS HC_LOCATION,\r\n"
					+ "COALESCE(HC_FIXNO,'-') AS HC_FIXNO,\r\n"
					+ "COALESCE(HC_FNNO,'-') AS HC_FNNO\r\n"
					+ "FROM " + Constant.DBNAME + ".HR_RECEIPT hr \r\n"
					+ "WHERE  HR_CONO  = '10'\r\n"
					+ "AND HC_RCNO  = '" + selectedRC + "'"
					+ "";

			System.out.println("GETITEMRC\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	/////////////////////////////////////////

	public static String CHECKTYPEGETITEM(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType, String getCono, String getDivi)
			throws Exception {
		logger.info("CHECKTYPE");

		System.out.println("---------------------------------------------------------------------xx");
		System.out.println("statemenType " + statemenType);
		System.out.println("getCono " + getCono);
		System.out.println("getDivi " + getDivi);

		System.out.println("---------------------------------------------------------------------xx");

		/*
		 * SCB
		 * KBANK
		 * BBL
		 * SCB_BILL
		 * KBANK_BILL
		 * BBL_BILL
		 * SCB_MMN
		 * SCB_CUR
		 * KBANK_CUR
		 * BBL_CUR
		 * BBL_QR
		 */
		if (statemenType.equalsIgnoreCase("SCB_BILL")) {

			System.out.println("SCB_BILL");

			return SelectData.GETITEMSCB_BILL(httpServletRequest, statemenDate, statemenType);

		} else if (statemenType.equalsIgnoreCase("KBANK_BILL")) {

			System.out.println("KBANK_BILL");

			return SelectData.GETITEMKBANK_BILL(httpServletRequest, statemenDate, statemenType);

		} else if (statemenType.equalsIgnoreCase("BBL_QR")) {

			System.out.println("BBL_QR");

			return SelectData.GETITEMBBL_QR(httpServletRequest, statemenDate, statemenType);

		}

		// ----------------- WTF -------------------

		else if (statemenType.equalsIgnoreCase("SCB_WTF")) {

			System.out.println("SCB_WTF");

			return SelectData.GETITEMSCB_WTF(httpServletRequest, statemenDate, statemenType);

		}

		else if (statemenType.equalsIgnoreCase("BBL_WTF")) {

			System.out.println("BBL_WTF");

			return SelectData.GETITEMBBL_WTF(httpServletRequest, statemenDate, statemenType);

		}

		// -----------------NSD---------------------

		else if (statemenType.equalsIgnoreCase("SCB_NSD")) {

			System.out.println("SCB_NSD");

			return SelectData.GETITEMSCB_NSD(httpServletRequest, statemenDate, statemenType);

		}

		// -----------------------------------------
		else {
			return SelectData.GETITEMKBANK_CUR(httpServletRequest, statemenDate, statemenType);

		}

		/*
		 * 
		 * if( statemenType.equalsIgnoreCase("SCB")) {
		 * 
		 * System.out.println("SCB");
		 * 
		 * return SelectData.GETITEMSCB(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("KBANK")) {
		 * 
		 * System.out.println("KBANK");
		 * 
		 * return SelectData.GETITEMKBANK(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("BBL")) {
		 * 
		 * System.out.println("BBL");
		 * 
		 * return SelectData.GETITEMBBL(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("SCB_BILL")) {
		 * 
		 * System.out.println("SCB_BILL");
		 * 
		 * return
		 * SelectData.GETITEMSCB_BILL(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("KBANK_BILL")) {
		 * 
		 * System.out.println("KBANK_BILL");
		 * 
		 * return
		 * SelectData.GETITEMKBANK_BILL(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("BBL_BILL")) {
		 * 
		 * System.out.println("BBL_BILL");
		 * 
		 * return
		 * SelectData.GETITEMBBL_BILL(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("SCB_MMN")) {
		 * 
		 * System.out.println("SCB_MMN");
		 * 
		 * return
		 * SelectData.GETITEMSCB_MMN(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("SCB_CUR")) {
		 * 
		 * System.out.println("SCB_CUR");
		 * 
		 * return
		 * SelectData.GETITEMSCB_CUR(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("KBANK_CUR")) {
		 * 
		 * System.out.println("KBANK_CUR");
		 * 
		 * return
		 * SelectData.GETITEMKBANK_CUR(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("BBL_CUR")) {
		 * 
		 * System.out.println("BBL_CUR");
		 * 
		 * return
		 * SelectData.GETITEMBBL_CUR(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else if( statemenType.equalsIgnoreCase("BBL_QR")) {
		 * 
		 * System.out.println("BBL_QR");
		 * 
		 * return
		 * SelectData.GETITEMBBL_QR(httpServletRequest,statemenDate,statemenType);
		 * 
		 * 
		 * }
		 * else {
		 * System.out.println("NONONONONO");
		 * 
		 * 
		 * }
		 * 
		 */

		// return "null";

	}

	public static String GETITEMSCB(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '3312431686' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'SCB'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99' \r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER') AS　HCTYPE,COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10'\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMKBANK(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '3402314428' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'KBANK'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99' \r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10'\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMBBL(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '1227360581' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'BBL'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99' \r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10'\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMSCB_BILL(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		String BNKCODE = "0";

		switch (statemenType) {

			case "SCB":
				BNKCODE = "3312431686";
				break;

			case "KBANK":
				BNKCODE = "3402314428";
				break;

			case "BBL":
				BNKCODE = "1227360581";
				break;

			case "SCB_BILL":
				BNKCODE = "3313019398";
				break;

			case "KBANK_BILL":
				BNKCODE = "3401018025";
				break;

			case "BBL_BILL":
				BNKCODE = "1223098219";
				break;
			case "SCB_CUR":
				BNKCODE = "3313019322";
				break;
			case "KBANK_CUR":
				BNKCODE = "3401018025";
				break;
			case "BBL_CUR":
				BNKCODE = "1223098219";
				break;

			case "SCB_MMN":
				BNKCODE = "3313019322";
				break;

			case "BBL_QR":
				BNKCODE = "1223098219";
				break;

		}

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT COALESCE(BM_CUNA,'-') AS CusName, BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '"
					+ BNKCODE
					+ "' AS AccNo , BM_REFCU1 , BM_DESC AS BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = '" + statemenType + "'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT   HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10' AND HC_PYDT = '" + statemenDate + "'\r\n"
					+ "\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER     ORDER BY ID  ASC";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMKBANK_BILL(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		String BNKCODE = "0";

		switch (statemenType) {

			case "SCB":
				BNKCODE = "3312431686";
				break;

			case "KBANK":
				BNKCODE = "3402314428";
				break;

			case "BBL":
				BNKCODE = "1227360581";
				break;

			case "SCB_BILL":
				BNKCODE = "3313019398";
				break;

			case "KBANK_BILL":
				BNKCODE = "3401018025";
				break;

			case "BBL_BILL":
				BNKCODE = "1223098219";
				break;
			case "SCB_CUR":
				BNKCODE = "3313019322";
				break;
			case "KBANK_CUR":
				BNKCODE = "3401018025";
				break;
			case "BBL_CUR":
				BNKCODE = "1223098219";
				break;

			case "SCB_MMN":
				BNKCODE = "3313019322";
				break;

			case "BBL_QR":
				BNKCODE = "1223098219";
				break;

		}

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT  COALESCE( REF2,'-') AS REF1,  COALESCE(BM_REFCU1,'-') AS REF2, BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '"
					+ BNKCODE
					+ "' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = '" + statemenType + "'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT   HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10' AND HC_PYDT = '" + statemenDate + "'\r\n"
					+ "\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER     ORDER BY ID  ASC";

			/*
			 * String query = "SELECT DISTINCT   * \r\n"
			 * + "                        FROM (\r\n"
			 * +
			 * "                        SELECT COALESCE( REF2,'-') AS REF1,  COALESCE(BM_REFCU1,'-') AS REF2, BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
			 * +
			 * "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '3401018025' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
			 * +DBNAMEPP+".BANK_MAPPING bm\r\n"
			 * + "					WHERE  BM_BANK  = 'KBANK_BILL'\r\n"
			 * + "					AND BM_DATE  = '"+statemenDate+"'\r\n"
			 * + "					AND BM_CONO  = '10' AND BM_STATUS != '99' \r\n"
			 * + "					) AS a \r\n"
			 * + "					LEFT JOIN \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
			 * +DBNAMEPP+".HR_RECEIPT hr \r\n"
			 * + "					WHERE hR_cono = '10'\r\n"
			 * + "					) AS b\r\n"
			 * + "					ON  a.GROUPID = B.HC_FNNO \r\n"
			 * + "					LEFT  JOIN  \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
			 * +DBNAMEPP+".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
			 * + "					) AS c \r\n"
			 * + "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
			 * + "					left join \r\n"
			 * + "					(\r\n"
			 * + "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM "
			 * +DBM3NAME+".OCUSMA o \r\n"
			 * + "					WHERE OKCONO = '10'\r\n"
			 * + "					) AS d \r\n"
			 * + "					on  d.OKCUNO = a.PAYER   ORDER BY ID  ASC";
			 */
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMBBL_BILL(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '1223098219' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'BBL_BILL'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10'\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER  ORDER BY ID  ASC";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMSCB_MMN(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '3313019322' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'SCB_MMN'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10'\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMSCB_CUR(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '3313019322' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'SCB_CUR'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT  HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10'\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMKBANK_CUR(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		String BNKCODE = "0";

		switch (statemenType) {

			case "SCB":
				BNKCODE = "3312431686";
				break;

			case "KBANK":
				BNKCODE = "3402314428";
				break;

			case "BBL":
				BNKCODE = "1227360581";
				break;

			case "SCB_BILL":
				BNKCODE = "3313019398";
				break;

			case "KBANK_BILL":
				BNKCODE = "3401018025";
				break;

			case "BBL_BILL":
				BNKCODE = "1223098219";
				break;
			case "SCB_CUR":
				BNKCODE = "3313019322";
				break;
			case "KBANK_CUR":
				BNKCODE = "3401018025";
				break;
			case "BBL_CUR":
				BNKCODE = "1223098219";
				break;

			case "SCB_MMN":
				BNKCODE = "3313019322";
				break;

			case "BBL_QR":
				BNKCODE = "1223098219";
				break;

		}

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '"
					+ BNKCODE
					+ "' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = '" + statemenType + "'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT   HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10' AND HC_PYDT = '" + statemenDate + "'\r\n"
					+ "\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER     ORDER BY ID  ASC";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMBBL_CUR(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '1223098219' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'BBL_CUR'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10'\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER";
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMBBL_QR(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String BNKCODE = "0";

			switch (statemenType) {

				case "SCB":
					BNKCODE = "3312431686";
					break;

				case "KBANK":
					BNKCODE = "3402314428";
					break;

				case "BBL":
					BNKCODE = "1227360581";
					break;

				case "SCB_BILL":
					BNKCODE = "3313019398";
					break;

				case "KBANK_BILL":
					BNKCODE = "3401018025";
					break;

				case "BBL_BILL":
					BNKCODE = "1223098219";
					break;
				case "SCB_CUR":
					BNKCODE = "3313019322";
					break;
				case "KBANK_CUR":
					BNKCODE = "3401018025";
					break;
				case "BBL_CUR":
					BNKCODE = "1223098219";
					break;

				case "SCB_MMN":
					BNKCODE = "3313019322";
					break;

				case "BBL_QR":
					BNKCODE = "1223098219";
					break;

			}

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT  COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '"
					+ BNKCODE
					+ "' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = '" + statemenType + "'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT   HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '10' AND HC_PYDT = '" + statemenDate + "'\r\n"
					+ "\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '10'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER     ORDER BY ID  ASC";

			/*
			 * String query = "SELECT DISTINCT   * \r\n"
			 * + "                        FROM (\r\n"
			 * +
			 * "                        SELECT COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
			 * +
			 * "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '1223098219' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
			 * +DBNAMEPP+".BANK_MAPPING bm\r\n"
			 * + "					WHERE  BM_BANK  = 'BBL_QR'\r\n"
			 * + "					AND BM_DATE  = '"+statemenDate+"'\r\n"
			 * + "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
			 * + "					) AS a \r\n"
			 * + "					LEFT JOIN \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
			 * +DBNAMEPP+".HR_RECEIPT hr \r\n"
			 * + "					WHERE hR_cono = '10'\r\n"
			 * + "					) AS b\r\n"
			 * + "					ON  a.GROUPID = B.HC_FNNO \r\n"
			 * + "					LEFT  JOIN  \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
			 * +DBNAMEPP+".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
			 * + "					) AS c \r\n"
			 * + "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
			 * + "					left join \r\n"
			 * + "					(\r\n"
			 * + "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM "
			 * +DBM3NAME+".OCUSMA o \r\n"
			 * + "					WHERE OKCONO = '10'\r\n"
			 * + "					) AS d \r\n"
			 * + "					on  d.OKCUNO = a.PAYER";
			 * 
			 */
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	/////////////////////////// WTF /////////////////

	public static String GETITEMSCB_WTF(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String BNKCODE = "0";

			switch (statemenType) {

				case "SCB_WTF":
					BNKCODE = "3312431686";
					break;

			}

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT  COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '"
					+ BNKCODE
					+ "' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'SCB'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '600' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT   HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'WTF') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '600' AND HC_PYDT = '" + statemenDate + "'\r\n"
					+ "\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'WTF') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '600'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '600'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER     ORDER BY ID  ASC";

			/*
			 * String query = "SELECT DISTINCT   * \r\n"
			 * + "                        FROM (\r\n"
			 * +
			 * "                        SELECT COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
			 * +
			 * "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '1223098219' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
			 * +DBNAMEPP+".BANK_MAPPING bm\r\n"
			 * + "					WHERE  BM_BANK  = 'BBL_QR'\r\n"
			 * + "					AND BM_DATE  = '"+statemenDate+"'\r\n"
			 * + "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
			 * + "					) AS a \r\n"
			 * + "					LEFT JOIN \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
			 * +DBNAMEPP+".HR_RECEIPT hr \r\n"
			 * + "					WHERE hR_cono = '10'\r\n"
			 * + "					) AS b\r\n"
			 * + "					ON  a.GROUPID = B.HC_FNNO \r\n"
			 * + "					LEFT  JOIN  \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
			 * +DBNAMEPP+".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
			 * + "					) AS c \r\n"
			 * + "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
			 * + "					left join \r\n"
			 * + "					(\r\n"
			 * + "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM "
			 * +DBM3NAME+".OCUSMA o \r\n"
			 * + "					WHERE OKCONO = '10'\r\n"
			 * + "					) AS d \r\n"
			 * + "					on  d.OKCUNO = a.PAYER";
			 * 
			 */
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	public static String GETITEMBBL_WTF(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String BNKCODE = "0";

			switch (statemenType) {

				case "BBL_WTF":
					BNKCODE = "1923053068";
					break;

			}

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT  COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '"
					+ BNKCODE
					+ "' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'BBL'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '600' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT   HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'WTF') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '600' AND HC_PYDT = '" + statemenDate + "'\r\n"
					+ "\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'WTF') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '600'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '600'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER     ORDER BY ID  ASC";

			/*
			 * String query = "SELECT DISTINCT   * \r\n"
			 * + "                        FROM (\r\n"
			 * +
			 * "                        SELECT COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
			 * +
			 * "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '1223098219' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
			 * +DBNAMEPP+".BANK_MAPPING bm\r\n"
			 * + "					WHERE  BM_BANK  = 'BBL_QR'\r\n"
			 * + "					AND BM_DATE  = '"+statemenDate+"'\r\n"
			 * + "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
			 * + "					) AS a \r\n"
			 * + "					LEFT JOIN \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
			 * +DBNAMEPP+".HR_RECEIPT hr \r\n"
			 * + "					WHERE hR_cono = '10'\r\n"
			 * + "					) AS b\r\n"
			 * + "					ON  a.GROUPID = B.HC_FNNO \r\n"
			 * + "					LEFT  JOIN  \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
			 * +DBNAMEPP+".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
			 * + "					) AS c \r\n"
			 * + "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
			 * + "					left join \r\n"
			 * + "					(\r\n"
			 * + "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM "
			 * +DBM3NAME+".OCUSMA o \r\n"
			 * + "					WHERE OKCONO = '10'\r\n"
			 * + "					) AS d \r\n"
			 * + "					on  d.OKCUNO = a.PAYER";
			 * 
			 */
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	////////////////////////// NSD////////////////////

	public static String GETITEMSCB_NSD(@Context HttpServletRequest httpServletRequest, String statemenDate,
			String statemenType) throws Exception {
		logger.info("GETITEM");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String BNKCODE = "0";

			switch (statemenType) {

				case "SCB_NSD":
					BNKCODE = "3313029408";
					break;

			}

			String query = "SELECT DISTINCT   * \r\n"
					+ "                        FROM (\r\n"
					+ "                        SELECT  COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
					+ "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '"
					+ BNKCODE
					+ "' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
					+ DBNAMEPP + ".BANK_MAPPING bm\r\n"
					+ "					WHERE  BM_BANK  = 'SCB'\r\n"
					+ "					AND BM_DATE  = '" + statemenDate + "'\r\n"
					+ "					AND BM_CONO  = '500' AND BM_STATUS != '99'\r\n"
					+ "					) AS a \r\n"
					+ "					LEFT JOIN \r\n"
					+ "					(\r\n"
					+ "					SELECT   HC_VCNO AS VCNO ,HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'NSD') AS LOCATIONTH  FROM "
					+ DBNAMEPP + ".HR_RECEIPT hr \r\n"
					+ "					WHERE hR_cono = '500' AND HC_PYDT = '" + statemenDate + "'\r\n"
					+ "\r\n"
					+ "					) AS b\r\n"
					+ "					ON  a.GROUPID = B.HC_FNNO \r\n"
					+ "					LEFT  JOIN  \r\n"
					+ "					(\r\n"
					+ "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'NSD') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
					+ DBNAMEPP + ".HEAD_RECIPT hr WHERE  H_CONO = '500'\r\n"
					+ "					) AS c \r\n"
					+ "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
					+ "					left join \r\n"
					+ "					(\r\n"
					+ "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM " + DBM3NAME + ".OCUSMA o \r\n"
					+ "					WHERE OKCONO = '500'\r\n"
					+ "					) AS d \r\n"
					+ "					on  d.OKCUNO = a.PAYER     ORDER BY ID  ASC";

			/*
			 * String query = "SELECT DISTINCT   * \r\n"
			 * + "                        FROM (\r\n"
			 * +
			 * "                        SELECT COALESCE(BM_REFCU1,'-') AS REF1,COALESCE(REF2,'-') AS REF2 , BM_CONO, BM_CUNO as PAYER, COALESCE(BM_OVPAY,0) AS OVERPAY,\r\n"
			 * +
			 * "					COALESCE(NULLIF(BM_CNDN, ''), '0')  AS CNDN  ,COALESCE(BM_BKCHARGE,0.00) as BANKCHARGE , '1223098219' AS AccNo , BM_DESC  , BM_DATE , BM_TIME , BM_AMOUNT, BM_PARENT_STS , COALESCE(BM_PARENT_ID,'-')AS BM_PARENT_ID    , BM_ID as ID , COALESCE (BM_FNNO,'-') AS　GROUPID  FROM "
			 * +DBNAMEPP+".BANK_MAPPING bm\r\n"
			 * + "					WHERE  BM_BANK  = 'BBL_QR'\r\n"
			 * + "					AND BM_DATE  = '"+statemenDate+"'\r\n"
			 * + "					AND BM_CONO  = '10' AND BM_STATUS != '99'\r\n"
			 * + "					) AS a \r\n"
			 * + "					LEFT JOIN \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT  HC_VCNO AS VCNO , HR_CONO , HC_FNNO , COALESCE(HC_TYPE,'TRANSFER'),COALESCE(HC_LOCATION,'LS') AS LOCATIONTH  FROM "
			 * +DBNAMEPP+".HR_RECEIPT hr \r\n"
			 * + "					WHERE hR_cono = '10'\r\n"
			 * + "					) AS b\r\n"
			 * + "					ON  a.GROUPID = B.HC_FNNO \r\n"
			 * + "					LEFT  JOIN  \r\n"
			 * + "					(\r\n"
			 * +
			 * "					SELECT H_RNNO,H_CONO , COALESCE(H_LOCATION,'LS') AS LOCATION ,COALESCE(H_TYPE,'TRANSFER') AS TYPE FROM "
			 * +DBNAMEPP+".HEAD_RECIPT hr WHERE  H_CONO = '10'\r\n"
			 * + "					) AS c \r\n"
			 * + "					ON c.H_rnno = COALESCE(a.GROUPID,'')\r\n"
			 * + "					left join \r\n"
			 * + "					(\r\n"
			 * + "					SELECT OKCONO,OKCUNO ,OKCUNM as PAYERNAME FROM "
			 * +DBM3NAME+".OCUSMA o \r\n"
			 * + "					WHERE OKCONO = '10'\r\n"
			 * + "					) AS d \r\n"
			 * + "					on  d.OKCUNO = a.PAYER";
			 * 
			 */
			System.out.println("GETITEM\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			System.out.print(getListData);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);

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

	/////////////////////////////////////////////////

	///////////////////////////////// BANKMAPPING/////////////////////////////
	public static List<String> getVisitorRequesterPP(String cono, String divi, String ordno, String location)
			throws Exception {
		logger.info("getVisitorRequester");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT H_EMAIL FROM " + Constant.DBNAME + ".VISITOR_HEAD WHERE H_ID = '" + ordno.trim()
					+ "' AND  H_CONO = '" + cono + "' AND　H＿DIVI = '" + divi + "' AND H_LOCATION = '" + location + "'";
			System.out.println("getVisitorRequester\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("H_EMAIL").trim());

			}
			return getListData;

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

	public static List<String> getVisitorRequester(String cono, String divi, String ordno) throws Exception {
		logger.info("getVisitorRequester");

		List<String> getListData = new ArrayList<String>();

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT H_COMPANYNAME2 FROM " + Constant.DBNAME + ".VISITOR_HEAD WHERE H_ID = '"
					+ ordno.trim() + "'";
			System.out.println("getVisitorRequester\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("H_COMPANYNAME2").trim());

			}
			return getListData;

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

	public static String showvisitor(String vID, String location) throws Exception {
		logger.info("getCompany");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "\r\n"
					+ "SELECT * FROM " + Constant.DBNAME + ".VISITOR_HEAD WHERE  H_ID  = '" + vID.trim()
					+ "' AND H_LOCATION = '" + location.trim() + "'"
					+ "";
			System.out.println("showvisitor\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			return ConvertResultSet.convertResultSetToJsonVPP2(mRes);
			// return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getID1() throws Exception {
		logger.info("getCompany");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "	 SELECT  COALESCE(MAX(H_ID),SUBSTRING(REPLACE(CHAR(current date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO   FROM "
					+ Constant.DBNAME + ".VISITOR_HEAD "
					+ "";
			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '10' AND SHDIVI = '101'";
			System.out.println("getID\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("ORDERNO").trim();
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

	public static String location(String CONO, String DIVI)
			throws Exception {
		logger.info("getemployee");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT  * FROM  " + DBNAME + ".COM_LOCAL WHERE  STATUS  = '20'\r\n"
					+ "AND  CONO = '" + CONO.trim() + "' AND  DIVI  = '" + DIVI.trim()
					+ "' AND PGM_NAME  = 'VISITOR' AND PGM_TYPE = 'N'";
			System.out.println("getlocation\n" + query);
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

	public static String getfollower(String vID)
			throws Exception {
		logger.info("getemployee");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT * FROM " + Constant.DBNAME + ".FOLLOWER_HEAD \r\n"
					+ "WHERE  F_ID  = '" + vID.trim() + "'\r\n"
					+ "AND H_NAME  = 'Follower'";
			System.out.println("getfollower\n" + query);
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

	public static String getemployee(String cono)
			throws Exception {
		logger.info("getemployee");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT  a.NAMELIST||'('||b.DESCRIPTION|| ')' || ' : ' || a.TEL  AS NAMELIST  , a.MAIL AS MAIL \r\n"
					+ "FROM(\r\n"
					+ "SELECT DISTINCT  TRIM(ST_TPNM)||' '||TRIM(ST_TNAME)||' '||TRIM(ST_TLNAME)||' ' AS namelist , ST_COSTC  ,ST_EMAIL AS MAIL, COALESCE(st_level,'-') AS TEL  FROM "
					+ Constant.DBNAME + ".STAFFLIST \r\n"
					+ "WHERE ST_STS  = '20' AND　ST_CONO　IN ('" + cono + "','100')\r\n"
					+ ") AS a \r\n"
					+ "LEFT JOIN \r\n"
					+ "(\r\n"
					+ "SELECT  TRIM(S2AITM) AS COSTCENTER, TRIM(S2TX15) AS DESCRIPTION \r\n"
					+ "FROM " + DBM3NAME + ".FSTLIN  \r\n"
					+ "WHERE S2CONO IN ('" + cono + "','100')\r\n"
					+ "AND S2SLVL = '1'  \r\n"
					+ "ORDER BY S2AITM\r\n"
					+ ") AS b \r\n"
					+ "ON a.ST_COSTC = b.COSTCENTER";
			System.out.println("getgetemployee\n" + query);
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

	public static String getoperationfilterdatabyuser(@Context HttpServletRequest httpServletRequest, String cono,
			String divi, String getLocation, String fromdate, String todate, String username)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT 'VISITOR' as USER, H_ID AS id , H_LICENSE AS license,\r\n"
					+ "H_NAME AS name ,H_SURNAME AS surname,\r\n"
					+ "H_TEL AS Tel,H_REASON AS Reason,\r\n"
					+ "H_COMPANYNAME2 AS Emp,\r\n"
					+ "H_STATUS AS status,H_CARDNO AS Card,\r\n"
					+ "H_ROOMNO AS Room , COALESCE(H_CHECKIN,CHAR(CURRENT DATE, ISO)) as CHECKIN ,COALESCE(H_CHECKOUT,CHAR(CURRENT DATE, ISO))  as CHECKOUT,H_IMG as IMG, H_MEETDATE as DATE , H_MEETTIME as TIME, H_REMARK1 as REMARK,  H_MEETDATEOUT as DATEOUT , H_MEETTIMEOUT as TIMEOUT ,COALESCE(H_CHECKINTIME,SUBSTR(CAST(CURRENT TIME AS CHAR(8)), 1, 5)) as CHECKINTIME ,COALESCE(H_CHECKOUTTIME,SUBSTR(CAST(CURRENT TIME AS CHAR(8)), 1, 5))  as CHECKOUTTIME ,COALESCE(H_EMAIL,'-')as MAIL \r\n"
					+ "\r\n"
					+ " FROM " + Constant.DBNAME
					+ ".VISITOR_HEAD WHERE　H_STATUS NOT　IN ('99') AND H_STATUS IS NOT　NULL AND H_LOCATION = '"
					+ getLocation.trim() + "' "
					+ "AND　H_USER　='" + username + "' ";

			/*
			 * String query ="SELECT H_ID AS id , H_LICENSE AS license,\r\n"
			 * + "H_NAME AS name ,H_SURNAME AS surname,\r\n"
			 * + "H_TEL AS Tel,H_REASON AS Reason,\r\n"
			 * + "H_COMPANYNAME2 AS Emp,\r\n"
			 * + "H_STATUS AS status,H_CARDNO AS Card,\r\n"
			 * + "H_ROOMNO AS Room , H_REMARK3  AS parentId , H_IMG as IMG \r\n"
			 * + " FROM "+DBNAME+".VISITOR_HEAD \r\n"
			 * + " WHERE H_STATUS NOT　IN ('99') \r\n"
			 * + "UNION ALL    \r\n"
			 * + " SELECT \r\n"
			 * +
			 * " F_ID AS id,H_COMPANYNAME2 AS license, H_NAME AS name , H_SURNAME AS surname,\r\n"
			 * +
			 * " H_COMPANYNAME2 AS Tel,H_COMPANYNAME2 AS Reason,H_COMPANYNAME2 AS Employee,\r\n"
			 * + " H_COMPANYNAME2 AS status,H_COMPANYNAME2 AS Card,\r\n"
			 * +
			 * " H_COMPANYNAME2 AS Room ,F_ID AS parentId ,COALESCE(H_COMPANYNAME2,'-') AS IMG\r\n"
			 * + " FROM "+DBNAME+".FOLLOWER_HEAD  \r\n"
			 * + " WHERE H_STATUS NOT　IN ('99') \r\n"
			 * + "ORDER BY  id  desc";
			 */

			System.out.println("getoperationdata\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);
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

	public static String getoperationfilterdata(@Context HttpServletRequest httpServletRequest, String cono,
			String divi, String getLocation, String fromdate, String todate)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT 'ADMIN' as USER, H_ID AS id , H_LICENSE AS license,\r\n"
					+ "H_NAME AS name ,H_SURNAME AS surname,\r\n"
					+ "H_TEL AS Tel,H_REASON AS Reason,\r\n"
					+ "H_COMPANYNAME2 AS Emp,\r\n"
					+ "H_STATUS AS status,H_CARDNO AS Card,\r\n"
					+ "H_ROOMNO AS Room , COALESCE(H_CHECKIN,CHAR(CURRENT DATE, ISO)) as CHECKIN ,COALESCE(H_CHECKOUT,CHAR(CURRENT DATE, ISO))  as CHECKOUT,H_IMG as IMG, H_MEETDATE as DATE , H_MEETTIME as TIME, H_REMARK1 as REMARK,  H_MEETDATEOUT as DATEOUT , H_MEETTIMEOUT as TIMEOUT ,COALESCE(H_CHECKINTIME,SUBSTR(CAST(CURRENT TIME AS CHAR(8)), 1, 5)) as CHECKINTIME ,COALESCE(H_CHECKOUTTIME,SUBSTR(CAST(CURRENT TIME AS CHAR(8)), 1, 5))  as CHECKOUTTIME ,COALESCE(H_EMAIL,'-')as MAIL \r\n"
					+ "\r\n"
					+ " FROM " + Constant.DBNAME
					+ ".VISITOR_HEAD WHERE　H_STATUS NOT　IN ('99') AND H_STATUS IS NOT　NULL AND H_LOCATION = '"
					+ getLocation.trim() + "' "
					+ "AND H_MEETDATE BETWEEN  '" + fromdate.trim() + "' AND '" + todate.trim() + "'  ";

			/*
			 * String query ="SELECT H_ID AS id , H_LICENSE AS license,\r\n"
			 * + "H_NAME AS name ,H_SURNAME AS surname,\r\n"
			 * + "H_TEL AS Tel,H_REASON AS Reason,\r\n"
			 * + "H_COMPANYNAME2 AS Emp,\r\n"
			 * + "H_STATUS AS status,H_CARDNO AS Card,\r\n"
			 * + "H_ROOMNO AS Room , H_REMARK3  AS parentId , H_IMG as IMG \r\n"
			 * + " FROM "+DBNAME+".VISITOR_HEAD \r\n"
			 * + " WHERE H_STATUS NOT　IN ('99') \r\n"
			 * + "UNION ALL    \r\n"
			 * + " SELECT \r\n"
			 * +
			 * " F_ID AS id,H_COMPANYNAME2 AS license, H_NAME AS name , H_SURNAME AS surname,\r\n"
			 * +
			 * " H_COMPANYNAME2 AS Tel,H_COMPANYNAME2 AS Reason,H_COMPANYNAME2 AS Employee,\r\n"
			 * + " H_COMPANYNAME2 AS status,H_COMPANYNAME2 AS Card,\r\n"
			 * +
			 * " H_COMPANYNAME2 AS Room ,F_ID AS parentId ,COALESCE(H_COMPANYNAME2,'-') AS IMG\r\n"
			 * + " FROM "+DBNAME+".FOLLOWER_HEAD  \r\n"
			 * + " WHERE H_STATUS NOT　IN ('99') \r\n"
			 * + "ORDER BY  id  desc";
			 */

			System.out.println("getoperationdata\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);
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

	public static boolean getcostcenter(String cono, String username)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT \r\n"
					+ "  CASE \r\n"
					+ "    WHEN TRIM(ST_COSTC) = 'S501' OR TRIM(ST_COSTC) = 'S501' THEN 'true'\r\n"
					+ "    ELSE 'false'\r\n"
					+ "  END AS Result\r\n"
					+ "FROM " + Constant.DBNAME + ".STAFFLIST s\r\n"
					+ "WHERE ST_N6L3  = '" + username.trim() + "'\r\n"
					+ "LIMIT 1";

			System.out.println("getoperationdata\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			if (mRes.next()) {
				String result = mRes.getString("Result");
				return "true".equalsIgnoreCase(result);
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

	public static String jsonschedule(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String getLocation)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			/*
			 * String query = "SELECT  H_MEETDATE ||' '|| H_MEETTIME AS START,\r\n"
			 * + "          H_MEETDATEOUT ||' '|| H_MEETTIMEOUT AS END,\r\n"
			 * + "          H_COMPANYNAME ||' '|| H_NAME ||' '|| H_SURNAME \r\n"
			 * +
			 * "          ||' '|| H_REASON ||' '|| H_REMARK1  AS title,COALESCE(H_ID,'-') AS ID, COALESCE(H_ROOMNO,'-') AS ROOM, COALESCE(H_IMG,'-') AS IMG \r\n"
			 * + "          FROM  "+DBNAME+".VISITOR_HEAD ";
			 */
			String query = "SELECT H_MEETDATE ||' '|| H_MEETTIME AS START,\r\n"
					+ "				       H_MEETDATEOUT ||' '|| H_MEETTIMEOUT AS END,\r\n"
					+ "		         		a.H_ID ||' '||H_COMPANYNAME ||' '|| H_NAME ||' '|| H_SURNAME \r\n"
					+ "				   	   ||' '|| H_REASON ||' '|| a.H_REMARK1  AS title,\r\n"
					+ "				   	   COALESCE(a.H_ID,'-') AS ID, \r\n"
					+ "				   	   COALESCE(H_ROOMNO,'-') AS ROOM,\r\n"
					+ "				   	   COALESCE(H_IMG,'-') AS IMG,\r\n"
					+ "				   	   COALESCE(b.H_FOODQTY,'-') AS FOOD, \r\n"
					+ "				   	  COALESCE(b.H_BEVERAGE,'-') AS BEVERAGE, \r\n"
					+ "				   	   COALESCE(b.H_SNACKS,'-') AS SNACK, \r\n"
					+ "				   	   COALESCE(b.H_SANDALS,'-') AS SANDAL, \r\n"
					+ "				   	   COALESCE(b.H_ATKQTY,'-') AS ATK, \r\n"
					+ "				   	     COALESCE(b.H_PARKQTY,'-') AS PARK, \r\n"
					+ "				   	     COALESCE(b.H_REMARK1,'-') AS ETC,\r\n"
					+ "				   	     COALESCE(a.H_COMPANYNAME2,'-') AS EMP\r\n"
					+ " FROM (\r\n"
					+ " SELECT *  FROM " + Constant.DBNAME + ".VISITOR_HEAD vh \r\n"
					+ " ) AS a \r\n"
					+ " LEFT JOIN \r\n"
					+ " (\r\n"
					+ " SELECT *  FROM " + Constant.DBNAME + ".VISITOR_ITEM  vi \r\n"
					+ " ) AS b \r\n"
					+ " ON a.H_ID =  b.H_ID  AND   a.H_CONO = b.H_CONO AND   a.H_DIVI = b.H_DIVI";

			System.out.println("getoperationdata\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);
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

	public static String getoperationdata(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String getLocation)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT H_ID AS id , H_LICENSE AS license,\r\n"
					+ "H_NAME AS name ,H_SURNAME AS surname,\r\n"
					+ "H_TEL AS Tel,H_REASON AS Reason,\r\n"
					+ "H_COMPANYNAME2 AS Emp,\r\n"
					+ "H_STATUS AS status,COALESCE(H_CARDNO,'-') AS Card,\r\n"
					+ "COALESCE(H_ROOMNO,'-') AS Room ,H_IMG as IMG, H_MEETDATE as DATE , H_MEETTIME as TIME , COALESCE( H_REMARK1,'-') as REMARK,COALESCE( H_EMAIL,'-') as MAIL  \r\n"
					+ " FROM " + Constant.DBNAME
					+ ".VISITOR_HEAD WHERE　H_STATUS NOT　IN ('99') AND H_STATUS IS NOT　NULL AND H_LOCATION = '"
					+ getLocation.trim() + "'   ";

			/*
			 * String query ="SELECT H_ID AS id , H_LICENSE AS license,\r\n"
			 * + "H_NAME AS name ,H_SURNAME AS surname,\r\n"
			 * + "H_TEL AS Tel,H_REASON AS Reason,\r\n"
			 * + "H_COMPANYNAME2 AS Emp,\r\n"
			 * + "H_STATUS AS status,H_CARDNO AS Card,\r\n"
			 * + "H_ROOMNO AS Room , H_REMARK3  AS parentId , H_IMG as IMG \r\n"
			 * + " FROM "+DBNAME+".VISITOR_HEAD \r\n"
			 * + " WHERE H_STATUS NOT　IN ('99') \r\n"
			 * + "UNION ALL    \r\n"
			 * + " SELECT \r\n"
			 * +
			 * " F_ID AS id,H_COMPANYNAME2 AS license, H_NAME AS name , H_SURNAME AS surname,\r\n"
			 * +
			 * " H_COMPANYNAME2 AS Tel,H_COMPANYNAME2 AS Reason,H_COMPANYNAME2 AS Employee,\r\n"
			 * + " H_COMPANYNAME2 AS status,H_COMPANYNAME2 AS Card,\r\n"
			 * +
			 * " H_COMPANYNAME2 AS Room ,F_ID AS parentId ,COALESCE(H_COMPANYNAME2,'-') AS IMG\r\n"
			 * + " FROM "+DBNAME+".FOLLOWER_HEAD  \r\n"
			 * + " WHERE H_STATUS NOT　IN ('99') \r\n"
			 * + "ORDER BY  id  desc";
			 */

			System.out.println("getoperationdata\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			return ConvertResultSet.convertResultSetToJsonVPP(mRes);
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

	public static String getID(String cono, String divi)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = " SELECT SHORNO AS ORDERNO FROM " + DBNAME + ".M3_SWRHEAD WHERE  SHCONO = '" + cono.trim()
					+ "' AND  SHDIVI = '" + divi.trim() + "'";
			System.out.println("getID\n" + query);
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

	public static String getDev(String cono, String divi)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = " SELECT ST_N6L3 AS US_DEV FROM " + Constant.DBNAME + ".STAFFLIST s \r\n"
					+ " WHERE  ST_COSTC = 'S8'\r\n"
					+ " AND ST_STS  = '20'\r\n"
					+ " AND ST_CONO  = '" + cono.trim() + "'";

			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";
			System.out.println("getDeptHead\n" + query);
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
	
	

	public static String getAuthenBySTATUS(String status, String id, String username)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT \r\n"
					+ "CASE \r\n"
					+ "WHEN (\r\n"
					+ "SELECT COUNT(*) \r\n"
					+ "FROM "+DBNAME+"."+SR_APPROVE+" \r\n"
					+ "WHERE STATUS = '"+status+"' \r\n"
					+ "AND DOC_CODE = 'ITRQ' \r\n"
					+ "AND DOC_NO = '"+id+"' \r\n"
					+ "AND APPROVE LIKE '%' || UPPER('"+username+"') || '%'\r\n"
					+ ") > 0 \r\n"
					+ "THEN 'OK' \r\n"
					+ "ELSE 'NOT FOUND' \r\n"
					+ "END AS RESULT\r\n"
					+ "FROM SYSIBM.SYSDUMMY1";

			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";
			System.out.println("getDeptHead\n" + query);
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
	
	
	
	
	
	public static String getlistuser(String cono, String status , String id )
			throws Exception {
		logger.info("getListEmail");

		Connection conn = null;
		Statement stmt = null;
		String jsonResult = "[]"; // default เป็น array ว่าง
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();
			
			
			
			
			
			int statusInt = Integer.parseInt(status);

			// ถ้าเป็น "00" ให้บวก 20
			if (status.equals("00")) {
			    statusInt += 20;
			} else {
			    // เช็คเงื่อนไขและปรับค่า
			    if (statusInt + 10 > 80) {
			        statusInt = 10;
			    } else {
			        statusInt += 10;
			    }
			}

			String query = "WITH RECURSIVE SplitNames (DOC_NO, APPROVER, REMAINING) AS (\r\n"
					+ "SELECT \r\n"
					+ "DOC_NO,\r\n"
					+ "SUBSTR(APPROVE, 1, LOCATE(',', APPROVE || ',') - 1),\r\n"
					+ "SUBSTR(APPROVE || ',', LOCATE(',', APPROVE || ',') + 1)\r\n"
					+ "FROM "+DBNAME+"."+SR_APPROVE+"\r\n"
					+ "WHERE STATUS = '"+statusInt+"'\r\n"
					+ "AND DOC_CODE = 'ITRQ'\r\n"
					+ "AND DOC_NO = '"+id+"'\r\n"
					+ "UNION ALL\r\n"
					+ "SELECT \r\n"
					+ "DOC_NO,\r\n"
					+ "SUBSTR(REMAINING, 1, LOCATE(',', REMAINING) - 1),\r\n"
					+ "SUBSTR(REMAINING, LOCATE(',', REMAINING) + 1)\r\n"
					+ "FROM SplitNames\r\n"
					+ "WHERE REMAINING <> ''\r\n"
					+ ")\r\n"
					+ "SELECT APPROVER\r\n"
					+ "FROM SplitNames";

			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";
			System.out.println("getDeptHead\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);
			
		
			jsonResult = ConvertResultSet.convertResultSetToJson(mRes);




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

		return jsonResult;

	}
	
	
	
	
	///////////////////////


	public static String getmailtemplete(String cono, String status , String id )
			throws Exception {
		logger.info("getListEmail");

		Connection conn = null;
		Statement stmt = null;
		String jsonResult = "[]"; // default เป็น array ว่าง
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();
			
			
			
			
			
			int statusInt = Integer.parseInt(status);

			// ถ้าเป็น "00" ให้บวก 20
			if (status.equals("00")) {
				statusInt  = 10; 
			} else {
				  if (statusInt + 10 > 70) {
				        statusInt = 22;
				    }
				
				  else {
					 statusInt = Integer.parseInt(status);
			    }
			}

			String query = "SELECT * FROM  BRLDTA0100.M3_WORKFLOWPROGRAMEMAIL mw \r\n"
					+ "WHERE EDOCUMENT  = 'ITRQ'\r\n"
					+ "AND ESTATUSNO = '"+statusInt +"'";


			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";
			System.out.println("getDeptHead\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);
			
		
			jsonResult = ConvertResultSet.convertResultSetToJson(mRes);




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

		return jsonResult;

	}
	
	

	public static String getlistuser2(String cono, String status , String id )
			throws Exception {
		logger.info("getListEmail");

		Connection conn = null;
		Statement stmt = null;
		String jsonResult = "[]"; // default เป็น array ว่าง
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();
			
			
			
			
			
			int statusInt = Integer.parseInt(status);

			// ถ้าเป็น "00" ให้บวก 20
			if (status.equals("00")) {
			    statusInt += 20;
			} else {
			    // เช็คเงื่อนไขและปรับค่า
			    if (statusInt + 10 > 80) {
			        statusInt = 10;
			    } else {
			        statusInt += 10;
			    }
			}

			String query = "WITH RECURSIVE SplitNames (DOC_NO, APPROVER, REMAINING) AS (\r\n"
					+ "  SELECT \r\n"
					+ "    DOC_NO,\r\n"
					+ "    SUBSTR(APPROVE, 1, LOCATE(',', APPROVE || ',') - 1),\r\n"
					+ "    SUBSTR(APPROVE || ',', LOCATE(',', APPROVE || ',') + 1)\r\n"
					+ "  FROM "+DBNAME+"."+SR_APPROVE+"\r\n"
					+ "  WHERE STATUS = '"+statusInt+"'  -- แทนด้วยค่าจริง\r\n"
					+ "    AND DOC_CODE = 'ITRQ'\r\n"
					+ "    AND DOC_NO = '"+id+"'         -- แทนด้วยค่าจริง\r\n"
					+ "  UNION ALL\r\n"
					+ "  SELECT \r\n"
					+ "    DOC_NO,\r\n"
					+ "    SUBSTR(REMAINING, 1, LOCATE(',', REMAINING) - 1),\r\n"
					+ "    SUBSTR(REMAINING, LOCATE(',', REMAINING) + 1)\r\n"
					+ "  FROM SplitNames\r\n"
					+ "  WHERE REMAINING <> ''\r\n"
					+ "),\r\n"
					+ "Emails AS (\r\n"
					+ "  SELECT \r\n"
					+ "    TRIM(s.ST_EMAIL) AS ST_EMAIL,\r\n"
					+ "    TRIM(s.ST_N6L3) AS ST_N6L3,\r\n"
					+ "    ROW_NUMBER() OVER () AS rn\r\n"
					+ "  FROM SplitNames sn\r\n"
					+ "  JOIN BRLDTABK01.STAFFLIST s ON s.ST_N6L3 = sn.APPROVER\r\n"
					+ ")\r\n"
					+ "SELECT \r\n"
					+ "  ST_EMAIL,\r\n"
					+ "  ST_N6L3\r\n"
					+ "FROM Emails \r\n"
					+ "ORDER BY rn";


			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";
			System.out.println("getDeptHead\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);
			
		
			jsonResult = ConvertResultSet.convertResultSetToJson(mRes);




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

		return jsonResult;

	}
	
	public static String getListEmail(String cono, String status , String id )
			throws Exception {
		logger.info("getListEmail");

		Connection conn = null;
		Statement stmt = null;
		String jsonResult = "[]"; // default เป็น array ว่าง

		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();
			
			
			int statusInt = Integer.parseInt(status);

			// ถ้าเป็น "00" ให้บวก 20
			if (status.equals("00")) {
			    statusInt += 20;
			} else {
			    // เช็คเงื่อนไขและปรับค่า
			    if (statusInt + 10 > 80) {
			        statusInt = 10;
			    } else {
			        statusInt += 10;
			    }
			}

			String query = "WITH RECURSIVE SplitNames (DOC_NO, APPROVER, REMAINING) AS (\r\n"
					+ "  SELECT \r\n"
					+ "    DOC_NO,\r\n"
					+ "    SUBSTR(APPROVE, 1, LOCATE(',', APPROVE || ',') - 1),\r\n"
					+ "    SUBSTR(APPROVE || ',', LOCATE(',', APPROVE || ',') + 1)\r\n"
					+ "  FROM "+DBNAME+"."+SR_APPROVE+"\r\n"
					+ "  WHERE STATUS = '"+statusInt+"'\r\n"
					+ "    AND DOC_CODE = 'ITRQ'\r\n"
					+ "    AND DOC_NO = '"+id+"'\r\n"
					+ "  UNION ALL\r\n"
					+ "  SELECT \r\n"
					+ "    DOC_NO,\r\n"
					+ "    SUBSTR(REMAINING, 1, LOCATE(',', REMAINING) - 1),\r\n"
					+ "    SUBSTR(REMAINING, LOCATE(',', REMAINING) + 1)\r\n"
					+ "  FROM SplitNames\r\n"
					+ "  WHERE REMAINING <> ''\r\n"
					+ "),\r\n"
					+ "Emails AS (\r\n"
					+ "  SELECT \r\n"
					+ "    TRIM(s.ST_EMAIL) AS ST_EMAIL,\r\n"
					+ "    ROW_NUMBER() OVER () AS rn\r\n"
					+ "  FROM SplitNames sn\r\n"
					+ "  JOIN BRLDTABK01.STAFFLIST s ON s.ST_N6L3 = sn.APPROVER\r\n"
					+ ")\r\n"
					+ "SELECT \r\n"
					+ "  ST_EMAIL \r\n"
					+ "FROM Emails \r\n"
					+ "ORDER BY rn";

			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";
			System.out.println("getDeptHead\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);
			jsonResult = ConvertResultSet.convertResultSetToJson(mRes);




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

		return jsonResult;

	}

	public static String getDeptHead(String cono)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT  * \r\n"
					+ "FROM(\r\n"
					+ "SELECT DISTINCT \r\n"
					+ "ROW_NUMBER() OVER(ORDER BY a.CTL_UID) AS ID,\r\n"
					+ "UPPER(TRIM(a.CTL_UID)) AS US_LOGIN,\r\n"
					+ "a.CTL_UID,\r\n"
					+ "TRIM(b.ST_EPNM) || ' ' || TRIM(b.ST_ENAME) || ' ' || TRIM(b.ST_ELNAME) AS fullname\r\n"
					+ "FROM BRLDTA0100.APPCTL1 a\r\n"
					+ "JOIN BRLDTA0100.STAFFLIST b ON b.ST_N6L3 = a.CTL_UID\r\n"
					+ "WHERE a.CTL_REM = 'AP'\r\n"
					+ "AND b.ST_STS = '20'\r\n"
					+ "AND a.CTL_CONO = '"+cono+"'\r\n"
					+ "AND a.CTL_UID != '-'\r\n"
					+ ") AS CL \r\n"
					+ "LEFT JOIN \r\n"
					+ "(\r\n"
					+ "SELECT ST_N6L3,ST_EMAIL FROM BRLDTA0100.STAFFLIST \r\n"
					+ "WHERE ST_CONO = '"+cono+"'\r\n"
					+ ") AS SL\r\n"
					+ "ON SL.ST_N6L3 = CL.US_LOGIN";

			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";
			System.out.println("getDeptHead\n" + query);
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

	public static String getnewID(String cono, String divi)
			throws Exception {
		logger.info("getID");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			// String query = " SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO , VARCHAR_FORMAT(CURRENT
			// TIMESTAMP, 'YYYY-MM-DD') AS REQDATE FROM "+DBNAME+".M3_SWRHEAD WHERE
			// SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI =
			// '"+divi.trim()+"'";
			// String query = "SELECT COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current
			// date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO FROM "+DBNAME+".M3_SWRHEAD
			// WHERE SUBSTRING(SHORNO,1,1) != '3' AND SHCONO = '"+cono.trim()+"' AND SHDIVI
			// = '"+divi.trim()+"'";

			String query = "SELECT  SHORNO FROM " + DBNAME
					+ ".M3_SWRHEAD WHERE  SHCONO ='10' AND SHDIVI = '101' ORDER BY SHORNO DESC ";

			System.out.println("getID\n" + query);
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

	public static String getSWRNumber(String cono, String divi)
			throws Exception {
		logger.info("getSWRNumber");

		Connection conn = null;
		Statement stmt = null;
		try {

			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY SFORNO DESC) AS ID, SFORNO \n"
					+ "FROM " + DBNAME + ".M3_SWRFILE \n"
					+ "ORDER BY SFORNO DESC";
			// System.out.println("getMARNumber\n" + query);
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

	public static String checkivs(String IVS) throws Exception {
		logger.info("checkivs");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT \r\n"
					+ "    CASE \r\n"
					+ "        WHEN acstcf IS NULL THEN 'UNKNOWN'\r\n"
					+ "        WHEN acstcf IN (8, 9) THEN 'LOCK'\r\n"
					+ "        ELSE 'acstcf'\r\n"
					+ "    END AS result\r\n"
					+ "FROM M3FDBPRD.FCR040\r\n"
					+ "WHERE ACCINO LIKE '%" + IVS.trim() + "%'\r\n"
					+ "";
			System.out.println("SelectCompany\n" + query);
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

	public static String getWF() throws Exception {
		logger.info("getCompany");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = " SELECT H_ID ,H_TIMEIN FROM BRLDTABK01.VISITOR_HEAD vh \r\n"
					+ " WHERE  H_ID  = '23000001'";
			System.out.println("SelectCompany\n" + query);
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

	public static String getDepartment(String vCONO) throws Exception {
		logger.info("getCompany");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY CTL_UID) AS ID, UPPER(TRIM(CTL_UID)) AS US_LOGIN ,CTL_UID,TRIM(ST_EPNM)||' '||TRIM(ST_ENAME)||' '||TRIM(ST_ELNAME)AS fullname\r\n"
					+ "                   FROM BRLDTA0100.APPCTL1 a, BRLDTA0100.STAFFLIST b \r\n"
					+ "                  WHERE CTL_REM = 'AP'\r\n"
					+ "               	 AND b.ST_N6L3 = a.CTL_UID\r\n"
					+ "                AND b.ST_STS = '20' AND CTL_UID != '-' \r\n"
					+ "                AND ST_CONO  = '" + vCONO + "'";
			System.out.println("SelectCompany\n" + query);
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

	public static String getCompany() throws Exception {
		logger.info("getCompany");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY CCCONO) AS ID, CCCONO, CCDIVI, CCCONM, TRIM(CCCONO) || ' : ' || TRIM(CCDIVI) || ' : ' || TRIM(CCCONM) AS COMPANY\n"
					+ "FROM " + DBM3NAME + ".CMNDIV\n" + "WHERE  CCCONO IN ('10','600','500') AND CCDIVI != ''\n"
					+ "  ORDER BY CCCONO";
			System.out.println("SelectCompany\n" + query);
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

	public static String getCompanyWithConoDivi(String cono, String divi) throws Exception {
		logger.info("getCompanyWithConoDivi");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT CCCONO, CCDIVI, TRIM(CCTX15) AS CCCONM, CCROW3 AS CCCONM, TRIM(CCCONO) || ' : ' || TRIM(CCDIVI) || ' : ' || TRIM(CCROW3) AS COMPANY\n"
					+ "FROM " + DBM3NAME + ".CMNDIV \n" + "WHERE CCCONO = '" + cono + "' \n" + "AND CCDIVI = '" + divi
					+ "' \n"
					+ "AND CCDIVI != '' \n" + "ORDER BY CCCONO";
			// System.out.println("getCompanyWithConoDivi\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("CCCONM") + " ; " + mRes.getString("COMPANY");
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

	public static String getUserAuth(String cono, String divi, String username) throws Exception {
		logger.info("getUserAuth");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT US_DEPT \n" + "FROM " + Constant.DBNAME + ".STAFFAUTH a, " + Constant.DBNAME
					+ ".STAFFLIST b \n"
					+ "WHERE US_CONO = '" + cono + "' \n" + "AND US_DIVI = '" + divi + "' \n" + "AND US_LOGIN = '"
					+ username + "' \n" + "AND b.ST_N6L3 = a.US_LOGIN \n" + "AND b.ST_STS = '20' \n"
					+ "GROUP BY US_DEPT";
			// System.out.println("getUserAuth\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("US_DEPT").trim();
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

	public static String getWarehouse(String cono, String fac) throws Exception {
		logger.info("getWarehouse");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY MWWHLO) AS ID, MWWHLO, MWWHNM, TRIM(MWWHLO) || ' : ' || TRIM(MWWHNM)  AS WAREHOUSE \n"
					+ "FROM " + DBM3NAME + ".MITWHL \n" + "WHERE MWCONO = '" + cono + "' \n"
					// + "AND MWFACI = '" + fac + "' \n"
					+ "ORDER BY MWWHLO";
			// System.out.println("getWarehouse\n" + query);
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

	public static String getBU(String cono, String divi) throws Exception {
		logger.info("getBU");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY S1STID) AS ID, TRIM(S1STID) AS S1STID, S1TX40, TRIM(S1STID) || ' : ' || TRIM(S1TX40) AS BU \n"
					+ "FROM " + DBM3NAME + ".FSTDEF \n" + "WHERE S1CONO = '" + cono + "' \n" + "AND S1DIVI = '" + divi
					+ "' \n"
					+ "AND S1SLVL = '2' \n" + "ORDER BY S1STTP";
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

	public static String getCostCenterByBU(String cono, String divi, String bu) throws Exception {
		logger.info("getCostCenterByBU");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY S2AITM) AS ID, S2CONO, S2DIVI, TRIM(S2AITM) AS S2AITM, TRIM(S2STID) AS S2STID, SUBSTR(EATX40,38,3) AS EATX40, TRIM(S2AITM) || ' : ' || TRIM(S2TX15) || ' : ' || SUBSTR(EATX40,38,3) AS COSTCENTER \n"
					+ "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME + ".FCHACC b \n" + "WHERE S2CONO = '" + cono
					+ "' \n"
					+ "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '1' \n" + "AND b.EACONO = a.S2CONO \n"
					+ "AND b.EADIVI = a.S2DIVI \n" + "AND b.EAAITP = '2' \n" + "AND b.EAAITM = a.S2AITM \n"
					+ "AND S2STID IN (SELECT TRIM(S2AITM) \n" + "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME
					+ ".FSTDEF b \n"
					+ "WHERE S2CONO = '" + cono + "' \n" + "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '2' \n"
					+ "AND S2STID = '" + bu + "' \n" + "AND S1CONO = a.S2CONO \n" + "AND S1DIVI = a.S2DIVI \n"
					+ "AND S1STID = a.S2AITM \n" + "ORDER BY S2AITM) \n" + "ORDER BY S2AITM";
			// System.out.println("getCostCenterByBU\n" + query);
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

	public static String getAccountant(String cono, String divi) throws Exception {
		logger.info("getAccountant");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY CTL_UID) AS ID, UPPER(TRIM(CTL_UID)) AS ACCOUNTANT \n"
					+ "FROM " + Constant.DBNAME + ".APPCTL1 a, " + Constant.DBNAME + ".STAFFLIST b \n"
					+ "WHERE CTL_CODE = 'MAR' \n"
					+ "AND CTL_GRP = 'AC' \n" + "AND TRIM(b.ST_N6L3) = TRIM(a.CTL_UID) \n" + "AND b.ST_STS = '20' \n"
					+ "ORDER BY ACCOUNTANT";
			// System.out.println("getAccountant\n" + query);
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

	public static String getApprove() throws Exception {
		logger.info("getApprove");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY CTL_UID) AS ID, UPPER(TRIM(CTL_UID)) AS US_LOGIN \n"
					+ "FROM " + Constant.DBNAME + ".APPCTL1 a, " + Constant.DBNAME + ".STAFFLIST b \n"
					+ "WHERE CTL_REM = 'AP' \n"
					+ "AND TRIM(b.ST_N6L3) = TRIM(a.CTL_UID) \n" + "AND b.ST_STS = '20' \n" + "GROUP BY CTL_UID";
			// System.out.println("getApprove\n" + query);
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

	public static String getDepartmentAndCostcenter(String cono, String divi) throws Exception {
		logger.info("getDepartmentAndCostcenter");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT a.S2CONO, a.S2DIVI, a.S2STID,DEPT_DESCRIPTION, a.S2AITM, a.DESCRIPTION, b.BU \r\n"
					+ "FROM  \r\n"
					+ "(SELECT S2CONO, S2DIVI, TRIM(S2STID) AS S2STID, TRIM(S2AITM) AS S2AITM, TRIM(S2TX15) AS DESCRIPTION   \r\n"
					+ "FROM " + DBM3NAME + ".FSTLIN \r\n"
					+ "WHERE S2CONO = '10' \r\n"
					+ "AND S2DIVI = '101' \r\n"
					+ "AND S2SLVL = '1' \r\n"
					+ "--AND S2AITM LIKE 'C6%' \r\n"
					+ "ORDER BY S2AITM) AS a \r\n"
					+ "LEFT JOIN  \r\n"
					+ "(SELECT S2CONO, S2DIVI, TRIM(S2STID) AS BU, TRIM(S2AITM) AS DEPT , TRIM(S2TX15)  AS DEPT_DESCRIPTION\r\n"
					+ "FROM " + DBM3NAME + ".FSTLIN \r\n"
					+ "ORDER BY S2AITM) AS b \r\n"
					+ "ON b.S2CONO = a.S2CONO \r\n"
					+ "AND b.S2DIVI = a.S2DIVI \r\n"
					+ "AND b.DEPT = a.S2STID";
			// System.out.println("getDepartmentAndCostcenter\n" + query);
			logger.debug(query);
			;
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

	public static String getMonitoringReceipt(String cono, String divi) throws Exception {
		logger.info("getDepartmentAndCostcenter");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT  COALESCE(HR_CONO ,'-') AS CONO , COALESCE(HR_DIVI ,'-') AS DIVI , COALESCE(HC_RCNO,'-') AS RCNO , COALESCE(HC_VCNO ,'-') AS VCNO  , COALESCE(HC_FIXNO ,'-') AS FIXNO  FROM M3FDBTST.HR_RECEIPT hr \r\n"
					+ "WHERE  HR_CONO  = '11'\r\n"
					+ "AND HR_DIVI  = '111'\r\n"
					+ "ORDER  BY HC_RCNO  DESC limit 10";
			// System.out.println("getDepartmentAndCostcenter\n" + query);
			logger.debug(query);
			;
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

	public static String getItem(String cono, String divi, String whs, String type) throws Exception {
		logger.info("getItem");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String balance = "";
			if (type.equals("0")) {
				balance = "AND b.MBSTQT > 0 \n";
			}

			String query = "SELECT MMCONO, MBFACI, MBWHLO, MBWHSL, TRIM(MMITNO) AS MMITNO, TRIM(MMITDS) AS MMITDS, TRIM(MMFUDS) AS MMFUDS, TRIM(MMITNO) || ' : ' || TRIM(MMFUDS) AS ITEM, CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMUNMS, MMPUPR, MMVTCP, TRIM(MMCUCD) AS MMCUCD, TRIM(MBORTY) AS MBORTY \n"
					+ "FROM " + DBM3NAME + ".MITMAS a, " + DBM3NAME + ".MITBAL b \n" + "WHERE a.MMCONO = '" + cono
					+ "' \n" + "AND a.MMSTAT = '20' \n"
					// + "AND a.MMITTY = 'FG' \n"
					+ balance + "AND SUBSTRING(a.MMITNO,0,2) BETWEEN 'A' AND 'Z' \n" + "AND b.MBCONO = a.MMCONO \n"
					+ "AND b.MBITNO = a.MMITNO \n" + "AND b.MBWHLO = '" + whs + "' \n" + "AND b.MBSTAT = '20'\n"
					+ "ORDER BY MMITNO";
			// System.out.println("getItem\n" + query);
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

	public static String getItemDetail(String cono, String divi, String whs, String item) throws Exception {
		logger.info("getItemDetail");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY MLITNO, MLWHSL, MLIDDT, MLBANO) AS ID, MLCONO, MLFACI, MLWHLO, MLWHSL, MLIDDT, MLITNO, MMITDS , MLBANO, MLBREF, MMUNMS, MLSTQT, MLALQT, ROUND(MLSTQT - MLALQT, 2) AS ONHAND \n"
					+ "FROM " + DBM3NAME + ".MITLOC a, " + DBM3NAME + ".MITMAS b \n" + "WHERE MLCONO = " + cono + " \n"
					+ "AND MLWHLO = '" + whs + "' \n" + "AND MLITNO = '" + item + "' \n" + "AND MLSTAS = '2' \n"
					+ "AND b.MMCONO = a.MLCONO  \n" + "AND b.MMITNO = a.MLITNO  \n" + "AND b.MMSTAT = '20' \n"
					+ "ORDER BY MLITNO, MLWHSL, MLIDDT, MLBANO";
			// System.out.println("getItemDetail\n" + query);
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

	public static String getItemDetailV2(String cono, String divi, String whs, String item) throws Exception {
		logger.info("getItemDetailV2");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY MLITNO,MLWHSL,MLIDDT,MLBANO) AS ID,MLCONO,MLITNO,MMFUDS,MLFACI,MLWHLO,MLWHSL,MLIDDT,MLBANO,MLSTQT,MLALQT,ONHAND,MMUNMS,CAST(MTTRPR AS DECIMAL(17,4)) AS MTTRPR,CAST(ONHAND * MTTRPR AS DECIMAL(17,4)) AS AMT \n"
					+ "FROM \n"
					+ "(SELECT MLCONO,TRIM(MLITNO) AS MLITNO,TRIM(MMFUDS) AS MMFUDS,MLFACI,TRIM(MLWHLO) AS MLWHLO,TRIM(MLWHSL) AS MLWHSL,SUBSTRING(MLIDDT,1,4)||'-'||SUBSTRING(MLIDDT,5,2)||'-'||SUBSTRING(MLIDDT,7,2) AS MLIDDT,TRIM(MLBANO) AS MLBANO,MLSTQT,MLALQT,CAST(MLSTQT - MLALQT AS DECIMAL(17,4)) AS ONHAND,MMUNMS \n"
					+ "FROM " + DBM3NAME + ".MITLOC A," + DBM3NAME + ".MITMAS M \n" + "WHERE MLCONO = '" + cono + "' \n"
					+ "AND MLWHLO = '" + whs + "' \n" + "AND MLITNO = '" + item + "' \n" + "AND MLSTAS = '2' \n"
					+ "AND M.MMCONO = A.MLCONO \n" + "AND M.MMITNO = A.MLITNO \n" + "AND M.MMSTAT = '20' \n"
					+ "AND TRIM(MLITNO) || TRIM(MLBANO) NOT IN (SELECT TRIM(MLITNO) || TRIM(MLLOTN) AS ITEMLOT \n"
					+ "FROM " + Constant.DBNAME + ".M3_MARDETAIL \n" + "WHERE MLCONO = '" + cono + "' \n"
					+ "AND MLDIVI = '" + divi
					+ "' \n" + "AND MLWHLO = '" + whs + "')" + "ORDER BY MLITNO,MLBANO) AS a \n" + "LEFT JOIN \n"
					+ "(SELECT MTCONO,MTWHLO,MTITNO,MTBANO,MTTRPR \n" + "FROM " + DBM3NAME + ".MITTRA \n"
					+ "GROUP BY MTCONO,MTWHLO,MTITNO,MTBANO,MTTRPR) AS b \n" + "ON b.MTCONO = a.MLCONO \n"
					+ "AND b.MTWHLO = a.MLWHLO \n" + "AND b.MTITNO = a.MLITNO \n" + "AND b.MTBANO = a.MLBANO \n"
					+ "ORDER BY MLITNO,MLWHSL,MLIDDT,MLBANO";
			// System.out.println("getItemDetail\n" + query);
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

	public static String getMARNumber(String cono, String divi, String fromstatus, String tostatus, String username)
			throws Exception {
		logger.info("getMARNumber");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT ROW_NUMBER() OVER(ORDER BY MHPREF,MHORNO DESC) AS ID, MHORNO, MHPREF || '-' || MHORNO AS MARNUMBER \n"
					+ "FROM " + Constant.DBNAME + ".M3_MARHEAD \n" + "WHERE MHCONO = '" + cono + "' \n"
					+ "AND MHDIVI = '" + divi
					+ "' \n" + "AND MHREQU = '" + username + "' \n" + "AND MHSTAT BETWEEN '" + fromstatus + "' AND '"
					+ tostatus + "' \n" + "ORDER BY MHPREF,MHORNO DESC";
			// System.out.println("getMARNumber\n" + query);
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

	public static String getMaxMARNumber(String cono, String divi, String prefix, String year, String username)
			throws Exception {
		logger.info("getMaxMARNumber");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT MAX(MHPREF || '-' || MHORNO) AS MARNUMBER \n" + "FROM " + Constant.DBNAME
					+ ".M3_MARHEAD \n"
					+ "WHERE MHCONO = '" + cono + "' \n" + "AND MHDIVI = '" + divi + "' \n" + "AND MHPREF = '" + prefix
					+ "' \n" + "AND SUBSTRING(CHAR(MHREDA,ISO),0,5) = '" + year + "' \n" + "AND MHREQU = '" + username
					+ "'";
			// System.out.println("getMaxMARNumber\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("MARNUMBER").trim();
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

	public static String getMARHead(String cono, String divi, String marno, String fromstatus, String tostatus)
			throws Exception {
		logger.info("getMARHead");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT MHCONO, MHDIVI, MHPREF, MHPREF || '-' || MHORNO AS MARNUMBER, MHORNO, CHAR(MHREDA, ISO) AS MHREDA, CHAR(MHPODA, ISO) AS MHPODA, MHMONT, MHTYPE, MHBU, MHCOCE, TRIM(S2AITM) || ' : ' || TRIM(S2TX15) || ' : ' || SUBSTR(EATX40,38,3) AS COSTCENTER\n"
					+ ", MHACCT, MHREQU, MHREM1, MHREM2, MHAPP1, MHAPDA1, MHAPP2, MHAPDA2, MHAPP3, MHAPDA3, MHAPP4, MHAPDA4, MHAPP5, MHAPDA5, MHAPICT, MHICTDA, MHAPCIO, MHCIODA, MHACCRE, MHAPRE1, MHAPRE2, MHAPRE3, MHAPRE4, MHAPRE5, MHICTRE, MHCIORE, CHAR(MHENDA, ISO) AS MHENDA, MHENTI, MHSTAT, MHENUS \n"
					+ "FROM " + Constant.DBNAME + ".M3_MARHEAD a, " + DBM3NAME + ".FSTLIN b, " + DBM3NAME
					+ ".FCHACC c \n" + "WHERE MHCONO = '"
					+ cono + "' \n" + "AND MHDIVI = '" + divi + "' \n" + "AND MHPREF || '-' || MHORNO = '" + marno
					+ "' \n" + "AND b.S2CONO = a.MHCONO \n" + "AND b.S2DIVI = a.MHDIVI \n"
					+ "AND b.S2AITM = a.MHCOCE \n" + "AND c.EACONO = a.MHCONO \n" + "AND c.EADIVI = a.MHDIVI \n"
					+ "AND c.EAAITP = '2' \n" + "AND c.EAAITM = a.MHCOCE ";
			// System.out.println("getMARHead\n" + query);
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

	public static String getMARDetail(String cono, String divi, String marno, String fromstatus, String tostatus)
			throws Exception {
		logger.info("getMARDetail");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT MLCONO, MLDIVI, MLPREF, MLORNO, MLRENO, MLTYPE, MLFACI, MLWHLO, MLLINE, MLITNO, MLITDE, MLLOCA, MLLOTN, CHAR(MLDATE, ISO) AS MLDATE, MLUNIT, CAST(MLQTY AS DECIMAL(17,4)) AS MLQTY, CAST(MLPRIC AS DECIMAL(17,4)) AS MLPRIC,CAST(MLQTY * MLPRIC AS DECIMAL(17,4)) AS AMT, MLREM1, MLREM2, CHAR(MLENDA, ISO) AS MLENDA, MLENTI, MLSTAT, MLENUS  \n"
					+ "FROM " + Constant.DBNAME + ".M3_MARDETAIL \n" + "WHERE MLCONO = '10' \n"
					+ "AND MLDIVI = '101' \n"
					+ "AND MLSTAT BETWEEN '" + fromstatus + "' AND '" + tostatus + "' \n"
					+ "AND MLPREF || '-' || MLORNO = '" + marno + "' \n" + "ORDER BY MLLINE";
			// System.out.println("getMARDetail\n" + query);
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

	public static String getMARFile(String cono, String divi, String marno) throws Exception {
		logger.info("getMARFile");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT MFCONO, MFDIVI, MFPREF, MFORNO, MFLINE, MFNAME, MFTYPE, MFREM1, MFREM2, MFENDA, MFENTI, MFENUS \n"
					+ "FROM " + Constant.DBNAME + ".M3_MARFILE \n" + "WHERE MFCONO = '" + cono + "' \n"
					+ "AND MFDIVI = '" + divi
					+ "' \n" + "AND MFPREF || '-' || MFORNO = '" + marno + "' \n" + "ORDER BY MFLINE";
			// System.out.println("getMARFile\n" + query);
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

	public static String getMaxMARFileLine(String cono, String divi, String marno) throws Exception {
		logger.info("getMaxMARFileLine");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT COALESCE(MAX(MFLINE),0) + 1 AS MFLIN \n" + "FROM " + Constant.DBNAME
					+ ".M3_MARFILE \n"
					+ "WHERE MFCONO = '" + cono + "' \n" + "AND MFDIVI = '" + divi + "' \n"
					+ "AND MFPREF || '-' || MFORNO = '" + marno + "'";
			// System.out.println("getCompanyWithConoDivi\n" + query);
			logger.debug(query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				return mRes.getString("MFLIN");
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String getMonth(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ROW_NUMBER() OVER(ORDER BY MONTH) AS ID, MONTH \n" + "FROM \n"
						+ "(SELECT YEAR(THDEDA) || SUBSTRING(CHAR(THDEDA, ISO),6,2) AS MONTH \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi
						+ "') AS a   \n" + "GROUP BY MONTH \n" + "ORDER BY MONTH DESC";
				// System.out.println("getMonth\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

			} else {
				logger.error("Server can't connect.");
			}

		} catch (SQLException sqle) {
			logger.error(sqle.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
			if (conn != null) {
				conn.close();
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return null;

	}

	public static String getADRNumber(String cono, String divi, String username, String fromstatus, String tostatus)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ROW_NUMBER() OVER(ORDER BY ADORNO) AS ID, ADORNO, ADPREF || '-' || ADORNO AS ADRNUMBER \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD \n" + "WHERE ADCONO = '" + cono + "' \n"
						+ "AND ADDIVI = '" + divi
						+ "' \n" + "AND ADREQU = '" + username + "' \n" + "AND ADSTAT BETWEEN '" + fromstatus
						+ "' AND '" + tostatus + "' \n" + "ORDER BY ADPREF,ADORNO DESC";
				// System.out.println("getADRNumber\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException e) {
			throw e;
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

	public static String getADRNumberAccountant(String cono, String divi, String username, String fromstatus,
			String tostatus) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ROW_NUMBER() OVER(ORDER BY ADORNO) AS ID, ADORNO, ADPREF || '-' || ADORNO AS ADRNUMBER \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD \n" + "WHERE ADCONO = '" + cono + "' \n"
						+ "AND ADDIVI = '" + divi
						+ "' \n" + "AND ADACCT = '" + username + "' \n" + "AND ADSTAT BETWEEN '" + fromstatus
						+ "' AND '" + tostatus + "' \n" + "ORDER BY ADPREF,ADORNO DESC";
				// System.out.println("getADRNumberAccountant\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException e) {
			throw e;
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

	public static String getADRHead(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, String fromstatus, String tostatus) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ADCONO, ADDIVI, ADPREF || '-' || ADORNO AS ADRNUMBER, ADORNO, CHAR(ADDATE, ISO) AS ADDATE, ADMONT, ADTYPE, ADPREF, ADBOI, ADBU, ADCOCE, TRIM(S2AITM) || ' : ' || TRIM(S2TX15) || ' : ' || SUBSTR(EATX40,38,3) AS COSTCENTER, ADVAT, ADACCT, ADREQU, ADREM1, ADREM2 \n"
						+ ", ADIMAG, ADAPP1, ADAPDA1, ADAPP2, ADAPDA2, ADAPP3, ADAPDA3, ADAPP4, ADAPDA4, ADACRE1, ADAPRE1, ADAPRE2, ADAPRE3, ADAPRE4, ADENDA, ADENTI, ADSTAT, ADENUS \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD a, " + DBM3NAME + ".FSTLIN b, " + DBM3NAME
						+ ".FCHACC c \n" + "WHERE ADCONO = '"
						+ cono + "' \n" + "AND ADDIVI = '" + divi + "' \n" + "AND ADPREF || '-' || ADORNO = '" + adrno
						+ "' \n" + "AND b.S2CONO = a.ADCONO \n" + "AND b.S2DIVI = a.ADDIVI \n"
						+ "AND b.S2AITM = a.ADCOCE\n" + "AND c.EACONO = a.ADCONO \n" + "AND c.EADIVI = a.ADDIVI \n"
						+ "AND c.EAAITP = '2' \n" + "AND c.EAAITM = a.ADCOCE ";
				// System.out.println("getADRHead\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				// return ConvertResultSet.convertResultSetToJson(mRes);
				return ConvertResultSet.convertResultSetToJsonV2(httpServletRequest, mRes);

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException e) {
			throw e;
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

	public static String getADRHeadMonitoring(String cono, String divi, String fromstatus, String tostatus)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ADCONO, ADDIVI, ADPREF || '-' || ADORNO AS ADRNUMBER, ADORNO, CHAR(ADDATE, ISO) AS ADDATE, ADMONT, ADTYPE, ADPREF, ADBOI, ADBU, ADCOCE, TRIM(S2AITM) || ' : ' || TRIM(S2TX15) || ' : ' || SUBSTR(EATX40,38,3) AS COSTCENTER, ADVAT, ADACCT, ADREQU, ADREM1, ADREM2 \n"
						+ ", ADAPP1, CHAR(ADAPDA1, ISO) AS ADAPDA1, ADAPP2, CHAR(ADAPDA2, ISO) AS ADAPDA2, ADAPP3, CHAR(ADAPDA3, ISO) AS ADAPDA3, ADAPP4, CHAR(ADAPDA4, ISO) AS ADAPDA4, ADACRE1, ADAPRE1, ADAPRE2, ADAPRE3, ADAPRE4, ADENDA, ADENTI, ADSTAT, ADENUS \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD a, " + DBM3NAME + ".FSTLIN b, " + DBM3NAME
						+ ".FCHACC c \n" + "WHERE ADCONO = '"
						+ cono + "' \n" + "AND ADDIVI = '" + divi + "' \n"
						// + "AND ADPREF || '-' || ADORNO = '"+adrno+"' \n"
						+ "AND b.S2CONO = a.ADCONO \n" + "AND b.S2DIVI = a.ADDIVI \n" + "AND b.S2AITM = a.ADCOCE\n"
						+ "AND c.EACONO = a.ADCONO \n" + "AND c.EADIVI = a.ADDIVI \n" + "AND c.EAAITP = '2' \n"
						+ "AND c.EAAITM = a.ADCOCE \n" + "ORDER BY ADDATE DESC, ADPREF || '-' || ADORNO DESC";
				// System.out.println("getADRHeadMonitoring\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException e) {
			throw e;
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

	public static String getADRDetail(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, String fromstatus, String tostatus) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ALCONO, ALDIVI, ALPREF || '-' || ALORNO AS ADRNUMBER, ALORNO, ALVHNO, ALIVNO, ALPREF, ALLINE, ALITNO, TRIM(b.FMTXT1) AS ALITDE, TRIM(FMASID) || ' : ' || FMSBNO || ' : ' || TRIM(FMTXT1) AS ITEM, ALSBNO, ALCOST, CHAR(ALDATE, ISO) AS ALDATE \n"
						+ ", CAST(ALACOS AS DECIMAL(15,2)) AS ALACOS, CAST(ALNETV AS DECIMAL(15,2)) AS ALNETV, CAST(ALQTY AS DECIMAL(15,2)) AS ALQTY, ALPRIC, ALREM1, ALREM2, ALIMAG, ALENDA, ALENTI, ALSTAT, ALENUS \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRDETAIL a, " + DBM3NAME + ".FFASMA b  \n"
						+ "WHERE ALCONO = '" + cono + "' \n"
						+ "AND ALDIVI = '" + divi + "' \n" + "AND ALSTAT BETWEEN '" + fromstatus + "' AND '" + tostatus
						+ "' \n" + "AND b.FMCONO = a.ALCONO \n" + "AND b.FMDIVI = a.ALDIVI \n"
						+ "AND b.FMASID = a.ALITNO  \n" + "AND b.FMSBNO = a.ALSBNO \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "' \n" + "ORDER BY ALORNO, ALLINE";
				// System.out.println("getADRDetail\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				// return ConvertResultSet.convertResultSetToJson(mRes);
				return ConvertResultSet.convertResultSetToJsonV2(httpServletRequest, mRes);

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException e) {
			throw e;
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

	public static List<String> getADRRequester(String cono, String divi, String adrno) throws Exception {

		List<String> getListData = new ArrayList<String>();
		Connection conn = ConnectDB2.doConnect();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT ADREQU, FULLNAME, EMAIL \n" + "FROM \n" + "(SELECT ADREQU \n"
					+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD \n" + "WHERE ADCONO = '" + cono + "' \n"
					+ "AND ADDIVI = '" + divi
					+ "' \n" + "AND ADPREF || '-' || ADORNO = '" + adrno + "') AS a \n" + "LEFT JOIN \n"
					+ "(SELECT DISTINCT ST_CONO, ST_N6L3, RTRIM(ST_ENAME) || '  ' || RTRIM(ST_ELNAME) AS FULLNAME, ST_EMAIL AS EMAIL \n"
					+ "FROM " + Constant.DBNAME + ".STAFFLIST) AS b \n" + "ON b.ST_N6L3 = a.ADREQU";
			// System.out.println("getADRDetailRequester\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("ADREQU").trim() + " ; " + mRes.getString("FULLNAME").trim() + " ; "
						+ mRes.getString("EMAIL").trim());

			}

			return getListData;

		} catch (Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static List<String> getADRAccountant(String cono, String divi, String adrno) throws Exception {

		List<String> getListData = new ArrayList<String>();
		Connection conn = ConnectDB2.doConnect();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT ADACCT, FULLNAME, EMAIL \n" + "FROM \n" + "(SELECT ADACCT \n"
					+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD \n" + "WHERE ADCONO = '" + cono + "' \n"
					+ "AND ADDIVI = '" + divi
					+ "' \n" + "AND ADPREF || '-' || ADORNO = '" + adrno + "') AS a \n" + "LEFT JOIN \n"
					+ "(SELECT DISTINCT ST_CONO, ST_N6L3, RTRIM(ST_ENAME) || '  ' || RTRIM(ST_ELNAME) AS FULLNAME, ST_EMAIL AS EMAIL \n"
					+ "FROM " + Constant.DBNAME + ".STAFFLIST) AS b \n" + "ON b.ST_N6L3 = a.ADACCT";
			// System.out.println("getADRDetailAccountant\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("ADACCT").trim() + " ; " + mRes.getString("FULLNAME").trim() + " ; "
						+ mRes.getString("EMAIL").trim());

			}

			return getListData;

		} catch (Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static List<String> getADRDeptHead(String cono, String divi, String adrno) throws Exception {

		List<String> getListData = new ArrayList<String>();
		Connection conn = ConnectDB2.doConnect();
		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT ADAPP1, FULLNAME, EMAIL \n" + "FROM \n" + "(SELECT ADAPP1 \n"
					+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD \n" + "WHERE ADCONO = '" + cono + "' \n"
					+ "AND ADDIVI = '" + divi
					+ "' \n" + "AND ADPREF || '-' || ADORNO = '" + adrno + "') AS a \n" + "LEFT JOIN \n"
					+ "(SELECT DISTINCT ST_CONO, ST_N6L3, RTRIM(ST_ENAME) || '  ' || RTRIM(ST_ELNAME) AS FULLNAME, ST_EMAIL AS EMAIL \n"
					+ "FROM " + Constant.DBNAME + ".STAFFLIST) AS b \n" + "ON b.ST_N6L3 = a.ADAPP1";
			// System.out.println("getDetailDeptHead\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("ADAPP1").trim() + " ; " + mRes.getString("FULLNAME").trim() + " ; "
						+ mRes.getString("EMAIL").trim());

			}

			return getListData;

		} catch (Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static List<String> getADRApprove(String cono, String divi, String adrno, String status) throws Exception {

		List<String> getListData = new ArrayList<String>();
		Connection conn = ConnectDB2.doConnect();
		try {

			String setApprove = "";
			if (status.equals("15")) {
				setApprove = "ADAPP1";

			} else if (status.equals("20")) {
				setApprove = "ADAPP2";

			} else if (status.equals("25") || status.equals("30")) {
				setApprove = "ADAPP3";

			} else if (status.equals("35")) {
				setApprove = "ADAPP4";

			}

			Statement stmt = conn.createStatement();
			String query = "SELECT APPROVE, FULLNAME, EMAIL \n" + "FROM \n" + "(SELECT " + setApprove + " AS APPROVE \n"
					+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD \n" + "WHERE ADCONO = '" + cono + "' \n"
					+ "AND ADDIVI = '" + divi
					+ "' \n" + "AND ADPREF || '-' || ADORNO = '" + adrno + "') AS a \n" + "LEFT JOIN \n"
					+ "(SELECT DISTINCT ST_CONO, ST_N6L3, RTRIM(ST_ENAME) || '  ' || RTRIM(ST_ELNAME) AS FULLNAME, ST_EMAIL AS EMAIL \n"
					+ "FROM " + Constant.DBNAME + ".STAFFLIST) AS b \n" + "ON b.ST_N6L3 = a.APPROVE";
			// System.out.println("getADRDetailApprove\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("APPROVE").trim() + " ; " + mRes.getString("FULLNAME").trim() + " ; "
						+ mRes.getString("EMAIL").trim());

			}

			return getListData;

		} catch (Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static Integer getADRCountApprove(String cono, String divi, String prno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT CASE WHEN HD_APP1 != '' AND HD_APP2 = '' AND HD_APP3 = '' AND HD_APP4 = '' THEN '1' \n"
						+ "WHEN HD_APP1 != '' AND HD_APP2 != '' AND HD_APP3 = '' AND HD_APP4 = '' THEN '2' \n"
						+ "WHEN HD_APP1 != '' AND HD_APP2 != '' AND HD_APP3 != '' AND HD_APP4 = '' THEN '3' \n"
						+ "WHEN HD_APP1 != '' AND HD_APP2 != '' AND HD_APP3 != '' AND HD_APP4 != '' THEN '4' \n"
						+ "ELSE '0' END AS COUNT \n" + "FROM " + Constant.DBNAME + ".M3_PRHEADNEW mp \n"
						+ "WHERE HD_IBCONO = '"
						+ cono + "' \n" + "AND HD_IBDIVI = '" + divi + "' \n" + "AND HD_IBPLPN = '" + prno + "'";
				// System.out.println("getCountApprove\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getInt("COUNT");
				}

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

	public static String getADRCheckApprove(String cono, String divi, String prno, String status) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				int getCountApprove = getADRCountApprove(cono, divi, prno);
				// System.out.println("getCountApprove: " + getCountApprove);

				if (getCountApprove > 0) {

					int count = 0;
					for (int i = 2; i <= getCountApprove; i++) { // int 2 = Approve2 ++
						String query = "SELECT COUNT(*) AS COUNT \n" + "FROM " + Constant.DBNAME + ".M3_PRHEADNEW mp \n"
								+ "WHERE HD_IBCONO = '" + cono + "' \n" + "AND HD_IBDIVI = '" + divi + "' \n"
								+ "AND HD_IBPLPN = '" + prno + "' \n" + "AND HD_APPDT" + i + " != 0";
						// System.out.println("getCheckApprove\n" + query);
						ResultSet mRes = stmt.executeQuery(query);

						while (mRes.next()) {
							count = mRes.getInt("COUNT");
							if (count > 0) {
								continue;
							} else {
								return String.valueOf(count);
							}

						}

					}

					return String.valueOf(count);

				}

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

	public static String getADREmailRequest(String cono, String divi, String prno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ST_N6L3, ST_EMAIL \n" + "FROM \n" + "(SELECT HD_IBPURC \n"
						+ "FROM " + Constant.DBNAME + ".M3_PRHEADNEW mp \n" + "WHERE HD_IBCONO = '" + cono + "' \n"
						+ "AND HD_IBDIVI = '" + divi + "' \n" + "AND HD_IBPLPN = '" + prno + "' \n"
						+ "GROUP BY HD_IBPURC) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT RTRIM(ST_N6L3) AS ST_N6L3, RTRIM(ST_EMAIL) AS ST_EMAIL \n"
						+ "FROM " + Constant.DBNAME + ".STAFFLIST) AS b \n" + "ON b.ST_N6L3 = a.HD_IBPURC";
				// System.out.println("getEmailRequest\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("ST_N6L3").trim() + " ; " + mRes.getString("ST_EMAIL").trim();
				}

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

	public static List<String> getADREmailPuchase(String cono, String divi, String prno) throws Exception {

		List<String> getListData = new ArrayList<String>();
		Connection conn = ConnectDB2.doConnect();
		try {
			Statement stmt = conn.createStatement();

			String query = "SELECT ST_N6L3, ST_EMAIL \n" + "FROM \n" + "(SELECT PR_IBBUYE \n"
					+ "FROM " + Constant.DBNAME + ".M3_PLANPRLINE mp \n" + "WHERE PR_IBCONO = '" + cono + "' \n"
					+ "AND PR_IBDIVI = '" + divi + "' \n" + "AND PR_IBPLPN = '" + prno + "' \n"
					+ "GROUP BY PR_IBBUYE) AS a \n" + "LEFT JOIN \n"
					+ "(SELECT RTRIM(ST_N6L3) AS ST_N6L3, RTRIM(ST_EMAIL) AS ST_EMAIL \n"
					+ "FROM " + Constant.DBNAME + ".STAFFLIST) AS b \n" + "ON b.ST_N6L3 = a.PR_IBBUYE";
			// System.out.println("getEmailPuchase\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("ST_N6L3").trim() + " ; " + mRes.getString("ST_EMAIL").trim());
			}

			return getListData;

		} catch (

		Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static Double getADRAmount(String cono, String divi, String adrno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT SUM(ALACOS) AS AMT \n" + "FROM " + Constant.DBNAME + ".M3_ADRDETAIL \n"
						+ "WHERE ALCONO = '"
						+ cono + "' \n" + "AND ALDIVI = '" + divi + "' \n"
						// + "AND ALSTAT BETWEEN '10' AND '10' \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "'";
				// System.out.println("getADRAmountV2\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getDouble("AMT");
				}

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

		return 0.00;

	}

	public static String getYear(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ROW_NUMBER() OVER(ORDER BY YEAR) AS ID, YEAR \n" + "FROM \n"
						+ "(SELECT YEAR(THDEDA) || SUBSTRING(CHAR(THDEDA, ISO),6,0) AS YEAR \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi
						+ "') AS a   \n" + "GROUP BY YEAR \n" + "ORDER BY YEAR DESC";
				// System.out.println("getMonth\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getCredit(String cono, String divi, String credit) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ROW_NUMBER() OVER(ORDER BY OAOBLC) AS ID, OAOBLC, CASE WHEN OAOBLC = 0 THEN '0 : No stop' \n"
						+ "WHEN OAOBLC = 1 THEN '1 : Credit limit 1 (Overdue)' \n"
						+ "WHEN OAOBLC = 2 THEN '2 : Credit limit 2 (Total outstanding)' \n"
						+ "WHEN OAOBLC = 3 THEN '3 : Credit limit 3 (Total outstanding + order stock)' \n"
						+ "WHEN OAOBLC = 4 THEN '4 : Credit limit 4 (Number of days overdue)' \n"
						+ "WHEN OAOBLC = 5 THEN '5 : Connected to letter of credit' \n"
						+ "WHEN OAOBLC = 6 THEN '6 : Time fenced credit limit issue' \n"
						+ "WHEN OAOBLC = 7 THEN '7 : Manually set stop for optional use' \n"
						+ "WHEN OAOBLC = 8 THEN '8 : Manually set stop for optional use' \n"
						+ "WHEN OAOBLC = 9 THEN '9 : Manually released order' END AS CREDIT \n" + "FROM " + DBM3NAME
						+ ".OOHEAD \n" + "WHERE OACONO = '" + cono + "' \n" + "AND OADIVI = '" + divi + "' \n"
						+ "AND OAOBLC IN (" + credit + ") \n" + "GROUP BY OAOBLC \n" + "ORDER BY OAOBLC";
				// System.out.println("getCredit\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getStatus(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ROW_NUMBER() OVER(ORDER BY THSTAS) AS ID, THSTAS AS STATUS \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + 10 + "' \n" + "AND THDIVI = '" + 101 + "' \n"
						+ "GROUP BY THSTAS \n" + "ORDER BY THSTAS";
				// System.out.println("getStatus\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getUserM3(String cono, String divi, String dept) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT UA_USER, UA_PASS \n" + "FROM " + Constant.DBNAME + ".M3_USERADDON \n"
						+ "WHERE UA_CONO = '"
						+ cono + "' \n" + "AND UA_DIVI = '" + divi + "' \n" + "AND UA_DEPT = '" + dept + "'";
				// System.out.println("getUserM3\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("UA_USER").trim() + " ; " + mRes.getString("UA_PASS").trim();
				}

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

	public static String getCustomer(String cono, String divi, String saleman) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				String textSql = "";
				if (saleman.equals("WATTAN_SAE")) {
					textSql = "AND OKSMCD != '' \n";
				} else {
					textSql = "AND OKSMCD = '" + saleman + "' \n";
				}

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER(ORDER BY OKCFC3,A.OKCONO,A.OKCUNO) AS ID,A.OKCONO,A.OKCFC3 CHANEL,CH.CTTX15 NCHANEL,A.OKCUCL GROUPC,TRIM(G.CTTX15) AS NGROUP,CASE WHEN OKCUNM LIKE '%à§Ô¹Ê´%' THEN 'YES' ELSE 'NO' END AS DCASH, \n"
						+ "A.OKCUNO CUST,A.OKCUNM,A.OKCUNO || ' : ' || A.OKCUNM AS CUSTOMER,A.OKPYNO PAYER,A.OKCUA1 ADDR1,A.OKCUA2 ADDR2,A.OKCUA3 ADDR3,A.OKCUA4 ADDR4,A.OKPHNO TEL,A.OKECAR AREA,R.CTTX15 NAREA, \n"
						+ "A.OKSMCD SALESMAN,S.CTTX15 NSALES,A.OKCRLM CREDIT1,A.OKCRL2 CREDIT2,A.OKCRL3 CREDIT3,A.OKTEPY PAYMENT,T.CTTX15 NPAYMENT,A.OKSTAT STATUS,TRIM(A.OKPLTB) AS PRICELIST, \n"
						+ "A.OKDISY DISC,A.OKCFC3 CHANEL,CH.CTTX15 NCHANEL,A.OKRGDT CREAT_DATE,A.OKCHID USERUPDATE,A.OKLMDT AS LAST_DATE \n"
						+ "FROM \n"
						+ "(SELECT OKCONO,OKCUNO,OKCUNM, OKPYNO,OKCFC3,OKCUCL,OKCUA1,OKCUA2,OKCUA3,OKCUA4,OKPHNO,OKECAR,OKCFC9,OKSMCD,OKCRLM,OKCRL2,OKCRL3,OKTEPY,OKPYCD,OKPLTB,OKDISY,OKLMDT,OKCHID,OKRGDT,OKSTAT \n"
						+ "FROM " + DBM3NAME + ".OCUSMA \n" + "WHERE OKCONO = '" + cono + "' \n"
						+ "AND OKSTAT = '20' \n"
						// + "AND OKSMCD = '" + saleman + "' \n"
						+ textSql
						+ "AND (OKCUCL BETWEEN '00' AND '13' OR OKCUCL BETWEEN '0A' AND '0Z' OR OKCUCL BETWEEN '1A' AND '1Z')) A \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS G ON G.CTSTKY = A.OKCUCL AND G.CTCONO = A.OKCONO AND G.CTSTCO ='CUCL' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS R ON R.CTSTKY = A.OKECAR AND R.CTCONO = A.OKCONO AND R.CTSTCO ='ECAR' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS T ON T.CTSTKY = A.OKTEPY AND T.CTCONO = A.OKCONO AND T.CTSTCO ='TEPY' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS CH ON CH.CTSTKY = A.OKCFC3 AND CH.CTCONO = A.OKCONO AND CH.CTSTCO ='CFC3' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS S ON S.CTSTKY = A.OKSMCD AND S.CTCONO = A.OKCONO AND S.CTSTCO ='SMCD' \n"
						+ "ORDER BY OKCFC3,A.OKCONO,A.OKCUNO";
				// System.out.println("getCustomer\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getCustomerV2(String cono, String divi, String saleman) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				String textSql = "";
				if (saleman.equals("WATTAN_SAE")) {
					textSql = "AND OKSMCD != '' \n";
				} else {
					textSql = "AND OKSMCD = '" + saleman + "' \n";
				}

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER(ORDER BY OKCFC3,A.OKCONO,A.OKCUNO) AS ID,A.OKCONO,A.OKCFC3 CHANEL,CH.CTTX15 NCHANEL,A.OKCUCL GROUPC,TRIM(G.CTTX15) AS NGROUP,CASE WHEN OKCUNM LIKE '%เงินสด%' THEN 'YES' ELSE 'NO' END AS DCASH, \n"
						+ "A.OKCUNO CUST,A.OKCUNM,A.OKCUNO || ' : ' || A.OKCUNM AS CUSTOMER,A.PAYER,A.OKCUA1 ADDR1,A.OKCUA2 ADDR2,A.OKCUA3 ADDR3,A.OKCUA4 ADDR4,A.OKPHNO TEL,A.OKECAR AREA,R.CTTX15 NAREA, \n"
						+ "A.OKSMCD SALESMAN,S.CTTX15 NSALES,A.OKCRLM CREDIT1,A.OKCRL2 CREDIT2,A.OKCRL3 CREDIT3,CCODIN, CCOINA, CCOVNI, CCOINA + CCOVNI AS BACKLOG, OKCRL3 - (CCOINA + CCOVNI) AS DIFFERENCES,A.OKTEPY PAYMENT,T.CTTX15 NPAYMENT,A.OKSTAT STATUS,TRIM(A.OKPLTB) AS PRICELIST, \n"
						+ "A.OKDISY DISC,A.OKCFC3 CHANEL,CH.CTTX15 NCHANEL,A.OKRGDT CREAT_DATE,A.OKCHID USERUPDATE,A.OKLMDT AS LAST_DATE \n"
						+ "FROM \n"
						+ "(SELECT OKCONO,OKCUNO,OKCUNM, OKPYNO,OKCFC3,OKCUCL,OKCUA1,OKCUA2,OKCUA3,OKCUA4,OKPHNO,OKECAR,OKCFC9,OKSMCD,OKCRLM,OKCRL2,OKCRL3,OKTEPY,OKPYCD,OKPLTB,OKDISY,OKLMDT,OKCHID,OKRGDT,OKSTAT, CASE WHEN OKPYNO = '' THEN OKCUNO ELSE OKPYNO END AS PAYER \n"
						+ "FROM " + DBM3NAME + ".OCUSMA \n" + "WHERE OKCONO = '" + cono + "' \n"
						+ "AND OKSTAT = '20' \n"
						// + "AND OKSMCD = '" + saleman + "' \n"
						+ textSql
						+ "AND (OKCUCL BETWEEN '00' AND '13' OR OKCUCL BETWEEN '0A' AND '0Z' OR OKCUCL BETWEEN '1A' AND '1Z')) A \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS G ON G.CTSTKY = A.OKCUCL AND G.CTCONO = A.OKCONO AND G.CTSTCO ='CUCL' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS R ON R.CTSTKY = A.OKECAR AND R.CTCONO = A.OKCONO AND R.CTSTCO ='ECAR' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS T ON T.CTSTKY = A.OKTEPY AND T.CTCONO = A.OKCONO AND T.CTSTCO ='TEPY' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS CH ON CH.CTSTKY = A.OKCFC3 AND CH.CTCONO = A.OKCONO AND CH.CTSTCO ='CFC3' \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB AS S ON S.CTSTKY = A.OKSMCD AND S.CTCONO = A.OKCONO AND S.CTSTCO ='SMCD' \n"
						+ "LEFT JOIN " + DBM3NAME + ".CCUCRL AS P ON P.CCCONO = A.OKCONO AND P.CCPYNO = A.PAYER \n"
						+ "ORDER BY OKCFC3,A.OKCONO,A.OKCUNO";
				// System.out.println("getCustomer\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static boolean getCustomerCashCode(String cono, String divi, String customerno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT OKCONO,OKCUNO,OKCUNM,CASE WHEN OKCUNM LIKE '%เงินสด%' THEN 'YES' ELSE 'NO' END AS DCASH \n"
						+ "FROM " + DBM3NAME + ".OCUSMA \n" + "WHERE OKCONO = '" + cono + "' \n" + "AND OKCUNO = '"
						+ customerno + "' \n" + "AND OKSTAT = '20' \n"
						+ "AND (OKCUCL BETWEEN '00' AND '13' OR OKCUCL BETWEEN '0A' AND '0Z' OR OKCUCL BETWEEN '1A' AND '1Z')";
				// System.out.println("getCustomerCashCode\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					if (mRes.getString("DCASH").trim().equals("YES")) {
						return true;

					}
				}

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

		return false;

	}

	public static String getOrderNumberSaleman(String cono, String divi, String saleman, String status)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n" + "WHERE THCONO = '"
						+ cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THSTAS = '" + status + "' \n"
						+ "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n"
						+ "AND THSANO = '" + saleman + "' \n" + "ORDER BY THORNO DESC";
				// System.out.println("getOrderNumber\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderNumberSupport_bac211115(String cono, String divi, String salesup, String status,
			String auth) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;
				if (auth.equals("1")) {
					String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TRIM(THSANO) || ' : ' || TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
							+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THSTAS = '"
							+ status + "' \n" + "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n"
							+ "AND b.OKSTAT = '20' \n" + "ORDER BY THSANO, THORNO DESC";
					// System.out.println("getOrderNumber\n" + query);
					mRes = stmt.executeQuery(query);

				} else {
					String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TRIM(THSANO) || ' : ' || TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
							+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THSTAS = '"
							+ status + "' \n" + "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n"
							+ "AND b.OKSTAT = '20' \n" + "AND THSASU = '" + salesup + "'\n"
							+ "ORDER BY THSANO, THORNO DESC";
					// System.out.println("getOrderNumber\n" + query);
					mRes = stmt.executeQuery(query);
				}

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderNumberSupport(String cono, String divi, String salesup, String status, String auth)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;
				if (auth.equals("1")) {
					String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO, CHAR(THDEDA, ISO) AS THDEDA,CHAR(THDEDA, ISO) || ' : ' || TRIM(THSANO) || ' : ' || TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
							+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THSTAS = '"
							+ status + "' \n" + "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n"
							+ "AND b.OKSTAT = '20' \n" + "ORDER BY THDEDA DESC, THSANO, THORNO DESC";
					// System.out.println("getOrderNumber\n" + query);
					mRes = stmt.executeQuery(query);

				} else {
					String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO, CHAR(THDEDA, ISO) AS THDEDA,CHAR(THDEDA, ISO) || ' : ' || TRIM(THSANO) || ' : ' || TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
							+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THSTAS = '"
							+ status + "' \n" + "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n"
							+ "AND b.OKSTAT = '20' \n" + "AND THSANO IN (SELECT S_SALE \n"
							+ "FROM " + Constant.DBNAME + ".SAL_SUPSALE \n" + "WHERE S_SUPP = '" + salesup + "' \n"
							+ "ORDER BY S_SUPP) \n" + "ORDER BY THDEDA DESC, THSANO, THORNO DESC";
					// System.out.println("getOrderNumber\n" + query);
					mRes = stmt.executeQuery(query);
				}

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderNumberHead(String cono, String divi, String username, String group, String auth)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;
				if (group.equals("SALESUP")) {

					if (auth.equals("1")) {
						String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
								+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
								+ "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n"
								+ "ORDER BY THORNO DESC \n" + "LIMIT 100";
						// System.out.println("getOrderNumber\n" + query);
						mRes = stmt.executeQuery(query);
					} else {
						String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
								+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
								+ "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n"
								+ "AND THSANO IN (SELECT S_SALE \n" + "FROM " + Constant.DBNAME + ".SAL_SUPSALE \n"
								+ "WHERE S_SUPP = '" + username + "' \n" + "ORDER BY S_SUPP) \n"
								+ "ORDER BY THORNO DESC \n" + "LIMIT 100";
						// System.out.println("getOrderNumber\n" + query);
						mRes = stmt.executeQuery(query);

					}

				} else {
					String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
							+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
							+ "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n"
							+ "AND THSANO = '" + username + "' \n" + "ORDER BY THORNO DESC \n" + "LIMIT 100";
					// System.out.println("getOrderNumber\n" + query);
					mRes = stmt.executeQuery(query);
				}

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getCONumber(String cono, String divi, String orderstatus, String fromstatus, String tostatus,
			String credit, String auth, String userid) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;

				if (auth.equals("1")) {

					String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TDCONU,OAORST,OAORSL,TRIM(OAORST) || ' : ' || TRIM(OAORSL) || ' : ' || TRIM(TDCONU) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT b, "
							+ DBM3NAME + ".OCUSMA c, " + DBM3NAME + ".OOHEAD d \n" + "WHERE THCONO = '" + cono + "' \n"
							+ "AND THDIVI = '" + divi + "' \n" + "AND THSTAS IN (" + orderstatus + ") \n"
							+ "AND (OAORST = '" + fromstatus + "' AND OAORSL = '" + tostatus + "') \n"
							+ "AND OAOBLC IN (" + credit + ") \n" + "AND b.TDCONO = THCONO \n"
							+ "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n" + "AND c.OKCONO = THCONO \n"
							+ "AND c.OKCUNO = THCUNO \n" + "AND d.OACONO = THCONO \n" + "AND d.OAORNO = TDCONU \n" +
							// "AND d.OARESP = '"+userid+"' \n" +
							// "AND d.OARESP = 'NISARA_REV' \n" +
							"GROUP BY THCONO, THDIVI, THORNO, TDCONU, OAORST, OAORSL, OKCUNM \n"
							+ "ORDER BY TDCONU ASC";
					// System.out.println("getCONumber\n" + query);
					mRes = stmt.executeQuery(query);

				} else {

					String query = "SELECT ROW_NUMBER() OVER(ORDER BY THORNO) AS ID,THCONO,THDIVI,THORNO,TDCONU,OAORST,OAORSL,TRIM(OAORST) || ' : ' || TRIM(OAORSL) || ' : ' || TRIM(TDCONU) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT b, "
							+ DBM3NAME + ".OCUSMA c, " + DBM3NAME + ".OOHEAD d \n" + "WHERE THCONO = '" + cono + "' \n"
							+ "AND THDIVI = '" + divi + "' \n" + "AND THSTAS IN (" + orderstatus + ") \n"
							+ "AND (OAORST = '" + fromstatus + "' AND OAORSL = '" + tostatus + "') \n"
							+ "AND OAOBLC IN (" + credit + ") \n" + "AND b.TDCONO = THCONO \n"
							+ "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n" + "AND c.OKCONO = THCONO \n"
							+ "AND c.OKCUNO = THCUNO \n" + "AND d.OACONO = THCONO \n" + "AND d.OAORNO = TDCONU \n"
							+ "AND d.OARESP = '" + userid + "' \n" +
							// "AND d.OARESP = 'NISARA_REV' \n" +
							"GROUP BY THCONO, THDIVI, THORNO, TDCONU, OAORST, OAORSL, OKCUNM \n"
							+ "ORDER BY TDCONU ASC";
					// System.out.println("getCONumber\n" + query);
					mRes = stmt.executeQuery(query);

				}

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderNumberFromCO(String cono, String divi, String conumber) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT TDORNO \n" + "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n"
						+ "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '" + divi + "' \n" + "AND TDCONU = '"
						+ conumber + "' \n" + "GROUP BY TDORNO";
				// System.out.println("getOrderNumberFromCO\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("TDORNO").trim();
				}

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

	public static String getOrderHead(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, CHAR(THORDA, ISO) AS THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, TRIM(THCUNO) || ' : ' || TRIM(OKCUNM) AS CUSTOMER, CASE WHEN OKCUNM LIKE '%à§Ô¹Ê´%' THEN 'YES' ELSE 'NO' END AS DCASH, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n" + "WHERE THCONO = '"
						+ cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND b.OKCONO = THCONO \n"
						+ "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n" + "AND THORNO = '" + orderno + "'";
				// System.out.println("getOrderHead\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderHeadV2(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, OKCUNM, CUSTOMER, DCASH, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, PAYER \n"
						+ ", CCOINA, CCOVNI, CCOINA + CCOVNI AS BACKLOG, OKCRL3 - (CCOINA + CCOVNI) AS DIFFERENCES \n"
						+ "FROM \n"
						+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, CHAR(THORDA, ISO) AS THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, TRIM(THCUNO) || ' : ' || TRIM(OKCUNM) AS CUSTOMER, CASE WHEN OKCUNM LIKE '%เงินสด%' THEN 'YES' ELSE 'NO' END AS DCASH, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI \n"
						+ ", CASE WHEN OKPYNO = '' THEN OKCUNO ELSE OKPYNO END AS PAYER, OKCRLM, OKCRL2, OKCRL3 \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n"
						+ "WHERE THCONO = '10' \n" + "AND THDIVI = '101' \n" + "AND b.OKCONO = THCONO \n"
						+ "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n" + "AND THORNO = '" + orderno
						+ "') AS a \n" + "LEFT JOIN " + DBM3NAME
						+ ".CCUCRL AS b ON b.CCCONO = a.THCONO AND b.CCPYNO = a.PAYER";
				// System.out.println("getOrderHeadV2\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderHeadV3(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, OKCUNM, CUSTOMER, DCASH, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, PAYER \n"
						+ ", CCOINA, CCOVNI, CCOINA + CCOVNI AS BACKLOG, OKCRL3 - (CCOINA + CCOVNI) AS DIFFERENCES, ROUNDDESC \n"
						+ "FROM \n"
						+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, CHAR(THORDA, ISO) AS THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, TRIM(THCUNO) || ' : ' || TRIM(OKCUNM) AS CUSTOMER, CASE WHEN OKCUNM LIKE '%เงินสด%' THEN 'YES' ELSE 'NO' END AS DCASH, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI \n"
						+ ", CASE WHEN OKPYNO = '' THEN OKCUNO ELSE OKPYNO END AS PAYER, OKCRLM, OKCRL2, OKCRL3,TRIM(CTSTKY) || ' : ' ||TRIM(SUBSTRING(CTPARM,0,LOCATE(' ',CTPARM))) AS ROUNDDESC \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b, " + DBM3NAME
						+ ".CSYTAB c \n" + "WHERE THCONO = '10' \n" + "AND THDIVI = '101' \n"
						+ "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n"
						+ "AND THORNO = '" + orderno + "' \n" + "AND c.CTCONO = THCONO \n" + "AND c.CTSTKY = THROUN \n"
						+ "AND CTSTCO = 'MODL' \n" + "AND (SUBSTR(CTSTKY,1,1) = '1' OR CTSTKY = '000')) AS a \n"
						+ "LEFT JOIN " + DBM3NAME + ".CCUCRL AS b ON b.CCCONO = a.THCONO AND b.CCPYNO = a.PAYER";
				// System.out.println("getOrderHeadV3\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getCOHead(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT OACONO, OADIVI, OAORNO, OAFACI, OAWHLO, OAORTP, OAORST, OAORSL, OACUNO, OAORDT, OARLDZ, OAMODL, OATEPA, OASMCD, OAPLTB, OADISY, OACHID \n"
						+ "FROM " + DBM3NAME + ".OOHEAD \n" + "WHERE OACONO = '" + cono + "' \n" + "AND OAORNO = '"
						+ orderno + "'";
				// System.out.println("getCOHead\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getCOCredit(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT OACONO, OADIVI, OAORNO, OACUNO, OAOBLC, CASE WHEN OAOBLC = 0 THEN 'No stop' \n"
						+ "WHEN OAOBLC = 1 THEN 'Credit limit 1 (Overdue)' \n"
						+ "WHEN OAOBLC = 2 THEN 'Credit limit 2 (Total outstanding)' \n"
						+ "WHEN OAOBLC = 3 THEN 'Credit limit 3 (Total outstanding + order stock)' \n"
						+ "WHEN OAOBLC = 4 THEN 'Credit limit 4 (Number of days overdue)' \n"
						+ "WHEN OAOBLC = 5 THEN 'Connected to letter of credit' \n"
						+ "WHEN OAOBLC = 6 THEN 'Time fenced credit limit issue' \n"
						+ "WHEN OAOBLC IN ('7','8') THEN 'Manually set stop for optional use' \n"
						+ "WHEN OAOBLC = 9 THEN 'Manually released order' END AS CREDIT \n" + "FROM " + DBM3NAME
						+ ".OOHEAD \n" + "WHERE OACONO = '" + cono + "' \n" + "AND OADIVI = '" + divi + "' \n"
						+ "AND OAORNO = '" + orderno + "'";
				// System.out.println("getCOCredit\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("CREDIT").trim();
				}

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

	public static String getOrderDetail(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL a, " + DBM3NAME + ".MITMAS b \n" + "WHERE TDCONO = '"
						+ cono + "' \n" + "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "AND b.MMCONO = TDCONO \n" + "AND b.MMITNO = TDITNO \n" + "ORDER BY TDORNO, TDLINE";
				// System.out.println("getOrderDetail\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderDetailV2(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT TDCONO, TDDIVI, TDORNO, THDEDA, THCUNO, THPRIC, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, TDIQTY, TDUNIT, UNITPRICE, OMSPUN, OMSPUN2, MUDMCF1, MUCOFA1, OMSAPR, OMSAPR2 \n"
						+ ", CAST(COALESCE(CASE WHEN OMSAPR IS NOT NULL \n"
						+ "THEN CASE WHEN UNITPRICE != OMSPUN THEN CASE WHEN MUDMCF1 = 1 THEN CAST(MUCOFA1 AS DECIMAL(15,2)) * CAST(OMSAPR AS DECIMAL(15,2)) ELSE CAST(OMSAPR AS DECIMAL(15,2)) / CAST(MUCOFA1 AS DECIMAL(15,2)) END ELSE OMSAPR END \n"
						+ "ELSE CASE WHEN UNITPRICE != OMSPUN2 THEN CASE WHEN MUDMCF1 = 1 THEN CAST(MUCOFA1 AS DECIMAL(15,2)) * CAST(OMSAPR2 AS DECIMAL(15,2)) ELSE CAST(OMSAPR2 AS DECIMAL(15,2)) / CAST(MUCOFA1 AS DECIMAL(15,2)) END ELSE OMSAPR2 END \n"
						+ "END, 0) AS DECIMAL(15,2)) AS SALEPRICE \n"
						+ ", TDREM1, UNITPRICE AS TDREM2,TDREM2 AS TDREM3, TDSTAS, TDENDA, TDENTI \n" + "FROM \n"
						+ "(SELECT TDCONO, TDDIVI, THDEDA, THCUNO, THROUN, THPRIC, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, TDIQTY, TDUNIT, MMUNMS \n"
						+ ", CASE WHEN TDREM2 = '' THEN CASE WHEN MUALUN2 IS NOT NULL THEN MUALUN2 ELSE MMUNMS END ELSE TDREM2 END AS UNITPRICE, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM \n"
						+ "(SELECT TDCONO, TDDIVI, THDEDA, THCUNO, THROUN, THPRIC, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, MMUNMS, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL a, " + DBNAME + ".M3_TAKEORDERHEAD b, " + DBM3NAME
						+ ".MITMAS c \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '" + divi + "'  \n"
						+ "AND TDORNO = '" + orderno + " ' \n" +
						// "AND TDSTAS = '10' \n" +
						"AND b.THCONO = TDCONO \n" + "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n"
						+ "AND c.MMCONO = TDCONO  \n" + "AND c.MMITNO = TDITNO \n" + "ORDER BY TDORNO, TDLINE) AS a \n"
						+ "LEFT JOIN \n" + "(SELECT MUCONO AS MUCONO2, MUITNO AS MUITNO2, MUALUN AS MUALUN2 \n"
						+ "FROM " + DBM3NAME + ".MITAUN \n" + "WHERE MUAUTP = '2' \n" + "AND MUAUS9 = '1' \n"
						+ "GROUP BY MUCONO,MUITNO,MUALUN) AS b \n" + "ON b.MUCONO2 = a.TDCONO \n"
						+ "AND b.MUITNO2 = a.TDITNO) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT MUCONO AS MUCONO1, MUITNO AS MUITNO1, MUALUN AS MUALUN1, MUDMCF AS MUDMCF1, MUCOFA AS MUCOFA1 \n"
						+ "FROM " + DBM3NAME + ".MITAUN \n" + "WHERE MUAUTP = '1' \n" + "AND MUAUS2 = '1' \n"
						+ "GROUP BY MUCONO,MUITNO,MUALUN,MUDMCF,MUCOFA) AS b \n" + "ON b.MUCONO1 = a.TDCONO \n"
						+ "AND b.MUITNO1 = a.TDITNO \n" + "AND b.MUALUN1 = a.UNITPRICE \n" + "LEFT JOIN \n"
						+ "(SELECT OMCONO,OMPRRF,OMCUNO,OMITNO,OMFVDT,OMLVDT,OMSAPR,OMSPUN \n" + "FROM \n"
						+ "(SELECT ROW_NUMBER() OVER(PARTITION BY OMITNO) AS NO, OMCONO,OMPRRF,OMCUNO,OMITNO,OMFVDT,OMLVDT,OMSAPR,OMSPUN \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL a, " + DBNAME + ".M3_TAKEORDERHEAD b, " + DBM3NAME
						+ ".OPRICL c \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '" + divi + "'  \n"
						+ "AND TDORNO = '" + orderno + "' \n" +
						// "AND TDSTAS = '10' \n" +
						"AND b.THCONO = TDCONO \n" + "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n"
						+ "AND c.OMCONO = b.THCONO \n" + "AND c.OMPRRF = b.THPRIC \n" + "AND c.OMCUNO = b.THCUNO \n"
						+ "--AND c.OMCUNO = ''  \n" + "AND c.OMITNO = a.TDITNO \n"
						+ "AND SUBSTRING(CHAR(THDEDA, ISO),0,5) || SUBSTRING(CHAR(THDEDA, ISO),6,2) || SUBSTRING(CHAR(THDEDA, ISO),9,2) BETWEEN c.OMFVDT AND c.OMLVDT \n"
						+ "ORDER BY OMITNO,OMFVDT DESC) AS a \n" + "WHERE NO = 1) AS c \n" + "ON c.OMITNO = a.TDITNO \n"
						+ "LEFT JOIN \n"
						+ "(SELECT OMCONO AS OMCONO2,OMPRRF AS OMPRRF2,OMCUNO AS OMCUNO2,OMITNO AS OMITNO2,OMFVDT AS OMFVDT2,OMLVDT AS OMLVDT2,OMSAPR AS OMSAPR2,OMSPUN AS OMSPUN2 \n"
						+ "FROM \n"
						+ "(SELECT ROW_NUMBER() OVER(PARTITION BY OMITNO) AS NO, OMCONO,OMPRRF,OMCUNO,OMITNO,OMFVDT,OMLVDT,OMSAPR,OMSPUN \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL a, " + DBNAME + ".M3_TAKEORDERHEAD b, " + DBM3NAME
						+ ".OPRICL c \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '" + divi + "'  \n"
						+ "AND TDORNO = '" + orderno + " ' \n" +
						// "AND TDSTAS = '10' \n" +
						"AND b.THCONO = TDCONO \n" + "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n"
						+ "AND c.OMCONO = b.THCONO \n" + "AND c.OMPRRF = b.THPRIC \n" + "--AND c.OMCUNO = b.THCUNO \n"
						+ "AND c.OMCUNO = '' \n" + "AND c.OMITNO = a.TDITNO \n"
						+ "AND SUBSTRING(CHAR(THDEDA, ISO),0,5) || SUBSTRING(CHAR(THDEDA, ISO),6,2) || SUBSTRING(CHAR(THDEDA, ISO),9,2) BETWEEN c.OMFVDT AND c.OMLVDT \n"
						+ "ORDER BY OMITNO,OMFVDT DESC) AS a \n" + "WHERE NO = 1) AS d \n"
						+ "ON d.OMITNO2 = a.TDITNO \n" + "ORDER BY TDLINE";
				// System.out.println("getOrderDetailV2\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderDetailSupport(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT TDCONO, TDDIVI, TDORNO, THDEDA, THCUNO, THPRIC, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, TDIQTY, TDUNIT, UNITPRICE, OMSPUN, OMSPUN2, MUDMCF1, MUCOFA1, OMSAPR, OMSAPR2 \n"
						+ ", CAST(COALESCE(CASE WHEN OMSAPR IS NOT NULL \n"
						+ "THEN CASE WHEN UNITPRICE != OMSPUN THEN CASE WHEN MUDMCF1 = 1 THEN CAST(MUCOFA1 AS DECIMAL(15,2)) * CAST(OMSAPR AS DECIMAL(15,2)) ELSE CAST(OMSAPR AS DECIMAL(15,2)) / CAST(MUCOFA1 AS DECIMAL(15,2)) END ELSE OMSAPR END \n"
						+ "ELSE CASE WHEN UNITPRICE != OMSPUN2 THEN CASE WHEN MUDMCF1 = 1 THEN CAST(MUCOFA1 AS DECIMAL(15,2)) * CAST(OMSAPR2 AS DECIMAL(15,2)) ELSE CAST(OMSAPR2 AS DECIMAL(15,2)) / CAST(MUCOFA1 AS DECIMAL(15,2)) END ELSE OMSAPR2 END \n"
						+ "END, 0) AS DECIMAL(15,2)) AS SALEPRICE \n"
						+ ", TDREM1, UNITPRICE AS TDREM2,TDREM2 AS TDREM3, TDSTAS, TDENDA, TDENTI \n" + "FROM \n"
						+ "(SELECT TDCONO, TDDIVI, THDEDA, THCUNO, THROUN, THPRIC, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, TDIQTY, TDUNIT, MMUNMS \n"
						+ ", CASE WHEN TDREM2 = '' THEN CASE WHEN MUALUN2 IS NOT NULL THEN MUALUN2 ELSE MMUNMS END ELSE TDREM2 END AS UNITPRICE, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM \n"
						+ "(SELECT TDCONO, TDDIVI, THDEDA, THCUNO, THROUN, THPRIC, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, MMUNMS, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBNAME + ".M3_TAKEORDERHEAD b, "
						+ DBM3NAME + ".MITMAS c \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '" + divi
						+ "'  \n" + "AND TDORNO = '" + orderno + " ' \n" + "AND TDSTAS = '10' \n"
						+ "AND b.THCONO = TDCONO \n" + "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n"
						+ "AND c.MMCONO = TDCONO  \n" + "AND c.MMITNO = TDITNO \n" + "ORDER BY TDORNO, TDLINE) AS a \n"
						+ "LEFT JOIN \n" + "(SELECT MUCONO AS MUCONO2, MUITNO AS MUITNO2, MUALUN AS MUALUN2 \n"
						+ "FROM " + DBM3NAME + ".MITAUN \n" + "WHERE MUAUTP = '2' \n" + "AND MUAUS9 = '1' \n"
						+ "GROUP BY MUCONO,MUITNO,MUALUN) AS b \n" + "ON b.MUCONO2 = a.TDCONO \n"
						+ "AND b.MUITNO2 = a.TDITNO) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT MUCONO AS MUCONO1, MUITNO AS MUITNO1, MUALUN AS MUALUN1, MUDMCF AS MUDMCF1, MUCOFA AS MUCOFA1 \n"
						+ "FROM " + DBM3NAME + ".MITAUN \n" + "WHERE MUAUTP = '1' \n" + "AND MUAUS2 = '1' \n"
						+ "GROUP BY MUCONO,MUITNO,MUALUN,MUDMCF,MUCOFA) AS b \n" + "ON b.MUCONO1 = a.TDCONO \n"
						+ "AND b.MUITNO1 = a.TDITNO \n" + "AND b.MUALUN1 = a.UNITPRICE \n" + "LEFT JOIN \n"
						+ "(SELECT OMCONO,OMPRRF,OMCUNO,OMITNO,OMFVDT,OMLVDT,OMSAPR,OMSPUN \n" + "FROM \n"
						+ "(SELECT ROW_NUMBER() OVER(PARTITION BY OMITNO) AS NO, OMCONO,OMPRRF,OMCUNO,OMITNO,OMFVDT,OMLVDT,OMSAPR,OMSPUN \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBNAME + ".M3_TAKEORDERHEAD b, "
						+ DBM3NAME + ".OPRICL c \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '" + divi
						+ "'  \n" + "AND TDORNO = '" + orderno + "' \n" + "AND TDSTAS = '10' \n"
						+ "AND b.THCONO = TDCONO \n" + "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n"
						+ "AND c.OMCONO = b.THCONO \n" + "AND c.OMPRRF = b.THPRIC \n" + "AND c.OMCUNO = b.THCUNO \n"
						+ "--AND c.OMCUNO = ''  \n" + "AND c.OMITNO = a.TDITNO \n"
						+ "AND SUBSTRING(CHAR(THDEDA, ISO),0,5) || SUBSTRING(CHAR(THDEDA, ISO),6,2) || SUBSTRING(CHAR(THDEDA, ISO),9,2) BETWEEN c.OMFVDT AND c.OMLVDT \n"
						+ "ORDER BY OMITNO,OMFVDT DESC) AS a \n" + "WHERE NO = 1) AS c \n" + "ON c.OMITNO = a.TDITNO \n"
						+ "LEFT JOIN \n"
						+ "(SELECT OMCONO AS OMCONO2,OMPRRF AS OMPRRF2,OMCUNO AS OMCUNO2,OMITNO AS OMITNO2,OMFVDT AS OMFVDT2,OMLVDT AS OMLVDT2,OMSAPR AS OMSAPR2,OMSPUN AS OMSPUN2 \n"
						+ "FROM \n"
						+ "(SELECT ROW_NUMBER() OVER(PARTITION BY OMITNO) AS NO, OMCONO,OMPRRF,OMCUNO,OMITNO,OMFVDT,OMLVDT,OMSAPR,OMSPUN \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBNAME + ".M3_TAKEORDERHEAD b, "
						+ DBM3NAME + ".OPRICL c \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '" + divi
						+ "'  \n" + "AND TDORNO = '" + orderno + " ' \n" + "AND TDSTAS = '10' \n"
						+ "AND b.THCONO = TDCONO \n" + "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n"
						+ "AND c.OMCONO = b.THCONO \n" + "AND c.OMPRRF = b.THPRIC \n" + "--AND c.OMCUNO = b.THCUNO \n"
						+ "AND c.OMCUNO = '' \n" + "AND c.OMITNO = a.TDITNO \n"
						+ "AND SUBSTRING(CHAR(THDEDA, ISO),0,5) || SUBSTRING(CHAR(THDEDA, ISO),6,2) || SUBSTRING(CHAR(THDEDA, ISO),9,2) BETWEEN c.OMFVDT AND c.OMLVDT \n"
						+ "ORDER BY OMITNO,OMFVDT DESC) AS a \n" + "WHERE NO = 1) AS d \n"
						+ "ON d.OMITNO2 = a.TDITNO \n" + "ORDER BY TDLINE";
				// System.out.println("getOrderDetailSupport\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderDetailSupport_bac211119(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT TDCONO, TDDIVI, TDORNO, THDEDA, THCUNO, THPRIC, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, TDIQTY, TDUNIT, UNITPRICE, OMSPUN, MUCOFA1, OMSAPR \n"
						+ ", CAST(COALESCE(CASE WHEN UNITPRICE != OMSPUN THEN CASE WHEN MUDMCF1 = 1 THEN CAST(MUCOFA1 AS DECIMAL(15,2)) * CAST(OMSAPR AS DECIMAL(15,2))  ELSE  CAST(OMSAPR AS DECIMAL(15,2))  / CAST(MUCOFA1 AS DECIMAL(15,2)) END ELSE OMSAPR END,0) AS DECIMAL(15,2)) AS SALEPRICE \n"
						+ ", TDREM1, UNITPRICE AS TDREM2,TDREM2 AS TDREM3, TDSTAS, TDENDA, TDENTI \n" + "FROM \n"
						+ "(SELECT TDCONO, TDDIVI, THDEDA, THCUNO, THROUN, THPRIC, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, TDIQTY, TDUNIT, MMUNMS \n"
						+ ", CASE WHEN TDREM2 = '' THEN CASE WHEN MUALUN2 IS NOT NULL THEN MUALUN2 ELSE MMUNMS END ELSE TDREM2 END AS UNITPRICE, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM \n"
						+ "(SELECT TDCONO, TDDIVI, THDEDA, THCUNO, THROUN, THPRIC, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, MMUNMS, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBNAME + ".M3_TAKEORDERHEAD b, "
						+ DBM3NAME + ".MITMAS c \n" + "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '" + divi
						+ "' \n" + "AND TDORNO = '" + orderno + "' \n" + "AND TDSTAS = '10' \n"
						+ "AND b.THCONO = TDCONO \n" + "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n"
						+ "AND c.MMCONO = TDCONO \n" + "AND c.MMITNO = TDITNO \n" + "ORDER BY TDORNO, TDLINE) AS a \n"
						+ "LEFT JOIN \n" + "(SELECT MUCONO AS MUCONO2, MUITNO AS MUITNO2, MUALUN AS MUALUN2 \n"
						+ "FROM " + DBM3NAME + ".MITAUN \n" + "WHERE MUAUTP = '2' \n" + "AND MUAUS9 = '1' \n"
						+ "GROUP BY MUCONO,MUITNO,MUALUN) AS b \n" + "ON b.MUCONO2 = a.TDCONO \n"
						+ "AND b.MUITNO2 = a.TDITNO) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT MUCONO AS MUCONO1, MUITNO AS MUITNO1, MUALUN AS MUALUN1, MUDMCF AS MUDMCF1, MUCOFA AS MUCOFA1 \n"
						+ "FROM " + DBM3NAME + ".MITAUN \n" + "WHERE MUAUTP = '1' \n" + "AND MUAUS2 = '1' \n"
						+ "GROUP BY MUCONO,MUITNO,MUALUN,MUDMCF,MUCOFA) AS b \n" + "ON b.MUCONO1 = a.TDCONO \n"
						+ "AND b.MUITNO1 = a.TDITNO \n" + "AND b.MUALUN1 = a.UNITPRICE \n" + "LEFT JOIN \n"
						+ "(SELECT OMCONO,OMPRRF,OMCUNO,OMITNO,OMFVDT,OMLVDT,OMSAPR,OMSPUN \n" + "FROM " + DBM3NAME
						+ ".OPRICL) AS c \n" + "ON c.OMCONO = a.TDCONO \n" + "AND c.OMCUNO = a.THCUNO \n"
						+ "AND c.OMPRRF = a.THPRIC \n" + "AND c.OMITNO = a.TDITNO \n" + "--AND d.OMSPUN = b.MUALUN2 \n"
						+ "AND SUBSTRING(CHAR(a.THDEDA, ISO),0,5) || SUBSTRING(CHAR(a.THDEDA, ISO),6,2) || SUBSTRING(CHAR(a.THDEDA, ISO),9,2) BETWEEN c.OMFVDT AND c.OMLVDT";
				// System.out.println("getOrderDetailSupport\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getOrderDetailSupport_bac211118(String cono, String divi, String orderno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".MITMAS b \n"
						+ "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '"
						+ orderno + "' \n" + "AND TDSTAS = '10' \n" + "AND b.MMCONO = TDCONO \n"
						+ "AND b.MMITNO = TDITNO \n" + "ORDER BY TDORNO, TDLINE";
				// System.out.println("getOrderDetail\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMonitoringHeadSaleman(String cono, String divi, String saleman) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT THCONO, THDIVI, THORNO, COALESCE(TDCONU, '') AS TDCONU, COALESCE(CAST('00' || UAIVNO AS VARCHAR(10)), '') AS UAIVNO, THORTY, THWHLO, THCOCE, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, OKCUNM, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, CASE WHEN UAIVNO IS NULL THEN THSTAS ELSE '97' END AS THSTAS, THENDA, THENTI, THENUS \n"
						+ "FROM \n"
						+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n" + "WHERE THCONO = '"
						+ cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND a.THCONO = b.OKCONO \n"
						+ "AND a.THCUNO = b.OKCUNO \n" + "AND THSANO = '" + saleman + "' \n" +
						// "AND THORNO = '0009665691' \n" +
						") a \n" + "LEFT JOIN \n" + "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO \n" + "FROM "
						+ DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n"
						+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO) AS b \n" + "ON b.TDCONO = THCONO \n"
						+ "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n" + "LEFT JOIN \n"
						+ "(SELECT UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4 \n" + "FROM " + DBM3NAME + ".ODHEAD \n"
						+ "GROUP BY UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4) AS c \n" + "ON c.UACONO = THCONO \n"
						+ "AND c.UADIVI = THDIVI \n" + "AND c.UAORNO = TDCONU \n" + "AND c.UAYEA4 = YEAR(THDEDA) \n"
						+ "ORDER BY THCONO, THDIVI, THORNO DESC";
				// System.out.println("getMonitorHead\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMonitoringHeadSupport(String cono, String divi, String salesup) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT THCONO, THDIVI, THORNO, COALESCE(TDCONU, '') AS TDCONU, COALESCE(CAST('00' || UAIVNO AS VARCHAR(10)), '') AS UAIVNO, THORTY, THWHLO, THCOCE, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, OKCUNM, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, CASE WHEN UAIVNO IS NULL THEN THSTAS ELSE '97' END AS THSTAS, THENDA, THENTI, THENUS \n"
						+ "FROM \n"
						+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n" + "WHERE THCONO = '"
						+ cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND a.THCONO = b.OKCONO \n"
						+ "AND a.THCUNO = b.OKCUNO \n" +
						// "AND THSASU = '"+salesup+"' \n"+
						// "AND THORNO = '0009665691' \n" +
						") a \n" + "LEFT JOIN \n" + "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO \n" + "FROM "
						+ DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n"
						+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO) AS b \n" + "ON b.TDCONO = THCONO \n"
						+ "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n" + "LEFT JOIN \n"
						+ "(SELECT UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4 \n" + "FROM " + DBM3NAME + ".ODHEAD \n"
						+ "GROUP BY UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4) AS c \n" + "ON c.UACONO = THCONO \n"
						+ "AND c.UADIVI = THDIVI \n" + "AND c.UAORNO = TDCONU \n" + "AND c.UAYEA4 = YEAR(THDEDA) \n"
						+ "ORDER BY THCONO, THDIVI, THORNO DESC";
				// System.out.println("getMonitorHead\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMonitoringHead(String cono, String divi, String orderno, String date, String month,
			String year, String status, String credit, String round, String username, String group, String auth)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				String limit = "";

				if (date == null && month == null) {
					limit = "LIMIT 1000";
				}

				if (orderno != null) {
					orderno = "IN ('" + orderno + "')";
				} else {
					orderno = "NOT IN ('0')";
				}

				if (date != null) {
					date = "IN ('" + date + "')";
				} else {
					date = "NOT IN ('1111-11-11')";
				}

				if (month != null) {
					month = "IN ('" + month + "')";
				} else {
					month = "NOT IN ('0')";
				}

				if (year != null) {
					year = "IN ('" + year + "')";
				} else {
					year = "NOT IN ('0')";
				}

				if (status != null) {
					status = "IN ('" + status + "')";
				} else {
					status = "NOT IN ('0')";
				}

				if (credit != null) {
					credit = "CREDIT IN ('" + credit + "')";
				} else {
					credit = "CREDIT IS NULL OR CREDIT IS NOT NULL";
				}

				if (round != null) {
					round = "IN ('" + round + "')";
				} else {
					round = "NOT IN ('0')";
				}

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;

				if (group.equals("SALESUP")) {

					if (auth.equals("1")) {

						String query = "SELECT THCONO, THDIVI, THORNO, COALESCE(TDCONU, '') AS TDCONU, COALESCE(CAST('00' || UAIVNO AS VARCHAR(10)), '') AS UAIVNO, THORTY, THWHLO, THCOCE, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, OKCUNM, THROUN, ROUNDDESC, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, CASE WHEN UAIVNO IS NULL THEN THSTAS ELSE '97' END AS THSTAS, COALESCE(CREDIT,'') AS CREDIT, CHAR(THENDA, ISO) AS THENDA, THENTI, THENUS \n"
								+ "FROM \n"
								+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THROUN, TRIM(SUBSTRING(CTPARM,0,LOCATE(' ',CTPARM))) AS ROUNDDESC, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b, " + DBM3NAME
								+ ".CSYTAB c \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
								+ "AND a.THCONO = b.OKCONO \n" + "AND a.THCUNO = b.OKCUNO \n" + "AND a.THORNO "
								+ orderno + " \n" + "AND a.THDEDA " + date + " \n"
								+ "AND YEAR(a.THDEDA) || SUBSTRING(CHAR(a.THDEDA, ISO),6,2) " + month + " \n"
								+ "AND YEAR(a.THDEDA) " + year + " \n" + "AND a.THSTAS " + status + " \n"
								+ "AND a.THROUN " + round + " \n" + "AND c.CTCONO = THCONO \n"
								+ "AND c.CTSTKY = THROUN \n" + "AND c.CTSTCO = 'MODL' \n"
								+ "AND (SUBSTR(c.CTSTKY,1,1) = '1' OR c.CTSTKY = '000')" + ") a \n" + "LEFT JOIN \n"
								+ "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, CHAR(OAOBLC) AS CREDIT \n" + "FROM "
								+ DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".OOHEAD b \n"
								+ "WHERE b.OACONO = a.TDCONO \n" + "AND b.OAORNO = a.TDCONU \n"
								+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, OAOBLC) AS b \n"
								+ "ON b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n"
								+ "LEFT JOIN \n" + "(SELECT UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4 \n" + "FROM "
								+ DBM3NAME + ".ODHEAD \n" + "GROUP BY UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4) AS c \n"
								+ "ON c.UACONO = THCONO \n" + "AND c.UADIVI = THDIVI \n" + "AND c.UAORNO = TDCONU \n"
								+ "AND c.UAYEA4 = YEAR(THDEDA) \n" + "WHERE " + credit + " \n"
								+ "ORDER BY THCONO, THDIVI, THORNO DESC " + limit + "";
						// System.out.println("getMonitorHead\n" + query);
						mRes = stmt.executeQuery(query);

					} else {

						String query = "SELECT THCONO, THDIVI, THORNO, COALESCE(TDCONU, '') AS TDCONU, COALESCE(CAST('00' || UAIVNO AS VARCHAR(10)), '') AS UAIVNO, THORTY, THWHLO, THCOCE, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, OKCUNM, THROUN, ROUNDDESC, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, CASE WHEN UAIVNO IS NULL THEN THSTAS ELSE '97' END AS THSTAS, COALESCE(CREDIT,'') AS CREDIT, CHAR(THENDA, ISO) AS THENDA, THENTI, THENUS \n"
								+ "FROM \n"
								+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THROUN, TRIM(SUBSTRING(CTPARM,0,LOCATE(' ',CTPARM))) AS ROUNDDESC, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b, " + DBM3NAME
								+ ".CSYTAB c \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
								+ "AND a.THCONO = b.OKCONO \n" + "AND a.THCUNO = b.OKCUNO \n" + "AND a.THORNO "
								+ orderno + " \n" + "AND a.THDEDA " + date + " \n"
								+ "AND YEAR(a.THDEDA) || SUBSTRING(CHAR(a.THDEDA, ISO),6,2) " + month + " \n"
								+ "AND YEAR(a.THDEDA) " + year + " \n" + "AND a.THSTAS " + status + " \n"
								+ "AND a.THROUN " + round + " \n" + "AND c.CTCONO = THCONO \n"
								+ "AND c.CTSTKY = THROUN \n" + "AND c.CTSTCO = 'MODL' \n"
								+ "AND (SUBSTR(c.CTSTKY,1,1) = '1' OR c.CTSTKY = '000')"
								+ "AND THSANO IN (SELECT S_SALE \n" + "FROM " + Constant.DBNAME + ".SAL_SUPSALE \n"
								+ "WHERE S_SUPP = '" + username + "' \n" + "ORDER BY S_SUPP)) a \n" + "LEFT JOIN \n"
								+ "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, CHAR(OAOBLC) AS CREDIT \n" + "FROM "
								+ DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".OOHEAD b \n"
								+ "WHERE b.OACONO = a.TDCONO \n" + "AND b.OAORNO = a.TDCONU \n"
								+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, OAOBLC) AS b \n"
								+ "ON b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n"
								+ "LEFT JOIN \n" + "(SELECT UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4 \n" + "FROM "
								+ DBM3NAME + ".ODHEAD \n" + "GROUP BY UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4) AS c \n"
								+ "ON c.UACONO = THCONO \n" + "AND c.UADIVI = THDIVI \n" + "AND c.UAORNO = TDCONU \n"
								+ "AND c.UAYEA4 = YEAR(THDEDA) \n" + "WHERE " + credit + " \n"
								+ "ORDER BY THCONO, THDIVI, THORNO DESC " + limit + "";
						// System.out.println("getMonitorHead\n" + query);
						mRes = stmt.executeQuery(query);

					}

				} else {
					String query = "SELECT THCONO, THDIVI, THORNO, COALESCE(TDCONU, '') AS TDCONU, COALESCE(CAST('00' || UAIVNO AS VARCHAR(10)), '') AS UAIVNO, THORTY, THWHLO, THCOCE, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, OKCUNM, THROUN, ROUNDDESC, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, CASE WHEN UAIVNO IS NULL THEN THSTAS ELSE '97' END AS THSTAS, COALESCE(CREDIT,'') AS CREDIT, CHAR(THENDA, ISO) AS THENDA, THENTI, THENUS \n"
							+ "FROM \n"
							+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THROUN, TRIM(SUBSTRING(CTPARM,0,LOCATE(' ',CTPARM))) AS ROUNDDESC, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b, " + DBM3NAME
							+ ".CSYTAB c \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
							+ "AND a.THCONO = b.OKCONO \n" + "AND a.THCUNO = b.OKCUNO \n" + "AND a.THORNO " + orderno
							+ " \n" + "AND a.THDEDA " + date + " \n"
							+ "AND YEAR(a.THDEDA) || SUBSTRING(CHAR(a.THDEDA, ISO),6,2) " + month + " \n"
							+ "AND YEAR(a.THDEDA) " + year + " \n" + "AND a.THSTAS " + status + " \n" + "AND a.THROUN "
							+ round + " \n" + "AND a.THSANO = '" + username + "' \n" + "AND c.CTCONO = THCONO \n"
							+ "AND c.CTSTKY = THROUN \n" + "AND c.CTSTCO = 'MODL' \n"
							+ "AND (SUBSTR(c.CTSTKY,1,1) = '1' OR c.CTSTKY = '000')" + ") a \n" + "LEFT JOIN \n"
							+ "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, CHAR(OAOBLC) AS CREDIT \n" + "FROM "
							+ DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".OOHEAD b \n"
							+ "WHERE b.OACONO = a.TDCONO \n" + "AND b.OAORNO = a.TDCONU \n"
							+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, OAOBLC) AS b \n"
							+ "ON b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n"
							+ "LEFT JOIN \n" + "(SELECT UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4 \n" + "FROM " + DBM3NAME
							+ ".ODHEAD \n" + "GROUP BY UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4) AS c \n"
							+ "ON c.UACONO = THCONO \n" + "AND c.UADIVI = THDIVI \n" + "AND c.UAORNO = TDCONU \n"
							+ "AND c.UAYEA4 = YEAR(THDEDA) \n" + "WHERE " + credit + " \n"
							+ "ORDER BY THCONO, THDIVI, THORNO DESC " + limit + "";
					// System.out.println("getMonitorHead\n" + query);
					mRes = stmt.executeQuery(query);
				}

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMonitoringHeadM3(String cono, String divi, String orderno, String date, String month,
			String year, String status, String credit, String round, String username, String group, String auth)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				String limit = "";

				if (date == null) {
					limit = "LIMIT 1000";
				}

				if (orderno != null) {
					orderno = "IN ('" + orderno + "')";
				} else {
					orderno = "NOT IN ('0')";
				}

				if (date != null) {
					date = "IN ('" + date + "')";
				} else {
					date = "NOT IN ('1111-11-11')";
				}

				if (month != null) {
					month = "IN ('" + month + "')";
				} else {
					month = "NOT IN ('0')";
				}

				if (year != null) {
					year = "IN ('" + year + "')";
				} else {
					year = "NOT IN ('0')";
				}

				if (status != null) {
					status = "IN ('" + status + "')";
				} else {
					status = "NOT IN ('0')";
				}

				if (credit != null) {
					credit = "OAOBLC IN ('" + credit + "')";
				} else {
					credit = "OAOBLC IS NULL OR OAOBLC IS NOT NULL";
				}

				if (round != null) {
					round = "IN ('" + round + "')";
				} else {
					round = "NOT IN ('0')";
				}

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;

				String query = "SELECT OACONO, OADIVI, OAORNO, OAORTP, OAWHLO, OAORST, OAORSL, OAPLTB, OAOBLC, OAORDT, OACUNO, OKCUNM, OAMODL, ROUNDDESC, OADISY, OASMCD, OARESP, OAYREF, OAOFNO, OALMDT, OALMTS, OACHID \n"
						+ "FROM \n"
						+ "(SELECT OACONO, OADIVI, OAORNO, OAORTP, OAWHLO, OAORST, OAORSL, OAOBLC, OAPLTB, CHAR(DATE(SUBSTRING(OAORDT,1,4)||'-'||SUBSTRING(OAORDT,5,2)||'-'||SUBSTRING(OAORDT,7,2)), ISO) AS OAORDT, OACUNO, TRIM(OKCUNM) AS OKCUNM \n"
						+ ", OAMODL, TRIM(SUBSTRING(CTPARM,0,LOCATE(' ',CTPARM))) AS ROUNDDESC, OADISY, OASMCD, OARESP, OAYREF, OAOFNO, OALMDT, OALMTS, OACHID \n"
						+ "FROM " + DBM3NAME + ".OOHEAD a, " + DBM3NAME + ".OCUSMA b, " + DBM3NAME + ".CSYTAB c \n"
						+ "WHERE OACONO = '" + cono + "' \n" + "AND OADIVI = '" + divi + "' \n"
						+ "AND SUBSTRING(OAORDT,1,4)||'-'||SUBSTRING(OAORDT,5,2)||'-'||SUBSTRING(OAORDT,7,2) " + date
						+ " \n"
						+ "AND YEAR(SUBSTRING(OAORDT,1,4)||'-'||SUBSTRING(OAORDT,5,2)||'-'||SUBSTRING(OAORDT,7,2)) || SUBSTRING(OAORDT,5,2) "
						+ month + " \n"
						+ "AND YEAR(SUBSTRING(OAORDT,1,4)||'-'||SUBSTRING(OAORDT,5,2)||'-'||SUBSTRING(OAORDT,7,2)) "
						+ year + " \n" + "AND OAMODL " + round + " \n" + "AND (OAORST = '22' AND OAORSL = '22') \n"
						+ "AND a.OACONO = b.OKCONO \n" + "AND a.OACUNO = b.OKCUNO \n" + "AND c.CTCONO = a.OACONO \n"
						+ "AND c.CTSTKY = a.OAMODL \n" + "AND c.CTSTCO = 'MODL' \n"
						+ "AND (SUBSTR(c.CTSTKY,1,1) = '1' OR c.CTSTKY = '000')) AS a \n" + "WHERE " + credit + " \n"
						+ "ORDER BY OAORDT DESC, OAORNO " + limit + "";
				// System.out.println("getMonitorHeadM3\n" + query);
				mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMonitoringCredit(String cono, String divi, String payer) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;

				String query = "SELECT a.*,b.PAYERNAME,b.OKCRL2 AS CREDIT2, b.OKCRL3 AS CREDIT3,b.OKCRL4 AS CREDIT4 \n"
						+ "FROM  \n"
						+ "(SELECT OACONO,OADIVI,OAPYNO,OACUNO,OAORDT,OAOBLC,OAORNO,CAST(SUM(DAA.CO_OUT) AS DECIMAL(15,2)) AS CO_OUT,CCOVNI AS SUM_CO_OUT,CCOINA AS AR,CCOVNI + CCOINA AS SUM_CO_AR \n"
						+ "FROM  \n" + "(SELECT OACONO,OADIVI,OAPYNO,OACUNO,CCOINA \n"
						+ ",CASE WHEN OAORTP = 'A18' THEN SUM(OANTAM + (OANTAM*0.07)) WHEN OAORTP = 'F58' THEN SUM(OANTAM + (OANTAM*0.07)) \n"
						+ "WHEN OAORTP = 'AA8' THEN SUM(OANTAM + (OANTAM*0.07)) ELSE SUM(OANTAM) END AS CO_OUT ,OAORNO,OAORDT,CCOVNI,OAOBLC  \n"
						+ "FROM " + DBM3NAME + ".OOHEAD \n"
						+ "LEFT JOIN " + DBM3NAME + ".CCUCRL ON CCCONO = OACONO AND OAPYNO = CCPYNO \n"
						+ "WHERE OACONO = '"
						+ cono + "' \n" + "AND OADIVI = '" + divi + "' \n" + "--AND OAPYNO = '" + payer + "' \n"
						+ "AND (OAPYNO = '" + payer + "' OR OACUNO = '" + payer + "') \n"
						+ "AND YEAR(SUBSTRING(OAORDT,1,4)||'-'||SUBSTRING(OAORDT,5,2)||'-'||SUBSTRING(OAORDT,7,2)) >= YEAR(CURRENT_DATE) - 1 \n"
						+ "AND OANTAM <> 0 \n" + "AND OACUNO IN (SELECT OKCUNO \n" + "FROM " + Constant.DBNAME
						+ ".CUSTPAYE \n"
						+ "WHERE OACONO = '" + cono + "') \n"
						+ "--AND (OAORST IN ('22','23','33','27','44','66') OR (OAORTP = 'A29' AND OAORSL = '10')) \n"
						+ "GROUP BY OACONO,OADIVI,OAPYNO,OACUNO,CCOVNI,OAORTP,OAORNO,OAORDT,OAOBLC,CCOINA \n"
						+ ") AS DAA \n"
						+ "GROUP BY DAA.OACONO,DAA.OADIVI,DAA.OAPYNO,OACUNO,DAA.CCOVNI,OAORNO,OAORDT,OAOBLC,CCOINA \n"
						+ "ORDER BY OAPYNO,OAORDT,OAORNO) AS a \n" + "LEFT JOIN  \n"
						+ "(SELECT OKCONO,OKCUNO,COALESCE(OKCUNM,'-') AS PAYERNAME ,TRIM(COALESCE(Z.CTTX15,'NOT FOUND')) AS CTTX15,COALESCE(A.OKCRL2,'-') AS OKCRL2 \n"
						+ ",TRIM(COALESCE(A.OKSMCD,'NOT FOUND')) AS OKSMCD ,TRIM(COALESCE(A.OKPYCD,'NOT FOUND')) AS OKPYCD ,COALESCE(A.OKCRL3,'-') AS OKCRL3 \n"
						+ ",COALESCE(OKODUD,'0') AS OKCRL4 \n" + "FROM " + DBM3NAME + ".OCUSMA A  \n"
						+ "LEFT JOIN " + DBM3NAME
						+ ".CSYTAB Z ON Z.CTCONO = A.OKCONO  AND Z.CTSTCO = 'TEPY' AND A.OKTEPY = Z.CTSTKY \n"
						+ "--WHERE A.OKCONO = '10'   \n" + "--AND A.OKCUNO = 'TH01010101' \n" + ") AS b \n"
						+ "ON b.OKCONO = a.OACONO \n" + "AND b.OKCUNO = a.OAPYNO \n" + "ORDER BY OAPYNO,OAORDT,OAORNO";
				// System.out.println("getMonitoringCredit\n" + query);
				mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMonitoringDetail_bac211209(String cono, String divi, String orderno, String conumber,
			String invnumber) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER() AS LINE, TTITNO, MMFUDS, MMUNMS AS UNIT, TAKENO_QTY, SUPPNO_QTY, CONO_QTY, INVNO_QTY, DIFF \n"
						+ "FROM  \n"
						+ "(SELECT NO, TTCONO, TTDIVI, TTORNO, TTITNO, MMFUDS, CASE WHEN TDUNIT IS NOT NULL THEN TDUNIT ELSE MMUNMS END AS MMUNMS, COALESCE(TDIQTY,0) AS TAKENO_QTY, COALESCE(TSIQTY,0) AS SUPPNO_QTY, COALESCE(OBORQA,0) AS CONO_QTY, COALESCE(UBIVQA,0) AS INVNO_QTY, COALESCE(TDIQTY,0) - COALESCE(UBIVQA,0) AS DIFF \n"
						+ "FROM  \n" + "( \n"
						+ "SELECT 1 AS NO, TDCONO AS TTCONO, TDDIVI AS TTDIVI, TDORNO AS TTORNO, TRIM(TDITNO) AS TTITNO \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL  \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "AND TDSTAS = '10' \n" + "UNION ALL \n"
						+ "SELECT 2 AS NO, TDCONO AS TTCONO, TDDIVI AS TTDIVI, TDORNO AS TTORNO, TRIM(TDITNO) AS TTITNO \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT  \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n" + "UNION ALL \n"
						+ "SELECT 3 AS NO, OBCONO AS TTCONO, OBDIVI AS TTDIVI, OBORNO AS TTORNO, TRIM(OBITNO) AS TTITNO \n"
						+ "FROM " + DBM3NAME + ".OOLINE \n" + "WHERE OBCONO = '" + cono + "' \n" + "AND OBDIVI = '"
						+ divi + "' \n" + "AND OBORNO = '" + conumber + "' \n" + "UNION ALL \n"
						+ "SELECT 4 AS NO, UBCONO AS TTCONO, UBDIVI AS TTDIVI, COALESCE(CAST('00' || UBIVNO AS VARCHAR(10)), '') AS TTORNO, TRIM(UBITNO) AS TTITNO \n"
						+ "FROM " + DBM3NAME + ".ODLINE \n" + "WHERE UBCONO = '" + cono + "' \n" + "AND UBDIVI = '"
						+ divi + "' \n" + "AND UBYEA4 = (SELECT YEAR(THDEDA) AS YEAR \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
						+ "AND THORNO = '" + orderno + "') \n" + "AND UBIVNO = '" + invnumber + "' \n" + ") AS a \n"
						+ "LEFT JOIN  \n" + "(SELECT TDCONO, TDDIVI, TDORNO, TDITNO, SUM(TDIQTY) AS TDIQTY, TDUNIT \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL  \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "AND TDSTAS = '10' \n" + "GROUP BY TDCONO, TDDIVI, TDORNO, TDITNO, TDUNIT \n"
						+ "--ORDER BY TDLINE \n" + ") AS b \n" + "ON b.TDCONO = a.TTCONO \n"
						+ "AND b.TDDIVI = a.TTDIVI \n" + "--AND b.TDORNO = a.TTORNO \n" + "AND b.TDITNO = a.TTITNO \n"
						+ "LEFT JOIN  \n"
						+ "(SELECT TDCONO AS TSCONO, TDDIVI AS TSDIVI, TDORNO AS TSORNO, TDITNO AS TSITNO, SUM(TDIQTY) AS TSIQTY, TDUNIT AS TSUNIT \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDITNO, TDUNIT \n" + "--ORDER BY TDLINE \n" + ") AS c \n"
						+ "ON c.TSCONO = a.TTCONO \n" + "AND c.TSDIVI = a.TTDIVI \n" + "--AND c.TSORNO = a.TTORNO \n"
						+ "AND c.TSITNO = a.TTITNO \n" + "LEFT JOIN  \n"
						+ "(SELECT OBCONO, OBDIVI, OBORNO, OBITNO, OBTEDS, SUM(OBORQA * 1) AS OBORQA \n" + "FROM "
						+ DBM3NAME + ".OOLINE \n" + "WHERE OBCONO = '" + cono + "' \n" + "AND OBDIVI = '" + divi
						+ "' \n" + "AND OBORNO = '" + conumber + "' \n"
						+ "GROUP BY OBCONO, OBDIVI, OBORNO, OBITNO, OBTEDS) AS d \n" + "ON d.OBCONO = a.TTCONO \n"
						+ "AND d.OBDIVI = a.TTDIVI \n" + "--AND d.OBORNO = a.TTORNO \n" + "AND d.OBITNO = a.TTITNO \n"
						+ "LEFT JOIN  \n" + "(SELECT UBCONO, UBDIVI, UBIVNO, UBITNO, SUM(UBIVQA * 1) AS UBIVQA \n"
						+ "FROM " + DBM3NAME + ".ODLINE \n" + "WHERE UBCONO = '" + cono + "' \n" + "AND UBDIVI = '"
						+ divi + "' \n" + "AND UBYEA4 = (SELECT YEAR(THDEDA) AS YEAR \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
						+ "AND THORNO = '" + orderno + "') \n" + "AND UBIVNO = '" + invnumber + "' \n"
						+ "GROUP BY UBCONO, UBDIVI, UBIVNO, UBITNO) AS e \n" + "ON e.UBCONO = a.TTCONO \n"
						+ "AND e.UBDIVI = a.TTDIVI \n" + "--AND e.UBIVNO = a.TTORNO \n" + "AND e.UBITNO = a.TTITNO \n"
						+ "LEFT JOIN  \n"
						+ "(SELECT MMCONO, MMITNO, TRIM(MMFUDS) AS MMFUDS, CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMUNMS \n"
						+ "FROM " + DBM3NAME + ".MITMAS) AS f \n" + "ON f.MMCONO = a.TTCONO \n"
						+ "AND f.MMITNO = a.TTITNO) AS aa \n"
						+ "GROUP BY TTITNO, MMFUDS, TAKENO_QTY, SUPPNO_QTY, CONO_QTY, INVNO_QTY, MMUNMS, DIFF";
				// System.out.println("getMonitoringDetailHistory\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMonitoringDetail(String cono, String divi, String orderno, String conumber,
			String invnumber) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER() AS LINE, TTITNO, MMFUDS, MMUNMS AS UNIT, TAKENO_QTY, SUPPNO_QTY, CONO_QTY, INVNO_QTY, DIFF, REMARK, CHAR(TDENDA, ISO) AS TAKEDATE,TDENTI AS TAKETIME, OBDATE AS CODATE, OBTIME AS COTIME, UBDATE AS INVDATE, UBTIME AS INVTIME \n"
						+ "FROM  \n"
						+ "(SELECT NO, TTCONO, TTDIVI ,THORNO, TSORNO, TSCONU, TSIVNO, THDEDA, TTITNO, MMFUDS, CASE WHEN TDUNIT IS NOT NULL THEN TDUNIT ELSE MMUNMS END AS MMUNMS, COALESCE(TDIQTY,0) AS TAKENO_QTY, COALESCE(TSIQTY,0) AS SUPPNO_QTY, COALESCE(OBORQA,0) AS CONO_QTY, COALESCE(UBIVQA,0) AS INVNO_QTY, COALESCE(TDIQTY,0) - COALESCE(UBIVQA,0) AS DIFF \n"
						+ ",COALESCE(TDREM1,'') AS REMARK, TDENDA, TDENTI, TSENDA, TSENTI, OBDATE, OBTIME, UBDATE, UBTIME \n"
						+ "FROM  \n"
						+ "(SELECT 1 AS NO, TDCONO AS TTCONO, TDDIVI AS TTDIVI, TDORNO AS TTORNO, TRIM(TDITNO) AS TTITNO \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL  \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "AND TDSTAS = '10' \n" + "UNION ALL \n"
						+ "SELECT 2 AS NO, TDCONO AS TTCONO, TDDIVI AS TTDIVI, TDORNO AS TTORNO, TRIM(TDITNO) AS TTITNO \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT  \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n" + "UNION ALL \n"
						+ "SELECT 3 AS NO, OBCONO AS TTCONO, OBDIVI AS TTDIVI, OBORNO AS TTORNO, TRIM(OBITNO) AS TTITNO \n"
						+ "FROM " + DBM3NAME + ".OOLINE \n" + "WHERE OBCONO = '" + cono + "' \n" + "AND OBDIVI = '"
						+ divi + "' \n" + "AND OBORNO = '" + conumber + "' \n" + "AND OBORST IN ('27','77') \n"
						+ "UNION ALL \n"
						+ "SELECT 4 AS NO, UBCONO AS TTCONO, UBDIVI AS TTDIVI, COALESCE(CAST('00' || UBIVNO AS VARCHAR(10)), '') AS TTORNO, TRIM(UBITNO) AS TTITNO \n"
						+ "FROM " + DBM3NAME + ".ODLINE \n" + "WHERE UBCONO = '" + cono + "' \n" + "AND UBDIVI = '"
						+ divi + "' \n" + "AND UBYEA4 = (SELECT YEAR(THDEDA) AS YEAR \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
						+ "AND THORNO = '" + orderno + "') \n" + "AND UBIVNO = '" + invnumber + "') AS a \n"
						+ "LEFT JOIN  \n"
						+ "(SELECT TDCONO, TDDIVI, TDORNO, TDITNO, SUM(TDIQTY) AS TDIQTY, TDUNIT, TDREM1, TDENDA, TDENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL  \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "AND TDSTAS = '10' \n"
						+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDITNO, TDUNIT, TDREM1, TDENDA, TDENTI) AS b \n"
						+ "ON b.TDCONO = a.TTCONO \n" + "AND b.TDDIVI = a.TTDIVI \n" + "AND b.TDITNO = a.TTITNO \n"
						+ "LEFT JOIN  \n"
						+ "(SELECT TDCONO AS TSCONO, TDDIVI AS TSDIVI, TDORNO AS TSORNO, TDCONU AS TSCONU, TDIVNO AS TSIVNO, TDITNO AS TSITNO, SUM(TDIQTY) AS TSIQTY, TDUNIT AS TSUNIT, TDENDA AS TSENDA, TDENTI AS TSENTI \n"
						+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" + "WHERE TDCONO = '" + cono + "' \n"
						+ "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDITNO, TDUNIT, TDENDA, TDENTI) AS c \n"
						+ "ON c.TSCONO = a.TTCONO \n" + "AND c.TSDIVI = a.TTDIVI \n" + "AND c.TSITNO = a.TTITNO \n"
						+ "LEFT JOIN  \n"
						+ "(SELECT OBCONO, OBDIVI, OBORNO, OBITNO, OBTEDS, SUM(OBORQA * 1) AS OBORQA \n"
						+ ",SUBSTRING(OBRGDT,1,4)||'-'||SUBSTRING(OBRGDT,5,2)||'-'||SUBSTRING(OBRGDT,7,2) AS OBDATE \n"
						+ ",CAST(SUBSTR(RIGHT('00' || OBRGTM, 6) ,1,2) || ':' || SUBSTR(RIGHT('00' || OBRGTM, 6) ,3,2) || ':' || SUBSTR(RIGHT( '00' || OBRGTM, 6) ,5,2) AS TIME) AS OBTIME \n"
						+ "FROM " + DBM3NAME + ".OOLINE \n" + "WHERE OBCONO = '" + cono + "' \n" + "AND OBDIVI = '"
						+ divi + "' \n" + "AND OBORNO = '" + conumber + "' \n" +
						// "AND OBORST IN ('27','77') \n" +
						"GROUP BY OBCONO, OBDIVI, OBORNO, OBITNO, OBTEDS, OBRGDT, OBRGTM) AS d \n"
						+ "ON d.OBCONO = a.TTCONO \n" + "AND d.OBDIVI = a.TTDIVI \n" + "AND d.OBITNO = a.TTITNO \n"
						+ "LEFT JOIN  \n" + "(SELECT UBCONO, UBDIVI, UBIVNO, UBITNO, SUM(UBIVQA * 1) AS UBIVQA \n"
						+ ",SUBSTRING(UBRGDT,1,4)||'-'||SUBSTRING(UBRGDT,5,2)||'-'||SUBSTRING(UBRGDT,7,2) AS UBDATE \n"
						+ ",CAST(SUBSTR(RIGHT('00' || UBRGTM, 6) ,1,2) || ':' || SUBSTR(RIGHT('00' || UBRGTM, 6) ,3,2) || ':' || SUBSTR(RIGHT( '00' || UBRGTM, 6) ,5,2) AS TIME) AS UBTIME \n"
						+ "FROM " + DBM3NAME + ".ODLINE \n" + "WHERE UBCONO = '" + cono + "' \n" + "AND UBDIVI = '"
						+ divi + "' \n" + "AND UBYEA4 = (SELECT YEAR(THDEDA) AS YEAR \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
						+ "AND THORNO = '" + orderno + "') \n" + "AND UBIVNO = '" + invnumber + "' \n"
						+ "GROUP BY UBCONO, UBDIVI, UBIVNO, UBITNO, UBRGDT, UBRGTM) AS e \n"
						+ "ON e.UBCONO = a.TTCONO \n" + "AND e.UBDIVI = a.TTDIVI \n" + "AND e.UBITNO = a.TTITNO \n"
						+ "LEFT JOIN  \n"
						+ "(SELECT MMCONO, MMITNO, TRIM(MMFUDS) AS MMFUDS, CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMUNMS \n"
						+ "FROM " + DBM3NAME + ".MITMAS) AS f \n" + "ON f.MMCONO = a.TTCONO \n"
						+ "AND f.MMITNO = a.TTITNO \n" + "LEFT JOIN \n"
						+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THORDA, THDEDA \n" + "FROM " + DBNAME
						+ ".M3_TAKEORDERHEAD \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
						+ "AND THORNO = '" + orderno + "') AS g \n" + "ON 1 = 1) AS aa \n"
						+ "GROUP BY TTITNO, MMFUDS, TAKENO_QTY, SUPPNO_QTY, CONO_QTY, INVNO_QTY, MMUNMS, DIFF, REMARK, TDENDA, TDENTI, TSENDA, TSENTI, OBDATE, OBTIME, UBDATE, UBTIME";
				// System.out.println("getMonitoringDetailV2\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getRSTakeOrder(String cono, String divi, String orderno, String date, String month,
			String year, String status, String credit, String username, String group, String auth) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				String limit = "";

				if (date == null && month == null) {
					limit = "LIMIT 1000";
				}

				if (orderno != null) {
					orderno = "IN ('" + orderno + "')";
				} else {
					orderno = "NOT IN ('0')";
				}

				if (date != null) {
					date = "IN ('" + date + "')";
				} else {
					date = "NOT IN ('1111-11-11')";
				}

				if (month != null) {
					month = "IN ('" + month + "')";
				} else {
					month = "NOT IN ('0')";
				}

				if (year != null) {
					year = "IN ('" + year + "')";
				} else {
					year = "NOT IN ('0')";
				}

				if (status != null) {
					status = "IN ('" + status + "')";
				} else {
					status = "NOT IN ('0')";
				}

				if (credit != null) {
					credit = "CREDIT IN ('" + credit + "')";
				} else {
					credit = "CREDIT IS NULL OR CREDIT IS NOT NULL";
				}

				Statement stmt = conn.createStatement();
				ResultSet mRes = null;
				String query = null;
				if (group.equals("SALESUP")) {

					if (auth.equals("1")) {

						query = "SELECT THCONO, THDIVI, THSTAS \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THORNO ELSE '' END AS ORDERNO \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THCUNO ELSE '' END AS CUSTOMER \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN OKCUNM ELSE '' END AS CUSTOMER_NAME \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THDEDA ELSE '' END AS DELI_DATE \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THREM1 ELSE '' END AS HREMARK \n"
								+ ",ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) AS NO \n"
								+ ",TDITNO AS ITEM, MMFUDS AS ITEM_DESC, COALESCE(TDIQTY,0) AS QTY, CASE WHEN TDUNIT IS NOT NULL THEN TDUNIT ELSE MMUNMS END AS UNIT \n"
								+ ",TDREM1 AS DREMARK, THSANO AS SALEMAN, THSASU AS SALESUP, COALESCE(CREDIT,'') AS CREDIT \n"
								+ "FROM  \n"
								+ "(SELECT THCONO, THDIVI, THSTAS, THORNO, THORTY, THWHLO, THROUN, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THREM1, THSANO, THSASU \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b  \n"
								+ "WHERE THCONO = '" + cono + "'  \n" + "AND THDIVI = '" + divi + "' \n"
								+ "AND a.THCONO = b.OKCONO  \n" + "AND a.THCUNO = b.OKCUNO \n" + "AND a.THORNO "
								+ orderno + " \n" + "AND a.THDEDA " + date + " \n"
								+ "AND YEAR(a.THDEDA) || SUBSTRING(CHAR(a.THDEDA, ISO),6,2) " + month + " \n"
								+ "AND YEAR(a.THDEDA) " + year + " \n" + "AND a.THSTAS " + status + " \n" +
								// "AND a.THSANO = '" + username + "' \n" +
								// "AND a.THSASU = '" + username + "' \n" +
								"ORDER BY THCONO, THDIVI, THDEDA, THORNO) AS a \n" + "LEFT JOIN  \n"
								+ "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDITNO, SUM(TDIQTY) AS TDIQTY, TDUNIT, TDREM1 \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL  \n"
								+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDITNO, TDUNIT, TDREM1) AS b \n"
								+ "ON b.TDCONO = a.THCONO \n" + "AND b.TDDIVI = a.THDIVI \n"
								+ "AND b.TDORNO = a.THORNO \n" + "LEFT JOIN  \n"
								+ "(SELECT TDCONO AS TSCONO, TDDIVI AS TSDIVI, TDORNO AS TSORNO, TDCONU AS TSCONU, TDIVNO AS TSIVNO, CHAR(OAOBLC) AS CREDIT \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".OOHEAD b \n"
								+ "WHERE b.OACONO = a.TDCONO \n" + "AND b.OAORNO = a.TDCONU \n"
								+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, OAOBLC) AS c \n"
								+ "ON c.TSCONO = a.THCONO \n" + "AND c.TSDIVI = a.THDIVI \n"
								+ "AND c.TSORNO = a.THORNO \n" + "LEFT JOIN  \n"
								+ "(SELECT MMCONO, MMITNO, TRIM(MMFUDS) AS MMFUDS, CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMUNMS \n"
								+ "FROM " + DBM3NAME + ".MITMAS) AS d \n" + "ON d.MMCONO = b.TDCONO \n"
								+ "AND d.MMITNO = b.TDITNO \n" + "WHERE " + credit + " \n" + "AND TDIQTY > 0 \n"
								+ "ORDER BY THCONO, THDIVI, THORNO DESC " + limit + "";
						// System.out.println("getRSTakeOrder\n" + query);
						// mRes = stmt.executeQuery(query);

					} else {

						query = "SELECT THCONO, THDIVI, THSTAS \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THORNO ELSE '' END AS ORDERNO \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THCUNO ELSE '' END AS CUSTOMER \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN OKCUNM ELSE '' END AS CUSTOMER_NAME \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THDEDA ELSE '' END AS DELI_DATE \n"
								+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THREM1 ELSE '' END AS HREMARK \n"
								+ ",ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) AS NO \n"
								+ ",TDITNO AS ITEM, MMFUDS AS ITEM_DESC, COALESCE(TDIQTY,0) AS QTY, CASE WHEN TDUNIT IS NOT NULL THEN TDUNIT ELSE MMUNMS END AS UNIT \n"
								+ ",TDREM1 AS DREMARK, THSANO AS SALEMAN, THSASU AS SALESUP, COALESCE(CREDIT,'') AS CREDIT \n"
								+ "FROM  \n"
								+ "(SELECT THCONO, THDIVI, THSTAS, THORNO, THORTY, THWHLO, THROUN, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THREM1, THSANO, THSASU \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b  \n"
								+ "WHERE THCONO = '" + cono + "'  \n" + "AND THDIVI = '" + divi + "' \n"
								+ "AND a.THCONO = b.OKCONO  \n" + "AND a.THCUNO = b.OKCUNO \n" + "AND a.THORNO "
								+ orderno + " \n" + "AND a.THDEDA " + date + " \n"
								+ "AND YEAR(a.THDEDA) || SUBSTRING(CHAR(a.THDEDA, ISO),6,2) " + month + " \n"
								+ "AND YEAR(a.THDEDA) " + year + " \n" + "AND a.THSTAS " + status + " \n" +
								// "AND a.THSANO = '" + username + "' \n" +
								"AND a.THSASU = '" + username + "' \n"
								+ "ORDER BY THCONO, THDIVI, THDEDA, THORNO) AS a \n" + "LEFT JOIN  \n"
								+ "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDITNO, SUM(TDIQTY) AS TDIQTY, TDUNIT, TDREM1 \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL  \n"
								+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDITNO, TDUNIT, TDREM1) AS b \n"
								+ "ON b.TDCONO = a.THCONO \n" + "AND b.TDDIVI = a.THDIVI \n"
								+ "AND b.TDORNO = a.THORNO \n" + "LEFT JOIN  \n"
								+ "(SELECT TDCONO AS TSCONO, TDDIVI AS TSDIVI, TDORNO AS TSORNO, TDCONU AS TSCONU, TDIVNO AS TSIVNO, CHAR(OAOBLC) AS CREDIT \n"
								+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".OOHEAD b \n"
								+ "WHERE b.OACONO = a.TDCONO \n" + "AND b.OAORNO = a.TDCONU \n"
								+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, OAOBLC) AS c \n"
								+ "ON c.TSCONO = a.THCONO \n" + "AND c.TSDIVI = a.THDIVI \n"
								+ "AND c.TSORNO = a.THORNO \n" + "LEFT JOIN  \n"
								+ "(SELECT MMCONO, MMITNO, TRIM(MMFUDS) AS MMFUDS, CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMUNMS \n"
								+ "FROM " + DBM3NAME + ".MITMAS) AS d \n" + "ON d.MMCONO = b.TDCONO \n"
								+ "AND d.MMITNO = b.TDITNO \n" + "WHERE " + credit + " \n" + "AND TDIQTY > 0 \n"
								+ "ORDER BY THCONO, THDIVI, THORNO DESC " + limit + "";
						// System.out.println("getRSTakeOrder\n" + query);
						// mRes = stmt.executeQuery(query);

					}

				} else {
					query = "SELECT THCONO, THDIVI, THSTAS \n"
							+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THORNO ELSE '' END AS ORDERNO \n"
							+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THCUNO ELSE '' END AS CUSTOMER \n"
							+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN OKCUNM ELSE '' END AS CUSTOMER_NAME \n"
							+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THDEDA ELSE '' END AS DELI_DATE \n"
							+ ",CASE WHEN ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) = 1 THEN THREM1 ELSE '' END AS HREMARK \n"
							+ ",ROW_NUMBER() OVER (PARTITION BY THCONO, THDIVI, THORNO) AS NO \n"
							+ ",TDITNO AS ITEM, MMFUDS AS ITEM_DESC, COALESCE(TDIQTY,0) AS QTY, CASE WHEN TDUNIT IS NOT NULL THEN TDUNIT ELSE MMUNMS END AS UNIT"
							+ ",TDREM1 AS DREMARK, THSANO AS SALEMAN, THSASU AS SALESUP, COALESCE(CREDIT,'') AS CREDIT \n"
							+ "FROM  \n"
							+ "(SELECT THCONO, THDIVI, THSTAS, THORNO, THORTY, THWHLO, THROUN, THORDA, CHAR(THDEDA, ISO) AS THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THREM1, THSANO, THSASU \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b  \n"
							+ "WHERE THCONO = '" + cono + "'  \n" + "AND THDIVI = '" + divi + "' \n"
							+ "AND a.THCONO = b.OKCONO  \n" + "AND a.THCUNO = b.OKCUNO \n" + "AND a.THORNO " + orderno
							+ " \n" + "AND a.THDEDA " + date + " \n"
							+ "AND YEAR(a.THDEDA) || SUBSTRING(CHAR(a.THDEDA, ISO),6,2) " + month + " \n"
							+ "AND YEAR(a.THDEDA) " + year + " \n" + "AND a.THSTAS " + status + " \n"
							+ "AND a.THSANO = '" + username + "' \n" +
							// "AND a.THSASU = '" + username + "' \n" +
							"ORDER BY THCONO, THDIVI, THDEDA, THORNO) AS a \n" + "LEFT JOIN  \n"
							+ "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDITNO, SUM(TDIQTY) AS TDIQTY, TDUNIT, TDREM1 \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERDETAIL  \n"
							+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDITNO, TDUNIT, TDREM1) AS b \n"
							+ "ON b.TDCONO = a.THCONO \n" + "AND b.TDDIVI = a.THDIVI \n" + "AND b.TDORNO = a.THORNO \n"
							+ "LEFT JOIN  \n"
							+ "(SELECT TDCONO AS TSCONO, TDDIVI AS TSDIVI, TDORNO AS TSORNO, TDCONU AS TSCONU, TDIVNO AS TSIVNO, CHAR(OAOBLC) AS CREDIT \n"
							+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".OOHEAD b \n"
							+ "WHERE b.OACONO = a.TDCONO \n" + "AND b.OAORNO = a.TDCONU \n"
							+ "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, OAOBLC) AS c \n"
							+ "ON c.TSCONO = a.THCONO \n" + "AND c.TSDIVI = a.THDIVI \n" + "AND c.TSORNO = a.THORNO \n"
							+ "LEFT JOIN  \n"
							+ "(SELECT MMCONO, MMITNO, TRIM(MMFUDS) AS MMFUDS, CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMUNMS \n"
							+ "FROM " + DBM3NAME + ".MITMAS) AS d \n" + "ON d.MMCONO = b.TDCONO \n"
							+ "AND d.MMITNO = b.TDITNO \n" + "WHERE " + credit + " \n" + "AND TDIQTY > 0 \n"
							+ "ORDER BY THCONO, THDIVI, THORNO DESC " + limit + "";
					// System.out.println("getRSTakeOrder\n" + query);
					// mRes = stmt.executeQuery(query);

				}

				return query;

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

	public static String getItemAsset(String cono, String divi, String bu, String year) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT FMCONO, FMDIVI, FMASID, FMSBNO, FMFAST, FMFADS, FMTXT1, FMPPER, FMAIT2, COALESCE(FHCOST,1) AS FHCOST, COALESCE(FHFAVA,1) AS FHFAVA, FMFAQT, ITEM \n"
						+ "FROM  \n"
						+ "(SELECT FMCONO, FMDIVI, TRIM(FMASID) AS FMASID, FMSBNO, FMFAST, TRIM(FMFADS) AS FMFADS, TRIM(FMTXT1) AS FMTXT1, SUBSTRING(FMPPER,1,4)||'-'||SUBSTRING(FMPPER,5,2)||'-'||SUBSTRING(FMPPER,7,2) AS FMPPER, FMAIT2, FMFAQT, '202211' AS YEAR \n"
						+ ", TRIM(FMASID) || ' : ' || FMSBNO || ' : ' || TRIM(FMTXT1) AS ITEM \n"
						+ "FROM " + DBM3NAME + ".FFASMA a \n" + "WHERE FMCONO = '" + cono + "' \n" + "AND FMDIVI = '"
						+ divi
						+ "' \n" + "AND FMFAST IN ('1','8') \n"
						+ "AND FMFATP NOT IN ('11','11.1','11.2','15','81','82','83') \n"
						+ "AND SUBSTRING(FMFATP,0,2) NOT IN ('9') \n" + "AND FMAIT2 IN (SELECT S2AITM \n"
						+ "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME + ".FCHACC b \n" + "WHERE S2CONO = '" + cono
						+ "' \n"
						+ "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '1' \n" + "AND b.EACONO = a.S2CONO \n"
						+ "AND b.EADIVI = a.S2DIVI \n" + "AND b.EAAITP = '2' \n" + "AND b.EAAITM = a.S2AITM \n"
						+ "AND S2STID IN (SELECT TRIM(S2AITM) \n" + "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME
						+ ".FSTDEF b \n"
						+ "WHERE S2CONO = '" + cono + "' \n" + "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '2' \n"
						+ "AND S2STID = '" + bu + "' \n" + "AND S1CONO = a.S2CONO \n" + "AND S1DIVI = a.S2DIVI \n"
						+ "AND S1STID = a.S2AITM \n" + "ORDER BY S2AITM) \n" + "ORDER BY S2AITM)"
						+ "ORDER BY FMASID) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT FHCONO, FHDIVI, FHASID, FHVPER, FHVATP, FHFAVA \n" + "FROM " + DBM3NAME
						+ ".FFAHIS \n"
						+ "WHERE FHVATP = '30' \n" + "--AND FHASID = '0000004982' \n" + ") AS b \n"
						+ "ON b.FHCONO = a.FMCONO \n" + "AND b.FHASID = a.FMASID  \n" + "AND b.FHVPER = a.YEAR \n"
						+ "LEFT JOIN \n" + "(SELECT FHCONO, FHDIVI, FHASID, FHAIT2, FHCOST \n"
						+ "FROM " + Constant.DBNAME + ".V_ADRITEM) AS c \n" + "ON c.FHCONO = a.FMCONO \n"
						+ "AND c.FHDIVI = a.FMDIVI \n" + "AND c.FHASID = a.FMASID \n" + "AND c.FHAIT2 = a.FMAIT2 ";
				// System.out.println("getItem\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getItemAssetV2(String cono, String divi, String bu, String year) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT FMCONO, FMDIVI, FMASID, FMSBNO, FMFAST, FMFADS, FMTXT1, FMPPER, FMAIT2, COALESCE(b.FHCOST,1) AS FHCOST \n"
						+ ", COALESCE(b.FHCOST - c.FHCOST,1) AS FHFAVA, FMFAQT, ITEM \n" + "FROM  \n"
						+ "(SELECT FMCONO, FMDIVI, TRIM(FMASID) AS FMASID, FMSBNO, FMFAST, TRIM(FMFADS) AS FMFADS, TRIM(FMTXT1) AS FMTXT1, SUBSTRING(FMPPER,1,4)||'-'||SUBSTRING(FMPPER,5,2)||'-'||SUBSTRING(FMPPER,7,2) AS FMPPER, FMAIT2, FMFAQT, '202211' AS YEAR \n"
						+ ", TRIM(FMASID) || ' : ' || FMSBNO || ' : ' || TRIM(FMTXT1) AS ITEM \n"
						+ "FROM " + DBM3NAME + ".FFASMA a \n" + "WHERE FMCONO = '" + cono + "' \n" + "AND FMDIVI = '"
						+ divi
						+ "' \n" + "AND FMFAST IN ('1','8') \n" + "--AND FMASID = '0000014369' \n"
						+ "AND FMFATP NOT IN ('11','11.1','11.2','15','81','82','83') \n"
						+ "AND SUBSTRING(FMFATP,0,2) NOT IN ('9') \n" + "AND FMAIT2 IN (SELECT S2AITM \n"
						+ "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME + ".FCHACC b \n" + "WHERE S2CONO = '" + cono
						+ "' \n"
						+ "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '1' \n" + "AND b.EACONO = a.S2CONO \n"
						+ "AND b.EADIVI = a.S2DIVI \n" + "AND b.EAAITP = '2' \n" + "AND b.EAAITM = a.S2AITM \n"
						+ "AND S2STID IN (SELECT TRIM(S2AITM) \n" + "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME
						+ ".FSTDEF b \n"
						+ "WHERE S2CONO = '" + cono + "' \n" + "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '2' \n"
						+ "AND S2STID = '" + bu + "' \n" + "AND S1CONO = a.S2CONO \n" + "AND S1DIVI = a.S2DIVI \n"
						+ "AND S1STID = a.S2AITM \n" + "ORDER BY S2AITM) \n"
						+ "ORDER BY S2AITM)ORDER BY FMASID) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT FHCONO, FHDIVI, TRIM(FHASID) AS FHASID, SUM(FHFAVA) AS FHCOST \n"
						+ "FROM " + DBM3NAME + ".FFAHIS \n" + "WHERE FHAIT2 != '' \n" + "--AND FHASID = 'MKFF200001' \n"
						+ "AND FHAIT2 IN (SELECT S2AITM \n" + "FROM " + DBM3NAME + ".FSTLIN a \n" + "WHERE S2CONO = '"
						+ cono
						+ "' \n" + "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '1') \n" + "AND FHVATP IN ('10') \n"
						+ "GROUP BY FHCONO, FHDIVI, FHASID) AS b \n" + "ON b.FHCONO = a.FMCONO \n"
						+ "AND b.FHASID = a.FMASID  \n" + "LEFT JOIN \n"
						+ "(SELECT FHCONO, FHDIVI, TRIM(FHASID) AS FHASID, SUM(FHFAVA) AS FHCOST \n"
						+ "FROM " + DBM3NAME + ".FFAHIS \n" + "WHERE FHAIT2 != '' \n" + "--AND FHASID = 'MKFF200001' \n"
						+ "AND FHAIT2 IN (SELECT S2AITM \n" + "FROM " + DBM3NAME + ".FSTLIN a \n" + "WHERE S2CONO = '"
						+ cono
						+ "' \n" + "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '1') \n"
						+ "AND FHVATP IN ('20','30') \n" + "GROUP BY FHCONO, FHDIVI, FHASID) AS c \n"
						+ "ON c.FHCONO = a.FMCONO \n" + "AND c.FHASID = a.FMASID \n"
						+ "WHERE b.FHCOST > 0 AND c.FHCOST > 0";
				// System.out.println("getItemAssetV2\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getItemAssetV3(String cono, String divi, String bu, String year) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT FMCONO, FMDIVI, FMASID, FMSBNO, FMFAST, FMFADS, FMTXT1, FMPPER, FMAIT2, COALESCE(b.FHCOST,1) AS COST_10, COALESCE(c.FHCOST,1) AS COST_2030, COALESCE(ALQTY,0) AS ALQTY \n"
						+ ", COALESCE(b.FHCOST,1) AS FHCOST, COALESCE(b.FHCOST - c.FHCOST,1) AS FHFAVA, COALESCE(FMFAQT,0) - COALESCE(ALQTY,0) AS FMFAQT, ITEM \n"
						+ "FROM  \n"
						+ "(SELECT FMCONO, FMDIVI, TRIM(FMASID) AS FMASID, FMSBNO, FMFAST, TRIM(FMFADS) AS FMFADS, TRIM(FMTXT1) AS FMTXT1, SUBSTRING(FMPPER,1,4)||'-'||SUBSTRING(FMPPER,5,2)||'-'||SUBSTRING(FMPPER,7,2) AS FMPPER, FMAIT2, FMFAQT, '202211' AS YEAR \n"
						+ ", TRIM(FMASID) || ' : ' || FMSBNO || ' : ' || TRIM(FMTXT1) AS ITEM \n"
						+ "FROM " + DBM3NAME + ".FFASMA a \n" + "WHERE FMCONO = '" + cono + "' \n" + "AND FMDIVI = '"
						+ divi
						+ "' \n" + "AND FMFAST IN ('1','8') \n" + "--AND FMASID = '0000004479' \n"
						+ "AND FMFATP NOT IN ('11','11.1','11.2','15','81','82','83') \n"
						+ "AND SUBSTRING(FMFATP,0,2) NOT IN ('9') \n" + "AND FMAIT2 IN (SELECT S2AITM \n"
						+ "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME + ".FCHACC b \n" + "WHERE S2CONO = '" + cono
						+ "' \n"
						+ "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '1' \n" + "AND b.EACONO = a.S2CONO \n"
						+ "AND b.EADIVI = a.S2DIVI \n" + "AND b.EAAITP = '2' \n" + "AND b.EAAITM = a.S2AITM \n"
						+ "AND S2STID IN (SELECT TRIM(S2AITM) \n" + "FROM " + DBM3NAME + ".FSTLIN a, " + DBM3NAME
						+ ".FSTDEF b \n"
						+ "WHERE S2CONO = '" + cono + "' \n" + "AND S2DIVI = '" + divi + "' \n" + "AND S2SLVL = '2' \n"
						+ "AND S2STID = '" + bu + "' \n" + "AND S1CONO = a.S2CONO \n" + "AND S1DIVI = a.S2DIVI \n"
						+ "AND S1STID = a.S2AITM \n" + "ORDER BY S2AITM) \n" + "ORDER BY S2AITM) \n"
						+ "ORDER BY FMASID) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT FHCONO, FHDIVI, TRIM(FHASID) AS FHASID, FHSBNO, SUM(FHFAVA) AS FHCOST \n"
						+ "FROM " + DBM3NAME + ".FFAHIS \n" + "WHERE FHAIT2 != '' \n" + "--AND FHASID = '0000004479' \n"
						+ "--AND FHAIT2 IN (SELECT S2AITM \n" + "--FROM " + DBM3NAME + ".FSTLIN a \n"
						+ "--WHERE S2CONO = '10' \n" + "--AND S2DIVI = '101' \n" + "--AND S2SLVL = '1') \n"
						+ "AND FHVATP IN ('10') \n" + "GROUP BY FHCONO, FHDIVI, FHASID, FHSBNO) AS b \n"
						+ "ON b.FHCONO = a.FMCONO \n" + "AND b.FHASID = a.FMASID \n" + "AND b.FHSBNO = a.FMSBNO \n"
						+ "LEFT JOIN \n"
						+ "(SELECT FHCONO, FHDIVI, TRIM(FHASID) AS FHASID, FHSBNO, SUM(FHFAVA) AS FHCOST \n"
						+ "FROM " + DBM3NAME + ".FFAHIS \n" + "WHERE FHAIT2 != '' \n" + "--AND FHASID = '0000000012' \n"
						+ "--AND FHAIT2 IN (SELECT S2AITM \n" + "--FROM " + DBM3NAME + ".FSTLIN a \n"
						+ "--WHERE S2CONO = '10' \n" + "--AND S2DIVI = '101' \n" + "--AND S2SLVL = '1') \n"
						+ "AND FHVATP IN ('20','30') \n" + "GROUP BY FHCONO, FHDIVI, FHASID, FHSBNO) AS c \n"
						+ "ON c.FHCONO = a.FMCONO \n" + "AND c.FHASID = a.FMASID \n" + "AND c.FHSBNO = a.FMSBNO \n"
						+ "LEFT JOIN \n" + "(SELECT ALCONO, ALDIVI, ALITNO, ALSBNO, SUM(ALQTY) AS ALQTY \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRDETAIL a, " + Constant.DBNAME + ".M3_ADRHEAD b  \n"
						+ "WHERE ADSTAT >= '40' \n"
						+ "AND b.ADCONO = a.ALCONO \n" + "AND b.ADDIVI = a.ALDIVI \n"
						+ "AND b.ADPREF || '-' || b.ADORNO = a.ALPREF || '-' || a.ALORNO \n"
						+ "GROUP BY ALCONO, ALDIVI, ALITNO, ALSBNO \n" + "ORDER BY ALITNO) AS d \n"
						+ "ON d.ALCONO = a.FMCONO \n" + "AND d.ALITNO = a.FMASID \n" + "AND d.ALSBNO = a.FMSBNO \n"
						+ "ORDER BY FMASID, FMSBNO";
				// System.out.println("getItemAssetV3\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getItemPricelist(String cono, String divi, String pricelist, String customer)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT MMCONO, MMITNO, MMITDS, MMFUDS, ITEM, MMPUUN, MMPUPR, MMVTCP, MMCUCD, COALESCE(MUAUS2,0) AS MUAUS2, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+ "FROM \n"
						+ "(SELECT OMCONO AS MMCONO, TRIM(OMITNO) AS MMITNO, TRIM(MMITDS) AS MMITDS, TRIM(MMFUDS) AS MMFUDS, TRIM(OMITNO) || ' : ' || TRIM(MMFUDS) AS ITEM \n"
						+ ",CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN, MMPUPR, MMVTCP, TRIM(MMCUCD) AS MMCUCD \n"
						+ "FROM " + DBM3NAME + ".OPRICL a, " + DBM3NAME + ".MITMAS b \n" + "WHERE OMCONO = '" + cono
						+ "' \n" + "AND OMPRRF = '" + pricelist + "' \n" +
						// "AND OMCUNO = '"+customer+"' \n" +
						"AND b.MMCONO = OMCONO \n" + "AND b.MMITNO = a.OMITNO \n" + "AND b.MMSTAT = '20' \n"
						+ "AND SUBSTRING(b.MMITNO,0,2) BETWEEN 'A' AND 'Z' \n"
						+ "GROUP BY OMCONO, OMITNO, MMITDS, MMFUDS, OMITNO, MMPUUN, MMPUPR, MMVTCP, MMCUCD, MMACTI) AS a \n"
						+ "LEFT JOIN \n" + "(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" + "FROM "
						+ DBM3NAME + ".MITAUN \n" + "WHERE MUAUS2 = '1') AS b \n" + "ON b.MUCONO = a.MMCONO \n"
						+ "AND b.MUITNO = a.MMITNO \n" + "ORDER BY MMFUDS DESC";
				// System.out.println("getItem\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getItemPricelistCustomer(String cono, String divi, String pricelist, String customer)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT MMCONO, MMITNO, MMITDS, MMFUDS, ITEM, MMPUUN, MMPUPR, MMVTCP, MMCUCD, COALESCE(MUAUS2,0) AS MUAUS2, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+ "FROM \n"
						+ "(SELECT OMCONO AS MMCONO, TRIM(OMITNO) AS MMITNO, TRIM(MMITDS) AS MMITDS, TRIM(MMFUDS) AS MMFUDS, TRIM(OMITNO) || ' : ' || TRIM(MMFUDS) AS ITEM \n"
						+ ",CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN, MMPUPR, MMVTCP, TRIM(MMCUCD) AS MMCUCD \n"
						+ "FROM " + DBM3NAME + ".OPRICL a, " + DBM3NAME + ".MITMAS b \n" + "WHERE OMCONO = '" + cono
						+ "' \n" + "AND OMPRRF = '" + pricelist + "' \n" + "AND OMCUNO = '" + customer + "' \n"
						+ "AND b.MMCONO = OMCONO \n" + "AND b.MMITNO = a.OMITNO \n" + "AND b.MMSTAT = '20' \n"
						+ "AND SUBSTRING(b.MMITNO,0,2) BETWEEN 'A' AND 'Z' \n"
						+ "GROUP BY OMCONO, OMITNO, MMITDS, MMFUDS, OMITNO, MMPUUN, MMPUPR, MMVTCP, MMCUCD, MMACTI) AS a \n"
						+ "LEFT JOIN \n" + "(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" + "FROM "
						+ DBM3NAME + ".MITAUN \n" + "WHERE MUAUS2 = '1') AS b \n" + "ON b.MUCONO = a.MMCONO \n"
						+ "AND b.MUITNO = a.MMITNO \n" + "ORDER BY MMFUDS DESC";
				// System.out.println("getItem\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getItemHistoryCustomer(String cono, String divi, String pricelist, String customer)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT MMCONO, MMITNO, MMITDS, MMFUDS, TRIM(MMITNO) || ' : ' || TRIM(MMFUDS) AS ITEM , MMPUUN, MMPUPR, MMVTCP, MMCUCD \n"
						+ ", COALESCE(MUAUS2,0) AS MUAUS2, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+ "FROM \n" + "(SELECT OBCONO, OBITNO \n" + "FROM \n" + "(SELECT OACONO AS OBCONO, OBITNO \n"
						+ "FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".CSYTAB," + DBM3NAME
						+ ".MITMAS \n" + "WHERE OACONO = '" + cono + "' \n" + "AND OAORNO = OBORNO \n"
						+ "AND OACONO = MMCONO \n" + "AND OBITNO = MMITNO \n" + "AND CTCONO = OACONO \n"
						+ "AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" + "AND OACUNO = '"
						+ customer + "' \n" + "AND MMSTAT = '20' \n"
						+ "AND SUBSTRING(MMITNO,0,2) BETWEEN 'A' AND 'Z' \n" + "AND MMITTY IN ('FG','SP','RM') \n"
						+ "AND CTSTKY = OASMCD \n" + "AND CTSTCO ='SMCD' \n" + "GROUP BY OACONO, OBITNO \n"
						+ "UNION ALL \n" + "SELECT OMCONO AS OBCONO, TRIM(OMITNO) AS OBITNO \n" + "FROM " + DBM3NAME
						+ ".OPRICL a, " + DBM3NAME + ".MITMAS b \n" + "WHERE OMCONO = '" + cono + "' \n"
						+ "AND OMPRRF = '" + pricelist + "' \n" + "AND OMCUNO = '" + customer + "' \n"
						+ "AND b.MMCONO = OMCONO \n" + "AND b.MMITNO = a.OMITNO \n" + "AND b.MMSTAT = '20' \n"
						+ "AND SUBSTRING(b.MMITNO,0,2) BETWEEN 'A' AND 'Z' \n" + "AND b.MMITTY IN ('FG','SP','RM') \n"
						+ "GROUP BY OMCONO, OMITNO) AS A \n" + "GROUP BY OBCONO, OBITNO) AS AA \n" + "INNER JOIN \n"
						+ "(SELECT MMCONO, MMITNO, MMITDS, MMFUDS, MMCUCD, MMVTCP, MMPUPR, MMACTI, MMPUUN    \n"
						+ ", CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS, MUAUS2, MUALUN \n"
						+ "FROM \n" + "(SELECT MMCONO, MMITNO, MMITDS, MMFUDS, MMCUCD, MMVTCP, MMPUPR, MMACTI   \n"
						+ ",CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN \n" + "FROM " + DBM3NAME
						+ ".MITMAS \n" + "WHERE MMCONO = '" + cono + "' \n" + "AND MMSTAT = '20' \n" + ") AS A \n"
						+ "LEFT JOIN \n" + "(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" + "FROM "
						+ DBM3NAME + ".MITAUN \n" + "WHERE MUCONO = '" + cono + "' \n" + "AND MUAUS2 = '1') AS B \n"
						+ "ON B.MUCONO = A.MMCONO \n" + "AND B.MUITNO = A.MMITNO) AS B \n"
						+ "ON B.MMCONO = AA.OBCONO \n" + "AND B.MMITNO = AA.OBITNO \n" + "ORDER BY MMFUDS DESC";
				// System.out.println("getItemHistory\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getItemUnit(String cono, String divi, String item) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ROW_NUMBER() OVER(ORDER BY MMUNMS) AS ID, MMUNMS \n" + "FROM \n"
						+ "(SELECT MMUNMS \n" + "FROM " + DBM3NAME + ".MITMAS \n" + "WHERE MMCONO = '" + cono + "' \n"
						+ "AND MMITNO = '" + item + "' \n" + "UNION ALL \n" + "SELECT MUALUN AS MMUNMS \n" + "FROM "
						+ DBM3NAME + ".MITAUN \n" + "WHERE MUCONO = '" + cono + "' \n" + "AND MUITNO = '" + item
						+ "' \n" + "GROUP BY MUALUN) AS A";
				// System.out.println("getItemUnit\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getMaxOrderNumber(String cono, String divi, String year) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT CASE WHEN MAX(THORNO) > 0 THEN MAX(THORNO) + 1 ELSE 0 END AS THORNO \n" + "FROM "
						+ DBNAME + ".M3_TAKEORDERHEAD mp \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '"
						+ divi + "' \n" + "AND SUBSTRING(THORNO,0,3) = '" + year + "'";
				// System.out.println("getMaxOrderNumber\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("THORNO").trim();
				}

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

	public static String getMaxOrderNumberV2(String cono, String divi, String year, String username) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT MAX(THORNO) AS THORNO \n" + "FROM " + DBNAME + ".M3_TAKEORDERHEAD \n"
						+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
						+ "AND SUBSTRING(THORNO,0,3) = '" + year + "' \n" + "AND THSANO = '" + username + "'";
				// System.out.println("getMaxOrderNumber\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("THORNO").trim();
				}

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

	public static String getMaxOrderNumberV3(String cono, String divi, String year, String username) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT THORNO,TRIM(THORNO) || ' : ' || TRIM(OKCUNM) AS ORDERNUMBER \n" + "FROM "
						+ DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n" + "WHERE THCONO = '" + cono
						+ "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THSTAS = '00' \n"
						+ "AND b.OKCONO = THCONO \n" + "AND b.OKCUNO = THCUNO \n" + "AND b.OKSTAT = '20' \n"
						+ "AND SUBSTRING(THORNO,0,3) = '" + year + "' \n" + "AND THSANO = '" + username + "' \n"
						+ "ORDER BY THORNO DESC \n" + "LIMIT 1";
				// System.out.println("getMaxOrderNumber\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("ORDERNUMBER").trim();
				}

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

	public static String getMaxADRNumber(String cono, String divi, String prefix, String year, String username)
			throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ADPREF || '-' || ADORNO AS ADRNUMBER \n" + "FROM " + Constant.DBNAME
						+ ".M3_ADRHEAD \n"
						+ "WHERE ADCONO = '" + cono + "' \n" + "AND ADDIVI = '" + divi + "' \n" + "AND ADPREF = '"
						+ prefix + "' \n" + "AND SUBSTRING(CHAR(ADDATE,ISO),0,5) = '" + year + "' \n" + "AND ADREQU = '"
						+ username + "' \n" + "ORDER BY ADORNO DESC \n" + "LIMIT 1";
				// System.out.println("getMaxADRNumber\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("ADRNUMBER").trim();
				}

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

	public static String getMaxADRLine(String cono, String divi, String adrno) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT COALESCE(MAX(ALLINE),0) + 1 AS ALLINE \n" + "FROM " + Constant.DBNAME
						+ ".M3_ADRDETAIL \n"
						+ "WHERE ALCONO = '" + cono + "' \n" + "AND ALDIVI = '" + divi + "' \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "'";
				// System.out.println("getMaxADRLine\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("ALLINE").trim();
				}

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

	public static String getSalesuport(String cono, String divi, String saleman) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT S_SUPP, S_SALE, S_CHAN \n" + "FROM " + Constant.DBNAME + ".SAL_SUPSALE a,"
						+ DBM3NAME + ".CMNUSR b \n"
						+ "WHERE b.JUUSID = a.S_SUPP \n" + "AND a.S_SALE = '" + saleman + "' \n"
						+ "GROUP BY S_SUPP,S_SALE,S_CHAN";
				// System.out.println("getSaleSuport\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("S_SUPP").trim();
				}

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

		return "n/a";

	}

	public static String getSalesuport_bac211005(String cono, String divi, String saleman) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT S_CONO, S_SUPP, S_SALE, S_CHAN, SP_USER \n"
						+ "FROM " + Constant.DBNAME + ".SAL_SUPSALE a, " + Constant.DBNAME + ".SAL_SUPPORT b \n"
						+ "WHERE S_CONO = '" + cono
						+ "' \n" + "AND S_SALE = '" + saleman + "' \n" + "AND b.SP_CONO = S_CONO \n"
						+ "AND b.SP_ID = S_SUPP";
				// System.out.println("getSaleSuport\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("S_SUPP").trim();
				}

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

	public static String getSaleman(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER(ORDER BY JUUSID) AS ID, TRIM(JUUSID) AS S_ID, TRIM(JUTX40) AS S_NAME, TRIM(JUUSID) || ' : ' || TRIM(JUTX40) AS SALEMAN \n"
						+ "FROM " + DBM3NAME + ".CMNUSR \n" + "WHERE JULSID != '' \n" + "AND JUFRF7 = 'SALEMAN' \n"
						+ "AND JUUSTA = '20'";
				// System.out.println("getSaleman\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getUser(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT JUUSID, JULSID, JUTX40, TRIM(JUFRF6) AS JUFRF6, JUFRF7, CAST(JUFRF8 AS DECIMAL(2,0)) AS JUFRF8 \n"
						+ "FROM " + DBM3NAME + ".CMNUSR \n" + "WHERE JULSID != '' \n"
						// + "AND JUFRF6 != '' \n"
						+ "AND JUFRF7 IN ('SALESUP') \n" + "AND JUUSTA = '20' \n" + "ORDER BY JUFRF7 DESC, JUUSID ";
				// System.out.println("getUserManagement\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getUser_bac211005(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT JUUSID, JULSID, JUTX40, TRIM(JUFRF6) AS JUFRF6, JUFRF7, CAST(JUFRF8 AS DECIMAL(2,0)) AS JUFRF8 \n"
						+ "FROM " + DBM3NAME + ".CMNUSR \n" + "WHERE JULSID != '' \n"
						// + "AND JUFRF6 != '' \n"
						+ "AND JUFRF7 != '' \n" + "AND JUUSTA = '20' \n" + "ORDER BY JUFRF7 DESC, JUFRF6 ";
				// System.out.println("getUserManagement\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getGroupingUser(String cono, String divi, String username) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT JUUSID, JULSID, JUTX40, TRIM(JUFRF6) AS JUFRF6, JUFRF7, JUFRF8, S_ID, S_NAME \n"
						+ "FROM \n" + "(SELECT JUUSID, JULSID, JUTX40, JUFRF6, JUFRF7, JUFRF8, S_SALE \n"
						+ "FROM " + DBM3NAME + ".CMNUSR a, " + Constant.DBNAME + ".SAL_SUPSALE b \n"
						+ "WHERE JUUSID = '" + username + "' \n"
						+ "AND JULSID != '' \n" + "AND JUFRF7 = 'SALESUP' \n" + "AND JUUSTA = '20' \n"
						+ "AND b.S_SUPP = JUUSID) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT JUUSID AS S_ID, JUTX40 AS S_NAME \n" + "FROM " + DBM3NAME + ".CMNUSR \n"
						+ "WHERE JULSID != '' \n" + "AND JUFRF7 = 'SALEMAN' \n" + "AND JUUSTA = '20' ) AS b \n"
						+ "ON b.S_ID = a.S_SALE \n" + "ORDER BY JUUSID, S_ID";
				// System.out.println("getGroupingUser\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getGroupingUser_bac211005(String cono, String divi, String username) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT JUUSID, JULSID, JUTX40, TRIM(JUFRF6) AS JUFRF6, JUFRF7, JUFRF8, S_ID, S_NAME \n"
						+ "FROM \n" + "(SELECT JUUSID, JULSID, JUTX40, JUFRF6, JUFRF7, JUFRF8, S_SALE \n"
						+ "FROM " + DBM3NAME + ".CMNUSR a, " + Constant.DBNAME + ".SAL_SUPSALE b \n"
						+ "WHERE JULSID != '' \n"
						+ "AND JUFRF6 = '" + username + "' \n" + "AND JUFRF7 = 'SALESUP' \n" + "AND JUUSTA = '20' \n"
						+ "AND b.S_SUPP = JUFRF6) AS a \n" + "LEFT JOIN \n"
						+ "(SELECT JUUSID AS S_ID, JUTX40 AS S_NAME \n" + "FROM " + DBM3NAME + ".CMNUSR \n"
						+ "WHERE JULSID != '' \n" + "AND JUFRF7 = 'SALEMAN' \n" + "AND JUUSTA = '20' ) AS b \n"
						+ "ON b.S_ID = a.S_SALE \n" + "ORDER BY JUFRF6, S_ID";
				// System.out.println("getGroupingUser\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getPriceList(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER(ORDER BY OJPRRF) AS ID, OJPRRF \n" + "FROM " + DBM3NAME
						+ ".OPRICH \n" + "WHERE OJCONO = 10 \n" + "--AND OJCUNO = 'TH01010477' \n"
						+ "AND OJPRRF != 'E1' \n" + "AND OJPRRF != '' \n" + "GROUP BY OJPRRF \n" + "ORDER BY OJPRRF";
				// System.out.println("getGroupingUser\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static List<String> getRSOrderDatail(String cono, String divi, String orderno) {

		List<String> getList = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = ConnectDB2.doConnect();
			Statement stmt = conn.createStatement();
			String query = "SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, TDREM1 \n"
					+ ", CASE WHEN TDREM2 = '' THEN CASE WHEN MUALUN2 IS NOT NULL THEN MUALUN2 ELSE MMUNMS END ELSE TDREM2 END AS TDREM2, TDSTAS, TDENDA, TDENTI \n"
					+ "FROM \n"
					+ "(SELECT TDCONO, TDDIVI, THDEDA, THCUNO, THROUN, THPRIC, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, MMUNMS, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
					+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBNAME + ".M3_TAKEORDERHEAD b, " + DBM3NAME
					+ ".MITMAS c \n" + "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '" + divi + "' \n"
					+ "AND TDORNO = '" + orderno + "' \n" + "AND TDSTAS = '10' \n" + "AND b.THCONO = TDCONO \n"
					+ "AND b.THDIVI = TDDIVI \n" + "AND b.THORNO = TDORNO \n" + "AND c.MMCONO = TDCONO \n"
					+ "AND c.MMITNO = TDITNO \n" + "ORDER BY TDORNO, TDLINE) AS a \n" + "LEFT JOIN \n"
					+ "(SELECT MUCONO AS MUCONO2, MUITNO AS MUITNO2, MUALUN AS MUALUN2 \n" + "FROM " + DBM3NAME
					+ ".MITAUN \n" + "WHERE MUAUTP = '2' \n" + "AND MUAUS9 = '1' \n"
					+ "GROUP BY MUCONO,MUITNO,MUALUN) AS b \n" + "ON b.MUCONO2 = a.TDCONO \n"
					+ "AND b.MUITNO2 = a.TDITNO";
			// System.out.println("getRSPRDetailGenPO\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getList.add(mRes.getString("TDORNO").trim() + " ; " + mRes.getString("TDCONU").trim() + " ; "
						+ mRes.getString("TDIVNO").trim() + " ; " + mRes.getString("TDLINE").trim() + " ; "
						+ mRes.getString("TDITNO").trim() + " ; " + mRes.getString("MMITDS").trim() + " ; "
						+ mRes.getString("TDIQTY").trim() + " ; " + mRes.getString("TDUNIT").trim() + " ; "
						+ mRes.getString("TDREM1").trim() + " ; " + mRes.getString("TDREM2").trim() + " ; "
						+ mRes.getString("TDSTAS").trim() + " ; " + mRes.getString("TDENDA").trim() + " ; "
						+ mRes.getString("TDENTI").trim());
			}
			return getList;

		} catch (Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static List<String> getRSOrderDatail_bac211118(String cono, String divi, String orderno) {

		List<String> getList = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = ConnectDB2.doConnect();
			Statement stmt = conn.createStatement();
			String query = "SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, MMITDS, CAST(TDIQTY AS DECIMAL(15,2)) AS TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI \n"
					+ "FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT a, " + DBM3NAME + ".MITMAS b \n"
					+ "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno
					+ "' \n" + "AND TDSTAS = '10' \n" + "AND b.MMCONO = TDCONO \n" + "AND b.MMITNO = TDITNO";
			// System.out.println("getRSPRDetailGenPO\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getList.add(mRes.getString("TDORNO").trim() + " ; " + mRes.getString("TDCONU").trim() + " ; "
						+ mRes.getString("TDIVNO").trim() + " ; " + mRes.getString("TDLINE").trim() + " ; "
						+ mRes.getString("TDITNO").trim() + " ; " + mRes.getString("MMITDS").trim() + " ; "
						+ mRes.getString("TDIQTY").trim() + " ; " + mRes.getString("TDUNIT").trim() + " ; "
						+ mRes.getString("TDREM1").trim() + " ; " + mRes.getString("TDREM2").trim() + " ; "
						+ mRes.getString("TDSTAS").trim() + " ; " + mRes.getString("TDENDA").trim() + " ; "
						+ mRes.getString("TDENTI").trim());
			}
			return getList;

		} catch (Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static List<String> getListInvNumber(String cono, String divi) {

		List<String> getList = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = ConnectDB2.doConnect();
			Statement stmt = conn.createStatement();
			String query = "SELECT THORNO, COALESCE(TDCONU, '') AS TDCONU, COALESCE(TDIVNO, '') AS TDIVNO, COALESCE(CAST('00' || UAIVNO AS VARCHAR(10)), '') AS UAIVNO \n"
					+ "FROM \n"
					+ "(SELECT THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, TRIM(OKCUNM) AS OKCUNM, THROUN, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI \n"
					+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n" + "WHERE THCONO = '" + cono
					+ "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND a.THCONO = b.OKCONO \n"
					+ "AND a.THCUNO = b.OKCUNO) AS a \n" + "INNER JOIN \n"
					+ "(SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO \n" + "FROM " + DBNAME
					+ ".M3_TAKEORDERDETAILSUPPORT \n" + "WHERE TDCONU != '' \n" + "AND TDIVNO = '' \n"
					+ "AND TDSTAS = '15' \n" + "GROUP BY TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO) AS b \n"
					+ "ON b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n"
					+ "INNER JOIN \n" + "(SELECT UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4 \n" + "FROM " + DBM3NAME
					+ ".ODHEAD \n" + "GROUP BY UACONO, UADIVI, UAORNO, UAIVNO, UAYEA4) AS c \n"
					+ "ON c.UACONO = THCONO \n" + "AND c.UADIVI = THDIVI \n" + "AND c.UAORNO = TDCONU \n"
					+ "AND c.UAYEA4 = YEAR(THDEDA) \n" + "ORDER BY THCONO, THDIVI, THORNO";
			// System.out.println("getListOrderInvoice\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getList.add(mRes.getString("THORNO").trim() + " ; " + mRes.getString("TDCONU").trim() + " ; "
						+ mRes.getString("TDIVNO").trim() + " ; " + mRes.getString("UAIVNO").trim());
			}
			return getList;

		} catch (Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static String getDiscountModel(String cono, String divi, String customer) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER(ORDER BY OKDISY) AS ID, TRIM(OKDISY) AS OKDISY \n" + "FROM "
						+ DBM3NAME + ".OCUSMA \n" + "WHERE OKCONO = '" + cono + "' \n" + "AND OKCUNO = '" + customer
						+ "'";
				// System.out.println("getDiscountModel\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getRound(String cono, String divi) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "SELECT ROW_NUMBER() OVER(ORDER BY CTSTKY) AS ID,CTSTKY,CTTX15,CTPARM,TRIM(CTSTKY) || ' : ' ||TRIM(SUBSTRING(CTPARM,0,LOCATE(' ',CTPARM))) AS ROUND \n"
						+ "FROM " + DBM3NAME + ".CSYTAB \n" + "WHERE CTCONO = '" + cono + "' \n"
						+ "AND CTSTCO = 'MODL' \n"
						+ "AND (CTSTKY BETWEEN '000' AND '102' OR CTSTKY BETWEEN '120' AND '200') \n"
						+ "AND (SUBSTR(CTSTKY,1,1) = '1' OR CTSTKY = '000') \n" + "ORDER BY CTSTKY";
				// System.out.println("getRound\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static List<String> getOrderEmailSalesup(String cono, String divi) throws Exception {

		List<String> getListData = new ArrayList<String>();
		Connection conn = ConnectDB2.doConnect();
		try {
			Statement stmt = conn.createStatement();

			String query = "SELECT ST_N6L3, ST_EMAIL \n" + "FROM \n" + "(SELECT ST_N6L3, ST_EMAIL \n" + "FROM \n"
					+ "(SELECT THORNO, THORTY, THWHLO, THORDA, THDEDA, THCUNO, THROUN, THPRIC, THSANO, THSASU, THREM1 \n"
					+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD \n" + "WHERE THSTAS = '" + cono + "' \n"
					+ "AND THSASU NOT IN ('M3SRVADM') \n" + "ORDER BY THSASU, THORNO DESC) AS a \n" + "LEFT JOIN \n"
					+ "(SELECT RTRIM(ST_N6L3) AS ST_N6L3, RTRIM(ST_EMAIL) AS ST_EMAIL \n"
					+ "FROM " + Constant.DBNAME + ".STAFFLIST) AS b  \n" + "ON b.ST_N6L3 = a.THSASU \n"
					+ "GROUP BY ST_N6L3, ST_EMAIL) AS a \n" + "WHERE ST_N6L3 IS NOT NULL";
			// System.out.println("getEmailPuchase\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {

				getListData.add(mRes.getString("ST_N6L3").trim() + " ; " + mRes.getString("ST_EMAIL").trim());
			}

			return getListData;

		} catch (

		Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static List<String> getListOrdernumber(String cono, String divi) throws Exception {

		List<String> getListData = new ArrayList<String>();
		Connection conn = ConnectDB2.doConnect();
		try {
			Statement stmt = conn.createStatement();

			String query = "SELECT THSASU, THORNO, THDEDA, THCUNO, OKCUNM, CASE WHEN THROUN = '101' THEN 'เช้า' ELSE 'บ่าย' END AS THROUN, THSANO, THREM1 \n"
					+ "FROM " + DBNAME + ".M3_TAKEORDERHEAD a, " + DBM3NAME + ".OCUSMA b \n" + "WHERE THSTAS = '" + cono
					+ "' \n" + "AND THSASU NOT IN ('M3SRVADM') \n" + "AND b.OKCONO = a.THCONO \n"
					+ "AND b.OKCUNO = a.THCUNO \n" + "ORDER BY THSASU, THORNO DESC";
			// System.out.println("getListOrdernumber\n" + query);
			ResultSet mRes = stmt.executeQuery(query);

			while (mRes.next()) {
				getListData.add(mRes.getString("THSASU").trim() + " ; " + mRes.getString("THORNO").trim() + " ; "
						+ mRes.getString("THDEDA").trim() + " ; " + mRes.getString("THCUNO").trim() + " ; "
						+ mRes.getString("OKCUNM").trim() + " ; " + mRes.getString("THROUN").trim() + " ; "
						+ mRes.getString("THSANO").trim() + " ; " + mRes.getString("THREM1").trim());
			}

			return getListData;

		} catch (

		Exception ex) {
			// System.out.println(ex.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					// System.out.println(ex.toString());
				}
			}
		}
		return null;
	}

	public static String getNameTemplate(String id, String cono) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT  H_COMPANYNAME2 as NAME FROM " + Constant.DBNAME + ".VISITOR_HEAD vh \r\n"
						+ "WHERE  H_ID  = '" + id + "' AND H_CONO  = '" + cono + "'";
				System.out.println("getEmailTemplate\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("NAME").trim();

				}

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

	public static String getEmailTemplate(String documentName, String status) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT EDOCUMENT, ESTATUSNO, ESTATUSNAME, COALESCE(ESTATUS,'') AS ESTATUS, COALESCE(ESUBJECT1,'') AS ESUBJECT1, COALESCE(ESUBJECT2,'') AS ESUBJECT2 \n"
						+ ", COALESCE(ESUBJECT3,'') AS ESUBJECT3, COALESCE(ESUBJECT4,'') AS ESUBJECT4, COALESCE(ESUBJECT5,'') AS ESUBJECT5 \n"
						+ ", COALESCE(EDETAIL1,'') AS EDETAIL1, COALESCE(EDETAIL2,'') AS EDETAIL2, COALESCE(EDETAIL3,'') AS EDETAIL3 \n"
						+ ", COALESCE(EDETAIL4,'') AS EDETAIL4, COALESCE(EDETAIL5,'') AS EDETAIL5, EACTIVE \n"
						+ "FROM " + Constant.DBNAME + ".M3_WORKFLOWPROGRAMEMAIL \n" + "WHERE EDOCUMENT = '"
						+ documentName + "' \n"
						+ "AND ESTATUSNO = '" + status + "' \n" + "AND EACTIVE = '20'";
				System.out.println("getEmailTemplate\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				while (mRes.next()) {
					return mRes.getString("EDOCUMENT").trim() + " ; " + mRes.getString("ESTATUSNO").trim() + " ; "
							+ mRes.getString("ESTATUSNAME").trim() + " ; " + mRes.getString("ESTATUS").trim() + " ; "
							+ mRes.getString("ESUBJECT1").trim() + " ; " + mRes.getString("ESUBJECT2").trim() + " ; "
							+ mRes.getString("ESUBJECT3").trim() + " ; " + mRes.getString("ESUBJECT4").trim() + " ; "
							+ mRes.getString("ESUBJECT5").trim() + " ; " + mRes.getString("EDETAIL1").trim() + " ; "
							+ mRes.getString("EDETAIL2").trim() + " ; " + mRes.getString("EDETAIL3").trim() + " ; "
							+ mRes.getString("EDETAIL4").trim() + " ; " + mRes.getString("EDETAIL5").trim() + " ; "
							+ mRes.getString("EACTIVE").trim();
				}

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

	public static String getADRHeadWOToken(String cono, String divi, String adrno, String fromstatus, String tostatus,
			String approve) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ADCONO, ADDIVI, ADRNUMBER, ADORNO, ADDATE, ADMONT, ADTYPE, ADPREF, ADBOI, ADBU, ADCOCE, COSTCENTER, ADVAT, ADACCT, ADREQU, ADREM1, ADREM2, ADAPP1, CHAR(ADAPDA1, ISO) AS ADAPDA1, ADAPP2, CHAR(ADAPDA2, ISO) AS ADAPDA2, ADAPP3, CHAR(ADAPDA3, ISO) AS ADAPDA3, ADAPP4, CHAR(ADAPDA4, ISO) AS ADAPDA4, ADACRE1, ADAPRE1, ADAPRE2, ADAPRE3, ADAPRE4, ADENDA, ADENTI, ADSTAT, ADENUS \n"
						+ ",'" + approve + "' AS HD_APP, CASE WHEN ADAPP1 = '" + approve
						+ "' AND ADAPDA1 IS NOT NULL THEN 1 \n" + "WHEN ADAPP2 = '" + approve
						+ "' AND ADAPDA2 IS NOT NULL THEN 1 \n" + "WHEN ADAPP3 = '" + approve
						+ "' AND ADAPDA3 IS NOT NULL THEN 1 \n" + "WHEN ADAPP4 = '" + approve
						+ "' AND ADAPDA4 IS NOT NULL THEN 1 \n" + "ELSE 0 END AS HD_APPCK, HD_APPSIGN, ADAPP1 \n"
						+ ",CASE WHEN ADAPP1 != '0' THEN HD_APPSIGN1 END AS HD_APPSIGN1, CASE WHEN ADAPDA1 IS NOT NULL THEN CHAR(ADAPDA1, ISO) ELSE NULL END AS HD_APPDT1 \n"
						+ ",CASE WHEN ADAPP2 != '0' THEN HD_APPSIGN2 END AS HD_APPSIGN2, CASE WHEN ADAPDA2 IS NOT NULL THEN CHAR(ADAPDA2, ISO) ELSE NULL END AS HD_APPDT2 \n"
						+ ",CASE WHEN ADAPP3 != '0' THEN HD_APPSIGN3 END AS HD_APPSIGN3, CASE WHEN ADAPDA3 IS NOT NULL THEN CHAR(ADAPDA3, ISO) ELSE NULL END AS HD_APPDT3 \n"
						+ ",CASE WHEN ADAPP4 != '0' THEN HD_APPSIGN4 END AS HD_APPSIGN4, CASE WHEN ADAPDA4 IS NOT NULL THEN CHAR(ADAPDA4, ISO) ELSE NULL END AS HD_APPDT4 \n"
						+ ",COMPANY \n" + "FROM \n"
						+ "(SELECT ADCONO, ADDIVI, ADPREF || '-' || ADORNO AS ADRNUMBER, ADORNO, CHAR(ADDATE, ISO) AS ADDATE, ADMONT, ADTYPE, ADPREF, ADBOI, ADBU, ADCOCE, TRIM(S2AITM) || ' : ' || TRIM(S2TX15) || ' : ' || SUBSTR(EATX40,38,3) AS COSTCENTER, ADVAT, ADACCT, ADREQU, ADREM1, ADREM2 \n"
						+ ", ADAPP1, ADAPDA1, ADAPP2, ADAPDA2, ADAPP3, ADAPDA3, ADAPP4, ADAPDA4, ADACRE1, ADAPRE1, ADAPRE2, ADAPRE3, ADAPRE4, ADENDA, ADENTI, ADSTAT, ADENUS \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRHEAD a, " + DBM3NAME + ".FSTLIN b, " + DBM3NAME
						+ ".FCHACC c \n" + "WHERE ADCONO = '"
						+ cono + "' \n" + "AND ADDIVI = '" + divi + "' \n" + "AND ADPREF || '-' || ADORNO = '" + adrno
						+ "' \n" + "AND ADSTAT BETWEEN '" + fromstatus + "' AND '" + tostatus + "' \n"
						+ "AND b.S2CONO = a.ADCONO \n" + "AND b.S2DIVI = a.ADDIVI \n" + "AND b.S2AITM = a.ADCOCE \n"
						+ "AND c.EACONO = a.ADCONO \n" + "AND c.EADIVI = a.ADDIVI \n" + "AND c.EAAITP = '2' \n"
						+ "AND c.EAAITM = a.ADCOCE \n" + ") AS a \n" + "LEFT JOIN \n"
						+ "(SELECT ST_CONO, ST_N6L3, ST_SIGN AS HD_APPSIGN \n" + "FROM " + Constant.DBNAME
						+ ".STAFFLIST \n"
						+ "WHERE ST_N6L3 = '" + approve + "') AS b \n" + "ON 1 = 1 \n" + "LEFT JOIN \n"
						+ "(SELECT ST_CONO, ST_N6L3, ST_SIGN AS HD_APPSIGN1 \n" + "FROM " + Constant.DBNAME
						+ ".STAFFLIST) AS c \n"
						+ "ON c.ST_N6L3 = a.ADAPP1 \n" + "LEFT JOIN \n"
						+ "(SELECT ST_CONO, ST_N6L3, ST_SIGN AS HD_APPSIGN2 \n" + "FROM " + Constant.DBNAME
						+ ".STAFFLIST) AS d \n"
						+ "ON d.ST_N6L3 = a.ADAPP2 \n" + "LEFT JOIN \n"
						+ "(SELECT ST_CONO, ST_N6L3, ST_SIGN AS HD_APPSIGN3 \n" + "FROM " + Constant.DBNAME
						+ ".STAFFLIST) AS e \n"
						+ "ON e.ST_N6L3 = a.ADAPP3 \n" + "LEFT JOIN \n"
						+ "(SELECT ST_CONO, ST_N6L3, ST_SIGN AS HD_APPSIGN4 \n" + "FROM " + Constant.DBNAME
						+ ".STAFFLIST) AS f \n"
						+ "ON f.ST_N6L3 = a.ADAPP4 \n" + "LEFT JOIN  \n"
						+ "(SELECT CCCONO, CCDIVI, CCROW3 AS CCCONM, TRIM(CCCONO) || ' : ' || TRIM(CCDIVI) || ' : ' || TRIM(CCROW3) AS COMPANY \n"
						+ "FROM " + DBM3NAME + ".CMNDIV \n" + "WHERE CCDIVI != '') AS g \n"
						+ "ON g.CCCONO = a.ADCONO  \n"
						+ "AND g.CCDIVI = a.ADDIVI ";
				// System.out.println("getADRHeadApprove\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

			} else {
				System.err.println("Server can't connect.");
			}

		} catch (SQLException e) {
			throw e;
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

	public static String getADRDetailWOToken(String cono, String divi, String adrno, String status) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "SELECT ALCONO, ALDIVI, ALORNO, ALPREF, ALLINE, ALITNO, TRIM(b.FMTXT1) AS ALITDE, ALSBNO, ALCOST, TRIM(S2TX15) AS ALCODE, CHAR(ALDATE, ISO) AS ALDATE \n"
						+ ", CAST(ALACOS AS DECIMAL(15,2)) AS ALACOS, CAST(ALNETV AS DECIMAL(15,2)) AS ALNETV, CAST(ALQTY AS DECIMAL(15,2)) AS ALQTY, ALPRIC, '' AS ALTOTA, ALREM1, ALREM2, ALIMAG, ALENDA, ALENTI, ALSTAT, ALENUS  \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRDETAIL a," + DBM3NAME + ".FFASMA b, " + DBM3NAME
						+ ".FSTLIN c \n" + "WHERE ALCONO = '"
						+ cono + "' \n" + "AND ALDIVI = '" + divi + "' \n"
						// + "AND ALSTAT = '"+status+"' \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "' \n" + "AND b.FMCONO = a.ALCONO \n"
						+ "AND b.FMDIVI = a.ALDIVI \n" + "AND b.FMASID = a.ALITNO \n" + "AND b.FMSBNO = a.ALSBNO \n"
						+ "AND c.S2CONO = a.ALCONO  \n" + "AND c.S2DIVI = a.ALDIVI  \n" + "AND c.S2AITM = a.ALCOST \n"
						+ "UNION ALL \n"
						+ "SELECT '' AS ALCONO, '' AS ALDIVI, '' AS ALORNO, '' AS ALPREF, 999 AS ALLINE, 'Total' AS ALITNO, '' AS ALITDE, '' AS ALSBNO, '' AS ALCOST, '' AS ALCODE, '' AS ALDATE \n"
						+ ", CAST(ROUND(SUM(ALACOS), 2) AS DECIMAL(15,2)) AS ALACOS \n"
						+ ", CAST(ROUND(SUM(ALNETV), 2) AS DECIMAL(15,2)) AS ALNETV \n"
						+ ", CAST(ROUND(SUM(ALQTY), 2) AS DECIMAL(15,2)) AS ALQTY \n" + ", '' ALPRIC \n"
						+ ", '' AS ALTOTA, '' AS ALREM1, '' AS ALREM2, '' AS ALIMAG, '' AS ALENDA, '' AS ALENTI, '' AS ALSTAT, '' AS ALENUS \n"
						+ "FROM " + Constant.DBNAME + ".M3_ADRDETAIL \n" + "WHERE ALCONO = '" + cono + "' \n"
						+ "AND ALDIVI = '"
						+ divi + "' \n"
						// + "AND ALSTAT = '"+status+"' \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "' \n" + "ORDER BY ALLINE";
				// System.out.println("getADRDetailApprove\n" + query);
				ResultSet mRes = stmt.executeQuery(query);

				return ConvertResultSet.convertResultSetToJson(mRes);

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

	public static String getSWRile(String cono, String divi) throws Exception {
		logger.info("getMARFile");

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "SELECT SFCONO,\r\n"
					+ "SFDIVI,\r\n"
					+ "SFPREF,\r\n"
					+ "SFORNO,\r\n"
					+ "SFLINE,\r\n"
					+ "SFNAME,\r\n"
					+ "SFTYPE,\r\n"
					+ "SFREM1,\r\n"
					+ "SFREM2,\r\n"
					+ "SFENDA,\r\n"
					+ "SFENTI,\r\n"
					+ "SFENUS FROM " + DBNAME + ".M3_SWRFILE";
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

	public static String getSWRilewithID(String cono, String divi, String vOrderno) throws Exception {

		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "";
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

}
