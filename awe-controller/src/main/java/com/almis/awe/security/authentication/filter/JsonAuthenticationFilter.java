package com.almis.awe.security.authentication.filter;

import com.almis.awe.component.AweHttpServletRequestWrapper;
import com.almis.awe.model.component.AweElements;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Json user and password parser
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AweElements elements;

  /**
   * Autowired constructor
   * @param aweElements AweElements
   */
  public JsonAuthenticationFilter(AweElements aweElements) {
    this.elements = aweElements;
  }


  @Override
  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    if (!requiresAuthentication((HttpServletRequest) req, (HttpServletResponse) res)) {
      super.doFilter(req, res, chain);
    } else {
      super.doFilter(new AweHttpServletRequestWrapper((HttpServletRequest) req), res, chain);
    }
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    return this.getAuthenticationManager().authenticate(getAuthRequest(request));
  }

  /**
   * Retrieve authorization request parameters
   * @param request Request
   * @return Authentication Token
   */
  private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
    // Read from request
    String body = ((AweHttpServletRequestWrapper) request).getBody();
    try {
      // Read the parameters
      ObjectNode parameters = (ObjectNode) new ObjectMapper().readTree(body);

      // Fill username and password authentication token with the right values
      return new UsernamePasswordAuthenticationToken(
        parameters.get(getUsernameParameter()).asText(),
        parameters.get(getPasswordParameter()).asText());
    } catch (IOException exc) {
      throw new InternalAuthenticationServiceException(elements.getLocale("ERROR_MESSAGE_INVALID_ARGUMENTS"));
    }
  }
}
