package com.almis.awe.annotation.processor.locale;

import com.almis.awe.annotation.aspect.LocaleAnnotation;
import com.almis.awe.annotation.entities.locale.Locale;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.constant.AweConstants;

/**
 * Locale annotation processor
 *
 * @author dfuentes Created by dfuentes on 28/04/2017.
 * @see Locale
 * @see LocaleAnnotation
 */
public class LocaleProcessor extends ServiceConfig {
  private static final String EMPTY = "";

  /**
   * Process locale annotation
   *
   * @param annotation
   * @param s
   * @return
   */
  public String processLocale(Locale annotation, String s) {
    if (annotation.value().equalsIgnoreCase(EMPTY)) {
      if (s != null && !s.equalsIgnoreCase(EMPTY)) {
        if (annotation.language().equalsIgnoreCase(EMPTY)) {
          return getTranslatedLocale(s, getSession().getParameter(String.class, AweConstants.SESSION_LANGUAGE), annotation.params());
        } else {
          return getTranslatedLocale(s, annotation.language(), annotation.params());
        }
      }
    } else {
      if (annotation.language().equalsIgnoreCase(EMPTY)) {
        return getTranslatedLocale(annotation.value(), getSession().getParameter(String.class, AweConstants.SESSION_LANGUAGE), annotation.params());
      } else {
        return getTranslatedLocale(annotation.value(), annotation.language(), annotation.params());
      }
    }
    return s;
  }

  /**
   * Parse locale with parameters
   *
   * @param localeId
   * @param lang
   * @param params
   * @return
   */
  private String getTranslatedLocale(String localeId, String lang, String[] params) {
    String parsed;
    if (params == null) {
      parsed = getLocale(localeId, lang);
    } else {
      parsed = getLocale(localeId, params);
    }
    return parsed;
  }
}
