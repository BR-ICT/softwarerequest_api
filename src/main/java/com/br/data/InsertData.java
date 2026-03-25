package com.br.data;

import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.sql.ResultSet; // <--- สำคัญตรงนี้

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.sql.PreparedStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONArray;

import com.br.connection.ConnectDB2;
import com.br.utility.Constant;
import com.br.utility.ConvertString;
import com.br.utility.FileUtillity;
import com.br.utility.HttpConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import groovy.ui.Console;

import java.util.HashMap;
import java.util.Map;

public class InsertData {
	
	private static final String UPLOAD_FOLDER = "D:/uploads/";


	private static final Logger logger = LogManager.getLogger(InsertData.class);

	protected static String DBNAME = Constant.DBNAME;
	protected static String DBM3NAME = Constant.DBM3NAME;

	public static String SR_HEAD = Constant.SR_HEAD;
	public static String SR_DETAIL = Constant.SR_DETAIL;
	public static String SR_APPROVE = Constant.SR_APPROVE;
	public static String SR_FLOW = Constant.SR_FLOW;
	public static String SR_GROUP = Constant.SR_GROUP;

	// protected static String DBNAMEPP = "BRLDTA0100";
	// protected static String DBNAMEPP = "BRLDTABK01";
	protected static String DBNAMEPP = "" + DBNAME + "";

	static DecimalFormat df0 = new DecimalFormat("0");
	static DecimalFormat df2 = new DecimalFormat("#.##");
	static DecimalFormat df3 = new DecimalFormat("#.###");
	static DecimalFormat df4 = new DecimalFormat("#.####");

	////////////////////////// BANKMAPPING ////////////////////////////////

	public static JSONObject uploadTempFiles(String base64FileData, String username, String depthead) throws Exception {
		JSONObject result = new JSONObject();

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = ConnectDB2.doConnect();

			// Decode base64 ไฟล์
			String base64Data = base64FileData.contains(",") ? base64FileData.split(",")[1] : base64FileData;
			byte[] fileBytes = Base64.getDecoder().decode(base64Data);

			// ตั้งชื่อไฟล์และ path (ปรับตามที่เก็บไฟล์จริง)
			String storedName = "upload_" + System.currentTimeMillis() + ".pdf";
			String uploadDir = "/your/upload/path/";
			String filePath = uploadDir + storedName;

			// เขียนไฟล์ลงดิสก์
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				fos.write(fileBytes);
			}

			int size = fileBytes.length;
			String mimeType = "application/pdf"; // ปรับถ้ารู้ชนิดไฟล์
			String originalName = "unknown.pdf"; // ถ้าได้ชื่อไฟล์จริงจากฝั่ง client ก็รับมาแทน

			// เตรียม SQL
			String sql = "INSERT INTO BRLDTABK01.SR_FILEUPLOAD (original_name, stored_name, path, mime_type, size, uploaded_by) VALUES (?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, originalName);
			pstmt.setString(2, storedName);
			pstmt.setString(3, filePath);
			pstmt.setString(4, mimeType);
			pstmt.setInt(5, size);
			pstmt.setString(6, username);

			pstmt.executeUpdate();

