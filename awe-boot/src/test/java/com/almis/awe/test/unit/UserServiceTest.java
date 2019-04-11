package com.almis.awe.test.unit;

import com.almis.awe.dao.UserDAO;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.dto.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.NamingException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

/**
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceTest extends TestUtil {

  @MockBean
  private UserDAO userDAO;

  @MockBean
  private AweElements elements;

  @MockBean
  private AweSession session;

  @Autowired
  private UserDetailsService userDetailService;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void setup() throws Exception {
    super.setup();
  }

  /**
   * Test context loaded
   * @throws NamingException Test error
   */
  @Test
  public void contextLoads() {

    // Check that controller are active
    assertThat(userDetailService).isNotNull();
  }

  /**
   * Test of check public addresses
   * @throws Exception Test error
   */
  @Test
  public void testLoadByUsername() throws Exception {
    given(elements.getProperty("PwdExp")).willReturn("30");
    given(userDAO.findByUserName(anyString())).willReturn(User.builder()
      .userName("test")
      .userPassword("asdada")
      .enabled(true)
      .profile("ADM")
      .lastChangedPasswordDate(new Date())
      .passwordLocked(true)
      .build());
    UserDetails details = userDetailService.loadUserByUsername("test");
    assertNotNull(details);
    assertFalse(details.isCredentialsNonExpired());
    assertFalse(details.isAccountNonLocked());
  }

  /**
   * Test of check public addresses
   * @throws Exception Test error
   */
  @Test
  public void testLoadByUsernameWithNullDate() throws Exception {
    given(elements.getProperty("PwdExp")).willReturn("30");
    given(userDAO.findByUserName(anyString())).willReturn(User.builder()
      .userName("test")
      .userPassword("asdada")
      .enabled(true)
      .profile("ADM")
      .passwordLocked(false)
      .build());
    UserDetails details = userDetailService.loadUserByUsername("test");
    assertNotNull(details);
    assertFalse(details.isCredentialsNonExpired());
    assertTrue(details.isAccountNonLocked());
  }
}