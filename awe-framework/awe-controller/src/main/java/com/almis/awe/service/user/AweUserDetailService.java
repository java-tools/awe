package com.almis.awe.service.user;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.dao.UserDAO;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.*;

/**
 * AWE user detail service
 * <p>
 * Retrieve user info to authenticate
 *
 * @author pvidal
 */
public class AweUserDetailService extends ServiceConfig implements UserDetailsService {

  // Autowired services
  private UserDAO userRepository;

  /**
   * Autowired constructor
   *
   * @param userDAO User DAO
   */
  @Autowired
  public AweUserDetailService(UserDAO userDAO) {
    this.userRepository = userDAO;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.security.core.userdetails.UserDetailsService#
   * loadUserByUsername(java.lang.String)
   */
  @Override
  public UserDetails loadUserByUsername(String username) {
    // Get user info
    User user = userRepository.findByUserName(username);

    // Store user details in session
    getSession().setParameter(AweConstants.SESSION_USER_DETAILS, user);

    // Build User details
    return new org.springframework.security.core.userdetails.User(
      user.getUserName(),
      user.getUserPassword(),
      user.isEnabled(), Boolean.TRUE,
      !checkExpiredPassword(user.getLastChangedPasswordDate()),
      !user.isPasswordLocked(),
      getAuthorities(user.getProfile())
    );
  }

  /**
   * Get authorities from profiles
   *
   * @param profile User profile
   * @return List or granted authority
   */
  public Collection<GrantedAuthority> getAuthorities(String profile) {
    List<GrantedAuthority> authList = new ArrayList<>();
    // Add authority
    authList.add(new SimpleGrantedAuthority("ROLE_" + profile));
    return authList;
  }

  private boolean checkExpiredPassword(Date updateDate) {
    // Get PwdExp (number of days for password to expire)
    String passwordExpirationDaysStr = getProperty("PwdExp");
    if (passwordExpirationDaysStr == null) {
      return false;
    } else if (updateDate == null) {
      // Unchanged password
      return true;
    } else {
      // Add password expiration days to update date to retrieve expiration date
      int passwordExpirationDays = Integer.parseInt(passwordExpirationDaysStr);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(updateDate);
      calendar.add(Calendar.DATE, passwordExpirationDays);
      Date expirationDate = calendar.getTime();
      Date currentDate = new Date();

      // Check expiration date versus current date
      return expirationDate.compareTo(currentDate) > 0;
    }
  }
}
