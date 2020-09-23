package com.almis.awe.model.settings;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebSettings component
 *
 * @author pgarcia
 */
@Getter
@Setter
@Accessors(chain = true)
@Builder(toBuilder = true)
@ConfigurationProperties(prefix = "settings")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSettings {

  static final Integer MB = 1024 * 1024;

  private String pathServer;
  private String initialURL;
  private String language;
  private String theme;
  private String charset;
  private String applicationName;
  private String dataSuffix;
  private String homeScreen;
  private Integer recordsPerPage;
  private Integer pixelsPerCharacter;
  private String defaultComponentSize;
  private Boolean reloadCurrentScreen;
  private Integer suggestTimeout;

  // Connection
  private String connectionProtocol;
  private String connectionTransport;
  private String connectionBackup;
  private Integer connectionTimeout;
  private String cometUID;
  // Upload
  private String uploadIdentifier;
  private String downloadIdentifier;
  private Integer uploadMaxSize;
  private String addressIdentifier;
  // Security
  private String passwordPattern;
  private Integer minlengthPassword;
  private Boolean encodeTransmission;
  private String encodeKey;
  private String tokenKey;
  // Debug
  private Integer actionsStack;
  private String debug;
  // Loading timeout
  private Integer loadingTimeout;
  // Help timeout
  private Integer helpTimeout;
  // Message timeouts
  private WebTooltip messageTimeout;
  // Number options
  private WebNumberOptions numericOptions;
  // Pivot options
  private WebPivotOptions pivotOptions;
  // Chart options
  private WebChartOptions chartOptions;
}
