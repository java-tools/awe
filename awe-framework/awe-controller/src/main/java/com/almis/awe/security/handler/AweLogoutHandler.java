package com.almis.awe.security.handler;

import com.almis.awe.model.component.AweRequest;
import com.almis.awe.session.AweSessionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logout access control bean
 * Created by pgarcia on 13/06/2019.
 */
@Log4j2
public class AweLogoutHandler extends SecurityContextLogoutHandler {

  private AweSessionDetails sessionDetails;
  private AweRequest request;

  /**
   * Constructor
   *
   * @param sessionDetails session details
   */
  public AweLogoutHandler(AweSessionDetails sessionDetails, AweRequest request) {
    this.sessionDetails = sessionDetails;
    this.request = request;
  }

  @Override
  public void logout(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) {
    if (!response.isCommitted()) {
      request.init(httpServletRequest);
      sessionDetails.onLogoutSuccess();
      setClearAuthentication(true);
      setInvalidateHttpSession(true);
      try {
        httpServletRequest.getRequestDispatcher("/action/logoutRedirect").forward(httpServletRequest, response);
      } catch (Exception exc) {
        log.error("Error redirecting logout handler", exc);
      }
    }
  }
}
