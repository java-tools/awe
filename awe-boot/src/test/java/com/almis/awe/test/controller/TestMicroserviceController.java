package com.almis.awe.test.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.MaintainService;
import com.almis.awe.service.QueryService;
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
public class TestMicroserviceController {

  @Autowired
  private AweRequest aweRequest;

  /**
   * Test post parameter list
   * @return Empty service data
   */
  @PostMapping(value = "/alu-microservice/data/aluWbsCorGetKey")
  @ResponseBody
  public ServiceData testPostParameterList(@RequestParam(name = "database", required = true) String database,
                                           @RequestParam(name = "username", required = true) String user,
                                           @RequestParam(name = "AppDat", required = true) @DateTimeFormat(pattern="dd/MM/yyyy") Date date,
                                           @RequestParam(name = "numPar", required = true) Integer parameterNumber,
                                           HttpServletRequest request) throws AWException {
    // Initialize parameters
    aweRequest.init(request);
    return new ServiceData();
  }

  /**
   * Test post parameter list
   * @return Empty service data
   */
  @PostMapping(value = "/alu-microservice2/invoke/{lala}")
  @ResponseBody
  public ServiceData testPostParameterListAnotherMicroservice(@PathVariable(value = "lala", required = true) String lala, @RequestBody ObjectNode jsonData, HttpServletRequest request) throws AWException {
    // Initialize parameters
    aweRequest.init(request);
    return new ServiceData();
  }

  /**
   * Test post parameter list
   * @return Empty service data
   */
  @PostMapping(value = "/alu-microservice3/invoke")
  @ResponseBody
  public ServiceData testPostMicroservice(HttpServletRequest request) throws AWException {
    // Initialize parameters
    aweRequest.init(request);
    return new ServiceData();
  }
}
