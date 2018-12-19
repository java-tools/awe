package com.almis.awe.annotation.entities.session;

import com.almis.awe.annotation.aspect.SessionAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to get parameter values from session context
 *
 *
 * @see SessionAnnotation
 * @author dfuentes
 * Created by dfuentes on 13/04/2017.
 */
@Target ({ElementType.PARAMETER, ElementType.METHOD})
@Retention (RetentionPolicy.RUNTIME)
public @interface FromSession {

  /**
   * Null behaviour enumerate
   *
   */
  enum OnNullBehaviour{
    OVERRIDE, IGNORE
  }

  /**
   * Parameter name
   *
   * @return
   */
  String name() default "";

  /**
   * Override field/input parameter value if already set
   *
   * @return
   */
  boolean overrideValue() default true;

  /**
   * Defines what to do when returned session value is null
   *
   * @return
   */
  OnNullBehaviour onNullSessionValue() default OnNullBehaviour.IGNORE;
}
