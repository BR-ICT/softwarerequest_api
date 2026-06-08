package com.br.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.br.data.DeleteData;
import com.br.data.InsertData;
import com.br.data.SelectData;
import com.br.data.UpdateData;
import com.sun.jersey.multipart.FormDataBodyPart;

public class FileUtillity {

  private static final Logger logger = LogManager.getLogger(FileUtillity.class);



  public static String writeToFileServerV3(InputStream inputStream, String fileName,
      String filePath) throws IOException {

    String qualifiedUploadFilePath = filePath + fileName;
    System.out.println("Saving file: " + qualifiedUploadFilePath);

    // ✅ ใช้ try-with-resources ปลอดภัยกว่า
    try (OutputStream outputStream =
        new FileOutputStream(new File(qualifiedUploadFilePath), false)) {
      byte[] bytes = new byte[1024];
      int read;
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
      throw ioe;
    }

    return qualifiedUploadFilePath;
  }



  public static JSONArray prepareGetToFileServer(String cono, String divi, String servicename,
      String serviceno) throws Exception {
    logger.info("prepareGetToFileServer");

    String filePath = "D:\\files\\api_project\\softwarerequest_files\\";


    String getSurveyFile = SelectData.getSurveyFile(cono, divi, servicename, serviceno);
    logger.info("getSurveyFile {}", getSurveyFile);
    // Fix #2: return empty array, not null
    if (getSurveyFile == null || getSurveyFile.trim().isEmpty()) {
      return new JSONArray();
    }

    JSONArray arr = new JSONArray(getSurveyFile);
    JSONArray resultArr = new JSONArray();
    for (int i = 0; i < arr.length(); i++) {
      JSONObject dbObj = arr.getJSONObject(i);
      String fileName = dbObj.optString("FIREM1", "");
      String itemName = dbObj.optString("FISNAM", "");
      String originfileName = dbObj.optString("FIFNAM", "");
      logger.info("fileName {}, itemName {}, originfileName {}", fileName, itemName,
          originfileName);

      // Fix #5: removed redundant null check
      if (fileName.trim().isEmpty()) {
        continue;
      }
      logger.info("filePath{},fileName{}", filePath,fileName);
      File file = new File(filePath, fileName);
      logger.info("file", file);
      // Fix #3: log when file is missing
      if (!file.exists()) {
        logger.warn("File not found on disk, skipping: {}", file.getAbsolutePath());
        continue;
      }

      byte[] fileBytes = Files.readAllBytes(file.toPath());
      String base64 = Base64.encodeBase64String(fileBytes);
      logger.info("base64{}", base64);
      String mimeType = Files.probeContentType(file.toPath());
      logger.info("mimeType", mimeType);
      if (mimeType == null)
        mimeType = "application/octet-stream";
      logger.info("serviceno" + serviceno);
      logger.info("itemName", itemName);
      logger.info("fileName", originfileName);
      logger.info("mimeType", mimeType);
      logger.info("base64" + base64);
      JSONObject fileObj = new JSONObject();
      fileObj.put("serviceno", serviceno);
      fileObj.put("itemName", itemName);
      fileObj.put("fileName", originfileName);
      fileObj.put("type", mimeType);
      fileObj.put("content", "data:" + mimeType + ";base64," + base64);

      resultArr.put(fileObj);
    }

    return resultArr;
  }


  public static String writeToFileServer(InputStream inputStream, String fileName, String filePath)
      throws IOException {

    OutputStream outputStream = null;
    String qualifiedUploadFilePath = filePath + fileName;

    System.out.print(qualifiedUploadFilePath);

    try {
      outputStream = new FileOutputStream(new File(qualifiedUploadFilePath), false);
      int read = 0;
      byte[] bytes = new byte[1024];
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
      outputStream.flush();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      outputStream.close();
    }
    return qualifiedUploadFilePath;

  }


