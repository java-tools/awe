package com.almis.awe.test.controller;

import com.almis.awe.model.component.AweSession;
import com.almis.awe.session.AweSessionDetails;
import com.almis.awe.test.listener.TestSessionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dfuentes on 29/05/2017.
 */
@Controller
@RequestMapping ("/session")
@Profile({"dev", "gitlab-ci"})
public class SessionController {

  @Autowired
  private AweSession session;

  @Autowired
  private AweSessionDetails aweSessionDetails;

  @Autowired
  private HttpSession httpSession;

  /**
   * Set session parameter
   * @param name Key
   * @param value Value
   * @return set
   */
  @PostMapping("/set/{name}")
  @ResponseBody
  public String setParameter(@PathVariable("name") String name, @RequestParam("value") String value) {

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

  /**
   * Invalidate session
   * @return Value
   */
  @GetMapping("/invalidate")
  @ResponseBody
  public String invalidate(HttpServletRequest request) {
    TestSessionListener.getAllSessions().values().forEach(HttpSession::invalidate);

    // Return string
    return "session invalidated";
  }
}
