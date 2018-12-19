package com.almis.awe.annotation.aspect;

import com.almis.awe.annotation.entities.security.Crypto;
import com.almis.awe.annotation.processor.security.CryptoProcessor;
import com.almis.awe.annotation.util.AnnotationUtils;
import com.almis.awe.exception.AWException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;

/**
 * Crypto annotation processor containing pointcuts and advises
 *
 * @see Crypto
 * @see CryptoProcessor
 * @author dfuentes
 * Created by dfuentes on 29/05/2017.
 */
@Aspect
public class CryptoAnnotation {

  // Autowired services
  private CryptoProcessor cryptoProcessor;

  /**
   * Autowired constructor
   * @param cryptoProcessor Crypto processor
   */
  @Autowired
  public CryptoAnnotation(CryptoProcessor cryptoProcessor) {
    this.cryptoProcessor = cryptoProcessor;
  }

  /**
   * Crypto method pointcut
   */
  @Pointcut ("@annotation(com.almis.awe.annotation.entities.security.Crypto)")
  public void cryptoMethodPointcut(){
    //This is a pointcut for Crypto annotations
  }

  /**
   * Crypto arguments pointcut
   */
  @Pointcut("execution(* *(@com.almis.awe.annotation.entities.security.Crypto (*)))")
  public void cryptoArgumentPointcut(){
    // This is a pointcut for Crypto annotations
  }

  /**
   * Crypto annotation processor for method annotations
   *
   * @param proceedingJoinPoint Join point
   * @return Result
   * @throws AWException Error on pointcut
   */
  @Around ("com.almis.awe.annotation.aspect.CryptoAnnotation.cryptoMethodPointcut()")
  public String cryptoMethodProcessor(ProceedingJoinPoint proceedingJoinPoint) throws AWException {
    // Process join point
    String result = (String) AnnotationUtils.processJoinPoint(proceedingJoinPoint);
    return cryptoProcessor.processCrypto(((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(Crypto.class), result);
  }

  /**
   * Crypto annotation processor for argument annotations
   *
   * @param proceedingJoinPoint Join point
   * @throws AWException Error on pointcut
   */
  @Around("com.almis.awe.annotation.aspect.CryptoAnnotation.cryptoArgumentPointcut()")
  public Object cryptoArgumentProcessor(ProceedingJoinPoint proceedingJoinPoint) throws AWException {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Object[] args = proceedingJoinPoint.getArgs();
    int paramIndex = 0;

    for (Annotation[] annotations : signature.getMethod().getParameterAnnotations()) {
      for (Annotation annotation : annotations) {
        if (annotation != null){
          // Get current field object
          Object arg = proceedingJoinPoint.getArgs()[paramIndex];

          // Apply processors
          if (annotation instanceof Crypto) {
            arg = cryptoProcessor.processCrypto((Crypto) annotation, (String) arg);
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