  public static JSONArray saveUploadedFiles(List<FormDataBodyPart> fileParts,
      List<FormDataBodyPart> fieldNameParts, String filePath, String cono, String divi, String vID,
      String username

  ) throws Exception {


    System.out.println("fileParts size = " + fileParts.size());

    JSONArray outputs = new JSONArray();

    for (int i = 0; i < fileParts.size(); i++) {

      FormDataBodyPart filePart = fileParts.get(i);

      InputStream fileInputStream = filePart.getValueAs(InputStream.class);
      String originalFileName = filePart.getContentDisposition().getFileName();
      if (originalFileName != null) {
        originalFileName = new String(originalFileName.getBytes("ISO-8859-1"), "UTF-8");
      }

      String fieldName = fieldNameParts.get(i).getValue();
      if (fieldName != null) {
        fieldName = new String(fieldName.getBytes("ISO-8859-1"), "UTF-8");
      }

      //
      // String originalFileName = filePart.getContentDisposition().getFileName();
      //
      // String fieldName = fieldNameParts.get(i).getValue();
      //
      int fileIndex = i + 1;

      String newFileName = vID + '_' + fileIndex + '_' + originalFileName;

      String filetype = filePart.getMediaType().toString();



      writeToFileServer(fileInputStream, newFileName, filePath);

      JSONObject obj = new JSONObject();
      obj.put("originalName", originalFileName);
      obj.put("savedName", newFileName);
      obj.put("fieldName", fieldName);


      InsertData.insertFILETODB(cono, divi, vID, newFileName, filetype, fieldName, username,
          fileIndex, originalFileName);

      outputs.put(obj);

    }

    return outputs;
  }