			result.put("result", "ok");
			result.put("stored_name", storedName);
			result.put("path", filePath);

		} catch (Exception e) {
			result.put("result", "nok");
			result.put("message", e.getMessage());
			throw e;
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}

		return result;
	}

	public static String CHECKTYPE(String username, String statemenType, String tableData, String getCono,
			String getDivi)
			throws Exception {
		logger.info("CHECKTYPE");

		System.out.println("---------------------------------------------------------------------xx");
		System.out.println("statemenType " + statemenType);

		System.out.println("---------------------------------------------------------------------xx");

		String LCODE = "-";

		switch (statemenType) {
			case "SCB_IntraDay":
				LCODE = "1AA2105";
				break;

			case "SCB_Historical":
				LCODE = "1AA2105";
				break;

			case "KBANK_IntraDay":
				LCODE = "1AA2110";
				break;

			case "KBANK_Historical":
				LCODE = "1AA2110";
				break;

			case "BBL_IntraDay":
				LCODE = "1AA2114";
				break;

			case "BBL_Historical":
				LCODE = "1AA2114";
				break;

			case "SCBBill_IntraDay":
				LCODE = "1AA2283";
				break;

			case "SCBBill_Historical":
				LCODE = "1AA2283";
				break;

			case "KBANKBill_IntraDay":
				LCODE = "1AA2250";
				break;

			case "KBANKBill_Historical":
				LCODE = "1AA2250";
				break;

			case "BBLBill_IntraDay":
				LCODE = "1AA2214";
				break;

			case "BBLBill_Historical":
				LCODE = "1AA2214";
				break;

			case "SCBMMN_IntraDay":
				LCODE = "1AA2286";
				break;

			case "SCBMMN_Historical":
				LCODE = "1AA2286";
				break;

			case "SCBCUR_IntraDay":
				LCODE = "1AA2286";
				break;

			case "SCBCUR_Historical":
				LCODE = "1AA2286";
				break;

			case "KBANKCUR_IntraDay":
				LCODE = "1AA2250";
				break;

			case "KBANKCUR_Historical":
				LCODE = "1AA2250";
				break;

			case "BBLCUR_IntraDay":
				LCODE = "1AA2214";
				break;

			case "BBLCUR_Historical":
				LCODE = "1AA2214";
				break;

			case "BBLQR_IntraDay":
				LCODE = "1AA2214";
				break;

			case "BBLQR_Historical":
				LCODE = "1AA2214";
				break;

			// -------- WTF ZONE --------

			case "SCB_IntraDay_WTF":
				LCODE = "1AA2201";
				break;

			case "SCB_Historical_WTF":
				LCODE = "1AA2201";
				break;

			case "BBL_IntraDay_WTF":
				LCODE = "1AA2202";
				break;

			case "BBL_Historical_WTF":
				LCODE = "1AA2202";
				break;

			// -------- NSD ZONE --------

			case "SCB_IntraDay_NSD":
				LCODE = "1AA2201";
				break;

			case "SCB_Historical_NSD":
				LCODE = "1AA2201";
				break;

			default:
				LCODE = statemenType;
				break;
		}

		// NORMAL

		if (statemenType.equalsIgnoreCase("SCB_IntraDay")) {

			System.out.println("SCB_IntraDay");

			return InsertData.SCB_IntraDay(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("SCB_Historical")) {

			System.out.println("SCB_Historical");

			return InsertData.SCB_Historical(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("KBANK_IntraDay")) {

			System.out.println("KBANK_IntraDay");

			return InsertData.KBANK_IntraDay(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("KBANK_Historical")) {

			System.out.println("KBANK_Historical");

			return InsertData.KBANK_Historical(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("BBL_IntraDay")) {

			System.out.println("BBL_Intraday");

			return InsertData.BBL_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("BBL_Historical")) {

			System.out.println("BBL_Historical");

			return InsertData.BBL_Historical(username, statemenType, tableData, LCODE);

		}

		// BILL PAYMENT

		else if (statemenType.equalsIgnoreCase("SCBBill_IntraDay")) {

			System.out.println("SCBBill_Intraday");

			return InsertData.SCBBill_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("SCBBill_Historical")) {

			System.out.println("SCBBill_Historical");

			return InsertData.SCBBill_Historical(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("KBANKBill_IntraDay")) {

			System.out.println("KBANKBill_Intraday");

			return InsertData.KBANKBill_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("KBANKBill_Historical")) {

			System.out.println("KBANKBill_Historical");

			return InsertData.KBANKBill_Historical(username, statemenType, tableData, LCODE);

		}

		else if (statemenType.equalsIgnoreCase("BBLBill_IntraDay")) {

			System.out.println("BBLBill_Intraday");

			return InsertData.BBLBill_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("BBLBill_Historical")) {

			System.out.println("BBLBill_Historical");

			return InsertData.BBLBill_Historical(username, statemenType, tableData, LCODE);

		}

		// MAE MANEE

		else if (statemenType.equalsIgnoreCase("SCBMMN_IntraDay")) {

			System.out.println("SCBMMN_Intraday");

			return InsertData.SCBMMN_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("SCBMMN_Historical")) {

			System.out.println("SCBMMN_Historical");

			return InsertData.SCBMMN_Historical(username, statemenType, tableData, LCODE);

		}

		// CURRENT

		else if (statemenType.equalsIgnoreCase("SCBCUR_IntraDay")) {

			System.out.println("SCBCUR_Intraday");

			return InsertData.SCBCUR_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("SCBCUR_Historical")) {

			System.out.println("SCBCUR_Historical");

			return InsertData.SCBCUR_Historical(username, statemenType, tableData, LCODE);

		}

		else if (statemenType.equalsIgnoreCase("KBANKCUR_IntraDay")) {

			System.out.println("KBANKCUR_Intraday");

			return InsertData.KBANKCUR_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("KBANKCUR_Historical")) {

			System.out.println("KBANKCUR_Historical");

			return InsertData.KBANKCUR_Historical(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("BBLCUR_IntraDay")) {

			System.out.println("BBLCUR_Intraday");

			return InsertData.BBLCUR_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("BBLCUR_Historical")) {

			System.out.println("BBLCUR_Historical");

			return InsertData.BBLCUR_Historical(username, statemenType, tableData, LCODE);

		}

		// QR

		else if (statemenType.equalsIgnoreCase("BBLQR_IntraDay")) {

			System.out.println("BBLQR_Intraday");

			return InsertData.BBLQR_Intraday(username, statemenType, tableData, LCODE);

		} else if (statemenType.equalsIgnoreCase("BBLQR_Historical")) {

			System.out.println("BBLQR_Historical");

			return InsertData.BBLQR_Historical(username, statemenType, tableData, LCODE);

		}

		///// --------- Winthai zone --------

		else if (statemenType.equalsIgnoreCase("SCB_IntraDay_WTF")) {

			System.out.println("SCB_IntraDay_WTF");

			return InsertData.SCB_IntraDay_WTF(username, statemenType, tableData, LCODE);

		}

		else if (statemenType.equalsIgnoreCase("SCB_Historical_WTF")) {

			System.out.println("SCB_Historical_WTF");

			return InsertData.SCB_Historical_WTF(username, statemenType, tableData, LCODE);

		}

		else if (statemenType.equalsIgnoreCase("BBL_IntraDay_WTF")) {

			System.out.println("BBL_IntraDay_WTF");

			return InsertData.BBL_IntraDay_WTF(username, statemenType, tableData, LCODE);

		}

		else if (statemenType.equalsIgnoreCase("BBL_Historical_WTF")) {

			System.out.println("BBL_Historical_WTF");

			return InsertData.BBL_Historical_WTF(username, statemenType, tableData, LCODE);

		}

		///// --------- NSD zone --------

		else if (statemenType.equalsIgnoreCase("SCB_IntraDay_NSD")) {

			System.out.println("SCB_IntraDay_NSD");

			return InsertData.SCB_IntraDay_NSD(username, statemenType, tableData, LCODE);

		}

		else if (statemenType.equalsIgnoreCase("SCB_Historical_NSD")) {

			System.out.println("SCB_Historical_NSD");

			return InsertData.SCB_Historical_NSD(username, statemenType, tableData, LCODE);

		}

		else {
			System.out.println("NONONONONO");

		}

		return "null";

	}
	
	
	
	
	
	//////////////////////////////
	
	
	/*
	   public static String saveFilesToServerAndDB(Part[] files, String username) {
	        JSONObject mJsonObj = new JSONObject();
	        Connection conn = null;
	        PreparedStatement pstmt = null;

	        try {
	            conn = ConnectDB2.doConnect();

	            String insertSQL = "INSERT INTO " + DBNAME + ".UPLOADED_FILES(filename, created_by, created_at) "
	                             + "VALUES(?, ?, CURRENT_TIMESTAMP)";
	            pstmt = conn.prepareStatement(insertSQL);

	            for (Part filePart : files) {
	                String originalFileName = getFileName(filePart);
	                if (originalFileName == null || originalFileName.isEmpty()) continue;

	                String savedFileName = System.currentTimeMillis() + "_" + originalFileName;
	                File fileToSave = new File(UPLOAD_FOLDER + savedFileName);

	                try (InputStream input = filePart.getInputStream()) {
	                    Files.copy(input, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
	                }

	                // insert ชื่อไฟล์ลง DB
	                pstmt.setString(1, savedFileName);
	                pstmt.setString(2, username);
	                pstmt.executeUpdate();
	            }

	            mJsonObj.put("result", "ok");
	            mJsonObj.put("message", files.length + " ไฟล์อัปโหลดและบันทึกลง DB สำเร็จ!");
	            logger.info(files.length + " files saved successfully.");
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            try { mJsonObj.put("result", "nok"); mJsonObj.put("message", e.getMessage()); } catch (Exception ex) {}
	        } finally {
	            try { if (pstmt != null) pstmt.close(); } catch (Exception e) { logger.error(e.getMessage(), e); }
	            try { if (conn != null) conn.close(); } catch (Exception e) { logger.error(e.getMessage(), e); }
	        }

	        return mJsonObj.toString();
	    }

	    private static String getFileName(Part part) {
	        String contentDisp = part.getHeader("content-disposition");
	        if (contentDisp == null) return null;
	        for (String cd : contentDisp.split(";")) {
	            if (cd.trim().startsWith("filename")) {
	                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
	            }
	        }
	        return null;
	    }

	
	*/ 
	///////////////////////////////

	// NORMAL

	public static String SCB_IntraDay(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {
			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE ,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_TRANSAC, BM_BRANCH, BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE + "','10', '101', 'SCB', '3312431686', ?,\r\n"
					+ "                                ?, ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					// แทนที่การเรียก getString ด้วยการใช้ toString เพื่อรองรับทุกประเภท
					pstmt.setString(1, row.get("Date").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(2, row.get("Time").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(3, row.get("Cheque Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(4, row.get("Transaction Code").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(5, row.get("Branch Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(6, row.get("Amount").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด

					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String SCB_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_Historical");
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println("Received Data: " + tableData);

		System.out.println("statemenType Data: " + statemenType);

		// กำหนดเวลาปัจจุบันในรูปแบบที่ต้องการ

		try {
			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_TRANSAC, BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE + "','10', '101', 'SCB', '3312431686', ?,\r\n"
					+ "                                 ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					// แทนที่การเรียก getString ด้วยการใช้ toString เพื่อรองรับทุกประเภท
					pstmt.setString(1, row.get("Date").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(2, row.get("Time").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(3, row.get("Cheque Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(4, row.get("Transaction Code").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(5, row.get("Credit Amount").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด

					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Description")) {
						pstmt.setString(6, row.get("Description").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	// KBANK

	public static String KBANK_IntraDay(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("KBANK_IntraDay");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;

		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);

		try {
			conn = ConnectDB2.doConnect();

			String insertQuery = " INSERT INTO " + DBNAMEPP + ".BANK_MAPPING " +
					"(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE, BM_TIME, BM_CHANEL, BM_AMOUNT, " +
					"BM_USER, BM_STATUS, BM_TIME1, BM_DESC) " +
					"VALUES('" + LCODE + "','10', '101', 'KBANK', '3402314428', ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(insertQuery);
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					pstmt.setString(1, row.optString("Date", "-"));
					pstmt.setString(2, row.optString("Time", "-"));
					pstmt.setString(3, row.optString("Channel", "-"));
					pstmt.setString(4, row.optString("Deposit", "-"));
					pstmt.setString(5, username);
					pstmt.setString(6, "10");
					pstmt.setString(7, formattedDateTime);
					pstmt.setString(8, row.optString("Description", "-"));

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String KBANK_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("KBANK_Historical");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		PreparedStatement pstmt = null;

		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);

		try {
			conn = ConnectDB2.doConnect();

			String insertQuery = " INSERT INTO " + DBNAMEPP + ".BANK_MAPPING " +
					"(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE, BM_TIME, BM_CHANEL, BM_AMOUNT, " +
					"BM_USER, BM_STATUS, BM_TIME1, BM_DESC) " +
					"VALUES('" + LCODE + "','10', '101', 'KBANK', '3402314428', ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(insertQuery);
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					pstmt.setString(1, row.optString("Date", "-"));
					pstmt.setString(2, row.optString("Time_Eff_Date", "-"));
					pstmt.setString(3, row.optString("Channel", "-"));
					pstmt.setString(4, row.optString("Deposit", "-"));
					pstmt.setString(5, username);
					pstmt.setString(6, "10");
					pstmt.setString(7, formattedDateTime);
					pstmt.setString(8, row.optString("Description", "-"));

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	// BBL

	public static String BBL_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHANEL ,REF2 ,\r\n"
					+ "			BM_BRANCH  , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'BBL', '1227360581',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					if (row.has("Value_Date")) {
						pstmt.setString(1, row.get("Value_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Tran_Date")) {
						pstmt.setString(2, row.get("Tran_Date").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Channel")) {
						pstmt.setString(3, row.get("Channel").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Terminal_Id")) {
						pstmt.setString(4, row.get("Terminal_Id").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Branch")) {
						pstmt.setString(5, row.get("Branch").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Credit")) {
						pstmt.setString(6, row.get("Credit").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String BBL_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHANEL ,REF2 ,\r\n"
					+ "			BM_BRANCH  , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'BBL', '1227360581',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					if (row.has("Value_Date")) {
						pstmt.setString(1, row.get("Value_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Tran_Date")) {
						pstmt.setString(2, row.get("Tran_Date").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Channel")) {
						pstmt.setString(3, row.get("Channel").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Terminal_Id")) {
						pstmt.setString(4, row.get("Terminal_Id").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Branch")) {
						pstmt.setString(5, row.get("Branch").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Credit")) {
						pstmt.setString(6, row.get("Credit").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	/////////// BILL PAYMENT//////////////

	public static String inserteditTEST(String vData, String username, String depthead) throws Exception {
		logger.info("insertRQ");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();
			Statement stmt2 = conn.createStatement();

			// สร้าง currentID จาก query
			String idQuery = "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(SERVICE_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
					+ "FROM " + DBNAME + "." + SR_DETAIL + " \n"
					+ "WHERE SUBSTR(SERVICE_ID, 1, 2) = '25'";
			logger.debug("ID Query: " + idQuery);

			rs = stmt.executeQuery(idQuery);
			String currentID = null;

			if (rs.next()) {
				currentID = rs.getString("CURRENT_ID");
			}

			if (currentID != null) {
				// insert ด้วย currentID
				String insertQuery = "INSERT INTO " + DBNAME + "." + SR_DETAIL + "  \n"
						+ "( json_data,SERVICE_ID,PROMGRAM_CODE,STATUS,DATE,TIME) \n"
						+ "VALUES ('" + vData + "','" + currentID + "','ITRQ', '20' ,CURRENT DATE ,CURRENT TIME)";
				logger.debug("Insert Query: " + insertQuery);

				java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
				String dateYYYYMMDD = new java.text.SimpleDateFormat("yyyyMMdd").format(currentTimestamp);

				String insertQueryHead = "INSERT INTO " + DBNAME + "." + SR_HEAD + "  \n"
						+ "( DOC_CODE,DOC_NO,REQUETER,CREATE_DATE,CREATE_TIME,STATUS,DEPTHEAD ,H_STATUS) \n"
						+ "VALUES ('ITRQ','" + currentID + "','" + username + "',CURRENT DATE ,CURRENT TIME, '20','"
						+ depthead + "',2)";
				logger.debug("Insert Query: " + insertQuery);

				/*
				 * String insertQueryTemp = "\r\n"
				 * + "INSERT INTO BRLDTABK01.Approve_Detail02 (\r\n"
				 * + "    ID,\r\n"
				 * + "    DOC_CODE,\r\n"
				 * + "    DOC_NO,\r\n"
				 * + "APPROVE,\r\n"
				 * + "APPROVE_DATE,\r\n"
				 * + "STATUS,\r\n"
				 * + "STS_DESC,\r\n"
				 * + "TIME_ST \r\n"
				 * + ")\r\n"
				 * + "SELECT\r\n"
				 * + "    STATUS,\r\n"
				 * + "    DOC_CODE,\r\n"
				 * + "    '" + currentID + "',\r\n"
				 * + "    'PP',\r\n"
				 * + "	'-',\r\n"
				 * + "	STATUS,\r\n"
				 * + "    'Wait for approve',\r\n"
				 * + "    '-'\r\n"
				 * + "FROM BRLDTABK01.flow_master\r\n"
				 * 
				 * + "WHERE DOC_CODE = 'ITRQ'\r\n"
				 * + "";
				 */
				stmt.executeUpdate(insertQuery);

				stmt.executeUpdate(insertQueryHead);

				// stmt.executeUpdate(insertQueryTemp);

				String recursiveQuery = "WITH RECURSIVE filtered_master AS (\r\n"
						+ "SELECT *\r\n"
						+ "FROM " + DBNAME + "." + SR_FLOW + "\r\n"
						+ "WHERE DOC_CODE = 'ITRQ'\r\n"
						+ "),\r\n"
						+ "filtered_group AS (\r\n"
						+ "SELECT *\r\n"
						+ "FROM " + DBNAME + "." + SR_GROUP + "\r\n"
						+ "WHERE WHS = 'A91'\r\n"
						+ "),\r\n"
						+ "joined_data AS (\r\n"
						+ "SELECT\r\n"
						+ "m.DOC_CODE,\r\n"
						+ "m.GROUP,\r\n"
						+ "m.SUBGROUP,\r\n"
						+ "m.STATUS,\r\n"
						+ "m.REMARK,\r\n"
						+ "g.NAME,\r\n"
						+ "ROW_NUMBER() OVER (\r\n"
						+ "PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP\r\n"
						+ "ORDER BY g.NAME\r\n"
						+ ") AS RN\r\n"
						+ "FROM filtered_master m\r\n"
						+ "JOIN filtered_group g\r\n"
						+ "ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP\r\n"
						+ "),\r\n"
						+ "concat_cte (\r\n"
						+ "DOC_CODE, GROUP_ID, SUBGROUP, STATUS, REMARK, RN, NAME_SERIAL\r\n"
						+ ") AS (\r\n"
						+ "SELECT\r\n"
						+ "DOC_CODE,\r\n"
						+ "GROUP,\r\n"
						+ "SUBGROUP,\r\n"
						+ "STATUS,\r\n"
						+ "REMARK,\r\n"
						+ "RN,\r\n"
						+ "NAME\r\n"
						+ "FROM joined_data\r\n"
						+ "WHERE RN = 1\r\n"
						+ "\r\n"
						+ "UNION ALL\r\n"
						+ "\r\n"
						+ "SELECT\r\n"
						+ "j.DOC_CODE,\r\n"
						+ "j.GROUP,\r\n"
						+ "j.SUBGROUP,\r\n"
						+ "j.STATUS,\r\n"
						+ "j.REMARK,\r\n"
						+ "j.RN,\r\n"
						+ "c.NAME_SERIAL || ',' || j.NAME\r\n"
						+ "FROM concat_cte c\r\n"
						+ "JOIN joined_data j\r\n"
						+ "ON c.DOC_CODE = j.DOC_CODE\r\n"
						+ "AND c.GROUP_ID = j.GROUP\r\n"
						+ "AND c.SUBGROUP = j.SUBGROUP\r\n"
						+ "AND j.RN = c.RN + 1\r\n"
						+ ")\r\n"
						+ "\r\n"
						+ "SELECT\r\n"
						+ "DOC_CODE,\r\n"
						+ "STATUS,\r\n"
						+ "REMARK,\r\n"
						+ "NAME_SERIAL\r\n"
						+ "FROM concat_cte c\r\n"
						+ "WHERE NOT EXISTS (\r\n"
						+ "SELECT 1\r\n"
						+ "FROM concat_cte c2\r\n"
						+ "WHERE\r\n"
						+ "c2.DOC_CODE = c.DOC_CODE\r\n"
						+ "AND c2.GROUP_ID = c.GROUP_ID\r\n"
						+ "AND c2.SUBGROUP = c.SUBGROUP\r\n"
						+ "AND c2.RN = c.RN + 1\r\n"
						+ ")\r\n"
						+ "ORDER BY STATUS";
				/*
				 * String recursiveQuery = "WITH RECURSIVE " +
				 * "filtered_master AS ( " +
				 * "  SELECT * FROM "+DBNAME+"."+SR_FLOW+" WHERE DOC_CODE = 'ITRQ' " +
				 * "), " +
				 * "filtered_group AS ( " +
				 * "  SELECT * FROM "+DBNAME+"."+SR_GROUP+" WHERE WHS = 'A91' " +
				 * "), " +
				 * "joined_data AS ( " +
				 * "  SELECT m.DOC_CODE, m.GROUP, m.SUBGROUP, m.STATUS, g.NAME, " +
				 * "         ROW_NUMBER() OVER (PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP ORDER BY g.NAME) AS RN "
				 * +
				 * "  FROM filtered_master m " +
				 * "  JOIN filtered_group g ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP "
				 * +
				 * "), " +
				 * "concat_cte (DOC_CODE, GROUP_ID, SUBGROUP, STATUS, RN, NAME_SERIAL) AS ( " +
				 * "  SELECT DOC_CODE, GROUP, SUBGROUP, STATUS, RN, NAME FROM joined_data WHERE RN = 1 "
				 * +
				 * "  UNION ALL " +
				 * "  SELECT j.DOC_CODE, j.GROUP, j.SUBGROUP, j.STATUS, j.RN, c.NAME_SERIAL || ':' || j.NAME "
				 * +
				 * "  FROM concat_cte c JOIN joined_data j " +
				 * "  ON c.DOC_CODE = j.DOC_CODE AND c.GROUP_ID = j.GROUP AND c.SUBGROUP = j.SUBGROUP AND j.RN = c.RN + 1 "
				 * +
				 * ") " +
				 * "SELECT DOC_CODE, STATUS, NAME_SERIAL FROM concat_cte c " +
				 * "WHERE NOT EXISTS ( " +
				 * "  SELECT 1 FROM concat_cte c2 " +
				 * "  WHERE c2.DOC_CODE = c.DOC_CODE AND c2.GROUP_ID = c.GROUP_ID AND c2.SUBGROUP = c.SUBGROUP AND c2.RN = c.RN + 1 "
				 * +
				 * ") ORDER BY STATUS";
				 * 
				 */

				logger.debug("PPPPPP : " + recursiveQuery);
				rs = stmt.executeQuery(recursiveQuery);

				while (rs.next()) {
					String docCode = rs.getString("DOC_CODE");
					String status = rs.getString("STATUS");
					String approve = rs.getString("NAME_SERIAL");
					String remark = rs.getString("REMARK");

					String insertDetail = "INSERT INTO " + DBNAME + "." + SR_APPROVE + " " +
							"( DOC_CODE, DOC_NO, APPROVE, APPROVE_DATE, STATUS, STS_DESC, TIME_ST, APPROVED_USER ,REMARK) "
							+
							"VALUES (" +
							"'" + docCode + "', " +
							"'" + currentID + "', " +
							"'" + approve + "', " +
							"'-', " +
							"'" + status + "', " +
							"'Wait for approve', " +
							"'-', " +
							"'-',  " +
							"'" + remark + "'" +
							")";
					stmt2.executeUpdate(insertDetail);
					logger.debug("xxxxxxin" + insertDetail);
				}

				String query2 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
						+ "SET  STS_DESC = 'Approved',APPROVE = '" + username + "' , TIME_ST = '" + currentTimestamp
						+ "',APPROVED_USER = 'PP', APPROVE_DATE = '" + dateYYYYMMDD +
						"' WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID + "' AND STATUS = '10' ";

				stmt2.executeUpdate(query2);

				String query222 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
						+ "SET APPROVE = '" + depthead + "' \n"
						+ "WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID + "' AND STATUS = '20'";

				stmt2.executeUpdate(query222);

				mJsonObj.put("result", "ok");
				mJsonObj.put("CURRENT_ID", currentID);
			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Cannot generate CURRENT_ID");
			}

			return currentID.toString();

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}

	public static String insertTEST(String vData, String username, String depthead) throws Exception {
		logger.info("insertRQ");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs9 = null;

		logger.debug("vData: " + vData);

		JSONObject obj = new JSONObject(vData);

		String company = obj.optString("company");
		String warehouse2 = obj.optString("warehouse");
		
		String vRemark2 = obj.optString("vUSRemark");
		

		String itemcode = obj.optString("vItemcode");
		String itemname = obj.optString("itemName");
		
		String programtype = obj.optString("programtype");
		
		String version = obj.optString("vVersion");
		
		logger.debug("vVersion: " + version);
		
		String checkVersion = SelectData.checkVersion("SRQ");
		   if (version == null || version.isEmpty() || !Objects.equals(checkVersion, version)) {
		    mJsonObj.put("result", "nok");
		    mJsonObj.put("message", "Can't create Service number, Please update your version to " + checkVersion + " :  "+version+" (Click F5 button).");
		    return mJsonObj.toString();

		   }
		
		
		
		
		

		logger.debug("ID programtype: " + programtype);

		logger.debug("company: " + company);
		logger.debug("warehouse2: " + warehouse2);
		logger.debug("vRemark: " + vRemark2);

		Map<String, String[]> companyMapping = new HashMap<>();
		companyMapping.put("10", new String[] { "10", "101" });
		companyMapping.put("600", new String[] { "600", "600" });
		companyMapping.put("500", new String[] { "500", "500" });
		// เพิ่มได้เรื่อยๆ เช่น
		// companyMapping.put("300", new String[] { "300", "301" });

		// ดึงข้อมูลตาม company
		String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
		String comcono = mapping[0];
		String comdivi = mapping[1];

		logger.debug("cono: " + comcono);
		logger.debug("divi: " + comdivi);

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();
			Statement stmt2 = conn.createStatement();

			// สร้าง currentID จาก query
			
			/* String idQuery = "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
			
					+ "FROM " + DBNAME + "." + SR_DETAIL + " \n"
					+ "WHERE SUBSTR(FDSRNO, 1, 2) = '25' AND FDCONO  = '" + comcono + "' AND　FDDIVI = '" + comdivi
					+ "' AND FDCODE ='ITRQ'  ";
			*/	
			
			String idQuery = "SELECT RIGHT(YEAR(CURRENT_DATE), 2)\r\n"
					+ "|| RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), RIGHT(YEAR(CURRENT_DATE),2) || '000000'), 3)) + 1), 6)\r\n"
					+ "AS CURRENT_ID\r\n"
					+ "FROM " + DBNAME + "." + SR_DETAIL + "\r\n"
					+ "WHERE SUBSTR(FDSRNO, 1, 2) = RIGHT(YEAR(CURRENT_DATE), 2)\r\n"
					+ "AND FDCONO = '" + comcono +"'\r\n"
					+ "AND FDDIVI = '" + comdivi + "'\r\n"
					+ "AND FDCODE = 'ITRQ'";

			/*
			 * String idQuery =
			 * "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(SERVICE_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
			 * + "FROM "+DBNAME+"."+SR_DETAIL+" \n"
			 * + "WHERE SUBSTR(SERVICE_ID, 1, 2) = '25'";
			 */
			logger.debug("ID Query: " + idQuery);

			rs = stmt.executeQuery(idQuery);
			
			String currentID = null;
			String fdtype = "1";

			if (rs.next()) {
				currentID = rs.getString("CURRENT_ID");
			}

			String getFDTYPEQuery = "SELECT RQTYPE  FROM BRLDTABK01.sr_requesttype\r\n"
					+ "WHERE RQCONO = '" + comcono + "'\r\n"
					+ "AND RQDIVI = '" + comdivi + "' AND RQNAME = '"+programtype+"'\r\n"
					+ "AND rqcode = 'ITRQ'";
		
			rs9 = stmt.executeQuery(getFDTYPEQuery);
			logger.debug("ID Query: " + getFDTYPEQuery);
			

			if (rs9.next()) {
				fdtype = rs9.getString("RQTYPE");
			}
			
			logger.debug("ID fdtype: " + fdtype);

			if (currentID != null) {
				// insert ด้วย currentID
				/*
				 * String insertQuery = "INSERT INTO "+DBNAME+"."+SR_DETAIL+"  \n"
				 * + "( json_data,SERVICE_ID,PROMGRAM_CODE,STATUS,DATE,TIME) \n"
				 * + "VALUES ('" + vData + "','" + currentID +
				 * "','ITMRQ', '10' ,CURRENT DATE ,CURRENT TIME)";
				 */

				String insertQuery = "INSERT INTO " + DBNAME + "." + SR_DETAIL + "\r\n"
						+ "(FDCONO,FDDIVI,FDTYPE,  FDDATA, FDSRNO,FDCODE, FDDSTA , FDENDA, FDENTI,FDENUS) \r\n"
						+ "VALUES ('" + comcono + "','" + comdivi + "','"+fdtype+"','" + vData + "','" + currentID
						+ "','ITRQ', '10', CURRENT DATE, CURRENT TIME ,'" + username.toString() + "')";

				logger.debug("Insert Query: " + insertQuery);

				java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
				String dateYYYYMMDD = new java.text.SimpleDateFormat("yyyyMMdd").format(currentTimestamp);

				/*
				 * String insertQueryHead = "INSERT INTO "+DBNAME+"."+SR_HEAD+"  \n"
				 * +
				 * "( DOC_CODE,DOC_NO,REQUETER,CREATE_DATE,CREATE_TIME,STATUS,DEPTHEAD ,H_STATUS) \n"
				 * + "VALUES ('ITRQ','" + currentID +
				 * "','"+username+"',CURRENT DATE ,CURRENT TIME, '10','"+depthead+"',1)";
				 */

				String insertQueryHead = "INSERT INTO " + DBNAME + "." + SR_HEAD + "\r\n"
						+ "(FHCONO,FHDIVI, FHCODE, FHSRNO,FHREQU ,FHENDA ,FHENTI,FHENUS ,FHREDA,FHHSTA ,FHDEPH , FHDSTA , FHDES1)\r\n"
						+ "VALUES ('" + comcono + "','" + comdivi + "','ITRQ', '" + currentID + "', '" + username
						+ "', CURRENT DATE , CURRENT TIME,'" + username + "' ,CURRENT DATE, 2, '" + depthead
						+ "', 10 , 'ITRQ-" + currentID + "-" + "" + programtype + "-"+itemname+"')";
				logger.debug("Insert Query: " + insertQueryHead);
				
				
				
				
				

				/*
				 * String insertQueryTemp = "\r\n"
				 * + "INSERT INTO BRLDTABK01.Approve_Detail02 (\r\n"
				 * + "    ID,\r\n"
				 * + "    DOC_CODE,\r\n"
				 * + "    DOC_NO,\r\n"
				 * + "APPROVE,\r\n"
				 * + "APPROVE_DATE,\r\n"
				 * + "STATUS,\r\n"
				 * + "STS_DESC,\r\n"
				 * + "TIME_ST \r\n"
				 * + ")\r\n"
				 * + "SELECT\r\n"
				 * + "    STATUS,\r\n"
				 * + "    DOC_CODE,\r\n"
				 * + "    '" + currentID + "',\r\n"
				 * + "    'PP',\r\n"
				 * + "	'-',\r\n"
				 * + "	STATUS,\r\n"
				 * + "    'Wait for approve',\r\n"
				 * + "    '-'\r\n"
				 * + "FROM BRLDTABK01.flow_master\r\n"
				 * 
				 * + "WHERE DOC_CODE = 'ITRQ'\r\n"
				 * + "";
				 */
				stmt.executeUpdate(insertQuery);

				stmt.executeUpdate(insertQueryHead);

				// stmt.executeUpdate(insertQueryTemp);
				
			
				String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM "+DBNAME+"."+SR_FLOW +"\r\n"
						+ "  WHERE PMCONO = '"+comcono+"'\r\n"
						+ "    AND PMDIVI = '"+comdivi+"'\r\n"
						+ "    AND PMCODE = 'ITRQ'\r\n"
						+ "),\r\n"
						+ "FILTERED_GROUP AS (\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM "+DBNAME+"."+SR_GROUP+"\r\n"
						+ "),\r\n"
						+ "JOINED_DATA AS (\r\n"
						+ "  SELECT\r\n"
						+ "    M.PMCONO,\r\n"
						+ "    M.PMDIVI,\r\n"
						+ "    M.PMCODE,\r\n"
						+ "    M.PMGROU,\r\n"
						+ "    M.PMSGRO,\r\n"
						+ "    M.PMSTAT,\r\n"
						+ "    M.PMDES1,\r\n"
						+ "    M.PMDES2,\r\n"
						+ "    M.PMDES3,        -- ⭐ เพิ่ม\r\n"
						+ "    M.PMDES4,        -- ⭐ เพิ่ม\r\n"
						+ "    G.GMUSER,\r\n"
						+ "    (\r\n"
						+ "      SELECT MIN(M2.PMSTAT)\r\n"
						+ "      FROM "+DBNAME+"."+SR_FLOW+" M2\r\n"
						+ "      WHERE M2.PMCONO = M.PMCONO\r\n"
						+ "        AND M2.PMDIVI = M.PMDIVI\r\n"
						+ "        AND M2.PMCODE = M.PMCODE\r\n"
						+ "        AND M2.PMSTAT > M.PMSTAT\r\n"
						+ "    ) AS NEXT_STAT,\r\n"
						+ "    ROW_NUMBER() OVER (\r\n"
						+ "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n"
						+ "      ORDER BY\r\n"
						+ "        CASE WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0 ELSE 1 END,\r\n"
						+ "        M.PMDES2 DESC,\r\n"
						+ "        G.GMUSER ASC\r\n"
						+ "    ) AS RN\r\n"
						+ "  FROM FILTERED_MASTER M\r\n"
						+ "  JOIN FILTERED_GROUP G\r\n"
						+ "    ON M.PMCONO = G.GMCONO\r\n"
						+ "   AND M.PMDIVI = G.GMDIVI\r\n"
						+ "   AND M.PMGROU = G.GMGROU\r\n"
						+ "   AND M.PMSGRO = G.GMSGRO\r\n"
						+ "),\r\n"
						+ "VACANT_FLAG AS (\r\n"
						+ "  SELECT\r\n"
						+ "    M.PMCONO,\r\n"
						+ "    M.PMDIVI,\r\n"
						+ "    M.PMCODE,\r\n"
						+ "    M.PMGROU,\r\n"
						+ "    M.PMSGRO,\r\n"
						+ "    CASE WHEN COUNT(G2.GMUSER) > 0 THEN 'Y' ELSE 'N' END AS SKIP_IF_VACANT\r\n"
						+ "  FROM FILTERED_MASTER M\r\n"
						+ "  LEFT JOIN "+DBNAME+"."+SR_GROUP+" G2\r\n"
						+ "    ON G2.GMCONO = M.PMCONO\r\n"
						+ "   AND G2.GMDIVI = M.PMDIVI\r\n"
						+ "   AND G2.GMGROU = M.PMGROU\r\n"
						+ "   AND G2.GMSGRO = M.PMSGRO\r\n"
						+ "   AND UPPER(TRIM(G2.GMUSER)) = 'VACANT'\r\n"
						+ "  GROUP BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMGROU, M.PMSGRO\r\n"
						+ "),\r\n"
						+ "CONCAT_CTE (\r\n"
						+ "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
						+ "  PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,   -- ⭐ เพิ่ม\r\n"
						+ "  RN, NAME_SERIAL\r\n"
						+ ") AS (\r\n"
						+ "  SELECT\r\n"
						+ "    PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
						+ "    PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,  -- ⭐ เพิ่ม\r\n"
						+ "    RN,\r\n"
						+ "    GMUSER AS NAME_SERIAL\r\n"
						+ "  FROM JOINED_DATA\r\n"
						+ "  WHERE RN = 1\r\n"
						+ "  UNION ALL\r\n"
						+ "  SELECT\r\n"
						+ "    J.PMCONO, J.PMDIVI, J.PMCODE, J.PMGROU, J.PMSGRO,\r\n"
						+ "    J.PMSTAT, J.PMDES1, J.PMDES2, J.PMDES3, J.PMDES4,  -- ⭐ เพิ่ม\r\n"
						+ "    J.RN,\r\n"
						+ "    C.NAME_SERIAL || ',' || J.GMUSER\r\n"
						+ "  FROM CONCAT_CTE C\r\n"
						+ "  JOIN JOINED_DATA J\r\n"
						+ "    ON C.PMCONO = J.PMCONO\r\n"
						+ "   AND C.PMDIVI = J.PMDIVI\r\n"
						+ "   AND C.PMCODE = J.PMCODE\r\n"
						+ "   AND C.PMGROU = J.PMGROU\r\n"
						+ "   AND C.PMSGRO = J.PMSGRO\r\n"
						+ "   AND J.RN = C.RN + 1\r\n"
						+ ")\r\n"
						+ "SELECT\r\n"
						+ "  C.PMCONO,\r\n"
						+ "  C.PMDIVI,\r\n"
						+ "  C.PMCODE,\r\n"
						+ "  C.PMGROU,\r\n"
						+ "  C.PMSGRO,\r\n"
						+ "  C.PMSTAT,\r\n"
						+ "  C.PMDES1,\r\n"
						+ "  C.PMDES2,\r\n"
						+ "  C.PMDES3 AS NEXT_STAT,\r\n"
						+ "  C.PMDES4 AS PREVIOUS_STAT,    \r\n"
						+ "  C.NAME_SERIAL,\r\n"
						+ "  V.SKIP_IF_VACANT\r\n"
						+ "FROM CONCAT_CTE C\r\n"
						+ "LEFT JOIN VACANT_FLAG V\r\n"
						+ "  ON C.PMCONO = V.PMCONO\r\n"
						+ " AND C.PMDIVI = V.PMDIVI\r\n"
						+ " AND C.PMCODE = V.PMCODE\r\n"
						+ " AND C.PMGROU = V.PMGROU\r\n"
						+ " AND C.PMSGRO = V.PMSGRO\r\n"
						+ "LEFT JOIN CONCAT_CTE C2\r\n"
						+ "  ON C2.PMCONO = C.PMCONO\r\n"
						+ " AND C2.PMDIVI = C.PMDIVI\r\n"
						+ " AND C2.PMCODE = C.PMCODE\r\n"
						+ " AND C2.PMGROU = C.PMGROU\r\n"
						+ " AND C2.PMSGRO = C.PMSGRO\r\n"
						+ " AND C2.RN = C.RN + 1\r\n"
						+ "WHERE C2.PMCONO IS NULL\r\n"
						+ "ORDER BY C.PMSTAT, C.PMGROU, C.PMSGRO\r\n"
						+ "";
				
				
				/*
				String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n"
						+ "  --  Step 1: Select all rows for PMCONO, PMDIVI, PMCODE='ITRQ' from SR_PROCESSMASTER\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM " + DBNAME + "." + SR_FLOW + "\r\n"
						+ "  WHERE PMCONO = '" + comcono + "'\r\n"
						+ "    AND PMDIVI = '" + comdivi + "'\r\n"
						+ "    AND PMCODE = 'ITRQ'\r\n"
						+ "),\r\n"
						+ "FILTERED_GROUP AS (\r\n"
						+ "  --  Step 2: Select all rows from SR_GROUPMASTER (no filter applied)\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM " + DBNAME + "." + SR_GROUP + "\r\n"
						+ "),\r\n"
						+ "JOINED_DATA AS (\r\n"
						+ "  --  Step 3: Join PROCESSMASTER and GROUPMASTER on CONO, DIVI, GROUP, SUBGROUP\r\n"
						+ "  -- Assign row numbers to handle duplicates per PMSTAT\r\n"
						+ "  SELECT\r\n"
						+ "    M.PMCONO,\r\n"
						+ "    M.PMDIVI,\r\n"
						+ "    M.PMCODE,\r\n"
						+ "    M.PMGROU,\r\n"
						+ "    M.PMSGRO,\r\n"
						+ "    M.PMSTAT,\r\n"
						+ "    M.PMDES1,\r\n"
						+ "    M.PMDES2,\r\n"
						+ "    G.GMUSER,\r\n"
						+ "    ROW_NUMBER() OVER (\r\n"
						+ "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n"
						+ "      ORDER BY\r\n"
						+ "        --  Priority 1: PMSGRO matches :TARGET_SUBGROUP\r\n"
						+ "        CASE\r\n"
						+ "          WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0\r\n"
						+ "          ELSE 1\r\n"
						+ "        END,\r\n"
						+ "        --  Priority 2: PMDES2 DESC (higher value preferred)\r\n"
						+ "        M.PMDES2 DESC,\r\n"
						+ "        --  Tie-breaker: PMSGRO alphabetically\r\n"
						+ "        M.PMSGRO ASC\r\n"
						+ "    ) AS RN\r\n"
						+ "  FROM FILTERED_MASTER M\r\n"
						+ "  JOIN FILTERED_GROUP G\r\n"
						+ "    ON M.PMCONO = G.GMCONO         -- ✅ Match company\r\n"
						+ "   AND M.PMDIVI = G.GMDIVI         -- ✅ Match division\r\n"
						+ "   AND M.PMGROU = G.GMGROU         -- ✅ Match group\r\n"
						+ "   AND M.PMSGRO = G.GMSGRO         -- ✅ Match subgroup\r\n"
						+ "),\r\n"
						+ "CONCAT_CTE (\r\n"
						+ "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO, PMSTAT, PMDES1, PMDES2, RN, NAME_SERIAL\r\n"
						+ ") AS (\r\n"
						+ "  --  Step 4: Start recursive concatenation\r\n"
						+ "  -- Pick only the highest priority row per PMSTAT (RN=1)\r\n"
						+ "  SELECT\r\n"
						+ "    PMCONO,\r\n"
						+ "    PMDIVI,\r\n"
						+ "    PMCODE,\r\n"
						+ "    PMGROU,\r\n"
						+ "    PMSGRO,\r\n"
						+ "    PMSTAT,\r\n"
						+ "    PMDES1,\r\n"
						+ "    PMDES2,\r\n"
						+ "    RN,\r\n"
						+ "    GMUSER\r\n"
						+ "  FROM JOINED_DATA\r\n"
						+ "  WHERE RN = 1\r\n"
						+ "  UNION ALL\r\n"
						+ "  --  Step 5: Concatenate GMUSER values for rows with RN > 1\r\n"
						+ "  SELECT\r\n"
						+ "    J.PMCONO,\r\n"
						+ "    J.PMDIVI,\r\n"
						+ "    J.PMCODE,\r\n"
						+ "    J.PMGROU,\r\n"
						+ "    J.PMSGRO,\r\n"
						+ "    J.PMSTAT,\r\n"
						+ "    J.PMDES1,\r\n"
						+ "    J.PMDES2,\r\n"
						+ "    J.RN,\r\n"
						+ "    C.NAME_SERIAL || ',' || J.GMUSER\r\n"
						+ "  FROM CONCAT_CTE C\r\n"
						+ "  JOIN JOINED_DATA J\r\n"
						+ "    ON C.PMCONO = J.PMCONO\r\n"
						+ "   AND C.PMDIVI = J.PMDIVI\r\n"
						+ "   AND C.PMCODE = J.PMCODE\r\n"
						+ "   AND C.PMGROU = J.PMGROU\r\n"
						+ "   AND C.PMSGRO = J.PMSGRO\r\n"
						+ "   AND J.RN = C.RN + 1\r\n"
						+ ")\r\n"
						+ "--  Step 6: Select only rows with no next RN (final row per group)\r\n"
						+ "SELECT\r\n"
						+ "  PMCONO,\r\n"
						+ "  PMDIVI,\r\n"
						+ "  PMCODE,\r\n"
						+ "  PMGROU,\r\n"
						+ "  PMSGRO,\r\n"
						+ "  PMSTAT,\r\n"
						+ "  PMDES1,\r\n"
						+ "  NAME_SERIAL\r\n"
						+ "FROM CONCAT_CTE C\r\n"
						+ "WHERE NOT EXISTS (\r\n"
						+ "  SELECT 1\r\n"
						+ "  FROM CONCAT_CTE C2\r\n"
						+ "  WHERE\r\n"
						+ "    C2.PMCONO = C.PMCONO\r\n"
						+ "    AND C2.PMDIVI = C.PMDIVI\r\n"
						+ "    AND C2.PMCODE = C.PMCODE\r\n"
						+ "    AND C2.PMGROU = C.PMGROU\r\n"
						+ "    AND C2.PMSGRO = C.PMSGRO\r\n"
						+ "    AND C2.RN = C.RN + 1\r\n"
						+ ")\r\n"
						+ "--  Step 7: Final sorting of result\r\n"
						+ "ORDER BY PMSTAT, PMGROU, PMSGRO";

				/*
				 * String recursiveQuery = "WITH RECURSIVE filtered_master AS (\r\n"
				 * + "SELECT *\r\n"
				 * + "FROM "+DBNAME+"."+SR_FLOW+"\r\n"
				 * + "WHERE DOC_CODE = 'ITRQ'\r\n"
				 * + "),\r\n"
				 * + "filtered_group AS (\r\n"
				 * + "SELECT *\r\n"
				 * + "FROM "+DBNAME+"."+SR_GROUP+"\r\n"
				 * + "WHERE WHS = 'A91'\r\n"
				 * + "),\r\n"
				 * + "joined_data AS (\r\n"
				 * + "SELECT\r\n"
				 * + "m.DOC_CODE,\r\n"
				 * + "m.GROUP,\r\n"
				 * + "m.SUBGROUP,\r\n"
				 * + "m.STATUS,\r\n"
				 * + "m.REMARK,\r\n"
				 * + "g.NAME,\r\n"
				 * + "ROW_NUMBER() OVER (\r\n"
				 * + "PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP\r\n"
				 * + "ORDER BY g.NAME\r\n"
				 * + ") AS RN\r\n"
				 * + "FROM filtered_master m\r\n"
				 * + "JOIN filtered_group g\r\n"
				 * + "ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP\r\n"
				 * + "),\r\n"
				 * + "concat_cte (\r\n"
				 * + "DOC_CODE, GROUP_ID, SUBGROUP, STATUS, REMARK, RN, NAME_SERIAL\r\n"
				 * + ") AS (\r\n"
				 * + "SELECT\r\n"
				 * + "DOC_CODE,\r\n"
				 * + "GROUP,\r\n"
				 * + "SUBGROUP,\r\n"
				 * + "STATUS,\r\n"
				 * + "REMARK,\r\n"
				 * + "RN,\r\n"
				 * + "NAME\r\n"
				 * + "FROM joined_data\r\n"
				 * + "WHERE RN = 1\r\n"
				 * + "\r\n"
				 * + "UNION ALL\r\n"
				 * + "\r\n"
				 * + "SELECT\r\n"
				 * + "j.DOC_CODE,\r\n"
				 * + "j.GROUP,\r\n"
				 * + "j.SUBGROUP,\r\n"
				 * + "j.STATUS,\r\n"
				 * + "j.REMARK,\r\n"
				 * + "j.RN,\r\n"
				 * + "c.NAME_SERIAL || ',' || j.NAME\r\n"
				 * + "FROM concat_cte c\r\n"
				 * + "JOIN joined_data j\r\n"
				 * + "ON c.DOC_CODE = j.DOC_CODE\r\n"
				 * + "AND c.GROUP_ID = j.GROUP\r\n"
				 * + "AND c.SUBGROUP = j.SUBGROUP\r\n"
				 * + "AND j.RN = c.RN + 1\r\n"
				 * + ")\r\n"
				 * + "\r\n"
				 * + "SELECT\r\n"
				 * + "DOC_CODE,\r\n"
				 * + "STATUS,\r\n"
				 * + "REMARK,\r\n"
				 * + "NAME_SERIAL\r\n"
				 * + "FROM concat_cte c\r\n"
				 * + "WHERE NOT EXISTS (\r\n"
				 * + "SELECT 1\r\n"
				 * + "FROM concat_cte c2\r\n"
				 * + "WHERE\r\n"
				 * + "c2.DOC_CODE = c.DOC_CODE\r\n"
				 * + "AND c2.GROUP_ID = c.GROUP_ID\r\n"
				 * + "AND c2.SUBGROUP = c.SUBGROUP\r\n"
				 * + "AND c2.RN = c.RN + 1\r\n"
				 * + ")\r\n"
				 * + "ORDER BY STATUS";
				 * 
				 */

				/*
				 * String recursiveQuery = "WITH RECURSIVE " +
				 * "filtered_master AS ( " +
				 * "  SELECT * FROM "+DBNAME+"."+SR_FLOW+" WHERE DOC_CODE = 'ITRQ' " +
				 * "), " +
				 * "filtered_group AS ( " +
				 * "  SELECT * FROM "+DBNAME+"."+SR_GROUP+" WHERE WHS = 'A91' " +
				 * "), " +
				 * "joined_data AS ( " +
				 * "  SELECT m.DOC_CODE, m.GROUP, m.SUBGROUP, m.STATUS, g.NAME, " +
				 * "         ROW_NUMBER() OVER (PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP ORDER BY g.NAME) AS RN "
				 * +
				 * "  FROM filtered_master m " +
				 * "  JOIN filtered_group g ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP "
				 * +
				 * "), " +
				 * "concat_cte (DOC_CODE, GROUP_ID, SUBGROUP, STATUS, RN, NAME_SERIAL) AS ( " +
				 * "  SELECT DOC_CODE, GROUP, SUBGROUP, STATUS, RN, NAME FROM joined_data WHERE RN = 1 "
				 * +
				 * "  UNION ALL " +
				 * "  SELECT j.DOC_CODE, j.GROUP, j.SUBGROUP, j.STATUS, j.RN, c.NAME_SERIAL || ':' || j.NAME "
				 * +
				 * "  FROM concat_cte c JOIN joined_data j " +
				 * "  ON c.DOC_CODE = j.DOC_CODE AND c.GROUP_ID = j.GROUP AND c.SUBGROUP = j.SUBGROUP AND j.RN = c.RN + 1 "
				 * +
				 * ") " +
				 * "SELECT DOC_CODE, STATUS, NAME_SERIAL FROM concat_cte c " +
				 * "WHERE NOT EXISTS ( " +
				 * "  SELECT 1 FROM concat_cte c2 " +
				 * "  WHERE c2.DOC_CODE = c.DOC_CODE AND c2.GROUP_ID = c.GROUP_ID AND c2.SUBGROUP = c.SUBGROUP AND c2.RN = c.RN + 1 "
				 * +
				 * ") ORDER BY STATUS";
				 * 
				 */

				logger.debug("PPPPPP : " + recursiveQuery);
				rs = stmt.executeQuery(recursiveQuery);

				while (rs.next()) {
					String cono = rs.getString("PMCONO");
					String divi = rs.getString("PMDIVI");
					String docCode = rs.getString("PMCODE");
					String status = rs.getString("PMSTAT");
					String approve = rs.getString("NAME_SERIAL");
					String remark = rs.getString("PMDES1");
					/*
					 * String insertDetail = "INSERT INTO "+DBNAME+"."+SR_APPROVE+" " +
					 * "( DOC_CODE, DOC_NO, APPROVE, APPROVE_DATE, STATUS, STS_DESC, TIME_ST, APPROVED_USER ,REMARK) "
					 * +
					 * "VALUES (" +
					 * "'" + docCode + "', " +
					 * "'" + currentID + "', " +
					 * "'" + approve + "', " +
					 * "'-', " +
					 * "'" + status + "', " +
					 * "'Wait for approve', " +
					 * "'-', " +
					 * "'-',  " +
					 * "'" + remark + "'" +
					 * ")";
					 * 
					 */

					String insertDetail = "INSERT INTO " + DBNAME + "." + SR_APPROVE + " " +
							"(FATYPE,FACONO,FADIVI, FACODE,FASRNO ,FAAPLI ,FAAPDA ,FASTAT , FADES1, FAENTI,FAENDA,FAAPBY,FADES2) "
							+
							"VALUES (" +
							" '1' , '" + comcono + "','" + comdivi + "','" + docCode + "', " +
							"'" + currentID + "', " +
							"'" + approve + "', " +
							"NULL, " +
							"'" + status + "', " +
							"'Wait for approve', " +
							"CURRENT TIME, " +
							"CURRENT DATE, " +
							"'',  " +
							"'" + remark + "'" +
							")";

					logger.debug("xxxxxxin " + insertDetail);
					stmt2.executeUpdate(insertDetail);

				}

				/*
				 * 
				 * String query2 = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
				 * + "SET  STS_DESC = 'Approved',APPROVE = '"+username+"' , TIME_ST = '" +
				 * currentTimestamp + "',APPROVED_USER = 'PP', APPROVE_DATE = '" + dateYYYYMMDD
				 * +
				 * "' WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID +
				 * "' AND STATUS = '10' ";
				 */

				String query2 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
						+ "SET FAENUS = '" + username + "', FADES1 = 'Approved',FAAPLI = '" + username
						+ "' ,FAAPDA = CURRENT DATE, FAENTI = CURRENT TIME, FAAPTI = CURRENT TIME ,FAAPBY = '"
						+ username + "', FAENDA = CURRENT DATE ,  FADES3 = '"+vRemark2+"' WHERE FACODE = 'ITRQ' AND FASRNO = '" + currentID
						+ "' AND FASTAT = '00'  AND FACONO = '"+comcono+"' AND  FADIVI = '"+comdivi+"' ";

				logger.debug("xxxxxxin " + query2);

				stmt2.executeUpdate(query2);

				/*
				 * 
				 * String query222 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
				 * + "SET APPROVE = '" + depthead + "' \n"
				 * + "WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID +
				 * "' AND STATUS = '20'";
				 * 
				 */

				String query222 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
						+ "SET  FAAPLI = '" + depthead + "' \n"
						+ "WHERE  FACODE = 'ITRQ' AND FASRNO  = '" + currentID + "' AND FASTAT = '10' AND FACONO = '"+comcono+"' AND FADIVI = '"+comdivi+"' ";

				logger.debug("xxxxxxin " + query222);
				stmt2.executeUpdate(query222);

				String data = SelectData.getSTATUSIDITEMRQ(currentID.toString(), comcono, comdivi);
				String url = "https://workflow.br-bangkokranch.com/webhook/sendtodb2";

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
				mJsonObj.put("message", "Service No. " + currentID);
			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Cannot generate Service No.");
			}

			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}
	
	
	public static String insertSRM(String vData, String username, String depthead) throws Exception {
		logger.info("insertRQ");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs9 = null;

		logger.debug("vData: " + vData);

		JSONObject obj = new JSONObject(vData);

		String company = obj.optString("company");
		String warehouse2 = obj.optString("warehouse");
		
		String vRemark2 = obj.optString("vUSRemark");
		

		String itemcode = obj.optString("vItemcode");
		String itemname = obj.optString("itemName");
		
		String programtype = obj.optString("programtype");
		
		String version = obj.optString("vVersion");
		
		logger.debug("vVersion: " + version);
		
		String checkVersion = SelectData.checkVersion("SRQ");
		   if (version == null || version.isEmpty() || !Objects.equals(checkVersion, version)) {
		    mJsonObj.put("result", "nok");
		    mJsonObj.put("message", "Can't create Service number, Please update your version to " + checkVersion + " :  "+version+" (Click F5 button).");
		    return mJsonObj.toString();

		   }
		
		
		
		
		

		logger.debug("ID programtype: " + programtype);

		logger.debug("company: " + company);
		logger.debug("warehouse2: " + warehouse2);
		logger.debug("vRemark: " + vRemark2);

		Map<String, String[]> companyMapping = new HashMap<>();
		companyMapping.put("10", new String[] { "10", "101" });
		companyMapping.put("600", new String[] { "600", "600" });
		companyMapping.put("500", new String[] { "500", "500" });
		// เพิ่มได้เรื่อยๆ เช่น
		// companyMapping.put("300", new String[] { "300", "301" });

		// ดึงข้อมูลตาม company
		String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
		String comcono = mapping[0];
		String comdivi = mapping[1];

		logger.debug("cono: " + comcono);
		logger.debug("divi: " + comdivi);

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();
			Statement stmt2 = conn.createStatement();

			// สร้าง currentID จาก query
			
			/* String idQuery = "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
			
					+ "FROM " + DBNAME + "." + SR_DETAIL + " \n"
					+ "WHERE SUBSTR(FDSRNO, 1, 2) = '25' AND FDCONO  = '" + comcono + "' AND　FDDIVI = '" + comdivi
					+ "' AND FDCODE ='ITRQ'  ";
			*/	
			
//			String idQuery = "SELECT RIGHT(YEAR(CURRENT_DATE), 2)\r\n"
//					+ "|| RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), RIGHT(YEAR(CURRENT_DATE),2) || '000000'), 3)) + 1), 6)\r\n"
//					+ "AS CURRENT_ID\r\n"
//					+ "FROM " + DBNAME + "." + SR_DETAIL + "\r\n"
//					+ "WHERE SUBSTR(FDSRNO, 1, 2) = RIGHT(YEAR(CURRENT_DATE), 2)\r\n"
//					+ "AND FDCONO = '" + comcono +"'\r\n"
//					+ "AND FDDIVI = '" + comdivi + "'\r\n"
//					+ "AND FDCODE = 'ITRQ'";
			
			String idQuery = "SELECT RIGHT(YEAR(CURRENT_DATE), 2)\n"
					+ "|| RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), RIGHT(YEAR(CURRENT_DATE),2) || '000000'), 3)) + 1), 6)\n"
					+ "AS CURRENT_ID\n"
					+ "FROM "+DBNAME+"."+SR_DETAIL+" sf \n"
					+ "WHERE SUBSTR(FDSRNO, 1, 2) = RIGHT(YEAR(CURRENT_DATE), 2)\n"
					+ "AND FDCONO = '"+comcono+"'\n"
					+ "AND FDDIVI = '"+comdivi+"'\n"
					+ "AND FDCODE = 'SWRQ'";

			/*
			 * String idQuery =
			 * "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(SERVICE_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
			 * + "FROM "+DBNAME+"."+SR_DETAIL+" \n"
			 * + "WHERE SUBSTR(SERVICE_ID, 1, 2) = '25'";
			 */
			logger.debug("ID Query: " + idQuery);

			rs = stmt.executeQuery(idQuery);
			
			String currentID = null;
			String fdtype = "1";

			if (rs.next()) {
				currentID = rs.getString("CURRENT_ID");
			}

			String getFDTYPEQuery = "SELECT RQTYPE  FROM BRLDTABK01.sr_requesttype\r\n"
					+ "WHERE RQCONO = '" + comcono + "'\r\n"
					+ "AND RQDIVI = '" + comdivi + "' AND RQNAME = '"+programtype+"'\r\n"
					+ "AND rqcode = 'SWRQ'";
		
			rs9 = stmt.executeQuery(getFDTYPEQuery);
			logger.debug("ID Query: " + getFDTYPEQuery);
			

			if (rs9.next()) {
				fdtype = rs9.getString("RQTYPE");
			}
			
			logger.debug("ID fdtype: " + fdtype);

			if (currentID != null) {
				// insert ด้วย currentID
				/*
				 * String insertQuery = "INSERT INTO "+DBNAME+"."+SR_DETAIL+"  \n"
				 * + "( json_data,SERVICE_ID,PROMGRAM_CODE,STATUS,DATE,TIME) \n"
				 * + "VALUES ('" + vData + "','" + currentID +
				 * "','ITMRQ', '10' ,CURRENT DATE ,CURRENT TIME)";
				 */

				String insertQuery = "INSERT INTO " + DBNAME + "." + SR_DETAIL + "\r\n"
						+ "(FDCONO,FDDIVI,FDTYPE,  FDDATA, FDSRNO,FDCODE, FDDSTA , FDENDA, FDENTI,FDENUS) \r\n"
						+ "VALUES ('" + comcono + "','" + comdivi + "','"+fdtype+"','" + vData + "','" + currentID
						+ "','SWRQ', '10', CURRENT DATE, CURRENT TIME ,'" + username.toString() + "')";

				logger.debug("Insert Query: " + insertQuery);

				java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
				String dateYYYYMMDD = new java.text.SimpleDateFormat("yyyyMMdd").format(currentTimestamp);

				/*
				 * String insertQueryHead = "INSERT INTO "+DBNAME+"."+SR_HEAD+"  \n"
				 * +
				 * "( DOC_CODE,DOC_NO,REQUETER,CREATE_DATE,CREATE_TIME,STATUS,DEPTHEAD ,H_STATUS) \n"
				 * + "VALUES ('ITRQ','" + currentID +
				 * "','"+username+"',CURRENT DATE ,CURRENT TIME, '10','"+depthead+"',1)";
				 */

				String insertQueryHead = "INSERT INTO " + DBNAME + "." + SR_HEAD + "\r\n"
						+ "(FHCONO,FHDIVI, FHCODE, FHSRNO,FHREQU ,FHENDA ,FHENTI,FHENUS ,FHREDA,FHHSTA ,FHDEPH , FHDSTA , FHDES1)\r\n"
						+ "VALUES ('" + comcono + "','" + comdivi + "','SWRQ', '" + currentID + "', '" + username
						+ "', CURRENT DATE , CURRENT TIME,'" + username + "' ,CURRENT DATE, 2, '" + depthead
						+ "', 10 , 'SWRQ-" + currentID + "-" + "" + programtype + "-"+itemname+"')";
				logger.debug("Insert Query: " + insertQueryHead);
				
				
				
				
				

				/*
				 * String insertQueryTemp = "\r\n"
				 * + "INSERT INTO BRLDTABK01.Approve_Detail02 (\r\n"
				 * + "    ID,\r\n"
				 * + "    DOC_CODE,\r\n"
				 * + "    DOC_NO,\r\n"
				 * + "APPROVE,\r\n"
				 * + "APPROVE_DATE,\r\n"
				 * + "STATUS,\r\n"
				 * + "STS_DESC,\r\n"
				 * + "TIME_ST \r\n"
				 * + ")\r\n"
				 * + "SELECT\r\n"
				 * + "    STATUS,\r\n"
				 * + "    DOC_CODE,\r\n"
				 * + "    '" + currentID + "',\r\n"
				 * + "    'PP',\r\n"
				 * + "	'-',\r\n"
				 * + "	STATUS,\r\n"
				 * + "    'Wait for approve',\r\n"
				 * + "    '-'\r\n"
				 * + "FROM BRLDTABK01.flow_master\r\n"
				 * 
				 * + "WHERE DOC_CODE = 'ITRQ'\r\n"
				 * + "";
				 */
				stmt.executeUpdate(insertQuery);

				stmt.executeUpdate(insertQueryHead);

				// stmt.executeUpdate(insertQueryTemp);
				
			
				String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM "+DBNAME+"."+SR_FLOW +"\r\n"
						+ "  WHERE PMCONO = '"+comcono+"'\r\n"
						+ "    AND PMDIVI = '"+comdivi+"'\r\n"
						+ "    AND PMCODE = 'SWRQ'\r\n"
						+ "),\r\n"
						+ "FILTERED_GROUP AS (\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM "+DBNAME+"."+SR_GROUP+"\r\n"
						+ "),\r\n"
						+ "JOINED_DATA AS (\r\n"
						+ "  SELECT\r\n"
						+ "    M.PMCONO,\r\n"
						+ "    M.PMDIVI,\r\n"
						+ "    M.PMCODE,\r\n"
						+ "    M.PMGROU,\r\n"
						+ "    M.PMSGRO,\r\n"
						+ "    M.PMSTAT,\r\n"
						+ "    M.PMDES1,\r\n"
						+ "    M.PMDES2,\r\n"
						+ "    M.PMDES3,        -- ⭐ เพิ่ม\r\n"
						+ "    M.PMDES4,        -- ⭐ เพิ่ม\r\n"
						+ "    G.GMUSER,\r\n"
						+ "    (\r\n"
						+ "      SELECT MIN(M2.PMSTAT)\r\n"
						+ "      FROM "+DBNAME+"."+SR_FLOW+" M2\r\n"
						+ "      WHERE M2.PMCONO = M.PMCONO\r\n"
						+ "        AND M2.PMDIVI = M.PMDIVI\r\n"
						+ "        AND M2.PMCODE = M.PMCODE\r\n"
						+ "        AND M2.PMSTAT > M.PMSTAT\r\n"
						+ "    ) AS NEXT_STAT,\r\n"
						+ "    ROW_NUMBER() OVER (\r\n"
						+ "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n"
						+ "      ORDER BY\r\n"
						+ "        CASE WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0 ELSE 1 END,\r\n"
						+ "        M.PMDES2 DESC,\r\n"
						+ "        G.GMUSER ASC\r\n"
						+ "    ) AS RN\r\n"
						+ "  FROM FILTERED_MASTER M\r\n"
						+ "  JOIN FILTERED_GROUP G\r\n"
						+ "    ON M.PMCONO = G.GMCONO\r\n"
						+ "   AND M.PMDIVI = G.GMDIVI\r\n"
						+ "   AND M.PMGROU = G.GMGROU\r\n"
						+ "   AND M.PMSGRO = G.GMSGRO\r\n"
						+ "),\r\n"
						+ "VACANT_FLAG AS (\r\n"
						+ "  SELECT\r\n"
						+ "    M.PMCONO,\r\n"
						+ "    M.PMDIVI,\r\n"
						+ "    M.PMCODE,\r\n"
						+ "    M.PMGROU,\r\n"
						+ "    M.PMSGRO,\r\n"
						+ "    CASE WHEN COUNT(G2.GMUSER) > 0 THEN 'Y' ELSE 'N' END AS SKIP_IF_VACANT\r\n"
						+ "  FROM FILTERED_MASTER M\r\n"
						+ "  LEFT JOIN "+DBNAME+"."+SR_GROUP+" G2\r\n"
						+ "    ON G2.GMCONO = M.PMCONO\r\n"
						+ "   AND G2.GMDIVI = M.PMDIVI\r\n"
						+ "   AND G2.GMGROU = M.PMGROU\r\n"
						+ "   AND G2.GMSGRO = M.PMSGRO\r\n"
						+ "   AND UPPER(TRIM(G2.GMUSER)) = 'VACANT'\r\n"
						+ "  GROUP BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMGROU, M.PMSGRO\r\n"
						+ "),\r\n"
						+ "CONCAT_CTE (\r\n"
						+ "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
						+ "  PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,   -- ⭐ เพิ่ม\r\n"
						+ "  RN, NAME_SERIAL\r\n"
						+ ") AS (\r\n"
						+ "  SELECT\r\n"
						+ "    PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
						+ "    PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,  -- ⭐ เพิ่ม\r\n"
						+ "    RN,\r\n"
						+ "    GMUSER AS NAME_SERIAL\r\n"
						+ "  FROM JOINED_DATA\r\n"
						+ "  WHERE RN = 1\r\n"
						+ "  UNION ALL\r\n"
						+ "  SELECT\r\n"
						+ "    J.PMCONO, J.PMDIVI, J.PMCODE, J.PMGROU, J.PMSGRO,\r\n"
						+ "    J.PMSTAT, J.PMDES1, J.PMDES2, J.PMDES3, J.PMDES4,  -- ⭐ เพิ่ม\r\n"
						+ "    J.RN,\r\n"
						+ "    C.NAME_SERIAL || ',' || J.GMUSER\r\n"
						+ "  FROM CONCAT_CTE C\r\n"
						+ "  JOIN JOINED_DATA J\r\n"
						+ "    ON C.PMCONO = J.PMCONO\r\n"
						+ "   AND C.PMDIVI = J.PMDIVI\r\n"
						+ "   AND C.PMCODE = J.PMCODE\r\n"
						+ "   AND C.PMGROU = J.PMGROU\r\n"
						+ "   AND C.PMSGRO = J.PMSGRO\r\n"
						+ "   AND J.RN = C.RN + 1\r\n"
						+ ")\r\n"
						+ "SELECT\r\n"
						+ "  C.PMCONO,\r\n"
						+ "  C.PMDIVI,\r\n"
						+ "  C.PMCODE,\r\n"
						+ "  C.PMGROU,\r\n"
						+ "  C.PMSGRO,\r\n"
						+ "  C.PMSTAT,\r\n"
						+ "  C.PMDES1,\r\n"
						+ "  C.PMDES2,\r\n"
						+ "  C.PMDES3 AS NEXT_STAT,\r\n"
						+ "  C.PMDES4 AS PREVIOUS_STAT,    \r\n"
						+ "  C.NAME_SERIAL,\r\n"
						+ "  V.SKIP_IF_VACANT\r\n"
						+ "FROM CONCAT_CTE C\r\n"
						+ "LEFT JOIN VACANT_FLAG V\r\n"
						+ "  ON C.PMCONO = V.PMCONO\r\n"
						+ " AND C.PMDIVI = V.PMDIVI\r\n"
						+ " AND C.PMCODE = V.PMCODE\r\n"
						+ " AND C.PMGROU = V.PMGROU\r\n"
						+ " AND C.PMSGRO = V.PMSGRO\r\n"
						+ "LEFT JOIN CONCAT_CTE C2\r\n"
						+ "  ON C2.PMCONO = C.PMCONO\r\n"
						+ " AND C2.PMDIVI = C.PMDIVI\r\n"
						+ " AND C2.PMCODE = C.PMCODE\r\n"
						+ " AND C2.PMGROU = C.PMGROU\r\n"
						+ " AND C2.PMSGRO = C.PMSGRO\r\n"
						+ " AND C2.RN = C.RN + 1\r\n"
						+ "WHERE C2.PMCONO IS NULL\r\n"
						+ "ORDER BY C.PMSTAT, C.PMGROU, C.PMSGRO\r\n"
						+ "";
				
				
				/*
				String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n"
						+ "  --  Step 1: Select all rows for PMCONO, PMDIVI, PMCODE='ITRQ' from SR_PROCESSMASTER\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM " + DBNAME + "." + SR_FLOW + "\r\n"
						+ "  WHERE PMCONO = '" + comcono + "'\r\n"
						+ "    AND PMDIVI = '" + comdivi + "'\r\n"
						+ "    AND PMCODE = 'ITRQ'\r\n"
						+ "),\r\n"
						+ "FILTERED_GROUP AS (\r\n"
						+ "  --  Step 2: Select all rows from SR_GROUPMASTER (no filter applied)\r\n"
						+ "  SELECT *\r\n"
						+ "  FROM " + DBNAME + "." + SR_GROUP + "\r\n"
						+ "),\r\n"
						+ "JOINED_DATA AS (\r\n"
						+ "  --  Step 3: Join PROCESSMASTER and GROUPMASTER on CONO, DIVI, GROUP, SUBGROUP\r\n"
						+ "  -- Assign row numbers to handle duplicates per PMSTAT\r\n"
						+ "  SELECT\r\n"
						+ "    M.PMCONO,\r\n"
						+ "    M.PMDIVI,\r\n"
						+ "    M.PMCODE,\r\n"
						+ "    M.PMGROU,\r\n"
						+ "    M.PMSGRO,\r\n"
						+ "    M.PMSTAT,\r\n"
						+ "    M.PMDES1,\r\n"
						+ "    M.PMDES2,\r\n"
						+ "    G.GMUSER,\r\n"
						+ "    ROW_NUMBER() OVER (\r\n"
						+ "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n"
						+ "      ORDER BY\r\n"
						+ "        --  Priority 1: PMSGRO matches :TARGET_SUBGROUP\r\n"
						+ "        CASE\r\n"
						+ "          WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0\r\n"
						+ "          ELSE 1\r\n"
						+ "        END,\r\n"
						+ "        --  Priority 2: PMDES2 DESC (higher value preferred)\r\n"
						+ "        M.PMDES2 DESC,\r\n"
						+ "        --  Tie-breaker: PMSGRO alphabetically\r\n"
						+ "        M.PMSGRO ASC\r\n"
						+ "    ) AS RN\r\n"
						+ "  FROM FILTERED_MASTER M\r\n"
						+ "  JOIN FILTERED_GROUP G\r\n"
						+ "    ON M.PMCONO = G.GMCONO         -- ✅ Match company\r\n"
						+ "   AND M.PMDIVI = G.GMDIVI         -- ✅ Match division\r\n"
						+ "   AND M.PMGROU = G.GMGROU         -- ✅ Match group\r\n"
						+ "   AND M.PMSGRO = G.GMSGRO         -- ✅ Match subgroup\r\n"
						+ "),\r\n"
						+ "CONCAT_CTE (\r\n"
						+ "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO, PMSTAT, PMDES1, PMDES2, RN, NAME_SERIAL\r\n"
						+ ") AS (\r\n"
						+ "  --  Step 4: Start recursive concatenation\r\n"
						+ "  -- Pick only the highest priority row per PMSTAT (RN=1)\r\n"
						+ "  SELECT\r\n"
						+ "    PMCONO,\r\n"
						+ "    PMDIVI,\r\n"
						+ "    PMCODE,\r\n"
						+ "    PMGROU,\r\n"
						+ "    PMSGRO,\r\n"
						+ "    PMSTAT,\r\n"
						+ "    PMDES1,\r\n"
						+ "    PMDES2,\r\n"
						+ "    RN,\r\n"
						+ "    GMUSER\r\n"
						+ "  FROM JOINED_DATA\r\n"
						+ "  WHERE RN = 1\r\n"
						+ "  UNION ALL\r\n"
						+ "  --  Step 5: Concatenate GMUSER values for rows with RN > 1\r\n"
						+ "  SELECT\r\n"
						+ "    J.PMCONO,\r\n"
						+ "    J.PMDIVI,\r\n"
						+ "    J.PMCODE,\r\n"
						+ "    J.PMGROU,\r\n"
						+ "    J.PMSGRO,\r\n"
						+ "    J.PMSTAT,\r\n"
						+ "    J.PMDES1,\r\n"
						+ "    J.PMDES2,\r\n"
						+ "    J.RN,\r\n"
						+ "    C.NAME_SERIAL || ',' || J.GMUSER\r\n"
						+ "  FROM CONCAT_CTE C\r\n"
						+ "  JOIN JOINED_DATA J\r\n"
						+ "    ON C.PMCONO = J.PMCONO\r\n"
						+ "   AND C.PMDIVI = J.PMDIVI\r\n"
						+ "   AND C.PMCODE = J.PMCODE\r\n"
						+ "   AND C.PMGROU = J.PMGROU\r\n"
						+ "   AND C.PMSGRO = J.PMSGRO\r\n"
						+ "   AND J.RN = C.RN + 1\r\n"
						+ ")\r\n"
						+ "--  Step 6: Select only rows with no next RN (final row per group)\r\n"
						+ "SELECT\r\n"
						+ "  PMCONO,\r\n"
						+ "  PMDIVI,\r\n"
						+ "  PMCODE,\r\n"
						+ "  PMGROU,\r\n"
						+ "  PMSGRO,\r\n"
						+ "  PMSTAT,\r\n"
						+ "  PMDES1,\r\n"
						+ "  NAME_SERIAL\r\n"
						+ "FROM CONCAT_CTE C\r\n"
						+ "WHERE NOT EXISTS (\r\n"
						+ "  SELECT 1\r\n"
						+ "  FROM CONCAT_CTE C2\r\n"
						+ "  WHERE\r\n"
						+ "    C2.PMCONO = C.PMCONO\r\n"
						+ "    AND C2.PMDIVI = C.PMDIVI\r\n"
						+ "    AND C2.PMCODE = C.PMCODE\r\n"
						+ "    AND C2.PMGROU = C.PMGROU\r\n"
						+ "    AND C2.PMSGRO = C.PMSGRO\r\n"
						+ "    AND C2.RN = C.RN + 1\r\n"
						+ ")\r\n"
						+ "--  Step 7: Final sorting of result\r\n"
						+ "ORDER BY PMSTAT, PMGROU, PMSGRO";

				/*
				 * String recursiveQuery = "WITH RECURSIVE filtered_master AS (\r\n"
				 * + "SELECT *\r\n"
				 * + "FROM "+DBNAME+"."+SR_FLOW+"\r\n"
				 * + "WHERE DOC_CODE = 'ITRQ'\r\n"
				 * + "),\r\n"
				 * + "filtered_group AS (\r\n"
				 * + "SELECT *\r\n"
				 * + "FROM "+DBNAME+"."+SR_GROUP+"\r\n"
				 * + "WHERE WHS = 'A91'\r\n"
				 * + "),\r\n"
				 * + "joined_data AS (\r\n"
				 * + "SELECT\r\n"
				 * + "m.DOC_CODE,\r\n"
				 * + "m.GROUP,\r\n"
				 * + "m.SUBGROUP,\r\n"
				 * + "m.STATUS,\r\n"
				 * + "m.REMARK,\r\n"
				 * + "g.NAME,\r\n"
				 * + "ROW_NUMBER() OVER (\r\n"
				 * + "PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP\r\n"
				 * + "ORDER BY g.NAME\r\n"
				 * + ") AS RN\r\n"
				 * + "FROM filtered_master m\r\n"
				 * + "JOIN filtered_group g\r\n"
				 * + "ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP\r\n"
				 * + "),\r\n"
				 * + "concat_cte (\r\n"
				 * + "DOC_CODE, GROUP_ID, SUBGROUP, STATUS, REMARK, RN, NAME_SERIAL\r\n"
				 * + ") AS (\r\n"
				 * + "SELECT\r\n"
				 * + "DOC_CODE,\r\n"
				 * + "GROUP,\r\n"
				 * + "SUBGROUP,\r\n"
				 * + "STATUS,\r\n"
				 * + "REMARK,\r\n"
				 * + "RN,\r\n"
				 * + "NAME\r\n"
				 * + "FROM joined_data\r\n"
				 * + "WHERE RN = 1\r\n"
				 * + "\r\n"
				 * + "UNION ALL\r\n"
				 * + "\r\n"
				 * + "SELECT\r\n"
				 * + "j.DOC_CODE,\r\n"
				 * + "j.GROUP,\r\n"
				 * + "j.SUBGROUP,\r\n"
				 * + "j.STATUS,\r\n"
				 * + "j.REMARK,\r\n"
				 * + "j.RN,\r\n"
				 * + "c.NAME_SERIAL || ',' || j.NAME\r\n"
				 * + "FROM concat_cte c\r\n"
				 * + "JOIN joined_data j\r\n"
				 * + "ON c.DOC_CODE = j.DOC_CODE\r\n"
				 * + "AND c.GROUP_ID = j.GROUP\r\n"
				 * + "AND c.SUBGROUP = j.SUBGROUP\r\n"
				 * + "AND j.RN = c.RN + 1\r\n"
				 * + ")\r\n"
				 * + "\r\n"
				 * + "SELECT\r\n"
				 * + "DOC_CODE,\r\n"
				 * + "STATUS,\r\n"
				 * + "REMARK,\r\n"
				 * + "NAME_SERIAL\r\n"
				 * + "FROM concat_cte c\r\n"
				 * + "WHERE NOT EXISTS (\r\n"
				 * + "SELECT 1\r\n"
				 * + "FROM concat_cte c2\r\n"
				 * + "WHERE\r\n"
				 * + "c2.DOC_CODE = c.DOC_CODE\r\n"
				 * + "AND c2.GROUP_ID = c.GROUP_ID\r\n"
				 * + "AND c2.SUBGROUP = c.SUBGROUP\r\n"
				 * + "AND c2.RN = c.RN + 1\r\n"
				 * + ")\r\n"
				 * + "ORDER BY STATUS";
				 * 
				 */

				/*
				 * String recursiveQuery = "WITH RECURSIVE " +
				 * "filtered_master AS ( " +
				 * "  SELECT * FROM "+DBNAME+"."+SR_FLOW+" WHERE DOC_CODE = 'ITRQ' " +
				 * "), " +
				 * "filtered_group AS ( " +
				 * "  SELECT * FROM "+DBNAME+"."+SR_GROUP+" WHERE WHS = 'A91' " +
				 * "), " +
				 * "joined_data AS ( " +
				 * "  SELECT m.DOC_CODE, m.GROUP, m.SUBGROUP, m.STATUS, g.NAME, " +
				 * "         ROW_NUMBER() OVER (PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP ORDER BY g.NAME) AS RN "
				 * +
				 * "  FROM filtered_master m " +
				 * "  JOIN filtered_group g ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP "
				 * +
				 * "), " +
				 * "concat_cte (DOC_CODE, GROUP_ID, SUBGROUP, STATUS, RN, NAME_SERIAL) AS ( " +
				 * "  SELECT DOC_CODE, GROUP, SUBGROUP, STATUS, RN, NAME FROM joined_data WHERE RN = 1 "
				 * +
				 * "  UNION ALL " +
				 * "  SELECT j.DOC_CODE, j.GROUP, j.SUBGROUP, j.STATUS, j.RN, c.NAME_SERIAL || ':' || j.NAME "
				 * +
				 * "  FROM concat_cte c JOIN joined_data j " +
				 * "  ON c.DOC_CODE = j.DOC_CODE AND c.GROUP_ID = j.GROUP AND c.SUBGROUP = j.SUBGROUP AND j.RN = c.RN + 1 "
				 * +
				 * ") " +
				 * "SELECT DOC_CODE, STATUS, NAME_SERIAL FROM concat_cte c " +
				 * "WHERE NOT EXISTS ( " +
				 * "  SELECT 1 FROM concat_cte c2 " +
				 * "  WHERE c2.DOC_CODE = c.DOC_CODE AND c2.GROUP_ID = c.GROUP_ID AND c2.SUBGROUP = c.SUBGROUP AND c2.RN = c.RN + 1 "
				 * +
				 * ") ORDER BY STATUS";
				 * 
				 */

				logger.debug("PPPPPP : " + recursiveQuery);
				rs = stmt.executeQuery(recursiveQuery);

				while (rs.next()) {
					String cono = rs.getString("PMCONO");
					String divi = rs.getString("PMDIVI");
					String docCode = rs.getString("PMCODE");
					String status = rs.getString("PMSTAT");
					String approve = rs.getString("NAME_SERIAL");
					String remark = rs.getString("PMDES1");
					/*
					 * String insertDetail = "INSERT INTO "+DBNAME+"."+SR_APPROVE+" " +
					 * "( DOC_CODE, DOC_NO, APPROVE, APPROVE_DATE, STATUS, STS_DESC, TIME_ST, APPROVED_USER ,REMARK) "
					 * +
					 * "VALUES (" +
					 * "'" + docCode + "', " +
					 * "'" + currentID + "', " +
					 * "'" + approve + "', " +
					 * "'-', " +
					 * "'" + status + "', " +
					 * "'Wait for approve', " +
					 * "'-', " +
					 * "'-',  " +
					 * "'" + remark + "'" +
					 * ")";
					 * 
					 */

					String insertDetail = "INSERT INTO " + DBNAME + "." + SR_APPROVE + " " +
							"(FATYPE,FACONO,FADIVI, FACODE,FASRNO ,FAAPLI ,FAAPDA ,FASTAT , FADES1, FAENTI,FAENDA,FAAPBY,FADES2) "
							+
							"VALUES (" +
							" '1' , '" + comcono + "','" + comdivi + "','" + docCode + "', " +
							"'" + currentID + "', " +
							"'" + approve + "', " +
							"NULL, " +
							"'" + status + "', " +
							"'Wait for approve', " +
							"CURRENT TIME, " +
							"CURRENT DATE, " +
							"'',  " +
							"'" + remark + "'" +
							")";

					logger.debug("xxxxxxin " + insertDetail);
					stmt2.executeUpdate(insertDetail);

				}

				/*
				 * 
				 * String query2 = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n"
				 * + "SET  STS_DESC = 'Approved',APPROVE = '"+username+"' , TIME_ST = '" +
				 * currentTimestamp + "',APPROVED_USER = 'PP', APPROVE_DATE = '" + dateYYYYMMDD
				 * +
				 * "' WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID +
				 * "' AND STATUS = '10' ";
				 */

				String query2 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
						+ "SET FAENUS = '" + username + "', FADES1 = 'Approved',FAAPLI = '" + username
						+ "' ,FAAPDA = CURRENT DATE, FAENTI = CURRENT TIME, FAAPTI = CURRENT TIME ,FAAPBY = '"
						+ username + "', FAENDA = CURRENT DATE ,  FADES3 = '"+vRemark2+"' WHERE FACODE = 'ITRQ' AND FASRNO = '" + currentID
						+ "' AND FASTAT = '00'  AND FACONO = '"+comcono+"' AND  FADIVI = '"+comdivi+"' ";

				logger.debug("xxxxxxin " + query2);

				stmt2.executeUpdate(query2);

				/*
				 * 
				 * String query222 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
				 * + "SET APPROVE = '" + depthead + "' \n"
				 * + "WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID +
				 * "' AND STATUS = '20'";
				 * 
				 */

				String query222 = "UPDATE " + DBNAME + "." + SR_APPROVE + " \n"
						+ "SET  FAAPLI = '" + depthead + "' \n"
						+ "WHERE  FACODE = 'ITRQ' AND FASRNO  = '" + currentID + "' AND FASTAT = '10' AND FACONO = '"+comcono+"' AND FADIVI = '"+comdivi+"' ";

				logger.debug("xxxxxxin " + query222);
				stmt2.executeUpdate(query222);

				String data = SelectData.getSTATUSIDITEMRQ(currentID.toString(), comcono, comdivi);
				String url = "https://workflow.br-bangkokranch.com/webhook/sendtodb2";

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
				mJsonObj.put("message", "Service No. " + currentID);
			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Cannot generate Service No.");
			}

			return mJsonObj.toString();

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}
	
	public static String prepareInsertSRM(String vData, String username, String depthead) throws Exception {
		logger.info("insertRQ");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs9 = null;

		logger.debug("vData: " + vData);

		JSONObject obj = new JSONObject(vData);

		String company = obj.optString("company");
	
		
		String programtype = obj.optString("programtype");
		
		String version = obj.optString("vVersion");
	
		String MaxNo = InsertSRMHead( vData,  username,  depthead);
		
		InsertSRMDetail(vData,username,depthead,MaxNo);
		InsertSRMApprove(vData,username,depthead,MaxNo);
		
		//SEND EMAIL
		SendEmail(vData,username,depthead,MaxNo);
		
		return vData;
	}
	

	
	public static void  SendEmail(String vData, String username, String depthead,String Maxno) throws Exception {
		logger.info("insertRQ");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs9 = null;
		try {
			
		
		
		conn = ConnectDB2.doConnect();
		stmt = conn.createStatement();
		Statement stmt2 = conn.createStatement();

		logger.debug("vData: " + vData);

		JSONObject obj = new JSONObject(vData);

		String company = obj.optString("company");
	
		
		String programtype = obj.optString("programtype");
		
		String version = obj.optString("vVersion");
		String softwareName = obj.optString("softwareName");
		String requestDate = obj.optString("requestDate");
		logger.debug("vVersion: " + version);
		String year2digit = requestDate.substring(0,4).substring(2);
		String maxNo = "";
		
		Map<String, String[]> companyMapping = new HashMap<>();
		companyMapping.put("10", new String[] { "10", "101" });
		companyMapping.put("600", new String[] { "600", "600" });
		companyMapping.put("500", new String[] { "500", "500" });
		// เพิ่มได้เรื่อยๆ เช่น
		// companyMapping.put("300", new String[] { "300", "301" });

		// ดึงข้อมูลตาม company
		String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
		String comcono = mapping[0];
		String comdivi = mapping[1];

		logger.debug("cono: " + comcono);
		logger.debug("divi: " + comdivi);

		String data = SelectData.getSTATUSIDSWRQ(Maxno.toString(), comcono, comdivi);
		String url = "https://workflow.br-bangkokranch.com/webhook/softwarereq_sendmail";

		String response = HttpConnection.sendRequest(
				"POST",
				url,
				Map.of("x-access-token",
						"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
				data,
				null // form-data
		);
		
		logger.debug("vVersion: " + version);
		
		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
//			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}
	
	
	

	
	public static String InsertSRMHead(String vData, String username, String depthead) throws Exception {
		logger.info("insertRQ");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs9 = null;
		try {
			
		
		
		conn = ConnectDB2.doConnect();
		stmt = conn.createStatement();
		Statement stmt2 = conn.createStatement();

		logger.debug("vData: " + vData);

		JSONObject obj = new JSONObject(vData);

		String company = obj.optString("company");
	
		
		String programtype = obj.optString("programtype");
		
		String version = obj.optString("vVersion");
		String softwareName = obj.optString("softwareName");
		String requestDate = obj.optString("requestDate");
		logger.debug("vVersion: " + version);
		String year2digit = requestDate.substring(0,4).substring(2);
		String maxNo = "";
		
		Map<String, String[]> companyMapping = new HashMap<>();
		companyMapping.put("10", new String[] { "10", "101" });
		companyMapping.put("600", new String[] { "600", "600" });
		companyMapping.put("500", new String[] { "500", "500" });
		// เพิ่มได้เรื่อยๆ เช่น
		// companyMapping.put("300", new String[] { "300", "301" });

		// ดึงข้อมูลตาม company
		String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
		String comcono = mapping[0];
		String comdivi = mapping[1];

		logger.debug("cono: " + comcono);
		logger.debug("divi: " + comdivi);
		
		
		String insertQueryHead = "INSERT INTO " + DBNAME + "." + SR_HEAD + "\n"
				+ "(FHCONO,FHDIVI, FHCODE, FHSRNO,FHREQU ,FHENDA ,FHENTI,FHENUS ,FHREDA,FHHSTA ,FHDEPH , FHDSTA , FHDES1)\n"
				+ "VALUES ('" + comcono + "','" + comdivi + "','SWRQ', ( SELECT \r\n"
						+ "   RIGHT(TRIM(CHAR(YEAR(CURRENT DATE))), 2) ||\r\n"
						+ "    RIGHT('000000' ||\n"
						+ "          COALESCE(\n"
						+ "              MAX(INTEGER(SUBSTR(FHSRNO,3,6))) + 1,\n"
						+ "              1\n"
						+ "          ),\n"
						+ "    6) AS NEXT_NUMBER\n"
						+ "FROM "+DBNAME+"."+SR_HEAD+" sf \n"
						+ "WHERE SUBSTR(FHSRNO,1,2) = "+year2digit+" AND FHCODE = 'SWRQ' ), '" + username + "'\n"
				+ ", CURRENT DATE , CURRENT TIME,'" + username + "' ,CURRENT DATE, 2, '" + depthead + "'\n"
				+ ", 10 , 'SWRQ-'||( SELECT \n"
				+ "   RIGHT(TRIM(CHAR(YEAR(CURRENT DATE))), 2) ||\n"
				+ "    RIGHT('000000' ||\r\n"
				+ "          COALESCE(\r\n"
				+ "              MAX(INTEGER(SUBSTR(FHSRNO,3,6))) + 1,\n"
				+ "              1\r\n"
				+ "          ),\r\n"
				+ "    6) AS NEXT_NUMBER\r\n"
				+ "FROM "+DBNAME+"."+SR_HEAD+" sf \r\n"
				+ "WHERE SUBSTR(FHSRNO,1,2) = 26 AND FHCODE = 'SWRQ')||'-" + "" + programtype + "-"+softwareName+"')";
		logger.debug("Insert Query: " + insertQueryHead);
		stmt.executeUpdate(insertQueryHead);
		
		
		String getMaxNumber = "SELECT MAX(FHSRNO) as MAXNO\r\n"
				+ "FROM "+DBNAME+"." + SR_HEAD +"\n"
				+ "WHERE FHCONO = '"+comcono+"'\n"
				+ "AND FHDIVI = '"+comdivi+"'\n"
				+ "AND FHCODE = 'SWRQ'\n"
				+ "AND FHREQU = '" + username + "'\n";
		
		rs9 = stmt.executeQuery(getMaxNumber);
		logger.debug("ID Query: " + getMaxNumber);
		

		if (rs9.next()) {
			maxNo = rs9.getString("MAXNO");
		}
		
	
		return maxNo;
		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}
	
	
	

	
	
	
	public static String InsertSRMDetail(String vData, String username, String depthead,String maxNo) throws Exception {
		logger.info("insertRQ");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs9 = null;
		try {
			
		
		
		conn = ConnectDB2.doConnect();
		stmt = conn.createStatement();
		Statement stmt2 = conn.createStatement();

		logger.debug("vData: " + vData);

		JSONObject obj = new JSONObject(vData);

		String company = obj.optString("company");
	
		
		String programtype = obj.optString("programtype");
		
		String version = obj.optString("vVersion");
		String softwareName = obj.optString("softwareName");
		String requestDate = obj.optString("requestDate");
		logger.debug("vVersion: " + version);
		String year2digit = requestDate.substring(0,4).substring(2);
		
		
		Map<String, String[]> companyMapping = new HashMap<>();
		companyMapping.put("10", new String[] { "10", "101" });
		companyMapping.put("600", new String[] { "600", "600" });
		companyMapping.put("500", new String[] { "500", "500" });
		// เพิ่มได้เรื่อยๆ เช่น
		// companyMapping.put("300", new String[] { "300", "301" });

		// ดึงข้อมูลตาม company
		String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
		String comcono = mapping[0];
		String comdivi = mapping[1];
		String fdtype = "1";
		logger.debug("cono: " + comcono);
		logger.debug("divi: " + comdivi);
		
		

		String insertQuery = "INSERT INTO " + DBNAME + "." + SR_DETAIL + "\n"
				+ "(FDCONO,FDDIVI,FDTYPE,  FDDATA, FDSRNO,FDCODE, FDDSTA , FDENDA, FDENTI,FDENUS) \n"
				+ "VALUES ('" + comcono + "','" + comdivi + "','"+fdtype+"','" + vData + "','" + maxNo + "'\n"
				+ ",'SWRQ', '10', CURRENT DATE, CURRENT TIME ,'" + username.toString() + "')";

		logger.debug("Insert Query: " + insertQuery);
//		logger.debug("Insert Query: " + insertQueryHead);
		
		stmt.executeUpdate(insertQuery);
		return "Yay it updated";
		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}
	
	
	public static String InsertSRMApprove(String vData, String username, String depthead,String maxNo) throws Exception {
		logger.info("insertSRMAPPROVE");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs9 = null;
		try {
			
		
		
		conn = ConnectDB2.doConnect();
		stmt = conn.createStatement();
		Statement stmt2 = conn.createStatement();

		logger.debug("vData: " + vData);

		JSONObject obj = new JSONObject(vData);

		String company = obj.optString("company");
	
		
		String programtype = obj.optString("programtype");
		
		String version = obj.optString("vVersion");
		String softwareName = obj.optString("softwareName");
		String requestDate = obj.optString("requestDate");
		logger.debug("vVersion: " + version);
		String year2digit = requestDate.substring(0,4).substring(2);
		
		
		Map<String, String[]> companyMapping = new HashMap<>();
		companyMapping.put("10", new String[] { "10", "101" });
		companyMapping.put("600", new String[] { "600", "600" });
		companyMapping.put("500", new String[] { "500", "500" });
		// เพิ่มได้เรื่อยๆ เช่น
		// companyMapping.put("300", new String[] { "300", "301" });

		// ดึงข้อมูลตาม company
		String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
		String comcono = mapping[0];
		String comdivi = mapping[1];
		String fdtype = "1";
		logger.debug("cono: " + comcono);
		logger.debug("divi: " + comdivi);
		
		

		String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n"
				+ "  SELECT *\r\n"
				+ "  FROM "+DBNAME+"."+SR_FLOW +"\r\n"
				+ "  WHERE PMCONO = '"+comcono+"'\r\n"
				+ "    AND PMDIVI = '"+comdivi+"'\r\n"
				+ "    AND PMCODE = 'SWRQ'\r\n"
				+ "),\r\n"
				+ "FILTERED_GROUP AS (\r\n"
				+ "  SELECT *\r\n"
				+ "  FROM "+DBNAME+"."+SR_GROUP+"\r\n"
				+ "),\r\n"
				+ "JOINED_DATA AS (\r\n"
				+ "  SELECT\r\n"
				+ "    M.PMCONO,\r\n"
				+ "    M.PMDIVI,\r\n"
				+ "    M.PMCODE,\r\n"
				+ "    M.PMGROU,\r\n"
				+ "    M.PMSGRO,\r\n"
				+ "    M.PMSTAT,\r\n"
				+ "    M.PMDES1,\r\n"
				+ "    M.PMDES2,\r\n"
				+ "    M.PMDES3,        -- ⭐ เพิ่ม\r\n"
				+ "    M.PMDES4,        -- ⭐ เพิ่ม\r\n"
				+ "    G.GMUSER,\r\n"
				+ "    (\r\n"
				+ "      SELECT MIN(M2.PMSTAT)\r\n"
				+ "      FROM "+DBNAME+"."+SR_FLOW+" M2\r\n"
				+ "      WHERE M2.PMCONO = M.PMCONO\r\n"
				+ "        AND M2.PMDIVI = M.PMDIVI\r\n"
				+ "        AND M2.PMCODE = M.PMCODE\r\n"
				+ "        AND M2.PMSTAT > M.PMSTAT\r\n"
				+ "    ) AS NEXT_STAT,\r\n"
				+ "    ROW_NUMBER() OVER (\r\n"
				+ "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n"
				+ "      ORDER BY\r\n"
				+ "        CASE WHEN M.PMSGRO = 'MANAGER' THEN 0 ELSE 1 END,\r\n"
				+ "        M.PMDES2 DESC,\r\n"
				+ "        G.GMUSER ASC\r\n"
				+ "    ) AS RN\r\n"
				+ "  FROM FILTERED_MASTER M\r\n"
				+ "  JOIN FILTERED_GROUP G\r\n"
				+ "    ON M.PMCONO = G.GMCONO\r\n"
				+ "   AND M.PMDIVI = G.GMDIVI\r\n"
				+ "   AND M.PMGROU = G.GMGROU\r\n"
				+ "   AND M.PMSGRO = G.GMSGRO\r\n"
				+ "),\r\n"
				+ "VACANT_FLAG AS (\r\n"
				+ "  SELECT\r\n"
				+ "    M.PMCONO,\r\n"
				+ "    M.PMDIVI,\r\n"
				+ "    M.PMCODE,\r\n"
				+ "    M.PMGROU,\r\n"
				+ "    M.PMSGRO,\r\n"
				+ "    CASE WHEN COUNT(G2.GMUSER) > 0 THEN 'Y' ELSE 'N' END AS SKIP_IF_VACANT\r\n"
				+ "  FROM FILTERED_MASTER M\r\n"
				+ "  LEFT JOIN "+DBNAME+"."+SR_GROUP+" G2\r\n"
				+ "    ON G2.GMCONO = M.PMCONO\r\n"
				+ "   AND G2.GMDIVI = M.PMDIVI\r\n"
				+ "   AND G2.GMGROU = M.PMGROU\r\n"
				+ "   AND G2.GMSGRO = M.PMSGRO\r\n"
				+ "   AND UPPER(TRIM(G2.GMUSER)) = 'VACANT'\r\n"
				+ "  GROUP BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMGROU, M.PMSGRO\r\n"
				+ "),\r\n"
				+ "CONCAT_CTE (\r\n"
				+ "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
				+ "  PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,   -- ⭐ เพิ่ม\r\n"
				+ "  RN, NAME_SERIAL\r\n"
				+ ") AS (\r\n"
				+ "  SELECT\r\n"
				+ "    PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
				+ "    PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,  -- ⭐ เพิ่ม\r\n"
				+ "    RN,\r\n"
				+ "    GMUSER AS NAME_SERIAL\r\n"
				+ "  FROM JOINED_DATA\r\n"
				+ "  WHERE RN = 1\r\n"
				+ "  UNION ALL\r\n"
				+ "  SELECT\r\n"
				+ "    J.PMCONO, J.PMDIVI, J.PMCODE, J.PMGROU, J.PMSGRO,\r\n"
				+ "    J.PMSTAT, J.PMDES1, J.PMDES2, J.PMDES3, J.PMDES4,  -- ⭐ เพิ่ม\r\n"
				+ "    J.RN,\r\n"
				+ "    C.NAME_SERIAL || ',' || J.GMUSER\r\n"
				+ "  FROM CONCAT_CTE C\r\n"
				+ "  JOIN JOINED_DATA J\r\n"
				+ "    ON C.PMCONO = J.PMCONO\r\n"
				+ "   AND C.PMDIVI = J.PMDIVI\r\n"
				+ "   AND C.PMCODE = J.PMCODE\r\n"
				+ "   AND C.PMGROU = J.PMGROU\r\n"
				+ "   AND C.PMSGRO = J.PMSGRO\r\n"
				+ "   AND J.RN = C.RN + 1\r\n"
				+ ")\r\n"
				+ "SELECT\r\n"
				+ "  C.PMCONO,\r\n"
				+ "  C.PMDIVI,\r\n"
				+ "  C.PMCODE,\r\n"
				+ "  C.PMGROU,\r\n"
				+ "  C.PMSGRO,\r\n"
				+ "  C.PMSTAT,\r\n"
				+ "  C.PMDES1,\r\n"
				+ "  C.PMDES2,\r\n"
				+ "  C.PMDES3 AS NEXT_STAT,\r\n"
				+ "  C.PMDES4 AS PREVIOUS_STAT,    \r\n"
				+ "  C.NAME_SERIAL,\r\n"
				+ "  V.SKIP_IF_VACANT\r\n"
				+ "FROM CONCAT_CTE C\r\n"
				+ "LEFT JOIN VACANT_FLAG V\r\n"
				+ "  ON C.PMCONO = V.PMCONO\r\n"
				+ " AND C.PMDIVI = V.PMDIVI\r\n"
				+ " AND C.PMCODE = V.PMCODE\r\n"
				+ " AND C.PMGROU = V.PMGROU\r\n"
				+ " AND C.PMSGRO = V.PMSGRO\r\n"
				+ "LEFT JOIN CONCAT_CTE C2\r\n"
				+ "  ON C2.PMCONO = C.PMCONO\r\n"
				+ " AND C2.PMDIVI = C.PMDIVI\r\n"
				+ " AND C2.PMCODE = C.PMCODE\r\n"
				+ " AND C2.PMGROU = C.PMGROU\r\n"
				+ " AND C2.PMSGRO = C.PMSGRO\r\n"
				+ " AND C2.RN = C.RN + 1\r\n"
				+ "WHERE C2.PMCONO IS NULL\r\n"
				+ "ORDER BY C.PMSTAT, C.PMGROU, C.PMSGRO\r\n"
				+ "";
		
		logger.debug("PPPPPP : " + recursiveQuery);
		rs = stmt.executeQuery(recursiveQuery);

		while (rs.next()) {
			String cono = rs.getString("PMCONO");
			String divi = rs.getString("PMDIVI");
			String docCode = rs.getString("PMCODE");
			String status = rs.getString("PMSTAT");
			String approve = rs.getString("NAME_SERIAL");
			String remark = rs.getString("PMDES1");
			/*
			 * String insertDetail = "INSERT INTO "+DBNAME+"."+SR_APPROVE+" " +
			 * "( DOC_CODE, DOC_NO, APPROVE, APPROVE_DATE, STATUS, STS_DESC, TIME_ST, APPROVED_USER ,REMARK) "
			 * +
			 * "VALUES (" +
			 * "'" + docCode + "', " +
			 * "'" + currentID + "', " +
			 * "'" + approve + "', " +
			 * "'-', " +
			 * "'" + status + "', " +
			 * "'Wait for approve', " +
			 * "'-', " +
			 * "'-',  " +
			 * "'" + remark + "'" +
			 * ")";
			 * 
			 */

			String insertDetail = "INSERT INTO " + DBNAME + "." + SR_APPROVE + " " +
					"(FATYPE,FACONO,FADIVI, FACODE,FASRNO ,FAAPLI ,FAAPDA ,FASTAT , FADES1, FAENTI,FAENDA,FAAPBY,FADES2) "
					+
					"VALUES (" +
					" '1' , '" + comcono + "','" + comdivi + "','" + docCode + "', " +
					"'" + maxNo + "', " +
					"'" + approve + "', " +
					"NULL, " +
					"'" + status + "', " +
					"'Wait for approve', " +
					"CURRENT TIME, " +
					"CURRENT DATE, " +
					"'',  " +
					"'" + remark + "'" +
					")";

			logger.debug("xxxxxxin " + insertDetail);
			stmt2.executeUpdate(insertDetail);

		}

//		logger.debug("Insert Query: " + insertQuery);
//		logger.debug("Insert Query: " + insertQueryHead);
		
//		stmt.executeUpdate(insertQuery);
		return "Yay it updated";
		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}
	
	
	
	
	
	public static String addOrderHeadV2(String cono, String divi, String customerno, String orderdate, String delidate,
			   String round, String pricelist, String ordertype, String whs, String status, String type, String remark,
			   String pono, String day, String userid, String version) throws Exception {
			  logger.info("addOrderHeadV2");

			  Connection conn = null;
			  Statement stmt = null;
			  JSONObject mJsonObj = new JSONObject();
			  try {
			   String checkVersion = SelectData.checkVersion("TOD");
			   if (version == null || version.isEmpty() || !Objects.equals(checkVersion, version)) {
			    mJsonObj.put("result", "nok");
			    mJsonObj.put("message", "Can't create Takeorder number, Please update your version to " + checkVersion + " (Click F5 button).");
			    return mJsonObj.toString();

			   }
			   
			   conn = ConnectDB2.doConnect();
			   stmt = conn.createStatement();

			   String getYear = delidate.substring(2, 4);
			   String saleSupport = SelectData.getSalesuport(cono, divi, userid);

			   // String getOrderNumber = SelectData.getMaxOrderNumber(cono, divi, getYear);
			   // if (Integer.valueOf(getOrderNumber) == 0) {// Create new ordernumber
			   // getOrderNumber = getYear + "00000001";
			   //
			   // }

			   remark = ConvertString.convertApostrophe(remark);
			   pono = ConvertString.convertApostrophe(pono);

			   String query = "INSERT INTO " + DBNAME + ".M3_TAKEORDERHEAD \n"
			     + "(THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS) \n"
			     + "VALUES('" + cono + "' \n"
			     + ", '" + divi + "' \n"
			     + ", (SELECT CASE WHEN CAST(MAX(THORNO) AS DECIMAL(10,0)) > 0 THEN CAST(MAX(THORNO) AS DECIMAL(10,0)) + 1 ELSE CAST(('"
			     + getYear + "' || '00000001') AS DECIMAL(10,0)) END AS THORNO \n"
			     + "FROM " + DBNAME + ".M3_TAKEORDERHEAD \n"
			     + "WHERE THCONO = '" + cono + "' \n"
			     + "AND THDIVI = '" + divi + "'  \n"
			     + "AND SUBSTRING(THORNO,0,3) = '" + getYear + "') \n"
			     + ", '" + ordertype + "' \n"
			     + ", '" + whs + "' \n"
			     + ", '' \n"
			     + ", '" + orderdate + "' \n"
			     + ", '" + delidate + "' \n"
			     + ", '" + customerno + "' \n"
			     + ", '" + round + "' \n"
			     + ", '" + pricelist + "' \n"
			     + ", '' \n"
			     + ", '' \n"
			     + ", '' \n"
			     + ", '' \n"
			     + ", '" + userid + "' \n"
			     + ", '" + saleSupport + "' \n"
			     + ", '" + remark + "' \n"
			     + ", '" + pono + "' \n"
			     + ", '" + status + "' \n"
			     + ", CURRENT DATE \n"
			     + ", CURRENT TIME \n"
			     + ", '" + userid + "')";
			   // System.out.println("addOrderHeadV2\n" + query);
			   logger.debug(query);
			   stmt.execute(query);

//			   String getTakeOrderNumber = SelectData.getMaxOrderNumberV3(cono, divi, getYear, userid);
			

			   mJsonObj.put("result", "ok");
//			   mJsonObj.put("message", getTakeOrderNumber);
			//   mJsonObj.put("order", getOrderNumber);

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

			  return mJsonObj.toString();

			 }

	public static String insertRQ(String vID, String vCOM, String vWH, String vITM, String vAPV) throws Exception {
		logger.info("insertRQ");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			// สร้าง currentID จาก query
			String idQuery = "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(H_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID "
					+
					"FROM BRLDTABK01.VISITOR_HEAD \n"
					+ "WHERE SUBSTR(H_ID, 1, 2) = '25'";
			logger.debug("ID Query: " + idQuery);

			rs = stmt.executeQuery(idQuery);
			String currentID = null;

			if (rs.next()) {
				currentID = rs.getString("CURRENT_ID");
			}

			if (currentID != null) {
				// insert ด้วย currentID
				String insertQuery = "INSERT INTO BRLDTABK01.VISITOR_HEAD " +
						"(H_ID, H_COMPANYNAME, H_LICENSE, H_NAME, H_REASON, H_TIMEIN, H_TEL) VALUES (" +
						"'" + currentID + "', '" + vCOM + "', '" + vWH + "', '" + vITM + "', '" + vAPV + "', " +
						"'2024-08-29 02:43:11', 'AP')";
				logger.debug("Insert Query: " + insertQuery);

				stmt.executeUpdate(insertQuery);

				mJsonObj.put("result", "ok");
				mJsonObj.put("CURRENT_ID", currentID);
			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Cannot generate CURRENT_ID");
			}

			return currentID.toString();

		} catch (SQLException e) {
			logger.error("SQL Error: " + e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
		}
	}

	/*
	 * 
	 * public static String insertRQ(String vID,String vCOM,String vWH,String
	 * vITM,String vAPV)
	 * throws Exception {
	 * logger.info("insertRQ");
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * Connection conn = null;
	 * Statement stmt = null;
	 * try {
	 * conn = ConnectDB2.doConnect();
	 * stmt = conn.createStatement();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * // String query =
	 * "INSERT INTO BRLDTABK01.VISITOR_HEAD  (H_ID,H_COMPANYNAME,H_LICENSE,H_NAME,H_REASON,H_TIMEIN,H_TEL) VALUES ('"
	 * +vID+"','"+vCOM+"','"+vWH+"','"+vITM+"','"+
	 * vAPV+"','2024-08-29 02:43:11','AP')";
	 * String query = "INSERT INTO BRLDTABK01.VISITOR_HEAD \r\n"
	 * + "  (H_ID, H_COMPANYNAME, H_LICENSE, H_NAME, H_REASON, H_TIMEIN, H_TEL)\r\n"
	 * + "SELECT \r\n"
	 * +
	 * "  '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(H_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID,\r\n"
	 * + "  '"+vCOM+"',\r\n"
	 * + "  '"+vWH+"',\r\n"
	 * + "  '"+vITM+"',\r\n"
	 * + "  '"+vAPV+"',\r\n"
	 * + "  '2024-08-29 02:43:11',\r\n"
	 * + "  'AP'\r\n"
	 * + "FROM \r\n"
	 * + "  BRLDTABK01.VISITOR_HEAD\r\n"
	 * + "WHERE \r\n"
	 * + "  SUBSTR(H_ID, 1, 2) = '25'\r\n"
	 * + "";
	 * System.out.println("insertRQ\n" + query);
	 * logger.debug(query);
	 * stmt.execute(query);
	 * 
	 * //todo
	 * 
	 * 
	 * 
	 * return mJsonObj.toString();
	 * //return getOrderID.toString();
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
	 */

	public static String testwf(String vCONO)
			throws Exception {
		logger.info("testwf");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "INSERT INTO BRLDTABK01.VISITOR_HEAD  (H_ID,H_TIMEIN) VALUES ('23000001','" + vCONO + "')";
			System.out.println("testwf\n" + query);
			logger.debug(query);
			stmt.execute(query);

			// todo

			return mJsonObj.toString();
			// return getOrderID.toString();

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

	////////////////////

	public static String SCBBill_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {
			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE,\r\n"
					+ "BM_DATE,BM_TIME,\r\n"
					+ "BM_REFCU1,\r\n"
					+ "BM_CHQNO, BM_TRANSAC,\r\n"
					+ "BM_CHANEL, BM_AMOUNT,\r\n"
					+ "BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS, BM_CUNA)\r\n"
					+ "VALUES('" + LCODE + "','10', '101', 'SCB_BILL', '3313019398',\r\n"
					+ "?, ?,\r\n"
					+ "?,\r\n"
					+ "?, ?,\r\n"
					+ "?, ?,\r\n"
					+ "'" + username + "', '10' , '" + formattedDateTime + "', ? , '00' ,?)";

			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					pstmt.setString(1, row.optString("Date", "-"));
					pstmt.setString(2, row.optString("Time", "-"));
					pstmt.setString(3, row.optString("Ref2", "-"));
					pstmt.setString(4, row.optString("Cheque_Number", "-"));
					pstmt.setString(5, row.optString("Transaction_Code", "-"));
					pstmt.setString(6, row.optString("Channel", "-"));
					pstmt.setString(7, row.optString("Amount", "-"));
					pstmt.setString(8, row.optString("CustomerNumber_Ref1", "-"));
					pstmt.setString(9, row.optString("Customer_Name", "-"));

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String SCBBill_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			// 3313019398

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "	(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "	BM_DATE,BM_TIME,\r\n"
					+ "	BM_REFCU1,\r\n"
					+ "	BM_CHQNO,\r\n"
					+ "	BM_CHANEL, BM_AMOUNT,\r\n"
					+ "	 BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS , BM_CUNA)\r\n"
					+ "	 VALUES('" + LCODE + "','10', '101', 'SCB_BILL', '3313019398',\r\n"
					+ "	 ?, ?,\r\n"
					+ "	 ?,\r\n"
					+ "	 ?, \r\n"
					+ "	 ?, ?,\r\n"
					+ "	 '" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00' , ?)";

			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("Date")) {
						pstmt.setString(1, row.get("Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Time")) {
						pstmt.setString(2, row.get("Time").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Ref2")) {
						pstmt.setString(3, row.get("Ref2").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Chq")) {
						pstmt.setString(4, row.get("Chq").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Ch_")) {
						pstmt.setString(5, row.get("Ch_").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Amount")) {
						pstmt.setString(6, row.get("Amount").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Customer_Name")) {
						pstmt.setString(7, row.get("Customer").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Customer")) {
						pstmt.setString(8, row.get("Customer_Name").toString());
					} else {
						pstmt.setString(8, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}
	/////// KBANK BILL PAYMENT //////

	public static String KBANKBill_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "	(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "	BM_DATE,BM_TIME,\r\n"
					+ "	BM_REFCU1,\r\n"
					+ "	BM_CHQNO,\r\n"
					+ "	BM_CHANEL, BM_AMOUNT,\r\n"
					+ "	 BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS, REF2)\r\n"
					+ "	 VALUES('" + LCODE + "','10', '101', 'KBANK_BILL', '3401018025',\r\n"
					+ "	 ?, ?,\r\n"
					+ "	 ?,\r\n"
					+ "	 ?, \r\n"
					+ "	 ?, ?,\r\n"
					+ "	 '" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00' , ?)";

			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("Transaction_Date")) {
						pstmt.setString(1, row.get("Transaction_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Transaction_Time")) {
						pstmt.setString(2, row.get("Transaction_Time").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Reference2")) {
						pstmt.setString(3, row.get("Reference2").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Cheque_No_")) {
						pstmt.setString(4, row.get("Cheque_No_").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Channel")) {
						pstmt.setString(5, row.get("Channel").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Amount")) {
						pstmt.setString(6, row.get("Amount").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Payer_Name")) {
						pstmt.setString(7, row.get("Payer_Name").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Reference1")) {
						pstmt.setString(8, row.get("Reference1").toString());
					} else {
						pstmt.setString(8, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String KBANKBill_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "	(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "	BM_DATE,BM_TIME,\r\n"
					+ "	BM_REFCU1,\r\n"
					+ "	BM_CHQNO,\r\n"
					+ "	BM_CHANEL, BM_AMOUNT,\r\n"
					+ "	 BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS, REF2)\r\n"
					+ "	 VALUES('" + LCODE + "','10', '101', 'KBANK_BILL', '3401018025',\r\n"
					+ "	 ?, ?,\r\n"
					+ "	 ?,\r\n"
					+ "	 ?, \r\n"
					+ "	 ?, ?,\r\n"
					+ "	 '" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00' , ?)";

			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("Transaction_Date")) {
						pstmt.setString(1, row.get("Transaction_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Transaction_Time")) {
						pstmt.setString(2, row.get("Transaction_Time").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Reference2")) {
						pstmt.setString(3, row.get("Reference2").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Cheque_No_")) {
						pstmt.setString(4, row.get("Cheque_No_").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Channel")) {
						pstmt.setString(5, row.get("Channel").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Amount")) {
						pstmt.setString(6, row.get("Amount").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Payer_Name")) {
						pstmt.setString(7, row.get("Payer_Name").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Reference2")) {
						pstmt.setString(8, row.get("Reference1").toString());
					} else {
						pstmt.setString(8, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	/////// BBL BILL PAYMENT ////

	public static String BBLBill_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			REF2,SENDER,\r\n"
					+ "			BM_CHANEL , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'BBL_BILL', '1223098219',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				System.out.print(row);

				try {

					if (row.has("PAY_DATE")) {
						pstmt.setString(1, row.get("PAY_DATE").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("PAY_TIME")) {
						pstmt.setString(2, row.get("PAY_TIME").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("CUSTOMER\\r\\nNO_")) {
						pstmt.setString(3, row.get("CUSTOMER\\r\\nNO_").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("CHQ_NO_")) {
						pstmt.setString(4, row.get("CHQ_NO_").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("FR_BR_")) {
						pstmt.setString(5, row.get("FR_BR_").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					System.out.println("xxxxxxxxxxxxxxx");

					System.out.println(row.get("AMOUNT__"));

					System.out.println("xxxxxxxxxxxxxxx");

					if (row.has("AMOUNT__") && row.get("AMOUNT__") != null) {
						pstmt.setString(6, row.get("AMOUNT__").toString());
					} else {
						pstmt.setString(6, "0"); // ถ้าไม่มี AMOUNT__ หรือเป็น null ให้ค่าเริ่มต้นเป็น "0"
					}

					if (row.has("CUSTOMER\\r\\nNAME")) {
						pstmt.setString(7, row.get("CUSTOMER\\r\\nNAME").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String BBLBill_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			REF2,SENDER,\r\n"
					+ "			BM_CHANEL , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES( '" + LCODE + "','10', '101', 'BBL_BILL', '1223098219',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				System.out.print(row);

				try {

					if (row.has("PAY_DATE")) {
						pstmt.setString(1, row.get("PAY_DATE").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("PAY_TIME")) {
						pstmt.setString(2, row.get("PAY_TIME").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("CUSTOMER_NO_")) {
						pstmt.setString(3, row.get("CUSTOMER_NO_").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("CHQ_NO_")) {
						pstmt.setString(4, row.get("CHQ_NO_").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("FR_BR_")) {
						pstmt.setString(5, row.get("FR_BR_").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					System.out.println("xxxxxxxxxxxxxxx");

					System.out.println(row.get("AMOUNT"));

					System.out.println("xxxxxxxxxxxxxxx");

					if (row.has("AMOUNT") && row.get("AMOUNT") != null) {
						pstmt.setString(6, row.get("AMOUNT").toString());
					} else {
						pstmt.setString(6, "0"); // ถ้าไม่มี AMOUNT__ หรือเป็น null ให้ค่าเริ่มต้นเป็น "0"
					}

					if (row.has("CUSTOMER_NAME")) {
						pstmt.setString(7, row.get("CUSTOMER_NAME").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}
	////////////// MAE MANEE //////////////////

	public static String SCBMMN_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			REF2,SENDER,\r\n"
					+ "			BM_CHANEL , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'SCB_MMN', '3313019322',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("Date")) {
						pstmt.setString(1, row.get("Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Time")) {
						pstmt.setString(2, row.get("Time").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Ref2")) {
						pstmt.setString(3, row.get("Ref2").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("CustomerNumber_Ref1")) {
						pstmt.setString(4, row.get("CustomerNumber_Ref1").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Channel")) {
						pstmt.setString(5, row.get("Channel").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Amount")) {
						pstmt.setString(6, row.get("Amount").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Customer_Name")) {
						pstmt.setString(7, row.get("Customer_Name").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String SCBMMN_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			REF2,SENDER,\r\n"
					+ "			BM_CHANEL , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'SCB_MMN', '3313019322',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("Date")) {
						pstmt.setString(1, row.get("Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Time")) {
						pstmt.setString(2, row.get("Time").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Ref2")) {
						pstmt.setString(3, row.get("Ref2").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Chq")) {
						pstmt.setString(4, row.get("Chq").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Ch_")) {
						pstmt.setString(5, row.get("Ch_").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Amount")) {
						pstmt.setString(6, row.get("Amount").toString());
					} else {
						pstmt.setString(6, "0"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Customer_Name")) {
						pstmt.setString(7, row.get("Customer_Name").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	//////// CURRENT ///////////

	public static String SCBCUR_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHQNO,BM_TRANSAC,\r\n"
					+ "			BM_CHANEL , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'SCB_CUR', '3313019322',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("Date")) {
						pstmt.setString(1, row.get("Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Time")) {
						pstmt.setString(2, row.get("Time").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Cheque Number")) {
						pstmt.setString(3, row.get("Cheque Number").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Transaction Code")) {
						pstmt.setString(4, row.get("Transaction Code").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Channel")) {
						pstmt.setString(5, row.get("Channel").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Amount")) {
						pstmt.setString(6, row.get("Amount").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String SCBCUR_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHQNO,BM_TRANSAC,\r\n"
					+ "			BM_CHANEL , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'SCB_CUR', '3313019322',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("Date")) {
						pstmt.setString(1, row.get("Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Time")) {
						pstmt.setString(2, row.get("Time").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Cheque_Number")) {
						pstmt.setString(3, row.get("Cheque_Number").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Transaction_Code")) {
						pstmt.setString(4, row.get("Transaction_Code").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Channel")) {
						pstmt.setString(5, row.get("Channel").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Credit_Amount")) {
						pstmt.setString(6, row.get("Credit_Amount").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	//////// KBANK CURRENT //////////

	public static String KBANKCUR_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_CHANEL , BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE
					+ "','10', '101', 'KBANK_CUR', '3401018025', ?,\r\n"
					+ "                                ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				// System.out.print( jsonArray.getJSONObject(i));

				try {
					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Date")) {
						pstmt.setString(1, row.get("Date").toString());
					} else {
						pstmt.setString(1, "-");
					}
					if (row.has("Time")) {
						pstmt.setString(2, row.get("Time").toString());
					} else {
						pstmt.setString(2, "-");
					}
					if (row.has("Cheque No.")) {
						pstmt.setString(3, row.get("Cheque No.").toString());
					} else {
						pstmt.setString(3, "-");
					}
					if (row.has("Channel")) {
						pstmt.setString(4, row.get("Channel").toString());
					} else {
						pstmt.setString(4, "-");
					}
					if (row.has("Deposit")) {
						pstmt.setString(5, row.get("Deposit").toString());
					} else {
						pstmt.setString(5, "-");
					}
					if (row.has("Description")) {
						pstmt.setString(6, row.get("Description").toString());
					} else {
						pstmt.setString(6, "-");
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String KBANKCUR_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		System.out.println("////////////////////////////////////////////////////////");
		System.out.println(tableData);
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String formattedDateTime = currentTime.format(formatter);
		System.out.println(formattedDateTime);
		System.out.println("////////////////////////////////////////////////////////");

		try {

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_CHANEL , BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE
					+ "','10', '101', 'KBANK_CUR', '3401018025', ?,\r\n"
					+ "                                ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				System.out.print(jsonArray.getJSONObject(i));

				try {

					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Date")) {
						pstmt.setString(1, row.get("Date").toString());
					} else {
						pstmt.setString(1, "-");
					}
					if (row.has("Time_Ent_Date")) {
						pstmt.setString(2, row.get("Time_Ent_Date").toString());
					} else {
						pstmt.setString(2, "-");
					}
					if (row.has("Cheque_No_")) {
						pstmt.setString(3, row.get("Cheque_No_").toString());
					} else {
						pstmt.setString(3, "-");
					}
					if (row.has("Channel")) {
						pstmt.setString(4, row.get("Channel").toString());
					} else {
						pstmt.setString(4, "-");
					}
					if (row.has("Deposit")) {
						pstmt.setString(5, row.get("Deposit").toString());
					} else {
						pstmt.setString(5, "-");
					}
					if (row.has("Description")) {
						pstmt.setString(6, row.get("Description").toString());
					} else {
						pstmt.setString(6, "-");
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	////////// BBL CURRENT ////////////

	public static String BBLCUR_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHANEL ,REF2 ,\r\n"
					+ "			BM_BRANCH  , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'BBL_CUR', '1223098219',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					if (row.has("Value_Date")) {
						pstmt.setString(1, row.get("Value_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Tran_Date")) {
						pstmt.setString(2, row.get("Tran_Date").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Channel")) {
						pstmt.setString(3, row.get("Channel").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Terminal_Id")) {
						pstmt.setString(4, row.get("Terminal_Id").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Branch")) {
						pstmt.setString(5, row.get("Branch").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Credit")) {
						pstmt.setString(6, row.get("Credit").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String BBLCUR_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHANEL ,REF2 ,\r\n"
					+ "			BM_BRANCH  , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'BBL_CUR', '1223098219',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					if (row.has("Value_Date")) {
						pstmt.setString(1, row.get("Value_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Tran_Date")) {
						pstmt.setString(2, row.get("Tran_Date").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Channel")) {
						pstmt.setString(3, row.get("Channel").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Terminal_Id")) {
						pstmt.setString(4, row.get("Terminal_Id").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Branch")) {
						pstmt.setString(5, row.get("Branch").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Credit")) {
						pstmt.setString(6, row.get("Credit").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	//////////// QR ///////////////////

	public static String BBLQR_Intraday(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			REF2,SENDER,\r\n"
					+ "			REF4 , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS , BM_REFCU1)\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'BBL_QR', '1223098219',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00', ?)";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("PAY_DATE")) {
						pstmt.setString(1, row.get("PAY_DATE").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("PAY_TIME")) {
						pstmt.setString(2, row.get("PAY_TIME").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("REF_2")) {
						pstmt.setString(3, row.get("REF_2").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("PAY_BY")) {
						pstmt.setString(4, row.get("PAY_BY").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("FR_BR__")) {
						pstmt.setString(5, row.get("FR_BR__").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("AMOUNT__")) {
						pstmt.setString(6, row.get("AMOUNT__").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("CUSTOMER_NAME")) {
						pstmt.setString(7, row.get("CUSTOMER_NAME").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("REF_1")) {
						pstmt.setString(8, row.get("REF_1").toString());
					} else {
						pstmt.setString(8, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String BBLQR_Historical(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			REF2,SENDER,\r\n"
					+ "			REF4 , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS , BM_REFCU1 )\r\n"
					+ "			VALUES('" + LCODE + "','10', '101', 'BBL_QR', '1223098219',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime.toString() + "' , ? , '00' ,?)";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					if (row.has("PAY_DATE")) {
						pstmt.setString(1, row.get("PAY_DATE").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("PAY_TIME")) {
						pstmt.setString(2, row.get("PAY_TIME").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("REFERENCE_NO_")) {
						pstmt.setString(3, row.get("REFERENCE_NO_").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("REFERENCE_NO_")) {
						pstmt.setString(4, row.get("REFERENCE_NO_").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("FR_BR_")) {
						pstmt.setString(5, row.get("FR_BR_").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("AMOUNT")) {
						pstmt.setString(6, row.get("AMOUNT").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("CUSTOMER_NAME")) {
						pstmt.setString(7, row.get("CUSTOMER_NAME").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("CUSTOMER_NO_")) {
						pstmt.setString(8, row.get("CUSTOMER_NO_").toString());
					} else {
						pstmt.setString(8, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	////////////////////////// BANKMAPPING ////////////////////////////////

	////////////// SPLIT ////////////////

	public static String SPLITBMSCB(String ID,
			String BM_CONO,
			String getDivi,
			String PAYER,
			String OVERPAY,
			String CNDN,
			String ACCNO,
			String BM_DESC,
			String BM_DATE,
			String BM_TIME,
			String BANKCHARGE,
			String BM_AMOUNT,
			String BM_PARENT,
			String GROUP_ID,
			String H_RNNO,
			String OKCUNO,
			String PAYERNAME,
			String TYPE,
			String CHECKTYPE,
			String newBM_AMOUNT)
			throws Exception {
		logger.info("SPLITBM");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			String lcode = "0";

			switch (CHECKTYPE) {

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
					lcode = "1AA2214";
					break;

			}

			String query = "INSERT  INTO  " + DBNAME + ".BANK_MAPPING\r\n"
					+ "(BM_CONO,BM_DIVI,BM_BANK,BM_ACCODE,BM_DATE,BM_TIME,BM_CHQNO,BM_TRANSAC,BM_CUNO,BM_CHANEL,BM_BRANCH,BM_AMOUNT,BM_DESC,BM_USER,BM_BKCHARGE,BM_OVPAY,BM_CNDN,BM_STATUS,REF2,REF3,SENDER,BM_REFCU,REF4,BM_REFCU1,BM_CUNA,BM_LCODE,BM_PARENT_STS,BM_PARENT_ID,BM_TIME1)\r\n"
					+ "  VALUES('" + BM_CONO + "','" + getDivi + "','" + CHECKTYPE + "','" + ACCNO + "','" + BM_DATE
					+ "',VARCHAR_FORMAT(\r\n"
					+ "    TIMESTAMP_FORMAT('" + BM_TIME
					+ "', 'HH12:MI:SS AM') + CAST((RAND() * 10) AS INTEGER) SECOND,\r\n"
					+ "    'HH12.MI.SS AM'\r\n"
					+ "  ) ,'0','X1','" + OKCUNO + "','-','-','" + newBM_AMOUNT + "','" + BM_DESC + "','TESTUSER','"
					+ BANKCHARGE + "','" + OVERPAY + "','" + CNDN + "','10','-','-','-','-','-','-','-','" + lcode
					+ "','77','" + ID + "','" + timestamp + "')\r\n"
					+ "";
			System.out.println("SPLITBM\n" + query);
			logger.debug(query);
			stmt.execute(query);

			String query2 = "UPDATE " + DBNAME + ".BANK_MAPPING\n"
					+ "SET BM_PARENT_STS = '88',  BM_AMOUNT = '" + BM_AMOUNT + "', BM_CUNO = '" + PAYER.trim()
					+ "' ,  BM_BKCHARGE = '" + BANKCHARGE.trim() + "', BM_OVPAY = '" + OVERPAY.trim() + "', BM_CNDN = '"
					+ CNDN.trim() + "'\n"
					+ "WHERE BM_CONO = '" + BM_CONO + "' "
					+ "AND BM_DIVI = '" + getDivi + "' "
					+ "AND BM_DATE = '" + BM_DATE.trim() + "' "
					+ "AND BM_ID = '" + ID.trim() + "' ";

			System.out.println("SPLITBM2\n" + query2);
			logger.debug(query2);
			stmt.execute(query2);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", query);

			return mJsonObj.toString();
			// return getOrderID.toString();

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

	////////////////// SPLITBM/////////////

	public static String addvisitorheader(String vCONO, String vDIVI, String vLocation)
			throws Exception {
		logger.info("addvisitorheader");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "　INSERT  INTO  BRLDTA0100.VISITOR_HEAD \r\n"
					+ "　(H_ID,H_TIMEIN,H_CONO,H_DIVI,H_LOCATION,H_ROOMNO,H_CARDNO,H_REMARK1)\r\n"
					+ "　VALUES ((SELECT COALESCE(MAX(H_ID)+1,\r\n"
					+ "	 SUBSTRING(REPLACE(CHAR(current date, ISO),'-',''),3,2) || '000001' ) AS DATE\r\n"
					+ "	 FROM BRLDTA0100.VISITOR_HEAD WHERE H_CONO = '" + vCONO.trim() + "' AND  H_DIVI = '"
					+ vDIVI.trim() + "' AND　H_LOCATION = '" + vLocation.trim()
					+ "'),(SELECT VARCHAR_FORMAT(CURRENT TIMESTAMP, 'YYYY-MM-DD HH:mi:ss')\r\n"
					+ "FROM SYSIBM.SYSDUMMY1),'" + vCONO.trim() + "','" + vDIVI.trim() + "','" + vLocation.trim()
					+ "','-','-','-')";
			System.out.println("addvisitorheader\n" + query);
			logger.debug(query);
			stmt.execute(query);

			// todo

			String getORDER = SelectData.getID1();

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", query);
			mJsonObj.put("ID", getORDER);

			return mJsonObj.toString();
			// return getOrderID.toString();

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

	public static String addfollower(String cono, String divi, String location, String H_SURNAME, String vID)
			throws Exception {
		logger.info("addfollower");

		System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = " INSERT INTO BRLDTA0100.FOLLOWER_HEAD \r\n"
					+ "(H_CONO,H_DIVI,H_LOCATION,H_NAME ,H_SURNAME , F_ID , H_REMARK1, H_STATUS)\r\n"
					+ "VALUES \r\n"
					+ "('" + cono.trim() + "','" + divi.trim() + "','" + location.trim() + "', 'Follower','"
					+ H_SURNAME.trim() + "','" + vID.trim()
					+ "', (SELECT  COALESCE(MAX(H_REMARK1)+1,'1' ) AS FOLLOWID  \r\n"
					+ "	 FROM BRLDTA0100.FOLLOWER_HEAD \r\n"
					+ "	 WHERE  F_ID  = '" + vID.trim() + "'),NULL)";
			System.out.println("addfollower\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", query);

			return mJsonObj.toString();
			// return getOrderID.toString();

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

	public static String addSwrHeader(String cono, String divi, String vSwrtype,
			String vSwrname, String vVersion, String vReqdate, String vFinishdate,
			String vRemark, String vRequester, String vDepthead, String vDev,
			String vAppdevmanager, String vGM, String vCIO, String vStatus)
			throws Exception {
		logger.info("addSwrHeader");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			/*
			 * String getYear4 = month.substring(0, 4);
			 * String getYear2 = month.substring(2, 4);
			 * remark = ConvertString.convertApostrophe(remark);
			 */

			// String getOrderID = SelectData.getnewID(cono, divi);

			// logger.debug("getOrderID {}", getOrderID);

			String query = "\r\n"
					+ "INSERT INTO BRLDTA0100.M3_SWRHEAD\r\n"
					+ "(SHORNO,SHCONO,SHDIVI,\r\n"
					+ "SHTYPE,SHREDA,SHFIDA,SHREM1,SHREQU\r\n"
					+ ",SHAPPV,SHSTAT,SHMOD,SHBU,SHCOCE,SHAPPC,SHENDA,SHENTI)\r\n"
					+ "VALUES \r\n"
					+ "((SELECT  COALESCE(MAX(SHORNO)+1,SUBSTRING(REPLACE(CHAR(current date, ISO),'-',''),3,2) || '000001' ) AS ORDERNO   FROM BRLDTA0100.M3_SWRHEAD WHERE   SHCONO = '10' AND  SHDIVI = '101'),\r\n"
					+ "'10','101',\r\n"
					+ "'','2024-04-12','2024-04-12',\r\n"
					+ "'qqqq','M3SRVICT','BSRI','00','-','-','-','-','2008-10-29','14:56:59')\r\n"
					+ "";
			System.out.println("addSWRHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", query);

			return mJsonObj.toString();
			// return getOrderID.toString();

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

	public static String addImageVisitor(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String id,
			InputStream imagefile,
			String imagename) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String setImageName = "visitor" + "_" + id + ".png";

				String query = "";
				stmt.execute(query);

				if (imagename != null) {
					String uploadFilePath = null;
					// String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
					String filePath = "D:\\files\\api_project\\adr_images\\";
					// System.out.println("filePath: " + filePath + setImageName);
					uploadFilePath = FileUtillity.writeToFileServer(imagefile, setImageName, filePath);

				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "complete");
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

	public static String addMARHead(String cono, String divi, String marno, String date, String postdate,
			String month, String type, String prefix, String bu, String costcenter, String accountant, String requestor,
			String remark, String purpose, String approve1, String approve2, String approve3, String approve4,
			String approve5, String acknoict, String acknocio, String status, String submit, String username)
			throws Exception {
		logger.info("addMARHead");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String getYear4 = month.substring(0, 4);
			String getYear2 = month.substring(2, 4);
			remark = ConvertString.convertApostrophe(remark);

			String query = "INSERT INTO BRLDTA0100.M3_MARHEAD \n"
					+ "(MHCONO, MHDIVI, MHPREF, MHORNO, MHREDA, MHPODA, MHMONT, MHTYPE, MHBU, MHCOCE, MHACCT, MHREQU, MHREM1, MHREM2, MHAPP1, MHAPDA1, MHAPP2, MHAPDA2, MHAPP3, MHAPDA3, MHAPP4, MHAPDA4, MHAPP5, MHAPDA5, MHAPICT, MHICTDA, MHAPCIO, MHCIODA, MHACCRE, MHAPRE1, MHAPRE2, MHAPRE3, MHAPRE4, MHAPRE5, MHICTRE, MHCIORE, MHENDA, MHENTI, MHSTAT, MHENUS) \n"
					+ "VALUES('" + cono + "' \n"
					+ ", '" + divi + "' \n"
					+ ", '" + prefix + "' \n"
					+ ", (SELECT CASE WHEN CAST(MAX(MHORNO) AS DECIMAL(10,0)) > 0 THEN CAST(MAX(MHORNO) AS DECIMAL(10,0)) + 1 \n"
					+ "ELSE CAST((SUBSTRING('" + date + "',3,2) || '000001') AS DECIMAL(10,0)) END AS MHORNO \n"
					+ "FROM BRLDTA0100.M3_MARHEAD \n"
					+ "WHERE MHCONO = '" + cono + "' \n"
					+ "AND MHDIVI = '" + divi + "' \n"
					+ "AND MHPREF = '" + prefix + "' \n"
					+ "AND SUBSTRING(CHAR(MHREDA,ISO),0,5) = SUBSTRING('" + date + "',0,5)) \n"
					+ ", '" + date + "' \n"
					+ ", '" + postdate + "' \n"
					+ ", '" + month + "' \n"
					+ ", '" + type + "' \n"
					+ ", '" + bu + "' \n"
					+ ", '" + costcenter + "' \n"
					+ ", '" + accountant + "' \n"
					+ ", '" + requestor + "' \n"
					+ ", '" + remark + "' \n"
					+ ", '" + purpose + "' \n"
					+ ", '" + approve1 + "' \n"
					+ ", NULL \n"
					+ ", '" + approve2 + "' \n"
					+ ", NULL \n"
					+ ", '" + approve3 + "' \n"
					+ ", NULL \n"
					+ ", '" + approve4 + "' \n"
					+ ", NULL \n"
					+ ", '" + approve5 + "' \n"
					+ ", NULL \n"
					+ ", '" + acknoict + "' \n"
					+ ", NULL \n"
					+ ", '" + acknocio + "' \n"
					+ ", NULL \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", CURRENT DATE \n"
					+ ", CURRENT TIME \n"
					+ ", '" + status + "' \n"
					+ ", '" + username + "')";
			// System.out.println("addADRHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			String getMARNumber = SelectData.getMaxMARNumber(cono, divi, prefix, getYear4, requestor);
			logger.debug("getMARNumber {}", getMARNumber);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", getMARNumber);

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

	public static String addSWRFile(String cono, String divi, String orderno, String refer, String name, String line,
			String type, String remark1, String remark2)
			throws Exception {
		logger.info("addSWRFile");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			remark1 = ConvertString.convertApostrophe(remark1);
			remark2 = ConvertString.convertApostrophe(remark2);

			String query = "INSERT INTO " + DBNAME + ".M3_SWRFILE \n"
					+ "(SFCONO, SFDIVI, SFPREF, SFORNO, SFLINE, SFNAME, SFTYPE, SFREM1, SFREM2, SFENDA, SFENTI, SFENUS) \n"
					+ "VALUES('10' \n"
					+ ", '101' \n"
					+ ", '" + refer + "' \n"
					+ ", '" + orderno + "' \n"
					+ ", '" + line + "' \n"
					+ ", '" + name + "' \n"
					+ ", '" + type + "' \n"
					+ ", '" + remark1 + "' \n"
					+ ", '" + remark2 + "' \n"
					+ ", CURRENT_DATE \n"
					+ ", CURRENT_TIME \n"
					+ ", '')";
			// System.out.println("addMARFile\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Insert file complete.");
			logger.info("Insert complete.");
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

	public static String addreceipt(String rcno, String voucher, String fixno)
			throws Exception {
		logger.info("addreceipt");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "    INSERT INTO M3FDBTST.HR_RECEIPT  \r\n"
					+ "                     (HR_CONO,HR_DIVI,HC_RCNO,HC_VCNO,HC_FIXNO)\r\n"
					+ "                     VALUES ('11','111','" + rcno.trim() + "','" + voucher.trim() + "','"
					+ fixno.trim() + "')";
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Insert complete.");
			logger.info("Insert complete.");
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

	public static String addMARDetail(String cono, String divi, String marno, String prefix, String refno,
			String typeadjust, String line, String item, String name, String fac, String whs, String loc,
			String lotno, String date, String unit, String qty, String price, String amt, String remark1,
			String remark2, String status, String username)
			throws Exception {
		logger.info("addMARDetail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String getMARNumber = marno.substring(2, 10);
			remark1 = ConvertString.convertApostrophe(remark1);
			remark2 = ConvertString.convertApostrophe(remark2);

			String query = "INSERT INTO BRLDTA0100.M3_MARDETAIL \n"
					+ "(MLCONO, MLDIVI, MLPREF, MLORNO, MLRENO, MLTYPE, MLFACI, MLWHLO, MLLINE, MLITNO, MLITDE, MLLOCA, MLLOTN, MLDATE, MLUNIT, MLQTY, MLPRIC, MLREM1, MLREM2, MLENDA, MLENTI, MLSTAT, MLENUS) \n"
					+ "VALUES('" + cono + "' \n"
					+ ", '" + divi + "' \n"
					+ ", '" + prefix + "' \n"
					+ ", '" + getMARNumber + "' \n"
					+ ", '" + refno + "' \n"
					+ ", '" + typeadjust + "' \n"
					+ ", '" + fac + "' \n"
					+ ", '" + whs + "' \n"
					+ ", (SELECT COALESCE(MAX(MLLINE),0) + 1 AS MLLINE \n"
					+ "FROM BRLDTA0100.M3_MARDETAIL \n"
					+ "WHERE MLCONO = '" + cono + "' \n"
					+ "AND MLDIVI = '" + divi + "' \n"
					+ "AND MLPREF || '-' || MLORNO = '" + marno + "') \n"
					+ ", '" + item + "' \n"
					+ ", '" + name + "' \n"
					+ ", '" + loc + "' \n"
					+ ", '" + lotno + "' \n"
					+ ", '" + date + "' \n"
					+ ", '" + unit + "' \n"
					+ ", " + qty + " \n"
					+ ", " + price + " \n"
					+ ", '" + remark1 + "' \n"
					+ ", '" + remark2 + "' \n"
					+ ", CURRENT DATE \n"
					+ ", CURRENT TIME \n"
					+ ", '" + status + "' \n"
					+ ", '" + username + "')";
			// System.out.println("addADRHead\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Insert complete.");
			logger.info("Insert complete.");
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

	public static String addMARFile(HttpServletRequest httpServletRequest, String cono, String divi, String prefix,
			String marno, InputStream file, String name,
			String type, String remark1, String remark2, String username)
			throws Exception {
		logger.info("addMARDetail");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String getMARNumber = marno.substring(2, 10);
			remark1 = ConvertString.convertApostrophe(remark1);
			remark2 = ConvertString.convertApostrophe(remark2);

			String getMaxLine = SelectData.getMaxMARFileLine(cono, divi, marno);

			String query = "INSERT INTO BRLDTA0100.M3_MARFILE \n"
					+ "(MFCONO, MFDIVI, MFPREF, MFORNO, MFLINE, MFNAME, MFTYPE, MFREM1, MFREM2, MFENDA, MFENTI, MFENUS) \n"
					+ "VALUES('" + cono + "' \n"
					+ ", '" + divi + "' \n"
					+ ", '" + prefix + "' \n"
					+ ", '" + getMARNumber + "' \n"
					+ ", '" + getMaxLine + "' \n"
					+ ", '" + name + "' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", '' \n"
					+ ", CURRENT DATE \n"
					+ ", CURRENT TIME \n"
					+ ", '" + username + "')";
			// System.out.println("addMARFile\n" + query);
			logger.debug(query);
			stmt.execute(query);

			String path = marno + "_" + getMaxLine + "_" + name;

			FileUtillity.writeToFileServerV2(httpServletRequest, file, name, path);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Insert file complete.");
			logger.info("Insert complete.");
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

	public static String addMonitoringReceipt(String rcno)
			throws Exception {
		logger.info("addMonitoringReceipt");

		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = ConnectDB2.doConnect();
			stmt = conn.createStatement();

			String query = "INSERT INTO M3FDBTST.HR_RECEIPT (HR_CONO,HR_DIVI,HC_RCNO,HC_VCNO,HC_FIXNO)\r\n"
					+ "VALUES ('11','111','" + rcno.trim() + "','44','55'); ";
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx\n" + query);
			logger.debug(query);
			stmt.execute(query);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Insert file complete.");
			logger.info("Insert complete.");
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static String addADRHead(String cono, String divi, String adrno, String date, String month, String type,
			String prefix, String boi, String bu, String costcenter, String vat, String accountant, String requestor,
			String remark1, String approve1, String approve2, String approve3, String approve4, String status,
			String submit, String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String getYear4 = month.substring(0, 4);
				String getYear2 = month.substring(2, 4);

				remark1 = ConvertString.convertApostrophe(remark1);

				String query = "INSERT INTO BRLDTA0100.M3_ADRHEAD \n"
						+ "(ADCONO, ADDIVI, ADORNO, ADDATE, ADMONT, ADTYPE, ADPREF, ADBOI, ADBU, ADCOCE, ADVAT, ADACCT, ADREQU, ADREM1, ADREM2, ADIMAG, ADAPP1, ADAPDA1, ADAPP2, ADAPDA2, ADAPP3, ADAPDA3, ADAPP4, ADAPDA4 \n"
						+ ", ADACRE1, ADAPRE1, ADAPRE2, ADAPRE3, ADAPRE4, ADENDA, ADENTI, ADSTAT, ADENUS) \n"
						+ "VALUES(" + cono + " \n"
						+ ", " + divi + " \n"
						+ ", (SELECT CASE WHEN CAST(MAX(ADORNO) AS DECIMAL(10,0)) > 0 THEN CAST(MAX(ADORNO) AS DECIMAL(10,0)) + 1 \n"
						+ "ELSE CAST(('" + getYear2 + "' || '000001') AS DECIMAL(10,0)) END AS ADORNO \n"
						+ "FROM BRLDTA0100.M3_ADRHEAD \n"
						+ "WHERE ADCONO = '" + 10 + "' \n"
						+ "AND ADDIVI = '" + divi + "' \n"
						+ "AND ADPREF = '" + prefix + "' \n"
						+ "AND SUBSTRING(CHAR(ADDATE,ISO),0,5) = '" + getYear4 + "') \n"
						+ ", '" + date + "' \n"
						+ ", '" + month + "' \n"
						+ ", '" + type + "' \n"
						+ ", '" + prefix + "' \n"
						+ ", '" + boi + "' \n"
						+ ", '" + bu + "' \n"
						+ ", '" + costcenter + "' \n"
						+ ", '" + vat + "' \n"
						+ ", '" + accountant + "' \n"
						+ ", '" + requestor + "' \n"
						+ ", '" + remark1 + "' \n"
						+ ", '' \n"
						+ ", '' \n"
						+ ", '" + approve1 + "' \n"
						+ ", NULL \n"
						+ ", '" + approve2 + "' \n"
						+ ", NULL \n"
						+ ", '" + approve3 + "' \n"
						+ ", NULL \n"
						+ ", '" + approve4 + "' \n"
						+ ", NULL \n"
						+ ", '' \n"
						+ ", '' \n"
						+ ", '' \n"
						+ ", '' \n"
						+ ", '' \n"
						+ ", CURRENT DATE \n"
						+ ", CURRENT TIME \n"
						+ ", '" + status + "' \n"
						+ ", '" + username + "')";
				// System.out.println("addADRHead\n" + query);
				stmt.execute(query);

				String getADRNumber = SelectData.getMaxADRNumber(cono, divi, prefix, getYear4, requestor);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", getADRNumber);
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

	public static String addADRDetail(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, String prefix, String line, String itemno, String sbno, String costcenter, String date,
			String assetcost, String netvalue, String qty, String price, String remark, InputStream imagefile,
			String imagename, String status, String submit, String username) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String getADRNumber = adrno.substring(2, 10);
				remark = ConvertString.convertApostrophe(remark);

				String getMaxADRLine = SelectData.getMaxADRLine(cono, divi, adrno);
				String setImageName = adrno + "_" + getMaxADRLine + ".png";

				String query = "INSERT INTO BRLDTA0100.M3_ADRDETAIL \n"
						+ "(ALCONO, ALDIVI, ALORNO, ALPREF, ALLINE, ALITNO, ALITDE, ALSBNO, ALCOST, ALDATE, ALACOS, ALNETV, ALQTY, ALPRIC, ALREM1, ALREM2, ALIMAG, ALENDA, ALENTI, ALSTAT, ALENUS) \n"
						+ "VALUES('" + cono + "' \n"
						+ ", '" + divi + "' \n"
						+ ", '" + getADRNumber + "' \n"
						+ ", '" + prefix + "' \n"
						// + ", (SELECT COALESCE(MAX(ALLINE),0) + 1 AS ALLINE \n"
						// + "FROM BRLDTA0100.M3_ADRDETAIL \n"
						// + "WHERE ALCONO = '"+cono+"' \n"
						// + "AND ALDIVI = '"+divi+"' \n"
						// + "AND ALPREF || '-' || ALORNO = '"+adrno+"') \n"
						+ ", '" + getMaxADRLine + "' \n"
						+ ", '" + itemno + "' \n"
						+ ", '' \n"
						+ ", '" + sbno + "' \n"
						+ ", '" + costcenter + "' \n"
						+ ", '" + date + "' \n"
						+ ", '" + assetcost + "' \n"
						+ ", '" + netvalue + "' \n"
						+ ", '" + qty + "' \n"
						+ ", '" + price + "' \n"
						+ ", '" + remark + "' \n"
						+ ", '' \n"
						+ ", '" + setImageName + "' \n"
						+ ", CURRENT DATE \n"
						+ ", CURRENT TIME \n"
						+ ", '" + status + "' \n"
						+ ", '" + username + "')";
				// System.out.println("addADRDetail\n" + query);
				stmt.execute(query);

				if (imagename != null) {
					String uploadFilePath = null;
					// String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
					String filePath = "D:\\files\\api_project\\adr_images\\";
					// System.out.println("filePath: " + filePath + setImageName);
					uploadFilePath = FileUtillity.writeToFileServer(imagefile, setImageName, filePath);

				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "complete");
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

	public static String addOrderHead(String cono, String divi, String customerno, String orderdate, String delidate,
			String round, String pricelist, String ordertype, String whs, String status, String type, String remark,
			String day, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String getYear = delidate.substring(2, 4);
				String getOrderNumber = SelectData.getMaxOrderNumber(cono, divi, getYear);
				if (Integer.valueOf(getOrderNumber) == 0) {// Create new ordernumber
					getOrderNumber = getYear + "00000001";

				}

				String saleSupport = SelectData.getSalesuport(cono, divi, userid);
				// System.out.println(getOrderNumber + " : " + getOrderNumber);

				String query = "INSERT INTO " + DBNAME + ".M3_TAKEORDERHEAD \n" +
						"(THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS) \n"
						+
						"VALUES('" + cono + "' \n" +
						", '" + divi + "' \n" +
						", '" + getOrderNumber + "' \n" +
						", '" + ordertype + "' \n" +
						", '" + whs + "' \n" +
						", '' \n" +
						", '" + orderdate + "' \n" +
						", '" + delidate + "' \n" +
						", '" + customerno + "' \n" +
						", '" + round + "' \n" +
						", '" + pricelist + "' \n" +
						", '' \n" +
						", '' \n" +
						", '' \n" +
						", '' \n" +
						", '" + userid + "' \n" +
						", '" + saleSupport + "' \n" +
						", '" + remark + "' \n" +
						", '' \n" +
						", '" + status + "' \n" +
						", CURRENT DATE \n" +
						", CURRENT TIME \n" +
						", '" + userid + "')";
				// System.out.println("addOrderHead\n" + query);
				stmt.execute(query);

				if (type.equals("create")) {
					addItemDetailHistory(cono, divi, getOrderNumber, customerno, day, userid);
				} else {
					addItemDetailCustomer(cono, divi, getOrderNumber, customerno, userid);
				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", getOrderNumber);
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

	public static String addOrderHeadV2(String cono, String divi, String customerno, String orderdate, String delidate,
			String round, String pricelist, String ordertype, String whs, String status, String type, String remark,
			String pono, String day, String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String getYear = delidate.substring(2, 4);
				String saleSupport = SelectData.getSalesuport(cono, divi, userid);

				// String getOrderNumber = SelectData.getMaxOrderNumber(cono, divi, getYear);
				// if (Integer.valueOf(getOrderNumber) == 0) {// Create new ordernumber
				// getOrderNumber = getYear + "00000001";
				//
				// }

				remark = ConvertString.convertApostrophe(remark);
				pono = ConvertString.convertApostrophe(pono);

				String query = "INSERT INTO " + DBNAME + ".M3_TAKEORDERHEAD \n" +
						"(THCONO, THDIVI, THORNO, THORTY, THWHLO, THCOCE, THORDA, THDEDA, THCUNO, THROUN, THPRIC, THCHAN, THNCHA, THGROU, THAREA, THSANO, THSASU, THREM1, THREM2, THSTAS, THENDA, THENTI, THENUS) \n"
						+
						"VALUES('" + cono + "' \n" +
						", '" + divi + "' \n" +
						", (SELECT CASE WHEN CAST(MAX(THORNO) AS DECIMAL(10,0)) > 0 THEN CAST(MAX(THORNO) AS DECIMAL(10,0)) + 1 ELSE CAST(('"
						+ getYear + "' || '00000001') AS DECIMAL(10,0)) END AS THORNO \n" +
						"FROM " + DBNAME + ".M3_TAKEORDERHEAD \n" +
						"WHERE THCONO = '" + cono + "' \n" +
						"AND THDIVI = '" + divi + "'  \n" +
						"AND SUBSTRING(THORNO,0,3) = '" + getYear + "') \n" +
						", '" + ordertype + "' \n" +
						", '" + whs + "' \n" +
						", '' \n" +
						", '" + orderdate + "' \n" +
						", '" + delidate + "' \n" +
						", '" + customerno + "' \n" +
						", '" + round + "' \n" +
						", '" + pricelist + "' \n" +
						", '' \n" +
						", '' \n" +
						", '' \n" +
						", '' \n" +
						", '" + userid + "' \n" +
						", '" + saleSupport + "' \n" +
						", '" + remark + "' \n" +
						", '" + pono + "' \n" +
						", '" + status + "' \n" +
						", CURRENT DATE \n" +
						", CURRENT TIME \n" +
						", '" + userid + "')";
				// System.out.println("addOrderHeadV2\n" + query);
				stmt.execute(query);

				String getTakeOrderNumber = SelectData.getMaxOrderNumberV3(cono, divi, getYear, userid);
				String getOrderNumber = getTakeOrderNumber.toLowerCase().split(":")[0];

				if (type.equals("create")) {
					addItemDetailHistoryV4(cono, divi, getOrderNumber, customerno, day, userid);
				} else {
					addItemDetailCustomerV4(cono, divi, getOrderNumber, customerno, userid);
				}

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", getTakeOrderNumber);
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

	public static String addItemDetailSupport(String cono, String divi, String orderno, String customerno, String line,
			String itemno, String desc1, String desc2, String qty, String unit, String remark, String status,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" +
						"(TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+
						"VALUES('" + cono + "' \n" +
						", '" + divi + "' \n" +
						", '" + orderno + "' \n" +
						", '' \n" +
						", '' \n" +
						", (SELECT COALESCE(MAX(TDLINE),0) + 1 \n" +
						"FROM " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" +
						"WHERE TDCONO = '" + cono + "' \n" +
						"AND TDDIVI = '" + divi + "' \n" +
						"AND TDORNO = '" + orderno + "') \n" +
						", '" + itemno + "' \n" +
						", '" + qty + "' \n" +
						", '" + unit + "' \n" +
						", '" + remark + "' \n" +
						", '' \n" +
						", '" + status + "' \n" +
						", CURRENT DATE \n" +
						", CURRENT TIME \n" +
						", '" + userid + "')";
				// System.out.println("addItemDetail\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailToSupport(String cono, String divi, String orderno) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME + ".M3_TAKEORDERDETAILSUPPORT \n" +
						"(TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS ) \n"
						+
						"SELECT TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS  \n"
						+
						"FROM " + DBNAME + ".M3_TAKEORDERDETAIL \n" +
						"WHERE TDCONO = '" + cono + "' \n" +
						"AND TDDIVI = '" + divi + "' \n" +
						"AND TDORNO = '" + orderno + "'  \n" +
						"AND TDIQTY > 0";
				// System.out.println("addItemDetailSupport\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailHistoryV2(String cono, String divi, String orderno, String customerno, String day,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, MMUNMS, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" +
						"FROM  \n" +
						"(SELECT A.OACONO,A.CHANEL,A.CUSTGROUP,A.OAORTP,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS \n"
						+
						"FROM \n" +
						"(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN,OBSAPR PRICE,OBLNAM,UAIVNO,UAIVDT \n"
						+
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".OCUSMA," + DBM3NAME
						+ ".MITMAS," + DBM3NAME + ".CSYTAB," + DBM3NAME + ".ODHEAD \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OACONO = UACONO \n" +
						"AND OAORNO = UAORNO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" +
						"AND DATE(SUBSTRING(CHAR(OAORDT),1,4) || '-' || SUBSTRING(CHAR(OAORDT),5,2) || '-' || SUBSTRING(CHAR(OAORDT),7,2)) BETWEEN CURRENT_DATE - "
						+ day + " DAYS AND CURRENT_DATE \n" +
						"AND OACUNO = '" + customerno + "' \n" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO = 'SMCD') AS  A \n" +
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OAORTP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS) AS A \n"
						+
						"LEFT JOIN  \n" +
						"(SELECT MMCONO, MMITNO, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+
						"FROM \n" +
						"(SELECT MMCONO, MMITNO,CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN \n" +
						"FROM " + DBM3NAME + ".MITMAS \n" +
						"WHERE MMCONO = '" + cono + "' \n" +
						"AND MMSTAT = '20'  \n" +
						"AND SUBSTRING(MMITNO,0,2) BETWEEN 'A' AND 'Z') AS A \n" +
						"LEFT JOIN \n" +
						"(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" +
						"FROM " + DBM3NAME + ".MITAUN \n" +
						"WHERE MUAUS2 = '1') AS B \n" +
						"ON B.MUCONO = A.MMCONO \n" +
						"AND B.MUITNO = A.MMITNO) AS B \n" +
						"ON B.MMCONO = A.OACONO \n" +
						"AND B.MMITNO = A.OBITNO \n" +
						"GROUP BY OBITNO,MMUNMS";
				// System.out.println("addItemDetailHistoryV2\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailHistoryV3(String cono, String divi, String orderno, String customerno, String day,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, OBALUN, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" +
						"FROM \n" +
						"(SELECT A.OACONO,A.CHANEL,A.CUSTGROUP,A.OAORTP,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS,A.OBALUN \n"
						+
						"FROM \n" +
						"(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN,OBSAPR PRICE,OBLNAM,UAIVNO,UAIVDT,OBORST \n"
						+
						"FROM " + DBM3NAME + ".OOHEAD a," + DBM3NAME + ".OOLINE b," + DBM3NAME + ".OCUSMA c," + DBM3NAME
						+ ".MITMAS d," + DBM3NAME + ".CSYTAB e," + DBM3NAME + ".ODHEAD f \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OACONO = UACONO \n" +
						"AND OAORNO = UAORNO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','A87','F31','F11') \n" +
						"AND OBORST IN ('27','77') \n" +
						"AND DATE(SUBSTRING(CHAR(OAORDT),1,4) || '-' || SUBSTRING(CHAR(OAORDT),5,2) || '-' || SUBSTRING(CHAR(OAORDT),7,2)) BETWEEN CURRENT_DATE - "
						+ day + " DAYS AND CURRENT_DATE \n" +
						"AND OACUNO = '" + customerno + "' \n" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO = 'SMCD' \n" +
						"AND MMITTY IN ('FG','SP','RM')) AS  A \n" +
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OAORTP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS, OBALUN) AS A \n"
						+
						"LEFT JOIN  \n" +
						"(SELECT MMCONO, MMITNO, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+
						"FROM \n" +
						"(SELECT MMCONO, MMITNO,CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN \n" +
						"FROM " + DBM3NAME + ".MITMAS \n" +
						"WHERE MMCONO = '" + cono + "' \n" +
						"AND MMSTAT = '20'  \n" +
						"AND SUBSTRING(MMITNO,0,2) BETWEEN 'A' AND 'Z') AS A \n" +
						"LEFT JOIN \n" +
						"(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" +
						"FROM " + DBM3NAME + ".MITAUN \n" +
						"WHERE MUAUS2 = '1') AS B \n" +
						"ON B.MUCONO = A.MMCONO \n" +
						"AND B.MUITNO = A.MMITNO) AS B \n" +
						"ON B.MMCONO = A.OACONO \n" +
						"AND B.MMITNO = A.OBITNO \n" +
						// "INNER JOIN \n" +
						// "(SELECT MLCONO,MLITNO \n" +
						// "FROM "+DBM3NAME+".MITLOC \n" +
						// "GROUP BY MLCONO,MLITNO) AS C \n" +
						// "ON C.MLCONO = A.OACONO \n" +
						// "AND C.MLITNO = A.OBITNO \n" +
						"GROUP BY OBITNO,OBALUN,MMUNMS";
				// System.out.println("addItemDetailHistoryV3\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailHistoryV4(String cono, String divi, String orderno, String customerno, String day,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, OBALUN, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" +
						"FROM \n" +
						"(SELECT A.OACONO,A.CHANEL,A.CUSTGROUP,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS,A.OBALUN \n"
						+
						"FROM \n" +
						"(SELECT OACONO, CHANEL, CUSTGROUP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS, OBALUN \n"
						+ "FROM \n"
						+ "(SELECT A.OACONO,A.CHANEL,A.CUSTGROUP,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS--,A.OBALUN \n"
						+ ", CASE WHEN MMFUDS LIKE '%เลือด%' THEN 'PCS' WHEN MMFUDS LIKE '%เป็ดเชอร์รี่%' THEN 'PCS' ELSE A.OBALUN END AS OBALUN \n"
						+ "FROM \n"
						+ "(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN,OBSAPR PRICE,OBLNAM,UAIVNO,UAIVDT,OBORST \n"
						+ "FROM " + DBM3NAME + ".OOHEAD a," + DBM3NAME + ".OOLINE b," + DBM3NAME + ".OCUSMA c,"
						+ DBM3NAME + ".MITMAS d," + DBM3NAME + ".CSYTAB e," + DBM3NAME + ".ODHEAD f \n"
						+ "WHERE OACONO = OBCONO \n"
						+ "AND OAORNO = OBORNO \n"
						+ "AND OACONO = '" + cono + "' \n"
						+ "AND OADIVI = '" + divi + "' \n"
						+ "AND OACUNO = OKCUNO \n"
						+ "AND OACONO = OKCONO \n"
						+ "AND OACONO = MMCONO \n"
						+ "AND OBITNO = MMITNO \n"
						+ "AND CTCONO = OACONO \n"
						+ "AND OACONO = UACONO \n"
						+ "AND OAORNO = UAORNO \n"
						+ "AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','A87','F31','F11') \n"
						+ "AND OBORST IN ('27','77','79') \n"
						+ "AND DATE(SUBSTRING(CHAR(OAORDT),1,4) || '-' || SUBSTRING(CHAR(OAORDT),5,2) || '-' || SUBSTRING(CHAR(OAORDT),7,2)) BETWEEN CURRENT_DATE - "
						+ day + " DAYS AND CURRENT_DATE \n"
						+ "AND OACUNO = '" + customerno + "' \n"
						+ "AND CTSTKY = OASMCD \n"
						+ "AND CTSTCO = 'SMCD' \n"
						+ "AND MMITTY IN ('FG','SP','RM')) AS  A \n"
						+ "GROUP BY OACONO, CHANEL, CUSTGROUP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS, OBALUN) AS AA \n"
						+ "GROUP BY OACONO, CHANEL, CUSTGROUP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS, OBALUN) AS  A \n"
						+
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS, OBALUN) AS A \n"
						+
						"LEFT JOIN  \n" +
						"(SELECT MMCONO, MMITNO, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+
						"FROM \n" +
						"(SELECT MMCONO, MMITNO,CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN \n" +
						"FROM " + DBM3NAME + ".MITMAS \n" +
						"WHERE MMCONO = '" + cono + "' \n" +
						"AND MMSTAT = '20'  \n" +
						"AND SUBSTRING(MMITNO,0,2) BETWEEN 'A' AND 'Z') AS A \n" +
						"LEFT JOIN \n" +
						"(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" +
						"FROM " + DBM3NAME + ".MITAUN \n" +
						"WHERE MUAUS2 = '1') AS B \n" +
						"ON B.MUCONO = A.MMCONO \n" +
						"AND B.MUITNO = A.MMITNO) AS B \n" +
						"ON B.MMCONO = A.OACONO \n" +
						"AND B.MMITNO = A.OBITNO \n" +
						"GROUP BY OBITNO,OBALUN,MMUNMS";
				// System.out.println("addItemDetailHistoryV3\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailHistory(String cono, String divi, String orderno, String customerno, String day,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, UNIT, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" + // MAX(QTY) AS QTYUNIT
						"FROM  \n" +
						"(SELECT A.CHANEL,A.CUSTGROUP,A.OAORTP,A.OACONO,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS,COALESCE(A.QTY,0) AS QTY,COALESCE(INVQTY,0) AS INVQTY,COALESCE(INVCAWE,0) AS INVCAWE,A.UNIT \n"
						+
						"FROM \n" +
						"(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN UNIT,OBSAPR PRICE,OBLNAM,UAIVNO,UAIVDT \n"
						+
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".OCUSMA," + DBM3NAME
						+ ".MITMAS," + DBM3NAME + ".CSYTAB," + DBM3NAME + ".ODHEAD \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OACONO = UACONO \n" +
						"AND OAORNO = UAORNO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" +
						"AND DATE(SUBSTRING(CHAR(OAORDT),1,4) || '-' || SUBSTRING(CHAR(OAORDT),5,2) || '-' || SUBSTRING(CHAR(OAORDT),7,2)) BETWEEN CURRENT_DATE - "
						+ day + " DAYS AND CURRENT_DATE \n" +
						"AND OACUNO = '" + customerno + "' \n" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO ='SMCD') A  \n" +
						"LEFT JOIN \n" +
						"(SELECT SUM(MTTRQT) AS INVQTY,SUM(MTCAWE) AS INVCAWE,MTRIDN,MTCONO,MTITNO \n" +
						"FROM " + DBM3NAME + ".MITTRA \n" +
						"GROUP BY MTRIDN,MTCONO,MTITNO) B \n" +
						"ON A.OACONO = B.MTCONO \n" +
						"AND A.OBITNO = B.MTITNO \n" +
						"AND A.OAORNO = B.MTRIDN) AS detail \n" +
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OAORTP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS, UNIT";
				// System.out.println("addItemDetailHistory\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailCustomerV2(String cono, String divi, String orderno, String customerno,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, MMUNMS, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" +
						"FROM  \n" +
						"(SELECT A.OACONO,A.CHANEL,A.CUSTGROUP,A.OAORTP,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS \n"
						+
						"FROM \n" +
						"(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN,OBSAPR PRICE,OBLNAM,UAIVNO,UAIVDT \n"
						+
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".OCUSMA," + DBM3NAME
						+ ".MITMAS," + DBM3NAME + ".CSYTAB," + DBM3NAME + ".ODHEAD \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OACONO = UACONO \n" +
						"AND OAORNO = UAORNO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" +
						// "AND DATE(SUBSTRING(CHAR(OAORDT),1,4) || '-' || SUBSTRING(CHAR(OAORDT),5,2)
						// || '-' || SUBSTRING(CHAR(OAORDT),7,2)) BETWEEN CURRENT_DATE - "+day+" DAYS
						// AND CURRENT_DATE \n" +
						"AND OACUNO = '" + customerno + "' \n" +
						"AND OAORNO = (SELECT MAX(OAORNO) \n" +
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" +
						"AND OACUNO = '" + customerno + "')" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO = 'SMCD' \n" +
						"AND MMITTY IN ('FG','SP','RM')) AS A \n" +
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OAORTP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS) AS A \n"
						+
						"LEFT JOIN  \n" +
						"(SELECT MMCONO, MMITNO, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+
						"FROM \n" +
						"(SELECT MMCONO, MMITNO,CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN \n" +
						"FROM " + DBM3NAME + ".MITMAS \n" +
						"WHERE MMCONO = '10' \n" +
						"AND MMSTAT = '20'  \n" +
						// "AND MMITTY = 'FG' \n" +
						"AND SUBSTRING(MMITNO,0,2) BETWEEN 'A' AND 'Z') AS A \n" +
						"LEFT JOIN \n" +
						"(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" +
						"FROM " + DBM3NAME + ".MITAUN \n" +
						"WHERE MUAUS2 = '1') AS B \n" +
						"ON B.MUCONO = A.MMCONO \n" +
						"AND B.MUITNO = A.MMITNO) AS B \n" +
						"ON B.MMCONO = A.OACONO \n" +
						"AND B.MMITNO = A.OBITNO \n" +
						"GROUP BY OBITNO,MMUNMS";
				// System.out.println("addItemDetailCustomerV2\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailCustomerV3(String cono, String divi, String orderno, String customerno,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, MMUNMS, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" +
						"FROM  \n" +
						"(SELECT A.OACONO,A.CHANEL,A.CUSTGROUP,A.OAORTP,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS \n"
						+
						"FROM \n" +
						"(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN,OBSAPR PRICE,OBLNAM \n"
						+
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".OCUSMA," + DBM3NAME
						+ ".MITMAS," + DBM3NAME + ".CSYTAB \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','A87','F31','F11') \n" +
						"AND OAORNO = (SELECT MAX(OAORNO) \n" +
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" +
						"AND OACUNO = '" + customerno + "')" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO = 'SMCD' \n" +
						"AND MMITTY IN ('FG','SP','RM')) AS A \n" +
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OAORTP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS) AS A \n"
						+
						"LEFT JOIN  \n" +
						"(SELECT MMCONO, MMITNO, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+
						"FROM \n" +
						"(SELECT MMCONO, MMITNO,CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN \n" +
						"FROM " + DBM3NAME + ".MITMAS \n" +
						"WHERE MMCONO = '10' \n" +
						"AND MMSTAT = '20'  \n" +
						// "AND MMITTY = 'FG' \n" +
						"AND SUBSTRING(MMITNO,0,2) BETWEEN 'A' AND 'Z') AS A \n" +
						"LEFT JOIN \n" +
						"(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" +
						"FROM " + DBM3NAME + ".MITAUN \n" +
						"WHERE MUAUS2 = '1') AS B \n" +
						"ON B.MUCONO = A.MMCONO \n" +
						"AND B.MUITNO = A.MMITNO) AS B \n" +
						"ON B.MMCONO = A.OACONO \n" +
						"AND B.MMITNO = A.OBITNO \n" +
						"GROUP BY OBITNO,MMUNMS";
				// System.out.println("addItemDetailCustomerV2\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailCustomerV4(String cono, String divi, String orderno, String customerno,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, MMUNMS, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" +
						"FROM  \n" +
						"(SELECT A.OACONO,A.CHANEL,A.CUSTGROUP,A.OAORTP,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS \n"
						+
						"FROM \n" +
						"(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN,OBSAPR PRICE,OBLNAM \n"
						+
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".OCUSMA," + DBM3NAME
						+ ".MITMAS," + DBM3NAME + ".CSYTAB \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','A87','F31','F11') \n" +
						"AND OAORNO = (SELECT MAX(OAORNO) AS OAORNO \n"
						+ "FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE \n"
						+ "WHERE OACONO = OBCONO \n"
						+ "AND OAORNO = OBORNO \n"
						+ "AND OACONO = '" + cono + "' \n"
						+ "AND OADIVI = '" + divi + "' \n"
						+ "AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','A87','F31','F11') \n"
						+ "AND OACUNO = '" + customerno + "' \n"
						+ "AND OAORDT = (SELECT MAX(OAORDT) AS OAORDT \n"
						+ "FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE \n"
						+ "WHERE OACONO = OBCONO \n"
						+ "AND OAORNO = OBORNO \n"
						+ "AND OACONO = '" + cono + "' \n"
						+ "AND OADIVI = '" + divi + "' \n"
						+ "AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','A87','F31','F11') \n"
						+ "AND OACUNO = '" + customerno + "' \n"
						+ "AND OBORST IN ('27','77','79'))) \n" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO = 'SMCD' \n" +
						"AND MMITTY IN ('FG','SP','RM')) AS A \n" +
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OAORTP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS) AS A \n"
						+
						"LEFT JOIN  \n" +
						"(SELECT MMCONO, MMITNO, CASE WHEN COALESCE(MUAUS2,0) = '1' THEN MUALUN ELSE MMPUUN END AS MMUNMS \n"
						+
						"FROM \n" +
						"(SELECT MMCONO, MMITNO,CASE WHEN MMACTI = '2' THEN 'PCS' ELSE MMPUUN END AS MMPUUN \n" +
						"FROM " + DBM3NAME + ".MITMAS \n" +
						"WHERE MMCONO = '10' \n" +
						"AND MMSTAT = '20'  \n" +
						// "AND MMITTY = 'FG' \n" +
						"AND SUBSTRING(MMITNO,0,2) BETWEEN 'A' AND 'Z') AS A \n" +
						"LEFT JOIN \n" +
						"(SELECT DISTINCT MUCONO,MUITNO,MUALUN,MUCOFA,MUDMCF,MUAUS2 \n" +
						"FROM " + DBM3NAME + ".MITAUN \n" +
						"WHERE MUAUS2 = '1') AS B \n" +
						"ON B.MUCONO = A.MMCONO \n" +
						"AND B.MUITNO = A.MMITNO) AS B \n" +
						"ON B.MMCONO = A.OACONO \n" +
						"AND B.MMITNO = A.OBITNO \n" +
						"GROUP BY OBITNO,MMUNMS";
				// System.out.println("addItemDetailCustomerV4\n" + query);
				stmt.execute(query);

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

	public static String addItemDetailCustomer(String cono, String divi, String orderno, String customerno,
			String userid) throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO " + DBNAME
						+ ".M3_TAKEORDERDETAIL (TDCONO, TDDIVI, TDORNO, TDCONU, TDIVNO, TDLINE, TDITNO, TDIQTY, TDUNIT, TDREM1, TDREM2, TDSTAS, TDENDA, TDENTI, TDENUS) \n"
						+ "SELECT '" + cono + "', '" + divi + "', '" + orderno
						+ "','','',ROW_NUMBER() OVER(ORDER BY OBITNO) AS LINE, OBITNO, 0 AS QTYUNIT, UNIT, '', '', '00', CURRENT DATE, CURRENT TIME, '"
						+ userid + "' \n" + // MAX(QTY) AS QTYUNIT
						"FROM  \n" +
						"(SELECT A.CHANEL,A.CUSTGROUP,A.OAORTP,A.OACONO,A.OASMCD,A.CTTX40,A.OACUNO,A.OKCUNM,A.OBITNO,A.MMFUDS,COALESCE(A.QTY,0) AS QTY,COALESCE(INVQTY,0) AS INVQTY,COALESCE(INVCAWE,0) AS INVCAWE,A.UNIT \n"
						+
						"FROM \n" +
						"(SELECT OACONO,OKECAR AS CHANEL,OKCUCL AS CUSTGROUP,OAORTP,OAORNO,OAORDT,CTTX40,OASMCD,OACUNO,OKCUNM,OBITNO,MMFUDS,OBORQA QTY,OBALUN UNIT,OBSAPR PRICE,OBLNAM,UAIVNO,UAIVDT \n"
						+
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".OCUSMA," + DBM3NAME
						+ ".MITMAS," + DBM3NAME + ".CSYTAB," + DBM3NAME + ".ODHEAD \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OACONO = UACONO \n" +
						"AND OAORNO = UAORNO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" +
						// "AND DATE(SUBSTRING(CHAR(OAORDT),1,4) || '-' || SUBSTRING(CHAR(OAORDT),5,2)
						// || '-' || SUBSTRING(CHAR(OAORDT),7,2)) BETWEEN CURRENT_DATE - 7 DAYS AND
						// CURRENT_DATE \n" +
						"AND OACUNO = '" + customerno + "' \n" +
						"AND OAORNO = (SELECT MAX(OAORNO) \n" +
						"FROM " + DBM3NAME + ".OOHEAD," + DBM3NAME + ".OOLINE," + DBM3NAME + ".OCUSMA," + DBM3NAME
						+ ".MITMAS," + DBM3NAME + ".CSYTAB," + DBM3NAME + ".ODHEAD \n" +
						"WHERE OACONO = OBCONO \n" +
						"AND OAORNO = OBORNO \n" +
						"AND OACONO = '" + cono + "' \n" +
						"AND OADIVI = '" + divi + "' \n" +
						"AND OACUNO = OKCUNO \n" +
						"AND OACONO = OKCONO \n" +
						"AND OACONO = MMCONO \n" +
						"AND OBITNO = MMITNO \n" +
						"AND CTCONO = OACONO \n" +
						"AND OACONO = UACONO \n" +
						"AND OAORNO = UAORNO \n" +
						"AND OAWHLO IN ('A11','A80','A81','A82','A83','A84','F31','F11') \n" +
						"AND OACUNO = '" + customerno + "' \n" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO ='SMCD') \n" +
						"AND CTSTKY = OASMCD \n" +
						"AND CTSTCO ='SMCD' \n" +
						"AND AND MMITTY = 'FG') A  \n" +
						"LEFT JOIN \n" +
						"(SELECT SUM(MTTRQT) AS INVQTY,SUM(MTCAWE) AS INVCAWE,MTRIDN,MTCONO,MTITNO \n" +
						"FROM " + DBM3NAME + ".MITTRA \n" +
						"GROUP BY MTRIDN,MTCONO,MTITNO) B \n" +
						"ON A.OACONO = B.MTCONO \n" +
						"AND A.OBITNO = B.MTITNO \n" +
						"AND A.OAORNO = B.MTRIDN) AS detail \n" +
						"GROUP BY OACONO, CHANEL, CUSTGROUP, OAORTP, OASMCD, CTTX40, OACUNO, OKCUNM, OBITNO, MMFUDS, UNIT";
				// System.out.println("addItemDetailCustomer\n" + query);
				stmt.execute(query);

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

	public static String addUser(String cono, String divi, String salesup, String saleman, String channel)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();
		Connection conn = ConnectDB2.doConnect();
		String getSalemanDuplicate = null;
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				getSalemanDuplicate = SelectData.getSalesuport(cono, divi, saleman);
				if (getSalemanDuplicate.equals("n/a")) {

					String query = "INSERT INTO BRLDTA0100.SAL_SUPSALE \n" +
							"(S_CONO, S_SUPP, S_SALE, S_CHAN) \n" +
							"VALUES ('" + cono + "' \n" +
							", '" + salesup + "' \n" +
							", '" + saleman + "' \n" +
							", '" + channel + "')";
					// System.out.println("addGroupingUser\n" + query);
					stmt.execute(query);

					mJsonObj.put("result", "ok");
					mJsonObj.put("message", "Complete.");
					return mJsonObj.toString();
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

		mJsonObj.put("result", "nok");
		mJsonObj.put("message", "Already " + saleman + " belong to " + getSalemanDuplicate);
		return mJsonObj.toString();

	}

	public static String addLogSendEmail(String EDDOCUMENT, String EDREFNO, String ESENTTO, String ESENTCC,
			String ESENTFROM, String EDSUBJECT, String EDDETAIL, String EDSTATUSNO, String CREATEBY, String ECONO,
			String EDIVISION) throws Exception {

		Connection conn = ConnectDB2.doConnect();
		try {
			if (conn != null) {

				Statement stmt = conn.createStatement();

				String query = "INSERT INTO BRLDTA0100.M3_STORAGEEMAILSEND \n"
						+ "(EDDOCUMENT, EDREFNO, ESENTTO, ESENTCC, ESENTFROM, EDSUBJECT, EDDETAIL, EDSTATUSNO, CREATEBY, ECONO, EDIVISION, SENTDATE, SENTTIME) \n"
						+ "VALUES ('" + EDDOCUMENT + "', '" + EDREFNO + "', '" + ESENTTO + "', '" + ESENTCC + "', '"
						+ ESENTFROM + "' \n" + ", '" + EDSUBJECT + "', '" + EDDETAIL + "', '" + EDSTATUSNO + "', '"
						+ CREATEBY + "', '" + ECONO + "', '" + EDIVISION + "', CURRENT DATE, CURRENT TIME) ";
				// System.out.println("addLogSendEmail\n" + query);
				stmt.execute(query);

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

	public static String SCB_IntraDay_WTF(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay_WTF");
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {
			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE ,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_TRANSAC, BM_BRANCH, BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE + "','600', '600', 'SCB', '3313029652', ?,\r\n"
					+ "                                ?, ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					// แทนที่การเรียก getString ด้วยการใช้ toString เพื่อรองรับทุกประเภท
					pstmt.setString(1, row.get("Date").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(2, row.get("Time").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(3, row.get("Cheque_Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(4, row.get("Transfer_Code").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(5, row.get("Branch_Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(6, row.get("Amount").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด

					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String SCB_Historical_WTF(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_Historical");
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println("Received Data: " + tableData);

		System.out.println("statemenType Data: " + statemenType);

		// กำหนดเวลาปัจจุบันในรูปแบบที่ต้องการ

		try {
			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_TRANSAC, BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE + "','600', '600', 'SCB', '3313029652', ?,\r\n"
					+ "                                 ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					// แทนที่การเรียก getString ด้วยการใช้ toString เพื่อรองรับทุกประเภท
					pstmt.setString(1, row.get("Date").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(2, row.get("Time").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(3, row.get("Cheque_Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(4, row.get("Transaction_Code").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(5, row.get("Credit_Amount").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด

					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Description")) {
						pstmt.setString(6, row.get("Description").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String BBL_IntraDay_WTF(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("BBL_IntraDay_WTF");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHANEL ,REF2 ,\r\n"
					+ "			BM_BRANCH  , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','600', '600', 'BBL', '1227360581',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					if (row.has("Value_Date")) {
						pstmt.setString(1, row.get("Value_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Tran_Date")) {
						pstmt.setString(2, row.get("Tran_Date").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Channel")) {
						pstmt.setString(3, row.get("Channel").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Terminal_Id")) {
						pstmt.setString(4, row.get("Terminal_Id").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Branch")) {
						pstmt.setString(5, row.get("Branch").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Credit")) {
						// pstmt.setString(6, row.get("Credit").toString().trim());
						String creditStr = row.get("Credit").toString().trim().replace(",", "");
						pstmt.setBigDecimal(6, new BigDecimal(creditStr));
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String BBL_Historical_WTF(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("BBL_Historical_WTF");
		JSONObject mJsonObj = new JSONObject();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {

			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAME + ".BANK_MAPPING\r\n"
					+ "			(BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, \r\n"
					+ "			BM_DATE,BM_TIME,\r\n"
					+ "			BM_CHANEL ,REF2 ,\r\n"
					+ "			BM_BRANCH  , BM_AMOUNT,\r\n"
					+ "			BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "			VALUES('" + LCODE + "','600', '600', 'BBL', '1227360581',\r\n"
					+ "			?, ?,\r\n"
					+ "			?,?,\r\n"
					+ "			?, ?,\r\n"
					+ "			'" + username + "', '10' , '" + formattedDateTime + "' , ?, '00')";

			System.out.print(insertQuery);
			pstmt = conn.prepareStatement(insertQuery);

			// แปลง tableData ให้เป็น JSONArray (สมมติว่า tableData คือ JSON string
			// ที่คุณได้รับ)
			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {
					if (row.has("Value_Date")) {
						pstmt.setString(1, row.get("Value_Date").toString());
					} else {
						pstmt.setString(1, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Tran_Date")) {
						pstmt.setString(2, row.get("Tran_Date").toString());
					} else {
						pstmt.setString(2, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Channel")) {
						pstmt.setString(3, row.get("Channel").toString());
					} else {
						pstmt.setString(3, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Terminal_Id")) {
						pstmt.setString(4, row.get("Terminal_Id").toString());
					} else {
						pstmt.setString(4, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Branch")) {
						pstmt.setString(5, row.get("Branch").toString());
					} else {
						pstmt.setString(5, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}
					if (row.has("Credit")) {
						String creditStr = row.get("Credit").toString().trim().replace(",", "");
						pstmt.setBigDecimal(6, new BigDecimal(creditStr));
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String SCB_IntraDay_NSD(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_IntraDay_NSD");
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println(tableData);

		try {
			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE ,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_TRANSAC, BM_BRANCH, BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE + "','500', '500', 'SCB', '3313029408', ?,\r\n"
					+ "                                ?, ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					// แทนที่การเรียก getString ด้วยการใช้ toString เพื่อรองรับทุกประเภท
					pstmt.setString(1, row.get("Date").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(2, row.get("Time").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(3, row.get("Cheque_Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(4, row.get("Transaction_Code").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(5, row.get("Branch_Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(6, row.get("Amount").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด

					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Description")) {
						pstmt.setString(7, row.get("Description").toString());
					} else {
						pstmt.setString(7, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

	public static String SCB_Historical_NSD(String username, String statemenType, String tableData, String LCODE)
			throws Exception {
		logger.info("SCB_Historical_NSD");
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println("Received Data: " + tableData);

		System.out.println("statemenType Data: " + statemenType);

		// กำหนดเวลาปัจจุบันในรูปแบบที่ต้องการ

		try {
			System.out.println("////////////////////////////////////////////////////////");
			System.out.println(tableData);
			LocalDateTime currentTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
			String formattedDateTime = currentTime.format(formatter);
			System.out.println(formattedDateTime);
			System.out.println("////////////////////////////////////////////////////////");

			conn = ConnectDB2.doConnect();
			String insertQuery = "INSERT INTO " + DBNAMEPP + ".BANK_MAPPING\r\n"
					+ "                                (BM_LCODE,BM_CONO, BM_DIVI, BM_BANK, BM_ACCODE, BM_DATE,\r\n"
					+ "                                BM_TIME, BM_CHQNO, BM_TRANSAC, BM_AMOUNT,\r\n"
					+ "                                BM_USER, BM_STATUS, BM_TIME1 , BM_DESC , BM_PARENT_STS)\r\n"
					+ "                                VALUES('" + LCODE + "','500', '500', 'SCB', '3313029408', ?,\r\n"
					+ "                                 ?, ?, ?, ?,\r\n"
					+ "                                '" + username + "', '10' , '" + formattedDateTime.toString()
					+ "' , ? , '00')";

			pstmt = conn.prepareStatement(insertQuery);

			JSONArray jsonArray = new JSONArray(tableData);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject row = jsonArray.getJSONObject(i);

				try {

					// แทนที่การเรียก getString ด้วยการใช้ toString เพื่อรองรับทุกประเภท
					pstmt.setString(1, row.get("Date").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(2, row.get("Time").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(3, row.get("Cheque_Number").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(4, row.get("Transaction_Code").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด
					pstmt.setString(5, row.get("Credit_Amount").toString()); // แปลงเป็น String ไม่ว่าจะเป็นประเภทใด

					// เช็คว่ามีคีย์ Description หรือไม่
					if (row.has("Description")) {
						pstmt.setString(6, row.get("Description").toString());
					} else {
						pstmt.setString(6, "-"); // ถ้าไม่มี Description ให้ค่าเริ่มต้น
					}

					pstmt.executeUpdate(); // เพิ่มข้อมูลในแต่ละรอบ
				} catch (SQLException ex) {
					logger.warn("Error inserting row: " + row + ". Error: " + ex.getMessage());
					// ข้ามไปยังรอบถัดไปในกรณีที่แทรกข้อมูลไม่ได้
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected error: " + e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				logger.error("Error closing PreparedStatement: " + e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error("Error closing Connection: " + e.getMessage());
			}
		}

		return null;
	}

}
