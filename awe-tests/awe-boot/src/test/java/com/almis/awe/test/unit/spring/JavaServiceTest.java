package com.almis.awe.test.unit.spring;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

  /**
   * Test call java service with bean parameter
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testServiceBeanParameter() throws Exception {
    // Launch query service
    given(aweSession.isAuthenticated()).willReturn(true);
    ObjectNode parameters = JsonNodeFactory.instance.objectNode();
    parameters.put("name", "Earth");
    parameters.put("rotationPeriod", "1d");
    parameters.put("orbitalPeriod", "365d 6h");
    parameters.put("diameter", "12313");
    parameters.put("climate", "Mixed");
    parameters.put("gravity", "9,8m/s");
    parameters.put("terrain", "Mixed");
    parameters.put("surfaceWater", "80%");
    parameters.put("population", 1231231L);
    parameters.put("created", "23/10/2015");
    parameters.put("edited", "20/05/2018");
    parameters.put("url", "https://www.dummy.url");
    ServiceData serviceData = getBean(QueryService.class).launchQuery("testServiceBeanParameter", parameters);
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
  }

  /**
   * Test call java service with bean parameter
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testServiceBeanParameterList() throws Exception {
    // Launch query service
    given(aweSession.isAuthenticated()).willReturn(true);
    JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
    ObjectNode parameters = nodeFactory.objectNode();
    ObjectMapper mapper = new ObjectMapper();
    parameters.set("name", mapper.readTree("[\"Earth\",\"Mars\",\"Jupiter\"]"));
    parameters.set("rotationPeriod", mapper.readTree("[\"1d\",\"4d\",\"8h\"]"));
    parameters.set("orbitalPeriod", mapper.readTree("[\"365d\",\"632d\",\"4y\"]"));
    parameters.set("diameter", mapper.readTree("[\"123123\",\"12123\",\"4345334\"]"));
    parameters.set("climate", mapper.readTree("[\"mixed\",\"dry\",\"stormy\"]"));
    parameters.set("gravity", mapper.readTree("[\"9,8m/s2\",\"4,8m/s2\",\"25,8m/s2\"]"));
    parameters.set("terrain", mapper.readTree("[\"mixed\",\"rock\",\"gas\"]"));
    parameters.set("surfaceWater", mapper.readTree("[\"80%\",\"2%\",\"12%\"]"));
    parameters.set("population", mapper.readTree("[13421341,4,1]"));
    parameters.set("created", mapper.readTree("[\"23/10/1978\",\"22/05/2012\",\"22/05/2019\"]"));
    parameters.set("edited", mapper.readTree("[\"23/10/2008\",\"22/05/2016\",\"22/06/2019\"]"));
    parameters.set("url", mapper.readTree("[\"https://www.dummy.url\",\"https://www.dummy.url\",\"https://www.dummy.url\"]"));
    ServiceData serviceData = getBean(QueryService.class).launchQuery("testServiceBeanParameterList", parameters);
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
  }

  /**
   * Test call java service with bean parameter
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testServiceBeanListParameter() throws Exception {
    // Launch query service
    given(aweSession.isAuthenticated()).willReturn(true);
    JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode parameters = nodeFactory.objectNode();
    parameters.set("nameList", mapper.readTree("[\"Earth\",\"Mars\",\"Jupiter\"]"));
    parameters.set("populationList", mapper.readTree("[13421341,4,1]"));
    ServiceData serviceData = getBean(QueryService.class).launchQuery("testServiceBeanListParameter", parameters);
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
  }
}
