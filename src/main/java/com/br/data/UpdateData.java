package com.br.data;
import java.io.StringReader;

import java.io.InputStream;
import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.br.connection.ConnectDB2;
import com.br.utility.Constant;
import com.br.utility.ConvertString;
import com.br.utility.FileUtillity;
import com.br.utility.HttpConnection;
import com.br.utility.SendEmail;

public class UpdateData {

	private static final Logger logger = LogManager.getLogger(UpdateData.class);

	protected static String DBNAME = Constant.DBNAME;
	protected static String DBM3NAME = Constant.DBM3NAME;
	
	  public static String SR_HEAD =  Constant.SR_HEAD;
	  public static String SR_DETAIL = Constant.SR_DETAIL; 
	  public static String SR_APPROVE = Constant.SR_APPROVE; 
	  public static String SR_FLOW = Constant.SR_FLOW; 
	  public static String SR_GROUP = Constant.SR_GROUP; 
	

	// protected static String DBNAMEPP = ""+Constant.DBNAME+"";
	protected static String DBNAMEPP = "" + DBNAME + "";

	static DecimalFormat df0 = new DecimalFormat("0");
	static DecimalFormat df2 = new DecimalFormat("#.##");
	static DecimalFormat df3 = new DecimalFormat("#.###");
	static DecimalFormat df4 = new DecimalFormat("#.####");

	public static String updateMARHead(String cono, String divi, String marno, String date, String postdate,
			String month, String type, String prefix, String bu, String costcenter, String accountant, String requestor,
			String remark, String purpose, String approve1, String approve2, String approve3, String approve4,
			String approve5, String acknoict, String acknocio, String status, String submit, String username)
			throws Exception {
		logger.info("updateMARHead");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String getYear4 = month.substring(0, 4);
			String getYear2 = month.substring(2, 4);
			remark = ConvertString.convertApostrophe(remark);

			String query = "UPDATE " + Constant.DBNAME + ".M3_MARHEAD \n" + "SET MHTYPE = '" + type + "' \n"
					+ ", MHBU = '" + bu
					+ "' \n" + ", MHCOCE = '" + costcenter + "' \n" + ", MHACCT = '" + accountant + "' \n"
					+ ", MHAPP1 = '" + approve1 + "' \n" + ", MHAPP2 = '" + approve2 + "' \n" + ", MHAPP3 = '"
					+ approve3 + "' \n" + ", MHAPP4 = '" + approve4 + "' \n" + ", MHAPP5 = '" + approve5 + "' \n"
					+ ", MHREM1 = '" + remark + "' \n" + ", MHREM2 = '" + purpose + "' \n"
					// + ", MHACCRE = '" + + "' \n"
					// + ", MHAPRE1 = '" + + "' \n"
					// + ", MHAPRE2 = '" + + "' \n"
					// + ", MHAPRE3 = '" + + "' \n"
					// + ", MHAPRE4 = '" + + "' \n"
					// + ", MHAPRE5 = '" + + "' \n"
					// + ", MHICTRE = '" + + "' \n"
					// + ", MHCIORE = '" + + "' \n"
					+ ", MHENDA = CURRENT DATE \n" + ", MHENTI = CURRENT TIME \n" + ", MHENUS = '" + username + "' \n"
					+ "WHERE MHCONO = '" + cono + "' \n" + "AND MHDIVI = '" + divi + "' \n"
					+ "AND MHPREF || '-' || MHORNO = '" + marno + "'";
			// System.out.println("updateMARHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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
	
//itemreq_api/resendemail/{cono}/{divi}/{serviceno}
	
	public static String resendemail(String cono, String divi, String serviceno)
			throws Exception {
		logger.info("resendemail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;

		String tt = "OK";
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

		
			//vApproval

			

			String data = SelectData.getSTATUSIDITEMRQ(serviceno,cono,divi);

			String url = "https://workflow.br-bangkokranch.com/webhook/saveitemrequest2"; 
			
			/* if (data != null && data.trim().startsWith("{")) {
			    JSONObject json = new JSONObject(data);
			    json.put("vApproval", true);
			    data = json.toString();
			}
			*/
			
			logger.debug("data: " + data);


			String response = HttpConnection.sendRequest(
					"POST",
					url,
					Map.of("x-access-token",
							"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
					data,
					null // form-data
			);
			
			logger.debug("response: " + response);



			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email complete.");
			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "error");
			mJsonObj.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			mJsonObj.put("result", "error");
			mJsonObj.put("message", e.getMessage());
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

		return mJsonObj.toString();
	}
	
	
	public static String prepareUpdateSWRQ(String vID, String vSTATUS, String vData, String vApproval,String vApprover,String vDepthead, String vRemark)
			throws Exception {
		logger.info("UpdateITEMREQUEST");
		
		vRemark = ConvertString.convertApostrophe(vRemark); 

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		
		ResultSet vcrs = null;
		ResultSet vcrs20 = null;
		Statement stmtvcrs = null;
		Boolean  isVacant = false; 
		Boolean  isVacant20 = false; 
		
		
		String tt = "OK";
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			// ตรวจสอบและกำหนดสถานะใหม่
			String newStatus = vSTATUS;
			String Status = vSTATUS;
			String cStatus = "10";
			
			
			JSONObject obj = new JSONObject(vData);

			String company = obj.optString("company");
//			String warehouse2 = obj.optString("warehouse2");
			
			String title = obj.optString("serviceTitle");
			
			String version = obj.optString("vVersion");
			
			logger.debug("vVersion: " + version);
			
			String checkVersion = SelectData.checkVersion("SRQ");
			   if (version == null || version.isEmpty() || !Objects.equals(checkVersion, version)) {
			    mJsonObj.put("result", "nok");
			    mJsonObj.put("message", "Can't create Service number, Please update your version to " + checkVersion + " :  "+version+" (Click F5 button).");
			    return mJsonObj.toString();

			   }

			

			logger.debug("company: " + company);
//			logger.debug("warehouse2: " + warehouse2);
			
			Map<String, String[]> companyMapping = new HashMap<>();
			companyMapping.put("10", new String[] { "10", "101" });
			companyMapping.put("600", new String[] { "600", "600" });
			// เพิ่มได้เรื่อยๆ เช่น
			// companyMapping.put("300", new String[] { "300", "301" });

			// ดึงข้อมูลตาม company
			String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
			String comcono = mapping[0];
			String comdivi = mapping[1];

			logger.debug("cono: " + comcono);
			logger.debug("divi: " + comdivi);
			
			String FADES = "FADES3"; 
			
			
			
			
			// เพิ่มก่อนเริ่ม update ใดๆ
			String checkSQL = "SELECT FAAPBY " +
			                  "FROM "+DBNAME+"."+SR_APPROVE+" " +
			                  "WHERE FACODE = 'SWRQ' " +
			                  "AND FASRNO = '" + vID + "' " +
			                  "AND FASTAT = '" + vSTATUS + "'  AND FACONO = '"+comcono+"'" +
			                  "FETCH FIRST 1 ROWS ONLY";

			logger.debug("checkSQL: " + checkSQL);

			try (ResultSet rs = stmt.executeQuery(checkSQL)) {
			    if (rs.next()) {
			        String faapby = rs.getString("FAAPBY");
			        if (faapby != null && !faapby.trim().isEmpty()) {
			            logger.info("FAAPBY already has value: " + faapby);
			            mJsonObj.put("result", "nok");
			            mJsonObj.put("message", "Approver already set. No update performed.");
			            return mJsonObj.toString(); // ออกจาก method ทันที
			        }
			    }
			}
			
			

			if ("false".equalsIgnoreCase(vApproval)) {
				newStatus = "00";
				
				FADES = "FADES4"; 
				  rejectSWRQ(vID,  vSTATUS,  vData,  vApproval, vApprover, vDepthead,  vRemark,Status,comcono,comdivi);
			} else {switch (vSTATUS) {
			case "00":
				newStatus = "10";
				
				String querysetapprove = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
						+ "SET  FAAPLI = '"+vDepthead+"' \n"
						+ "WHERE FACODE = 'SWRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('10') AND FACONO = '"+comcono+"' ";
				stmt.executeUpdate(querysetapprove);
				break;
				
			case "10": 
				newStatus = "20";
				break;
			case "20":
				newStatus = "30";
				break;
			case "30":
				newStatus = "40";
				break;
			case "40":
				newStatus = "50";
				break;
			case "50":
				
				newStatus = "60";
			case "60":
				newStatus = "60";
				String completeSQL = "UPDATE "+DBNAME+"."+SR_HEAD+" "
						+ "SET FHHSTA = 3 WHERE FHCODE = 'SWRQ' AND FHSRNO = '" + vID + "'   AND FHCONO = '"+comcono+"' AND FHDIVI = '"+comdivi+"' ";
				stmt.executeUpdate(completeSQL);
				break;
		
			
			default:
				break;
				
		}
			}

			// สร้าง timestamp และวันที่
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
			String dateYYYYMMDD = new java.text.SimpleDateFormat("yyyyMMdd").format(currentTimestamp);

			// บวก STATUS เป็นเลข
			int nextStatusInt = 0;
			try {
				nextStatusInt = Integer.parseInt(vSTATUS) + 10;
			} catch (NumberFormatException e) {
				logger.warn("STATUS is not numeric, using default nextStatusInt = 0");
			}

			// เตรียม SQL
		/*	String query1 = "UPDATE "+DBNAME+"."+SR_DETAIL+" \n"
					+ "SET FDDATA = '" + vData + "', FDENDA = CURRENT DATE, FDENTI  = CURRENT TIME ,FDDSTA = '" + newStatus + "' \n"
					+ "WHERE FDSRNO = '" + vID + "'   AND FDCONO = '"+comcono+"' AND FDDIVI = '"+comdivi+"' ";
			*/
			
			

		
		/////

			
			if ("false".equalsIgnoreCase(vApproval)) {
			    cStatus = "01";
			} else {
			    if ("00".equals(vSTATUS)) {
			        cStatus = "00";
			    } else if ("60".equals(vSTATUS)) {
			        cStatus = "60";
			    }
			    else {
			    		 cStatus = "99";
			    }
			}

			
			
			String query222 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
	                + "SET FAENUS = '"+vApprover+"' ,FAAPBY = '" + vDepthead + "', FAAPDA = CURRENT DATE , FADES1 = 'Approved' \n"
	                + "WHERE FACODE = 'SWRQ' AND FASRNO = '" + vID + "' AND FASTAT  = '"+cStatus+"' AND FACONO = '"+comcono+"'";



			String query2 = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
					+ "SET   FAAPTI = CURRENT TIME ,  FAENUS = '"+vApprover+"' ,FADES1 = 'Approved',  FAAPDA = CURRENT DATE,FAENTI =  CURRENT TIME,FAAPBY = '"+vApprover+"' , "+FADES+" = '"+vRemark+"' , FAENDA = CURRENT DATE" +
					" WHERE FACODE = 'SWRQ' AND FASRNO = '" + vID + "' AND FASTAT = '" + Status + "' AND FACONO = '"+comcono+"' ";

			String query3 = "UPDATE "+DBNAME+"."+SR_HEAD+" "
					+ "SET   FHDSTA = '" + newStatus +
					"' , FHENDA = CURRENT DATE ,FHENTI = CURRENT TIME WHERE FHCODE = 'SWRQ' AND FHSRNO = '" + vID + "'  AND FHCONO = '"+comcono+"' AND FHDIVI = '"+comdivi+"'  ";

			
			String query = "UPDATE " + DBNAME + "." + SR_DETAIL + " " +
		               "SET FDDATA = ?, FDENDA = CURRENT DATE, FDENTI = CURRENT TIME, FDDSTA = ? " +
		               "WHERE FDSRNO = ? AND FDCONO = ? AND FDDIVI = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {

		    // ใช้ StringReader สำหรับ JSON ยาว ๆ
		    ps.setCharacterStream(1, new StringReader(vData), vData.length());
		    ps.setString(2, newStatus);
		    ps.setString(3, vID);
		    ps.setString(4, comcono);
		    ps.setString(5, comdivi);

		    int updated = ps.executeUpdate();
		    System.out.println("Rows updated: " + updated);
		}

			
			
			//tt = query1 + " ; " + query2; // Debug
			//logger.debug(query1);
			logger.debug(query222);
			logger.debug(query2);
			logger.debug(query3);

			//stmt.executeUpdate(query1);
			logger.debug("xxxxxxxxxxxxxxxxxx");
			//logger.debug(query1);
			//stmt.executeUpdate(query1);
			//ps.close();
			logger.debug("xxxxxxxxxxxxxxxxxx");
			
			
			
			stmt.executeUpdate(query222);
			
			
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3);
			
			if ("false".equalsIgnoreCase(vApproval)) {
				newStatus = "00";
				
				rejectreverseSWRQ( vID,  vSTATUS,  vData,  vApproval, vApprover, vDepthead,  vRemark, Status,FADES, comcono, comdivi);

				
				
			}
			
			String data = SelectData.getSTATUSIDSWRQ(vID.toString(),comcono,comdivi);
			String url = "https://workflow.br-bangkokranch.com/webhook/savesoftware_req"; 
			

			String response = HttpConnection.sendRequest(
					"POST",
					url,
					Map.of("x-access-token",
							"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
					data,
					null // form-data
			);
			
			logger.debug("response: " + response);

			
			
			
			
			

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "error");
			mJsonObj.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			mJsonObj.put("result", "error");
			mJsonObj.put("message", e.getMessage());
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

		return mJsonObj.toString();
	}
	
