package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.dto.ServiceDataWrapper;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.services.AbstractServiceRest;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.type.RestContentType;
import com.almis.awe.model.util.log.LogUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Launches a Rest service
 */
public abstract class AbstractRestConnector extends AbstractServiceConnector {

  // Autowired services
  private LogUtil logger;

  /**
   * Autowired constructor
   * @param logger Logger
   */
  @Autowired
  public AbstractRestConnector(LogUtil logger) {
    this.logger = logger;
  }

  /**
   * Generates the request and launches it
   *
   * @param url                  where the rest service is located
   * @param service              object information
   * @param paramsMapFromRequest parameter values received
   * @return response
   * @throws AWException Error launching request
   */
  protected ServiceData doRequest(String url, AbstractServiceRest service, Map<String, Object> paramsMapFromRequest) throws AWException {

    // Define URI builder
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);

    // Define request manager
    CloseableHttpClient httpClient = HttpClients.custom()
      .setSSLHostnameVerifier(new NoopHostnameVerifier())
      .build();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);
    RestTemplate restTemplate = new RestTemplate(requestFactory);

    // Define request object
    HttpEntity request;

    // Define response object
    ResponseEntity<? extends ResponseWrapper> response;

    // If url has wildcards, retrieve values from parameters
    Map<String, Object> urlParameters = new HashMap<>();
    if (service.getParameterList() != null) {
      urlParameters = new HashMap<>();

      Pattern pattern = Pattern.compile("\\{([\\w]+)\\}");
      Matcher matcher = pattern.matcher(url);

      while (matcher.find()) {
        String wildCard = matcher.group(1);
        urlParameters.put(wildCard, paramsMapFromRequest.get(wildCard));
      }
    }

    // Create request using headers and parameters defined previously
    try {
      request = generateRequest(service, uriBuilder, urlParameters, paramsMapFromRequest);
    } catch (JsonProcessingException exc) {
      throw new AWException("Error processing parameters", exc);
    }

    // Build the url
    String finalUrl = uriBuilder.build().toString();

    // Retrieve the wrapper class to handle the response
    Class<? extends ResponseWrapper> wrapper = ServiceDataWrapper.class;
    if (service.getWrapper() != null) {
      try {
        wrapper = (Class<? extends ResponseWrapper>) Class.forName(service.getWrapper());
      } catch (Exception e) {
        throw new AWException("Wrapper provided could not be found or does not implement ResponseWrapper", e);
      }
    }

    // Do request
    logger.log(AbstractRestConnector.class, Level.INFO, "Doing {0} request to url {1}", service.getMethod(), finalUrl);
    try {
      response = restTemplate.exchange(finalUrl, HttpMethod.valueOf(service.getMethod()), request, wrapper, urlParameters);
    } catch (Exception e) {
      throw new AWException("Request failed", e);
    }

    // Handle response status
    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new AWException(String.format("Operation unsuccessful %d", response.getStatusCodeValue()));
    }

    return response.getBody().toServiceData();
  }

  /**
   * Generate rest request
   *
   * @param rest                 Rest service
   * @param uriBuilder           Url Builder
   * @param urlParameters        Url parameters
   * @param paramsMapFromRequest Request parameters
   * @return Request
   * @throws com.fasterxml.jackson.core.JsonProcessingException
   */
  protected HttpEntity generateRequest(AbstractServiceRest rest, UriComponentsBuilder uriBuilder, Map<String, Object> urlParameters, Map<String, Object> paramsMapFromRequest) throws JsonProcessingException {
    // Define request headers
    HttpHeaders headers = new HttpHeaders();

    RestContentType restContentType = rest.getContentType() != null ? RestContentType.valueOf(rest.getContentType()) : RestContentType.URLENCODED;
    MediaType contentType = restContentType.equals(RestContentType.JSON) ? MediaType.APPLICATION_JSON_UTF8 : MediaType.APPLICATION_FORM_URLENCODED;

    // Set content type
    if (!rest.getMethod().equalsIgnoreCase("GET")) {
      // Define extra headers
      headers.setContentType(contentType);
    }

    // Generate parameters
    if (MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
      return new HttpEntity<>(getParameterMap(rest, uriBuilder, urlParameters, paramsMapFromRequest), headers);
    } else {
      return new HttpEntity<>(getParametersJson(rest, uriBuilder, urlParameters, paramsMapFromRequest), headers);
    }
  }

  /**
   * Generate parameters as MAP
   * @param rest Rest service
   * @param uriBuilder URI builder
   * @param urlParameters URL parameters
   * @param paramsMapFromRequest Parameters from request
   * @return Entity
   */
  private MultiValueMap<String, String> getParameterMap(AbstractServiceRest rest, UriComponentsBuilder uriBuilder, Map<String, Object> urlParameters, Map<String, Object> paramsMapFromRequest) {
    MultiValueMap<String, String> requestParametersMap = new LinkedMultiValueMap<>();

    if (rest.getParameterList() != null) {
      for (ServiceInputParameter param : rest.getParameterList()) {
        String paramName = param.getName();
        if (!urlParameters.containsKey(paramName)) {
          setParametersInURI(rest, uriBuilder, paramName, paramsMapFromRequest);
          readParameterMap(requestParametersMap, param, paramsMapFromRequest);
        }
      }
    }

    // Create request using headers and parameters defined previously
    return requestParametersMap;
  }

  /**
   * Read parameter map
   * @param requestParametersMap Request parameters
   * @param param Parameter to read
   * @param paramsMapFromRequest Parameters from request
   */
  private void readParameterMap(MultiValueMap<String, String> requestParametersMap, ServiceInputParameter param, Map<String, Object> paramsMapFromRequest) {
    // If it has parameters, expand the url avoiding parameters already used
    ObjectMapper mapper = new ObjectMapper();
    String paramName = param.getName();
    JsonNode nodeValue = mapper.valueToTree(paramsMapFromRequest.get(paramName));
    if (param.isList()) {
      for (JsonNode value : nodeValue) {
        requestParametersMap.add(paramName, value.asText());
      }
    } else if (nodeValue == null){
      requestParametersMap.set(paramName, null);
    } else {
      requestParametersMap.set(paramName, nodeValue.asText());
    }
  }

  /**
   * Generate parameters as JSON
   * @param rest Rest service
   * @param uriBuilder URI Builder
   * @param urlParameters URL parameters
   * @param paramsMapFromRequest Parameters from request
   * @return Entity
   * @throws JsonProcessingException Error processing JSON
   */
  private String getParametersJson(AbstractServiceRest rest, UriComponentsBuilder uriBuilder, Map<String, Object> urlParameters, Map<String, Object> paramsMapFromRequest) throws JsonProcessingException {
    ObjectNode requestParametersJson = JsonNodeFactory.instance.objectNode();
    ObjectMapper mapper = new ObjectMapper();
    if (rest.getParameterList() != null) {
      for (ServiceInputParameter param : rest.getParameterList()) {
        // If it has parameters, expand the url avoiding parameters already used
        String paramName = param.getName();
        if (!urlParameters.containsKey(paramName)) {
          setParametersInURI(rest, uriBuilder, paramName, paramsMapFromRequest);
          readParameterJson(requestParametersJson, param, paramsMapFromRequest);
        }
      }
    }

    // Create request using headers and parameters defined previously
    return mapper.writeValueAsString(requestParametersJson);
  }

  /**
   * Read parameter json
   * @param requestParametersJson Request parameters
   * @param param Parameter to read
   * @param paramsMapFromRequest Parameters from request
   */
  private void readParameterJson(ObjectNode requestParametersJson, ServiceInputParameter param, Map<String, Object> paramsMapFromRequest) {
    // If it has parameters, expand the url avoiding parameters already used
    ObjectMapper mapper = new ObjectMapper();
    String paramName = param.getName();
    JsonNode nodeValue = mapper.valueToTree(paramsMapFromRequest.get(paramName));
    nodeValue = nodeValue == null ? JsonNodeFactory.instance.nullNode() : nodeValue;
    if (param.isList()) {
      ArrayNode list = JsonNodeFactory.instance.arrayNode();
      if (nodeValue.isArray()) {
        for (JsonNode value : nodeValue) {
          list.add(value);
        }
      } else if (!nodeValue.isNull() && !nodeValue.asText().isEmpty()){
        list.add(nodeValue);
      }
      nodeValue = list;
    }
    requestParametersJson.set(paramName, nodeValue);
  }

  /**
   * Set parameters into URI
   * @param rest Rest service
   * @param uriBuilder URI builder
   * @param parameterName Parameter name
   * @param paramsMapFromRequest Parameter map
   */
  private void setParametersInURI(AbstractServiceRest rest, UriComponentsBuilder uriBuilder, String parameterName, Map<String, Object> paramsMapFromRequest) {
    if (rest.getMethod().equalsIgnoreCase("GET")) {
      uriBuilder.queryParam(parameterName, paramsMapFromRequest.get(parameterName));
    }
  }

  /**
   * Launches a subscription to a service
   *
   * @param query Subscribed query
   * @return Service data
   * @throws AWException Error in subscription
   */
  @Override
  public ServiceData subscribe(Query query) throws AWException {
    // Return service output
    return new ServiceData();
  }

}
