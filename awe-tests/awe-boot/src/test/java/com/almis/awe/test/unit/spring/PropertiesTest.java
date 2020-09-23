package com.almis.awe.test.unit.spring;

import com.almis.awe.exception.AWException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.Assert.assertEquals;

public class PropertiesTest extends AweSpringBootTests {

  // Defined property
  @Value("${awe.database.enabled:false}")
  private String definedProperty;

  @Value("${MinPwd:false}")
  private String databaseProperty;

  /**
   * Test an autowired defined property (with @Value)
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testAutowiredDefinedProperty() throws AWException {
    assertEquals("true", definedProperty);
  }

  /**
   * Test an autowired database property (with @Value)
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testAutowiredDatabaseProperty() throws AWException {
    assertEquals("3", databaseProperty);
  }

  /**
   * Test a defined property (with getProperty)
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testDefinedProperty() throws AWException {
    assertEquals("true", getProperty("awe.database.enabled"));
  }

  /**
   * Test a database property (with getProperty)
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testDatabaseProperty() throws Exception {
    assertEquals("3", getProperty("MinPwd"));
  }
}
