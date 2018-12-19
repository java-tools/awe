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
 * @see SessionAnnotation
 * @author dfuentes
 * Created by dfuentes on 13/04/2017.
 */
@Target ( ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
public @interface GoTo {

  /**
   * Screen name
   *
   * @return
   */
  String screenName() default "";
}
