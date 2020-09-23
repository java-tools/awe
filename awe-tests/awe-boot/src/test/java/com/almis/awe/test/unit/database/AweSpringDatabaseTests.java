package com.almis.awe.test.unit.database;

import com.almis.awe.service.TemplateService;
import com.almis.awe.test.unit.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "test", password = "test")
public abstract class AweSpringDatabaseTests extends TestUtil {

  @MockBean
  protected TemplateService templateService;

  /**
   * Initializes json mapper for tests
   * @throws Exception error updating user
   */
  @BeforeEach
  public void setup() throws Exception{
    super.setup();
  }
}