package com.almis.awe.annotation.entities.util;

import com.almis.awe.annotation.aspect.SessionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to add a ClientAction on return
 *
 *
 * @author dfuentes
 * Created by dfuentes on 13/04/2017.
 * @see SessionAnnotation
 */
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface Download {

  /**
   * Base path enum
   */
  enum BasePath {
    TEMPORAL_FOLDER,
    APPLICATION_BASEPATH,
    CUSTOM
  }

  /**
   * Spring expression language
   *
   * @return
   */
  String value() default "";

  /**
   * Screen name
   *
   * @return
   */
  String file() default "";

  /**
   * Set file base path
   *
   * @return
   */
  BasePath basePath() default BasePath.CUSTOM;

  /**
   * File name
   *
   * @return
   */
  String name();
}
