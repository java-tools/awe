package com.almis.awe.test.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
import com.almis.awe.test.bean.Concert;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * Created by pgarcia on 18/05/2018.
 */
@Controller
@RequestMapping("/testapi")
public class TestRestController {

  @Autowired
  private QueryService queryService;

  @Autowired
  private MaintainService maintainService;

  @Autowired
  private AweRequest aweRequest;

  /**
   * Test get send
   *
   * @return Empty service data
   */
  @GetMapping("/simple")
  @ResponseBody
  public ServiceData testGet() {
    return new ServiceData();
  }

  /**
   * Test post send
   *
   * @return Empty service data
   */
  @PostMapping("/simple")
  @ResponseBody
  public ServiceData testPost() {
    return new ServiceData();
  }

  /**
   * Test get send
   *
   * @return Empty service data
   */
  @GetMapping("/complex/{name}")
  @ResponseBody
  public ServiceData testGetQuery(@PathVariable String name) throws AWException {
    return queryService.launchPrivateQuery(name);
  }

  /**
   * Test post send
   *
   * @return Empty service data
   */
  @PostMapping("/complex/{name}")
  @ResponseBody
  public ServiceData testLaunchMaintain(@PathVariable String name) throws AWException {
    return maintainService.launchPrivateMaintain(name);
  }

  /**
   * Test get send
   *
   * @return Empty service data
   */
  @GetMapping("/complex/{name}/{value}")
  @ResponseBody
  public ServiceData testGetQueryParameters(@PathVariable String name, @PathVariable Integer value) throws AWException {
    // Initialize parameters
    aweRequest.init(JsonNodeFactory.instance.objectNode(), "sdsf");
    aweRequest.setParameter("value", JsonNodeFactory.instance.numberNode(value));

    return queryService.launchPrivateQuery(name);
  }

  /**
   * Test post send
   *
   * @return Empty service data
   */
  @PostMapping(value = "/complex/parameters/{name}")
  @ResponseBody
  public ServiceData testLaunchMaintainParameters(@PathVariable String name, @RequestParam(name = "value", required = true) Integer value) throws AWException {
    // Initialize parameters
    aweRequest.init(JsonNodeFactory.instance.objectNode(), "asada");
    aweRequest.setParameter("value", JsonNodeFactory.instance.numberNode(value));

    return maintainService.launchPrivateMaintain(name);
  }

  /**
   * Test post parameter list
   *
   * @return Empty service data
   */
  @PostMapping(value = "/complex/parameterList")
  @ResponseBody
  public ServiceData testPostParameterList(@RequestParam(name = "integerList", required = true) List<Integer> integerList,
                                           @RequestParam(name = "stringList", required = true) List<String> stringList,
                                           @RequestParam(name = "dateList", required = true) @DateTimeFormat(pattern = "dd/MM/yyyy") List<Date> dateList) throws AWException {
    // Initialize parameters
    return new ServiceData();
  }

  /**
   * Test post parameter list
   *
   * @return Empty service data
   */
  @PostMapping(value = "/complex/parameterListGetParameters")
  @ResponseBody
  public ServiceData testPostParameterListGetParametersFromRequest(HttpServletRequest request) throws AWException {
    // Initialize parameters
    return new ServiceData();
  }

  /**
   * Test post parameter list
   *
   * @return Empty service data
   */
  @PostMapping(value = "/complex/parameterListJson")
  @ResponseBody
  public ServiceData testPostParameterListJson(@RequestBody ObjectNode result) throws AWException {
    // Initialize parameters
    aweRequest.init(JsonNodeFactory.instance.objectNode(), "asasd");
    aweRequest.setParameterList(result);
    return new ServiceData();
  }

  /**
   * Test post parameter list
   *
   * @return Empty service data
   */
  @PutMapping(value = "/complex/pojoJSON/{tutu}")
  @ResponseBody
  public ServiceData testPostParameterListPOJO(@PathVariable("tutu") String tutu, @RequestBody Concert concert) throws AWException {
    // Initialize parameters
    aweRequest.init(JsonNodeFactory.instance.objectNode(), "asasd");

    return new ServiceData();
  }

  /**
   * Test post send json
   *
   * @return Empty service data
   */
  @PostMapping(value = "/complex/parameters/json/{name}")
  @ResponseBody
  public ServiceData testLaunchMaintainParametersJson(@PathVariable String name,
                                                      @RequestBody ObjectNode result) throws AWException {
    // Initialize parameters
    aweRequest.init(JsonNodeFactory.instance.objectNode(), "asasdas");
    aweRequest.setParameter("value", result.get("value"));

    return maintainService.launchPrivateMaintain(name);
  }
}
