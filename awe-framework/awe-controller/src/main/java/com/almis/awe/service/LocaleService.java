package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;

import javax.cache.annotation.CacheRemove;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Manage application locales
 */
public class LocaleService extends ServiceConfig {

  /**
   * Change language to locales
   *
   * @param language New language
   * @return change language
   */
  @CacheRemove(cacheName = "locale")
  public ServiceData changeLanguage(String language) {
    getSession().setParameter(AweConstants.SESSION_LANGUAGE, language);
    return new ServiceData();
  }

  /**
   * Generate json object with application locals
   *
   * @return get application locales
   * @throws AWException AWE exception
   */
  public ServiceData getApplicationLocales() throws AWException {
    try {
      // Get language parameter
      String language = getRequest().getParameter(AweConstants.SESSION_LANGUAGE).asText();

      // Write application settings in output
      Map<String, Map<String, String>> locals = getApplicationLocales(language);

      // Store json data as javascript
      ServiceData out = new ServiceData();
      out.addVariable(AweConstants.ACTION_MESSAGE_TYPE, "ok");
      out.addVariable(AweConstants.ACTION_LANGUAGE, language);
      out.addVariable(AweConstants.ACTION_LOCALS, new CellData(locals.get("translations")));

      return out;
    } catch (Exception exc) {
      throw new AWException(getElements().getLocale("ERROR_TITLE_READING_LOCALS"), getElements().getLocale("ERROR_MESSAGE_READING_LOCALS"), exc);
    }
  }

  /**
   * Retrieve application locales
   * 
   * @param language language
   * @return Map with application locales
   */
  private Map<String, Map<String, String>> getApplicationLocales(String language) {

    Map<String, Map<String, String>> locales = new HashMap<>();

    // Add translations as map
    Map<String, String> translations = new HashMap<>();
    locales.put("translations", translations);

    // Get local list for selected language
    Map<String, String> localeList = getElements().getLocales().get(language.toLowerCase());

    // Store local list in translations
    for (Entry<String, String> locale : localeList.entrySet()) {
      translations.put(locale.getKey(), locale.getValue());
    }

    return locales;
  }

}
