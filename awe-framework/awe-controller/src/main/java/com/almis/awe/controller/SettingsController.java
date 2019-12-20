package com.almis.awe.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.settings.WebSettings;
import com.almis.awe.service.InitService;
import com.almis.awe.service.MenuService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

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
  private AweRequest request;

  /**
   * Initialize controller
   *
   * @param environment Environment
   * @param menuService Menu service
   * @param initService Init service
   * @param request AWE request
   */
  @Autowired
  public SettingsController(Environment environment, MenuService menuService, InitService initService, AweRequest request) {
    this.environment = environment;
    this.menuService = menuService;
    this.initService = initService;
    this.request = request;
  }

  /**
   * Retrieve application settings
   * @param token initial token
   * @param httpServletRequest  Servlet request
   * @return WebSettings
   * @throws AWException Error generating settings
   */
  @PostMapping
  public WebSettings getSettings(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                 HttpServletRequest httpServletRequest) throws AWException {
    WebSettings settings = getBean(WebSettings.class).toBuilder().build();

    // Initialize parameters
    request.init(JsonNodeFactory.instance.objectNode(), token);

    // Launch client start service
    initService.onClientStart();

    // Overwrite settings parameters
    overwriteSettingParameters(settings);

    // Handle initial redirection
    handleInitialRedirection(settings, httpServletRequest);

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
    settings.setCometUID(getRequest().getToken());
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
}
