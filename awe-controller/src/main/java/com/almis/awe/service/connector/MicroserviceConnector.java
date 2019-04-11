package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.entities.services.ServiceMicroservice;
import com.almis.awe.model.entities.services.ServiceType;
import com.almis.awe.model.type.ParameterType;
import com.almis.awe.model.util.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Launches a Microservice
 */
public class MicroserviceConnector extends AbstractRestConnector {

  @Value("${awe.microservices.endpoint}")
  private String endpointBaseUrl;

  /**
   * Autowired constructor
   * @param logger Logger
   */
  @Autowired
  public MicroserviceConnector(LogUtil logger) {
    super(logger);
  }

  @Override
  public ServiceData launch(ServiceType service, Map<String, Object> paramsMapFromRequest) throws AWException {
    // Variable definition
    ServiceData outData;
    String partialUrl = null;

    ServiceMicroservice microservice = null;
    if (service != null) {
      microservice = (ServiceMicroservice) service;
      partialUrl = endpointBaseUrl + microservice.getName() + microservice.getEndpoint();

      // Add specific parameters to the microservice call
      addDefinedParameters(microservice, paramsMapFromRequest);

      // Create request to microservice
      try {
        outData = doRequest(partialUrl, microservice, paramsMapFromRequest);
      } catch (RestClientException exc) {
        throw new AWException(getLocale("ERROR_TITLE_INVALID_CONNECTION"),
                getLocale("ERROR_MESSAGE_CONNECTION_MICROSERVICE", microservice.getName()), exc);
      }
    } else {
      outData = new ServiceData();
    }

    return outData;
  }

  /**
   * Read defined parameters from properties and add them to the parameter map
   * @param microservice Microservice
   * @param paramsMapFromRequest Parameter map
   */
  private void addDefinedParameters(ServiceMicroservice microservice, Map<String, Object> paramsMapFromRequest) {

    // Read session parameters
    List<String> parameterList = getProperty("microservice." + microservice.getName() + ".session", List.class);
    if (parameterList != null) {
      for (String parameterName: parameterList) {
        String sessionParameter = getProperty("microservice.parameter." + parameterName);
        paramsMapFromRequest.put(parameterName, getSession().getParameter(sessionParameter));
        addParameterToService(microservice, parameterName);
      }
    }

    // Read static parameters
    parameterList = getProperty("microservice." + microservice.getName() + ".static", List.class);
    if (parameterList != null) {
      for (String parameterName: parameterList) {
        String staticValue = getProperty("microservice.parameter." + parameterName);
        paramsMapFromRequest.put(parameterName, staticValue);
        addParameterToService(microservice, parameterName);
      }
    }
  }

  /**
   * Add parameter to service
   * @param microservice Microservice
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
