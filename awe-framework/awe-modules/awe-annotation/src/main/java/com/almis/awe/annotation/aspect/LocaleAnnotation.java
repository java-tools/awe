package com.almis.awe.annotation.aspect;

import com.almis.awe.annotation.entities.locale.Locale;
import com.almis.awe.annotation.processor.locale.LocaleProcessor;
import com.almis.awe.annotation.util.AnnotationUtils;
import com.almis.awe.exception.AWException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

/**
 * This class contains the pointcuts and advises for the @Locale annotation
 *
 * @author dfuentes
 * Created by dfuentes on 29/05/2017.
 * @see Locale
 * @see LocaleProcessor
 */
@Aspect
public class LocaleAnnotation {
  // Autowired services
  private final LocaleProcessor localeProcessor;

  /**
   * Autowired constructor
   *
   * @param localeProcessor Locale processor
   */
  public LocaleAnnotation(LocaleProcessor localeProcessor) {
    this.localeProcessor = localeProcessor;
  }

  /**
   * Locale method pointcut
   */
  @Pointcut("@annotation(com.almis.awe.annotation.entities.locale.Locale)")
  public void localeMethodPointcut() {
    //This is a pointcut for Locale annotations
  }

  /**
   * Locale arguments pointcut
   */
  @Pointcut("execution(* *(@com.almis.awe.annotation.entities.locale.Locale (*)))")
  public void localeArgumentPointcut() {
    //This is a pointcut for Locale annotations
  }


  /**
   * Locale annotation processor for method annotations
   *
   * @param proceedingJoinPoint Join point
   * @return Result
   * @throws AWException Error on pointcut
   */
  @Around("com.almis.awe.annotation.aspect.LocaleAnnotation.localeMethodPointcut()")
  public String localeMethodProcessor(ProceedingJoinPoint proceedingJoinPoint) throws AWException {

    // Process join point
    String result = AnnotationUtils.processJoinPoint(proceedingJoinPoint);
    return localeProcessor.processLocale(((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(Locale.class), result);
  }

  /**
   * Locale annotation processor for argument annotations
   *
   * @param proceedingJoinPoint Join point
   * @throws AWException Error on pointcut
   */
  @Around("com.almis.awe.annotation.aspect.LocaleAnnotation.localeArgumentPointcut()")
  public Object localeArgumentProcessor(ProceedingJoinPoint proceedingJoinPoint) throws AWException {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Object[] args = proceedingJoinPoint.getArgs();
    int paramIndex = 0;

    for (Annotation[] annotations : signature.getMethod().getParameterAnnotations()) {
      for (Annotation annotation : annotations) {
        if (annotation != null) {
          //Get current field object
          Object arg = proceedingJoinPoint.getArgs()[paramIndex];

          /* Apply processors */
          if (annotation instanceof Locale) {
            arg = localeProcessor.processLocale((Locale) annotation, (String) arg);
          }

          /* Save value */
          args[paramIndex] = arg;
        }
      }
      paramIndex++;
    }

    // Process join point
    return AnnotationUtils.processJoinPoint(proceedingJoinPoint, args);
  }
}