  public static String writeToFileServerV2(HttpServletRequest httpServletRequest, InputStream file,
      String fileName, String filePath) throws IOException {
    logger.info("writeToFileServerV2");

    String path = httpServletRequest.getRealPath("/") + "WEB-INF\\marfile\\" + filePath;
    logger.debug("path {}", path);
    OutputStream outputStream = null;
    String qualifiedUploadFilePath = path;
    try {
      outputStream = new FileOutputStream(new File(qualifiedUploadFilePath), false);
      int read = 0;
      byte[] bytes = new byte[1024];
      while ((read = file.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
      outputStream.flush();
      logger.debug("Write file complete.");

    } catch (IOException e) {
      logger.error(e.getMessage());
    } finally {
      outputStream.close();
    }
    return qualifiedUploadFilePath;

  }



  public static void deleteFileServer(String filePath) throws IOException {

    // System.out.println("delete " + filePath);
    try {
      File file = new File(filePath);

      if (file.delete()) {
        System.out.println(file.getName() + " is deleted!");
      } else {
        System.out.println("Sorry, unable to delete the file.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  public static void deleteFileServerV2(String filePath) throws IOException {

    // System.out.println("delete " + filePath);
    try {
      File file = new File(filePath);

      file.deleteOnExit();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }



  public static String addFile(InputStream inputStream, String fileName, String filePath)
      throws IOException {

    OutputStream outputStream = null;
    String qualifiedUploadFilePath = filePath + fileName;

    try {
      outputStream = new FileOutputStream(new File(qualifiedUploadFilePath), false);
      int read = 0;
      byte[] bytes = new byte[1024];
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
      outputStream.flush();

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      outputStream.close();
    }
    return qualifiedUploadFilePath;

  }

  public static File getFile(HttpServletRequest httpServletRequest, String fileName)
      throws IOException {

    String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\" + fileName;
    System.out.println("getFile " + filePath);

    File file = new File(filePath);
    return file;

    // System.out.println("deleteFile " + filePath);
    // try {
    // File file = new File(filePath);
    // return file;
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return null;

  }

  public static Response getFileV3(HttpServletRequest httpServletRequest, String fileName) {

    String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\marfile\\" + fileName;
    System.out.println("getFile " + filePath);

    File file = new File(filePath);

    if (!file.exists()) {
      return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
    }

    InputStream inputStream = null;
    try {
      String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
      inputStream = new FileInputStream(file);
      Response.ResponseBuilder response = Response.ok(inputStream);
      response.header("Content-Disposition", "attachment; filename=" + encodedFileName + "");
      return response.build();

    } catch (IOException e) {
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Error while reading the file.").build();
    }

  }

  public static Response deleteFileV3(HttpServletRequest httpServletRequest, String fileName) {

    String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\" + fileName;
    System.out.println("deleteFile " + filePath);

    File file = new File(filePath);

    if (!file.exists()) {
      return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
    }

    if (file.delete()) {
      return Response.status(Response.Status.OK).entity("File deleted successfully").build();
    } else {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Failed to delete the file").build();
    }

  }

  public static void deleteFile(String filePath) throws IOException {

    // System.out.println("deleteFile " + filePath);
    try {
      File file = new File(filePath);

      if (file.delete()) {
        System.out.println(file.getName() + " is deleted!");
      } else {
        System.out.println("Sorry, unable to delete the file.");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static Response deleteFileV4(HttpServletRequest httpServletRequest, String filePath) {
    logger.info("deleteFileV4");

    String path = httpServletRequest.getRealPath("/") + "WEB-INF\\marfile\\" + filePath;
    logger.debug("path {}", path);

    File file = new File(path);

    if (!file.exists()) {
      logger.error("File not found");
      return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
    }

    if (file.delete()) {
      logger.info("File deleted successfully");
      return Response.status(Response.Status.OK).entity("File deleted successfully").build();
    } else {
      logger.error("Failed to delete the file");
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity("Failed to delete the file").build();
    }

  }

  public static JSONArray prepareWriteToFileServer(String cono, String divi, String servicename,
      String serviceno, List<FormDataBodyPart> file, List<FormDataBodyPart> fieldname,
      String username) throws Exception {
    logger.info("prepareWriteToFileServer");
    logger.info("file {}", file.size());

    // Fix #1: validate list sizes match
    if (file.size() != fieldname.size()) {
      throw new IllegalArgumentException("file and fieldname lists must be the same size");
    }

    String filePath = "D:\\files\\api_project\\softwarerequest_files\\"; // Windows Server
    // String filePath = "/home/wattana/files/api_project/servicerequest_files"; //
    // Ubuntu Server

    JSONArray mJsonObj = new JSONArray();
    for (int i = 0; i < file.size(); i++) {
      int fileIndex = i + 1;
      try {
        FormDataBodyPart filePart = file.get(i);
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);

        String fieldName = fieldname.get(i).getValue();
        String originalFileName =
            normalizeUploadedFileName(filePart.getContentDisposition().getFileName());
        // Fix #2: normalize once, don't re-normalize the assembled name
        String newFileName = serviceno + "_" + fileIndex + "_" + originalFileName;
        String fileType = filePart.getMediaType().toString();

        writeFileServer(fileInputStream, newFileName, filePath);

        // Fix #4: insert DB only after confirming file exists on disk
        File savedFile = new File(filePath, newFileName);
        if (!savedFile.exists()) {
          logger.error("File was not written to disk, skipping DB insert: {}", newFileName);
          continue;
        }

        InsertData.addSurveyFile(cono, divi, servicename, serviceno, fileIndex, fieldName,
            originalFileName, fileType, newFileName, username);

        JSONObject obj = new JSONObject();
        obj.put("originalName", originalFileName);
        obj.put("savedName", newFileName);
        obj.put("fieldName", fieldName);

        mJsonObj.put(obj);

        // Fix #5: handle all exceptions consistently — log and re-throw
      } catch (Exception e) {
        logger.error("Failed to process file at index {}: {}", fileIndex, e.getMessage());
        throw e;
      }
    }

    return mJsonObj;
  }

  private static String normalizeUploadedFileName(String fileName) {
    if (fileName == null) {
      return "";
    }

    String normalized = new File(fileName).getName().trim();
    if (normalized.isEmpty()) {
      return normalized;
    }

    if (looksMisdecoded(normalized)) {
      normalized =
          new String(normalized.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    return normalized;
  }

  private static boolean looksMisdecoded(String value) {
    return value.indexOf('\u00E0') >= 0 || value.indexOf('\u00C3') >= 0
        || value.indexOf('\u00E2') >= 0;
  }

  public static String writeFileServer(InputStream inputStream, String fileName, String filePath)
      throws IOException {

    OutputStream outputStream = null;
    String qualifiedUploadFilePath = filePath + fileName;

    try {
      outputStream = new FileOutputStream(new File(qualifiedUploadFilePath), false);
      int read = 0;
      byte[] bytes = new byte[1024];
      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
      outputStream.flush();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      outputStream.close();
    }
    return qualifiedUploadFilePath;

  }

  public static JSONArray prepareUpdateToFileServer(String cono, String divi, String servicename,
      String serviceno, List<FormDataBodyPart> file, List<FormDataBodyPart> fieldname,
      String username) throws Exception {
    logger.info("prepareUpdateToFileServer");
    logger.info("file {}", file.size());

    if (file.size() != fieldname.size()) {
      throw new IllegalArgumentException("file and fieldname lists must be the same size");
    }

    String filePath = "D:\\files\\api_project\\servicerequest_files\\"; // Windows Server
    // String filePath = "/home/wattana/files/api_project/servicerequest_files"; // Ubuntu Server

    // Step 1: load existing records \u2192 map by FISNAM (field name)
    String existingJson = SelectData.getSurveyFile(cono, divi, servicename, serviceno);
    Map<String, JSONObject> existingMap = new HashMap<>();
    int maxLine = 0;

    if (existingJson != null && !existingJson.trim().isEmpty()) {
      JSONArray existing = new JSONArray(existingJson);
      for (int i = 0; i < existing.length(); i++) {
        JSONObject obj = existing.getJSONObject(i);
        String fName = obj.optString("FISNAM", "").trim();
        int line = obj.optInt("FILINE", 0);
        if (!fName.isEmpty()) {
          existingMap.put(fName, obj);
        }
        if (line > maxLine)
          maxLine = line;
      }
    }

    logger.info("existingMap size {}, maxLine {}", existingMap.size(), maxLine);

    // Build set of incoming fieldNames for orphan detection
    Set<String> incomingFieldNames = new HashSet<>();
    for (int i = 0; i < fieldname.size(); i++) {
      incomingFieldNames.add(fieldname.get(i).getValue());
    }

    JSONArray resultArr = new JSONArray();

    // Step 2: process each incoming file
    for (int i = 0; i < file.size(); i++) {
      try {
        FormDataBodyPart filePart = file.get(i);
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);
        String fieldName = fieldname.get(i).getValue();
        String originalFileName =
            normalizeUploadedFileName(filePart.getContentDisposition().getFileName());
        String fileType = filePart.getMediaType().toString();

        logger.info("processing fieldName {}, originalFileName {}", fieldName, originalFileName);

        JSONObject resultObj = new JSONObject();
        resultObj.put("fieldName", fieldName);
        resultObj.put("originalName", originalFileName);

        if (existingMap.containsKey(fieldName)) {
          // UPDATE: same field name already exists \u2192 replace file and update DB row
          JSONObject existing = existingMap.get(fieldName);
          String oldSavedName = existing.optString("FIREM1", "").trim();
          int lineNo = existing.optInt("FILINE", 0);
          String oldOriginalFileName = existing.optString("FIFNAM", "").trim();
          String oldFileType = existing.optString("FITYPE", "").trim();

          boolean fileNameChanged = !originalFileName.equals(oldOriginalFileName);
          boolean fileTypeChanged = !fileType.equals(oldFileType);

          if (!fileNameChanged && !fileTypeChanged) {
            logger.info(
                "fieldName {} unchanged (originalFileName={}, fileType={}), skipping update",
                fieldName, originalFileName, fileType);
            resultObj.put("action", "skip");
            resultArr.put(resultObj);
            continue;
          }

          String newFileName = serviceno + "_" + lineNo + "_" + originalFileName;

          // Delete old file from disk
          if (!oldSavedName.isEmpty()) {
            deleteFileServer(filePath + oldSavedName);
          }

          // Write new file
          writeFileServer(fileInputStream, newFileName, filePath);

          if (!new File(filePath, newFileName).exists()) {
            logger.error("File was not written to disk, skipping DB update: {}", newFileName);
            continue;
          }

          UpdateData.updateSurveyFile(cono, divi, servicename, serviceno, fieldName,
              originalFileName, fileType, newFileName, username);

          resultObj.put("action", "update");
          resultObj.put("savedName", newFileName);

        } else {
          // INSERT: new field \u2192 write file and add new DB row
          maxLine++;
          String newFileName = serviceno + "_" + maxLine + "_" + originalFileName;

          writeFileServer(fileInputStream, newFileName, filePath);

          if (!new File(filePath, newFileName).exists()) {
            logger.error("File was not written to disk, skipping DB insert: {}", newFileName);
            continue;
          }

          InsertData.addSurveyFile(cono, divi, servicename, serviceno, maxLine, fieldName,
              originalFileName, fileType, newFileName, username);

          resultObj.put("action", "insert");
          resultObj.put("savedName", newFileName);
        }

        resultArr.put(resultObj);

      } catch (Exception e) {
        logger.error("Failed to process file at index {}: {}", i + 1, e.getMessage());
        throw e;
      }
    }

    // Step 3: delete orphaned files (exist in DB but not in new submission)
    for (Map.Entry<String, JSONObject> entry : existingMap.entrySet()) {
      String existingFieldName = entry.getKey();
      if (!incomingFieldNames.contains(existingFieldName)) {
        JSONObject orphan = entry.getValue();
        String orphanSavedName = orphan.optString("FIREM1", "").trim();
        logger.info("orphan detected fieldName {}, savedName {}", existingFieldName,
            orphanSavedName);

        if (!orphanSavedName.isEmpty()) {
          deleteFileServer(filePath + orphanSavedName);
        }

        DeleteData.deleteSurveyFile(cono, divi, servicename, serviceno, existingFieldName);

        JSONObject deletedObj = new JSONObject();
        deletedObj.put("fieldName", existingFieldName);
        deletedObj.put("action", "delete");
        resultArr.put(deletedObj);
      }
    }

    return resultArr;
  }

}
