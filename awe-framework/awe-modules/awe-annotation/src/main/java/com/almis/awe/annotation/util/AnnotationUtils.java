package com.almis.awe.annotation.util;

import com.almis.awe.exception.AWException;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by pgarcia on 06/06/2017.
 */
public class AnnotationUtils {

  /**
   * Hide constructor
   */
  private AnnotationUtils() {}

  /**
   * Proceed join point
   * @param proceedingJoinPoint Join point
   * @return result
   * @throws AWException Error proceeding join point
   */
  public static <T> T processJoinPoint(ProceedingJoinPoint proceedingJoinPoint) throws AWException {
    return processJoinPoint(proceedingJoinPoint, null);
  }

  /**
   * Proceed join point
   * @param proceedingJoinPoint Join point
   * @param arguments arguments
   * @return result
   * @throws AWException Error proceeding join point
   */
  public static <T> T processJoinPoint(ProceedingJoinPoint proceedingJoinPoint, Object... arguments) throws AWException {
    try {
      T result;
      if (arguments == null) {
         result = (T) proceedingJoinPoint.proceed();
      } else {
         result = (T) proceedingJoinPoint.proceed(arguments);
      }
      return result;
    } catch (Throwable throwable) {
      throw new AWException("Unable to process annotation", "Unable to process argument annotation", throwable);
    }
  }
}
