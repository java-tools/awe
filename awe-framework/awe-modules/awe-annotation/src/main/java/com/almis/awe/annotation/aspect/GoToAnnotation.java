package com.almis.awe.annotation.aspect;

import com.almis.awe.annotation.entities.audit.Audit;
import com.almis.awe.annotation.entities.audit.AuditParams;
import com.almis.awe.annotation.entities.util.GoTo;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * GoTo annotations pointcuts and advices
 *
 * @author dfuentes
 * Created by dfuentes on 01/06/2017.
 * @see Audit
 * @see AuditParams
 */
@Aspect
public class GoToAnnotation {

  /**
   * Pointcut for annotated methods
   */
  @Pointcut ("execution(@com.almis.awe.annotation.entities.util.GoTo * *.*(..))")
  void annotatedMethod() {
    //This is a pointcut for Audit annotations
  }

  /**
   * GoTo annotation on methods
   *
   * @param proceedingJoinPoint Join point
   *
   * @throws AWException Error on pointcut
   */
  @Around ("com.almis.awe.annotation.aspect.GoToAnnotation.annotatedMethod()")
  public Object goToMethodProcessor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
    GoTo goToAnnotation = org.springframework.core.annotation.AnnotationUtils.getAnnotation(methodSignature.getMethod(), GoTo.class);

    Object result = proceedingJoinPoint.proceed();

    if (result instanceof ServiceData) {
      if (!goToAnnotation.screenName().isEmpty()) {
        ServiceData resultObj = (ServiceData) result;

        ClientAction clientAction = generateClientAction(goToAnnotation);
        resultObj.addClientAction(clientAction);

        return resultObj;
      }
    } else if (result instanceof ClientAction && !goToAnnotation.screenName().isEmpty()) {
      return generateClientAction(goToAnnotation);
    }
    return result;
  }

  /**
   * Create Client Action from annotation
   *
   * @param goToAnnotation
   *
   * @return
   */
  private ClientAction generateClientAction(GoTo goToAnnotation) {

    //Create new client action
    return new ClientAction("screen")
      .setTarget(goToAnnotation.screenName());
  }
}
