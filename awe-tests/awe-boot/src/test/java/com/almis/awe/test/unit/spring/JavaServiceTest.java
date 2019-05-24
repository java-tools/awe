package com.almis.awe.test.unit.spring;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.QueryService;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

public class JavaServiceTest extends AweSpringBootTests {

  /**
   * Test call java service by Qualifier name
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testCallServiceByQualifierName() throws AWException {
    // Launch query service
    given(aweSession.isAuthenticated()).willReturn(true);
    ServiceData serviceData = getBean(QueryService.class).launchQuery("testServiceByQualifier");
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
  }
}
