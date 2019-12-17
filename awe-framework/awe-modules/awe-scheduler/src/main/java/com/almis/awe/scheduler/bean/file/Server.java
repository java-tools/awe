package com.almis.awe.scheduler.bean.file;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

import static com.almis.awe.scheduler.constant.ServerConstants.*;

/**
 * @author dfuentes
 */
@Data
@Accessors(chain = true)
public class Server implements Serializable {
  // Server identifier
  private Integer serverId = null;
  // Server name
  private String name = null;
  // Server ip
  private String host = null;
  // Protocol to connect to server
  private String typeOfConnection = null;
  // Port to use when connecting to server
  private Integer port;
  // Server is active
  boolean active = false;

  public int getPort() {
    return port == null ? getPortForProtocol(0, getTypeOfConnection()) : port;
  }

  /**
   * Set the default port for each protocol if the port from the configuration
   * is not valid
   *
   * @param port
   * @param protocol
   * @return integer
   */
  private int getPortForProtocol(int port, String protocol) {
    if (port <= 0) {
      // Set default port as HTTP port
      switch (protocol.toLowerCase()) {
        case HTTPS:
          return DEFAULT_HTTPS_PORT;
        case FTP:
          return DEFAULT_FTP_PORT;
        case SSH:
          return DEFAULT_SSH_PORT;
        case HTTP:
        default:
          return DEFAULT_HTTP_PORT;
      }
    }
    return port;
  }
}
