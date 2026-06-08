package com.br.utility;

public class Constant {

	// Production
//	 public static String PROFILENAME = "PRD"; // PRD
//	 public static String MNELOGONURL = "https://bkrmvxm3.bangkokranch.com:21008/mne/servlet/MvxMCSvt"; // PRD
//	 public static int APPPORT = 16105; // PRD
//	 public static String APPSERVER = "192.200.9.190";
//	 public static String DBNAME = "BRLDTA0100"; // PRD
//	 public static String DBM3NAME = "M3FDBPRD"; // PRD
//	 public static String WORKFLOW = "https://workflow.br-bangkokranch.com/webhook/service-request"; // PRD

	// Development
	public static String PROFILENAME = "TST"; // TST
	public static String MNELOGONURL = "https://bkrmvxm3.bangkokranch.com:22008/mne/servlet/MvxMCSvt"; // TST
	public static int APPPORT = 16305; // TST
	public static String APPSERVER = "192.200.9.190";
	public static String DBNAME = "BRLDTABK01"; // TST
	public static String DBM3NAME = "M3FDBTST"; // TST
	public static String WORKFLOW = "https://workflow.br-bangkokranch.com/webhook/tst/service-request"; // TST

	// Localhost
	public static String CHECKTOKENURL = "http://localhost:8080/authen_api/auth/checktoken";
	public static String GENTOKENURL = "http://localhost:8080/authen_api/auth/gentoken";
	public static String GENTOKENWOTOKENURL = "http://localhost:8080/authen_api/auth/gentokenwotoken";
	
	// Server
//	public static String CHECKTOKENURL = "http://192.200.9.186:8080/authen_api/auth/checktoken";
//	public static String GENTOKENURL = "http://192.200.9.186:8080/authen_api/auth/gentoken";
//	public static String GENTOKENWOTOKENURL = "http://192.200.9.186:8080/authen_api/auth/gentokenwotoken";
	
	// Domain
//	public static String CHECKTOKENURL = "https://service.br-bangkokranch.com/authen_api/auth/v2/checktoken";
//	public static String GENTOKENURL = "https://service.br-bangkokranch.com/authen_api/auth/v2/gentoken";
//	public static String GENTOKENWOTOKENURL = "https://service.br-bangkokranch.com/authen_api/auth/v2/gentokenwotoken";

}
