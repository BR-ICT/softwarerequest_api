package com.br.api;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
//import org.json.JSONArray;
import com.br.auth.JwtManager;
import com.br.data.DeleteData;
import com.br.data.InsertData;
import com.br.data.SelectData;
import com.br.data.UpdateData;
import com.br.utility.ConvertStringtoObject;
import com.br.utility.FileUtillity;
import com.br.utility.HttpConnection;
import com.br.utility.SendEmail;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

@Path("/data")
public class api_data {

	protected static final Logger logger = LogManager.getLogger(api_data.class);

	@GET
	@Path("/company")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCompany(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/company");

		JSONObject mJsonObj = new JSONObject();
		// String getToken = headers.getRequestHeaders().getFirst("x-access-token");

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
	@Path("/getDeptHead/{cono}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDeptHead(@Context HttpHeaders headers, @PathParam("cono") String cono,
			@Context HttpServletRequest httpServletRequest)
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
	@Path("/getsupplier/{cono}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getsupplier(@Context HttpHeaders headers, @PathParam("cono") String cono,
			@Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getsupplier");

		JSONObject mJsonObj = new JSONObject();

		try {

			return Response
					.ok(SelectData.getsupplier(cono), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	

	///////////////////////////////

	
	
	
	
	
	
	/*
	@POST
	@Path("/files")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response uploadFiles(FormDataMultiPart multiPart, @Context HttpServletRequest request) {
		JSONObject mJsonObj = new JSONObject();
		JSONArray uploadedFiles = new JSONArray();

			return Response.ok(mJsonObj.toString()).build();

		
	}
	
	*/
	
	
	
	
	/*
	@POST
	@Path("/files")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addImages(
	        @FormDataParam("vImageFile") List<InputStream> fileInputStreams,
	        @FormDataParam("vImageFile") List<FormDataContentDisposition> fileFormDataDispositions,
	        @FormDataParam("vImageName") List<String> vImageNames,
	        @Context HttpServletRequest request) throws JSONException {

	    JSONObject mJsonObj = new JSONObject();
	    JSONArray uploadedFiles = new JSONArray();

	    try {
	        String uploadDirPath = request.getRealPath("/") + "uploads/";
	        File uploadDir = new File(uploadDirPath);
	        if (!uploadDir.exists()) uploadDir.mkdirs();

	        for (int i = 0; i < fileInputStreams.size(); i++) {
	            InputStream in = fileInputStreams.get(i);
	            String fileName = vImageNames.get(i);

	            FileUtillity.writeToFileServerV3(in, fileName, uploadDirPath);

	            String fileUrl = request.getScheme() + "://" +
	                    request.getServerName() + ":" +
	                    request.getServerPort() +
	                    request.getContextPath() + "/uploads/" + fileName;

	            JSONObject fileObj = new JSONObject();
	            fileObj.put("fileName", fileName);
	            fileObj.put("url", fileUrl);

	            uploadedFiles.put(fileObj);
	        }

	        mJsonObj.put("result", "ok");
	        mJsonObj.put("files", uploadedFiles);

	        return Response.ok(mJsonObj.toString()).build();

	    } catch (Exception e) {
	        mJsonObj.put("result", "nok");
	        mJsonObj.put("message", e.getMessage());
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity(mJsonObj.toString()).build();
	    }
	}

	
	*/ 
	
	  

	/*
	 * @POST
	 * 
	 * @Path("/files")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addImages(
	 * 
	 * @FormDataParam("vImageFile") List<InputStream> fileInputStreams,
	 * 
	 * @FormDataParam("vImageFile") List<FormDataContentDisposition>
	 * fileFormDataDispositions,
	 * 
	 * @FormDataParam("vImageName") List<String> vImageNames,
	 * 
	 * @Context HttpServletRequest request) throws JSONException {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * JSONArray uploadedFiles = new JSONArray();
	 * 
	 * 
	 * try {
	 * String uploadDirPath = request.getRealPath("/") + "uploads/";
	 * File uploadDir = new File(uploadDirPath);
	 * if (!uploadDir.exists()) uploadDir.mkdirs();
	 * 
	 * for (int i = 0; i < fileInputStreams.size(); i++) {
	 * InputStream in = fileInputStreams.get(i);
	 * String fileName = vImageNames.get(i);
	 * 
	 * FileUtillity.writeToFileServerV3(in, fileName, uploadDirPath);
	 * 
	 * String fileUrl = request.getScheme() + "://" +
	 * request.getServerName() + ":" +
	 * request.getServerPort() +
	 * request.getContextPath() + "/uploads/" + fileName;
	 * 
	 * JSONObject fileObj = new JSONObject();
	 * fileObj.put("fileName", fileName);
	 * fileObj.put("url", fileUrl);
	 * 
	 * uploadedFiles.put(fileObj);
	 * }
	 * 
	 * mJsonObj.put("result", "ok");
	 * mJsonObj.put("files", uploadedFiles);
	 * 
	 * return Response.ok(mJsonObj.toString()).build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * e.printStackTrace();
	 * return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	 * .entity(mJsonObj.toString()).build();
	 * }
	 * 
	 * 
	 * 
	 * return Response.ok(mJsonObj.toString()).build();
	 * }
	 * 
	 * 
	 */

	/*
	 * @POST
	 * 
	 * @Path("/files")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addImages(
	 * 
	 * @FormDataParam("vImageFile") List<InputStream> fileInputStreams,
	 * 
	 * @FormDataParam("vImageFile") List<FormDataContentDisposition>
	 * fileFormDataDispositions,
	 * 
	 * @FormDataParam("vImageName") List<String> vImageNames,
	 * 
	 * @Context HttpServletRequest request) throws JSONException {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * JSONArray uploadedFiles = new JSONArray();
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * try {
	 * String uploadDirPath = request.getRealPath("/") + "uploads/";
	 * File uploadDir = new File(uploadDirPath);
	 * if (!uploadDir.exists()) uploadDir.mkdirs();
	 * 
	 * for (int i = 0; i < fileInputStreams.size(); i++) {
	 * InputStream in = fileInputStreams.get(i);
	 * String fileName = vImageNames.get(i);
	 * FileUtillity.writeToFileServerV3(in, fileName, uploadDirPath);
	 * 
	 * String fileUrl = request.getScheme() + "://" +
	 * request.getServerName() + ":" +
	 * request.getServerPort() +
	 * request.getContextPath() + "/uploads/" + fileName;
	 * 
	 * JSONObject fileObj = new JSONObject();
	 * fileObj.put("fileName", fileName);
	 * fileObj.put("url", fileUrl);
	 * 
	 * uploadedFiles.put(fileObj);
	 * }
	 * 
	 * mJsonObj.put("result", "ok");
	 * mJsonObj.put("files", uploadedFiles);
	 * 
	 * return Response.ok(mJsonObj.toString()).build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * e.printStackTrace();
	 * return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	 * .entity(mJsonObj.toString()).build();
	 * }
	 * 
	 * 
	 * 
	 * return Response.ok(mJsonObj.toString()).build();
	 * }
	 */

	/*
	 * @POST
	 * 
	 * @Path("/files")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addImage1(
	 * 
	 * @FormDataParam("vImageFile") InputStream fileInputStream,
	 * 
	 * @FormDataParam("vImageFile") FormDataContentDisposition
	 * fileFormDataContentDisposition,
	 * 
	 * @FormDataParam("vImageName") String vImageName,
	 * 
	 * @Context HttpServletRequest httpServletRequest) throws JSONException {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * System.out.println(
	 * "------------------------------------------------------------");
	 * 
	 * try {
	 * // 📁 เปลี่ยนจาก WEB-INF → uploads (เข้าถึงผ่าน URL ได้)
	 * String uploadDirPath = httpServletRequest.getRealPath("/") + "uploads/";
	 * File uploadDir = new File(uploadDirPath);
	 * if (!uploadDir.exists()) uploadDir.mkdirs();
	 * 
	 * System.out.println("filePath: " + uploadDirPath + vImageName);
	 * 
	 * // ✅ เรียกใช้ Utility เดิม
	 * String savedPath = FileUtillity.writeToFileServerV3(fileInputStream,
	 * vImageName, uploadDirPath);
	 * 
	 * // ✅ สร้าง URL สำหรับเข้าถึงไฟล์
	 * String fileUrl = httpServletRequest.getScheme() + "://" +
	 * httpServletRequest.getServerName() + ":" +
	 * httpServletRequest.getServerPort() +
	 * httpServletRequest.getContextPath() + "/uploads/" + vImageName;
	 * 
	 * 
	 * System.out.println("File uploaded. Access URL: " + fileUrl);
	 * 
	 * 
	 * // ✅ สร้าง JSON ตอบกลับ
	 * mJsonObj.put("result", "ok");
	 * mJsonObj.put("fileName", vImageName);
	 * mJsonObj.put("url", fileUrl);
	 * 
	 * return Response.ok(mJsonObj.toString()).build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * e.printStackTrace();
	 * return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	 * .entity(mJsonObj.toString()).build();
	 * }
	 * }
	 * 
	 */

	/*
	 * 
	 * @POST
	 * 
	 * @Path("/files")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addImages(
	 * 
	 * @FormDataParam("vImageFile") List<InputStream> fileInputStreams,
	 * 
	 * @FormDataParam("vImageFile") List<FormDataContentDisposition>
	 * fileDispositions,
	 * 
	 * @Context HttpServletRequest httpServletRequest) throws JSONException {
	 * 
	 * JSONObject result = new JSONObject();
	 * JSONArray uploadedFiles = new JSONArray();
	 * 
	 * try {
	 * String uploadDirPath = httpServletRequest.getRealPath("/") + "uploads/";
	 * File uploadDir = new File(uploadDirPath);
	 * if (!uploadDir.exists()) uploadDir.mkdirs();
	 * 
	 * // 🔁 วน loop ทุกไฟล์ที่ส่งมา
	 * for (int i = 0; i < fileInputStreams.size(); i++) {
	 * InputStream fileInputStream = fileInputStreams.get(i);
	 * String fileName = fileDispositions.get(i).getFileName();
	 * 
	 * // ✅ เขียนไฟล์
	 * FileUtillity.writeToFileServerV3(fileInputStream, fileName, uploadDirPath);
	 * 
	 * // ✅ สร้าง URL สำหรับเข้าถึงไฟล์
	 * String fileUrl = httpServletRequest.getScheme() + "://" +
	 * httpServletRequest.getServerName() + ":" +
	 * httpServletRequest.getServerPort() +
	 * httpServletRequest.getContextPath() + "/uploads/" + fileName;
	 * 
	 * JSONObject fileObj = new JSONObject();
	 * fileObj.put("fileName", fileName);
	 * fileObj.put("url", fileUrl);
	 * 
	 * uploadedFiles.put(fileObj);
	 * }
	 * 
	 * result.put("result", "ok");
	 * result.put("files", uploadedFiles);
	 * 
	 * return Response.ok(result.toString()).build();
	 * 
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * result.put("result", "nok");
	 * result.put("message", e.getMessage());
	 * return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	 * .entity(result.toString()).build();
	 * }
	 * }
	 * 
	 */

	/*
	 * 
	 * @POST
	 * 
	 * @Path("/files")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addImage1(
	 * 
	 * @FormDataParam("vImageFile") InputStream fileInputStream,
	 * 
	 * @FormDataParam("vImageFile") FormDataContentDisposition
	 * fileFormDataContentDisposition,
	 * 
	 * @FormDataParam("vImageName") String vImageName,
	 * 
	 * @Context HttpServletRequest httpServletRequest) throws JSONException {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * System.out.println(
	 * "------------------------------------------------------------");
	 * 
	 * try {
	 * // 📁 เปลี่ยนจาก WEB-INF → uploads (เข้าถึงผ่าน URL ได้)
	 * String uploadDirPath = httpServletRequest.getRealPath("/") + "uploads/";
	 * File uploadDir = new File(uploadDirPath);
	 * if (!uploadDir.exists()) uploadDir.mkdirs();
	 * 
	 * System.out.println("filePath: " + uploadDirPath + vImageName);
	 * 
	 * // ✅ เรียกใช้ Utility เดิม
	 * String savedPath = FileUtillity.writeToFileServerV3(fileInputStream,
	 * vImageName, uploadDirPath);
	 * 
	 * // ✅ สร้าง URL สำหรับเข้าถึงไฟล์
	 * String fileUrl = httpServletRequest.getScheme() + "://" +
	 * httpServletRequest.getServerName() + ":" +
	 * httpServletRequest.getServerPort() +
	 * httpServletRequest.getContextPath() + "/uploads/" + vImageName;
	 * 
	 * 
	 * System.out.println("File uploaded. Access URL: " + fileUrl);
	 * 
	 * 
	 * // ✅ สร้าง JSON ตอบกลับ
	 * mJsonObj.put("result", "ok");
	 * mJsonObj.put("fileName", vImageName);
	 * mJsonObj.put("url", fileUrl);
	 * 
	 * return Response.ok(mJsonObj.toString()).build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * e.printStackTrace();
	 * return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	 * .entity(mJsonObj.toString()).build();
	 * }
	 * }
	 * 
	 * 
	 */

	@DELETE
	@Path("/files")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteFile(@QueryParam("fileName") String fileName,
			@Context HttpServletRequest request) throws JSONException {
		JSONObject mJsonObj = new JSONObject();
		try {
			// ใช้ ServletContext แทน request.getRealPath
			String uploadDir = request.getServletContext().getRealPath("/uploads/");
			File file = new File(uploadDir, fileName);

			System.out.println("xxxxxxx = " + fileName);

			System.out.println("Trying to delete file: " + file.getAbsolutePath());
			System.out.println("File exists? " + file.exists());

			if (file.exists() && file.delete()) {
				mJsonObj.put("result", "ok");
				mJsonObj.put("message", "File deleted successfully");
			} else {
				mJsonObj.put("result", "nok");
				mJsonObj.put("message", "File not found or cannot delete");
			}
		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
		}
		return Response.ok(mJsonObj.toString()).build();
	}

	///////////////////////////////

	/*
	 * 
	 * @POST
	 * 
	 * @Path("/files")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * // @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addImage1(@FormDataParam("vImageFile") InputStream
	 * fileInputStream,
	 * 
	 * @FormDataParam("vImageFile") FormDataContentDisposition
	 * fileFormDataContentDisposition,
	 * 
	 * @FormDataParam("vImageName") String vImageName, @Context HttpHeaders headers,
	 * 
	 * @Context HttpServletRequest httpServletRequest) throws JSONException {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * System.out.println(
	 * "------------------------------------------------------------" );
	 * 
	 * 
	 * try {
	 * 
	 * 
	 * String uploadFilePath = null;
	 * String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
	 * 
	 * try {
	 * 
	 * System.out.println("filePath: " + filePath + vImageName);
	 * 
	 * uploadFilePath = FileUtillity.writeToFileServer(fileInputStream, vImageName,
	 * filePath);
	 * return Response.status(Response.Status.OK).build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e);
	 * 
	 * }
	 * 
	 * } catch (SignatureException e) {
	 * mJsonObj.put("auth", "false");
	 * mJsonObj.put("message", e.getMessage());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	 * 
	 * }
	 * 
	 */

	////////////////////////

	/*
	 * 
	 * 
	 * @POST
	 * 
	 * @Path("/files")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response uploadFiles(@Context HttpHeaders headersy
	 * //@FormDataParam("vDate") String vDate
	 * // @FormDataParam("username") String username,
	 * // @FormDataParam("file") InputStream uploadedInputStream,
	 * // @FormDataParam("file") FormDataContentDisposition fileDetail
	 * ) {
	 * logger.info("/upload/files");
	 * 
	 * System.out.print("xxxx");
	 * 
	 * // logger.info("username : "+username);
	 * // logger.info("uploadedInputStream : "+uploadedInputStream);
	 * // logger.info("fileDetail : "+fileDetail);
	 * 
	 * //
	 * // String uploadedFileLocation = "/tmp/" + fileDetail.getFileName();
	 * // Files.copy(uploadedInputStream, Paths.get(uploadedFileLocation),
	 * StandardCopyOption.REPLACE_EXISTING);
	 * //
	 * 
	 * 
	 * /*
	 * JSONObject res = new JSONObject();
	 * res.put("result", "ok");
	 * res.put("fileName", fileDetail.getFileName());
	 * res.put("username", username);
	 * 
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * try {
	 * // เรียก method ของคุณเพื่อ save ไฟล์ + insert DB
	 * // String result =
	 * InsertData.saveFilesToServerAndDB(username,uploadedInputStream,fileDetail);
	 * 
	 * String result = "OK";
	 * 
	 * // ส่ง response กลับ
	 * return Response.ok(result, MediaType.APPLICATION_JSON +
	 * ";charset=utf-8").build();
	 * } catch (Exception e) {
	 * logger.error(e.getMessage(), e);
	 * try { mJsonObj.put("result", "nok"); mJsonObj.put("message", e.getMessage());
	 * } catch (Exception ex) {}
	 * return Response.status(Response.Status.BAD_REQUEST).entity(mJsonObj).build();
	 * }
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/////////////////////////////////

	@GET
	@Path("/getlistuser2/{cono}/{status}/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getlistuser2(@Context HttpHeaders headers, @PathParam("cono") String cono,
			@PathParam("status") String status, @PathParam("id") String id,
			@Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();

		try {

			return Response
					.ok(SelectData.getlistuser2(cono, status, id), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

//	@GET
//	@Path("/getmailtemplete/{cono}/{program}/{status}/{requester}/{programtype}")
//	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//	public Response getmailtemplete(@Context HttpHeaders headers, @PathParam("cono") String cono,
//			@PathParam("program") String program, @PathParam("status") String status,
//			@PathParam("requester") String requester, @PathParam("programtype") String programtype,
//			@Context HttpServletRequest httpServletRequest)
//			throws JSONException {
//		logger.info("/getDeptHead");
//
//		JSONObject mJsonObj = new JSONObject();
//
//		try {
//
//			// return Response
//			// .ok(SelectData.getmailtemplete(program,status,requester,programtype),
//			// MediaType.APPLICATION_JSON + ";charset=utf8")
//			// .build();
//
//			return Response
//					.ok(SelectData.getmailtempleteV2(cono, program, status, requester, programtype),
//							MediaType.APPLICATION_JSON + ";charset=utf8")
//					.build();
//
//		} catch (Exception e) {
//			mJsonObj.put("result", "nok");
//			mJsonObj.put("message", e.getMessage());
//			logger.error(e.getMessage());
//		}
//
//		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
//
//	}
	
	@GET
	@Path("/getmailtemplete/{cono}/{program}/{status}/{requester}/{programtype}/{requesttype}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getmailtemplete(@Context HttpHeaders headers, @PathParam("cono") String cono,
			@PathParam("program") String program, @PathParam("status") String status,
			@PathParam("requester") String requester, @PathParam("programtype") String programtype,
		 @PathParam("requesttype") String requesttype,
			@Context HttpServletRequest httpServletRequest)
			throws JSONException {
		logger.info("/getDeptHead");

		JSONObject mJsonObj = new JSONObject();

		try {

			// return Response
			// .ok(SelectData.getmailtemplete(program,status,requester,programtype),
			// MediaType.APPLICATION_JSON + ";charset=utf8")
			// .build();

			return Response
					.ok(SelectData.getmailtempleteV2(cono, program, status, requester, programtype,requesttype),
							MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getDepartment/{cono}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDepartment(@Context HttpHeaders headers, @PathParam("cono") String cono, String req)
			throws JSONException {
		logger.info("/company");

		JSONObject mJsonObj = new JSONObject();
		// String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.getDepartment(cono), MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/historyindex/{username}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response historyindex(@Context HttpHeaders headers, @PathParam("username") String username, String req)
			throws JSONException {
		logger.info("/historyindex");

		JSONObject mJsonObj = new JSONObject();
		// String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.getHistoryIndex(username), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getdescriptionmitmas/{itemno}/{comcono}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getdescriptionmitmas(@Context HttpHeaders headers, @PathParam("itemno") String itemno,
			@PathParam("comcono") String comcono, String req)
			throws JSONException {
		logger.info("/getdescriptionmitmas");

		JSONObject mJsonObj = new JSONObject();
		// String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response
					.ok(SelectData.getdescriptionmitmas(itemno, comcono), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e);
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/history/{id}/{cono}/{divi}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getistory(@Context HttpHeaders headers, @PathParam("id") String id, @PathParam("cono") String cono,
			@PathParam("divi") String divi, String req)
			throws JSONException {
		logger.info("/history");

		JSONObject mJsonObj = new JSONObject();
		// String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		try {
			return Response.ok(SelectData.getHistory(id, cono, divi), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

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

	@PUT
	@Path("/updatefollower")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
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

			return Response.ok(UpdateData.updatefollower(vID, H_SURNAME, oldH_SURNAME, vCONO, vDIVI, vLocaton),
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
	public Response checkoutwithdatetime(@Context HttpHeaders headers, @FormDataParam("vID") String vID,
			@FormDataParam("vStatuscheck") String vStatuscheck, @FormDataParam("vRemark") String remark,
			@FormDataParam("vCheckout") String vcheckout, @FormDataParam("vCheckouttime") String vcheckouttime

	) throws JSONException {
		logger.info("/visitorheader");
		System.out.print("visitorheaderrrrrrrrrrrrrrrrrrr00");
		String sts = "00";
		switch (vStatuscheck) {
			case "CHECKOUT":
				sts = "50";
				break;

			case "SUBMIT":
				sts = "10";
				break;
			case "APPROVE":
				sts = "30";
				break;
			case "CANCEL":
				sts = "99";
				break;

			case "REJECT":
				sts = "80";
				break;

			case "CHECKIN":
				sts = "10";
				break;

			default:
				sts = "00";
				break;

		}

		JSONObject mJsonObj = new JSONObject();

		String getCono = "10";
		String getDivi = "101";
		String getLocation = "11";

		try {

			return Response.ok(UpdateData.checkoutwithdatetime(vID, sts, getLocation, remark, vcheckout, vcheckouttime),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

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
	public Response checkout(@Context HttpHeaders headers, @FormDataParam("vID") String vID,
			@FormDataParam("vStatuscheck") String vStatuscheck, @FormDataParam("vRemark") String remark,
			@FormDataParam("vCheckout") String vCheckout

	) throws JSONException {
		logger.info("/visitorheader");
		System.out.print("visitorheaderrrrrrrrrrrrrrrrrrr00");
		String sts = "00";
		switch (vStatuscheck) {
			case "CHECKOUT":
				sts = "50";
				break;

			case "SUBMIT":
				sts = "10";
				break;
			case "APPROVE":
				sts = "30";
				break;
			case "CANCEL":
				sts = "99";
				break;

			case "REJECT":
				sts = "80";
				break;

			case "CHECKIN":
				sts = "10";
				break;

			default:
				sts = "00";
				break;

		}

		JSONObject mJsonObj = new JSONObject();
		// String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		// if (getToken != null && !getToken.isEmpty()) {
		// String getTokenData = HttpConnection.httpConnectionV2(getToken);
		// System.out.println("getTokenData: " + getTokenData);

		// JSONObject dataObject = new JSONObject(getTokenData);
		// boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

		// if (checkToken) {
		// JSONObject getDataObject = dataObject.getJSONObject("body");
		// String getSubject[] = getDataObject.getString("sub").split(" : ");
		// String getCono = getSubject[0];
		// String getDivi = getSubject[1];
		// getCompanyName = getSubject[2];
		// String getUsername = getDataObject.getString("aud");
		// String getAuth = getDataObject.getString("role");
		// String getLocation = getSubject[3];

		String getCono = "10";
		String getDivi = "101";
		String getLocation = "11";

		try {

			return Response.ok(UpdateData.checkout(vID, sts, getLocation, remark),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

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
	public Response checkout(@Context HttpHeaders headers, @FormDataParam("vID") String vID,
			@FormDataParam("vStatuscheck") String vStatuscheck, @FormDataParam("vROOM") String room,
			@FormDataParam("vLocation") String location, @FormDataParam("vRemark") String remark)
			throws JSONException {
		// logger.info("/visitorheader");
		System.out.print("checkout111111111111111111111");
		String sts = "00";
		switch (vStatuscheck) {
			case "CHECKOUT":
				sts = "50";
				break;

			case "SUBMIT":
				sts = "10";
				break;
			case "APPROVE":
				sts = "30";
				break;
			case "CANCEL":
				sts = "99";
				break;

			case "REJECT":
				sts = "80";
				break;

			case "CHECKIN":
				sts = "10";
				break;

			default:
				sts = "00";
				break;

		}

		JSONObject mJsonObj = new JSONObject();

		try {

			return Response.ok(UpdateData.checkout1(vID, sts, location, room, remark),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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








	///////////////////// VSITORHEADER //////////////

	// todo


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

		// if (getToken != null && !getToken.isEmpty()) {
		// String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
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
							status, submit, getUsername, getToken, getLocation),
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

		// if (getToken != null && !getToken.isEmpty()) {
		// String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
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
	public Response sendemailppwithoutauthen(@Context HttpHeaders headers,
			@Context HttpServletRequest httpServletRequest,
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

		// if (getToken != null && !getToken.isEmpty()) {
		// String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
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
							status, submit, getUsername, getToken, getLocation, vMeetdate, vMeettime, vName, vSurname,
							vROOMNO, vRemark),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	//////////////////////////////////////////////// BANKMAPPING
	//////////////////////////////////////////////// //////////////////////////////////////////////
	/*
	 * 
	 * public static class StatementData {
	 * public Object tableData;
	 * public String statemenType;
	 * 
	 * // Constructor, getters, setters (Optional)
	 * }
	 * 
	 * @POST
	 * 
	 * @Path("/uploadstatement")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * 
	 * public Response uploadStatement(@Context HttpHeaders headers, StatementData
	 * statementData) throws JSONException {
	 * String statemenType = statementData.statemenType;
	 * Object tableData = statementData.tableData;
	 * 
	 * System.out.print(
	 * "---------------------------------------------------------------------");
	 * System.out.print(tableData);
	 * System.out.print(statemenType);
	 * System.out.print(
	 * "---------------------------------------------------------------------");
	 * 
	 * return Response.ok().entity("Upload successful").build();
	 * }
	 * 
	 */

	@GET
	@Path("/getivoiceid")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getivoiceid(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/getivoiceid");

		JSONObject mJsonObj = new JSONObject();
		// System.out.println("getToken: " + getToken);

		// String getCono = "10";
		// String getDivi = "101";

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

		// String getCono = "10";
		// String getDivi = "101";

		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
		// System.out.println("getToken: " + getToken);

		JSONObject dataObject = new JSONObject(getTokenData);
		boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

		// JSONObject getDataObject = dataObject.getJSONObject("body");
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

	/*
	 * 
	 * @GET
	 * 
	 * @Path("/getreport/{fromdate}/{todate}/{type}/{location}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response getreport(@Context HttpServletRequest
	 * httpServletRequest,@Context HttpHeaders headers,
	 * 
	 * @PathParam("fromdate") String fromdate ,
	 * 
	 * @PathParam("todate") String todate ,
	 * 
	 * @PathParam("type") String type ,
	 * 
	 * @PathParam("location") String location
	 * ) throws JSONException {
	 * 
	 * 
	 * System.out.println(
	 * "xxxx---------------------------------------------------------------------");
	 * 
	 * System.out.println(fromdate);
	 * System.out.println(todate);
	 * System.out.println(type);
	 * System.out.println(location);
	 * 
	 * System.out.println(
	 * "xxxx555---------------------------------------------------------------------"
	 * );
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * 
	 * String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	 * 
	 * 
	 * System.out.println(getToken);
	 * System.out.println("xzzzz");
	 * 
	 * 
	 * if (getToken != null && !getToken.isEmpty()) {
	 * 
	 * 
	 * 
	 * String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
	 * 
	 * 
	 * System.out.println("ttttttt : " + getTokenData);
	 * 
	 * 
	 * 
	 * 
	 * JSONObject dataObject = new JSONObject(getTokenData);
	 * 
	 * 
	 * 
	 * boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
	 * 
	 * 
	 * if (checkToken) {
	 * JSONObject getDataObject = dataObject.getJSONObject("body");
	 * 
	 * String getUsername = getDataObject.getString("aud");
	 * 
	 * try {
	 * 
	 * System.out.print(getUsername);
	 * 
	 * return Response.ok(
	 * SelectData.getuser(getUsername),
	 * MediaType.APPLICATION_JSON + ";charset=utf8").build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * logger.error(e.getMessage());
	 * }
	 * 
	 * }}
	 * 
	 * return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	 * 
	 * 
	 * 
	 * }
	 * 
	 */

	/////////////////////////////////

	@GET
	@Path("/getuser")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getuser(@Context HttpServletRequest httpServletRequest, @Context HttpHeaders headers)
			throws JSONException {

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

			}
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getuserdel")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getuserdel(@Context HttpServletRequest httpServletRequest, @Context HttpHeaders headers)
			throws JSONException {

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

			}
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@GET
	@Path("/getusermas")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getusermas(@Context HttpServletRequest httpServletRequest, @Context HttpHeaders headers)
			throws JSONException {

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

			}
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	////////////////////////

	@GET
	@Path("/getitem/{statemenDate}/{statemenType}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getitem(@Context HttpServletRequest httpServletRequest,
			@PathParam("statemenDate") String statemenDate, @PathParam("statemenType") String statemenType,
			@Context HttpHeaders headers, String req) throws JSONException {

		// todo addcono

		System.out.println("xxxx---------------------------------------------------------------------");

		System.out.println("statemenDate: " + statemenDate);
		System.out.println("statemenType: " + statemenType);
		System.out.println("xxxx---------------------------------------------------------------------");

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");

		// todo user

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
					SelectData.CHECKTYPEGETITEM(httpServletRequest, statemenDate, statemenType, getCono, getDivi),
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
	public Response getitembankmapping(@Context HttpServletRequest httpServletRequest,
			@PathParam("statemenDate") String statemenDate, @PathParam("statemenType") String statemenType,
			@Context HttpHeaders headers, String req) throws JSONException {

		System.out.println("xxxx---------------------------------------------------------------------");

		System.out.println("statemenType: " + statemenType);
		System.out.println("statemenDate: " + statemenDate);
		System.out.println("xxxx---------------------------------------------------------------------");

		JSONObject mJsonObj = new JSONObject();

		try {

			return Response.ok(
					SelectData.GETITEMBANKMAPPING(httpServletRequest, statemenDate, statemenType),
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
	public Response getitemlrc(@Context HttpServletRequest httpServletRequest,
			@PathParam("selectedLRC") String selectedLRC, @Context HttpHeaders headers, String req)
			throws JSONException {

		System.out.println("xxxx---------------------------------------------------------------------");

		System.out.println("statemenID: " + selectedLRC);
		System.out.println("xxxx---------------------------------------------------------------------");

		JSONObject mJsonObj = new JSONObject();

		try {

			return Response.ok(
					SelectData.GETITEMLRC(httpServletRequest, selectedLRC),
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
	public Response getitemrc(@Context HttpServletRequest httpServletRequest,
			@PathParam("selectedRC") String selectedRC, @Context HttpHeaders headers, String req) throws JSONException {

		System.out.println("xxxx---------------------------------------------------------------------");

		System.out.println("statemenID: " + selectedRC);
		System.out.println("xxxx---------------------------------------------------------------------");

		JSONObject mJsonObj = new JSONObject();

		try {

			return Response.ok(
					SelectData.GETITEMRC(httpServletRequest, selectedRC),
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
	public Response getitemhead(@Context HttpServletRequest httpServletRequest,
			@PathParam("selectedID") String selectedID, @Context HttpHeaders headers, String req) throws JSONException {

		System.out.println("xxxx---------------------------------------------------------------------");

		System.out.println("statemenID: " + selectedID);
		System.out.println("xxxx---------------------------------------------------------------------");

		JSONObject mJsonObj = new JSONObject();

		try {

			return Response.ok(
					SelectData.GETITEMHEAD(httpServletRequest, selectedID),
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
					UpdateData.deleteidmove(getCono, ID, GROUP_ID),
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
					UpdateData.deleteid(getCono, getDivi, ID, BM_CONO, BM_PARENT, GROUP_ID, H_RNNO, CHECKTYPE),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	//////////////////////// master //////////////////////////

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
					UpdateData.savehead(H_CONO, H_DIVI, H_RNNO, H_RCNO, H_CUNO, H_PYNO, H_STS, H_VCNO, H_LOCATION,
							H_TYPE),
					MediaType.APPLICATION_JSON + ";charset=utf8").build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	/*
	 * 
	 * 
	 * @GET
	 * 
	 * @Path("/getwf")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response getWF(@Context HttpHeaders headers, String req) throws
	 * JSONException {
	 * logger.info("/company");
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * //String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	 * 
	 * try {
	 * return Response.ok(SelectData.getWF(), MediaType.APPLICATION_JSON +
	 * ";charset=utf8").build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e);
	 * logger.error(e.getMessage());
	 * }
	 * 
	 * return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * //https://210.1.14.22:8444/bank_mapping_api/data/getcurrentID
	 * 
	 * @GET
	 * 
	 * @Path("/getcurrentID")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response getcurrentID(@Context HttpHeaders headers, String req) throws
	 * JSONException {
	 * logger.info("/getpayer");
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * // System.out.println("getToken: " + getToken);
	 * 
	 * //String getCono = "10";
	 * //String getDivi = "101";
	 * 
	 * 
	 * // String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	 * // String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
	 * // System.out.println("getToken: " + getToken);
	 * 
	 * /*
	 * 
	 * JSONObject dataObject = new JSONObject(getTokenData);
	 * boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));
	 * 
	 * JSONObject getDataObject = dataObject.getJSONObject("body");
	 * String getSubject[] = getDataObject.getString("sub").split(" : ");
	 * String getCono = getSubject[0];
	 * String getDivi = getSubject[1];
	 * String getCompanyName = getSubject[2];
	 * String getUsername = getDataObject.getString("aud");
	 * String getAuth = getDataObject.getString("role");
	 * 
	 * 
	 * 
	 * 
	 * 
	 * try {
	 * return Response
	 * .ok(SelectData.getcurrentID(), MediaType.APPLICATION_JSON + ";charset=utf8")
	 * .build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * logger.error(e.getMessage());
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * //https://210.1.14.22:8444/bank_mapping_api/data/getcurrentID
	 * 
	 * @GET
	 * 
	 * @Path("/getSTATUSID/{vID}")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response getSTATUSID(@Context HttpHeaders headers, String
	 * req, @PathParam("vID") String vID) throws JSONException {
	 * logger.info("/getSTATUSID");
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * 
	 * 
	 * 
	 * try {
	 * return Response
	 * .ok(SelectData.getSTATUSID(vID), MediaType.APPLICATION_JSON +
	 * ";charset=utf8")
	 * .build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * logger.error(e.getMessage());
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	 * 
	 * }
	 * 
	 */

	/////////////////////////////////////////////////////////// REAL WF

	// 25000140

	/*
	 * @GET
	 * 
	 * @Path("/resendemail/{cono}/{divi}/")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response getSTATUSIDITEMRQ(@Context HttpHeaders headers, String
	 * req, @PathParam("vID") String vID)
	 * throws JSONException {
	 * logger.info("/getSTATUSID");
	 */
	@POST
	@Path("/resendemail")
	@Consumes(MediaType.MULTIPART_FORM_DATA)

	public Response resendemail(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("cono") String cono, @FormDataParam("divi") String divi,
			@FormDataParam("serviceno") String serviceno)
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println(cono);
		System.out.println(divi);
		System.out.println(serviceno);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		try {

			return Response
					.ok(UpdateData.resendemail(cono, divi, serviceno), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	@POST
	@Path("/updateITEMREQUEST")
	@Consumes(MediaType.MULTIPART_FORM_DATA)

	public Response updateITEMREQUEST(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vID") String vID, @FormDataParam("vSTATUS") String vSTATUS,
			@FormDataParam("vData") String vData, @FormDataParam("vApproval") String vApproval,
			@FormDataParam("vApprover") String vApprover, @FormDataParam("vDepthead") String vDepthead,
			@FormDataParam("vRemark") String vRemark)
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

			return Response.ok(UpdateData.updateITEMREQUEST(vID, vSTATUS, vData,
					vApproval, vApprover, vDepthead, vRemark), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}

	/////// N8N ///////////////////////

	@POST
	@Path("/testwebhook/jsonbody")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response testWebhookJsonBody(@Context HttpHeaders headers, String req) throws JSONException {
		logger.info("/testwebhook/jsonbody");

		JSONObject mJsonObj = new JSONObject();
		try {
			return Response
					.ok(SelectData.testWebhookJsonBody(),
							MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(mJsonObj).build();

	}

	//////////////////////////////////

	@POST
	@Path("/insertTEST")
	@Consumes(MediaType.MULTIPART_FORM_DATA)

	public Response insertTEST(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vData") String vData, @FormDataParam("username") String username,
			@FormDataParam("depthead") String depthead, @FormDataParam("remark") String remark)
			throws JSONException {
		logger.info("/insertRQ");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		try {

			return Response
					.ok(InsertData.insertTEST(vData, username, depthead), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
//	@POST
//	@Path("/insertsrm")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	public Response insertSRM(FormDataMultiPart formData,@Context HttpServletRequest request)
//			 {
//
//
//		try {
//			logger.info("/insertSRM");
//
//			JSONObject mJsonObj = new JSONObject();
//
//			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
//
//			  String vData = formData.getField("vData").getValue();
//		         String username = formData.getField("username").getValue();
//		         String depthead = formData.getField("depthead").getValue();
//		         String remark = formData.getField("remark").getValue();
//		        
//			JSONObject obj = new JSONObject(vData);
//
//			String company = obj.optString("company");
//			 String constantSoftwareType =obj.optString("constantSoftwareType");
////				System.out.println("Softwaretype"+constantSoftwareType);
//			
//		
//			Map<String, String[]> companyMapping = new HashMap<>();
//			companyMapping.put("10", new String[] { "10", "101" });
//			companyMapping.put("600", new String[] { "600", "600" });
//			companyMapping.put("500", new String[] { "500", "500" });
//			String[] mapping = companyMapping.getOrDefault(company, new String[] { company, company });
//			String comcono = mapping[0];
//			String comdivi = mapping[1];
//
//			String result = 
//					(InsertData.prepareInsertSRM(vData, username, depthead,constantSoftwareType));
//					
//
//			List<FormDataBodyPart> fileParts = formData.getFields("files");
//	         List<FormDataBodyPart> fieldNameParts = formData.getFields("fieldnames");
//
//	         JSONArray outputs = new JSONArray();
//
//	         if (fileParts != null && !fileParts.isEmpty()) {
//
////	             String filePath = request.getRealPath("/") + "WEB-INF/image/";
//	    String filePath = "D:\\files\\api_project\\software_files\\"; // Window
////	    String filePath = "/home/wattana/files/api_project/supplier_files"; // Ubuntu 
//
//	             // ⬅ เรียกฟังก์ชันที่แยกออกมา
//	             outputs = FileUtillity.saveUploadedFiles(
//	                     fileParts,
//	                     fieldNameParts,
//	                     filePath,
//	                      comcono,
//	                comdivi,
//	                result,
//	                username
//	             );
//	         }
//		
//
//		
//
//	         JSONObject resp = new JSONObject();
//	         resp.put("REQUEST COMPLETE SERVICE ID : ", result);
//	        // resp.put("files", outputs);
//
//	         return Response.ok(resp.toString()).build();
//		 } catch (Exception e) {
//		        e.printStackTrace();
//		        logger.error(e.getMessage()); 
//		        return Response.status(Response.Status.BAD_REQUEST)
//		                .entity(e.getMessage())
//		                .build();
//		    }
//
//	}
	
	
	
	
	
	
	@PUT
	@Path("/updatesrm")
	@Consumes(MediaType.MULTIPART_FORM_DATA)

	public Response updateSWRQ(@Context HttpHeaders headers, @Context HttpServletRequest httpServletRequest,
			@FormDataParam("vID") String vID, @FormDataParam("vSTATUS") String vSTATUS,
			@FormDataParam("vData") String vData, @FormDataParam("vApproval") String vApproval,
			@FormDataParam("vApprover") String vApprover, @FormDataParam("vDepthead") String vDepthead,
			@FormDataParam("vRemark") String vRemark)
			throws JSONException {
		logger.info("/updatesrm");

		JSONObject mJsonObj = new JSONObject();

		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println(vSTATUS);
		System.out.println(vID);
		System.out.println(vApproval);
		System.out.println(vData);
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");

		try {

			return Response.ok(UpdateData.prepareUpdateSWRQ(vID, vSTATUS, vData,
					vApproval, vApprover, vDepthead, vRemark), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	@POST
	@Path("/softwareform")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addSurveyForm(@Context HttpHeaders headers, @FormDataParam("vServiceName") String vServiceName,
			@FormDataParam("vStatus") String vStatus, @FormDataParam("vRequestType") String vRequestType,
			@FormDataParam("vRequestDate") String vRequestDate, @FormDataParam("vUsername") String vUsername,
			@FormDataParam("vDepthead") String vDepthead, @FormDataParam("vApproval") String vApproval, @FormDataParam("vReason") String vReason,
			@FormDataParam("vData") String vData, @FormDataParam("vFile") List<FormDataBodyPart> vFile,
			@FormDataParam("vFileName") List<FormDataBodyPart> vFileName,
			@FormDataParam("vFieldName") List<FormDataBodyPart> vFieldName)
			throws JSONException {
		logger.info("/softwareform");
		logger.info("/create");
		logger.info("vData {}", vData);
		logger.info("vFile {}, vFileName {}, vFieldName {}", vFile, vFileName, vFieldName);

		JSONObject mJsonObj = new JSONObject();
		String getToken = headers.getRequestHeaders().getFirst("x-access-token");
		// System.out.println("getToken: " + getToken);

		if (getToken != null && !getToken.isEmpty()) {
			String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
			// System.out.println("getTokenData: " + getTokenData);

			JSONObject dataObject = new JSONObject(getTokenData);
			boolean checkToken = Boolean.parseBoolean(dataObject.getString("message"));

			if (checkToken) {
				JSONObject getDataObject = new JSONObject(dataObject.getString("body"));
				String[] getSubject = getDataObject.getString("sub").split(" : ");
				String getCono = getSubject[0];
				String getDivi = getSubject[1];
				String getCompanyName = getSubject[2];
				String getUsername = getDataObject.getString("aud");
				String getAuth = getDataObject.getString("role");

				try {

					return Response.ok(
							InsertData.prepareAddSoftwareForm(getCono, getDivi, vServiceName, vRequestDate, vUsername,
									vDepthead, vRequestType, vReason, vData, vFile, vFileName, vFieldName),
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

		return Response.status(Response.Status.BAD_REQUEST).entity(mJsonObj).build();

	}
	
	@GET
    @Path("/surveyfile/{cono}/{divi}/{servicename}/{serviceno}")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
     @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response getSurveyFile(@Context HttpHeaders headers, @PathParam("cono") String cono,
            @PathParam("divi") String divi,
            @PathParam("servicename") String servicename, @PathParam("serviceno") String serviceno)
            throws JSONException {
        logger.info("/surveyfile/{cono}/{divi}/{servicename}/{serviceno}");

        JSONObject mJsonObj = new JSONObject();
        try {
            return Response.ok(FileUtillity.prepareGetToFileServer(cono, divi, servicename, serviceno),
                    MediaType.APPLICATION_JSON + ";charset=utf8").build();

        } catch (Exception e) {
            mJsonObj.put("result", "nok");
            mJsonObj.put("message", e.getMessage());
            logger.error(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(mJsonObj).build();

    }

	@PUT
    @Path("/softwareform")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    // @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response updateSurveyForm(@Context HttpHeaders headers, @FormDataParam("vCono") String vCono,
            @FormDataParam("vDivi") String vDivi, @FormDataParam("vServiceName") String vServiceName,
            @FormDataParam("vServiceNo") String vServiceNo, @FormDataParam("vRequestType") String vRequestType,
            @FormDataParam("vStatus") String vStatus, @FormDataParam("vData") String vData,
            @FormDataParam("vFile") List<FormDataBodyPart> vFile,
            @FormDataParam("vFileName") List<FormDataBodyPart> vFileName,
            @FormDataParam("vFieldName") List<FormDataBodyPart> vFieldName)
            throws JSONException {
        logger.info("SoftwareForm");
        logger.info("Update");
        logger.info("vData {}", vData);
        logger.info("vFile {}, vFileName {}, vFieldName {}", vFile, vFileName, vFieldName);
        String getToken = headers.getRequestHeaders().getFirst("x-access-token");
        String getTokenData = HttpConnection.httpConnectionCheckToken(getToken);
        // System.out.println("getTokenData: " + getTokenData);
        JSONObject mJsonObj = new JSONObject();

        try {

            return Response.ok(
                
                    UpdateData.prepareUpdateSoftwareForm(vCono, vDivi, vServiceName, vServiceNo, vRequestType, vStatus,
                            vData, vFile, vFileName, vFieldName),
                    MediaType.APPLICATION_JSON + ";charset=utf8").build();

        } catch (Exception e) {
            mJsonObj.put("result", "nok");
            mJsonObj.put("message", e.getMessage());
            logger.error(e.getMessage());
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(mJsonObj).build();

    }
	
	
	@GET
	@Path("/getSTATUSIDSWRQ/{vID}/{cono}/{divi}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSTATUSIDSWRQ(@Context HttpHeaders headers, String req, @PathParam("vID") String vID,
			@PathParam("cono") String cono, @PathParam("divi") String divi)
			throws JSONException {
		logger.info("/getSTATUSID");

		JSONObject mJsonObj = new JSONObject();

		try {
			return Response
					.ok(SelectData.getSTATUSIDSWRQ(vID, cono, divi), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}
	
	
	@GET
	@Path("/getSoftwareCode/{cono}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSoftwareCode(@Context HttpHeaders headers, String req, @PathParam("cono") String cono)
			throws JSONException {
		logger.info("/getSoftwareCode");

		JSONObject mJsonObj = new JSONObject();

		try {
			return Response
					.ok(SelectData.getSoftwareCode(cono), MediaType.APPLICATION_JSON + ";charset=utf8")
					.build();

		} catch (Exception e) {
			mJsonObj.put("result", "nok");
			mJsonObj.put("message", e.getMessage());
			logger.error(e.getMessage());
		}

		return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();

	}



	
	
	////////////

	/*
	 * @POST
	 * 
	 * @Path("/image")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * // @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addImage(@FormDataParam("vImageFile") InputStream
	 * fileInputStream,
	 * 
	 * @FormDataParam("vImageFile") FormDataContentDisposition
	 * fileFormDataContentDisposition,
	 * 
	 * @FormDataParam("vImageName") String vImageName, @Context HttpHeaders headers,
	 * 
	 * @Context HttpServletRequest httpServletRequest) throws JSONException {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	 * // System.out.println("getToken: " + getToken);
	 * 
	 * Jws<Claims> validate = null;
	 * 
	 * if (getToken != null) {
	 * 
	 * try {
	 * 
	 * // jwt verify token
	 * validate = JwtManager.parseToken(getToken);
	 * // System.out.println("validate: " + validate);
	 * String username = validate.getBody().get("aud", String.class);
	 * String company = validate.getBody().get("sub", String.class);
	 * String getCompany[] = company.split(" : ");
	 * String getCono = getCompany[0];
	 * String getDivi = getCompany[1];
	 * String getCompanyName = getCompany[2];
	 * // String getCono = "10";
	 * // String getDivi = "101";
	 * 
	 * String uploadFilePath = null;
	 * String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
	 * 
	 * try {
	 * // System.out.println("getFileName: " +
	 * // fileFormDataContentDisposition.getFileName());
	 * // fileName = fileFormDataContentDisposition.getFileName();
	 * // fileName = "jaonaay";
	 * System.out.println("filePath: " + filePath + vImageName);
	 * 
	 * uploadFilePath = FileUtillity.writeToFileServer(fileInputStream, vImageName,
	 * filePath);
	 * return Response.status(Response.Status.OK).build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e);
	 * 
	 * }
	 * 
	 * } catch (SignatureException e) {
	 * mJsonObj.put("auth", "false");
	 * mJsonObj.put("message", e.getMessage());
	 * 
	 * }
	 * 
	 * } else {
	 * mJsonObj.put("auth", "false");
	 * mJsonObj.put("message", "No token provided");
	 * }
	 * 
	 * return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @POST
	 * 
	 * @Path("/file")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * // @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * public Response addFile(@FormDataParam("vImageFile") InputStream
	 * fileInputStream,
	 * 
	 * @FormDataParam("vImageFile") FormDataContentDisposition
	 * fileFormDataContentDisposition,
	 * 
	 * @FormDataParam("vImageName") String vImageName, @Context HttpHeaders headers,
	 * 
	 * @Context HttpServletRequest httpServletRequest) throws JSONException {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * String getToken = headers.getRequestHeaders().getFirst("x-access-token");
	 * 
	 * 
	 * String uploadFilePath = null;
	 * String filePath = httpServletRequest.getRealPath("/") + "WEB-INF\\image\\";
	 * 
	 * try {
	 * 
	 * System.out.println("filePath: " + filePath + vImageName);
	 * 
	 * uploadFilePath = FileUtillity.writeToFileServer(fileInputStream, vImageName,
	 * filePath);
	 * return Response.status(Response.Status.OK).build();
	 * 
	 * } catch (Exception e) {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * return Response.status(Response.Status.NOT_FOUND).entity(mJsonObj).build();
	 * 
	 * }
	 * 
	 * 
	 */

	//////////////////// Todo --image upload
//
//	@POST
//	@Path("/uploadimage")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response uploadFiles(
//			@FormDataParam("file") InputStream uploadedInputStream,
//			@FormDataParam("file") FormDataContentDisposition fileDetail,
//			@Context HttpServletRequest request) throws JSONException {
//
//		JSONObject result = new JSONObject();
//
//		try {
//			// สร้างโฟลเดอร์
//			String uploadDir = request.getServletContext().getRealPath("/") + "WEB-INF/upload/";
//			File uploadFolder = new File(uploadDir);
//			if (!uploadFolder.exists()) {
//				uploadFolder.mkdirs();
//			}
//
//			System.out.print(uploadDir);
//
//			// ตั้งชื่อไฟล์ใหม่ให้ปลอดภัย
//			String fileName = fileDetail.getFileName();
//			String safeFileName = UUID.randomUUID().toString() + "_" + fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
//
//			File targetFile = new File(uploadDir + File.separator + safeFileName);
//
//			System.out.print(fileName);
//			System.out.print(safeFileName);
//			System.out.print(targetFile);
//
//			// เขียนไฟล์ลงเซิร์ฟเวอร์
//			Files.copy(uploadedInputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//			// สร้าง URL ส่งกลับ
//			String fileUrl = "https://yourdomain.com/api/file/" + safeFileName;
//			result.put("url", fileUrl);
//			result.put("status", "success");
//
//			return Response.ok(result.toString()).build();
//
//		} catch (Exception e) {
//			result.put("status", "error");
//			result.put("message", e.getMessage());
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.toString()).build();
//		}
//	}

	////////////

	/*
	 * @POST
	 * 
	 * @Path("/uploadTempFiles")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * public Response uploadfile(
	 * 
	 * @FormDataParam("vData") String vData,
	 * 
	 * @FormDataParam("username") String username,
	 * 
	 * @FormDataParam("depthead") String depthead,
	 * 
	 * @FormDataParam("remark") String remark) {
	 * 
	 * JSONObject mJsonObj = new JSONObject();
	 * 
	 * try {
	 * // สมมติเรียกฟังก์ชันประมวลผลและบันทึกไฟล์/ข้อมูล
	 * // JSONObject result = InsertData.uploadTempFiles(vData, username, depthead);
	 * 
	 * JSONObject result = null;
	 * 
	 * // ส่งผลลัพธ์กลับ (JSON + charset UTF-8)
	 * return Response.ok(result.toString(), MediaType.APPLICATION_JSON +
	 * ";charset=utf-8").build();
	 * 
	 * } catch (Exception e) {
	 * try {
	 * mJsonObj.put("result", "nok");
	 * mJsonObj.put("message", e.getMessage());
	 * } catch (JSONException jsonException) {
	 * // handle exception เพิ่มเติมถ้าต้องการ
	 * }
	 * return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	 * .entity(mJsonObj.toString())
	 * .type(MediaType.APPLICATION_JSON + ";charset=utf-8")
	 * .build();
	 * }
	 * }
	 * 
	 * 
	 * 
	 * 
	 * private void saveToFile(InputStream uploadedInputStream, String
	 * uploadedFileLocation) throws IOException {
	 * try (OutputStream out = new FileOutputStream(new File(uploadedFileLocation)))
	 * {
	 * int read;
	 * byte[] bytes = new byte[1024];
	 * while ((read = uploadedInputStream.read(bytes)) != -1) {
	 * out.write(bytes, 0, read);
	 * }
	 * }
	 * }
	 * 
	 */


	/////////////

	////////////////

}
