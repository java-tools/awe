package com.almis.awe.rest.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Data endpoint
 */
@RestController
@RequestMapping("/api/data")
public class DataRestController extends ServiceConfig {

  // Autowired services
  private QueryService queryService;

  /**
   * Autowired constructor
   * @param queryService Query service
   */
  @Autowired
  public DataRestController(QueryService queryService) {
    this.queryService = queryService;
  }

  /**
   * Request a query
   * @param queryId Query id
   * @param jsonbody JSON parameters
   * @return JSON response
   */
  @PostMapping ("/{queryId}")
  public ResponseEntity dataController(@PathVariable String queryId, @RequestBody (required = false) JsonNode jsonbody) {
    try {
      if (jsonbody != null) {
        getRequest().setParameterList((ObjectNode) jsonbody);
      }
      return ResponseEntity.ok(queryService.launchQuery(queryId));
    } catch (Exception e) {
      return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
