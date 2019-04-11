package com.almis.awe.dao;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.dto.User;
import com.almis.awe.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

/**
 * User data implementation
 */
public class UserDAOImpl extends ServiceConfig implements UserDAO {

  // Query service
  private QueryService queryService;

  /**
   * Autowired constructor
   *
   * @param queryService Query service
   */
  @Autowired
  public UserDAOImpl(QueryService queryService) {
    this.queryService = queryService;
  }

  @Override
  public User findByUserName(String userName) {

    try {
      // Get user details from database
      getRequest().setParameter("user", userName);
      ServiceData userData = queryService.launchQuery(AweConstants.USER_DETAIL_QUERY);
      Map<String, CellData> userDataListMap = userData.getDataList().getRows().get(0);
      return User.
        builder().
        userID(userDataListMap.get("userId").getIntegerValue()).
        userName(userDataListMap.get("user").getStringValue().trim()).
        userFullName(userDataListMap.get("fullName").getStringValue()).
        userPassword(userDataListMap.get("password").getStringValue()).
        enabled(userDataListMap.get("active").getIntegerValue().equals(1)).
        passwordLocked(userDataListMap.get("locked").getIntegerValue().equals(1)).
        lastChangedPasswordDate(userDataListMap.get("lastChangedPasswordDate").getDateValue()).
        userNotConected(userDataListMap.get("connected").getIntegerValue().equals(0)).
        profile(userDataListMap.get("profile").getStringValue()).
        profileID(userDataListMap.get("profileId").getIntegerValue()).
        lastLogin(userDataListMap.get("lastLogin").getDateValue()).
        printerName(userDataListMap.get("printer").getStringValue()).
        updateDate(userDataListMap.get("updateDate").getDateValue()).
        language(userDataListMap.get("language").getStringValue()).
        emailServer(userDataListMap.get("emailServer").getStringValue()).
        email(userDataListMap.get("email").getStringValue()).
        userTheme(userDataListMap.get("userTheme").getStringValue()).
        profileTheme(userDataListMap.get("profileTheme").getStringValue()).
        userInitialScreen(userDataListMap.get("userInitialScreen").getStringValue()).
        profileInitialScreen(userDataListMap.get("profileInitialScreen").getStringValue()).
        userFileRestriction(userDataListMap.get("userRestriction").getStringValue()).
        profileFileRestriction(userDataListMap.get("profileRestriction").getStringValue()).
        loginAttempts(userDataListMap.get("loginAttempts").getIntegerValue()).
        build();
    } catch (Exception exc) {
      throw new UsernameNotFoundException(userName);
    }
  }

}
