package com.almis.awe.test.unit;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.QueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@WithMockUser(username = "test")
@SpringBootTest
public class JavaServiceTest extends TestUtil {

  @Autowired
  QueryService queryService;

  /**
   * Test call java service by Qualifier name
   *
   * @throws AWException AWE exception
   */
  @Test
  public void testCallServiceByQualifierName() throws AWException {
    // Launch query service
    ServiceData serviceData = queryService.launchQuery("testServiceByQualifier");
    assertNotNull(serviceData);
    assertTrue(serviceData.isValid());
  }
}
