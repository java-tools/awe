package com.almis.awe.model.entities.queries;

import com.almis.awe.model.dto.CellData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * DatabaseConnectionInfo Class
 *
 * Bean class with message queue connection info
 *
 *
 * @author Pablo GARCIA - 03/Aug/2017
 */
@Data
@Accessors(chain = true)
public class DatabaseConnectionInfo {
  private String alias;
  private String description;
  private String connectionType;
  private String databaseType;
  private String url;
  private String user;
  private String password;
  private String driver;
  private String jndi;
  private String environment;

  /**
   * Constructor
   */
  public DatabaseConnectionInfo() {
    super();
  }

  /**
   * Constructor from database row
   *
   * @param connectionInfo Database row
   */
  public DatabaseConnectionInfo(Map<String, CellData> connectionInfo) {
    super();
    alias = connectionInfo.get("alias").getStringValue();
    description = connectionInfo.get("description").getStringValue();
    connectionType = connectionInfo.get("connectionType").getStringValue();
    databaseType = connectionInfo.get("databaseType").getStringValue();
    environment = connectionInfo.get("environment").getStringValue();
    url = connectionInfo.get("url").getStringValue();
    user = connectionInfo.get("user").getStringValue();
    password = connectionInfo.get("password").getStringValue();
    driver = connectionInfo.get("driver").getStringValue();
    jndi = "D".equalsIgnoreCase(connectionType) ? "java:comp/env/" + url : null;
  }
}
