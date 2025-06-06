package com.br.api;

import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.br.auth.JwtManager;
import com.br.data.SelectData;
import com.br.report.GenReport;
import com.br.utility.ConvertStringtoObject;
import com.br.utility.NumberToText;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Path("/report")
public class api_report {

    @GET
    @Path("/viewmpr/{prno}")
    public Response viewMPRReport(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("prno") String prno,
	    @PathParam("reportName") String reportName) throws SQLException, JSONException {

	JSONObject mJsonObj = new JSONObject();
	String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	System.out.println("getToken: " + getToken);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "MPR_Report";

	Jws<Claims> validate = null;

	if (getToken != null) {

	    // jwt verify token
	    validate = JwtManager.parseToken(getToken);
	    String company = validate.getBody().get("role", String.class);
	    String getCompany[] = company.split(" : ");
	    String getCono = getCompany[0];
	    String getDivi = getCompany[1];

	    Map params = new HashMap();
	    params.put("cono", getCono);
	    params.put("divi", getDivi);
	    params.put("prno", prno);

	    GenReport grp = new GenReport();
	    byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	    String fileName = reportName + ".pdf";
	    return Response.ok(bytes).type("application/pdf")
		    .header("Content-Disposition", "filename=\"" + fileName + "\"").build();

	} else {

	    mJsonObj.put("auth", "false");
	    mJsonObj.put("message", "No token provided");
	}

	return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

    @GET
    @Path("/viewmpr/{cono}/{divi}/{prno}")
    public Response viewMPRReportWithOutToken(@Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("prno") String prno,
	    @PathParam("reportName") String reportName) throws SQLException {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "MPR_Report";

	Map params = new HashMap();
	params.put("cono", cono);
	params.put("divi", divi);
	params.put("prno", prno);

	GenReport grp = new GenReport();
	byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	String fileName = reportName + ".pdf";
	return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + fileName + "\"")
		.build();
    }

