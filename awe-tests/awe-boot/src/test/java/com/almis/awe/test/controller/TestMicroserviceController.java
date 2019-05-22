package com.almis.awe.test.controller;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


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
    return new ServiceData();
  }

  /**
   * Test post parameter list
   * @return Empty service data
   */
  @PostMapping(value = "/alu-microservice2/invoke/{lala}")
  @ResponseBody
  public ServiceData testPostParameterListAnotherMicroservice(@PathVariable(value = "lala", required = true) String lala, @RequestBody ObjectNode jsonData, HttpServletRequest request) throws AWException {
    Map<String, CellData> row = new HashMap<>();
    row.put("text", new CellData("test"));
    row.put("date", new CellData(new GregorianCalendar(1978, Calendar.OCTOBER, 23).getTime()));
    row.put("integer", new CellData(22));
    row.put("long", new CellData(22L));
    row.put("double", new CellData(22D));
    row.put("float", new CellData(22F));
    row.put("null", new CellData());

    DataList dataList = new DataList();
    dataList.addRow(row);

    // Initialize parameters
    return new ServiceData()
      .setDataList(dataList);
  }

  /**
   * Test post parameter list
   * @return Empty service data
   */
  @PostMapping(value = "/alu-microservice3/invoke")
  @ResponseBody
  public ServiceData testPostMicroservice(HttpServletRequest request) throws AWException {
    // Initialize parameters
    return new ServiceData();
  }
}
