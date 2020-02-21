package com.almis.awe.test.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.MaintainService;
import com.almis.awe.test.bean.Concert;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Dummy Service class to test call services from <code>qualifier</code> attribute
 *
 * @author pvidal
 */
@Service
@Qualifier("CustomQualifierNameService")
public class DummyQualifierService extends ServiceConfig {

  @Autowired
  private MaintainService maintainService;

  /**
   * Constructor
   */
  public DummyQualifierService() {
    // Constructor
  }

  /**
   * Check if service has been instantiated
   *
   * @return check flag
   */
  public ServiceData getInstance() throws AWException {
    ServiceData serviceData = new ServiceData();
    serviceData.setValid(true);
    return serviceData;
  }

  /**
   * @param concert
   * @return
   */
  public ServiceData testComplexRestParametersPOJO(Concert concert) throws AWException {
    ObjectNode parameters = JsonNodeFactory.instance.objectNode();
    parameters.putPOJO("concert", concert);
    return maintainService.launchMaintain("TestComplexRestParametersPOJO", parameters);
  }
}
