package com.almis.awe.annotation.processor.locale;

import com.almis.awe.annotation.aspect.LocaleAnnotation;
import com.almis.awe.annotation.entities.locale.Locale;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Locale annotation processor
 *
 * @author dfuentes Created by dfuentes on 28/04/2017.
 * @see Locale
 * @see LocaleAnnotation
 */
public class LocaleProcessor {
  private static final String EMPTY = "";

  // Autowired objects
  ObjectFactory<AweSession> aweSessionObjectFactory;
  ObjectFactory<AweElements> aweElementsObjectFactory;

  /**
   * Autowired constructor
   * @param aweSessionObjectFactory
   * @param aweElementsObjectFactory
   */
  @Autowired
  public LocaleProcessor(final ObjectFactory<AweSession> aweSessionObjectFactory,
                         final ObjectFactory<AweElements> aweElementsObjectFactory) {
    this.aweSessionObjectFactory = aweSessionObjectFactory;
    this.aweElementsObjectFactory = aweElementsObjectFactory;
  }


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
          return getTranslatedLocale(s, aweSessionObjectFactory.getObject().getParameter(String.class, AweConstants.SESSION_LANGUAGE), annotation.params());
        } else {
          return getTranslatedLocale(s, annotation.language(), annotation.params());
        }
      }
    } else {
      if (annotation.language().equalsIgnoreCase(EMPTY)) {
        return getTranslatedLocale(annotation.value(), aweSessionObjectFactory.getObject().getParameter(String.class, AweConstants.SESSION_LANGUAGE), annotation.params());
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
      parsed = aweElementsObjectFactory.getObject().getLocaleWithLanguage(localeId, lang);
    } else {
      parsed = aweElementsObjectFactory.getObject().getLocaleWithLanguage(localeId, lang, (Object[]) params);
    }
    return parsed;
  }
}
