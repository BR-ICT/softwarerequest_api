package com.br.utility;

import java.io.InputStream;
import java.util.Properties;

public class Constant {

    public static final String PROFILENAME;
    public static final String MNELOGONURL;
    public static final int APPPORT;
    public static final String APPSERVER;
    public static final String DBNAME;
    public static final String DBM3NAME;
    public static final String WORKFLOW;
    public static final String CHECKTOKENURL;
    public static final String GENTOKENURL;
    public static final String GENTOKENWOTOKENURL;

    static {
        Properties props = new Properties();
        try (InputStream is = Constant.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("config.properties not found in classpath");
            props.load(is);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
        PROFILENAME = props.getProperty("PROFILENAME");
        MNELOGONURL = props.getProperty("MNELOGONURL");
        APPPORT = Integer.parseInt(props.getProperty("APPPORT"));
        APPSERVER = props.getProperty("APPSERVER");
        DBNAME = props.getProperty("DBNAME");
        DBM3NAME = props.getProperty("DBM3NAME");
        WORKFLOW = props.getProperty("WORKFLOW");
        CHECKTOKENURL = props.getProperty("CHECKTOKENURL");
        GENTOKENURL = props.getProperty("GENTOKENURL");
        GENTOKENWOTOKENURL = props.getProperty("GENTOKENWOTOKENURL");
    }
}
