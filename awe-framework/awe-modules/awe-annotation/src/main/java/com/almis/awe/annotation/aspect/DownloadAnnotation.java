package com.almis.awe.annotation.aspect;

import com.almis.awe.annotation.entities.audit.Audit;
import com.almis.awe.annotation.entities.audit.AuditParams;
import com.almis.awe.annotation.entities.util.Download;
import com.almis.awe.annotation.util.SpringExpressionLanguageParser;
import com.almis.awe.builder.client.DownloadActionBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.nio.file.Paths;

/**
 * GoTo annotations pointcuts and advices
 *
 * @author dfuentes
 * Created by dfuentes on 01/06/2017.
 * @see Audit
 * @see AuditParams
 */
@Aspect
public class DownloadAnnotation {

  @Value("${application.base.path:/}")
  private String applicationBasePath;

  @Value("${application.paths.temp:/temp/}")
  private String tempBasePath;

  /**
   * Pointcut for annotated methods
   */
  @Pointcut("execution(@com.almis.awe.annotation.entities.util.Download * *.*(..))")
  void annotatedMethod() {
    //This is a pointcut for Audit annotations
  }

  /**
   * GoTo annotation on methods
   *
   * @param proceedingJoinPoint Join point
   * @throws AWException Error on pointcut
   */
  @Around("com.almis.awe.annotation.aspect.DownloadAnnotation.annotatedMethod()")
  public Object goToMethodProcessor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
    Download downloadAnnotation = org.springframework.core.annotation.AnnotationUtils.getAnnotation(methodSignature.getMethod(), Download.class);

    Object result = proceedingJoinPoint.proceed();
    File file;

    // Get file dynamically with Spring Expression Language
    try {
      file = new SpringExpressionLanguageParser(
        methodSignature.getParameterNames(),
        proceedingJoinPoint.getArgs())
        .getDynamicValue(
          downloadAnnotation.value(),
          File.class);
    } catch (Exception exc) {
      file = null;
    }

    if (!downloadAnnotation.file().isEmpty() && Paths.get(downloadAnnotation.file()).toFile().exists()) {
      switch (downloadAnnotation.basePath()) {
        case APPLICATION_BASEPATH:
          file = new File(applicationBasePath + File.separator + downloadAnnotation.file());
          break;
        case CUSTOM:
          file = new File(downloadAnnotation.file());
          break;
        case TEMPORAL_FOLDER:
        default:
          file = new File(tempBasePath + File.separator + downloadAnnotation.file());
      }
    }

    // Generate client action and return
    if (file != null) {
      FileData fileData = new FileData(file.getName(), file.length(), "application/octet-stream");
      fileData.setBasePath(file.getParent());
      fileData.setFileName(downloadAnnotation.name());

      DownloadActionBuilder actionBuilder = new DownloadActionBuilder(fileData);

      // Return client action
      if (result instanceof ServiceData) {
        return ((ServiceData) result).addClientAction(actionBuilder.build());
      } else if (result instanceof ClientAction) {
        return actionBuilder.build();
      }
    }

    return result;
  }
}
