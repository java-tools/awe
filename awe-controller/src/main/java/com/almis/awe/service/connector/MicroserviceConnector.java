package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.services.ServiceMicroservice;
import com.almis.awe.model.entities.services.ServiceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Launches a Microservice
 */
public class MicroserviceConnector extends AbstractRestConnector {

  @Value("${awe.microservices.endpoint}")
  private String endpointBaseUrl;

  @Override
  public ServiceData launch(ServiceType service, Map<String, Object> paramsMapFromRequest) throws AWException {
    // Variable definition
    ServiceData outData;
    String partialUrl = null;

    ServiceMicroservice microservice = null;
    if (service != null) {
      microservice = (ServiceMicroservice) service;
      partialUrl = endpointBaseUrl + microservice.getName() + microservice.getEndpoint();

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
}
