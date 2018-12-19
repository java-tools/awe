package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.services.ServiceRest;
import com.almis.awe.model.entities.services.ServiceType;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Launches a Rest service
 */
public class RestConnector extends AbstractRestConnector {

  @Override
  public ServiceData launch(ServiceType service, Map<String, Object> paramsMapFromRequest) throws AWException {
    // Variable definition
    ServiceData outData;
    String url = null;

    ServiceRest rest = null;
    if (service != null) {
      url = "";
      rest = (ServiceRest) service;

      // Retrieve rest server (if defined)
      if (rest.getServer() != null) {
        url = getProperty("rest.server." +  rest.getServer());
      }

      // Add endpoint to url
      url += rest.getEndpoint();

      // Create request to rest service
      try {
        outData = doRequest(url, rest, paramsMapFromRequest);
      } catch (RestClientException exc) {
        throw new AWException(getLocale("ERROR_TITLE_INVALID_CONNECTION"),
                getLocale("ERROR_MESSAGE_CONNECTION_REST", url), exc);
      }
    } else {
      outData = new ServiceData();
    }

    return outData;
  }
}
