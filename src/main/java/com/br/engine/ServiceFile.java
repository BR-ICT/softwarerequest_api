package com.br.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.multipart.FormDataBodyPart;

public class ServiceFile {

	private static final Logger logger = LogManager.getLogger(ServiceFile.class);

	// Every file of this API's forms is written here (SR_FILE.FIREM1 keeps the saved name):
	// FILEBASEDIR / FILEFOLDER of config.properties, e.g. /mnt/api_files/itemrequest_files
	static final String FILE_DIR = fileDir(EngineConfig.FILEFOLDER);

	/**
	 * Folder under FILEBASEDIR (config.properties of the env), ending with the separator: callers append names.
	 * The system property FILEBASEDIR wins: the tests (surefire, pom.xml) use target/test-files, never the
	 * real folders of the env being built.
	 */
	static String fileDir(String name) {
		String base = System.getProperty("FILEBASEDIR", EngineConfig.FILEBASEDIR);
		if (base == null || base.trim().isEmpty()) {
			throw new IllegalStateException("FILEBASEDIR is not set in config.properties");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalStateException("FILEFOLDER is not set in config.properties");
		}
		return new File(base.trim(), name.trim()).getPath() + File.separator;
	}

	/**
	 * Saved name of a new file: cono_divi_service_serviceno_line_name. The service number runs per
	 * cono / divi / service / year (every one starts at YY000001), so all of them are needed to keep
	 * the name unique in one folder. Old files keep their old name (serviceno_line_name) in FIREM1.
	 */
	static String savedFileName(String cono, String divi, String servicename, String serviceno, int line,
			String originalFileName) {
		return cono.trim() + "_" + divi.trim() + "_" + servicename.trim() + "_" + serviceno.trim() + "_" + line
				+ "_" + originalFileName;
	}

	/** Where a saved file is (old files keep their old name, serviceno_line_name, in the same folder). */
	static File locateFile(String servicename, String savedName) {
		return new File(FILE_DIR, savedName);
	}

