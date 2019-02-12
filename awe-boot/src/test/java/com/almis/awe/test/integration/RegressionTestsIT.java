package com.almis.awe.test.integration;


import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTestsIT extends SeleniumTestsUtil {

  @Test
  public void t000_loginTest() throws Exception {
    doLogin();
  }

  @Test
  public void t999_logoutTest() throws Exception {
    doLogout();
  }

  @Test
  public void t001_selectTestModule() throws Exception {

  }
}
