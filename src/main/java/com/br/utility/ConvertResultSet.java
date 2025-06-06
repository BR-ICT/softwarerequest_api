package com.br.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.ibm.as400.util.BASE64Encoder;

public class ConvertResultSet {

	public static String convertResultSetToJson(ResultSet rsset)
			throws SQLException {

		JSONArray mJSonArr = new JSONArray();
		ResultSetMetaData metadata = rsset.getMetaData();
		int numColumns = metadata.getColumnCount();

		while (rsset.next()) {
			JSONObject mJsonObj = new JSONObject();
			for (int i = 1; i <= numColumns; ++i) {
				try {
					String column_name = metadata.getColumnName(i);
					// mJsonObj.put(column_name, rsset.getObject(column_name));
					mJsonObj.put(column_name, rsset.getString(column_name));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// System.out.println(mJsonObj);
			mJSonArr.put(mJsonObj);
		}
		return mJSonArr.toString();
	}
	
	
	public static String convertResultSetToJsonVPP( ResultSet rsset)
			throws SQLException, IOException {

		JSONArray mJSonArr = new JSONArray();
		ResultSetMetaData metadata = rsset.getMetaData();
		int numColumns = metadata.getColumnCount();
		System.out.println(numColumns);
		while (rsset.next()) {
			JSONObject mJsonObj = new JSONObject();
			for (int i = 1; i <= numColumns; ++i) {
				try {
					String column_name = metadata.getColumnName(i);

					System.out.println(column_name);
					if (column_name.equals("IMG") || column_name.equals("IMG")) {
//					if (column_name.equals("ADIMAG")) {
//						System.out.println(column_name);
						mJsonObj.put(column_name, rsset.getString(column_name));

						System.out.println("zzzzz");
						System.out.println(column_name);
						if (rsset.getString(column_name).length() > 0) {
							
							System.out.println("xxxxx");
							String fileName = rsset.getString(column_name);
							String filePath = "D:\\files\\api_project\\visitor_file\\" + fileName;
//							String filePath = "D:\\files\\api_project\\adr_images\\" + fileName;
//							System.out.println("filePath: " + filePath);

							File file = new File(filePath);
							boolean fileExists = file.exists();
							if(fileExists) {
								System.out.println("xxxxx22222");
								// encode the file to string
								byte[] fileContent = FileUtils.readFileToByteArray(file);
								String encodedString = Base64.getEncoder().encodeToString(fileContent);
								mJsonObj.put("IMAGE", encodedString);
								
							} else {
								mJsonObj.put("IMAGE", "");
								
							}
								

						} else {
							mJsonObj.put("IMAGE", "");
							
						}
						
						
					} else {
						mJsonObj.put(column_name, rsset.getString(column_name));
						
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// System.out.println(mJsonObj);
			mJSonArr.put(mJsonObj);
		}
		return mJSonArr.toString();
	}

	
	public static String convertResultSetToJsonVPP2( ResultSet rsset)
			throws SQLException, IOException {

		JSONArray mJSonArr = new JSONArray();
		ResultSetMetaData metadata = rsset.getMetaData();
		int numColumns = metadata.getColumnCount();
		System.out.println(numColumns);
		while (rsset.next()) {
			JSONObject mJsonObj = new JSONObject();
			for (int i = 1; i <= numColumns; ++i) {
				try {
					String column_name = metadata.getColumnName(i);

					System.out.println(column_name);
					if (column_name.equals("H_IMG") || column_name.equals("IMG")) {
//					if (column_name.equals("ADIMAG")) {
//						System.out.println(column_name);
						mJsonObj.put(column_name, rsset.getString(column_name));

						System.out.println("zzzzz");
						System.out.println(column_name);
						if (rsset.getString(column_name).length() > 0) {
							
							System.out.println("xxxxx");
							String fileName = rsset.getString(column_name);
							String filePath = "D:\\files\\api_project\\visitor_file\\" + fileName;
//							String filePath = "D:\\files\\api_project\\adr_images\\" + fileName;
//							System.out.println("filePath: " + filePath);

							File file = new File(filePath);
							boolean fileExists = file.exists();
							if(fileExists) {
								System.out.println("xxxxx22222");
								// encode the file to string
								byte[] fileContent = FileUtils.readFileToByteArray(file);
								String encodedString = Base64.getEncoder().encodeToString(fileContent);
								mJsonObj.put("IMAGE", encodedString);
								
							} else {
								mJsonObj.put("IMAGE", "");
								
							}
								

						} else {
							mJsonObj.put("IMAGE", "");
							
						}
						
						
					} else {
						mJsonObj.put(column_name, rsset.getString(column_name));
						
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// System.out.println(mJsonObj);
			mJSonArr.put(mJsonObj);
		}
		return mJSonArr.toString();
	}
	public static String convertResultSetToJsonV2(@Context HttpServletRequest httpServletRequest, ResultSet rsset)
			throws SQLException, IOException {

		JSONArray mJSonArr = new JSONArray();
		ResultSetMetaData metadata = rsset.getMetaData();
		int numColumns = metadata.getColumnCount();

		while (rsset.next()) {
			JSONObject mJsonObj = new JSONObject();
			for (int i = 1; i <= numColumns; ++i) {
				try {
					String column_name = metadata.getColumnName(i);

					if (column_name.equals("ADIMAG") || column_name.equals("ALIMAG")) {
//					if (column_name.equals("ADIMAG")) {
//						System.out.println(column_name);
						mJsonObj.put(column_name, rsset.getString(column_name));

						if (rsset.getString(column_name).length() > 0) {
							
							String fileName = rsset.getString(column_name);
//							String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\" + fileName;
							String filePath = "D:\\files\\api_project\\adr_images\\" + fileName;
//							System.out.println("filePath: " + filePath);

							File file = new File(filePath);
							boolean fileExists = file.exists();
							if(fileExists) {
								// encode the file to string
								byte[] fileContent = FileUtils.readFileToByteArray(file);
								String encodedString = Base64.getEncoder().encodeToString(fileContent);
								mJsonObj.put("IMAGE", encodedString);
								
							} else {
								mJsonObj.put("IMAGE", "");
								
							}
								

						} else {
							mJsonObj.put("IMAGE", "");
							
						}
						
						
					} else {
						mJsonObj.put(column_name, rsset.getString(column_name));
						
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// System.out.println(mJsonObj);
			mJSonArr.put(mJsonObj);
		}
		return mJSonArr.toString();
	}
	
	public static String convertResultSetToJsonV3(@Context HttpServletRequest httpServletRequest, ResultSet rsset)
			throws SQLException, IOException {

		JSONArray mJSonArr = new JSONArray();
		ResultSetMetaData metadata = rsset.getMetaData();
		int numColumns = metadata.getColumnCount();

		while (rsset.next()) {
			JSONObject mJsonObj = new JSONObject();
			for (int i = 1; i <= numColumns; ++i) {
				try {
					String column_name = metadata.getColumnName(i);

//					if (column_name.equals("ADIMAG") || column_name.equals("ALIMAG")) {
					if (column_name.equals("ALIMAG")) {
						System.out.println(column_name);
						mJsonObj.put(column_name, rsset.getString(column_name));

						if (rsset.getString(column_name).length() > 0) {
							
							String fileName = rsset.getString(column_name);
//							String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\" + fileName;
							String filePath = "D:\\files\\api_project\\adr_images\\" + fileName;
//							System.out.println("filePath: " + filePath);

							File file = new File(filePath);
							boolean fileExists = file.exists();
							if(fileExists) {
								// encode the file to string
								byte[] fileContent = FileUtils.readFileToByteArray(file);
								String encodedString = Base64.getEncoder().encodeToString(fileContent);
								mJsonObj.put("IMAGE", encodedString);
								
							} else {
								mJsonObj.put("IMAGE", "");
								
							}
								

						} else {
							mJsonObj.put("IMAGE", "");
							
						}
						
						
					} else {
						mJsonObj.put(column_name, rsset.getString(column_name));
						
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			// System.out.println(mJsonObj);
			mJSonArr.put(mJsonObj);
		}
		return mJSonArr.toString();
	}

	public void fileToBase64StringConversion(String inputFilePath,
			String outputFilePath) throws IOException {
		// load file from /src/test/resources
		ClassLoader classLoader = getClass().getClassLoader();
		File inputFile = new File(
				classLoader.getResource(inputFilePath).getFile());

		byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
		String encodedString = Base64.getEncoder().encodeToString(fileContent);

		// create output file
		File outputFile = new File(inputFile.getParentFile().getAbsolutePath()
				+ File.pathSeparator + outputFilePath);

		// decode the string and write to file
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		FileUtils.writeByteArrayToFile(outputFile, decodedBytes);

		// assertTrue(FileUtils.contentEquals(inputFile, outputFile));
	}

}
