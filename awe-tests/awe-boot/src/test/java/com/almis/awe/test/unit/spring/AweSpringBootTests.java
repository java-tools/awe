package com.almis.awe.test.unit.spring;

import com.almis.awe.dao.UserDAO;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.test.unit.TestUtil;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Class used for testing rest services through ActionController
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(username = "test", password = "test")
@TestPropertySource({
  "classpath:hsql.properties",
  "classpath:cache.properties"
})
public abstract class AweSpringBootTests extends TestUtil {

  @MockBean
  protected AweSession aweSession;

  @MockBean
  protected AweRequest aweRequest;

  @MockBean
  protected DirContextOperations contextOperations;

  @MockBean
  protected DirContextAdapter contextAdapter;

  @MockBean
  protected UserDAO userDAO;

  @MockBean
  protected BroadcastService broadcastService;

  /**
   * Initializes json mapper for tests
   * @throws Exception error updating user
   */
  @Before
  public void setup() throws Exception{
    super.setup();
  }
}