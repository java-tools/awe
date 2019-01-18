package com.almis.awe.test.controller;

import com.almis.awe.model.component.AweSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dfuentes on 29/05/2017.
 */
@Controller
@RequestMapping ("/session")
public class SessionController {

  @Autowired
  private AweSession session;

  /**
   * Set session parameter
   * @param name Key
   * @param value Value
   * @return set
   */
  @GetMapping("/set/{name}/{value}")
  @ResponseBody
  public String setParameter(@PathVariable("name") String name, @PathVariable("value") String value) {

    // Initialize parameters
    session.setParameter(name, value);

    // Launch action
    return name + " = " + value;
  }

  /**
   * Get session parameter
   * @param name Parameter
   * @return Value
   */
  @GetMapping("/get/{name}")
  @ResponseBody
  public String getParameter(@PathVariable("name") String name) {

    // Launch action
    return (String) session.getParameter(name);
  }

  /**
   * Get session parameter
   * @param name Parameter
   * @return Value
   */
  @GetMapping("/remove/{name}")
  @ResponseBody
  public String removeParameter(@PathVariable("name") String name) {

    // Remove parameter
    session.removeParameter(name);

    // Launch action
    return name + " removed";
  }
}
