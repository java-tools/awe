package com.almis.awe.autoconfigure;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * Class used to start AWE as a microservice
 */
@Configuration
public class RestConfig {

  @Value("${rest.request.timeout:5}")
  private Integer requestTimeout;

  @Value("${rest.connection.timeout:5}")
  private Integer connectionTimeout;

  /**
   * Define client http request factory bean
   * @return Client http request factory
   */
  @Bean
  public ClientHttpRequestFactory httpComponentsClientHttpRequestFactory() {
    CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);
    requestFactory.setConnectionRequestTimeout(requestTimeout * 1000);
    requestFactory.setConnectTimeout(connectionTimeout * 1000);
    return requestFactory;
  }
}
