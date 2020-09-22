package com.almis.awe.annotation.aspect;

import com.almis.awe.annotation.entities.session.FromSession;
import com.almis.awe.annotation.entities.session.ToSession;
import com.almis.awe.annotation.processor.session.SessionProcessor;
import com.almis.awe.annotation.util.AnnotationUtils;
import com.almis.awe.exception.AWException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

/**
 * Session annotations processor containing pointcuts and advises
 *
 * @author dfuentes
 * Created by dfuentes on 29/05/2017.
 * @see FromSession
 * @see ToSession
 * @see SessionProcessor
 */
@Aspect
public class SessionAnnotation {

  // Autowired services
  private final SessionProcessor sessionProcessor;

  /**
   * Autowired constructor
   *
   * @param sessionProcessor Session processor
   */
  public SessionAnnotation(SessionProcessor sessionProcessor) {
    this.sessionProcessor = sessionProcessor;
  }

  /**
   * FromSession method pointcut
   */
  @Pointcut("@annotation(com.almis.awe.annotation.entities.session.FromSession)")
  public void fromSessionMetPointcut() {
    //This is a pointcut for Session annotations
  }

  /**
   * ToSession method pointcut
   */
  @Pointcut("@annotation(com.almis.awe.annotation.entities.session.ToSession)")
  public void toSessionMetPointcut() {
    //This is a pointcut for Session annotations
  }

  /**
   * FromSession arguments pointcut
   */
  @Pointcut("execution(* *(@com.almis.awe.annotation.entities.session.FromSession (*)))")
  public void fromSessionArgPointcut() {
    //This is a pointcut for Session annotations
  }

  /**
   * ToSession arguments pointcut
   */
  @Pointcut("execution(* *(@com.almis.awe.annotation.entities.session.ToSession (*)))")
  public void toSessionArgPointcut() {
    //This is a pointcut for Session annotations
  }

  /**
   * FromSession annotation processor for method annotations
   *
   * @param proceedingJoinPoint Join point
   * @return Result
   * @throws AWException Error on pointcut
   */
  @Around("com.almis.awe.annotation.aspect.SessionAnnotation.fromSessionMetPointcut()")
  public Object fromSessionMethod(ProceedingJoinPoint proceedingJoinPoint) throws AWException {

    // Process join point
    Object result = AnnotationUtils.processJoinPoint(proceedingJoinPoint);

    FromSession fromSession = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(FromSession.class);
    Object obj = sessionProcessor.getFromSession(fromSession, result.getClass());

    if (fromSession.overrideValue() && (obj != null || (fromSession.onNullSessionValue() == FromSession.OnNullBehaviour.OVERRIDE))) {
      return obj;
    }
    return result;
  }

  /**
   * FromSession annotation processor for argument annotations
   *
   * @param proceedingJoinPoint Join point
   * @throws AWException Error on pointcut
   */
  @Around("com.almis.awe.annotation.aspect.SessionAnnotation.fromSessionArgPointcut()")
  public Object fromSessionArgument(ProceedingJoinPoint proceedingJoinPoint) throws AWException {
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
    Object[] args = proceedingJoinPoint.getArgs();
    int paramIndex = 0;

    for (Annotation[] annotations : signature.getMethod().getParameterAnnotations()) {
      for (Annotation annotation : annotations) {
        if (annotation != null) {
          /* Save value */
          args[paramIndex] = getArgument(proceedingJoinPoint, paramIndex, annotation);
        }
      }
      paramIndex++;
    }
    // Process join point
    return AnnotationUtils.processJoinPoint(proceedingJoinPoint, args);
  }

  /**
   * Retrieve argument from join point
   *
   * @param index
   * @param point
   * @param annotation
   * @return
   */
  private Object getArgument(ProceedingJoinPoint point, int index, Annotation annotation) {
    // Get current field object
    Object arg = point.getArgs()[index];

    // Apply processors
    if (annotation instanceof FromSession) {
      Object modified = sessionProcessor.getFromSession((FromSession) annotation, arg.getClass());

      if (((FromSession) annotation).overrideValue() && (modified != null || ((FromSession) annotation).onNullSessionValue() == FromSession.OnNullBehaviour.OVERRIDE)) {
        arg = modified;
      }
    }

    return arg;
  }

  /**
   * ToSession annotation processor for method annotations
   *
   * @param joinPoint Join point
   */
  @AfterReturning(value = "com.almis.awe.annotation.aspect.SessionAnnotation.toSessionMetPointcut()", returning = "result")
  public void toSessionMethod(JoinPoint joinPoint, Object result) {
    // Process join point
    ToSession toSession = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ToSession.class);
    sessionProcessor.setToSession(toSession, result);
  }

  /**
   * ToSession annotation processor for argument annotations
   *
   * @param joinPoint Join point
   */
  @Before("com.almis.awe.annotation.aspect.SessionAnnotation.toSessionArgPointcut()")
  public void toSessionArgument(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    int paramIndex = 0;

    for (Annotation[] annotations : signature.getMethod().getParameterAnnotations()) {
      for (Annotation annotation : annotations) {
        if (annotation != null) {
          // Get current field object
          Object arg = joinPoint.getArgs()[paramIndex];

          // Apply processors
          if (annotation instanceof ToSession) {
            sessionProcessor.setToSession((ToSession) annotation, arg);
          }
        }
      }
      paramIndex++;
    }
  }
}
