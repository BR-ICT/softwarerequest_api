







package com.br.api;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.*;

import java.io.File;


import java.io.InputStream;
import java.text.BreakIterator;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.Parameters;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.br.auth.JwtManager;
import com.br.data.DeleteData;
import com.br.data.InsertData;
import com.br.data.SelectData;
import com.br.data.UpdateData;
import com.br.utility.ConvertStringtoObject;
import com.br.utility.FileUtillity;
import com.br.utility.HttpConnection;
import com.br.utility.SendEmail;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureException;

@Path("/data")
public class api_data {

	protected static final Logger logger = LogManager.getLogger(api_data.class);

	@GET
	@Path("/company")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCompany(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/company");

		JSONObject mJsonObj = new JSONObject();
		//String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.getCompany(), MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	@GET
	@Path("/getDepartment/{vCONO}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDepartment(@Context HttpHeaders headers, @PathParam("vCONO") String vCONO, String req) throws JSONException {
		logger.info("/company");

		JSONObject mJsonObj = new JSONObject();
		//String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.getDepartment(vCONO), MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	@GET
	@Path("/history/{ID}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getistory(@Context HttpHeaders headers, @PathParam("ID") String ID, String req) throws JSONException {
		logger.info("/company");

		JSONObject mJsonObj = new JSONObject();
		//String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.getHistory(ID), MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}


	@GET
	@Path("/staffcode/{username}/{lastname}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getStaffcode(@Context HttpHeaders headers, @PathParam("username") String username,
			@PathParam("lastname") String lastname, String req) throws JSONException {
		logger.info("/staffcode");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response
					.ok(SelectData.getstaffcode(username, lastname), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/bu")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getBU(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/bu");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				logger.debug("getUsername {} getUser {}", getUsername, getAuth);

				try {
					return Response.ok(SelectData.getBU(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/costcenterbu/{bu}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCostcenterByBU(@Context HttpHeaders headers, @PathParam("bu") String bu, String req)
			throws JSONException {
		logger.info("/costcenterbu/{bu}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(SelectData.getCostCenterByBU(getCono, getDivi, bu),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/warehouse")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getWarehouse(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/warehouse");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response
							.ok(SelectData.getWarehouse(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/accountant")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAccountant(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/accountant");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(SelectData.getAccountant(getCono, getDivi),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/approve")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getApprove(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/approve");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(SelectData.getApprove(), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/deptandcost")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDeparmentAndCostcenter(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/deptandcost");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(SelectData.getDepartmentAndCostcenter(getCono, getDivi),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getoperationdata")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getoperationdata(@Context HttpServletRequest httpServletRequest,@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/getoperationdata");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
			
			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");
				
				String getLocation = getSubject[3];

				try {

					return Response.ok(SelectData.getoperationdata(httpServletRequest,getCono, getDivi,getLocation),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	@GET
	@Path("/jsonschedule")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getjsonschedule(@Context HttpServletRequest httpServletRequest,@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/jsonschedule");
		System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
			
			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");
				
				String getLocation = getSubject[3];

				try {

					return Response.ok(SelectData.jsonschedule(httpServletRequest,getCono, getDivi,getLocation),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	
	@GET
	@Path("/getoperationfilterdata/{fromdate}/{todate}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getoperationfilterdata(@Context HttpServletRequest httpServletRequest, @PathParam("fromdate") String fromdate, @PathParam("todate") String todate,@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/getoperationdata");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
			
			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");
				
				String getLocation = getSubject[3];
			

				try {
					boolean IsAdmin = SelectData.getcostcenter(getCono, getUsername);

					if(IsAdmin) {
						
						return Response.ok(SelectData.getoperationfilterdata(httpServletRequest,getCono, getDivi,getLocation,fromdate,todate),
								MediaType.APPLICATION_JSON + ";charset=utf8").build();
						
					}
					else {
						
						return Response.ok(SelectData.getoperationfilterdatabyuser(httpServletRequest,getCono, getDivi,getLocation,fromdate,todate,getUsername),
								MediaType.APPLICATION_JSON + ";charset=utf8").build();
						
					
					}

					

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/monitoringreceipt")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMonitoringReceipt(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/deptandcost");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(SelectData.getMonitoringReceipt(getCono, getDivi),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/marnumber/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMARNumber(@Context HttpHeaders headers, @PathParam("fromstatus") String fromstatus,
			@PathParam("tostatus") String tostatus, String req) throws JSONException {
		logger.info("/marnumber/{fromstatus}/{tostatus}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(SelectData.getMARNumber(getCono, getDivi, fromstatus, tostatus, getUsername),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	

	@GET
	@Path("/item/{whs}/{type}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getItem(@Context HttpHeaders headers, @PathParam("whs") String whs, @PathParam("type") String type,
			String req) throws JSONException {
		logger.info("getItem");
		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(SelectData.getItem(getCono, getDivi, whs, type),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/itemdetail/{whs}/{item}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getItemDetail(@Context HttpHeaders headers, @PathParam("whs") String whs,
			@PathParam("item") String item, String req) throws JSONException {
		logger.info("getItemDetail");
		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(SelectData.getItemDetailV2(getCono, getDivi, whs, item),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/marhead/{marno}/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMARHead(@Context HttpHeaders headers, @PathParam("marno") String marno,
			@PathParam("fromstatus") String fromstatus, @PathParam("tostatus") String tostatus, String req)
			throws JSONException {
		logger.info("/marhead/{marno}/{fromstatus}/{tostatus}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(SelectData.getMARHead(getCono, getDivi, marno, fromstatus, tostatus),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/mardetail/{marno}/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMARDetail(@Context HttpHeaders headers, @PathParam("marno") String marno,
			@PathParam("fromstatus") String fromstatus, @PathParam("tostatus") String tostatus, String req)
			throws JSONException {
		logger.info("/marhead/{marno}/{fromstatus}/{tostatus}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(SelectData.getMARDetail(getCono, getDivi, marno, fromstatus, tostatus),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/marfile/{marno}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMARFile(@Context HttpHeaders headers, @PathParam("marno") String marno, String req)
			throws JSONException {
		logger.info("/marfile/{marno}");
		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(SelectData.getMARFile(getCono, getDivi, marno),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	// @GET
	// @Path("/file/{filename}/{token}")
	// @Produces("image/png, application/pdf, application/vnd.ms-excel,
	// application/msword")
	// public Response getFile(@Context HttpHeaders headers, @PathParam("filename")
	// String fileName,
	// @PathParam("token") String token, @Context HttpServletRequest
	// httpServletRequest) {
	// JSONObject mJsonObj = new JSONObject();
	// String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	// System.out.println("headers: " + headers);
	// System.out.println("getToken: " + getToken);
	//
	// return FileUtillity.getFileV3(httpServletRequest, fileName);
	//
	// }

	@GET
	@Path("/file/{filename}/{token}")
	@Produces("image/jpeg, image/png, application/pdf, application/vnd.ms-excel, application/msword")
	public Response getFile(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("filename") String fileName, @PathParam("token") String token, String req) throws JSONException {
		logger.info("/file/{filename}/{token}");
		JSONObject mJsonObj = new JSONObject();
		String getToken = token;
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					// return Response.ok(SelectData.getMARFile(getCono, getDivi, marno),
					// MediaType.APPLICATION_JSON + ";charset=utf8").build();

					return FileUtillity.getFileV3(httpServletRequest, fileName);

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).build();

	}

	@POST
	@Path("/marhead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addMARHead(@Context HttpHeaders headers, @FormDataParam("vMARNumber") String vMARNumber,
			@FormDataParam("vDate") String vDate, @FormDataParam("vPostDate") String vPostDate,
			@FormDataParam("vMonth") String vMonth, @FormDataParam("vType") String vType,
			@FormDataParam("vPrefix") String vPrefix, @FormDataParam("vBU") String vBU,
			@FormDataParam("vCostcenter") String vCostcenter, @FormDataParam("vAccountant") String vAccountant,
			@FormDataParam("vRequestor") String vRequestor, @FormDataParam("vPurpose") String vPurpose,
			@FormDataParam("vRemark") String vRemark, @FormDataParam("vPurpose") String purpose,
			@FormDataParam("vApprove1") String vApprove1, @FormDataParam("vApprove2") String vApprove2,
			@FormDataParam("vApprove3") String vApprove3, @FormDataParam("vApprove4") String vApprove4,
			@FormDataParam("vApprove5") String vApprove5, @FormDataParam("vAppICT") String vAppICT,
			@FormDataParam("vAppCIO") String vAppCIO, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vSubmit") String vSubmit) throws JSONException {
		logger.info("/marhead");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(InsertData.addMARHead(getCono, getDivi, vMARNumber, vDate, vPostDate, vMonth,
							vType, vPrefix, vBU, vCostcenter, vAccountant, vRequestor, vRemark, purpose, vApprove1,
							vApprove2, vApprove3, vApprove4, vApprove5, vAppICT, vAppCIO, vStatus, vSubmit,
							getUsername), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/mardetail")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addMARDetail(@Context HttpHeaders headers, @FormDataParam("vMARNumber") String vMARNumber,
			@FormDataParam("vPrefix") String vPrefix, @FormDataParam("vRefNumber") String vRefNumber,
			@FormDataParam("vTypeAdjust") String vTypeAdjust, @FormDataParam("vItemLine") String vItemLine,
			@FormDataParam("vItem") String vItem, @FormDataParam("vItemDesc1") String vItemDesc1,
			@FormDataParam("vFacility") String vFacility, @FormDataParam("vWarehouse") String vWarehouse,
			@FormDataParam("vLocation") String vLocation, @FormDataParam("vLotNo") String vLotNo,
			@FormDataParam("vDate") String vDate, @FormDataParam("vUnit") String vUnit,
			@FormDataParam("vQty") String vQty, @FormDataParam("vUnitPrice") String vUnitPrice,
			@FormDataParam("vAmount") String vAmount, @FormDataParam("vRemark1") String vRemark1,
			@FormDataParam("vRemark2") String vRemark2, @FormDataParam("vStatus") String vStatus) throws JSONException {
		logger.info("/mardetail");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(
							InsertData.addMARDetail(getCono, getDivi, vMARNumber, vPrefix, vRefNumber, vTypeAdjust,
									vItemLine, vItem, vItemDesc1, vFacility, vWarehouse, vLocation, vLotNo, vDate,
									vUnit, vQty, vUnitPrice, vAmount, vRemark1, vRemark2, vStatus, getUsername),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/addmonitoringreceipt")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addMonitoringReceipt(@Context HttpHeaders headers, @FormDataParam("rcno") String rcno)
			throws JSONException {
		logger.info("/addmonitoringreceipt");
		System.out.println("addmonitoringreceipt\n");
		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					// FileUtillity.writeToFileServerV2(httpServletRequest, vFile, vFileName,
					// vFilePath);
					// FileUtillity.deleteFileV4(httpServletRequest, vFilePath);
					return Response
							.ok(InsertData.addMonitoringReceipt(rcno), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/marfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addMARFile(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vMARNumber") String vMARNumber, @FormDataParam("vPrefix") String vPrefix,
			@FormDataParam("vFile") InputStream vFile, @FormDataParam("vFileName") String vFileName,
			@FormDataParam("vFileType") String vFileType, @FormDataParam("vFileLine") String vFileLine,
			@FormDataParam("vRemark1") String vRemark1, @FormDataParam("vRemark2") String vRemark2)
			throws JSONException {
		logger.info("/marfile");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					// FileUtillity.writeToFileServerV2(httpServletRequest, vFile, vFileName,
					// vFilePath);
					// FileUtillity.deleteFileV4(httpServletRequest, vFilePath);
					return Response.ok(
							InsertData.addMARFile(httpServletRequest, getCono, getDivi, vPrefix, vMARNumber, vFile,
									vFileName, vFileType, vRemark1, vRemark2, getUsername),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

//////////////////////////////

	@PUT
	@Path("/updateroomcard")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateroomcard(@Context HttpHeaders headers, @FormDataParam("vID") String vID,
			@FormDataParam("vRoom") String vRoom, @FormDataParam("vCard") String vCard

	) throws JSONException {
		logger.info("/updateroomcard");
		System.out.print("updateroomcard");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");
				String getLocation = getSubject[3];


				try {

					return Response.ok(UpdateData.updateroomcard(vID, vRoom, vCard ,getLocation),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	@GET
	@Path("/getimage/{vID}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getimage(@Context HttpHeaders headers,@Context HttpServletRequest httpServletRequest,
		 @PathParam("vID") String vID,
			String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();

		try {

			try {

				return Response.ok(SelectData.getimage(httpServletRequest,vID),
						MediaType.APPLICATION_JSON + ";charset=utf8").build();

			} catch (Exception e) {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", e);
			e.printStackTrace();
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	
	
	@PUT
	@Path("/updateemp")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateemp(@Context HttpHeaders headers, @FormDataParam("vID") String vID,@FormDataParam("vEmail") String vEmail,
			@FormDataParam("vEmployeedialog") String vEmployeedialog,	@FormDataParam("vRemark") String vRemark,@FormDataParam("vStatus") String vStatus
			,@FormDataParam("vCheckin") String vCheckin,@FormDataParam("vCheckintime") String vCheckintime
			,@FormDataParam("vCheckout") String vCheckout,@FormDataParam("vCheckouttime") String vCheckouttime

	) throws JSONException {
		logger.info("/updateroomcard");
		System.out.print("updateroomcard");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");
				String getLocation = getSubject[3];


				try {

					return Response.ok(UpdateData.updateemp(vID,vEmployeedialog,vEmail,getLocation,vRemark,vStatus,vCheckin,vCheckintime,vCheckout,vCheckouttime),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	@PUT
	@Path("/updatefollower")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
//@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updatefollower(@Context HttpHeaders headers, @FormDataParam("vID") String vID,
			@FormDataParam("H_SURNAME") String H_SURNAME, @FormDataParam("oldH_SURNAME") String oldH_SURNAME,
			@FormDataParam("vCONO") String vCONO,
			@FormDataParam("vDIVI") String vDIVI,
			@FormDataParam("vLocaton") String vLocaton

	) throws JSONException {
		logger.info("/visitorheader");
		System.out.print("visitorheaderrrrrrrrrrrrrrrrrrr");

		JSONObject mJsonObj = new JSONObject();
	

			
			
				try {

					return Response.ok(UpdateData.updatefollower(vID, H_SURNAME, oldH_SURNAME , vCONO,vDIVI,vLocaton),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	


	@PUT
	@Path("/checkoutwithdatetime")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response checkoutwithdatetime(@Context HttpHeaders headers, @FormDataParam("vID") String vID
			,@FormDataParam("vStatuscheck") String vStatuscheck
			,@FormDataParam("vRemark") String remark
			,@FormDataParam("vCheckout") String vcheckout
			,@FormDataParam("vCheckouttime") String vcheckouttime

			

	) throws JSONException {
		logger.info("/visitorheader");
		System.out.print("visitorheaderrrrrrrrrrrrrrrrrrr00");
		String sts = "00";
		switch(vStatuscheck) {
		case "CHECKOUT":
			sts ="50";
			break;
		
		case "SUBMIT":
		sts ="10";
		break;
		case "APPROVE":
			sts ="30";
			break;
		case "CANCEL":
			sts ="99";
			break;
			
		case "REJECT":
			sts ="80";
			break;
			
		case "CHECKIN":
			sts ="10";
			break;
			
			default:
			sts ="00";
			break;
			
	}
			

		JSONObject mJsonObj = new JSONObject();

				
				String getCono = "10";
				String getDivi = "101";
				String getLocation = "11";


				try {

					return Response.ok(UpdateData.checkoutwithdatetime(vID,sts,getLocation,remark, vcheckout, vcheckouttime), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	

	@PUT
	@Path("/checkout")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response checkout(@Context HttpHeaders headers, @FormDataParam("vID") String vID
			,@FormDataParam("vStatuscheck") String vStatuscheck
			,@FormDataParam("vRemark") String remark
			,@FormDataParam("vCheckout") String vCheckout

	) throws JSONException {
		logger.info("/visitorheader");
		System.out.print("visitorheaderrrrrrrrrrrrrrrrrrr00");
		String sts = "00";
		switch(vStatuscheck) {
		case "CHECKOUT":
			sts ="50";
			break;
		
		case "SUBMIT":
		sts ="10";
		break;
		case "APPROVE":
			sts ="30";
			break;
		case "CANCEL":
			sts ="99";
			break;
			
		case "REJECT":
			sts ="80";
			break;
			
		case "CHECKIN":
			sts ="10";
			break;
			
			default:
			sts ="00";
			break;
			
	}
			

		JSONObject mJsonObj = new JSONObject();
	//	String getToken = headers.getRequestHeaders().getFirst("x-access-token");
// System.out.println("getToken: " + getToken);

		//if (getToken != null && !getToken.isEmpty()) {
		//	String getTokenData = HttpConnection.httpConnectionV2(getToken);
// System.out.println("getTokenData: " + getTokenData);

			//JSONObject dataObject = new JSONObject(getTokenData);
			//boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			//if (checkToken) {
			//	JSONObject getDataObject = dataObject.getJSONObject("body");
			//	String getSubject[] = getDataObject.getString("sub").split(" : ");
			//	String getCono = getSubject[0];
			//	String getDivi = getSubject[1];
			// getCompanyName = getSubject[2];
			//	String getUsername = getDataObject.getString("aud");
			//	String getAuth = getDataObject.getString("role");
			//	String getLocation = getSubject[3];
				
				String getCono = "10";
				String getDivi = "101";
				String getLocation = "11";


				try {

					return Response.ok(UpdateData.checkout(vID,sts,getLocation,remark), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	

	@PUT
	@Path("/checkout1")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response checkout(@Context HttpHeaders headers, @FormDataParam("vID") String vID
			,@FormDataParam("vStatuscheck") String vStatuscheck
			,@FormDataParam("vROOM") String room
			,@FormDataParam("vLocation") String location
			,@FormDataParam("vRemark") String remark)
	throws JSONException {
		//logger.info("/visitorheader");
		System.out.print("checkout111111111111111111111");
		String sts = "00";
		switch(vStatuscheck) {
		case "CHECKOUT":
			sts ="50";
			break;
		
		case "SUBMIT":
		sts ="10";
		break;
		case "APPROVE":
			sts ="30";
			break;
		case "CANCEL":
			sts ="99";
			break;
			
		case "REJECT":
			sts ="80";
			break;
			
		case "CHECKIN":
			sts ="10";
			break;
			
			default:
			sts ="00";
			break;
			
	}
			

		JSONObject mJsonObj = new JSONObject();

			
				try {

					return Response.ok(UpdateData.checkout1(vID,sts,location,room,remark), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

		
	return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
}





	@PUT
	@Path("/addvisitorheader")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addvisitorheader(@Context HttpHeaders headers, @FormDataParam("vID") String vID,
			@FormDataParam("vIMG") String vIMG, @FormDataParam("vTimein") String vTimein,
			@FormDataParam("vTimeout") String vTimeout, @FormDataParam("vLicense") String vLicense,
			@FormDataParam("vName") String vName, @FormDataParam("vSurname") String vSurname,
			@FormDataParam("vTel") String vTel, @FormDataParam("vReason") String vReason,
			@FormDataParam("vEmployee") String vEmployee, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vCono") String vCono, @FormDataParam("vCompany") String vCompany,
			@FormDataParam("vMeetdate") String vMeetdate, @FormDataParam("vMeettime") String vMeettime,
			@FormDataParam("vMail") String vMail,@FormDataParam("imagefile") InputStream imagefile,
			@FormDataParam("imagename") String imagename,@FormDataParam("vCONO") String vCONO,
			@FormDataParam("vDIVI") String vDIVI,@FormDataParam("vLocation") String vLocation,
			@FormDataParam("vFoodnumber") String vFoodnumber,
			@FormDataParam("vATKnumber") String vATKnumber,
			@FormDataParam("vParknumber") String vParknumber,
			@FormDataParam("vIstoken") String vIstoken,@FormDataParam("vETC") String vETC,
			@FormDataParam("vRoom") String vRoom,
			@FormDataParam("vMeetdateout") String vMeetdateout,
			@FormDataParam("vMeettimeout") String vMeettimeout,
			
			@FormDataParam("vBeveragenumber") String vBeveragenumber,
			@FormDataParam("vSnacksnumber") String vSnacksnumber,
			@FormDataParam("vSandalsnumber") String vSandalsnumber
			
		
	) throws JSONException {
		logger.info("/visitorheader");
		System.out.print("visitorheaderrrrrrrrrrrrrrrrrrr");

	
		JSONObject mJsonObj = new JSONObject();
		String username = "VISITOR";

			if(vIstoken.equalsIgnoreCase("true")) {
				
				String getToken = headers.getRequestHeaders().getFirst("x-access-token");
				// System.out.println("getToken: " + getToken);

						if (getToken != null && !getToken.isEmpty()) {
							String getTokenData = HttpConnection.httpConnectionV2(getToken);
				// System.out.println("getTokenData: " + getTokenData);

							JSONObject dataObject = new JSONObject(getTokenData);
							boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

							if (checkToken) {
								JSONObject getDataObject = dataObject.getJSONObject("body");
								String getSubject[] = getDataObject.getString("sub").split(" : ");
								String getCono = getSubject[0];
								String getDivi = getSubject[1];
								String getCompanyName = getSubject[2];
								String getUsername = getDataObject.getString("aud");
								String getAuth = getDataObject.getString("role");
								
								username = getUsername; 
								
								

							} else {
								mJsonObj.put("result", "nok");
								mJsonObj.put("message", "Token expired.");
								logger.error("Token expired.");
							}

						} else {
							mJsonObj.put("result", "nok");
							mJsonObj.put("message", "No token provided.");
							logger.error("No token provided.");
						}

		
				
			}


				try {

					return Response.ok(
							UpdateData.addvisitorheader(vID, vIMG, vTimein, vTimeout, vLicense, vName, vSurname, vTel,
									vReason, vEmployee, vStatus, vCono, vCompany, vMeetdate, vMeettime, vMail,imagefile,imagename, vCONO, vDIVI, vLocation ,vFoodnumber ,vATKnumber ,vParknumber,vIstoken,vETC,vRoom,vMeetdateout,vMeettimeout,vBeveragenumber,vSnacksnumber,vSandalsnumber,username),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	@GET
	@Path("/showvisitor/{vID}/{location}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response showvisitor(@Context HttpHeaders headers, @PathParam("vID") String vID,@PathParam("location") String location,
			@Context HttpServletRequest httpServletRequest) throws JSONException {
		logger.info("/getemployee");

		JSONObject mJsonObj = new JSONObject();
		

				try {

					return Response.ok(
							SelectData.showvisitor(vID,location ),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}



///////////////////////////////	

	@PUT
	@Path("/updateswrfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateMARHead(@Context HttpHeaders headers, @FormDataParam("oldSFORNO") String oldSFORNO,
			@FormDataParam("oldSFCONO") String oldSFCONO, @FormDataParam("oldSFDIVI") String oldSFDIVI,

			@FormDataParam("newSFORNO") String newSFORNO, @FormDataParam("newSFPREF") String newSFPREF,
			@FormDataParam("newSFLINE") String newSFLINE, @FormDataParam("newSFNAME") String newSFNAME,
			@FormDataParam("newSFTYPE") String newSFTYPE, @FormDataParam("newSFREM1") String newSFREM1,
			@FormDataParam("newSFREM2") String newSFREM2) throws JSONException {
		logger.info("/swrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
		System.out.print("swrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(
							UpdateData.updateSWRFile(oldSFORNO, oldSFCONO, oldSFDIVI, newSFORNO, newSFPREF, newSFLINE,
									newSFNAME, newSFTYPE, newSFREM1, newSFREM2, getUsername),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/marhead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateMARHead(@Context HttpHeaders headers, @FormDataParam("vMARNumber") String vMARNumber,
			@FormDataParam("vDate") String vDate, @FormDataParam("vPostDate") String vPostDate,
			@FormDataParam("vMonth") String vMonth, @FormDataParam("vType") String vType,
			@FormDataParam("vPrefix") String vPrefix, @FormDataParam("vBU") String vBU,
			@FormDataParam("vCostcenter") String vCostcenter, @FormDataParam("vAccountant") String vAccountant,
			@FormDataParam("vRequestor") String vRequestor, @FormDataParam("vPurpose") String vPurpose,
			@FormDataParam("vRemark") String vRemark, @FormDataParam("vPurpose") String purpose,
			@FormDataParam("vApprove1") String vApprove1, @FormDataParam("vApprove2") String vApprove2,
			@FormDataParam("vApprove3") String vApprove3, @FormDataParam("vApprove4") String vApprove4,
			@FormDataParam("vApprove5") String vApprove5, @FormDataParam("vAppICT") String vAppICT,
			@FormDataParam("vAppCIO") String vAppCIO, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vSubmit") String vSubmit) throws JSONException {
		logger.info("/marhead");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(UpdateData.updateMARHead(getCono, getDivi, vMARNumber, vDate, vPostDate, vMonth,
							vType, vPrefix, vBU, vCostcenter, vAccountant, vRequestor, vRemark, purpose, vApprove1,
							vApprove2, vApprove3, vApprove4, vApprove5, vAppICT, vAppCIO, vStatus, vSubmit,
							getUsername), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/marhead/{marno}/{status}/{submit}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateStatusMARHead(@Context HttpHeaders headers, @PathParam("marno") String marno,
			@PathParam("status") String status, @PathParam("submit") String submit, String req) throws JSONException {
		logger.info("/marhead");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response
							.ok(UpdateData.updateStatusMARHead(getCono, getDivi, marno, status, submit, getUsername),
									MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/mardetail")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateMARDetail(@Context HttpHeaders headers, @FormDataParam("vMARNumber") String vMARNumber,
			@FormDataParam("vPrefix") String vPrefix, @FormDataParam("vRefNumber") String vRefNumber,
			@FormDataParam("vTypeAdjust") String vTypeAdjust, @FormDataParam("vItemLine") String vItemLine,
			@FormDataParam("vItem") String vItem, @FormDataParam("vItemDesc1") String vItemDesc1,
			@FormDataParam("vFacility") String vFacility, @FormDataParam("vWarehouse") String vWarehouse,
			@FormDataParam("vLocation") String vLocation, @FormDataParam("vLotNo") String vLotNo,
			@FormDataParam("vDate") String vDate, @FormDataParam("vUnit") String vUnit,
			@FormDataParam("vQty") String vQty, @FormDataParam("vUnitPrice") String vUnitPrice,
			@FormDataParam("vAmount") String vAmount, @FormDataParam("vRemark1") String vRemark1,
			@FormDataParam("vRemark2") String vRemark2, @FormDataParam("vStatus") String vStatus) throws JSONException {
		logger.info("/mardetail");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(
							UpdateData.updateMARDetail(getCono, getDivi, vMARNumber, vPrefix, vRefNumber, vTypeAdjust,
									vItemLine, vItem, vItemDesc1, vFacility, vWarehouse, vLocation, vLotNo, vDate,
									vUnit, vQty, vUnitPrice, vAmount, vRemark1, vRemark2, vStatus, getUsername),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@DELETE
	@Path("/deleteswrfile/{SFORNO}")

	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteSWRFile(@Context HttpHeaders headers, @PathParam("SFORNO") String SFORNO)
			throws JSONException {
		logger.info("/mardetail/{marno}/{line}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(DeleteData.deleteSWRFile(getCono, getDivi, SFORNO),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/swrnumber")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSWRNumber(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("swrnumber");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String[] getSubject = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response
							.ok(SelectData.getSWRNumber(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@DELETE
	@Path("/mardetail/{marno}/{line}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteMARDetail(@Context HttpHeaders headers, @PathParam("marno") String marno,
			@PathParam("line") String line, String req) throws JSONException {
		logger.info("/mardetail/{marno}/{line}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);

			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(DeleteData.deleteMARDetail(getCono, getDivi, marno, line),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/marfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response deleteMARFile(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vMARNumber") String vMARNumber, @FormDataParam("vPrefix") String vPrefix,
			@FormDataParam("vFile") InputStream vFile, @FormDataParam("vFileName") String vFileName,
			@FormDataParam("vFileType") String vFileType, @FormDataParam("vFileLine") String vFileLine,
			@FormDataParam("vRemark1") String vRemark1, @FormDataParam("vRemark2") String vRemark2)
			throws JSONException {
		logger.info("/marfile");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response.ok(DeleteData.deleteMARFile(httpServletRequest, getCono, getDivi, vMARNumber,
							vFileLine, vFileName), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@GET
	@Path("/month")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMonth(@Context HttpHeaders headers, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {

					return Response
							.ok(SelectData.getMonth(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/year")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getYear(@Context HttpHeaders headers, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {

					return Response
							.ok(SelectData.getYear(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/credit/{credit}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCredit(@Context HttpHeaders headers, @PathParam("credit") String credit, String req)
			throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {

					return Response.ok(SelectData.getCredit(getCono, getDivi, credit),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/round")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getRound(@Context HttpHeaders headers, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {

					return Response
							.ok(SelectData.getRound(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getStatus(@Context HttpHeaders headers, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {

					return Response
							.ok(SelectData.getStatus(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/customer")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCustomer(@Context HttpHeaders headers, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {
					return Response.ok(SelectData.getCustomerV2(getCono, getDivi, userID),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/adrnumber/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getADRNumber(@Context HttpHeaders headers, @PathParam("fromstatus") String fromstatus,
			@PathParam("tostatus") String tostatus, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(SelectData.getADRNumber(getCono, getDivi, username, fromstatus, tostatus),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/adrnumberaccountant/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getADRNumberAccountant(@Context HttpHeaders headers, @PathParam("fromstatus") String fromstatus,
			@PathParam("tostatus") String tostatus, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response
							.ok(SelectData.getADRNumberAccountant(getCono, getDivi, username, fromstatus, tostatus),
									MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/adrhead/{adrno}/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getADRHead(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("adrno") String adrno, @PathParam("fromstatus") String fromstatus,
			@PathParam("tostatus") String tostatus, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							SelectData.getADRHead(httpServletRequest, getCono, getDivi, adrno, fromstatus, tostatus),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/adrheadmonitoring/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getADRHeadMonitoring(@Context HttpHeaders headers, @PathParam("fromstatus") String fromstatus,
			@PathParam("tostatus") String tostatus, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(SelectData.getADRHeadMonitoring(getCono, getDivi, fromstatus, tostatus),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/adrdetail/{adrno}/{fromstatus}/{tostatus}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getADRDetail(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("adrno") String adrno, @PathParam("fromstatus") String fromstatus,
			@PathParam("tostatus") String tostatus, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							SelectData.getADRDetail(httpServletRequest, getCono, getDivi, adrno, fromstatus, tostatus),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/monitoringhead/{orderno}/{date}/{month}/{year}/{status}/{credit}/{round}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMonitorHead(@Context HttpHeaders headers, @PathParam("orderno") String orderno,
			@PathParam("date") String date, @PathParam("month") String month, @PathParam("year") String year,
			@PathParam("status") String status, @PathParam("credit") String credit, @PathParam("round") String round,
			String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {
					return Response.ok(
							SelectData.getMonitoringHead(getCono, getDivi,
									ConvertStringtoObject.convertNullToObject(orderno),
									ConvertStringtoObject.convertNullToObject(date),
									ConvertStringtoObject.convertNullToObject(month),
									ConvertStringtoObject.convertNullToObject(year),
									ConvertStringtoObject.convertNullToObject(status),
									ConvertStringtoObject.convertNullToObject(credit),
									ConvertStringtoObject.convertNullToObject(round), userID, group, auth),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/image/{filename}")
	@Produces({ "image/png", "image/jpg", "image/gif" })
	public Response getImage(@Context HttpHeaders headers, @PathParam("filename") String filename,
			@Context HttpServletRequest httpServletRequest) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(SelectData.getBU(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/image")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addImage(@FormDataParam("vImageFile") InputStream fileInputStream,
			@FormDataParam("vImageFile") FormDataContentDisposition fileFormDataContentDisposition,
			@FormDataParam("vImageName") String vImageName, @Context HttpHeaders headers,
			@Context HttpServletRequest httpServletRequest) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				String uploadFilePath = null;
				String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";

				try {
					// System.out.println("getFileName: " +
					// fileFormDataContentDisposition.getFileName());
					// fileName = fileFormDataContentDisposition.getFileName();
					// fileName = "jaonaay";
					System.out.println("filePath: " + filePath + vImageName);

					uploadFilePath = FileUtillity.writeToFileServer(fileInputStream, vImageName, filePath);
					return Response.status(Response.Status.OK).build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/file")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addFile(@FormDataParam("vImageFile") InputStream fileInputStream,
			@FormDataParam("vImageFile") FormDataContentDisposition fileFormDataContentDisposition,
			@FormDataParam("vImageName") String vImageName, @Context HttpHeaders headers,
			@Context HttpServletRequest httpServletRequest) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		// Jws<Claims> validate = null;
		//
		// if (getToken != null) {
		//
		// try {
		//
		// // jwt verify token
		// validate = JwtManager.parseToken(getToken);
		// // System.out.println("validate: " + validate);
		// String username = validate.getBody().get("aud", String.class);
		// String company = validate.getBody().get("sub", String.class);
		// String getCompany[] = company.split(" : ");
		// String getCono = getCompany[0];
		// String getDivi = getCompany[1];
		// String getCompanyName = getCompany[2];
		// // String getCono = "10";
		// // String getDivi = "101";

		String uploadFilePath = null;
		String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";

		try {
			// System.out.println("getFileName: " +
			// fileFormDataContentDisposition.getFileName());
			// fileName = fileFormDataContentDisposition.getFileName();
			// fileName = "jaonaay";
			System.out.println("filePath: " + filePath + vImageName);

			uploadFilePath = FileUtillity.writeToFileServer(fileInputStream, vImageName, filePath);
			return Response.status(Response.Status.OK).build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);

		}

		// } catch (SignatureException e) {
		// mJsonObj.put("auth", "false");
		// mJsonObj.put("message", e.getMessage());
		//
		// }
		//
		// } else {
		// mJsonObj.put("auth", "false");
		// mJsonObj.put("message", "No token provided");
		// }

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	// @Path("/files")
	// public class FileDownloadService {
	private static final String FILE_PATH = "D:\\files\\api_project\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\mar_api\\WEB-INF\\image\\TEST.pdf";

	// @GET
	// @Path("/file/{filename}/{token}")
	// @Produces("image/png, application/pdf, application/vnd.ms-excel,
	// application/msword")
	// public Response getFile(@Context HttpHeaders headers, @PathParam("filename")
	// String fileName,
	// @PathParam("token") String token, @Context HttpServletRequest
	// httpServletRequest) {
	// JSONObject mJsonObj = new JSONObject();
	// String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	// System.out.println("headers: " + headers);
	// System.out.println("getToken: " + getToken);
	//
	// return FileUtillity.getFileV3(httpServletRequest, fileName);
	//
	// }

	@DELETE
	@Path("/file/{filename}/{token}")
	@Produces("image/png, application/pdf, application/vnd.ms-excel, application/msword")
	public Response deleteFile(@Context HttpHeaders headers, @PathParam("filename") String fileName,
			@PathParam("token") String token, @Context HttpServletRequest httpServletRequest) {
		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		System.out.println("headers: " + headers);
		System.out.println("getToken: " + getToken);

		return FileUtillity.deleteFileV3(httpServletRequest, fileName);

	}

	@GET
	@Path("/filev2/{name}/{token}")
	@Produces("image/png, application/pdf, application/vnd.ms-excel, application/msword")
	public Response getFileV2(@Context HttpHeaders headers) {
		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		System.out.println("headers: " + headers);
		System.out.println("getToken: " + getToken);

		File file = new File(FILE_PATH);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=\"TEST.pdf\"");
		return response.build();
		// return null;

		// return Response.ok(SelectData.getADRDetail(httpServletRequest, getCono,
		// getDivi, adrno, fromstatus, tostatus),
		// MediaType.APPLICATION_JSON + ";charset=utf8").build();

	}

	// vMARNumber: "",
	// vDate: moment(new Date()).format("YYYY-MM-DD"),
	// vPostDate: moment(new Date()).format("YYYY-MM-DD"),
	// vMonth: moment(new Date()).format("YYYYMM"),
	// vType: "",
	// vPrefix: "",
	// vBU: "",
	// vCostcenter: "",
	// vAccountant: "",
	// vRequestor: loginActions.getTokenUsername(),
	// vPurpose: "",
	// vRemark: "",
	// vApprove1: "",
	// vApprove2: "",
	// vApprove3: "",
	// vApprove4: "",
	// vApprove5: "",
	// vStatus: "",

	@POST
	@Path("/adrhead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addADRHead(@Context HttpHeaders headers, @FormDataParam("vADRNumber") String vADRNumber,
			@FormDataParam("vDate") String vDate, @FormDataParam("vMonth") String vMonth,
			@FormDataParam("vType") String vType, @FormDataParam("vPrefix") String vPrefix,
			@FormDataParam("vBOI") String vBOI, @FormDataParam("vBU") String vBU,
			@FormDataParam("vCostcenter") String vCostcenter, @FormDataParam("vVat") String vVat,
			@FormDataParam("vAccountant") String vAccountant, @FormDataParam("vRequestor") String vRequestor,
			@FormDataParam("vRemark") String vRemark, @FormDataParam("vApprove1") String vApprove1,
			@FormDataParam("vApprove2") String vApprove2, @FormDataParam("vApprove3") String vApprove3,
			@FormDataParam("vApprove4") String vApprove4, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vSubmit") String vSubmit) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							InsertData.addADRHead(getCono, getDivi, vADRNumber, vDate, vMonth, vType, vPrefix, vBOI,
									vBU, vCostcenter, vVat, vAccountant, vRequestor, vRemark, vApprove1, vApprove2,
									vApprove3, vApprove4, vStatus, vSubmit, username),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/orderhead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addOrderHead(@Context HttpHeaders headers, @FormDataParam("vOrderNumber") String vOrderNumber,
			@FormDataParam("vCustomerNo") String vCustomerNo, @FormDataParam("vOrderDate") String vOrderDate,
			@FormDataParam("vDeliveryDate") String vDeliveryDate, @FormDataParam("vRound") String vRound,
			@FormDataParam("vPricelist") String vPricelist, @FormDataParam("vOrderType") String vOrderType,
			@FormDataParam("vWarehouse") String vWarehouse, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vType") String vType, @FormDataParam("vRemark") String vRemark,
			@FormDataParam("vPONumber") String vPONumber, @FormDataParam("vDay") String vDay) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

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

				try {

					return Response.ok(InsertData.addOrderHeadV2(getCono, getDivi, vCustomerNo, vOrderDate,
							vDeliveryDate, vRound, vPricelist, vOrderType, vWarehouse, vStatus, vType, vRemark,
							vPONumber, vDay, userID), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/adrdetail")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addADRDetail(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vADRNumber") String vADRNumber, @FormDataParam("vPrefix") String vPrefix,
			@FormDataParam("vItemLine") String vItemLine, @FormDataParam("vItemNo") String vItemNo,
			@FormDataParam("vSBNO") String vSBNO, @FormDataParam("vItemCostcenter") String vItemCostcenter,
			@FormDataParam("vItemDate") String vItemDate, @FormDataParam("vAssetCost") String vAssetCost,
			@FormDataParam("vNetValue") String vNetValue, @FormDataParam("vItemQty") String vItemQty,
			@FormDataParam("vItemPrice") String vItemPrice, @FormDataParam("vItemRemark") String vItemRemark,
			@FormDataParam("vImageFile") InputStream fileInputStream,
			@FormDataParam("vImageFile") FormDataContentDisposition fileFormDataContentDisposition,
			@FormDataParam("vImageName") String vImageName, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vSubmit") String vSubmit) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							InsertData.addADRDetail(httpServletRequest, getCono, getDivi, vADRNumber, vPrefix,
									vItemLine, vItemNo, vSBNO, vItemCostcenter, vItemDate, vAssetCost, vNetValue,
									vItemQty, vItemPrice, vItemRemark, fileInputStream,
									ConvertStringtoObject.convertNullToObject(vImageName), vStatus, vSubmit, username),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/adrdetail")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateADRDetail(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vADRNumber") String vADRNumber, @FormDataParam("vPrefix") String vPrefix,
			@FormDataParam("vItemLine") String vItemLine, @FormDataParam("vItemNo") String vItemNo,
			@FormDataParam("vSBNO") String vSBNO, @FormDataParam("vItemCostcenter") String vItemCostcenter,
			@FormDataParam("vItemDate") String vItemDate, @FormDataParam("vAssetCost") String vAssetCost,
			@FormDataParam("vNetValue") String vNetValue, @FormDataParam("vItemQty") String vItemQty,
			@FormDataParam("vItemPrice") String vItemPrice, @FormDataParam("vItemRemark") String vItemRemark,
			@FormDataParam("vImageFile") InputStream fileInputStream,
			@FormDataParam("vImageFile") FormDataContentDisposition fileFormDataContentDisposition,
			@FormDataParam("vImageName") String vImageName, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vSubmit") String vSubmit) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							UpdateData.updateADRDetail(httpServletRequest, getCono, getDivi, vADRNumber, vPrefix,
									vItemLine, vItemNo, vSBNO, vItemCostcenter, vItemDate, vAssetCost, vNetValue,
									vItemQty, vItemPrice, vItemRemark, fileInputStream,
									ConvertStringtoObject.convertNullToObject(vImageName), vStatus, vSubmit, username),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/adrimage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateADRImage(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vADRNumber") String vADRNumber, @FormDataParam("vImageFile") InputStream fileInputStream,
			@FormDataParam("vImageFile") FormDataContentDisposition fileFormDataContentDisposition,
			@FormDataParam("vImageName") String vImageName) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							UpdateData.updateADRImage(httpServletRequest, getCono, getDivi, vADRNumber, fileInputStream,
									ConvertStringtoObject.convertNullToObject(vImageName), username),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/adrhead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateADRHead(@Context HttpHeaders headers, @FormDataParam("vADRNumber") String vADRNumber,
			@FormDataParam("vDate") String vDate, @FormDataParam("vMonth") String vMonth,
			@FormDataParam("vType") String vType, @FormDataParam("vPrefix") String vPrefix,
			@FormDataParam("vBOI") String vBOI, @FormDataParam("vBU") String vBU,
			@FormDataParam("vCostcenter") String vCostcenter, @FormDataParam("vVat") String vVat,
			@FormDataParam("vAccountant") String vAccountant, @FormDataParam("vRequestor") String vRequestor,
			@FormDataParam("vRemark") String vRemark, @FormDataParam("vApprove1") String vApprove1,
			@FormDataParam("vApprove2") String vApprove2, @FormDataParam("vApprove3") String vApprove3,
			@FormDataParam("vApprove4") String vApprove4, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vSubmit") String vSubmit) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							UpdateData.updateADRHead(getCono, getDivi, vADRNumber, vDate, vMonth, vType, vPrefix, vBOI,
									vBU, vCostcenter, vVat, vAccountant, vRequestor, vRemark, vApprove1, vApprove2,
									vApprove3, vApprove4, vStatus, vSubmit, username),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/statusadrhead/{adrno}/{status}/{submit}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateStatusADRHead(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("adrno") String adrno, @PathParam("status") String status, @PathParam("submit") String submit,
			String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];

				try {
					return Response.ok(UpdateData.updateStatusADRHead(httpServletRequest, getCono, getDivi, adrno,
							status, submit, username), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/rejectadrhead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") ADACRE1, ADAPRE1,
	// ADAPRE2, ADAPRE3, ADAPRE4
	public Response updateRejectADRHead(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vADRNumber") String vADRNumber, @FormDataParam("vRemark") String vRemark,
			@FormDataParam("vAccRemark") String vAccRemark, @FormDataParam("vAppRemark1") String vAppRemark1,
			@FormDataParam("vAppRemark2") String vAppRemark2, @FormDataParam("vAppRemark3") String vAppRemark3,
			@FormDataParam("vAppRemark4") String vAppRemark4, @FormDataParam("vStatus") String vStatus,
			@FormDataParam("vSubmit") String vSubmit) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];
				// String getCono = "10";
				// String getDivi = "101";

				try {

					return Response.ok(
							UpdateData.updateRejectADRHead(httpServletRequest, getCono, getDivi, vADRNumber, vRemark,
									vAccRemark, ConvertStringtoObject.convertNullToObject(vAppRemark1),
									ConvertStringtoObject.convertNullToObject(vAppRemark2),
									ConvertStringtoObject.convertNullToObject(vAppRemark3),
									ConvertStringtoObject.convertNullToObject(vAppRemark4), vStatus, vSubmit, username),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@DELETE
	@Path("/adrdetail/{adrno}/{line}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteADRDetail(@Context HttpHeaders headers, @PathParam("adrno") String adrno,
			@PathParam("line") String line) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		Jws<Claims> validate = null;

		if (getToken != null) {

			try {

				// jwt verify token
				validate = JwtManager.parseToken(getToken);
				// System.out.println("validate: " + validate);
				String username = validate.getBody().get("aud", String.class);
				String company = validate.getBody().get("sub", String.class);
				String getCompany[] = company.split(" : ");
				String getCono = getCompany[0];
				String getDivi = getCompany[1];
				String getCompanyName = getCompany[2];

				try {

					return Response.ok(DeleteData.deleteADRDetail(getCono, getDivi, adrno, line),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e);

				}

			} catch (SignatureException e) {
				mJsonObj.put("auth", "false");
				mJsonObj.put("message", e.getMessage());

			}

		} else {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", "No token provided");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	/// WITH OUT TOKEN

	@GET
	@Path("/adrheadwotoken/{cono}/{divi}/{adrno}/{fromstatus}/{tostatus}/{approve}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getADRHeadWOToken(@Context HttpHeaders headers, @PathParam("cono") String cono,
			@PathParam("divi") String divi, @PathParam("adrno") String adrno,
			@PathParam("fromstatus") String fromstatus, @PathParam("tostatus") String tostatus,
			@PathParam("approve") String approve, String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();

		try {

			try {

				return Response.ok(SelectData.getADRHeadWOToken(cono, divi, adrno, fromstatus, tostatus, approve),
						MediaType.APPLICATION_JSON + ";charset=utf8").build();

			} catch (Exception e) {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", e);

			}

		} catch (SignatureException e) {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/adrdetailwotoken/{cono}/{divi}/{adrno}/{status}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getADRDetailWOToken(@Context HttpHeaders headers, @PathParam("cono") String cono,
			@PathParam("divi") String divi, @PathParam("adrno") String adrno, @PathParam("status") String status,
			String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();

		try {

			try {

				return Response.ok(SelectData.getADRDetailWOToken(cono, divi, adrno, status),
						MediaType.APPLICATION_JSON + ";charset=utf8").build();

			} catch (Exception e) {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", e);

			}

		} catch (SignatureException e) {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/statusadrheadwotoken/{cono}/{divi}/{adrno}/{status}/{submit}/{username}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateStatusADRHeadWOToken(@Context HttpHeaders headers,
			@Context HttpServletRequest httpServletRequest, @PathParam("cono") String cono,
			@PathParam("divi") String divi, @PathParam("adrno") String adrno, @PathParam("status") String status,
			@PathParam("submit") String submit, @PathParam("username") String username, String req)
			throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		try {

			System.out.println(cono + " " + divi + " " + adrno + " " + status + " " + submit + " " + username);

			try {
				return Response.ok(
						UpdateData.updateStatusADRHead(httpServletRequest, cono, divi, adrno, status, submit, username),
						MediaType.APPLICATION_JSON + ";charset=utf8").build();

			} catch (Exception e) {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", e);

			}

		} catch (SignatureException e) {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@PUT
	@Path("/rejectadrheadwotoken")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") ADACRE1, ADAPRE1,
	// ADAPRE2, ADAPRE3, ADAPRE4
	public Response updateRejectADRHeadWOToken(@Context HttpHeaders headers,
			@Context HttpServletRequest httpServletRequest, @FormDataParam("vCono") String vCono,
			@FormDataParam("vDivi") String vDivi, @FormDataParam("vADRNumber") String vADRNumber,
			@FormDataParam("vRemark") String vRemark, @FormDataParam("vAccRemark") String vAccRemark,
			@FormDataParam("vAppRemark1") String vAppRemark1, @FormDataParam("vAppRemark2") String vAppRemark2,
			@FormDataParam("vAppRemark3") String vAppRemark3, @FormDataParam("vAppRemark4") String vAppRemark4,
			@FormDataParam("vStatus") String vStatus, @FormDataParam("vSubmit") String vSubmit,
			@FormDataParam("vUsername") String vUsername) throws JSONException {

		JSONObject mJsonObj = new JSONObject();

		try {

			try {

				return Response.ok(
						UpdateData.updateRejectADRHead(httpServletRequest, vCono, vDivi, vADRNumber, vRemark,
								vAccRemark, ConvertStringtoObject.convertNullToObject(vAppRemark1),
								ConvertStringtoObject.convertNullToObject(vAppRemark2),
								ConvertStringtoObject.convertNullToObject(vAppRemark3),
								ConvertStringtoObject.convertNullToObject(vAppRemark4), vStatus, vSubmit, vUsername),
						MediaType.APPLICATION_JSON + ";charset=utf8").build();

			} catch (Exception e) {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", e);

			}

		} catch (SignatureException e) {
			mJsonObj.put("auth", "false");
			mJsonObj.put("message", e.getMessage());

		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/addreceipt")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addreceipt(@FormDataParam("rcno") String rcno, @FormDataParam("voucher") String voucher,
			@FormDataParam("fixno") String fixno) throws JSONException {
		logger.info("/staffcode");
		JSONObject mJsonObj = new JSONObject();

		try {
			return Response
					.ok(InsertData.addreceipt(rcno, voucher, fixno), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	///////////////////// VSITORHEADER //////////////

//todo
	
	

	@POST
	@Path("insertheadervisitor")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response addheadervisitor(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, @FormDataParam("vCONO") String vCONO,@FormDataParam("vDIVI") String vDIVI,@FormDataParam("vLocation") String vLocation)
			throws JSONException {
		logger.info("/addheadervisitor");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(InsertData.addvisitorheader(vCONO,vDIVI,vLocation), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	
	//////////// SENDEMAIL///////////////
	
	
	
	

	@POST
	@Path("/sendemailpp/{prefix}/{ordno}/{status}/{submit}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response sendemailpp(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("prefix") String prefix, @PathParam("ordno") String ordno, @PathParam("status") String status,
			@PathParam("submit") String submit,
			String req) throws JSONException {
		logger.info("/sendemailpp/{prefix}/{ordno}/{status}/{submit}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		 System.out.println("mailllllllllllllllllllllllllllllllllllllllllllllllllll");

		//if (getToken != null && !getToken.isEmpty()) {
			//String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
			// System.out.println("getTokenData: " + getTokenData);


				String getCono = "10";
				String getDivi = "101";
				String getCompanyName = "BR";
				String getUsername = "xxx";
				String getAuth = "xxxx"; 
				String getLocation = "11";

				try {

					return Response.ok(
							SendEmail.prepareResendEmail(httpServletRequest, getCono, getDivi, prefix, ordno, status,
									status, submit, getUsername, getToken , getLocation),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	

	

	
	@POST
	@Path("/sendemailxx/{prefix}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response sendemailxx(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("prefix") String prefix, 
		
			String req) throws JSONException {

		System.out.print("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

		JSONObject mJsonObj = new JSONObject();
		String getToken = "xxx";
		 System.out.println("mail no auth");

		//if (getToken != null && !getToken.isEmpty()) {
			//String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
			// System.out.println("getTokenData: " + getTokenData);

				try {


					
					return Response.ok(
							SendEmail.prepareResendxx(httpServletRequest, prefix),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	@POST
	@Path("/sendemailppwithoutauthen/{prefix}/{ordno}/{status}/{submit}/{cono}/{divi}/{location}/{vMeetdate}/{vMeettime}/{vName}/{vSurname}/{vROOMNO}/{vRemark}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response sendemailppwithoutauthen(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("prefix") String prefix, 
			@PathParam("ordno") String ordno, 
			@PathParam("status") String status,
			@PathParam("submit") String submit,
			@PathParam("cono") String cono,
			@PathParam("divi") String divi,
			@PathParam("location") String location,
			@PathParam("vMeetdate") String vMeetdate,
			@PathParam("vMeettime") String vMeettime,
			@PathParam("vName") String vName,
			@PathParam("vSurname") String vSurname,
			@PathParam("vROOMNO") String vROOMNO,
			@PathParam("vRemark") String vRemark,
			String req) throws JSONException {

		System.out.print("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

		JSONObject mJsonObj = new JSONObject();
		String getToken = "xxx";
		 System.out.println("mail no auth");

		//if (getToken != null && !getToken.isEmpty()) {
			//String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
			// System.out.println("getTokenData: " + getTokenData);


				String getCono = cono;
				String getDivi = divi;
				String getCompanyName = "BR";
				String getUsername = "xxx";
				String getAuth = "xxxx"; 
				String getLocation = location;

				try {


					
					return Response.ok(
							SendEmail.prepareResendEmailSHOW(httpServletRequest, getCono, getDivi, prefix, ordno, status,
									status, submit, getUsername, getToken , getLocation,vMeetdate,vMeettime,vName,vSurname,vROOMNO,vRemark),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	//////////////////////////////////////////////// BANKMAPPING //////////////////////////////////////////////
	/*
	
	public static class StatementData {
        public Object tableData;  
        public String statemenType;

        // Constructor, getters, setters (Optional)
    }

    @POST
	@Path("/uploadstatement")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")

    public Response uploadStatement(@Context HttpHeaders headers, StatementData statementData) throws JSONException {
        String statemenType = statementData.statemenType;
        Object tableData = statementData.tableData;

        System.out.print("---------------------------------------------------------------------");
        System.out.print(tableData);
        System.out.print(statemenType);
        System.out.print("---------------------------------------------------------------------");

        return Response.ok().entity("Upload successful").build();
    }
    
    */
	
	
	
	
	@GET
	@Path("/getivoiceid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getivoiceid(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/getivoiceid");

		JSONObject mJsonObj = new JSONObject();
		// System.out.println("getToken: " + getToken);

		//String getCono = "10";
		//String getDivi = "101";
		
		
        
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];

		
		

				try {
					return Response
							.ok(SelectData.getivoiceid(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

	

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	
	
	
	
	@GET
	@Path("/getpayer")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getpayer(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/getpayer");

		JSONObject mJsonObj = new JSONObject();
		// System.out.println("getToken: " + getToken);

		//String getCono = "10";
		//String getDivi = "101";

        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						//JSONObject getDataObject = dataObject.getJSONObject("body");
						JSONObject getDataObject = new JSONObject(dataObject.getString("body"));
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
		
						
		
		
		

				try {
					return Response
							.ok(SelectData.getpayer(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

	

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	//////////////////////
	
	
	@GET
	@Path("/getreport/{reportName}/{fromdate}/{todate}/{type}/{location}")
    @Produces("application/pdf")
    public Response getreport(@PathParam("reportName") String reportName, 
    		@PathParam("fromdate") String fromdate,
    		@PathParam("todate") String todate,
                                   @PathParam("type") String type,
                                   @PathParam("location") String location

    		) {
        try {
           // String jasperPath = "/path/to/reports/" + reportName + ".jasper";
            String jasperPath = "/WebContent/WEB-INF/report/BM_NEW.jasper";
            

            File jasperFile = new File(jasperPath);
            if (!jasperFile.exists()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Report template not found").build();
            }

            List<Map<String, Object>> data = new ArrayList<>();
            Map<String, Object> sampleData = new HashMap<>();
            sampleData.put("fromdate", fromdate);
            sampleData.put("todate", todate);
            sampleData.put("type", type);
            sampleData.put("location", location);

            data.add(sampleData);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("com", "10");
            parameters.put("mono", "10");
            parameters.put("to", "20241101");
            parameters.put("from", "20241125");
            parameters.put("Type", "TRANSFER");
            parameters.put("Location", "LS");
            parameters.put("parameterA", "TRANSFER");
            parameters.put("parameterB", "TRANSFER");
            parameters.put("FGLOACATION", "11");
            parameters.put("RV", "RVC-TRN");




            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parameters, dataSource);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            byte[] pdfData = outputStream.toByteArray();

            return Response.ok(pdfData)
                    .header("Content-Disposition", "inline; filename=" + reportName + ".pdf")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error generating report: " + e.getMessage()).build();
        }
	}
	

	
	
	
	
	
	/*

	@GET
	@Path("/getreport/{fromdate}/{todate}/{type}/{location}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getreport(@Context HttpServletRequest httpServletRequest,@Context HttpHeaders headers,
			@PathParam("fromdate") 	String fromdate , 
			@PathParam("todate") 	String todate , 
			@PathParam("type") 	String type , 
			@PathParam("location") 	String location 
) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");

        System.out.println(fromdate);
        System.out.println(todate);
        System.out.println(type);
        System.out.println(location);

        System.out.println("xxxx555---------------------------------------------------------------------");
        
        
        
        
        
      
        
        JSONObject mJsonObj = new JSONObject();
        
    		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

    		
			System.out.println(getToken);
			System.out.println("xzzzz");

    		
    		if (getToken != null && !getToken.isEmpty()) {
    			

    		
    			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
    			
    			
    			System.out.println("ttttttt : " + getTokenData);

    			


    			JSONObject dataObject = new JSONObject(getTokenData);
    			


    			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
    			

    			if (checkToken) {
    				JSONObject getDataObject = dataObject.getJSONObject("body");
    		
    				String getUsername = getDataObject.getString("aud");

        try {

        	System.out.print(getUsername);
		
			return Response.ok(
					SelectData.getuser(getUsername),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
    			}}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
		
		 

    }
	
*/ 
	
	
	
	
	
	
	
	
	
	
	/////////////////////////////////
	


	@GET
	@Path("/getuser")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getuser(@Context HttpServletRequest httpServletRequest,@Context HttpHeaders headers
) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");


        System.out.println("xxxx555---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
    		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

    		
			System.out.println(getToken);
			System.out.println("xzzzz");

    		
    		if (getToken != null && !getToken.isEmpty()) {
    			

    		
    			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
    			
    			
    			System.out.println("ttttttt : " + getTokenData);

    			


    			JSONObject dataObject = new JSONObject(getTokenData);
    			


    			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
    			

    			if (checkToken) {
    				JSONObject getDataObject = dataObject.getJSONObject("body");
    		
    				String getUsername = getDataObject.getString("aud");

        try {

        	System.out.print(getUsername);
		
			return Response.ok(
					SelectData.getuser(getUsername),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
    			}}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	
	
	
	

	@GET
	@Path("/getuserdel")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getuserdel(@Context HttpServletRequest httpServletRequest,@Context HttpHeaders headers
) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");


        System.out.println("xxxx555---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
    		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

    		
			System.out.println(getToken);
			System.out.println("xzzzz");

    		
    		if (getToken != null && !getToken.isEmpty()) {
    			

    		
    			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
    			
    			
    			System.out.println("ttttttt : " + getTokenData);

    			


    			JSONObject dataObject = new JSONObject(getTokenData);
    			


    			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
    			

    			if (checkToken) {
    				JSONObject getDataObject = dataObject.getJSONObject("body");
    		
    				String getUsername = getDataObject.getString("aud");

        try {

        	System.out.print(getUsername);
		
			return Response.ok(
					SelectData.getuserdel(getUsername),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
    			}}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	

	@GET
	@Path("/getusermas")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getusermas(@Context HttpServletRequest httpServletRequest,@Context HttpHeaders headers
) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");


        System.out.println("xxxx555---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
    		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

    		
			System.out.println(getToken);
			System.out.println("xzzzz");

    		
    		if (getToken != null && !getToken.isEmpty()) {
    			

    		
    			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
    			
    			
    			System.out.println("ttttttt : " + getTokenData);

    			


    			JSONObject dataObject = new JSONObject(getTokenData);
    			


    			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
    			

    			if (checkToken) {
    				JSONObject getDataObject = dataObject.getJSONObject("body");
    		
    				String getUsername = getDataObject.getString("aud");

        try {

        	System.out.print(getUsername);
		
			return Response.ok(
					SelectData.getusermas(getUsername),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
    			}}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	
	
	
	////////////////////////
	
	


	@GET
	@Path("/getitem/{statemenDate}/{statemenType}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getitem(@Context HttpServletRequest httpServletRequest, @PathParam("statemenDate") String statemenDate, @PathParam("statemenType") String statemenType,@Context HttpHeaders headers, String req) throws JSONException {

		
		//todo addcono

        System.out.println("xxxx---------------------------------------------------------------------");

        System.out.println("statemenDate: " + statemenDate);
        System.out.println("statemenType: " + statemenType);
        System.out.println("xxxx---------------------------------------------------------------------");
        
        
        JSONObject mJsonObj = new JSONObject();
      		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

      //todo user
      		
      		
      			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);

      			JSONObject dataObject = new JSONObject(getTokenData);
      			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
      			
      			System.out.print(checkToken);

      				
      				
      					JSONObject getDataObject = dataObject.getJSONObject("body");
      					String[] getSubject = getDataObject.getString("sub").split(" : ");
      					String getCono = getSubject[0];
      					String getDivi = getSubject[1];
      					String getUsername = getDataObject.getString("aud");

        try {

        	
		
			return Response.ok(
					SelectData.CHECKTYPEGETITEM(httpServletRequest,statemenDate,statemenType, getCono , getDivi),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	

	


	@GET
	@Path("/getitembankmapping/{statemenDate}/{statemenType}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getitembankmapping(@Context HttpServletRequest httpServletRequest, @PathParam("statemenDate") String statemenDate, @PathParam("statemenType") String statemenType,@Context HttpHeaders headers, String req) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");

        System.out.println("statemenType: " + statemenType);
        System.out.println("statemenDate: " + statemenDate);
        System.out.println("xxxx---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();

        try {

        	
		
			return Response.ok(
					SelectData.GETITEMBANKMAPPING(httpServletRequest,statemenDate,statemenType),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	
	
	
	@GET
	@Path("/getitemlrc/{selectedLRC}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getitemlrc(@Context HttpServletRequest httpServletRequest, @PathParam("selectedLRC") String selectedLRC,@Context HttpHeaders headers, String req) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");

        System.out.println("statemenID: " + selectedLRC);
        System.out.println("xxxx---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();

        try {

        	
		
			return Response.ok(
					SelectData.GETITEMLRC(httpServletRequest,selectedLRC),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	
	@GET
	@Path("/getitemrc/{selectedRC}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getitemrc(@Context HttpServletRequest httpServletRequest, @PathParam("selectedRC") String selectedRC,@Context HttpHeaders headers, String req) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");

        System.out.println("statemenID: " + selectedRC);
        System.out.println("xxxx---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();

        try {

        	
		
			return Response.ok(
					SelectData.GETITEMRC(httpServletRequest,selectedRC),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	@GET
	@Path("/getitemhead/{selectedID}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getitemhead(@Context HttpServletRequest httpServletRequest, @PathParam("selectedID") String selectedID,@Context HttpHeaders headers, String req) throws JSONException {


        System.out.println("xxxx---------------------------------------------------------------------");

        System.out.println("statemenID: " + selectedID);
        System.out.println("xxxx---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();

        try {

        	
		
			return Response.ok(
					SelectData.GETITEMHEAD(httpServletRequest,selectedID),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	
	
	
	
	

	@PUT
    @Path("/deleteidmove")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deleteidmove(
    		@Context HttpHeaders headers,
            @FormDataParam("ID") String ID, 
            @FormDataParam("GROUP_ID") String GROUP_ID
         
            


    		) throws JSONException {

        System.out.println("---------------------------------------------------------------------");

        System.out.println("ID : " + ID);
        System.out.println("GROUP_ID : " + GROUP_ID);

        
        
        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						



        try {
        	
        
        		return Response.ok(
    					UpdateData.deleteidmove(getCono, ID,GROUP_ID),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }


	@PUT
    @Path("/deleteid")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deleteid(
    		@Context HttpHeaders headers,
            @FormDataParam("ID") String ID, 
            @FormDataParam("BM_CONO") String BM_CONO, 
            @FormDataParam("BM_PARENT") String BM_PARENT,
            @FormDataParam("GROUP_ID") String GROUP_ID,
            @FormDataParam("H_RNNO") String H_RNNO,
            @FormDataParam("CHECKTYPE") String CHECKTYPE
            


    		) throws JSONException {

        System.out.println("---------------------------------------------------------------------");

        System.out.println("ID : " + ID);
        System.out.println("BM_CONO : " + BM_CONO);
        System.out.println("BM_PARENT : " + BM_PARENT);
        System.out.println("GROUP_ID : " + GROUP_ID);
        System.out.println("H_RNNO : " + H_RNNO);
        System.out.println("CHECKTYPE : " + CHECKTYPE);

        
        
        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						



        try {
        	
        
        		return Response.ok(
    					UpdateData.deleteid(getCono,getDivi, ID,BM_CONO,BM_PARENT,GROUP_ID,H_RNNO,CHECKTYPE),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	////////////////////////  master //////////////////////////
	
	
	
	//// HEAD 
	
	
	
	@PUT
    @Path("/savehead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response savehead(
            @FormDataParam("H_CONO") String H_CONO, 
            @FormDataParam("H_DIVI") String H_DIVI, 
            @FormDataParam("H_RNNO") String H_RNNO, 
            @FormDataParam("H_RCNO") String H_RCNO, 
            @FormDataParam("H_CUNO") String H_CUNO, 
            @FormDataParam("H_PYNO") String H_PYNO, 
            @FormDataParam("H_STS") String H_STS, 
            @FormDataParam("H_VCNO") String H_VCNO, 
            @FormDataParam("H_LOCATION") String H_LOCATION, 
            @FormDataParam("H_TYPE") String H_TYPE

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.savehead(H_CONO,H_DIVI,H_RNNO,H_RCNO,H_CUNO,H_PYNO,H_STS,H_VCNO,H_LOCATION,H_TYPE),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	
/*
	
	
	@GET
	@Path("/getwf")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getWF(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/company");

		JSONObject mJsonObj = new JSONObject();
		//String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.getWF(), MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	

	//https://210.1.14.22:8444/bank_mapping_api/data/getcurrentID
	@GET
	@Path("/getcurrentID")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getcurrentID(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/getpayer");

		JSONObject mJsonObj = new JSONObject();
		// System.out.println("getToken: " + getToken);

		//String getCono = "10";
		//String getDivi = "101";

        
      //  String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	  //  String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);

/*

					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
		
		
		
		

				try {
					return Response
							.ok(SelectData.getcurrentID(), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

	

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	

	//https://210.1.14.22:8444/bank_mapping_api/data/getcurrentID
	@GET
	@Path("/getSTATUSID/{vID}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSTATUSID(@Context HttpHeaders headers, String req, @PathParam("vID") String vID) throws JSONException {
		logger.info("/getSTATUSID");

		JSONObject mJsonObj = new JSONObject();
		
		

				try {
					return Response
							.ok(SelectData.getSTATUSID(vID), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

	

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	*/
	
	/////////////////////////////////////////////////////////// REAL WF 
	
	
	
	

	@POST
	@Path("/updateITEMREQUEST")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response updateITEMREQUEST(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,@FormDataParam("vID") String vID,@FormDataParam("vSTATUS") String vSTATUS, @FormDataParam("vData") String vData , @FormDataParam("vApproval") String vApproval,@FormDataParam("vApprover") String vApprover ,@FormDataParam("vDepthrad") String vDepthrad )
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println(vSTATUS);
		System.out.println(vID);
		System.out.println(vApproval);
		System.out.println(vData);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(UpdateData.updateITEMREQUEST(vID,vSTATUS,vData,
							vApproval,vApprover,vDepthrad), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
		
		
		

	}
	
	


	@POST
	@Path("/insertTEST")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response insertTEST(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, @FormDataParam("vData") String vData,@FormDataParam("username") String username,@FormDataParam("depthead") String depthead)
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(InsertData.insertTEST(vData,username,depthead), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
		
		
		

	}
	
	
	

	@GET
	@Path("/getSTATUSID/{vID}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSTATUSIDITEMRQ(@Context HttpHeaders headers, String req, @PathParam("vID") String vID) throws JSONException {
		logger.info("/getSTATUSID");

		JSONObject mJsonObj = new JSONObject();
		
		

				try {
					return Response
							.ok(SelectData.getSTATUSIDITEMRQ(vID), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

	

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	/////////////////////////////////////////////////////////// REAL WF 

	
/*	
	

	@POST
	@Path("/insertRQ")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response insertRQ(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, @FormDataParam("vID") String vID, @FormDataParam("vCOM") String vCOM,@FormDataParam("vWH") String vWH,@FormDataParam("vITM") String vITM,@FormDataParam("vAPV") String vAPV)
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(InsertData.insertRQ(vID,vCOM,vWH,vITM,vAPV), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
		
		
		

	}
	
	
	
	

	@POST
	@Path("/UpdateIT")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response UpdateIT(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, @FormDataParam("vID") String vID, @FormDataParam("vITAPVD") String vITAPVD,@FormDataParam("vCOMITAPVD") String vCOMITAPVD)
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(UpdateData.UpdateIT(vID,vITAPVD,vCOMITAPVD), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	
	
	
	
	

	@POST
	@Path("/UpdateRQ")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response UpdateRQ(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, @FormDataParam("vID") String vID, @FormDataParam("vCOM") String vCOM,@FormDataParam("vWH") String vWH,@FormDataParam("vITM") String vITM,@FormDataParam("vAPV") String vAPV)
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(UpdateData.UpdateRQ(vID,vCOM,vWH,vITM,vAPV), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	@POST
	@Path("/UpdateAP")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response UpdateAP(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, @FormDataParam("vID") String vID, @FormDataParam("vAPVD") String vAPVD,@FormDataParam("vCOMAPVD") String vCOMAPVD)
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(UpdateData.UpdateAP(vID,vAPVD,vCOMAPVD), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	*/
	

	@POST
	@Path("/testwf")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	
	public Response testwf(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest, @FormDataParam("vCONO") String vCONO)
			throws JSONException {
		logger.info("/addheadervisitor");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		

		
	

				try {

					return Response.ok(InsertData.testwf(vCONO), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	

	
	@PUT
    @Path("/deletehead")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deletehead(
            @FormDataParam("H_CONO") String H_CONO, 
            @FormDataParam("H_DIVI") String H_DIVI, 
            @FormDataParam("H_RCNO") String H_RCNO

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.deletehead(H_CONO,H_DIVI,H_RCNO),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	//// MAPPING  
	
	

	@PUT
    @Path("/savemapping")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response savehead(
            @FormDataParam("BM_ID") String BM_ID, 
            @FormDataParam("BM_CONO") String BM_CONO, 
            @FormDataParam("BM_DIVI") String BM_DIVI, 
            @FormDataParam("BM_BANK") String BM_BANK, 
            @FormDataParam("BM_ACCODE") String BM_ACCODE, 
            @FormDataParam("BM_DATE") String BM_DATE, 
            @FormDataParam("BM_TIME") String BM_TIME, 
            @FormDataParam("BM_CHQNO") String BM_CHQNO, 
            @FormDataParam("BM_CUNO") String BM_CUNO, 
            @FormDataParam("BM_AMOUNT") String BM_AMOUNT, 
            @FormDataParam("BM_DESC") String BM_DESC, 
            @FormDataParam("BM_RCNO") String BM_RCNO, 
            @FormDataParam("BM_USER") String BM_USER, 
            @FormDataParam("BM_BKCHARGE") String BM_BKCHARGE, 
            @FormDataParam("BM_OVPAY") String BM_OVPAY, 
            @FormDataParam("BM_CNDN") String BM_CNDN, 
            @FormDataParam("BM_STATUS") String BM_STATUS, 
            @FormDataParam("BM_FNNO") String BM_FNNO,
            @FormDataParam("BM_LCODE") String BM_LCODE,
            @FormDataParam("BM_PARENT_ID") String BM_PARENT_ID,
            @FormDataParam("BM_PARENT_STS") String BM_PARENT_STS

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.savemapping(BM_ID,BM_CONO,BM_DIVI,BM_BANK,BM_ACCODE,BM_DATE,BM_TIME,BM_CHQNO,BM_CUNO,BM_AMOUNT,BM_DESC,BM_RCNO,BM_USER,BM_BKCHARGE,BM_OVPAY,BM_CNDN,BM_STATUS,BM_FNNO,BM_LCODE,BM_PARENT_ID,BM_PARENT_STS),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	

	@PUT
    @Path("/deletemapping")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deletemapping(
            @FormDataParam("BM_ID") String BM_ID

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.deletemapping(BM_ID),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	
	
	
	
	
	
	//// HCRC
	
	
	
	@PUT
    @Path("/savehcrc")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response savehead(
            @FormDataParam("HR_CONO") String HR_CONO, 
            @FormDataParam("HR_DIVI") String HR_DIVI, 
            @FormDataParam("HC_RCNO") String HC_RCNO, 
            @FormDataParam("HC_TRDT") String HC_TRDT, 
            @FormDataParam("HC_PYNO") String HC_PYNO, 
            @FormDataParam("HC_REAMT") String HC_REAMT, 
            @FormDataParam("HC_TYPE") String HC_TYPE, 
            @FormDataParam("HC_BANK") String HC_BANK, 
            @FormDataParam("HC_ACCODE") String HC_ACCODE, 
            @FormDataParam("HC_PYDT") String HC_PYDT, 
            @FormDataParam("HC_CHKNO") String HC_CHKNO, 
            @FormDataParam("HC_USER") String HC_USER, 
            @FormDataParam("HC_VCNO") String HC_VCNO, 
            @FormDataParam("HC_STS") String HC_STS, 
            @FormDataParam("HR_BKCHG") String HR_BKCHG, 
            @FormDataParam("HC_LOCATION") String HC_LOCATION, 
            @FormDataParam("HC_FIXNO") String HC_FIXNO, 
            @FormDataParam("HC_FNNO") String HC_FNNO

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.savehcrc(HR_CONO,HR_DIVI,HC_RCNO,HC_TRDT,HC_PYNO,HC_REAMT,HC_TYPE,HC_BANK,HC_ACCODE,HC_PYDT,HC_CHKNO,HC_USER,HC_VCNO,HC_STS,HR_BKCHG,HC_LOCATION,HC_FIXNO,HC_FNNO),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	

	@PUT
    @Path("/deletehcrc")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deletehcrc(
            @FormDataParam("HR_CONO") String HR_CONO,
            @FormDataParam("HR_DIVI") String HR_DIVI,
            @FormDataParam("HC_RCNO") String HC_RCNO

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.deletehcrc(HR_CONO,HR_DIVI,HC_RCNO),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	
	
	/////// LNRC 
	
	
	
	
	
	@PUT
    @Path("/savelnrc")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response savelnrc(
            @FormDataParam("LR_CONO") String LR_CONO, 
            @FormDataParam("LR_DIVI") String LR_DIVI, 
            @FormDataParam("LR_RCNO") String LR_RCNO, 
            @FormDataParam("LR_INVNO") String LR_INVNO, 
            @FormDataParam("LR_INVDT") String LR_INVDT, 
            @FormDataParam("LR_INVAMT") String LR_INVAMT, 
            @FormDataParam("LR_REAMT") String LR_REAMT, 
            @FormDataParam("LR_STS") String LR_STS 

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.savelnrc(LR_CONO,LR_DIVI,LR_RCNO,LR_INVNO,LR_INVDT,LR_INVAMT,LR_REAMT,LR_STS),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
		
	

	
	

	@PUT
    @Path("/deletelnrc")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deletelnrc(
            @FormDataParam("LR_CONO") String LR_CONO,
            @FormDataParam("LR_DIVI") String LR_DIVI,
            @FormDataParam("LR_RCNO") String LR_RCNO

    		) throws JSONException {

      
        JSONObject mJsonObj = new JSONObject();

        try {
        	
        
        		return Response.ok(
    					UpdateData.deletelnrc(LR_CONO,LR_DIVI,LR_RCNO),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }

	
	
	
	
	
	

	
	
	@GET
	@Path("/checkinv/{IVS}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response checkinv(@Context HttpHeaders headers,@PathParam("IVS") String IVS  , String req) throws JSONException {
		logger.info("/checkinv");

		JSONObject mJsonObj = new JSONObject();
		//String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.checkivs(IVS), MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();


	}

	


	@PUT
    @Path("/addstatement")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addstatement(
    		@Context HttpHeaders headers,
            @FormDataParam("BANK") String BANK,
            @FormDataParam("BANKCODE") String BANKCODE,
            @FormDataParam("DATE1") String DATE1,
            @FormDataParam("TIME") String TIME,
            @FormDataParam("AMOUNT") String AMOUNT,
            @FormDataParam("DESC") String DESC,
            @FormDataParam("REFCUS") String REFCUS,
            @FormDataParam("LCODE") String LCODE,
            @FormDataParam("CUSTOMERNAME") String CUSTOMERNAME
          

    		) throws JSONException {

        System.out.println("--------------------------------save-------------------------------------");

       // System.out.println("MONTH : " + MONTH);
    

        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						


        try {
        	
        
        		return Response.ok(
    					UpdateData.addstatement(getCono,getDivi,BANK,BANKCODE,DATE1,TIME,AMOUNT,DESC,REFCUS,LCODE,CUSTOMERNAME),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	

	
	

	@PUT
    @Path("/unlockinv")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addstatement(
    		@Context HttpHeaders headers,
            @FormDataParam("INV") String INV
           
          

    		) throws JSONException {

        System.out.println("--------------------------------save-------------------------------------");

       // System.out.println("MONTH : " + MONTH);
    

        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						


        try {
        	
        
        		return Response.ok(
    					UpdateData.unlockinv(getCono,getDivi,INV),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	

	
	
	

	@PUT
    @Path("/setlcode")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response setlcode(
    		@Context HttpHeaders headers,
            @FormDataParam("MONTH") String MONTH
          

    		) throws JSONException {

        System.out.println("--------------------------------save-------------------------------------");

        System.out.println("MONTH : " + MONTH);
    

        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						

        try {
        	
        
        		return Response.ok(
    					UpdateData.setlcode(getCono,MONTH),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	
	
	
	

	@PUT
    @Path("/returninvoice")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response returninvoice(
    		@Context HttpHeaders headers,
            @FormDataParam("ID") String ID
          

    		) throws JSONException {

        System.out.println("--------------------------------save-------------------------------------");

        System.out.println("ID : " + ID);
    

        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						

        try {
        	
        
        		return Response.ok(
    					UpdateData.returninvoice(getCono,ID),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	

	@PUT
    @Path("/setpost")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response setpost(
    		@Context HttpHeaders headers,
            @FormDataParam("ID") String ID
          

    		) throws JSONException {

        System.out.println("--------------------------------save-------------------------------------");

        System.out.println("ID : " + ID);
    

        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						


        try {
        	
        
        		return Response.ok(
    					UpdateData.setpost(getCono,getDivi,ID),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	

	
	
	
	
	
	////////////////////////////////////////////////////////////////


	@PUT
    @Path("/saveid")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveid(
    		@Context HttpHeaders headers,
            @FormDataParam("ID") String ID, 
            @FormDataParam("BM_CONO") String BM_CONO, 
            @FormDataParam("PAYER") String PAYER, 
            @FormDataParam("OVERPAY") String OVERPAY, 
            @FormDataParam("CNDN") String CNDN, 
            @FormDataParam("ACCNO") String ACCNO, 
            @FormDataParam("TYPE") String TYPE, 
            @FormDataParam("BANKCHARGE") String BANKCHARGE ,
            @FormDataParam("CHECKTYPE") String CHECKTYPE,
            @FormDataParam("GROUPID") String GROUPID,
            @FormDataParam("LOCATION") String LOCATION


            



            


    		) throws JSONException {

        System.out.print("--------------------------------save-------------------------------------");

        System.out.println("ID : " + ID);
        System.out.println("PAYER : " + PAYER);
        System.out.println("BANKCHARGE : " + BANKCHARGE);
        System.out.println("OVERPAY : " + OVERPAY);
        System.out.println("CNDN : " + CNDN);
        System.out.println("TYPE : " + TYPE);
        System.out.println("GROUPID : " + GROUPID);
        System.out.println("TYPE : " + TYPE);

        System.out.println("LOCATION : " + LOCATION);


        
        
        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						



        try {

        
        		return Response.ok(
    					UpdateData.saveid(getCono,getDivi,ID,PAYER,BANKCHARGE,OVERPAY,CNDN,TYPE,GROUPID,LOCATION),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
       

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	

	@PUT
    @Path("/updateonly")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateonly(
    		@Context HttpHeaders headers,
            @FormDataParam("PAYER") String PAYER, 
            @FormDataParam("ID") String ID, 
            @FormDataParam("BANKCHARGE") String BANKCHARGE, 
            @FormDataParam("OVERPAY") String OVERPAY, 
            @FormDataParam("CNDN") String CNDN, 
            @FormDataParam("TYPE") String TYPE, 
            @FormDataParam("LOCATION") String LOCATION ,
            @FormDataParam("BMDATE") String BMDATE ,
            @FormDataParam("GROUPID") String GROUPID ,
            @FormDataParam("BMAMT") String BMAMT ,
			@FormDataParam("BANKTYPE") String BANKTYPE 



            


    		) throws JSONException {

        System.out.print("-------------------------------uponly--------------------------------------");

        System.out.println("ID : " + ID);
        System.out.println("PAYER : " + PAYER);
        System.out.println("BANKCHARGE : " + BANKCHARGE);
        System.out.println("OVERPAY : " + OVERPAY);
        System.out.println("CNDN : " + CNDN);
        System.out.println("TYPE : " + TYPE);
        System.out.println("LOCATION : " + LOCATION);
        System.out.println("BMDATE : " + BMDATE);
        System.out.println("GROUPID : " + GROUPID);
        System.out.println("BMAMT : " + BMAMT);


        
        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						


        try {
        	
        	if(GROUPID.equalsIgnoreCase("-")) {
        		
        		return Response.ok(
    					UpdateData.CREATEID(getCono,getDivi, BMDATE,ID,PAYER,BANKCHARGE,OVERPAY,CNDN,TYPE,LOCATION,GROUPID,BMAMT,BANKTYPE),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        	}
        	else {
        		return Response.ok(
    					UpdateData.UPDATEID(getCono,getDivi,BMDATE,ID,PAYER,BANKCHARGE,OVERPAY,CNDN,TYPE,LOCATION,GROUPID,BMAMT),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
        		
        	}

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
    
	
	///////// RETURN /////////

	
	@PUT
    @Path("/deletelist")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response deletelist(
    		@Context HttpHeaders headers,
            @FormDataParam("ID") String ID

          


    		) throws JSONException {

        System.out.println("---------------------------------------------------------------------");

        System.out.println("ID : " + ID);

        
        System.out.println("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						


        try {
        	
        		
        		return Response.ok(
    					UpdateData.DELETELIST(getCono,getDivi,ID),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        	
        	
			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	

	  
		@PUT
	    @Path("/returnnormal")
		@Consumes(MediaType.MULTIPART_FORM_DATA)
	    public Response returnnormal(
	            @FormDataParam("CONO") String CONO, 
	            @FormDataParam("ID") String ID,
	            @FormDataParam("GROUPID") String GROUPID

	          


	    		) throws JSONException {

	        System.out.println("---------------------------------------------------------------------");

	        System.out.println("ID : " + ID);
	        System.out.println("CONO : " + CONO);
	        System.out.println("GROUPID : " + GROUPID);




	        
	        System.out.println("---------------------------------------------------------------------");
	        
	        JSONObject mJsonObj = new JSONObject();

	        try {
	        	
	        		
	        		return Response.ok(
	    					UpdateData.RETURNNORMAL(ID,CONO,GROUPID),
	    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
	        	
	        	
				

			} catch (Exception e) {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", e.getMessage());
				logger.error(e.getMessage());
			}
	        
			return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	    }
		

		@PUT
	    @Path("/returnparent")
		@Consumes(MediaType.MULTIPART_FORM_DATA)
	    public Response returnparent(
	            @FormDataParam("CONO") String CONO, 
	            @FormDataParam("ID") String ID
	          


	    		) throws JSONException {

	        System.out.print("---------------------------------------------------------------------");

	        System.out.println("ID : " + ID);
	        System.out.println("CONO : " + CONO);



	        
	        System.out.print("---------------------------------------------------------------------");
	        
	        JSONObject mJsonObj = new JSONObject();

	        try {
	        	
	        		
	        		return Response.ok(
	    					UpdateData.RETURNPARENT(ID,CONO),
	    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
	        	
	        	
				

			} catch (Exception e) {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", e.getMessage());
				logger.error(e.getMessage());
			}
	        
			return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	    }
		
	
	
	
	////////////////////////////



	@PUT
    @Path("/updateandgetid")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateandgetid(
    		@Context HttpHeaders headers,
            @FormDataParam("PAYER") String PAYER, 
            @FormDataParam("ID") String ID, 
            @FormDataParam("BANKCHARGE") String BANKCHARGE, 
            @FormDataParam("OVERPAY") String OVERPAY, 
            @FormDataParam("CNDN") String CNDN, 
            @FormDataParam("TYPE") String TYPE, 
            @FormDataParam("LOCATION") String LOCATION ,
            @FormDataParam("BMDATE") String BMDATE ,
            @FormDataParam("GROUPID") String GROUPID ,
            @FormDataParam("BMAMT") String BMAMT ,
			@FormDataParam("BANKTYPE") String BANKTYPE 

			


            


    		) throws JSONException {

        System.out.print("---------------------------------kkkkkk------------------------------------");

        System.out.println("ID : " + ID);
        System.out.println("PAYER : " + PAYER);
        System.out.println("BANKCHARGE : " + BANKCHARGE);
        System.out.println("OVERPAY : " + OVERPAY);
        System.out.println("CNDN : " + CNDN);
        System.out.println("TYPE : " + TYPE);
        System.out.println("LOCATION : " + LOCATION);
        System.out.println("BMDATE : " + BMDATE);
        System.out.println("GROUPID : " + GROUPID);
        System.out.println("BMAMT : " + BMAMT);
		System.out.println("BANKTYPE : " + BANKTYPE);



        
        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						



        try {
        	
        	if(GROUPID.equalsIgnoreCase("-")) {
        		
        		return Response.ok(
    					UpdateData.CREATEID(getCono,getDivi, BMDATE,ID,PAYER,BANKCHARGE,OVERPAY,CNDN,TYPE,LOCATION,GROUPID,BMAMT,BANKTYPE),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        	}
        	else {
        		return Response.ok(
    					UpdateData.UPDATEID(getCono,getDivi,BMDATE,ID,PAYER,BANKCHARGE,OVERPAY,CNDN,TYPE,LOCATION,GROUPID,BMAMT),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
        		
        	}

			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
    
	@PUT
    @Path("/uploadstatement")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadStatement(
    		@Context HttpHeaders headers,
            @FormDataParam("tableData") String tableData,  
            @FormDataParam("statemenType") String statemenType) throws JSONException {

        System.out.print("---------------------------------------------------------------------");

        
        //todo addcono,divi

        System.out.println("tableData: " + tableData);
        System.out.println("statemenType: " + statemenType);
        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

//todo user
		
		if (getToken != null && !getToken.isEmpty()) {
		
			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
			
			System.out.print(checkToken);

			if (checkToken) {
				
				
					JSONObject getDataObject = dataObject.getJSONObject("body");
					String[] getSubject = getDataObject.getString("sub").split(" : ");
					String getCono = getSubject[0];
					String getDivi = getSubject[1];
					String getUsername = getDataObject.getString("aud");
		
						System.out.println("----------------------------------add cono divi-----------------------------------");
					 	System.out.println(getCono);
				        System.out.println(getDivi);
				        System.out.println("---------------------------add cono divi------------------------------------------");


        try {

			return Response.ok(
					InsertData.CHECKTYPE(getUsername,statemenType,tableData,getCono,getDivi),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
	} else {
		mJsonObj.put("result", "nok");
		mJsonObj.put("message", "Token expired.");
		logger.error("Token expired.");
	}

	} else {
	mJsonObj.put("result", "nok");
	mJsonObj.put("message", "No token provided.");
	logger.error("No token provided.");
	}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	
	
	
	//////////////////////////////////////////////// BANKMAPPING //////////////////////////////////////////////

	
	///////////////////// SPLITBM/////////////////
	

	
	@PUT
    @Path("/splitbm")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadStatement(
    		@Context HttpHeaders headers,
            @FormDataParam("ID") String ID,
            @FormDataParam("BM_CONO") String BM_CONO,
            @FormDataParam("PAYER") String PAYER,
            @FormDataParam("OVERPAY") String OVERPAY,
            @FormDataParam("CNDN") String CNDN,
            @FormDataParam("ACCNO") String ACCNO,
            @FormDataParam("BM_DESC") String BM_DESC,
            @FormDataParam("BM_DATE") String BM_DATE,
            @FormDataParam("BM_TIME") String BM_TIME,
            @FormDataParam("BANKCHARGE") String BANKCHARGE,
            @FormDataParam("BM_AMOUNT") String BM_AMOUNT,
            @FormDataParam("BM_PARENT") String BM_PARENT,
            @FormDataParam("GROUP_ID") String GROUP_ID,
            @FormDataParam("H_RNNO") String H_RNNO,
            @FormDataParam("OKCUNO") String OKCUNO,
            @FormDataParam("PAYERNAME") String PAYERNAME,
            @FormDataParam("TYPE") String TYPE,
            @FormDataParam("CHECKTYPE") String CHECKTYPE,
            @FormDataParam("newBM_AMOUNT") String newBM_AMOUNT

            

            )throws JSONException {

        System.out.print("---------------------------------------------------------------------");

        System.out.println("ID : " + ID);
        System.out.print("---------------------------------------------------------------------");
        
        JSONObject mJsonObj = new JSONObject();
        
        
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	    String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);



					JSONObject dataObject = new JSONObject(getTokenData);
					boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

						JSONObject getDataObject = dataObject.getJSONObject("body");
						String getSubject[] = getDataObject.getString("sub").split(" : ");
						String getCono = getSubject[0];
						String getDivi = getSubject[1];
						String getCompanyName = getSubject[2];
						String getUsername = getDataObject.getString("aud");
						String getAuth = getDataObject.getString("role");
						
						  System.out.print("---------------------------------------------------------------------");

					        System.out.println("getCono : " + getCono);

					        System.out.println("getDivi : " + getDivi);
					        System.out.print("---------------------------------------------------------------------");
						

        try {

        	if(CHECKTYPE.equalsIgnoreCase("SCB"))
        	{
        		
        		return Response.ok(
    					InsertData.SPLITBMSCB(ID,
    				             BM_CONO,
    				             getDivi,
    				           PAYER,
    				            OVERPAY,
    				            CNDN,
    				           ACCNO,
    				          BM_DESC,
    				         BM_DATE,
    				        BM_TIME,
    				           BANKCHARGE,
    				          BM_AMOUNT,
    				           BM_PARENT,
    				          GROUP_ID,
    				           H_RNNO,
    				          OKCUNO,
    				        PAYERNAME,
    				            TYPE,
    				            CHECKTYPE,
    				            newBM_AMOUNT),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
        	}
        	else if(CHECKTYPE.equalsIgnoreCase("KBANK_CUR"))
        	{
        		
        		return Response.ok(
    					InsertData.SPLITBMSCB(ID,
    				             BM_CONO,
    				             getDivi,
    				           PAYER,
    				            OVERPAY,
    				            CNDN,
    				           ACCNO,
    				          BM_DESC,
    				         BM_DATE,
    				        BM_TIME,
    				           BANKCHARGE,
    				          BM_AMOUNT,
    				           BM_PARENT,
    				          GROUP_ID,
    				           H_RNNO,
    				          OKCUNO,
    				        PAYERNAME,
    				            TYPE,
    				            CHECKTYPE,
    				            newBM_AMOUNT),
    					MediaType.APPLICATION_JSON + ";charset=utf8").build();
        		
        	}
        	
        	
        	
        	else {
        		
        		
        		return Response.ok(
        				InsertData.SPLITBMSCB(ID,
   				             BM_CONO,
   				          getDivi,
   				           PAYER,
   				            OVERPAY,
   				            CNDN,
   				           ACCNO,
   				          BM_DESC,
   				         BM_DATE,
   				        BM_TIME,
   				           BANKCHARGE,
   				          BM_AMOUNT,
   				           BM_PARENT,
   				          GROUP_ID,
   				           H_RNNO,
   				          OKCUNO,
   				        PAYERNAME,
   				            TYPE,
   				            CHECKTYPE,
   				            newBM_AMOUNT),
   					MediaType.APPLICATION_JSON + ";charset=utf8").build();
    					
        	}
			

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}
        
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

    }
	
	
	//////////////////////// SPLITBM //////////////
	
	

	@POST
	@Path("/sendemailpp2/{prefix}/{ordno}/{status}/{submit}/{cono}/{divi}/{location}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response sendemailpp(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("prefix") String prefix, @PathParam("ordno") String ordno, @PathParam("status") String status,
			@PathParam("submit") String submit,
			@PathParam("cono") String cono,
			@PathParam("divi") String divi,
			@PathParam("location") String location,
			String req) throws JSONException {
		//logger.info("/sendemailpp/{prefix}/{ordno}/{status}/{submit}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = "ccccc";
		 System.out.println("Authhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhmail");

		//if (getToken != null && !getToken.isEmpty()) {
			//String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
			// System.out.println("getTokenData: " + getTokenData);


				String getCono = "10";
				String getDivi = "101";
				String getCompanyName = "BR";
				String getUsername = "xxx";
				String getAuth = "xxxx"; 
				String getLocation = "11";

				try {

					return Response.ok(
							SendEmail.prepareResendEmail(httpServletRequest, cono, divi, prefix, ordno, status,
									status, submit, getUsername, getToken , location),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	

	@POST
	@Path("/sendemailppFAP/{prefix}/{ordno}/{status}/{submit}/{foodcheck}/{foodqty}/{atkcheck}/{atkqty}/{parkcheck}/{parkqty}/{etc}/{beveragecheck}/{beveragenumber}/{snackcheck}/{snacksnumber}/{sandalcheck}/{sandalnumber}/{meetdate}/{meettime}/{meetdateout}/{meettimeout}/{room}/{company}/{name}/{surname}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response sendemailppFAP(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("prefix") String prefix, @PathParam("ordno") String ordno, @PathParam("status") String status,
			@PathParam("submit") String submit,
			@PathParam("foodcheck") String foodcheck,
			@PathParam("foodqty") String foodqty,
			@PathParam("atkcheck") String atkcheck,
			@PathParam("atkqty") String atkqty,
			@PathParam("parkcheck") String parkcheck,
			@PathParam("parkqty") String parkqty,
			@PathParam("etc") String etc,
			@PathParam("beveragecheck") String beveragecheck,
			@PathParam("beveragenumber") String beveragenumber,
			@PathParam("snackcheck") String snackcheck,
			@PathParam("snacksnumber") String snacksnumber,
			@PathParam("sandalcheck") String sandalcheck,
			@PathParam("sandalnumber") String sandalnumber,
			@PathParam("meetdate") String meetdate,
			@PathParam("meettime") String meettime,
			@PathParam("meetdateout") String meetdateout,
			@PathParam("meettimeout") String meettimeout,
			@PathParam("room") String room,
	        @PathParam("company") String company,
		    @PathParam("name") String name,
		    @PathParam("surname") String surname,

			String req) throws JSONException {

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String[] getSubject = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");
				String getLocation = getSubject[3];

				try {
					
					
				



					return Response.ok(
							SendEmail.prepareEmailFAP(httpServletRequest,getCono,getDivi,prefix,ordno,status,status,submit,getUsername,getToken,getLocation,foodcheck,foodqty,atkcheck,atkqty,parkcheck,parkqty,etc,beveragecheck,beveragenumber,snackcheck,snacksnumber,sandalcheck,sandalnumber,meetdate
									,meettime,meetdateout,meettimeout,room,company,name,surname),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();
					
				

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	
	
	
	@POST
	@Path("/resendemail/{prefix}/{marno}/{status}/{submit}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response resendEmail(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@PathParam("prefix") String prefix, @PathParam("marno") String marno, @PathParam("status") String status,
			@PathParam("submit") String submit,@PathParam("getLocation") String getLocation,
			String req) throws JSONException {
		logger.info("/resendemail/{prefix}/{marno}/{status}/{submit}");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String[] getSubject = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(
							SendEmail.prepareResendEmail(httpServletRequest, getCono, getDivi, prefix, marno, status,
									status, submit, getUsername, getToken , getLocation),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//////////////////////////////
	
	
	
	
	

	@GET
	@Path("/location/{CONO}/{DIVI}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response location(@Context HttpHeaders headers,
			 @PathParam("CONO") String CONO,
			 @PathParam("DIVI") String DIVI,
			@Context HttpServletRequest httpServletRequest) throws JSONException {
		logger.info("/getfollower");

		JSONObject mJsonObj = new JSONObject();

		
			

				try {

					return Response.ok(SelectData.location(CONO,DIVI), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

		
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	

	@GET
	@Path("/getfollower/{vID}/{CONO}/{DIVI}/{LOCATION}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getfollower(@Context HttpHeaders headers, @PathParam("vID") String vID,
			 @PathParam("CONO") String CONO,
			 @PathParam("DIVI") String DIVI,
			 @PathParam("LOCATION") String LOCATION,
			@Context HttpServletRequest httpServletRequest) throws JSONException {
		logger.info("/getfollower");

		JSONObject mJsonObj = new JSONObject();

		
			

				try {

					return Response.ok(SelectData.getfollower(vID), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

		
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getemployee")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getemployee(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getemployee");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(SelectData.getemployee(getCono), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

//////// SWR HEADER　/////////////

	@GET
	@Path("/getID")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getID(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getID");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(SelectData.getID(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getnewID")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getnewID(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getID");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response
							.ok(SelectData.getnewID(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	

	@GET
	@Path("/getlistemail/{cono}/{status}/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getListEmail(@Context HttpHeaders headers,@PathParam("cono") String cono,@PathParam("status") String status ,@PathParam("id") String id, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();
	

				try {

					return Response
							.ok(SelectData.getListEmail(cono,status,id), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	@GET
	@Path("/getlistuser/{cono}/{status}/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getlistuser(@Context HttpHeaders headers,@PathParam("cono") String cono,@PathParam("status") String status ,@PathParam("id") String id, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();
	

				try {

					return Response
							.ok(SelectData.getlistuser(cono,status,id), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	

	@GET
	@Path("/getmailtemplete/{program}/{status}/{requester}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getmailtemplete(@Context HttpHeaders headers,@PathParam("program") String program,@PathParam("status") String status ,@PathParam("requester") String requester, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();
	

				try {

					return Response
							.ok(SelectData.getmailtemplete(program,status,requester), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	
	@GET
	@Path("/getlistuser2/{cono}/{status}/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getlistuser2(@Context HttpHeaders headers,@PathParam("cono") String cono,@PathParam("status") String status ,@PathParam("id") String id, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();
	

				try {

					return Response
							.ok(SelectData.getlistuser2(cono,status,id), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	
	
	@GET
	@Path("/getDeptHead/{cono}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDeptHead(@Context HttpHeaders headers,@PathParam("cono") String cono, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();
	

				try {

					return Response
							.ok(SelectData.getDeptHead(cono), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			
		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	
	@GET
	@Path("/getAuthenBySTATUS/{status}/{id}/{username}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAuthenBySTATUS(@Context HttpHeaders headers, @PathParam("status") String status, @PathParam("id") String id, @PathParam("username") String username,@Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();
		

				try {

					return Response
							.ok(SelectData.getAuthenBySTATUS(status,id, username), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

		

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getDev")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDev(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response
							.ok(SelectData.getDev(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

//////////////////////////////////////////

	@POST
	@Path("/addfollower")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addfollower(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("H_SURNAME") String H_SURNAME, @FormDataParam("vID") String vID
			, @FormDataParam("vCONO") String vCONO
			, @FormDataParam("vDIVI") String vDIVI
			, @FormDataParam("vLocation") String vLocation) throws JSONException {
		logger.info("/addfollower");

		JSONObject mJsonObj = new JSONObject();
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

	
				try {

					return Response.ok(InsertData.addfollower( vCONO, vDIVI,vLocation, H_SURNAME, vID),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/addheaeder")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addHeader(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vSwrtype") String vSwrtype, @FormDataParam("vSwrname") String vSwrname,
			@FormDataParam("vVersion") String vVersion, @FormDataParam("vReqdate") String vReqdate,
			@FormDataParam("vFinishdate") String vFinishdate, @FormDataParam("vRemark") String vRemark,
			@FormDataParam("vRequester") String vRequester, @FormDataParam("vDepthead") String vDepthead,
			@FormDataParam("vDev") String vDev, @FormDataParam("vAppdevmanager") String vAppdevmanager,
			@FormDataParam("vGM") String vGM, @FormDataParam("vCIO") String vCIO,
			@FormDataParam("vStatus") String vStatus) throws JSONException {
		logger.info("/addheaeder");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(InsertData.addSwrHeader(getCono, getDivi, vSwrtype, vSwrname, vVersion, vReqdate,
							vFinishdate, vRemark, vRequester, vDepthead, vDev, vAppdevmanager, vGM, vCIO, vStatus),
							MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/swrfile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response addSWRFile(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vReference") String vReference, @FormDataParam("vOrderno") String vOrderno,
			@FormDataParam("vName") String vName, @FormDataParam("vLine") String vLine,
			@FormDataParam("vType") String vType, @FormDataParam("vRemark1") String vRemark1,
			@FormDataParam("vRemark2") String vRemark2) throws JSONException {
		logger.info("/marfile");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(InsertData.addSWRFile(getCono, getDivi, vOrderno, vReference, vName, vLine,
							vType, vRemark1, vRemark2), MediaType.APPLICATION_JSON + ";charset=utf8").build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getswrfile")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSWRFile(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/swrfile");
		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionV2(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = dataObject.getJSONObject("body");
				String getSubject[] = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {
					return Response
							.ok(SelectData.getSWRile(getCono, getDivi), MediaType.APPLICATION_JSON + ";charset=utf8")
							.build();

				} catch (Exception e) {
					mJsonObj.put("result", "nok");
					mJsonObj.put("message", e.getMessage());
					logger.error(e.getMessage());
				}

			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "Token expired.");
				logger.error("Token expired.");
			}

		} else {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", "No token provided.");
			logger.error("No token provided.");
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	/////////////

	////////////////

}
