/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.model.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * WebSettings component
 * 
 * @author pgarcia
 */
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
  private Boolean shareSessionInTabs;
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

  /**
   * @return the pathServer
   */
  public String getPathServer() {
    return pathServer;
  }

  /**
   * @param pathServer the pathServer to set
   */
  public void setPathServer(String pathServer) {
    this.pathServer = pathServer;
  }

  /**
   * Get the initial url
   * 
   * @return Initial url
   */
  public String getInitialURL() {
    return initialURL;
  }

  /**
   * Set the initial url
   * 
   * @param initialURL Initial url
   * @return this
   */
  public WebSettings setInitialURL(String initialURL) {
    this.initialURL = initialURL;
    return this;
  }

  /**
   * @return the language
   */
  public String getLanguage() {
    return this.language;
  }

  /**
   * @param language the language to set
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * @return the theme
   */
  public String getTheme() {
    return this.theme;
  }

  /**
   * @param theme the theme to set
   */
  public void setTheme(String theme) {
    this.theme = theme;
  }

  /**
   * @return the charset
   */
  public String getCharset() {
    return charset;
  }

  /**
   * @param charset the charset to set
   */
  public void setCharset(String charset) {
    this.charset = charset;
  }

  /**
   * @return the applicationName
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * @param applicationName the applicationName to set
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * @return the dataSuffix
   */
  public String getDataSuffix() {
    return dataSuffix;
  }

  /**
   * @param dataSuffix the dataSuffix to set
   */
  public void setDataSuffix(String dataSuffix) {
    this.dataSuffix = dataSuffix;
  }

  /**
   * @return the homeScreen
   */
  public String getHomeScreen() {
    return homeScreen;
  }

  /**
   * @param homeScreen the homeScreen to set
   */
  public void setHomeScreen(String homeScreen) {
    this.homeScreen = homeScreen;
  }

  /**
   * @return the recordsPerPage
   */
  public Integer getRecordsPerPage() {
    return recordsPerPage;
  }

  /**
   * @param recordsPerPage the recordsPerPage to set
   */
  public void setRecordsPerPage(Integer recordsPerPage) {
    this.recordsPerPage = recordsPerPage;
  }

  /**
   * @return the pixelsPerCharacter
   */
  public Integer getPixelsPerCharacter() {
    return pixelsPerCharacter;
  }

  /**
   * @param pixelsPerCharacter the pixelsPerCharacter to set
   */
  public void setPixelsPerCharacter(Integer pixelsPerCharacter) {
    this.pixelsPerCharacter = pixelsPerCharacter;
  }

  /**
   * @return the defaultComponentSize
   */
  public String getDefaultComponentSize() {
    return defaultComponentSize;
  }

  /**
   * @param defaultComponentSize the defaultComponentSize to set
   */
  public void setDefaultComponentSize(String defaultComponentSize) {
    this.defaultComponentSize = defaultComponentSize;
  }

  /**
   * @return the shareSessionInTabs
   */
  public Boolean getShareSessionInTabs() {
    return shareSessionInTabs;
  }

  /**
   * @param shareSessionInTabs the shareSessionInTabs to set
   */
  public void setShareSessionInTabs(Boolean shareSessionInTabs) {
    this.shareSessionInTabs = shareSessionInTabs;
  }

  /**
   * @return the reloadCurrentScreen
   */
  public Boolean getReloadCurrentScreen() {
    return reloadCurrentScreen;
  }

  /**
   * @param reloadCurrentScreen the reloadCurrentScreen to set
   */
  public void setReloadCurrentScreen(Boolean reloadCurrentScreen) {
    this.reloadCurrentScreen = reloadCurrentScreen;
  }

  /**
   * @return the suggestTimeout
   */
  public Integer getSuggestTimeout() {
    return suggestTimeout;
  }

  /**
   * @param suggestTimeout the suggestTimeout to set
   */
  public void setSuggestTimeout(Integer suggestTimeout) {
    this.suggestTimeout = suggestTimeout;
  }

  /**
   * @return the connectionProtocol
   */
  public String getConnectionProtocol() {
    return connectionProtocol;
  }

  /**
   * @param connectionProtocol the connectionProtocol to set
   */
  public void setConnectionProtocol(String connectionProtocol) {
    this.connectionProtocol = connectionProtocol;
  }

  /**
   * @return the connectionTransport
   */
  public String getConnectionTransport() {
    return connectionTransport;
  }

  /**
   * @param connectionTransport the connectionTransport to set
   */
  public void setConnectionTransport(String connectionTransport) {
    this.connectionTransport = connectionTransport;
  }

  /**
   * @return the connectionBackup
   */
  public String getConnectionBackup() {
    return connectionBackup;
  }

  /**
   * @param connectionBackup the connectionBackup to set
   */
  public void setConnectionBackup(String connectionBackup) {
    this.connectionBackup = connectionBackup;
  }

  /**
   * @return the connectionTimeout
   */
  public Integer getConnectionTimeout() {
    return connectionTimeout;
  }

  /**
   * @param connectionTimeout the connectionTimeout to set
   */
  public void setConnectionTimeout(Integer connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  /**
   * @return the cometUID
   */
  public String getCometUID() {
    return cometUID;
  }

  /**
   * @param cometUID the cometUID to set
   */
  public void setCometUID(String cometUID) {
    this.cometUID = cometUID;
  }

  /**
   * @return the uploadIdentifier
   */
  public String getUploadIdentifier() {
    return uploadIdentifier;
  }

  /**
   * @param uploadIdentifier the uploadIdentifier to set
   */
  public void setUploadIdentifier(String uploadIdentifier) {
    this.uploadIdentifier = uploadIdentifier;
  }

  /**
   * Retrieve download identifier
   * 
   * @return Download identifier
   */
  public String getDownloadIdentifier() {
    return downloadIdentifier;
  }

  /**
   * Set download identifier
   * 
   * @param downloadIdentifier Download identifier
   * @return this
   */
  public WebSettings setDownloadIdentifier(String downloadIdentifier) {
    this.downloadIdentifier = downloadIdentifier;
    return this;
  }

  /**
   * Get upload max size
   * 
   * @return Upload max size
   */
  public Integer getUploadMaxSize() {
    return uploadMaxSize * MB;
  }

  /**
   * Set upload max size
   * 
   * @param uploadMaxSize Max size (in MB)
   * @return this
   */
  public WebSettings setUploadMaxSize(Integer uploadMaxSize) {
    this.uploadMaxSize = uploadMaxSize;
    return this;
  }

  /**
   * @return the addressIdentifier
   */
  public String getAddressIdentifier() {
    return addressIdentifier;
  }

  /**
   * @param addressIdentifier the addressIdentifier to set
   */
  public void setAddressIdentifier(String addressIdentifier) {
    this.addressIdentifier = addressIdentifier;
  }

  /**
   * @return the passwordPattern
   */
  public String getPasswordPattern() {
    return passwordPattern;
  }

  /**
   * @param passwordPattern the passwordPattern to set
   */
  public void setPasswordPattern(String passwordPattern) {
    this.passwordPattern = passwordPattern;
  }

  /**
   * @return the minlengthPassword
   */
  public Integer getMinlengthPassword() {
    return minlengthPassword;
  }

  /**
   * @param minlengthPassword the minlengthPassword to set
   */
  public void setMinlengthPassword(Integer minlengthPassword) {
    this.minlengthPassword = minlengthPassword;
  }

  /**
   * @return the encodeTransmission
   */
  public Boolean getEncodeTransmission() {
    return encodeTransmission;
  }

  /**
   * @param encodeTransmission the encodeTransmission to set
   */
  public void setEncodeTransmission(Boolean encodeTransmission) {
    this.encodeTransmission = encodeTransmission;
  }

  /**
   * @return the encodeKey
   */
  public String getEncodeKey() {
    return encodeKey;
  }

  /**
   * @param encodeKey the encodeKey to set
   */
  public void setEncodeKey(String encodeKey) {
    this.encodeKey = encodeKey;
  }

  /**
   * @return the tokenKey
   */
  public String getTokenKey() {
    return tokenKey;
  }

  /**
   * @param tokenKey the tokenKey to set
   */
  public void setTokenKey(String tokenKey) {
    this.tokenKey = tokenKey;
  }

  /**
   * @return the actionsStack
   */
  public Integer getActionsStack() {
    return actionsStack;
  }

  /**
   * @param actionsStack the actionsStack to set
   */
  public void setActionsStack(Integer actionsStack) {
    this.actionsStack = actionsStack;
  }

  /**
   * @return the debug
   */
  public String getDebug() {
    return debug;
  }

  /**
   * @param debug the debug to set
   */
  public void setDebug(String debug) {
    this.debug = debug;
  }

  /**
   * @return the loadingTimeout
   */
  public Integer getLoadingTimeout() {
    return loadingTimeout;
  }

  /**
   * @param loadingTimeout the loadingTimeout to set
   */
  public void setLoadingTimeout(Integer loadingTimeout) {
    this.loadingTimeout = loadingTimeout;
  }


  /**
   * @return the helpTimeout
   */
  public Integer getHelpTimeout() {
    return helpTimeout;
  }

  /**
   * @param helpTimeout the helpTimeout to set
   */
  public void setHelpTimeout(Integer helpTimeout) {
    this.helpTimeout = helpTimeout;
  }

  /**
   * @return the messageTimeout
   */
  public WebTooltip getMessageTimeout() {
    return messageTimeout;
  }

  /**
   * @param messageTimeout the messageTimeout to set
   */
  @Autowired
  public void setMessageTimeout(WebTooltip messageTimeout) {
    this.messageTimeout = messageTimeout;
  }

  /**
   * @return the numericOptions
   */
  public WebNumberOptions getNumericOptions() {
    return numericOptions;
  }

  /**
   * @param numericOptions the numericOptions to set
   */
  @Autowired
  public void setNumericOptions(WebNumberOptions numericOptions) {
    this.numericOptions = numericOptions;
  }

  /**
   * @return the pivotOptions
   */
  public WebPivotOptions getPivotOptions() {
    return pivotOptions;
  }

  /**
   * @param pivotOptions the pivotOptions to set
   */
  @Autowired
  public void setPivotOptions(WebPivotOptions pivotOptions) {
    this.pivotOptions = pivotOptions;
  }

  /**
   * @return the chartOptions
   */
  public WebChartOptions getChartOptions() {
    return chartOptions;
  }

  /**
   * @param chartOptions the chartOptions to set
   */
  @Autowired
  public void setChartOptions(WebChartOptions chartOptions) {
    this.chartOptions = chartOptions;
  }
}
