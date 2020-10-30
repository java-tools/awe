package com.almis.awe.controller;

import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.entities.screen.data.ScreenData;
import com.almis.awe.service.ScreenService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * Manage all incoming action requests
 */
@RestController
@RequestMapping("/screen-data")
public class ScreenDataController {

  // Autowired services
  private final ScreenService screenService;
  private final AweRequest request;

  /**
   * Autowired constructor
   *
   * @param screenService Screen service
   * @param aweRequest    Awe request
   */
  @Autowired
  public ScreenDataController(ScreenService screenService, AweRequest aweRequest) {
    this.screenService = screenService;
    this.request = aweRequest;
  }

  /**
   * Retrieve screen data
   *
   * @param token      Connection token
   * @param parameters Parameters
   * @return Client action list
   */
  @PostMapping
  public ScreenData getDefaultScreenData(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                         @RequestBody ObjectNode parameters) {

    // Initialize parameters
    request.init(parameters, token);

    // Launch action
    return screenService.getScreenData();
  }

  /**
   * Retrieve screen data
   *
   * @param token      Connection token
   * @param optionId   Option identifier
   * @param parameters Parameters
   * @return Client action list
   */
  @PostMapping("/{optionId}")
  public ScreenData getOptionScreenData(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                        @PathVariable("optionId") String optionId,
                                        @RequestBody ObjectNode parameters) {

    // Initialize parameters
    request.init(parameters, token);

    // Launch action
    return screenService.getScreenData(optionId);
  }
}