    @GET
    @Path("/viewepr/{prno}")
    public Response viewEPRReport(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("prno") String prno,
	    @PathParam("reportName") String reportName) throws SQLException, JSONException {

	JSONObject mJsonObj = new JSONObject();
	String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	System.out.println("getToken: " + getToken);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "EPR_Report";

	Jws<Claims> validate = null;

	if (getToken != null) {

	    // jwt verify token
	    validate = JwtManager.parseToken(getToken);
	    String company = validate.getBody().get("role", String.class);
	    String getCompany[] = company.split(" : ");
	    String getCono = getCompany[0];
	    String getDivi = getCompany[1];

	    Map params = new HashMap();
	    params.put("cono", getCono);
	    params.put("divi", getDivi);
	    params.put("prno", prno);

	    GenReport grp = new GenReport();
	    byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	    String fileName = reportName + ".pdf";
	    return Response.ok(bytes).type("application/pdf")
		    .header("Content-Disposition", "filename=\"" + fileName + "\"").build();

	} else {

	    mJsonObj.put("auth", "false");
	    mJsonObj.put("message", "No token provided");
	}

	return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

    @GET
	@Path("/getreport/{reportName}/{fromdate}/{todate}/{type}/{location}")
    public Response getreport( @Context HttpServletRequest httpServletRequest,@PathParam("reportName") String reportName, 
    		@PathParam("fromdate") String fromdate,
    		@PathParam("todate") String todate,
                                   @PathParam("type") String type,
                                   @PathParam("location") String location

    		) throws SQLException {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "BM_NEW";

	Map params = new HashMap();
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
     
	GenReport grp = new GenReport();
	byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);
	

	String fileName = reportName + ".pdf";
	return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + fileName + "\"")
		.build();
    }
    
    
    

    @GET
    @Path("/viewpocancel/{prno}")
    public Response viewPOCancelReport(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("prno") String prno,
	    @PathParam("reportName") String reportName) throws SQLException, JSONException {

	JSONObject mJsonObj = new JSONObject();
	String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	System.out.println("getToken: " + getToken);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "POCancel_Report";

	Jws<Claims> validate = null;

	if (getToken != null) {

	    // jwt verify token
	    validate = JwtManager.parseToken(getToken);
	    String company = validate.getBody().get("role", String.class);
	    String getCompany[] = company.split(" : ");
	    String getCono = getCompany[0];
	    String getDivi = getCompany[1];

	    Map params = new HashMap();
	    params.put("cono", getCono);
	    params.put("divi", getDivi);
	    params.put("prno", prno);
	    params.put("header", "(COPY)");
	    params.put("department", "Purchase");

	    GenReport grp = new GenReport();
	    byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	    String fileName = reportName + ".pdf";
	    return Response.ok(bytes).type("application/pdf")
		    .header("Content-Disposition", "filename=\"" + fileName + "\"").build();

	} else {

	    mJsonObj.put("auth", "false");
	    mJsonObj.put("message", "No token provided");
	}

	return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

    @GET
    @Path("/viewpocancel/{cono}/{divi}/{pono}")
    public Response viewPOCancelReportWithOutToken(@Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("pono") String pono,
	    @PathParam("reportName") String reportName) throws Exception {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "POCancel_Report";

	String textamount = "";
	String grandTotal = ""; // SelectData.getPOTotalAmount(cono, divi, pono);

//	ThaiBaht
	textamount = NumberToText.ThaiBaht(grandTotal);

//	EnglishBaht
//	String amountDecimal = grandTotal.split("\\.")[0];
//	String digitDecimal = grandTotal.split("\\.")[1];
//	amountDecimal = NumberToText.EnglishBaht(amountDecimal);
//	digitDecimal = NumberToText.EnglishBaht(digitDecimal.substring(0, 2));
//	textamount = amountDecimal + " and " + digitDecimal;

	Map params = new HashMap();
	params.put("cono", cono);
	params.put("divi", divi);
	params.put("pono", pono);
	params.put("thaibaht", textamount);

	GenReport grp = new GenReport();
	byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	String fileName = reportName + ".pdf";
	return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + fileName + "\"")
		.build();
    }
    
    
    @GET
    @Path("/viewco/{fconumber}/{tconumber}")
    public Response viewCO(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("fconumber") String fconumber, @PathParam("tconumber") String tconumber,
	    @PathParam("reportName") String reportName) throws SQLException, JSONException {

	JSONObject mJsonObj = new JSONObject();
	String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	System.out.println("getToken: " + getToken);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "PICKINGLIST";


	Jws<Claims> validate = null;

	if (getToken != null) {

	    // jwt verify token
	    validate = JwtManager.parseToken(getToken);
		System.out.println("validate: " + validate);
		String username = validate.getBody().get("aud", String.class);

		String getUser[] = validate.getBody().get("role", String.class).split(" ; ");
		String userID = getUser[0];
		String userRef = getUser[1];
		String group = getUser[2];
		String auth = getUser[3];

		String getSubject[] = validate.getBody().get("sub", String.class).split(" ; ");
		String company = getSubject[0];
		String getCompany[] = company.split(" : ");
		String getCono = getCompany[0];
		String getDivi = getCompany[1];
		String getCompanyName = getCompany[2];

	    	Map params = new HashMap();
		params.put("cono", cono);
		params.put("divi", divi);
		params.put("fconumber", fconumber);
		params.put("tconumber", tconumber);
		params.put("fdate", 0);
		params.put("tdate", 0);
		params.put("username", userID);

	    GenReport grp = new GenReport();
	    byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	    String fileName = reportName + ".pdf";
	    return Response.ok(bytes).type("application/pdf")
		    .header("Content-Disposition", "filename=\"" + fileName + "\"").build();

	} else {

	    mJsonObj.put("auth", "false");
	    mJsonObj.put("message", "No token provided");
	}

	return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
    
    @GET
    @Path("/viewcowotoken/{cono}/{divi}/{fconumber}/{tconumber}/{credit}/{status}/{auth}/{username}")
    public Response viewCOWithOutToken(@Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("fconumber") String fconumber, @PathParam("tconumber") String tconumber
	    , @PathParam("credit") String credit, @PathParam("status") String status, @PathParam("auth") String auth, @PathParam("username") String username, @PathParam("reportName") String reportName) throws Exception {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "PICKINGLIST";
//	reportName = "PICKINGLIST_TST";
	
	
	if (!Boolean.valueOf(auth)) {
	    username = "= " + "'"  + username + "'";
	} else {
	    username = "!= ''";
	}
	
	System.out.println(username);

	Map params = new HashMap();
	params.put("cono", cono);
	params.put("divi", divi);
	params.put("fconumber", fconumber);
	params.put("tconumber", tconumber);
	params.put("fdate", 0);
	params.put("tdate", 0);
	params.put("tdate", 0);
	params.put("credit", credit);
	params.put("status", status);
	params.put("auth", auth);
	params.put("username", username);

	GenReport grp = new GenReport();
	byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	String fileName = reportName + ".pdf";
	return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + fileName + "\"")
		.build();
    }
    
    @GET
    @Path("/viewcowotokenv2/{cono}/{divi}/{fconumber}/{tconumber}/{auth}/{username}")
    public Response viewCOWithOutTokenV2(@Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("fconumber") String fconumber, @PathParam("tconumber") String tconumber, @PathParam("auth") String auth, @PathParam("username") String username, @PathParam("reportName") String reportName) throws Exception {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "PICKINGLIST_M3";
//	reportName = "PICKINGLIST_M3_TST";
	
	
	if (!Boolean.valueOf(auth)) {
	    username = "= " + "'"  + username + "'";
	} else {
	    username = "!= ''";
	}
	
	System.out.println(username);

	Map params = new HashMap();
	params.put("cono", cono);
	params.put("divi", divi);
	params.put("fconumber", fconumber);
	params.put("tconumber", tconumber);
	params.put("fdate", 0);
	params.put("tdate", 0);
	params.put("tdate", 0);
	params.put("auth", auth);
	params.put("username", username);

	GenReport grp = new GenReport();
	byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	String fileName = reportName + ".pdf";
	return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + fileName + "\"")
		.build();
    }
    
    @GET
    @Path("/exporttakeorder/{orderno}/{date}/{month}/{year}/{status}/{credit}/{token}")
    public Response exportTakeOrder(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
	    @PathParam("orderno") String orderno, @PathParam("date") String date, @PathParam("month") String month, @PathParam("year") String year,
	    @PathParam("status") String status, @PathParam("credit") String credit, @PathParam("token") String token, @PathParam("reportName") String reportName) throws SQLException, JSONException {

	JSONObject mJsonObj = new JSONObject();
	String getToken = token;
	System.out.println("getToken: " + getToken);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "TAKEORDER_XLSX";

	Jws<Claims> validate = null;

	if (getToken != null) {

	    // jwt verify token
	    validate = JwtManager.parseToken(getToken);
	    String username = validate.getBody().get("aud", String.class);

	    String getUser[] = validate.getBody().get("role", String.class).split(" ; ");
	    String userID = getUser[0];
	    String userRef = getUser[1];
	    String group = getUser[2];
	    String auth = getUser[3];

	    String getSubject[] = validate.getBody().get("sub", String.class).split(" ; ");
	    String company = getSubject[0];
	    String getCompany[] = company.split(" : ");
	    String getCono = getCompany[0];
	    String getDivi = getCompany[1];
	    String getCompanyName = getCompany[2];
	    
	    String query = null;
	    try {	    
		    query = SelectData.getRSTakeOrder(getCono, getDivi,
		    ConvertStringtoObject.convertNullToObject(orderno),
		    ConvertStringtoObject.convertNullToObject(date),
		    ConvertStringtoObject.convertNullToObject(month),
		    ConvertStringtoObject.convertNullToObject(year),
		    ConvertStringtoObject.convertNullToObject(status),
		    ConvertStringtoObject.convertNullToObject(credit),
		    userID, group, auth);    

	    } catch (Exception e) {
		mJsonObj.put("result", "nok");
		mJsonObj.put("message", e);
		e.printStackTrace();
	    }
	    
	    Map params = new HashMap();
	    params.put("query", query);

	    GenReport grp = new GenReport();
	    byte[] bytes = grp.genReportExcel(httpServletRequest, reportName, outputStream, params);

	    String fileName = reportName + ".xlsx"; // vnd.openxmlformats-officedocument.spreadsheetml.sheet // csv
	    return Response.ok(bytes).type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
		    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
	    
	} else {

	    mJsonObj.put("auth", "false");
	    mJsonObj.put("message", "No token provided");
	}

	return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

    @GET
    @Path("/exporttakeorderwotoken")
    public Response exportTakeOrderWOToken(@Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("pono") String pono,
	    @PathParam("reportName") String reportName) throws Exception {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "TAKEORDER_XLSX";

	String textamount = "";
	String grandTotal = ""; // SelectData.getPOTotalAmount(cono, divi, pono);

	Map params = new HashMap();
	params.put("cono", cono);
	params.put("divi", divi);
	params.put("pono", pono);
	params.put("thaibaht", textamount);

	GenReport grp = new GenReport();
	byte[] bytes = grp.genReportExcel(httpServletRequest, reportName, outputStream, params);

	String fileName = reportName + ".xlsx"; // vnd.openxmlformats-officedocument.spreadsheetml.sheet // csv
	return Response.ok(bytes).type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
		.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
    }
    
    @GET
    @Path("/viewadr/{cono}/{divi}/{adrno}")
    public Response viewADReportWithOutToken(@Context HttpServletRequest httpServletRequest,
	    @PathParam("cono") String cono, @PathParam("divi") String divi, @PathParam("adrno") String adrno,
	    @PathParam("reportName") String reportName) throws SQLException {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	reportName = "ADR_Report";
	String filePath = "D:\\files\\api_project\\adr_images\\";

	Map params = new HashMap();
	params.put("cono", cono);
	params.put("divi", divi);
	params.put("adrno", adrno);
	params.put("imagesDir", filePath);

	GenReport grp = new GenReport();
	byte[] bytes = grp.genReportPdf(httpServletRequest, reportName, outputStream, params);

	String fileName = reportName + ".pdf";
	return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + fileName + "\"")
		.build();
    }

}
