package com.almis.awe.security.handler;

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

  /**
   * Constructor
   *
   * @param sessionDetails
   */
  public AweLogoutHandler(AweSessionDetails sessionDetails) {
    this.sessionDetails = sessionDetails;
  }

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    if (!response.isCommitted()) {
      sessionDetails.onLogoutSuccess();
      setClearAuthentication(true);
      setInvalidateHttpSession(true);
      try {
        request.getRequestDispatcher("/action/logoutRedirect").forward(request, response);
      } catch (Exception exc) {
        log.error("Error redirecting logout handler", exc);
      }
    }
  }
}
