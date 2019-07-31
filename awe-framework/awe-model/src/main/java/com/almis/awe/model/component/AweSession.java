package com.almis.awe.model.component;

import com.almis.awe.model.messages.AweMessage;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by dfuentes on 13/04/2017.
 */
@Log4j2
public class AweSession implements Serializable {
  private transient Authentication authentication;
  public static final String ROLE = "ROLE_";
  private AweSessionStorage sessionStorage;

  /**
   * Autowired constructor
   */
  public AweSession() {
    // Merged with spring security
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Authentication contextAuthentication = securityContext.getAuthentication();
    if (contextAuthentication != null) {
      this.setAuthentication(contextAuthentication);
    }

    // Generate session storage
    sessionStorage = new AweSessionStorage();
  }

  /**
   * Retrieve session authentication
   *
   * @return Authentication
   */
  public Authentication getAuthentication() {
    if (authentication == null) {
      SecurityContext securityContext = SecurityContextHolder.getContext();
      authentication = securityContext.getAuthentication();
    }
    return authentication;
  }

  /**
   * Get current session id
   *
   * @return Session id
   */
  public String getSessionId() {
    return RequestContextHolder.currentRequestAttributes().getSessionId();
  }

  /**
   * Get current session user
   *
   * @return Session user
   */
  public String getUser() {
    return isAuthenticated() ? ((UserDetails) getAuthentication().getPrincipal()).getUsername() : null;
  }

  /**
   * Check if current user has the given role
   *
   * @param roleName Role name
   * @return Has the role
   */
  public boolean hasRole(String roleName) {
    String fullRoleName = roleName.startsWith(ROLE) ? roleName : ROLE + roleName;
    return getAuthentication() != null && getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(fullRoleName));
  }

  /**
   * Check if current user has all the given role
   *
   * @param roleName Role names
   * @return Has all roles
   */
  public boolean hasRoles(String... roleName) {
    if (roleName != null) {
      for (String role : roleName) {
        if (!hasRole(role)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Check if current user has any of the given role
   *
   * @param roleName Role names
   * @return Has any of the roles
   */
  public boolean hasAnyRole(String... roleName) {
    if (roleName != null) {
      for (String role : roleName) {
        if (hasRole(role)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Set new parameter
   *
   * @param name  Parameter name
   * @param value Parameter value
   */
  public void setParameter(String name, Object value) {
    sessionStorage.store(name, value);
    log.log(Level.TRACE, new AweMessage("Attribute added: {} = {} ", name, value));
  }

  /**
   * Get parameter
   *
   * @param name Parameter name
   * @return Parameter value
   */
  public Object getParameter(String name) {
    Object value = sessionStorage.retrieve(name);
    log.log(Level.TRACE, new AweMessage("Attribute retrieved: {} = {}", name, value));
    return value;
  }

  /**
   * Returns parameter value casted to the given class
   *
   * @param clazz Parameter class
   * @param name Parameter name
   * @param <T> class parameter type
   * @return Parameter value
   */
  public <T> T getParameter(Class<T> clazz, String name) {
    T value = sessionStorage.retrieve(clazz, name);
    log.log(Level.TRACE, new AweMessage("Attribute retrieved: {} = {}", name, value));
    return value;
  }

  /**
   * Remove parameter from session
   *
   * @param name Parameter name
   */
  public void removeParameter(String name) {
    sessionStorage.remove(name);
    log.log(Level.TRACE, new AweMessage("Attribute removed: {}", name));
  }

  /**
   * Check if there is a parameter in the session
   *
   * @param name Parameter name
   * @return Session has parameter
   */
  public boolean hasParameter(String name) {
    return sessionStorage.has(name);
  }

  /**
   * Get parameter names from current session
   *
   * @return Parameter names
   */
  public Set<String> getParameterNames() {
    return sessionStorage.sessionKeys();
  }

  /**
   * Check if user is authenticated
   *
   * @return User is authenticated
   */
  public boolean isAuthenticated() {
    return getAuthentication() != null && !(getAuthentication() instanceof AnonymousAuthenticationToken);
  }

  /**
   * Set session authentication
   *
   * @param authentication {@link Authentication}
   * @return AWE session
   */
  public AweSession setAuthentication(Authentication authentication) {
    this.authentication = authentication;
    return this;
  }
}