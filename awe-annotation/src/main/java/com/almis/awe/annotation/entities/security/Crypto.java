package com.almis.awe.annotation.entities.security;

import com.almis.awe.annotation.aspect.CryptoAnnotation;
import com.almis.awe.annotation.classload.SecurityAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Encryption annotation to encrypt/decrypt fields and input parameters
 *
 * @see SecurityAnnotationProcessor
 * @see CryptoAnnotation
 * @author dfuentes Created by dfuentes on 15/03/2017.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Crypto {

  /**
   * Action type to apply
   */
  enum ActionType {
    ENCRYPT, DECRYPT
  }

  /**
   * Action type to apply encryp/decrypt
   *
   * @return
   */
  ActionType action();

  /**
   * Password to use with the algorithm
   *
   * @return
   */
  String password();
}
