package com.almis.awe.test.unit;

import com.almis.awe.dao.UserDAO;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.dto.User;
import com.almis.awe.service.user.LdapAweUserDetailsMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.ppolicy.PasswordPolicyControl;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.NamingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

/**
 *
 * @author pgarcia
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class UserServiceTest extends TestUtil {

  @MockBean
  private UserDAO userDAO;

  @MockBean
  private AweElements elements;

  @MockBean
  private AweSession session;

  @MockBean
  private DirContextOperations contextOperations;

  @MockBean
  private DirContextAdapter contextAdapter;

  @Autowired
  private UserDetailsService userDetailService;

  @Autowired
  private LdapAweUserDetailsMapper userDetailsMapper;

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

  /**
   * User details mapper test
   * @throws Exception Test error
   */
  @Test
  public void testLdapUserDetails() throws Exception {
    given(contextOperations.getNameInNamespace()).willReturn("test");
    given(elements.getProperty("PwdExp")).willReturn("30");
    given(userDAO.findByUserName(anyString())).willReturn(User.builder()
      .userName("test")
      .userPassword("asdada")
      .enabled(true)
      .profile("ADM")
      .passwordLocked(false)
      .build());
    UserDetails details = userDetailsMapper.mapUserFromContext(contextOperations, "test", Collections.EMPTY_LIST);
    assertNotNull(details);
  }

  /**
   * User details mapper test
   * @throws Exception Test error
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testLdapUserDetailsWithPasswordRetrieved() throws Exception {
    given(contextOperations.getNameInNamespace()).willReturn("test");
    given(contextOperations.getObjectAttribute("userPassword")).willReturn("asdada");
    given(contextOperations.getObjectAttribute(PasswordPolicyControl.OID)).willReturn(Mockito.mock(PasswordPolicyResponseControl.class));
    given(contextOperations.getStringAttributes("lala")).willReturn(new String[]{"tutu", "lala"});
    given(elements.getProperty("PwdExp")).willReturn("30");
    given(userDAO.findByUserName(anyString())).willReturn(User.builder()
      .userName("test")
      .userPassword("asdada")
      .enabled(true)
      .profile("ADM")
      .passwordLocked(false)
      .build());
    userDetailsMapper.setRoleAttributes(new String[]{"tutu", "lala"});
    UserDetails details = userDetailsMapper.mapUserFromContext(contextOperations, "test", Arrays.asList(new SimpleGrantedAuthority("ROLE_LALA")));
    assertNotNull(details);
    userDetailsMapper.mapUserToContext(details, contextAdapter);
  }
}