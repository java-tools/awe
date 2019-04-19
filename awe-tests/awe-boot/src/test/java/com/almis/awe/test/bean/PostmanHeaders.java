package com.almis.awe.test.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostmanHeaders {

  private String host;
  private String accept;
  private String acceptEncoding;
  private String acceptLanguage;
  private String cacheControl;
  private String cookie;
  private String postmanToken;
  private String userAgent;
  private String xForwardedPort;
  private String xForwardedProto;

  @JsonProperty("host")
  public String getHost() {
    return host;
  }

  public PostmanHeaders setHost(String host) {
    this.host = host;
    return this;
  }

  @JsonProperty("accept")
  public String getAccept() {
    return accept;
  }

  public PostmanHeaders setAccept(String accept) {
    this.accept = accept;
    return this;
  }

  @JsonProperty("accept-encoding")
  public String getAcceptEncoding() {
    return acceptEncoding;
  }

  public PostmanHeaders setAcceptEncoding(String acceptEncoding) {
    this.acceptEncoding = acceptEncoding;
    return this;
  }

  @JsonProperty("accept-language")
  public String getAcceptLanguage() {
    return acceptLanguage;
  }

  public PostmanHeaders setAcceptLanguage(String acceptLanguage) {
    this.acceptLanguage = acceptLanguage;
    return this;
  }

  @JsonProperty("cache-control")
  public String getCacheControl() {
    return cacheControl;
  }

  public PostmanHeaders setCacheControl(String cacheControl) {
    this.cacheControl = cacheControl;
    return this;
  }

  @JsonProperty("cookie")
  public String getCookie() {
    return cookie;
  }

  public PostmanHeaders setCookie(String cookie) {
    this.cookie = cookie;
    return this;
  }

  @JsonProperty("postman-token")
  public String getPostmanToken() {
    return postmanToken;
  }

  public PostmanHeaders setPostmanToken(String postmanToken) {
    this.postmanToken = postmanToken;
    return this;
  }

  @JsonProperty("user-agent")
  public String getUserAgent() {
    return userAgent;
  }

  public PostmanHeaders setUserAgent(String userAgent) {
    this.userAgent = userAgent;
    return this;
  }

  @JsonProperty("x-forwarded-port")
  public String getxForwardedPort() {
    return xForwardedPort;
  }

  public PostmanHeaders setxForwardedPort(String xForwardedPort) {
    this.xForwardedPort = xForwardedPort;
    return this;
  }

  @JsonProperty("x-forwarded-proto")
  public String getxForwardedProto() {
    return xForwardedProto;
  }

  public PostmanHeaders setxForwardedProto(String xForwardedProto) {
    this.xForwardedProto = xForwardedProto;
    return this;
  }
}
