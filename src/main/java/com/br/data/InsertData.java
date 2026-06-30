package com.br.data;

import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.sql.ResultSet; // <--- สำคัญตรงนี้

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.codehaus.jettison.json.JSONObject;
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


import com.br.connection.ConnectDB2;
import com.br.utility.Constant;
import com.br.utility.ConvertString;
import com.br.utility.FileUtillity;
import com.br.utility.HttpConnection;
import com.sun.jersey.multipart.FormDataBodyPart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import groovy.ui.Console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertData {

  private static final String UPLOAD_FOLDER = "D:/uploads/";


  private static final Logger logger = LogManager.getLogger(InsertData.class);

  protected static String DBNAME = Constant.DBNAME;
  protected static String DBM3NAME = Constant.DBM3NAME;

  // public static String SR_HEAD = Constant.SR_HEAD;
  // public static String SR_DETAIL = Constant.SR_DETAIL;
  // public static String SR_APPROVE = Constant.SR_APPROVE;
  // public static String SR_FLOW = Constant.SR_FLOW;
  // public static String SR_GROUP = Constant.SR_GROUP;

  // protected static String DBNAMEPP = "BRLDTA0100";
  // protected static String DBNAMEPP = "BRLDTABK01";
  protected static String DBNAMEPP = "" + DBNAME + "";

  static DecimalFormat df0 = new DecimalFormat("0");
  static DecimalFormat df2 = new DecimalFormat("#.##");
  static DecimalFormat df3 = new DecimalFormat("#.###");
  static DecimalFormat df4 = new DecimalFormat("#.####");

  ////////////////////////// BANKMAPPING ////////////////////////////////


  // public static String insertFILETODB(
  // String cono,
  // String divi,
  // String vID,
  // String filename,
  // String filetype,
  // String fieldname,
  // String username,
  // int fileIndex,
  // String originalFileName
  // ) throws Exception {
  //
  // logger.info("insertFILETODB");
  // JSONObject mJsonObj = new JSONObject();
  // Connection conn = null;
  // PreparedStatement ps = null;
  // ResultSet rs = null;
  //
  // try {
  // conn = ConnectDB2.doConnect();
  //
  // // ---- 1) สร้าง currentID ----
  //
  // // ---- 2) INSERT ----
  // String selectQuery = ""
  // + "SELECT FILINE "
  // + "FROM " + DBNAME +".SR_FILE "
  // + "WHERE FICONO = ? AND FIDIVI = ? AND FISRNO = ? AND FISNAM = ?";
  //
  // try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
  // selectStmt.setString(1, cono);
  // selectStmt.setString(2, divi);
  // selectStmt.setString(3, vID);
  // selectStmt.setString(4, fieldname);
  //
  // try (ResultSet selectRs = selectStmt.executeQuery()) {
  // if (selectRs.next()) {
  // String updateQuery = ""
  // + "UPDATE " + DBNAME +".SR_FILE "
  // + "SET FIFNAM = ?, FITYPE = ?, FIENDA = CURRENT DATE, FIENTI = CURRENT TIME, FIENUS = ?, FILINE
  // = ? , FIREM1 = ?"
  // + "WHERE FICONO = ? AND FIDIVI = ? AND FISRNO = ? AND FISNAM = ?";
  //
  // try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
  // updateStmt.setString(1, originalFileName);
  // updateStmt.setString(2, filetype);
  // updateStmt.setString(3, username);
  // updateStmt.setInt(4, fileIndex);
  // updateStmt.setString(5, filename);
  //
  // updateStmt.setString(6, cono);
  // updateStmt.setString(7, divi);
  // updateStmt.setString(8, vID);
  // updateStmt.setString(9, fieldname);
  // updateStmt.executeUpdate();
  // }
  //
  // mJsonObj.put("result", "ok");
  // mJsonObj.put("action", "update");
  // logger.debug("Updated SR_FILE record for " + vID + " / " + fieldname);
  //
  // return mJsonObj.toString();
  // }
  // }
  // }
  //
  //
  // String insertQuery = "\r\n"
  // + "INSERT INTO " + DBNAME +".SR_FILE\r\n"
  // + "(FICONO, FIDIVI,FICODE , FISRNO, FISNAM,FIFNAM ,FITYPE ,FIENDA,FIENTI,FIENUS,FILINE, FIREM1
  // ) \r\n"
  // + "VALUES (?, ?, ?, ?, ?, ?, ?,CURRENT DATE, CURRENT TIME,? , ? , ?)";
  //
  // logger.debug("Insert Query: " + insertQuery);
  //
  // ps = conn.prepareStatement(insertQuery);
  // ps.setString(1, cono);
  // ps.setString(2, divi);
  // ps.setString(3, "SWRQ");
  // ps.setString(4, vID);
  // ps.setString(5, fieldname);
  // ps.setString(6, originalFileName);
  // ps.setString(7, filetype);
  // ps.setString(8, username);
  // ps.setInt(9, fileIndex);
  // ps.setString(10, filename);
  //
  //
  //
  // ps.executeUpdate();
  //
  // mJsonObj.put("result", "ok");
  //
  // return mJsonObj.toString();
  //
  // } catch (SQLException e) {
  // logger.error("SQL Error: " + e.getMessage());
  // mJsonObj.put("result", "nok");
  // mJsonObj.put("message", e.getMessage());
  // return mJsonObj.toString();
  //
  // } finally {
  // try { if (rs != null) rs.close(); } catch (Exception ignored) {}
  // try { if (ps != null) ps.close(); } catch (Exception ignored) {}
  // try { if (conn != null) conn.close(); } catch (Exception ignored) {}
  // }
  // }
  //
  //


  public static String insertFILETODB(String cono, String divi, String vID, String filename,
      String filetype, String fieldname, String username, int fileIndex, String originalFileName)
      throws Exception {

    logger.info("insertFILETODB");
    JSONObject mJsonObj = new JSONObject();
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
      conn = ConnectDB2.doConnect();
      stmt = conn.createStatement();

      // ---- Escape single quotes (IMPORTANT) ----
      cono = cono.replace("'", "''");
      divi = divi.replace("'", "''");
      vID = vID.replace("'", "''");
      filename = filename.replace("'", "''");
      filetype = filetype.replace("'", "''");
      fieldname = fieldname.replace("'", "''");
      username = username.replace("'", "''");
      originalFileName = originalFileName.replace("'", "''");

      // ---- SELECT ----
      String selectQuery = "SELECT FILINE FROM " + DBNAME + ".SR_FILE WHERE " + "FICONO = '" + cono
          + "' " + "AND FIDIVI = '" + divi + "' " + "AND FISRNO = '" + vID + "' " + "AND FISNAM = '"
          + fieldname + "'";

      rs = stmt.executeQuery(selectQuery);

      // ---- IF EXISTS → UPDATE ----
      if (rs.next()) {

        String updateQuery = "UPDATE " + DBNAME + ".SR_FILE SET " + "FIFNAM = '" + originalFileName
            + "', " + "FITYPE = '" + filetype + "', " + "FIENDA = CURRENT DATE, "
            + "FIENTI = CURRENT TIME, " + "FIENUS = '" + username + "', " + "FILINE = " + fileIndex
            + ", " + "FIREM1 = '" + filename + "' " + "WHERE FICONO = '" + cono + "' "
            + "AND FIDIVI = '" + divi + "' " + "AND FISRNO = '" + vID + "' " + "AND FISNAM = '"
            + fieldname + "'";

        stmt.executeUpdate(updateQuery);

        mJsonObj.put("result", "ok");
        mJsonObj.put("action", "update");
        logger.debug("Updated SR_FILE record for " + vID + " / " + fieldname);

        return mJsonObj.toString();
      }

      // ---- INSERT ----
      String insertQuery = "INSERT INTO " + DBNAME + ".SR_FILE "
          + "(FICONO, FIDIVI, FICODE, FISRNO, FISNAM, FIFNAM, FITYPE, FIENDA, FIENTI, FIENUS, FILINE, FIREM1) VALUES ("
          + "'" + cono + "', " + "'" + divi + "', " + "'SWRQ', " + "'" + vID + "', " + "'"
          + fieldname + "', " + "'" + originalFileName + "', " + "'" + filetype + "', "
          + "CURRENT DATE, " + "CURRENT TIME, " + "'" + username + "', " + fileIndex + ", " + "'"
          + filename + "'" + ")";

      stmt.executeUpdate(insertQuery);

      mJsonObj.put("result", "ok");
      return mJsonObj.toString();

    } catch (SQLException e) {
      logger.error("SQL Error: " + e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
      return mJsonObj.toString();

    } finally {
      try {
        if (rs != null)
          rs.close();
      } catch (Exception ignored) {
      }
      try {
        if (stmt != null)
          stmt.close();
      } catch (Exception ignored) {
      }
      try {
        if (conn != null)
          conn.close();
      } catch (Exception ignored) {
      }
    }
  }

  public static JSONObject uploadTempFiles(String base64FileData, String username, String depthead)
      throws Exception {
    JSONObject result = new JSONObject();

    Connection conn = null;
    PreparedStatement pstmt = null;

    try {
      conn = ConnectDB2.doConnect();

      // Decode base64 ไฟล์
      String base64Data =
          base64FileData.contains(",") ? base64FileData.split(",")[1] : base64FileData;
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
      String sql =
          "INSERT INTO BRLDTABK01.SR_FILEUPLOAD (original_name, stored_name, path, mime_type, size, uploaded_by) VALUES (?, ?, ?, ?, ?, ?)";

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
      mJsonObj.put("message", "Can't create Service number, Please update your version to "
          + checkVersion + " :  " + version + " (Click F5 button).");
      return mJsonObj.toString();

    }



    logger.debug("ID programtype: " + programtype);

    logger.debug("company: " + company);
    logger.debug("warehouse2: " + warehouse2);
    logger.debug("vRemark: " + vRemark2);

    Map<String, String[]> companyMapping = new HashMap<>();
    companyMapping.put("10", new String[] {"10", "101"});
    companyMapping.put("600", new String[] {"600", "600"});
    companyMapping.put("500", new String[] {"500", "500"});
    // เพิ่มได้เรื่อยๆ เช่น
    // companyMapping.put("300", new String[] { "300", "301" });

    // ดึงข้อมูลตาม company
    String[] mapping = companyMapping.getOrDefault(company, new String[] {company, company});
    String comcono = mapping[0];
    String comdivi = mapping[1];

    logger.debug("cono: " + comcono);
    logger.debug("divi: " + comdivi);

    try {
      conn = ConnectDB2.doConnect();
      stmt = conn.createStatement();
      Statement stmt2 = conn.createStatement();

      // สร้าง currentID จาก query

      /*
       * String idQuery =
       * "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
       * 
       * + "FROM " + DBNAME + ".SR_FLOWDETAIL \n" +
       * "WHERE SUBSTR(FDSRNO, 1, 2) = '25' AND FDCONO  = '" + comcono + "' AND　FDDIVI = '" +
       * comdivi + "' AND FDCODE ='ITRQ'  ";
       */

      String idQuery = "SELECT RIGHT(YEAR(CURRENT_DATE), 2)\r\n"
          + "|| RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), RIGHT(YEAR(CURRENT_DATE),2) || '000000'), 3)) + 1), 6)\r\n"
          + "AS CURRENT_ID\r\n" + "FROM " + DBNAME + ".SR_FLOWDETAIL\r\n"
          + "WHERE SUBSTR(FDSRNO, 1, 2) = RIGHT(YEAR(CURRENT_DATE), 2)\r\n" + "AND FDCONO = '"
          + comcono + "'\r\n" + "AND FDDIVI = '" + comdivi + "'\r\n" + "AND FDCODE = 'ITRQ'";

      /*
       * String idQuery =
       * "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(SERVICE_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
       * + "FROM "+DBNAME+"."+SR_DETAIL+" \n" + "WHERE SUBSTR(SERVICE_ID, 1, 2) = '25'";
       */
      logger.debug("ID Query: " + idQuery);

      rs = stmt.executeQuery(idQuery);

      String currentID = null;
      String fdtype = "1";

      if (rs.next()) {
        currentID = rs.getString("CURRENT_ID");
      }

      String getFDTYPEQuery = "SELECT RQTYPE  FROM BRLDTABK01.sr_requesttype\r\n"
          + "WHERE RQCONO = '" + comcono + "'\r\n" + "AND RQDIVI = '" + comdivi + "' AND RQNAME = '"
          + programtype + "'\r\n" + "AND rqcode = 'ITRQ'";

      rs9 = stmt.executeQuery(getFDTYPEQuery);
      logger.debug("ID Query: " + getFDTYPEQuery);


      if (rs9.next()) {
        fdtype = rs9.getString("RQTYPE");
      }

      logger.debug("ID fdtype: " + fdtype);

      if (currentID != null) {
        // insert ด้วย currentID
        /*
         * String insertQuery = "INSERT INTO "+DBNAME+"."+SR_DETAIL+"  \n" +
         * "( json_data,SERVICE_ID,PROMGRAM_CODE,STATUS,DATE,TIME) \n" + "VALUES ('" + vData + "','"
         * + currentID + "','ITMRQ', '10' ,CURRENT DATE ,CURRENT TIME)";
         */

        String insertQuery = "INSERT INTO " + DBNAME + ".SR_FLOWDETAIL\r\n"
            + "(FDCONO,FDDIVI,FDTYPE,  FDDATA, FDSRNO,FDCODE, FDDSTA , FDENDA, FDENTI,FDENUS) \r\n"
            + "VALUES ('" + comcono + "','" + comdivi + "','" + fdtype + "','" + vData + "','"
            + currentID + "','ITRQ', '10', CURRENT DATE, CURRENT TIME ,'" + username.toString()
            + "')";

        logger.debug("Insert Query: " + insertQuery);

        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
        String dateYYYYMMDD = new java.text.SimpleDateFormat("yyyyMMdd").format(currentTimestamp);

        /*
         * String insertQueryHead = "INSERT INTO "+DBNAME+"."+SR_HEAD+"  \n" +
         * "( DOC_CODE,DOC_NO,REQUETER,CREATE_DATE,CREATE_TIME,STATUS,DEPTHEAD ,H_STATUS) \n" +
         * "VALUES ('ITRQ','" + currentID +
         * "','"+username+"',CURRENT DATE ,CURRENT TIME, '10','"+depthead+"',1)";
         */

        String insertQueryHead = "INSERT INTO " + DBNAME + ".SR_FLOWHEAD\r\n"
            + "(FHCONO,FHDIVI, FHCODE, FHSRNO,FHREQU ,FHENDA ,FHENTI,FHENUS ,FHREDA,FHHSTA ,FHDEPH , FHDSTA , FHDES1)\r\n"
            + "VALUES ('" + comcono + "','" + comdivi + "','ITRQ', '" + currentID + "', '"
            + username + "', CURRENT DATE , CURRENT TIME,'" + username + "' ,CURRENT DATE, 2, '"
            + depthead + "', 10 , 'ITRQ-" + currentID + "-" + "" + programtype + "-" + itemname
            + "')";
        logger.debug("Insert Query: " + insertQueryHead);



        /*
         * String insertQueryTemp = "\r\n" + "INSERT INTO BRLDTABK01.Approve_Detail02 (\r\n" +
         * "    ID,\r\n" + "    DOC_CODE,\r\n" + "    DOC_NO,\r\n" + "APPROVE,\r\n" +
         * "APPROVE_DATE,\r\n" + "STATUS,\r\n" + "STS_DESC,\r\n" + "TIME_ST \r\n" + ")\r\n" +
         * "SELECT\r\n" + "    STATUS,\r\n" + "    DOC_CODE,\r\n" + "    '" + currentID + "',\r\n" +
         * "    'PP',\r\n" + "	'-',\r\n" + "	STATUS,\r\n" + "    'Wait for approve',\r\n" +
         * "    '-'\r\n" + "FROM BRLDTABK01.flow_master\r\n"
         * 
         * + "WHERE DOC_CODE = 'ITRQ'\r\n" + "";
         */
        stmt.executeUpdate(insertQuery);

        stmt.executeUpdate(insertQueryHead);

        // stmt.executeUpdate(insertQueryTemp);


        String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n" + "  SELECT *\r\n"
            + "  FROM " + DBNAME + ".SR_PROCESSMASTER\r\n" + "  WHERE PMCONO = '" + comcono
            + "'\r\n" + "    AND PMDIVI = '" + comdivi + "'\r\n" + "    AND PMCODE = 'ITRQ'\r\n"
            + "),\r\n" + "FILTERED_GROUP AS (\r\n" + "  SELECT *\r\n" + "  FROM " + DBNAME
            + ".SR_GROUPMASTER" + "\r\n" + "),\r\n" + "JOINED_DATA AS (\r\n" + "  SELECT\r\n"
            + "    M.PMCONO,\r\n" + "    M.PMDIVI,\r\n" + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n"
            + "    M.PMSGRO,\r\n" + "    M.PMSTAT,\r\n" + "    M.PMDES1,\r\n" + "    M.PMDES2,\r\n"
            + "    M.PMDES3,        -- ⭐ เพิ่ม\r\n" + "    M.PMDES4,        -- ⭐ เพิ่ม\r\n"
            + "    G.GMUSER,\r\n" + "    (\r\n" + "      SELECT MIN(M2.PMSTAT)\r\n" + "      FROM "
            + DBNAME + ".SR_PROCESSMASTER M2\r\n" + "      WHERE M2.PMCONO = M.PMCONO\r\n"
            + "        AND M2.PMDIVI = M.PMDIVI\r\n" + "        AND M2.PMCODE = M.PMCODE\r\n"
            + "        AND M2.PMSTAT > M.PMSTAT\r\n" + "    ) AS NEXT_STAT,\r\n"
            + "    ROW_NUMBER() OVER (\r\n"
            + "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n" + "      ORDER BY\r\n"
            + "        CASE WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0 ELSE 1 END,\r\n"
            + "        M.PMDES2 DESC,\r\n" + "        G.GMUSER ASC\r\n" + "    ) AS RN\r\n"
            + "  FROM FILTERED_MASTER M\r\n" + "  JOIN FILTERED_GROUP G\r\n"
            + "    ON M.PMCONO = G.GMCONO\r\n" + "   AND M.PMDIVI = G.GMDIVI\r\n"
            + "   AND M.PMGROU = G.GMGROU\r\n" + "   AND M.PMSGRO = G.GMSGRO\r\n" + "),\r\n"
            + "VACANT_FLAG AS (\r\n" + "  SELECT\r\n" + "    M.PMCONO,\r\n" + "    M.PMDIVI,\r\n"
            + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n" + "    M.PMSGRO,\r\n"
            + "    CASE WHEN COUNT(G2.GMUSER) > 0 THEN 'Y' ELSE 'N' END AS SKIP_IF_VACANT\r\n"
            + "  FROM FILTERED_MASTER M\r\n" + "  LEFT JOIN " + DBNAME + ".SR_GROUPMASTER G2\r\n"
            + "    ON G2.GMCONO = M.PMCONO\r\n" + "   AND G2.GMDIVI = M.PMDIVI\r\n"
            + "   AND G2.GMGROU = M.PMGROU\r\n" + "   AND G2.GMSGRO = M.PMSGRO\r\n"
            + "   AND UPPER(TRIM(G2.GMUSER)) = 'VACANT'\r\n"
            + "  GROUP BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMGROU, M.PMSGRO\r\n" + "),\r\n"
            + "CONCAT_CTE (\r\n" + "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
            + "  PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,   -- ⭐ เพิ่ม\r\n" + "  RN, NAME_SERIAL\r\n"
            + ") AS (\r\n" + "  SELECT\r\n" + "    PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
            + "    PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,  -- ⭐ เพิ่ม\r\n" + "    RN,\r\n"
            + "    GMUSER AS NAME_SERIAL\r\n" + "  FROM JOINED_DATA\r\n" + "  WHERE RN = 1\r\n"
            + "  UNION ALL\r\n" + "  SELECT\r\n"
            + "    J.PMCONO, J.PMDIVI, J.PMCODE, J.PMGROU, J.PMSGRO,\r\n"
            + "    J.PMSTAT, J.PMDES1, J.PMDES2, J.PMDES3, J.PMDES4,  -- ⭐ เพิ่ม\r\n"
            + "    J.RN,\r\n" + "    C.NAME_SERIAL || ',' || J.GMUSER\r\n"
            + "  FROM CONCAT_CTE C\r\n" + "  JOIN JOINED_DATA J\r\n"
            + "    ON C.PMCONO = J.PMCONO\r\n" + "   AND C.PMDIVI = J.PMDIVI\r\n"
            + "   AND C.PMCODE = J.PMCODE\r\n" + "   AND C.PMGROU = J.PMGROU\r\n"
            + "   AND C.PMSGRO = J.PMSGRO\r\n" + "   AND J.RN = C.RN + 1\r\n" + ")\r\n"
            + "SELECT\r\n" + "  C.PMCONO,\r\n" + "  C.PMDIVI,\r\n" + "  C.PMCODE,\r\n"
            + "  C.PMGROU,\r\n" + "  C.PMSGRO,\r\n" + "  C.PMSTAT,\r\n" + "  C.PMDES1,\r\n"
            + "  C.PMDES2,\r\n" + "  C.PMDES3 AS NEXT_STAT,\r\n"
            + "  C.PMDES4 AS PREVIOUS_STAT,    \r\n" + "  C.NAME_SERIAL,\r\n"
            + "  V.SKIP_IF_VACANT\r\n" + "FROM CONCAT_CTE C\r\n" + "LEFT JOIN VACANT_FLAG V\r\n"
            + "  ON C.PMCONO = V.PMCONO\r\n" + " AND C.PMDIVI = V.PMDIVI\r\n"
            + " AND C.PMCODE = V.PMCODE\r\n" + " AND C.PMGROU = V.PMGROU\r\n"
            + " AND C.PMSGRO = V.PMSGRO\r\n" + "LEFT JOIN CONCAT_CTE C2\r\n"
            + "  ON C2.PMCONO = C.PMCONO\r\n" + " AND C2.PMDIVI = C.PMDIVI\r\n"
            + " AND C2.PMCODE = C.PMCODE\r\n" + " AND C2.PMGROU = C.PMGROU\r\n"
            + " AND C2.PMSGRO = C.PMSGRO\r\n" + " AND C2.RN = C.RN + 1\r\n"
            + "WHERE C2.PMCONO IS NULL\r\n" + "ORDER BY C.PMSTAT, C.PMGROU, C.PMSGRO\r\n" + "";


        /*
         * String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n" +
         * "  --  Step 1: Select all rows for PMCONO, PMDIVI, PMCODE='ITRQ' from SR_PROCESSMASTER\r\n"
         * + "  SELECT *\r\n" + "  FROM " + DBNAME + ".SR_PROCESSMASTER\r\n" + "  WHERE PMCONO = '"
         * + comcono + "'\r\n" + "    AND PMDIVI = '" + comdivi + "'\r\n" +
         * "    AND PMCODE = 'ITRQ'\r\n" + "),\r\n" + "FILTERED_GROUP AS (\r\n" +
         * "  --  Step 2: Select all rows from SR_GROUPMASTER (no filter applied)\r\n" +
         * "  SELECT *\r\n" + "  FROM " + DBNAME + ".SR_GROUPMASTER\r\n" + "),\r\n" +
         * "JOINED_DATA AS (\r\n" +
         * "  --  Step 3: Join PROCESSMASTER and GROUPMASTER on CONO, DIVI, GROUP, SUBGROUP\r\n" +
         * "  -- Assign row numbers to handle duplicates per PMSTAT\r\n" + "  SELECT\r\n" +
         * "    M.PMCONO,\r\n" + "    M.PMDIVI,\r\n" + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n" +
         * "    M.PMSGRO,\r\n" + "    M.PMSTAT,\r\n" + "    M.PMDES1,\r\n" + "    M.PMDES2,\r\n" +
         * "    G.GMUSER,\r\n" + "    ROW_NUMBER() OVER (\r\n" +
         * "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n" + "      ORDER BY\r\n" +
         * "        --  Priority 1: PMSGRO matches :TARGET_SUBGROUP\r\n" + "        CASE\r\n" +
         * "          WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0\r\n" + "          ELSE 1\r\n" +
         * "        END,\r\n" + "        --  Priority 2: PMDES2 DESC (higher value preferred)\r\n" +
         * "        M.PMDES2 DESC,\r\n" + "        --  Tie-breaker: PMSGRO alphabetically\r\n" +
         * "        M.PMSGRO ASC\r\n" + "    ) AS RN\r\n" + "  FROM FILTERED_MASTER M\r\n" +
         * "  JOIN FILTERED_GROUP G\r\n" +
         * "    ON M.PMCONO = G.GMCONO         -- ✅ Match company\r\n" +
         * "   AND M.PMDIVI = G.GMDIVI         -- ✅ Match division\r\n" +
         * "   AND M.PMGROU = G.GMGROU         -- ✅ Match group\r\n" +
         * "   AND M.PMSGRO = G.GMSGRO         -- ✅ Match subgroup\r\n" + "),\r\n" +
         * "CONCAT_CTE (\r\n" +
         * "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO, PMSTAT, PMDES1, PMDES2, RN, NAME_SERIAL\r\n" +
         * ") AS (\r\n" + "  --  Step 4: Start recursive concatenation\r\n" +
         * "  -- Pick only the highest priority row per PMSTAT (RN=1)\r\n" + "  SELECT\r\n" +
         * "    PMCONO,\r\n" + "    PMDIVI,\r\n" + "    PMCODE,\r\n" + "    PMGROU,\r\n" +
         * "    PMSGRO,\r\n" + "    PMSTAT,\r\n" + "    PMDES1,\r\n" + "    PMDES2,\r\n" +
         * "    RN,\r\n" + "    GMUSER\r\n" + "  FROM JOINED_DATA\r\n" + "  WHERE RN = 1\r\n" +
         * "  UNION ALL\r\n" + "  --  Step 5: Concatenate GMUSER values for rows with RN > 1\r\n" +
         * "  SELECT\r\n" + "    J.PMCONO,\r\n" + "    J.PMDIVI,\r\n" + "    J.PMCODE,\r\n" +
         * "    J.PMGROU,\r\n" + "    J.PMSGRO,\r\n" + "    J.PMSTAT,\r\n" + "    J.PMDES1,\r\n" +
         * "    J.PMDES2,\r\n" + "    J.RN,\r\n" + "    C.NAME_SERIAL || ',' || J.GMUSER\r\n" +
         * "  FROM CONCAT_CTE C\r\n" + "  JOIN JOINED_DATA J\r\n" + "    ON C.PMCONO = J.PMCONO\r\n"
         * + "   AND C.PMDIVI = J.PMDIVI\r\n" + "   AND C.PMCODE = J.PMCODE\r\n" +
         * "   AND C.PMGROU = J.PMGROU\r\n" + "   AND C.PMSGRO = J.PMSGRO\r\n" +
         * "   AND J.RN = C.RN + 1\r\n" + ")\r\n" +
         * "--  Step 6: Select only rows with no next RN (final row per group)\r\n" + "SELECT\r\n" +
         * "  PMCONO,\r\n" + "  PMDIVI,\r\n" + "  PMCODE,\r\n" + "  PMGROU,\r\n" + "  PMSGRO,\r\n" +
         * "  PMSTAT,\r\n" + "  PMDES1,\r\n" + "  NAME_SERIAL\r\n" + "FROM CONCAT_CTE C\r\n" +
         * "WHERE NOT EXISTS (\r\n" + "  SELECT 1\r\n" + "  FROM CONCAT_CTE C2\r\n" + "  WHERE\r\n"
         * + "    C2.PMCONO = C.PMCONO\r\n" + "    AND C2.PMDIVI = C.PMDIVI\r\n" +
         * "    AND C2.PMCODE = C.PMCODE\r\n" + "    AND C2.PMGROU = C.PMGROU\r\n" +
         * "    AND C2.PMSGRO = C.PMSGRO\r\n" + "    AND C2.RN = C.RN + 1\r\n" + ")\r\n" +
         * "--  Step 7: Final sorting of result\r\n" + "ORDER BY PMSTAT, PMGROU, PMSGRO";
         * 
         * /* String recursiveQuery = "WITH RECURSIVE filtered_master AS (\r\n" + "SELECT *\r\n" +
         * "FROM "+DBNAME+"."+SR_FLOW+"\r\n" + "WHERE DOC_CODE = 'ITRQ'\r\n" + "),\r\n" +
         * "filtered_group AS (\r\n" + "SELECT *\r\n" + "FROM "+DBNAME+"."+SR_GROUP+"\r\n" +
         * "WHERE WHS = 'A91'\r\n" + "),\r\n" + "joined_data AS (\r\n" + "SELECT\r\n" +
         * "m.DOC_CODE,\r\n" + "m.GROUP,\r\n" + "m.SUBGROUP,\r\n" + "m.STATUS,\r\n" +
         * "m.REMARK,\r\n" + "g.NAME,\r\n" + "ROW_NUMBER() OVER (\r\n" +
         * "PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP\r\n" + "ORDER BY g.NAME\r\n" +
         * ") AS RN\r\n" + "FROM filtered_master m\r\n" + "JOIN filtered_group g\r\n" +
         * "ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP\r\n" + "),\r\n" + "concat_cte (\r\n"
         * + "DOC_CODE, GROUP_ID, SUBGROUP, STATUS, REMARK, RN, NAME_SERIAL\r\n" + ") AS (\r\n" +
         * "SELECT\r\n" + "DOC_CODE,\r\n" + "GROUP,\r\n" + "SUBGROUP,\r\n" + "STATUS,\r\n" +
         * "REMARK,\r\n" + "RN,\r\n" + "NAME\r\n" + "FROM joined_data\r\n" + "WHERE RN = 1\r\n" +
         * "\r\n" + "UNION ALL\r\n" + "\r\n" + "SELECT\r\n" + "j.DOC_CODE,\r\n" + "j.GROUP,\r\n" +
         * "j.SUBGROUP,\r\n" + "j.STATUS,\r\n" + "j.REMARK,\r\n" + "j.RN,\r\n" +
         * "c.NAME_SERIAL || ',' || j.NAME\r\n" + "FROM concat_cte c\r\n" + "JOIN joined_data j\r\n"
         * + "ON c.DOC_CODE = j.DOC_CODE\r\n" + "AND c.GROUP_ID = j.GROUP\r\n" +
         * "AND c.SUBGROUP = j.SUBGROUP\r\n" + "AND j.RN = c.RN + 1\r\n" + ")\r\n" + "\r\n" +
         * "SELECT\r\n" + "DOC_CODE,\r\n" + "STATUS,\r\n" + "REMARK,\r\n" + "NAME_SERIAL\r\n" +
         * "FROM concat_cte c\r\n" + "WHERE NOT EXISTS (\r\n" + "SELECT 1\r\n" +
         * "FROM concat_cte c2\r\n" + "WHERE\r\n" + "c2.DOC_CODE = c.DOC_CODE\r\n" +
         * "AND c2.GROUP_ID = c.GROUP_ID\r\n" + "AND c2.SUBGROUP = c.SUBGROUP\r\n" +
         * "AND c2.RN = c.RN + 1\r\n" + ")\r\n" + "ORDER BY STATUS";
         * 
         */

        /*
         * String recursiveQuery = "WITH RECURSIVE " + "filtered_master AS ( " +
         * "  SELECT * FROM "+DBNAME+"."+SR_FLOW+" WHERE DOC_CODE = 'ITRQ' " + "), " +
         * "filtered_group AS ( " + "  SELECT * FROM "+DBNAME+"."+SR_GROUP+" WHERE WHS = 'A91' " +
         * "), " + "joined_data AS ( " +
         * "  SELECT m.DOC_CODE, m.GROUP, m.SUBGROUP, m.STATUS, g.NAME, " +
         * "         ROW_NUMBER() OVER (PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP ORDER BY g.NAME) AS RN "
         * + "  FROM filtered_master m " +
         * "  JOIN filtered_group g ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP " + "), " +
         * "concat_cte (DOC_CODE, GROUP_ID, SUBGROUP, STATUS, RN, NAME_SERIAL) AS ( " +
         * "  SELECT DOC_CODE, GROUP, SUBGROUP, STATUS, RN, NAME FROM joined_data WHERE RN = 1 " +
         * "  UNION ALL " +
         * "  SELECT j.DOC_CODE, j.GROUP, j.SUBGROUP, j.STATUS, j.RN, c.NAME_SERIAL || ':' || j.NAME "
         * + "  FROM concat_cte c JOIN joined_data j " +
         * "  ON c.DOC_CODE = j.DOC_CODE AND c.GROUP_ID = j.GROUP AND c.SUBGROUP = j.SUBGROUP AND j.RN = c.RN + 1 "
         * + ") " + "SELECT DOC_CODE, STATUS, NAME_SERIAL FROM concat_cte c " +
         * "WHERE NOT EXISTS ( " + "  SELECT 1 FROM concat_cte c2 " +
         * "  WHERE c2.DOC_CODE = c.DOC_CODE AND c2.GROUP_ID = c.GROUP_ID AND c2.SUBGROUP = c.SUBGROUP AND c2.RN = c.RN + 1 "
         * + ") ORDER BY STATUS";
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
           * + "VALUES (" + "'" + docCode + "', " + "'" + currentID + "', " + "'" + approve + "', "
           * + "'-', " + "'" + status + "', " + "'Wait for approve', " + "'-', " + "'-',  " + "'" +
           * remark + "'" + ")";
           * 
           */

          String insertDetail = "INSERT INTO " + DBNAME + ".SR_FLOWAPPROVE "
              + "(FATYPE,FACONO,FADIVI, FACODE,FASRNO ,FAAPLI ,FAAPDA ,FASTAT , FADES1, FAENTI,FAENDA,FAAPBY,FADES2) "
              + "VALUES (" + " '1' , '" + comcono + "','" + comdivi + "','" + docCode + "', " + "'"
              + currentID + "', " + "'" + approve + "', " + "NULL, " + "'" + status + "', "
              + "'Wait for approve', " + "CURRENT TIME, " + "CURRENT DATE, " + "'',  " + "'"
              + remark + "'" + ")";

          logger.debug("xxxxxxin " + insertDetail);
          stmt2.executeUpdate(insertDetail);

        }

        /*
         * 
         * String query2 = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n" +
         * "SET  STS_DESC = 'Approved',APPROVE = '"+username+"' , TIME_ST = '" + currentTimestamp +
         * "',APPROVED_USER = 'PP', APPROVE_DATE = '" + dateYYYYMMDD +
         * "' WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID + "' AND STATUS = '10' ";
         */

        String query2 = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n" + "SET FAENUS = '" + username
            + "', FADES1 = 'Approved',FAAPLI = '" + username
            + "' ,FAAPDA = CURRENT DATE, FAENTI = CURRENT TIME, FAAPTI = CURRENT TIME ,FAAPBY = '"
            + username + "', FAENDA = CURRENT DATE ,  FADES3 = '" + vRemark2
            + "' WHERE FACODE = 'ITRQ' AND FASRNO = '" + currentID
            + "' AND FASTAT = '00'  AND FACONO = '" + comcono + "' AND  FADIVI = '" + comdivi
            + "' ";

        logger.debug("xxxxxxin " + query2);

        stmt2.executeUpdate(query2);

        /*
         * 
         * String query222 = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n" + "SET APPROVE = '" +
         * depthead + "' \n" + "WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID +
         * "' AND STATUS = '20'";
         * 
         */

        String query222 = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n" + "SET  FAAPLI = '" + depthead
            + "' \n" + "WHERE  FACODE = 'ITRQ' AND FASRNO  = '" + currentID
            + "' AND FASTAT = '10' AND FACONO = '" + comcono + "' AND FADIVI = '" + comdivi + "' ";

        logger.debug("xxxxxxin " + query222);
        stmt2.executeUpdate(query222);

        String data = SelectData.getSTATUSIDITEMRQ(currentID.toString(), comcono, comdivi);
        String url = "https://workflow.br-bangkokranch.com/webhook/sendtodb2";

        String response = HttpConnection.sendRequest("POST", url, Map.of("x-access-token",
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
            data, null // form-data
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
      mJsonObj.put("message", "Can't create Service number, Please update your version to "
          + checkVersion + " :  " + version + " (Click F5 button).");
      return mJsonObj.toString();

    }



    logger.debug("ID programtype: " + programtype);

    logger.debug("company: " + company);
    logger.debug("warehouse2: " + warehouse2);
    logger.debug("vRemark: " + vRemark2);

    Map<String, String[]> companyMapping = new HashMap<>();
    companyMapping.put("10", new String[] {"10", "101"});
    companyMapping.put("600", new String[] {"600", "600"});
    companyMapping.put("500", new String[] {"500", "500"});
    // เพิ่มได้เรื่อยๆ เช่น
    // companyMapping.put("300", new String[] { "300", "301" });

    // ดึงข้อมูลตาม company
    String[] mapping = companyMapping.getOrDefault(company, new String[] {company, company});
    String comcono = mapping[0];
    String comdivi = mapping[1];

    logger.debug("cono: " + comcono);
    logger.debug("divi: " + comdivi);

    try {
      conn = ConnectDB2.doConnect();
      stmt = conn.createStatement();
      Statement stmt2 = conn.createStatement();

      // สร้าง currentID จาก query

      /*
       * String idQuery =
       * "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
       * 
       * + "FROM " + DBNAME + ".SR_FLOWDETAIL \n" +
       * "WHERE SUBSTR(FDSRNO, 1, 2) = '25' AND FDCONO  = '" + comcono + "' AND　FDDIVI = '" +
       * comdivi + "' AND FDCODE ='ITRQ'  ";
       */

      // String idQuery = "SELECT RIGHT(YEAR(CURRENT_DATE), 2)\r\n"
      // + "|| RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), RIGHT(YEAR(CURRENT_DATE),2) ||
      // '000000'), 3)) + 1), 6)\r\n"
      // + "AS CURRENT_ID\r\n"
      // + "FROM " + DBNAME + ".SR_FLOWDETAIL\r\n"
      // + "WHERE SUBSTR(FDSRNO, 1, 2) = RIGHT(YEAR(CURRENT_DATE), 2)\r\n"
      // + "AND FDCONO = '" + comcono +"'\r\n"
      // + "AND FDDIVI = '" + comdivi + "'\r\n"
      // + "AND FDCODE = 'ITRQ'";

      String idQuery = "SELECT RIGHT(YEAR(CURRENT_DATE), 2)\n"
          + "|| RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(FDSRNO), RIGHT(YEAR(CURRENT_DATE),2) || '000000'), 3)) + 1), 6)\n"
          + "AS CURRENT_ID\n" + "FROM " + DBNAME + ".SR_FLOWDETAIL sf \n"
          + "WHERE SUBSTR(FDSRNO, 1, 2) = RIGHT(YEAR(CURRENT_DATE), 2)\n" + "AND FDCONO = '"
          + comcono + "'\n" + "AND FDDIVI = '" + comdivi + "'\n" + "AND FDCODE = 'SWRQ'";

      /*
       * String idQuery =
       * "SELECT '25' || RIGHT('000000' || (INT(SUBSTR(COALESCE(MAX(SERVICE_ID), '25000000'), 3)) + 1), 6) AS CURRENT_ID \r\n"
       * + "FROM "+DBNAME+"."+SR_DETAIL+" \n" + "WHERE SUBSTR(SERVICE_ID, 1, 2) = '25'";
       */
      logger.debug("ID Query: " + idQuery);

      rs = stmt.executeQuery(idQuery);

      String currentID = null;
      String fdtype = "1";

      if (rs.next()) {
        currentID = rs.getString("CURRENT_ID");
      }

      String getFDTYPEQuery = "SELECT RQTYPE  FROM BRLDTABK01.sr_requesttype\r\n"
          + "WHERE RQCONO = '" + comcono + "'\r\n" + "AND RQDIVI = '" + comdivi + "' AND RQNAME = '"
          + programtype + "'\r\n" + "AND rqcode = 'SWRQ'";

      rs9 = stmt.executeQuery(getFDTYPEQuery);
      logger.debug("ID Query: " + getFDTYPEQuery);


      if (rs9.next()) {
        fdtype = rs9.getString("RQTYPE");
      }

      logger.debug("ID fdtype: " + fdtype);

      if (currentID != null) {
        // insert ด้วย currentID
        /*
         * String insertQuery = "INSERT INTO "+DBNAME+"."+SR_DETAIL+"  \n" +
         * "( json_data,SERVICE_ID,PROMGRAM_CODE,STATUS,DATE,TIME) \n" + "VALUES ('" + vData + "','"
         * + currentID + "','ITMRQ', '10' ,CURRENT DATE ,CURRENT TIME)";
         */

        String insertQuery = "INSERT INTO " + DBNAME + ".SR_FLOWDETAIL\r\n"
            + "(FDCONO,FDDIVI,FDTYPE,  FDDATA, FDSRNO,FDCODE, FDDSTA , FDENDA, FDENTI,FDENUS) \r\n"
            + "VALUES ('" + comcono + "','" + comdivi + "','" + fdtype + "','" + vData + "','"
            + currentID + "','SWRQ', '10', CURRENT DATE, CURRENT TIME ,'" + username.toString()
            + "')";

        logger.debug("Insert Query: " + insertQuery);

        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());
        String dateYYYYMMDD = new java.text.SimpleDateFormat("yyyyMMdd").format(currentTimestamp);

        /*
         * String insertQueryHead = "INSERT INTO "+DBNAME+"."+SR_HEAD+"  \n" +
         * "( DOC_CODE,DOC_NO,REQUETER,CREATE_DATE,CREATE_TIME,STATUS,DEPTHEAD ,H_STATUS) \n" +
         * "VALUES ('ITRQ','" + currentID +
         * "','"+username+"',CURRENT DATE ,CURRENT TIME, '10','"+depthead+"',1)";
         */

        String insertQueryHead = "INSERT INTO " + DBNAME + ".SR_FLOWHEAD\r\n"
            + "(FHCONO,FHDIVI, FHCODE, FHSRNO,FHREQU ,FHENDA ,FHENTI,FHENUS ,FHREDA,FHHSTA ,FHDEPH , FHDSTA , FHDES1)\r\n"
            + "VALUES ('" + comcono + "','" + comdivi + "','SWRQ', '" + currentID + "', '"
            + username + "', CURRENT DATE , CURRENT TIME,'" + username + "' ,CURRENT DATE, 2, '"
            + depthead + "', 10 , 'SWRQ-" + currentID + "-" + "" + programtype + "-" + itemname
            + "')";
        logger.debug("Insert Query: " + insertQueryHead);



        /*
         * String insertQueryTemp = "\r\n" + "INSERT INTO BRLDTABK01.Approve_Detail02 (\r\n" +
         * "    ID,\r\n" + "    DOC_CODE,\r\n" + "    DOC_NO,\r\n" + "APPROVE,\r\n" +
         * "APPROVE_DATE,\r\n" + "STATUS,\r\n" + "STS_DESC,\r\n" + "TIME_ST \r\n" + ")\r\n" +
         * "SELECT\r\n" + "    STATUS,\r\n" + "    DOC_CODE,\r\n" + "    '" + currentID + "',\r\n" +
         * "    'PP',\r\n" + "	'-',\r\n" + "	STATUS,\r\n" + "    'Wait for approve',\r\n" +
         * "    '-'\r\n" + "FROM BRLDTABK01.flow_master\r\n"
         * 
         * + "WHERE DOC_CODE = 'ITRQ'\r\n" + "";
         */
        stmt.executeUpdate(insertQuery);

        stmt.executeUpdate(insertQueryHead);

        // stmt.executeUpdate(insertQueryTemp);


        String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n" + "  SELECT *\r\n"
            + "  FROM " + DBNAME + ".SR_PROCESSMASTER\r\n" + "  WHERE PMCONO = '" + comcono
            + "'\r\n" + "    AND PMDIVI = '" + comdivi + "'\r\n" + "    AND PMCODE = 'SWRQ'\r\n"
            + "),\r\n" + "FILTERED_GROUP AS (\r\n" + "  SELECT *\r\n" + "  FROM " + DBNAME
            + ".SR_GROUPMASTER" + "\r\n" + "),\r\n" + "JOINED_DATA AS (\r\n" + "  SELECT\r\n"
            + "    M.PMCONO,\r\n" + "    M.PMDIVI,\r\n" + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n"
            + "    M.PMSGRO,\r\n" + "    M.PMSTAT,\r\n" + "    M.PMDES1,\r\n" + "    M.PMDES2,\r\n"
            + "    M.PMDES3,        -- ⭐ เพิ่ม\r\n" + "    M.PMDES4,        -- ⭐ เพิ่ม\r\n"
            + "    G.GMUSER,\r\n" + "    (\r\n" + "      SELECT MIN(M2.PMSTAT)\r\n" + "      FROM "
            + DBNAME + ".SR_PROCESSMASTER M2\r\n" + "      WHERE M2.PMCONO = M.PMCONO\r\n"
            + "        AND M2.PMDIVI = M.PMDIVI\r\n" + "        AND M2.PMCODE = M.PMCODE\r\n"
            + "        AND M2.PMSTAT > M.PMSTAT\r\n" + "    ) AS NEXT_STAT,\r\n"
            + "    ROW_NUMBER() OVER (\r\n"
            + "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n" + "      ORDER BY\r\n"
            + "        CASE WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0 ELSE 1 END,\r\n"
            + "        M.PMDES2 DESC,\r\n" + "        G.GMUSER ASC\r\n" + "    ) AS RN\r\n"
            + "  FROM FILTERED_MASTER M\r\n" + "  JOIN FILTERED_GROUP G\r\n"
            + "    ON M.PMCONO = G.GMCONO\r\n" + "   AND M.PMDIVI = G.GMDIVI\r\n"
            + "   AND M.PMGROU = G.GMGROU\r\n" + "   AND M.PMSGRO = G.GMSGRO\r\n" + "),\r\n"
            + "VACANT_FLAG AS (\r\n" + "  SELECT\r\n" + "    M.PMCONO,\r\n" + "    M.PMDIVI,\r\n"
            + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n" + "    M.PMSGRO,\r\n"
            + "    CASE WHEN COUNT(G2.GMUSER) > 0 THEN 'Y' ELSE 'N' END AS SKIP_IF_VACANT\r\n"
            + "  FROM FILTERED_MASTER M\r\n" + "  LEFT JOIN " + DBNAME + ".SR_GROUPMASTER G2\r\n"
            + "    ON G2.GMCONO = M.PMCONO\r\n" + "   AND G2.GMDIVI = M.PMDIVI\r\n"
            + "   AND G2.GMGROU = M.PMGROU\r\n" + "   AND G2.GMSGRO = M.PMSGRO\r\n"
            + "   AND UPPER(TRIM(G2.GMUSER)) = 'VACANT'\r\n"
            + "  GROUP BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMGROU, M.PMSGRO\r\n" + "),\r\n"
            + "CONCAT_CTE (\r\n" + "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
            + "  PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,   -- ⭐ เพิ่ม\r\n" + "  RN, NAME_SERIAL\r\n"
            + ") AS (\r\n" + "  SELECT\r\n" + "    PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
            + "    PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,  -- ⭐ เพิ่ม\r\n" + "    RN,\r\n"
            + "    GMUSER AS NAME_SERIAL\r\n" + "  FROM JOINED_DATA\r\n" + "  WHERE RN = 1\r\n"
            + "  UNION ALL\r\n" + "  SELECT\r\n"
            + "    J.PMCONO, J.PMDIVI, J.PMCODE, J.PMGROU, J.PMSGRO,\r\n"
            + "    J.PMSTAT, J.PMDES1, J.PMDES2, J.PMDES3, J.PMDES4,  -- ⭐ เพิ่ม\r\n"
            + "    J.RN,\r\n" + "    C.NAME_SERIAL || ',' || J.GMUSER\r\n"
            + "  FROM CONCAT_CTE C\r\n" + "  JOIN JOINED_DATA J\r\n"
            + "    ON C.PMCONO = J.PMCONO\r\n" + "   AND C.PMDIVI = J.PMDIVI\r\n"
            + "   AND C.PMCODE = J.PMCODE\r\n" + "   AND C.PMGROU = J.PMGROU\r\n"
            + "   AND C.PMSGRO = J.PMSGRO\r\n" + "   AND J.RN = C.RN + 1\r\n" + ")\r\n"
            + "SELECT\r\n" + "  C.PMCONO,\r\n" + "  C.PMDIVI,\r\n" + "  C.PMCODE,\r\n"
            + "  C.PMGROU,\r\n" + "  C.PMSGRO,\r\n" + "  C.PMSTAT,\r\n" + "  C.PMDES1,\r\n"
            + "  C.PMDES2,\r\n" + "  C.PMDES3 AS NEXT_STAT,\r\n"
            + "  C.PMDES4 AS PREVIOUS_STAT,    \r\n" + "  C.NAME_SERIAL,\r\n"
            + "  V.SKIP_IF_VACANT\r\n" + "FROM CONCAT_CTE C\r\n" + "LEFT JOIN VACANT_FLAG V\r\n"
            + "  ON C.PMCONO = V.PMCONO\r\n" + " AND C.PMDIVI = V.PMDIVI\r\n"
            + " AND C.PMCODE = V.PMCODE\r\n" + " AND C.PMGROU = V.PMGROU\r\n"
            + " AND C.PMSGRO = V.PMSGRO\r\n" + "LEFT JOIN CONCAT_CTE C2\r\n"
            + "  ON C2.PMCONO = C.PMCONO\r\n" + " AND C2.PMDIVI = C.PMDIVI\r\n"
            + " AND C2.PMCODE = C.PMCODE\r\n" + " AND C2.PMGROU = C.PMGROU\r\n"
            + " AND C2.PMSGRO = C.PMSGRO\r\n" + " AND C2.RN = C.RN + 1\r\n"
            + "WHERE C2.PMCONO IS NULL\r\n" + "ORDER BY C.PMSTAT, C.PMGROU, C.PMSGRO\r\n" + "";


        /*
         * String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n" +
         * "  --  Step 1: Select all rows for PMCONO, PMDIVI, PMCODE='ITRQ' from SR_PROCESSMASTER\r\n"
         * + "  SELECT *\r\n" + "  FROM " + DBNAME + ".SR_PROCESSMASTER\r\n" + "  WHERE PMCONO = '"
         * + comcono + "'\r\n" + "    AND PMDIVI = '" + comdivi + "'\r\n" +
         * "    AND PMCODE = 'ITRQ'\r\n" + "),\r\n" + "FILTERED_GROUP AS (\r\n" +
         * "  --  Step 2: Select all rows from SR_GROUPMASTER (no filter applied)\r\n" +
         * "  SELECT *\r\n" + "  FROM " + DBNAME + ".SR_GROUPMASTER\r\n" + "),\r\n" +
         * "JOINED_DATA AS (\r\n" +
         * "  --  Step 3: Join PROCESSMASTER and GROUPMASTER on CONO, DIVI, GROUP, SUBGROUP\r\n" +
         * "  -- Assign row numbers to handle duplicates per PMSTAT\r\n" + "  SELECT\r\n" +
         * "    M.PMCONO,\r\n" + "    M.PMDIVI,\r\n" + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n" +
         * "    M.PMSGRO,\r\n" + "    M.PMSTAT,\r\n" + "    M.PMDES1,\r\n" + "    M.PMDES2,\r\n" +
         * "    G.GMUSER,\r\n" + "    ROW_NUMBER() OVER (\r\n" +
         * "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n" + "      ORDER BY\r\n" +
         * "        --  Priority 1: PMSGRO matches :TARGET_SUBGROUP\r\n" + "        CASE\r\n" +
         * "          WHEN M.PMSGRO = '" + warehouse2 + "' THEN 0\r\n" + "          ELSE 1\r\n" +
         * "        END,\r\n" + "        --  Priority 2: PMDES2 DESC (higher value preferred)\r\n" +
         * "        M.PMDES2 DESC,\r\n" + "        --  Tie-breaker: PMSGRO alphabetically\r\n" +
         * "        M.PMSGRO ASC\r\n" + "    ) AS RN\r\n" + "  FROM FILTERED_MASTER M\r\n" +
         * "  JOIN FILTERED_GROUP G\r\n" +
         * "    ON M.PMCONO = G.GMCONO         -- ✅ Match company\r\n" +
         * "   AND M.PMDIVI = G.GMDIVI         -- ✅ Match division\r\n" +
         * "   AND M.PMGROU = G.GMGROU         -- ✅ Match group\r\n" +
         * "   AND M.PMSGRO = G.GMSGRO         -- ✅ Match subgroup\r\n" + "),\r\n" +
         * "CONCAT_CTE (\r\n" +
         * "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO, PMSTAT, PMDES1, PMDES2, RN, NAME_SERIAL\r\n" +
         * ") AS (\r\n" + "  --  Step 4: Start recursive concatenation\r\n" +
         * "  -- Pick only the highest priority row per PMSTAT (RN=1)\r\n" + "  SELECT\r\n" +
         * "    PMCONO,\r\n" + "    PMDIVI,\r\n" + "    PMCODE,\r\n" + "    PMGROU,\r\n" +
         * "    PMSGRO,\r\n" + "    PMSTAT,\r\n" + "    PMDES1,\r\n" + "    PMDES2,\r\n" +
         * "    RN,\r\n" + "    GMUSER\r\n" + "  FROM JOINED_DATA\r\n" + "  WHERE RN = 1\r\n" +
         * "  UNION ALL\r\n" + "  --  Step 5: Concatenate GMUSER values for rows with RN > 1\r\n" +
         * "  SELECT\r\n" + "    J.PMCONO,\r\n" + "    J.PMDIVI,\r\n" + "    J.PMCODE,\r\n" +
         * "    J.PMGROU,\r\n" + "    J.PMSGRO,\r\n" + "    J.PMSTAT,\r\n" + "    J.PMDES1,\r\n" +
         * "    J.PMDES2,\r\n" + "    J.RN,\r\n" + "    C.NAME_SERIAL || ',' || J.GMUSER\r\n" +
         * "  FROM CONCAT_CTE C\r\n" + "  JOIN JOINED_DATA J\r\n" + "    ON C.PMCONO = J.PMCONO\r\n"
         * + "   AND C.PMDIVI = J.PMDIVI\r\n" + "   AND C.PMCODE = J.PMCODE\r\n" +
         * "   AND C.PMGROU = J.PMGROU\r\n" + "   AND C.PMSGRO = J.PMSGRO\r\n" +
         * "   AND J.RN = C.RN + 1\r\n" + ")\r\n" +
         * "--  Step 6: Select only rows with no next RN (final row per group)\r\n" + "SELECT\r\n" +
         * "  PMCONO,\r\n" + "  PMDIVI,\r\n" + "  PMCODE,\r\n" + "  PMGROU,\r\n" + "  PMSGRO,\r\n" +
         * "  PMSTAT,\r\n" + "  PMDES1,\r\n" + "  NAME_SERIAL\r\n" + "FROM CONCAT_CTE C\r\n" +
         * "WHERE NOT EXISTS (\r\n" + "  SELECT 1\r\n" + "  FROM CONCAT_CTE C2\r\n" + "  WHERE\r\n"
         * + "    C2.PMCONO = C.PMCONO\r\n" + "    AND C2.PMDIVI = C.PMDIVI\r\n" +
         * "    AND C2.PMCODE = C.PMCODE\r\n" + "    AND C2.PMGROU = C.PMGROU\r\n" +
         * "    AND C2.PMSGRO = C.PMSGRO\r\n" + "    AND C2.RN = C.RN + 1\r\n" + ")\r\n" +
         * "--  Step 7: Final sorting of result\r\n" + "ORDER BY PMSTAT, PMGROU, PMSGRO";
         * 
         * /* String recursiveQuery = "WITH RECURSIVE filtered_master AS (\r\n" + "SELECT *\r\n" +
         * "FROM "+DBNAME+"."+SR_FLOW+"\r\n" + "WHERE DOC_CODE = 'ITRQ'\r\n" + "),\r\n" +
         * "filtered_group AS (\r\n" + "SELECT *\r\n" + "FROM "+DBNAME+"."+SR_GROUP+"\r\n" +
         * "WHERE WHS = 'A91'\r\n" + "),\r\n" + "joined_data AS (\r\n" + "SELECT\r\n" +
         * "m.DOC_CODE,\r\n" + "m.GROUP,\r\n" + "m.SUBGROUP,\r\n" + "m.STATUS,\r\n" +
         * "m.REMARK,\r\n" + "g.NAME,\r\n" + "ROW_NUMBER() OVER (\r\n" +
         * "PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP\r\n" + "ORDER BY g.NAME\r\n" +
         * ") AS RN\r\n" + "FROM filtered_master m\r\n" + "JOIN filtered_group g\r\n" +
         * "ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP\r\n" + "),\r\n" + "concat_cte (\r\n"
         * + "DOC_CODE, GROUP_ID, SUBGROUP, STATUS, REMARK, RN, NAME_SERIAL\r\n" + ") AS (\r\n" +
         * "SELECT\r\n" + "DOC_CODE,\r\n" + "GROUP,\r\n" + "SUBGROUP,\r\n" + "STATUS,\r\n" +
         * "REMARK,\r\n" + "RN,\r\n" + "NAME\r\n" + "FROM joined_data\r\n" + "WHERE RN = 1\r\n" +
         * "\r\n" + "UNION ALL\r\n" + "\r\n" + "SELECT\r\n" + "j.DOC_CODE,\r\n" + "j.GROUP,\r\n" +
         * "j.SUBGROUP,\r\n" + "j.STATUS,\r\n" + "j.REMARK,\r\n" + "j.RN,\r\n" +
         * "c.NAME_SERIAL || ',' || j.NAME\r\n" + "FROM concat_cte c\r\n" + "JOIN joined_data j\r\n"
         * + "ON c.DOC_CODE = j.DOC_CODE\r\n" + "AND c.GROUP_ID = j.GROUP\r\n" +
         * "AND c.SUBGROUP = j.SUBGROUP\r\n" + "AND j.RN = c.RN + 1\r\n" + ")\r\n" + "\r\n" +
         * "SELECT\r\n" + "DOC_CODE,\r\n" + "STATUS,\r\n" + "REMARK,\r\n" + "NAME_SERIAL\r\n" +
         * "FROM concat_cte c\r\n" + "WHERE NOT EXISTS (\r\n" + "SELECT 1\r\n" +
         * "FROM concat_cte c2\r\n" + "WHERE\r\n" + "c2.DOC_CODE = c.DOC_CODE\r\n" +
         * "AND c2.GROUP_ID = c.GROUP_ID\r\n" + "AND c2.SUBGROUP = c.SUBGROUP\r\n" +
         * "AND c2.RN = c.RN + 1\r\n" + ")\r\n" + "ORDER BY STATUS";
         * 
         */

        /*
         * String recursiveQuery = "WITH RECURSIVE " + "filtered_master AS ( " +
         * "  SELECT * FROM "+DBNAME+"."+SR_FLOW+" WHERE DOC_CODE = 'ITRQ' " + "), " +
         * "filtered_group AS ( " + "  SELECT * FROM "+DBNAME+"."+SR_GROUP+" WHERE WHS = 'A91' " +
         * "), " + "joined_data AS ( " +
         * "  SELECT m.DOC_CODE, m.GROUP, m.SUBGROUP, m.STATUS, g.NAME, " +
         * "         ROW_NUMBER() OVER (PARTITION BY m.DOC_CODE, m.GROUP, m.SUBGROUP ORDER BY g.NAME) AS RN "
         * + "  FROM filtered_master m " +
         * "  JOIN filtered_group g ON m.GROUP = g.PROGROUP AND m.SUBGROUP = g.SUBGROUP " + "), " +
         * "concat_cte (DOC_CODE, GROUP_ID, SUBGROUP, STATUS, RN, NAME_SERIAL) AS ( " +
         * "  SELECT DOC_CODE, GROUP, SUBGROUP, STATUS, RN, NAME FROM joined_data WHERE RN = 1 " +
         * "  UNION ALL " +
         * "  SELECT j.DOC_CODE, j.GROUP, j.SUBGROUP, j.STATUS, j.RN, c.NAME_SERIAL || ':' || j.NAME "
         * + "  FROM concat_cte c JOIN joined_data j " +
         * "  ON c.DOC_CODE = j.DOC_CODE AND c.GROUP_ID = j.GROUP AND c.SUBGROUP = j.SUBGROUP AND j.RN = c.RN + 1 "
         * + ") " + "SELECT DOC_CODE, STATUS, NAME_SERIAL FROM concat_cte c " +
         * "WHERE NOT EXISTS ( " + "  SELECT 1 FROM concat_cte c2 " +
         * "  WHERE c2.DOC_CODE = c.DOC_CODE AND c2.GROUP_ID = c.GROUP_ID AND c2.SUBGROUP = c.SUBGROUP AND c2.RN = c.RN + 1 "
         * + ") ORDER BY STATUS";
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
           * + "VALUES (" + "'" + docCode + "', " + "'" + currentID + "', " + "'" + approve + "', "
           * + "'-', " + "'" + status + "', " + "'Wait for approve', " + "'-', " + "'-',  " + "'" +
           * remark + "'" + ")";
           * 
           */

          String insertDetail = "INSERT INTO " + DBNAME + ".SR_FLOWAPPROVE "
              + "(FATYPE,FACONO,FADIVI, FACODE,FASRNO ,FAAPLI ,FAAPDA ,FASTAT , FADES1, FAENTI,FAENDA,FAAPBY,FADES2) "
              + "VALUES (" + " '1' , '" + comcono + "','" + comdivi + "','" + docCode + "', " + "'"
              + currentID + "', " + "'" + approve + "', " + "NULL, " + "'" + status + "', "
              + "'Wait for approve', " + "CURRENT TIME, " + "CURRENT DATE, " + "'',  " + "'"
              + remark + "'" + ")";

          logger.debug("xxxxxxin " + insertDetail);
          stmt2.executeUpdate(insertDetail);

        }

        /*
         * 
         * String query2 = "UPDATE "+DBNAME+"."+SR_APPROVE+" \n" +
         * "SET  STS_DESC = 'Approved',APPROVE = '"+username+"' , TIME_ST = '" + currentTimestamp +
         * "',APPROVED_USER = 'PP', APPROVE_DATE = '" + dateYYYYMMDD +
         * "' WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID + "' AND STATUS = '10' ";
         */

        String query2 = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n" + "SET FAENUS = '" + username
            + "', FADES1 = 'Approved',FAAPLI = '" + username
            + "' ,FAAPDA = CURRENT DATE, FAENTI = CURRENT TIME, FAAPTI = CURRENT TIME ,FAAPBY = '"
            + username + "', FAENDA = CURRENT DATE ,  FADES3 = '" + vRemark2
            + "' WHERE FACODE = 'ITRQ' AND FASRNO = '" + currentID
            + "' AND FASTAT = '00'  AND FACONO = '" + comcono + "' AND  FADIVI = '" + comdivi
            + "' ";

        logger.debug("xxxxxxin " + query2);

        stmt2.executeUpdate(query2);

        /*
         * 
         * String query222 = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n" + "SET APPROVE = '" +
         * depthead + "' \n" + "WHERE DOC_CODE = 'ITRQ' AND DOC_NO = '" + currentID +
         * "' AND STATUS = '20'";
         * 
         */

        String query222 = "UPDATE " + DBNAME + ".SR_FLOWAPPROVE \n" + "SET  FAAPLI = '" + depthead
            + "' \n" + "WHERE  FACODE = 'ITRQ' AND FASRNO  = '" + currentID
            + "' AND FASTAT = '10' AND FACONO = '" + comcono + "' AND FADIVI = '" + comdivi + "' ";

        logger.debug("xxxxxxin " + query222);
        stmt2.executeUpdate(query222);

        String data = SelectData.getSTATUSIDITEMRQ(currentID.toString(), comcono, comdivi);
        String url = "https://workflow.br-bangkokranch.com/webhook/sendtodb2";

        String response = HttpConnection.sendRequest("POST", url, Map.of("x-access-token",
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
            data, null // form-data
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

  public static String prepareInsertSRM(String vData, String username, String depthead,
      String constantSoftwareType) throws Exception {
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
    Map<String, String[]> companyMapping = new HashMap<>();
    companyMapping.put("10", new String[] {"10", "101"});
    companyMapping.put("600", new String[] {"600", "600"});
    companyMapping.put("500", new String[] {"500", "500"});
    // เพิ่มได้เรื่อยๆ เช่น
    // companyMapping.put("300", new String[] { "300", "301" });

    // ดึงข้อมูลตาม company
    String[] mapping = companyMapping.getOrDefault(company, new String[] {company, company});
    String comcono = mapping[0];
    String comdivi = mapping[1];


    String MaxNo = InsertSRMHead(vData, username, depthead, constantSoftwareType);

    InsertSRMDetail(vData, username, depthead, MaxNo);
    InsertSRMApprove(vData, username, depthead, MaxNo);
    UpdateData.UpdateSRMUserforinsert(username, MaxNo, comcono, comdivi, depthead);
    // SEND EMAIL
    SendEmail(vData, username, depthead, MaxNo);

    return MaxNo;
  }



  public static void SendEmail(String vData, String username, String depthead, String Maxno)
      throws Exception {
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
      String year2digit = requestDate.substring(0, 4).substring(2);
      String maxNo = "";

      Map<String, String[]> companyMapping = new HashMap<>();
      companyMapping.put("10", new String[] {"10", "101"});
      companyMapping.put("600", new String[] {"600", "600"});
      companyMapping.put("500", new String[] {"500", "500"});
      // เพิ่มได้เรื่อยๆ เช่น
      // companyMapping.put("300", new String[] { "300", "301" });

      // ดึงข้อมูลตาม company
      String[] mapping = companyMapping.getOrDefault(company, new String[] {company, company});
      String comcono = mapping[0];
      String comdivi = mapping[1];

      logger.debug("cono: " + comcono);
      logger.debug("divi: " + comdivi);

      String data = SelectData.getSTATUSIDSWRQ(Maxno.toString(), comcono, comdivi);
      System.out.println("test");
      String url = "https://workflow.br-bangkokranch.com/webhook/savesoftware_req";
      // String url = "https://workflow.br-bangkokranch.com/webhook-test/tst/savesoftware_req";
      // createsupplier

      String response = HttpConnection.sendRequest("POST", url, Collections.singletonMap(
          "x-access-token",
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
          data, null // form-data
      );



      logger.debug("vVersion: " + version);

    } catch (SQLException e) {
      logger.error("SQL Error: " + e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
      // return mJsonObj.toString();
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



  public static String InsertSRMHead(String vData, String username, String depthead,
      String constantSoftwareType) throws Exception {
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
      String year2digit = requestDate.substring(0, 4).substring(2);
      String maxNo = "";

      Map<String, String[]> companyMapping = new HashMap<>();
      companyMapping.put("10", new String[] {"10", "101"});
      companyMapping.put("600", new String[] {"600", "600"});
      companyMapping.put("500", new String[] {"500", "500"});
      // เพิ่มได้เรื่อยๆ เช่น
      // companyMapping.put("300", new String[] { "300", "301" });

      // ดึงข้อมูลตาม company
      String[] mapping = companyMapping.getOrDefault(company, new String[] {company, company});
      String comcono = mapping[0];
      String comdivi = mapping[1];

      logger.debug("cono: " + comcono);
      logger.debug("divi: " + comdivi);


      String insertQueryHead = "INSERT INTO " + DBNAME + ".SR_FLOWHEAD\n"
          + "(FHCONO,FHDIVI, FHCODE, FHSRNO,FHREQU ,FHENDA ,FHENTI,FHENUS ,FHREDA,FHHSTA ,FHDEPH , FHDSTA , FHDES1)\n"
          + "VALUES ('" + comcono + "','" + comdivi + "','SWRQ', ( SELECT \r\n"
          + "   RIGHT(TRIM(CHAR(YEAR(CURRENT DATE))), 2) ||\r\n" + "    RIGHT('000000' ||\n"
          + "          COALESCE(\n" + "              MAX(INTEGER(SUBSTR(FHSRNO,3,6))) + 1,\n"
          + "              1\n" + "          ),\n" + "    6) AS NEXT_NUMBER\n" + "FROM " + DBNAME
          + ".SR_FLOWHEAD sf \n" + "WHERE SUBSTR(FHSRNO,1,2) = " + year2digit
          + " AND FHCODE = 'SWRQ' ), '" + username + "'\n" + ", CURRENT DATE , CURRENT TIME,'"
          + username + "' ,CURRENT DATE, 2, '" + depthead + "'\n" + ", 10 , 'SWRQ-'||( SELECT \n"
          + "   RIGHT(TRIM(CHAR(YEAR(CURRENT DATE))), 2) ||\n" + "    RIGHT('000000' ||\r\n"
          + "          COALESCE(\r\n" + "              MAX(INTEGER(SUBSTR(FHSRNO,3,6))) + 1,\n"
          + "              1\r\n" + "          ),\r\n" + "    6) AS NEXT_NUMBER\r\n" + "FROM "
          + DBNAME + ".SR_FLOWHEAD sf \r\n"
          + "WHERE SUBSTR(FHSRNO,1,2) = 26 AND FHCODE = 'SWRQ')||'-" + "" + programtype + "/"
          + constantSoftwareType + "-" + softwareName + "')";
      logger.debug("Insert Query: " + insertQueryHead);
      stmt.executeUpdate(insertQueryHead);


      String getMaxNumber = "SELECT MAX(FHSRNO) as MAXNO\r\n" + "FROM " + DBNAME + ".SR_FLOWHEAD"
          + "\n" + "WHERE FHCONO = '" + comcono + "'\n" + "AND FHDIVI = '" + comdivi + "'\n"
          + "AND FHCODE = 'SWRQ'\n" + "AND FHREQU = '" + username + "'\n";

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



  public static String InsertSRMDetail(String vData, String username, String depthead, String maxNo)
      throws Exception {
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
      String year2digit = requestDate.substring(0, 4).substring(2);


      Map<String, String[]> companyMapping = new HashMap<>();
      companyMapping.put("10", new String[] {"10", "101"});
      companyMapping.put("600", new String[] {"600", "600"});
      companyMapping.put("500", new String[] {"500", "500"});
      // เพิ่มได้เรื่อยๆ เช่น
      // companyMapping.put("300", new String[] { "300", "301" });

      // ดึงข้อมูลตาม company
      String[] mapping = companyMapping.getOrDefault(company, new String[] {company, company});
      String comcono = mapping[0];
      String comdivi = mapping[1];
      String fdtype = "1";
      logger.debug("cono: " + comcono);
      logger.debug("divi: " + comdivi);



      String insertQuery = "INSERT INTO " + DBNAME + ".SR_FLOWDETAIL\n"
          + "(FDCONO,FDDIVI,FDTYPE,  FDDATA, FDSRNO,FDCODE, FDDSTA , FDENDA, FDENTI,FDENUS) \n"
          + "VALUES ('" + comcono + "','" + comdivi + "','" + fdtype + "','" + vData + "','" + maxNo
          + "'\n" + ",'SWRQ', '10', CURRENT DATE, CURRENT TIME ,'" + username.toString() + "')";

      logger.debug("Insert Query: " + insertQuery);
      // logger.debug("Insert Query: " + insertQueryHead);

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


  public static String InsertSRMApprove(String vData, String username, String depthead,
      String maxNo) throws Exception {
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
      String year2digit = requestDate.substring(0, 4).substring(2);


      Map<String, String[]> companyMapping = new HashMap<>();
      companyMapping.put("10", new String[] {"10", "101"});
      companyMapping.put("600", new String[] {"600", "600"});
      companyMapping.put("500", new String[] {"500", "500"});
      // เพิ่มได้เรื่อยๆ เช่น
      // companyMapping.put("300", new String[] { "300", "301" });

      // ดึงข้อมูลตาม company
      String[] mapping = companyMapping.getOrDefault(company, new String[] {company, company});
      String comcono = mapping[0];
      String comdivi = mapping[1];
      String fdtype = "1";
      logger.debug("cono: " + comcono);
      logger.debug("divi: " + comdivi);



      String recursiveQuery = "WITH RECURSIVE FILTERED_MASTER AS (\r\n" + "  SELECT *\r\n"
          + "  FROM " + DBNAME + ".SR_PROCESSMASTER\r\n" + "  WHERE PMCONO = '" + comcono + "'\r\n"
          + "    AND PMDIVI = '" + comdivi + "'\r\n" + "    AND PMCODE = 'SWRQ'\r\n" + "),\r\n"
          + "FILTERED_GROUP AS (\r\n" + "  SELECT *\r\n" + "  FROM " + DBNAME + ".SR_GROUPMASTER"
          + "\r\n" + "),\r\n" + "JOINED_DATA AS (\r\n" + "  SELECT\r\n" + "    M.PMCONO,\r\n"
          + "    M.PMDIVI,\r\n" + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n" + "    M.PMSGRO,\r\n"
          + "    M.PMSTAT,\r\n" + "    M.PMDES1,\r\n" + "    M.PMDES2,\r\n"
          + "    M.PMDES3,        -- ⭐ เพิ่ม\r\n" + "    M.PMDES4,        -- ⭐ เพิ่ม\r\n"
          + "    G.GMUSER,\r\n" + "    (\r\n" + "      SELECT MIN(M2.PMSTAT)\r\n" + "      FROM "
          + DBNAME + ".SR_PROCESSMASTER M2\r\n" + "      WHERE M2.PMCONO = M.PMCONO\r\n"
          + "        AND M2.PMDIVI = M.PMDIVI\r\n" + "        AND M2.PMCODE = M.PMCODE\r\n"
          + "        AND M2.PMSTAT > M.PMSTAT\r\n" + "    ) AS NEXT_STAT,\r\n"
          + "    ROW_NUMBER() OVER (\r\n"
          + "      PARTITION BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMSTAT\r\n" + "      ORDER BY\r\n"
          + "        CASE WHEN M.PMSGRO = 'MANAGER' THEN 0 ELSE 1 END,\r\n"
          + "        M.PMDES2 DESC,\r\n" + "        G.GMUSER ASC\r\n" + "    ) AS RN\r\n"
          + "  FROM FILTERED_MASTER M\r\n" + "  JOIN FILTERED_GROUP G\r\n"
          + "    ON M.PMCONO = G.GMCONO\r\n" + "   AND M.PMDIVI = G.GMDIVI\r\n"
          + "   AND M.PMGROU = G.GMGROU\r\n" + "   AND M.PMSGRO = G.GMSGRO\r\n" + "),\r\n"
          + "VACANT_FLAG AS (\r\n" + "  SELECT\r\n" + "    M.PMCONO,\r\n" + "    M.PMDIVI,\r\n"
          + "    M.PMCODE,\r\n" + "    M.PMGROU,\r\n" + "    M.PMSGRO,\r\n"
          + "    CASE WHEN COUNT(G2.GMUSER) > 0 THEN 'Y' ELSE 'N' END AS SKIP_IF_VACANT\r\n"
          + "  FROM FILTERED_MASTER M\r\n" + "  LEFT JOIN " + DBNAME + ".SR_GROUPMASTER G2\r\n"
          + "    ON G2.GMCONO = M.PMCONO\r\n" + "   AND G2.GMDIVI = M.PMDIVI\r\n"
          + "   AND G2.GMGROU = M.PMGROU\r\n" + "   AND G2.GMSGRO = M.PMSGRO\r\n"
          + "   AND UPPER(TRIM(G2.GMUSER)) = 'VACANT'\r\n"
          + "  GROUP BY M.PMCONO, M.PMDIVI, M.PMCODE, M.PMGROU, M.PMSGRO\r\n" + "),\r\n"
          + "CONCAT_CTE (\r\n" + "  PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
          + "  PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,   -- ⭐ เพิ่ม\r\n" + "  RN, NAME_SERIAL\r\n"
          + ") AS (\r\n" + "  SELECT\r\n" + "    PMCONO, PMDIVI, PMCODE, PMGROU, PMSGRO,\r\n"
          + "    PMSTAT, PMDES1, PMDES2, PMDES3, PMDES4,  -- ⭐ เพิ่ม\r\n" + "    RN,\r\n"
          + "    GMUSER AS NAME_SERIAL\r\n" + "  FROM JOINED_DATA\r\n" + "  WHERE RN = 1\r\n"
          + "  UNION ALL\r\n" + "  SELECT\r\n"
          + "    J.PMCONO, J.PMDIVI, J.PMCODE, J.PMGROU, J.PMSGRO,\r\n"
          + "    J.PMSTAT, J.PMDES1, J.PMDES2, J.PMDES3, J.PMDES4,  -- ⭐ เพิ่ม\r\n"
          + "    J.RN,\r\n" + "    C.NAME_SERIAL || ',' || J.GMUSER\r\n" + "  FROM CONCAT_CTE C\r\n"
          + "  JOIN JOINED_DATA J\r\n" + "    ON C.PMCONO = J.PMCONO\r\n"
          + "   AND C.PMDIVI = J.PMDIVI\r\n" + "   AND C.PMCODE = J.PMCODE\r\n"
          + "   AND C.PMGROU = J.PMGROU\r\n" + "   AND C.PMSGRO = J.PMSGRO\r\n"
          + "   AND J.RN = C.RN + 1\r\n" + ")\r\n" + "SELECT\r\n" + "  C.PMCONO,\r\n"
          + "  C.PMDIVI,\r\n" + "  C.PMCODE,\r\n" + "  C.PMGROU,\r\n" + "  C.PMSGRO,\r\n"
          + "  C.PMSTAT,\r\n" + "  C.PMDES1,\r\n" + "  C.PMDES2,\r\n"
          + "  C.PMDES3 AS NEXT_STAT,\r\n" + "  C.PMDES4 AS PREVIOUS_STAT,    \r\n"
          + "  C.NAME_SERIAL,\r\n" + "  V.SKIP_IF_VACANT\r\n" + "FROM CONCAT_CTE C\r\n"
          + "LEFT JOIN VACANT_FLAG V\r\n" + "  ON C.PMCONO = V.PMCONO\r\n"
          + " AND C.PMDIVI = V.PMDIVI\r\n" + " AND C.PMCODE = V.PMCODE\r\n"
          + " AND C.PMGROU = V.PMGROU\r\n" + " AND C.PMSGRO = V.PMSGRO\r\n"
          + "LEFT JOIN CONCAT_CTE C2\r\n" + "  ON C2.PMCONO = C.PMCONO\r\n"
          + " AND C2.PMDIVI = C.PMDIVI\r\n" + " AND C2.PMCODE = C.PMCODE\r\n"
          + " AND C2.PMGROU = C.PMGROU\r\n" + " AND C2.PMSGRO = C.PMSGRO\r\n"
          + " AND C2.RN = C.RN + 1\r\n" + "WHERE C2.PMCONO IS NULL\r\n"
          + "ORDER BY C.PMSTAT, C.PMGROU, C.PMSGRO\r\n" + "";

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
         * + "VALUES (" + "'" + docCode + "', " + "'" + currentID + "', " + "'" + approve + "', " +
         * "'-', " + "'" + status + "', " + "'Wait for approve', " + "'-', " + "'-',  " + "'" +
         * remark + "'" + ")";
         * 
         */

        String insertDetail = "INSERT INTO " + DBNAME + ".SR_FLOWAPPROVE "
            + "(FATYPE,FACONO,FADIVI, FACODE,FASRNO ,FAAPLI ,FAAPDA ,FASTAT , FADES1, FAENTI,FAENDA,FAAPBY,FADES2) "
            + "VALUES (" + " '1' , '" + comcono + "','" + comdivi + "','" + docCode + "', " + "'"
            + maxNo + "', " + "'" + approve + "', " + "NULL, " + "'" + status + "', "
            + "'Wait for approve', " + "CURRENT TIME, " + "CURRENT DATE, " + "'',  " + "'" + remark
            + "'" + ")";

        logger.debug("xxxxxxin " + insertDetail);
        stmt2.executeUpdate(insertDetail);

      }

      // logger.debug("Insert Query: " + insertQuery);
      // logger.debug("Insert Query: " + insertQueryHead);

      // stmt.executeUpdate(insertQuery);
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


  public static String prepareAddSoftwareForm(String cono, String divi, String servicename,
      String requestdate, String username, String depthead, String requesttype, String reason,
      String data, List<FormDataBodyPart> file, List<FormDataBodyPart> filename,
      List<FormDataBodyPart> fieldname) throws Exception {
    logger.info("prepareAddSurveyForm");

    JSONObject mJsonObj = new JSONObject();
    try {
      JSONObject dataObject = new JSONObject(data);
      Boolean getApproval =dataObject.getBoolean("vApproval");
      String usernameMain = dataObject.getString("usernameMain");
//      String getRequester = dataObject.getString("vRequester");
      String getDepthead = dataObject.getString("vDepthead");
      String getApproveBy = dataObject.getString("vApproveBy");
//      String getRemark = dataObject.getString("vRemark");
      String getVersion = dataObject.getString("vVersion");
      String getRequestDate = dataObject.getString("requestDateMain");
      
      String getSoftwareName = dataObject.getString("softwareName");
      logger.debug("getApproval {}, getDepthead {}, getApproveBy {}, getVersion {}", getApproval, getDepthead, getApproveBy, getVersion);
      String checkVersion = SelectData.checkVersion("SWRQ");
      if (getVersion == null || getVersion.isEmpty() || !Objects.equals(checkVersion, getVersion)) {
          mJsonObj.put("result", "nok");
          mJsonObj.put("message", "Can't Create service number, Please update your version to " + checkVersion
                  + " :  " + getVersion + " (Click F5 button).");
          return mJsonObj.toString();

      }
      
      String getRequestGroup = "", getNextStatus = "", getNextApprover = "", getNextGroup = "";
      String setDefaultStatus = "00";
      String[] getStatusNewFlowApprove =
          SelectData.getStatusNewFlowApprove(cono, divi, servicename, setDefaultStatus).split(";");
      getRequestGroup = getStatusNewFlowApprove[2].trim();
      getNextStatus = getStatusNewFlowApprove[3].trim();
      getNextApprover = getStatusNewFlowApprove[4].trim();
      getNextGroup = getStatusNewFlowApprove[5].trim();

      // Insert Head
      String serviceNumber =
          addSoftwareHead(cono, divi, servicename, getRequestDate, usernameMain, getDepthead);

      // Insert Detail
      addSoftwareDetail(cono, divi, servicename, serviceNumber, requesttype, data, usernameMain,
          getDepthead);
      logger.info("getNextStatus Before: " +getNextStatus);
      String setTitle =
          servicename + "-" + serviceNumber + "-" + requesttype.toUpperCase() + "-" + getSoftwareName;
      UpdateData.updateSurveyHeadTitle(cono, divi, servicename, serviceNumber, getNextStatus,
          setTitle, usernameMain);

      // Insert Flow Approve
      addSurveyFlowApprove(cono, divi, servicename, serviceNumber, requesttype);

      if (getRequestGroup.equals("REQUESTER")) {
        // Update Requester Name
        UpdateData.updateSurveyUserFlowApprove(cono, divi, servicename, serviceNumber,
            setDefaultStatus, usernameMain, usernameMain);

        // Submit first
        UpdateData.updateSurveyFlowApproveBy(cono, divi, servicename, serviceNumber,
            setDefaultStatus, "", usernameMain);
      }

      if (getNextGroup.equals("DEPTHEAD")) {
        // Update Depthead Name
        UpdateData.updateSurveyUserFlowApprove(cono, divi, servicename, serviceNumber,
            getNextStatus, usernameMain, getDepthead);
      }

      // Insert File
      if (file != null && !file.isEmpty()) {
        FileUtillity.prepareWriteToFileServer(cono, divi, servicename, serviceNumber, file,
            fieldname, usernameMain);
      }
      logger.info("getNextStatus After: " +getNextStatus);

      // Send mail n8n
      JSONObject body = new JSONObject();
      body.put("cono", cono);
      body.put("divi", divi);
      body.put("servicename", servicename);
      body.put("serviceno", serviceNumber);
      body.put("requesttype", requesttype);
      body.put("status", getNextStatus);

      String url = Constant.WORKFLOW;
      String response = HttpConnection.sendRequest("POST", url, Collections.singletonMap(
          "x-access-token",
          "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMCA6IDEwMSA6IOC4muC4o-C4tOC4qeC4seC4lyDguJrguLLguIfguIHguK3guIHguYHguKPguYnguJnguIrguYwg4LiI4Liz4LiB4Lix4LiUICjguKHguKvguLLguIrguJkpIiwiaXNzIjoiYXV0aGVuLXNlcnZpY2UiLCJhdWQiOiIwMTAyOTA2Iiwicm9sZSI6Ik1QTV8xQTEgOiBBUFBST1ZFIDogU0FMRU1BTiA6IDAiLCJleHAiOjE3NTAxNzY1NzF9.cAMs1gdcg3cxfYNTJi_WTHpBCKDxaw-MjwrDpmFPPSo"), // headers
          body.toString(), null // form-data
      );
//      String response = "yes";

      logger.info("body: " + body.toString());
      logger.info("response: " + response);

      mJsonObj.put("result", "ok");
      mJsonObj.put("message", "Service number " + serviceNumber);

    } catch (SQLException e) {
      logger.error(e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
    } catch (Exception e) {
      logger.error(e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
    }

    return mJsonObj.toString();

  }


  public static String addSurveyFile(String cono, String divi, String servicename, String serviceno,
      int fileIndex, String fieldname, String originalFileName, String filetype, String filename,
      String username) throws Exception {
    logger.info("addSurveyFile");

    JSONObject mJsonObj = new JSONObject();
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = ConnectDB2.doConnect();
      stmt = conn.createStatement();

      String query = "INSERT INTO " + DBNAME + ".SR_FILE \n"
          + "(FICONO, FIDIVI, FICODE, FISRNO, FILINE, FISNAM, FIFNAM, FITYPE, FIREM1, FIREM2, FIENDA, FIENTI, FIENUS) \n"
          + "VALUES(" + cono + " \n" + ", " + divi + " \n" + ", '" + servicename + "' \n" + ", '"
          + serviceno + "' \n" + ", '" + fileIndex + "' \n" + ", '" + fieldname + "' \n" + ", '"
          + originalFileName + "' \n" + ", '" + filetype + "' \n" + ", '" + filename + "' \n"
          + ", '' \n" + ", CURRENT DATE \n" + ", CURRENT TIME \n" + ", '" + username + "')";
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

  public static String addSoftwareDetail(String cono, String divi, String servicename, String serviceno,
      String requesttype, String data,
      String username, String depthead)
      throws Exception {
  logger.info("addSurveyDetail");

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
              + ", '10' \n"
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

  public static String addSoftwareHead(String cono, String divi, String servicename, String requestdate,
      String username, String depthead)
      throws Exception {
  logger.info("addSurveyHead");

  JSONObject mJsonObj = new JSONObject();
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
              + ", '10' \n"
              + ", CURRENT DATE \n"
              + ", CURRENT TIME \n"
              + ", '" + username + "')";
      // System.out.println("addADRHead\n" + query);
      logger.debug(query);
      stmt.execute(query);

      String getServiceNumber = SelectData.getMaxServiceNumber(cono, divi, servicename, getYear4, username);
      logger.debug("getServiceNumber {}", getServiceNumber);

      mJsonObj.put("result", "ok");
      mJsonObj.put("message", "Create complete.");

      return getServiceNumber;

  } catch (SQLException e) {
      logger.error(e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
  } catch (Exception e) {
      logger.error(e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
  } finally {
      try {
          if (stmt != null) {
              stmt.close();
          }
      } catch (SQLException e) {
          logger.error(e.getMessage());
          mJsonObj.put("result", "nok");
          mJsonObj.put("message", e.getMessage());
      }
      try {
          if (conn != null) {
              conn.close();
          }
      } catch (SQLException e) {
          logger.error(e.getMessage());
          mJsonObj.put("result", "nok");
          mJsonObj.put("message", e.getMessage());
      }

  }

  return mJsonObj.toString();

}

  public static String addSurveyFlowApprove(String cono, String divi, String servicename,
      String serviceno, String requesttype) throws Exception {
    logger.info("addSurveyFlowApprove");

    JSONObject mJsonObj = new JSONObject();
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = ConnectDB2.doConnect();
      stmt = conn.createStatement();

      String query = "INSERT INTO " + DBNAME + ".SR_FLOWAPPROVE \n"
          + "(FACONO, FADIVI, FACODE, FASRNO, FATYPE, FAAPLI, FAAPBY, FAAPDA, FAAPTI, FARJBY, FARJDA, FARJTI, FADES1, FADES2, FADES3, FADES4, FASTAT, FASTDE, FAENDA, FAENTI, FAENUS) \n"
          + "SELECT PMCONO, PMDIVI, PMCODE, '" + serviceno + "' AS PMSRNO \n"
          + ", (SELECT RQTYPE \n" + "FROM " + DBNAME + ".SR_REQUESTTYPE \n" + "WHERE RQCONO = '"
          + cono + "' \n" + "AND RQDIVI = '" + divi + "' \n" + "AND RQCODE = '" + servicename
          + "' \n" + "AND RQNAME = '" + requesttype + "') AS PMTYPE \n"
          + ", NAME_SERIAL, '' AS PMAPBY,CAST(NULL AS DATE) AS PMAPDA, CAST(NULL AS TIME) AS PMAPTI \n"
          + ", '' AS PMRJBY, CAST(NULL AS DATE) AS PMRJDA, CAST(NULL AS TIME) AS PMRJTI, 'Wait for approve' AS PMDES1, PMDES1 AS PMDES2, '' AS PMDES3, '' AS PMDES4 \n"
          + ", PMSTAT, '', CURRENT_DATE, CURRENT_TIME, 'M3SRVADM' \n" + "FROM " + DBNAME
          + ".VI_PROCESSMASTER \n" + "WHERE PMCONO = '" + cono + "' \n" + "AND PMDIVI = '" + divi
          + "' \n" + "AND PMCODE = '" + servicename + "' \n" + "ORDER BY INT(PMSTAT)";
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

  public static String addLogSendEmail(String document, String cono, String divi, String marno,
      String toemail, String ccemail, String sendfrom, String subject, String detail, String status,
      String createby) throws Exception {
    logger.info("addLogSendEmail");

    JSONObject mJsonObj = new JSONObject();
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = ConnectDB2.doConnect();
      stmt = conn.createStatement();

      String query = "INSERT INTO " + DBNAME + ".M3_STORAGEEMAILSEND \n"
          + "(ECONO, EDIVISION, EDDOCUMENT, EDREFNO, ESENTTO, ESENTCC, ESENTFROM, EDSUBJECT, EDDETAIL, EDSTATUSNO, CREATEBY, SENTDATE, SENTTIME) \n"
          + "VALUES ('" + cono + "' \n" + ", '" + divi + "' \n" + ", '" + document + "' \n" + ", '"
          + marno + "' \n" + ", '" + toemail + "' \n" + ", '" + ccemail + "' \n" + ", '" + sendfrom
          + "' \n" + ", '" + subject + "' \n" + ", '" + detail + "' \n" + ", '" + status + "' \n"
          + ", '" + createby + "' \n" + ", CURRENT DATE \n" + ", CURRENT TIME) ";
      // System.out.println("addLogSendEmail\n" + query);
      logger.debug(query);
      stmt.execute(query);

      mJsonObj.put("result", "ok");
      mJsonObj.put("message", "Insert complete.");
      logger.info("Insert complete.");

    } catch (SQLException e) {
      logger.error(e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
    } catch (Exception e) {
      logger.error(e.getMessage());
      mJsonObj.put("result", "nok");
      mJsonObj.put("message", e.getMessage());
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException e) {
        logger.error(e.getMessage());
        mJsonObj.put("result", "nok");
        mJsonObj.put("message", e.getMessage());
      }
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        logger.error(e.getMessage());
        mJsonObj.put("result", "nok");
        mJsonObj.put("message", e.getMessage());
      }

    }

    return mJsonObj.toString();

  }
}
