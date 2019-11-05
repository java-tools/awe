package com.almis.awe.test.unit.spring;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.*;
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

  /**
   * Test call java service with bean parameter
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testServiceJsonBeanParameter() throws Exception {
    // Launch query service
    given(aweSession.isAuthenticated()).willReturn(true);
    ObjectNode parameters = JsonNodeFactory.instance.objectNode();
    ObjectNode value = JsonNodeFactory.instance.objectNode();
    value.put("name", "Earth");
    value.put("rotationPeriod", "1d");
    value.put("orbitalPeriod", "365d 6h");
    value.put("diameter", "12313");
    value.put("climate", "Mixed");
    value.put("gravity", "9,8m/s");
    value.put("terrain", "Mixed");
    value.put("surfaceWater", "80%");
    value.put("population", 1231231L);
    value.put("created", "2015-10-23");
    value.put("edited", "2018-05-20");
    value.put("url", "https://www.dummy.url");
    parameters.set("value", value);
    ServiceData serviceData = getBean(QueryService.class).launchQuery("testServiceJsonBeanParameter", parameters);
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
  }

  /**
   * Test call java service with bean parameter
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testServiceJsonNullBean() throws Exception {
    // Launch query service
    given(aweSession.isAuthenticated()).willReturn(true);
    ServiceData serviceData = getBean(QueryService.class).launchQuery("testServiceJsonBeanParameter", JsonNodeFactory.instance.objectNode());
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
  }

  /**
   * Test call java service with bean parameter
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testServiceJsonParameter() throws Exception {
    // Launch query service
    given(aweSession.isAuthenticated()).willReturn(true);
    ObjectNode parameters = JsonNodeFactory.instance.objectNode();
    ObjectNode value = JsonNodeFactory.instance.objectNode();
    value.put("name", "Earth");
    value.put("rotationPeriod", "1d");
    value.put("orbitalPeriod", "365d 6h");
    value.put("diameter", "12313");
    value.put("climate", "Mixed");
    value.put("gravity", "9,8m/s");
    value.put("terrain", "Mixed");
    value.put("surfaceWater", "80%");
    value.put("population", 1231231L);
    value.put("created", "23/10/2015");
    value.put("edited", "20/05/2018");
    value.put("url", "https://www.dummy.url");
    parameters.set("value", value);
    ServiceData serviceData = getBean(QueryService.class).launchQuery("testServiceJsonParameter", parameters);
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
    assertEquals("tutu", serviceData.getVariable(AweConstants.ACTION_MESSAGE_TITLE).getStringValue());
    assertEquals("lala", serviceData.getVariable(AweConstants.ACTION_MESSAGE_DESCRIPTION).getStringValue());
  }
}
