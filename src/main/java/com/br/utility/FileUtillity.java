package com.br.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import com.br.data.InsertData;
import com.sun.jersey.multipart.FormDataBodyPart;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class FileUtillity {

	private static final Logger logger = LogManager.getLogger(FileUtillity.class);

	
	
	
	

	public static String writeToFileServerV3(InputStream inputStream, String fileName, String filePath)
	        throws IOException {

	    String qualifiedUploadFilePath = filePath + fileName;
	    System.out.println("Saving file: " + qualifiedUploadFilePath);

	    // ✅ ใช้ try-with-resources ปลอดภัยกว่า
	    try (OutputStream outputStream = new FileOutputStream(new File(qualifiedUploadFilePath), false)) {
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
	

	public static JSONArray saveUploadedFiles(
	         List<FormDataBodyPart> fileParts,
	         List<FormDataBodyPart> fieldNameParts,
	         String filePath,
	              String cono,
	              String divi,
	              String vID,
	              String username
	         
	 ) throws Exception {
	  
	  
	  System.out.println("fileParts size = " + fileParts.size());

	     JSONArray outputs = new JSONArray();

	     for (int i = 0; i < fileParts.size(); i++) {

	         FormDataBodyPart filePart = fileParts.get(i);

	         InputStream fileInputStream = filePart.getValueAs(InputStream.class);

	         String originalFileName = filePart.getContentDisposition().getFileName();

	         String fieldName = fieldNameParts.get(i).getValue();
	         
	         int fileIndex = i + 1;  

	         String newFileName = +'_'+fileIndex+'_'+originalFileName;
	         
	         String filetype = filePart.getMediaType().toString();
	         
	        

	         writeToFileServer(fileInputStream, newFileName, filePath);

	         JSONObject obj = new JSONObject();
	         obj.put("originalName", originalFileName);
	         obj.put("savedName", newFileName);
	         obj.put("fieldName", fieldName);
	         
	         
	         InsertData.insertFILETODB( cono, divi,vID,newFileName, filetype,fieldName, username,fileIndex, originalFileName);

	         outputs.put(obj);
	         
	     }

	     return outputs;
	 }

	public static String writeToFileServerV2(HttpServletRequest httpServletRequest, InputStream file, String fileName,
			String filePath) throws IOException {
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
	
	

	public static String addFile(InputStream inputStream, String fileName, String filePath) throws IOException {

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

	public static File getFile(HttpServletRequest httpServletRequest, String fileName) throws IOException {

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
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error while reading the file.")
					.build();
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
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete the file").build();
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
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete the file").build();
		}

	}

}
