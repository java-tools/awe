package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.services.ServiceRest;
import com.almis.awe.model.entities.services.ServiceType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * Launches a Rest service
 */
public class RestConnector extends AbstractRestConnector {

  /**
   * Autowired constructor
   *
   * @param requestFactory Request factory
   */
  public RestConnector(ClientHttpRequestFactory requestFactory) {
    super(requestFactory);
  }

  @Override
  public ServiceData launch(ServiceType service, Map<String, Object> paramsMapFromRequest) throws AWException {
    // Variable definition
    ServiceData outData;
    StringBuilder urlBuilder = new StringBuilder();

    ServiceRest rest = (ServiceRest) service;

    // Retrieve rest server (if defined)
    if (rest.getServer() != null) {
      String serverProperty = "rest.server." + rest.getServer();
      urlBuilder.append(getProperty(serverProperty));
      rest.setAuthentication(getProperty(serverProperty + ".authentication"));
      rest.setUsername(getProperty(serverProperty + ".authentication.username"));
      rest.setPassword(getProperty(serverProperty + ".authentication.password"));
    }

    // Add endpoint to url
    urlBuilder.append(rest.getEndpoint());

    // Create request to rest service
    try {
      outData = doRequest(urlBuilder.toString(), rest, paramsMapFromRequest);
    } catch (RestClientException exc) {
      throw new AWException(getLocale("ERROR_TITLE_INVALID_CONNECTION"),
        getLocale("ERROR_MESSAGE_CONNECTION_REST", urlBuilder.toString()), exc);
    }

    // Check service response
    checkServiceResponse(outData);

    return outData;
  }
}
