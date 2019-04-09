package com.almis.awe.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.TemplateService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * Manage template request
 */
@Controller
@RequestMapping("/taglist")
public class TagListController {

  // Autowired services
  private TemplateService templateService;
  private LogUtil logger;
  private AweRequest aweRequest;

  /**
   * Autowired constructor
   * @param templateService Template service
   * @param logger Logger
   * @param aweRequest Request
   */
  @Autowired
  public TagListController(TemplateService templateService, LogUtil logger, AweRequest aweRequest) {
    this.templateService = templateService;
    this.logger = logger;
    this.aweRequest = aweRequest;
  }

  /**
   * Retrieve taglist template
   *
   * @param tagListId Taglist id
   * @param parameters Parameters
   * @return Taglist template
   */
  @PostMapping(value = "/{tagListId}", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody
  String generateTaglistTemplate(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                 @PathVariable String tagListId,
                                 @RequestBody ObjectNode parameters) throws AWException {
    // Initialize parameters
    aweRequest.init(parameters, token);

    // Generate taglist template
    return templateService.generateTaglistTemplate(tagListId);
  }

  /**
   * Retrieve taglist template
   *
   * @param optionId  Option id
   * @param tagListId Taglist id
   * @param parameters Parameters
   * @return Taglist template
   */
  @PostMapping(value = "/{optionId}/{tagListId}", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody
  String generateTaglistTemplate(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                 @PathVariable String optionId,
                                 @PathVariable String tagListId,
                                 @RequestBody ObjectNode parameters) throws AWException {
    // Initialize parameters
    aweRequest.init(parameters, token);

    // Generate taglist template
    return templateService.generateTaglistTemplate(optionId, tagListId);
  }

  /**
   * Handle upload error
   *
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler(AWException.class)
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody
  String handleAWException(AWException exc) {
    // Retrieve exception
    logger.log(TagListController.class, Level.ERROR, "Error generating taglist template", exc);
    return "";
  }
}
