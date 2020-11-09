package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.dto.ServiceDataWrapper;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.services.AbstractServiceRest;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.type.RestContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Launches a Rest service
 */
@Log4j2
public abstract class AbstractRestConnector extends AbstractServiceConnector {

  // Autowired services
  private final ClientHttpRequestFactory requestFactory;

  /**
   * Autowired constructor
   *
   * @param requestFactory Request factory
   */
  protected AbstractRestConnector(ClientHttpRequestFactory requestFactory) {
    this.requestFactory = requestFactory;
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
    RestTemplate restTemplate;
    if (service.getAuthentication() != null) {
      restTemplate = new RestTemplateBuilder()
        .basicAuthentication(service.getUsername(), service.getPassword())
        .build();
    } else {
      restTemplate = new RestTemplate(requestFactory);
    }

    // Define request object
    HttpEntity request;

    // Define response object
    ResponseEntity<? extends ResponseWrapper> response;

    // If url has wildcards, retrieve values from parameters
    Map<String, Object> urlParameters = new HashMap<>();
    if (service.getParameterList() != null) {
      urlParameters = new HashMap<>();

      Pattern pattern = Pattern.compile("\\{([\\w]+)}");
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
    log.info("Doing {} request to url {}", service.getMethod(), finalUrl);
    try {
      response = restTemplate.exchange(finalUrl, HttpMethod.valueOf(service.getMethod()), request, wrapper, urlParameters);
    } catch (Exception e) {
      throw new AWException("Request failed", e);
    }

    // Handle response status
    if (!response.getStatusCode().is2xxSuccessful()) {
      throw new AWException(String.format("Operation unsuccessful %d", response.getStatusCodeValue()));
    }

    return Optional.ofNullable((ResponseWrapper) response.getBody()).orElse((ResponseWrapper) new ServiceDataWrapper().setDataList(new DataList())).toServiceData();
  }

  /**
   * Generate rest request
   *
   * @param rest                 Rest service
   * @param uriBuilder           Url Builder
   * @param urlParameters        Url parameters
   * @param paramsMapFromRequest Request parameters
   * @return Request
   * @throws com.fasterxml.jackson.core.JsonProcessingException {@link JsonProcessingException}
   */
  protected HttpEntity generateRequest(AbstractServiceRest rest, UriComponentsBuilder uriBuilder, Map<String, Object> urlParameters, Map<String, Object> paramsMapFromRequest) throws JsonProcessingException {
    // Define request headers
    HttpHeaders headers = new HttpHeaders();

    RestContentType restContentType = rest.getContentType() != null ? RestContentType.valueOf(rest.getContentType()) : RestContentType.URLENCODED;
    MediaType contentType = restContentType.equals(RestContentType.JSON) ? MediaType.APPLICATION_JSON : MediaType.APPLICATION_FORM_URLENCODED;

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
   *
   * @param rest                 Rest service
   * @param uriBuilder           URI builder
   * @param urlParameters        URL parameters
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
   *
   * @param requestParametersMap Request parameters
   * @param param                Parameter to read
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
    } else if (nodeValue == null) {
      requestParametersMap.set(paramName, null);
    } else {
      requestParametersMap.set(paramName, nodeValue.asText());
    }
  }

  /**
   * Generate parameters as JSON
   *
   * @param rest                 Rest service
   * @param uriBuilder           URI Builder
   * @param urlParameters        URL parameters
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

        // If parameter is a POJO, replace all parameters from request parameters
        if (param.getBeanClass() != null) {
          requestParametersJson = readParameterPOJO(param, paramsMapFromRequest);
        }
      }
    }

    // Create request using headers and parameters defined previously
    return mapper.writeValueAsString(requestParametersJson);
  }

  /**
   * Read parameter json
   *
   * @param requestParametersJson Request parameters
   * @param param                 Parameter to read
   * @param paramsMapFromRequest  Parameters from request
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
      } else if (!nodeValue.isNull() && !nodeValue.asText().isEmpty()) {
        list.add(nodeValue);
      }
      nodeValue = list;
    }
    requestParametersJson.set(paramName, nodeValue);
  }

  /**
   * Read parameter POJO
   *
   * @param param                Parameter to read
   * @param paramsMapFromRequest Parameters from request
   */
  private ObjectNode readParameterPOJO(ServiceInputParameter param, Map<String, Object> paramsMapFromRequest) {
    // If it has parameters, expand the url avoiding parameters already used
    ObjectMapper mapper = new ObjectMapper();
    String paramName = param.getName();
    return mapper.valueToTree(paramsMapFromRequest.get(paramName));
  }

  /**
   * Set parameters into URI
   *
   * @param rest                 Rest service
   * @param uriBuilder           URI builder
   * @param parameterName        Parameter name
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
   */
  @Override
  public ServiceData subscribe(Query query) {
    // Return service output
    return new ServiceData();
  }

}
