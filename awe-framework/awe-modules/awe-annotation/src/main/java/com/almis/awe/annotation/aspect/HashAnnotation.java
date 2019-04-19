package com.almis.awe.annotation.aspect;

import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.annotation.processor.security.HashProcessor;
import com.almis.awe.annotation.util.AnnotationUtils;
import com.almis.awe.exception.AWException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

/**
 * Hash annotation processor containing pointcuts and advises
 *
 * @see Hash
 * @see HashProcessor
 * @author dfuentes
 * Created by dfuentes on 29/05/2017.
 */
@Aspect
public class HashAnnotation {

  /**
   * Hash method pointcut
   */
  @Pointcut("@annotation(com.almis.awe.annotation.entities.security.Hash)")
  public void hashMethodPointcut(){
    //This is a pointcut for Hash annotations
  }

  /**
   * Hash arguments pointcut
   */
  @Pointcut("execution(* *(@com.almis.awe.annotation.entities.security.Hash (*)))")
  public void hashArgumentPointcut(){
    //This is a pointcut for Hash annotations
  }

  /**
   * Hash annotation processor for method annotations
   *
   * @param proceedingJoinPoint Join point
   * @return Result
   * @throws AWException Error on pointcut
   */
  @Around ("com.almis.awe.annotation.aspect.HashAnnotation.hashMethodPointcut()")
  public String hashMethodProcessor(ProceedingJoinPoint proceedingJoinPoint) throws AWException {

    // Process join point
    String result = AnnotationUtils.processJoinPoint(proceedingJoinPoint);
    return HashProcessor.processHashing(((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(Hash.class), result);
  }

  /**
   * Hash annotation processor for argument annotations
   *
   * @param proceedingJoinPoint Join point
   * @throws AWException Error on pointcut
   */
  @Around("com.almis.awe.annotation.aspect.HashAnnotation.hashArgumentPointcut()")
  public Object hashArgumentProcessor(ProceedingJoinPoint proceedingJoinPoint) throws AWException {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Object[] args = proceedingJoinPoint.getArgs();
    int paramIndex = 0;

    for (Annotation[] annotations : signature.getMethod().getParameterAnnotations()) {
      for (Annotation annotation : annotations) {
        if (annotation != null){
          // Get current field object
          Object arg = proceedingJoinPoint.getArgs()[paramIndex];

          // Apply processors
          if (annotation instanceof Hash) {
            arg = HashProcessor.processHashing(((Hash) annotation), (String) arg);
          }

          // Save value
          args[paramIndex] = arg;
        }
      }
      paramIndex++;
    }

    // Process join point
    return AnnotationUtils.processJoinPoint(proceedingJoinPoint, args);
  }
}