	public static JSONArray prepareWriteToFileServer(String cono, String divi, String servicename, String serviceno,
			List<FormDataBodyPart> file, List<FormDataBodyPart> fieldname, String username) throws Exception {
		logger.info("prepareWriteToFileServer");
		logger.info("file {}", file.size());

		// Fix #1: validate list sizes match
		if (file.size() != fieldname.size()) {
			throw new IllegalArgumentException("file and fieldname lists must be the same size");
		}

		String filePath = FILE_DIR;

		JSONArray mJsonObj = new JSONArray();
		for (int i = 0; i < file.size(); i++) {
			int fileIndex = i + 1;
			try {
				FormDataBodyPart filePart = file.get(i);
				InputStream fileInputStream = filePart.getValueAs(InputStream.class);

				String fieldName = fieldname.get(i).getValue();
				String originalFileName = normalizeUploadedFileName(filePart.getContentDisposition().getFileName());
				// Fix #2: normalize once, don't re-normalize the assembled name
				String newFileName = savedFileName(cono, divi, servicename, serviceno, fileIndex, originalFileName);
				String fileType = filePart.getMediaType().toString();

				writeFileServer(fileInputStream, newFileName, filePath);

				// Fix #4: insert DB only after confirming file exists on disk
				File savedFile = new File(filePath, newFileName);
				if (!savedFile.exists()) {
					logger.error("File was not written to disk, skipping DB insert: {}", newFileName);
					continue;
				}

				FlowInsert.addServiceFile(cono, divi, servicename, serviceno, fileIndex, fieldName, originalFileName,
						fileType, newFileName, username);

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

	public static JSONArray prepareGetToFileServer(String cono, String divi, String servicename, String serviceno)
			throws Exception {
		logger.info("prepareGetToFileServer");

		String getServiceFile = FlowQuery.getServiceFile(cono, divi, servicename, serviceno);

		// Fix #2: return empty array, not null
		if (getServiceFile == null || getServiceFile.trim().isEmpty()) {
			return new JSONArray();
		}

		JSONArray arr = new JSONArray(getServiceFile);
		JSONArray resultArr = new JSONArray();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject dbObj = arr.getJSONObject(i);
			String fileName = dbObj.optString("FIREM1", "");
			String itemName = dbObj.optString("FISNAM", "");
			String originfileName = dbObj.optString("FIFNAM", "");
			logger.info("fileName {}, itemName {}, originfileName {}", fileName, itemName, originfileName);

			// Fix #5: removed redundant null check
			if (fileName.trim().isEmpty()) {
				continue;
			}

			File file = locateFile(servicename, fileName);

			// Fix #3: log when file is missing
			if (!file.exists()) {
				logger.warn("File not found on disk, skipping: {}", file.getAbsolutePath());
				continue;
			}

			byte[] fileBytes = Files.readAllBytes(file.toPath());
			String base64 = Base64.getEncoder().encodeToString(fileBytes);

			String mimeType = Files.probeContentType(file.toPath());
			if (mimeType == null)
				mimeType = "application/octet-stream";

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

	public static JSONArray prepareUpdateToFileServer(String cono, String divi, String servicename, String serviceno,
			List<FormDataBodyPart> file, List<FormDataBodyPart> fieldname, String username) throws Exception {
		logger.info("prepareUpdateToFileServer");
		logger.info("file {}", file.size());

		if (file.size() != fieldname.size()) {
			throw new IllegalArgumentException("file and fieldname lists must be the same size");
		}

		String filePath = FILE_DIR;

		// Step 1: load existing records \u2192 map by FISNAM (field name)
		String existingJson = FlowQuery.getServiceFile(cono, divi, servicename, serviceno);
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
				if (line > maxLine) maxLine = line;
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
				String originalFileName = normalizeUploadedFileName(filePart.getContentDisposition().getFileName());
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
						logger.info("fieldName {} unchanged (originalFileName={}, fileType={}), skipping update", fieldName, originalFileName, fileType);
						resultObj.put("action", "skip");
						resultArr.put(resultObj);
						continue;
					}

					String newFileName = savedFileName(cono, divi, servicename, serviceno, lineNo, originalFileName);

					// Delete old file from disk (it may still be in the service's old folder)
					if (!oldSavedName.isEmpty()) {
						deleteFileServer(locateFile(servicename, oldSavedName).getPath());
					}

					// Write new file
					writeFileServer(fileInputStream, newFileName, filePath);

					if (!new File(filePath, newFileName).exists()) {
						logger.error("File was not written to disk, skipping DB update: {}", newFileName);
						continue;
					}

					FlowUpdate.updateServiceFile(cono, divi, servicename, serviceno, fieldName, originalFileName,
							fileType, newFileName, username);

					resultObj.put("action", "update");
					resultObj.put("savedName", newFileName);

				} else {
					// INSERT: new field \u2192 write file and add new DB row
					maxLine++;
					String newFileName = savedFileName(cono, divi, servicename, serviceno, maxLine, originalFileName);

					writeFileServer(fileInputStream, newFileName, filePath);

					if (!new File(filePath, newFileName).exists()) {
						logger.error("File was not written to disk, skipping DB insert: {}", newFileName);
						continue;
					}

					FlowInsert.addServiceFile(cono, divi, servicename, serviceno, maxLine, fieldName, originalFileName,
							fileType, newFileName, username);

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
				logger.info("orphan detected fieldName {}, savedName {}", existingFieldName, orphanSavedName);

				if (!orphanSavedName.isEmpty()) {
					deleteFileServer(locateFile(servicename, orphanSavedName).getPath());
				}

				FlowDelete.deleteServiceFile(cono, divi, servicename, serviceno, existingFieldName);

				JSONObject deletedObj = new JSONObject();
				deletedObj.put("fieldName", existingFieldName);
				deletedObj.put("action", "delete");
				resultArr.put(deletedObj);
			}
		}

		return resultArr;
	}

	/**
	 * Throws when the file cannot be written completely (folder missing, disk / share full, upload cut off):
	 * a half written file must not get its SR_FILE row. The half written file is removed.
	 */
	public static String writeFileServer(InputStream inputStream, String fileName, String filePath)
			throws IOException {

		String qualifiedUploadFilePath = filePath + fileName;
		File target = new File(qualifiedUploadFilePath);

		try (OutputStream outputStream = new FileOutputStream(target, false)) {
			int read = 0;
			byte[] bytes = new byte[8192];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
		} catch (IOException ioe) {
			logger.error("Can't write file {}: {}", qualifiedUploadFilePath, ioe.getMessage());
			if (target.exists() && !target.delete()) {
				logger.error("Can't remove the half written file {}", qualifiedUploadFilePath);
			}
			throw new IOException("Can't save file " + fileName + ": " + ioe.getMessage(), ioe);
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

	private static String normalizeUploadedFileName(String fileName) {
		if (fileName == null) {
			return "";
		}

		String normalized = new File(fileName).getName().trim();
		if (normalized.isEmpty()) {
			return normalized;
		}

		if (looksMisdecoded(normalized)) {
			normalized = new String(normalized.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		}

		return normalized;
	}

	private static boolean looksMisdecoded(String value) {
		return value.indexOf('\u00E0') >= 0 || value.indexOf('\u00C3') >= 0 || value.indexOf('\u00E2') >= 0;
	}
}
