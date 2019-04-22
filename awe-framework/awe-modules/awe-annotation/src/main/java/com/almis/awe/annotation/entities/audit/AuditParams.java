package com.almis.awe.annotation.entities.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that defines the elements to be audited on @Audit annotations
 *
 * @see Audit
 * @author dfuentes
 * Created by dfuentes on 01/06/2017.
 */
@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface AuditParams {

  /**
   * Defines if the private methods are going too be audited
   *
   * This is used when the Audit annotation is placed in class level.
   *
   * @return
   */
  boolean privateMethods();

  /**
   * Defines if the return value needs to be audited
   *
   * @return
   */
  boolean returnValues();

}
