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
