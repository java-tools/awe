package com.almis.awe.session;

import com.almis.awe.model.component.AweSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * Awe Session strategy
 */
public class AweHttpSessionStrategy implements HttpSessionStrategy {

  private static final String DEFAULT_DELIMITER = "|";
  private static final String SESSION_IDS_WRITTEN_ATTR = AweHttpSessionStrategy.class
    .getName().concat(".SESSIONS_WRITTEN_ATTR");

  private CookieSerializer cookieSerializer;

  /**
   * Autowired constructor
   *
   * @param cookieSerializer Cookie serializer
   */
  @Autowired
  public AweHttpSessionStrategy(CookieSerializer cookieSerializer) {
    this.cookieSerializer = cookieSerializer;
  }

  /**
   * The delimiter between a session alias and a session id when reading a cookie value.
   * The default value is "|".
   */
  private String deserializationDelimiter = DEFAULT_DELIMITER;

  /**
   * The delimiter between a session alias and a session id when writing a cookie value.
   * The default is "|".
   */
  private String serializationDelimiter = DEFAULT_DELIMITER;

  /**
   * Get requested session Id from alias
   *
   * @param request Servlet request
   * @return Session id
   */
  public String getRequestedSessionId(HttpServletRequest request) {
    return getCurrentSessionAlias(request, null);
  }

  /**
   * Get current session alias
   *
   * @param request Servlet request
   * @return Session alias
   */
  public String getCurrentSessionAlias(HttpServletRequest request, AweSession session) {
    return getConnectionId(request, session);
  }

  /**
   * Get new session alias from comet uid
   *
   * @param request Servlet request
   * @return Session alias
   */
  public String getNewSessionAlias(HttpServletRequest request) {
    return getConnectionId(request, null);
  }

  /**
   * On new session creation
   *
   * @param session  Session
   * @param request  Servlet request
   * @param response Servlet response
   */
  public void onNewSession(Session session, HttpServletRequest request,
                           HttpServletResponse response) {
    if (request.isRequestedSessionIdValid()) {
      onInvalidateSession(request, response);
    } else {
      AweSession aweSession = session.getAttribute("scopedTarget.aweSession");
      Set<String> sessionIdsWritten = getSessionIdsWritten(request);
      String sessionAlias = getCurrentSessionAlias(request, aweSession);
      String authorizationHeader = getAuthorizationHeader(request);

      if (sessionAlias == null) {
        return;
      }

      if (sessionIdsWritten.contains(session.getId())) {
        return;
      }

      // Remove authorization header from session set and add new session alias
      Set<String> sessionIds = getSessionIds(request);
      if (!authorizationHeader.equalsIgnoreCase(sessionAlias) || !sessionIds.contains(sessionAlias)) {
        sessionIdsWritten.remove(authorizationHeader);
        sessionIds.remove(authorizationHeader);

        if (aweSession.isAuthenticated()) {
          sessionIdsWritten.add(sessionAlias);
          sessionIds.add(sessionAlias);

          // Write cookie with session ids
          this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, createSessionCookieValue(sessionIds)));
        }
      }
    }
  }

  /**
   * Get current session ids
   *
   * @param request Servlet request
   * @return Session Id List
   */
  @SuppressWarnings("unchecked")
  private Set<String> getSessionIdsWritten(HttpServletRequest request) {
    Set<String> sessionsWritten = (Set<String>) request
      .getAttribute(SESSION_IDS_WRITTEN_ATTR);
    if (sessionsWritten == null) {
      sessionsWritten = new HashSet<>();
      request.setAttribute(SESSION_IDS_WRITTEN_ATTR, sessionsWritten);
    }
    return sessionsWritten;
  }

  /**
   * Create session cookie value
   *
   * @param sessionIds Session id list
   * @return Session cookie value
   */
  private String createSessionCookieValue(Set<String> sessionIds) {
    return StringUtils.join(sessionIds, serializationDelimiter);
  }

  /**
   * Invalidate session
   *
   * @param request  Servlet request
   * @param response Servlet response
   */
  public void onInvalidateSession(HttpServletRequest request,
                                  HttpServletResponse response) {
    Set<String> sessionIds = getSessionIds(request);
    String requestedAlias = getCurrentSessionAlias(request, null);
    sessionIds.remove(requestedAlias);

    // Write cookie
    cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, createSessionCookieValue(sessionIds)));
  }

  /**
   * Sets the {@link CookieSerializer} to be used.
   *
   * @param cookieSerializer the cookieSerializer to set. Cannot be null.
   */
  public void setCookieSerializer(CookieSerializer cookieSerializer) {
    Assert.notNull(cookieSerializer, "cookieSerializer cannot be null");
    this.cookieSerializer = cookieSerializer;
  }

  /**
   * Sets the delimiter between a session alias and a session id when deserializing a
   * cookie. The default is " " This is useful when using
   * <a href="https://tools.ietf.org/html/rfc6265">RFC 6265</a> for writing the cookies
   * which doesn't allow for spaces in the cookie values.
   *
   * @param delimiter the delimiter to set (i.e. "_ " will try a delimeter of either "_"
   *                  or " ")
   */
  public void setDeserializationDelimiter(String delimiter) {
    this.deserializationDelimiter = delimiter;
  }

  /**
   * Sets the delimiter between a session alias and a session id when deserializing a
   * cookie. The default is " ". This is useful when using
   * <a href="https://tools.ietf.org/html/rfc6265">RFC 6265</a> for writing the cookies
   * which doesn't allow for spaces in the cookie values.
   *
   * @param delimiter the delimiter to set (i.e. "_")
   */
  public void setSerializationDelimiter(String delimiter) {
    this.serializationDelimiter = delimiter;
  }

  /**
   * Get current session ids
   *
   * @param request Servlet request
   * @return Session id map
   */
  public Set<String> getSessionIds(HttpServletRequest request) {
    List<String> cookieValues = this.cookieSerializer.readCookieValues(request);
    String sessionCookieValue = cookieValues.isEmpty() ? ""
      : cookieValues.iterator().next();
    Set<String> result = new HashSet<>();
    StringTokenizer tokens = new StringTokenizer(sessionCookieValue,
      this.deserializationDelimiter);
    while (tokens.hasMoreTokens()) {
      String alias = tokens.nextToken();
      result.add(alias);
    }
    return result;
  }

  /**
   * Get session connection id
   *
   * @return Connection id
   */
  public String getConnectionId(HttpServletRequest request, AweSession session) {
    if (session != null && session.isAuthenticated()) {
      return request.getSession().getId();
    } else {
      return getAuthorizationHeader(request);
    }
  }

  /**
   * Get authorization header from current request
   *
   * @return Comet UUID
   */
  public String getAuthorizationHeader(HttpServletRequest request) {
    return request.getHeader(SESSION_CONNECTION_HEADER);
  }
}
