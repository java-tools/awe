package com.almis.awe.annotation.entities.session;

import com.almis.awe.annotation.aspect.SessionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to add parameter values to session context
 *
 *
 * @see SessionAnnotation
 * @author dfuentes
 * Created by dfuentes on 13/04/2017.
 */
@Target ({ElementType.PARAMETER, ElementType.METHOD})
@Retention (RetentionPolicy.RUNTIME)
public @interface ToSession {

  /**
   * Parameter name
   *
   * @return
   */
  String name() default "";

  /**
   * Override parameter if already exists
   *
   * @return
   */
  boolean overrideIfExist() default true;
}
