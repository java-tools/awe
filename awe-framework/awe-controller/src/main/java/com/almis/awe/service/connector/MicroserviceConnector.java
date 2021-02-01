package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.entities.services.ServiceMicroservice;
import com.almis.awe.model.entities.services.ServiceType;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.util.data.QueryUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;

import java.util.*;

/**
 * Launches a Microservice
 */
@Log4j2
public class MicroserviceConnector extends AbstractRestConnector {

  public static final String MICROSERVICE = "microservice";
  @Value("${awe.microservices.endpoint}")
  private String endpointBaseUrl;
  private final QueryUtil queryUtil;

  /**
   * Autowired constructor
   *
   * @param requestFactory Request factory
   */
  public MicroserviceConnector(ClientHttpRequestFactory requestFactory, QueryUtil queryUtil) {
    super(requestFactory);
    this.queryUtil = queryUtil;
  }

  @Override
  public ServiceData launch(ServiceType service, Map<String, Object> paramsMapFromRequest) throws AWException {
    // Variable definition
    ServiceData outData;
    StringBuilder urlBuilder = new StringBuilder();

    ServiceMicroservice microservice = (ServiceMicroservice) service;
    String microserviceProperty = String.format("%s.%s",MICROSERVICE, microservice.getName());
    String microServiceName = Optional.ofNullable(getProperty(microserviceProperty)).orElse(microservice.getName());

    urlBuilder
      .append(endpointBaseUrl)
      .append(microServiceName)
      .append(microservice.getEndpoint());

    // Retrieve microservice auth (if defined)
    microservice.setAuthentication(getProperty(String.format("%s.authentication", microserviceProperty)));
    microservice.setUsername(getProperty(String.format("%s.authentication.username", microserviceProperty)));
    microservice.setPassword(getProperty(String.format("%s.authentication.password", microserviceProperty)));

    // Add specific parameters to the microservice call
    addDefinedParameters(microservice, paramsMapFromRequest);

    // Create request to microservice
    try {
      outData = doRequest(urlBuilder.toString(), microservice, paramsMapFromRequest);
    } catch (RestClientException exc) {
      throw new AWException(getLocale("ERROR_TITLE_INVALID_CONNECTION"),
        getLocale("ERROR_MESSAGE_CONNECTION_MICROSERVICE", microservice.getName()), exc);
    }

    // Check service response
    checkServiceResponse(outData);

    return outData;
  }

  /**
   * Read defined parameters from properties and add them to the parameter map
   *
   * @param microservice         Microservice
   * @param paramsMapFromRequest Parameter map
   */
  private void addDefinedParameters(ServiceMicroservice microservice, Map<String, Object> paramsMapFromRequest) {
    // Read session parameters
    List<String> parameterList = getProperty(String.format("%s.%s.parameters", MICROSERVICE, microservice.getName()), List.class);
    Optional.ofNullable(parameterList).orElse(Collections.emptyList())
      .forEach(parameter -> {
        paramsMapFromRequest.put(parameter, getParameter(parameter));
        addParameterToService(microservice, parameter);
      });
  }

  /**
   * Retrieve parameter value
   * @param parameterName Parameter name
   * @return Parameter value
   */
  private Object getParameter(String parameterName) {
    String parameter = getProperty(String.format("%s.parameter.%s", MICROSERVICE, parameterName));
    String type = getProperty(String.format("%s.parameter.%s.type", MICROSERVICE, parameterName));

    switch (Optional.ofNullable(type).orElse("default")) {
      case "session":
        return getSession().getParameter(parameter);
      case "request":
        return queryUtil.getRequestParameter(parameter);
      case "static":
      default:
        return parameter;
    }
  }

  /**
   * Add parameter to service
   *
   * @param microservice  Microservice
   * @param parameterName Parameter to add
   */
  private void addParameterToService(ServiceMicroservice microservice, String parameterName) {
    ServiceInputParameter parameter = new ServiceInputParameter();
    parameter.setName(parameterName);
    parameter.setValue(parameterName);
    parameter.setType(ParameterType.STRING.toString());
    List<ServiceInputParameter> parameterList = microservice.getParameterList();
    if (parameterList == null) {
      parameterList = new ArrayList<>();
    }
    parameterList.add(parameter);
    microservice.setParameterList(parameterList);
  }
}
