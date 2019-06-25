package com.almis.awe.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.settings.WebSettings;
import com.almis.awe.model.type.LaunchPhaseType;
import com.almis.awe.service.InitService;
import com.almis.awe.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Manage settings request
 */
@RestController
@RequestMapping("/settings")
public class SettingsController extends ServiceConfig {

  // Autowired services
  private Environment environment;
  private MenuService menuService;
  private InitService initService;
  private String cookieName;
  private String cookiePath;
  private String cookieDomain;

  /**
   * Initialize controller
   *
   * @param environment Environment
   * @param menuService Menu service
   * @param initService Init service
   */
  @Autowired
  public SettingsController(Environment environment, MenuService menuService, InitService initService) {
    this.environment = environment;
    this.menuService = menuService;
    this.initService = initService;
  }

  /**
   * Set application cookie name
   *
   * @param cookieName Cookie name
   * @return Controller
   */
  @Value("${application.cookie.name:}")
  public SettingsController setCookieName(String cookieName) {
    this.cookieName = cookieName == null ? "" : cookieName;
    return this;
  }

  /**
   * Set application cookie path
   *
   * @param cookiePath Cookie path
   * @return Controller
   */
  @Value("${application.cookie.path:/}")
  public SettingsController setCookiePath(String cookiePath) {
    this.cookiePath = cookiePath;
    return this;
  }

  /**
   * Set application cookie domain pattern
   *
   * @param cookieDomain Cookie domain pattern
   * @return Controller
   */
  @Value("${application.cookie.pattern:}")
  public SettingsController setCookieDomain(String cookieDomain) {
    this.cookieDomain = cookieDomain;
    return this;
  }

  /**
   * Retrieve application settings
   *
   * @param request  Servlet request
   * @param response Servlet response
   * @return WebSettings
   * @throws AWException Error generating settings
   */
  @PostMapping
  public WebSettings getSettings(HttpServletRequest request, HttpServletResponse response) throws AWException {
    WebSettings settings = getBean(WebSettings.class).toBuilder().build();

    // Launch client start service
    initService.launchPhaseServices(LaunchPhaseType.CLIENT_START);

    // Overwrite settings parameters
    overwriteSettingParameters(settings);

    // Generate cookie if defined
    generateApplicationCookie(request, response);

    // Handle initial redirection
    handleInitialRedirection(settings, request);

    // Retrieve settings
    return settings;
  }

  /**
   * Overwrite setting parameters
   *
   * @param settings Settings
   */
  private void overwriteSettingParameters(WebSettings settings) {
    if (getSession().getParameter(String.class, AweConstants.SESSION_LANGUAGE) != null) {
      settings.setLanguage(getSession().getParameter(String.class, AweConstants.SESSION_LANGUAGE));
    } else {
      settings.setLanguage(environment.getProperty(AweConstants.PROPERTY_SETTINGS_HEADER + AweConstants.SESSION_LANGUAGE));
    }
    if (getSession().getParameter(String.class, AweConstants.SESSION_THEME) != null) {
      settings.setTheme(getSession().getParameter(String.class, AweConstants.SESSION_THEME));
    } else {
      settings.setTheme(environment.getProperty(AweConstants.PROPERTY_SETTINGS_HEADER + AweConstants.SESSION_THEME));
    }
    if (getSession().getParameter(String.class, AweConstants.SESSION_INITIAL_URL) != null) {
      settings.setInitialURL(getSession().getParameter(String.class, AweConstants.SESSION_INITIAL_URL));
    } else {
      settings.setInitialURL(environment.getProperty(AweConstants.PROPERTY_SETTINGS_HEADER + AweConstants.SESSION_INITIAL_URL));
    }
    if (getSession().getParameter(String.class, AweConstants.SESSION_TOKEN) != null) {
      settings.setCometUID(getSession().getParameter(String.class, AweConstants.SESSION_TOKEN));
    } else {
      settings.setCometUID(null);
    }
  }

  /**
   * Generate application cookie if defined
   *
   * @param request  Request
   * @param response Response
   */
  private void generateApplicationCookie(HttpServletRequest request, HttpServletResponse response) {
    if (!cookieName.isEmpty() && !findCookie(request.getCookies(), cookieName)) {
      String uuid = UUID.randomUUID().toString();
      Cookie cookie = new Cookie(cookieName, uuid.substring(0, uuid.lastIndexOf('-')));
      cookie.setHttpOnly(true);
      cookie.setSecure(request.isSecure());
      cookie.setPath(cookiePath);
      cookie.setDomain(cookieDomain);
      response.addCookie(cookie);
    }
  }

  /**
   * Handle initial redirection
   *
   * @param settings Web settings
   * @param request  Request
   * @throws AWException Error handling redirection
   */
  private void handleInitialRedirection(WebSettings settings, HttpServletRequest request) throws AWException {
    String referer = request.getHeader("referer");
    String origin = request.getHeader("origin");
    String basePath = origin + request.getContextPath() + AweConstants.FILE_SEPARATOR;
    if (referer != null && origin != null && referer.startsWith(basePath)) {
      String address = referer.replace(basePath, "");
      settings.setReloadCurrentScreen(!menuService.checkOptionAddress(address));
    } else {
      settings.setReloadCurrentScreen(false);
    }
  }

  /**
   * Find if cookie is already defined
   *
   * @param cookieJar Cookie list
   * @param cookieId  Cookie id
   * @return found
   */
  private boolean findCookie(Cookie[] cookieJar, String cookieId) {
    if (cookieJar == null) return false;
    for (Cookie cookie : cookieJar) {
      if (cookieId.equalsIgnoreCase(cookie.getName())) {
        return true;
      }
    }
    return false;
  }
}