	public static void rejectreverseSWRQ(String vID, String vSTATUS, String vData, String vApproval,String vApprover,String vDepthead, String vRemark,String Status,String FADES,String comcono,String comdivi) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		
		ResultSet vcrs = null;
		ResultSet vcrs20 = null;
		Statement stmtvcrs = null;
		Boolean  isVacant = false; 
		Boolean  isVacant20 = false; 
		


		String query2e = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
				+ "SET  FADES1 = 'Wait for approve', FAAPTI = null,FAAPBY = ' ', FAAPDA = NULL  ,FAENDA  = CURRENT DATE , FAENTI  = CURRENT TIME \n"
				+ "WHERE FACODE = 'SWRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('10','15','20','30','40','50','60','70','80') AND FACONO = '"+comcono+"'";

		String query3e = "UPDATE "+DBNAME+"."+SR_HEAD+" "
				+ "SET FHDEPH = '-' WHERE FHCODE = 'SWRQ' AND FHSRNO = '" + vID + "'  AND FHCONO = '"+comcono+"' AND FHDIVI = '"+comdivi+"'  ";
		
		
		String query4re = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
				+ "SET  "+FADES+" = '"+vRemark+"' \n"
				+ "WHERE FACODE = 'SWRQ' AND FASRNO = '" + vID + "'  AND  FASTAT = '"+Status+"' AND FACONO = '"+comcono+"' ";

