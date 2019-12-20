package com.almis.awe.scheduler.constant;

/**
 *
 * @author dfuentes
 */
public class ServerConstants {

  private ServerConstants(){}

  // Parameter names
  public static final String SERVER_NAME = "Nom";
  public static final String SERVER_HOST = "Hst";
  public static final String SERVER_PORT = "Prt";
  public static final String SERVER_PROTOCOL = "Pro";
  public static final String SERVER_ACTIVE = "Act";
  public static final String SERVER_IDE = "LchSrv";
  public static final String SERVER_PATH = "LchPth";
  public static final String SERVER_PATTERN = "LchPat";
  public static final String SERVER_USER = "LchUsr";
  public static final String SERVER_PASS = "LchPwd";

  // Types of connection
  public static final String HTTP = "http";
  public static final String HTTPS = "https";
  public static final String FTP = "ftp";
  public static final String SSH = "ssh";

  // The default ports to take for the different types of protocols
  public static final int DEFAULT_HTTP_PORT = 80;
  public static final int DEFAULT_HTTPS_PORT = 443;
  public static final int DEFAULT_FTP_PORT = 21;
  public static final int DEFAULT_SSH_PORT = 22;
}
