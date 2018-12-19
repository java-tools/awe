package com.almis.awe.annotation.entities.locale;

import com.almis.awe.annotation.aspect.LocaleAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to translate Locale identifiers to their respective translated value
 *
 * @see LocaleAnnotation
 * @author dfuentes
 * Created by dfuentes on 28/04/2017.
 */
@Target ({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention (RetentionPolicy.RUNTIME)
public @interface Locale {
  /**
   * Locale identifier to be translated
   *
   * @return
   */
  String value() default "";

  /**
   * Language to translate to
   *
   * @return
   */
  String language() default "";

  /**
   * Parameters to be added to the translated locale
   *
   * @return
   */
  String[] params() default "";
}
