package com.almis.awe.session;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweClientTracker;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.almis.awe.model.constant.AweConstants.*;

/**
 * Session details
 */
public class AweSessionDetails extends ServiceConfig {

  // Autowired services
  private AweClientTracker clientTracker;
  private QueryService queryService;
  private Map<String, List<String>> connectedUsers;

  // Change password screen
  @Value("${session.parameters:}")
  private List<String> sessionParameters;

  @Value("${language.default:en}")
  private String defaultLanguage;

  @Value("${application.theme:sky}")
  private String defaultTheme;

  @Value("${screen.configuration.information:information}")
  private String defaultInitialScreen;

  @Value("${security.default.restriction:general}")
  private String defaultRestriction;

  @Value("${application.parameter.comet.id:s}")
  private String sessionKey;

  /**
   * Autowired constructor
   * @param aweClientTracker
   * @param queryService
   * @param connectedUsers
   */
  @Autowired
  public AweSessionDetails(AweClientTracker aweClientTracker, QueryService queryService, Map<String, List<String>> connectedUsers) {
    this.clientTracker = aweClientTracker;
    this.queryService = queryService;
    this.connectedUsers = connectedUsers;
  }

  /**
   * Manage login success
   * @param authentication Authentication
   */
  public void onLoginSuccess(Authentication authentication) {
    AweSession session = getSession().setAuthentication(authentication);
    try {
      // Store user in session
      session.setParameter(SESSION_USER, session.getUser());

      // Store user details
      storeUserDetails();

      // Add cometUID to user session
      List<String> sessionList = null;
      if (connectedUsers.containsKey(session.getUser())) {
        sessionList = connectedUsers.get(session.getUser());
      } else {
        sessionList = new ArrayList<>();
        connectedUsers.put(session.getUser(), sessionList);
      }
      sessionList.add(getRequest().getParameterAsString(sessionKey));

      // Initialize session variables
      initializeSessionVariables();
    } catch (AWException exc) {
      session.setParameter(SESSION_FAILURE, exc);
    }
  }

  /**
   * Manage login failure
   * @param authenticationException Authentication error
   */
  public void onLoginFailure(AuthenticationException authenticationException) {
    getSession().setParameter(SESSION_FAILURE, new AWException(authenticationException.getMessage(), authenticationException));
  }

  /**
   * Manage logout
   */
  public void onBeforeLogout() {
    // Close client tracker
    clientTracker.removeObservers();

    // Get user
    String user = getSession().getUser();

    // Remove cometUID from user session
    List<String> sessionList = null;
    if (connectedUsers.containsKey(user)) {
      sessionList = connectedUsers.get(user);
      sessionList.remove(getRequest().getParameterAsString(sessionKey));
    }
  }

  /**
   * Manage logout success
   */
  public void onLogoutSuccess() {
    AweSession session = getSession();

    // Remove from session
    session.removeParameter(SESSION_LAST_LOGIN);
    session.removeParameter(SESSION_FULLNAME);
    session.removeParameter(SESSION_LANGUAGE);
    session.removeParameter(SESSION_THEME);
    session.removeParameter(SESSION_PROFILE);
    session.removeParameter(SESSION_RESTRICTION);
    session.removeParameter(SESSION_INITIAL_SCREEN);
    session.removeParameter(SESSION_INITIAL_URL);
    session.removeParameter(SESSION_TOKEN);
  }

  /**
   * Store user details
   * @throws AWException User storage failed
   */
  private void initializeSessionVariables() throws AWException {
    if (sessionParameters != null) {
      for (String parameter : sessionParameters) {
        if (!parameter.isEmpty()) {
          String parameterQuery = getProperty("session." + parameter + ".query");
          ServiceData queryOutput = queryService.launchQuery(parameterQuery, "1", "0");
          DataList queryData = queryOutput.getDataList();
          if (queryData != null && !queryData.getRows().isEmpty()) {
            Map<String, CellData> row = queryData.getRows().get(0);
            getSession().setParameter(parameter, row.get(AweConstants.JSON_VALUE_PARAMETER).getStringValue());
          }
        }
      }
    }
    // throw new AWException(LocalSingleton.getInstance().getLocal("ERROR_TITLE_DEFAULT_SESSION_PARAMETERS"), LocalSingleton.getInstance().getLocal("ERROR_MESSAGE_DEFAULT_SESSION_PARAMETERS"), exc)
  }

  /**
   * Store user details
   * @throws AWException User storage failed
   */
  private void storeUserDetails() throws AWException {
    // Launch query to retrieve user data
    ServiceData userDetailOutput = queryService.launchQuery(USER_DETAIL_QUERY, "1", "0");
    DataList userDetail = userDetailOutput.getDataList();

    // Get user data
    Map<String, CellData> userData = userDetail.getRows().get(0);
    String fullName = userData.get(USER_FULLNAME).getStringValue();
    String userLanguage = userData.get(USER_LANGUAGE).getStringValue();
    String userTheme = userData.get(USER_THEME).getStringValue();
    String userInitialScreen = userData.get(USER_INITIAL_SCREEN).getStringValue();
    String profileTheme = userData.get(PROFILE_THEME).getStringValue();
    String profileInitialScreen = userData.get(PROFILE_INITIAL_SCREEN).getStringValue();
    String profile = userData.get(USER_PROFILE).getStringValue();
    String userRestriction = userData.get(USER_RESTRICTION).getStringValue();
    String profileRestriction = userData.get(PROFILE_RESTRICTION).getStringValue();
    String theme = defaultTheme;
    String initialScreen = defaultInitialScreen;
    String language = defaultLanguage;
    String restriction = defaultRestriction;

    // Specific language (from less to more specific)
    language = userLanguage.isEmpty() ? language : userLanguage.substring(0,2).toLowerCase();

    // Specific restriction (from less to more specific)
    theme = profileTheme.isEmpty() ? theme : profileTheme;
    theme = userTheme.isEmpty() ? theme : userTheme;

    // Specific restriction (from less to more specific)
    restriction = profileRestriction.isEmpty() ? restriction : profileRestriction;
    restriction = userRestriction.isEmpty() ? restriction : userRestriction;

    // Specific initial screen (from less to more specific)
    initialScreen = profileInitialScreen.isEmpty() ? initialScreen : profileInitialScreen;
    initialScreen = userInitialScreen.isEmpty() ? initialScreen : userInitialScreen;

    // Store user data in session
    AweSession session = getSession();
    session.setParameter(SESSION_LAST_LOGIN, new Date());
    session.setParameter(SESSION_FULLNAME, fullName);
    session.setParameter(SESSION_LANGUAGE, language);
    session.setParameter(SESSION_THEME, theme);
    session.setParameter(SESSION_PROFILE, profile);
    session.setParameter(SESSION_RESTRICTION, restriction);
    session.setParameter(SESSION_INITIAL_SCREEN, initialScreen);
    session.setParameter(SESSION_TOKEN, getRequest().getParameterAsString(sessionKey));
  }
}
