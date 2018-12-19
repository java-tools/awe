package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.services.ServiceType;

import java.util.Map;

/**
 * Manages distinct service launchers
 *
 * @author pvidal
 */
public interface ServiceConnector {

  /**
   * Launch service
   *
   * @param paramsMapFromRequest Parameter list
   * @param service Service to launch
   *
   * @return Service data
   * @throws AWException Error launching service
   */
  ServiceData launch(ServiceType service, Map<String, Object> paramsMapFromRequest) throws AWException;

  /**
   * Subscribe to a service data
   *
   * @param query Query to subscribe to
   * @return Service data
   * @throws AWException Error in subscription
   */
  ServiceData subscribe(Query query) throws AWException;
}
