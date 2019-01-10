package com.almis.awe.test.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by pgarcia on 18/05/2018.
 */
@Controller
@RequestMapping ("/testapi")
public class TestRestController {

  @Autowired
  private QueryService queryService;

  @Autowired
  private MaintainService maintainService;

  @Autowired
  private AweRequest aweRequest;

  /**
   * Test get send
   * @return Empty service data
   */
  @GetMapping("/simple")
  @ResponseBody
  public ServiceData testGet() {
    return new ServiceData();
  }

  /**
   * Test post send
   * @return Empty service data
   */
  @PostMapping("/simple")
  @ResponseBody
  public ServiceData testPost() {
    return new ServiceData();
  }

  /**
   * Test get send
   * @return Empty service data
   */
  @GetMapping("/complex/{name}")
  @ResponseBody
  public ServiceData testGetQuery(@PathVariable String name) throws AWException {
    return queryService.launchPrivateQuery(name);
  }

  /**
   * Test post send
   * @return Empty service data
   */
  @PostMapping("/complex/{name}")
  @ResponseBody
  public ServiceData testLaunchMaintain(@PathVariable String name) throws AWException {
    return maintainService.launchPrivateMaintain(name);
  }

  /**
   * Test get send
   * @return Empty service data
   */
  @GetMapping("/complex/{name}/{value}")
  @ResponseBody
  public ServiceData testGetQueryParameters(@PathVariable String name, @PathVariable Integer value, HttpServletRequest request) throws AWException {
    // Initialize parameters
    aweRequest.init(request);
    aweRequest.setParameter("value", JsonNodeFactory.instance.numberNode(value));

    return queryService.launchPrivateQuery(name);
  }

  /**
   * Test post send
   * @return Empty service data
   */
  @PostMapping(value = "/complex/parameters/{name}")
  @ResponseBody
  public ServiceData testLaunchMaintainParameters(@PathVariable String name, @RequestParam(name = "value", required = true) Integer value, HttpServletRequest request) throws AWException {
    // Initialize parameters
    aweRequest.init(request);
    aweRequest.setParameter("value", JsonNodeFactory.instance.numberNode(value));

    return maintainService.launchPrivateMaintain(name);
  }

  /**
   * Test post send json
   * @return Empty service data
   */
  @PostMapping(value = "/complex/parameters/json/{name}")
  @ResponseBody
  public ServiceData testLaunchMaintainParametersJson(@PathVariable String name, @RequestBody ObjectNode result, HttpServletRequest request) throws AWException {
    // Initialize parameters
    aweRequest.init(request);
    aweRequest.setParameter("value", result.get("value"));

    return maintainService.launchPrivateMaintain(name);
  }
}
