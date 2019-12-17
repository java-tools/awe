package com.almis.awe.test.unit.spring;

import com.almis.awe.model.dto.User;
import com.almis.awe.service.user.LdapAweUserDetailsMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.ppolicy.PasswordPolicyControl;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;

import javax.naming.NamingException;
import java.util.Date;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 *
 * @author pgarcia
 */
public class UserServiceTest extends AweSpringBootTests {

  private UserDetailsService userDetailService;
  private LdapAweUserDetailsMapper userDetailsMapper;

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() throws Exception {
    userDetailService = getBean(UserDetailsService.class);
    userDetailsMapper = getBean(LdapAweUserDetailsMapper.class);
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
    given(userDAO.findByUserName(anyString())).willReturn(User.builder()
      .userName("test")
      .userPassword("asdada")
      .enabled(true)
      .profile("ADM")
      .passwordLocked(false)
      .build());
    UserDetails details = userDetailsMapper.mapUserFromContext(contextOperations, "test", EMPTY_LIST);
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
    given(userDAO.findByUserName(anyString())).willReturn(User.builder()
      .userName("test")
      .userPassword("asdada")
      .enabled(true)
      .profile("ADM")
      .passwordLocked(false)
      .build());
    userDetailsMapper.setRoleAttributes(new String[]{"tutu", "lala"});
    UserDetails details = userDetailsMapper.mapUserFromContext(contextOperations, "test", singletonList(new SimpleGrantedAuthority("ROLE_LALA")));
    assertNotNull(details);
    userDetailsMapper.mapUserToContext(details, contextAdapter);
  }
}