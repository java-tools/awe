package com.almis.awe.scheduler.enums;

/**
 * @author dfuentes
 */
public enum ServerConnectionType {
  // Types of connection
  HTTP("http", 80),
  HTTPS("https", 443),
  FTP("ftp", 21),
  SSH("ssh", 22),
  FOLDER("folder", null);

  private final String name;
  private final Integer port;

  /**
   * Generate server connection
   *
   * @param name Name
   * @param port Port
   */
  ServerConnectionType(String name, Integer port) {
    this.name = name;
    this.port = port;
  }

  /**
   * Retrieve enumerated name
   *
   * @return Name
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieve enumerated port
   *
   * @return Port
   */
  public Integer getPort() {
    return port;
  }
}
