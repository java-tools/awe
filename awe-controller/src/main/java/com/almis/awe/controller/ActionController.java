package com.almis.awe.controller;

import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.service.ActionService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.almis.awe.model.constant.AweConstants.SESSION_CONNECTION_HEADER;

/**
 * Manage all incoming action requests
 */
@RestController
@RequestMapping("/action")
public class ActionController {

  // Autowired services
  private ActionService actionService;
  private AweRequest request;

  /**
   * Autowired constructor
   * @param actionService
   * @param aweRequest
   */
  @Autowired
  public ActionController(ActionService actionService, AweRequest aweRequest) {
    this.actionService = actionService;
    this.request = aweRequest;
  }

  /**
   * Launch server action
   * @param token Connection token
   * @param actionId Action identifier
   * @param parameters Parameters
   * @return Client action list
   */
  @PostMapping("/{actionId}")
  public List<ClientAction> launchAction(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                         @PathVariable("actionId") String actionId,
                                         @RequestBody ObjectNode parameters) {

    // Initialize parameters
    request.init(parameters, token);

    // Launch action
    return actionService.launchAction(actionId);
  }

  /**
   * Launch server action with target
   * @param token Connection token
   * @param actionId Action identifier
   * @param targetId Target action
   * @param parameters Parameters
   * @return Client action list
   */
  @PostMapping("/{actionId}/{targetId}")
  public List<ClientAction> launchAction(@RequestHeader(SESSION_CONNECTION_HEADER) String token,
                                         @PathVariable("actionId") String actionId,
                                         @PathVariable("targetId") String targetId,
                                         @RequestBody ObjectNode parameters) {

    // Initialize parameters
    request.init(targetId, parameters, token);

    // Launch action
    return actionService.launchAction(actionId);
  }
}
