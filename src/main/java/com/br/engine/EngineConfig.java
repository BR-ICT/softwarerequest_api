package com.br.engine;

import java.io.InputStream;
import java.util.Properties;

/**
 * The keys of config.properties (src/main/resources/env/{dev|uat|prd}) the engine reads. Every form API that
 * carries the engine needs them:
 *
 * DBNAME         AS400 library of the SR_* tables
 * DBM3NAME       M3 library (CMNDIV: company name on the form)
 * WORKFLOW       n8n webhook that emails the approver of the next status
 * WORKFLOWTOKEN  the x-access-token n8n expects on that webhook
 * FILEBASEDIR    folder that holds the *_files folders (prd: the share mounted on Ubuntu)
 * FILEFOLDER     this API's folder under FILEBASEDIR, e.g. servicerequest_files, itemrequest_files
 */
public final class EngineConfig {

	public static final String DBNAME;
	public static final String DBM3NAME;
	public static final String WORKFLOW;
	public static final String WORKFLOWTOKEN;
	public static final String FILEBASEDIR;
	public static final String FILEFOLDER;

	static {
		Properties props = new Properties();
		try (InputStream is = EngineConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (is == null) throw new RuntimeException("config.properties not found in classpath");
			props.load(is);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
		DBNAME = props.getProperty("DBNAME");
		DBM3NAME = props.getProperty("DBM3NAME");
		WORKFLOW = props.getProperty("WORKFLOW");
		WORKFLOWTOKEN = props.getProperty("WORKFLOWTOKEN", "");
		FILEBASEDIR = props.getProperty("FILEBASEDIR");
		FILEFOLDER = props.getProperty("FILEFOLDER");
	}

	private EngineConfig() {
	}
}
