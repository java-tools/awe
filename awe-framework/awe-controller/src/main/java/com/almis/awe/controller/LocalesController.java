package com.almis.awe.controller;

import com.almis.awe.service.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Manage all incoming action requests
 */
@RestController
@RequestMapping("/locales")
public class LocalesController {

  // Autowired services
  private final LocaleService localeService;

  /**
   * Autowired constructor
   *
   * @param localeService Locale service
   */
  @Autowired
  public LocalesController(LocaleService localeService) {
    this.localeService = localeService;
  }

  /**
   * Retrieve all locales for a language
   *
   * @param language Language
   * @return Locale list
   */
  @GetMapping("/{language}")
  public Map<String, String> getLocalesForLanguage(@PathVariable("language") String language) {
    // Launch action
    return localeService.getLocaleResource(language);
  }
}
