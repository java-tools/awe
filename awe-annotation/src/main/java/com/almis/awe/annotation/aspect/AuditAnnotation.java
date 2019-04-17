package com.almis.awe.annotation.aspect;

import com.almis.awe.annotation.entities.audit.Audit;
import com.almis.awe.annotation.entities.audit.AuditParams;
import com.almis.awe.annotation.util.AnnotationUtils;
import com.almis.awe.exception.AWException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Audit annotations pointcuts and advices
 *
 * @author dfuentes
 * Created by dfuentes on 01/06/2017.
 * @see Audit
 * @see AuditParams
 */
@Aspect
public class AuditAnnotation {

  // Logger
  private static final Logger logger = LogManager.getLogger(AuditAnnotation.class);

  /**
   * Pointcut for annotated methods
   */
  @Pointcut ("execution(@com.almis.awe.annotation.entities.audit.Audit * *.*(..))")
  void annotatedMethod() {
    //This is a pointcut for Audit annotations
  }

  /**
   * Pointcut for methods in annotated classes
   */
  @Pointcut ("execution(* (@com.almis.awe.annotation.entities.audit.Audit *).*(..))")
  void methodOfAnnotatedClass() {
    //This is a pointcut for Audit annotations
  }


  /**
   * Audit advise method for class and method Audit annotations
   *
   * @param proceedingJoinPoint Join point
   *
   * @throws AWException Error on pointcut
   */
  @Around ("com.almis.awe.annotation.aspect.AuditAnnotation.annotatedMethod() || com.almis.awe.annotation.aspect.AuditAnnotation.methodOfAnnotatedClass()")
  public Object auditClassProcessor(ProceedingJoinPoint proceedingJoinPoint) throws AWException {
    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
    Audit audit = org.springframework.core.annotation.AnnotationUtils.getAnnotation(proceedingJoinPoint.getSignature().getDeclaringType(), Audit.class);
    Audit annotationAudit = org.springframework.core.annotation.AnnotationUtils.getAnnotation(methodSignature.getMethod(), Audit.class);
    String currentExecutionId = UUID.randomUUID().toString();

    //Method audit has preference
    if (audit == null || annotationAudit != null) {
      audit = annotationAudit;
    }

    AuditParams params = audit.value();

    if (methodSignature.getMethod().isAccessible() || params.privateMethods()) {
      methodOutput(methodSignature.getMethod(), proceedingJoinPoint.getArgs(), methodSignature.getParameterNames(), methodSignature.getParameterTypes(), currentExecutionId);

      // Process join point
      Object result = AnnotationUtils.processJoinPoint(proceedingJoinPoint);
      returnOutput(audit, result, currentExecutionId);
      return result;
    }
    return null;
  }

  /**
   * Method output
   *
   * @param method             Method
   * @param args               Arguments
   * @param parameterNames     Parameter names
   * @param parameterClasses   Parameter classes
   * @param currentExecutionId
   */
  private void methodOutput(Method method, Object[] args, String[] parameterNames, Class[] parameterClasses, String currentExecutionId) {
    StringBuilder methodAudit = new StringBuilder();
    methodAudit.append("[AUDIT] [EXECUTION_")
      .append(currentExecutionId)
      .append("] Executed method: ")
      .append(method.getName())
      .append("(");
    for (int i = 0; i < args.length; i++) {
      Object value = args[i];
      String name = parameterNames[i];
      Class type = parameterClasses[i];

      if (Collection.class.isAssignableFrom(type)) {
        processEntry(methodAudit, type.getName(), name);
        methodAudit.append("\n");
        processIterator(methodAudit, ((Collection) value).iterator());
      } else if (Map.class.isAssignableFrom(type)) {
        processEntry(methodAudit, type.getName(), name);
        methodAudit.append("\n");
        processEntrySet(methodAudit, ((Map) value).entrySet());
      } else {
        processEntry(methodAudit, type.getName(), name);
        methodAudit.append(value.toString())
          .append(i == args.length - 1 ? "" : ",");
      }
      methodAudit.append(i == args.length - 1 ? "" : ",")
        .append(")");
      logger.info(methodAudit);
    }
  }

  /**
   * Return value output
   *
   * @param audit              Audit
   * @param currentExecutionId
   * @param result             Result
   */
  private void returnOutput(Audit audit, Object result, String currentExecutionId) {
    if (audit.value().returnValues() && result != null) {
      StringBuilder methodAudit = new StringBuilder();
      methodAudit.append("[AUDIT] [EXECUTION_")
        .append(currentExecutionId)
        .append("] [RESULT] Return value: <")
        .append(result.getClass().getName())
        .append("> ");
      if (Collection.class.isAssignableFrom(result.getClass())) {
        methodAudit.append("\n");
        processIterator(methodAudit, ((Collection) result).iterator());
      } else if (Map.class.isAssignableFrom(result.getClass())) {
        methodAudit.append("\n");
        processEntrySet(methodAudit, ((Map) result).entrySet());
      } else {
        methodAudit.append(result.toString());
      }
      logger.info(methodAudit);
    }
  }

  /**
   * Process entry
   * @param builder
   * @param type
   * @param name
   */
  private void processEntry(StringBuilder builder, String type, String name) {
    builder.append("<")
      .append(type)
      .append("> (")
      .append(name)
      .append(") ");
  }

  /**
   * Process entry set
   * @param builder
   * @param entrySet
   */
  private void processEntrySet(StringBuilder builder, Set<Map.Entry> entrySet) {
    for (Map.Entry entry : entrySet) {
      builder.append("[")
        .append(entry.getKey())
        .append("] ")
        .append(entry.getValue())
        .append(",\n");
    }
  }

  /**
   * Process iterator
   * @param builder
   * @param iterator
   */
  private void processIterator(StringBuilder builder, Iterator iterator) {
    int indexParam = 0;
    while (iterator.hasNext()) {
      builder.append("[")
        .append(indexParam++)
        .append("] ")
        .append(iterator.next())
        .append(",\n");
    }
  }
}
