package com.almis.awe.rest.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Data endpoint
 */
@RestController
@RequestMapping("/api")
public class AweRestController {

  // Autowired services
  private final QueryService queryService;
  private final MaintainService maintainService;
  private final AweRequest request;

  /**
   * Autowired constructor
   *
   * @param queryService Query service
   */
  @Autowired
  public AweRestController(QueryService queryService, MaintainService maintainService, AweRequest request) {
    this.queryService = queryService;
    this.maintainService = maintainService;
    this.request = request;
  }

  /**
   * Set parameters in request
   *
   * @param parameters Parameters
   */
  private void setParameters(ObjectNode parameters) {
    if (parameters != null) {
      request.setParameterList(parameters);
    }
  }

  /**
   * Request a query
   *
   * @param queryId    Query id
   * @param parameters JSON parameters
   * @return JSON response
   */
  @PostMapping("/data/{queryId}")
  public ResponseEntity dataController(@PathVariable String queryId, @RequestBody(required = false) ObjectNode parameters) throws AWException {
    setParameters(parameters);
    return ResponseEntity.ok(queryService.launchQuery(queryId));
  }

  /**
   * Launch a maintain
   *
   * @param maintainId Maintain id
   * @param parameters JSON parameters
   * @return JSON response
   */
  @PostMapping("/maintain/{maintainId}")
  public ResponseEntity<ServiceData> maintainController(@PathVariable String maintainId, @RequestBody(required = false) ObjectNode parameters) throws AWException {
    setParameters(parameters);
    return ResponseEntity.ok(maintainService.launchMaintain(maintainId));
  }

  /**
   * Handle upload error
   *
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler(AWException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ServiceData handleAWException(AWException exc) {
    // Retrieve exception
    return new ServiceData()
      .setType(exc.getType())
      .setTitle(exc.getTitle())
      .setMessage(exc.getMessage());
  }

  /**
   * Handle upload error
   *
   * @param exc Exception to handle
   * @return Response error
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ServiceData handleException(Exception exc) {
    // Retrieve exception
    return new ServiceData()
      .setType(AnswerType.ERROR)
      .setTitle("Request error")
      .setMessage(exc.getMessage());
  }
}
