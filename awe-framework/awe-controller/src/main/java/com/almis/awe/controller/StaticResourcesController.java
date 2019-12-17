package com.almis.awe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticResourcesController {

  @Value("${application.icon.favicon}")
  private String faviconIcon;

  @Value("${application.icon.phone}")
  private String phoneIcon;

  @Value("${application.icon.tablet}")
  private String tabletIcon;

  @Value("${application.images.startup.logo}")
  private String startupLogo;

  @Value("${application.images.startup.background}")
  private String startupBackground;

  @Value("${application.images.navbar.logo}")
  private String navbarLogo;

  /**
   * Handler for index page
   *
   * @return Index page
   */
  @GetMapping(value = {"/", "/screen/**"})
  public String index(Model model) {
    model.addAttribute("faviconIcon", faviconIcon);
    model.addAttribute("phoneIcon", phoneIcon);
    model.addAttribute("tabletIcon", tabletIcon);
    return "index";
  }

  /**
   * Parse styles and set image values
   *
   * @param model
   * @return
   */
  @GetMapping("/css/styles{xxx}.css")
  public String getStyles(Model model) {
    model.addAttribute("startupLogo", startupLogo);
    model.addAttribute("startupBackground", startupBackground);
    model.addAttribute("navbarLogo", navbarLogo);
    return "styles.css";
  }
}

