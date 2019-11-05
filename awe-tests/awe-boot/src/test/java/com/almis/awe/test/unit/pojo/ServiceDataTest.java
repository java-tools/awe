package com.almis.awe.test.unit.pojo;

import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Service data tests
 *
 * @author pgarcia
 */
public class ServiceDataTest extends TestUtil {

  /**
   * Test of check public addresses
   *
   * @throws Exception Test error
   */
  @Test
  public void testServiceDataCopy() throws Exception {
    // Prepare
    ServiceData serviceData = new ServiceData();
    serviceData.setTitle("aaaa");
    serviceData.setMessage("bbbb");
    serviceData.setType(AnswerType.ERROR);

    // Run
    ServiceData serviceDataCopy = serviceData.copy();

    // Assert
    assertEquals("aaaa", serviceDataCopy.getTitle());
    assertEquals("bbbb", serviceDataCopy.getMessage());
    assertEquals(AnswerType.ERROR, serviceDataCopy.getType());
    assertFalse(serviceDataCopy.isValid());
  }

  /**
   * Test of check public addresses
   *
   * @throws Exception Test error
   */
  @Test
  public void testServiceDataCopyConstructor() throws Exception {
    // Prepare
    ServiceData serviceData = new ServiceData();
    serviceData.setTitle("aaaa");
    serviceData.setMessage("bbbb");
    serviceData.setType(AnswerType.WARNING);

    // Run
    ServiceData serviceDataCopy = new ServiceData(serviceData);

    // Assert
    assertEquals("aaaa", serviceDataCopy.getTitle());
    assertEquals("bbbb", serviceDataCopy.getMessage());
    assertEquals(AnswerType.WARNING, serviceDataCopy.getType());
    assertTrue(serviceDataCopy.isValid());
  }
}