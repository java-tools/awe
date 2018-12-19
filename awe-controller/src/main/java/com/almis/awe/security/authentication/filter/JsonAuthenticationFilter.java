package com.almis.awe.security.authentication.filter;

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Json user and password parser
 * Created by dfuentes on 24/03/2017.
 */
public class JsonAuthenticationFilter implements Filter {

  private final String userParameter;
  private final String passwordParameter;
  private final String securityParameter;

  /**
   * Authentication filter
   *
   * @param userParameter User parameter
   * @param passwordParameter Password parameter
   * @param securityParameter Security parameter
   */
  public JsonAuthenticationFilter(String userParameter, String passwordParameter, String securityParameter) {
    this.userParameter = userParameter;
    this.passwordParameter = passwordParameter;
    this.securityParameter = securityParameter;
  }

  /**
   * Initialize current class
   *
   * @param filterConfig Filter configuration
   *
   * @throws ServletException Error initializing class
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // TODO: Añadir log
  }

  /**
   * Apply current filter
   *
   * @param request  Servlet request
   * @param response Servlet response
   * @param chain    Filter chain
   *
   * @throws IOException      Error accessing files
   * @throws ServletException Error with servlet management
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      JsonNode loginRequest = new ObjectMapper().reader().readTree(String.valueOf(request.getParameter(securityParameter)));
      final String user;
      final String password;
      if (loginRequest != NullNode.getInstance()) {
        user = loginRequest.get(userParameter).textValue();
        password = loginRequest.get(passwordParameter).textValue();
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {
          @Override
          public String getParameter(String name) {
            if (name.equals(userParameter)) {
              return user;
            }
            if (name.equals(passwordParameter)) {
              return password;
            }
            return super.getParameter(name);
          }
        };
        chain.doFilter(requestWrapper, response);
      } else {
        throw new AWException("Not valid user/password");
      }
    } catch (Exception e) {
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
    // TODO: Añadir log
  }

  /**
   * Get user parameter
   *
   * @return User parameter
   */
  public String getUserParameter() {
    return userParameter;
  }

  /**
   * Get password parameter
   *
   * @return Password parameter
   */
  public String getPasswordParameter() {
    return passwordParameter;
  }
}
