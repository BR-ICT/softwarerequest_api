package com.br.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.br.connection.ConnectDB2;
import com.br.data.SelectData;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class GenReportBM {

	public byte[] genReportPdf(@Context HttpServletRequest httpServletRequest, String jasperReportName, ByteArrayOutputStream outputStream, Map parameter)
			throws SQLException {
		JRPdfExporter exporter = new JRPdfExporter();
		Connection conn = null;
		try {
			conn = ConnectDB2.doConnect();
			String reportLocation = httpServletRequest.getRealPath("/") + "WEB-INF\\report\\" + jasperReportName + ".jrxml";
			System.out.println("reportLocation: " + reportLocation);
			InputStream jrxmlInput = new FileInputStream(new File(reportLocation));
			JasperDesign design = JRXmlLoader.load(jrxmlInput);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);

			// JasperCompileManager.compileReport(reportLocation);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			exporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in generate Report..." + e);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return outputStream.toByteArray();
	}

	public static File genReportFilePdf(@Context HttpServletRequest httpServletRequest, String jasperReportName, String cono, String divi, String adrno)
			throws SQLException {

		String reportLocation = httpServletRequest.getRealPath("/") + "WEB-INF\\report\\" + jasperReportName + ".jrxml";
		System.out.println("reportLocation: " + reportLocation);
		Connection conn = null;
		try {

			String filePath = "D:\\files\\api_project\\adr_images\\";
			conn = ConnectDB2.doConnect();
			Map params = new HashMap();
		/*	params.put("cono", cono);
			params.put("divi", divi);
			params.put("marno", adrno);
			
			*/ 
			params.put("com", "10");
			params.put("mono", "10");
			params.put("to", "20240925");
			params.put("from", "20240901");
			params.put("Type", "");
			params.put("Location", "11");
			params.put("parameterA", "TRANSFER");
			params.put("parameterB", "TRANSFER");
			params.put("FGLOACATION", "L/S");
			params.put("RV", "RVA_TRN");
//			params.put("imagesDir", filePath);
			
			JasperReport report = JasperCompileManager.compileReport(reportLocation);
			JasperPrint print = JasperFillManager.fillReport(report, params, conn);
			
			String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
			String companyName = getCompany[0].trim();
			String companyFull = getCompany[1].trim();
			
			File pdf = File.createTempFile(jasperReportName + "_" + adrno + "_" + companyName, ".pdf");
			JasperExportManager.exportReportToPdfStream(print, new FileOutputStream(pdf));
			
			System.out.println("Gen report successfully.");
			return pdf;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public byte[] genReportExcel(@Context HttpServletRequest httpServletRequest, String jasperReportName, ByteArrayOutputStream outputStream, Map parameter)
		throws SQLException {
	JRXlsxExporter exporterXls = new JRXlsxExporter();
	Connection conn = null;
	try {
		conn = ConnectDB2.doConnect();
		String reportLocation = httpServletRequest.getRealPath("/") + "WEB-INF\\report\\" + jasperReportName + ".jrxml";
		System.out.println("reportLocation: " + reportLocation);
		InputStream jrxmlInput = new FileInputStream(new File(reportLocation));
		JasperDesign design = JRXmlLoader.load(jrxmlInput);
		JasperReport jasperReport = JasperCompileManager.compileReport(design);

		// JasperCompileManager.compileReport(reportLocation);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);

		exporterXls.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporterXls.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);

		exporterXls.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporterXls.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
		exporterXls.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		exporterXls.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		
		exporterXls.exportReport();
		
		
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("Error in generate Report..." + e);
	} finally {
		if (conn != null) {
			conn.close();
		}
	}
	return outputStream.toByteArray();
}


}
