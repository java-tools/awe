package com.almis.awe.test.unit.spring;

import com.almis.awe.model.dto.User;
import com.almis.awe.service.user.LdapAweUserDetailsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.ppolicy.PasswordPolicyControl;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;

import java.util.Date;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
  @BeforeEach
  public void initBeans() throws Exception {
    userDetailService = getBean(UserDetailsService.class);
    userDetailsMapper = getBean(LdapAweUserDetailsMapper.class);
  }

  /**
   * Test context loaded
   */
  @Test
  public void contextLoads() {

    // Check that controller are active
    assertThat(userDetailService).isNotNull();
  }

  /**
   * Test of check public addresses
   */
  @Test
  public void testLoadByUsername() {
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
   */
  @Test
  public void testLoadByUsernameWithNullDate() {
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
   */
  @Test
  public void testLdapUserDetails() {
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
   */
  @Test
  public void testLdapUserDetailsWithPasswordRetrieved() {
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

    assertThrows(UnsupportedOperationException.class, () -> {
      userDetailsMapper.mapUserToContext(details, contextAdapter);
    });
  }
}