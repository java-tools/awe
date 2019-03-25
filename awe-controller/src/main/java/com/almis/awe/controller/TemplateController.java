package com.almis.awe.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.HelpService;
import com.almis.awe.service.TemplateService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * Manage template request
 */
@Controller
@RequestMapping("/template")
public class TemplateController {

  // Autowired services
  private TemplateService templateService;
  private HelpService helpService;
  private LogUtil logger;
  private AweRequest aweRequest;

  // Angular templates path
  @Value("${application.paths.templates.angular:angular/}")
  private String angularPath;

  /**
   * Autowired constructor
   * @param templateService Template service
   * @param helpService Help service
   * @param logger Logger
   * @param aweRequest Request
   */
  @Autowired
  public TemplateController(TemplateService templateService, HelpService helpService, LogUtil logger, AweRequest aweRequest) {
    this.templateService = templateService;
    this.helpService = helpService;
    this.logger = logger;
    this.aweRequest = aweRequest;
  }

  /**
   * Retrieve angular templates
   *
   * @param template Angular template
   * @return Angular template
   */
  @GetMapping("/angular/{template}")
  @Cacheable("angularTemplates")
  public String getAngularTemplate(@PathVariable String template) {
    return angularPath + template;
  }

  /**
   * Retrieve angular module templates
   *
   * @param module   Angular module
   * @param template Angular template
   * @return Angular template
   */
  @GetMapping("/angular/{module}/{template}")
  @Cacheable("angularTemplates")
  public String getAngularSubTemplate(@PathVariable String module, @PathVariable String template) {
    return angularPath + module + "/" + template;
  }

  /**
   * Retrieve screen templates
   *
   * @param view     Screen view
   * @param optionId Option identifier
   * @param response Servlet response
   * @return Screen template
   */
  @GetMapping("/screen/{view}/{optionId}")
  public @ResponseBody
  String getScreenTemplate(@PathVariable String view, @PathVariable String optionId,
                           final HttpServletResponse response) {
    String template = null;
    try {
      template = templateService.getTemplate(view, optionId);
    } catch (AWException exc) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      logger.log(TemplateService.class, Level.ERROR, "Error generating template - view: {0}, option: {1}", exc, view, optionId);
      template = templateService.generateErrorTemplate(exc);
    }
    return template;
  }

  /**
   * Retrieve default screen template
   *
   * @param response Servlet response
   * @return Screen template
   */
  @GetMapping("/screen")
  @Cacheable(value = "screenTemplates", key = "'default'")
  public @ResponseBody
  String getDefaultScreenTemplate(final HttpServletResponse response) {
    String template;
    try {
      template = templateService.getTemplate();
    } catch (AWException exc) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      logger.log(TemplateService.class, Level.ERROR, "Error generating default template - view: {0}", exc, AweConstants.BASE_VIEW);
      template = templateService.generateErrorTemplate(exc);
    }
    return template;
  }

  /**
   * Retrieve taglist template
   *
   * @param tagListId Taglist id
   * @param parameters Parameters
   * @return Taglist template
   */
  @PostMapping(value = "/taglist/{tagListId}", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody
  String generateTaglistTemplate(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                 @PathVariable String tagListId,
                                 @RequestBody ObjectNode parameters) {
    String template = null;
    try {
      // Initialize parameters
      aweRequest.init(parameters, token);

      // Generate taglist template
      template = templateService.generateTaglistTemplate(tagListId);
    } catch (AWException exc) {
      logger.log(TemplateService.class, Level.ERROR, "Error generating taglist template - taglist: {0}", exc, tagListId);
      template = "";
    }
    return template;
  }

  /**
   * Retrieve taglist template
   *
   * @param optionId  Option id
   * @param tagListId Taglist id
   * @param parameters Parameters
   * @return Taglist template
   */
  @PostMapping(value = "/taglist/{optionId}/{tagListId}", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody
  String generateTaglistTemplate(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                 @PathVariable String optionId,
                                 @PathVariable String tagListId,
                                 @RequestBody ObjectNode parameters) {
    String template = null;
    try {
      // Initialize parameters
      aweRequest.init(parameters, token);

      // Generate taglist template
      template = templateService.generateTaglistTemplate(optionId, tagListId);
    } catch (AWException exc) {
      logger.log(TemplateService.class, Level.ERROR, "Error generating taglist template - option: {0}, taglist: {1}", exc, optionId, tagListId);
      template = "";
    }
    return template;
  }

  /**
   * Retrieve application help
   *
   * @return Application help
   */
  @GetMapping("/help")
  @Cacheable("helpTemplates")
  public @ResponseBody
  String getApplicationHelp() {
    return helpService.getApplicationHelp();
  }

  /**
   * Retrieve option help
   *
   * @param option Option
   * @return Option help
   */
  @GetMapping("/help/{option}")
  @Cacheable(value = "helpTemplates", key = "#option")
  public @ResponseBody
  String getOptionHelp(@PathVariable String option) {
    return helpService.getOptionHelp(option);
  }
}
