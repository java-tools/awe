package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.dto.ServiceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

/**
 * Context help generation service
 *
 * @author dgutierrez, pgarcia
 */
public class HelpService extends ServiceConfig {

  // Autowired services
  private TemplateService templateService;

  @Value("${module.app.documents:static/docs/awe/}")
  private String documentsPath;

  /**
   * Autowired constructor
   * @param templateService Template service
   */
  @Autowired
  public HelpService(TemplateService templateService) {
    this.templateService = templateService;
  }

  /**
   * Generates context help for all application
   *
   * @return Application help
   */
  @Cacheable(value = "helpTemplates", key = "'default'")
  public String getApplicationHelp() {
    String help;

    try {
      help = templateService.generateApplicationHelpTemplate(false);
    } catch (AWException exc) {
      help = templateService.generateErrorTemplate(exc);
      exc.log();
    } catch (Exception exc) {
      AWException aweException = new AWException("ERROR_TITLE_GENERATING_APPLICATION_HELP", exc.getMessage(), exc);
      help = templateService.generateErrorTemplate(aweException);
      aweException.log();
    }
    return help;
  }

  /**
   * Generates context help for the actual screen
   *
   * @param optionId Option identifier
   * @return Option help
   */
  @Cacheable(value = "helpTemplates", key = "#p0")
  public String getOptionHelp(String optionId) {
    String help;

    try {
      // Generate screen help
      help = templateService.generateOptionHelpTemplate(optionId, false);
    } catch (AWException exc) {
      help = templateService.generateErrorTemplate(exc);
      exc.log();
    } catch (Exception exc) {
      AWException aweException = new AWException("ERROR_TITLE_GENERATING_SCREEN_HELP", exc.getMessage(), exc);
      help = templateService.generateErrorTemplate(aweException);
      aweException.log();
    }
    return help;
  }

  /**
   * Retrieve application manual header
   *
   * @param manualHeader Manual header
   * @return Application manual
   */
  public ServiceData getApplicationManual(String manualHeader) {
    ServiceData serviceData = new ServiceData();
    String fileName = manualHeader + "-" + getSession().getParameter(String.class, AweConstants.SESSION_LANGUAGE).toUpperCase() + ".pdf";
    Resource resource = new ClassPathResource(documentsPath + fileName);
    try {
      FileData fileData = null;
      if (resource.exists()) {
        File helpFile = resource.getFile();
        fileData = new FileData(helpFile.getName(), resource.contentLength(), "application/pdf")
                .setBasePath(helpFile.getParent());
      }
      return serviceData.setData(fileData);
    } catch (IOException exc) {
      AWException aweException = new AWException("ERROR_TITLE_GENERATING_APPLICATION_HELP", exc.getMessage(), exc);
      aweException.log();
      return serviceData;
    }
  }
}
