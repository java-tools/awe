package com.almis.awe.security.accessbean;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * Access bean must implement this class to allow the correct execution of the access call from spring security configuration
 * Created by dfuentes on 11/04/2017.
 */
public interface AccessBean {

    /**
     * Method to check if current user is allowed to access some resource/action
     *
     * @param authentication Authentication
     * @param request Request
     * @return Access granted
     */
    boolean checkAccess(Authentication authentication, HttpServletRequest request);
}
