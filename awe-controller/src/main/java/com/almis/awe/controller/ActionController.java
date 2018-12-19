package com.almis.awe.controller;

import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Manage all incoming action requests
 */
@RestController
@RequestMapping("/action")
public class ActionController {

  // Autowired services
  private ActionService actionService;
  private AweRequest aweRequest;

  /**
   * Autowired constructor
   * @param actionService
   * @param aweRequest
   */
  @Autowired
  public ActionController(ActionService actionService, AweRequest aweRequest) {
    this.actionService = actionService;
    this.aweRequest = aweRequest;
  }

  /**
   * Launch server action
   *
   * @param actionId Action identifier
   * @param request Request
   * @return Client action list
   */
  @PostMapping("/{actionId}")
  public List<ClientAction> launchAction(@PathVariable("actionId") String actionId, HttpServletRequest request) {

    // Initialize parameters
    aweRequest.init(request);

    // Launch action
    return actionService.launchAction(actionId);
  }

  /**
   * Launch server action with target
   *
   * @param actionId Action identifier
   * @param targetId Target action
   * @param request Request
   * @return Client action list
   */
  @PostMapping("/{actionId}/{targetId}")
  public List<ClientAction> launchAction( @PathVariable("actionId") String actionId, @PathVariable("targetId") String targetId,
          HttpServletRequest request) {

    // Initialize parameters
    aweRequest.init(targetId, request);

    // Launch action
    return actionService.launchAction(actionId);
  }
}