		logger.debug(query2e);
		logger.debug(query3e);
		logger.debug(query4re);

		
		stmt.executeUpdate(query2e);
		stmt.executeUpdate(query3e);
		stmt.executeUpdate(query4re);

		

		
	}
	
	public static void rejectSWRQ(String vID, String vSTATUS, String vData, String vApproval,String vApprover,String vDepthead, String vRemark,String Status,String comcono,String comdivi) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		
		ResultSet vcrs = null;
		ResultSet vcrs20 = null;
		Statement stmtvcrs = null;
		
		/*
		String query1e = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
				+ "SET  FAAPLI = '' \n"
				+ "WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('10') ";
				*/


		String query2e = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
				+ "SET  FADES1 = 'Wait for approve', FAAPTI = null,FAAPBY = ' ', FAAPDA = NULL  ,FAENDA  = CURRENT DATE , FAENTI  = CURRENT TIME ,FADES3 = '' \n"
				+ "WHERE FACODE = 'SWRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('00','10','20','30','40','50','60','70','80') AND  FACONO = '"+comcono+"' AND FADIVI = '"+comdivi+"' ";

		String query3e = "UPDATE "+DBNAME+"."+SR_HEAD+" "
				+ "SET FHDEPH = '-' WHERE FHCODE = 'SWRQ' AND FHSRNO = '" + vID + "'  AND FHCONO = '"+comcono+"'  ";
	 
		
		String query4e ="UPDATE "+DBNAME+"."+SR_APPROVE+" \r\n"
				+ "	SET  FARJDA  = CURRENT DATE , FARJTI  = CURRENT TIME , FARJBY = '"+vApprover+"' \r\n"
				+ "	WHERE FACODE = 'SWRQ' AND FASRNO =  '"+vID+"'   AND  FASTAT = '"+Status+"' AND FACONO = '"+comcono+"'  ";

		//logger.debug(query1e);
		logger.debug(query2e);
		logger.debug(query3e);
		logger.debug(query4e);

		//stmt.executeUpdate(query1e);
		stmt.executeUpdate(query2e);
		stmt.executeUpdate(query3e);
		stmt.executeUpdate(query4e);

		
	}
	
	

	
	

	public static String updateITEMREQUEST(String vID, String vSTATUS, String vData, String vApproval,String vApprover,String vDepthead, String vRemark)
			throws Exception {
		logger.info("UpdateITEMREQUEST");
		
		vRemark = ConvertString.convertApostrophe(vRemark); 

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		
		ResultSet vcrs = null;
		ResultSet vcrs20 = null;
		Statement stmtvcrs = null;
		Boolean  isVacant = false; 
		Boolean  isVacant20 = false; 
		
		
		
		
		

		String tt = "OK";
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			// ตรวจสอบและกำหนดสถานะใหม่
			String newStatus = vSTATUS;
			String Status = vSTATUS;
			String cStatus = "10";
			
			
			JSONObject obj = new JSONObject(vData);

			String company = obj.optString("company");
			String warehouse2 = obj.optString("warehouse2");
			
			String title = obj.optString("serviceTitle");
			
			String version = obj.optString("vVersion");
			
			logger.debug("vVersion: " + version);
			
			String checkVersion = SelectData.checkVersion("SRQ");
			   if (version == null || version.isEmpty() || !Objects.equals(checkVersion, version)) {
			    mJsonObj.put("result", "nok");
			    mJsonObj.put("message", "Can't create Service number, Please update your version to " + checkVersion + " :  "+version+" (Click F5 button).");
			    return mJsonObj.toString();

			   }

			

			logger.debug("company: " + company);
			logger.debug("warehouse2: " + warehouse2);
			
			Map<String, String[]> companyMapping = new HashMap<>();
			companyMapping.put("10", new String[] { "10", "101" });
			companyMapping.put("600", new String[] { "600", "600" });
			// เพิ่มได้เรื่อยๆ เช่น
			// companyMapping.put("300", new String[] { "300", "301" });

			// ดึงข้อมูลตาม company
			String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
			String comcono = mapping[0];
			String comdivi = mapping[1];

			logger.debug("cono: " + comcono);
			logger.debug("divi: " + comdivi);
			
			String FADES = "FADES3"; 
			
			
			
			
			// เพิ่มก่อนเริ่ม update ใดๆ
			String checkSQL = "SELECT FAAPBY " +
			                  "FROM "+DBNAME+"."+SR_APPROVE+" " +
			                  "WHERE FACODE = 'ITRQ' " +
			                  "AND FASRNO = '" + vID + "' " +
			                  "AND FASTAT = '" + vSTATUS + "'  AND FACONO = '"+comcono+"'" +
			                  "FETCH FIRST 1 ROWS ONLY";

			logger.debug("checkSQL: " + checkSQL);

			try (ResultSet rs = stmt.executeQuery(checkSQL)) {
			    if (rs.next()) {
			        String faapby = rs.getString("FAAPBY");
			        if (faapby != null && !faapby.trim().isEmpty()) {
			            logger.info("FAAPBY already has value: " + faapby);
			            mJsonObj.put("result", "nok");
			            mJsonObj.put("message", "Approver already set. No update performed.");
			            return mJsonObj.toString(); // ออกจาก method ทันที
			        }
			    }
			}
			
			

			if ("false".equalsIgnoreCase(vApproval)) {
				newStatus = "00";
				
				FADES = "FADES4"; 
				
			
				/*
				String query1e = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
						+ "SET  FAAPLI = '' \n"
						+ "WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('10') ";
						*/


				String query2e = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
						+ "SET  FADES1 = 'Wait for approve', FAAPTI = null,FAAPBY = ' ', FAAPDA = NULL  ,FAENDA  = CURRENT DATE , FAENTI  = CURRENT TIME ,FADES3 = '' \n"
						+ "WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('00','10','20','30','40','50','60','70','80') AND  FACONO = '"+comcono+"' AND FADIVI = '"+comdivi+"' ";

				String query3e = "UPDATE "+DBNAME+"."+SR_HEAD+" "
						+ "SET FHDEPH = '-' WHERE FHCODE = 'ITRQ' AND FHSRNO = '" + vID + "'  AND FHCONO = '"+comcono+"'  ";
			 
				
				String query4e ="UPDATE "+DBNAME+"."+SR_APPROVE+" \r\n"
						+ "	SET  FARJDA  = CURRENT DATE , FARJTI  = CURRENT TIME , FARJBY = '"+vApprover+"' \r\n"
						+ "	WHERE FACODE = 'ITRQ' AND FASRNO =  '"+vID+"'   AND  FASTAT = '"+Status+"' AND FACONO = '"+comcono+"'  ";

				//logger.debug(query1e);
				logger.debug(query2e);
				logger.debug(query3e);
				logger.debug(query4e);

				//stmt.executeUpdate(query1e);
				stmt.executeUpdate(query2e);
				stmt.executeUpdate(query3e);
				stmt.executeUpdate(query4e);

				
				
				
			} else {
				switch (vSTATUS) {
					case "00":
						newStatus = "10";
						
						String querysetapprove = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
								+ "SET  FAAPLI = '"+vDepthead+"' \n"
								+ "WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('10') AND FACONO = '"+comcono+"' ";
						stmt.executeUpdate(querysetapprove);
						break;
						
					case "10": 
												
						String querycheckapprovestatus = 
							    "SELECT CASE " +
							    "        WHEN COUNT(*) > 0 THEN 'TRUE' " +
							    "        ELSE 'FALSE' " +
							    "    END AS result " +
							    "FROM "+DBNAME+"."+SR_APPROVE+" sf " +
							    "WHERE FACONO = '"+comcono+"' " +
							    "  AND FACODE = 'ITRQ' " +
							    "  AND FASRNO = '"+vID+"' " +
							    "  AND FASTAT = '15'";

							ResultSet rs = stmt.executeQuery(querycheckapprovestatus);

							if (rs.next()) {
							    String result = rs.getString("RESULT");
							    if ("TRUE".equalsIgnoreCase(result)) {
							        newStatus = "15";
							    } else {
							        newStatus = "20";
							    }
							}
							
							
							
							String querysetisVacant = "SELECT \r\n"
									+ "  CASE \r\n"
									+ "    WHEN COUNT(*) > 0 THEN 'TRUE'\r\n"
									+ "    ELSE 'FALSE'\r\n"
									+ "  END AS result\r\n"
									+ "  FROM "+DBNAME+".SR_FLOWAPPROVE sf\r\n"
									+ "	 WHERE FASRNO = '"+vID+"'\r\n"
									+ "  AND FASTAT = '"+newStatus+"'\r\n"
									+ "  AND TRIM(FAAPLI) = 'VACANT'\r\n"
									+ "  AND FACONO  = '"+comcono+"'";

			
							logger.debug("ID Query: " + querysetisVacant);

							vcrs = stmt.executeQuery(querysetisVacant);
							if (vcrs.next()) {
								isVacant = vcrs.getBoolean("RESULT");
							}
							
							
							

							String querysetisVacant20 = "SELECT \r\n"
									+ "  CASE \r\n"
									+ "    WHEN COUNT(*) > 0 THEN 'TRUE'\r\n"
									+ "    ELSE 'FALSE'\r\n"
									+ "  END AS result\r\n"
									+ "  FROM "+DBNAME+".SR_FLOWAPPROVE sf\r\n"
									+ "	 WHERE FASRNO = '"+vID+"'\r\n"
									+ "  AND FASTAT = '20'\r\n"
									+ "  AND TRIM(FAAPLI) = 'VACANT'\r\n"
									+ "  AND FACONO  = '"+comcono+"'";

			
							logger.debug("ID Query: " + querysetisVacant20);

							vcrs20 = stmt.executeQuery(querysetisVacant20);
							if (vcrs20.next()) {
								isVacant20 = vcrs20.getBoolean("RESULT");
							}
							
							
						/*	
							if(isVacant && newStatus  == "15" && isVacant20 ) {
								newStatus = "30";
								
							}
							else if 
							(isVacant && newStatus == "20" && isVacant20) {
								newStatus = "30";
							}
							
							else if 
							(isVacant && newStatus == "15" && !isVacant) {
								newStatus = "15";
							}
							else 
							{
								newStatus = "20";
							}	
							
							*/ 
							
							if (isVacant && newStatus.equals("15") && isVacant20) {
							    newStatus = "30";
							}
							else if (isVacant && newStatus.equals("20") && isVacant20) {
							    newStatus = "30";
							}
							else if (newStatus.equals("15") && !isVacant20 && !isVacant) {
							    newStatus = "15";   // คงค่าเดิมไว้
							}
							else if (newStatus.equals("15") && !isVacant20 && isVacant) {
							    newStatus = "20";
							}
							else {
							    newStatus = "20";
							}

							

							System.out.println("newStatus = " + newStatus);

						
						break; 
						
					case "15":
						
						newStatus = "20";
						
						String querysetisVacant2 = "SELECT \r\n"
								+ "  CASE \r\n"
								+ "    WHEN COUNT(*) > 0 THEN 'TRUE'\r\n"
								+ "    ELSE 'FALSE'\r\n"
								+ "  END AS result\r\n"
								+ "  FROM "+DBNAME+".SR_FLOWAPPROVE sf\r\n"
								+ "	 WHERE FASRNO = '"+vID+"'\r\n"
								+ "  AND FASTAT = '"+newStatus+"'\r\n"
								+ "  AND TRIM(FAAPLI) = 'VACANT'\r\n"
								+ "  AND FACONO  = '"+comcono+"'";

		
						logger.debug("ID Query: " + querysetisVacant2);

						vcrs = stmt.executeQuery(querysetisVacant2);
						if (vcrs.next()) {
							isVacant = vcrs.getBoolean("RESULT");
						}
						
						
						
						if(isVacant) {
							newStatus = "30";
							
						}
						
						
						
						
						break;
					case "20":
						newStatus = "30";
						break;
					case "30":
						newStatus = "40";
						break;
					case "40":
						newStatus = "50";
						break;
					case "50":
						newStatus = "60";
						break;
					case "60":
						newStatus = "70";
						break;
					case "70":
						newStatus = "70";
						String completeSQL = "UPDATE "+DBNAME+"."+SR_HEAD+" "
								+ "SET FHHSTA = 3 WHERE FHCODE = 'ITRQ' AND FHSRNO = '" + vID + "'   AND FHCONO = '"+comcono+"' AND FHDIVI = '"+comdivi+"' ";
						stmt.executeUpdate(completeSQL);
						break;
					case "80":
						newStatus = "22";
						String completeSQL2 = "UPDATE "+DBNAME+"."+SR_HEAD+" "
								+ "SET FHHSTA = 3 WHERE FHCODE = 'ITRQ' AND FHSRNO = '" + vID + "'  AND FHCONO = '"+comcono+"' AND FHDIVI = '"+comdivi+"'  ";
						stmt.executeUpdate(completeSQL2);

					
						break;
					default:
						break;
				}
			}

			// สร้าง timestamp และวันที่
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
			String dateYYYYMMDD = new java.text.SimpleDateFormat("yyyyMMdd").format(currentTimestamp);

			// บวก STATUS เป็นเลข
			int nextStatusInt = 0;
			try {
				nextStatusInt = Integer.parseInt(vSTATUS) + 10;
			} catch (NumberFormatException e) {
				logger.warn("STATUS is not numeric, using default nextStatusInt = 0");
			}

			// เตรียม SQL
		/*	String query1 = "UPDATE "+DBNAME+"."+SR_DETAIL+" \n"
					+ "SET FDDATA = '" + vData + "', FDENDA = CURRENT DATE, FDENTI  = CURRENT TIME ,FDDSTA = '" + newStatus + "' \n"
					+ "WHERE FDSRNO = '" + vID + "'   AND FDCONO = '"+comcono+"' AND FDDIVI = '"+comdivi+"' ";
			*/
			
			

		
		/////

			
			if ("false".equalsIgnoreCase(vApproval)) {
			    cStatus = "01";
			} else {
			    if ("00".equals(vSTATUS)) {
			        cStatus = "00";
			    } else if ("70".equals(vSTATUS)) {
			        cStatus = "70";
			    }
			    else {
			    		 cStatus = "99";
			    }
			}

			
			
			String query222 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
	                + "SET FAENUS = '"+vApprover+"' ,FAAPBY = '" + vDepthead + "', FAAPDA = CURRENT DATE , FADES1 = 'Approved' \n"
	                + "WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "' AND FASTAT  = '"+cStatus+"' AND FACONO = '"+comcono+"'";



			String query2 = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
					+ "SET   FAAPTI = CURRENT TIME ,  FAENUS = '"+vApprover+"' ,FADES1 = 'Approved',  FAAPDA = CURRENT DATE,FAENTI =  CURRENT TIME,FAAPBY = '"+vApprover+"' , "+FADES+" = '"+vRemark+"' , FAENDA = CURRENT DATE" +
					" WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "' AND FASTAT = '" + Status + "' AND FACONO = '"+comcono+"' ";

			String query3 = "UPDATE "+DBNAME+"."+SR_HEAD+" "
					+ "SET   FHDSTA = '" + newStatus +
					"' , FHENDA = CURRENT DATE ,FHENTI = CURRENT TIME WHERE FHCODE = 'ITRQ' AND FHSRNO = '" + vID + "'  AND FHCONO = '"+comcono+"' AND FHDIVI = '"+comdivi+"'  ";

			
			String query = "UPDATE " + DBNAME + "." + SR_DETAIL + " " +
		               "SET FDDATA = ?, FDENDA = CURRENT DATE, FDENTI = CURRENT TIME, FDDSTA = ? " +
		               "WHERE FDSRNO = ? AND FDCONO = ? AND FDDIVI = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {

		    // ใช้ StringReader สำหรับ JSON ยาว ๆ
		    ps.setCharacterStream(1, new StringReader(vData), vData.length());
		    ps.setString(2, newStatus);
		    ps.setString(3, vID);
		    ps.setString(4, comcono);
		    ps.setString(5, comdivi);

		    int updated = ps.executeUpdate();
		    System.out.println("Rows updated: " + updated);
		}

			
			
			//tt = query1 + " ; " + query2; // Debug
			//logger.debug(query1);
			logger.debug(query222);
			logger.debug(query2);
			logger.debug(query3);

			//stmt.executeUpdate(query1);
			logger.debug("xxxxxxxxxxxxxxxxxx");
			//logger.debug(query1);
			//stmt.executeUpdate(query1);
			//ps.close();
			logger.debug("xxxxxxxxxxxxxxxxxx");
			
			
			
			stmt.executeUpdate(query222);
			
			
			stmt.executeUpdate(query2);
			stmt.executeUpdate(query3);
			
			if ("false".equalsIgnoreCase(vApproval)) {
				newStatus = "00";
				
				


				String query2e = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
						+ "SET  FADES1 = 'Wait for approve', FAAPTI = null,FAAPBY = ' ', FAAPDA = NULL  ,FAENDA  = CURRENT DATE , FAENTI  = CURRENT TIME \n"
						+ "WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "'  AND  FASTAT IN ('10','15','20','30','40','50','60','70','80') AND FACONO = '"+comcono+"'";

				String query3e = "UPDATE "+DBNAME+"."+SR_HEAD+" "
						+ "SET FHDEPH = '-' WHERE FHCODE = 'ITRQ' AND FHSRNO = '" + vID + "'  AND FHCONO = '"+comcono+"' AND FHDIVI = '"+comdivi+"'  ";
				
				
				String query4re = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
						+ "SET  "+FADES+" = '"+vRemark+"' \n"
						+ "WHERE FACODE = 'ITRQ' AND FASRNO = '" + vID + "'  AND  FASTAT = '"+Status+"' AND FACONO = '"+comcono+"' ";

				logger.debug(query2e);
				logger.debug(query3e);
				logger.debug(query4re);

				
				stmt.executeUpdate(query2e);
				stmt.executeUpdate(query3e);
				stmt.executeUpdate(query4re);

				
				
				
			}
			
			

			String data = SelectData.getSTATUSIDITEMRQ(vID.toString(),comcono,comdivi);
			String url = "https://workflow.br-bangkokranch.com/webhook/saveitemrequest2"; 

			String response = HttpConnection.sendRequest(
					"POST",
					url,
					Map.of("x-access-token",
							"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
					data,
					null // form-data
			);
			
			logger.debug("response: " + response);

			
			
			
			
			

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "error");
			mJsonObj.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			mJsonObj.put("result", "error");
			mJsonObj.put("message", e.getMessage());
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

		return mJsonObj.toString();
	}

	/*
	 * 
	 * public static String updateITEMREQUEST(String vID, String vSTATUS, String
	 * vData, String vApproval) throws Exception {
	 * logger.info("UpdateITEMREQUEST");
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * Connection conn = null;
	 * Statement stmt = null;
	 * 
	 * 
	 * String tt = "OK";
	 * try {
	 * 
	 * conn = ConnectDB2.doConnect();
	 * stmt = conn.createStatement();
	 * 
	 * // ตรวจสอบและกำหนดสถานะใหม่
	 * String newStatus = vSTATUS;
	 * 
	 * if ("false".equalsIgnoreCase(vApproval)) {
	 * newStatus = "00";
	 * } else {
	 * switch (vSTATUS) {
	 * case "00":
	 * newStatus = "10";
	 * break;
	 * case "10":
	 * newStatus = "20";
	 * break;
	 * case "20":
	 * newStatus = "30";
	 * break;
	 * case "30":
	 * newStatus = "40";
	 * break;
	 * case "40":
	 * newStatus = "50";
	 * break;
	 * case "50":
	 * newStatus = "60";
	 * break;
	 * case "60":
	 * newStatus = "22";
	 * break;
	 * default:
	 * // ถ้าไม่ตรงเงื่อนไข ให้ใช้ vSTATUS เดิม
	 * break;
	 * }
	 * }
	 * 
	 * // เตรียม SQL update
	 * String query =
	 * "UPDATE BRLDTABK01.sjson_test SET JSON_DATA = '"+vData+"', STATUS = '"
	 * +newStatus+"' WHERE SERVICE_ID = '"+vID+"'";
	 * 
	 * String query2 =
	 * "UPDATE BRLDTABK01.Approve_Detail SET JSON_DATA = '"+vData+"', STATUS = '"
	 * +STATUS+10
	 * +"',STS_DESC = 'Approved', TIME_ST = 'timestamp',APPROVE_DATE = 'dateYYYYMMDD' WHERE DOC_CODE = '"
	 * +DOC_CODE+"' AND DOC_NO = '"+DOC_NO+"' ";
	 * 
	 * 
	 * tt= query;
	 * 
	 * logger.debug(query);
	 * logger.debug(query2);
	 * stmt.execute(query);
	 * stmt.execute(query2);
	 * 
	 * mJsonObj.put("result", "ok");
	 * mJsonObj.put("message", "Update complete.");
	 * 
	 * return mJsonObj.toString();
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
	 * return tt;
	 * 
	 * }
	 * 
	 */

	public static String UpdateRQ(String vID, String vCOM, String vWH, String vITM, String vAPV) throws Exception {
		logger.info("UpdateAP");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			System.out.print("xxxxxxxxxxxxxxxxxxx" + "UpdateRQ");

			String query = "UPDATE  BRLDTABK01.VISITOR_HEAD   \n"
					+ "SET   H_COMPANYNAME = '" + vCOM + "', H_LICENSE = '" + vWH
					+ "', H_NAME = '" + vITM + "', H_REASON= '" + vAPV
					+ "', H_TIMEIN = '2024-08-29 02:43:11', H_TEL = 'AP' \n"
					+ "WHERE H_ID = '" + vID + "'";
			System.out.println("UpdateAP\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String UpdateAP(String vID, String vAPVD, String vCOMAPVD) throws Exception {
		logger.info("UpdateAP");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;

		String CHKSTATUS = vAPVD;
		String STATUS = "IT";

		if ("true".equalsIgnoreCase(CHKSTATUS.trim())) {
			STATUS = "IT";
		} else {
			STATUS = "RQ";
		}

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			System.out.print("xxxxxxxxxxxxxxxxxxx" + "UpdateAP");

			String query = "UPDATE  BRLDTABK01.VISITOR_HEAD   \n"
					+ "SET H_TIMEOUT = '" + vAPVD + "' , H_EMPLOYEE = '"
					+ vCOMAPVD + "', H_TEL='" + STATUS + "' \n"
					+ "WHERE H_ID = '" + vID + "'";
			System.out.println("UpdateAP\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	/*
	 * public static String UpdateIT(String vID,String vITAPVD,String vCOMITAPVD)
	 * throws Exception {
	 * logger.info("UpdateIT");
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * Connection conn = null;
	 * Statement stmt = null;
	 * 
	 * String CHKSTATUS = vITAPVD ;
	 * String STATUS = "RQ" ;
	 * 
	 * if ("true".equalsIgnoreCase(CHKSTATUS.trim())) {
	 * STATUS = "CP";
	 * } else {
	 * STATUS = "RQ";
	 * }
	 * 
	 * 
	 * 
	 * try {
	 * conn = ConnectDB2.doConnect();
	 * stmt = conn.createStatement();
	 * 
	 * System.out.print("xxxxxxxxxxxxxxxxxxx" + "UpdateAP");
	 * 
	 * 
	 * 
	 * String query = "UPDATE  BRLDTABK01.VISITOR_HEAD   SET H_SURNAME = '"
	 * +vITAPVD+"' , H_ROOMNO = '"+vCOMITAPVD+"', H_TEL='"+STATUS+"' WHERE H_ID = '"
	 * +vID+"'";
	 * System.out.println("UpdateAP\n" + query);
	 * logger.debug(query);
	 * stmt.execute(query);
	 * 
	 * mJsonObj.put("result", "ok");
	 * mJsonObj.put("message", "Update complete.");
	 * 
	 * return mJsonObj.toString();
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
	 * }
	 * 
	 */

	public static String UpdateIT(String vID, String vITAPVD, String vCOMITAPVD) throws Exception {
		logger.info("UpdateIT");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;

		String CHKSTATUS = vITAPVD;
		String STATUS = "RQ";

		if ("true".equalsIgnoreCase(CHKSTATUS.trim())) {
			STATUS = "CP";
		}

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			System.out.println("xxxxxxxxxxxxxxxxxxx UpdateIT");

			String query = "UPDATE BRLDTABK01.VISITOR_HEAD \n"
					+ "SET " +
					"H_SURNAME = '" + vITAPVD.replace("'", "''") + "', " +
					"H_ROOMNO = '" + vCOMITAPVD.replace("'", "''") + "', " +
					"H_TEL = '" + STATUS + "' " +
					"WHERE H_ID = '" + vID.replace("'", "''") + "'";

			System.out.println("UpdateIT\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error(e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
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

	public static String updateemp(String vID, String vEmployeedialog, String vEmail, String getLocation,
			String vRemark, String vStatus, String vCheckin, String vCheckintime, String vCheckout,
			String vCheckouttime) throws Exception {
		logger.info("updateSWRfile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		String checksts = "00";
		String checksts2 = "00";

		checksts = vStatus;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			System.out.print("xxxxxxxxxxxxxxxxxxx" + checksts);

			if (checksts.equalsIgnoreCase("10")) {

				System.out.print("xxxxxxxxxxxxxxxxxxx" + checksts);

				checksts2 = "15";

				System.out.print("xxxxxxxxxxxxxxxxxxx" + checksts2);

			} else {
				checksts2 = vStatus;
			}

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_COMPANYNAME2 = '"
					+ vEmployeedialog.trim()
					+ "' ,H_STATUS ='" + checksts2 + "' , H_Email = '" + vEmail.trim() + "', H_REMARK1 = '"
					+ vRemark.trim() + "' , H_CHECKIN ='" + vCheckin + "' , H_CHECKOUT = '" + vCheckout
					+ "',H_CHECKINTIME = '" + vCheckintime + "',H_CHECKOUTTIME = '" + vCheckouttime + "' \r\n"
					+ "　WHERE H_ID  =  '" + vID.trim() + "' AND H_LOCATION = '" + getLocation.trim() + "'";
			System.out.println("updateEmployee\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String updateEmail(String ordno) throws Exception {
		logger.info("updateEmail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_STATUS = '20' \r\n"
					+ "　WHERE H_ID  =  '"
					+ ordno.trim() + "'";
			System.out.println("updateEmail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String updateEmailreject(String ordno) throws Exception {
		logger.info("updateEmail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_STATUS = '80' \r\n"
					+ "　WHERE H_ID  =  '"
					+ ordno.trim() + "'";
			System.out.println("updateEmail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String updateroomcard(String vID, String vRoom, String vCard, String getLocation) throws Exception {
		logger.info("updateSWRfile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_ROOMNO = '" + vRoom.trim()
					+ "' , H_CARDNO = '" + vCard.trim() + "' \r\n" + "　WHERE H_ID  =  '" + vID.trim()
					+ "' AND H_LOCATION = '" + getLocation.trim() + "'";

			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String updatefollower(String vID, String H_SURNAME, String oldH_SURNAME, String vCONO, String vDIVI,
			String vLocaton) throws Exception {
		logger.info("updateSWRfile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　UPDATE  " + Constant.DBNAME + ".FOLLOWER_HEAD\r\n" + "　SET H_SURNAME = '" + H_SURNAME
					+ "' \r\n"
					+ "　WHERE F_ID  =  '" + vID.trim() + "' AND H_SURNAME = '" + oldH_SURNAME.trim()
					+ "' AND  H_CONO = '" + vCONO.trim() + "' AND H_DIVI = '" + vDIVI.trim() + "' AND H_LOCATION = '"
					+ vLocaton.trim() + "' ";
			System.out.println("updateSWRfile\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String checkout1(String vID, String sts, String getLocation, String room, String remark)
			throws Exception {
		logger.info("updateSWRfile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_REMARK1 = '" + remark
					+ "', H_STATUS = '"
					+ sts + "',  H_Timeout = VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS') \r\n"
					+ "　WHERE H_ID  =  '" + vID + "' AND H_LOCATION = '" + getLocation.trim() + "'";
			System.out.println("updateSWRfile\n" + query);

			String query2 = "　UPDATE  " + Constant.DBNAME + ".FOLLOWER_HEAD\r\n" + "　SET H_STATUS = '" + sts + "' \r\n"
					+ "　WHERE F_ID  =  '" + vID + "' AND H_LOCATION = '" + getLocation.trim() + "'";
			System.out.println("updateSWRfile\n" + query);

			String query3 = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_ROOMNO = '" + room + "' \r\n"
					+ "　WHERE H_ID  =  '" + vID + "' AND H_LOCATION = '" + getLocation.trim() + "'";

			logger.debug(query);
			stmt.execute(query);

			logger.debug(query2);
			stmt.execute(query2);

			logger.debug(query3);
			stmt.execute(query3);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String checkoutwithdatetime(String vID, String sts, String getLocation, String remark,
			String vcheckout, String vcheckouttime) throws Exception {
		logger.info("checkoutwithdatetime");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_REMARK1 = '" + remark
					+ "', H_STATUS = '"
					+ sts
					+ "',  H_Timeout = VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS') , H_CHECKOUT = '"
					+ vcheckout + "' , H_CHECKOUTTIME = '" + vcheckouttime + "' \r\n" + "　WHERE H_ID  =  '" + vID
					+ "' AND H_LOCATION = '" + getLocation.trim() + "'";
			System.out.println("updateSWRfile\n" + query);

			String query2 = "　UPDATE  " + Constant.DBNAME + ".FOLLOWER_HEAD\r\n" + "　SET H_STATUS = '" + sts + "' \r\n"
					+ "　WHERE F_ID  =  '" + vID + "' AND H_LOCATION = '" + getLocation.trim() + "'";
			System.out.println("updateSWRfile\n" + query);

			logger.debug(query);
			stmt.execute(query);

			logger.debug(query2);
			stmt.execute(query2);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String checkout(String vID, String sts, String getLocation, String remark) throws Exception {
		logger.info("updateSWRfile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_REMARK1 = '" + remark
					+ "', H_STATUS = '"
					+ sts
					+ "',  H_Timeout = VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS') ,H_CHECKOUTTIME = SUBSTR(CAST(CURRENT TIME AS CHAR(8)), 1, 5) , H_CHECKOUT = CHAR(CURRENT DATE, ISO)  \r\n"
					+ "　WHERE H_ID  =  '" + vID + "' AND H_LOCATION = '" + getLocation.trim() + "'";
			System.out.println("updateSWRfile\n" + query);

			String query2 = "　UPDATE  " + Constant.DBNAME + ".FOLLOWER_HEAD\r\n" + "　SET H_STATUS = '" + sts + "' \r\n"
					+ "　WHERE F_ID  =  '" + vID + "' AND H_LOCATION = '" + getLocation.trim() + "'";
			System.out.println("updateSWRfile\n" + query);

			logger.debug(query);
			stmt.execute(query);

			logger.debug(query2);
			stmt.execute(query2);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String addvisitorheader(String vID, String vIMG, String vTimein, String vTimeout, String vLicense,
			String vName, String vSurname, String vTel, String vReason, String vEmployee, String vStatus, String vCono,
			String vCompany, String vMeetdate, String vMeettime, String vMail, InputStream imagefile, String imagename,
			String vCONO, String vDIVI, String vLocation, String vFoodnumber, String vATKnumber, String vParknumber,
			String vIstoken, String vETC, String vRoom, String vMeetdateout, String vMeettimeout,
			String vBeveragenumber, String vSnacksnumber, String vSandalsnumber, String username) throws Exception {
		logger.info("updateSWRfile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String setImageName = "visitor" + "_" + vID + ".png";

			String query = "　UPDATE  " + Constant.DBNAME + ".VISITOR_HEAD\r\n" + "　SET H_LICENSE = '" + vLicense
					+ "' , H_NAME = '"
					+ vName + "', H_SURNAME = '" + vSurname + "' , H_TEL = '" + vTel + "', \r\n" + "　H_REASON = '"
					+ vReason + "', H_COMPANYNAME2 = '" + vEmployee + "',H_STATUS = '" + vStatus
					+ "' ,H_REMARK1 = '-' , H_COMPANYNAME= '" + vCompany + "' , H_MEETDATE = '" + vMeetdate
					+ "' , H_MEETTIME = '" + vMeettime + "', H_IMG ='" + setImageName + "', H_ROOMNO = '" + vRoom.trim()
					+ "', H_MEETDATEOUT = '" + vMeetdateout + "',H_MEETTIMEOUT = '" + vMeettimeout + "', H_USER = '"
					+ username + "' \r\n" + "　WHERE H_ID  =  '" + vID + "' AND H_CONO = '" + vCONO.trim()
					+ "' AND H_DIVI = '" + vDIVI.trim() + "' AND H_LOCATION = '" + vLocation.trim() + "' ";
			System.out.println("updateSWRfile\n" + query);
			logger.debug(query);
			stmt.execute(query);

			String query2 = "　UPDATE  " + Constant.DBNAME + ".FOLLOWER_HEAD\r\n" + "　SET H_STATUS = '10' \r\n"
					+ "　WHERE F_ID  =  '"
					+ vID + "'";
			System.out.println("updateSWRfile\n" + query);
			logger.debug(query2);
			stmt.execute(query2);

			if (vIstoken.equalsIgnoreCase("true")) {

				String query3 = "INSERT  INTO  " + Constant.DBNAME + ".VISITOR_ITEM \r\n"
						+ "				(H_CONO,H_DIVI,H_ID,H_FOODQTY,H_ATKQTY,H_PARKQTY,H_REMARK1,H_REMARK2,H_REMARK3,H_REMARK4,H_BEVERAGE,H_SNACKS,H_SANDALS)\r\n"
						+ "		　VALUES ('" + vCONO.trim() + "','" + vDIVI.trim() + "','" + vID + "','" + vFoodnumber
						+ "','" + vATKnumber + "','" + vParknumber + "','" + vETC.trim() + "','0','0','0','"
						+ vBeveragenumber + "','" + vSnacksnumber + "','" + vSandalsnumber + "')";
				System.out.println("updateSWRfile\n" + query2);
				logger.debug(query3);
				stmt.execute(query3);

			}

			if (imagename != null) {
				String uploadFilePath = null;
				// String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
				String filePath = "D:\\files\\api_project\\visitor_file\\";
				// System.out.println("filePath: " + filePath + setImageName);
				uploadFilePath = FileUtillity.writeToFileServer(imagefile, setImageName, filePath);

			}

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String updateSWRFile(String oldSFORNO, String oldSFCONO, String oldSFDIVI, String newSFORNO,
			String newSFPREF, String newSFLINE, String newSFNAME, String newSFTYPE, String newSFREM1, String newSFREM2,
			String getUsername) throws Exception {
		logger.info("updateSWRfile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE  " + DBNAME + ".M3_SWRFILE\r\n" + "			SET SFPREF = '" + newSFPREF
					+ "' ,SFLINE = '" + newSFLINE + "',  SFNAME = '" + newSFNAME + "',SFTYPE ='" + newSFTYPE
					+ "',SFREM1 = '" + newSFREM1 + "' , SFREM2 = '" + newSFREM2 + "' WHERE SFORNO = '" + oldSFORNO
					+ "' AND SFCONO= '" + oldSFCONO + "' AND SFDIVI = '" + oldSFDIVI + "'";
			System.out.println("updateSWRfile\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String updateStatusMARHead(String cono, String divi, String marno, String status, String submit,
			String username) throws Exception {
		logger.info("updateStatusMARHead {}", submit);

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "UPDATE " + Constant.DBNAME + ".M3_MARHEAD \n" + "SET MHSTAT = '" + status + "' \n"
					+ ", MHENDA = CURRENT DATE \n" + ", MHENTI = CURRENT TIME \n" + ", MHENUS = '" + username + "' \n"
					+ "WHERE MHCONO = '" + cono + "' \n" + "AND MHDIVI = '" + divi + "' \n"
					+ "AND MHPREF || '-' || MHORNO = '" + marno + "'";
			// System.out.println("updateMARHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");

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

	public static String updateMARDetail(String cono, String divi, String marno, String prefix, String refno,
			String typeadjust, String line, String item, String name, String fac, String whs, String loc, String lotno,
			String date, String unit, String qty, String price, String amt, String remark1, String remark2,
			String status, String username) throws Exception {
		logger.info("updateMARDetail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			remark1 = ConvertString.convertApostrophe(remark1);
			remark2 = ConvertString.convertApostrophe(remark2);

			String query = "UPDATE " + Constant.DBNAME + ".M3_MARDETAIL \n" + "SET MLREM1 = '" + remark1 + "' \n"
					+ ", MLREM2 = '"
					+ remark2 + "' \n" + ", MLENDA = CURRENT DATE \n" + ", MLENTI = CURRENT TIME \n" + ", MLENUS = '"
					+ username + "' \n" + "WHERE MLCONO = '" + cono + "' \n" + "AND MLDIVI = '" + divi + "' \n"
					+ "AND MLLINE = '" + line + "' \n" + "AND MLITNO = '" + item + "' \n"
					+ "AND MLPREF || '-' || MLORNO = '" + marno + "'";
			// System.out.println("updateMARDetail\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Update complete.");
			logger.info("Update complete.");
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

	public static String updateADRHead(String cono, String divi, String adrno, String date, String month, String type,
			String prefix, String boi, String bu, String costcenter, String vat, String accountant, String requestor,
			String remark1, String approve1, String approve2, String approve3, String approve4, String status,
			String submit, String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		remark1 = ConvertString.convertApostrophe(remark1);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + Constant.DBNAME + ".M3_ADRHEAD \n" + "SET ADTYPE = '" + type + "' \n"
						+ ", ADBOI = '"
						+ boi + "' \n" + ", ADBU = '" + bu + "' \n" + ", ADCOCE = '" + costcenter + "' \n"
						+ ", ADACCT = '" + accountant + "' \n" + ", ADREM1 = '" + remark1 + "' \n"
						+ ", ADENDA = CURRENT DATE \n" + ", ADENTI = CURRENT TIME \n" + ", ADENUS = '" + username
						+ "' \n" + "WHERE ADCONO = '" + cono + "' \n" + "AND ADDIVI = '" + divi + "' \n"
						+ "AND ADPREF || '-' || ADORNO = '" + adrno + "'";
				// System.out.println("updateADRHead\n" + query);
				stmt.execute(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Update complete.");
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

	public static String updateADRDetail(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, String prefix, String line, String itemno, String sbno, String costcenter, String date,
			String assetcost, String netvalue, String qty, String price, String remark, InputStream imagefile,
			String imagename, String status, String submit, String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		remark = ConvertString.convertApostrophe(remark);
		String setImageName = adrno + "_" + line + ".png";

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + Constant.DBNAME + ".M3_ADRDETAIL \n" + "SET ALQTY = '" + qty + "' \n"
						+ ", ALPRIC = '"
						+ price + "' \n" + ", ALREM1 = '" + remark + "' \n" + ", ALIMAG = '" + setImageName + "' \n"
						+ ", ALENDA = CURRENT DATE \n" + ", ALENTI = CURRENT TIME \n" + ", ALENUS = '" + username
						+ "' \n" + "WHERE ALCONO = '" + cono + "' \n" + "AND ALDIVI = '" + divi + "' \n"
						+ "AND ALLINE = '" + line + "' \n" + "AND ALITNO = '" + itemno + "' \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "'";
				// System.out.println("updateADRDetail\n" + query);
				stmt.execute(query);

				// System.out.println("imagefile: " + imagefile);
				// System.out.println("imagename: " + imagename);

				if (imagename != null) {
					String uploadFilePath = null;
					// String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
					String filePath = "D:\\files\\api_project\\adr_images\\";
					// System.out.println("filePath: " + filePath + setImageName);
					uploadFilePath = FileUtillity.writeToFileServer(imagefile, setImageName, filePath);

				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Update complete.");
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

	public static String updateADRImage(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, InputStream imagefile, String imagename, String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		String setImageName = adrno + ".png";

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + Constant.DBNAME + ".M3_ADRHEAD \n" + "SET ADIMAG = '" + setImageName + "' \n"
						+ ", ADENDA = CURRENT DATE \n" + ", ADENTI = CURRENT TIME \n" + ", ADENUS = '" + username
						+ "' \n" + "WHERE ADCONO = '" + cono + "' \n" + "AND ADDIVI = '" + divi + "' \n"
						+ "AND ADPREF || '-' || ADORNO = '" + adrno + "'";
				// System.out.println("updateADRImage\n" + query);
				stmt.execute(query);

				// System.out.println("imagefile: " + imagefile);
				// System.out.println("imagename: " + imagename);

				if (imagename != null) {
					String uploadFilePath = null;
					// String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
					String filePath = "D:\\files\\api_project\\adr_images\\";
					// System.out.println("filePath: " + filePath + setImageName);
					uploadFilePath = FileUtillity.writeToFileServer(imagefile, setImageName, filePath);

				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Update complete.");
				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
			}

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

	public static String updateStatusADRHead(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, String tostatus, String submit, String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				String setStatus = "", setDate = "";
				String getResultSendEmail;
				JSONObject mJResult = new JSONObject();

				if (tostatus.equals("10")) { // Accountant
					setStatus = "10";
					getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus,
							"ADR", "PRD");
					// System.out.println("getResultSendEmail\n" + getResultSendEmail);
					mJResult = new JSONObject(getResultSendEmail);

				} else if (tostatus.equals("15")) { // Approve1
					setStatus = "15";
					getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus,
							"ADR", "PRD");
					// System.out.println("getResultSendEmail\n" + getResultSendEmail);
					mJResult = new JSONObject(getResultSendEmail);

				} else if (tostatus.equals("20")) { // Approve2
					setStatus = "20";
					setDate = ", ADAPDA1 = CURRENT DATE \n";
					getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus,
							"ADR", "PRD");
					// System.out.println("getResultSendEmail\n" + getResultSendEmail);
					mJResult = new JSONObject(getResultSendEmail);

				} else if (tostatus.equals("25")) { // Approve3
					double getADRAssetCost = SelectData.getADRAmount(cono, divi, adrno);
					if (getADRAssetCost >= 50000) {
						setStatus = "25";
						setDate = ", ADAPDA2 = CURRENT DATE \n";
						getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno,
								setStatus, "ADR", "PRD");
						// System.out.println("getResultSendEmail\n" + getResultSendEmail);
						mJResult = new JSONObject(getResultSendEmail);

					} else {
						setStatus = "30";
						setDate = ", ADAPDA2 = CURRENT DATE";
						// mJResult = new JSONObject("{\"result\":\"ok\"}");

						getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno,
								setStatus, "ADR", "PRD");
						// System.out.println("getResultSendEmail\n" + getResultSendEmail);
						mJResult = new JSONObject(getResultSendEmail);

					}

				} else if (tostatus.equals("30")) { // Requester
					setStatus = "30";
					setDate = ", ADAPDA3 = CURRENT DATE \n";
					getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus,
							"ADR", "PRD");
					// System.out.println("getResultSendEmail\n" + getResultSendEmail);
					mJResult = new JSONObject(getResultSendEmail);

				} else if (tostatus.equals("35")) { // Accountant
					setStatus = "35";
					setDate = ", ADAPDA4 = CURRENT DATE \n";
					getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus,
							"ADR", "PRD");
					// System.out.println("getResultSendEmail\n" + getResultSendEmail);
					mJResult = new JSONObject(getResultSendEmail);

				} else if (tostatus.equals("40")) { // Complete
					setStatus = "40";
					getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus,
							"ADR", "PRD");
					// System.out.println("getResultSendEmail\n" + getResultSendEmail);
					mJResult = new JSONObject(getResultSendEmail);

				} else {
					mJResult = new JSONObject("{\"result\":\"ok\"}");

				}

				if (!mJResult.getString("result").equals("ok")) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", submit + " not complete.");
					return mJsonObj.toString();

				}

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + Constant.DBNAME + ".M3_ADRHEAD \n" + "SET ADSTAT = '" + setStatus + "' \n"
						+ "" + setDate
						+ "" + ", ADENDA = CURRENT DATE \n" + ", ADENTI = CURRENT TIME \n" + ", ADENUS = '" + username
						+ "' \n" + "WHERE ADCONO = '" + cono + "' \n" + "AND ADDIVI = '" + divi + "' \n"
						+ "AND ADPREF || '-' || ADORNO = '" + adrno + "'";
				// System.out.println("updateStatusADRHead\n" + query);
				stmt.execute(query);

				if (setStatus.equals("10")) {
					updateStatusADRDetail(cono, divi, adrno, "10", submit, username);

				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", submit + " complete.");
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

	public static String updateStatusADRDetail(String cono, String divi, String adrno, String status, String submit,
			String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + Constant.DBNAME + ".M3_ADRDETAIL \n" + "SET ALSTAT = '" + status + "' \n"
						+ ", ALENDA = CURRENT DATE \n" + ", ALENTI = CURRENT TIME \n" + ", ALENUS = '" + username
						+ "' \n" + "WHERE ALCONO = '" + cono + "' \n" + "AND ALDIVI = '" + divi + "' \n"
						+ "AND ALPREF || '-' || ALORNO = '" + adrno + "'";
				// System.out.println("updateStatusADRDetail\n" + query);
				stmt.execute(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", submit + " complete.");
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

	public static String updateRejectADRHead(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, String remark, String accremark, String appremark1, String appremark2, String appremark3,
			String appremark4, String fromstatus, String submit, String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		String setStatus = "05", setRemark = "", setReason = "";
		String getResultSendEmail;
		JSONObject mJResult = new JSONObject();

		if (fromstatus.equals("10")) { // Requester
			accremark = ConvertString.convertApostrophe(accremark);
			setRemark = ", ADACRE1 = '" + accremark + "' \n";
			setReason = "from Accountant, reason: " + accremark;

			getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus, "ADR",
					setReason);
			// System.out.println("getResultSendEmail\n" + getResultSendEmail);
			mJResult = new JSONObject(getResultSendEmail);

		} else if (fromstatus.equals("15")) { // Approve1
			appremark1 = ConvertString.convertApostrophe(appremark1);
			setRemark = ", ADAPRE1 = '" + appremark1 + "' \n";
			setReason = "from Approve1, reason: " + appremark1;

			getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus, "ADR",
					setReason);
			// System.out.println("getResultSendEmail\n" + getResultSendEmail);
			mJResult = new JSONObject(getResultSendEmail);

		} else if (fromstatus.equals("20")) { // Approve2
			appremark2 = ConvertString.convertApostrophe(appremark2);
			setRemark = ", ADAPRE2 = '" + appremark2 + "' \n";
			setReason = "from Approve2, reason: " + appremark2;

			getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus, "ADR",
					setReason);
			// System.out.println("getResultSendEmail\n" + getResultSendEmail);
			mJResult = new JSONObject(getResultSendEmail);

		} else if (fromstatus.equals("25")) { // Approve3
			appremark3 = ConvertString.convertApostrophe(appremark3);
			setRemark = ", ADAPRE3 = '" + appremark3 + "' \n";
			setReason = "from Approve3, reason: " + appremark3;

			getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus, "ADR",
					setReason);
			// System.out.println("getResultSendEmail\n" + getResultSendEmail);
			mJResult = new JSONObject(getResultSendEmail);

		} else if (fromstatus.equals("30")) { // Requester
			appremark4 = ConvertString.convertApostrophe(appremark4);
			setRemark = ", ADAPRE4 = '" + appremark4 + "' \n";
			setReason = "from Approve4, reason: " + appremark4;

			getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus, "ADR",
					setReason);
			// System.out.println("getResultSendEmail\n" + getResultSendEmail);
			mJResult = new JSONObject(getResultSendEmail);

		} else if (fromstatus.equals("35")) { // Accountant
			accremark = ConvertString.convertApostrophe(accremark);
			setRemark = ", ADACRE1 = '" + accremark + "' \n";
			setReason = "from Accountant, reason: " + accremark;

			getResultSendEmail = SendEmail.prepareSendEmail(httpServletRequest, cono, divi, adrno, setStatus, "ADR",
					setReason);
			// System.out.println("getResultSendEmail\n" + getResultSendEmail);
			mJResult = new JSONObject(getResultSendEmail);

			setStatus = "30";

		} else {
			mJResult = new JSONObject("{\"result\":\"ok\"}");

		}

		if (!mJResult.getString("result").equals("ok")) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", submit + " not complete.");
			return mJsonObj.toString();

		}

		try {
			if (conn != null) {
				Statement stmt = conn.createStatement();
				String query = "UPDATE " + Constant.DBNAME + ".M3_ADRHEAD \n" + "SET ADSTAT = '" + setStatus + "' \n"
						+ ""
						+ setRemark + "" + ", ADENDA = CURRENT DATE \n" + ", ADENTI = CURRENT TIME \n" + ", ADENUS = '"
						+ username + "' \n" + "WHERE ADCONO = '" + cono + "' \n" + "AND ADDIVI = '" + divi + "' \n"
						+ "AND ADPREF || '-' || ADORNO = '" + adrno + "'";
				// System.out.println("updateRejectADRHead\n" + query);
				stmt.execute(query);

				if (setStatus.equals("05")) {
					updateStatusADRDetail(cono, divi, adrno, "00", submit, username);

				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", submit + " complete.");
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

	///////////////////////////////////// BANK MAPPING
	///////////////////////////////////// //////////////////////////////////

	public static String deleteidmove(String getCono, String ID, String GROUP_ID) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				/*
				 * String query = "UPDATE "+DBNAME+".BANK_MAPPING\r\n" +
				 * "		 SET BM_FNNO = '-'\r\n"
				 * + "	     WHERE BM_CONO  = '"+getCono+"' AND BM_ID = '" + ID +
				 * "' AND BM_CUNO  ='-'";
				 */

				String query = "UPDATE " + DBNAME + ".BANK_MAPPING\r\n" + "		 SET BM_FNNO = '-'\r\n"
						+ "	     WHERE BM_CONO  = '" + getCono + "' AND BM_ID = '" + ID + "' ";
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	public static String deleteid(String getCono, String getDivi, String ID, String BM_CONO, String BM_PARENT,
			String GROUP_ID, String H_RNNO,
			String CHECKTYPE) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "UPDATE " + DBNAME + ".BANK_MAPPING\r\n"
						+ "		 SET BM_STATUS = '99' , BM_FNNO = NULL \r\n"
						+ "	     WHERE BM_CONO  = '" + getCono + "' AND BM_ID = '" + ID + "' AND BM_PARENT_STS  ='77'";
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//////////////// RETURN ////////////

	public static String RETURNPARENT(String ID, String CONO) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "UPDATE " + DBNAMEPP + ".BANK_MAPPING\r\n"
						+ "		 SET BM_BKCHARGE = '0', BM_OVPAY = '0' , BM_CNDN = '0',  BM_PARENT_STS  ='00' ,BM_CUNO = '-' \r\n"
						+ "	     WHERE BM_CONO  = '" + CONO + "' AND BM_ID = '" + ID + "'";
				stmt.executeUpdate(query);

				System.out.println(query);

				String query2 = "UPDATE " + DBNAMEPP + ".BANK_MAPPING\r\n"
						+ "		 SET BM_PARENT_ID = NULL , BM_STATUS = '99' \r\n"
						+ "	     WHERE BM_CONO  = '" + CONO + "' AND BM_PARENT_ID = '" + ID + "'";
				stmt.executeUpdate(query2);

				System.out.println(query);

				System.out.println(query2);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//////// master //////////

	/// head

	public static String deletehead(String H_CONO, String H_DIVI, String H_RCNO) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = " DELETE " + Constant.DBNAME + ".HEAD_RECIPT\r\n"
						+ "	 WHERE  H_CONO  = '" + H_CONO + "'\r\n"
						+ "	AND H_DIVI = '" + H_DIVI + "'\r\n"
						+ "	 AND H_RCNO = '" + H_RCNO + "'";

				System.out.println(query);
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//// bankmapping

	public static String deletemapping(String BM_ID) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = " DELETE " + Constant.DBNAME + ".BANK_MAPPING\r\n"
						+ "	 WHERE  BM_ID  = '" + BM_ID + "'";

				System.out.println(query);
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//// hcrc

	public static String deletehcrc(String HR_CONO, String HR_DIVI, String HC_RCNO) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "DELETE " + Constant.DBNAME + ".HR_RECEIPT\r\n"
						+ "	 WHERE HR_CONO = '" + HR_CONO + "'\r\n"
						+ "	 AND  HR_DIVI = '" + HR_DIVI + "'\r\n"
						+ "	 AND  HC_RCNO = '" + HC_RCNO + "'";

				System.out.println(query);
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//// lnrc

	public static String deletelnrc(String LR_CONO, String LR_DIVI, String LR_RCNO) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "DELETE " + Constant.DBNAME + ".LR_LINERECEIPT\r\n"
						+ "	 WHERE LR_CONO = '" + LR_CONO + "'\r\n"
						+ "	 AND  LR_DIVI = '" + LR_DIVI + "'\r\n"
						+ "	 AND  LR_RCNO = '" + LR_RCNO + "'";

				System.out.println(query);
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	///////////////////////////

	public static String DELETELIST(String getCono, String getDivi, String ID) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "DELETE  " + Constant.DBNAME + ".BANK_MAPPING \r\n"
						+ "                     WHERE  BM_ID  = '" + ID + "'\r\n"
						+ "                     AND (BM_FNNO  IS NULL OR BM_FNNO = '-')\r\n"
						+ "                     AND BM_CONO = '" + getCono + "'\r\n"
						+ "                     AND BM_DIVI  = '" + getDivi + "'";

				System.out.println(query);
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	public static String RETURNNORMAL(String ID, String CONO, String GROUPID) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "  UPDATE  " + DBNAMEPP + ".BANK_MAPPING  \n"
						+ "  SET  BM_CUNO = '-' , BM_RCNO = NULL , BM_BKCHARGE = 0 \n" + "  WHERE   BM_FNNO  = '"
						+ GROUPID.trim() + "' AND BM_CONO  = '" + CONO.trim() + "' ";

				System.out.println(query);
				stmt.executeUpdate(query);

				Statement stmt1 = conn.createStatement();
				String query1 = "  UPDATE  " + DBNAMEPP + ".HEAD_RECIPT  \n"
						+ "  SET  H_CUNO = NULL , H_PYNO = NULL , H_STS = 1 , H_VCNO = NULL , H_TYPE = NULL \n"
						+ "  WHERE   H_RNNO  = '" + GROUPID.trim() + "' AND  H_CONO = '" + CONO.trim() + "' ";

				System.out.println(query1);
				stmt1.executeUpdate(query1);

				Statement stmt2 = conn.createStatement();
				String query2 = "   UPDATE  " + DBNAMEPP + ".HR_RECEIPT  \n"
						+ "  SET  HC_TRDT = NULL , HC_PYNO = NULL ,  HC_REAMT = 0 , HC_TYPE = NULL   ,HC_BANK = NULL ,HC_ACCODE = NULL ,HC_PYDT = NULL, HC_USER = NULL, HC_VCNO = NULL , HC_STS = 2 , HR_BKCHG = 0 , HC_FIXNO = NULL \n"
						+ "  WHERE   HC_FNNO  = '" + GROUPID.trim() + "' AND  HR_CONO = '" + CONO.trim() + "' ";

				System.out.println(query2);
				stmt2.executeUpdate(query2);

				Statement stmt3 = conn.createStatement();
				String query3 = "DELETE FROM   " + DBNAMEPP + ".LR_LINERECEIPT\n"
						+ "        WHERE LR_RCNO  IN  (SELECT HC_RCNO FROM " + DBNAMEPP
						+ ".HR_RECEIPT hr WHERE  HC_FNNO = '" + GROUPID.trim() + "' ) AND LR_CONO  = '" + CONO.trim()
						+ "' ";

				System.out.println(query3);
				stmt3.executeUpdate(query3);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	///////////////////////////////

	///////////////// master ///////////////

	///// HEAD

	public static String savehead(String H_CONO, String H_DIVI, String H_RNNO, String H_RCNO, String H_CUNO,
			String H_PYNO, String H_STS, String H_VCNO, String H_LOCATION, String H_TYPE) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = " UPDATE  " + Constant.DBNAME + ".HEAD_RECIPT\r\n"
						+ "  SET H_RNNO = '" + H_RNNO + "',H_CUNO = '" + H_CUNO + "',H_PYNO = '" + H_PYNO
						+ "',H_STS = '" + H_STS + "',H_VCNO = '" + H_VCNO + "',H_LOCATION = '" + H_LOCATION
						+ "',H_TYPE= '" + H_TYPE + "'\r\n"
						+ "  WHERE H_CONO = '" + H_CONO + "'\r\n"
						+ "  AND H_DIVI  = '" + H_DIVI + "'\r\n"
						+ "  AND H_RCNO = '" + H_RCNO + "'";
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//// mapping

	public static String savemapping(String BM_ID, String BM_CONO, String BM_DIVI, String BM_BANK, String BM_ACCODE,
			String BM_DATE, String BM_TIME, String BM_CHQNO, String BM_CUNO, String BM_AMOUNT, String BM_DESC,
			String BM_RCNO, String BM_USER, String BM_BKCHARGE, String BM_OVPAY, String BM_CNDN, String BM_STATUS,
			String BM_FNNO, String BM_LCODE, String BM_PARENT_ID, String BM_PARENT_STS) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "  UPDATE  " + Constant.DBNAME + ".BANK_MAPPING \r\n"
						+ "  SET BM_CONO = '" + BM_CONO + "',BM_DIVI = '" + BM_DIVI + "',BM_BANK = '" + BM_BANK
						+ "',BM_ACCODE = '" + BM_ACCODE + "',BM_DATE = '" + BM_DATE + "',BM_TIME = '" + BM_TIME
						+ "',BM_CHQNO = '" + BM_CHQNO + "',BM_CUNO = '" + BM_CUNO + "',BM_AMOUNT = '" + BM_AMOUNT
						+ "',BM_DESC = '" + BM_DESC + "',BM_RCNO = '" + BM_RCNO + "',\r\n"
						+ "  BM_USER = '" + BM_USER + "',BM_BKCHARGE = '" + BM_BKCHARGE + "',BM_OVPAY = '" + BM_OVPAY
						+ "',BM_CNDN = '" + BM_CNDN + "',BM_STATUS = '" + BM_STATUS + "',BM_FNNO = '" + BM_FNNO
						+ "',BM_LCODE = '" + BM_LCODE + "',BM_PARENT_ID = '" + BM_PARENT_ID + "',BM_PARENT_STS = '"
						+ BM_PARENT_STS + "'\r\n"
						+ "  WHERE BM_ID = '" + BM_ID + "'";
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//// HCRC

	public static String savehcrc(String HR_CONO, String HR_DIVI, String HC_RCNO, String HC_TRDT, String HC_PYNO,
			String HC_REAMT, String HC_TYPE, String HC_BANK, String HC_ACCODE, String HC_PYDT, String HC_CHKNO,
			String HC_USER, String HC_VCNO, String HC_STS, String HR_BKCHG, String HC_LOCATION, String HC_FIXNO,
			String HC_FNNO) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = " UPDATE  " + Constant.DBNAME + ".HR_RECEIPT \r\n"
						+ "  SET HC_TRDT = '" + HC_TRDT + "',HC_PYNO = '" + HC_PYNO + "',HC_REAMT = '" + HC_REAMT
						+ "',HC_TYPE = '" + HC_TYPE + "',\r\n"
						+ "  HC_BANK = '" + HC_BANK + "',HC_ACCODE = '" + HC_ACCODE + "',HC_PYDT = '" + HC_PYDT
						+ "',HC_CHKNO = '" + HC_CHKNO + "',HC_USER = '" + HC_USER + "',HC_VCNO = '" + HC_VCNO
						+ "',HC_STS = '" + HC_STS + "',HR_BKCHG = '" + HR_BKCHG + "',\r\n"
						+ "  HC_LOCATION = '" + HC_LOCATION + "',HC_FIXNO = '" + HC_FIXNO + "',HC_FNNO = '" + HC_FNNO
						+ "'\r\n"
						+ "  WHERE HR_CONO = '" + HR_CONO + "'\r\n"
						+ "  AND HR_DIVI  = '" + HR_DIVI + "'\r\n"
						+ "  AND HC_RCNO = '" + HC_RCNO + "'";
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	//// LNRC

	public static String savelnrc(String LR_CONO, String LR_DIVI, String LR_RCNO, String LR_INVNO, String LR_INVDT,
			String LR_INVAMT, String LR_REAMT, String LR_STS)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "UPDATE  " + Constant.DBNAME + ".LR_LINERECEIPT  \r\n"
						+ "  SET LR_INVNO = '" + LR_INVNO + "',LR_INVDT = '" + LR_INVDT + "',LR_INVAMT = '" + LR_INVAMT
						+ "',LR_REAMT = '" + LR_REAMT + "',LR_STS = '" + LR_STS + "'\r\n"
						+ "  WHERE LR_CONO = '" + LR_CONO + "'\r\n"
						+ "  AND LR_DIVI  = '" + LR_DIVI + "'\r\n"
						+ "  AND LR_RCNO = '" + LR_RCNO + "'\r\n"
						+ "";
				stmt.executeUpdate(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	////////////////////////////////////////

	public static String unlockinv(String getCono, String getDivi, String INV) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query0 = "DELETE  FROM M3FDBPRD.FCR040\r\n"
						+ "WHERE ACCINO LIKE '%" + INV + "%'\r\n"
						+ "AND ACSTCF IN ( 9,0 )";
				System.out.print(query0);

				// ใช้ executeUpdate() สำหรับ UPDATE statement
				int rowsAffected = stmt.executeUpdate(query0);

				// ตรวจสอบว่ามีแถวถูกอัปเดตหรือไม่
				if (rowsAffected > 0) {
					mJsonObj.put("result", "ok");
					mJsonObj.put("message", "Update complete.");
				} else {
					mJsonObj.put("result", "error");
					mJsonObj.put("message", "No records updated.");
				}

				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
				mJsonObj.put("result", "error");
				mJsonObj.put("message", "Server connection failed.");
				return mJsonObj.toString();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static String addstatement(String getCono, String getDivi, String BANK, String BANKCODE, String DATE1,
			String TIME, String AMOUNT, String DESC, String REFCUS, String LCODE, String CUSTOMERNAME)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query0 = "INSERT INTO " + Constant.DBNAME
						+ ".BANK_MAPPING  (bm_cono, bm_divi , bm_bank, BM_ACCODE, BM_DATE, BM_TIME, BM_CHQNO, BM_TRANSAC, BM_CHANEL , BM_BRANCH , BM_AMOUNT, BM_DESC , BM_TIME1, BM_USER, BM_STATUS, BM_REFCU, BM_REFCU1, BM_LCODE, BM_CUNA) \r\n"
						+ "VALUES ( '" + getCono + "', '" + getDivi + "' , '" + BANK + "', '" + BANKCODE + "', '"
						+ DATE1 + "' , '" + TIME + "' , '-', '-', '-', '-', '" + AMOUNT + "', '" + DESC
						+ "', VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'), 'MNADD', '10', '-', '"
						+ REFCUS + "', '" + LCODE + "', '" + CUSTOMERNAME + "')";

				System.out.print(query0);

				// ใช้ executeUpdate() สำหรับ UPDATE statement
				int rowsAffected = stmt.executeUpdate(query0);

				// ตรวจสอบว่ามีแถวถูกอัปเดตหรือไม่
				if (rowsAffected > 0) {
					mJsonObj.put("result", "ok");
					mJsonObj.put("message", "Update complete.");
				} else {
					mJsonObj.put("result", "error");
					mJsonObj.put("message", "No records updated.");
				}

				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
				mJsonObj.put("result", "error");
				mJsonObj.put("message", "Server connection failed.");
				return mJsonObj.toString();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static String setlcode(String getCono, String MONTH) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query0 = "UPDATE " + Constant.DBNAME + ".BANK_MAPPING\r\n"
						+ "SET BM_LCODE = \r\n"
						+ "  CASE \r\n"
						+ "    WHEN BM_BANK = 'BBL' THEN '1AA2114'\r\n"
						+ "    WHEN BM_BANK = 'BBL_CUR' THEN '1AA2214'\r\n"
						+ "    WHEN BM_BANK = 'BBL_QR' THEN '1AA2214'\r\n"
						+ "    WHEN BM_BANK = 'KBANK' THEN '1AA2110'\r\n"
						+ "    WHEN BM_BANK = 'KBANK_BILL' THEN '1AA2250'\r\n"
						+ "    WHEN BM_BANK = 'KBANK_CUR' THEN '1AA2250'\r\n"
						+ "    WHEN BM_BANK = 'SCB' THEN '1AA2105'\r\n"
						+ "    WHEN BM_BANK = 'SCB_BILL' THEN '1AA2283'\r\n"
						+ "    WHEN BM_BANK = 'SCB_CUR' THEN '1AA2286'\r\n"
						+ "    WHEN BM_BANK = 'SCB_MMN' THEN '1AA2286'\r\n"
						+ "    ELSE BM_LCODE \r\n"
						+ "  END\r\n"
						+ "WHERE (BM_LCODE IS NULL OR BM_LCODE = '-') \r\n"
						+ "  AND BM_CONO = '" + getCono + "'\r\n"
						+ "  AND BM_DATE  LIKE  '" + MONTH.trim() + "%'";

				System.out.print(query0);

				// ใช้ executeUpdate() สำหรับ UPDATE statement
				int rowsAffected = stmt.executeUpdate(query0);

				// ตรวจสอบว่ามีแถวถูกอัปเดตหรือไม่
				if (rowsAffected > 0) {
					mJsonObj.put("result", "ok");
					mJsonObj.put("message", "Update complete.");
				} else {
					mJsonObj.put("result", "error");
					mJsonObj.put("message", "No records updated.");
				}

				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
				mJsonObj.put("result", "error");
				mJsonObj.put("message", "Server connection failed.");
				return mJsonObj.toString();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static String returninvoice(String getCono, String ID) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query0 = "DELETE  " + Constant.DBNAME + ".LR_LINERECEIPT ll  \r\n"
						+ "WHERE  LR_RCNO  IN (SELECT  H_RCNO FROM " + Constant.DBNAME + ".HEAD_RECIPT hr \r\n"
						+ "WHERE  H_RNNO  = '" + ID + "')\r\n"
						+ "AND LR_CONO  = '" + getCono + "'";

				// ใช้ executeUpdate() สำหรับ UPDATE statement
				int rowsAffected = stmt.executeUpdate(query0);

				// ตรวจสอบว่ามีแถวถูกอัปเดตหรือไม่
				if (rowsAffected > 0) {
					mJsonObj.put("result", "ok");
					mJsonObj.put("message", "Update complete.");
				} else {
					mJsonObj.put("result", "error");
					mJsonObj.put("message", "No records updated.");
				}

				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
				mJsonObj.put("result", "error");
				mJsonObj.put("message", "Server connection failed.");
				return mJsonObj.toString();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static String setpost(String getCono, String getDivi, String ID) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query0 = "UPDATE " + Constant.DBNAME + ".BANK_MAPPING "
						+ "SET BM_RCNO = ( "
						+ "    SELECT MAX(H_RCNO) "
						+ "    FROM " + Constant.DBNAME + ".HEAD_RECIPT hr "
						+ "    WHERE hr.H_RNNO = '" + ID + "' "
						+ ") "
						+ "WHERE BM_FNNO = '" + ID + "' "
						+ "AND BM_CONO = '" + getCono + "' "
						+ "AND BM_DIVI = '" + getDivi + "'";

				// ใช้ executeUpdate() สำหรับ UPDATE statement
				int rowsAffected = stmt.executeUpdate(query0);

				// ตรวจสอบว่ามีแถวถูกอัปเดตหรือไม่
				if (rowsAffected > 0) {
					mJsonObj.put("result", "ok");
					mJsonObj.put("message", "Update complete.");
				} else {
					mJsonObj.put("result", "error");
					mJsonObj.put("message", "No records updated.");
				}

				return mJsonObj.toString();

			} else {
				System.err.println("Server can't connect.");
				mJsonObj.put("result", "error");
				mJsonObj.put("message", "Server connection failed.");
				return mJsonObj.toString();
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw sqle;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static String saveid(String getCono, String getDivi, String ID, String PAYER, String BANKCHARGE,
			String OVERPAY, String CNDN, String TYPE,
			String GROUPID, String LOCATION) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String hcRcno = ""; // ตัวแปรสำหรับเก็บค่า HC_RCNO

				String query0 = "SELECT  HC_RCNO \r\n"
						+ "FROM " + Constant.DBNAME + ".HR_RECEIPT hr \r\n"
						+ "WHERE  HR_CONO  = '" + getCono + "'\r\n"
						+ "AND HC_FNNO  = '" + GROUPID + "'";

				ResultSet rs = stmt.executeQuery(query0);

				// ตรวจสอบว่ามีผลลัพธ์หรือไม่
				if (rs.next()) {
					hcRcno = rs.getString("HC_RCNO");

					// ตรวจสอบว่าค่าที่ได้เป็น null หรือไม่
					if (hcRcno == null || hcRcno.trim().isEmpty()) {
						hcRcno = "-"; // หรือกำหนดข้อความที่ต้องการเมื่อไม่มีค่า
					}
				} else {
					hcRcno = "-"; // กรณีที่ไม่มีแถวในผลลัพธ์ของ query
				}

				rs.close();

				String query = "UPDATE " + DBNAMEPP + ".BANK_MAPPING\r\n" + "		 SET BM_FNNO = '" + GROUPID
						+ "', BM_RCNO = '" + hcRcno + "'  , BM_BKCHARGE = '" + BANKCHARGE + "', BM_OVPAY = '" + OVERPAY
						+ "' , BM_CNDN = '" + CNDN
						+ "' \r\n" + "	     WHERE BM_CONO  = '" + getCono + "' AND BM_ID = '" + ID
						+ "' AND BM_PARENT_STS  ='77'";
				stmt.executeUpdate(query);

				String query2 = "UPDATE " + DBNAMEPP + ".HEAD_RECIPT\r\n" + "		 SET H_LOCATION = '" + LOCATION
						+ "', H_TYPE = '" + TYPE + "' \r\n" + "	     WHERE H_CONO  = '" + getCono + "' AND H_RNNO = '"
						+ GROUPID
						+ "'";
				stmt.executeUpdate(query2);

				System.out.println(query);

				System.out.println(query2);

				/*
				 * Double Amt_Include_BKCHG = Double.parseDouble(BMAMT) +
				 * Double.parseDouble(BANKCHARGE);
				 * 
				 * System.out.println(query); Statement stmt2 = conn.createStatement(); String
				 * query2 = "UPDATE "+DBNAME+".HEAD_RECIPT SET  H_PYNO = '" +
				 * PAYER.toUpperCase() + "', H_LOCATION = '" + LOCATION + "' , H_TYPE = '" +
				 * TYPE + "' , H_CUNO = 'HEAD' \n" + "WHERE H_RNNO  = '" + GROUPID + "' \n" +
				 * "AND H_CONO  = '10' AND H_STS = 1 \n" + "AND H_DIVI  = '101'";
				 * System.out.println(query2); stmt2.executeUpdate(query2);
				 * 
				 */

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	public static String UPDATEID(String getCono, String getDivi, String BMDATE, String ID, String PAYER,
			String BANKCHARGE, String OVERPAY,
			String CNDN, String TYPE, String LOCATION, String GROUPID, String BMAMT) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "UPDATE " + DBNAME + ".BANK_MAPPING\n" + "SET BM_CUNO = '" + PAYER.trim()
						+ "' ,  BM_BKCHARGE = '" + BANKCHARGE.trim() + "', BM_OVPAY = '" + OVERPAY.trim()
						+ "', BM_CNDN = '" + CNDN.trim() + "'\n" + "WHERE BM_CONO = '" + getCono + "' "
						+ "AND BM_DIVI = '" + getDivi + "' "
						+ "AND BM_DATE = '" + BMDATE.trim() + "' " + "AND BM_ID = '" + ID.trim() + "' "
						+ "AND BM_FNNO = '" + GROUPID.trim() + "' ";

				stmt.executeUpdate(query);

				Double Amt_Include_BKCHG = Double.parseDouble(BMAMT) + Double.parseDouble(BANKCHARGE);
				System.out.println(query);
				Statement stmt2 = conn.createStatement();

				String query2 = "UPDATE " + DBNAME + ".HEAD_RECIPT SET  H_PYNO = '" + PAYER.toUpperCase()
						+ "', H_LOCATION = '" + LOCATION + "' , H_TYPE = '" + TYPE + "' , H_CUNO = 'HEAD' \n"
						+ "WHERE H_RNNO  = '" + GROUPID + "' \n" + "AND H_CONO  = '" + getCono + "' AND H_STS = 1 \n"
						+ "AND H_DIVI  = '" + getDivi + "'";

				System.out.println(query2);
				stmt2.executeUpdate(query2);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }", " complete.");
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

	public static String CREATEID(String getCono, String getDivi, String BMDATE, String ID, String PAYER,
			String BANKCHARGE, String OVERPAY,
			String CNDN, String TYPE, String LOCATION, String GROUPID, String BMAMT, String BANKTYPE) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		// remark = ConvertString.convertApostrophe(remark);
		// pono = ConvertString.convertApostrophe(pono);

		String lcode = "0";
		switch (BANKTYPE) {

			case "SCB":
				lcode = "1AA2105";
				break;

			case "KBANK":
				lcode = "1AA2110";
				break;

			case "BBL":
				lcode = "1AA2114";
				break;

			case "SCB_BILL":
				lcode = "1AA2283";
				break;

			case "KBANK_BILL":
				lcode = "1AA2250";
				break;

			case "BBL_BILL":
				lcode = "1AA2214";
				break;
			case "SCB_CUR":
				lcode = "1AA2286";
				break;
			case "KBANK_CUR":
				lcode = "1AA2250";
				break;
			case "BBL_CUR":
				lcode = "1AA22140";
				break;

			case "SCB_MMN":
				lcode = "1AA2286";
				break;

			case "BBL_QR":
				lcode = "1223098219";
				break;

		}

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String BMDATESTR = BMDATE.substring(2, 4);

				int yearint = Integer.parseInt(BMDATESTR) + 43;

				String sqlhead = "";

				String BM_FNNO = "";

				String sqlFNNO = "SELECT CASE WHEN CAST(MAX(H_RNNO) AS DECIMAL(10,0)) > 0 THEN CAST(MAX(H_RNNO) AS DECIMAL(10,0)) + 1\r\n"
						+ "                           ELSE CAST(('" + yearint
						+ "' || '000001') AS DECIMAL(10,0)) END AS THORNO\r\n"
						+ "                           FROM " + DBNAME + ".HEAD_RECIPT\r\n"
						+ "                           WHERE H_CONO  = '" + getCono + "'\r\n"
						+ "                           AND H_DIVI  = '" + getDivi + "'\r\n"
						+ "                           AND SUBSTRING(H_RNNO,0,3) = '" + yearint + "'";
				System.out.println(sqlFNNO);

				ResultSet mRes3 = stmt.executeQuery(sqlFNNO);
				System.out.println(sqlFNNO);

				while (mRes3.next()) {
					BM_FNNO = mRes3.getString(1);
				}

				sqlhead = "INSERT INTO " + DBNAME + ".HEAD_RECIPT\n" + "(H_CONO, H_DIVI, H_CUNO, H_RCNO, \n"
						+ " H_RNNO, H_PYNO , H_STS , H_LOCATION , H_TYPE)\n" + "VALUES('" + getCono + "', '" + getDivi
						+ "','HEAD', 9 || "
						+ BM_FNNO + " ,  \n" + " '" + BM_FNNO + "' ,'" + PAYER.trim().toUpperCase() + "' , '1' , '"
						+ LOCATION + "' , '" + TYPE + "'\n" + " \n" + " )";
				System.out.println(sqlhead);
				stmt.execute(sqlhead);

				String sql2 = "UPDATE " + DBNAME + ".BANK_MAPPING\n" + "SET   BM_CNDN = '"
						+ CNDN.trim() + "' , BM_OVPAY = '" + OVERPAY.trim() + "' ,BM_BKCHARGE = '" + BANKCHARGE.trim()
						+ "' ,BM_CUNO = UPPER('" + PAYER.trim() + "') ,BM_FNNO = '" + BM_FNNO.trim() + "' \n"
						+ "WHERE BM_CONO = '" + getCono + "'\n" + "AND BM_DIVI  = '" + getDivi + "'\n"
						+ "AND BM_DATE  = '" + BMDATE + "'\n"
						+ "AND BM_ID = '" + ID.trim() + "' ";
				System.out.println(sql2);

				stmt.execute(sql2);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message order }" + BM_FNNO, " complete.");
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

	///////////////////////////////////// BANK MAPPING
	///////////////////////////////////// //////////////////////////////////

	public static String updateOrderHead(String cono, String divi, String orderno, String orderdate, String delidate,
			String round, String pricelist, String ordertype, String whs, String status, String type, String remark,
			String pono, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		remark = ConvertString.convertApostrophe(remark);
		pono = ConvertString.convertApostrophe(pono);

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THORDA = '" + orderdate + "' \n"
						+ ", THDEDA = '" + delidate + "' \n" + ", THROUN = '" + round + "' \n" + ", THPRIC = '"
						+ pricelist + "' \n" + ", THORTY = '" + ordertype + "' \n" + ", THWHLO = '" + whs + "' \n"
						+ ", THREM1 = '" + remark + "' \n" + ", THREM2 = '" + pono + "' \n" + ", THSTAS = '" + status
						+ "' \n" + ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + ", THENUS = '" + userid
						+ "' \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
						+ "AND THORNO = '" + orderno + "'";
				// System.out.println("updateOrderHead\n" + query);
				stmt.execute(query);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", type + " complete.");
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

	public static String updateStatusOrderHead(String cono, String divi, String orderno, String status, String userid)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THSTAS = '" + status + "' \n"
						+ ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + ", THENUS = '" + userid + "' \n"
						+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THORNO = '"
						+ orderno + "'";
				// System.out.println("updateOrderHead\n" + query);
				stmt.execute(query);
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

	public static String updateStatusOrderHeadPrintCO(String cono, String divi, String fromco, String toco,
			String updatestatus, String fromstatus, String tostatus, String credit, String auth, String userid)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				if (auth.equals("1")) {

					String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THSTAS = '" + updatestatus
							+ "' \n" + ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + ", THENUS = '"
							+ userid + "' \n" + "WHERE THORNO IN (SELECT THORNO \n" + "FROM " + DBNAME
							+ ".M3_TAKEORDERHEAD a, " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT b, " + DBM3NAME
							+ ".OOHEAD c \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "'  \n"
							+ "AND b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n"
							+ "AND b.TDCONU BETWEEN '" + fromco + "' AND '" + toco + "' \n" + "AND (c.OAORST = '"
							+ fromstatus + "' AND c.OAORSL = '" + tostatus + "') \n" + "AND c.OAOBLC IN (" + credit
							+ ") \n" + "AND c.OACONO = THCONO \n" + "AND c.OAORNO = b.TDCONU \n"
							// + "AND c.OARESP = '"+userid+"' \n"
							+ "GROUP BY THORNO)";
					// System.out.println("updateOrderHead\n" + query);
					stmt.execute(query);

				} else {

					String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THSTAS = '" + updatestatus
							+ "' \n" + ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + ", THENUS = '"
							+ userid + "' \n" + "WHERE THORNO IN (SELECT THORNO \n" + "FROM " + DBNAME
							+ ".M3_TAKEORDERHEAD a, " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT b, " + DBM3NAME
							+ ".OOHEAD c \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
							+ "AND b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n" + "AND b.TDORNO = THORNO \n"
							+ "AND b.TDCONU BETWEEN '" + fromco + "' AND '" + toco + "' \n" + "AND (c.OAORST = '"
							+ fromstatus + "' AND c.OAORSL = '" + tostatus + "') \n" + "AND c.OAOBLC IN (" + credit
							+ ") \n" + "AND c.OACONO = THCONO \n" + "AND c.OAORNO = b.TDCONU \n" + "AND c.OARESP = '"
							+ userid + "' \n" + "GROUP BY THORNO)";
					// System.out.println("updateOrderHead\n" + query);
					stmt.execute(query);

				}

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

	public static String updateStatusOrderHeadPrintCOV2(String cono, String divi, String fromco, String toco,
			String updatestatus, String fromstatus, String tostatus, String credit, String auth, String userid)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				if (auth.equals("1")) {

					String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THSTAS = '" + updatestatus
							+ "' \n" + ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + ", THENUS = '"
							+ userid + "' \n" + "WHERE THORNO IN (SELECT THORNO \n" + "FROM " + DBNAME
							+ ".M3_TAKEORDERHEAD a, " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT b, " + DBM3NAME
							+ ".OOHEAD c \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "'  \n"
							+ "AND THSTAS = '92' \n" + "AND b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n"
							+ "AND b.TDORNO = THORNO \n" + "AND b.TDCONU BETWEEN '" + fromco + "' AND '" + toco + "' \n"
							+ "AND (c.OAORST = '" + fromstatus + "' AND c.OAORSL = '" + tostatus + "') \n"
							+ "AND c.OAOBLC IN (" + credit + ") \n" + "AND c.OACONO = THCONO \n"
							+ "AND c.OAORNO = b.TDCONU \n"
							// + "AND c.OARESP = '"+userid+"' \n"
							+ "GROUP BY THORNO)";
					// System.out.println("updateOrderHeadV2\n" + query);
					stmt.execute(query);

				} else {

					String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THSTAS = '" + updatestatus
							+ "' \n" + ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + ", THENUS = '"
							+ userid + "' \n" + "WHERE THORNO IN (SELECT THORNO \n" + "FROM " + DBNAME
							+ ".M3_TAKEORDERHEAD a, " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT b, " + DBM3NAME
							+ ".OOHEAD c \n" + "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n"
							+ "AND THSTAS = '92' \n" + "AND b.TDCONO = THCONO \n" + "AND b.TDDIVI = THDIVI \n"
							+ "AND b.TDORNO = THORNO \n" + "AND b.TDCONU BETWEEN '" + fromco + "' AND '" + toco + "' \n"
							+ "AND (c.OAORST = '" + fromstatus + "' AND c.OAORSL = '" + tostatus + "') \n"
							+ "AND c.OAOBLC IN (" + credit + ") \n" + "AND c.OACONO = THCONO \n"
							+ "AND c.OAORNO = b.TDCONU \n" + "AND c.OARESP = '" + userid + "' \n" + "GROUP BY THORNO)";
					// System.out.println("updateOrderHeadV2\n" + query);
					stmt.execute(query);

				}

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

	public static String updateStatusOrderHeadInvNumber(String cono, String divi, String orderno, String status)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THSTAS = '" + status + "' \n"
						+ ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + "WHERE THCONO = '" + cono
						+ "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THORNO = '" + orderno + "'";
				// System.out.println("updateOrderHead\n" + query);
				stmt.execute(query);
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

	public static String updateStatusOrderDetail(String cono, String divi, String orderno, String status)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERDETAIL \n" + "SET TDSTAS = '" + status + "' \n"
						+ ", TDENDA = CURRENT DATE \n" + ", TDENTI = CURRENT TIME \n" + "WHERE TDCONO = '" + cono
						+ "' \n" + "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '" + orderno + "' \n"
						+ "AND TDIQTY > 0";
				// System.out.println("updateStatusOrderDetail\n" + query);
				stmt.execute(query);

				if (status.equals("10")) {
					InsertData.addItemDetailToSupport(cono, divi, orderno);
				}

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

	public static String updateItemDetail(String cono, String divi, String orderno, String line, String qty,
			String unit, String remark, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				if (remark == null) {
					remark = "";
				}

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERDETAIL \n" + "SET TDIQTY = '" + qty + "' \n"
						+ ", TDUNIT = '" + unit + "' \n" + ", TDREM1 = '" + remark + "' \n"
						+ ", TDENDA = CURRENT DATE \n" + ", TDENTI = CURRENT TIME \n" + ", TDENUS = '" + userid + "' \n"
						+ "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '"
						+ orderno + "' \n" + "AND TDLINE = '" + line + "'";
				// System.out.println("updateOrderDatail\n" + query);
				stmt.execute(query);
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

	public static String updateItemDetailUpdate(String cono, String divi, String orderno, String line, String qty,
			String remark, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				if (qty == null) {
					qty = "0";
				}

				if (remark == null) {
					remark = "";
				}

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERDETAIL \n" + "SET TDIQTY = '" + qty + "' \n"
						+ ", TDREM1 = '" + remark + "' \n" + ", TDENDA = CURRENT DATE \n" + ", TDENTI = CURRENT TIME \n"
						+ ", TDENUS = '" + userid + "' \n" + "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '"
						+ divi + "' \n" + "AND TDORNO = '" + orderno + "' \n" + "AND TDLINE = '" + line + "'";
				// System.out.println("updateItemDetailUpdate\n" + query);
				stmt.execute(query);
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

	public static String updateItemDetailSupport(String cono, String divi, String orderno, String line, String qty,
			String unit, String remark, String remark2, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				if (remark == null) {
					remark = "";
				}

				if (remark2 == null) {
					remark2 = "";
				}

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" + "SET TDIQTY = '" + qty + "' \n"
						+ ", TDUNIT = '" + unit + "' \n"
						// + ", TDREM1 = '" + remark + "' \n"
						+ ", TDREM1 = UPPER('" + remark + "') \n" + ", TDREM2 = UPPER('" + remark2 + "') \n"
						+ ", TDENDA = CURRENT DATE \n" + ", TDENTI = CURRENT TIME \n" + ", TDENUS = '" + userid + "' \n"
						+ "WHERE TDCONO = '" + cono + "' \n" + "AND TDDIVI = '" + divi + "' \n" + "AND TDORNO = '"
						+ orderno + "' \n" + "AND TDLINE = '" + line + "'";
				// System.out.println("updateOrderDatail\n" + query);
				stmt.execute(query);
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

	// public static String updateCOOrderDetail(String cono, String divi, String
	// orderno, String conumber, String status)
	// throws Exception {
	//
	// JSONObject mJsonObj = new JSONObject();
	//
	// Connection conn = ConnectDB2.doConnect();
	// try {
	// if (conn != null) {
	//
	// Statement stmt = conn.createStatement();
	// String query = "UPDATE "+DBNAME+".M3_TAKEORDERDETAIL \n"
	// + "SET TDCONU = '" + conumber + "' \n"
	// + ", TDSTAS = '" + status + "' \n"
	// + "WHERE TDCONO = '" + cono + "' \n"
	// + "AND TDDIVI = '" + divi + "' \n"
	// + "AND TDORNO = '" + orderno + "' \n"
	// + "AND TDSTAS = '10'";
	// System.out.println("updateOrderDatail\n" + query);
	// stmt.execute(query);
	// return mJsonObj.toString();
	//
	// } else {
	// System.err.println("Server can't connect.");
	// }
	//
	// } catch (SQLException sqle) {
	// throw sqle;
	// } catch (Exception e) {
	// e.printStackTrace();
	// if (conn != null) {
	// conn.close();
	// }
	// throw e;
	// } finally {
	// if (conn != null) {
	// conn.close();
	// }
	// }
	//
	// return null;
	//
	// }

	public static String updateCONumberItemDetailSupport(String cono, String divi, String orderno, String conumber,
			String status) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" + "SET TDCONU = '" + conumber
						+ "' \n" + ", TDSTAS = '" + status + "' \n" + ", TDENDA = CURRENT DATE \n"
						+ ", TDENTI = CURRENT TIME \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '" + divi
						+ "'  \n" + "AND TDORNO = '" + orderno + "' \n" + "AND TDSTAS = '10'";
				// System.out.println("updateCOOrderDetailSupport\n" + query);
				stmt.execute(query);
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

	public static String updateInvNumberItemDetailSupport(String cono, String divi) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				String orderno = "", conumber = "", invnumber = "", statushead = "97", statusdetail = "20";
				List<String> getListInvNumber = SelectData.getListInvNumber(cono, divi);

				for (int i = 0; i < getListInvNumber.size(); i++) {
					String[] getDataInvNumber = getListInvNumber.get(i).split(";");
					// System.out.println(getDataInvNumber.get(i));

					orderno = getDataInvNumber[0].trim();
					conumber = getDataInvNumber[1].trim();
					invnumber = getDataInvNumber[3].trim();

					// System.out.println(orderno + " : " + conumber + " : " + invnumber);

					updateStatusOrderHeadInvNumber(cono, divi, orderno, statushead);

					Statement stmt = conn.createStatement();
					String query = "UPDATE " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" + "SET TDIVNO = '" + invnumber
							+ "' \n" + ", TDSTAS = '" + statusdetail + "' \n" + ", TDENDA = CURRENT DATE \n"
							+ ", TDENTI = CURRENT TIME \n" + "WHERE TDCONO = '" + cono + "'  \n" + "AND TDDIVI = '"
							+ divi + "'  \n" + "AND TDORNO = '" + orderno + "' \n" + "AND TDCONU = '" + conumber
							+ "' \n" + "AND TDSTAS = '15'";
					// System.out.println("updateInvNumberItemDetailSupport\n" + query);
					stmt.execute(query);

					mJsonObj.put("result", "ok");

				}

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

	public static String updateUser(String cono, String divi, String username, String status) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBM3NAME + ".CMNUSR \n" + "SET JUFRF8 = '" + status + "' \n"
						+ "WHERE JUUSID = '"
						+ username + "' \n" + "AND JUFRF7 = 'SALESUP' ";
				// System.out.println("updateOrderDatail\n" + query);
				stmt.execute(query);
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

	public static String updateOrderHeadCO(String cono, String divi, String orderno, String delidate, String userid)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();

		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBNAME + ".M3_TAKEORDERHEAD \n" + "SET THDEDA = '" + delidate + "' \n"
						+ ", THENDA = CURRENT DATE \n" + ", THENTI = CURRENT TIME \n" + ", THENUS = '" + userid + "' \n"
						+ "WHERE THCONO = '" + cono + "' \n" + "AND THDIVI = '" + divi + "' \n" + "AND THORNO = '"
						+ orderno + "'";
				// System.out.println("updateOrderHead\n" + query);
				stmt.execute(query);

				mJsonObj.put("result", "ok");
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

	public static String resetAuthTakeorder(String cono, String divi, String status) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBM3NAME + ".CMNUSR \n" + "SET JUFRF8 = '" + status + "' \n"
						+ "WHERE JUFRF7 = 'SALESUP'";
				// System.out.println("resetAuthTakeorder\n" + query);
				stmt.execute(query);
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

	public static String updateODHEAD(String cono, String divi, String orderno, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBM3NAME + ".ODHEAD a \n" + "SET UACHID = '" + userid + "' \n"
						+ "WHERE UACONO = '" + cono + "' \n" + "AND UADIVI = '" + divi + "' \n" + "AND UAORNO = '"
						+ orderno + "'";
				// System.out.println("updateODHEAD\n" + query);
				stmt.execute(query);
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

	public static String updateOOHEAD(String cono, String divi, String orderno, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();
				String query = "UPDATE " + DBM3NAME + ".OOHEAD \n" + "SET OACHID = '" + userid + "' \n"
						+ "WHERE OACONO = '" + cono + "' \n" + "AND OADIVI = '" + divi + "' \n" +
						// "AND OARESP = '"+userid+"' \n" +
						"AND OAORNO = '" + orderno + "'";
				// System.out.println("updateOOHEAD\n" + query);
				stmt.execute(query);
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
