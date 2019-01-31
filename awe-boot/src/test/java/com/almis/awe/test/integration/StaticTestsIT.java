package com.almis.awe.test.integration;


import org.junit.Test;
import org.openqa.selenium.By;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class StaticTestsIT extends SeleniumTestsUtil {

  @Test
  public void loginTest() throws Exception {
    doLogin();
  }

  @Test
  public void newSite() throws Exception {
    // Wait for text in selector
    waitUntil(presenceOfElementLocated(By.name("tools")));

    // Click on screen
    click(By.name("tools"));

    // Wait for text in selector
    waitUntil(visibilityOfElementLocated(By.name("sites")));

    // Click on screen
    click(By.name("sites"));

    // Wait for element not visible
    waitUntil(invisibilityOfElementLocated(By.id("loading-bar")));
  }
}
