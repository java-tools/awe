package com.almis.awe.session;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.dto.User;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.tracker.AweClientTracker;
import com.almis.awe.model.tracker.AweConnectionTracker;
import com.almis.awe.service.BroadcastService;
import com.almis.awe.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.almis.awe.model.constant.AweConstants.*;

/**
 * Session details
 */
public class AweSessionDetails extends ServiceConfig {

  // Autowired services
  private AweClientTracker clientTracker;
  private QueryService queryService;
  private AweConnectionTracker connectionTracker;
  private BroadcastService broadcastService;

  // Change password screen
  @Value("#{'${session.parameters:}'.split(',')}")
  private List<String> sessionParameters;

  @Value("${language.default:en}")
  private String defaultLanguage;

  @Value("${application.theme:sky}")
  private String defaultTheme;

  @Value("${screen.configuration.information:information}")
  private String defaultInitialScreen;

  @Value("${security.default.restriction:general}")
  private String defaultRestriction;

  @Value("${screen.parameter.username:cod_usr}")
  private String usernameParameter;

  /**
   * Autowired constructor
   *
   * @param aweClientTracker  awe client tracker
   * @param queryService      query service
   * @param connectionTracker connection tracker
   */
  @Autowired
  public AweSessionDetails(AweClientTracker aweClientTracker, QueryService queryService, AweConnectionTracker connectionTracker, BroadcastService broadcastService) {
    this.clientTracker = aweClientTracker;
    this.queryService = queryService;
    this.connectionTracker = connectionTracker;
    this.broadcastService = broadcastService;
  }

  /**
   * Manage login success
   *
   * @param authentication Authentication
   */
  public void onLoginSuccess(Authentication authentication) {
    AweSession session = getSession().setAuthentication(authentication);
    try {
      // Store user in session
      session.setParameter(SESSION_USER, session.getUser());

      // Store user details
      storeUserDetails();

      // Initialize session variables
      initializeSessionVariables();
    } catch (AWException exc) {
      session.setParameter(SESSION_FAILURE, exc);
    }
  }

  /**
   * Manage login failure
   *
   * @param exc Authentication exception
   */
  public void onLoginFailure(AuthenticationException exc) {
    AweSession session = getSession();
    String username = getRequest().getParameterAsString(usernameParameter);
    if (exc instanceof UsernameNotFoundException) {
      session.setParameter(SESSION_FAILURE, new AWException(getLocale("ERROR_TITLE_INVALID_USER"), getLocale("ERROR_MESSAGE_INVALID_USER", username), exc));
    } else if (exc instanceof BadCredentialsException) {
      session.setParameter(SESSION_FAILURE, new AWException(getLocale("ERROR_TITLE_INVALID_CREDENTIALS"), getLocale("ERROR_MESSAGE_INVALID_CREDENTIALS", username), exc));
    } else {
      session.setParameter(SESSION_FAILURE, new AWException(getLocale("ERROR_TITLE_INVALID_CREDENTIALS"), exc.getMessage(), exc));
    }
  }

  /**
   * Manage logout
   */
  private void onBeforeLogout() {
    // Close client tracker
    clientTracker.removeObservers();

    // Get user
    String user = getSession().getUser();

    // Send logout broadcast to other connections
    connectionTracker.getUserConnectionsFromSession(user, getSession().getSessionId())
      .stream()
      .filter(c -> !getRequest().getToken().equalsIgnoreCase(c))
      .collect(Collectors.toSet())
      .forEach(c -> broadcastService.broadcastMessageToUID(c, new ClientAction("logout")));

    // Remove cometUID from user session
    connectionTracker.removeAllConnectionsFromUserSession(user, getSession().getSessionId());
  }

  /**
   * Manage logout success
   */
  public void onLogoutSuccess() {
    AweSession session = getSession();

    // Call oBeforeLogout
    onBeforeLogout();

    // Remove from session
    session.removeParameter(SESSION_USER_DETAILS);
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
   *
   * @throws AWException User storage failed
   */
  private void initializeSessionVariables() throws AWException {
    AweSession session = getSession();
    if (sessionParameters != null) {
      for (String parameter : sessionParameters) {
        if (!parameter.isEmpty()) {
          String parameterQuery = getProperty("session." + parameter + ".query");
          ServiceData queryOutput = queryService.launchQuery(parameterQuery, "1", "0");
          DataList queryData = queryOutput.getDataList();
          if (queryData != null && !queryData.getRows().isEmpty()) {
            Map<String, CellData> row = queryData.getRows().get(0);
            session.setParameter(parameter, row.get(AweConstants.JSON_VALUE_PARAMETER).getStringValue());
          }
        }
      }
    }
  }

  /**
   * Store user details
   */
  private void storeUserDetails() {
    AweSession session = getSession();
    User userDetails = session.getParameter(User.class, SESSION_USER_DETAILS);

    Assert.notNull(userDetails, "User details must not be null. Check if the authentication provider saves user information in session");

    // Get user data
    String theme = defaultTheme;
    String initialScreen = defaultInitialScreen;
    String language = defaultLanguage;
    String restriction = defaultRestriction;
    String userTheme = userDetails.getUserTheme();
    String profileTheme = userDetails.getProfileTheme();
    String userRestriction = userDetails.getUserFileRestriction();
    String profileRestriction = userDetails.getProfileFileRestriction();
    String userInitialScreen = userDetails.getUserInitialScreen();
    String profileInitialScreen = userDetails.getProfileInitialScreen();

    // Specific language (from less to more specific)
    language = userDetails.getLanguage().isEmpty() ? language : userDetails.getLanguage().substring(0, 2).toLowerCase();

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
    session.setParameter(SESSION_LAST_LOGIN, new Date());
    session.setParameter(SESSION_FULLNAME, userDetails.getUserFullName());
    session.setParameter(SESSION_LANGUAGE, language);
    session.setParameter(SESSION_THEME, theme);
    session.setParameter(SESSION_PROFILE, userDetails.getProfile());
    session.setParameter(SESSION_RESTRICTION, restriction);
    session.setParameter(SESSION_INITIAL_SCREEN, initialScreen);
    session.setParameter(SESSION_TOKEN, getRequest().getToken());
  }
}
