package com.almis.awe.service.user;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.dao.UserDAO;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.ppolicy.PasswordPolicyControl;
import org.springframework.security.ldap.ppolicy.PasswordPolicyResponseControl;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Custom ldap user details mapper
 * Used to set UserDetails
 */
@Log4j2
public class LdapAweUserDetailsMapper extends ServiceConfig implements UserDetailsContextMapper {

  private String userCredentialsAttribute = "userPassword";
  private String rolePrefix = "ROLE_";
  private String[] roleAttributes = null;
  private boolean convertToUpperCase = true;

  // Autowired services
  private UserDAO userRepository;

  /**
   * Autowired constructor
   * @param userRepository AWE user DAO
   */
  @Autowired
  public LdapAweUserDetailsMapper(UserDAO userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {

    String dn = ctx.getNameInNamespace();

    log.debug("Mapping user details from context with DN: " + dn);

    LdapUserDetailsImpl.Essence essence = new LdapUserDetailsImpl.Essence();
    essence.setDn(dn);

    Object passwordValue = ctx.getObjectAttribute(userCredentialsAttribute);

    if (passwordValue != null) {
      essence.setPassword(mapPassword(passwordValue));
    }

    essence.setUsername(username);

    // Map the roles
    if (roleAttributes != null) {
      mapRoleAttributes(ctx, essence, dn);
    }

    // Add the supplied authorities
    for (GrantedAuthority authority : authorities) {
      essence.addAuthority(authority);
    }

    // Check for PPolicy data

    PasswordPolicyResponseControl passwordPolicy = (PasswordPolicyResponseControl) ctx
            .getObjectAttribute(PasswordPolicyControl.OID);

    if (passwordPolicy != null) {
      essence.setTimeBeforeExpiration(passwordPolicy.getTimeBeforeExpiration());
      essence.setGraceLoginsRemaining(passwordPolicy.getGraceLoginsRemaining());
    }

    // Get user info
    User user = userRepository.findByUserName(username);

    // Store user details in session
    getSession().setParameter(AweConstants.SESSION_USER_DETAILS, user);

    return essence.createUserDetails();
  }

  @Override
  public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
    throw new UnsupportedOperationException(
            "LdapUserDetailsMapper only supports reading from a context. Please"
                    + "use a subclass if mapUserToContext() is required.");
  }

  /**
   * Map role attributes
   * @param ctx Dir context operations
   * @param essence Essence
   * @param dn DN
   */
  private void mapRoleAttributes(DirContextOperations ctx, LdapUserDetailsImpl.Essence essence, String dn) {
    for (String roleAttribute : roleAttributes) {
      String[] rolesForAttribute = ctx.getStringAttributes(roleAttribute);

      if (rolesForAttribute != null) {
        for (String role : rolesForAttribute) {
          GrantedAuthority authority = createAuthority(role);

          if (authority != null) {
            essence.addAuthority(authority);
          }
        }
      } else {
        log.debug("Couldn't read role attribute ''{0}'' for user {1}", new Object[]{roleAttribute, dn});
      }
    }
  }

  /**
   * Extension point to allow customized creation of the user's password from the
   * attribute stored in the directory.
   *
   * @param passwordValue the value of the password attribute
   * @return a String representation of the password.
   */
  private String mapPassword(Object passwordValue) {

    if (!(passwordValue instanceof String)) {
      // Assume it's binary
      passwordValue = new String((byte[]) passwordValue);
    }

    return (String) passwordValue;

  }

  /**
   * Creates a GrantedAuthority from a role attribute. Override to customize authority
   * object creation.
   * <p>
   * The default implementation converts string attributes to roles, making use of the
   * <tt>rolePrefix</tt> and <tt>convertToUpperCase</tt> properties. Non-String
   * attributes are ignored.
   * </p>
   *
   * @param role the attribute returned from
   * @return the authority to be added to the list of authorities for the user, or null
   * if this attribute should be ignored.
   */
  private GrantedAuthority createAuthority(Object role) {
    if (role instanceof String) {
      if (convertToUpperCase) {
        role = ((String) role).toUpperCase();
      }
      return new SimpleGrantedAuthority(rolePrefix + role);
    }
    return null;
  }

  /**
   * Determines whether role field values will be converted to upper case when loaded.
   * The default is true.
   *
   * @param convertToUpperCase true if the roles should be converted to upper case.
   */
  public void setConvertToUpperCase(boolean convertToUpperCase) {
    this.convertToUpperCase = convertToUpperCase;
  }

  /**
   * The name of the attribute which contains the user's password. Defaults to
   * "userPassword".
   *
   * @param passwordAttributeName the name of the attribute
   */
  public void setPasswordAttributeName(String passwordAttributeName) {
    this.userCredentialsAttribute = passwordAttributeName;
  }

  /**
   * The names of any attributes in the user's entry which represent application roles.
   * These will be converted to <tt>GrantedAuthority</tt>s and added to the list in the
   * returned LdapUserDetails object. The attribute values must be Strings by default.
   *
   * @param roleAttributes the names of the role attributes.
   */
  public void setRoleAttributes(String[] roleAttributes) {
    Assert.notNull(roleAttributes, "roleAttributes array cannot be null");
    this.roleAttributes = roleAttributes;
  }

  /**
   * The prefix that should be applied to the role names
   * @param rolePrefix the prefix (defaults to "ROLE_").
   */
  public void setRolePrefix(String rolePrefix) {
    this.rolePrefix = rolePrefix;
  }
}
