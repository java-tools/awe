package com.almis.awe.annotation.entities.audit;

import com.almis.awe.annotation.aspect.AuditAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Audit annotation
 *
 * This annotation can audit a full class or specified methods depending on the place the annotation is set
 *
 * @see AuditAnnotation
 * @see AuditParams
 * @author dfuentes Created by dfuentes on 01/06/2017.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

  /**
   * Annotation to define which elements of the audited methods are going to be showed
   *
   * @return
   */
  AuditParams value();
}
