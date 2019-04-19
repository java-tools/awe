package com.almis.awe.security.accessbean;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * Login access control bean
 * Created by dfuentes on 11/04/2017.
 */
public class LoginAccessControl implements AccessBean {

    @Override
    public boolean checkAccess(Authentication authentication, HttpServletRequest request){
        return authentication.isAuthenticated();
    }
}
