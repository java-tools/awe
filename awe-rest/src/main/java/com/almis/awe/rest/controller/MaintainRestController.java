package com.almis.awe.rest.controller;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.MaintainService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Maintain endpoint
 */
@RestController
@RequestMapping("/api/maintain")
public class MaintainRestController extends ServiceConfig{

  // Autowired services
  private MaintainService maintainService;

  /**
   * Autowired constructor
   * @param maintainService Maintain service
   */
  @Autowired
  public MaintainRestController(MaintainService maintainService) {
    this.maintainService = maintainService;
  }

  /**
   * Launch a maintain
   * @param maintainId Maintain id
   * @param jsonbody JSON parameters
   * @return JSON response
   */
  @PostMapping("/{maintainId}")
  public ResponseEntity<ServiceData> maintainController(@PathVariable String maintainId, @RequestBody (required = false) JsonNode jsonbody) {
    try {
      if (jsonbody != null) {
        getRequest().setParameterList((ObjectNode) jsonbody);
      }
      return ResponseEntity.ok(maintainService.launchMaintain(maintainId));
    } catch (Exception e) {
      return new ResponseEntity(e , HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
