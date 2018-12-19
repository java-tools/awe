package com.almis.awe.session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionManager;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Awe Session strategy
 */
public class AweHttpSessionStrategy implements HttpSessionStrategy, HttpSessionManager {

  private static final String DEFAULT_DELIMITER = "|";
  private static final String SESSION_IDS_WRITTEN_ATTR = AweHttpSessionStrategy.class
          .getName().concat(".SESSIONS_WRITTEN_ATTR");

  @Value ("${security.json.parameter:p}")
  String securityParameter = "p";

  @Value ("${application.parameter.comet.id:s}")
  private String sessionAliasParameter;

  private static final Pattern ALIAS_PATTERN = Pattern.compile("^[\\w-]{1,50}$");

  private String sessionParam = sessionAliasParameter;

  private CookieSerializer cookieSerializer = new DefaultCookieSerializer();

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
    Map<String, String> sessionIds = getSessionIds(request);
    String sessionAlias = getCurrentSessionAlias(request);
    return sessionIds.get(sessionAlias);
  }

  /**
   * Get current session alias
   *
   * @param request Servlet request
   * @return Session alias
   */
  public String getCurrentSessionAlias(HttpServletRequest request) {
    return getCometUUIDFromRequest(request);
  }

  /**
   * Get new session alias from comet uid
   *
   * @param request Servlet request
   * @return Session alias
   */
  public String getNewSessionAlias(HttpServletRequest request) {
    return getCometUUIDFromRequest(request);
  }

  /**
   * On new session creation
   *
   * @param session Session
   * @param request Servlet request
   * @param response Servlet response
   */
  public void onNewSession(Session session, HttpServletRequest request,
                           HttpServletResponse response) {
    Set<String> sessionIdsWritten = getSessionIdsWritten(request);
    String sessionAlias = getCurrentSessionAlias(request);

    if(sessionAlias == null){
      return;
    }

    if (sessionIdsWritten.contains(session.getId())) {
      return;
    }
    sessionIdsWritten.add(session.getId());

    Map<String, String> sessionIds = getSessionIds(request);
    sessionIds.put(sessionAlias, session.getId());

    String cookieValue = createSessionCookieValue(sessionIds);
    this.cookieSerializer
            .writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));
  }

  /**
   * Get current session ids
   *
   * @param request Servlet request
   * @return Session Id List
   */
  @SuppressWarnings ("unchecked")
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
  private String createSessionCookieValue(Map<String, String> sessionIds) {
    if (sessionIds.isEmpty()) {
      return "";
    }

    StringBuffer buffer = new StringBuffer();
    for (Map.Entry<String, String> entry : sessionIds.entrySet()) {
      String alias = entry.getKey();
      String id = entry.getValue();

      buffer.append(alias);
      buffer.append(this.serializationDelimiter);
      buffer.append(id);
      buffer.append(this.serializationDelimiter);
    }
    buffer.deleteCharAt(buffer.length() - 1);
    return buffer.toString();
  }

  /**
   * Invalidate session
   *
   * @param request Servlet request
   * @param response Servlet response
   */
  public void onInvalidateSession(HttpServletRequest request,
                                  HttpServletResponse response) {
    Map<String, String> sessionIds = getSessionIds(request);
    String requestedAlias = getCurrentSessionAlias(request);
    sessionIds.remove(requestedAlias);

    String cookieValue = createSessionCookieValue(sessionIds);
    this.cookieSerializer
            .writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));
  }

  /**
   * Sets the name of the HTTP parameter that is used to specify the session alias. If
   * the value is null, then only a single session is supported per browser.
   *
   * @param sessionAliasParamName the name of the HTTP parameter used to specify the
   *                              session alias. If null, then ony a single session is supported per browser.
   */
  public void setSessionAliasParamName(String sessionAliasParamName) {
    this.sessionParam = sessionAliasParamName;
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
   * Sets the name of the cookie to be used.
   *
   * @param cookieName the name of the cookie to be used
   *
   * @deprecated use {@link #setCookieSerializer(CookieSerializer)}
   */
  @Deprecated
  public void setCookieName(String cookieName) {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();
    serializer.setCookieName(cookieName);
    this.cookieSerializer = serializer;
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
  public Map<String, String> getSessionIds(HttpServletRequest request) {
    List<String> cookieValues = this.cookieSerializer.readCookieValues(request);
    String sessionCookieValue = cookieValues.isEmpty() ? ""
            : cookieValues.iterator().next();
    Map<String, String> result = new LinkedHashMap<String, String>();
    StringTokenizer tokens = new StringTokenizer(sessionCookieValue,
            this.deserializationDelimiter);
    while (tokens.hasMoreTokens()) {
      String alias = tokens.nextToken();
      if (!tokens.hasMoreTokens()) {
        break;
      }
      String id = tokens.nextToken();
      result.put(alias, id);
    }
    return result;
  }

  /**
   * Encode url with session value
   *
   * @param url URL
   * @param sessionAlias Session alias
   * @return URL encoded
   */
  @Override
  public String encodeURL(String url, String sessionAlias) {
    return url;
  }

  /**
   * Get alias from current request
   *
   * @param request Servlet request
   * @return Comet UUID
   */
  public String getCometUUIDFromRequest(HttpServletRequest request) {
    try {
      JsonNode jsonRequest = new ObjectMapper().reader().readTree(String.valueOf(request.getParameter(securityParameter)));
      return jsonRequest.get(sessionAliasParameter).textValue();
    } catch (Exception e) {
      return request.getParameter(sessionAliasParameter);
    }
  }
}
