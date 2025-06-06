package com.br.utility;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.ws.rs.GET;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.br.auth.JwtManager;
import com.br.data.SelectData;
import com.br.data.InsertData;
import com.br.data.UpdateData;
import com.br.report.GenReport;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class SendEmail {

	private static final Logger logger = LogManager.getLogger(SendEmail.class);

	protected static String PROFILENAME = Constant.PROFILENAME;
	protected static String DBNAME = Constant.DBNAME;
	protected static String DBM3NAME = Constant.DBM3NAME;

	// protected static String WEBLINK = "http://192.200.9.106:5000/marsystems";
	//protected static String WEBLINK = "http://localhost:3001/showvisitor";
	protected static String WEBLINK = "http://210.1.14.22:5001/visitor/showvisitor";
	//protected static String WEBLINK = "http://192.200.9.94:5000/visitor/showvisitor";
	// http://localhost:3001/showvisitor/24000003
	protected static String WEBLINKPUBBLIC = "http://192.200.9.106:5000/marsystems";
	protected static String WEBLINKAPPROVE = "http://192.200.9.106:5000/smartapprove";
	protected static String WEBLINKAPPROVEPUBBLIC = "http://210.1.14.22:5000/smartapprove";

	public static String prepareResendEmail(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String prefix, String ordno, String fromstatus, String tostatus, String submit, String username,
			String token, String getLocation) throws Exception {
		logger.info("prepareReSendEmail");

		JSONObject mJsonObj = new JSONObject();
		try {
			logger.debug("Submit Visitor, Send mail to approve1, {} {} {}", prefix, fromstatus, tostatus);
			if (prefix.equals("EMP")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				SendEmail.prepareSendEmailVISITOR(httpServletRequest, documentName, reportName, cono, divi, ordno,
						tostatus, remark, username, token ,getLocation);
				UpdateData.updateEmail(ordno);

			}
			

			else if (prefix.equals("OP")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				// SendEmail.prepareSendEmailMAR(httpServletRequest, documentName, reportName,
				// cono, divi, marno, tostatus,
				// remark, username, token);

			}

			/*
			 * else {
			 * 
			 * String documentName = "MARA"; String reportName = "MARA_Report"; String
			 * remark = ""; SendEmail.prepareSendEmailMARA(httpServletRequest, documentName,
			 * reportName, cono, divi, marno, tostatus, remark, username, token);
			 * 
			 * 
			 * }
			 * 
			 */

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email complete, MAR " + ordno + ".");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error(e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		}

	}
	
	
	public static String prepareResendEmailSHOW(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String prefix, String ordno, String fromstatus, String tostatus, String submit, String username,
			String token, String getLocation,String vMeetdate,String vMeettime,String vName,String vSurname,String vROOMNO,String vRemark) throws Exception {
		logger.info("prepareReSendEmail");

		JSONObject mJsonObj = new JSONObject();
		try {
			System.out.print("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

			
			 System.out.println("mail no auth2");

			logger.debug("Submit Visitor, Send mail to approve1, {} {} {}", prefix, fromstatus, tostatus);
			if (prefix.equals("EMP")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				SendEmail.prepareSendEmailVISITOR(httpServletRequest, documentName, reportName, cono, divi, ordno,
						tostatus, remark, username, token ,getLocation);
				UpdateData.updateEmail(ordno);

			}
			else if (prefix.equals("SHOW")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				
				
				System.out.print("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
				SendEmail.prepareSendEmailSHOW(httpServletRequest, documentName, reportName, cono, divi, ordno,
						tostatus, remark, username, token ,getLocation,vMeetdate,vMeettime,vName,vSurname,vROOMNO,vRemark);
				UpdateData.updateEmailreject(ordno);

			}

			else if (prefix.equals("OP")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				// SendEmail.prepareSendEmailMAR(httpServletRequest, documentName, reportName,
				// cono, divi, marno, tostatus,
				// remark, username, token);

			}

			/*
			 * else {
			 * 
			 * String documentName = "MARA"; String reportName = "MARA_Report"; String
			 * remark = ""; SendEmail.prepareSendEmailMARA(httpServletRequest, documentName,
			 * reportName, cono, divi, marno, tostatus, remark, username, token);
			 * 
			 * 
			 * }
			 * 
			 */

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email complete, MAR " + ordno + ".");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error(e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		}

	}
	
	
	public static String prepareResendxx(@Context HttpServletRequest httpServletRequest,
			String prefix) throws Exception {
		logger.info("prepareReSendEmail");

		JSONObject mJsonObj = new JSONObject();
		try {
			System.out.print("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

			
			 System.out.println("mail no auth2");

		   if (prefix.equals("SHOW")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				
				
				System.out.print("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
				SendEmail.prepareSendxx(httpServletRequest, documentName);

			}

			else if (prefix.equals("OP")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				// SendEmail.prepareSendEmailMAR(httpServletRequest, documentName, reportName,
				// cono, divi, marno, tostatus,
				// remark, username, token);

			}

			/*
			 * else {
			 * 
			 * String documentName = "MARA"; String reportName = "MARA_Report"; String
			 * remark = ""; SendEmail.prepareSendEmailMARA(httpServletRequest, documentName,
			 * reportName, cono, divi, marno, tostatus, remark, username, token);
			 * 
			 * 
			 * }
			 * 
			 */

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email complete, MAR " +"xxxx" + ".");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error(e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		}

	}
	
	

	public static String prepareSendxx(@Context HttpServletRequest httpServletRequest, String documentname) throws Exception {
		logger.info("prepareSendEmail");

		String approvePage = "emailapprove/approvemar/approve";

		JSONObject mJsonObj = new JSONObject();
		try {
			String[] getDataEmailTemplate = SelectData.getEmailTemplate(documentname, "80").split(";");
			System.out.println("getDataEmailTemplate : " + getDataEmailTemplate);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

			String subject1 = getDataEmailTemplate[4].trim();
			String subject2 = getDataEmailTemplate[5].trim();
			String subject3 = getDataEmailTemplate[6].trim();
			String subject4 = getDataEmailTemplate[7].trim();
			String subject5 = getDataEmailTemplate[8].trim();
			String detail1 = getDataEmailTemplate[9].trim();
			String detail2 = getDataEmailTemplate[10].trim();
			String detail3 = getDataEmailTemplate[11].trim();
			String detail4 = getDataEmailTemplate[12].trim();
			String detail5 = getDataEmailTemplate[13].trim();

	//		List<String> getListRequester = null;
//			getListRequester = SelectData.getVisitorRequesterPP(cono, divi, ordno,getLocation);
			// logger.debug("getListRequester {}", getListRequester);

			// JSONObject mJsonObj = new JSONObject();
			
			
	

			//	String[] getDataRequester = getListRequester.get(i).split(":");
			//	String getNameRequester = getDataRequester[1].trim();
			//	String getFullNameRequester = getDataRequester[0].trim();

			

				// String getNameRequester = "PHONGS_PHO";
				// String getFullNameRequester = "PPPPP";
				

			//String getEmailRequester = "Phongsathon@br-bangkokranch.com";
			String getEmailRequester = "reception@br-bangkokranch.com";


				//getEmailRequester = "Phongsathon@br-bangkokranch.com";

				// String[] getCompany = SelectData.getCompanyWithConoDivi(cono,
				// divi).split(";");
				// String companyName = getCompany[0].trim();
				// String companyFull = getCompany[1].trim();
			
			// String cono, String divi, String ordno, String status, String remark, String username,
				//String token, String getLocation,String vMeetdate,String vMeettime,String vName,String vSurname,String vROOMNO,String vRemark

				String companyName = "REJECT";
				String companyFull = "REJECT";

				String linkPlanMAR = "<br/><br/><br/> REJECT ID　： ";
				String subjectEmail = subject1 + " " + "RECEPTION2" + " " + subject2 + "xxxx " 						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " xxxx "  + linkPlanMAR + "/xxx <br/> ";
			
				

				// String detailEmail = linkPlanMAR +"/"+ adrno.trim() + " ";
				//logger.debug("subjectEmail {} {} {} {} {} {} {} {}", subject1, getNameRequester, getFullNameRequester,
					//	getEmailRequester, subject2, ordno, subject3, companyName);
			//	logger.debug("detailEmail {} {} {} {}", detail1, ordno, detail2, linkPlanMAR, detail3);

				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222");

				sendEmail(documentname, "xxx", subjectEmail, detailEmail, "", getEmailRequester,"xxx","xx","xxx");

				// Save log
				// InsertData.addLogSendEmail(documentname, cono, divi, adrno,
				// getEmailRequester, "", "ItCenter",
				// subjectEmail, detailEmail, status, "ItCenter");

			

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email MDR " +"xss" + " successfully.");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error("Send email error.");
			throw new IllegalArgumentException("Send email error.");
		}

	}
	
	
	
	public static String prepareSendEmailSHOW(@Context HttpServletRequest httpServletRequest, String documentname,
			String reportname, String cono, String divi, String ordno, String status, String remark, String username,
			String token, String getLocation,String vMeetdate,String vMeettime,String vName,String vSurname,String vROOMNO,String vRemark) throws Exception {
		logger.info("prepareSendEmail");

		String approvePage = "emailapprove/approvemar/approve";

		JSONObject mJsonObj = new JSONObject();
		try {
			System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + status);
			String[] getDataEmailTemplate = SelectData.getEmailTemplate(documentname, status).split(";");
			System.out.println("getDataEmailTemplate : " + getDataEmailTemplate);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

			String subject1 = getDataEmailTemplate[4].trim();
			String subject2 = getDataEmailTemplate[5].trim();
			String subject3 = getDataEmailTemplate[6].trim();
			String subject4 = getDataEmailTemplate[7].trim();
			String subject5 = getDataEmailTemplate[8].trim();
			String detail1 = getDataEmailTemplate[9].trim();
			String detail2 = getDataEmailTemplate[10].trim();
			String detail3 = getDataEmailTemplate[11].trim();
			String detail4 = getDataEmailTemplate[12].trim();
			String detail5 = getDataEmailTemplate[13].trim();

	//		List<String> getListRequester = null;
//			getListRequester = SelectData.getVisitorRequesterPP(cono, divi, ordno,getLocation);
			// logger.debug("getListRequester {}", getListRequester);

			// JSONObject mJsonObj = new JSONObject();
			
			
	

			//	String[] getDataRequester = getListRequester.get(i).split(":");
			//	String getNameRequester = getDataRequester[1].trim();
			//	String getFullNameRequester = getDataRequester[0].trim();

			

				// String getNameRequester = "PHONGS_PHO";
				// String getFullNameRequester = "PPPPP";
				

			String getEmailRequester = "Phongsathon@br-bangkokranch.com";

				//getEmailRequester = "Phongsathon@br-bangkokranch.com";

				// String[] getCompany = SelectData.getCompanyWithConoDivi(cono,
				// divi).split(";");
				// String companyName = getCompany[0].trim();
				// String companyFull = getCompany[1].trim();
			
			// String cono, String divi, String ordno, String status, String remark, String username,
				//String token, String getLocation,String vMeetdate,String vMeettime,String vName,String vSurname,String vROOMNO,String vRemark

				String companyName = "REJECT";
				String companyFull = "REJECT";

				String linkPlanMAR = "<br/><br/><br/> REJECT ID　： ";
				String subjectEmail = subject1 + " " + "RECEPTION2" + " " + subject2 + " " + ordno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + ordno + " " + remark + " " + linkPlanMAR + "/" + ordno.trim() + " <br/> ";
						
						detailEmail += "Name : " + vName + "<br/>";
				detailEmail += "Surname : " + vSurname + "<br/>";
				detailEmail += "Meetdate : " + vMeetdate + "<br/>";
				detailEmail += "Meettime : " + vMeettime + "<br/>";
				detailEmail += "Remark : " + vRemark + "<br/>";
				

				// String detailEmail = linkPlanMAR +"/"+ adrno.trim() + " ";
				//logger.debug("subjectEmail {} {} {} {} {} {} {} {}", subject1, getNameRequester, getFullNameRequester,
					//	getEmailRequester, subject2, ordno, subject3, companyName);
			//	logger.debug("detailEmail {} {} {} {}", detail1, ordno, detail2, linkPlanMAR, detail3);

				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222");

				sendEmail(documentname, ordno, subjectEmail, detailEmail, "", getEmailRequester, status, cono, divi);

				// Save log
				// InsertData.addLogSendEmail(documentname, cono, divi, adrno,
				// getEmailRequester, "", "ItCenter",
				// subjectEmail, detailEmail, status, "ItCenter");

			

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email MDR " + ordno + " successfully.");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error("Send email error.");
			throw new IllegalArgumentException("Send email error.");
		}

	}
	
	

	public static String prepareSendEmailVISITOR(@Context HttpServletRequest httpServletRequest, String documentname,
			String reportname, String cono, String divi, String ordno, String status, String remark, String username,
			String token, String getLocation) throws Exception {
		logger.info("prepareSendEmail");

		String approvePage = "emailapprove/approvemar/approve";

		JSONObject mJsonObj = new JSONObject();
		try {
			System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + status);
			String[] getDataEmailTemplate = SelectData.getEmailTemplate(documentname, status).split(";");
			
			String[] getNameTemplate = SelectData.getNameTemplate(ordno, cono).split(";");
			System.out.println("getDataEmailTemplate : " + getDataEmailTemplate);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

			String subject1 = getDataEmailTemplate[4].trim();
			String subject2 = getDataEmailTemplate[5].trim();
			String subject3 = getDataEmailTemplate[6].trim();
			String subject4 = getDataEmailTemplate[7].trim();
			String subject5 = getDataEmailTemplate[8].trim();
			String detail1 = getDataEmailTemplate[9].trim();
			String detail2 = getDataEmailTemplate[10].trim();
			String detail3 = getDataEmailTemplate[11].trim();
			String detail4 = getDataEmailTemplate[12].trim();
			String detail5 = getDataEmailTemplate[13].trim();
			
			
			String name = getNameTemplate[0].trim();
			

			List<String> getListRequester = null;
			getListRequester = SelectData.getVisitorRequesterPP(cono, divi, ordno,getLocation);
			// logger.debug("getListRequester {}", getListRequester);

			// JSONObject mJsonObj = new JSONObject();
			

			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx--visitor");
			System.out.println(getListRequester.size());
			System.out.println(getListRequester);
			
			for (int i = 0; i < getListRequester.size(); i++) {

				String getDataRequester = getListRequester.get(i);

			//	String[] getDataRequester = getListRequester.get(i).split(":");
			//	String getNameRequester = getDataRequester[1].trim();
			//	String getFullNameRequester = getDataRequester[0].trim();

				 String getEmailRequester = getDataRequester;
				 
				 System.out.println(getEmailRequester);

				// String getNameRequester = "PHONGS_PHO";
				// String getFullNameRequester = "PPPPP";
				
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222ddddd"+ getEmailRequester);

				//String getEmailRequester = "Phongsathon@br-bangkokranch.com";

				//getEmailRequester = "Phongsathon@br-bangkokranch.com";

				// String[] getCompany = SelectData.getCompanyWithConoDivi(cono,
				// divi).split(";");
				// String companyName = getCompany[0].trim();
				// String companyFull = getCompany[1].trim();

				String companyName = "BR";
				String companyFull = "BANGKOKRANCH";

				String linkPlanMAR = "<br/><br/><br/> " + WEBLINK + "/" + getLocation;
				String subjectEmail = subject1 + " " + name.split(" ")[1] + " " + subject2 + " " + ordno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + ordno + " " + remark + " " + linkPlanMAR + "/" + ordno.trim() + " "
						+ detail2 + " " + detail3;

				// String detailEmail = linkPlanMAR +"/"+ adrno.trim() + " ";
				//logger.debug("subjectEmail {} {} {} {} {} {} {} {}", subject1, getNameRequester, getFullNameRequester,
					//	getEmailRequester, subject2, ordno, subject3, companyName);
			//	logger.debug("detailEmail {} {} {} {}", detail1, ordno, detail2, linkPlanMAR, detail3);

				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222");

				sendEmail(documentname, ordno, subjectEmail, detailEmail, "", getEmailRequester, status, cono, divi);

				// Save log
				// InsertData.addLogSendEmail(documentname, cono, divi, adrno,
				// getEmailRequester, "", "ItCenter",
				// subjectEmail, detailEmail, status, "ItCenter");

			}

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email MDR " + ordno + " successfully.");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error("Send email error.");
			throw new IllegalArgumentException("Send email error.");
		}

	}
	
	
	public static String prepareSendEmailFAP(@Context HttpServletRequest httpServletRequest, String documentname,
			String reportname, String cono, String divi, String ordno, String status, String remark, String username,
			String token, String getLocation,String foodcheck,String foodqty ,String atkcheck ,String atkqty ,String parkcheck ,String parkqty ,String etc,String beveragecheck ,String beveragenumber ,String snackcheck ,String snacknumber ,String sandalcheck,String sandalnumber,String meetdate,
			String  meettime, 
			String  meetdateout, 
			String  meettimeout,
			String  room,
			String  company,
			String  name, 
			String  surname) throws Exception {
		logger.info("prepareSendEmailFAP");

		String approvePage = "emailapprove/approvemar/approve";

		JSONObject mJsonObj = new JSONObject();
		try {
			System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr" + status);
			String[] getDataEmailTemplate = SelectData.getEmailTemplate(documentname, status).split(";");
			System.out.println("getDataEmailTemplate : " + getDataEmailTemplate);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

			String subject1 = getDataEmailTemplate[4].trim();
			String subject2 = getDataEmailTemplate[5].trim();
			String subject3 = getDataEmailTemplate[6].trim();
			String subject4 = getDataEmailTemplate[7].trim();
			String subject5 = getDataEmailTemplate[8].trim();
			String detail1 = getDataEmailTemplate[9].trim();
			String detail2 = getDataEmailTemplate[10].trim();
			String detail3 = getDataEmailTemplate[11].trim();
			String detail4 = getDataEmailTemplate[12].trim();
			String detail5 = getDataEmailTemplate[13].trim();

			List<String> getListRequester = null;
			//getListRequester = SelectData.getVisitorRequesterPP(cono, divi, ordno,getLocation);
			// logger.debug("getListRequester {}", getListRequester);

			// JSONObject mJsonObj = new JSONObject();
			

			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx--3333");
			//for (int i = 0; i < getListRequester.size(); i++) {

			//	String[] getDataRequester = getListRequester.get(i).split(":");
			//	String getNameRequester = getDataRequester[1].trim();
			//	String getFullNameRequester = getDataRequester[0].trim();

				// String getEmailRequester = getDataRequester[1].trim();

				// String getNameRequester = "PHONGS_PHO";
				// String getFullNameRequester = "PPPPP";
				
			//	System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222ddddd"+ getEmailRequester);
			

			String getEmailRequesterREP = "Phongsathon@br-bangkokranch.com";
	
			
		

				
				/*    
				 
พี่มล ศศิวิมล : Sasivimol@br-bangkokranch.com

พี่ตา พิกุล : phikun@br-bangkokranch.com

พี่หวาน ปัทมา : Pattama@br-bangkokranch.com

โบนัส Reception : reception@br-bangkokranch.com

คุณภูมิภิดล GA : Bhumibhidol@br-bangkokranch.com

GA Center : ga@br-bangkokranch.com

			   
			   	String getEmailRequesterREP = "reception@br-bangkokranch.com";
	
			
			String getEmailRequesterFood = " reception@br-bangkokranch.com";
			String getEmailRequesterFoodCC1 = "phikun@br-bangkokranch.com";
			String getEmailRequesterFoodCC2 = "Sasivimol@br-bangkokranch.com";

				
				String getEmailRequesterATK = " reception@br-bangkokranch.com";
				String getEmailRequesterATKCC1 = "Pattama@br-bangkokranch.com";


				String getEmailRequesterPark = " reception@br-bangkokranch.com";
				String getEmailRequesterParkCC1 = "Bhumibhidol@br-bangkokranch.com";
				String getEmailRequesterParkCC2 = "ga@br-bangkokranch.com";

				
				*/

				//getEmailRequester = "Phongsathon@br-bangkokranch.com";

				// String[] getCompany = SelectData.getCompanyWithConoDivi(cono,
				// divi).split(";");
				// String companyName = getCompany[0].trim();
				// String companyFull = getCompany[1].trim();
				
		
				

				String companyName = "BR";
				String companyFull = "BANGKOKRANCH";
				
						
				String heademail =  detail1 + " " + ordno + " " + remark  + "<br/><br/><br/> "
						+ "COMPANY : " + company + "   "  + "<br/> "
						+ "NAME : " + name + "  		SURNAME : " + surname  + "<br/> "
						+ "MEETDATE : " + meetdate + " "+meettime  +  "<br/> "
						+" TO : " + meetdateout + " "+meettimeout  + "<br/> "
						+ "ROOM : " + room + " "  + "<br/> "

						+ "---------------------------"  + "<br/> <br/> <br/>"; 
				
			   String emaildetail = " "; 

				//String FoodlinkPlanMAR = "<br/><br/><br/> " + WEBLINK + "/" + getLocation;
				String FoodsubjectEmail = subject1 + " " + "phikun & Sasivimol " + " " + subject2 + " " + ordno + " "
						+ subject3 + " " + companyName;
				
				String snackW = snackcheck.equalsIgnoreCase("true")?"ต้องการ":"NO";
				
				String FooddetailEmail = " "
						
						+ "FOOD : สำหรับ " + foodqty + "คน/  "  + "<br/> "
						+ "BEVERAGE : สำหรับ " + beveragenumber + "คน/  " + "<br/> "
					    + "SNACK : " + snackW + "/" + "<br/> "
					    + "SANDALS FOR ENTERING THE LINE  : สำหรับ " + sandalnumber + "คน/  " + "<br/> "
						+ "Etc. : " + etc  + "<br/> "
						+ "---------------------------"  + "<br/> <br/> <br/>"; 


				
				String ATKsubjectEmail = subject1 + " " + "Pattama" + " " + subject2 + " " + ordno + " "
						+ subject3 + " " + companyName;
				String ATKdetailEmail = " "


						+ "ATK : สำหรับ " + atkqty + "คน/  "  + "<br/> "
						+ "Etc. : " + etc  + "<br/> "
						+ "---------------------------"  + "<br/> <br/> <br/>"; 

			
				
				String ParksubjectEmail = subject1 + " " + "Bhumibhidol & GA" + " " + subject2 + " " + ordno + " "
						+ subject3 + " " + companyName;
				String ParkdetailEmail= " "
						+ "PARK : " + parkqty + "คัน/  "  + "<br/> "
						+ "Etc. : " + etc  + "<br/> "
						+ "---------------------------"  + "<br/> <br/> <br/>"; 


				String tailemail = " "+ detail2 + " " + detail3;

	
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222");
				System.out.println(foodcheck);
				System.out.println(beveragecheck);

				System.out.println(snackcheck);
				System.out.println(sandalcheck);

//String emailCC = "Phongsathon@br-bangkokranch.com"; 
String emailCC = " reception@br-bangkokranch.com";


/*
String getEmailRequesterFood = " reception@br-bangkokranch.com";
String getEmailRequesterFoodCC1 = "phikun@br-bangkokranch.com";
String getEmailRequesterFoodCC2 = "Sasivimol@br-bangkokranch.com";

	
	String getEmailRequesterATK = " reception@br-bangkokranch.com";
	String getEmailRequesterATKCC1 = "Pattama@br-bangkokranch.com";


	String getEmailRequesterPark = " reception@br-bangkokranch.com";
	String getEmailRequesterParkCC1 = "Bhumibhidol@br-bangkokranch.com";
	String getEmailRequesterParkCC2 = "ga@br-bangkokranch.com";
*/

String getEmailRequesterCC = " ";

String  namehead = "";

//String getEmailRequester = "Phongsathon@br-bangkokranch.com" ;

String getEmailRequester = "reception@br-bangkokranch.com" ;

String getEmailRequesterFoodCC1 = "phikun@br-bangkokranch.com,";
String getEmailRequesterFoodCC2 = "Sasivimol@br-bangkokranch.com,";
String getEmailRequesterATKCC1 = "Pattama@br-bangkokranch.com,";
String getEmailRequesterParkCC1 = "Bhumibhidol@br-bangkokranch.com,";
String getEmailRequesterParkCC2 = "ga@br-bangkokranch.com";




emaildetail +=  heademail;

				if(foodcheck.trim().equalsIgnoreCase("true")||
						beveragecheck.trim().equalsIgnoreCase("true")||
						snackcheck.trim().equalsIgnoreCase("true") ||
						sandalcheck.trim().equalsIgnoreCase("true"))
				{
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222ffffff");
					

emaildetail +=  FooddetailEmail;

namehead += " phikun & Sasivimol , ";

getEmailRequesterCC += getEmailRequesterFoodCC1+getEmailRequesterFoodCC2; 




				//sendEmail(documentname, ordno, FoodsubjectEmail, FooddetailEmail, "", getEmailRequesterFood, status, cono, divi);
				}

				
				if(atkcheck.equalsIgnoreCase("true"))
				{
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222aaaaaaa");
					
					emaildetail +=  ATKdetailEmail;
					namehead += " Pattama , ";
					getEmailRequesterCC +=getEmailRequesterATKCC1; 



				//sendEmail(documentname, ordno, ATKsubjectEmail, ATKdetailEmail, "", getEmailRequesterATK, status, cono, divi);
				}
				if(parkcheck.equalsIgnoreCase("true"))
				{
					System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222pppp");
					
					emaildetail +=  ParkdetailEmail;
					namehead += " Bhumibhidol & GA ";
					getEmailRequesterCC +=getEmailRequesterParkCC1+getEmailRequesterParkCC2; 

				//sendEmail(documentname, ordno, ParksubjectEmail, ParkdetailEmail, "", getEmailRequesterPark, status, cono, divi);
				}
				//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx222out");
				

				emaildetail+= tailemail;

				
				String subjectEmail = "(ทดสอบระบบ Welcome Visitor ระหว่างวันที่ 13/08/2024-23/08/2024)"+subject1 + " " +namehead + " " + subject2 + " " + ordno + " "
						+ subject3 + " " + companyName;; 
		
				
				sendEmail(documentname, ordno, subjectEmail, emaildetail, getEmailRequesterCC, getEmailRequester, status, cono, divi);
				
				
				// Save log
				// InsertData.addLogSendEmail(documentname, cono, divi, adrno,
				// getEmailRequester, "", "ItCenter",
				// subjectEmail, detailEmail, status, "ItCenter");

		//	}

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email MDR " + ordno + " successfully.");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error("Send email error.");
			throw new IllegalArgumentException("Send email error.");
		}

	}
	
	
	
	public static String prepareEmailFAP(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String prefix, String ordno, String fromstatus, String tostatus, String submit, String username,
			String token, String getLocation,String foodcheck,String foodqty ,String atkcheck ,String atkqty ,String parkcheck ,String parkqty ,String etc, String beveragecheck ,String beveragenumber ,String snackcheck ,String snacknumber ,String sandalcheck,String sandalnumber, String meetdate,String meettime,String meetdateout,String meettimeout,String room,String company,String name,String surname) throws Exception {
		logger.info("prepareReSendEmail");

		JSONObject mJsonObj = new JSONObject();
		try {
			logger.debug("Submit Visitor, Send mail to approve1, {} {} {}", prefix, fromstatus, tostatus);
			if (prefix.equals("EMP")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				SendEmail.prepareSendEmailFAP(httpServletRequest, documentName, reportName, cono, divi, ordno,
						tostatus, remark, username, token ,getLocation , foodcheck,
						  foodqty,
						  atkcheck,
						  atkqty,
						  parkcheck,
						  parkqty,
						  etc,
						  beveragecheck,
						  beveragenumber,
						  snackcheck,
						  snacknumber,
						  sandalcheck,
						  sandalnumber,
						  meetdate,
						  meettime, 
						  meetdateout, 
						  meettimeout,
						  room,
						  company,
						  name, 
						  surname);
				UpdateData.updateEmail(ordno);

			}

			else if (prefix.equals("OP")) {
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				// SendEmail.prepareSendEmailMAR(httpServletRequest, documentName, reportName,
				// cono, divi, marno, tostatus,
				// remark, username, token);

			}
			else if (prefix.equals("SPRQ")) {
				
				
				System.out.print("SPRQSPRQSPRQSPRQSPRQSPRQSPRQSPRQSPRQSPRQSPRQ");
				String documentName = "VISITOR";
				String reportName = "MARD_Report";
				String remark = "";
				SendEmail.prepareSendEmailFAP(httpServletRequest, documentName, reportName, cono, divi, ordno,
						tostatus, remark, username, token ,getLocation , foodcheck,foodqty ,atkcheck ,atkqty ,parkcheck ,parkqty ,etc,beveragecheck ,beveragenumber ,snackcheck ,snacknumber ,sandalcheck,sandalnumber,meetdate,
						  meettime, 
						  meetdateout, 
						  meettimeout,
						  room,
						  company,
						  name, 
						  surname);
				//UpdateData.updateEmail(ordno);

			}


			/*
			 * else {
			 * 
			 * String documentName = "MARA"; String reportName = "MARA_Report"; String
			 * remark = ""; SendEmail.prepareSendEmailMARA(httpServletRequest, documentName,
			 * reportName, cono, divi, marno, tostatus, remark, username, token);
			 * 
			 * 
			 * }
			 * 
			 */

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email complete, MAR " + ordno + ".");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error(e.getMessage());
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			return mJsonObj.toString();
		}

	}


	public static String prepareSendEmailOPERATOR(@Context HttpServletRequest httpServletRequest, String documentname,
			String reportname, String cono, String divi, String ordno, String status, String remark, String username,
			String token) throws Exception {
		logger.info("prepareSendEmail");

		String approvePage = "emailapprove/approvemar/approve";

		JSONObject mJsonObj = new JSONObject();
		try {
			System.out.print("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
			String[] getDataEmailTemplate = SelectData.getEmailTemplate(documentname, status).split(";");
			System.out.println("getDataEmailTemplate : " + getDataEmailTemplate);
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

			String subject1 = getDataEmailTemplate[4].trim();
			String subject2 = getDataEmailTemplate[5].trim();
			String subject3 = getDataEmailTemplate[6].trim();
			String subject4 = getDataEmailTemplate[7].trim();
			String subject5 = getDataEmailTemplate[8].trim();
			String detail1 = getDataEmailTemplate[9].trim();
			String detail2 = getDataEmailTemplate[10].trim();
			String detail3 = getDataEmailTemplate[11].trim();
			String detail4 = getDataEmailTemplate[12].trim();
			String detail5 = getDataEmailTemplate[13].trim();

			List<String> getListRequester = null;
			// getListRequester = SelectData.getVisitorRequester(cono, divi, ordno);
			// logger.debug("getListRequester {}", getListRequester);

			// JSONObject mJsonObj = new JSONObject();
			for (int i = 0; i < getListRequester.size(); i++) {

				String[] getDataRequester = getListRequester.get(i).split(":");
		//		String getNameRequester = getDataRequester[1].trim();
		//		String getFullNameRequester = getDataRequester[0].trim();

				 String getEmailRequester = getDataRequester[2].trim();

				// String getNameRequester = "PHONGS_PHO";
				// String getFullNameRequester = "PPPPP";
				//String getEmailRequester = "Phongsathon@br-bangkokranch.com";

			//getEmailRequester = "Phongsathon@br-bangkokranch.com";

				// String[] getCompany = SelectData.getCompanyWithConoDivi(cono,
				// divi).split(";");
				// String companyName = getCompany[0].trim();
				// String companyFull = getCompany[1].trim();

				String companyName = "BR";
				String companyFull = "BANGKOKRANCH";

				String linkPlanMAR = "<br/><br/><br/> " + WEBLINK;
				String subjectEmail = subject1 + " " + "PP" + " " + subject2 + " " + ordno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + ordno + " " + remark + " " + linkPlanMAR + "/" + ordno.trim() + " "
						+ detail2 + " " + detail3;

				// String detailEmail = linkPlanMAR +"/"+ adrno.trim() + " ";
				logger.debug("subjectEmail {} {} {} {} {} {} {} {}", subject1, "PP", "PP",
						getEmailRequester, subject2, ordno, subject3, companyName);
				logger.debug("detailEmail {} {} {} {}", detail1, ordno, detail2, linkPlanMAR, detail3);

				sendEmail(documentname, ordno, subjectEmail, detailEmail, "", getEmailRequester, status, cono, divi);

				// Save log
				// InsertData.addLogSendEmail(documentname, cono, divi, adrno,
				// getEmailRequester, "", "ItCenter",
				// subjectEmail, detailEmail, status, "ItCenter");

			}

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send email MDR " + ordno + " successfully.");
			return mJsonObj.toString();

		} catch (Exception e) {
			logger.error("Send email error.");
			throw new IllegalArgumentException("Send email error.");
		}

	}

	public static String prepareSendEmail(@Context HttpServletRequest httpServletRequest, String cono, String divi,
			String adrno, String status, String document, String remark) throws Exception {

		String documentName = "";
		String reportName = "";
		String approvePage = "";
		String webLink = "";
		String webLinkApprove = "";
		String webLinkPubblic = "";
		String webLinkPubblicApprove = "";

		if (document.equals("ADR")) {
			documentName = "ADR";
			reportName = "ADR_Report";
			approvePage = "emailapprove/approveadr/approve";
			webLink = "http://192.200.9.106:5000/adrsystems";
			webLinkPubblic = "http://210.1.14.22:5000/adrsystems";
		}

//	if (lib.equals("PRD")) {
		webLinkApprove = "http://192.200.9.106:5000/smartapprove";
		webLinkPubblicApprove = "http://210.1.14.22:5000/smartapprove";
//	} else {
//	    webLink = "http://localhost:3000";
//	}

		String[] getDataEmailTemplate = SelectData.getEmailTemplate(documentName, status).toString().split(";");
//		System.out.println("getDataEmailTemplate: " + getDataEmailTemplate);

		String subject1 = getDataEmailTemplate[4].trim();
		String subject2 = getDataEmailTemplate[5].trim();
		String subject3 = getDataEmailTemplate[6].trim();
		String subject4 = getDataEmailTemplate[7].trim();
		String subject5 = getDataEmailTemplate[8].trim();
		String detail1 = getDataEmailTemplate[9].trim();
		String detail2 = getDataEmailTemplate[10].trim();
		String detail3 = getDataEmailTemplate[11].trim();
		String detail4 = getDataEmailTemplate[12].trim();
		String detail5 = getDataEmailTemplate[13].trim();

		if (status.equals("00") || status.equals("05")) {
			// sendEmailReject
			List<String> getListRequester = null;

			if (documentName == "ADR") {
				getListRequester = SelectData.getADRRequester(cono, divi, adrno);

			}

			JSONObject mJsonObj = new JSONObject();

			for (int i = 0; i < getListRequester.size(); i++) {
				String[] getDataRequesterName = getListRequester.get(i).split(";");
				String getNameRequester = getDataRequesterName[0].trim();
				String getFullNameRequester = getDataRequesterName[1].trim();
				String getEmailRequester = getDataRequesterName[2].trim();
//		getEmailRequester = "wattana@br-bangkokranch.com";

				String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
				String companyName = getCompany[0].trim();
				String companyFull = getCompany[1].trim();

				String linkApprove = " <br/> " + webLink + "/" + adrno + "/";

				String subjectEmail = subject1 + " " + getFullNameRequester + " " + subject2 + " " + adrno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + adrno + " " + remark + " " + detail2 + " " + linkApprove + " "
						+ detail3;
				System.out.println("subjectEmail: " + subject1 + " " + getNameRequester + " " + getFullNameRequester
						+ " " + getEmailRequester + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName
						+ "\n" + "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " "
						+ detail3);

				sendEmail(documentName, adrno, subjectEmail, detailEmail, "", getEmailRequester, status, cono, divi);

				// Save log
				InsertData.addLogSendEmail(documentName, adrno, getEmailRequester, "", "ItCenter", subjectEmail,
						detailEmail, status, "ItCenter", cono, divi);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Send attach file email ADR " + adrno + " successfully.");
				return mJsonObj.toString();

			}

		} else if (status.equals("10")) {
			// sendEmailAccountant
			List<String> getListAccountant = null;

			if (documentName == "ADR") {
				getListAccountant = SelectData.getADRAccountant(cono, divi, adrno);

			}

			JSONObject mJsonObj = new JSONObject();

			for (int i = 0; i < getListAccountant.size(); i++) {
				System.out.println("getDataAccountant: " + getListAccountant);
				String[] getDataAccountant = getListAccountant.get(i).split(";");
				String getNameAccountant = getDataAccountant[0].trim();
				String getFullNameAccountant = getDataAccountant[1].trim();
				String getEmailAccountant = getDataAccountant[2].trim();
//		getEmailAccountant = "wattana@br-bangkokranch.com";

				String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
				String companyName = getCompany[0].trim();
				String companyFull = getCompany[1].trim();

				String linkApprove = " <br/> " + webLink + "/confirm_adr/";

				String subjectEmail = subject1 + " " + getFullNameAccountant + " " + subject2 + " " + adrno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " " + detail3;
				System.out.println("subjectEmail: " + subject1 + " " + getNameAccountant + " " + getFullNameAccountant
						+ " " + getEmailAccountant + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName
						+ "\n" + "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " "
						+ detail3);

				sendEmail(documentName, adrno, subjectEmail, detailEmail, "", getEmailAccountant, status, cono, divi);

				// Save log
				InsertData.addLogSendEmail(documentName, adrno, getEmailAccountant, "", "ItCenter", subjectEmail,
						detailEmail, status, "ItCenter", cono, divi);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Send email ADR " + adrno + " successfully.");
			}

			return mJsonObj.toString();

		} else if (status.equals("15")) {
			// sendEmailDeptHead
			List<String> getListDeptHead = null;

			if (documentName == "ADR") {
				getListDeptHead = SelectData.getADRApprove(cono, divi, adrno, status);

			}

			JSONObject mJsonObj = new JSONObject();

			for (int i = 0; i < getListDeptHead.size(); i++) {
				System.out.println("getDataDeptHead: " + getListDeptHead);
				String[] getDataDeptHead = getListDeptHead.get(i).split(";");
				String getNameDeptHead = getDataDeptHead[0].trim();
				String getFullNameDeptHead = getDataDeptHead[1].trim();
				String getEmailDeptHead = getDataDeptHead[2].trim();
//		getEmailDeptHead = "wattana@br-bangkokranch.com";

				String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
				String companyName = getCompany[0].trim();
				String companyFull = getCompany[1].trim();

				String userAuth = SelectData.getUserAuth(cono, divi, getNameDeptHead);
				String getToken = JwtManager.createTokenApprove(companyFull, getNameDeptHead, userAuth);

//		String linkApprove = "<br/> Local link --> " + webLinkApprove + "/" + approvePage + "/" + cono + "/" + divi + "/" + adrno + "/" + getNameDeptHead + "/" + getToken;		
				String linkApprove = "<br/> Local link (Connect OpenVPN only) --> " + webLinkApprove + "/" + approvePage
						+ "/" + cono + "/" + divi + "/" + adrno + "/" + getNameDeptHead + "/" + getToken
						+ "<br/><br/> Pubblic link (Not connect OpenVPN) --> " + webLinkPubblicApprove + "/"
						+ approvePage + "/" + cono + "/" + divi + "/" + adrno + "/" + getNameDeptHead + "/" + getToken;

				String subjectEmail = subject1 + " " + getFullNameDeptHead + " " + subject2 + " " + adrno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " " + detail3;
				System.out.println("subjectEmail: " + subject1 + " " + getNameDeptHead + " " + getFullNameDeptHead + " "
						+ getEmailDeptHead + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName + "\n"
						+ "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " " + detail3);

				sendEmail(documentName, adrno, subjectEmail, detailEmail, "", getEmailDeptHead, status, cono, divi);

				// Save log
				InsertData.addLogSendEmail(documentName, adrno, getEmailDeptHead, "", "ItCenter", subjectEmail,
						detailEmail, status, "ItCenter", cono, divi);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Send email ADR " + adrno + " successfully.");

			}

			return mJsonObj.toString();

		} else if (status.equals("20") || status.equals("25")) {
			// sendEmailApprove
			List<String> getListApprove = null;

			if (documentName == "ADR") {
				getListApprove = SelectData.getADRApprove(cono, divi, adrno, status);
				System.out.println("getListApprove: " + getListApprove);

			}

			JSONObject mJsonObj = new JSONObject();

			for (int i = 0; i < getListApprove.size(); i++) {
				System.out.println("getDataApprove: " + getListApprove);
				String[] getDataApprove = getListApprove.get(i).split(";");
				String getNameApprove = getDataApprove[0].trim();
				String getFullNameApprove = getDataApprove[1].trim();
				String getEmailApprove = getDataApprove[2].trim();
//		getEmailApprove = "wattana@br-bangkokranch.com";

				String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
				String companyName = getCompany[0].trim();
				String companyFull = getCompany[1].trim();

				String userAuth = SelectData.getUserAuth(cono, divi, getNameApprove);
				String getToken = JwtManager.createTokenApprove(companyFull, getNameApprove, userAuth);

//		String linkApprove = "<br/> Local link --> " + webLinkApprove + "/" + approvePage + "/" + cono + "/" + divi + "/" + adrno + "/" + getNameDeptHead + "/" + getToken;		
				String linkApprove = "<br/> Local link (Connect OpenVPN only) --> " + webLinkApprove + "/" + approvePage
						+ "/" + cono + "/" + divi + "/" + adrno + "/" + getNameApprove + "/" + getToken
						+ "<br/><br/> Pubblic link (Not connect OpenVPN) --> " + webLinkPubblicApprove + "/"
						+ approvePage + "/" + cono + "/" + divi + "/" + adrno + "/" + getNameApprove + "/" + getToken;

				String subjectEmail = subject1 + " " + getFullNameApprove + " " + subject2 + " " + adrno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " " + detail3;
				System.out.println("subjectEmail: " + subject1 + " " + getNameApprove + " " + getFullNameApprove + " "
						+ getEmailApprove + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName + "\n"
						+ "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " " + detail3);

				sendEmail(documentName, adrno, subjectEmail, detailEmail, "", getEmailApprove, status, cono, divi);

				// Save log
				InsertData.addLogSendEmail(documentName, adrno, getEmailApprove, "", "ItCenter", subjectEmail,
						detailEmail, status, "ItCenter", cono, divi);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Send email ADR " + adrno + " successfully.");

			}

			return mJsonObj.toString();

		} else if (status.equals("30")) {
			// sendEmailReject
			List<String> getListRequester = null;

			if (documentName == "ADR") {
				getListRequester = SelectData.getADRRequester(cono, divi, adrno);

			}

			JSONObject mJsonObj = new JSONObject();

			for (int i = 0; i < getListRequester.size(); i++) {
				String[] getDataRequesterName = getListRequester.get(i).split(";");
				String getNameRequester = getDataRequesterName[0].trim();
				String getFullNameRequester = getDataRequesterName[1].trim();
				String getEmailRequester = getDataRequesterName[2].trim();
//		getEmailRequester = "wattana@br-bangkokranch.com";

				String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
				String companyName = getCompany[0].trim();
				String companyFull = getCompany[1].trim();

				String linkApprove = " <br/> " + webLink + "/plan_adr/";

				// String subjectEmail = subject1 + " " + getFullNameRequester + " " + subject2
				// + " " + adrno + " " + subject3 + " " + companyName;
				// String detailEmail = detail1 + " " + adrno + " " + detail2 + " " +
				// linkApprove + " " + detail3;
				String subjectEmail = "hello subject";
				String detailEmail = "hi detail";

				// System.out.println("subjectEmail: " + subject1 + " " + getNameRequester + " "
				// + getFullNameRequester + " " + getEmailRequester + " " + subject2 + " " +
				// adrno + " " + subject3 + " " + companyName
				// + "\n" + "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " +
				// linkApprove + " " + detail3);

				sendEmail(documentName, adrno, subjectEmail, detailEmail, "", getEmailRequester, status, cono, divi);

				// Save log
				// InsertData.addLogSendEmail(documentName, adrno, getEmailRequester, "",
				// "ItCenter", subjectEmail, detailEmail, status, "ItCenter", cono, divi);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Send attach file email ADR " + adrno + " successfully.");
				return mJsonObj.toString();

			}

		} else if (status.equals("35")) {
			// sendEmailAccountant
			List<String> getListAccountant = null;

			if (documentName == "ADR") {
				getListAccountant = SelectData.getADRAccountant(cono, divi, adrno);

			}

			JSONObject mJsonObj = new JSONObject();

			for (int i = 0; i < getListAccountant.size(); i++) {
				System.out.println("getDataAccountant: " + getListAccountant);
				String[] getDataAccountant = getListAccountant.get(i).split(";");
				String getNameAccountant = getDataAccountant[0].trim();
				String getFullNameAccountant = getDataAccountant[1].trim();
				String getEmailAccountant = getDataAccountant[2].trim();
//		getEmailAccountant = "wattana@br-bangkokranch.com";

				String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
				String companyName = getCompany[0].trim();
				String companyFull = getCompany[1].trim();

				String linkApprove = " <br/> " + webLink + "/confirm_adr/";

				String subjectEmail = subject1 + " " + getFullNameAccountant + " " + subject2 + " " + adrno + " "
						+ subject3 + " " + companyName;
				String detailEmail = detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " " + detail3;
				System.out.println("subjectEmail: " + subject1 + " " + getNameAccountant + " " + getFullNameAccountant
						+ " " + getEmailAccountant + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName
						+ "\n" + "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " "
						+ detail3);

				sendEmail(documentName, adrno, subjectEmail, detailEmail, "", getEmailAccountant, status, cono, divi);

				// Save log
				InsertData.addLogSendEmail(documentName, adrno, getEmailAccountant, "", "ItCenter", subjectEmail,
						detailEmail, status, "ItCenter", cono, divi);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Send email ADR " + adrno + " successfully.");
			}

			return mJsonObj.toString();

		} else if (status.equals("40")) {
			// sendEmailRequester, Accountant
			List<String> getListRequester = null;
			List<String> getListAccountant = null;

			String getNameRequester = "";
			String getFullNameRequester = "";
			String getEmailRequester = "";
			String getNameAccountant = "";
			String getFullNameAccountant = "";
			String getEmailAccountant = "";
			String emailSentTo = "";

			JSONObject mJsonObj = new JSONObject();

			if (documentName == "ADR") {
				getListRequester = SelectData.getADRRequester(cono, divi, adrno);
				getListAccountant = SelectData.getADRAccountant(cono, divi, adrno);

			}

			for (int i = 0; i < getListRequester.size(); i++) {
				String[] getDataRequesterName = getListRequester.get(i).split(";");
				getNameRequester = getDataRequesterName[0].trim();
				getFullNameRequester = getDataRequesterName[1].trim();
				getEmailRequester = getDataRequesterName[2].trim();

			}

			for (int ii = 0; ii < getListAccountant.size(); ii++) {
				String[] getDataAccountant = getListAccountant.get(ii).split(";");
				getNameAccountant = getDataAccountant[0].trim();
				getFullNameAccountant = getDataAccountant[1].trim();
				getEmailAccountant = getDataAccountant[2].trim();

			}

//	    getEmailRequester = "wattana@br-bangkokranch.com";
//	    getEmailAccountant = "saeung2532@gmail.com";

			emailSentTo = getEmailRequester + ", " + getEmailAccountant;

			System.out.print("emailSentTo: " + emailSentTo);

			String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
			String companyName = getCompany[0].trim();
			String companyFull = getCompany[1].trim();

			String linkApprove = " <br/> " + webLink + "/confirm_adr/";

			String subjectEmail = "" + " " + "" + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName;
			String detailEmail = detail1 + " " + adrno + " " + detail2 + " " + "" + " " + detail3;
			System.out.println("subjectEmail: " + subject1 + " " + getNameAccountant + " " + getFullNameAccountant + " "
					+ getEmailAccountant + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName + "\n"
					+ "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " + linkApprove + " " + detail3);

			// Create PDF file
			File pdfADRReport = GenReport.genReportFilePdf(httpServletRequest, reportName, cono, divi, adrno);

			// Send Attach file
			sendEmailAttachFile(documentName, adrno, subjectEmail, detailEmail, "", emailSentTo, status, cono, divi,
					pdfADRReport, companyName);

			// Save log
			InsertData.addLogSendEmail(documentName, adrno, emailSentTo, "", "ItCenter", subjectEmail, detailEmail,
					status, "ItCenter", cono, divi);

			mJsonObj.put("result", "ok");
			mJsonObj.put("message", "Send attach file email ADR " + adrno + " successfully.");
			return mJsonObj.toString();

		} else if (status.equals("92")) {
			// sendAttachFileEmail Status 40
			JSONObject mJsonObj = new JSONObject();
			String getCompany[] = SelectData.getCompanyWithConoDivi(cono, divi).split(";");
			String companyName = getCompany[0].trim();
			String companyFull = getCompany[1].trim();

			String emailRequestor = "";
			String emailPurchase = "";
			String getNameRequest = "";
			String getEmailRequest = "";

			String[] getDataEmailRequestor = null;
			List<String> getListEmailPuchase = null;

			if (documentName == "ADR") {

				getDataEmailRequestor = SelectData.getADREmailRequest(cono, divi, adrno).toString().split(";");
				getNameRequest = getDataEmailRequestor[0];
				getEmailRequest = getDataEmailRequestor[1];

				getListEmailPuchase = SelectData.getADREmailPuchase(cono, divi, adrno);
				for (int i = 0; i < getListEmailPuchase.size(); i++) {
					String[] getDataEmailPuchase = getListEmailPuchase.get(i).split(";");
					String getNamePuchase = getDataEmailPuchase[0];
					String getEmailPuchase = getDataEmailPuchase[1];

					if (i > 0) {
						emailPurchase += "," + getEmailPuchase;
					} else {
						emailPurchase += getEmailPuchase;
					}

				}

				emailRequestor = getEmailRequest;

				System.out.println("emailRequestor: " + emailRequestor);
				System.out.println("emailPurchase: " + emailPurchase);
				emailRequestor = "wattana@br-bangkokranch.com";
				emailPurchase = "wattana@br-bangkokranch.com";

				String emailSentToPHAndUser = emailPurchase + "," + emailRequestor;
				String emailSentToUser = emailRequestor;
				String emailSentToPH = emailPurchase;
				String subjectEmail = subject1 + " " + subject2 + " " + adrno + " " + subject3 + " " + companyName;
				String detailEmail = "";
				System.out.println("subjectEmail: " + subject1 + " " + subject2 + " " + adrno + " " + subject3 + " "
						+ companyName + "\n" + "detailEmail: " + detail1 + " " + adrno + " " + detail2 + " " + detail3);

				System.out.println("getPRNumber: " + adrno);

//			UpdateData.updateStsEPRHead(httpServletRequest, cono, divi, adrno, status);
				File pdfEPRReport = GenReport.genReportFilePdf(httpServletRequest, reportName, cono, divi, adrno);

				detailEmail = detail1 + " " + adrno + " \n " + " <br/><br/> " + adrno + detail2 + detail3;

				// Send AttachFile EPR to User
				sendEmailAttachFile(documentName, adrno, subjectEmail, detailEmail, "", emailSentToUser, status, cono,
						divi, pdfEPRReport, companyName);

				String linkApprove = " <br/> " + webLink + "/grouping_pr/";

				detailEmail = detail1 + " " + adrno + " \n " + " <br/><br/> " + adrno + linkApprove + detail2 + detail3;
				System.out.println("detailEmail: " + detailEmail);

				// Send AttachFile EPR to PH
				sendEmailAttachFile(documentName, adrno, subjectEmail, detailEmail, "", emailSentToPH, status, cono,
						divi, pdfEPRReport, companyName);

				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "Send attach file email EPR " + adrno + " successfully.");
				return mJsonObj.toString();

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Send attach file email " + adrno + " failed.");
				return mJsonObj.toString();

			}

		}
		return null;

	}
	
	
	public static void getcalendar(String documentName, String refNo, String subject, String detail, String emailCC,
			String emailSentTo, String status, String cono, String divi) throws Exception {

		
		final String auth_host = "mail.br-bangkokranch.com";
		final String auth_email = "itcenter@br-bangkokranch.com";
		final String auth_password = "10052508";
		
		
		final String domain_url = "https://192.200.9.174/service/home/phongsathon@br-bangkokranch.com/calendar.json";
		final String cal_email = "phongsathon@br-bangkokranch.com";
		final String cal_password = "-";
		
		
	

		
		//final String emailBCC = "phongsathon@br-bangkokranch.com";


		Properties props = new Properties();
		props.put("mail.smtp.host", auth_host);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		System.out.println(documentName);
		System.out.println(refNo);
		System.out.println(subject);
		System.out.println(detail);
		System.out.println(emailCC);
		System.out.println(emailSentTo);
		System.out.println(status);
		System.out.println(cono);
		System.out.println(divi);

		try {

			Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(auth_email, auth_password);
				}
			});

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(auth_email));

			/**
			 * * Recipient **
			 */
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailSentTo)); // To
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailCC)); // To
			//message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailBCC)); // To

			message.setSubject(subject);
			message.setText(detail);
			message.setContent(detail, "text/html; charset=utf-8");

			Transport.send(message);

			System.out.println("Send email successfully.");

		} catch (MessagingException e) {
			e.printStackTrace();
			InsertData.addLogSendEmail(documentName, refNo, emailSentTo, emailCC, "ItCenter", subject,
					"Send Email Error: " + e.toString(), status, "ItCenter", cono, divi);
		}

	}
	
	

	public static void sendEmail(String documentName, String refNo, String subject, String detail, String emailCC,
			String emailSentTo, String status, String cono, String divi) throws Exception {

		final String auth_host = "mail.br-bangkokranch.com";
		final String auth_email = "itcenter@br-bangkokranch.com";
		final String auth_password = "10052508";
		
		//final String emailBCC = "phongsathon@br-bangkokranch.com";


		Properties props = new Properties();
		props.put("mail.smtp.host", auth_host);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		System.out.println(documentName);
		System.out.println(refNo);
		System.out.println(subject);
		System.out.println(detail);
		System.out.println(emailCC);
		System.out.println(emailSentTo);
		System.out.println(status);
		System.out.println(cono);
		System.out.println(divi);

		try {

			Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(auth_email, auth_password);
				}
			});

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(auth_email));

			/**
			 * * Recipient **
			 */
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailSentTo)); // To
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(emailCC)); // To
			//message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailBCC)); // To

			message.setSubject(subject);
			message.setText(detail);
			message.setContent(detail, "text/html; charset=utf-8");

			Transport.send(message);

			System.out.println("Send email successfully.");

		} catch (MessagingException e) {
			e.printStackTrace();
			InsertData.addLogSendEmail(documentName, refNo, emailSentTo, emailCC, "ItCenter", subject,
					"Send Email Error: " + e.toString(), status, "ItCenter", cono, divi);
		}

	}

	public static void sendEmailAttachFile(String documentName, String refNo, String subject, String detail,
			String emailCC, String emailSentTo, String status, String cono, String divi, File pdfFile, String company)
			throws Exception {

		JSONObject mJsonObj = new JSONObject();
		final String auth_host = "mail.br-bangkokranch.com";
		final String auth_email = "itcenter@br-bangkokranch.com";
		final String auth_password = "10052508";
//	final String auth_email = "wattana@br-bangkokranch.com";
//	final String auth_password = "Aha@1989";

		Properties props = new Properties();
		props.put("mail.smtp.host", auth_host);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		try {

			Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(auth_email, auth_password);
				}
			});

			Message message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(auth_email)); // From
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailSentTo)); // To
			message.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(detail);
			messageBodyPart.setContent(detail, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(pdfFile);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("" + documentName + "_" + refNo + "_" + company + ".pdf");
//	    System.out.println("" + documentName + "_" + refNo + "_" + company + ".pdf");

			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);

			Transport.send(message);

			System.out.println("Send attach file email successfully.");

		} catch (MessagingException e) {
			e.printStackTrace();
			InsertData.addLogSendEmail(documentName, refNo, emailSentTo, emailCC, "ItCenter", subject,
					"Send Email Error: " + e.toString(), status, "ItCenter", cono, divi);
		}

	}

	public void attachFile(File file, Multipart multipart, MimeBodyPart messageBodyPart) throws MessagingException {
		DataSource source = new FileDataSource(file);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(file.getName());
		multipart.addBodyPart(messageBodyPart);
	}

	private static void addAttachment(Multipart multipart, File filename) throws MessagingException {
		DataSource source = new FileDataSource(filename);
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(filename.toString() + ".pdf");
		multipart.addBodyPart(messageBodyPart);
	}

}
