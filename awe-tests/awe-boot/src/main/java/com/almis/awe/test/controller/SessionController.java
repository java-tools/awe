package com.almis.awe.test.controller;

import com.almis.awe.model.component.AweSession;
import com.almis.awe.session.AweSessionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

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

  @Autowired
  private SessionRegistry sessionRegistry;

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
  public String invalidate() {

    // Remove parameter
    List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
    for (Object principal : allPrincipals) {
      List<SessionInformation> sessionList = sessionRegistry.getAllSessions(principal, false);
      for (SessionInformation information : sessionList) {
        information.expireNow();
      }
    }

    // Return string
    return "session invalidated";
  }
}
